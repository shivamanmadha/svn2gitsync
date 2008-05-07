package com.exsys.fix.message;
/**
* This class is used to represent the FIX message ResendRequest
*
*/
public class FixResendRequest extends FixMessage {

	/**
	* setter method to set BeginSeqNo
	*
	* @param int - BeginSeqNo
	*/
	public void setBeginSeqNo(int _BeginSeqNo) {
		addBodyField(7, getString(_BeginSeqNo));
	}
	/**
	* setter method to set BeginSeqNo
	*
	* @param String - BeginSeqNo
	*/
	public void setBeginSeqNo(String _BeginSeqNo) {
		addBodyField(7, _BeginSeqNo);
	}
	/**
	* getter method to get BeginSeqNo
	*
	* @return int - BeginSeqNo
	*/
	public int getBeginSeqNo() {
		return (stringToint(getBodyFieldValue(7)));
	}
	/**
	* getter method to get BeginSeqNoAsString
	*
	* @return String - BeginSeqNoAsString
	*/
	public String getBeginSeqNoAsString() {
		return (getBodyFieldValue(7));
	}
	/**
	* setter method to set EndSeqNo
	*
	* @param int - EndSeqNo
	*/
	public void setEndSeqNo(int _EndSeqNo) {
		addBodyField(16, getString(_EndSeqNo));
	}
	/**
	* setter method to set EndSeqNo
	*
	* @param String - EndSeqNo
	*/
	public void setEndSeqNo(String _EndSeqNo) {
		addBodyField(16, _EndSeqNo);
	}
	/**
	* getter method to get EndSeqNo
	*
	* @return int - EndSeqNo
	*/
	public int getEndSeqNo() {
		return (stringToint(getBodyFieldValue(16)));
	}
	/**
	* getter method to get EndSeqNoAsString
	*
	* @return String - EndSeqNoAsString
	*/
	public String getEndSeqNoAsString() {
		return (getBodyFieldValue(16));
	}

	/**
	* Constructor to construct FixResendRequest object 
	*
	*/
	public FixResendRequest() {
		setMessageType(FixConstants.FIX_MSGTYPE_RESENDREQUEST);
		setMsgType(FixConstants.FIX_MSGTYPE_RESENDREQUEST);
	}
}
