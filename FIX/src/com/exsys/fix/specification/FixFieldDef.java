package com.exsys.fix.specification;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;

/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 7:20:00 AM)
 * @author: Administrator
 */
public class FixFieldDef {
	private int tagNumber;
	private java.lang.String tagName;
	private java.lang.String dataType;
	private java.lang.String description;
	private java.lang.String customDescription;
	private Hashtable validValues = new Hashtable();
	private ArrayList validValuesList = new ArrayList();
/**
 * FixFieldDef constructor comment.
 */
public FixFieldDef() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:23:39 AM)
 * @param newTagNumber int
 * @param newTagName java.lang.String
 * @param newFormat java.lang.String
 * @param newDescription java.lang.String
 * @param newCustomDescription java.lang.String
 */
public FixFieldDef(int newTagNumber, 
				String newTagName, 
				String newDataType, 
				String newDescription, 
				String newCustomDescription) 
{
	tagNumber = newTagNumber;
	tagName = newTagName;
	dataType = newDataType;
	description = newDescription;
	customDescription = newCustomDescription;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:19:50 AM)
 * @param value java.lang.String
 * @param description java.lang.String
 */
public void addAllowedValue(String value, String description) 
{
	addAllowedValue( value, description, true );
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:19:03 AM)
 * @param value java.lang.String
 * @param description java.lang.String
 * @param isSupported boolean
 */
public void addAllowedValue(String value, String description, boolean isSupported) 
{
	FixFieldValue fv = new FixFieldValue( value, description, isSupported);
	validValues.put( value, fv );
	validValuesList.add( fv );
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:22:45 AM)
 * @return java.lang.String
 */
public java.lang.String getCustomDescription() {
	return customDescription;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:22:07 AM)
 * @return java.lang.String
 */
public java.lang.String getDataType() {
	return dataType;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:22:21 AM)
 * @return java.lang.String
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:21:07 AM)
 * @return java.lang.String
 */
public java.lang.String getTagName() {
	return tagName;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:20:39 AM)
 * @return int
 */
public int getTagNumber() {
	return tagNumber;
}
/**
 * Insert the method's description here.
 * Creation date: (9/16/2002 7:36:58 AM)
 * @return java.util.ArrayList
 */
public ArrayList getValidValues() 
{
	return validValuesList;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 11:23:17 AM)
 * @return boolean
 * @param value java.lang.String
 */
public boolean isValueValid(String value) {
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:22:45 AM)
 * @param newCustomDescription java.lang.String
 */
public void setCustomDescription(java.lang.String newCustomDescription) {
	customDescription = newCustomDescription;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:22:07 AM)
 * @param newFormat java.lang.String
 */
public void setDataType(java.lang.String newDataType) {
	dataType = newDataType;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:22:21 AM)
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:21:07 AM)
 * @param newTagName java.lang.String
 */
public void setTagName(java.lang.String newTagName) {
	tagName = newTagName;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:20:39 AM)
 * @param newTagNumber int
 */
public void setTagNumber(int newTagNumber) {
	tagNumber = newTagNumber;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 1:49:22 PM)
 * @return java.lang.String
 */
public String toString() {
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("Tag Number: " + tagNumber );
	returnString.append("\n");

	returnString.append("Tag Name: " + tagName );
	returnString.append("\n");

	returnString.append("Data Type: " + dataType );
	returnString.append("\n");
	
	returnString.append("Description: " + description );
	returnString.append("\n");

	returnString.append("CustomDescription: " + customDescription );
	returnString.append("\n");

    if( !validValues.isEmpty())
    {
		returnString.append("Valid Values: " + tagNumber );
		/*
		for( Enumeration e=validValues.elements(); e.hasMoreElements();)
		{
			FixFieldValue fieldValue = (FixFieldValue)e.nextElement();
			returnString.append(fieldValue.toString());
		}
		*/
		for( int i=0; i<validValuesList.size();i++)
		{
			FixFieldValue fieldValue = (FixFieldValue)validValuesList.get(i);
			returnString.append(fieldValue.toString());
		}		
		
    }
	returnString.append("\n");
	

	
		
	
	return returnString.toString();
}
}
