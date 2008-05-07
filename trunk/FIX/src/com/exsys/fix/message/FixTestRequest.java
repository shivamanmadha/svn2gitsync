package com.exsys.fix.message;
/**
* This class is used to represent the FIX message TestRequest
*
*/
public class FixTestRequest extends FixMessage {
	/**
	* Constructor to construct FixTestRequest object 
	*
	*/
	public FixTestRequest() {
		setMessageType(FixConstants.FIX_MSGTYPE_TESTREQUEST);
		setMsgType(FixConstants.FIX_MSGTYPE_TESTREQUEST);
	}
	/**
	* setter method to set TestReqID
	*
	* @param int - TestReqID
	*/
	public void setTestReqID(int _TestReqID) {
		addBodyField(112, getString(_TestReqID));
	}
	/**
	* setter method to set TestReqID
	*
	* @param String - TestReqID
	*/
	public void setTestReqID(String _TestReqID) {
		addBodyField(112, _TestReqID);
	}
	/**
	* getter method to get TestReqID
	*
	* @return int - TestReqID
	*/
	public int getTestReqID() {
		return (stringToint(getBodyFieldValue(112)));
	}
	/**
	* getter method to get TestReqIDAsString
	*
	* @return String - TestReqIDAsString
	*/
	public String getTestReqIDAsString() {
		return (getBodyFieldValue(112));
	}

}
