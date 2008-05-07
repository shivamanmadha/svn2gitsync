package com.exsys.fix.message;

import java.util.*;

/**
* This class is used to represent the FIX message RGNoRelatedSym
*
*/
public class FixRGNoRelatedSym extends FixRepeatedGroup {
	public static ArrayList fieldList = new ArrayList();
	static {
		fieldList.add("311");
		fieldList.add("307");
		fieldList.add("313");
		fieldList.add("314");
		fieldList.add("326");
		fieldList.add("9013");
		fieldList.add("9014");
		fieldList.add("9017");
		fieldList.add("9083");
		fieldList.add("9084");
		fieldList.add("9030");
		fieldList.add("9031");
		fieldList.add("9032");
		fieldList.add("9061");
		fieldList.add("9091");
		fieldList.add("9092");
		fieldList.add("9093");
		fieldList.add("9094");
		fieldList.add("9095");
		fieldList.add("9002");
		fieldList.add("9004");
		fieldList.add("9005");

	};

	public String FIRST_FIELD = "311";

	/**
	* getter method to get FirstField
	*
	* @return String - FirstField
	*/
	public String getFirstField() {
		return "311";
	}
	/**
	* getter method to get RepeatedGroupTag
	*
	* @return String - RepeatedGroupTag
	*/
	public String getRepeatedGroupTag() {
		return "146";
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
	* Constructor to construct FixRGNoRelatedSym object 
	*
	*/
	public FixRGNoRelatedSym() {
	}
	/**
	* setter method to set UnderlyingSymbol
	*
	* @param String - UnderlyingSymbol
	*/
	public void setUnderlyingSymbol(String _UnderlyingSymbol) {
		addBodyField(311, _UnderlyingSymbol);
	}
	/**
	* getter method to get UnderlyingSymbol
	*
	* @return String - UnderlyingSymbol
	*/
	public String getUnderlyingSymbol() {
		return (getBodyFieldValue(311));
	}
	/**
	* setter method to set UnderlyingSecurityDes
	*
	* @param String - UnderlyingSecurityDes
	*/
	public void setUnderlyingSecurityDes(String _UnderlyingSecurityDes) {
		addBodyField(307, _UnderlyingSecurityDes);
	}
	/**
	* getter method to get UnderlyingSecurityDes
	*
	* @return String - UnderlyingSecurityDes
	*/
	public String getUnderlyingSecurityDes() {
		return (getBodyFieldValue(307));
	}
	/**
	* setter method to set UnderlyingMaturityMonthYear
	*
	* @param Date - UnderlyingMaturityMonthYear
	*/
	public void setUnderlyingMaturityMonthYear(Date _UnderlyingMaturityMonthYear) {
		addBodyField(313, getMonthYearString(_UnderlyingMaturityMonthYear));
	}
	/**
	* setter method to set UnderlyingMaturityMonthYear
	*
	* @param String - UnderlyingMaturityMonthYear
	*/
	public void setUnderlyingMaturityMonthYear(String _UnderlyingMaturityMonthYear) {
		addBodyField(313, _UnderlyingMaturityMonthYear);
	}
	/**
	* getter method to get UnderlyingMaturityMonthYear
	*
	* @return Date - UnderlyingMaturityMonthYear
	*/
	public Date getUnderlyingMaturityMonthYear() {
		return (MonthYearStringToDate(getBodyFieldValue(313)));
	}
	/**
	* getter method to get UnderlyingMaturityMonthYearAsString
	*
	* @return String - UnderlyingMaturityMonthYearAsString
	*/
	public String getUnderlyingMaturityMonthYearAsString() {
		return (getBodyFieldValue(313));
	}
	/**
	* setter method to set UnderlyingMaturityDay
	*
	* @param int - UnderlyingMaturityDay
	*/
	public void setUnderlyingMaturityDay(int _UnderlyingMaturityDay) {
		addBodyField(314, getString(_UnderlyingMaturityDay));
	}
	/**
	* setter method to set UnderlyingMaturityDay
	*
	* @param String - UnderlyingMaturityDay
	*/
	public void setUnderlyingMaturityDay(String _UnderlyingMaturityDay) {
		addBodyField(314, _UnderlyingMaturityDay);
	}
	/**
	* getter method to get UnderlyingMaturityDay
	*
	* @return int - UnderlyingMaturityDay
	*/
	public int getUnderlyingMaturityDay() {
		return (stringToint(getBodyFieldValue(314)));
	}
	/**
	* getter method to get UnderlyingMaturityDayAsString
	*
	* @return String - UnderlyingMaturityDayAsString
	*/
	public String getUnderlyingMaturityDayAsString() {
		return (getBodyFieldValue(314));
	}
	/**
	* setter method to set SecurityTradingStatus
	*
	* @param int - SecurityTradingStatus
	*/
	public void setSecurityTradingStatus(int _SecurityTradingStatus) {
		addBodyField(326, getString(_SecurityTradingStatus));
	}
	/**
	* setter method to set SecurityTradingStatus
	*
	* @param String - SecurityTradingStatus
	*/
	public void setSecurityTradingStatus(String _SecurityTradingStatus) {
		addBodyField(326, _SecurityTradingStatus);
	}
	/**
	* getter method to get SecurityTradingStatus
	*
	* @return int - SecurityTradingStatus
	*/
	public int getSecurityTradingStatus() {
		return (stringToint(getBodyFieldValue(326)));
	}
	/**
	* getter method to get SecurityTradingStatusAsString
	*
	* @return String - SecurityTradingStatusAsString
	*/
	public String getSecurityTradingStatusAsString() {
		return (getBodyFieldValue(326));
	}
	/**
	* setter method to set IncrementPrice
	*
	* @param float - IncrementPrice
	*/
	public void setIncrementPrice(float _IncrementPrice) {
		addBodyField(9013, getString(_IncrementPrice));
	}
	/**
	* setter method to set IncrementPrice
	*
	* @param String - IncrementPrice
	*/
	public void setIncrementPrice(String _IncrementPrice) {
		addBodyField(9013, _IncrementPrice);
	}
	/**
	* getter method to get IncrementPrice
	*
	* @return float - IncrementPrice
	*/
	public float getIncrementPrice() {
		return (stringTofloat(getBodyFieldValue(9013)));
	}
	/**
	* getter method to get IncrementPriceAsString
	*
	* @return String - IncrementPriceAsString
	*/
	public String getIncrementPriceAsString() {
		return (getBodyFieldValue(9013));
	}
	/**
	* setter method to set IncrementQty
	*
	* @param float - IncrementQty
	*/
	public void setIncrementQty(float _IncrementQty) {
		addBodyField(9014, getString(_IncrementQty));
	}
	/**
	* setter method to set IncrementQty
	*
	* @param String - IncrementQty
	*/
	public void setIncrementQty(String _IncrementQty) {
		addBodyField(9014, _IncrementQty);
	}
	/**
	* getter method to get IncrementQty
	*
	* @return float - IncrementQty
	*/
	public float getIncrementQty() {
		return (stringTofloat(getBodyFieldValue(9014)));
	}
	/**
	* getter method to get IncrementQtyAsString
	*
	* @return String - IncrementQtyAsString
	*/
	public String getIncrementQtyAsString() {
		return (getBodyFieldValue(9014));
	}
	/**
	* setter method to set LotSize
	*
	* @param int - LotSize
	*/
	public void setLotSize(int _LotSize) {
		addBodyField(9017, getString(_LotSize));
	}
	/**
	* setter method to set LotSize
	*
	* @param String - LotSize
	*/
	public void setLotSize(String _LotSize) {
		addBodyField(9017, _LotSize);
	}
	/**
	* getter method to get LotSize
	*
	* @return int - LotSize
	*/
	public int getLotSize() {
		return (stringToint(getBodyFieldValue(9017)));
	}
	/**
	* getter method to get LotSizeAsString
	*
	* @return String - LotSizeAsString
	*/
	public String getLotSizeAsString() {
		return (getBodyFieldValue(9017));
	}
	/**
	* setter method to set NumOfDecimalPrice
	*
	* @param int - NumOfDecimalPrice
	*/
	public void setNumOfDecimalPrice(int _NumOfDecimalPrice) {
		addBodyField(9083, getString(_NumOfDecimalPrice));
	}
	/**
	* setter method to set NumOfDecimalPrice
	*
	* @param String - NumOfDecimalPrice
	*/
	public void setNumOfDecimalPrice(String _NumOfDecimalPrice) {
		addBodyField(9083, _NumOfDecimalPrice);
	}
	/**
	* getter method to get NumOfDecimalPrice
	*
	* @return int - NumOfDecimalPrice
	*/
	public int getNumOfDecimalPrice() {
		return (stringToint(getBodyFieldValue(9083)));
	}
	/**
	* getter method to get NumOfDecimalPriceAsString
	*
	* @return String - NumOfDecimalPriceAsString
	*/
	public String getNumOfDecimalPriceAsString() {
		return (getBodyFieldValue(9083));
	}
	/**
	* setter method to set NumOfDecimalQty
	*
	* @param int - NumOfDecimalQty
	*/
	public void setNumOfDecimalQty(int _NumOfDecimalQty) {
		addBodyField(9084, getString(_NumOfDecimalQty));
	}
	/**
	* setter method to set NumOfDecimalQty
	*
	* @param String - NumOfDecimalQty
	*/
	public void setNumOfDecimalQty(String _NumOfDecimalQty) {
		addBodyField(9084, _NumOfDecimalQty);
	}
	/**
	* getter method to get NumOfDecimalQty
	*
	* @return int - NumOfDecimalQty
	*/
	public int getNumOfDecimalQty() {
		return (stringToint(getBodyFieldValue(9084)));
	}
	/**
	* getter method to get NumOfDecimalQtyAsString
	*
	* @return String - NumOfDecimalQtyAsString
	*/
	public String getNumOfDecimalQtyAsString() {
		return (getBodyFieldValue(9084));
	}
	/**
	* setter method to set BaseNumLots
	*
	* @param int - BaseNumLots
	*/
	public void setBaseNumLots(int _BaseNumLots) {
		addBodyField(9030, getString(_BaseNumLots));
	}
	/**
	* setter method to set BaseNumLots
	*
	* @param String - BaseNumLots
	*/
	public void setBaseNumLots(String _BaseNumLots) {
		addBodyField(9030, _BaseNumLots);
	}
	/**
	* getter method to get BaseNumLots
	*
	* @return int - BaseNumLots
	*/
	public int getBaseNumLots() {
		return (stringToint(getBodyFieldValue(9030)));
	}
	/**
	* getter method to get BaseNumLotsAsString
	*
	* @return String - BaseNumLotsAsString
	*/
	public String getBaseNumLotsAsString() {
		return (getBodyFieldValue(9030));
	}
	/**
	* setter method to set NumBlocks
	*
	* @param int - NumBlocks
	*/
	public void setNumBlocks(int _NumBlocks) {
		addBodyField(9031, getString(_NumBlocks));
	}
	/**
	* setter method to set NumBlocks
	*
	* @param String - NumBlocks
	*/
	public void setNumBlocks(String _NumBlocks) {
		addBodyField(9031, _NumBlocks);
	}
	/**
	* getter method to get NumBlocks
	*
	* @return int - NumBlocks
	*/
	public int getNumBlocks() {
		return (stringToint(getBodyFieldValue(9031)));
	}
	/**
	* getter method to get NumBlocksAsString
	*
	* @return String - NumBlocksAsString
	*/
	public String getNumBlocksAsString() {
		return (getBodyFieldValue(9031));
	}
	/**
	* setter method to set TickValue
	*
	* @param float - TickValue
	*/
	public void setTickValue(float _TickValue) {
		addBodyField(9032, getString(_TickValue));
	}
	/**
	* setter method to set TickValue
	*
	* @param String - TickValue
	*/
	public void setTickValue(String _TickValue) {
		addBodyField(9032, _TickValue);
	}
	/**
	* getter method to get TickValue
	*
	* @return float - TickValue
	*/
	public float getTickValue() {
		return (stringTofloat(getBodyFieldValue(9032)));
	}
	/**
	* getter method to get TickValueAsString
	*
	* @return String - TickValueAsString
	*/
	public String getTickValueAsString() {
		return (getBodyFieldValue(9032));
	}
	/**
	* setter method to set ProductId
	*
	* @param int - ProductId
	*/
	public void setProductId(int _ProductId) {
		addBodyField(9061, getString(_ProductId));
	}
	/**
	* setter method to set ProductId
	*
	* @param String - ProductId
	*/
	public void setProductId(String _ProductId) {
		addBodyField(9061, _ProductId);
	}
	/**
	* getter method to get ProductId
	*
	* @return int - ProductId
	*/
	public int getProductId() {
		return (stringToint(getBodyFieldValue(9061)));
	}
	/**
	* getter method to get ProductIdAsString
	*
	* @return String - ProductIdAsString
	*/
	public String getProductIdAsString() {
		return (getBodyFieldValue(9061));
	}
	/**
	* setter method to set ClearedAlias
	*
	* @param String - ClearedAlias
	*/
	public void setClearedAlias(String _ClearedAlias) {
		addBodyField(9091, _ClearedAlias);
	}
	/**
	* getter method to get ClearedAlias
	*
	* @return String - ClearedAlias
	*/
	public String getClearedAlias() {
		return (getBodyFieldValue(9091));
	}
	/**
	* setter method to set Denominator
	*
	* @param int - Denominator
	*/
	public void setDenominator(int _Denominator) {
		addBodyField(9092, getString(_Denominator));
	}
	/**
	* setter method to set Denominator
	*
	* @param String - Denominator
	*/
	public void setDenominator(String _Denominator) {
		addBodyField(9092, _Denominator);
	}
	/**
	* getter method to get Denominator
	*
	* @return int - Denominator
	*/
	public int getDenominator() {
		return (stringToint(getBodyFieldValue(9092)));
	}
	/**
	* getter method to get DenominatorAsString
	*
	* @return String - DenominatorAsString
	*/
	public String getDenominatorAsString() {
		return (getBodyFieldValue(9092));
	}
	/**
	* setter method to set InitialMargin
	*
	* @param int - InitialMargin
	*/
	public void setInitialMargin(int _InitialMargin) {
		addBodyField(9093, getString(_InitialMargin));
	}
	/**
	* setter method to set InitialMargin
	*
	* @param String - InitialMargin
	*/
	public void setInitialMargin(String _InitialMargin) {
		addBodyField(9093, _InitialMargin);
	}
	/**
	* getter method to get InitialMargin
	*
	* @return int - InitialMargin
	*/
	public int getInitialMargin() {
		return (stringToint(getBodyFieldValue(9093)));
	}
	/**
	* getter method to get InitialMarginAsString
	*
	* @return String - InitialMarginAsString
	*/
	public String getInitialMarginAsString() {
		return (getBodyFieldValue(9093));
	}
	/**
	* setter method to set LotMultiplier
	*
	* @param float - LotMultiplier
	*/
	public void setLotMultiplier(float _LotMultiplier) {
		addBodyField(9094, getString(_LotMultiplier));
	}
	/**
	* setter method to set LotMultiplier
	*
	* @param String - LotMultiplier
	*/
	public void setLotMultiplier(String _LotMultiplier) {
		addBodyField(9094, _LotMultiplier);
	}
	/**
	* getter method to get LotMultiplier
	*
	* @return float - LotMultiplier
	*/
	public float getLotMultiplier() {
		return (stringTofloat(getBodyFieldValue(9094)));
	}
	/**
	* getter method to get LotMultiplierAsString
	*
	* @return String - LotMultiplierAsString
	*/
	public String getLotMultiplierAsString() {
		return (getBodyFieldValue(9094));
	}
	/**
	* setter method to set ProductType
	*
	* @param String - ProductType
	*/
	public void setProductType(String _ProductType) {
		addBodyField(9095, _ProductType);
	}
	/**
	* getter method to get ProductType
	*
	* @return String - ProductType
	*/
	public String getProductType() {
		return (getBodyFieldValue(9095));
	}
	/**
	* setter method to set ImpliedType
	*
	* @param String - ImpliedType
	*/
	public void setImpliedType(String _ImpliedType) {
		addBodyField(9002, _ImpliedType);
	}
	/**
	* getter method to get ImpliedType
	*
	* @return String - ImpliedType
	*/
	public String getImpliedType() {
		return (getBodyFieldValue(9002));
	}
	/**
	* setter method to set PrimaryLegSymbol
	*
	* @param String - PrimaryLegSymbol
	*/
	public void setPrimaryLegSymbol(String _PrimaryLegSymbol) {
		addBodyField(9004, _PrimaryLegSymbol);
	}
	/**
	* getter method to get PrimaryLegSymbol
	*
	* @return String - PrimaryLegSymbol
	*/
	public String getPrimaryLegSymbol() {
		return (getBodyFieldValue(9004));
	}
	/**
	* setter method to set SecondaryLegSymbol
	*
	* @param String - SecondaryLegSymbol
	*/
	public void setSecondaryLegSymbol(String _SecondaryLegSymbol) {
		addBodyField(9005, _SecondaryLegSymbol);
	}
	/**
	* getter method to get SecondaryLegSymbol
	*
	* @return String - SecondaryLegSymbol
	*/
	public String getSecondaryLegSymbol() {
		return (getBodyFieldValue(9005));
	}

}
