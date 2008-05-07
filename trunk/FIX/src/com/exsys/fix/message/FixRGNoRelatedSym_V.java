package com.exsys.fix.message;

import java.util.*;

/**
* This class is used to represent the FIX message RGNoRelatedSym_V
*
*/
public class FixRGNoRelatedSym_V extends FixRepeatedGroup {
	public static ArrayList fieldList = new ArrayList();
	static {
		fieldList.add("55");

	};

	public String FIRST_FIELD = "55";

	/**
	* getter method to get FirstField
	*
	* @return String - FirstField
	*/
	public String getFirstField() {
		return "55";
	}
	/**
	* getter method to get RepeatedGroupTag
	*
	* @return String - RepeatedGroupTag
	*/
	public String getRepeatedGroupTag() {
		return "146";
	}

	/**
	* method  isFirstField
	*
	* @param String - tagNum
	*
	* @return boolean - isFirstField
	*/
	public boolean isFirstField(String tagNum) {
		return tagNum.equals(FIRST_FIELD);

	}
	/**
	* method  isMemberField
	*
	* @param String - tagNum
	*
	* @return boolean - isMemberField
	*/
	public boolean isMemberField(String tagNum) {
		return fieldList.contains(tagNum);
	}

	/**
	* Constructor to construct FixRGNoRelatedSym_V object 
	*
	*/
	public FixRGNoRelatedSym_V() {
	}
	/**
	* setter method to set Symbol
	*
	* @param String - Symbol
	*/
	public void setSymbol(String _Symbol) {
		addBodyField(55, _Symbol);
	}
	/**
	* getter method to get Symbol
	*
	* @return String - Symbol
	*/
	public String getSymbol() {
		return (getBodyFieldValue(55));
	}

}
