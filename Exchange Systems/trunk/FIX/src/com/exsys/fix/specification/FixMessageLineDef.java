package com.exsys.fix.specification;

/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 7:20:00 AM)
 * @author: Administrator
 */
public class FixMessageLineDef {
	private int tagNumber;
	private java.lang.String tagName;
	private boolean required;
	private java.lang.String description;
	private java.lang.String customDescription;
/**
 * FixFieldDef constructor comment.
 */
public FixMessageLineDef() {
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
public FixMessageLineDef(int newTagNumber, 
				String newTagName, 
				boolean newRequired, 
				String newDescription, 
				String newCustomDescription) 
{
	tagNumber = newTagNumber;
	tagName = newTagName;
	required = newRequired;
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
 * Creation date: (8/4/2002 7:22:07 AM)
 * @return java.lang.String
 */
public boolean isRequired() {
	return required;
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
 * Creation date: (8/4/2002 7:22:21 AM)
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 7:22:07 AM)
 * @param newFormat java.lang.String
 */
public void setRequired(boolean newRequired) {
	required = newRequired;
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
 * Creation date: (8/4/2002 10:38:16 PM)
 * @return java.lang.String
 */
public String toString() 
{
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("Tag Number: " + tagNumber );
	returnString.append("\n");

	returnString.append("Tag Name: " + tagName );
	returnString.append("\n");

	returnString.append("Required " + required );
	returnString.append("\n");
	
	returnString.append("Description: " + description );
	returnString.append("\n");

	returnString.append("CustomDescription: " + customDescription );
	returnString.append("\n");


	
	return returnString.toString();

}
}
