package com.exsys.fix.message;

/**
* This class is used to represent the FIX message TraderLogout
*
*/
public class FixTraderLogout extends FixMessage {

	/**
	* Constructor to construct FixTraderLogout object 
	*
	*/
	public FixTraderLogout() {
		setMessageType(FixConstants.FIX_MSGTYPE_TRADER_LOGOUT);
		setMsgType(FixConstants.FIX_MSGTYPE_TRADER_LOGOUT);
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
