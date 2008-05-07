package com.exsys.fix.message;

/**
* This class is used to represent the FIX message TraderLogonResponse
*
*/
public class FixTraderLogonResponse extends FixMessage {

	/**
	* Constructor to construct FixTraderLogonResponse object 
	*
	*/
	public FixTraderLogonResponse() {
		setMessageType(FixConstants.FIX_MSGTYPE_TRADER_LOGON_RESPONSE);
		setMsgType(FixConstants.FIX_MSGTYPE_TRADER_LOGON_RESPONSE);
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
	* getter method to get Text
	*
	* @return String - Text
	*/
	public String getText() {
		return (getBodyFieldValue(58));
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
	/**
	* setter method to set Text
	*
	* @param String - Text
	*/
	public void setText(String _Text) {
		addBodyField(58, _Text);
	}

}
