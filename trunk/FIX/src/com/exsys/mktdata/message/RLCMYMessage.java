package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMYMessage
*
*/
public class RLCMYMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMYMessage object 
	*
	*/
	public RLCMYMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MY, true);
	}
	/**
	* Constructor to construct RLCMYMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMYMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length < RLCMessageConstants.RLC_MESSAGE_LENGTH_MY) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MY Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MY);
		}
	}
	/**
	* Constructor to construct RLCMYMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMYMessage(int size, boolean fillWithSpaces) {
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
	* setter method to set MatchingType
	*
	* @param String - MatchingType
	*
	*/
	public void setMatchingType(String _MatchingType) {
		write(70, _MatchingType);
	}
	/**
	* getter method to get MatchingType
	*
	* @return String - MatchingType
	*/
	public String getMatchingType() {
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
		return (readString(77, 20));
	}
	/**
	* setter method to set BuyLimitQuantity
	*
	* @param int - BuyLimitQuantity
	*
	* @param int - index
	*/
	public void setBuyLimitQuantity(int _BuyLimitQuantity, int index) {
		write(97 + (index * 64), pad(String.valueOf(_BuyLimitQuantity), '0', 12));
	}
	/**
	* setter method to set BuyLimitQuantity
	*
	* @param String - BuyLimitQuantity
	*
	* @param int - index
	*/
	public void setBuyLimitQuantity(String _BuyLimitQuantity, int index) {
		write(97 + (index * 64), _BuyLimitQuantity);
	}
	/**
	* getter method to get BuyLimitQuantityAsint
	*
	* @param int - index
	*
	* @return int - BuyLimitQuantityAsint
	*/
	public int getBuyLimitQuantityAsint(int index) {
		return (stringToint(readString(97 + (index * 62), 12)));
	}
	/**
	* getter method to get BuyLimitQuantity
	*
	* @param int - index
	*
	* @return String - BuyLimitQuantity
	*/
	public String getBuyLimitQuantity(int index) {
		return (readString(97 + (index * 62), 12));
	}
	
	/**
	* setter method to set DecimalLocatorForBuy
	*
	* @param String - DecimalLocatorForBuy
	*
	* @param int - index
	*/
	/*
	public void setDecimalLocatorForBuy(String _DecimalLocatorForBuy, int index){
		write( 110+(index*64),  _DecimalLocatorForBuy);
	}
	*/
	/**
	* getter method to get DecimalLocatorForBuy
	*
	* @param int - index
	*
	* @return String - DecimalLocatorForBuy
	*/
	/*
	public String  getDecimalLocatorForBuy(int index){
		 return (readString( 110+(index*64),1 )) ;
	}
	*/
	/**
	* setter method to set BuyLimitPrice
	*
	* @param BigDecimal - BuyLimitPrice
	*
	* @param int - index
	*/
	public void setBuyLimitPrice(BigDecimal _BuyLimitPrice, int index) {
		write(109 + (index * 64), priceToString(_BuyLimitPrice));
	}
	/**
	* setter method to set BuyLimitPrice
	*
	* @param String - BuyLimitPrice
	*
	* @param int - index
	*/
	public void setBuyLimitPrice(String _BuyLimitPrice, int index) {
		write(109 + (index * 64), _BuyLimitPrice);
	}
	/**
	* getter method to get BuyLimitPriceAsPrice
	*
	* @param int - index
	*
	* @return BigDecimal - BuyLimitPriceAsPrice
	*/
	public BigDecimal getBuyLimitPriceAsPrice(int index) {
		return (stringToPrice(readString(109 + (index * 62), 19)));
	}
	/**
	* getter method to get BuyLimitPrice
	*
	* @param int - index
	*
	* @return String - BuyLimitPrice
	*/
	public String getBuyLimitPrice(int index) {
		return (readString(109 + (index * 62), 19));
	}
	
	/**
	* setter method to set DecimalLocatorForSell
	*
	* @param String - DecimalLocatorForSell
	*
	* @param int - index
	*/
	/*
	public void setDecimalLocatorForSell(String _DecimalLocatorForSell, int index){
		write( 130+(index*64),  _DecimalLocatorForSell);
	}
	*/
	/**
	* getter method to get DecimalLocatorForSell
	*
	* @param int - index
	*
	* @return String - DecimalLocatorForSell
	*/
	/*
	public String  getDecimalLocatorForSell(int index){
		 return (readString( 130+(index*64),1 )) ;
	}
	*/
	/**
	* setter method to set SellLimitPrice
	*
	* @param BigDecimal - SellLimitPrice
	*
	* @param int - index
	*/
	public void setSellLimitPrice(BigDecimal _SellLimitPrice, int index) {
		write(128 + (index * 62), priceToString(_SellLimitPrice));
	}
	/**
	* setter method to set SellLimitPrice
	*
	* @param String - SellLimitPrice
	*
	* @param int - index
	*/
	public void setSellLimitPrice(String _SellLimitPrice, int index) {
		write(128 + (index * 62), _SellLimitPrice);
	}
	/**
	* getter method to get SellLimitPriceAsPrice
	*
	* @param int - index
	*
	* @return BigDecimal - SellLimitPriceAsPrice
	*/
	public BigDecimal getSellLimitPriceAsPrice(int index) {
		return (stringToPrice(readString(128 + (index * 62), 19)));
	}
	/**
	* getter method to get SellLimitPrice
	*
	* @param int - index
	*
	* @return String - SellLimitPrice
	*/
	public String getSellLimitPrice(int index) {
		return (readString(128 + (index * 62), 19));
	}
	/**
	* setter method to set SellLimitQuantity
	*
	* @param int - SellLimitQuantity
	*
	* @param int - index
	*/
	public void setSellLimitQuantity(int _SellLimitQuantity, int index) {
		write(147 + (index * 62), pad(String.valueOf(_SellLimitQuantity), '0', 12));
	}
	/**
	* setter method to set SellLimitQuantity
	*
	* @param String - SellLimitQuantity
	*
	* @param int - index
	*/
	public void setSellLimitQuantity(String _SellLimitQuantity, int index) {
		write(147 + (index * 62), _SellLimitQuantity);
	}
	/**
	* getter method to get SellLimitQuantityAsint
	*
	* @param int - index
	*
	* @return int - SellLimitQuantityAsint
	*/
	public int getSellLimitQuantityAsint(int index) {
		return (stringToint(readString(147 + (index * 62), 12)));
	}
	/**
	* getter method to get SellLimitQuantity
	*
	* @param int - index
	*
	* @return String - SellLimitQuantity
	*/
	public String getSellLimitQuantity(int index) {
		return (readString(147 + (index * 62), 12));
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
		sw.write("MatchingType=>" + getMatchingType() + "<\n");
		sw.write("TradingMode=>" + getTradingMode() + "<\n");
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write("ChangeOfLimitFlag=>" + getChangeOfLimitFlag() + "<\n");
		String str = getChangeOfLimitFlag();

		int index = 0;
		for (int i = 0; i < 5; i++) {
			if (str.charAt(i) == '1') {
				sw.write("Implied Change at Level + " + (i + 1));

				sw.write("BuyLimitQuantity=>" + getBuyLimitQuantityAsint(index) + "<\n");
				sw.write("BuyLimitQuantity=>" + getBuyLimitQuantity(index) + "<\n");
				//sw.write("DecimalLocatorForBuy=>"+ getDecimalLocatorForBuy(index) +"<\n");
				sw.write("BuyLimitPrice=>" + getBuyLimitPriceAsPrice(index) + "<\n");
				sw.write("BuyLimitPrice=>" + getBuyLimitPrice(index) + "<\n");
				//sw.write("DecimalLocatorForSell=>"+ getDecimalLocatorForSell(index) +"<\n");
				sw.write("SellLimitPrice=>" + getSellLimitPriceAsPrice(index) + "<\n");
				sw.write("SellLimitPrice=>" + getSellLimitPrice(index) + "<\n");
				sw.write("SellLimitQuantity=>" + getSellLimitQuantityAsint(index) + "<\n");
				sw.write("SellLimitQuantity=>" + getSellLimitQuantity(index) + "<\n");
				index++;

			}

		}

		return (super.toString() + sw.toString());
	}
}
