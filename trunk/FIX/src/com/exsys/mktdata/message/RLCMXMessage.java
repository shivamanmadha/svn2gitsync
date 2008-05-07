package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMXMessage
*
*/
public class RLCMXMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMXMessage object 
	*
	*/
	public RLCMXMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MX, true);
	}
	/**
	* Constructor to construct RLCMXMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMXMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_MX) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MX Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MX);
		}
	}
	/**
	* Constructor to construct RLCMXMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMXMessage(int size, boolean fillWithSpaces) {
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
	* setter method to set BidQuantity
	*
	* @param int - BidQuantity
	*
	*/
	public void setBidQuantity(int _BidQuantity) {
		write(70, pad(String.valueOf(_BidQuantity), '0', 12));
	}
	/**
	* setter method to set BidQuantity
	*
	* @param String - BidQuantity
	*
	*/
	public void setBidQuantity(String _BidQuantity) {
		write(70, _BidQuantity);
	}
	/**
	* getter method to get BidQuantityAsint
	*
	* @return int - BidQuantityAsint
	*/
	public int getBidQuantityAsint() {
		return (stringToint(readString(70, 12)));
	}
	/**
	* getter method to get BidQuantity
	*
	* @return String - BidQuantity
	*/
	public String getBidQuantity() {
		return (readString(70, 12));
	}
	/**
	* setter method to set NumberOfBidQuotes
	*
	* @param int - NumberOfBidQuotes
	*
	*/
	public void setNumberOfBidQuotes(int _NumberOfBidQuotes) {
		write(82, pad(String.valueOf(_NumberOfBidQuotes), '0', 4));
	}
	/**
	* setter method to set NumberOfBidQuotes
	*
	* @param String - NumberOfBidQuotes
	*
	*/
	public void setNumberOfBidQuotes(String _NumberOfBidQuotes) {
		write(82, _NumberOfBidQuotes);
	}
	/**
	* getter method to get NumberOfBidQuotesAsint
	*
	* @return int - NumberOfBidQuotesAsint
	*/
	public int getNumberOfBidQuotesAsint() {
		return (stringToint(readString(82, 4)));
	}
	/**
	* getter method to get NumberOfBidQuotes
	*
	* @return String - NumberOfBidQuotes
	*/
	public String getNumberOfBidQuotes() {
		return (readString(82, 4));
	}
	/**
	* setter method to set BidPrice
	*
	* @param BigDecimal - BidPrice
	*
	*/
	public void setBidPrice(BigDecimal _BidPrice) {
		write(86, priceToString(_BidPrice));
	}
	/**
	* setter method to set BidPrice
	*
	* @param String - BidPrice
	*
	*/
	public void setBidPrice(String _BidPrice) {
		write(86, _BidPrice);
	}
	/**
	* getter method to get BidPriceAsPrice
	*
	* @return BigDecimal - BidPriceAsPrice
	*/
	public BigDecimal getBidPriceAsPrice() {
		return (stringToPrice(readString(86, 19)));
	}
	/**
	* getter method to get BidPrice
	*
	* @return String - BidPrice
	*/
	public String getBidPrice() {
		return (readString(86, 19));
	}
	/**
	* setter method to set AskPrice
	*
	* @param BigDecimal - AskPrice
	*
	*/
	public void setAskPrice(BigDecimal _AskPrice) {
		write(105, priceToString(_AskPrice));
	}
	/**
	* setter method to set AskPrice
	*
	* @param String - AskPrice
	*
	*/
	public void setAskPrice(String _AskPrice) {
		write(105, _AskPrice);
	}
	/**
	* getter method to get AskPriceAsPrice
	*
	* @return BigDecimal - AskPriceAsPrice
	*/
	public BigDecimal getAskPriceAsPrice() {
		return (stringToPrice(readString(105, 19)));
	}
	/**
	* getter method to get AskPrice
	*
	* @return String - AskPrice
	*/
	public String getAskPrice() {
		return (readString(105, 19));
	}
	/**
	* setter method to set NumberOfAskQuotes
	*
	* @param int - NumberOfAskQuotes
	*
	*/
	public void setNumberOfAskQuotes(int _NumberOfAskQuotes) {
		write(124, pad(String.valueOf(_NumberOfAskQuotes), '0', 4));
	}
	/**
	* setter method to set NumberOfAskQuotes
	*
	* @param String - NumberOfAskQuotes
	*
	*/
	public void setNumberOfAskQuotes(String _NumberOfAskQuotes) {
		write(124, _NumberOfAskQuotes);
	}
	/**
	* getter method to get NumberOfAskQuotesAsint
	*
	* @return int - NumberOfAskQuotesAsint
	*/
	public int getNumberOfAskQuotesAsint() {
		return (stringToint(readString(124, 4)));
	}
	/**
	* getter method to get NumberOfAskQuotes
	*
	* @return String - NumberOfAskQuotes
	*/
	public String getNumberOfAskQuotes() {
		return (readString(124, 4));
	}
	/**
	* setter method to set AskQuantity
	*
	* @param int - AskQuantity
	*
	*/
	public void setAskQuantity(int _AskQuantity) {
		write(128, pad(String.valueOf(_AskQuantity), '0', 12));
	}
	/**
	* setter method to set AskQuantity
	*
	* @param String - AskQuantity
	*
	*/
	public void setAskQuantity(String _AskQuantity) {
		write(128, _AskQuantity);
	}
	/**
	* getter method to get AskQuantityAsint
	*
	* @return int - AskQuantityAsint
	*/
	public int getAskQuantityAsint() {
		return (stringToint(readString(128, 12)));
	}
	/**
	* getter method to get AskQuantity
	*
	* @return String - AskQuantity
	*/
	public String getAskQuantity() {
		return (readString(128, 12));
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
		sw.write("BidQuantity=>" + getBidQuantityAsint() + "<\n");
		sw.write("BidQuantity=>" + getBidQuantity() + "<\n");
		sw.write("NumberOfBidQuotes=>" + getNumberOfBidQuotesAsint() + "<\n");
		sw.write("NumberOfBidQuotes=>" + getNumberOfBidQuotes() + "<\n");
		sw.write("BidPrice=>" + getBidPriceAsPrice() + "<\n");
		sw.write("BidPrice=>" + getBidPrice() + "<\n");
		sw.write("AskPrice=>" + getAskPriceAsPrice() + "<\n");
		sw.write("AskPrice=>" + getAskPrice() + "<\n");
		sw.write("NumberOfAskQuotes=>" + getNumberOfAskQuotesAsint() + "<\n");
		sw.write("NumberOfAskQuotes=>" + getNumberOfAskQuotes() + "<\n");
		sw.write("AskQuantity=>" + getAskQuantityAsint() + "<\n");
		sw.write("AskQuantity=>" + getAskQuantity() + "<\n");
		return (super.toString() + sw.toString());
	}
}
