package com.exsys.fix.message;

/**
* This class is used to represent the FIX message ketDataIncrementalRefresh
*
*/
public class FixMarketDataIncrementalRefresh extends FixMessage {
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
		return (tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NOMDENTRIES));
	}

	/**
	* Constructor to construct MarketDataIncrementalRefresh object 
	*
	*/
	public FixMarketDataIncrementalRefresh() {
		setMessageType(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH);
		setMsgType(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH);
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
	* setter method to set NoMDEntries
	*
	* @param int - NoMDEntries
	*/
	public void setNoMDEntries(int _NoMDEntries) {
		addBodyField(268, getString(_NoMDEntries));
	}
	/**
	* setter method to set NoMDEntries
	*
	* @param String - NoMDEntries
	*/
	public void setNoMDEntries(String _NoMDEntries) {
		addBodyField(268, _NoMDEntries);
	}
	/**
	* getter method to get NoMDEntries
	*
	* @return int - NoMDEntries
	*/
	public int getNoMDEntries() {
		return (stringToint(getBodyFieldValue(268)));
	}
	/**
	* getter method to get NoMDEntriesAsString
	*
	* @return String - NoMDEntriesAsString
	*/
	public String getNoMDEntriesAsString() {
		return (getBodyFieldValue(268));
	}

}
