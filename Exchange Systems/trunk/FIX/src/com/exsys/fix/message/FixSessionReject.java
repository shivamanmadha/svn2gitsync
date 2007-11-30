package com.exsys.fix.message;

/**
* This class is used to represent the FIX message SessionReject
*
*/
public class FixSessionReject extends FixMessage {

	/**
	* Constructor to construct FixSessionReject object 
	*
	*/
	public FixSessionReject() {
		setMessageType(FixConstants.FIX_MSGTYPE_REJECT);
		setMsgType(FixConstants.FIX_MSGTYPE_REJECT);
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
	* getter method to get EncodedTextLen
	*
	* @return int - EncodedTextLen
	*/
	public int getEncodedTextLen() {
		return (stringToint(getBodyFieldValue(354)));
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
	* getter method to get RefMsgTType
	*
	* @return String - RefMsgTType
	*/
	public String getRefMsgTType() {
		return (getHeaderFieldValue(372));
	}
	/**
	* getter method to get RefTagID
	*
	* @return String - RefTagID
	*/
	public String getRefTagID() {
		return ((getBodyFieldValue(371)));
	}
	/**
	* getter method to get RefTagIDAsString
	*
	* @return String - RefTagIDAsString
	*/
	public String getRefTagIDAsString() {
		return (getBodyFieldValue(371));
	}
	/**
	* getter method to get RefSeqNum
	*
	* @return int - RefSeqNum
	*/
	public int getRefSeqNum() {
		return (stringToint(getBodyFieldValue(45)));
	}
	/**
	* getter method to get RefSeqNumAsString
	*
	* @return String - RefSeqNumAsString
	*/
	public String getRefSeqNumAsString() {
		return (getBodyFieldValue(45));
	}
	/**
	* getter method to get SessionRejectReason
	*
	* @return String - SessionRejectReason
	*/
	public String getSessionRejectReason() {
		return (getBodyFieldValue(373));
	}
	/**
	* getter method to get Text
	*
	* @return String - Text
	*/
	public String getText() {
		return (getBodyFieldValue(58));
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
	* setter method to set EncodedTextLen
	*
	* @param int - EncodedTextLen
	*/
	public void setEncodedTextLen(int _EncodedTextLen) {
		addBodyField(354, getString(_EncodedTextLen));
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
	* setter method to set RefMsgTType
	*
	* @param String - RefMsgTType
	*/
	public void setRefMsgTType(String _RefMsgTType) {
		addBodyField(372, _RefMsgTType);
	}
	/**
	* setter method to set RefTagID
	*
	* @param String - RefTagID
	*/
	public void setRefTagID(String _RefTagID) {
		addBodyField(371, (_RefTagID));
	}
	/**
	* setter method to set RefSeqNum
	*
	* @param int - RefSeqNum
	*/
	public void setRefSeqNum(int _RefSeqNum) {
		addBodyField(45, getString(_RefSeqNum));
	}
	/**
	* setter method to set RefSeqNum
	*
	* @param String - RefSeqNum
	*/
	public void setRefSeqNum(String _RefSeqNum) {
		addBodyField(45, _RefSeqNum);
	}
	/**
	* setter method to set SessionRejectReason
	*
	* @param String - SessionRejectReason
	*/
	public void setSessionRejectReason(String _SessionRejectReason) {
		addBodyField(373, _SessionRejectReason);
	}
	/**
	* setter method to set Text
	*
	* @param String - Text
	*/
	public void setText(String _Text) {
		addBodyField(58, _Text);
	}
}
