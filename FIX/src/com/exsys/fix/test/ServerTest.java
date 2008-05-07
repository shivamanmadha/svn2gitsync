package com.exsys.fix.test;

import java.io.*;

import com.exsys.fix.session.*;
import com.exsys.application.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;

public class ServerTest extends BaseApplication
{
	
	public ServerTest( String[] args ) throws ConfigFileNotFound
	{
		super(args);


	    String serverPort=null;
	    try
	    {
	    	serverPort = ConfigurationService.getValue(ConfigConstants.FIXSERVER_LISTEN_PORT);
		   	System.out.println("Server Port is " + serverPort );
	    }
	    catch( ConfigAttributeNotFound exc )
	    {
		    System.out.println("CONFIG VALUES DOES NOT EXIST");
		    exc.printStackTrace();
		    System.exit(0);	    
	    }
		

		int port = Integer.parseInt( serverPort );
		System.out.println(getLogonType());
		FixServer server = new FixServer(port,getLogonType().equals(ConfigConstants.FIXCOMMON_LOGON_TYPE_BOW));
		server.start();
	}	
		
	public static void main( String[] args )
	{
		try
		{
			new ServerTest( args );
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
	}

}
