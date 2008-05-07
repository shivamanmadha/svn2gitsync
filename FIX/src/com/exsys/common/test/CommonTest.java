package com.exsys.common.test;

import com.exsys.common.config.*;
import com.exsys.common.exceptions.*;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
/**
 * Insert the type's description here.
 * Creation date: (6/2/01 10:21:33 AM)
 * @author: Administrator
 */
public class CommonTest {
	static Category cat = Category.getInstance( CommonTest.class.getName());
/**
 * CommonTest constructor comment.
 */
public CommonTest() {
	super();
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) 
{
	
		// Insert code to start the application here.
		PropertyConfigurator.configure(args[0]);
		cat.info("About to construct ConfigSouce");
		try
		{
			ConfigSource configSource = new ConfigFile( args[0] );
			cat.debug("Config file constructed successfully...");
			String value = configSource.getValue("TEST");
			cat.debug(" Value is "+ value );
			value = configSource.getValue("TEST1","default");
			cat.debug(" Value is "+ value );
			
			
		}catch( BaseException e )
		{
			cat.fatal("Exception with configuration", e);
		}
		
		cat.info("Exiting ...");
		
		
	
}
}
