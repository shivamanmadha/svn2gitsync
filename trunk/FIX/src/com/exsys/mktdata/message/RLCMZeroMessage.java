package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMZeroMessage
*
*/
public class RLCMZeroMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMZeroMessage object 
	*
	*/
	public RLCMZeroMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_M0, true);
	}
	/**
	* Constructor to construct RLCMZeroMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMZeroMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length > RLCMessageConstants.RLC_MESSAGE_LENGTH_M0) {
			throw new RLCProtocolError(
				"Invalid Message Length" + newBytes.length,
				"MessageLength for M0 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_M0);
		}
	}
	/**
	* Constructor to construct RLCMZeroMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMZeroMessage(int size, boolean fillWithSpaces) {
		super(size, fillWithSpaces);
	}
	/**
	* setter method to set Filler_2
	*
	* @param String - Filler_2
	*
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
	*
	*/
	public void setTradingDate(Date _TradingDate) {
		write(42, getYYYYMMDDString(_TradingDate));
	}
	/**
	* setter method to set TradingDate
	*
	* @param String - TradingDate
	*
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
	*
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
	* setter method to set LastPrice
	*
	* @param BigDecimal - LastPrice
	*
	*/
	public void setLastPrice(BigDecimal _LastPrice) {
		write(70, priceToString(_LastPrice));
	}
	/**
	* setter method to set LastPrice
	*
	* @param String - LastPrice
	*
	*/
	public void setLastPrice(String _LastPrice) {
		write(70, _LastPrice);
	}
	/**
	* getter method to get LastPriceAsPrice
	*
	* @return BigDecimal - LastPriceAsPrice
	*/
	public BigDecimal getLastPriceAsPrice() {
		return (stringToPrice(readString(70, 19)));
	}
	/**
	* getter method to get LastPrice
	*
	* @return String - LastPrice
	*/
	public String getLastPrice() {
		return (readString(70, 19));
	}
	/**
	* setter method to set LastPriceType
	*
	* @param String - LastPriceType
	*
	*/
	public void setLastPriceType(String _LastPriceType) {
		write(89, _LastPriceType);
	}
	/**
	* getter method to get LastPriceType
	*
	* @return String - LastPriceType
	*/
	public String getLastPriceType() {
		return (readString(89, 1));
	}
	/**
	* setter method to set HighestPrice
	*
	* @param BigDecimal - HighestPrice
	*
	*/
	public void setHighestPrice(BigDecimal _HighestPrice) {
		write(90, priceToString(_HighestPrice));
	}
	/**
	* setter method to set HighestPrice
	*
	* @param String - HighestPrice
	*
	*/
	public void setHighestPrice(String _HighestPrice) {
		write(90, _HighestPrice);
	}
	/**
	* getter method to get HighestPriceAsPrice
	*
	* @return BigDecimal - HighestPriceAsPrice
	*/
	public BigDecimal getHighestPriceAsPrice() {
		return (stringToPrice(readString(90, 19)));
	}
	/**
	* getter method to get HighestPrice
	*
	* @return String - HighestPrice
	*/
	public String getHighestPrice() {
		return (readString(90, 19));
	}
	/**
	* setter method to set HighestPriceType
	*
	* @param String - HighestPriceType
	*
	*/
	public void setHighestPriceType(String _HighestPriceType) {
		write(109, _HighestPriceType);
	}
	/**
	* getter method to get HighestPriceType
	*
	* @return String - HighestPriceType
	*/
	public String getHighestPriceType() {
		return (readString(109, 1));
	}
	/**
	* setter method to set LowestPrice
	*
	* @param BigDecimal - LowestPrice
	*
	*/
	public void setLowestPrice(BigDecimal _LowestPrice) {
		write(110, priceToString(_LowestPrice));
	}
	/**
	* setter method to set LowestPrice
	*
	* @param String - LowestPrice
	*
	*/
	public void setLowestPrice(String _LowestPrice) {
		write(110, _LowestPrice);
	}
	/**
	* getter method to get LowestPriceAsPrice
	*
	* @return BigDecimal - LowestPriceAsPrice
	*/
	public BigDecimal getLowestPriceAsPrice() {
		return (stringToPrice(readString(110, 19)));
	}
	/**
	* getter method to get LowestPrice
	*
	* @return String - LowestPrice
	*/
	public String getLowestPrice() {
		return (readString(110, 19));
	}
	/**
	* setter method to set LowestPriceType
	*
	* @param String - LowestPriceType
	*
	*/
	public void setLowestPriceType(String _LowestPriceType) {
		write(129, _LowestPriceType);
	}
	/**
	* getter method to get LowestPriceType
	*
	* @return String - LowestPriceType
	*/
	public String getLowestPriceType() {
		return (readString(129, 1));
	}
	/**
	* setter method to set LastTradeQuantity
	*
	* @param int - LastTradeQuantity
	*
	*/
	public void setLastTradeQuantity(int _LastTradeQuantity) {
		write(130, pad(String.valueOf(_LastTradeQuantity), '0', 12));
	}
	/**
	* setter method to set LastTradeQuantity
	*
	* @param String - LastTradeQuantity
	*
	*/
	public void setLastTradeQuantity(String _LastTradeQuantity) {
		write(130, _LastTradeQuantity);
	}
	/**
	* getter method to get LastTradeQuantityAsint
	*
	* @return int - LastTradeQuantityAsint
	*/
	public int getLastTradeQuantityAsint() {
		return (stringToint(readString(130, 12)));
	}
	/**
	* getter method to get LastTradeQuantity
	*
	* @return String - LastTradeQuantity
	*/
	public String getLastTradeQuantity() {
		return (readString(130, 12));
	}
	/**
	* setter method to set TradingMode
	*
	* @param String - TradingMode
	*
	*/
	public void setTradingMode(String _TradingMode) {
		write(142, _TradingMode);
	}
	/**
	* getter method to get TradingMode
	*
	* @return String - TradingMode
	*/
	public String getTradingMode() {
		return (readString(142, 1));
	}
	/**
	* setter method to set NetChangeLimitsExpr
	*
	* @param String - NetChangeLimitsExpr
	*
	*/
	public void setNetChangeLimitsExpr(String _NetChangeLimitsExpr) {
		write(143, _NetChangeLimitsExpr);
	}
	/**
	* getter method to get NetChangeLimitsExpr
	*
	* @return String - NetChangeLimitsExpr
	*/
	public String getNetChangeLimitsExpr() {
		return (readString(143, 1));
	}
	/**
	* setter method to set ActualNetChange
	*
	* @param BigDecimal - ActualNetChange
	*
	*/
	public void setActualNetChange(BigDecimal _ActualNetChange) {
		write(144, priceToString(_ActualNetChange));
	}
	/**
	* setter method to set ActualNetChange
	*
	* @param String - ActualNetChange
	*
	*/
	public void setActualNetChange(String _ActualNetChange) {
		write(144, _ActualNetChange);
	}
	/**
	* getter method to get ActualNetChangeAsPrice
	*
	* @return BigDecimal - ActualNetChangeAsPrice
	*/
	public BigDecimal getActualNetChangeAsPrice() {
		return (stringToPrice(readString(144, 19)));
	}
	/**
	* getter method to get ActualNetChange
	*
	* @return String - ActualNetChange
	*/
	public String getActualNetChange() {
		return (readString(144, 19));
	}
	/**
	* setter method to set SideVariation
	*
	* @param String - SideVariation
	*
	*/
	public void setSideVariation(String _SideVariation) {
		write(163, _SideVariation);
	}
	/**
	* getter method to get SideVariation
	*
	* @return String - SideVariation
	*/
	public String getSideVariation() {
		return (readString(163, 1));
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
		sw.write("LastPrice=>" + getLastPriceAsPrice() + "<\n");
		sw.write("LastPrice=>" + getLastPrice() + "<\n");
		sw.write("LastPriceType=>" + getLastPriceType() + "<\n");
		sw.write("HighestPrice=>" + getHighestPriceAsPrice() + "<\n");
		sw.write("HighestPrice=>" + getHighestPrice() + "<\n");
		sw.write("HighestPriceType=>" + getHighestPriceType() + "<\n");
		sw.write("LowestPrice=>" + getLowestPriceAsPrice() + "<\n");
		sw.write("LowestPrice=>" + getLowestPrice() + "<\n");
		sw.write("LowestPriceType=>" + getLowestPriceType() + "<\n");
		sw.write("LastTradeQuantity=>" + getLastTradeQuantityAsint() + "<\n");
		sw.write("LastTradeQuantity=>" + getLastTradeQuantity() + "<\n");
		sw.write("TradingMode=>" + getTradingMode() + "<\n");
		sw.write("NetChangeLimitsExpr=>" + getNetChangeLimitsExpr() + "<\n");
		sw.write("ActualNetChange=>" + getActualNetChangeAsPrice() + "<\n");
		sw.write("ActualNetChange=>" + getActualNetChange() + "<\n");
		//sw.write("SideVariation=>"+ getSideVariation() +"<\n");
		return (super.toString() + sw.toString());
	}
}
