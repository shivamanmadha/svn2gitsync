package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMKMessage
*
*/
public class RLCMKMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMKMessage object 
	*
	*/
	public RLCMKMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MK, true);
	}
	/**
	* Constructor to construct RLCMKMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMKMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_MK) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MK Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MK);
		}
	}
	/**
	* Constructor to construct RLCMKMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMKMessage(int size, boolean fillWithSpaces) {
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
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("Filler_2=>" + getFiller_2() + "<\n");
		sw.write("TradingDate=>" + getTradingDateAsDate() + "<\n");
		sw.write("TradingDate=>" + getTradingDate() + "<\n");
		return (super.toString() + sw.toString());
	}
}
