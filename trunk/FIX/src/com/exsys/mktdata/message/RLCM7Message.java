package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCM7Message
*
*/
public class RLCM7Message extends RLCMessage {
	/**
	* Constructor to construct RLCM7Message object 
	*
	*/
	public RLCM7Message() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_M7, true);
	}
	/**
	* Constructor to construct RLCM7Message object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCM7Message(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_M7) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for M7 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_M7);
		}
	}
	/**
	* Constructor to construct RLCM7Message object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCM7Message(int size, boolean fillWithSpaces) {
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
	* setter method to set Price
	*
	* @param BigDecimal - Price
	*/
	public void setPrice(BigDecimal _Price) {
		write(70, priceToString(_Price));
	}
	/**
	* setter method to set Price
	*
	* @param String - Price
	*/
	public void setPrice(String _Price) {
		write(70, _Price);
	}
	/**
	* getter method to get PriceAsPrice
	*
	* @return BigDecimal - PriceAsPrice
	*/
	public BigDecimal getPriceAsPrice() {
		return (stringToPrice(readString(70, 19)));
	}
	/**
	* getter method to get Price
	*
	* @return String - Price
	*/
	public String getPrice() {
		return (readString(70, 19));
	}
	/**
	* setter method to set HighestPrice
	*
	* @param BigDecimal - HighestPrice
	*/
	public void setHighestPrice(BigDecimal _HighestPrice) {
		write(89, priceToString(_HighestPrice));
	}
	/**
	* setter method to set HighestPrice
	*
	* @param String - HighestPrice
	*/
	public void setHighestPrice(String _HighestPrice) {
		write(89, _HighestPrice);
	}
	/**
	* getter method to get HighestPriceAsPrice
	*
	* @return BigDecimal - HighestPriceAsPrice
	*/
	public BigDecimal getHighestPriceAsPrice() {
		return (stringToPrice(readString(89, 19)));
	}
	/**
	* getter method to get HighestPrice
	*
	* @return String - HighestPrice
	*/
	public String getHighestPrice() {
		return (readString(89, 19));
	}
	/**
	* setter method to set LowestPrice
	*
	* @param BigDecimal - LowestPrice
	*/
	public void setLowestPrice(BigDecimal _LowestPrice) {
		write(108, priceToString(_LowestPrice));
	}
	/**
	* setter method to set LowestPrice
	*
	* @param String - LowestPrice
	*/
	public void setLowestPrice(String _LowestPrice) {
		write(108, _LowestPrice);
	}
	/**
	* getter method to get LowestPriceAsPrice
	*
	* @return BigDecimal - LowestPriceAsPrice
	*/
	public BigDecimal getLowestPriceAsPrice() {
		return (stringToPrice(readString(108, 19)));
	}
	/**
	* getter method to get LowestPrice
	*
	* @return String - LowestPrice
	*/
	public String getLowestPrice() {
		return (readString(108, 19));
	}
	/**
	* setter method to set PriceTypeFlag
	*
	* @param String - PriceTypeFlag
	*/
	public void setPriceTypeFlag(String _PriceTypeFlag) {
		write(127, _PriceTypeFlag);
	}
	/**
	* getter method to get PriceTypeFlag
	*
	* @return String - PriceTypeFlag
	*/
	public String getPriceTypeFlag() {
		return (readString(127, 2));
	}
	/**
	* setter method to set NetChangeLimitsExpression
	*
	* @param String - NetChangeLimitsExpression
	*/
	public void setNetChangeLimitsExpression(String _NetChangeLimitsExpression) {
		write(129, _NetChangeLimitsExpression);
	}
	/**
	* getter method to get NetChangeLimitsExpression
	*
	* @return String - NetChangeLimitsExpression
	*/
	public String getNetChangeLimitsExpression() {
		return (readString(129, 1));
	}
	/**
	* setter method to set NetChangePrice
	*
	* @param BigDecimal - NetChangePrice
	*/
	public void setNetChangePrice(BigDecimal _NetChangePrice) {
		write(130, priceToString(_NetChangePrice));
	}
	/**
	* setter method to set NetChangePrice
	*
	* @param String - NetChangePrice
	*/
	public void setNetChangePrice(String _NetChangePrice) {
		write(130, _NetChangePrice);
	}
	/**
	* getter method to get NetChangePriceAsPrice
	*
	* @return BigDecimal - NetChangePriceAsPrice
	*/
	public BigDecimal getNetChangePriceAsPrice() {
		return (stringToPrice(readString(130, 19)));
	}
	/**
	* getter method to get NetChangePrice
	*
	* @return String - NetChangePrice
	*/
	public String getNetChangePrice() {
		return (readString(130, 19));
	}
	/**
	* setter method to set SideVariation
	*
	* @param String - SideVariation
	*/
	public void setSideVariation(String _SideVariation) {
		write(149, _SideVariation);
	}
	/**
	* getter method to get SideVariation
	*
	* @return String - SideVariation
	*/
	public String getSideVariation() {
		return (readString(149, 1));
	}
	/**
	* setter method to set Filler_3
	*
	* @param String - Filler_3
	*/
	public void setFiller_3(String _Filler_3) {
		write(150, _Filler_3);
	}
	/**
	* getter method to get Filler_3
	*
	* @return String - Filler_3
	*/
	public String getFiller_3() {
		return (readString(150, 19));
	}
	/**
	* setter method to set MarginRate
	*
	* @param String - MarginRate
	*/
	public void setMarginRate(String _MarginRate) {
		write(169, _MarginRate);
	}
	/**
	* getter method to get MarginRate
	*
	* @return String - MarginRate
	*/
	public String getMarginRate() {
		return (readString(169, 6));
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
		sw.write("Price=>" + getPriceAsPrice() + "<\n");
		sw.write("Price=>" + getPrice() + "<\n");
		sw.write("HighestPrice=>" + getHighestPriceAsPrice() + "<\n");
		sw.write("HighestPrice=>" + getHighestPrice() + "<\n");
		sw.write("LowestPrice=>" + getLowestPriceAsPrice() + "<\n");
		sw.write("LowestPrice=>" + getLowestPrice() + "<\n");
		sw.write("PriceTypeFlag=>" + getPriceTypeFlag() + "<\n");
		sw.write(
			"NetChangeLimitsExpression=>" + getNetChangeLimitsExpression() + "<\n");
		sw.write("NetChangePrice=>" + getNetChangePriceAsPrice() + "<\n");
		sw.write("NetChangePrice=>" + getNetChangePrice() + "<\n");
		sw.write("SideVariation=>" + getSideVariation() + "<\n");
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write("MarginRate=>" + getMarginRate() + "<\n");
		return (super.toString() + sw.toString());
	}
}
