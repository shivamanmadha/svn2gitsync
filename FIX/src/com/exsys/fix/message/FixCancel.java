package com.exsys.fix.message;

//05162007 - added 9139 (OriginatorUserId) tag per ICE

import java.util.Date;


/**
* This class is used to represent the FIX message Cancel
*
*/
public class FixCancel extends FixMessage {
	/**
	* Constructor to construct FixCancel object 
	*
	*/
	public FixCancel() {
		setMessageType(FixConstants.FIX_MSGTYPE_CANCEL);
		setMsgType(FixConstants.FIX_MSGTYPE_CANCEL);

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
	* getter method to get CashOrderQty
	*
	* @return float - CashOrderQty
	*/
	public float getCashOrderQty() {
		return (stringTofloat(getBodyFieldValue(152)));
	}
	/**
	* getter method to get CashOrderQtyAsString
	*
	* @return String - CashOrderQtyAsString
	*/
	public String getCashOrderQtyAsString() {
		return (getBodyFieldValue(152));
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
	* getter method to get ComplianceID
	*
	* @return String - ComplianceID
	*/
	public String getComplianceID() {
		return (getBodyFieldValue(376));
	}
	/**
	* getter method to get ContractMultiplier
	*
	* @return float - ContractMultiplier
	*/
	public float getContractMultiplier() {
		return (stringTofloat(getBodyFieldValue(231)));
	}
	/**
	* getter method to get ContractMultiplierAsString
	*
	* @return String - ContractMultiplierAsString
	*/
	public String getContractMultiplierAsString() {
		return (getBodyFieldValue(231));
	}
	/**
	* getter method to get CouponRate
	*
	* @return float - CouponRate
	*/
	public float getCouponRate() {
		return (stringTofloat(getBodyFieldValue(223)));
	}
	/**
	* getter method to get CouponRateAsString
	*
	* @return String - CouponRateAsString
	*/
	public String getCouponRateAsString() {
		return (getBodyFieldValue(223));
	}
	/**
	* getter method to get EncodedIssuerLen
	*
	* @return int - EncodedIssuerLen
	*/
	public int getEncodedIssuerLen() {
		return (stringToint(getBodyFieldValue(348)));
	}
	/**
	* getter method to get EncodedIssuerLenAsString
	*
	* @return String - EncodedIssuerLenAsString
	*/
	public String getEncodedIssuerLenAsString() {
		return (getBodyFieldValue(348));
	}
	/**
	* getter method to get EncodedSecurityDescLen
	*
	* @return int - EncodedSecurityDescLen
	*/
	public int getEncodedSecurityDescLen() {
		return (stringToint(getBodyFieldValue(350)));
	}
	/**
	* getter method to get EncodedSecurityDescLenAsString
	*
	* @return String - EncodedSecurityDescLenAsString
	*/
	public String getEncodedSecurityDescLenAsString() {
		return (getBodyFieldValue(350));
	}
	/**
	* getter method to get EncodedTextLen
	*
	* @return int - EncodedTextLen
	*/
	public int getEncodedTextLen() {
		return (stringToint(getBodyFieldValue(354)));
	}
	/**
	* getter method to get EncodedTextLenAsString
	*
	* @return String - EncodedTextLenAsString
	*/
	public String getEncodedTextLenAsString() {
		return (getBodyFieldValue(354));
	}
	/**
	* getter method to get ExecBroker
	*
	* @return String - ExecBroker
	*/
	public String getExecBroker() {
		return (getBodyFieldValue(76));
	}
	/**
	* getter method to get IDSource
	*
	* @return String - IDSource
	*/
	public String getIDSource() {
		return (getBodyFieldValue(22));
	}
	/**
	* getter method to get Issuer
	*
	* @return String - Issuer
	*/
	public String getIssuer() {
		return (getBodyFieldValue(106));
	}
	/**
	* getter method to get ListID
	*
	* @return String - ListID
	*/
	public String getListID() {
		return (getBodyFieldValue(66));
	}
	/**
	* getter method to get MaturityDay
	*
	* @return int - MaturityDay
	*/
	public int getMaturityDay() {
		return (stringToint(getBodyFieldValue(205)));
	}
	/**
	* getter method to get MaturityDayAsString
	*
	* @return String - MaturityDayAsString
	*/
	public String getMaturityDayAsString() {
		return (getBodyFieldValue(205));
	}
	/**
	* getter method to get MaturityYear
	*
	* @return Date - MaturityYear
	*/
	public Date getMaturityYear() {
		return (MonthYearStringToDate(getBodyFieldValue(200)));
	}
	/**
	* getter method to get MaturityYearAsString
	*
	* @return String - MaturityYearAsString
	*/
	public String getMaturityYearAsString() {
		return (getBodyFieldValue(200));
	}
	/**
	* getter method to get OptAttribute
	*
	* @return String - OptAttribute
	*/
	public String getOptAttribute() {
		return (getBodyFieldValue(206));
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
	* getter method to get OrigClOrderID
	*
	* @return String - OrigClOrderID
	*/
	public String getOrigClOrderID() {
		return (getBodyFieldValue(41));
	}
	/**
	* getter method to get PutOrCall
	*
	* @return int - PutOrCall
	*/
	public int getPutOrCall() {
		return (stringToint(getBodyFieldValue(201)));
	}
	/**
	* getter method to get PutOrCallAsString
	*
	* @return String - PutOrCallAsString
	*/
	public String getPutOrCallAsString() {
		return (getBodyFieldValue(201));
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
	* getter method to get SecurityExchange
	*
	* @return String - SecurityExchange
	*/
	public String getSecurityExchange() {
		return (getBodyFieldValue(207));
	}
	/**
	* getter method to get SecurityID
	*
	* @return String - SecurityID
	*/
	public String getSecurityID() {
		return (getBodyFieldValue(48));
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
	* getter method to get SolicitedFlag
	*
	* @return boolean - SolicitedFlag
	*/
	public boolean getSolicitedFlag() {
		return (stringToboolean(getBodyFieldValue(377)));
	}
	/**
	* getter method to get SolicitedFlagAsString
	*
	* @return String - SolicitedFlagAsString
	*/
	public String getSolicitedFlagAsString() {
		return (getBodyFieldValue(377));
	}
	/**
	* getter method to get StrikePrice
	*
	* @return float - StrikePrice
	*/
	public float getStrikePrice() {
		return (stringTofloat(getBodyFieldValue(202)));
	}
	/**
	* getter method to get StrikePriceAsString
	*
	* @return String - StrikePriceAsString
	*/
	public String getStrikePriceAsString() {
		return (getBodyFieldValue(202));
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
	* getter method to get SymbolSfx
	*
	* @return String - SymbolSfx
	*/
	public String getSymbolSfx() {
		return (getBodyFieldValue(65));
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
	* setter method to set Account
	*
	* @param String - Account
	*/
	public void setAccount(String _Account) {
		addBodyField(1, _Account);
	}
	/**
	* setter method to set CashOrderQty
	*
	* @param float - CashOrderQty
	*/
	public void setCashOrderQty(float _CashOrderQty) {
		addBodyField(152, getString(_CashOrderQty));
	}
	/**
	* setter method to set CashOrderQty
	*
	* @param String - CashOrderQty
	*/
	public void setCashOrderQty(String _CashOrderQty) {
		addBodyField(152, _CashOrderQty);
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
	* setter method to set ComplianceID
	*
	* @param String - ComplianceID
	*/
	public void setComplianceID(String _ComplianceID) {
		addBodyField(376, _ComplianceID);
	}
	/**
	* setter method to set ContractMultiplier
	*
	* @param float - ContractMultiplier
	*/
	public void setContractMultiplier(float _ContractMultiplier) {
		addBodyField(231, getString(_ContractMultiplier));
	}
	/**
	* setter method to set ContractMultiplier
	*
	* @param String - ContractMultiplier
	*/
	public void setContractMultiplier(String _ContractMultiplier) {
		addBodyField(231, _ContractMultiplier);
	}
	/**
	* setter method to set CouponRate
	*
	* @param float - CouponRate
	*/
	public void setCouponRate(float _CouponRate) {
		addBodyField(223, getString(_CouponRate));
	}
	/**
	* setter method to set CouponRate
	*
	* @param String - CouponRate
	*/
	public void setCouponRate(String _CouponRate) {
		addBodyField(223, _CouponRate);
	}
	/**
	* setter method to set EncodedIssuerLen
	*
	* @param int - EncodedIssuerLen
	*/
	public void setEncodedIssuerLen(int _EncodedIssuerLen) {
		addBodyField(348, getString(_EncodedIssuerLen));
	}
	/**
	* setter method to set EncodedIssuerLen
	*
	* @param String - EncodedIssuerLen
	*/
	public void setEncodedIssuerLen(String _EncodedIssuerLen) {
		addBodyField(348, _EncodedIssuerLen);
	}
	/**
	* setter method to set EncodedSecurityDescLen
	*
	* @param int - EncodedSecurityDescLen
	*/
	public void setEncodedSecurityDescLen(int _EncodedSecurityDescLen) {
		addBodyField(350, getString(_EncodedSecurityDescLen));
	}
	/**
	* setter method to set EncodedSecurityDescLen
	*
	* @param String - EncodedSecurityDescLen
	*/
	public void setEncodedSecurityDescLen(String _EncodedSecurityDescLen) {
		addBodyField(350, _EncodedSecurityDescLen);
	}
	/**
	* setter method to set EncodedTextLen
	*
	* @param int - EncodedTextLen
	*/
	public void setEncodedTextLen(int _EncodedTextLen) {
		addBodyField(354, getString(_EncodedTextLen));
	}
	/**
	* setter method to set EncodedTextLen
	*
	* @param String - EncodedTextLen
	*/
	public void setEncodedTextLen(String _EncodedTextLen) {
		addBodyField(354, _EncodedTextLen);
	}
	/**
	* setter method to set ExecBroker
	*
	* @param String - ExecBroker
	*/
	public void setExecBroker(String _ExecBroker) {
		addBodyField(76, _ExecBroker);
	}
	/**
	* setter method to set IDSource
	*
	* @param String - IDSource
	*/
	public void setIDSource(String _IDSource) {
		addBodyField(22, _IDSource);
	}
	/**
	* setter method to set Issuer
	*
	* @param String - Issuer
	*/
	public void setIssuer(String _Issuer) {
		addBodyField(106, _Issuer);
	}
	/**
	* setter method to set ListID
	*
	* @param String - ListID
	*/
	public void setListID(String _ListID) {
		addBodyField(66, _ListID);
	}
	/**
	* setter method to set MaturityDay
	*
	* @param int - MaturityDay
	*/
	public void setMaturityDay(int _MaturityDay) {
		addBodyField(205, getString(_MaturityDay));
	}
	/**
	* setter method to set MaturityDay
	*
	* @param String - MaturityDay
	*/
	public void setMaturityDay(String _MaturityDay) {
		addBodyField(205, _MaturityDay);
	}
	/**
	* setter method to set MaturityYear
	*
	* @param String - MaturityYear
	*/
	public void setMaturityYear(String _MaturityYear) {
		addBodyField(200, _MaturityYear);
	}
	/**
	* setter method to set MaturityYear
	*
	* @param Date - MaturityYear
	*/
	public void setMaturityYear(Date _MaturityYear) {
		addBodyField(200, getMonthYearString(_MaturityYear));
	}
	/**
	* setter method to set OptAttribute
	*
	* @param String - OptAttribute
	*/
	public void setOptAttribute(String _OptAttribute) {
		addBodyField(206, _OptAttribute);
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
	* setter method to set OrderQty
	*
	* @param float - OrderQty
	*/
	public void setOrderQty(float _OrderQty) {
		addBodyField(38, getString(_OrderQty));
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
	* setter method to set OrigClOrderID
	*
	* @param String - OrigClOrderID
	*/
	public void setOrigClOrderID(String _OrigClOrderID) {
		addBodyField(41, _OrigClOrderID);
	}
	/**
	* setter method to set PutOrCall
	*
	* @param int - PutOrCall
	*/
	public void setPutOrCall(int _PutOrCall) {
		addBodyField(201, getString(_PutOrCall));
	}
	/**
	* setter method to set PutOrCall
	*
	* @param String - PutOrCall
	*/
	public void setPutOrCall(String _PutOrCall) {
		addBodyField(201, _PutOrCall);
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
	* setter method to set SecurityExchange
	*
	* @param String - SecurityExchange
	*/
	public void setSecurityExchange(String _SecurityExchange) {
		addBodyField(207, _SecurityExchange);
	}
	/**
	* setter method to set SecurityID
	*
	* @param String - SecurityID
	*/
	public void setSecurityID(String _SecurityID) {
		addBodyField(48, _SecurityID);
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
	* setter method to set SolicitedFlag
	*
	* @param String - SolicitedFlag
	*/
	public void setSolicitedFlag(String _SolicitedFlag) {
		addBodyField(377, _SolicitedFlag);
	}
	/**
	* setter method to set SolicitedFlag
	*
	* @param boolean - SolicitedFlag
	*/
	public void setSolicitedFlag(boolean _SolicitedFlag) {
		addBodyField(377, getString(_SolicitedFlag));
	}
	/**
	* setter method to set StrikePrice
	*
	* @param float - StrikePrice
	*/
	public void setStrikePrice(float _StrikePrice) {
		addBodyField(202, getString(_StrikePrice));
	}
	/**
	* setter method to set StrikePrice
	*
	* @param String - StrikePrice
	*/
	public void setStrikePrice(String _StrikePrice) {
		addBodyField(202, _StrikePrice);
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
	* setter method to set SymbolSfx
	*
	* @param String - SymbolSfx
	*/
	public void setSymbolSfx(String _SymbolSfx) {
		addBodyField(65, _SymbolSfx);
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
