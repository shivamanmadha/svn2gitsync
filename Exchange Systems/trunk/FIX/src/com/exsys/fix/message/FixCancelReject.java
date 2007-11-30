package com.exsys.fix.message;

////05162007 - added 9139 (OriginatorUserId) tag per ICE

import java.util.*;

/**
* This class is used to represent the FIX message CancelReject
*
*/
public class FixCancelReject extends FixMessage {

	/**
	* Constructor to construct FixCancelReject object 
	*
	*/
	public FixCancelReject() {
		setMessageType(FixConstants.FIX_MSGTYPE_CANCEL_REJECT);
		setMsgType(FixConstants.FIX_MSGTYPE_CANCEL_REJECT);
	}
	/**
	* setter method to set OriginatorUserID
	*
	* @param String - origUser
	*/
	public void setOriginatorUserID(String _origUser) {
		addBodyField(9139, _origUser);
	}
	/**
	* getter method to get OriginatorUserID
	*
	* @return String - OriginatorUserID
	*/
	public String getOriginatorUserID() {
		return (getBodyFieldValue(9139));
	}

	/**
	* getter method to get Account
	*
	* @return String - Account
	*/
	public String getAccount() {
		return (getBodyFieldValue(1));
	}
	/**
	* getter method to get ClOrderID
	*
	* @return String - ClOrderID
	*/
	public String getClOrderID() {
		return (getBodyFieldValue(11));
	}
	/**
	* getter method to get ExecID
	*
	* @return String - ExecID
	*/
	public String getExecID() {
		return (getBodyFieldValue(17));
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
	* getter method to get OrdStatus
	*
	* @return String - OrdStatus
	*/
	public String getOrdStatus() {
		return (getBodyFieldValue(39));
	}
	/**
	* getter method to get OrigClOrderID
	*
	* @return String - OrigClOrderID
	*/
	public String getOrigClOrderID() {
		return (getBodyFieldValue(41));
	}
	/**
	* getter method to get Text
	*
	* @return String - Text
	*/
	public String getText() {
		return (getBodyFieldValue(58));
	}
	/**
	* getter method to get TransactTime
	*
	* @return Date - TransactTime
	*/
	public Date getTransactTime() {
		return (UTCTimeStampStringToDate(getBodyFieldValue(60)));
	}
	/**
	* getter method to get TransactTimeAsString
	*
	* @return String - TransactTimeAsString
	*/
	public String getTransactTimeAsString() {
		return (getBodyFieldValue(60));
	}
	/**
	* getter method to get CxlRejReason
	*
	* @return String - CxlRejReason
	*/
	public String getCxlRejReason() {
		return (getBodyFieldValue(102));
	}
	/**
	* getter method to get CxlRejResponseTo
	*
	* @return String - CxlRejResponseTo
	*/
	public String getCxlRejResponseTo() {
		return (getBodyFieldValue(434));
	}
	/**
	* getter method to get CorrelationClOrdID
	*
	* @return String - CorrelationClOrdID
	*/
	public String getCorrelationClOrdID() {
		return (getBodyFieldValue(9717));
	}

	/**
	* setter method to set Account
	*
	* @param String - Account
	*/
	public void setAccount(String _Account) {
		addBodyField(1, _Account);
	}
	/**
	* setter method to set ClOrderID
	*
	* @param String - ClOrderID
	*/
	public void setClOrderID(String _ClOrderID) {
		addBodyField(11, _ClOrderID);
	}
	/**
	* setter method to set ExecID
	*
	* @param String - ExecID
	*/
	public void setExecID(String _ExecID) {
		addBodyField(17, _ExecID);
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
	* setter method to set OrdStatus
	*
	* @param String - OrdStatus
	*/
	public void setOrdStatus(String _OrdStatus) {
		addBodyField(39, _OrdStatus);
	}
	/**
	* setter method to set OrigClOrderID
	*
	* @param String - OrigClOrderID
	*/
	public void setOrigClOrderID(String _OrigClOrderID) {
		addBodyField(41, _OrigClOrderID);
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
	* setter method to set TransactTime
	*
	* @param Date - TransactTime
	*/
	public void setTransactTime(Date _TransactTime) {
		addBodyField(60, getUTCTimeStampString(_TransactTime));
	}
	/**
	* setter method to set TransactTime
	*
	* @param String - TransactTime
	*/
	public void setTransactTime(String _TransactTime) {
		addBodyField(60, _TransactTime);
	}
	/**
	* setter method to set CxlRejReason
	*
	* @param String - CxlRejReason
	*/
	public void setCxlRejReason(String _CxlRejReason) {
		addBodyField(102, _CxlRejReason);
	}
	/**
	* setter method to set CxlRejResponseTo
	*
	* @param String - CxlRejResponseTo
	*/
	public void setCxlRejResponseTo(String _CxlRejResponseTo) {
		addBodyField(434, _CxlRejResponseTo);
	}
	/**
	* setter method to set CorrelationClOrdID
	*
	* @param String - CorrelationClOrdID
	*/
	public void setCorrelationClOrdID(String _CorrelationClOrdID) {
		addBodyField(9717, _CorrelationClOrdID);
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
	/**
	* setter method to set Side
	*
	* @param String - Side
	*/
	public void setSide(String _Side) {
		addBodyField(54, _Side);
	}
	/**
	* getter method to get Side
	*
	* @return String - Side
	*/
	public String getSide() {
		return (getBodyFieldValue(54));
	}
	/**
	* setter method to set SecurityDesc
	*
	* @param String - SecurityDesc
	*/
	public void setSecurityDesc(String _SecurityDesc) {
		addBodyField(107, _SecurityDesc);
	}
	/**
	* getter method to get SecurityDesc
	*
	* @return String - SecurityDesc
	*/
	public String getSecurityDesc() {
		return (getBodyFieldValue(107));
	}
	/**
	* setter method to set OrderQty
	*
	* @param String - OrderQty
	*/
	public void setOrderQty(String _OrderQty) {
		addBodyField(38, _OrderQty);
	}
	/**
	* getter method to get OrderQty
	*
	* @return float - OrderQty
	*/
	public float getOrderQty() {
		return (stringTofloat(getBodyFieldValue(38)));
	}
	/**
	* getter method to get OrderQtyAsString
	*
	* @return String - OrderQtyAsString
	*/
	public String getOrderQtyAsString() {
		return (getBodyFieldValue(38));
	}
	/**
	* setter method to set Price
	*
	* @param String - Price
	*/
	public void setPrice(String _Price) {
		addBodyField(44, _Price);
	}
	/**
	* getter method to get Price
	*
	* @return float - Price
	*/
	public float getPrice() {
		return (stringTofloat(getBodyFieldValue(44)));
	}
	/**
	* getter method to get PriceAsString
	*
	* @return String - PriceAsString
	*/
	public String getPriceAsString() {
		return (getBodyFieldValue(44));
	}

}
