package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMHMessage
*
*/
public class RLCMHMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMHMessage object 
	*
	*/
	public RLCMHMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MH, true);
	}
	/**
	* Constructor to construct RLCMHMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMHMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_MH) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MH Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MH);
		}
	}
	/**
	* Constructor to construct RLCMHMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMHMessage(int size, boolean fillWithSpaces) {
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
	* setter method to set InstrumentGroupIdentification
	*
	* @param String - InstrumentGroupIdentification
	*
	*/
	public void setInstrumentGroupIdentification(String _InstrumentGroupIdentification) {
		write(50, _InstrumentGroupIdentification);
	}
	/**
	* getter method to get InstrumentGroupIdentification
	*
	* @return String - InstrumentGroupIdentification
	*/
	public String getInstrumentGroupIdentification() {
		return (readString(50, 2));
	}
	/**
	* setter method to set TradingTransition
	*
	* @param String - TradingTransition
	*
	*/
	public void setTradingTransition(String _TradingTransition) {
		write(52, _TradingTransition);
	}
	/**
	* getter method to get TradingTransition
	*
	* @return String - TradingTransition
	*/
	public String getTradingTransition() {
		// test showed that it is 639
		return (readString(52, 639));
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
		sw.write(
			"InstrumentGroupIdentification=>" + getInstrumentGroupIdentification() + "<\n");
		sw.write("TradingTransition=>" + getTradingTransition() + "<\n");
		return (super.toString() + sw.toString());
	}
}
