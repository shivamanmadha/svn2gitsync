package com.exsys.fix.session;



import com.exsys.common.exceptions.*;

import com.exsys.service.Logger;
import com.exsys.fix.message.*;
import com.exsys.service.ConfigConstants;
import com.exsys.service.ConfigurationService;

/**
 * @author kreddy
 *
 * This class provides functionality necessary 
 * for the fix client
 */
public class FixClientSession extends FixSession
{
	private int port=0;
	private String host;

	private java.lang.String senderCompID;
	private java.lang.String targetCompID;
	private java.lang.String sessionID;
	private java.lang.String firmID;
	private java.lang.String fti;
	private java.lang.String senderSubID;
	private java.lang.String senderLocationID;
	private java.lang.String targetSubID;
	private java.lang.String password;
	private long agedOrderLimit = 0;
	private boolean isServerPrimary = true;

	private java.lang.String userName;// specific to ICE
	private java.lang.String traderName;// specific to ICE
	private java.lang.String traderpw;//specific to ICE
	private int secReqID = 1;
	
	private boolean logoutSent = false;

/**
 * FixClientSession constructor
 * @param playback 
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public FixClientSession(boolean playback)
		throws ConfigAttributeNotFound, 
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	//initializeConfig();
}

	/**
	 * FixClientSession constructor
	 * @param newHost
	 * @param newPort
	 * @throws ConfigAttributeNotFound
	 * @throws ConfigAttributeInvalid
	 * @throws SystemException
	 */
	public FixClientSession( String newHost, int newPort ) 
						throws ConfigAttributeNotFound, 
								ConfigAttributeInvalid,
								SystemException
	{
		super( newHost, newPort );


	    	sessionID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SESSION_ID);
	    	firmID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_FIRM_ID);
	    	fti = ConfigurationService.getValue(ConfigConstants.FIXCOMMON_FTI);
	    	String invalidMsg = null;
	    	if(sessionID.length() != 3)
	    	{
			    invalidMsg = "INVALID SESSION ID - Length must be 3";			    
	    	}
	    	else if(firmID.length() != 3)
	    	{
	    		invalidMsg = "INVALID FIRM ID- Length must be 3";
	    	}
	 	    else if(fti.length() != 1 || !(fti.equals("U")||fti.equals("N")))
	    	{
	 	    	invalidMsg = "INVALID FTI- Length must be 1 and value must be U or N";			  
	    	}
	    	if(invalidMsg != null)
	    	{
				 ConfigAttributeInvalid ain = 
		 			new ConfigAttributeInvalid( invalidMsg,invalidMsg );
				 throw ain;	    		
	    	}

			// if the FTI is N then we do not support fault tolerance
			// if the FTI is U then we wait for the server to tell us
			// if this will be primary channel or backup channel
			
			String serverFlag = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SERVER_PRIMARY_FLAG);			
	    	
	    	if(serverFlag.equals("N"))
	    	{
	    		isServerPrimary = false;
	    	}
	    	String value = ConfigurationService.getValue(
    						ConfigConstants.FIXCLIENT_AGEDORDER_LIMIT_IN_SECONDS);	   	
 		   	agedOrderLimit = Integer.parseInt(value);
	    	

			if(getExchange().equals("CME"))
			{
	  		  	senderCompID = sessionID + firmID + fti;
			}
			else
			{
				senderCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SENDER_COMP_ID);
			}

	    	senderSubID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SENDER_SUB_ID);
	    	senderLocationID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SENDER_LOCATION_ID);
	    	targetSubID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_TARGET_SUB_ID);
	    	password = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_PASSWORD);
		   	Logger.debug("Sender Comp ID is " + senderCompID );
	    	targetCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_TARGET_COMP_ID);
		   	Logger.debug("Target Comp ID is " + targetCompID );

			// only for ICE
			if(getExchange().equals("ICE"))
			{
				userName = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_USERNAME);
				traderName = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_TRADERNAME);
				traderpw = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_TRADER_PASSWORD);
			}

		host = newHost;
		port = newPort;
		initializeFixSession();

	}

/**
 * method to check if server is primary
 * @return
 */
public boolean isServerPrimary()
{
	return isServerPrimary == true;
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#getSenderCompID()
 */
public java.lang.String getSenderCompID() {
	return senderCompID;
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#getTargetCompID()
 */
public java.lang.String getTargetCompID() {
	return targetCompID;
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#initializeFixSession()
 */
public boolean initializeFixSession()
{

	// we need to send Logon Message
	// construct FixSessionContext
	super.initializeFixSession();
	
	// initialize fix session manager if NOT participating in fault tolerance
	if(getFti().equals("N") || isServerPrimary())
	{
		initializeSessionManager();
	}
	else
	{
		setPrimary(false);
	}
	setInitialized(true);
	return true;
}

// MAY BE THIS METHOD SHOULD THROW EXCEPTION
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processLogonMessage(com.exsys.fix.message.FixMessage, boolean)
 */
public boolean processLogonMessage(FixMessage logon, boolean normal) 
{
	// in case if this is participating in fault tolerance
	// fix client session should interrogate target compid
	// to determine if primary or backup
	if(getExchange().equals("CME"))
	{
		String id = logon.getTargetCompID();
		String lastChar = id.substring(id.length()-1);
		if(lastChar.equals("N"))
		{
			// client is not participating in fault tolerance
			// no need to do any thing
		}
		else 
		{
			senderCompID = sessionID + firmID + lastChar;
			if( lastChar.equals("P") )
			{
				setPrimary(true);
			}
			else
			{
				setPrimary(false);
			}
		}
	
	}
	
	logonSuccessful();
	return true;
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#switchToPrimary()
 */
public void switchToPrimary()
{
	super.switchToPrimary();
	senderCompID =  senderCompID.substring(0,senderCompID.length()-1)+"P";
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#switchToBackup()
 */
public void switchToBackup()
{
	super.switchToBackup();
}



/**
 * method to send a logon message
 */
public void sendLogonMessage()
{


	FixLogon logon = new FixLogon();
	setHeaderFields(logon);
	logon.setRawDataLength(getPassword().length());
	logon.setRawData(getPassword());
	logon.setHeartBtInt(getHeartBeatInSeconds());
	logon.setEncryptMethod(0);
	// if exchange is ICE, then set UserName
	if(getExchange().equals("ICE"))
	{
		logon.setUserName(userName);
		logon.setResetSeqNumFlag(false);
	}
	//04082007 - open fix does not like tag 141 - commenting
	//logon.setResetSeqNumFlag(false);
	
	// just send instead of sendAndGetResponse
	sendMessage(logon);



}

/**
 * method to send a logon message
 */
public void sendResetLogonMessage()
{


	FixLogon logon = new FixLogon();
	setHeaderFields(logon);
	logon.setRawDataLength(getPassword().length());
	logon.setRawData(getPassword());
	logon.setHeartBtInt(getHeartBeatInSeconds());
	logon.setEncryptMethod(0);
	// if exchange is ICE, then set UserName
	if(getExchange().equals("ICE"))
	{
		logon.setUserName(userName);
		logon.setResetSeqNumFlag(true);
	}
	//04082007 - open fix does not like tag 141 - commenting
	//logon.setResetSeqNumFlag(false);
	
	// just send instead of sendAndGetResponse
	sendResetLogonMessage(logon,1);



}
//ICE ONLY METHOD
/**
 * method to send fix trader logon 
 */
public void sendTraderLogonMessage()
{


	FixTraderLogon logon = new FixTraderLogon();
	setHeaderFields(logon);	
	//logon.setRawData(getPassword());
	logon.setRawData(traderpw);
	logon.setUserName(traderName);
	logon.setClientId(getSenderCompID());
	sendMessage(logon);

}

//ICE ONLY METHOD
/**
 * method to send security definition request
 * @param secType
 */
public void sendSecurityDefinitionRequest(String secType)
{


	FixSecurityDefinitionRequest sec = new FixSecurityDefinitionRequest();
	setHeaderFields(sec);	
	sec.setSecurityReqID("SECREQ"+secReqID++);
	sec.setSecurityRequestType("3");
	sec.setSecurityType(secType);
	sendMessage(sec);

}

//ICE ONLY METHOD
/**
 * method to send trader logout message
 */
public void sendTraderLogoutMessage()
{


	FixTraderLogout logout = new FixTraderLogout();
	setHeaderFields(logout);	
	logout.setUserName(traderName);
	logout.setClientId(getSenderCompID());
	sendMessage(logout);

}

/**
 * method to send fix logout message
 * @param seq
 */
public void sendLogoutMessage(int seq)
{

	logoutSent = true;
	FixLogout logout = new FixLogout();
	setHeaderFields(logout);
	if(getExchange().equals("CME"))	
	{
		logout.setNextExpectedMsgSeqNum(getInSequenceNumber()+1);
	}
	sendMessage(logout,seq);



}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processLogoutMessage(java.lang.String, int)
 */
public void processLogoutMessage(String text, int nextExpSeq)
{

	
	if( logoutSent )
	{
		logoutSent = false;
		// this must be logout confirmation
		String msg = "Logout Confirm Received from Server - " + text;
		Logger.debug(msg);
		debug(msg);		
		shutdownSession();
		// after shutting down, stop listening for application messages
	}
	else
	{
		// this must be an error
  	    // NOT SURE IF CLIENT NEED TO SEND LOGOUT CONFIRM
		// we can just shut down session
		Logger.error(" Unsolicited Logout Received from Server - " + text);
		//send logout
		sendLogoutMessage(0);
		shutdownSession();		
	}
	 	
		
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#isAgedMessage(java.lang.String, com.exsys.fix.message.FixMessage)
 */
public boolean isAgedMessage(String msgType,FixMessage msg)
{
	if(msgType.equals(FixConstants.FIX_MSGTYPE_ORDER) 
	|| msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
	{
		if(agedOrderLimit != 0 
			&& msg.getMessageAgeInMillis()/1000 > agedOrderLimit )
		{
			return true;
		}
	}
	
	return false;
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#setHeaderFields(com.exsys.fix.message.FixMessage)
 */
public void setHeaderFields(FixMessage fixMsg)
{
	fixMsg.setSenderCompID(getSenderCompID());
	fixMsg.setSenderSubID(getSenderSubID());
	fixMsg.setSendingTime(FixMessage.getUTCCurrentTime());
	fixMsg.setTargetCompID(targetCompID);
	// ICE does not need the following fields
	if(!getExchange().equals("ICE"))	
	{
		fixMsg.setSenderLocationID(getSenderLocationID());
		fixMsg.setTargetSubID(getTargetSubID());
		fixMsg.setLastMsgSeqNumProcessed(getInSequenceNumber());
	}

}

/**
 * setter method
 * @param newSenderCompID
 */
public void setSenderCompID(java.lang.String newSenderCompID) {
	senderCompID = newSenderCompID;
}

/**
 * @param newTargetCompID
 */
public void setTargetCompID(java.lang.String newTargetCompID) {
	targetCompID = newTargetCompID;
}

	/**
	 * @return
	 */
	public java.lang.String getFirmID() {
		return firmID;
	}

	/**
	 * @param firmID
	 */
	public void setFirmID(java.lang.String firmID) {
		this.firmID = firmID;
	}

	/**
	 * @return
	 */
	public java.lang.String getFti() {
		return fti;
	}

	/**
	 * @param fti
	 */
	public void setFti(java.lang.String fti) {
		this.fti = fti;
	}

	/**
	 * @return
	 */
	public java.lang.String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#getSenderLocationID()
	 */
	public java.lang.String getSenderLocationID() {
		return senderLocationID;
	}

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#setSenderLocationID(java.lang.String)
	 */
	public void setSenderLocationID(java.lang.String senderLocationID) {
		this.senderLocationID = senderLocationID;
	}

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#getSenderSubID()
	 */
	public java.lang.String getSenderSubID() {
		return senderSubID;
	}

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#setSenderSubID(java.lang.String)
	 */
	public void setSenderSubID(java.lang.String senderSubID) {
		this.senderSubID = senderSubID;
	}

	/**
	 * @return
	 */
	public java.lang.String getSessionID() {
		return sessionID;
	}

	/**
	 * @param sessionID
	 */
	public void setSessionID(java.lang.String sessionID) {
		this.sessionID = sessionID;
	}

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#getTargetSubID()
	 */
	public java.lang.String getTargetSubID() {
		return targetSubID;
	}

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#setTargetSubID(java.lang.String)
	 */
	public void setTargetSubID(java.lang.String targetSubID) {
		this.targetSubID = targetSubID;
	}
	
	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#isServerSession()
	 */
	public boolean isServerSession()
	{
		return false;
	}
	
}
