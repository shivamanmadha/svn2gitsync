package com.exsys.fix.message;

//05162007 - added 9139 (OriginatorUserId) tag per ICE

import java.util.Date;

/**
* This class is used to represent the FIX message CancelReplace
*
*/
public class FixCancelReplace extends FixMessage {
	/**
	* Constructor to construct FixCancelReplace object 
	*
	*/
	public FixCancelReplace() {
		setMessageType(FixConstants.FIX_MSGTYPE_CANCELREPLACE);
		setMsgType(FixConstants.FIX_MSGTYPE_CANCELREPLACE);
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
	* setter method to set CtiCode
	*
	* @param int - CtiCode
	*/
	public void setCtiCode(int _CtiCode) {
		addBodyField(9702, getString(_CtiCode));
	}
	/**
	* setter method to set CtiCode
	*
	* @param String - CtiCode
	*/
	public void setCtiCode(String _CtiCode) {
		addBodyField(9702, _CtiCode);
	}
	/**
	* getter method to get Code
	*
	* @return int - Code
	*/
	public int CtiCode() {
		return (stringToint(getBodyFieldValue(9702)));
	}
	/**
	* getter method to get CtiCodeAsString
	*
	* @return String - CtiCodeAsString
	*/
	public String getCtiCodeAsString() {
		return (getBodyFieldValue(9702));
	}
	/**
	* setter method to set FeeBilling
	*
	* @param String - FeeBilling
	*/
	public void setFeeBilling(String _FeeBilling) {
		addBodyField(9706, _FeeBilling);
	}
	/**
	* getter method to get FeeBilling
	*
	* @return String - FeeBilling
	*/
	public String getFeeBilling() {
		return (getBodyFieldValue(9706));
	}

	/**
	* getter method to get GiveupFirm
	*
	* @return String - GiveupFirm
	*/
	public String getGiveupFirm() {
		return (getBodyFieldValue(9707));
	}
	/**
	* setter method to set GiveupFirm
	*
	* @param String - GiveupFirm
	*/
	public void setGiveupFirm(String _GiveupFirm) {
		addBodyField(9707, _GiveupFirm);
	}
	/**
	* getter method to get CmtaGiveupCD
	*
	* @return String - CmtaGiveupCD
	*/
	public String getCmtaGiveupCD() {
		return (getBodyFieldValue(9708));
	}
	/**
	* setter method to set CmtaGiveupCD
	*
	* @param String - CmtaGiveupCD
	*/
	public void setCmtaGiveupCD(String _CmtaGiveupCD) {
		addBodyField(9708, _CmtaGiveupCD);
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
	* setter method to set OmnibusAccount
	*
	* @param String - OmnibusAccount
	*/
	public void setOmnibusAccount(String _OmnibusAccount) {
		addBodyField(9701, _OmnibusAccount);
	}
	/**
	* getter method to get OmnibusAccount
	*
	* @return String - OmnibusAccount
	*/
	public String getOmnibusAccount() {
		return (getBodyFieldValue(9701));
	}
	/**
	* setter method to set IFMOverride
	*
	* @param String - IFMOverride
	*/
	public void setIFMOverride(String _IFMOverride) {
		addBodyField(9768, _IFMOverride);
	}
	/**
	* getter method to get IFMOverride
	*
	* @return String - IFMOverride
	*/
	public String getIFMOverride() {
		return (getBodyFieldValue(9768));
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
	* getter method to get ClearingAccount
	*
	* @return String - ClearingAccount
	*/
	public String getClearingAccount() {
		return (getBodyFieldValue(440));
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
	* getter method to get CommType
	*
	* @return String - CommType
	*/
	public String getCommType() {
		return (getBodyFieldValue(13));
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
	* getter method to get CoveredOrUncovered
	*
	* @return int - CoveredOrUncovered
	*/
	public int getCoveredOrUncovered() {
		return (stringToint(getBodyFieldValue(203)));
	}
	/**
	* getter method to get CoveredOrUncoveredAsString
	*
	* @return String - CoveredOrUncoveredAsString
	*/
	public String getCoveredOrUncoveredAsString() {
		return (getBodyFieldValue(203));
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
	* getter method to get CustomerOrFirm
	*
	* @return int - CustomerOrFirm
	*/
	public int getCustomerOrFirm() {
		return (stringToint(getBodyFieldValue(204)));
	}
	/**
	* getter method to get CustomerOrFirmAsString
	*
	* @return String - CustomerOrFirmAsString
	*/
	public String getCustomerOrFirmAsString() {
		return (getBodyFieldValue(204));
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
	* getter method to get ExDestination
	*
	* @return String - ExDestination
	*/
	public String getExDestination() {
		return (getBodyFieldValue(100));
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
	* getter method to get ExecInst
	*
	* @return String - ExecInst
	*/
	public String getExecInst() {
		return (getBodyFieldValue(18));
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
	* getter method to get ForexReq
	*
	* @return boolean - ForexReq
	*/
	public boolean getForexReq() {
		return (stringToboolean(getBodyFieldValue(121)));
	}
	/**
	* getter method to get ForexReqAsString
	*
	* @return String - ForexReqAsString
	*/
	public String getForexReqAsString() {
		return (getBodyFieldValue(121));
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
	* getter method to get FutSettDateAsString
	*
	* @return String - FutSettDateAsString
	*/
	public String getFutSettDateAsString() {
		return (getBodyFieldValue(64));
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
	* getter method to get LocateReqd
	*
	* @return boolean - LocateReqd
	*/
	public boolean getLocateReqd() {
		return (stringToboolean(getBodyFieldValue(114)));
	}
	/**
	* getter method to get LocateReqdAsString
	*
	* @return String - LocateReqdAsString
	*/
	public String getLocateReqdAsString() {
		return (getBodyFieldValue(114));
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
	* getter method to get OpenClose
	*
	* @return String - OpenClose
	*/
	public String getOpenClose() {
		return (getBodyFieldValue(77));
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
	* getter method to get OrderCapacity
	*
	* @return String - OrderCapacity
	*/
	public String getOrderCapacity() {
		return (getBodyFieldValue(47));
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
	* getter method to get OrderQtyAsString
	*
	* @return String - OrderQtyAsString
	*/
	public String getOrderQtyAsString() {
		return (getBodyFieldValue(38));
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
	* getter method to get OrigClOrderID
	*
	* @return String - OrigClOrderID
	*/
	public String getOrigClOrderID() {
		return (getBodyFieldValue(41));
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
	* getter method to get PrevClosePx
	*
	* @return float - PrevClosePx
	*/
	public float getPrevClosePx() {
		return (stringTofloat(getBodyFieldValue(140)));
	}
	/**
	* getter method to get PrevClosePxAsString
	*
	* @return String - PrevClosePxAsString
	*/
	public String getPrevClosePxAsString() {
		return (getBodyFieldValue(140));
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
	* getter method to get SettlCurrency
	*
	* @return String - SettlCurrency
	*/
	public String getSettlCurrency() {
		return (getBodyFieldValue(120));
	}
	/**
	* getter method to get SettlementTyp
	*
	* @return String - SettlementTyp
	*/
	public String getSettlementTyp() {
		return (getBodyFieldValue(63));
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
	* getter method to get TimeInForce
	*
	* @return String - TimeInForce
	*/
	public String getTimeInForce() {
		return (getBodyFieldValue(59));
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
	* setter method to set ClearingAccount
	*
	* @param String - ClearingAccount
	*/
	public void setClearingAccount(String _ClearingAccount) {
		addBodyField(440, _ClearingAccount);
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
	* setter method to set CommType
	*
	* @param String - CommType
	*/
	public void setCommType(String _CommType) {
		addBodyField(13, _CommType);
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
	* setter method to set CoveredOrUncovered
	*
	* @param int - CoveredOrUncovered
	*/
	public void setCoveredOrUncovered(int _CoveredOrUncovered) {
		addBodyField(203, getString(_CoveredOrUncovered));
	}
	/**
	* setter method to set CoveredOrUncovered
	*
	* @param String - CoveredOrUncovered
	*/
	public void setCoveredOrUncovered(String _CoveredOrUncovered) {
		addBodyField(203, _CoveredOrUncovered);
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
	* setter method to set CustomerOrFirm
	*
	* @param int - CustomerOrFirm
	*/
	public void setCustomerOrFirm(int _CustomerOrFirm) {
		addBodyField(204, getString(_CustomerOrFirm));
	}
	/**
	* setter method to set CustomerOrFirm
	*
	* @param String - CustomerOrFirm
	*/
	public void setCustomerOrFirm(String _CustomerOrFirm) {
		addBodyField(204, _CustomerOrFirm);
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
	* setter method to set EffectiveTime
	*
	* @param String - EffectiveTime
	*/
	public void setEffectiveTime(String _EffectiveTime) {
		addBodyField(168, _EffectiveTime);
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
	* setter method to set ExDestination
	*
	* @param String - ExDestination
	*/
	public void setExDestination(String _ExDestination) {
		addBodyField(100, _ExDestination);
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
	* setter method to set ExecInst
	*
	* @param String - ExecInst
	*/
	public void setExecInst(String _ExecInst) {
		addBodyField(18, _ExecInst);
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
	* setter method to set ExpireDate
	*
	* @param Date - ExpireDate
	*/
	public void setExpireDate(Date _ExpireDate) {
		addBodyField(432, getLocalMktDateString(_ExpireDate));
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
	* setter method to set ExpireTime
	*
	* @param Date - ExpireTime
	*/
	public void setExpireTime(Date _ExpireTime) {
		addBodyField(126, getUTCTimeStampString(_ExpireTime));
	}
	/**
	* setter method to set ForexReq
	*
	* @param String - ForexReq
	*/
	public void setForexReq(String _ForexReq) {
		addBodyField(121, _ForexReq);
	}
	/**
	* setter method to set ForexReq
	*
	* @param boolean - ForexReq
	*/
	public void setForexReq(boolean _ForexReq) {
		addBodyField(121, getString(_ForexReq));
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
	* setter method to set FutSettDate
	*
	* @param Date - FutSettDate
	*/
	public void setFutSettDate(Date _FutSettDate) {
		addBodyField(64, getLocalMktDateString(_FutSettDate));
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
	* setter method to set FutSettDate2
	*
	* @param Date - FutSettDate2
	*/
	public void setFutSettDate2(Date _FutSettDate2) {
		addBodyField(193, getLocalMktDateString(_FutSettDate2));
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
	* setter method to set LocateReqd
	*
	* @param String - LocateReqd
	*/
	public void setLocateReqd(String _LocateReqd) {
		addBodyField(114, _LocateReqd);
	}
	/**
	* setter method to set LocateReqd
	*
	* @param boolean - LocateReqd
	*/
	public void setLocateReqd(boolean _LocateReqd) {
		addBodyField(114, getString(_LocateReqd));
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
	* setter method to set OpenClose
	*
	* @param String - OpenClose
	*/
	public void setOpenClose(String _OpenClose) {
		addBodyField(77, _OpenClose);
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
	* setter method to set OrderCapacity
	*
	* @param String - OrderCapacity
	*/
	public void setOrderCapacity(String _OrderCapacity) {
		addBodyField(47, _OrderCapacity);
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
	* setter method to set OrdType
	*
	* @param String - OrdType
	*/
	public void setOrdType(String _OrdType) {
		addBodyField(40, _OrdType);
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
	* setter method to set PrevClosePx
	*
	* @param float - PrevClosePx
	*/
	public void setPrevClosePx(float _PrevClosePx) {
		addBodyField(140, getString(_PrevClosePx));
	}
	/**
	* setter method to set PrevClosePx
	*
	* @param String - PrevClosePx
	*/
	public void setPrevClosePx(String _PrevClosePx) {
		addBodyField(140, _PrevClosePx);
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
	* setter method to set SettlCurrency
	*
	* @param String - SettlCurrency
	*/
	public void setSettlCurrency(String _SettlCurrency) {
		addBodyField(120, _SettlCurrency);
	}
	/**
	* setter method to set SettlementTyp
	*
	* @param String - SettlementTyp
	*/
	public void setSettlementTyp(String _SettlementTyp) {
		addBodyField(63, _SettlementTyp);
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
	* setter method to set TimeInForce
	*
	* @param String - TimeInForce
	*/
	public void setTimeInForce(String _TimeInForce) {
		addBodyField(59, _TimeInForce);
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
