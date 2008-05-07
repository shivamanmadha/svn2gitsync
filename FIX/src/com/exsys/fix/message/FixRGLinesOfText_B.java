package com.exsys.fix.message;

import java.util.*;

/**
* This class is used to represent the FIX message RGLinesOfText_B
*
*/
public class FixRGLinesOfText_B extends FixRepeatedGroup {
	public static ArrayList fieldList = new ArrayList();
	static {
		fieldList.add("58");

	};

	public String FIRST_FIELD = "58";

	/**
	* getter method to get FirstField
	*
	* @return String - FirstField
	*/
	public String getFirstField() {
		return "58";
	}
	/**
	* getter method to get RepeatedGroupTag
	*
	* @return String - RepeatedGroupTag
	*/
	public String getRepeatedGroupTag() {
		return "33";
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
	* Constructor to construct FixRGLinesOfText_B object 
	*
	*/
	public FixRGLinesOfText_B() {
	}
	/**
	* setter method to set Text
	*
	* @param String - Text
	*/
	public void setText(String _Text) {
		addBodyField(58, _Text);
	}
	/**
	* getter method to get Text
	*
	* @return String - Text
	*/
	public String getText() {
		return (getBodyFieldValue(58));
	}

}
