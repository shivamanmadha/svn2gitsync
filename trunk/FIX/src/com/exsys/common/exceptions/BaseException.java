package com.exsys.common.exceptions;

/**
 * BaseException is the base class for all exceptions.
 * Creation date: (6/2/01 10:09:04 AM)
 * @author: Administrator
 */
public class BaseException extends Exception {
	private java.lang.String externalMessage;
/**
 * BaseException constructor.
 */
public BaseException() {
	super();
}
/**
 * BaseException constructor.
 * @param s java.lang.String
 */
public BaseException(String s) {
	super(s);
	externalMessage = s;
}
/**
 * BaseException constructor.
 * Creation date: (6/2/01 10:10:51 AM)
 * @param message java.lang.String
 * @param extMessage java.lang.String
 */
public BaseException(String message, String extMessage) 
{
	super( message );
	externalMessage = extMessage;
}
/**
 * Method returns externalMessage of the exception
 * Creation date: (6/2/01 10:11:44 AM)
 * @return java.lang.String
 */
public java.lang.String getExternalMessage() {
	return externalMessage;
}
/**
 * Method to set externalMessage of the exception
 * Creation date: (6/2/01 10:11:44 AM)
 * @param newExternalMessage java.lang.String
 */
public void setExternalMessage(java.lang.String newExternalMessage) {
	externalMessage = newExternalMessage;
}
}
