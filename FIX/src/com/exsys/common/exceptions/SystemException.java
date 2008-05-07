package com.exsys.common.exceptions;

/**
 * This exception is used to represent system exception
 * Creation date: (6/2/01 9:32:52 AM)
 * @author: Administrator
 */
public class SystemException extends BaseException {
/**
 * SystemException constructor.
 */
public SystemException() {
	super();
}
/**
 * SystemException constructor.
 * @param s java.lang.String
 */
public SystemException(String s) {
	super(s);
}
/**
 * SystemException constructor.
 * Creation date: (6/2/01 10:18:29 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public SystemException(String intMsg, String extMsg) 
{
	super( intMsg, extMsg );
}
}
