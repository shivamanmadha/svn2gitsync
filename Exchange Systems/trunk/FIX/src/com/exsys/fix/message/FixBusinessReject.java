package com.exsys.fix.message;

/**
 * This class is used to represent the FIX business reject ( msg type = j)
 */
public class FixBusinessReject extends FixMessage {
	/**
	 * Constructor to construct FixBusinessReject object
	 *
	 */

	public FixBusinessReject() {
		setMessageType(FixConstants.FIX_MSGTYPE_BUSINESS_REJECT);
		setMsgType(FixConstants.FIX_MSGTYPE_BUSINESS_REJECT);
	}
  /**
    * getter method to return EncodedText     
    * @return    String - EncodedText.
    */
	public String getEncodedText() {
		return (getHeaderFieldValue(355));
	}
  /**
    * getter method to return EncodedTextLen     
    * @return    int - EncodedTextLen.
    */	
	public int getEncodedTextLen() {
		return (stringToint(getBodyFieldValue(354)));
	}
  /**
    * getter method to return EncodedTextLen     
    * @return    String - EncodedTextLen.
    */	
	public String getEncodedTextLenAsString() {
		return (getBodyFieldValue(354));
	}
  /**
    * getter method to return RefMsgTType     
    * @return    String - RefMsgTType.
    */	
	public String getRefMsgType() {
		return (getHeaderFieldValue(372));
	}
  /**
    * getter method to return BusinessRejectRefID     
    * @return    String - BusinessRejectRefID.
    */	
	public String getBusinessRejectRefID() {
		return ((getBodyFieldValue(379)));
	}
  /**
    * getter method to return RefSeqNum     
    * @return    int - RefSeqNum.
    */
	public int getRefSeqNum() {
		return (stringToint(getBodyFieldValue(45)));
	}
  /**
    * getter method to return RefSeqNum     
    * @return    String - RefSeqNum.
    */	
	public String getRefSeqNumAsString() {
		return (getBodyFieldValue(45));
	}
  /**
    * getter method to return SessionRejectReason     
    * @return    String - SessionRejectReason.
    */	
	public String getSessionRejectReason() {
		return (getBodyFieldValue(373));
	}
  /**
    * getter method to return Text     
    * @return    String - Text.
    */	
	public String getText() {
		return (getBodyFieldValue(58));
	}
	
  /**
    * setter method to set BusinessRejectReason     
    * @param    int - BusinessRejectReason.
    */	
	public void setBusinessRejectReason(int _BusinessRejectReason) {
		addBodyField(380, getString(_BusinessRejectReason));
	}
	
  /**
    * setter method to set BusinessRejectReason     
    * @param    int - BusinessRejectReason.
    */		
	public void setBusinessRejectReason(String _BusinessRejectReason) {
		addBodyField(380, _BusinessRejectReason);
	}
	
  /**
    * getter method to return BusinessRejectReason     
    * @return    int - BusinessRejectReason.
    */		
	public int getBusinessRejectReason() {
		return (stringToint(getBodyFieldValue(380)));
	}
	
  /**
    * getter method to return BusinessRejectReason     
    * @return    String - BusinessRejectReason.
    */
	public String getBusinessRejectReasonAsString() {
		return (getBodyFieldValue(380));
	}
	
  /**
    * setter method to set EncodedText     
    * @param    String - EncodedText.
    */
	public void setEncodedText(String _EncodedText) {
		addBodyField(355, _EncodedText);
	}
	
  /**
    * setter method to set EncodedTextLen     
    * @param    int - EncodedTextLen.
    */
	public void setEncodedTextLen(int _EncodedTextLen) {
		addBodyField(354, getString(_EncodedTextLen));
	}
	
  /**
    * setter method to set EncodedTextLen     
    * @param    String - EncodedTextLen.
    */
	public void setEncodedTextLen(String _EncodedTextLen) {
		addBodyField(354, _EncodedTextLen);
	}
	
  /**
    * setter method to set RefMsgType     
    * @param    String - RefMsgType.
    */
	public void setRefMsgType(String _RefMsgType) {
		addBodyField(372, _RefMsgType);
	}
	
  /**
    * setter method to set BusinessRejectRefID     
    * @param    String - BusinessRejectRefID.
    */
	public void setBusinessRejectRefID(String _BusinessRejectRefID) {
		addBodyField(379, (_BusinessRejectRefID));
	}
	
  /**
    * setter method to set RefSeqNum     
    * @param    int - RefSeqNum.
    */
	public void setRefSeqNum(int _RefSeqNum) {
		addBodyField(45, getString(_RefSeqNum));
	}
	
  /**
    * setter method to set RefSeqNum     
    * @param    String - RefSeqNum.
    */
	public void setRefSeqNum(String _RefSeqNum) {
		addBodyField(45, _RefSeqNum);
	}
	
	
  /**
    * setter method to set Text     
    * @param    String - Text.
    */
	public void setText(String _Text) {
		addBodyField(58, _Text);
	}
}