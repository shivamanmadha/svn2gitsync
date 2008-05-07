package com.exsys.fix.message;

/**
* This class is used to represent the FIX message SecurityDefinitionRequest
*
*/
public class FixSecurityDefinitionRequest extends FixMessage {

	/**
	* Constructor to construct FixSecurityDefinitionRequest object 
	*
	*/
	public FixSecurityDefinitionRequest() {
		setMessageType(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST);
		setMsgType(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST);

	}
	/**
	* setter method to set SecurityReqID
	*
	* @param String - SecurityReqID
	*/
	public void setSecurityReqID(String _SecurityReqID) {
		addBodyField(320, _SecurityReqID);
	}
	/**
	* getter method to get SecurityReqID
	*
	* @return String - SecurityReqID
	*/
	public String getSecurityReqID() {
		return (getBodyFieldValue(320));
	}
	/**
	* setter method to set SecurityRequestType
	*
	* @param int - SecurityRequestType
	*/
	public void setSecurityRequestType(int _SecurityRequestType) {
		addBodyField(321, getString(_SecurityRequestType));
	}
	/**
	* setter method to set SecurityRequestType
	*
	* @param String - SecurityRequestType
	*/
	public void setSecurityRequestType(String _SecurityRequestType) {
		addBodyField(321, _SecurityRequestType);
	}
	/**
	* getter method to get SecurityRequestType
	*
	* @return int - SecurityRequestType
	*/
	public int getSecurityRequestType() {
		return (stringToint(getBodyFieldValue(321)));
	}
	/**
	* getter method to get SecurityRequestTypeAsString
	*
	* @return String - SecurityRequestTypeAsString
	*/
	public String getSecurityRequestTypeAsString() {
		return (getBodyFieldValue(321));
	}
	/**
	* setter method to set SecurityType
	*
	* @param String - SecurityType
	*/
	public void setSecurityType(String _SecurityType) {
		addBodyField(167, _SecurityType);
	}
	/**
	* getter method to get SecurityType
	*
	* @return String - SecurityType
	*/
	public String getSecurityType() {
		return (getBodyFieldValue(167));
	}

}
