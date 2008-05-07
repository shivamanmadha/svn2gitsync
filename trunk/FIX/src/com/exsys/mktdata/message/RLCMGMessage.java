package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMGMessage
*
*/
public class RLCMGMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMGMessage object 
	*
	*/
	public RLCMGMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_MG, true);
	}
	/**
	* Constructor to construct RLCMGMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMGMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_MG) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for MG Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_MG);
		}
	}
	/**
	* Constructor to construct RLCMGMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMGMessage(int size, boolean fillWithSpaces) {
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
	* setter method to set TradingStatusFlag
	*
	* @param String - TradingStatusFlag
	*
	*/
	public void setTradingStatusFlag(String _TradingStatusFlag) {
		write(70, _TradingStatusFlag);
	}
	/**
	* getter method to get TradingStatusFlag
	*
	* @return String - TradingStatusFlag
	*/
	public String getTradingStatusFlag() {
		return (readString(70, 1));
	}
	/**
	* setter method to set InstrumentReservationOrderFlag
	*
	* @param String - InstrumentReservationOrderFlag
	*
	*/
	public void setInstrumentReservationOrderFlag(String _InstrumentReservationOrderFlag) {
		write(71, _InstrumentReservationOrderFlag);
	}
	/**
	* getter method to get InstrumentReservationOrderFlag
	*
	* @return String - InstrumentReservationOrderFlag
	*/
	public String getInstrumentReservationOrderFlag() {
		return (readString(71, 1));
	}
	/**
	* setter method to set InstrumentSuspensionDate
	*
	* @param Date - InstrumentSuspensionDate
	*
	*/
	public void setInstrumentSuspensionDate(Date _InstrumentSuspensionDate) {
		write(72, getYYYYMMDDString(_InstrumentSuspensionDate));
	}
	/**
	* setter method to set InstrumentSuspensionDate
	*
	* @param String - InstrumentSuspensionDate
	*
	*/
	public void setInstrumentSuspensionDate(String _InstrumentSuspensionDate) {
		write(72, _InstrumentSuspensionDate);
	}
	/**
	* getter method to get InstrumentSuspensionDateAsDate
	*
	* @return Date - InstrumentSuspensionDateAsDate
	*/
	public Date getInstrumentSuspensionDateAsDate() {
		return (YYYYMMDDStringToDate(readString(72, 8)));
	}
	/**
	* getter method to get InstrumentSuspensionDate
	*
	* @return String - InstrumentSuspensionDate
	*/
	public String getInstrumentSuspensionDate() {
		return (readString(72, 8));
	}
	/**
	* setter method to set InstrumentSuspensionTime
	*
	* @param String - InstrumentSuspensionTime
	*
	*/
	public void setInstrumentSuspensionTime(String _InstrumentSuspensionTime) {
		write(80, _InstrumentSuspensionTime);
	}
	/**
	* getter method to get InstrumentSuspensionTime
	*
	* @return String - InstrumentSuspensionTime
	*/
	public String getInstrumentSuspensionTime() {
		return (readString(80, 6));
	}
	/**
	* setter method to set InstrumentState
	*
	* @param String - InstrumentState
	*
	*/
	public void setInstrumentState(String _InstrumentState) {
		write(86, _InstrumentState);
	}
	/**
	* getter method to get InstrumentState
	*
	* @return String - InstrumentState
	*/
	public String getInstrumentState() {
		return (readString(86, 2));
	}
	/**
	* setter method to set Filler_3
	*
	* @param String - Filler_3
	*
	*/
	public void setFiller_3(String _Filler_3) {
		write(88, _Filler_3);
	}
	/**
	* getter method to get Filler_3
	*
	* @return String - Filler_3
	*/
	public String getFiller_3() {
		return (readString(88, 1));
	}
	/**
	* setter method to set ProgrammingInstrumentOpeningTime
	*
	* @param String - ProgrammingInstrumentOpeningTime
	*
	*/
	public void setProgrammingInstrumentOpeningTime(String _ProgrammingInstrumentOpeningTime) {
		write(89, _ProgrammingInstrumentOpeningTime);
	}
	/**
	* getter method to get ProgrammingInstrumentOpeningTime
	*
	* @return String - ProgrammingInstrumentOpeningTime
	*/
	public String getProgrammingInstrumentOpeningTime() {
		return (readString(89, 6));
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
		sw.write("TradingStatusFlag=>" + getTradingStatusFlag() + "<\n");
		sw.write(
			"InstrumentReservationOrderFlag=>"
				+ getInstrumentReservationOrderFlag()
				+ "<\n");
		sw.write(
			"InstrumentSuspensionDate=>" + getInstrumentSuspensionDateAsDate() + "<\n");
		sw.write("InstrumentSuspensionDate=>" + getInstrumentSuspensionDate() + "<\n");
		sw.write("InstrumentSuspensionTime=>" + getInstrumentSuspensionTime() + "<\n");
		sw.write("InstrumentState=>" + getInstrumentState() + "<\n");
		sw.write("Filler_3=>" + getFiller_3() + "<\n");
		sw.write(
			"ProgrammingInstrumentOpeningTime=>"
				+ getProgrammingInstrumentOpeningTime()
				+ "<\n");
		return (super.toString() + sw.toString());
	}
}
