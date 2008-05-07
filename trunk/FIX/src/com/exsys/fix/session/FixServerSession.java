package com.exsys.fix.session;

import java.util.*;
import java.io.*;

import com.exsys.common.exceptions.*;
import com.exsys.common.tcpsession.*;
import com.exsys.common.util.*;
import com.exsys.fix.message.*;
import com.exsys.service.ConfigConstants;
import com.exsys.service.ConfigurationService;

/**
 * @author kreddy
 *
 * This class provides functionality necessary for a fix
 * server
 */
public class FixServerSession extends FixSession
{
	private java.lang.String senderCompID;
	private java.lang.String targetCompID;
	private java.lang.String fti;
	private java.lang.String senderSubID;
	private java.lang.String senderLocationID;
	private java.lang.String targetSubID;
	private java.lang.String targetLocationID;
	
	
	

/**
 * FixServerSession constructor
 * @param _socket
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public FixServerSession(java.net.Socket _socket) throws ConfigAttributeNotFound, 
								ConfigAttributeInvalid,
								SystemException
{
	super(_socket);
	
		String invalidMsg = null;
    	fti = ConfigurationService.getValue(ConfigConstants.FIXCOMMON_FTI);
	 	if(fti.length() != 1 || !(fti.equals("P")||fti.equals("B")||fti.equals("N")))
	   	{
	 	   	invalidMsg = "INVALID FTI- Length must be 1 and value must be P,B or N";			  
	   	}
   	
    	if(invalidMsg != null)
	    {
			ConfigAttributeInvalid ain = 
				new ConfigAttributeInvalid( invalidMsg,invalidMsg );
			 throw ain;	    		
		}
		
		// if the server is backup set primary flag to false
		if(fti.equals("B"))
		{
			setPrimary(false);
		}
				
	
	initializeFixSession();

}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processLogonMessage(com.exsys.fix.message.FixMessage, boolean)
 */
public boolean processLogonMessage(FixMessage logon, boolean normal) 
{

	// we need to authenticate and return
	// logon confirmation
	// after authentication, set server side fields
	
	targetCompID = logon.getSenderCompID();
	
	senderCompID = logon.getTargetCompID();
	senderSubID = logon.getTargetSubID();
	targetSubID = logon.getSenderSubID();
	targetLocationID = logon.getSenderLocationID();
	//senderLocationID = logon.getTargetLocationID();
	boolean resetFlag = ((FixLogon)logon).getResetSeqNumFlag();
	int seqNum = logon.getMsgSeqNum();
	
	String clientSenderCompID = targetCompID;
	String password  = ((FixLogon)logon).getRawData();
	
	boolean success = true;
	String errorText = null;
	
	if(getExchange().equals("CME"))
	{
		// validate that length is 7 chars
		if(clientSenderCompID.length() != 7)
		{
			errorText = "Invalid SenderComp ID";
		 	success = false;
		}
		else if( isBOWLogon() && (resetFlag || seqNum != 1))
		{
			// in case of backup channel, seqNum will be zero
			if(isPrimary())
			{		
				errorText = "Failed to reset sequence numbers at";
				errorText += " the beginning of the week. Logout Forced";
				success = false;
			}
		}
		else if ( !password.equals("1234"))
		{
			errorText = "Invalid Password";
			success = false;
		}
		// check the last character of client's sendercompid
		String lastChar = clientSenderCompID.substring(clientSenderCompID.length()-1);
		if(lastChar.equals("N"))
		{
			// client is not participating in fault tolerance
			// no need to do any thing
		}
		else if( lastChar.equals("U") )
		{
			if(isPrimary())
			{
				lastChar = "P";
			}
			else
			{
				lastChar = "B";
			}
			targetCompID = clientSenderCompID.substring
							(0,clientSenderCompID.length()-1)+lastChar;
		}
	}
	if( success )
	{	
		sendLogonResponse();
		// in case of normal ( non missed messages case )
		// send test request
		if(normal)
		{
			sendTestRequest("INITTR");
		}
	}
	else
	{
		// send LOGOUT
		processLogoutMessage(errorText);
	}
	
	return success;
}



/**
 * method to send logon response
 */
public void sendLogonResponse() 
{

	FixLogon logonResponse = new FixLogon();
    setHeaderFields(logonResponse);
	logonResponse.setHeartBtInt(getHeartBeatInSeconds());
	logonResponse.setEncryptMethod(0);

	sendMessage( logonResponse );
	logonSuccessful();

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
	fixMsg.setTargetLocationID(targetLocationID);
	fixMsg.setLastMsgSeqNumProcessed(getInSequenceNumber());
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#initializeFixSession()
 */
public boolean initializeFixSession() 
{

	// we need to send Logon Message
	// construct FixSessionContext
	super.initializeFixSession();
	return false;
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#switchToPrimary()
 */
public void switchToPrimary()
{
	super.switchToPrimary();
	targetCompID =  targetCompID.substring(0,targetCompID.length()-1)+"P";
	sendHeartBeat();
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#switchToBackup()
 */
public void switchToBackup()
{
	super.switchToBackup();
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

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#getSenderCompID()
	 */
	public java.lang.String getSenderCompID() {
		return senderCompID;
	}

	/**
	 * @param senderCompID
	 */
	public void setSenderCompID(java.lang.String senderCompID) {
		this.senderCompID = senderCompID;
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

	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#getTargetCompID()
	 */
	public java.lang.String getTargetCompID() {
		return targetCompID;
	}

	/**
	 * @param targetCompID
	 */
	public void setTargetCompID(java.lang.String targetCompID) {
		this.targetCompID = targetCompID;
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

	/**
	 * @return
	 */
	public java.lang.String getTargetLocationID() {
		return targetLocationID;
	}

	/**
	 * @param targetLocationID
	 */
	public void setTargetLocationID(java.lang.String targetLocationID) {
		this.targetLocationID = targetLocationID;
	}
	/* (non-Javadoc)
	 * @see com.exsys.fix.session.FixSession#isServerSession()
	 */
	public boolean isServerSession()
	{
		return true;
	}	
	
}
