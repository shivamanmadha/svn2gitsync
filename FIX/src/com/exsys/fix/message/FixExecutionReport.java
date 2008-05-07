package com.exsys.fix.message;

////05162007 - added 9139 (OriginatorUserId) tag per ICE
////07182007 - added 9175 (OrderState) tag per ICE


import java.util.*;

/**
* This class is used to represent the FIX message ExecutionReport
*
*/
public class FixExecutionReport extends FixMessage {
	/**
	* Constructor to construct FixExecutionReport object
	*
	*/
	public FixExecutionReport() {
		setMessageType(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT);
		setMsgType(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT);
	}

	/** fields added per FXMarketSpace*/
	/**
	* setter method to set CalcCcyLastQty
	*
	* @param float - val
	*/
	public void setCalcCcyLastQty(float _val) {
		addBodyField(1056, getString(_val));
	}
	/**
	* setter method to set CalcCcyLastQty
	*
	* @param String - val
	*/
	public void setCalcCcyLastQty(String _val) {
		addBodyField(1056, _val);
	}
	/**
	* getter method to get CalcCcyLastQty
	*
	* @return float - CalcCcyLastQty
	*/
	public float getCalcCcyLastQty() {
		return (stringTofloat(getBodyFieldValue(1056)));
	}
	/**
	* getter method to get CalcCcyLastQtyAsString
	*
	* @return String - CalcCcyLastQtyAsString
	*/
	public String getCalcCcyLastQtyAsString() {
		return (getBodyFieldValue(1056));
	}

	/**
	* setter method to set SettlCurrency
	*
	* @param String - cur
	*/
	public void setSettlCurrency(String _cur) {
		addBodyField(120, _cur);
	}
	/**
	* getter method to get SettlCurrency
	*
	* @return String - SettlCurrency
	*/
	public String getSettlCurrency() {
		return (getBodyFieldValue(120));
	}

	/**
	* setter method to set AggressorIndicator
	*
	* @param String - cur
	*/
	public void setAggressorIndicator(String _cur) {
		addBodyField(1057, _cur);
	}
	/**
	* getter method to get AggressorIndicator
	*
	* @return String - AggressorIndicator
	*/
	public String getAggressorIndicator() {
		return (getBodyFieldValue(1057));
	}

	/** END fields added per FXMarketSpace*/

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
	* setter method to set OrderState
	*
	* @param String - OrderState
	*/
	public void setOrderState( String _os )
	{
		addBodyField( 9175,  _os);
	}
	/**
	* getter method to get OrderState
	*
	* @return String - OrderState
	*/
	public String getOrderState()
	{
		return (getBodyFieldValue( 9175 )) ;
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
	* setter method to set SecondaryOrderID
	*
	* @param String - SecondaryOrderID
	*/
	public void setSecondaryOrderID(String _SecondaryOrderID) {
		addBodyField(198, _SecondaryOrderID);
	}
	/**
	* getter method to get SecondaryOrderID
	*
	* @return String - SecondaryOrderID
	*/
	public String getSecondaryOrderID() {
		return (getBodyFieldValue(198));
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
	* getter method to get ClOrderID
	*
	* @return String - ClOrderID
	*/
	public String getClOrderID() {
		return (getBodyFieldValue(11));
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
	* getter method to get OrigClOrderID
	*
	* @return String - OrigClOrderID
	*/
	public String getOrigClOrderID() {
		return (getBodyFieldValue(41));
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
	* getter method to get ClientID
	*
	* @return String - ClientID
	*/
	public String getClientID() {
		return (getBodyFieldValue(109));
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
	* getter method to get ExecBroker
	*
	* @return String - ExecBroker
	*/
	public String getExecBroker() {
		return (getBodyFieldValue(76));
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
	* getter method to get ListID
	*
	* @return String - ListID
	*/
	public String getListID() {
		return (getBodyFieldValue(66));
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
	* getter method to get ExecID
	*
	* @return String - ExecID
	*/
	public String getExecID() {
		return (getBodyFieldValue(17));
	}
	/**
	* setter method to set ExecTransType
	*
	* @param String - ExecTransType
	*/
	public void setExecTransType(String _ExecTransType) {
		addBodyField(20, _ExecTransType);
	}
	/**
	* getter method to get ExecTransType
	*
	* @return String - ExecTransType
	*/
	public String getExecTransType() {
		return (getBodyFieldValue(20));
	}
	/**
	* setter method to set ExecRefID
	*
	* @param String - ExecRefID
	*/
	public void setExecRefID(String _ExecRefID) {
		addBodyField(19, _ExecRefID);
	}
	/**
	* getter method to get ExecRefID
	*
	* @return String - ExecRefID
	*/
	public String getExecRefID() {
		return (getBodyFieldValue(19));
	}
	/**
	* setter method to set ExecType
	*
	* @param String - ExecType
	*/
	public void setExecType(String _ExecType) {
		addBodyField(150, _ExecType);
	}
	/**
	* getter method to get ExecType
	*
	* @return String - ExecType
	*/
	public String getExecType() {
		return (getBodyFieldValue(150));
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
	* getter method to get OrdStatus
	*
	* @return String - OrdStatus
	*/
	public String getOrdStatus() {
		return (getBodyFieldValue(39));
	}
	/**
	* setter method to set OrdRejReason
	*
	* @param int - OrdRejReason
	*/
	public void setOrdRejReason(int _OrdRejReason) {
		addBodyField(103, getString(_OrdRejReason));
	}
	/**
	* setter method to set OrdRejReason
	*
	* @param String - OrdRejReason
	*/
	public void setOrdRejReason(String _OrdRejReason) {
		addBodyField(103, _OrdRejReason);
	}
	/**
	* getter method to get OrdRejReason
	*
	* @return int - OrdRejReason
	*/
	public int getOrdRejReason() {
		return (stringToint(getBodyFieldValue(103)));
	}
	/**
	* getter method to get OrdRejReasonAsString
	*
	* @return String - OrdRejReasonAsString
	*/
	public String getOrdRejReasonAsString() {
		return (getBodyFieldValue(103));
	}
	/**
	* setter method to set ExecRestatementReason
	*
	* @param int - ExecRestatementReason
	*/
	public void setExecRestatementReason(int _ExecRestatementReason) {
		addBodyField(378, getString(_ExecRestatementReason));
	}
	/**
	* setter method to set ExecRestatementReason
	*
	* @param String - ExecRestatementReason
	*/
	public void setExecRestatementReason(String _ExecRestatementReason) {
		addBodyField(378, _ExecRestatementReason);
	}
	/**
	* getter method to get ExecRestatementReason
	*
	* @return int - ExecRestatementReason
	*/
	public int getExecRestatementReason() {
		return (stringToint(getBodyFieldValue(378)));
	}
	/**
	* getter method to get ExecRestatementReasonAsString
	*
	* @return String - ExecRestatementReasonAsString
	*/
	public String getExecRestatementReasonAsString() {
		return (getBodyFieldValue(378));
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
	* getter method to get Account
	*
	* @return String - Account
	*/
	public String getAccount() {
		return (getBodyFieldValue(1));
	}
	/**
	* setter method to set SettImntTyp
	*
	* @param String - SettImntTyp
	*/
	public void setSettImntTyp(String _SettImntTyp) {
		addBodyField(63, _SettImntTyp);
	}
	/**
	* getter method to get SettImntTyp
	*
	* @return String - SettImntTyp
	*/
	public String getSettImntTyp() {
		return (getBodyFieldValue(63));
	}
	/**
	* setter method to set FutSettDate
	*
	* @param Date - FutSettDate
	*/
	public void setFutSettDate(Date _FutSettDate) {
		addBodyField(64, getLocalMktDateString(_FutSettDate));
	}
	/**
	* setter method to set FutSettDate
	*
	* @param String - FutSettDate
	*/
	public void setFutSettDate(String _FutSettDate) {
		addBodyField(64, _FutSettDate);
	}
	/**
	* getter method to get FutSettDate
	*
	* @return Date - FutSettDate
	*/
	public Date getFutSettDate() {
		return (LocalMktDateStringToDate(getBodyFieldValue(64)));
	}
	/**
	* getter method to get FutSettDateAsString
	*
	* @return String - FutSettDateAsString
	*/
	public String getFutSettDateAsString() {
		return (getBodyFieldValue(64));
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
	* setter method to set SymbolSfx
	*
	* @param String - SymbolSfx
	*/
	public void setSymbolSfx(String _SymbolSfx) {
		addBodyField(65, _SymbolSfx);
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
	* setter method to set SecurityID
	*
	* @param String - SecurityID
	*/
	public void setSecurityID(String _SecurityID) {
		addBodyField(48, _SecurityID);
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
	* setter method to set IDSource
	*
	* @param String - IDSource
	*/
	public void setIDSource(String _IDSource) {
		addBodyField(22, _IDSource);
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
	* setter method to set MaturityMonthYear
	*
	* @param Date - MaturityMonthYear
	*/
	public void setMaturityMonthYear(Date _MaturityMonthYear) {
		addBodyField(200, getMonthYearString(_MaturityMonthYear));
	}
	/**
	* setter method to set MaturityMonthYear
	*
	* @param String - MaturityMonthYear
	*/
	public void setMaturityMonthYear(String _MaturityMonthYear) {
		addBodyField(200, _MaturityMonthYear);
	}
	/**
	* getter method to get MaturityMonthYear
	*
	* @return Date - MaturityMonthYear
	*/
	public Date getMaturityMonthYear() {
		return (MonthYearStringToDate(getBodyFieldValue(200)));
	}
	/**
	* getter method to get MaturityMonthYearAsString
	*
	* @return String - MaturityMonthYearAsString
	*/
	public String getMaturityMonthYearAsString() {
		return (getBodyFieldValue(200));
	}
	/**
	* setter method to set MaturiyDay
	*
	* @param int - MaturiyDay
	*/
	public void setMaturiyDay(int _MaturiyDay) {
		addBodyField(205, getString(_MaturiyDay));
	}
	/**
	* setter method to set MaturiyDay
	*
	* @param String - MaturiyDay
	*/
	public void setMaturiyDay(String _MaturiyDay) {
		addBodyField(205, _MaturiyDay);
	}
	/**
	* getter method to get MaturiyDay
	*
	* @return int - MaturiyDay
	*/
	public int getMaturiyDay() {
		return (stringToint(getBodyFieldValue(205)));
	}
	/**
	* getter method to get MaturiyDayAsString
	*
	* @return String - MaturiyDayAsString
	*/
	public String getMaturiyDayAsString() {
		return (getBodyFieldValue(205));
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
	* setter method to set OptAttribute
	*
	* @param String - OptAttribute
	*/
	public void setOptAttribute(String _OptAttribute) {
		addBodyField(206, _OptAttribute);
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
	* setter method to set SecurityExchange
	*
	* @param String - SecurityExchange
	*/
	public void setSecurityExchange(String _SecurityExchange) {
		addBodyField(207, _SecurityExchange);
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
	* setter method to set Issuer
	*
	* @param String - Issuer
	*/
	public void setIssuer(String _Issuer) {
		addBodyField(106, _Issuer);
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
	* setter method to set Price
	*
	* @param float - Price
	*/
	public void setPrice(float _Price) {
		addBodyField(44, getString(_Price));
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
	/**
	* setter method to set StopPx
	*
	* @param float - StopPx
	*/
	public void setStopPx(float _StopPx) {
		addBodyField(99, getString(_StopPx));
	}
	/**
	* setter method to set StopPx
	*
	* @param String - StopPx
	*/
	public void setStopPx(String _StopPx) {
		addBodyField(99, _StopPx);
	}
	/**
	* getter method to get StopPx
	*
	* @return float - StopPx
	*/
	public float getStopPx() {
		return (stringTofloat(getBodyFieldValue(99)));
	}
	/**
	* getter method to get StopPxAsString
	*
	* @return String - StopPxAsString
	*/
	public String getStopPxAsString() {
		return (getBodyFieldValue(99));
	}
	/**
	* setter method to set PegDifference
	*
	* @param float - PegDifference
	*/
	public void setPegDifference(float _PegDifference) {
		addBodyField(211, getString(_PegDifference));
	}
	/**
	* setter method to set PegDifference
	*
	* @param String - PegDifference
	*/
	public void setPegDifference(String _PegDifference) {
		addBodyField(211, _PegDifference);
	}
	/**
	* getter method to get PegDifference
	*
	* @return float - PegDifference
	*/
	public float getPegDifference() {
		return (stringTofloat(getBodyFieldValue(211)));
	}
	/**
	* getter method to get PegDifferenceAsString
	*
	* @return String - PegDifferenceAsString
	*/
	public String getPegDifferenceAsString() {
		return (getBodyFieldValue(211));
	}
	/**
	* setter method to set DiscretionInst
	*
	* @param String - DiscretionInst
	*/
	public void setDiscretionInst(String _DiscretionInst) {
		addBodyField(388, _DiscretionInst);
	}
	/**
	* getter method to get DiscretionInst
	*
	* @return String - DiscretionInst
	*/
	public String getDiscretionInst() {
		return (getBodyFieldValue(388));
	}
	/**
	* setter method to set DiscretionOffset
	*
	* @param float - DiscretionOffset
	*/
	public void setDiscretionOffset(float _DiscretionOffset) {
		addBodyField(389, getString(_DiscretionOffset));
	}
	/**
	* setter method to set DiscretionOffset
	*
	* @param String - DiscretionOffset
	*/
	public void setDiscretionOffset(String _DiscretionOffset) {
		addBodyField(389, _DiscretionOffset);
	}
	/**
	* getter method to get DiscretionOffset
	*
	* @return float - DiscretionOffset
	*/
	public float getDiscretionOffset() {
		return (stringTofloat(getBodyFieldValue(389)));
	}
	/**
	* getter method to get DiscretionOffsetAsString
	*
	* @return String - DiscretionOffsetAsString
	*/
	public String getDiscretionOffsetAsString() {
		return (getBodyFieldValue(389));
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
	* setter method to set ComplianceID
	*
	* @param String - ComplianceID
	*/
	public void setComplianceID(String _ComplianceID) {
		addBodyField(376, _ComplianceID);
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
	* setter method to set SolicitedFlag
	*
	* @param boolean - SolicitedFlag
	*/
	public void setSolicitedFlag(boolean _SolicitedFlag) {
		addBodyField(377, getString(_SolicitedFlag));
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
	* setter method to set EffectiveTime
	*
	* @param Date - EffectiveTime
	*/
	public void setEffectiveTime(Date _EffectiveTime) {
		addBodyField(168, getUTCTimeStampString(_EffectiveTime));
	}
	/**
	* setter method to set EffectiveTime
	*
	* @param String - EffectiveTime
	*/
	public void setEffectiveTime(String _EffectiveTime) {
		addBodyField(168, _EffectiveTime);
	}
	/**
	* getter method to get EffectiveTime
	*
	* @return Date - EffectiveTime
	*/
	public Date getEffectiveTime() {
		return (UTCTimeStampStringToDate(getBodyFieldValue(168)));
	}
	/**
	* getter method to get EffectiveTimeAsString
	*
	* @return String - EffectiveTimeAsString
	*/
	public String getEffectiveTimeAsString() {
		return (getBodyFieldValue(168));
	}
	/**
	* setter method to set ExpireDate
	*
	* @param Date - ExpireDate
	*/
	public void setExpireDate(Date _ExpireDate) {
		addBodyField(432, getLocalMktDateString(_ExpireDate));
	}
	/**
	* setter method to set ExpireDate
	*
	* @param String - ExpireDate
	*/
	public void setExpireDate(String _ExpireDate) {
		addBodyField(432, _ExpireDate);
	}
	/**
	* getter method to get ExpireDate
	*
	* @return Date - ExpireDate
	*/
	public Date getExpireDate() {
		return (LocalMktDateStringToDate(getBodyFieldValue(432)));
	}
	/**
	* getter method to get ExpireDateAsString
	*
	* @return String - ExpireDateAsString
	*/
	public String getExpireDateAsString() {
		return (getBodyFieldValue(432));
	}
	/**
	* setter method to set ExpireTime
	*
	* @param Date - ExpireTime
	*/
	public void setExpireTime(Date _ExpireTime) {
		addBodyField(126, getUTCTimeStampString(_ExpireTime));
	}
	/**
	* setter method to set ExpireTime
	*
	* @param String - ExpireTime
	*/
	public void setExpireTime(String _ExpireTime) {
		addBodyField(126, _ExpireTime);
	}
	/**
	* getter method to get ExpireTime
	*
	* @return Date - ExpireTime
	*/
	public Date getExpireTime() {
		return (UTCTimeStampStringToDate(getBodyFieldValue(126)));
	}
	/**
	* getter method to get ExpireTimeAsString
	*
	* @return String - ExpireTimeAsString
	*/
	public String getExpireTimeAsString() {
		return (getBodyFieldValue(126));
	}
	/**
	* setter method to set ExecInst
	*
	* @param String - ExecInst
	*/
	public void setExecInst(String _ExecInst) {
		addBodyField(18, _ExecInst);
	}
	/**
	* getter method to get ExecInst
	*
	* @return String - ExecInst
	*/
	public String getExecInst() {
		return (getBodyFieldValue(18));
	}
	/**
	* setter method to set OrderCapacity
	*
	* @param String - OrderCapacity
	*/
	public void setOrderCapacity(String _OrderCapacity) {
		addBodyField(47, _OrderCapacity);
	}
	/**
	* getter method to get OrderCapacity
	*
	* @return String - OrderCapacity
	*/
	public String getOrderCapacity() {
		return (getBodyFieldValue(47));
	}
	/**
	* setter method to set LastShares
	*
	* @param float - LastShares
	*/
	public void setLastShares(float _LastShares) {
		addBodyField(32, getString(_LastShares));
	}
	/**
	* setter method to set LastShares
	*
	* @param String - LastShares
	*/
	public void setLastShares(String _LastShares) {
		addBodyField(32, _LastShares);
	}
	/**
	* getter method to get LastShares
	*
	* @return float - LastShares
	*/
	public float getLastShares() {
		return (stringTofloat(getBodyFieldValue(32)));
	}
	/**
	* getter method to get LastSharesAsString
	*
	* @return String - LastSharesAsString
	*/
	public String getLastSharesAsString() {
		return (getBodyFieldValue(32));
	}
	/**
	* setter method to set LastPx
	*
	* @param float - LastPx
	*/
	public void setLastPx(float _LastPx) {
		addBodyField(31, getString(_LastPx));
	}
	/**
	* setter method to set LastPx
	*
	* @param String - LastPx
	*/
	public void setLastPx(String _LastPx) {
		addBodyField(31, _LastPx);
	}
	/**
	* getter method to get LastPx
	*
	* @return float - LastPx
	*/
	public float getLastPx() {
		return (stringTofloat(getBodyFieldValue(31)));
	}
	/**
	* getter method to get LastPxAsString
	*
	* @return String - LastPxAsString
	*/
	public String getLastPxAsString() {
		return (getBodyFieldValue(31));
	}
	/**
	* setter method to set LastSpotRate
	*
	* @param float - LastSpotRate
	*/
	public void setLastSpotRate(float _LastSpotRate) {
		addBodyField(194, getString(_LastSpotRate));
	}
	/**
	* setter method to set LastSpotRate
	*
	* @param String - LastSpotRate
	*/
	public void setLastSpotRate(String _LastSpotRate) {
		addBodyField(194, _LastSpotRate);
	}
	/**
	* getter method to get LastSpotRate
	*
	* @return float - LastSpotRate
	*/
	public float getLastSpotRate() {
		return (stringTofloat(getBodyFieldValue(194)));
	}
	/**
	* getter method to get LastSpotRateAsString
	*
	* @return String - LastSpotRateAsString
	*/
	public String getLastSpotRateAsString() {
		return (getBodyFieldValue(194));
	}
	/**
	* setter method to set LastForwardPoints
	*
	* @param float - LastForwardPoints
	*/
	public void setLastForwardPoints(float _LastForwardPoints) {
		addBodyField(195, getString(_LastForwardPoints));
	}
	/**
	* setter method to set LastForwardPoints
	*
	* @param String - LastForwardPoints
	*/
	public void setLastForwardPoints(String _LastForwardPoints) {
		addBodyField(195, _LastForwardPoints);
	}
	/**
	* getter method to get LastForwardPoints
	*
	* @return float - LastForwardPoints
	*/
	public float getLastForwardPoints() {
		return (stringTofloat(getBodyFieldValue(195)));
	}
	/**
	* getter method to get LastForwardPointsAsString
	*
	* @return String - LastForwardPointsAsString
	*/
	public String getLastForwardPointsAsString() {
		return (getBodyFieldValue(195));
	}
	/**
	* setter method to set LastMkt
	*
	* @param String - LastMkt
	*/
	public void setLastMkt(String _LastMkt) {
		addBodyField(30, _LastMkt);
	}
	/**
	* getter method to get LastMkt
	*
	* @return String - LastMkt
	*/
	public String getLastMkt() {
		return (getBodyFieldValue(30));
	}
	/**
	* setter method to set TradingSessionID
	*
	* @param String - TradingSessionID
	*/
	public void setTradingSessionID(String _TradingSessionID) {
		addBodyField(336, _TradingSessionID);
	}
	/**
	* getter method to get TradingSessionID
	*
	* @return String - TradingSessionID
	*/
	public String getTradingSessionID() {
		return (getBodyFieldValue(336));
	}
	/**
	* setter method to set LastCapacity
	*
	* @param String - LastCapacity
	*/
	public void setLastCapacity(String _LastCapacity) {
		addBodyField(29, _LastCapacity);
	}
	/**
	* getter method to get LastCapacity
	*
	* @return String - LastCapacity
	*/
	public String getLastCapacity() {
		return (getBodyFieldValue(29));
	}
	/**
	* setter method to set LeavesQty
	*
	* @param float - LeavesQty
	*/
	public void setLeavesQty(float _LeavesQty) {
		addBodyField(151, getString(_LeavesQty));
	}
	/**
	* setter method to set LeavesQty
	*
	* @param String - LeavesQty
	*/
	public void setLeavesQty(String _LeavesQty) {
		addBodyField(151, _LeavesQty);
	}
	/**
	* getter method to get LeavesQty
	*
	* @return float - LeavesQty
	*/
	public float getLeavesQty() {
		return (stringTofloat(getBodyFieldValue(151)));
	}
	/**
	* getter method to get LeavesQtyAsString
	*
	* @return String - LeavesQtyAsString
	*/
	public String getLeavesQtyAsString() {
		return (getBodyFieldValue(151));
	}
	/**
	* setter method to set CumQty
	*
	* @param float - CumQty
	*/
	public void setCumQty(float _CumQty) {
		addBodyField(14, getString(_CumQty));
	}
	/**
	* setter method to set CumQty
	*
	* @param String - CumQty
	*/
	public void setCumQty(String _CumQty) {
		addBodyField(14, _CumQty);
	}
	/**
	* getter method to get CumQty
	*
	* @return float - CumQty
	*/
	public float getCumQty() {
		return (stringTofloat(getBodyFieldValue(14)));
	}
	/**
	* getter method to get CumQtyAsString
	*
	* @return String - CumQtyAsString
	*/
	public String getCumQtyAsString() {
		return (getBodyFieldValue(14));
	}
	/**
	* setter method to set AvgPx
	*
	* @param float - AvgPx
	*/
	public void setAvgPx(float _AvgPx) {
		addBodyField(6, getString(_AvgPx));
	}
	/**
	* setter method to set AvgPx
	*
	* @param String - AvgPx
	*/
	public void setAvgPx(String _AvgPx) {
		addBodyField(6, _AvgPx);
	}
	/**
	* getter method to get AvgPx
	*
	* @return float - AvgPx
	*/
	public float getAvgPx() {
		return (stringTofloat(getBodyFieldValue(6)));
	}
	/**
	* getter method to get AvgPxAsString
	*
	* @return String - AvgPxAsString
	*/
	public String getAvgPxAsString() {
		return (getBodyFieldValue(6));
	}
	/**
	* setter method to set DayOrderQty
	*
	* @param float - DayOrderQty
	*/
	public void setDayOrderQty(float _DayOrderQty) {
		addBodyField(424, getString(_DayOrderQty));
	}
	/**
	* setter method to set DayOrderQty
	*
	* @param String - DayOrderQty
	*/
	public void setDayOrderQty(String _DayOrderQty) {
		addBodyField(424, _DayOrderQty);
	}
	/**
	* getter method to get DayOrderQty
	*
	* @return float - DayOrderQty
	*/
	public float getDayOrderQty() {
		return (stringTofloat(getBodyFieldValue(424)));
	}
	/**
	* getter method to get DayOrderQtyAsString
	*
	* @return String - DayOrderQtyAsString
	*/
	public String getDayOrderQtyAsString() {
		return (getBodyFieldValue(424));
	}
	/**
	* setter method to set DayCumQty
	*
	* @param float - DayCumQty
	*/
	public void setDayCumQty(float _DayCumQty) {
		addBodyField(425, getString(_DayCumQty));
	}
	/**
	* setter method to set DayCumQty
	*
	* @param String - DayCumQty
	*/
	public void setDayCumQty(String _DayCumQty) {
		addBodyField(425, _DayCumQty);
	}
	/**
	* getter method to get DayCumQty
	*
	* @return float - DayCumQty
	*/
	public float getDayCumQty() {
		return (stringTofloat(getBodyFieldValue(425)));
	}
	/**
	* getter method to get DayCumQtyAsString
	*
	* @return String - DayCumQtyAsString
	*/
	public String getDayCumQtyAsString() {
		return (getBodyFieldValue(425));
	}
	/**
	* setter method to set DayAvgPx
	*
	* @param float - DayAvgPx
	*/
	public void setDayAvgPx(float _DayAvgPx) {
		addBodyField(426, getString(_DayAvgPx));
	}
	/**
	* setter method to set DayAvgPx
	*
	* @param String - DayAvgPx
	*/
	public void setDayAvgPx(String _DayAvgPx) {
		addBodyField(426, _DayAvgPx);
	}
	/**
	* getter method to get DayAvgPx
	*
	* @return float - DayAvgPx
	*/
	public float getDayAvgPx() {
		return (stringTofloat(getBodyFieldValue(426)));
	}
	/**
	* getter method to get DayAvgPxAsString
	*
	* @return String - DayAvgPxAsString
	*/
	public String getDayAvgPxAsString() {
		return (getBodyFieldValue(426));
	}
	/**
	* setter method to set GTBookingInst
	*
	* @param int - GTBookingInst
	*/
	public void setGTBookingInst(int _GTBookingInst) {
		addBodyField(427, getString(_GTBookingInst));
	}
	/**
	* setter method to set GTBookingInst
	*
	* @param String - GTBookingInst
	*/
	public void setGTBookingInst(String _GTBookingInst) {
		addBodyField(427, _GTBookingInst);
	}
	/**
	* getter method to get GTBookingInst
	*
	* @return int - GTBookingInst
	*/
	public int getGTBookingInst() {
		return (stringToint(getBodyFieldValue(427)));
	}
	/**
	* getter method to get GTBookingInstAsString
	*
	* @return String - GTBookingInstAsString
	*/
	public String getGTBookingInstAsString() {
		return (getBodyFieldValue(427));
	}
	/**
	* setter method to set TradeDate
	*
	* @param Date - TradeDate
	*/
	public void setTradeDate(Date _TradeDate) {
		addBodyField(75, getLocalMktDateString(_TradeDate));
	}
	/**
	* setter method to set TradeDate
	*
	* @param String - TradeDate
	*/
	public void setTradeDate(String _TradeDate) {
		addBodyField(75, _TradeDate);
	}
	/**
	* getter method to get TradeDate
	*
	* @return Date - TradeDate
	*/
	public Date getTradeDate() {
		return (LocalMktDateStringToDate(getBodyFieldValue(75)));
	}
	/**
	* getter method to get TradeDateAsString
	*
	* @return String - TradeDateAsString
	*/
	public String getTradeDateAsString() {
		return (getBodyFieldValue(75));
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
	* setter method to set ReportToExch
	*
	* @param boolean - ReportToExch
	*/
	public void setReportToExch(boolean _ReportToExch) {
		addBodyField(113, getString(_ReportToExch));
	}
	/**
	* setter method to set ReportToExch
	*
	* @param String - ReportToExch
	*/
	public void setReportToExch(String _ReportToExch) {
		addBodyField(113, _ReportToExch);
	}
	/**
	* getter method to get ReportToExch
	*
	* @return boolean - ReportToExch
	*/
	public boolean getReportToExch() {
		return (stringToboolean(getBodyFieldValue(113)));
	}
	/**
	* getter method to get ReportToExchAsString
	*
	* @return String - ReportToExchAsString
	*/
	public String getReportToExchAsString() {
		return (getBodyFieldValue(113));
	}
	/**
	* setter method to set Commission
	*
	* @param float - Commission
	*/
	public void setCommission(float _Commission) {
		addBodyField(12, getString(_Commission));
	}
	/**
	* setter method to set Commission
	*
	* @param String - Commission
	*/
	public void setCommission(String _Commission) {
		addBodyField(12, _Commission);
	}
	/**
	* getter method to get Commission
	*
	* @return float - Commission
	*/
	public float getCommission() {
		return (stringTofloat(getBodyFieldValue(12)));
	}
	/**
	* getter method to get CommissionAsString
	*
	* @return String - CommissionAsString
	*/
	public String getCommissionAsString() {
		return (getBodyFieldValue(12));
	}
	/**
	* setter method to set CommType
	*
	* @param String - CommType
	*/
	public void setCommType(String _CommType) {
		addBodyField(13, _CommType);
	}
	/**
	* getter method to get CommType
	*
	* @return String - CommType
	*/
	public String getCommType() {
		return (getBodyFieldValue(13));
	}
	/**
	* setter method to set GrossTradeAmt
	*
	* @param float - GrossTradeAmt
	*/
	public void setGrossTradeAmt(float _GrossTradeAmt) {
		addBodyField(381, getString(_GrossTradeAmt));
	}
	/**
	* setter method to set GrossTradeAmt
	*
	* @param String - GrossTradeAmt
	*/
	public void setGrossTradeAmt(String _GrossTradeAmt) {
		addBodyField(381, _GrossTradeAmt);
	}
	/**
	* getter method to get GrossTradeAmt
	*
	* @return float - GrossTradeAmt
	*/
	public float getGrossTradeAmt() {
		return (stringTofloat(getBodyFieldValue(381)));
	}
	/**
	* getter method to get GrossTradeAmtAsString
	*
	* @return String - GrossTradeAmtAsString
	*/
	public String getGrossTradeAmtAsString() {
		return (getBodyFieldValue(381));
	}
	/**
	* setter method to set SettlCurrAmt
	*
	* @param float - SettlCurrAmt
	*/
	public void setSettlCurrAmt(float _SettlCurrAmt) {
		addBodyField(119, getString(_SettlCurrAmt));
	}
	/**
	* setter method to set SettlCurrAmt
	*
	* @param String - SettlCurrAmt
	*/
	public void setSettlCurrAmt(String _SettlCurrAmt) {
		addBodyField(119, _SettlCurrAmt);
	}
	/**
	* getter method to get SettlCurrAmt
	*
	* @return float - SettlCurrAmt
	*/
	public float getSettlCurrAmt() {
		return (stringTofloat(getBodyFieldValue(119)));
	}
	/**
	* getter method to get SettlCurrAmtAsString
	*
	* @return String - SettlCurrAmtAsString
	*/
	public String getSettlCurrAmtAsString() {
		return (getBodyFieldValue(119));
	}
	/**
	* setter method to set SettlCurrFxRate
	*
	* @param float - SettlCurrFxRate
	*/
	public void setSettlCurrFxRate(float _SettlCurrFxRate) {
		addBodyField(155, getString(_SettlCurrFxRate));
	}
	/**
	* setter method to set SettlCurrFxRate
	*
	* @param String - SettlCurrFxRate
	*/
	public void setSettlCurrFxRate(String _SettlCurrFxRate) {
		addBodyField(155, _SettlCurrFxRate);
	}
	/**
	* getter method to get SettlCurrFxRate
	*
	* @return float - SettlCurrFxRate
	*/
	public float getSettlCurrFxRate() {
		return (stringTofloat(getBodyFieldValue(155)));
	}
	/**
	* getter method to get SettlCurrFxRateAsString
	*
	* @return String - SettlCurrFxRateAsString
	*/
	public String getSettlCurrFxRateAsString() {
		return (getBodyFieldValue(155));
	}
	/**
	* setter method to set SettlCurrFxRateCalc
	*
	* @param String - SettlCurrFxRateCalc
	*/
	public void setSettlCurrFxRateCalc(String _SettlCurrFxRateCalc) {
		addBodyField(156, _SettlCurrFxRateCalc);
	}
	/**
	* getter method to get SettlCurrFxRateCalc
	*
	* @return String - SettlCurrFxRateCalc
	*/
	public String getSettlCurrFxRateCalc() {
		return (getBodyFieldValue(156));
	}
	/**
	* setter method to set HandlInst
	*
	* @param String - HandlInst
	*/
	public void setHandlInst(String _HandlInst) {
		addBodyField(21, _HandlInst);
	}
	/**
	* getter method to get HandlInst
	*
	* @return String - HandlInst
	*/
	public String getHandlInst() {
		return (getBodyFieldValue(21));
	}
	/**
	* setter method to set MinQty
	*
	* @param float - MinQty
	*/
	public void setMinQty(float _MinQty) {
		addBodyField(110, getString(_MinQty));
	}
	/**
	* setter method to set MinQty
	*
	* @param String - MinQty
	*/
	public void setMinQty(String _MinQty) {
		addBodyField(110, _MinQty);
	}
	/**
	* getter method to get MinQty
	*
	* @return float - MinQty
	*/
	public float getMinQty() {
		return (stringTofloat(getBodyFieldValue(110)));
	}
	/**
	* getter method to get MinQtyAsString
	*
	* @return String - MinQtyAsString
	*/
	public String getMinQtyAsString() {
		return (getBodyFieldValue(110));
	}
	/**
	* setter method to set MaxFloor
	*
	* @param float - MaxFloor
	*/
	public void setMaxFloor(float _MaxFloor) {
		addBodyField(111, getString(_MaxFloor));
	}
	/**
	* setter method to set MaxFloor
	*
	* @param String - MaxFloor
	*/
	public void setMaxFloor(String _MaxFloor) {
		addBodyField(111, _MaxFloor);
	}
	/**
	* getter method to get MaxFloor
	*
	* @return float - MaxFloor
	*/
	public float getMaxFloor() {
		return (stringTofloat(getBodyFieldValue(111)));
	}
	/**
	* getter method to get MaxFloorAsString
	*
	* @return String - MaxFloorAsString
	*/
	public String getMaxFloorAsString() {
		return (getBodyFieldValue(111));
	}
	/**
	* setter method to set OpenClose
	*
	* @param String - OpenClose
	*/
	public void setOpenClose(String _OpenClose) {
		addBodyField(77, _OpenClose);
	}
	/**
	* getter method to get OpenClose
	*
	* @return String - OpenClose
	*/
	public String getOpenClose() {
		return (getBodyFieldValue(77));
	}
	/**
	* setter method to set MaxShow
	*
	* @param float - MaxShow
	*/
	public void setMaxShow(float _MaxShow) {
		addBodyField(210, getString(_MaxShow));
	}
	/**
	* setter method to set MaxShow
	*
	* @param String - MaxShow
	*/
	public void setMaxShow(String _MaxShow) {
		addBodyField(210, _MaxShow);
	}
	/**
	* getter method to get MaxShow
	*
	* @return float - MaxShow
	*/
	public float getMaxShow() {
		return (stringTofloat(getBodyFieldValue(210)));
	}
	/**
	* getter method to get MaxShowAsString
	*
	* @return String - MaxShowAsString
	*/
	public String getMaxShowAsString() {
		return (getBodyFieldValue(210));
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
	* setter method to set FutSettDate2
	*
	* @param Date - FutSettDate2
	*/
	public void setFutSettDate2(Date _FutSettDate2) {
		addBodyField(193, getLocalMktDateString(_FutSettDate2));
	}
	/**
	* setter method to set FutSettDate2
	*
	* @param String - FutSettDate2
	*/
	public void setFutSettDate2(String _FutSettDate2) {
		addBodyField(193, _FutSettDate2);
	}
	/**
	* getter method to get FutSettDate2
	*
	* @return Date - FutSettDate2
	*/
	public Date getFutSettDate2() {
		return (LocalMktDateStringToDate(getBodyFieldValue(193)));
	}
	/**
	* getter method to get FutSettDate2AsString
	*
	* @return String - FutSettDate2AsString
	*/
	public String getFutSettDate2AsString() {
		return (getBodyFieldValue(193));
	}
	/**
	* setter method to set OrderQty2
	*
	* @param float - OrderQty2
	*/
	public void setOrderQty2(float _OrderQty2) {
		addBodyField(192, getString(_OrderQty2));
	}
	/**
	* setter method to set OrderQty2
	*
	* @param String - OrderQty2
	*/
	public void setOrderQty2(String _OrderQty2) {
		addBodyField(192, _OrderQty2);
	}
	/**
	* getter method to get OrderQty2
	*
	* @return float - OrderQty2
	*/
	public float getOrderQty2() {
		return (stringTofloat(getBodyFieldValue(192)));
	}
	/**
	* getter method to get OrderQty2AsString
	*
	* @return String - OrderQty2AsString
	*/
	public String getOrderQty2AsString() {
		return (getBodyFieldValue(192));
	}
	/**
	* setter method to set ClearingFirm
	*
	* @param String - ClearingFirm
	*/
	public void setClearingFirm(String _ClearingFirm) {
		addBodyField(439, _ClearingFirm);
	}
	/**
	* getter method to get ClearingFirm
	*
	* @return String - ClearingFirm
	*/
	public String getClearingFirm() {
		return (getBodyFieldValue(439));
	}
	/**
	* setter method to set ClearingAccount
	*
	* @param String - ClearingAccount
	*/
	public void setClearingAccount(String _ClearingAccount) {
		addBodyField(440, _ClearingAccount);
	}
	/**
	* getter method to get ClearingAccount
	*
	* @return String - ClearingAccount
	*/
	public String getClearingAccount() {
		return (getBodyFieldValue(440));
	}
	/**
	* setter method to set MultiLegReportingType
	*
	* @param String - MultiLegReportingType
	*/
	public void setMultiLegReportingType(String _MultiLegReportingType) {
		addBodyField(442, _MultiLegReportingType);
	}
	/**
	* getter method to get MultiLegReportingType
	*
	* @return String - MultiLegReportingType
	*/
	public String getMultiLegReportingType() {
		return (getBodyFieldValue(442));
	}

}
