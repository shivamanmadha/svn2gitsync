package com.exsys.fix.message;


/**
 * Insert the type's description here.
 * Creation date: (11/18/01 10:13:09 PM)
 * @author: Administrator
 */
public class FixMessageValidation
{
/**
 * FixMessageValidation constructor comment.
 */
public FixMessageValidation() {
	super();

}
/**
 * Insert the method's description here.
 * Creation date: (11/18/01 10:13:49 PM)
 * @return com.exsys.fix.message.FixMessage
 * @param msg byte[]
 */
public static FixSessionReject validateFixMessage(byte[] raw,FixMessage msg)

{


	// we need to construct specific type of message
	FixSessionReject sessionReject = null;
	String rejectReason = null;
	String reasonCode = null;
	
	/*if(!checkChecksum(msg))
	{
		rejectReason = "InvalidChecksum";
		reasonCode = "10";
	}
	else if(!checkBodyLength(msg))
	{
		rejectReason = "Invalid Body Length";
		reasonCode = "10";
	}		
	else */if(!checkMissingOrigSendingTime(msg))
	{
		rejectReason = "Original Sending Time is missing";
		reasonCode = "1";
	}	
	else if(!checkOrigSendingTime(msg))
	{
		rejectReason = "Original Sending Time is less than Sending Time";
		reasonCode = "10";
	}
	
	


	if(rejectReason != null)
	{
		sessionReject = new FixSessionReject();
	
	    sessionReject.setRefSeqNum(msg.getMsgSeqNumAsString());
	    sessionReject.setText(rejectReason);
	    if(reasonCode != null)
		    sessionReject.setSessionRejectReason(reasonCode);	
		
	}

	return sessionReject;

}
public static boolean checkChecksum(FixMessage msg)
{
	return (msg.getCheckSum().equals(msg.getCalculatedChecksum()));

}
public static boolean checkBodyLength(FixMessage msg)
{
	return (msg.getBodyLength() == msg.getCalculatedBodyLength());

}


public static boolean checkMissingOrigSendingTime(FixMessage msg)
{
	if(msg.getMsgType().equals(FixConstants.FIX_MSGTYPE_SEQUENCERESET))
		return true;
		
	return (!msg.getPossDupFlag()
			|| msg.getOrigSendingTimeAsString() != null);

}
public static boolean checkOrigSendingTime(FixMessage msg)
{
	if(msg.getMsgType().equals(FixConstants.FIX_MSGTYPE_SEQUENCERESET))
		return true;
	
	return (!msg.getPossDupFlag() ||
			 msg.getOrigSendingTime().before(msg.getSendingTime()));

}
}
