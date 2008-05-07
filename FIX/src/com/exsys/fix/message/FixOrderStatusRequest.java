package com.exsys.fix.message;

import java.util.*;

/**
* This class is used to represent the FIX message OrderStatusRequest
*
*/
public class FixOrderStatusRequest extends FixMessage {
	/**
	* Constructor to construct FixOrderStatusRequest object 
	*
	*/
	public FixOrderStatusRequest() {
		setMessageType(FixConstants.FIX_MSGTYPE_ORDER_STATUS_REQUEST);
		setMsgType(FixConstants.FIX_MSGTYPE_ORDER_STATUS_REQUEST);

	}
	/**
	* getter method to get ClientID
	*
	* @return String - ClientID
	*/
	public String getClientID() {
		return (getBodyFieldValue(109));
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
	* getter method to get CorrelationClOrdID
	*
	* @return String - CorrelationClOrdID
	*/
	public String getCorrelationClOrdID() {
		return (getBodyFieldValue(9717));
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
	* getter method to get OrderID
	*
	* @return String - OrderID
	*/
	public String getOrderID() {
		return (getBodyFieldValue(37));
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
	* getter method to get SecurityType
	*
	* @return String - SecurityType
	*/
	public String getSecurityType() {
		return (getBodyFieldValue(167));
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
	* getter method to get Symbol
	*
	* @return String - Symbol
	*/
	public String getSymbol() {
		return (getBodyFieldValue(55));
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
	* setter method to set ClientID
	*
	* @param String - ClientID
	*/
	public void setClientID(String _ClientID) {
		addBodyField(109, _ClientID);
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
	* setter method to set OrderID
	*
	* @param String - OrderID
	*/
	public void setOrderID(String _OrderID) {
		addBodyField(37, _OrderID);
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
	* setter method to set SecurityType
	*
	* @param String - SecurityType
	*/
	public void setSecurityType(String _SecurityType) {
		addBodyField(167, _SecurityType);
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
	* setter method to set Symbol
	*
	* @param String - Symbol
	*/
	public void setSymbol(String _Symbol) {
		addBodyField(55, _Symbol);
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
	* setter method to set TransactTime
	*
	* @param Date - TransactTime
	*/
	public void setTransactTime(Date _TransactTime) {
		addBodyField(60, getUTCTimeStampString(_TransactTime));
	}
}
