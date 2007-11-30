package com.exsys.fix.test;

import java.io.*;
import java.util.*;

import com.exsys.fix.session.*;
import com.exsys.fix.message.*;
import com.exsys.application.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;
// database related
import com.exsys.common.db.*;
import com.exsys.common.db.dbsql.*;

public class FixDBTest extends BaseApplication
{

  //private Connection dbConnection = null;
  private DatabaseManager dbManager = null;
  private FixDBService dbService = null;


	public FixDBTest( String[] args ) throws ConfigFileNotFound
	{
		super(args);
	    String connectionPort=null;
	    String serverHost=null;
	    try
	    {
	    	String driver =  ConfigurationService.getValue("JDBCdriver");
	    	System.out.println("JDBCdriver is " + driver );
	    	
	    	String dbName = ConfigurationService.getValue("DatabaseName");
	    	System.out.println("DatabaseName is " + dbName );
	    	
	    	String dbUserId = ConfigurationService.getValue("DatabaseUserId");
	    	System.out.println("DatabaseUserId is " + dbUserId );

	    	String dbPassword = ConfigurationService.getValue("DatabasePassword");
	    	System.out.println("DatabasePassword is " + dbPassword );
	    
	    	String connectionURL =  ConfigurationService.getValue("ConnectionURL");
	    	System.out.println("ConnectionURL is " + connectionURL );
	    		
	    	String useAuthentication =  ConfigurationService.getValue("UseAuthentication");
	    	System.out.println("UseAuthentication is " + useAuthentication );    	
	    	
	    	//String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";create=true";
	    	//String connectionURL = "jdbc:derby://localhost:1527/fixdb;";
	    	
			dbManager = new DatabaseManager(dbUserId,dbPassword,connectionURL,driver);
			
			
			dbService = new FixDBService();
			if(connectionURL.startsWith("jdbc:sqlserver"))
			{
				System.out.println("Connecting to SQL Server");
				dbService.setSQLServer(true);
			}
			dbService.setDatabaseManager(dbManager,(useAuthentication.equals("Y")?true:false));
			
			
 	     	 String outFile = ConfigurationService.getValue("FixOutLogFile");
 	    	 ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	    	 String inFile = ConfigurationService.getValue("FixInLogFile");
 	    	 ArrayList inList = FixSessionManager.getApplicationMessagesList(inFile);
 	    	 // first process out list
 	    	 
 	    	for(int i=0; i<outList.size();i++)
	    	{
    			FixMessage msg = (FixMessage)outList.get(i);
	    		String msgType = msg.getMessageType();
	    		handleRestoredMessage(msgType,msg);
	    	}


 	    	 // then process in list
 	    	for(int i=0; i<inList.size();i++)
	    	{
    			FixMessage msg = (FixMessage)inList.get(i);
	    		String msgType = msg.getMessageType();
	    		handleRestoredMessage(msgType,msg);
	    	}
	    	
		}
		catch(ConfigAttributeNotFound exc)
		{
			exc.printStackTrace();
			Logger.fatal("Please check the configuration - " + exc.getExternalMessage());
			return;
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			return;
		}

	}


  private void restoreFromFix() throws Exception
  {
    try
    {
 	     String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("FixInLogFile");
 	     ArrayList inList = FixSessionManager.getApplicationMessagesList(inFile);
 	     // first process out list
 	    for(int i=0; i<outList.size();i++)
	    {
    		FixMessage msg = (FixMessage)outList.get(i);
	    	String msgType = msg.getMessageType();
	    	handleRestoredMessage(msgType,msg);
	    }


 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
    		FixMessage msg = (FixMessage)inList.get(i);
	    	String msgType = msg.getMessageType();
	    	handleRestoredMessage(msgType,msg);
	    }


    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }


  private void handleRestoredMessage(String msgType,FixMessage msg) throws Exception
  {
	if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
	{
		processRestoredOrder( (FixOrder)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL) )
	{
		processRestoredCancel( (FixCancel)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE) )
	{
		processRestoredCancelReplace( (FixCancelReplace)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT) )
	{
		processExecutionReport( (FixExecutionReport)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT) )
	{
		processCancelReject( (FixCancelReject)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_BUSINESS_REJECT) )
	{
		processBusinessReject( (FixBusinessReject)msg );
	}
	
  }

   public void processRestoredOrder(FixOrder fixorder) throws Exception
  {
	System.out.println("processRestoredOrder");
	fixorder.debugPrint();
	if(dbService != null)
	{
		try
		{
			dbService.addFixOrder(fixorder,"test","PEND","PEND",false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

  }
  public void processRestoredCancel(FixCancel fixcxl)throws Exception
  {
	  System.out.println("processRestoredCancel");
	fixcxl.debugPrint();
	if(dbService != null)
	{
		try
		{
			dbService.addFixCancel(fixcxl,"test","PEND",false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}	  

  }

  // ========================================================================
  // ========================================================================
  public void processRestoredCancelReplace(FixCancelReplace fixcxr)throws Exception
  {
	  System.out.println("processCancelReplace");
	  fixcxr.debugPrint();
	  if(dbService != null)
	 {
		try
		{
			dbService.addFixCancelReplace(fixcxr,"test","PEND","PEND",true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	  }
	
  }

  public void processCancelReject(FixCancelReject cxlRej)throws Exception
  {
    System.out.println("processCancelReject");
    cxlRej.debugPrint();
	if(dbService != null)
	{
		try
		{
			dbService.addFixCancelReject(cxlRej,"test");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

  }

  public void processBusinessReject(FixBusinessReject busRej)throws Exception
  {
    System.out.println("processBusinessReject");
    busRej.debugPrint();
	if(dbService != null)
	{
		try
		{
			dbService.addFixBusinessReject(busRej,"test");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

  }

 public void processExecutionReport(FixExecutionReport response)throws Exception
 {
    System.out.println("processExecutionReport");
    response.debugPrint();
	if(dbService != null)
	{
		try
		{
			dbService.addFixExecutionReport(response,"test","PEND");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
 }

	public static void main( String[] args )
	{
		try
		{
			new FixDBTest( args );
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}

	}
}
