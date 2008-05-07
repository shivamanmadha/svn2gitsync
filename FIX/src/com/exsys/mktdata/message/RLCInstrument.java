package com.exsys.mktdata.message;

import com.exsys.common.util.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.math.*;

/**
* This class is used to represent the RLC message RLCInstrument
*
*/
public class RLCInstrument {
	private RLCContract contract = null;
	private byte[] instrBytes = null;
	private boolean isOption = false;
	private boolean isSpread = false;
	private boolean isStrategy = false;
	private String strategyType = null;
	private String strikePrice = null;
	public static char strategy_ind = ':';
	public static char spread_ind = '-';
	public static char blank_ind = ' ';

	/**
	* Constructor to construct RLCInstrument object
	*  
	* @param newBytes
	*/
	public RLCInstrument(byte[] newBytes) {
		instrBytes = newBytes;
		String instrument = new String(newBytes).trim();
		//System.out.println("Instrument is -"+ instrument);
		// first see if there is :
		int strategyIndex = instrument.indexOf(strategy_ind);
		if (strategyIndex != -1) {
			//System.out.println("StrategyIndex -"+ strategyIndex);
			isStrategy = true;
			strategyType = instrument.substring(strategyIndex + 1, strategyIndex + 3);
			//System.out.println("strategyType -"+ strategyType);
			// we do not support strategies yet
			//contract = new RLCContract(instrument.substring(0,strategyIndex));
		} else {
			int spreadIndex = instrument.indexOf(spread_ind);
			int blankIndex = instrument.indexOf(blank_ind);
			if (spreadIndex != -1) {
				//System.out.println("spreadIndex -"+ spreadIndex);
				isSpread = true;
				// we do not support spreads yet
			} else {
				if (blankIndex != -1) {
					//System.out.println("blankIndex -"+ blankIndex);
					// if there is a blank, see if the next char is P or C
					char nextChar = instrument.charAt(blankIndex + 1);
					if (nextChar == 'C' || nextChar == 'P') {
						isOption = true;
						strikePrice = instrument.substring(blankIndex + 2);
						//System.out.println("StrikePrice -"+ strikePrice);
					}
				}
				contract =
					new RLCContract(
						(isOption ? instrument.substring(0, blankIndex) : instrument).getBytes());

			}
		}

	}

	/**
	* method  isOption
	*
	* @return boolean - isOption
	*/
	public boolean isOption() {
		return isOption;
	}
	/**
	* method  isSpread
	*
	* @return boolean - isSpread
	*/
	public boolean isSpread() {
		return isSpread;
	}
	/**
	* method  isStrategy
	*
	* @return boolean - isStrategy
	*/
	public boolean isStrategy() {
		return isStrategy;
	}
	/**
	* getter method to get StrategyType
	*
	* @return String - StrategyType
	*/
	public String getStrategyType() {
		return strategyType;
	}
	/**
	* getter method to get Contract
	*
	* @return RLCContract - Contract
	*/
	public RLCContract getContract() {
		return contract;
	}

	/**
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("\n\n*****Instrument*****\n");
		sw.write("Instrument=>" + new String(instrBytes) + "<\n");
		sw.write("isStrategy=>" + isStrategy + "<\n");
		sw.write("StrategyType=>" + strategyType + "<\n");
		sw.write("isOption=>" + isOption + "<\n");
		sw.write("strikePrice=>" + strikePrice + "<\n");
		sw.write("isSpread=>" + isSpread + "<\n");
		sw.write(
			"contract=" + (contract == null ? "NULL" : contract.toString()) + "\n");
		return sw.toString();
	}

}
