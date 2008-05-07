package com.exsys.service;

import com.exsys.common.config.*;
import com.exsys.common.exceptions.*;
/**
 * Insert the type's description here.
 * Creation date: (2/2/2002 8:14:13 AM)
 * @author: Administrator
 */
public class ConfigurationService {
	private static ConfigFile cfg = null;
/**
 * ConfigurationService constructor comment.
 */
public ConfigurationService() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 8:24:46 AM)
 * @param attribute java.lang.String
 * @exception com.exsys.common.exceptions.ConfigAttributeNotFound The exception description.
 */
public static String getValue(String attribute) throws ConfigAttributeNotFound 
{
	String value = null;
	if( cfg != null )
	{
		value = cfg.getValue( attribute );
	}

	return value;
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 8:24:46 AM)
 * @param attribute java.lang.String
 * @exception com.exsys.common.exceptions.ConfigAttributeNotFound The exception description.
 */
public static String getValue(String attribute, String defValue) throws ConfigAttributeNotFound 
{
	String value = null;
	if( cfg != null )
	{
		value = cfg.getValue( attribute, defValue );
	}

	return value;
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 8:19:25 AM)
 * @param cfgFileName java.lang.String
 * @exception com.exsys.common.exceptions.ConfigFileNotFound The exception description.
 */
public static void initialize(String cfgFileName) throws ConfigFileNotFound 
{
	if( cfg == null )
	{
		cfg = new ConfigFile( cfgFileName );
	}
}
}
