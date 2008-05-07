package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMOMessage
*
*/
public class RLCMOMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMOMessage object 
	*
	*/
	public RLCMOMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MO, true);
	}
	/**
	* Constructor to construct RLCMOMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMOMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_MO) {
			throw new RLCProtocolError(
				"Invalid Message Length -" + newBytes.length,
				"MessageLength for MO Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MO);
		}
	}
	/**
	* Constructor to construct RLCMOMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMOMessage(int size, boolean fillWithSpaces) {
		super(size, fillWithSpaces);
	}
	/**
	* setter method to set SequenceNumber
	*
	* @param int - SequenceNumber
	*
	*/
	public void setSequenceNumber(int _SequenceNumber) {
		write(36, pad(String.valueOf(_SequenceNumber), '0', 6));
	}
	/**
	* setter method to set SequenceNumber
	*
	* @param String - SequenceNumber
	*
	*/
	public void setSequenceNumber(String _SequenceNumber) {
		write(36, _SequenceNumber);
	}
	/**
	* getter method to get SequenceNumberAsint
	*
	* @return int - SequenceNumberAsint
	*/
	public int getSequenceNumberAsint() {
		return (stringToint(readString(36, 6)));
	}
	/**
	* getter method to get SequenceNumber
	*
	* @return String - SequenceNumber
	*/
	public String getSequenceNumber() {
		return (readString(36, 6));
	}
	/**
	* setter method to set InstrumentActivationDateTime
	*
	* @param Date - InstrumentActivationDateTime
	*
	*/
	public void setInstrumentActivationDateTime(Date _InstrumentActivationDateTime) {
		write(42, getyyyymmddhhmmssString(_InstrumentActivationDateTime));
	}
	/**
	* setter method to set InstrumentActivationDateTime
	*
	* @param String - InstrumentActivationDateTime
	*
	*/
	public void setInstrumentActivationDateTime(String _InstrumentActivationDateTime) {
		write(42, _InstrumentActivationDateTime);
	}
	/**
	* getter method to get InstrumentActivationDateTimeAsDate
	*
	* @return Date - InstrumentActivationDateTimeAsDate
	*/
	public Date getInstrumentActivationDateTimeAsDate() {
		return (yyyymmddhhmmssStringToDate(readString(42, 14)));
	}
	/**
	* getter method to get InstrumentActivationDateTime
	*
	* @return String - InstrumentActivationDateTime
	*/
	public String getInstrumentActivationDateTime() {
		return (readString(42, 14));
	}
	/**
	* setter method to set InstrumentExpirationDateTime
	*
	* @param Date - InstrumentExpirationDateTime
	*
	*/
	public void setInstrumentExpirationDateTime(Date _InstrumentExpirationDateTime) {
		write(56, getyyyymmddhhmmssString(_InstrumentExpirationDateTime));
	}
	/**
	* setter method to set InstrumentExpirationDateTime
	*
	* @param String - InstrumentExpirationDateTime
	*
	*/
	public void setInstrumentExpirationDateTime(String _InstrumentExpirationDateTime) {
		write(56, _InstrumentExpirationDateTime);
	}
	/**
	* getter method to get InstrumentExpirationDateTimeAsDate
	*
	* @return Date - InstrumentExpirationDateTimeAsDate
	*/
	public Date getInstrumentExpirationDateTimeAsDate() {
		return (yyyymmddhhmmssStringToDate(readString(56, 14)));
	}
	/**
	* getter method to get InstrumentExpirationDateTime
	*
	* @return String - InstrumentExpirationDateTime
	*/
	public String getInstrumentExpirationDateTime() {
		return (readString(56, 14));
	}
	/**
	* setter method to set InstrumentGroupCode
	*
	* @param String - InstrumentGroupCode
	*
	*/
	public void setInstrumentGroupCode(String _InstrumentGroupCode) {
		write(70, _InstrumentGroupCode);
	}
	/**
	* getter method to get InstrumentGroupCode
	*
	* @return String - InstrumentGroupCode
	*/
	public String getInstrumentGroupCode() {
		return (readString(70, 2));
	}
	/**
	* setter method to set CompleteInstrumentCode
	*
	* @param String - CompleteInstrumentCode
	*
	*/
	public void setCompleteInstrumentCode(String _CompleteInstrumentCode) {
		write(72, _CompleteInstrumentCode);
	}
	/**
	* getter method to get CompleteInstrumentCode
	*
	* @return String - CompleteInstrumentCode
	*/
	public String getCompleteInstrumentCode() {
		return (readString(72, 20));
	}
	/**
	* setter method to set InstrumentType
	*
	* @param String - InstrumentType
	*
	*/
	public void setInstrumentType(String _InstrumentType) {
		write(92, _InstrumentType);
	}
	/**
	* getter method to get InstrumentType
	*
	* @return String - InstrumentType
	*/
	public String getInstrumentType() {
		return (readString(92, 1));
	}
	/**
	* setter method to set DefaultDateValidity
	*
	* @param String - DefaultDateValidity
	*
	*/
	public void setDefaultDateValidity(String _DefaultDateValidity) {
		write(93, _DefaultDateValidity);
	}
	/**
	* getter method to get DefaultDateValidity
	*
	* @return String - DefaultDateValidity
	*/
	public String getDefaultDateValidity() {
		return (readString(93, 1));
	}
	/**
	* setter method to set TradeUnit
	*
	* @param String - TradeUnit
	*
	*/
	public void setTradeUnit(String _TradeUnit) {
		write(94, _TradeUnit);
	}
	/**
	* getter method to get TradeUnit
	*
	* @return String - TradeUnit
	*/
	public String getTradeUnit() {
		return (readString(94, 19));
	}
	/**
	* setter method to set PartialLegISIN
	*
	* @param String - PartialLegISIN
	*
	*/
	public void setPartialLegISIN(String _PartialLegISIN) {
		write(113, _PartialLegISIN);
	}
	/**
	* getter method to get PartialLegISIN
	*
	* @return String - PartialLegISIN
	*/
	public String getPartialLegISIN() {
		return (readString(113, 240));
	}
	/**
	* setter method to set SpreadRatio
	*
	* @param String - SpreadRatio
	*
	*/
	public void setSpreadRatio(String _SpreadRatio) {
		write(353, _SpreadRatio);
	}
	/**
	* getter method to get SpreadRatio
	*
	* @return String - SpreadRatio
	*/
	public String getSpreadRatio() {
		return (readString(353, 120));
	}
	/**
	* setter method to set Filler_2
	*
	* @param String - Filler_2
	*
	*/
	public void setFiller_2(String _Filler_2) {
		write(473, _Filler_2);
	}
	/**
	* getter method to get Filler_2
	*
	* @return String - Filler_2
	*/
	public String getFiller_2() {
		return (readString(473, 2));
	}
	/**
	* setter method to set LimitRangeValue
	*
	* @param BigDecimal - LimitRangeValue
	*
	*/
	public void setLimitRangeValue(BigDecimal _LimitRangeValue) {
		write(475, priceToString(_LimitRangeValue));
	}
	/**
	* setter method to set LimitRangeValue
	*
	* @param String - LimitRangeValue
	*
	*/
	public void setLimitRangeValue(String _LimitRangeValue) {
		write(475, _LimitRangeValue);
	}
	/**
	* getter method to get LimitRangeValueAsPrice
	*
	* @return BigDecimal - LimitRangeValueAsPrice
	*/
	public BigDecimal getLimitRangeValueAsPrice() {
		return (stringToPrice(readString(475, 19)));
	}
	/**
	* getter method to get LimitRangeValue
	*
	* @return String - LimitRangeValue
	*/
	public String getLimitRangeValue() {
		return (readString(475, 19));
	}
	/**
	* setter method to set StrikePrice
	*
	* @param BigDecimal - StrikePrice
	*
	*/
	public void setStrikePrice(BigDecimal _StrikePrice) {
		write(494, priceToString(_StrikePrice));
	}
	/**
	* setter method to set StrikePrice
	*
	* @param String - StrikePrice
	*
	*/
	public void setStrikePrice(String _StrikePrice) {
		write(494, _StrikePrice);
	}
	/**
	* getter method to get StrikePriceAsPrice
	*
	* @return BigDecimal - StrikePriceAsPrice
	*/
	public BigDecimal getStrikePriceAsPrice() {
		return (stringToPrice(readString(494, 19)));
	}
	/**
	* getter method to get StrikePrice
	*
	* @return String - StrikePrice
	*/
	public String getStrikePrice() {
		return (readString(494, 19));
	}
	/**
	* setter method to set StrikePriceCurrency
	*
	* @param String - StrikePriceCurrency
	*
	*/
	public void setStrikePriceCurrency(String _StrikePriceCurrency) {
		write(513, _StrikePriceCurrency);
	}
	/**
	* getter method to get StrikePriceCurrency
	*
	* @return String - StrikePriceCurrency
	*/
	public String getStrikePriceCurrency() {
		return (readString(513, 3));
	}
	/**
	* setter method to set Filler_3
	*
	* @param String - Filler_3
	*
	*/
	public void setFiller_3(String _Filler_3) {
		write(516, _Filler_3);
	}
	/**
	* getter method to get Filler_3
	*
	* @return String - Filler_3
	*/
	public String getFiller_3() {
		return (readString(516, 5));
	}
	/**
	* setter method to set OrderMinQty
	*
	* @param int - OrderMinQty
	*
	*/
	public void setOrderMinQty(int _OrderMinQty) {
		write(521, pad(String.valueOf(_OrderMinQty), '0', 12));
	}
	/**
	* setter method to set OrderMinQty
	*
	* @param String - OrderMinQty
	*
	*/
	public void setOrderMinQty(String _OrderMinQty) {
		write(521, _OrderMinQty);
	}
	/**
	* getter method to get OrderMinQtyAsint
	*
	* @return int - OrderMinQtyAsint
	*/
	public int getOrderMinQtyAsint() {
		return (stringToint(readString(521, 12)));
	}
	/**
	* getter method to get OrderMinQty
	*
	* @return String - OrderMinQty
	*/
	public String getOrderMinQty() {
		return (readString(521, 12));
	}
	/**
	* setter method to set OrderMaxQty
	*
	* @param int - OrderMaxQty
	*
	*/
	public void setOrderMaxQty(int _OrderMaxQty) {
		write(533, pad(String.valueOf(_OrderMaxQty), '0', 12));
	}
	/**
	* setter method to set OrderMaxQty
	*
	* @param String - OrderMaxQty
	*
	*/
	public void setOrderMaxQty(String _OrderMaxQty) {
		write(533, _OrderMaxQty);
	}
	/**
	* getter method to get OrderMaxQtyAsint
	*
	* @return int - OrderMaxQtyAsint
	*/
	public int getOrderMaxQtyAsint() {
		return (stringToint(readString(533, 12)));
	}
	/**
	* getter method to get OrderMaxQty
	*
	* @return String - OrderMaxQty
	*/
	public String getOrderMaxQty() {
		return (readString(533, 12));
	}
	/**
	* setter method to set TradingCurrency
	*
	* @param String - TradingCurrency
	*
	*/
	public void setTradingCurrency(String _TradingCurrency) {
		write(545, _TradingCurrency);
	}
	/**
	* getter method to get TradingCurrency
	*
	* @return String - TradingCurrency
	*/
	public String getTradingCurrency() {
		return (readString(545, 3));
	}
	/**
	* setter method to set ProductTickIncrementValueAmount
	*
	* @param BigDecimal - ProductTickIncrementValueAmount
	*
	*/
	public void setProductTickIncrementValueAmount(BigDecimal _ProductTickIncrementValueAmount) {
		write(548, priceToString(_ProductTickIncrementValueAmount));
	}
	/**
	* setter method to set ProductTickIncrementValueAmount
	*
	* @param String - ProductTickIncrementValueAmount
	*
	*/
	public void setProductTickIncrementValueAmount(String _ProductTickIncrementValueAmount) {
		write(548, _ProductTickIncrementValueAmount);
	}
	/**
	* getter method to get ProductTickIncrementValueAmountAsPrice
	*
	* @return BigDecimal - ProductTickIncrementValueAmountAsPrice
	*/
	public BigDecimal getProductTickIncrementValueAmountAsPrice() {
		return (stringToPrice(readString(548, 19)));
	}
	/**
	* getter method to get ProductTickIncrementValueAmount
	*
	* @return String - ProductTickIncrementValueAmount
	*/
	public String getProductTickIncrementValueAmount() {
		return (readString(548, 19));
	}
	/**
	* setter method to set PriceCalculationIndicator
	*
	* @param String - PriceCalculationIndicator
	*
	*/
	public void setPriceCalculationIndicator(String _PriceCalculationIndicator) {
		write(567, _PriceCalculationIndicator);
	}
	/**
	* getter method to get PriceCalculationIndicator
	*
	* @return String - PriceCalculationIndicator
	*/
	public String getPriceCalculationIndicator() {
		return (readString(567, 1));
	}
	/**
	* setter method to set NumberOfMinimumTickIncrement
	*
	* @param int - NumberOfMinimumTickIncrement
	*
	*/
	public void setNumberOfMinimumTickIncrement(int _NumberOfMinimumTickIncrement) {
		write(568, pad(String.valueOf(_NumberOfMinimumTickIncrement), '0', 4));
	}
	/**
	* setter method to set NumberOfMinimumTickIncrement
	*
	* @param String - NumberOfMinimumTickIncrement
	*
	*/
	public void setNumberOfMinimumTickIncrement(String _NumberOfMinimumTickIncrement) {
		write(568, _NumberOfMinimumTickIncrement);
	}
	/**
	* getter method to get NumberOfMinimumTickIncrementAsint
	*
	* @return int - NumberOfMinimumTickIncrementAsint
	*/
	public int getNumberOfMinimumTickIncrementAsint() {
		return (stringToint(readString(568, 4)));
	}
	/**
	* getter method to get NumberOfMinimumTickIncrement
	*
	* @return String - NumberOfMinimumTickIncrement
	*/
	public String getNumberOfMinimumTickIncrement() {
		return (readString(568, 4));
	}
	/**
	* setter method to set TickDisplayFormatType
	*
	* @param String - TickDisplayFormatType
	*
	*/
	public void setTickDisplayFormatType(String _TickDisplayFormatType) {
		write(572, _TickDisplayFormatType);
	}
	/**
	* getter method to get TickDisplayFormatType
	*
	* @return String - TickDisplayFormatType
	*/
	public String getTickDisplayFormatType() {
		return (readString(572, 2));
	}
	/**
	* setter method to set NumberOfDecimalsInDisplayedPrice
	*
	* @param int - NumberOfDecimalsInDisplayedPrice
	*
	*/
	public void setNumberOfDecimalsInDisplayedPrice(int _NumberOfDecimalsInDisplayedPrice) {
		write(574, pad(String.valueOf(_NumberOfDecimalsInDisplayedPrice), '0', 2));
	}
	/**
	* setter method to set NumberOfDecimalsInDisplayedPrice
	*
	* @param String - NumberOfDecimalsInDisplayedPrice
	*
	*/
	public void setNumberOfDecimalsInDisplayedPrice(String _NumberOfDecimalsInDisplayedPrice) {
		write(574, _NumberOfDecimalsInDisplayedPrice);
	}
	/**
	* getter method to get NumberOfDecimalsInDisplayedPriceAsint
	*
	* @return int - NumberOfDecimalsInDisplayedPriceAsint
	*/
	public int getNumberOfDecimalsInDisplayedPriceAsint() {
		return (stringToint(readString(574, 2)));
	}
	/**
	* getter method to get NumberOfDecimalsInDisplayedPrice
	*
	* @return String - NumberOfDecimalsInDisplayedPrice
	*/
	public String getNumberOfDecimalsInDisplayedPrice() {
		return (readString(574, 2));
	}
	/**
	* setter method to set VariableTickTableIndexCode
	*
	* @param String - VariableTickTableIndexCode
	*
	*/
	public void setVariableTickTableIndexCode(String _VariableTickTableIndexCode) {
		write(576, _VariableTickTableIndexCode);
	}
	/**
	* getter method to get VariableTickTableIndexCode
	*
	* @return String - VariableTickTableIndexCode
	*/
	public String getVariableTickTableIndexCode() {
		return (readString(576, 2));
	}
	/**
	* setter method to set SettlementDate
	*
	* @param Date - SettlementDate
	*
	*/
	public void setSettlementDate(Date _SettlementDate) {
		write(578, getYYYYMMDDString(_SettlementDate));
	}
	/**
	* setter method to set SettlementDate
	*
	* @param String - SettlementDate
	*
	*/
	public void setSettlementDate(String _SettlementDate) {
		write(578, _SettlementDate);
	}
	/**
	* getter method to get SettlementDateAsDate
	*
	* @return Date - SettlementDateAsDate
	*/
	public Date getSettlementDateAsDate() {
		return (YYYYMMDDStringToDate(readString(578, 8)));
	}
	/**
	* getter method to get SettlementDate
	*
	* @return String - SettlementDate
	*/
	public String getSettlementDate() {
		return (readString(578, 8));
	}
	/**
	* setter method to set ReferenceSettlementPriceIndicator
	*
	* @param String - ReferenceSettlementPriceIndicator
	*
	*/
	public void setReferenceSettlementPriceIndicator(String _ReferenceSettlementPriceIndicator) {
		write(586, _ReferenceSettlementPriceIndicator);
	}
	/**
	* getter method to get ReferenceSettlementPriceIndicator
	*
	* @return String - ReferenceSettlementPriceIndicator
	*/
	public String getReferenceSettlementPriceIndicator() {
		return (readString(586, 1));
	}
	/**
	* setter method to set HighLimit
	*
	* @param BigDecimal - HighLimit
	*
	*/
	public void setHighLimit(BigDecimal _HighLimit) {
		write(606, priceToString(_HighLimit));
	}
	/**
	* setter method to set HighLimit
	*
	* @param String - HighLimit
	*
	*/
	public void setHighLimit(String _HighLimit) {
		write(606, _HighLimit);
	}
	/**
	* getter method to get HighLimitAsPrice
	*
	* @return BigDecimal - HighLimitAsPrice
	*/
	public BigDecimal getHighLimitAsPrice() {
		return (stringToPrice(readString(606, 19)));
	}
	/**
	* getter method to get HighLimit
	*
	* @return String - HighLimit
	*/
	public String getHighLimit() {
		return (readString(606, 19));
	}
	/**
	* setter method to set LowLimit
	*
	* @param BigDecimal - LowLimit
	*
	*/
	public void setLowLimit(BigDecimal _LowLimit) {
		write(625, priceToString(_LowLimit));
	}
	/**
	* setter method to set LowLimit
	*
	* @param String - LowLimit
	*
	*/
	public void setLowLimit(String _LowLimit) {
		write(625, _LowLimit);
	}
	/**
	* getter method to get LowLimitAsPrice
	*
	* @return BigDecimal - LowLimitAsPrice
	*/
	public BigDecimal getLowLimitAsPrice() {
		return (stringToPrice(readString(625, 19)));
	}
	/**
	* getter method to get LowLimit
	*
	* @return String - LowLimit
	*/
	public String getLowLimit() {
		return (readString(625, 19));
	}
	/**
	* setter method to set Filler_4
	*
	* @param String - Filler_4
	*
	*/
	public void setFiller_4(String _Filler_4) {
		write(644, _Filler_4);
	}
	/**
	* getter method to get Filler_4
	*
	* @return String - Filler_4
	*/
	public String getFiller_4() {
		return (readString(644, 9));
	}
	/**
	* setter method to set Filler_5
	*
	* @param String - Filler_5
	*
	*/
	public void setFiller_5(String _Filler_5) {
		write(653, _Filler_5);
	}
	/**
	* getter method to get Filler_5
	*
	* @return String - Filler_5
	*/
	public String getFiller_5() {
		return (readString(653, 3));
	}
	/**
	* setter method to set Filler_6
	*
	* @param String - Filler_6
	*
	*/
	public void setFiller_6(String _Filler_6) {
		write(656, _Filler_6);
	}
	/**
	* getter method to get Filler_6
	*
	* @return String - Filler_6
	*/
	public String getFiller_6() {
		return (readString(656, 19));
	}
	/**
	* setter method to set MarginRate
	*
	* @param String - MarginRate
	*
	*/
	public void setMarginRate(String _MarginRate) {
		write(675, _MarginRate);
	}
	/**
	* getter method to get MarginRate
	*
	* @return String - MarginRate
	*/
	public String getMarginRate() {
		return (readString(675, 6));
	}
	/**
	* setter method to set Filler_7
	*
	* @param String - Filler_7
	*
	*/
	public void setFiller_7(String _Filler_7) {
		write(681, _Filler_7);
	}
	/**
	* getter method to get Filler_7
	*
	* @return String - Filler_7
	*/
	public String getFiller_7() {
		return (readString(681, 2));
	}
	/**
	* setter method to set BookDepth
	*
	* @param int - BookDepth
	*
	*/
	public void setBookDepth(int _BookDepth) {
		write(683, pad(String.valueOf(_BookDepth), '0', 2));
	}
	/**
	* setter method to set BookDepth
	*
	* @param String - BookDepth
	*
	*/
	public void setBookDepth(String _BookDepth) {
		write(683, _BookDepth);
	}
	/**
	* getter method to get BookDepthAsint
	*
	* @return int - BookDepthAsint
	*/
	public int getBookDepthAsint() {
		return (stringToint(readString(683, 2)));
	}
	/**
	* getter method to get BookDepth
	*
	* @return String - BookDepth
	*/
	public String getBookDepth() {
		return (readString(683, 2));
	}
	/**
	* setter method to set MatchingAlgorithmIndicator
	*
	* @param String - MatchingAlgorithmIndicator
	*
	*/
	public void setMatchingAlgorithmIndicator(String _MatchingAlgorithmIndicator) {
		write(685, _MatchingAlgorithmIndicator);
	}
	/**
	* getter method to get MatchingAlgorithmIndicator
	*
	* @return String - MatchingAlgorithmIndicator
	*/
	public String getMatchingAlgorithmIndicator() {
		return (readString(685, 1));
	}
	/**
	* setter method to set TypeOfQuantity
	*
	* @param String - TypeOfQuantity
	*
	*/
	public void setTypeOfQuantity(String _TypeOfQuantity) {
		write(686, _TypeOfQuantity);
	}
	/**
	* getter method to get TypeOfQuantity
	*
	* @return String - TypeOfQuantity
	*/
	public String getTypeOfQuantity() {
		return (readString(686, 3));
	}
	/**
	* setter method to set Filler_8
	*
	* @param String - Filler_8
	*
	*/
	public void setFiller_8(String _Filler_8) {
		write(689, _Filler_8);
	}
	/**
	* getter method to get Filler_8
	*
	* @return String - Filler_8
	*/
	public String getFiller_8() {
		return (readString(689, 38));
	}
	/**
	* setter method to set StrategyType
	*
	* @param String - StrategyType
	*
	*/
	public void setStrategyType(String _StrategyType) {
		write(727, _StrategyType);
	}
	/**
	* getter method to get StrategyType
	*
	* @return String - StrategyType
	*/
	public String getStrategyType() {
		return (readString(727, 2));
	}
	/**
	* setter method to set Filler_9
	*
	* @param String - Filler_9
	*
	*/
	public void setFiller_9(String _Filler_9) {
		write(729, _Filler_9);
	}
	/**
	* getter method to get Filler_9
	*
	* @return String - Filler_9
	*/
	public String getFiller_9() {
		return (readString(729, 17));
	}
	/**
	* setter method to set UnderlyingInstrumentCode
	*
	* @param String - UnderlyingInstrumentCode
	*
	*/
	public void setUnderlyingInstrumentCode(String _UnderlyingInstrumentCode) {
		write(746, _UnderlyingInstrumentCode);
	}
	/**
	* getter method to get UnderlyingInstrumentCode
	*
	* @return String - UnderlyingInstrumentCode
	*/
	public String getUnderlyingInstrumentCode() {
		return (readString(746, 12));
	}
	/**
	* setter method to set PBandVar
	*
	* @param BigDecimal - PBandVar
	*
	*/
	public void setPBandVar(BigDecimal _PBandVar) {
		write(758, priceToString(_PBandVar));
	}
	/**
	* setter method to set PBandVar
	*
	* @param String - PBandVar
	*
	*/
	public void setPBandVar(String _PBandVar) {
		write(758, _PBandVar);
	}
	/**
	* getter method to get PBandVarAsPrice
	*
	* @return BigDecimal - PBandVarAsPrice
	*/
	public BigDecimal getPBandVarAsPrice() {
		return (stringToPrice(readString(758, 19)));
	}
	/**
	* getter method to get PBandVar
	*
	* @return String - PBandVar
	*/
	public String getPBandVar() {
		return (readString(758, 19));
	}
	/**
	* setter method to set ImpliedSpreadIndicator
	*
	* @param String - ImpliedSpreadIndicator
	*
	*/
	public void setImpliedSpreadIndicator(String _ImpliedSpreadIndicator) {
		write(777, _ImpliedSpreadIndicator);
	}
	/**
	* getter method to get ImpliedSpreadIndicator
	*
	* @return String - ImpliedSpreadIndicator
	*/
	public String getImpliedSpreadIndicator() {
		return (readString(777, 1));
	}
	/**
	* setter method to set Filler_10
	*
	* @param String - Filler_10
	*
	*/
	public void setFiller_10(String _Filler_10) {
		write(778, _Filler_10);
	}
	/**
	* getter method to get Filler_10
	*
	* @return String - Filler_10
	*/
	public String getFiller_10() {
		return (readString(778, 9));
	}
	/**
	* setter method to set ElectronicMatchEligibleFlag
	*
	* @param String - ElectronicMatchEligibleFlag
	*
	*/
	public void setElectronicMatchEligibleFlag(String _ElectronicMatchEligibleFlag) {
		write(787, _ElectronicMatchEligibleFlag);
	}
	/**
	* getter method to get ElectronicMatchEligibleFlag
	*
	* @return String - ElectronicMatchEligibleFlag
	*/
	public String getElectronicMatchEligibleFlag() {
		return (readString(787, 1));
	}
	/**
	* setter method to set OrderCrossEligibleFlag
	*
	* @param String - OrderCrossEligibleFlag
	*
	*/
	public void setOrderCrossEligibleFlag(String _OrderCrossEligibleFlag) {
		write(788, _OrderCrossEligibleFlag);
	}
	/**
	* getter method to get OrderCrossEligibleFlag
	*
	* @return String - OrderCrossEligibleFlag
	*/
	public String getOrderCrossEligibleFlag() {
		return (readString(788, 1));
	}
	/**
	* setter method to set EFPEligibleFlag
	*
	* @param String - EFPEligibleFlag
	*
	*/
	public void setEFPEligibleFlag(String _EFPEligibleFlag) {
		write(789, _EFPEligibleFlag);
	}
	/**
	* getter method to get EFPEligibleFlag
	*
	* @return String - EFPEligibleFlag
	*/
	public String getEFPEligibleFlag() {
		return (readString(789, 1));
	}
	/**
	* setter method to set EBFEligibleFlag
	*
	* @param String - EBFEligibleFlag
	*
	*/
	public void setEBFEligibleFlag(String _EBFEligibleFlag) {
		write(790, _EBFEligibleFlag);
	}
	/**
	* getter method to get EBFEligibleFlag
	*
	* @return String - EBFEligibleFlag
	*/
	public String getEBFEligibleFlag() {
		return (readString(790, 1));
	}
	/**
	* setter method to set EBSEligibleFlag
	*
	* @param String - EBSEligibleFlag
	*
	*/
	public void setEBSEligibleFlag(String _EBSEligibleFlag) {
		write(791, _EBSEligibleFlag);
	}
	/**
	* getter method to get EBSEligibleFlag
	*
	* @return String - EBSEligibleFlag
	*/
	public String getEBSEligibleFlag() {
		return (readString(791, 1));
	}
	/**
	* setter method to set EFSEligibleFlag
	*
	* @param String - EFSEligibleFlag
	*
	*/
	public void setEFSEligibleFlag(String _EFSEligibleFlag) {
		write(792, _EFSEligibleFlag);
	}
	/**
	* getter method to get EFSEligibleFlag
	*
	* @return String - EFSEligibleFlag
	*/
	public String getEFSEligibleFlag() {
		return (readString(792, 1));
	}
	/**
	* setter method to set EFREligibleFlag
	*
	* @param String - EFREligibleFlag
	*
	*/
	public void setEFREligibleFlag(String _EFREligibleFlag) {
		write(793, _EFREligibleFlag);
	}
	/**
	* getter method to get EFREligibleFlag
	*
	* @return String - EFREligibleFlag
	*/
	public String getEFREligibleFlag() {
		return (readString(793, 1));
	}
	/**
	* setter method to set OTCEligibleFlag
	*
	* @param String - OTCEligibleFlag
	*
	*/
	public void setOTCEligibleFlag(String _OTCEligibleFlag) {
		write(794, _OTCEligibleFlag);
	}
	/**
	* getter method to get OTCEligibleFlag
	*
	* @return String - OTCEligibleFlag
	*/
	public String getOTCEligibleFlag() {
		return (readString(794, 1));
	}
	/**
	* setter method to set Filler_11
	*
	* @param String - Filler_11
	*
	*/
	public void setFiller_11(String _Filler_11) {
		write(795, _Filler_11);
	}
	/**
	* getter method to get Filler_11
	*
	* @return String - Filler_11
	*/
	public String getFiller_11() {
		return (readString(795, 17));
	}
	/**
	* setter method to set Filler_12
	*
	* @param String - Filler_12
	*
	*/
	public void setFiller_12(String _Filler_12) {
		write(812, _Filler_12);
	}
	/**
	* getter method to get Filler_12
	*
	* @return String - Filler_12
	*/
	public String getFiller_12() {
		return (readString(812, 12));
	}
	/**
	* setter method to set MaturityDate
	*
	* @param String - MaturityDate
	*
	*/
	public void setMaturityDate(String _MaturityDate) {
		write(824, _MaturityDate);
	}
	/**
	* getter method to get MaturityDate
	*
	* @return String - MaturityDate
	*/
	public String getMaturityDate() {
		return (readString(824, 6));
	}
	/**
	* setter method to set ProductCode
	*
	* @param String - ProductCode
	*
	*/
	public void setProductCode(String _ProductCode) {
		write(830, _ProductCode);
	}
	/**
	* getter method to get ProductCode
	*
	* @return String - ProductCode
	*/
	public String getProductCode() {
		return (readString(830, 10));
	}
	/**
	* setter method to set MinimumPriceFluctuation
	*
	* @param BigDecimal - MinimumPriceFluctuation
	*
	*/
	public void setMinimumPriceFluctuation(BigDecimal _MinimumPriceFluctuation) {
		write(840, priceToString(_MinimumPriceFluctuation));
	}
	/**
	* setter method to set MinimumPriceFluctuation
	*
	* @param String - MinimumPriceFluctuation
	*
	*/
	public void setMinimumPriceFluctuation(String _MinimumPriceFluctuation) {
		write(840, _MinimumPriceFluctuation);
	}
	/**
	* getter method to get MinimumPriceFluctuationAsPrice
	*
	* @return BigDecimal - MinimumPriceFluctuationAsPrice
	*/
	public BigDecimal getMinimumPriceFluctuationAsPrice() {
		return (stringToPrice(readString(840, 19)));
	}
	/**
	* getter method to get MinimumPriceFluctuation
	*
	* @return String - MinimumPriceFluctuation
	*/
	public String getMinimumPriceFluctuation() {
		return (readString(840, 19));
	}
	/**
	* setter method to set MinimumMonetaryValue
	*
	* @param BigDecimal - MinimumMonetaryValue
	*
	*/
	public void setMinimumMonetaryValue(BigDecimal _MinimumMonetaryValue) {
		write(859, priceToString(_MinimumMonetaryValue));
	}
	/**
	* setter method to set MinimumMonetaryValue
	*
	* @param String - MinimumMonetaryValue
	*
	*/
	public void setMinimumMonetaryValue(String _MinimumMonetaryValue) {
		write(859, _MinimumMonetaryValue);
	}
	/**
	* getter method to get MinimumMonetaryValueAsPrice
	*
	* @return BigDecimal - MinimumMonetaryValueAsPrice
	*/
	public BigDecimal getMinimumMonetaryValueAsPrice() {
		return (stringToPrice(readString(859, 19)));
	}
	/**
	* getter method to get MinimumMonetaryValue
	*
	* @return String - MinimumMonetaryValue
	*/
	public String getMinimumMonetaryValue() {
		return (readString(859, 19));
	}
	/**
	* setter method to set DisplayConversionFactor
	*
	* @param BigDecimal - DisplayConversionFactor
	*
	*/
	public void setDisplayConversionFactor(BigDecimal _DisplayConversionFactor) {
		write(878, priceToString(_DisplayConversionFactor));
	}
	/**
	* setter method to set DisplayConversionFactor
	*
	* @param String - DisplayConversionFactor
	*
	*/
	public void setDisplayConversionFactor(String _DisplayConversionFactor) {
		write(878, _DisplayConversionFactor);
	}
	/**
	* getter method to get DisplayConversionFactorAsPrice
	*
	* @return BigDecimal - DisplayConversionFactorAsPrice
	*/
	public BigDecimal getDisplayConversionFactorAsPrice() {
		return (stringToPrice(readString(878, 19)));
	}
	/**
	* getter method to get DisplayConversionFactor
	*
	* @return String - DisplayConversionFactor
	*/
	public String getDisplayConversionFactor() {
		return (readString(878, 19));
	}
	/**
	* setter method to set ExtendedInstrumentType
	*
	* @param String - ExtendedInstrumentType
	*
	*/
	public void setExtendedInstrumentType(String _ExtendedInstrumentType) {
		write(897, _ExtendedInstrumentType);
	}
	/**
	* getter method to get ExtendedInstrumentType
	*
	* @return String - ExtendedInstrumentType
	*/
	public String getExtendedInstrumentType() {
		return (readString(897, 1));
	}
	/**
	* setter method to set Buffer
	*
	* @param String - Buffer
	*
	*/
	public void setBuffer(String _Buffer) {
		write(898, _Buffer);
	}
	/**
	* getter method to get Buffer
	*
	* @return String - Buffer
	*/
	public String getBuffer() {
		return (readString(898, 25));
	}

	/**
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("SequenceNumber=>" + getSequenceNumberAsint() + "<\n");
		sw.write("SequenceNumber=>" + getSequenceNumber() + "<\n");
		sw.write(
			"InstrumentActivationDateTime=>"
				+ getInstrumentActivationDateTimeAsDate()
				+ "<\n");
		sw.write(
			"InstrumentActivationDateTime=>" + getInstrumentActivationDateTime() + "<\n");
		sw.write(
			"InstrumentExpirationDateTime=>"
				+ getInstrumentExpirationDateTimeAsDate()
				+ "<\n");
		sw.write(
			"InstrumentExpirationDateTime=>" + getInstrumentExpirationDateTime() + "<\n");
		sw.write("InstrumentGroupCode=>" + getInstrumentGroupCode() + "<\n");
		sw.write("CompleteInstrumentCode=>" + getCompleteInstrumentCode() + "<\n");
		sw.write("InstrumentType=>" + getInstrumentType() + "<\n");
		sw.write("DefaultDateValidity=>" + getDefaultDateValidity() + "<\n");
		sw.write("TradeUnit=>" + getTradeUnit() + "<\n");
		sw.write("PartialLegISIN=>" + getPartialLegISIN() + "<\n");
		sw.write("SpreadRatio=>" + getSpreadRatio() + "<\n");
		sw.write("Filler_2=>" + getFiller_2() + "<\n");
		sw.write("LimitRangeValue=>" + getLimitRangeValueAsPrice() + "<\n");
		sw.write("LimitRangeValue=>" + getLimitRangeValue() + "<\n");
		sw.write("StrikePrice=>" + getStrikePriceAsPrice() + "<\n");
		sw.write("StrikePrice=>" + getStrikePrice() + "<\n");
		sw.write("StrikePriceCurrency=>" + getStrikePriceCurrency() + "<\n");
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write("OrderMinQty=>" + getOrderMinQtyAsint() + "<\n");
		sw.write("OrderMinQty=>" + getOrderMinQty() + "<\n");
		sw.write("OrderMaxQty=>" + getOrderMaxQtyAsint() + "<\n");
		sw.write("OrderMaxQty=>" + getOrderMaxQty() + "<\n");
		sw.write("TradingCurrency=>" + getTradingCurrency() + "<\n");
		sw.write(
			"ProductTickIncrementValueAmount=>"
				+ getProductTickIncrementValueAmountAsPrice()
				+ "<\n");
		sw.write(
			"ProductTickIncrementValueAmount=>"
				+ getProductTickIncrementValueAmount()
				+ "<\n");
		sw.write(
			"PriceCalculationIndicator=>" + getPriceCalculationIndicator() + "<\n");
		sw.write(
			"NumberOfMinimumTickIncrement=>"
				+ getNumberOfMinimumTickIncrementAsint()
				+ "<\n");
		sw.write(
			"NumberOfMinimumTickIncrement=>" + getNumberOfMinimumTickIncrement() + "<\n");
		sw.write("TickDisplayFormatType=>" + getTickDisplayFormatType() + "<\n");
		sw.write(
			"NumberOfDecimalsInDisplayedPrice=>"
				+ getNumberOfDecimalsInDisplayedPriceAsint()
				+ "<\n");
		sw.write(
			"NumberOfDecimalsInDisplayedPrice=>"
				+ getNumberOfDecimalsInDisplayedPrice()
				+ "<\n");
		sw.write(
			"VariableTickTableIndexCode=>" + getVariableTickTableIndexCode() + "<\n");
		sw.write("SettlementDate=>" + getSettlementDateAsDate() + "<\n");
		sw.write("SettlementDate=>" + getSettlementDate() + "<\n");
		sw.write(
			"ReferenceSettlementPriceIndicator=>"
				+ getReferenceSettlementPriceIndicator()
				+ "<\n");
		sw.write("HighLimit=>" + getHighLimitAsPrice() + "<\n");
		sw.write("HighLimit=>" + getHighLimit() + "<\n");
		sw.write("LowLimit=>" + getLowLimitAsPrice() + "<\n");
		sw.write("LowLimit=>" + getLowLimit() + "<\n");
		sw.write("Filler_4=>" + getFiller_4() + "<\n");
		sw.write("Filler_5=>" + getFiller_5() + "<\n");
		sw.write("Filler_6=>" + getFiller_6() + "<\n");
		sw.write("MarginRate=>" + getMarginRate() + "<\n");
		sw.write("Filler_7=>" + getFiller_7() + "<\n");
		sw.write("BookDepth=>" + getBookDepthAsint() + "<\n");
		sw.write("BookDepth=>" + getBookDepth() + "<\n");
		sw.write(
			"MatchingAlgorithmIndicator=>" + getMatchingAlgorithmIndicator() + "<\n");
		sw.write("TypeOfQuantity=>" + getTypeOfQuantity() + "<\n");
		sw.write("Filler_8=>" + getFiller_8() + "<\n");
		sw.write("StrategyType=>" + getStrategyType() + "<\n");
		sw.write("Filler_9=>" + getFiller_9() + "<\n");
		sw.write("UnderlyingInstrumentCode=>" + getUnderlyingInstrumentCode() + "<\n");
		sw.write("PBandVar=>" + getPBandVarAsPrice() + "<\n");
		sw.write("PBandVar=>" + getPBandVar() + "<\n");
		sw.write("ImpliedSpreadIndicator=>" + getImpliedSpreadIndicator() + "<\n");
		sw.write("Filler_10=>" + getFiller_10() + "<\n");
		sw.write(
			"ElectronicMatchEligibleFlag=>" + getElectronicMatchEligibleFlag() + "<\n");
		sw.write("OrderCrossEligibleFlag=>" + getOrderCrossEligibleFlag() + "<\n");
		sw.write("EFPEligibleFlag=>" + getEFPEligibleFlag() + "<\n");
		sw.write("EBFEligibleFlag=>" + getEBFEligibleFlag() + "<\n");
		sw.write("EBSEligibleFlag=>" + getEBSEligibleFlag() + "<\n");
		sw.write("EFSEligibleFlag=>" + getEFSEligibleFlag() + "<\n");
		sw.write("EFREligibleFlag=>" + getEFREligibleFlag() + "<\n");
		sw.write("OTCEligibleFlag=>" + getOTCEligibleFlag() + "<\n");
		sw.write("Filler_11=>" + getFiller_11() + "<\n");
		sw.write("Filler_12=>" + getFiller_12() + "<\n");
		sw.write("MaturityDate=>" + getMaturityDate() + "<\n");
		sw.write("ProductCode=>" + getProductCode() + "<\n");
		sw.write(
			"MinimumPriceFluctuation=>" + getMinimumPriceFluctuationAsPrice() + "<\n");
		sw.write("MinimumPriceFluctuation=>" + getMinimumPriceFluctuation() + "<\n");
		sw.write("MinimumMonetaryValue=>" + getMinimumMonetaryValueAsPrice() + "<\n");
		sw.write("MinimumMonetaryValue=>" + getMinimumMonetaryValue() + "<\n");
		sw.write(
			"DisplayConversionFactor=>" + getDisplayConversionFactorAsPrice() + "<\n");
		sw.write("DisplayConversionFactor=>" + getDisplayConversionFactor() + "<\n");
		sw.write("ExtendedInstrumentType=>" + getExtendedInstrumentType() + "<\n");
		sw.write("Buffer=>" + getBuffer() + "<\n");
		return (super.toString() + sw.toString());
	}
}
