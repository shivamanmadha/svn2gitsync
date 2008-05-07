package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMIMessage
*
*/
public class RLCMIMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMIMessage object 
	*
	*/
	public RLCMIMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MI, true);
	}
	/**
	* Constructor to construct RLCMIMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMIMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_MI) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MI Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MI);
		}
	}
	/**
	* Constructor to construct RLCMIMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMIMessage(int size, boolean fillWithSpaces) {
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
	* setter method to set UpperPriceThreshold
	*
	* @param BigDecimal - UpperPriceThreshold
	*
	*/
	public void setUpperPriceThreshold(BigDecimal _UpperPriceThreshold) {
		write(70, priceToString(_UpperPriceThreshold));
	}
	/**
	* setter method to set UpperPriceThreshold
	*
	* @param String - UpperPriceThreshold
	*
	*/
	public void setUpperPriceThreshold(String _UpperPriceThreshold) {
		write(70, _UpperPriceThreshold);
	}
	/**
	* getter method to get UpperPriceThresholdAsPrice
	*
	* @return BigDecimal - UpperPriceThresholdAsPrice
	*/
	public BigDecimal getUpperPriceThresholdAsPrice() {
		return (stringToPrice(readString(70, 19)));
	}
	/**
	* getter method to get UpperPriceThreshold
	*
	* @return String - UpperPriceThreshold
	*/
	public String getUpperPriceThreshold() {
		return (readString(70, 19));
	}
	/**
	* setter method to set LowerPriceThreshold
	*
	* @param BigDecimal - LowerPriceThreshold
	*
	*/
	public void setLowerPriceThreshold(BigDecimal _LowerPriceThreshold) {
		write(89, priceToString(_LowerPriceThreshold));
	}
	/**
	* setter method to set LowerPriceThreshold
	*
	* @param String - LowerPriceThreshold
	*
	*/
	public void setLowerPriceThreshold(String _LowerPriceThreshold) {
		write(89, _LowerPriceThreshold);
	}
	/**
	* getter method to get LowerPriceThresholdAsPrice
	*
	* @return BigDecimal - LowerPriceThresholdAsPrice
	*/
	public BigDecimal getLowerPriceThresholdAsPrice() {
		return (stringToPrice(readString(89, 19)));
	}
	/**
	* getter method to get LowerPriceThreshold
	*
	* @return String - LowerPriceThreshold
	*/
	public String getLowerPriceThreshold() {
		return (readString(89, 19));
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
		sw.write("UpperPriceThreshold=>" + getUpperPriceThresholdAsPrice() + "<\n");
		sw.write("UpperPriceThreshold=>" + getUpperPriceThreshold() + "<\n");
		sw.write("LowerPriceThreshold=>" + getLowerPriceThresholdAsPrice() + "<\n");
		sw.write("LowerPriceThreshold=>" + getLowerPriceThreshold() + "<\n");
		return (super.toString() + sw.toString());
	}
}
