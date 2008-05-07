package com.exsys.mktdata.message;

import com.exsys.common.util.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.math.*;

/**
* This class is used to represent the RLC message RLCMTickSizeCalculator
*
*/
public class RLCTickSizeCalculator {
	private double tickSize = 0;

	/**
	* Constructor for RLCTickSizeCalculator
	* 
	* @param f1_NumMinElemTickIncr
	* @param f2_TickDisplayFormatType
	* @param f3_NumDecimals
	* @param f4_VTTIndexCode
	* @param currentPrice
	*/
	public RLCTickSizeCalculator(
		String f1_NumMinElemTickIncr,
		String f2_TickDisplayFormatType,
		String f3_NumDecimals,
		String f4_VTTIndexCode,
		double currentPrice) {
		tickSize =
			RLCMessage.calculateTickSize(
				f1_NumMinElemTickIncr,
				f2_TickDisplayFormatType,
				f3_NumDecimals,
				f4_VTTIndexCode,
				currentPrice);
	}

	/**
	* getter method to get TickSize
	*
	* @return double - TickSize
	*/
	public double getTickSize() {
		return tickSize;
	}

}
