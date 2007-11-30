package com.exsys.mktdata.message;

import com.exsys.common.util.*;
import java.util.*;
import java.text.*;
import java.io.*;
import java.math.*;

/**
* This class is used to represent the RLC message RLCContract
*
*/
public class RLCContract {
	private byte[] contractBytes = null;
	private String contractYear = null;
	private String contractMonth = null;
	private String commodityCode = null;

	/**
	* Constructor to construct RLCContract object 
	* 
	* @param newBytes
	*/
	public RLCContract(byte[] newBytes) {
		//System.out.println("ContractBytes is -"+ new String(newBytes));
		boolean lastCharDigit = Character.isDigit((char) newBytes[newBytes.length - 1]);
		contractBytes = newBytes;
		int length = contractBytes.length;
		if (lastCharDigit) {
			//System.out.println("Last byte is a digit");
			//System.out.println("Length="+length);
			for (int i = length - 2; i != -1; i--) {
				//System.out.println("char["+(i)+"]="+(char)newBytes[i]);
				if (Character.isDigit((char) newBytes[i]))
					continue;
				else {
					//System.out.println("Non Numeric");
					//System.out.println("i="+i);
					contractYear = new String(newBytes, i + 1, length - (i + 1));
					//System.out.println("contractYear="+contractYear);
					contractMonth = String.valueOf((char) newBytes[i]);
					//System.out.println("contractMonth="+contractMonth);
					commodityCode = new String(newBytes, 0, i);
					//System.out.println("commodityCode="+commodityCode);
					break;
				}
			}
		} else {
			commodityCode = new String(contractBytes);
		}

	}

	/**
	* getter method to get ContractYear
	*
	* @return String - ContractYear
	*/
	public String getContractYear() {
		return contractYear;
	}
	/**
	* getter method to get ContractMonth
	*
	* @return String - ContractMonth
	*/
	public String getContractMonth() {
		return contractMonth;
	}
	/**
	* getter method to get CommodityCode
	*
	* @return String - CommodityCode
	*/
	public String getCommodityCode() {
		return commodityCode;
	}

	/**
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("\n\n*****Contract*****\n");
		sw.write("Contract=>" + new String(contractBytes) + "<\n");
		sw.write("ContractYear=>" + contractYear + "<\n");
		sw.write("ContractMonth=>" + contractMonth + "<\n");
		sw.write("CommodityCode=>" + commodityCode + "<\n");
		return sw.toString();
	}

}
