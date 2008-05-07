package com.exsys.common.exceptions;

/**
 * This exception is used to represent sender exception
 * Creation date: (6/2/01 9:51:43 AM)
 * @author: Administrator
 */
public class SenderException extends BusinessException {
/**
 * SenderException constructor.
 */
public SenderException() {
	super();
}
/**
 * SenderException constructor..
 * @param s java.lang.String
 */
public SenderException(String s) {
	super(s);
}
/**
 * SenderException constructor.
 * Creation date: (6/2/01 10:17:51 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public SenderException(String intMsg, String extMsg)
{
	super( intMsg, extMsg );
}
}
