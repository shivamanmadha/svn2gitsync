package com.exsys.fix.test;

import java.io.*;

import com.exsys.fix.session.*;
import com.exsys.application.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;

public class ClientTest extends BaseApplication
{


	public ClientTest( String[] args ) throws ConfigFileNotFound
	{
		super(args);
	    String connectionPort=null;
	    String serverHost=null;
	    try
	    {
	    	connectionPort = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_CONNECT_PORT);
		   	Logger.debug("Connection Port is " + connectionPort );
	    	serverHost = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SERVER_HOST);
		   	Logger.debug("Server Host is " + serverHost );

	    
			int port = Integer.parseInt( connectionPort );
			JmsFixClientSession session = new JmsFixClientSession(serverHost,port);		
			session.setBOWLogon(getLogonType().equals(ConfigConstants.FIXCOMMON_LOGON_TYPE_BOW));
			session.start();
			session.sendLogonMessage();
		}
		catch(ConfigAttributeNotFound exc)
		{
			exc.printStackTrace();			
			Logger.fatal("Please check the configuration - " + exc.getExternalMessage());
			return;
		}
		catch(ConfigAttributeInvalid exc)
		{
			exc.printStackTrace();			
			Logger.fatal("Please check the configuration - " + exc.getExternalMessage());
			return;
		}	
		catch(SystemException exc)
		{
			exc.printStackTrace();			
			Logger.fatal("System Exception External Message - " + exc.getExternalMessage());
			Logger.fatal("System Exception Internal Message - " + exc.getMessage());
			return;
		}
		
	}

	public static void main( String[] args )
	{
		try
		{
			new ClientTest( args );
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}

	}
}
