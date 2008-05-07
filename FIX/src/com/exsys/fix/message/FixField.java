package com.exsys.fix.message;

/**
 * This class FixField represents a single fix field with tag number
 * and tag value
 */
public class FixField {
	private int tagNumber;
	private java.lang.String tagValue;
/**
 * Default constructor for FixField.
 */
public FixField() {
	super();
}
/**
 * Constructor that takes tag number and tag value as parameters
 * Creation date: (11/11/01 6:26:11 PM)
 * @param newTagNumber int
 * @param newTagValue java.lang.String
 */
public FixField(int newTagNumber, String newTagValue)
{
	tagNumber = newTagNumber;
	tagValue = newTagValue;
}
/**
 * getter method to get tag number as integer
 * Creation date: (11/11/01 6:25:17 PM)
 * @return int
 */
public int getTagNumber() {
	return tagNumber;
}
/**
 * getter method to get tag number as string.
 * Creation date: (11/18/01 7:36:40 PM)
 * @return java.lang.String
 */
public String getTagNumberAsString() {
	return String.valueOf( tagNumber );
}
/**
 *  getter method to get tag value as integer
 * Creation date: (11/11/01 6:25:42 PM)
 * @return java.lang.String
 */
public java.lang.String getTagValue() {
	return tagValue;
}
/**
 * setter method to set tag number of the fix field object
 * Creation date: (11/11/01 6:25:17 PM)
 * @param newTagNumber int
 */
public void setTagNumber(int newTagNumber) {
	tagNumber = newTagNumber;
}
/**
 * setter method to set tag value of the fix field object
 * Creation date: (11/11/01 6:25:42 PM)
 * @param newTagValue java.lang.String
 */
public void setTagValue(java.lang.String newTagValue) {
	tagValue = newTagValue;
}
}
