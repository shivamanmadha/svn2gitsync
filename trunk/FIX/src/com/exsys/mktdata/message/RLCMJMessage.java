package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMJMessage
*
*/
public class RLCMJMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMJMessage object 
	*
	*/
	public RLCMJMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MJ, true);
	}
	/**
	* Constructor to construct RLCMJMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMJMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_MJ) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MJ Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MJ);
		}
	}
	/**
	* Constructor to construct RLCMJMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMJMessage(int size, boolean fillWithSpaces) {
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
	* setter method to set InstrumentGroupStatus
	*
	* @param String - InstrumentGroupStatus
	*
	*/
	public void setInstrumentGroupStatus(String _InstrumentGroupStatus) {
		write(52, _InstrumentGroupStatus);
	}
	/**
	* getter method to get InstrumentGroupStatus
	*
	* @return String - InstrumentGroupStatus
	*/
	public String getInstrumentGroupStatus() {
		return (readString(52, 1));
	}
	/**
	* setter method to set NumberOfLinks
	*
	* @param String - NumberOfLinks
	*
	*/
	public void setNumberOfLinks(String _NumberOfLinks) {
		write(53, _NumberOfLinks);
	}
	/**
	* getter method to get NumberOfLinks
	*
	* @return String - NumberOfLinks
	*/
	public String getNumberOfLinks() {
		return (readString(53, 2));
	}
	/**
	* setter method to set ClosingIndicator
	*
	* @param String - ClosingIndicator
	*
	*/
	public void setClosingIndicator(String _ClosingIndicator) {
		write(55, _ClosingIndicator);
	}
	/**
	* getter method to get ClosingIndicator
	*
	* @return String - ClosingIndicator
	*/
	public String getClosingIndicator() {
		return (readString(55, 1));
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
		sw.write("InstrumentGroupStatus=>" + getInstrumentGroupStatus() + "<\n");
		sw.write("NumberOfLinks=>" + getNumberOfLinks() + "<\n");
		sw.write("ClosingIndicator=>" + getClosingIndicator() + "<\n");
		return (super.toString() + sw.toString());
	}
}
