package com.exsys.mktdata.message;

import java.util.*;
import com.exsys.common.exceptions.*;
/**
 * Insert the type's description here.
 * Creation date: (11/18/01 10:13:09 PM)
 * @author: Administrator
 */
public class RLCMessageFactory
{


/**
 * RLCMessageFactory constructor comment.
 */
public RLCMessageFactory() {
	super();

}
/**
 * Insert the method's description here.
 * Creation date: (11/18/01 10:13:49 PM)
 * @return com.exsys.fix.message.RLCMessage
 * @param msg byte[]
 */
public static RLCMessage createRLCMessage(byte[] msg)
		throws RLCProtocolError
{


	// we need to construct specific type of message
	RLCMessage rlcMsg = null;

	String msgType = null;

	// first get message type
	msgType = new String(msg, 33, 2);
	if(msgType == null || msgType.trim().length() < 2)
	{
		throw new RLCProtocolError("Invalid Message Length");
	}
	if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M0))
	{
		rlcMsg = new RLCMZeroMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M9))
	{
		rlcMsg = new RLCM9Message(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MG))
	{
		rlcMsg = new RLCMGMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MH))
	{
		rlcMsg = new RLCMHMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MJ))
	{
		rlcMsg = new RLCMJMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MI))
	{
		rlcMsg = new RLCMIMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MK))
	{
		rlcMsg = new RLCMKMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_ML))
	{
		rlcMsg = new RLCMLMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MM))
	{
		rlcMsg = new RLCMMMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MO))
	{
		rlcMsg = new RLCMOMessage(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MU))
	{
		//rlcMsg = new RLCMUMessage(msg);
		throw new RLCProtocolError("Not Implemented Yet");
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M4))
	{
		rlcMsg = new RLCM4Message(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MA))
	{
		rlcMsg = new RLCMAMessage(msg);
		
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MX))
	{
		rlcMsg = new RLCMXMessage(msg);
		
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MY))
	{
		rlcMsg = new RLCMYMessage(msg);
		
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M5))
	{
		rlcMsg = new RLCM5Message(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M6))
	{
		rlcMsg = new RLCM6Message(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M7))
	{
		rlcMsg = new RLCM7Message(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M8))
	{
		rlcMsg = new RLCM8Message(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_09))
	{
		rlcMsg = new RLC09Message(msg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_23))
	{
		rlcMsg = new RLC23Message(msg);
	}
	else
	{
		System.out.println("UNKNOWN MSG TYPE = " + msgType);
		throw new RLCProtocolError("UNKNOWN MSG TYPE. Not Implemented Yet");
		//rlcMsg = new RLCMessage(msg);
	}
	return rlcMsg;

}

}
