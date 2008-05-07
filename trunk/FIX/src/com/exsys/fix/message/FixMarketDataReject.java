package com.exsys.fix.message;

/**
* This class is used to represent the FIX message ketDataReject
*
*/
public class FixMarketDataReject extends FixMessage {
	/**
	* Constructor to construct MarketDataReject object 
	*
	*/
	public FixMarketDataReject() {
		setMessageType(FixConstants.FIX_MSGTYPE_MARKETDATA_REJECT);
		setMsgType(FixConstants.FIX_MSGTYPE_MARKETDATA_REJECT);
	}
	/**
	* setter method to set MDReqID
	*
	* @param String - MDReqID
	*/
	public void setMDReqID(String _MDReqID) {
		addBodyField(262, _MDReqID);
	}
	/**
	* getter method to get MDReqID
	*
	* @return String - MDReqID
	*/
	public String getMDReqID() {
		return (getBodyFieldValue(262));
	}
	/**
	* setter method to set MDReqRejReason
	*
	* @param String - MDReqRejReason
	*/
	public void setMDReqRejReason(String _MDReqRejReason) {
		addBodyField(281, _MDReqRejReason);
	}
	/**
	* getter method to get MDReqRejReason
	*
	* @return String - MDReqRejReason
	*/
	public String getMDReqRejReason() {
		return (getBodyFieldValue(281));
	}
	/**
	* setter method to set Text
	*
	* @param String - Text
	*/
	public void setText(String _Text) {
		addBodyField(55, _Text);
	}
	/**
	* getter method to get Text
	*
	* @return String - Text
	*/
	public String getText() {
		return (getBodyFieldValue(55));
	}

}
