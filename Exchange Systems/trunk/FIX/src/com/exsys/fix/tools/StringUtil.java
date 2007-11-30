package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (10/8/2002 11:43:52 PM)
 * @author: Administrator
 */
public class StringUtil {
/**
 * StringUtil constructor comment.
 */
public StringUtil() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:44:41 PM)
 * @return java.lang.String
 * @param str java.lang.String
 */
public static String checkForNullsAndTrim(String str) {
	return ((str == null || str.trim().length()==0)?"null":str.trim());
}
}
