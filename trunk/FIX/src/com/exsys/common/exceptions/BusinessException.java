package com.exsys.common.exceptions;

/**
 * BusinessException is used to represent business exceptions.
 * Creation date: (6/2/01 9:32:29 AM)
 * @author: Administrator
 */
public class BusinessException extends BaseException {
/**
 * BusinessException constructor .
 */
public BusinessException() {
	super();
}
/**
 * BusinessException constructor.
 * @param s java.lang.String
 */
public BusinessException(String s) {
	super(s);
}
/**
 * BusinessException constructor.
 * Creation date: (6/2/01 10:15:23 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public BusinessException(String intMsg, String extMsg) 
{
	super( intMsg, extMsg );
}
}
