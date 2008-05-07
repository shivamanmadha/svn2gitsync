package com.exsys.fix.message;

/**
* This class is used to represent the FIX message HeartBeat
*
*/
public class FixHeartBeat extends FixMessage {
	/**
	* Constructor to construct FixHeartBeat object 
	*
	*/
	public FixHeartBeat() {
		setMessageType(FixConstants.FIX_MSGTYPE_HEARTBEAT);
		setMsgType(FixConstants.FIX_MSGTYPE_HEARTBEAT);
	}
	/**
	* getter method to get TestReqID
	*
	* @return String - TestReqID
	*/
	public String getTestReqID() {
		return (getHeaderFieldValue(112));
	}
	/**
	* setter method to set TestReqID
	*
	* @param String - TestReqID
	*/
	public void setTestReqID(String _TestReqID) {
		addBodyField(112, _TestReqID);
	}
}
