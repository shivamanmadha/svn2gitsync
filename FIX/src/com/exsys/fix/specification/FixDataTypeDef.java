package com.exsys.fix.specification;

/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 7:20:00 AM)
 * @author: Administrator
 */
public class FixDataTypeDef {
	private java.lang.String dataType;
	private java.lang.String description;
	private java.lang.String customDescription;
/**
 * FixFieldDef constructor comment.
 */
public FixDataTypeDef() {
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
public FixDataTypeDef(String newDataType, 
				String newDescription, 
				String newCustomDescription) 
{
	dataType = newDataType;
	description = newDescription;
	customDescription = newCustomDescription;
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
 * Creation date: (8/4/2002 10:38:16 PM)
 * @return java.lang.String
 */
public String toString() 
{
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("DataType: " + dataType );
	returnString.append("\n");

	returnString.append("Description: " + description );
	returnString.append("\n");

	returnString.append("CustomDescription: " + customDescription );
	returnString.append("\n");

	
	return returnString.toString();

}
}
