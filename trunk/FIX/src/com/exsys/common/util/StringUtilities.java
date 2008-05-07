package com.exsys.common.util;

import java.math.BigDecimal;

/**
 * @author kreddy
 *
 * Helper class to process string data
 */
public class StringUtilities {
/**
 * Helper method to count leading zeros
 * Creation date: (4/10/2003 3:58:15 PM)
 * @return int
 * @param value java.lang.String
 */
private static int countLeadingZeros(String value) {
	if (value == null || value.trim().length() == 0) {
		return 0;
	}
	// Trim off white space
	value = value.trim();
	int length = value.length();

	// Count the leading zeros by finding the first index of 0 and then moving
	// the starting point back
	int count = 0;
	int beginIndex = 0;
	while(length > count &&
		  value.indexOf('0', beginIndex) == beginIndex) {
		count++;
		beginIndex++;
	}
	return count;
}
/**
 * method to count trailing zeros
 * Creation date: (4/10/2003 3:58:15 PM)
 * @return int
 * @param value java.lang.String
 */
private static int countTrailingZeros(String value) {
	if (value == null || value.trim().length() == 0) {
		return 0;
	}
	// Trim off white space
	value = value.trim();
	int length = value.length();

	// Count the trailing zeros by finding the last index of 0 and then moving
	// the starting point back
	int count = 0;
	int endIndex = length - 1;
	while(length > count &&
		  value.lastIndexOf('0', endIndex) == endIndex) {
		count++;
		endIndex--;
	}
	return count;
}
/**
 * This will scan a field value from a machine readable record and validate that it can
 * be treated as a number.  AlphaNumeric numbers used for decimal parts of numbers
 * can have trailing spaces or be empty but they can't have leading spaces.
 */
public final static boolean isAlphaNumericDecimal(String field) {
	return field == null || !field.startsWith(" ") || field.trim().length() == 0;
}
/**
 * This will scan a field value from a machine readable record and validate that it can
 * be treated as a number.  AlphaNumeric numbers used for whole parts of numbers
 * can have leading spaces or be empty but they can't have trailing spaces.
 */
public final static boolean isAlphaNumericWhole(String field) {
	return field == null || !field.endsWith(" ") || field.trim().length() == 0;
}
/**
 * This will scan a field value from a machine readable record and validate that it can
 * be treated as a number.  Numeric vales can't contain leading or trailing spaces or be null.
 */
public final static boolean isNumeric(String field, int fieldLength) {
	return field != null && !field.startsWith(" ") && !field.endsWith(" ") && field.length() == fieldLength;
}
/**
 * method to limit a string to a particular length
 * Creation date: (8/27/01 3:40:41 PM)
 * @return java.lang.String
 * @param string java.lang.String
 * @param length int
 */
public static final String limitString(String string, int length) {
	if (string.length() <= length) {
		return string;
	} else {
		return string.substring(0, length);
	}
}
/**
 * method to remove leading zeros
 * Creation date: (4/10/2003 3:59:28 PM)
 * @return java.lang.String
 * @param value java.lang.String
 */
public static String removeLeadingZeros(String value) {
	// return blank string if the value is null or blank
	if (value == null || value.trim().length() == 0) {
		return new String();
	}
	// Trim off white space
	value = value.trim();

	return value.substring(countLeadingZeros(value), value.length());
}
/**
 * method to remove trailing zeros
 * Creation date: (4/10/2003 3:59:28 PM)
 * @return java.lang.String
 * @param value java.lang.String
 */
public static String removeTrailingZeros(String value) {
	// return blank string if the value is null or blank
	if (value == null || value.trim().length() == 0) {
		return new String();
	}
	// Trim off white space
	value = value.trim();

	return value.substring(0, value.length() - countTrailingZeros(value));
}


/**
 * method to prepad a string
 * @param value
 * @param pad
 * @param width
 * @return
 */
public static String prePad(String value, char pad, int width) {
	int length = value.length();
	if (length < width) {
		StringBuffer buffer = new StringBuffer(value);
		while (length < width) {
			buffer.insert(0, pad);
			length++;
		}
		return buffer.toString();
	} else {
		return value;
	}
}

/**
 * method to check if a string is blank
 * @param value
 * @return
 */
public static boolean isBlank(String value )
{
	return (value == null || value.trim().length() == 0 );
}

/**
 * method to check if a string is alphabetic
 * @param inStr
 * @return
 */
public static boolean isAlpha(String inStr) 
{
	char[] chrAry =  inStr.toCharArray();
	for (int charcount = 0; charcount < inStr.length(); charcount++)
	{
		if(!Character.isLetter(chrAry[charcount]))
		{
			return false;
		}
	}
	return true;
}
/*
 * This method returns true only if every digit of 
 * the String passed has numaric in it.
 * Creation date: (9/24/04 09:50:09 AM)
 * @return boolean
 * @param inStr java.lang.String
 */
public static boolean isNumeric(String inStr) 
{
	char[] chrAry =  inStr.toCharArray();
	for (int charcount = 0; charcount < inStr.length(); charcount++)
	{
		if(!Character.isDigit(chrAry[charcount]))
		{
			return false;
		}
	}
	return true;
}
/*
 * This method returns ture only if the passed value
 * is either valid decimal or numaric.
 * @author rtandra
 *
 * @return boolean
 * @param inStr java.lang.String
 */
 public static boolean isDecimal(String inStr){
 	try{
 		new BigDecimal(inStr);
 		return true;
 	}catch(NumberFormatException n){
 		return false;
 	}
 }

}
