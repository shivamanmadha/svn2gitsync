package com.exsys.fix.test;

import java.io.*;

import com.exsys.fix.specification.*;
import com.exsys.application.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;

public class FixSpecificationTest extends BaseApplication
{
	public FixSpecificationTest( String[] args ) throws ConfigFileNotFound
	{
		super(args);
		/*
	    String connectionPort=null;
	    String serverHost=null;
	    String senderCompID = null;
	    String targetCompID = null;
	    try
	    {
	    	connectionPort = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_CONNECT_PORT);
		   	System.out.println("Connection Port is " + connectionPort );
	    	serverHost = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SERVER_HOST);
		   	System.out.println("Server Host is " + serverHost );
	    	senderCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SENDER_COMP_ID);
		   	System.out.println("Sender Comp ID is " + senderCompID );
	    	targetCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_TARGET_COMP_ID);
		   	System.out.println("Target Comp ID is " + targetCompID );
		   	
		   	
	    }
	    catch( ConfigAttributeNotFound exc )
	    {
		    System.out.println("CONFIG VALUES DOES NOT EXIST");
		    exc.printStackTrace();
		    System.exit(0);	    
	    }		
		int port = Integer.parseInt( connectionPort );
		JmsFixClientSession session = new JmsFixClientSession(serverHost,port,senderCompID,targetCompID);
		session.start();
		session.sendLogonMessage();
		*/
		FixSpecification spec = FixSpecification.getSpecification();
		spec.debugPrint();
	}
	public static void main( String[] args )
	{
		try
		{
			new FixSpecificationTest( args );
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}		
		
	}
}
