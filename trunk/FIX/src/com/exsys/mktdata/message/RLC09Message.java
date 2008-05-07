package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLC09Message
*
*/
public class RLC09Message extends RLCMessage {
	/**
	* Constructor to construct RLC09Message object 
	*
	*/
	public RLC09Message() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_09, true);
	}
	/**
	* Constructor to construct RLC09Message object 
	*
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLC09Message(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_09) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for 09 Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_09);
		}
	}
	/**
	* Constructor to construct RLC09Message object 
	*
	* @param size
	* @param fillWithSpaces
	*/
	public RLC09Message(int size, boolean fillWithSpaces) {
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
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("Filler_2=>" + getFiller_2() + "<\n");
		return (super.toString() + sw.toString());
	}
}
