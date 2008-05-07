package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (8/9/2002 3:54:02 PM)
 * @author: Administrator
 */
public class OrderTypeDef {
	private java.lang.String value;
	private java.lang.String description;
	private java.lang.String fixValue;
/**
 * OrderTypeDef constructor comment.
 */
public OrderTypeDef() {
	super();
}
/**
 * OrderTypeDef constructor comment.
 */
public OrderTypeDef(String newValue, 
					String newDescription,
					String newFixValue) {
	super();
	value = newValue;
	description = newDescription;
	fixValue = newFixValue;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:55:33 PM)
 * @return java.lang.String
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:55:49 PM)
 * @return java.lang.String
 */
public java.lang.String getFixValue() {
	return fixValue;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:55:22 PM)
 * @return java.lang.String
 */
public java.lang.String getValue() {
	return value;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:55:33 PM)
 * @param newDescription java.lang.String
 */
public void setDescription(java.lang.String newDescription) {
	description = newDescription;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:55:49 PM)
 * @param newFixValue java.lang.String
 */
public void setFixValue(java.lang.String newFixValue) {
	fixValue = newFixValue;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:55:22 PM)
 * @param newValue java.lang.String
 */
public void setValue(java.lang.String newValue) {
	value = newValue;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:57:01 PM)
 * @return java.lang.String
 */
public String toString() {
	return description;
}
}
