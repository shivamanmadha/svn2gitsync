package com.exsys.fix.specification;

/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 11:25:42 AM)
 * @author: Administrator
 */
public class FixFieldValue {
	private java.lang.String value;
	private java.lang.String description;
	private boolean supported;
/**
 * FixFieldValue constructor comment.
 */
public FixFieldValue() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:43:11 AM)
 * @param newValue java.lang.String
 * @param newDescription java.lang.String
 * @param newFlag boolean
 */
public FixFieldValue(String newValue, 
	String newDescription,
	boolean newFlag) 
{
	value = newValue;
	description = newDescription;
	supported = newFlag;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:41:20 AM)
 * @return java.lang.String
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:26:18 AM)
 * @return java.lang.String
 */
public java.lang.String getValue() {
	return value;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:41:45 AM)
 * @return boolean
 */
public boolean isSupported() {
	return supported;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:41:20 AM)
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:41:45 AM)
 * @param newSupported boolean
 */
public void setSupported(boolean newSupported) {
	supported = newSupported;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:26:18 AM)
 * @param newValue java.lang.String
 */
public void setValue(java.lang.String newValue) {
	value = newValue;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 1:57:20 PM)
 * @return java.lang.String
 */
public String toString() {
	StringBuffer returnString = new StringBuffer();
	
	returnString.append(value);
	returnString.append("--");

	returnString.append(description);
	if( !supported )
	{
		returnString.append("-- Not Supported");
	}
	returnString.append("\n");
		
	
	return returnString.toString();}
}
