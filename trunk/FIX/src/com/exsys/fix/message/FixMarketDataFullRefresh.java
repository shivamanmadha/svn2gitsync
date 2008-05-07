package com.exsys.fix.message;

/**
* This class is used to represent the FIX message ketDataFullRefresh
*
*/
public class FixMarketDataFullRefresh extends FixMessage {
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
	* Constructor to construct MarketDataFullRefresh object 
	*
	*/
	public FixMarketDataFullRefresh() {
		setMessageType(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH);
		setMsgType(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH);
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
	* setter method to set TotalVolumeTraded
	*
	* @param float - TotalVolumeTraded
	*/
	public void setTotalVolumeTraded(float _TotalVolumeTraded) {
		addBodyField(387, getString(_TotalVolumeTraded));
	}
	/**
	* setter method to set TotalVolumeTraded
	*
	* @param String - TotalVolumeTraded
	*/
	public void setTotalVolumeTraded(String _TotalVolumeTraded) {
		addBodyField(387, _TotalVolumeTraded);
	}
	/**
	* getter method to get TotalVolumeTraded
	*
	* @return float - TotalVolumeTraded
	*/
	public float getTotalVolumeTraded() {
		return (stringTofloat(getBodyFieldValue(387)));
	}
	/**
	* getter method to get TotalVolumeTradedAsString
	*
	* @return String - TotalVolumeTradedAsString
	*/
	public String getTotalVolumeTradedAsString() {
		return (getBodyFieldValue(387));
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
