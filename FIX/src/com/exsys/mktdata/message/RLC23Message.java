package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLC23Message
*
*/
public class RLC23Message extends RLCMessage {
	/**
	* Constructor to construct RLC23Message object 
	*
	*/
	public RLC23Message() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_23, true);
	}
	/**
	* Constructor to construct RLC23Message object 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLC23Message(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_23) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for 23 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_23);
		}
	}
	/**
	* Constructor to construct RLC23Message object
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLC23Message(int size, boolean fillWithSpaces) {
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
		return (readString(36, 19));
	}
	/**
	* setter method to set SPIMessage
	*
	* @param String - SPIMessage
	*/
	public void setSPIMessage(String _SPIMessage) {
		write(55, _SPIMessage);
	}
	/**
	* getter method to get SPIMessage
	*
	* @return String - SPIMessage
	*/
	public String getSPIMessage() {
		return (readString(55, 200));
	}

	/**
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("Filler_2=>" + getFiller_2() + "<\n");
		sw.write("SPIMessage=>" + getSPIMessage() + "<\n");
		return (super.toString() + sw.toString());
	}
}
