package com.exsys.impact.session;

import java.io.*;
import java.util.*;

import com.exsys.common.exceptions.*;
import com.exsys.common.tcpsession.*;
import com.exsys.common.util.*;
import com.exsys.impact.message.*;
import com.exsys.service.ConfigConstants;
import com.exsys.service.ConfigurationService;
import com.exsys.service.Logger;
import com.exsys.common.trading.*;
import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;


/**
 * @author kreddy
 *
 *Main class that implements FIX communication
 *and FIX Session layer.
 *This also has methods to support both fix client
 *and fix server
 */
public class ImpactSession extends ImpactTcpSession
{
	//private FixSessionContext context = null;
	private boolean initialized = false;
	private ImpactSessionManager sessionMgr = null;
	private int inSeq = 0;
	private int outSeq = 0;
	private ImpactSessionHeartbeat heartBeatThread = null;
	// queue and thread to handle outbound messages
	private com.exsys.common.util.Queue outQueue = null;
	private DequeueThread outThread;

	// queue and thread to handle ignored messages
	private com.exsys.common.util.Queue ignoreQueue = null;
	private DequeueThread ignoreThread;


	public boolean resendRequestFlag = false;
	private ImpactMessageProcessor mMDProcessor = null;
	private boolean bowLogon = false;
	private static String impactVersion = null;
	private String exchange = null;
	private int heartBeatInSeconds = 30;
	private Object inSequenceLock = new Object();
	private Object outSequenceLock = new Object();


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
			writeMessage( (byte[])msg );
		}

	}

	private class IgnoreQueueAdapter implements DequeueAdapter
	{
		public void dequeuedMessage( Object msg )
		{
			// write this message into ignore msg log file
			sessionMgr.logIgnoreImpactMessage( 0,(byte[])msg);

		}

	}

/**
 * ImpactSession constructor
 * @param playback
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ImpactSession(boolean playback)
		throws ConfigAttributeNotFound,
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	playbackMode = true;
	//initializeConfig();
}
/**
 * ImpactSession constructor
 * @param host
 * @param port
 * @throws ConfigAttributeNotFound
 * @throws SystemException
 */
public ImpactSession(String host, int port)
		throws ConfigAttributeNotFound,
			   SystemException
{
	super( host, port );
	//initialized = initializeFixSession();
	initializeCommon();

}

/**
 * ImpactSession constructor
 * @param _socket
 * @throws ConfigAttributeNotFound
 * @throws SystemException
 */
public ImpactSession(java.net.Socket _socket)
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
public boolean isAdminMessage(char msgType)
{
	if( msgType == RawMessageFactory.LoginResponseType
		|| msgType == RawMessageFactory.LogoutRequestType
		|| msgType == RawMessageFactory.LoginResponseType
		|| msgType == RawMessageFactory.LogoutResponseType
		|| msgType == RawMessageFactory.DebugResponseType
		|| msgType == RawMessageFactory.DebugResponseType
		|| msgType == RawMessageFactory.HeartBeatMessageType
		|| msgType == RawMessageFactory.ErrorResponseType
		)
	{
		return true;
	}

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

		try
		{
		byte[] message = (byte[])msg;
		//Logger.debug("ImpactSession - dequeuedMessage - Message Received is "+ message );
		
		if((message.length-1) > RawMessageFactory.MAX_MESSAGE_BODY_LENGTH)
		{
			String fmsg = "ImpactSession - dequeuedMessage -  Message Too Long - " + message[0];			
			Logger.warn(fmsg);
			ignoreInboundMsg(message);
			warn(fmsg);
			return;				
		}
		
		if(RawMessageFactory.isValid(message[0]))
		{
			//Logger.debug("**************  Valid Message  ****************"+(char)message[0]);
			
			incrementInSequenceNumber();
			sessionMgr.logInImpactMessage( getInSequenceNumber(),
						message);

			if(isAdminMessage((char)message[0]))
			{
				//Logger.debug("Admin Message Received");
				processIncomingImpactMessage(message);
			}
			else
			{
				Logger.debug("**************  Valid Message  ****************"+(char)message[0]);
				processApplicationMessage(message);			
			}
		}
		else
		{
			String fmsg = "ImpactSession - dequeuedMessage - Unknown Message - " + message[0];			
			Logger.warn(fmsg);
			ignoreInboundMsg(message);
			warn(fmsg);
			return;			
		}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

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
 * method to initialize ImpactSession
 * @return
 */
public boolean initializeImpactSession()
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

	sessionMgr = new ImpactSessionManager( scId );
	sessionMgr.setLogDirectory(logFileDirectory);
	sessionMgr.initializeSequenceNumbers();
	inSeq = sessionMgr.getInSeq();
	outSeq = sessionMgr.getOutSeq();
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
    	impactVersion = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_VERSION);
    	exchange = "ICE";
    	String heartBeat = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_HEARTBEAT_IN_SECONDS);
    	// handle parseException
    	heartBeatInSeconds = Integer.parseInt(heartBeat);

		logFileDirectory = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_LOG_FILE_DIRECTORY);
}

/**
 *
 * method to handle logonSuccessful event
 */
public void logonSuccessful()
{

	if(playbackMode) return;

	heartBeatThread = new ImpactSessionHeartbeat( this );
	Logger.debug("Starting Heartbeat Thread");
	Logger.debug("Heart beat int is "+ getHeartBeatInterval());
	heartBeatThread.start();
}


/**
 * main method to process application layer messages
 * @param msg
 * @param msgType
 */
public void processApplicationMessage(byte[] msg)
{

	if(  mMDProcessor == null)
	{
		Logger.debug("Could not handle application messasge");
		// we may have to throw an exception
		return;
	}

	Logger.debug("processApplicationMessage :" + (char)msg[0]);
	char msgType = (char)msg[0];
	
	
	if(msgType == RawMessageFactory.ProductDefinitionResponseType)
	{
		mMDProcessor.processProductDefinitionResponse(msg);
	}
		else if(msgType==RawMessageFactory.MarketSnapshotMessageType)
		{
			mMDProcessor.processMarketSnapshot(msg);
		}
		else if(msgType==RawMessageFactory.MarketSnapshotOrderMessageType)
		{
			mMDProcessor.processMarketSnapshotOrder(msg);
		}		
		else if(msgType==RawMessageFactory.AddModifyOrderMessageType)
		{
			mMDProcessor.processAddModifyOrder(msg);
		}		
		else if(msgType==RawMessageFactory.DeleteOrderMessageType)
		{
			mMDProcessor.processDeleteOrder(msg);
		}		
		else if(msgType==RawMessageFactory.TradeMessageType)
		{
			mMDProcessor.processTrade(msg);
		}		
		else if(msgType==RawMessageFactory.CancelledTradeMessageType)
		{
			mMDProcessor.processCancelledTrade(msg);
		}		
		else if(msgType==RawMessageFactory.MarketStatisticsMessageType)
		{
			mMDProcessor.processMarketStatistics(msg);
		}		
		else if(msgType==RawMessageFactory.OpenPriceMessageType)
		{
			mMDProcessor.processOpenPrice(msg);
		}		
		else if(msgType==RawMessageFactory.OpenInterestMessageType)
		{
			mMDProcessor.processOpenInterest(msg);
		}		
		else if(msgType==RawMessageFactory.SettlementPriceMessageType)
		{
			mMDProcessor.processSettlementPrice(msg);
		}		
		else if(msgType==RawMessageFactory.MarketStateChangeMessageType)
		{
			mMDProcessor.processMarketStateChange(msg);
		}						
	
	else
	{
		System.out.println("Unhandled Application message");
	}	
	/*

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
*/

}

/**
 * method to process beartbeat message
 * @param heartbeat
 */
public void processHeartBeatMessage(MDMessage heartbeat)
{
	Logger.debug("Heartbeat Received");
	//sendHeartBeat();
}

/**
 * main method to process inbound fix messages
 * @param msg
 */
public void processIncomingImpactMessage(byte[] msg) throws Exception
{

     if((char)msg[0] == RawMessageFactory.LoginResponseType)
     {
     	LoginResponse resp = (LoginResponse)RawMessageFactory.getObject(msg);
     	processLogonMessage(resp);
     }
     else if((char)msg[0] == RawMessageFactory.HeartBeatMessageType)
     {
     }
	 else if((char)msg[0] == RawMessageFactory.ErrorResponseType)
	 {
		ErrorResponse resp = (ErrorResponse)RawMessageFactory.getObject(msg);
		Logger.debug("Error is " + String.valueOf(resp.Text));
	 }	     
     else
     {
     	Logger.debug("Un implemented Admin Message");
     }

/*
	String msgType = msg.getMessageType();
	Logger.debug("processIncomingFixMessage :" + msgType);
	
	if( msgType.equals(ImpactMessageConstants.IMPACT_MESSAGE_TYPE_A))
	{
		processLogonMessage( (LoginResponse)msg );
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
*/
}


public boolean processLogonMessage(LoginResponse logon) {return false;}



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
/*
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

*/
}


/**
 * method to register market data message processor
 * @param proc
 */
public void registerImpactMessageProcessor(ImpactMessageProcessor proc)
{
	mMDProcessor = proc;
}


/**
 * method to send a fix message and wait for response
 * @param msg
 * @return
 */
/*
public byte[] sendAndGetResponse(byte[] msg)
{

	// This needs to translate msg to FIX String
	// Prepend required header tags
	// compute checksum
	//msg.setMsgSeqNum( ++outSeq );
	String impString = new String(msg.getBytes());
	//Logger.debug(" About to send Msg is " + fixString );
	System.out.println(sessionMgr == null );
	sessionMgr.logOutImpactMessage( outSeq,msg);
	byte[] response = writeAndWaitForResponse( msg );
	Logger.debug("Waiting for response");
	ImpactMessage responseMsg = null;
	try
	{
		responseMsg = ImpactMessageFactory.createImpactMessage(
				response.toString().getBytes());
		//inSeq = responseMsg.getMsgSeqNum();
		sessionMgr.logInImpactMessage(inSeq, responseMsg.getBytes());
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}


	return responseMsg;

}
*/

/**
 * method to send heartbeat message
 */
public void sendHeartBeat()
{
}
/**
 * method to send fix logout message
 * @param msg
 * @param nextExpSeqNum
 */
public void sendLogout(LogoutRequest msg)
{
	sendMessage(msg);
	/*
	FixLogout logout = new FixLogout();
	setHeaderFields(logout);
	logout.setText(msg);
	if(getExchange().equals("CME"))
		logout.setNextExpectedMsgSeqNum(nextExpSeqNum);
	sendMessage( logout );
	*/
}

/**
 * method to send a fix message
 * @param msg
 * @param seqNum
 * @return
 */
public int sendResetLogonMessage(LoginRequest msg) 
{
	
	synchronized(sendMessageLock)
	{
		msg.Version = MessageUtil.toRawChars(impactVersion,msg.Version.length);
		
		byte[] msgBytes = msg.serialize().array();
		
		Logger.debug(" About to send Msg is " + msg.toString() );
		sequenceReset();
		//inSeq = seqNum;
		outSeq = 1;
		sessionMgr.logOutImpactMessage( outSeq,msgBytes);
		outQueue.enqueue(msgBytes);
	}
	return 1;


}
/**
 * method to send a fix message
 * @param msg
 * @param seqNum
 * @return
 */
public int sendMessage(Request msg)
{
	// This needs to translate msg to FIX String
	// Prepend required header tags
	// compute checksum

	// set sequence number

	int seqNum = 0;
	synchronized(sendMessageLock)
	{
		seqNum = incrementOutSequenceNumber();
		setHeaderFields(msg);
		byte[] msgBytes = msg.serialize().array();		
		sessionMgr.logOutImpactMessage( seqNum,msgBytes);
		outQueue.enqueue(msgBytes);
	}
	return seqNum;


}

/**
 * method to send security definition request
 * used in ICE
 * @param req
 */
/*
public void sendSecurityDefinitionRequest(ProductDefinitionRequest req)
{
	Logger.debug("Sending SecurityDefinitionRequest");
	setHeaderFields(req);
	sendMessage(req);
}
*/
/**
 * method to send market data request message
 * used in ICE
 * @param req
 */
/*
public void sendMarketDataRequest(FixMarketDataRequest req)
{
	Logger.debug("Sending MarketData Request");
	setHeaderFields(req);
	sendMessage(req);
}
*/

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
	public static String getImpactVersion() {
		return impactVersion;
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
private void ignoreInboundMsg(byte[] msg)
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
public void setHeaderFields(Request impMsg)
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



 	    String inFile = ConfigurationService.getValue("ImpactInLogFile");

		FileInputStream stream= new FileInputStream(inFile);
		Logger.debug("File is " + inFile);


 	    byte[] msg = ImpactSessionManager.getApplicationMessages(stream);


 	     // then process in list
 	    while(msg != null)
	    {
	    	Logger.debug("Restoring");
	    	Logger.debug("Restoring - "+(char)msg[0]);

	    	processApplicationMessage(msg);


	   		msg = ImpactSessionManager.getApplicationMessages(stream);

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
				
					Logger.debug("Sleeping for 3 seconds");

					Thread.sleep(3000);
	    	}
			catch(Throwable e)
			{
				e.printStackTrace();
			}					
				




 	     //String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     //ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("ImpactInLogFile");
 	     ArrayList inList = ImpactSessionManager.getApplicationMessagesList(inFile);




 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	Logger.debug("Restoring");
    		byte[] msg = (byte[])inList.get(i);
	    	//String msgType = msg.getMessageType();
	    	Logger.debug("Restoring - "+msg[0]);
	    	processApplicationMessage(msg);

	    }





    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
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
}