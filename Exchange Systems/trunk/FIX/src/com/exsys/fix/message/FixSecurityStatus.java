package com.exsys.fix.message;

/**
* This class is used to represent the FIX message SecurityStatus
*
*/
public class FixSecurityStatus extends FixMessage {
	/**
	* Constructor to construct FixSecurityStatus object 
	*
	*/
	public FixSecurityStatus() {
		setMessageType(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS);
		setMsgType(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS);

	}
	/**
	* setter method to set Symbol
	*
	* @param String - Symbol
	*/
	public void setSymbol(String _Symbol) {
		addBodyField(55, _Symbol);
	}
	/**
	* getter method to get Symbol
	*
	* @return String - Symbol
	*/
	public String getSymbol() {
		return (getBodyFieldValue(55));
	}
	/**
	* setter method to set SecurityTradingStatus
	*
	* @param int - SecurityTradingStatus
	*/
	public void setSecurityTradingStatus(int _SecurityTradingStatus) {
		addBodyField(326, getString(_SecurityTradingStatus));
	}
	/**
	* setter method to set SecurityTradingStatus
	*
	* @param String - SecurityTradingStatus
	*/
	public void setSecurityTradingStatus(String _SecurityTradingStatus) {
		addBodyField(326, _SecurityTradingStatus);
	}
	/**
	* getter method to get SecurityTradingStatus
	*
	* @return int - SecurityTradingStatus
	*/
	public int getSecurityTradingStatus() {
		return (stringToint(getBodyFieldValue(326)));
	}
	/**
	* getter method to get SecurityTradingStatusAsString
	*
	* @return String - SecurityTradingStatusAsString
	*/
	public String getSecurityTradingStatusAsString() {
		return (getBodyFieldValue(326));
	}

}
