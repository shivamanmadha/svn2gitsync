package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCM5Message
*
*/
public class RLCM5Message extends RLCMessage {
	/**
	* Constructor to construct RLCM5Message object 
	*
	*/
	public RLCM5Message() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_M5, true);
	}
	/**
	* Constructor to construct RLCM5Message object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCM5Message(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length > RLCMessageConstants.RLC_MESSAGE_LENGTH_M5) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for M5 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_M5);
		}
	}
	/**
	* Constructor to construct RLCM5Message object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCM5Message(int size, boolean fillWithSpaces) {
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
	* setter method to set TradeQuantity
	*
	* @param int - TradeQuantity
	*/
	public void setTradeQuantity(int _TradeQuantity) {
		write(70, pad(String.valueOf(_TradeQuantity), '0', 12));
	}
	/**
	* setter method to set TradeQuantity
	*
	* @param String - TradeQuantity
	*/
	public void setTradeQuantity(String _TradeQuantity) {
		write(70, _TradeQuantity);
	}
	/**
	* getter method to get TradeQuantityAsint
	*
	* @return int - TradeQuantityAsint
	*/
	public int getTradeQuantityAsint() {
		return (stringToint(readString(70, 12)));
	}
	/**
	* getter method to get TradeQuantity
	*
	* @return String - TradeQuantity
	*/
	public String getTradeQuantity() {
		return (readString(70, 12));
	}
	/**
	* setter method to set TradePrice
	*
	* @param BigDecimal - TradePrice
	*/
	public void setTradePrice(BigDecimal _TradePrice) {
		write(82, priceToString(_TradePrice));
	}
	/**
	* setter method to set TradePrice
	*
	* @param String - TradePrice
	*/
	public void setTradePrice(String _TradePrice) {
		write(82, _TradePrice);
	}
	/**
	* getter method to get TradePriceAsPrice
	*
	* @return BigDecimal - TradePriceAsPrice
	*/
	public BigDecimal getTradePriceAsPrice() {
		return (stringToPrice(readString(82, 19)));
	}
	/**
	* getter method to get TradePrice
	*
	* @return String - TradePrice
	*/
	public String getTradePrice() {
		return (readString(82, 19));
	}
	/**
	* setter method to set Filler_3
	*
	* @param String - Filler_3
	*/
	public void setFiller_3(String _Filler_3) {
		write(101, _Filler_3);
	}
	/**
	* getter method to get Filler_3
	*
	* @return String - Filler_3
	*/
	public String getFiller_3() {
		return (readString(101, 16));
	}
	/**
	* setter method to set TotalTradedDailyQuantity
	*
	* @param int - TotalTradedDailyQuantity
	*/
	public void setTotalTradedDailyQuantity(int _TotalTradedDailyQuantity) {
		write(117, pad(String.valueOf(_TotalTradedDailyQuantity), '0', 12));
	}
	/**
	* setter method to set TotalTradedDailyQuantity
	*
	* @param String - TotalTradedDailyQuantity
	*/
	public void setTotalTradedDailyQuantity(String _TotalTradedDailyQuantity) {
		write(117, _TotalTradedDailyQuantity);
	}
	/**
	* getter method to get TotalTradedDailyQuantityAsint
	*
	* @return int - TotalTradedDailyQuantityAsint
	*/
	public int getTotalTradedDailyQuantityAsint() {
		return (stringToint(readString(117, 12)));
	}
	/**
	* getter method to get TotalTradedDailyQuantity
	*
	* @return String - TotalTradedDailyQuantity
	*/
	public String getTotalTradedDailyQuantity() {
		return (readString(117, 12));
	}
	/**
	* setter method to set NetChangeFormatType
	*
	* @param String - NetChangeFormatType
	*/
	public void setNetChangeFormatType(String _NetChangeFormatType) {
		write(129, _NetChangeFormatType);
	}
	/**
	* getter method to get NetChangeFormatType
	*
	* @return String - NetChangeFormatType
	*/
	public String getNetChangeFormatType() {
		return (readString(129, 1));
	}
	/**
	* setter method to set NetChange
	*
	* @param BigDecimal - NetChange
	*/
	public void setNetChange(BigDecimal _NetChange) {
		write(130, priceToString(_NetChange));
	}
	/**
	* setter method to set NetChange
	*
	* @param String - NetChange
	*/
	public void setNetChange(String _NetChange) {
		write(130, _NetChange);
	}
	/**
	* getter method to get NetChangeAsPrice
	*
	* @return BigDecimal - NetChangeAsPrice
	*/
	public BigDecimal getNetChangeAsPrice() {
		return (stringToPrice(readString(130, 19)));
	}
	/**
	* getter method to get NetChange
	*
	* @return String - NetChange
	*/
	public String getNetChange() {
		return (readString(130, 19));
	}
	/**
	* setter method to set TradeTypeIndicator
	*
	* @param String - TradeTypeIndicator
	*/
	public void setTradeTypeIndicator(String _TradeTypeIndicator) {
		write(149, _TradeTypeIndicator);
	}
	/**
	* getter method to get TradeTypeIndicator
	*
	* @return String - TradeTypeIndicator
	*/
	public String getTradeTypeIndicator() {
		return (readString(149, 1));
	}
	/**
	* setter method to set LastTradeAtSamePriceIndicator
	*
	* @param String - LastTradeAtSamePriceIndicator
	*/
	public void setLastTradeAtSamePriceIndicator(String _LastTradeAtSamePriceIndicator) {
		write(150, _LastTradeAtSamePriceIndicator);
	}
	/**
	* getter method to get LastTradeAtSamePriceIndicator
	*
	* @return String - LastTradeAtSamePriceIndicator
	*/
	public String getLastTradeAtSamePriceIndicator() {
		return (readString(150, 1));
	}
	/**
	* setter method to set PriceVariation
	*
	* @param String - PriceVariation
	*/
	public void setPriceVariation(String _PriceVariation) {
		write(151, _PriceVariation);
	}
	/**
	* getter method to get PriceVariation
	*
	* @return String - PriceVariation
	*/
	public String getPriceVariation() {
		return (readString(151, 1));
	}
	/**
	* setter method to set Filler_4
	*
	* @param String - Filler_4
	*/
	public void setFiller_4(String _Filler_4) {
		write(152, _Filler_4);
	}
	/**
	* getter method to get Filler_4
	*
	* @return String - Filler_4
	*/
	public String getFiller_4() {
		return (readString(152, 16));
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
		sw.write("TradeQuantity=>" + getTradeQuantityAsint() + "<\n");
		sw.write("TradeQuantity=>" + getTradeQuantity() + "<\n");
		sw.write("TradePrice=>" + getTradePriceAsPrice() + "<\n");
		sw.write("TradePrice=>" + getTradePrice() + "<\n");
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write(
			"TotalTradedDailyQuantity=>" + getTotalTradedDailyQuantityAsint() + "<\n");
		sw.write("TotalTradedDailyQuantity=>" + getTotalTradedDailyQuantity() + "<\n");
		sw.write("NetChangeFormatType=>" + getNetChangeFormatType() + "<\n");
		sw.write("NetChange=>" + getNetChangeAsPrice() + "<\n");
		sw.write("NetChange=>" + getNetChange() + "<\n");
		sw.write("TradeTypeIndicator=>" + getTradeTypeIndicator() + "<\n");
		sw.write(
			"LastTradeAtSamePriceIndicator=>" + getLastTradeAtSamePriceIndicator() + "<\n");
		sw.write("PriceVariation=>" + getPriceVariation() + "<\n");
		//sw.write("Filler_4=>"+ getFiller_4() +"<\n");
		return (super.toString() + sw.toString());
	}
}
