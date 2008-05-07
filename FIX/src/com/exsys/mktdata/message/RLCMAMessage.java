package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMAMessage
*
*/
public class RLCMAMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMAMessage object 
	*
	*/
	public RLCMAMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MA, true);
	}
	/**
	* Constructor to construct RLCMAMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMAMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length < RLCMessageConstants.RLC_MESSAGE_LENGTH_MA) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MA Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MA);
		}
	}
	/**
	* Constructor to construct RLCMAMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMAMessage(int size, boolean fillWithSpaces) {
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
	* setter method to set TradingOriginIndicator
	*
	* @param String - TradingOriginIndicator
	*
	*/
	public void setTradingOriginIndicator(String _TradingOriginIndicator) {
		write(70, _TradingOriginIndicator);
	}
	/**
	* getter method to get TradingOriginIndicator
	*
	* @return String - TradingOriginIndicator
	*/
	public String getTradingOriginIndicator() {
		return (readString(70, 1));
	}
	/**
	* setter method to set TradingMode
	*
	* @param String - TradingMode
	*
	*/
	public void setTradingMode(String _TradingMode) {
		write(71, _TradingMode);
	}
	/**
	* getter method to get TradingMode
	*
	* @return String - TradingMode
	*/
	public String getTradingMode() {
		return (readString(71, 1));
	}
	/**
	* setter method to set Filler_3
	*
	* @param String - Filler_3
	*
	*/
	public void setFiller_3(String _Filler_3) {
		write(72, _Filler_3);
	}
	/**
	* getter method to get Filler_3
	*
	* @return String - Filler_3
	*/
	public String getFiller_3() {
		return (readString(72, 5));
	}
	/**
	* setter method to set ChangeOfLimitFlag
	*
	* @param String - ChangeOfLimitFlag
	*
	*/
	public void setChangeOfLimitFlag(String _ChangeOfLimitFlag) {
		write(77, _ChangeOfLimitFlag);
	}
	/**
	* getter method to get ChangeOfLimitFlag
	*
	* @return String - ChangeOfLimitFlag
	*/
	public String getChangeOfLimitFlag() {
		return (readString(77, 6));
	}
	/**
	* setter method to set BuyLimitQuantity
	*
	* @param int - BuyLimitQuantity
	*
	* @param int - index
	*/
	public void setBuyLimitQuantity(int _BuyLimitQuantity, int index) {
		write(83 + (index * 72), pad(String.valueOf(_BuyLimitQuantity), '0', 12));
	}
	/**
	* setter method to set BuyLimitQuantity
	*
	* @param String - BuyLimitQuantity
	*
	* @param int - index
	*/
	public void setBuyLimitQuantity(String _BuyLimitQuantity, int index) {
		write(83 + (index * 72), _BuyLimitQuantity);
	}
	/**
	* getter method to get BuyLimitQuantityAsint
	*
	* @param int - index
	*
	* @return int - BuyLimitQuantityAsint
	*/
	public int getBuyLimitQuantityAsint(int index) {
		return (stringToint(readString(83 + (index * 72), 12)));
	}
	/**
	* getter method to get BuyLimitQuantity
	*
	* @param int - index
	*
	* @return String - BuyLimitQuantity
	*/
	public String getBuyLimitQuantity(int index) {
		return (readString(83 + (index * 72), 12));
	}
	/**
	* setter method to set NumberOfBuyOrders
	*
	* @param int - NumberOfBuyOrders
	*
	* @param int - index
	*/
	public void setNumberOfBuyOrders(int _NumberOfBuyOrders, int index) {
		write(95 + (index * 72), pad(String.valueOf(_NumberOfBuyOrders), '0', 4));
	}
	/**
	* setter method to set NumberOfBuyOrders
	*
	* @param String - NumberOfBuyOrders
	*
	* @param int - index
	*/
	public void setNumberOfBuyOrders(String _NumberOfBuyOrders, int index) {
		write(95 + (index * 72), _NumberOfBuyOrders);
	}
	/**
	* getter method to get NumberOfBuyOrdersAsint
	*
	* @param int - index
	*
	* @return int - NumberOfBuyOrdersAsint
	*/
	public int getNumberOfBuyOrdersAsint(int index) {
		return (stringToint(readString(95 + (index * 72), 4)));
	}
	/**
	* getter method to get NumberOfBuyOrders
	*
	* @param int - index
	*
	* @return String - NumberOfBuyOrders
	*/
	public String getNumberOfBuyOrders(int index) {
		return (readString(95 + (index * 72), 4));
	}
	/**
	* setter method to set BuyLimitPrice
	*
	* @param BigDecimal - BuyLimitPrice
	*
	* @param int - index
	*/
	public void setBuyLimitPrice(BigDecimal _BuyLimitPrice, int index) {
		write(99 + (index * 72), priceToString(_BuyLimitPrice));
	}
	/**
	* setter method to set BuyLimitPrice
	*
	* @param String - BuyLimitPrice
	*
	* @param int - index
	*/
	public void setBuyLimitPrice(String _BuyLimitPrice, int index) {
		write(99 + (index * 72), _BuyLimitPrice);
	}
	/**
	* getter method to get BuyLimitPriceAsPrice
	*
	* @param int - index
	*
	* @return BigDecimal - BuyLimitPriceAsPrice
	*/
	public BigDecimal getBuyLimitPriceAsPrice(int index) {
		return (stringToPrice(readString(99 + (index * 72), 19)));
	}
	/**
	* getter method to get BuyLimitPrice
	*
	* @param int - index
	*
	* @return String - BuyLimitPrice
	*/
	public String getBuyLimitPrice(int index) {
		return (readString(99 + (index * 72), 19));
	}
	/**
	* setter method to set SellLimitPrice
	*
	* @param BigDecimal - SellLimitPrice
	*
	* @param int - index
	*/
	public void setSellLimitPrice(BigDecimal _SellLimitPrice, int index) {
		write(118 + (index * 72), priceToString(_SellLimitPrice));
	}
	/**
	* setter method to set SellLimitPrice
	*
	* @param String - SellLimitPrice
	*
	* @param int - index
	*/
	public void setSellLimitPrice(String _SellLimitPrice, int index) {
		write(118 + (index * 72), _SellLimitPrice);
	}
	/**
	* getter method to get SellLimitPriceAsPrice
	*
	* @param int - index
	*
	* @return BigDecimal - SellLimitPriceAsPrice
	*/
	public BigDecimal getSellLimitPriceAsPrice(int index) {
		return (stringToPrice(readString(118 + (index * 72), 19)));
	}
	/**
	* getter method to get SellLimitPrice
	*
	* @param int - index
	*
	* @return String - SellLimitPrice
	*/
	public String getSellLimitPrice(int index) {
		return (readString(118 + (index * 72), 19));
	}
	/**
	* setter method to set NumberOfSellOrders
	*
	* @param int - NumberOfSellOrders
	*
	* @param int - index
	*/
	public void setNumberOfSellOrders(int _NumberOfSellOrders, int index) {
		write(137 + (index * 72), pad(String.valueOf(_NumberOfSellOrders), '0', 4));
	}
	/**
	* setter method to set NumberOfSellOrders
	*
	* @param String - NumberOfSellOrders
	*
	* @param int - index
	*/
	public void setNumberOfSellOrders(String _NumberOfSellOrders, int index) {
		write(137 + (index * 72), _NumberOfSellOrders);
	}
	/**
	* getter method to get NumberOfSellOrdersAsint
	*
	* @param int - index
	*
	* @return int - NumberOfSellOrdersAsint
	*/
	public int getNumberOfSellOrdersAsint(int index) {
		return (stringToint(readString(137 + (index * 72), 4)));
	}
	/**
	* getter method to get NumberOfSellOrders
	*
	* @param int - index
	*
	* @return String - NumberOfSellOrders
	*/
	public String getNumberOfSellOrders(int index) {
		return (readString(137 + (index * 72), 4));
	}
	/**
	* setter method to set SellLimitQuantity
	*
	* @param int - SellLimitQuantity
	*
	* @param int - index
	*/
	public void setSellLimitQuantity(int _SellLimitQuantity, int index) {
		write(141 + (index * 72), pad(String.valueOf(_SellLimitQuantity), '0', 12));
	}
	/**
	* setter method to set SellLimitQuantity
	*
	* @param String - SellLimitQuantity
	*
	* @param int - index
	*/
	public void setSellLimitQuantity(String _SellLimitQuantity, int index) {
		write(141 + (index * 72), _SellLimitQuantity);
	}
	/**
	* getter method to get SellLimitQuantityAsint
	*
	* @param int - index
	*
	* @return int - SellLimitQuantityAsint
	*/
	public int getSellLimitQuantityAsint(int index) {
		return (stringToint(readString(141 + (index * 72), 12)));
	}
	/**
	* getter method to get SellLimitQuantity
	*
	* @param int - index
	*
	* @return String - SellLimitQuantity
	*/
	public String getSellLimitQuantity(int index) {
		return (readString(141 + (index * 72), 12));
	}
	/**
	* setter method to set BookLevelSeparator
	*
	* @param String - BookLevelSeparator
	*
	* @param int - index
	*/
	public void setBookLevelSeparator(String _BookLevelSeparator, int index) {
		write(153 + (index * 72), _BookLevelSeparator);
	}
	/**
	* getter method to get BookLevelSeparator
	*
	* @param int - index
	*
	* @return String - BookLevelSeparator
	*/
	public String getBookLevelSeparator(int index) {
		return (readString(153 + (index * 72), 2));
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
		sw.write("TradingOriginIndicator=>" + getTradingOriginIndicator() + "<\n");
		sw.write("TradingMode=>" + getTradingMode() + "<\n");
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write("ChangeOfLimitFlag=>" + getChangeOfLimitFlag() + "<\n");
		String str = getChangeOfLimitFlag();
		int index = 0;
		for (int i = 0; i < 5; i++) {
			if (str.charAt(i) == '1') {
				sw.write("Change at Level + " + (i + 1));

				sw.write("BuyLimitQuantity=>" + getBuyLimitQuantityAsint(index) + "<\n");
				sw.write("BuyLimitQuantity=>" + getBuyLimitQuantity(index) + "<\n");
				sw.write("NumberOfBuyOrders=>" + getNumberOfBuyOrdersAsint(index) + "<\n");
				sw.write("NumberOfBuyOrders=>" + getNumberOfBuyOrders(index) + "<\n");
				sw.write("BuyLimitPrice=>" + getBuyLimitPriceAsPrice(index) + "<\n");
				sw.write("BuyLimitPrice=>" + getBuyLimitPrice(index) + "<\n");
				sw.write("SellLimitPrice=>" + getSellLimitPriceAsPrice(index) + "<\n");
				sw.write("SellLimitPrice=>" + getSellLimitPrice(index) + "<\n");
				sw.write("NumberOfSellOrders=>" + getNumberOfSellOrdersAsint(index) + "<\n");
				sw.write("NumberOfSellOrders=>" + getNumberOfSellOrders(index) + "<\n");
				sw.write("SellLimitQuantity=>" + getSellLimitQuantityAsint(index) + "<\n");
				//sw.write("SellLimitQuantity=>"+ getSellLimitQuantity(index) +"<\n");
				//sw.write("BookLevelSeparator=>"+ getBookLevelSeparator(index) +"<\n");						 				 		
				index++;

			}

		}

		return (super.toString() + sw.toString());
	}
}
