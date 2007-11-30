package com.exsys.fix.session;

import java.io.*;
import java.util.*;

import com.exsys.common.exceptions.*;
import com.exsys.common.tcpsession.*;
import com.exsys.common.util.*;
import com.exsys.fix.message.*;
import com.exsys.service.ConfigConstants;
import com.exsys.service.ConfigurationService;
import com.exsys.service.Logger;
import com.exsys.common.trading.*;

/**
 * @author kreddy
 *
 *Main class that implements FIX communication
 *and FIX Session layer.
 *This also has methods to support both fix client
 *and fix server
 */
public class FixSession extends TcpSession 
{
	//private FixSessionContext context = null;
	private boolean initialized = false;
	private FixSessionManager sessionMgr = null;
	private int inSeq = 0;
	private int outSeq = 0;
	private FixSessionHeartbeat heartBeatThread = null;
	// queue and thread to handle outbound messages
	private com.exsys.common.util.Queue outQueue = null;
	private DequeueThread outThread;
	
	// queue and thread to handle ignored messages
	private com.exsys.common.util.Queue ignoreQueue = null;
	private DequeueThread ignoreThread;


	public boolean resendRequestFlag = false;
	private TradeMessageProcessor mProcessor = null;
	private SecuritiesMessageProcessor mSecProcessor = null;	
	private MarketDataMessageProcessor mMDProcessor = null;		
	private NewsMessageProcessor mNewsProcessor = null;			
	private boolean bowLogon = false;
	private static String fixVersion = null;
	private String exchange = null;
	private int heartBeatInSeconds = 30;
	private Object inSequenceLock = new Object();
	private Object outSequenceLock = new Object();
	
	
	// the following are added to handle resendRequests
	// sent when incoming seq number gaps are noticed
	// Also, we only send a new message the first time
	// subsequently, send posDup, on the same seq number
	// as the original one.
	private int lastResendExpSeqNum = 0;
	private int lastResendSeqNum = 0;
	
	// need to store resendReqSeqNum recieved so that if we receive
	// duplicates for the same,we can ignore it
	private int resendReqSeqNum = 0;
	
	private ArrayList sessionRejectList = new ArrayList(10);
	private Object sendMessageLock = new Object();
	
	
	// added to support fault tolerant session
	private boolean primary = true;
	private String logFileDirectory = null;
	
	private boolean playbackMode  = false;
	
	private class OutQueueAdapter implements DequeueAdapter
	{
		public void dequeuedMessage( Object msg )
		{
			// write this message
			writeMessage( (String)msg );
		}
		
	}
	
	private class IgnoreQueueAdapter implements DequeueAdapter
	{
		public void dequeuedMessage( Object msg )
		{
			// write this message into ignore msg log file
			sessionMgr.logIgnoreFixMessage( ((FixMessage)msg).getMsgSeqNum(), 
						(((FixMessage)msg).toString()).getBytes());
					
		}
		
	}

/**
 * FixSession constructor
 * @param playback 
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public FixSession(boolean playback)
		throws ConfigAttributeNotFound, 
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	playbackMode = true;
	//initializeConfig();
}
/**
 * FixSession constructor
 * @param host
 * @param port
 * @throws ConfigAttributeNotFound
 * @throws SystemException
 */
public FixSession(String host, int port) 
		throws ConfigAttributeNotFound,
			   SystemException
{
	super( host, port );
	//initialized = initializeFixSession();
	initializeCommon();
      
}

/**
 * FixSession constructor
 * @param _socket
 * @throws ConfigAttributeNotFound
 * @throws SystemException
 */
public FixSession(java.net.Socket _socket)
			throws ConfigAttributeNotFound,
				   SystemException
{
	super(_socket);
	// once the socket initialization is successful
	// initialize fix session
	//initialized = initializeFixSession();
	//if( initialized )
	//{
	//	initializeSessionManager();
	//}
	initializeCommon();
}

/**
 * isAdminMessage - tests to see if a message is an admin msg
 * @param msgType
 * @return true or false
 */
public boolean isAdminMessage(String msgType) 
{
	if( (msgType.equals(FixConstants.FIX_MSGTYPE_LOGON)) 
		|| ( msgType.equals(FixConstants.FIX_MSGTYPE_HEARTBEAT) )
		|| ( msgType.equals(FixConstants.FIX_MSGTYPE_TESTREQUEST) ) 
		|| ( msgType.equals(FixConstants.FIX_MSGTYPE_RESENDREQUEST) )
		|| ( msgType.equals(FixConstants.FIX_MSGTYPE_SEQUENCERESET) ) 
		|| ( msgType.equals(FixConstants.FIX_MSGTYPE_LOGOUT) ) 
		|| ( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST) ) )
	{
		return true;	
	}
	
	return false;
}

// THIS METHOD IS OVERRIDEN BY THE CLIENT
/**
 * method to see if a particular message is old
 * @param msgType
 * @param msg
 * @return
 */
public boolean isAgedMessage(String msgType,FixMessage msg)
{
	return false;
}

  /* (non-Javadoc)
 * @see com.exsys.common.util.DequeueAdapter#dequeuedMessage(java.lang.Object)
 */
// main method that implements FIX Session layer
// and handles all the incoming fix messages that are
// received over the socket
public void dequeuedMessage( Object msg )
  {

		StringBuffer message = (StringBuffer)msg;
		//Logger.debug("FixSession - dequeuedMessage - Message Received is "+ message );
		FixMessage fixMsg = null;
		try
		{
			fixMsg = FixMessageFactory.createFixMessage(
					message.toString().getBytes(),exchange,
					isServerSession());
		}
		catch(FixProtocolError e)
		{
			String fmsg = "FixSession - dequeuedMessage - Fix Protocol Error - " +e.getMessage();
			Logger.error("Fix Msg Received - " + message);
			Logger.error(fmsg);
			Logger.error(e.getExternalMessage());
			error(fmsg + "\n" + e.getExternalMessage()+"\n"+message);
			String refSeqNum = FixMessageFactory.getSeqNum(message.toString().getBytes());			
			if(isServerSession())
			{
				if(initialized)
				{
					if(e.getErrorCode().equals("0"))
					{
						ignoreInboundMsg(fixMsg);
						warn("Ignored - Invalid first 3 fields");
						return;
					}
					// need to throw session reject
					//try to get sequence number from the message
					// if not set to zero										
					incrementInSequenceNumber();
					if(e.getErrorCode().equals("11"))
					{
						sendSessionReject(fmsg, refSeqNum, e.getErrorCode());
					}
					else if(e.getErrorCode().equals("99"))
					{
						sendBusinessReject(fmsg, refSeqNum,"3");
					}
					else
					{
						sendLogout(fmsg,getInSequenceNumber());
					}
					// this will result in shutdown
				}
				else
				{
					Logger.error("INVALID FIX MESSAGE WHILE EXPECTING LOGON");
					error("INVALID MESSAGE. EXPECTING LOGON");
					shutdownSession();										
				}
			}
			else
			{
					if(e.getErrorCode().equals("0"))
					{
						ignoreInboundMsg(fixMsg);
						warn("Ignored - Invalid first 3 fields");
						return;
					}
				
				incrementInSequenceNumber();
				if(e.getErrorCode().equals("11"))
				{
					sendSessionReject(fmsg, refSeqNum, e.getErrorCode());
				}
				else if(e.getErrorCode().equals("99"))
				{
						sendBusinessReject(fmsg, refSeqNum,"3");
				}
				else
				{
					sendLogout(e.getMessage(),getInSequenceNumber());				
				}
				error("Received an invalid FIX message from server");
			}
			return;
		}
		
		String msgType = fixMsg.getMessageType();

		if( !initialized && isServerSession() )
		{
			// This means we did not restore sequence numbers
			// This must be logon message else it is an error
			System.out.println(fixMsg.getMessageType());
			if( msgType.equals(FixConstants.FIX_MSGTYPE_LOGON) )
			{
				String senderCompID = fixMsg.getSenderCompID();
				//FixSessionContext context = new FixSessionContext( senderCompID, "Fix4.2",30);				
				//setSessionContext( context);
				
				initialized = true;
				// initialize SessionManager only if primary
				if(isPrimary())
				{
					initializeSessionManager(senderCompID);
				}
					
			}
			else
			{
				// throw an exception
				Logger.error("INVALID MESSAGE. EXPECTING LOGON");
				error("INVALID MESSAGE. EXPECTING LOGON");
				shutdownSession();
			}
		}

		if(!isPrimary())
		{
			// in case of client, we need to see if the FTI indicator 
			// in target compid is set to P
			String tcID = fixMsg.getTargetCompID();
			if(!isServerSession() && 
					tcID.substring(tcID.length()-1).equals("P"))
			{
				switchToPrimary();
			}
			else
			{
				// if in backup mode no need to check seq nums
				processIncomingFixMessage(fixMsg);
				return;
			}
		}

		int recSeqNum = fixMsg.getMsgSeqNum();
		int expSeqNum = getInSequenceNumber() + 1;
		
		// if body length is invalid, just ignore the message
		
		
		if(!(FixMessageValidation.checkBodyLength(fixMsg)))
		{
			ignoreInboundMsg(fixMsg);
			warn("Ignored a message with invalid body length");
			return;
		}
		
		
		// if checksum is invalid, just ignore the message
		if(!(FixMessageValidation.checkChecksum(fixMsg)))
		{
			ignoreInboundMsg(fixMsg);
			warn("Ignored a message with invalid checksum");
			return;
		}		
		// make sure that the message passes session level validations
		//CME did not send OriginalSendingTime in resendrequest
		// 04282007 - comment temporarily
		/*
		FixSessionReject reject = FixMessageValidation.validateFixMessage(
										message.toString().getBytes(),fixMsg);
		if(reject != null)
		{
			Logger.error("Invalid Fix Message - "+reject.getText());
			// send logout message
			incrementInSequenceNumber();
			sessionMgr.logInFixMessage( getInSequenceNumber(), 
						message.toString().getBytes());
			setHeaderFields(reject);			
			sendMessage(reject);
			//send logout message
			sendLogout(reject.getText(),getInSequenceNumber());
			//shutdownSession();
			return;								
		}
		*/
		// validate sender comp id
		if(!isServerSession())
		{
			if(!getTargetCompID().equals(fixMsg.getSenderCompID()))
			{
				incrementInSequenceNumber();
				sendSessionReject("Invalid SenderCompID", fixMsg.getMsgSeqNumAsString(), "9");
				sendLogout("Invalid SenderCompID -" + fixMsg.getSenderCompID(),getInSequenceNumber());
				return;
			}
		}
		
		
		// before processing seq numbers, check for sequence reset
		// if this message is SEQUENCE RESET - RESET
		// handle it first by resetting the seq numbers.
		if(msgType.equals(FixConstants.FIX_MSGTYPE_SEQUENCERESET))
		{
			// if not gapfill, then it is reset
			if(!((FixSequenceReset)fixMsg).getGapFillFalg())
			{
				System.out.println("SEQUENCE RESET - RESET RECEIVED");
				// log this message
				sessionMgr.logInFixMessage( recSeqNum, 
							message.toString().getBytes());
											
				int newSeq = ((FixSequenceReset)fixMsg).getNewSeqNo();
				if( newSeq >= expSeqNum )
				{
					setInSequenceNumber(newSeq - 1);
				}
				else
				{
					// SERIOUS ERROR - LOGOUT and DISCONNECT
					// send LOGOUT MSG with expected sequence number
					processLogoutMessage("Invalid Sequence Number in SequenceReset");	
					disconnect("SERIOUS ERROR - INVALID SEQUENCENUMBER");
					
				}
				return;
												
			}
		}		
				
		
		Logger.debug("**************  About to Check InSeq Number  ****************");
		Logger.debug("Comparing Seq# :" + expSeqNum + "==" + recSeqNum );
		
		if( expSeqNum == recSeqNum )
		{
			Logger.debug("**************  Correct Seq Received  ****************");
			// when to increment sequence number ?????
			// in the case of processing resend request
			// since we will be stopping the outbound thread
			// we will increment sequence numbers after stopping
			// the outbound thread
			if(msgType.equals(FixConstants.FIX_MSGTYPE_RESENDREQUEST))
			{
				int beginSeq = ((FixResendRequest)fixMsg).getBeginSeqNo();
				if(beginSeq != resendReqSeqNum)
				{
					resendReqSeqNum = beginSeq;
					// sequence number will be incremented
					// inside this method after stopping the outbound thread
					handleResendRequest((FixResendRequest)fixMsg,true);
					sessionMgr.logInFixMessage( getInSequenceNumber(), 
							message.toString().getBytes());
				}
				else
				{
					// ignore this message, as we have already received this one
					Logger.debug("Ignoring duplicate Resend Request");
					// IMPORTANT !!! we still need to increment sequence number here
					incrementInSequenceNumber();
					ignoreInboundMsg(fixMsg);
				}
				
			}
			else
			{				
				incrementInSequenceNumber();
				sessionMgr.logInFixMessage( getInSequenceNumber(), 
							message.toString().getBytes());
				processIncomingFixMessage(fixMsg);								
			}
			
		}
		else
		{
			Logger.debug("**************  Some messages are lost  ****************");
			Logger.debug("Message Type : " + fixMsg.getMessageType());
			// sequence numbers out of synch
			if( expSeqNum < recSeqNum )
			{
				// we might have missed messages
				handleMissedMessages(expSeqNum, recSeqNum,fixMsg );
				
			}
			else 
			{
				// SEQUENCE  NUMBER WILL NOT BE INCREMENTED
				if(!fixMsg.getPossDupFlag())
				{
					// This is a serious error if posDup = N
					Logger.error("SERIOUS ERROR");
					// will have to throw alerts and shutdown
					if(isServerSession())
					{
						// send LOGOUT MSG with expected sequence number
						processLogoutMessage("Expected Sequence Number is " + expSeqNum,expSeqNum);
						disconnect("SERIOUS ERROR - INVALID SEQUENCENUMBER");
					}
					else
					{
						// shutdown 
						//???? pass a parameter indicating the reason for shutdown
						// client need to send logout and disconnect immediately
						// without waiting for confirmation
						// disconnect ???
						//shutdownSession();
						sendLogout("Disconnecting",expSeqNum);
						disconnect("MaxResend size error");
					}
				}
				else
				{
					// ignore the msg if posDup = Y
					ignoreInboundMsg(fixMsg);
				}
				
				
			}
		}//expSeqNum <> recSeqNum
		
		


  }

/**
 * method used to reset sequence numbers
 */
private void sequenceReset()
{
	// this will close the current log file
	// and open a new one
	sessionMgr.resetSequenceNumbers();
	inSeq = sessionMgr.getInSeq();
	outSeq = sessionMgr.getOutSeq();
	// reset current state
	resendReqSeqNum = 0;
	lastResendExpSeqNum = 0;
	lastResendSeqNum = 0;
	sessionRejectList.clear();
	
}  
/**
 * method to handle resend request 
 * @param fixMsg
 * @param normalMode
 */
private void handleResendRequest(FixResendRequest fixMsg, boolean normalMode )
{
    // stop the send of new messages
    // get resend messages from the file
    // send the resend messages
    ArrayList resendList = null;
    int beginSeq = fixMsg.getBeginSeqNo();
    int endSeq = fixMsg.getEndSeqNo();
    
    Logger.debug("In handleResendRequest-"+beginSeq+"-"+endSeq);
     
    synchronized( sendMessageLock )
    {
    	//Logger.debug("obtained sendmessage lock");
    	outQueue.setOnHold();
    	//Logger.debug("set outQueue on hold");
    	// only increment in seq number in normal mode
    	// other wise we will never be get in synch
    	if(normalMode)
    	{
    		incrementInSequenceNumber();
    	}
    	//Logger.debug("incremented in seq num");
    	resendList = sessionMgr.getResendMessages(beginSeq,endSeq);
    	//Logger.debug("Retrieved resend messages list");    	
    }
    if( resendList.size() == 0 )
    {
    	// THIS IS A SERIOUS ERROR
    	// WE DID NOT FIND ANY MESSAGES
    	// we may have to send a RESET MESSAGE
    	// IMPLEMENT LATER
    	Logger.debug("Sending a Sequence Reset");    	    	
    	FixSequenceReset seqReset = new FixSequenceReset();
    	setHeaderFields(seqReset);
    	seqReset.setNewSeqNo(getOutSequenceNumber()+1);
    	seqReset.setGapFillFalg(false);
    	seqReset.setPossDupFlag(true);
    	seqReset.setOrigSendingTime(seqReset.getSendingTime());
    	sendResendMessage(seqReset,getOutSequenceNumber());
    	outQueue.removeHold();
    	return;
    	
    	//shutdownSession();
    }

    Logger.debug("Starting to resend messages - "+resendList.size());    	    	    
    int beginGapFill = 0;
    int endGapFill = 0;
    int curSeqNum = 0;
    for(int i=0; i<resendList.size();i++)
    {
    	FixMessage msg = (FixMessage)resendList.get(i);
    	String msgType = msg.getMessageType();
    	curSeqNum = msg.getMsgSeqNum();
    	if(beginGapFill == 0)
    	{
    		beginGapFill = curSeqNum;
    	}
    	String seqNumAsString = msg.getMsgSeqNumAsString();
    	if(isAdminMessage(msgType)
    		||isAgedMessage(msgType,msg)
    		||sessionRejectList.contains(seqNumAsString))
    	{
    		//gap fill this message
    		endGapFill = msg.getMsgSeqNum();
    		Logger.debug("Skipping this message - " + endGapFill);    	    	
    	}
    	else
    	{
    		// send pending gap fill
    		if(endGapFill != 0 )
    		{
    			Logger.debug("Sending gapfill message - " + endGapFill);
    			FixSequenceReset gapFill = new FixSequenceReset();
    			setHeaderFields(gapFill);
    			gapFill.setNewSeqNo(endGapFill+1);
    			gapFill.setGapFillFalg(true);
    			gapFill.setPossDupFlag(true);
    			gapFill.setOrigSendingTime(gapFill.getSendingTime());
    			sendResendMessage(gapFill,beginGapFill);
    		}
    		Logger.debug("Sending rensed message - " + curSeqNum);
    		// send this message msg
    		Date sendingTime = msg.getSendingTime();
    		msg.setOrigSendingTime(sendingTime);
    		msg.setSendingTime(FixMessage.getUTCCurrentTime());
    		msg.setPossDupFlag(true);
    		sendResendMessage(msg,curSeqNum);
    		    		
    		// reset
    		endGapFill = 0;
    		beginGapFill = 0;
    		
    		
    	}
    	
    	
    }
    // check if any pending gap fill
    if(endGapFill != 0 )
    {
    	Logger.debug("No application messages to send");
    	Logger.debug("Sending gapfill message - " + endGapFill);     	
    	FixSequenceReset gapFill = new FixSequenceReset();
    	setHeaderFields(gapFill);
    	gapFill.setNewSeqNo(endGapFill+1);
    	gapFill.setGapFillFalg(true);
  		gapFill.setPossDupFlag(true);
    	gapFill.setOrigSendingTime(gapFill.getSendingTime());
    	sendResendMessage(gapFill,beginGapFill);
     		
     	//reset
    	endGapFill = 0;
    	beginGapFill = 0;
    }

    
    outQueue.removeHold();
    	
}

/**
 * method used to send a sequence reset message
 * @param newSeq
 */
public void sendSequenceReset(int newSeq)
{
    	FixSequenceReset seqReset = new FixSequenceReset();
    	setHeaderFields(seqReset);
    	seqReset.setNewSeqNo(newSeq);
    	seqReset.setGapFillFalg(false);
    	seqReset.setOrigSendingTime(seqReset.getSendingTime());
    	sendMessage(seqReset);
    	// after sending seq reset message, set out seq num 
    	// cme testing 04282007
    	setOutSequenceNumber(newSeq-1);
	
}  
/**
 * method to handle scenario where messages are missed
 * @param expSeqNum
 * @param recSeqNum
 * @param fixMsg
 */
private void handleMissedMessages(int expSeqNum, int recSeqNum, FixMessage fixMsg )
{
	String msgType = fixMsg.getMessageType();
	boolean logoutMsg = false;
	if( msgType.equals(FixConstants.FIX_MSGTYPE_LOGON))
	{
		if( isServerSession() )
		{
			boolean logonSuccess = processLogonMessage(fixMsg,false);
			if(!logonSuccess)
			{
				// we would have already sent a LOGOUT
				Logger.error("SERIOUS ERROR");
				shutdownServerSession();
			}
		}
		else
		{
			// if this is client receiving logon confirm
			// HANDLE BOW UNDELIVERED MESSAGES
			// HANDLE MOW UNDELIVERED MESSAGES
			// WHEN TO CALL LOGONSUCCESSFUL method
			Logger.debug("LOGON CONFIRM RECEIVED FROM SERVER");
			Logger.debug("THERE WERE UNDELIVERED MESSAGES");
			//logonSuccessful();
			// 04272007 - need to call processLogonMessage
			// so that FTI indicator gets set properly
			processLogonMessage(fixMsg,false);
		}
	}
	else if( msgType.equals(FixConstants.FIX_MSGTYPE_RESENDREQUEST))
	{
		int beginSeq = ((FixResendRequest)fixMsg).getBeginSeqNo();
		if(beginSeq != resendReqSeqNum)
		{
			resendReqSeqNum = beginSeq;
			// other side sent a resend request, we need to handle this one first
			handleResendRequest((FixResendRequest)fixMsg,false);
		}
		else
		{
			// ignore this message, as we have already received this one
			Logger.debug("Ignoring duplicate Resend Request - Missed");
			ignoreInboundMsg(fixMsg);
		}
		
		
	}
	else if( msgType.equals(FixConstants.FIX_MSGTYPE_LOGOUT))
	{
		logoutMsg = true;
		//??? should this be ignored or not
		ignoreInboundMsg(fixMsg);
	}
	else if( msgType.equals(FixConstants.FIX_MSGTYPE_REJECT))
	{

		processSessionRejectMessage((FixSessionReject)fixMsg);
	}	
	else
	{
		// ignore this message
		// we will(have to) get this message again
		ignoreInboundMsg(fixMsg);
	}

    // send our own resendRequest now
    
    // send posdup flag also -- cme testing 04292007
    sendResendRequest(expSeqNum,fixMsg.getPossDupFlag());
    

	/*  We cant send confirm, unless client fulfilled resend
	// we will probably recieve logout again and process it at that time  
	// IMPLEMENT LATER
	// In the case of LOGOUT, implement a synchronous resendRequest version
	// and then issue logoutconfirm
    if(logoutMsg)
    {
    	processLogoutMessage("Logout Confirmed");
    }
    */
    	
}
/**
 * method to disconnect the session
 * @param reason
 */
private void disconnect(String reason)
{
	Logger.debug("DISCONNECTING - "+ reason);
	//implement proper disconnect
	shutdownSession();
	//System.exit(0);
}

/**
 * method to process stale connection scenario
 */
public void staleConnectionDetected()
{
	Logger.debug("STALE CONNECTION");
	//implement proper disconnect
	shutdownSession();
	//System.exit(0);
}

/* (non-Javadoc)
 * @see com.exsys.common.tcpsession.TcpSession#shutdownSession()
 */
protected void shutdownSession()
{
	if(isServerSession())
	{
		shutdownServerSession();
	}
	else
	{
		shutdownClientSession();
	}
}

/**
 * method to shutdown a fix client session
 */
protected void shutdownClientSession()
{
	// need to implement graceful shutdown
	Logger.debug("Shutting down client session");
	try
	{
		// first shutdown heartbeat thead
		heartBeatThread.stopThread();
		
		if(heartBeatThread.isAlive())
			heartBeatThread.join();

		Logger.debug("Closing out queue");
		outQueue.closeQueue();
		outThread.stopThread();
			
		if(outThread.isAlive())
			outThread.join();
			
		Logger.debug("Closing ignore queue");
		ignoreQueue.closeQueue();
		ignoreThread.stopThread();
			
		if(ignoreThread.isAlive())
			ignoreThread.join();			
			
	}
	catch(Exception e)
	{
		// ignore exceptions.
	}
	Logger.debug("Heartbeat Thread exited");
	Logger.debug("Out Thread exited");
	Logger.debug("IgnoreMsg Thread exited");
	
	// first shutdown tcp thread
	super.stopThread();


	
	//System.exit(0);
}


/**
 * method to handle shutting down server session
 */
protected void shutdownServerSession()
{
	// need to implement graceful shutdown
	// need to implement graceful shutdown
	Logger.debug("Shutting down server session");
	try
	{
		// first shutdown heartbeat thead
		heartBeatThread.stopThread();
		
		if(heartBeatThread.isAlive())
			heartBeatThread.join();

		Logger.debug("Closing out queue");
		outQueue.closeQueue();
		outThread.stopThread();
			
		if(outThread.isAlive())
			outThread.join();
			
		Logger.debug("Closing ignore queue");
		ignoreQueue.closeQueue();
		ignoreThread.stopThread();
			
		if(ignoreThread.isAlive())
			ignoreThread.join();			
			
	}
	catch(Exception e)
	{
		// ignore exceptions.
	}
	Logger.debug("Heartbeat Thread exited");
	Logger.debug("Out Thread exited");
	Logger.debug("IgnoreMsg Thread exited");
	
	// first shutdown tcp thread
	super.stopThread();
	
	//System.exit(0);
}

/**
 * method to send a resend request message
 * @param expSeqNum
 * @param isPosDup
 */
private void sendResendRequest(int expSeqNum, boolean isPosDup)
{
	FixResendRequest msg = new FixResendRequest();
	setHeaderFields(msg);
	msg.setBeginSeqNo(expSeqNum);
	msg.setEndSeqNo(0);
	boolean isDuplicate = false;
	if( lastResendExpSeqNum != expSeqNum || !isPosDup )
	{
		lastResendExpSeqNum = expSeqNum;		
	}
	else
	{
		// else we expect that we have already sent a resend request
		// and send a posDup
		isDuplicate = true;
		//04282007 - cme did not like pos dup flag????
		msg.setPossDupFlag(true);
	}
	if( !isDuplicate )
	{
		lastResendSeqNum = sendMessage(msg,0);
	}
	else
	{
		sendMessage(msg,lastResendSeqNum);
	}
}
  

/**
 * getter method that returns heartbeat interval
 * @return value in milli seconds
 */
public int getHeartBeatInterval() 
{
	return heartBeatInSeconds*1000;
}

/*
public int getInSeq() {
	return inSeq;
}
*/

/*
public int getOutSeq() {
	return outSeq;
}
*/

//public FixSessionContext getSessionContext() {
//	return context;
//}

/**
 * method to initialize FixSession
 * @return
 */
public boolean initializeFixSession() 
{
	outQueue = new com.exsys.common.util.Queue();
	outThread = new DequeueThread( outQueue, new OutQueueAdapter()  );
	outThread.start();
	return false;
}

/**
 * method to initialize FixSessionManager
 */
protected void initializeSessionManager() 
{

	String scId = getSenderCompID();
	if(scId == null)
	{
		System.out.println("SenderCompId is NULL");
		// MAY BE THROW EXCEPTION -- SERIOUS ERROR
		shutdownSession();
	}
	initializeSessionManager(scId);
}

/**
 * method to initialize fix session manager
 * @param scId
 */
protected void initializeSessionManager(String scId) 
{

	sessionMgr = new FixSessionManager( scId );
	sessionMgr.setLogDirectory(logFileDirectory);
	sessionMgr.initializeSequenceNumbers();
	inSeq = sessionMgr.getInSeq();
	outSeq = sessionMgr.getOutSeq();
	if( inSeq != 0 )
	{
		sessionRejectList = sessionMgr.getSessionRejectSeqList();
		if(!sessionRejectList.isEmpty())
		{
			Logger.debug("RESTORE HAD SESSION REJECTED MSGS");
		}
		
	}
}

/**
 * method to handle failover for CME
 */
synchronized public void switchToPrimary()
{
	if(!isPrimary())
	{
		Logger.debug("Initializing Session Manager");
		if(isServerSession())
		{

			String tcID = getTargetCompID();
			Logger.debug("Target Comp ID = " + tcID);
			Logger.debug("Client's sender comp id = "+ 
					(tcID.substring(0,tcID.length()-1)+"U"));			
			initializeSessionManager(tcID.substring(0,tcID.length()-1)+"U");
					
		}
		else
		{
			String scID = getSenderCompID();
			Logger.debug("Sender Comp ID = " + scID);
			Logger.debug("Client's sender comp id = "+ 
					(scID.substring(0,scID.length()-1)+"U"));			
			initializeSessionManager(scID.substring(0,scID.length()-1)+"U");		
		}
		
		// initialize fix session first as we want it to use
		// old sendercompid for file names
		// then flip the last character in targetCompID	
		setPrimary(true);	
	}
}

/**
 * method to handle failover for CME
 */
public void switchToBackup()
{
	if(isPrimary())
	{
		shutdownSession();
	}
}


/**
 * getter method to get sender comp id
 * @return
 */
public java.lang.String getSenderCompID() {	
	Logger.debug("THIS SHOULD NOT BE CALLED");
	return null;
}
/**
 * getter method to get target comp id
 * @return
 */
public java.lang.String getTargetCompID() {
		return null;
}


/**
 * getter method to get sender location id
 * @return
 */
public java.lang.String getSenderLocationID() {
	return null;
}

/**
 * setter method to set sender location id
 * @param senderLocationID
 */
public void setSenderLocationID(java.lang.String senderLocationID) {	
}

/**
 * getter method to get sender sub id
 * @return
 */
public java.lang.String getSenderSubID() {
	return null;
}

/**
 * setter method to set sender sub id
 * @param senderSubID
 */
public void setSenderSubID(java.lang.String senderSubID) {	
}


/**
 * @return
 */
public java.lang.String getTargetSubID() {
	return null;
}

/**
 * @param targetSubID
 */
public void setTargetSubID(java.lang.String targetSubID) {	
}

/**
 * @return
 */
public java.lang.String getExchange() {
	return exchange;
}

/**
 * method to initialize common attributes to client and server
 * @throws ConfigAttributeNotFound
 */
protected void initializeCommon() throws ConfigAttributeNotFound
{
    	fixVersion = ConfigurationService.getValue(ConfigConstants.FIXCOMMON_FIX_VERSION);
    	exchange = ConfigurationService.getValue(ConfigConstants.FIXCOMMON_FIX_EXCHANGE);
    	String heartBeat = ConfigurationService.getValue(ConfigConstants.FIXCOMMON_HEARTBEAT_IN_SECONDS);
    	// handle parseException
    	heartBeatInSeconds = Integer.parseInt(heartBeat);	   		   	

		logFileDirectory = ConfigurationService.getValue(ConfigConstants.FIXCOMMON_LOG_FILE_DIRECTORY);
}

/**
 * 
 * method to handle logonSuccessful event
 */
public void logonSuccessful() 
{

	if(playbackMode) return;
	
	heartBeatThread = new FixSessionHeartbeat( this );
	Logger.debug("Starting Heartbeat Thread");
	Logger.debug("Heart beat int is "+ getHeartBeatInterval());
	heartBeatThread.start();
}

/**
 * method that prepares the FixBuffer to be sent over
 * @param msg
 * @return
 */
public String prepareFixBuffer(FixMessage msg) 
{
	return prepareFixBuffer(msg,false);
}

/**
 * method to prepare Fix messsage
 * @param msg
 * @param resend
 * @return
 */
public String prepareFixBuffer(FixMessage msg, boolean resend) 
{

	// This needs to translate msg to FIX String
	// Prepend required header tags
	// compute checksum
	String fixString = null;

	try
	{
		StringWriter sw = new StringWriter();
		NativeFixTranslator ft = new NativeFixTranslator( sw );
		
		if(!resend)
			ft.translateHeader( msg, false );
		else
			ft.translateResendHeader(msg);
			
		String headerString = sw.toString();
		sw.getBuffer().setLength(0);
		
		
		if(!resend)
			ft.translateBody( msg );
		else
			ft.translateBody(msg,true);
			
		String bodyString = sw.toString();

		String msgTypeString = FixConstants.FIX_HEADER_MSGTYPE_TAG +
							  FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
							  msg.getMessageType() +
							  (char)FixConstants.FIX_FIELD_DELIMITER ;
							  
		int bodyLength = headerString.getBytes().length + 
						 bodyString.getBytes().length +
						 msgTypeString.getBytes().length;
		
		// prefix header fields
		String prefixString = FixConstants.FIX_HEADER_BEGINSTRING_TAG +
							  FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
							  getFixVersion() +
							  (char)FixConstants.FIX_FIELD_DELIMITER +
							  FixConstants.FIX_HEADER_BODYLENGTH_TAG +
							  FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
							  String.valueOf( bodyLength ) +
							  (char)FixConstants.FIX_FIELD_DELIMITER +
							  msgTypeString;							  

							  
		fixString = prefixString + headerString + bodyString;					  
		
		String checkSum = FixMessage.checksum(fixString);

		String trailerString = 	FixConstants.FIX_TRAILER_CHECKSUM_TAG +
								FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
								checkSum +
								(char)FixConstants.FIX_FIELD_DELIMITER ;
		
		fixString += trailerString;
		
			

	}
	catch(Exception e )
	{
		e.printStackTrace();
	}

	return fixString;

}

/**
 * main method to process application layer messages
 * @param msg
 * @param msgType
 */
public void processApplicationMessage(FixMessage msg, String msgType) 
{
			
	if( mProcessor == null && mSecProcessor == null &&  mMDProcessor == null)
	{
		Logger.debug("Could not handle application messasge");
		// we may have to throw an exception
		return;
	}
	
	Logger.debug("processIncomingAppFixMessage :" + msgType);

	if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
	{
		if(isServerSession())
		{
			mProcessor.processOrder( (FixOrder)msg );
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
			
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL) )
	{
		if(isServerSession())
		{
			mProcessor.processCancel( (FixCancel)msg );
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}		
		
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER_STATUS_REQUEST) )
	{
		if(isServerSession())
		{
			mProcessor.processStatusRequest( (FixOrderStatusRequest)msg );
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}		
		
	}	
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE) )
	{
		
		if(isServerSession())
		{
			mProcessor.processCancelReplace( (FixCancelReplace)msg );
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}		
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT) )
	{
		
		if(!isServerSession())
		{
			mProcessor.processExecutionReport( (FixExecutionReport)msg );
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT) )
	{
		
		if(!isServerSession())
		{
			mProcessor.processCancelReject( (FixCancelReject)msg );
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE) )
	{
		
		if(!isServerSession())
		{
			// handle it later. for now ignore
			Logger.info("Received Security Definition Response");
			mSecProcessor.processSecurityDefinitionResponse((FixSecurityDefinitionResponse)msg);
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH) )
	{
		
		if(!isServerSession())
		{
			// handle it later. for now ignore
			Logger.info("Received Market Data Full Refresh");
			mMDProcessor.processMarketDataFullRefresh((FixMarketDataFullRefresh)msg);
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}	
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH) )
	{
		
		if(!isServerSession())
		{
			// handle it later. for now ignore
			Logger.info("Received Market Data Incremental Refresh");
			mMDProcessor.processMarketDataIncrementalRefresh((FixMarketDataIncrementalRefresh)msg);
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}	
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REJECT) )
	{
		
		if(!isServerSession())
		{
			// handle it later. for now ignore
			Logger.info("Received Market Data Reject");
			mMDProcessor.processMarketDataReject((FixMarketDataReject)msg);
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}	
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS) )
	{
		
		if(!isServerSession())
		{
			// handle it later. for now ignore
			Logger.info("Received Market Data Security Status");
			mMDProcessor.processMarketDataSecurityStatus((FixSecurityStatus)msg);
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}	
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_NEWS) )
	{
		
		if(!isServerSession())
		{
			// handle it later. for now ignore
			Logger.info("Received News");
			if(mNewsProcessor != null)
				mNewsProcessor.processNews((FixNews)msg);
			else
				ignoreInboundMsg(msg);
		}
		else
		{
			String seqNum = msg.getMsgSeqNumAsString();
			sendSessionReject("Unknown Message Type", seqNum);
		}
		
	}						
	else
	{
		// UNKNOWN APPLICATION TYPE
		Logger.error("UNIMPLEMENTED MSG TYPE RECEIVED - " + msgType);
		String seqNum = msg.getMsgSeqNumAsString();
		sendSessionReject("Message Type Not Implemented", seqNum);

		shutdownSession();
	}
		
		
}

/**
 * method to process beartbeat message
 * @param heartbeat
 */
public void processHeartBeatMessage(FixMessage heartbeat) 
{
	Logger.debug("Heartbeat Received");
	//sendHeartBeat();
}

/**
 * main method to process inbound fix messages
 * @param msg
 */
public void processIncomingFixMessage(FixMessage msg) 
{

	String msgType = msg.getMessageType();
	Logger.debug("processIncomingFixMessage :" + msgType);
	if( msgType.equals(FixConstants.FIX_MSGTYPE_LOGON))
	{
		processLogonMessage( msg, true );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_HEARTBEAT) )
	{
		processHeartBeatMessage( msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_TESTREQUEST) )
	{
		processTestRequestMessage( (FixTestRequest)msg );
	}
	//else if ( msgType.equals(FixConstants.FIX_MSGTYPE_RESENDREQUEST) )
	//{
		// ignore this messsage, as it is processed already
		//System.out.println("About to call processResendRequestMessage()");
		//processResendRequestMessage( (FixResendRequest)msg );
		
	//}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_REJECT) )
	{
		processSessionRejectMessage( (FixSessionReject)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_BUSINESS_REJECT) )
	{
		processBusinessRejectMessage( (FixBusinessReject)msg );
	}	
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_SEQUENCERESET) )
	{
		processSequenceResetMessage((FixSequenceReset) msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_LOGOUT) )
	{
		processLogoutMessage( "Logout Confirmed" );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_TRADER_LOGON_RESPONSE) )
	{
		processTraderLogonResponse((FixTraderLogonResponse)msg);
	}	
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_TRADER_LOGOUT_RESPONSE) )
	{
		processTraderLogoutResponse((FixTraderLogoutResponse)msg);
	}		
	else
	{

		processApplicationMessage(msg, msgType);
		// This must be application message
	}
	
}

// MAY BE THIS METHOD SHOULD THROW EXCEPTION
/**
 * process logon message
 * @param logon
 * @param normal
 * @return
 */
public boolean processLogonMessage(FixMessage logon, boolean normal) {return false;}

// MAY BE THIS METHOD SHOULD THROW EXCEPTION
/**
 * process trader logon response msg used for ICE only
 * @param msg
 * @return
 */
public boolean processTraderLogonResponse(FixTraderLogonResponse msg) {return false;}

// MAY BE THIS METHOD SHOULD THROW EXCEPTION
/**
 * process trader logout response msg used for ICE only
 * @param msg
 * @return
 */
public boolean processTraderLogoutResponse(FixTraderLogoutResponse msg) {return false;}



/**
 * process Logout Message
 * @param text
 */
public void processLogoutMessage(String text)
{

	processLogoutMessage(text,0);	 	
		
}
/**
 * process logout message
 * @param text
 * @param nextExpSeq
 */
public void processLogoutMessage(String text, int nextExpSeq)
{

	FixLogout logoutConfirm = new FixLogout();
	if(isServerSession())
	{
	    setHeaderFields(logoutConfirm);
		logoutConfirm.setText(text);
		if(nextExpSeq != 0 )
		{
			logoutConfirm.setNextExpectedMsgSeqNum(nextExpSeq);
		}
		else
		{
			logoutConfirm.setNextExpectedMsgSeqNum(getInSequenceNumber()+1);
		}
		sendMessage( logoutConfirm );
	    
	}
	else
	{
	 	// NOT SURE IF CLIENT NEED TO SEND LOGOUT CONFIRM
	 	// we can just shut down session
	 	shutdownSession();
	}
	 	
		
}
/**
 * process Fix Business Reject
 * @param msgReject
 */
private void processBusinessRejectMessage(FixBusinessReject msgReject) 
{
	String seqNum = msgReject.getRefSeqNumAsString();
	String text = msgReject.getText();
	Logger.debug("BUSINESS REJECT - Text = " + text);
	error("BUSINESS REJECT - text = " + text);				
	int rejReason = msgReject.getBusinessRejectReason();
	Logger.debug("BUSINESS REJECT - reason = " + rejReason);
	error("BUSINESS REJECT - reason = " + rejReason);				
	
}


/**
 * process Fix Session Reject message
 * @param msgReject
 */
private void processSessionRejectMessage(FixSessionReject msgReject) 
{
	String seqNum = msgReject.getRefSeqNumAsString();
	String errorMsg = msgReject.getText();
	Logger.debug("SESSION REJECT - reason = " + errorMsg);
	error("SESSION REJECT - reason = " + errorMsg);
	
	// check if the message is related to max 2500 resend msgs error
	// if so the client or server needs to LOGOUT
	if(isServerSession())
	{
		Logger.error("SERIOUS ERROR - SERVER RECEIVED SESSION REJECT");
		shutdownSession();
	}
	else
	{
		if(errorMsg.lastIndexOf("2500") != -1)
		{
			// need to LOGOUT and disconnect
			sendLogout("Disconnecting",getInSequenceNumber());
			disconnect("MaxResend size error");
			
		}
	}
	
		
	// IMPLEMENT LATER
	// PROVIDE ALERT MECHANISM - NOT SURE IF THIS NEEDS TO BE WRITTEN TO
	// A SEPARATE LOG FILE
	// add to arraylist
	sessionRejectList.add(seqNum);		
	
}

/**
 * process sequence reset message
 * @param msgSequenceReset
 */
public void processSequenceResetMessage(FixSequenceReset msgSequenceReset) 
{

	if (msgSequenceReset.getGapFillFalg())
	{
		Logger.debug("**************  In Sequence Reset Gap Fill Case  ****************");
		//Sequence Reset-GapFill message issued to reset sequenceNo
		Logger.debug("About to change Inseq  = " + msgSequenceReset.getNewSeqNo());
		if (inSeq < msgSequenceReset.getNewSeqNo())
		{
			inSeq=msgSequenceReset.getNewSeqNo() - 1;
		}
		else
		{
			//Serious problem, so reject the message.
			// SERIOUS ERROR - LOGOUT and DISCONNECT
			// send LOGOUT MSG with expected sequence number
			processLogoutMessage("Invalid Sequence Number in SequenceReset");	
			disconnect("SERIOUS ERROR - INVALID SEQUENCENUMBER");
			
		}
	}
	
	
}

/**
 * process test request message
 * @param msgTestRequest
 */
public void processTestRequestMessage(FixTestRequest msgTestRequest) 
{
	String testRequestID = msgTestRequest.getTestReqIDAsString();
	FixHeartBeat heartBeat = new FixHeartBeat();
	setHeaderFields(heartBeat);
	heartBeat.setTestReqID( testRequestID );

	sendMessage( heartBeat );
	
	
}

/**
 * method to register application message processor
 * @param proc
 */
public void registerApplicationMessageProcessor(TradeMessageProcessor proc) 
{
	mProcessor = proc;	
}
/**
 * method to register securities message processor
 * @param proc
 */
public void registerSecuritiesMessageProcessor(SecuritiesMessageProcessor proc) 
{
	mSecProcessor = proc;	
}
/**
 * method to register market data message processor
 * @param proc
 */
public void registerMarketDataMessageProcessor(MarketDataMessageProcessor proc) 
{
	mMDProcessor = proc;	
}
/**
 * method to register news message processor
 * @param proc
 */
public void registerNewsMessageProcessor(NewsMessageProcessor proc) 
{
	mNewsProcessor = proc;	
}

/**
 * method to send a fix message and wait for response
 * @param msg
 * @return
 */
public FixMessage sendAndGetResponse(FixMessage msg) 
{
	
	// This needs to translate msg to FIX String
	// Prepend required header tags
	// compute checksum
	msg.setMsgSeqNum( ++outSeq );
	String fixString = prepareFixBuffer( msg );
	Logger.debug(" About to send Msg is " + fixString );
	System.out.println(sessionMgr == null );
	sessionMgr.logOutFixMessage( outSeq,fixString.getBytes());	
	StringBuffer response = writeAndWaitForResponse( fixString );
	Logger.debug("Waiting for response");
	FixMessage responseMsg = null;
	try
	{
		responseMsg = FixMessageFactory.createFixMessage(
				response.toString().getBytes(),exchange,
				isServerSession());
		inSeq = responseMsg.getMsgSeqNum();
		sessionMgr.logInFixMessage(inSeq, response.toString().getBytes());
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}


	return responseMsg;

}

/**
 * method to send fix execution report message
 * @param response
 */
public void sendExecutionReport(FixExecutionReport response) 
{
	Logger.debug("Sending ExecutionReport");
	setHeaderFields(response);
	sendMessage(response);
}
/**
 * method to send fix cance reject message
 * @param cxlRej
 */
public void sendCancelReject(FixCancelReject cxlRej) 
{
	Logger.debug("Sending Cancel Reject");
	setHeaderFields(cxlRej);
	sendMessage(cxlRej);
}

/**
 * method to send heartbeat message
 */
public void sendHeartBeat() 
{
	FixHeartBeat heartBeat = new FixHeartBeat();
	setHeaderFields(heartBeat);
	sendMessage( heartBeat );
	/* added due to test testcase 3.6  on page 27
	if(isServerSession())
	{
		sendMessage( heartBeat,100 );
	}
	else
	{
		sendMessage( heartBeat );
	}
	*/
}
/**
 * method to send fix session reject message
 * @param reason
 * @param refSeqNum
 */
public void sendSessionReject(String reason, String refSeqNum) 
{
	sendSessionReject(reason,refSeqNum,null);
}

/**
 * method to send fix session reject message
 * @param reason
 * @param refSeqNum
 * @param reasonCode
 */
public void sendSessionReject(String reason, String refSeqNum, String reasonCode) 
{
	FixSessionReject reject = new FixSessionReject();
	setHeaderFields(reject);
	reject.setRefSeqNum(refSeqNum);
	reject.setText(reason);
	if(reasonCode != null)
		reject.setSessionRejectReason(reasonCode);	
	sendMessage( reject );
}

/**
 * method to send fix business reject message
 * @param reason
 * @param refSeqNum
 * @param reasonCode
 */
public void sendBusinessReject(String reason, String refSeqNum, String reasonCode) 
{
	FixBusinessReject reject = new FixBusinessReject();
	setHeaderFields(reject);
	reject.setRefSeqNum(refSeqNum);
	reject.setText(reason);
	if(reasonCode != null)
		reject.setBusinessRejectReason(reasonCode);	
	sendMessage( reject );
}

/**
 * method to send fix test request message
 * @param id
 */
public void sendTestRequest(String id) 
{
	FixTestRequest testRequest = new FixTestRequest();
	setHeaderFields(testRequest);
	testRequest.setTestReqID(id);
	sendMessage( testRequest );
}
/**
 * method to send fix logout message
 * @param msg
 * @param nextExpSeqNum
 */
public void sendLogout(String msg,int nextExpSeqNum)
{
	FixLogout logout = new FixLogout();
	setHeaderFields(logout);
	logout.setText(msg);
	if(getExchange().equals("CME"))
		logout.setNextExpectedMsgSeqNum(nextExpSeqNum);
	sendMessage( logout );
}

/**
 * method to send any fix message
 * @param msg
 * @return
 */
public int sendMessage(FixMessage msg) 
{
	return sendMessage(msg,0);

}

/**
 * method to send a resend of a message
 * @param msg
 * @param seqNum
 * @return
 */
public int sendResendMessage(FixMessage msg, int seqNum) 
{
	// This needs to translate msg to FIX String
	// Prepend required header tags
	// compute checksum

	// set sequence number
	
		msg.setMsgSeqNum( seqNum );
		String fixString = prepareFixBuffer( msg,true );
		Logger.debug(" About to re-send Msg is " + fixString );
		//set LastMsgSeqNumProcessed
		msg.setLastMsgSeqNumProcessed(getInSequenceNumber());
		writeMessage( fixString );
	return seqNum;


}
/**
 * method to send a fix message
 * @param msg
 * @param seqNum
 * @return
 */
public int sendMessage(FixMessage msg, int seqNum) 
{
	// This needs to translate msg to FIX String
	// Prepend required header tags
	// compute checksum

	// set sequence number
	
	synchronized(sendMessageLock)
	{
		int sentSeqNum = seqNum;
		if( seqNum == 0)
		{
			if(isPrimary())
				seqNum = incrementOutSequenceNumber();
		}
		msg.setMsgSeqNum( seqNum );
		String fixString = prepareFixBuffer( msg );
		//Logger.debug(" About to send Msg is " + fixString );
		if( sentSeqNum == 0)
		{
			if(isPrimary())
				sessionMgr.logOutFixMessage( outSeq,fixString.getBytes());
		}
		//set LastMsgSeqNumProcessed
		msg.setLastMsgSeqNumProcessed(getInSequenceNumber());
		//writeMessage( fixString );
		outQueue.enqueue(fixString);
	}
	return seqNum;


}


/**
 * method to send a fix message
 * @param msg
 * @param seqNum
 * @return
 */
public int sendResetLogonMessage(FixMessage msg, int seqNum) 
{
	// This needs to translate msg to FIX String
	// Prepend required header tags
	// compute checksum

	// set sequence number
	
	
	synchronized(sendMessageLock)
	{
		int sentSeqNum = seqNum;

		msg.setMsgSeqNum( seqNum );
		String fixString = prepareFixBuffer( msg );
		//Logger.debug(" About to send Msg is " + fixString );
		sequenceReset();
		//inSeq = seqNum;
		outSeq = seqNum;
		sessionMgr.logOutFixMessage( outSeq,fixString.getBytes());
		//set LastMsgSeqNumProcessed
		msg.setLastMsgSeqNumProcessed(getInSequenceNumber());
		//writeMessage( fixString );
		outQueue.enqueue(fixString);
	}
	return seqNum;


}

/**
 * method to send fix order
 * @param ord
 */
public void sendOrder(FixOrder ord) 
{
	System.out.println("Sending Order");
	setHeaderFields(ord);
	sendMessage(ord);
	
}
/**
 * method to send fix order status request message
 * @param stat
 */
public void sendStatusRequest(FixOrderStatusRequest stat) 
{
	System.out.println("Sending Status");
	setHeaderFields(stat);
	sendMessage(stat);
	
}

//public void setSessionContext(FixSessionContext ctx) 
//{
//	context = ctx;
//}


/**
 * method to get Mesages to Resend
 * @param stream
 * @param seqNum
 * @param endSeq
 * @return
 */
public Vector getMessagestoResend(FileInputStream stream, int seqNum, int endSeq) {
	
	int charRead;
	StringBuffer seq = new StringBuffer();
	StringBuffer length = new StringBuffer();
	String returnMessage=null;
	Vector vecItems=new Vector();
	try
	{
		while( (charRead=stream.read()) != -1 )
		{
			seq.setLength(0);
			length.setLength(0);
			seq.append((char)charRead);
			while( (charRead=stream.read()) != -1 )
			{

				if( (char)charRead != ';' )
				{
					seq.append( (char)charRead );
				}
				else
				  break;			
			}
		
			while( (charRead=stream.read()) != -1 )
			{
				if( (char)charRead != ';' )
				{
					length.append( (char)charRead );
				}
				else
				  break;			
			}
			int currSeq;
			currSeq=Integer.parseInt(seq.toString());
			
			if (endSeq == 0)			
 			{
				if ( seqNum <= currSeq)
				{
					int len = Integer.parseInt(length.toString());
					byte[] msgRead = new byte[len];
					stream.read( msgRead );
					String message1 = new String(msgRead);
				//	System.out.println("Message is : " + message1);
					//FixMessage fixMsg = FixMessageFactory.createFixMessage( message.toString().getBytes());
					FixMessage fixMsg = FixMessageFactory.createFixMessage(msgRead, exchange,isServerSession());
					vecItems.addElement(fixMsg);
				}else
					stream.skip( Integer.parseInt(length.toString()) );

			}else if (endSeq > 0)
			{
				if (( seqNum <= currSeq) && ( (endSeq) >= currSeq))
				{
					int len = Integer.parseInt(length.toString());
					byte[] msgRead = new byte[len];
					stream.read( msgRead );
					String message2 = new String(msgRead);
				//	System.out.println("Message is : " + message2);
					FixMessage fixMsg = FixMessageFactory.createFixMessage(msgRead,exchange,isServerSession());
					vecItems.addElement(fixMsg);
				}else
					stream.skip( Integer.parseInt(length.toString()) );
			}
			
			
				
		}
				
		
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

	return vecItems;
	

}




/**
 * method to send fix cancel
 * @param cxl
 */
public void sendCancel(FixCancel cxl) 
{
	Logger.debug("Sending Cancel");
	setHeaderFields(cxl);
	sendMessage(cxl);
	
}
/**
 * method to send security definition request
 * used in ICE
 * @param req
 */
public void sendSecurityDefinitionRequest(FixSecurityDefinitionRequest req) 
{
	Logger.debug("Sending SecurityDefinitionRequest");
	setHeaderFields(req);
	sendMessage(req);	
}
/**
 * method to send market data request message
 * used in ICE
 * @param req
 */
public void sendMarketDataRequest(FixMarketDataRequest req) 
{
	Logger.debug("Sending MarketData Request");
	setHeaderFields(req);
	sendMessage(req);	
}


/**
 * method to send fix cancel replace message
 * @param cxr
 */
public void sendCancelReplace(FixCancelReplace cxr) 
{
	Logger.debug("Sending CancelReplace");
	setHeaderFields(cxr);
	sendMessage(cxr);
	
}
/**
 * @param bow
 */
public void setBOWLogon(boolean bow)
{
	bowLogon = bow;
}
/**
 * @return
 */
public boolean isBOWLogon()
{
	return bowLogon;
}

	/**
	 * @return
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param initialized
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * @return
	 */
	public static String getFixVersion() {
		return fixVersion;
	}

	/**
	 * @param fixVersion
	 */
	public static void setFixVersion(String fixVersion) {
		fixVersion = fixVersion;
	}

	/**
	 * @return
	 */
	public int getHeartBeatInSeconds() {
		return heartBeatInSeconds;
	}

	/**
	 * @param heartBeatInSeconds
	 */
	public void setHeartBeatInSeconds(int heartBeatInSeconds) {
		this.heartBeatInSeconds = heartBeatInSeconds;
	}

/**
 * @return
 */
public boolean isPrimary()
{
	return primary==true;
}
/**
 * @param flag
 */
public void setPrimary(boolean flag)
{
	primary = flag;
}
/**
 * @return
 */
private int incrementInSequenceNumber()
{
	synchronized(inSequenceLock)
	{
		return ++inSeq;
	}
	
}	

/**
 * @return
 */
private int incrementOutSequenceNumber()
{
	synchronized(outSequenceLock)
	{
		return ++outSeq;
	}
	
}	
/**
 * @param seq
 * @return
 */
private int setInSequenceNumber( int seq )
{
	synchronized(inSequenceLock)
	{
		return inSeq = seq;
	}
	
}	

/**
 * @param seq
 * @return
 */
private int setOutSequenceNumber( int seq )
{
	synchronized(outSequenceLock)
	{
		return outSeq = seq;
	}
	
}

/**
 * @return
 */
public int getInSequenceNumber()
{
	synchronized(inSequenceLock)
	{
		return inSeq;
	}
	
}	

/**
 * @return
 */
public int getOutSequenceNumber()
{
	synchronized(outSequenceLock)
	{
		return outSeq;
	}
	
}

/**
 * @return
 */
public boolean isServerSession()
{
	return false;
}

/**
 * method to ignore an inbound fix message
 * @param msg
 */
private void ignoreInboundMsg(FixMessage msg)
{
	Logger.debug("MSG BEING IGNORED");
	if( ignoreQueue == null )
	{
		ignoreQueue = new com.exsys.common.util.Queue();
		ignoreThread = new DequeueThread( ignoreQueue, new IgnoreQueueAdapter()  );
		ignoreThread.start();
		ignoreQueue.enqueue(msg);
	}
	else
	{
		ignoreQueue.enqueue(msg);		
	}
	
	
}
/**
 * @param fixMsg
 */
public void setHeaderFields(FixMessage fixMsg)
{
}

public void playbackMessages()
{
    try
    {
    	
    	
			try
			{
				logonSuccessful();
				{
					Logger.debug("Sleeping for 3 seconds");
					
					Thread.sleep(3000);
				}
			}
			catch(Throwable e)
			{
			}	    	
	   		     	


 	     //String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     //ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("FixInLogFile");
 	     
		FileInputStream stream= new FileInputStream(inFile);
		Logger.debug("File is " + inFile);
 	     
 	     
 	    FixMessage msg = FixSessionManager.getApplicationMessages(stream);
		

 	     // then process in list
 	    while(msg != null)
	    {
	    	Logger.debug("Restoring");
    		//FixMessage msg = (FixMessage)inList.get(i);    	
	    	String msgType = msg.getMessageType();
	    	Logger.debug("Restoring - "+msgType);
			System.out.println("FIX SEQ # :" + msg.getMsgSeqNumAsString());
			
	    	processApplicationMessage(msg,msgType);
	    	
	    	//Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());
	    	
	    	/*
			try
			{
				if(isProcessed)
				{
					Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());
					
					Thread.sleep(3000);
				}
			}
			catch(Throwable e)
			{
			}	    	
	   		*/ 	
	   		
	   		msg = FixSessionManager.getApplicationMessages(stream);
	    	
	    }
	    stream.close();
	    

      


    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
    catch( FileNotFoundException exc )
    {
      Logger.debug("PLAYBACK FILE NOT FOUND");
      exc.printStackTrace();
      System.exit(-1);
    }
    catch( IOException exc )
    {
      Logger.debug("PLAYBACK FILE CAN NOT BE CLOSED");
      exc.printStackTrace();
      System.exit(-1);
    }    
    
}

// this method loads all the messages and then processes one at a time
public void playbackMessagesAllAtOnce()
{
    try
    {
    	
    	
			try
			{
				logonSuccessful();
				{
					Logger.debug("Sleeping for 3 seconds");
					
					Thread.sleep(3000);
				}
			}
			catch(Throwable e)
			{
			}	    	
	   		     	


 	     //String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     //ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("FixInLogFile");
 	     ArrayList inList = FixSessionManager.getApplicationMessagesList(inFile);

		/*
 	     // first process out list
 	    for(int i=0; i<outList.size();i++)
	    {
    		FixMessage msg = (FixMessage)outList.get(i);
	    	String msgType = msg.getMessageType();
	    		Logger.debug("Restoring - "+msgType);
	    	handleRestoredMessage(msgType,msg);
	    }

		*/


 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	Logger.debug("Restoring");
    		FixMessage msg = (FixMessage)inList.get(i);    	
	    	String msgType = msg.getMessageType();
	    	Logger.debug("Restoring - "+msgType);
	    	processApplicationMessage(msg,msgType);
	    	//Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());
	    	System.out.println("FIX SEQ # :" + msg.getMsgSeqNumAsString());
	    	/*
	    	boolean isProcessed =true;
			try
			{
				if(isProcessed)
				{
					Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());
					
					Thread.sleep(300);
				}
			}
			catch(Throwable e)
			{
			}	
			*/    	
	   		 	
	    	
	    }

      


    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
}


}
