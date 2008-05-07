package com.exsys.fix.message;

/**
* This class is used to represent the FIX message ketDataRequest
*
*/
public class FixMarketDataRequest extends FixMessage {
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
		return (
			tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NORELATEDSYM)
				|| tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NOMDENTRYTYPTES));
	}

	/**
	* Constructor to construct MarketDataRequest object 
	*
	*/
	public FixMarketDataRequest() {
		setMessageType(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST);
		setMsgType(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST);

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
	* setter method to set SubscriptionRequestType
	*
	* @param String - SubscriptionRequestType
	*/
	public void setSubscriptionRequestType(String _SubscriptionRequestType) {
		addBodyField(263, _SubscriptionRequestType);
	}
	/**
	* getter method to get SubscriptionRequestType
	*
	* @return String - SubscriptionRequestType
	*/
	public String getSubscriptionRequestType() {
		return (getBodyFieldValue(263));
	}
	/**
	* setter method to set MarketDepth
	*
	* @param int - MarketDepth
	*/
	public void setMarketDepth(int _MarketDepth) {
		addBodyField(264, getString(_MarketDepth));
	}
	/**
	* setter method to set MarketDepth
	*
	* @param String - MarketDepth
	*/
	public void setMarketDepth(String _MarketDepth) {
		addBodyField(264, _MarketDepth);
	}
	/**
	* getter method to get MarketDepth
	*
	* @return int - MarketDepth
	*/
	public int getMarketDepth() {
		return (stringToint(getBodyFieldValue(264)));
	}
	/**
	* getter method to get MarketDepthAsString
	*
	* @return String - MarketDepthAsString
	*/
	public String getMarketDepthAsString() {
		return (getBodyFieldValue(264));
	}
	/**
	* setter method to set MDUpdateType
	*
	* @param int - MDUpdateType
	*/
	public void setMDUpdateType(int _MDUpdateType) {
		addBodyField(265, getString(_MDUpdateType));
	}
	/**
	* setter method to set MDUpdateType
	*
	* @param String - MDUpdateType
	*/
	public void setMDUpdateType(String _MDUpdateType) {
		addBodyField(265, _MDUpdateType);
	}
	/**
	* getter method to get MDUpdateType
	*
	* @return int - MDUpdateType
	*/
	public int getMDUpdateType() {
		return (stringToint(getBodyFieldValue(265)));
	}
	/**
	* getter method to get MDUpdateTypeAsString
	*
	* @return String - MDUpdateTypeAsString
	*/
	public String getMDUpdateTypeAsString() {
		return (getBodyFieldValue(265));
	}
	/**
	* setter method to set NoMDEntryTypes
	*
	* @param int - NoMDEntryTypes
	*/
	public void setNoMDEntryTypes(int _NoMDEntryTypes) {
		addBodyField(267, getString(_NoMDEntryTypes));
	}
	/**
	* setter method to set NoMDEntryTypes
	*
	* @param String - NoMDEntryTypes
	*/
	public void setNoMDEntryTypes(String _NoMDEntryTypes) {
		addBodyField(267, _NoMDEntryTypes);
	}
	/**
	* getter method to get NoMDEntryTypes
	*
	* @return int - NoMDEntryTypes
	*/
	public int getNoMDEntryTypes() {
		return (stringToint(getBodyFieldValue(267)));
	}
	/**
	* getter method to get NoMDEntryTypesAsString
	*
	* @return String - NoMDEntryTypesAsString
	*/
	public String getNoMDEntryTypesAsString() {
		return (getBodyFieldValue(267));
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

}
