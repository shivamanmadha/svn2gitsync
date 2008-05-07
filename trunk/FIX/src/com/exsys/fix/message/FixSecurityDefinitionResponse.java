package com.exsys.fix.message;

/**
* This class is used to represent the FIX message SecurityDefinitionResponse
*
*/
public class FixSecurityDefinitionResponse extends FixMessage {

	/**
	* method  hasRepeatingGroupFields
	*
	* @return boolean - hasRepeatingGroupFields
	*/
	public boolean hasRepeatingGroupFields() {
		return true;
	}
	/**
	* method  isFieldRepeatingGroup
	*
	* @param String - tagNum
	*
	* @return boolean - isFieldRepeatingGroup
	*/
	public boolean isFieldRepeatingGroup(String tagNum) {
		return tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NORELATEDSYM);
	}

	/**
	* Constructor to construct FixSecurityDefinitionResponse object 
	*
	*/
	public FixSecurityDefinitionResponse() {
		setMessageType(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE);
		setMsgType(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE);

	}
	/**
	* setter method to set SecurityResponseID
	*
	* @param String - SecurityResponseID
	*/
	public void setSecurityResponseID(String _SecurityResponseID) {
		addBodyField(322, _SecurityResponseID);
	}
	/**
	* getter method to get SecurityResponseID
	*
	* @return String - SecurityResponseID
	*/
	public String getSecurityResponseID() {
		return (getBodyFieldValue(322));
	}
	/**
	* setter method to set SecurityResponseType
	*
	* @param int - SecurityResponseType
	*/
	public void setSecurityResponseType(int _SecurityResponseType) {
		addBodyField(323, getString(_SecurityResponseType));
	}
	/**
	* setter method to set SecurityResponseType
	*
	* @param String - SecurityResponseType
	*/
	public void setSecurityResponseType(String _SecurityResponseType) {
		addBodyField(323, _SecurityResponseType);
	}
	/**
	* getter method to get SecurityResponseType
	*
	* @return int - SecurityResponseType
	*/
	public int getSecurityResponseType() {
		return (stringToint(getBodyFieldValue(323)));
	}
	/**
	* getter method to get SecurityResponseTypeAsString
	*
	* @return String - SecurityResponseTypeAsString
	*/
	public String getSecurityResponseTypeAsString() {
		return (getBodyFieldValue(323));
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
	* setter method to set Currency
	*
	* @param String - Currency
	*/
	public void setCurrency(String _Currency) {
		addBodyField(15, _Currency);
	}
	/**
	* getter method to get Currency
	*
	* @return String - Currency
	*/
	public String getCurrency() {
		return (getBodyFieldValue(15));
	}
	/**
	* setter method to set TotalNumSecurities
	*
	* @param int - TotalNumSecurities
	*/
	public void setTotalNumSecurities(int _TotalNumSecurities) {
		addBodyField(393, getString(_TotalNumSecurities));
	}
	/**
	* setter method to set TotalNumSecurities
	*
	* @param String - TotalNumSecurities
	*/
	public void setTotalNumSecurities(String _TotalNumSecurities) {
		addBodyField(393, _TotalNumSecurities);
	}
	/**
	* getter method to get TotalNumSecurities
	*
	* @return int - TotalNumSecurities
	*/
	public int getTotalNumSecurities() {
		return (stringToint(getBodyFieldValue(393)));
	}
	/**
	* getter method to get TotalNumSecuritiesAsString
	*
	* @return String - TotalNumSecuritiesAsString
	*/
	public String getTotalNumSecuritiesAsString() {
		return (getBodyFieldValue(393));
	}
	/**
	* setter method to set NoRpts
	*
	* @param int - NoRpts
	*/
	public void setNoRpts(int _NoRpts) {
		addBodyField(82, getString(_NoRpts));
	}
	/**
	* setter method to set NoRpts
	*
	* @param String - NoRpts
	*/
	public void setNoRpts(String _NoRpts) {
		addBodyField(82, _NoRpts);
	}
	/**
	* getter method to get NoRpts
	*
	* @return int - NoRpts
	*/
	public int getNoRpts() {
		return (stringToint(getBodyFieldValue(82)));
	}
	/**
	* getter method to get NoRptsAsString
	*
	* @return String - NoRptsAsString
	*/
	public String getNoRptsAsString() {
		return (getBodyFieldValue(82));
	}
	/**
	* setter method to set ListSeqNo
	*
	* @param int - ListSeqNo
	*/
	public void setListSeqNo(int _ListSeqNo) {
		addBodyField(67, getString(_ListSeqNo));
	}
	/**
	* setter method to set ListSeqNo
	*
	* @param String - ListSeqNo
	*/
	public void setListSeqNo(String _ListSeqNo) {
		addBodyField(67, _ListSeqNo);
	}
	/**
	* getter method to get ListSeqNo
	*
	* @return int - ListSeqNo
	*/
	public int getListSeqNo() {
		return (stringToint(getBodyFieldValue(67)));
	}
	/**
	* getter method to get ListSeqNoAsString
	*
	* @return String - ListSeqNoAsString
	*/
	public String getListSeqNoAsString() {
		return (getBodyFieldValue(67));
	}
	/**
	* setter method to set NoRelatedSym
	*
	* @param int - NoRelatedSym
	*/
	public void setNoRelatedSym(int _NoRelatedSym) {
		addBodyField(146, getString(_NoRelatedSym));
	}
	/**
	* setter method to set NoRelatedSym
	*
	* @param String - NoRelatedSym
	*/
	public void setNoRelatedSym(String _NoRelatedSym) {
		addBodyField(146, _NoRelatedSym);
	}
	/**
	* getter method to get NoRelatedSym
	*
	* @return int - NoRelatedSym
	*/
	public int getNoRelatedSym() {
		return (stringToint(getBodyFieldValue(146)));
	}
	/**
	* getter method to get NoRelatedSymAsString
	*
	* @return String - NoRelatedSymAsString
	*/
	public String getNoRelatedSymAsString() {
		return (getBodyFieldValue(146));
	}
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
		return (getBodyFieldValue(58));
	}

}
