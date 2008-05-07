package com.exsys.service;

import com.exsys.common.config.*;
import com.exsys.common.exceptions.*;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
/**
 * Insert the type's description here.
 * Creation date: (6/2/01 10:21:33 AM)
 * @author: Administrator
 */
public class Logger {
	static Category cat = Category.getInstance(Logger.class.getName());
/**
 * CommonTest constructor comment.
 */
public Logger() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (9/14/2002 1:20:13 AM)
 * @param value java.lang.String
 */
public static void debug(String value) 
{
	cat.debug( value );
}
/**
 * Insert the method's description here.
 * Creation date: (9/14/2002 1:20:13 AM)
 * @param value java.lang.String
 */
public static void error(String value) 
{
	cat.error( value );
}
/**
 * Insert the method's description here.
 * Creation date: (9/14/2002 1:20:13 AM)
 * @param value java.lang.String
 */
public static void fatal(String value) 
{
	cat.fatal( value );
}
/**
 * Insert the method's description here.
 * Creation date: (9/14/2002 1:20:13 AM)
 * @param value java.lang.String
 */
public static void info(String value) 
{
	cat.info( value );
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void initialize(String properties) 
{
		// Insert code to start the application here.
		PropertyConfigurator.configure(properties);		
	
}
/**
 * Insert the method's description here.
 * Creation date: (9/14/2002 1:20:13 AM)
 * @param value java.lang.String
 */
public static void warn(String value) 
{
	cat.warn( value );
}
}
