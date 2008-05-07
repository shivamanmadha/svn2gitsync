package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCM8Message
*
*/
public class RLCM8Message extends RLCMessage {
	/**
	* Constructor to construct RLCM8Message object 
	*
	*/
	public RLCM8Message() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_M8, true);
	}
	/**
	* Constructor to construct RLCM8Message object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCM8Message(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_M8) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for M8 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_M8);
		}
	}
	/**
	* Constructor to construct RLCM8Message object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCM8Message(int size, boolean fillWithSpaces) {
		super(size, fillWithSpaces);
	}
	/**
	* setter method to set Filler_2
	*
	* @param String - Filler_2
	*/
	public void setFiller_2(String _Filler_2) {
		write(36, _Filler_2);
	}
	/**
	* getter method to get Filler_2
	*
	* @return String - Filler_2
	*/
	public String getFiller_2() {
		return (readString(36, 6));
	}
	/**
	* setter method to set TradingDate
	*
	* @param Date - TradingDate
	*/
	public void setTradingDate(Date _TradingDate) {
		write(42, getYYYYMMDDString(_TradingDate));
	}
	/**
	* setter method to set TradingDate
	*
	* @param String - TradingDate
	*/
	public void setTradingDate(String _TradingDate) {
		write(42, _TradingDate);
	}
	/**
	* getter method to get TradingDateAsDate
	*
	* @return Date - TradingDateAsDate
	*/
	public Date getTradingDateAsDate() {
		return (YYYYMMDDStringToDate(readString(42, 8)));
	}
	/**
	* getter method to get TradingDate
	*
	* @return String - TradingDate
	*/
	public String getTradingDate() {
		return (readString(42, 8));
	}
	/**
	* setter method to set CompleteInstrumentCode
	*
	* @param String - CompleteInstrumentCode
	*/
	public void setCompleteInstrumentCode(String _CompleteInstrumentCode) {
		write(50, _CompleteInstrumentCode);
	}
	/**
	* getter method to get CompleteInstrumentCode
	*
	* @return String - CompleteInstrumentCode
	*/
	public String getCompleteInstrumentCode() {
		return (readString(50, 20));
	}
	/**
	* setter method to set TheoreticalPrice
	*
	* @param BigDecimal - TheoreticalPrice
	*/
	public void setTheoreticalPrice(BigDecimal _TheoreticalPrice) {
		write(70, priceToString(_TheoreticalPrice));
	}
	/**
	* setter method to set TheoreticalPrice
	*
	* @param String - TheoreticalPrice
	*/
	public void setTheoreticalPrice(String _TheoreticalPrice) {
		write(70, _TheoreticalPrice);
	}
	/**
	* getter method to get TheoreticalPriceAsPrice
	*
	* @return BigDecimal - TheoreticalPriceAsPrice
	*/
	public BigDecimal getTheoreticalPriceAsPrice() {
		return (stringToPrice(readString(70, 19)));
	}
	/**
	* getter method to get TheoreticalPrice
	*
	* @return String - TheoreticalPrice
	*/
	public String getTheoreticalPrice() {
		return (readString(70, 19));
	}
	/**
	* setter method to set Filler_3
	*
	* @param String - Filler_3
	*/
	public void setFiller_3(String _Filler_3) {
		write(89, _Filler_3);
	}
	/**
	* getter method to get Filler_3
	*
	* @return String - Filler_3
	*/
	public String getFiller_3() {
		return (readString(89, 12));
	}
	/**
	* setter method to set NetChangeLimitsExpression
	*
	* @param String - NetChangeLimitsExpression
	*/
	public void setNetChangeLimitsExpression(String _NetChangeLimitsExpression) {
		write(101, _NetChangeLimitsExpression);
	}
	/**
	* getter method to get NetChangeLimitsExpression
	*
	* @return String - NetChangeLimitsExpression
	*/
	public String getNetChangeLimitsExpression() {
		return (readString(101, 1));
	}
	/**
	* setter method to set NetChangePrice
	*
	* @param BigDecimal - NetChangePrice
	*/
	public void setNetChangePrice(BigDecimal _NetChangePrice) {
		write(102, priceToString(_NetChangePrice));
	}
	/**
	* setter method to set NetChangePrice
	*
	* @param String - NetChangePrice
	*/
	public void setNetChangePrice(String _NetChangePrice) {
		write(102, _NetChangePrice);
	}
	/**
	* getter method to get NetChangePriceAsPrice
	*
	* @return BigDecimal - NetChangePriceAsPrice
	*/
	public BigDecimal getNetChangePriceAsPrice() {
		return (stringToPrice(readString(102, 19)));
	}
	/**
	* getter method to get NetChangePrice
	*
	* @return String - NetChangePrice
	*/
	public String getNetChangePrice() {
		return (readString(102, 19));
	}
	/**
	* setter method to set UnfilledQuantitySideAtOpening
	*
	* @param String - UnfilledQuantitySideAtOpening
	*/
	public void setUnfilledQuantitySideAtOpening(String _UnfilledQuantitySideAtOpening) {
		write(121, _UnfilledQuantitySideAtOpening);
	}
	/**
	* getter method to get UnfilledQuantitySideAtOpening
	*
	* @return String - UnfilledQuantitySideAtOpening
	*/
	public String getUnfilledQuantitySideAtOpening() {
		return (readString(121, 1));
	}
	/**
	* setter method to set UnfilledQuantitiesAtOpening
	*
	* @param int - UnfilledQuantitiesAtOpening
	*/
	public void setUnfilledQuantitiesAtOpening(int _UnfilledQuantitiesAtOpening) {
		write(122, pad(String.valueOf(_UnfilledQuantitiesAtOpening), '0', 12));
	}
	/**
	* setter method to set UnfilledQuantitiesAtOpening
	*
	* @param String - UnfilledQuantitiesAtOpening
	*/
	public void setUnfilledQuantitiesAtOpening(String _UnfilledQuantitiesAtOpening) {
		write(122, _UnfilledQuantitiesAtOpening);
	}
	/**
	* getter method to get UnfilledQuantitiesAtOpeningAsint
	*
	* @return int - UnfilledQuantitiesAtOpeningAsint
	*/
	public int getUnfilledQuantitiesAtOpeningAsint() {
		return (stringToint(readString(122, 12)));
	}
	/**
	* getter method to get UnfilledQuantitiesAtOpening
	*
	* @return String - UnfilledQuantitiesAtOpening
	*/
	public String getUnfilledQuantitiesAtOpening() {
		return (readString(122, 12));
	}
	/**
	* setter method to set Filler_4
	*
	* @param String - Filler_4
	*/
	public void setFiller_4(String _Filler_4) {
		write(134, _Filler_4);
	}
	/**
	* getter method to get Filler_4
	*
	* @return String - Filler_4
	*/
	public String getFiller_4() {
		return (readString(134, 12));
	}
	/**
	* setter method to set SimulatedBuyLimitPrice
	*
	* @param BigDecimal - SimulatedBuyLimitPrice
	*/
	public void setSimulatedBuyLimitPrice(BigDecimal _SimulatedBuyLimitPrice) {
		write(146, priceToString(_SimulatedBuyLimitPrice));
	}
	/**
	* setter method to set SimulatedBuyLimitPrice
	*
	* @param String - SimulatedBuyLimitPrice
	*/
	public void setSimulatedBuyLimitPrice(String _SimulatedBuyLimitPrice) {
		write(146, _SimulatedBuyLimitPrice);
	}
	/**
	* getter method to get SimulatedBuyLimitPriceAsPrice
	*
	* @return BigDecimal - SimulatedBuyLimitPriceAsPrice
	*/
	public BigDecimal getSimulatedBuyLimitPriceAsPrice() {
		return (stringToPrice(readString(146, 19)));
	}
	/**
	* getter method to get SimulatedBuyLimitPrice
	*
	* @return String - SimulatedBuyLimitPrice
	*/
	public String getSimulatedBuyLimitPrice() {
		return (readString(146, 19));
	}
	/**
	* setter method to set SimulatedSellLimitPrice
	*
	* @param BigDecimal - SimulatedSellLimitPrice
	*/
	public void setSimulatedSellLimitPrice(BigDecimal _SimulatedSellLimitPrice) {
		write(165, priceToString(_SimulatedSellLimitPrice));
	}
	/**
	* setter method to set SimulatedSellLimitPrice
	*
	* @param String - SimulatedSellLimitPrice
	*/
	public void setSimulatedSellLimitPrice(String _SimulatedSellLimitPrice) {
		write(165, _SimulatedSellLimitPrice);
	}
	/**
	* getter method to get SimulatedSellLimitPriceAsPrice
	*
	* @return BigDecimal - SimulatedSellLimitPriceAsPrice
	*/
	public BigDecimal getSimulatedSellLimitPriceAsPrice() {
		return (stringToPrice(readString(165, 19)));
	}
	/**
	* getter method to get SimulatedSellLimitPrice
	*
	* @return String - SimulatedSellLimitPrice
	*/
	public String getSimulatedSellLimitPrice() {
		return (readString(165, 19));
	}
	/**
	* setter method to set Filler_5
	*
	* @param String - Filler_5
	*/
	public void setFiller_5(String _Filler_5) {
		write(184, _Filler_5);
	}
	/**
	* getter method to get Filler_5
	*
	* @return String - Filler_5
	*/
	public String getFiller_5() {
		return (readString(184, 12));
	}

	/**
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("Filler_2=>" + getFiller_2() + "<\n");
		sw.write("TradingDate=>" + getTradingDateAsDate() + "<\n");
		sw.write("TradingDate=>" + getTradingDate() + "<\n");
		sw.write("CompleteInstrumentCode=>" + getCompleteInstrumentCode() + "<\n");
		sw.write("TheoreticalPrice=>" + getTheoreticalPriceAsPrice() + "<\n");
		sw.write("TheoreticalPrice=>" + getTheoreticalPrice() + "<\n");
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write(
			"NetChangeLimitsExpression=>" + getNetChangeLimitsExpression() + "<\n");
		sw.write("NetChangePrice=>" + getNetChangePriceAsPrice() + "<\n");
		sw.write("NetChangePrice=>" + getNetChangePrice() + "<\n");
		sw.write(
			"UnfilledQuantitySideAtOpening=>" + getUnfilledQuantitySideAtOpening() + "<\n");
		sw.write(
			"UnfilledQuantitiesAtOpening=>"
				+ getUnfilledQuantitiesAtOpeningAsint()
				+ "<\n");
		sw.write(
			"UnfilledQuantitiesAtOpening=>" + getUnfilledQuantitiesAtOpening() + "<\n");
		sw.write("Filler_4=>" + getFiller_4() + "<\n");
		sw.write(
			"SimulatedBuyLimitPrice=>" + getSimulatedBuyLimitPriceAsPrice() + "<\n");
		sw.write("SimulatedBuyLimitPrice=>" + getSimulatedBuyLimitPrice() + "<\n");
		sw.write(
			"SimulatedSellLimitPrice=>" + getSimulatedSellLimitPriceAsPrice() + "<\n");
		sw.write("SimulatedSellLimitPrice=>" + getSimulatedSellLimitPrice() + "<\n");
		sw.write("Filler_5=>" + getFiller_5() + "<\n");
		return (super.toString() + sw.toString());
	}
}
