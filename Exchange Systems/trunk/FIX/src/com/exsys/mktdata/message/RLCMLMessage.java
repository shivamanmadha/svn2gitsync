package com.exsys.mktdata.message;

import java.io.*;
import java.util.*;
import java.math.*;
import com.exsys.common.util.*;
import com.exsys.common.exceptions.*;

/**
* This class is used to represent the RLC message RLCMLMessage
*
*/
public class RLCMLMessage extends RLCMessage {
	/**
	* Constructor to construct RLCMLMessage object 
	*
	*/
	public RLCMLMessage() {
		super(RLCMessageConstants.RLC_MESSAGE_LENGTH_ML, true);
	}
	/**
	* Constructor to construct RLCMLMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMLMessage(byte[] newBytes) throws RLCProtocolError {
		super(newBytes);
		if (newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_ML) {
			throw new RLCProtocolError(
				"Invalid Message Length",
				"MessageLength for ML Message = " + RLCMessageConstants.RLC_MESSAGE_LENGTH_ML);
		}
	}
	/**
	* Constructor to construct RLCMLMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMLMessage(int size, boolean fillWithSpaces) {
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
