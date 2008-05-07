package com.exsys.common.db;

import java.io.*;
import java.util.*;
import java.sql.*;

public class DBLookup
{
  private Connection dbConnection = null;
  private DatabaseManager dbManager = null;

	public DBLookup()
	{
	}

	public void setDatabaseManager( DatabaseManager dbMgr )
	{
	  dbManager = dbMgr;
		try
		{
		  dbConnection = dbManager.GetConnection();
		  //dbConnection.setAutoCommit( false );
		}
		//catch( SQLException e )
		//{
		  //e.printStackTrace();

		//}
		catch( Exception e )
		{
		  e.printStackTrace();

		}
	}


  public ResultSet executeQuery(String query)
    throws Exception
  {
    System.out.println("In GetPOStatus");
    ResultSet rs;
    if( dbConnection == null )
      throw new Exception("No Connection To Database");

      synchronized ( dbConnection )
      {
        try
        {
          Statement stmt = dbConnection.createStatement();
          rs = stmt.executeQuery(query);
        }
        catch( Exception e )
        {
          e.printStackTrace();
          throw e;
        }

      }

      return rs;
  }

}
