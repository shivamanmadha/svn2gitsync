package com.exsys.common.exceptions;

/**
 * This exception is used to represent fix field not found
 * Creation date: (6/2/01 9:51:43 AM)
 * @author: Administrator
 */
public class FixFieldNotFound extends BusinessException {
/**
 * FixFieldNotFound constructor.
 */
public FixFieldNotFound() {
	super();
}
/**
 * FixFieldNotFound constructor.
 * @param s java.lang.String
 */
public FixFieldNotFound(String s) {
	super(s);
}
/**
 * FixFieldNotFound constructor.
 * Creation date: (6/2/01 10:17:51 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public FixFieldNotFound(String intMsg, String extMsg)
{
	super( intMsg, extMsg );
}
}
