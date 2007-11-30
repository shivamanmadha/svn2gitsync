package com.exsys.fix.message;

//05152007 - added tag 553 (username) per ICE

/**
* This class is used to represent the FIX message TraderLogon
*
*/
public class FixTraderLogon extends FixMessage {
	/**
	* Constructor to construct FixTraderLogon object 
	*
	*/
	public FixTraderLogon() {
		setMsgType(FixConstants.FIX_MSGTYPE_TRADER_LOGON);
		setMessageType(FixConstants.FIX_MSGTYPE_TRADER_LOGON);
	}

	/**
	* getter method to get RawData
	*
	* @return String - RawData
	*/
	public String getRawData() {
		return (getBodyFieldValue(96));
	}
	// ICE specific method
	/**
	* getter method to get UserName
	*
	* @return String - UserName
	*/
	public String getUserName() {
		return (getBodyFieldValue(553));
	}
	/**
	* getter method to get ClientId
	*
	* @return String - ClientId
	*/
	public String getClientId() {
		return (getBodyFieldValue(109));
	}

	/**
	* setter method to set RawData
	*
	* @param String - RawData
	*/
	public void setRawData(String _RawData) {
		addBodyField(96, _RawData);
	}
	// ICE specific method
	/**
	* setter method to set UserName
	*
	* @param String - UserName
	*/
	public void setUserName(String _UserName) {
		addBodyField(553, _UserName);
	}
	/**
	* setter method to set ClientId
	*
	* @param String - ClientId
	*/
	public void setClientId(String _ClientId) {
		addBodyField(109, _ClientId);
	}
}
