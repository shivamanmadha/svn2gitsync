package com.exsys.common.exceptions;

/**
 * This exception is used to represent config file not found
 * Creation date: (6/2/01 9:51:43 AM)
 * @author: Administrator
 */
public class ConfigFileNotFound extends BusinessException {
/**
 * ConfigFileNotFound constructor.
 */
public ConfigFileNotFound() {
	super();
}
/**
 * ConfigFileNotFound constructor.
 * @param s java.lang.String
 */
public ConfigFileNotFound(String s) {
	super(s);
}
/**
 * ConfigFileNotFound constructor.
 * Creation date: (6/2/01 10:17:51 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public ConfigFileNotFound(String intMsg, String extMsg)
{
	super( intMsg, extMsg );
}
}
