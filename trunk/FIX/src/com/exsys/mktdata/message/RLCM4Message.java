package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCM4Message
*
*/
public class RLCM4Message extends RLCMessage {
	/**
	* Constructor to construct RLCM4Message object 
	*
	*/
	public RLCM4Message() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_M4, true);
	}
	/**
	* Constructor to construct RLCM4Message object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCM4Message(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_M4) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for M4 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_M4);
		}
	}
	/**
	* Constructor to construct RLCM4Message object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCM4Message(int size, boolean fillWithSpaces) {
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
	* setter method to set Filler_3
	*
	* @param String - Filler_3
	*/
	public void setFiller_3(String _Filler_3) {
		write(70, _Filler_3);
	}
	/**
	* getter method to get Filler_3
	*
	* @return String - Filler_3
	*/
	public String getFiller_3() {
		return (readString(70, 16));
	}
	/**
	* setter method to set TradeQuantity
	*
	* @param int - TradeQuantity
	*/
	public void setTradeQuantity(int _TradeQuantity) {
		write(86, pad(String.valueOf(_TradeQuantity), '0', 12));
	}
	/**
	* setter method to set TradeQuantity
	*
	* @param String - TradeQuantity
	*/
	public void setTradeQuantity(String _TradeQuantity) {
		write(86, _TradeQuantity);
	}
	/**
	* getter method to get TradeQuantityAsint
	*
	* @return int - TradeQuantityAsint
	*/
	public int getTradeQuantityAsint() {
		return (stringToint(readString(86, 12)));
	}
	/**
	* getter method to get TradeQuantity
	*
	* @return String - TradeQuantity
	*/
	public String getTradeQuantity() {
		return (readString(86, 12));
	}
	/**
	* setter method to set Side
	*
	* @param String - Side
	*/
	public void setSide(String _Side) {
		write(98, _Side);
	}
	/**
	* getter method to get Side
	*
	* @return String - Side
	*/
	public String getSide() {
		return (readString(98, 1));
	}
	/**
	* setter method to set QuoteType
	*
	* @param String - QuoteType
	*/
	public void setQuoteType(String _QuoteType) {
		write(99, _QuoteType);
	}
	/**
	* getter method to get QuoteType
	*
	* @return String - QuoteType
	*/
	public String getQuoteType() {
		return (readString(99, 1));
	}
	/**
	* setter method to set ExchangeQuoteRequestID
	*
	* @param String - ExchangeQuoteRequestID
	*/
	public void setExchangeQuoteRequestID(String _ExchangeQuoteRequestID) {
		write(100, _ExchangeQuoteRequestID);
	}
	/**
	* getter method to get ExchangeQuoteRequestID
	*
	* @return String - ExchangeQuoteRequestID
	*/
	public String getExchangeQuoteRequestID() {
		return (readString(100, 23));
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
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write("TradeQuantity=>" + getTradeQuantityAsint() + "<\n");
		sw.write("TradeQuantity=>" + getTradeQuantity() + "<\n");
		sw.write("Side=>" + getSide() + "<\n");
		sw.write("QuoteType=>" + getQuoteType() + "<\n");
		sw.write("ExchangeQuoteRequestID=>" + getExchangeQuoteRequestID() + "<\n");
		return (super.toString() + sw.toString());
	}
}
