package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCM9Message
*
*/
public class RLCM9Message extends RLCMessage {
	/**
	* Constructor to construct RLCM9Message object 
	*
	*/
	public RLCM9Message() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_M9, true);
	}
	/**
	* Constructor to construct RLCM9Message object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCM9Message(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_M9) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for M9 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_M9);
		}
	}
	/**
	* Constructor to construct RLCM9Message object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCM9Message(int size, boolean fillWithSpaces) {
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
	* setter method to set FirstTradedPrice
	*
	* @param BigDecimal - FirstTradedPrice
	*/
	public void setFirstTradedPrice(BigDecimal _FirstTradedPrice) {
		write(70, priceToString(_FirstTradedPrice));
	}
	/**
	* setter method to set FirstTradedPrice
	*
	* @param String - FirstTradedPrice
	*/
	public void setFirstTradedPrice(String _FirstTradedPrice) {
		write(70, _FirstTradedPrice);
	}
	/**
	* getter method to get FirstTradedPriceAsPrice
	*
	* @return BigDecimal - FirstTradedPriceAsPrice
	*/
	public BigDecimal getFirstTradedPriceAsPrice() {
		return (stringToPrice(readString(70, 19)));
	}
	/**
	* getter method to get FirstTradedPrice
	*
	* @return String - FirstTradedPrice
	*/
	public String getFirstTradedPrice() {
		return (readString(70, 19));
	}
	/**
	* setter method to set LastTradedPrice
	*
	* @param BigDecimal - LastTradedPrice
	*/
	public void setLastTradedPrice(BigDecimal _LastTradedPrice) {
		write(89, priceToString(_LastTradedPrice));
	}
	/**
	* setter method to set LastTradedPrice
	*
	* @param String - LastTradedPrice
	*/
	public void setLastTradedPrice(String _LastTradedPrice) {
		write(89, _LastTradedPrice);
	}
	/**
	* getter method to get LastTradedPriceAsPrice
	*
	* @return BigDecimal - LastTradedPriceAsPrice
	*/
	public BigDecimal getLastTradedPriceAsPrice() {
		return (stringToPrice(readString(89, 19)));
	}
	/**
	* getter method to get LastTradedPrice
	*
	* @return String - LastTradedPrice
	*/
	public String getLastTradedPrice() {
		return (readString(89, 19));
	}
	/**
	* setter method to set HighestTradedPrice
	*
	* @param BigDecimal - HighestTradedPrice
	*/
	public void setHighestTradedPrice(BigDecimal _HighestTradedPrice) {
		write(108, priceToString(_HighestTradedPrice));
	}
	/**
	* setter method to set HighestTradedPrice
	*
	* @param String - HighestTradedPrice
	*/
	public void setHighestTradedPrice(String _HighestTradedPrice) {
		write(108, _HighestTradedPrice);
	}
	/**
	* getter method to get HighestTradedPriceAsPrice
	*
	* @return BigDecimal - HighestTradedPriceAsPrice
	*/
	public BigDecimal getHighestTradedPriceAsPrice() {
		return (stringToPrice(readString(108, 19)));
	}
	/**
	* getter method to get HighestTradedPrice
	*
	* @return String - HighestTradedPrice
	*/
	public String getHighestTradedPrice() {
		return (readString(108, 19));
	}
	/**
	* setter method to set LowestTradedPrice
	*
	* @param BigDecimal - LowestTradedPrice
	*/
	public void setLowestTradedPrice(BigDecimal _LowestTradedPrice) {
		write(127, priceToString(_LowestTradedPrice));
	}
	/**
	* setter method to set LowestTradedPrice
	*
	* @param String - LowestTradedPrice
	*/
	public void setLowestTradedPrice(String _LowestTradedPrice) {
		write(127, _LowestTradedPrice);
	}
	/**
	* getter method to get LowestTradedPriceAsPrice
	*
	* @return BigDecimal - LowestTradedPriceAsPrice
	*/
	public BigDecimal getLowestTradedPriceAsPrice() {
		return (stringToPrice(readString(127, 19)));
	}
	/**
	* getter method to get LowestTradedPrice
	*
	* @return String - LowestTradedPrice
	*/
	public String getLowestTradedPrice() {
		return (readString(127, 19));
	}
	/**
	* setter method to set TotalTradeQuantity
	*
	* @param int - TotalTradeQuantity
	*/
	public void setTotalTradeQuantity(int _TotalTradeQuantity) {
		write(146, pad(String.valueOf(_TotalTradeQuantity), '0', 12));
	}
	/**
	* setter method to set TotalTradeQuantity
	*
	* @param String - TotalTradeQuantity
	*/
	public void setTotalTradeQuantity(String _TotalTradeQuantity) {
		write(146, _TotalTradeQuantity);
	}
	/**
	* getter method to get TotalTradeQuantityAsint
	*
	* @return int - TotalTradeQuantityAsint
	*/
	public int getTotalTradeQuantityAsint() {
		return (stringToint(readString(146, 12)));
	}
	/**
	* getter method to get TotalTradeQuantity
	*
	* @return String - TotalTradeQuantity
	*/
	public String getTotalTradeQuantity() {
		return (readString(146, 12));
	}
	/**
	* setter method to set TrendFlag
	*
	* @param String - TrendFlag
	*/
	public void setTrendFlag(String _TrendFlag) {
		write(158, _TrendFlag);
	}
	/**
	* getter method to get TrendFlag
	*
	* @return String - TrendFlag
	*/
	public String getTrendFlag() {
		return (readString(158, 2));
	}
	/**
	* setter method to set NetChangeLimitsExpr
	*
	* @param String - NetChangeLimitsExpr
	*/
	public void setNetChangeLimitsExpr(String _NetChangeLimitsExpr) {
		write(160, _NetChangeLimitsExpr);
	}
	/**
	* getter method to get NetChangeLimitsExpr
	*
	* @return String - NetChangeLimitsExpr
	*/
	public String getNetChangeLimitsExpr() {
		return (readString(160, 1));
	}
	/**
	* setter method to set NetChangePrice
	*
	* @param BigDecimal - NetChangePrice
	*/
	public void setNetChangePrice(BigDecimal _NetChangePrice) {
		write(161, priceToString(_NetChangePrice));
	}
	/**
	* setter method to set NetChangePrice
	*
	* @param String - NetChangePrice
	*/
	public void setNetChangePrice(String _NetChangePrice) {
		write(161, _NetChangePrice);
	}
	/**
	* getter method to get NetChangePriceAsPrice
	*
	* @return BigDecimal - NetChangePriceAsPrice
	*/
	public BigDecimal getNetChangePriceAsPrice() {
		return (stringToPrice(readString(161, 19)));
	}
	/**
	* getter method to get NetChangePrice
	*
	* @return String - NetChangePrice
	*/
	public String getNetChangePrice() {
		return (readString(161, 19));
	}
	/**
	* setter method to set PriceVariation
	*
	* @param String - PriceVariation
	*/
	public void setPriceVariation(String _PriceVariation) {
		write(180, _PriceVariation);
	}
	/**
	* getter method to get PriceVariation
	*
	* @return String - PriceVariation
	*/
	public String getPriceVariation() {
		return (readString(180, 1));
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
		sw.write("FirstTradedPrice=>" + getFirstTradedPriceAsPrice() + "<\n");
		sw.write("FirstTradedPrice=>" + getFirstTradedPrice() + "<\n");
		sw.write("LastTradedPrice=>" + getLastTradedPriceAsPrice() + "<\n");
		sw.write("LastTradedPrice=>" + getLastTradedPrice() + "<\n");
		sw.write("HighestTradedPrice=>" + getHighestTradedPriceAsPrice() + "<\n");
		sw.write("HighestTradedPrice=>" + getHighestTradedPrice() + "<\n");
		sw.write("LowestTradedPrice=>" + getLowestTradedPriceAsPrice() + "<\n");
		sw.write("LowestTradedPrice=>" + getLowestTradedPrice() + "<\n");
		sw.write("TotalTradeQuantity=>" + getTotalTradeQuantityAsint() + "<\n");
		sw.write("TotalTradeQuantity=>" + getTotalTradeQuantity() + "<\n");
		sw.write("TrendFlag=>" + getTrendFlag() + "<\n");
		sw.write("NetChangeLimitsExpr=>" + getNetChangeLimitsExpr() + "<\n");
		sw.write("NetChangePrice=>" + getNetChangePriceAsPrice() + "<\n");
		sw.write("NetChangePrice=>" + getNetChangePrice() + "<\n");
		sw.write("PriceVariation=>" + getPriceVariation() + "<\n");
		return (super.toString() + sw.toString());
	}
}
