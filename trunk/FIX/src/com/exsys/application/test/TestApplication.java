package com.exsys.application.test;

import com.exsys.application.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;
/**
 * Insert the type's description here.
 * Creation date: (2/2/2002 8:44:15 AM)
 * @author: Administrator
 */
public class TestApplication extends BaseApplication {
/**
 * TestApplication constructor comment.
 */
public TestApplication() {
	super();
}
/**
 * TestApplication constructor comment.
 * @param args java.lang.String[]
 * @exception com.exsys.common.exceptions.ConfigFileNotFound The exception description.
 */
public TestApplication(java.lang.String[] args) throws ConfigFileNotFound {
	super(args);
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 8:54:07 AM)
 * @param args java.lang.String[]
 */
public static void main(String[] args)
{
	try
	{
		TestApplication app = new TestApplication( args );
		app.testConfig();
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 8:46:33 AM)
 */
public void testConfig() 
{
	try
	{
		String value = ConfigurationService.getValue("TestAttribute");
		System.out.println("Value is " + value );
	}
	catch(Exception e )
	{
		e.printStackTrace();
	}
}
}
