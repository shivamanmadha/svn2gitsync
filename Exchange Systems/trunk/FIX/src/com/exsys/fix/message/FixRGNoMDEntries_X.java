package com.exsys.fix.message;

import java.util.*;

/**
* This class is used to represent the FIX message RGNoMDEntries_X
*
*/
public class FixRGNoMDEntries_X extends FixRepeatedGroup {
	public static ArrayList fieldList = new ArrayList();
	static {
		fieldList.add("279");
		fieldList.add("285");
		fieldList.add("269");
		fieldList.add("55");
		fieldList.add("270");
		fieldList.add("271");
		fieldList.add("272");
		fieldList.add("273");
		fieldList.add("59");
		fieldList.add("37");
		fieldList.add("40");
		fieldList.add("418");
		fieldList.add("9175");
		fieldList.add("9141");
		fieldList.add("9003");
		fieldList.add("387");
		fieldList.add("58");

	};

	public String FIRST_FIELD = "279";

	/**
	* getter method to get FirstField
	*
	* @return String - FirstField
	*/
	public String getFirstField() {
		return "279";
	}
	/**
	* getter method to get RepeatedGroupTag
	*
	* @return String - RepeatedGroupTag
	*/
	public String getRepeatedGroupTag() {
		return "268";
	}

	/**
	* method  isFirstField
	*
	* @param String - tagNum
	*
	* @return boolean - isFirstField
	*/
	public boolean isFirstField(String tagNum) {
		return tagNum.equals(FIRST_FIELD);

	}
	/**
	* method  isMemberField
	*
	* @param String - tagNum
	*
	* @return boolean - isMemberField
	*/
	public boolean isMemberField(String tagNum) {
		return fieldList.contains(tagNum);
	}

	/**
	* Constructor to construct FixRGNoMDEntries_X object 
	*
	*/
	public FixRGNoMDEntries_X() {
	}
	/**
	* setter method to set MDUpdateAction
	*
	* @param String - MDUpdateAction
	*/
	public void setMDUpdateAction(String _MDUpdateAction) {
		addBodyField(279, _MDUpdateAction);
	}
	/**
	* getter method to get MDUpdateAction
	*
	* @return String - MDUpdateAction
	*/
	public String getMDUpdateAction() {
		return (getBodyFieldValue(279));
	}
	/**
	* setter method to set DeleteReason
	*
	* @param String - DeleteReason
	*/
	public void setDeleteReason(String _DeleteReason) {
		addBodyField(285, _DeleteReason);
	}
	/**
	* getter method to get DeleteReason
	*
	* @return String - DeleteReason
	*/
	public String getDeleteReason() {
		return (getBodyFieldValue(285));
	}
	/**
	* setter method to set MDEntryType
	*
	* @param String - MDEntryType
	*/
	public void setMDEntryType(String _MDEntryType) {
		addBodyField(269, _MDEntryType);
	}
	/**
	* getter method to get MDEntryType
	*
	* @return String - MDEntryType
	*/
	public String getMDEntryType() {
		return (getBodyFieldValue(269));
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
	* setter method to set MDEntryPx
	*
	* @param float - MDEntryPx
	*/
	public void setMDEntryPx(float _MDEntryPx) {
		addBodyField(270, getString(_MDEntryPx));
	}
	/**
	* setter method to set MDEntryPx
	*
	* @param String - MDEntryPx
	*/
	public void setMDEntryPx(String _MDEntryPx) {
		addBodyField(270, _MDEntryPx);
	}
	/**
	* getter method to get MDEntryPx
	*
	* @return float - MDEntryPx
	*/
	public float getMDEntryPx() {
		return (stringTofloat(getBodyFieldValue(270)));
	}
	/**
	* getter method to get MDEntryPxAsString
	*
	* @return String - MDEntryPxAsString
	*/
	public String getMDEntryPxAsString() {
		return (getBodyFieldValue(270));
	}
	/**
	* setter method to set MDEntrySize
	*
	* @param float - MDEntrySize
	*/
	public void setMDEntrySize(float _MDEntrySize) {
		addBodyField(271, getString(_MDEntrySize));
	}
	/**
	* setter method to set MDEntrySize
	*
	* @param String - MDEntrySize
	*/
	public void setMDEntrySize(String _MDEntrySize) {
		addBodyField(271, _MDEntrySize);
	}
	/**
	* getter method to get MDEntrySize
	*
	* @return float - MDEntrySize
	*/
	public float getMDEntrySize() {
		return (stringTofloat(getBodyFieldValue(271)));
	}
	/**
	* getter method to get MDEntrySizeAsString
	*
	* @return String - MDEntrySizeAsString
	*/
	public String getMDEntrySizeAsString() {
		return (getBodyFieldValue(271));
	}
	/**
	* setter method to set MDEntryDate
	*
	* @param Date - MDEntryDate
	*/
	public void setMDEntryDate(Date _MDEntryDate) {
		addBodyField(272, getUTCDateString(_MDEntryDate));
	}
	/**
	* setter method to set MDEntryDate
	*
	* @param String - MDEntryDate
	*/
	public void setMDEntryDate(String _MDEntryDate) {
		addBodyField(272, _MDEntryDate);
	}
	/**
	* getter method to get MDEntryDate
	*
	* @return Date - MDEntryDate
	*/
	public Date getMDEntryDate() {
		return (UTCDateStringToDate(getBodyFieldValue(272)));
	}
	/**
	* getter method to get MDEntryDateAsString
	*
	* @return String - MDEntryDateAsString
	*/
	public String getMDEntryDateAsString() {
		return (getBodyFieldValue(272));
	}
	/**
	* setter method to set MDEntryTime
	*
	* @param Date - MDEntryTime
	*/
	public void setMDEntryTime(Date _MDEntryTime) {
		addBodyField(273, getUTCTimeString(_MDEntryTime));
	}
	/**
	* setter method to set MDEntryTime
	*
	* @param String - MDEntryTime
	*/
	public void setMDEntryTime(String _MDEntryTime) {
		addBodyField(273, _MDEntryTime);
	}
	/**
	* getter method to get MDEntryTime
	*
	* @return Date - MDEntryTime
	*/
	public Date getMDEntryTime() {
		return (UTCTimeStringToDate(getBodyFieldValue(273)));
	}
	/**
	* getter method to get MDEntryTimeAsString
	*
	* @return String - MDEntryTimeAsString
	*/
	public String getMDEntryTimeAsString() {
		return (getBodyFieldValue(273));
	}
	/**
	* setter method to set TimeInForce
	*
	* @param String - TimeInForce
	*/
	public void setTimeInForce(String _TimeInForce) {
		addBodyField(59, _TimeInForce);
	}
	/**
	* getter method to get TimeInForce
	*
	* @return String - TimeInForce
	*/
	public String getTimeInForce() {
		return (getBodyFieldValue(59));
	}
	/**
	* setter method to set OrderID
	*
	* @param String - OrderID
	*/
	public void setOrderID(String _OrderID) {
		addBodyField(37, _OrderID);
	}
	/**
	* getter method to get OrderID
	*
	* @return String - OrderID
	*/
	public String getOrderID() {
		return (getBodyFieldValue(37));
	}
	/**
	* setter method to set OrdType
	*
	* @param String - OrdType
	*/
	public void setOrdType(String _OrdType) {
		addBodyField(40, _OrdType);
	}
	/**
	* getter method to get OrdType
	*
	* @return String - OrdType
	*/
	public String getOrdType() {
		return (getBodyFieldValue(40));
	}
	/**
	* setter method to set TradeType
	*
	* @param String - TradeType
	*/
	public void setTradeType(String _TradeType) {
		addBodyField(418, _TradeType);
	}
	/**
	* getter method to get TradeType
	*
	* @return String - TradeType
	*/
	public String getTradeType() {
		return (getBodyFieldValue(418));
	}
	/**
	* setter method to set OrderState
	*
	* @param String - OrderState
	*/
	public void setOrderState(String _OrderState) {
		addBodyField(9175, _OrderState);
	}
	/**
	* getter method to get OrderState
	*
	* @return String - OrderState
	*/
	public String getOrderState() {
		return (getBodyFieldValue(9175));
	}
	/**
	* setter method to set SequenceId
	*
	* @param int - SequenceId
	*/
	public void setSequenceId(int _SequenceId) {
		addBodyField(9141, getString(_SequenceId));
	}
	/**
	* setter method to set SequenceId
	*
	* @param String - SequenceId
	*/
	public void setSequenceId(String _SequenceId) {
		addBodyField(9141, _SequenceId);
	}
	/**
	* getter method to get SequenceId
	*
	* @return int - SequenceId
	*/
	public int getSequenceId() {
		return (stringToint(getBodyFieldValue(9141)));
	}
	/**
	* getter method to get SequenceIdAsString
	*
	* @return String - SequenceIdAsString
	*/
	public String getSequenceIdAsString() {
		return (getBodyFieldValue(9141));
	}
	/**
	* setter method to set ImpliedIndicator
	*
	* @param boolean - ImpliedIndicator
	*/
	public void setImpliedIndicator(boolean _ImpliedIndicator) {
		addBodyField(9003, getString(_ImpliedIndicator));
	}
	/**
	* setter method to set ImpliedIndicator
	*
	* @param String - ImpliedIndicator
	*/
	public void setImpliedIndicator(String _ImpliedIndicator) {
		addBodyField(9003, _ImpliedIndicator);
	}
	/**
	* getter method to get ImpliedIndicator
	*
	* @return boolean - ImpliedIndicator
	*/
	public boolean getImpliedIndicator() {
		return (stringToboolean(getBodyFieldValue(9003)));
	}
	/**
	* getter method to get ImpliedIndicatorAsString
	*
	* @return String - ImpliedIndicatorAsString
	*/
	public String getImpliedIndicatorAsString() {
		return (getBodyFieldValue(9003));
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
