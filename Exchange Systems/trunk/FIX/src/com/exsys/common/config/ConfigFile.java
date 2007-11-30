package com.exsys.common.config;

import java.io.*;
import com.exsys.common.exceptions.*;
import java.util.*;

/**
 * @author kreddy
 *
 * Helper class to help read config values from files
 */
public class ConfigFile extends Properties implements ConfigSource {
	private java.lang.String cfgFileName;

/**
 * ConfigFile constructor
 * @param fileName
 * @throws ConfigFileNotFound
 */
public ConfigFile( String fileName) throws ConfigFileNotFound
{
	  cfgFileName = fileName;
	  try
	  {
		load( new FileInputStream( fileName ) );
	  }
	  catch( Exception e )
	  {

			 String extMsg = fileName + " - NotFound.";
	    	 String intMsg = e.getMessage();
		     ConfigFileNotFound cfnf = 
		     		new ConfigFileNotFound( intMsg,extMsg );
	     	 throw cfnf;
	  }

}

/**
 * ConfigFile constructor
 * @param defaults
 */
public ConfigFile(Properties defaults) {
	super(defaults);
}

/**
 * method getCfgFileName to return file name
 * @return cfgFileName String
 */
public java.lang.String getCfgFileName() {
	return cfgFileName;
}

/* (non-Javadoc)
 * @see com.exsys.common.config.ConfigSource#getValue(java.lang.String)
 */
public String getValue(String attribute) throws ConfigAttributeNotFound 
{

	  String result = getProperty( attribute );
	  if( result == null )
	  {

		 String extMsg = attribute +" - Not Found in file "+ cfgFileName ;
		 String intMsg = extMsg;
		 ConfigAttributeNotFound anf = 
		 			new ConfigAttributeNotFound( intMsg,extMsg );
		 throw anf;
	  }

	
	return result.trim();

}

/* (non-Javadoc)
 * @see com.exsys.common.config.ConfigSource#getValue(java.lang.String, java.lang.String)
 */
public String getValue(String attribute, String defaultValue) 
{
	  
	  String result = getProperty( attribute );
	  if( result == null )
	  {	  	  
		  result = defaultValue;
	  }

	  return result.trim();

}

/**
 * method to set cfgFileName
 * @param newCfgFileName
 */
void setCfgFileName(java.lang.String newCfgFileName) {
	cfgFileName = newCfgFileName;
}
}
