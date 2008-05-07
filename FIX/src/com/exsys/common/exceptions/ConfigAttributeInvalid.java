package com.exsys.common.exceptions;

/**
 * This exception is used to represent invalid config attribute
 * Creation date: (6/2/01 9:36:44 AM)
 * @author: Administrator
 */
public class ConfigAttributeInvalid extends BusinessException {
/**
 * ConfigAttributeNotFound constructor.
 */
public ConfigAttributeInvalid() {
	super();
}
/**
 * ConfigAttributeNotFound constructor.
 * @param s java.lang.String
 */
public ConfigAttributeInvalid(String s) {
	super(s);
}
/**
 * ConfigAttributeNotFound constructor.
 * Creation date: (6/2/01 10:16:55 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public ConfigAttributeInvalid(String intMsg, String extMsg)
{
	super( intMsg, extMsg );
}
}
