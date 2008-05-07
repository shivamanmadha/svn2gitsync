package com.exsys.fix.message;

import java.util.*;

/**
* This class is used to represent the FIX message RGNoMDEntryTypes_V
*
*/
public class FixRGNoMDEntryTypes_V extends FixRepeatedGroup {
	public static ArrayList fieldList = new ArrayList();
	static {
		fieldList.add("269");

	};

	public String FIRST_FIELD = "269";

	/**
	* getter method to get FirstField
	*
	* @return String - FirstField
	*/
	public String getFirstField() {
		return "269";
	}
	/**
	* getter method to get RepeatedGroupTag
	*
	* @return String - RepeatedGroupTag
	*/
	public String getRepeatedGroupTag() {
		return "267";
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
	* Constructor to construct FixRGNoMDEntryTypes_V object 
	*
	*/
	public FixRGNoMDEntryTypes_V() {
	}
	/**
	* setter method to set MDEntryType
	*
	* @param String - MDEntryType
	*/
	public void setMDEntryType(String _MDEntryType) {
		addBodyField(269, _MDEntryType);
	}
	/**
	* getter method to get MDEntryType
	*
	* @return String - MDEntryType
	*/
	public String getMDEntryType() {
		return (getBodyFieldValue(269));
	}

}
