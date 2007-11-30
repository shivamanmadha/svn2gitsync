package com.exsys.fix.message;

/**
* This class is used to represent the FIX message Logout
*
*/
public class FixLogout extends FixMessage {

	/**
	* setter method to set Text
	*
	* @param String - Text
	*/
	public void setText(String _Text) {
		addBodyField(58, _Text);
	}
	/**
	* getter method to get Text
	*
	* @return String - Text
	*/
	public String getText() {
		return (getHeaderFieldValue(58));
	}
	/**
	* setter method to set EncodedTextLen
	*
	* @param String - EncodedTextLen
	*/
	public void setEncodedTextLen(String _EncodedTextLen) {
		addBodyField(354, _EncodedTextLen);
	}
	/**
	* getter method to get EncodedTextLenAsString
	*
	* @return String - EncodedTextLenAsString
	*/
	public String getEncodedTextLenAsString() {
		return (getBodyFieldValue(354));
	}
	/**
	* setter method to set EncodedText
	*
	* @param String - EncodedText
	*/
	public void setEncodedText(String _EncodedText) {
		addBodyField(355, _EncodedText);
	}
	/**
	* getter method to get EncodedText
	*
	* @return String - EncodedText
	*/
	public String getEncodedText() {
		return (getHeaderFieldValue(355));
	}
	/**
	* getter method to get NextExpectedMsgSeqNum
	*
	* @return int - NextExpectedMsgSeqNum
	*/
	public int getNextExpectedMsgSeqNum() {
		return (stringToint(getBodyFieldValue(789)));
	}
	/**
	* getter method to get NextExpectedMsgSeqNumAsString
	*
	* @return String - NextExpectedMsgSeqNumAsString
	*/
	public String getNextExpectedMsgSeqNumAsString() {
		return (getBodyFieldValue(789));
	}

	/**
	* Constructor to construct FixLogout object 
	*
	*/
	public FixLogout() {
		setMessageType(FixConstants.FIX_MSGTYPE_LOGOUT);
		setMsgType(FixConstants.FIX_MSGTYPE_LOGOUT);
	}

	/**
	* getter method to get EncodedTextLen
	*
	* @return int - EncodedTextLen
	*/
	public int getEncodedTextLen() {
		return (stringToint(getBodyFieldValue(354)));
	}

	/**
	* setter method to set EncodedTextLen
	*
	* @param int - EncodedTextLen
	*/
	public void setEncodedTextLen(int _EncodedTextLen) {
		addBodyField(354, getString(_EncodedTextLen));
	}
	/**
	* setter method to set NextExpectedMsgSeqNum
	*
	* @param int - MsgSeqNum
	*/
	public void setNextExpectedMsgSeqNum(int _MsgSeqNum) {
		addBodyField(789, getString(_MsgSeqNum));
	}
	/**
	* setter method to set NextExpectedMsgSeqNum
	*
	* @param String - MsgSeqNum
	*/
	public void setNextExpectedMsgSeqNum(String _MsgSeqNum) {
		addBodyField(789, _MsgSeqNum);
	}

}
