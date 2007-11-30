package com.exsys.common.db;

import java.sql.*;
import java.util.*;

public class DatabaseManager
{
  Vector connectionPool = new Vector();
//  public static final int INIT_CONNECTIONS = 15;
  public static final int INIT_CONNECTIONS = 0;
  private String connectionURL;
  private String userId;
  private String password;

  /*
  public static void main( String[] args )
  {
      String url = "jdbc:odbc:TestDB";
      try
      {
          Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
          Connection con = DriverManager.getConnection(url,"po8","po8");
          DatabaseMetaData dbData = con.getMetaData();
          System.out.println("Database Product Name: " +
                              dbData.getDatabaseProductName() );
          System.out.println("Database Product Version: " +
                              dbData.getDatabaseProductVersion() );

          System.out.println("JDBC Driver Name: " +
                              dbData.getDriverName() );
          System.out.println("JDBC Driver Version: " +
                              dbData.getDriverVersion() );
          Statement stmt = con.createStatement();

          ResultSet rs = dbData.getProcedures( "KREDDY","KREDDY","KREDDY");

          System.out.println( rs );
          //System.out.println("Total Rows: " + rs.getFetchSize() );


          ResultSet rs = dbData.getSchemas();

          String schemaName;
          //System.out.println("Total Rows: " + rs.getFetchSize() );
          while( rs.next() )
          {
            schemaName = rs.getString("TABLE_CAT");
            System.out.println(
              "Schema Name: "+ schemaName );
          }

          ResultSet rs1 = dbData.getSchemas();

          String catalogName;
          //System.out.println("Total Rows: " + rs.getFetchSize() );
          while( rs1.next() )
          {
            catalogName = rs1.getString("TABLE_SCHEM");
            System.out.println(
              "Catalog Name: "+ catalogName );
          }



          //ResultSet rSet = dbData.getSchemas();
          ResultSetMetaData rsData = rs.getMetaData();
          for( int i=1;i<=rsData.getColumnCount();i++)
          {
            System.out.println("Colum No: "+i);
            System.out.println("Column Name: "+rsData.getColumnName(i));
            //System.out.println("Column Class Name: "+rsData.getColumnClassName(i));
            System.out.println("Column Class Name: "+rsData.getColumnTypeName(i));
          }


      }
      catch(ClassNotFoundException e)
      {
        System.out.println(e);
      }
      catch(SQLException e)
      {
        System.out.println(e);
      }
  }
  */

  public DatabaseManager()
  {
    connectionURL = "jdbc:oracle:thin:@localhost:1521:orcl";
    userId = "scott";
    password = "tiger";

      try
      {
          Class.forName("oracle.jdbc.driver.OracleDriver");
      }
      catch(ClassNotFoundException e)
      {
        System.out.println(e);
      }

      InitializeConnections();
  }
  public DatabaseManager( String _userId, String _password )
  {
    connectionURL = "jdbc:oracle:thin:@localhost:1621:orcl";
    userId = _userId;
    password = _password;

    System.out.println("DatabaseManager::CTOR-"+_userId+"/"+_password+"-"+connectionURL);

      try
      {
          Class.forName("oracle.jdbc.driver.OracleDriver");
      }
      catch(ClassNotFoundException e)
      {
        System.out.println(e);
        e.printStackTrace();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }

      InitializeConnections();
  }
  public DatabaseManager( String _userId, String _password, String _url, String _driver )
  {
    connectionURL = _url;
    userId = _userId;
    password = _password;

      try
      {
          Class.forName( _driver );
      }
      catch(ClassNotFoundException e)
      {
        System.out.println(e);
      }

      InitializeConnections();
  }
  public DatabaseManager( boolean usePW,String _userId, String _password, String _url, String _driver )
  {
    connectionURL = _url;
    userId = _userId;
    password = _password;

      try
      {
          Class.forName( _driver );
      }
      catch(ClassNotFoundException e)
      {
        System.out.println(e);
      }

      InitializeConnections(usePW);
  }
  
  private void CloseConnections()
  {
  }
  private Connection CreateNewConnection()
  {
      Connection con = null;
      try
      {
      	/*
        con = DriverManager.getConnection(connectionURL,
                                                    userId,
                                                    password);
        */
        con = DriverManager.getConnection(connectionURL);        

      }
      catch( Exception e )
      {
        System.out.println( e );
        e.printStackTrace();
      }

      return con;
  }
  private Connection CreateNewConnection(boolean usePW)
  {
      Connection con = null;
      try
      {
      	if(usePW)
        	con = DriverManager.getConnection(connectionURL,
                                                    userId,
                                                    password);
                                                
        else
        	con = DriverManager.getConnection(connectionURL);        

      }
      catch( Exception e )
      {
        System.out.println( e );
        e.printStackTrace();
      }

      return con;
  }
  public void ExecuteQuery( DBActionQuery action )
  {
    Connection con = GetConnection();
    System.out.println("Got Connection");
    try
    {
      action.executeQuery( con );
      System.out.println("Query Executed");
    }
    catch( Exception e )
    {
        System.out.println( e );
        e.printStackTrace();
    }

    ReturnConnection( con );
    System.out.println("Returned Connection");

  }
  public void ExecuteQuery( DBSelectQuery action )
  {
    System.err.println("DatabaseManager::ExecuteQuery-SELECT");
    Connection con = GetConnection();
    System.out.println("Got Connection");
    try
    {
      action.executeQuery( con );
      System.out.println("Query Executed");
    }
    catch( Exception e )
    {
        System.out.println( e );
        e.printStackTrace();
    }

    ReturnConnection( con );
    System.out.println("Returned Connection");

  }
  public Connection GetConnection()
  {
    /*
      synchronized( connectionPool )
      {
          if( connectionPool.size() != 0 )
          {
              return( (Connection)connectionPool.remove(0) );
          }
          else
          {
              return CreateNewConnection();
          }
      }

    */
            return CreateNewConnection();
  }
  
  public Connection GetConnection(boolean usePW)
  {
    /*
      synchronized( connectionPool )
      {
          if( connectionPool.size() != 0 )
          {
              return( (Connection)connectionPool.remove(0) );
          }
          else
          {
              return CreateNewConnection();
          }
      }

    */
            return CreateNewConnection(usePW);
  }
  private void InitializeConnections()
  {

      try
      {
        for( int i=0; i<INIT_CONNECTIONS;i++)
        {
          Connection con = CreateNewConnection();

          if( con != null )
            connectionPool.addElement( con );

        }
      }
      catch(Exception e)
      {
        System.out.println(e);
      }

  }
    private void InitializeConnections(boolean usePW)
  {

      try
      {
        for( int i=0; i<INIT_CONNECTIONS;i++)
        {
          Connection con = CreateNewConnection(usePW);

          if( con != null )
            connectionPool.addElement( con );

        }
      }
      catch(Exception e)
      {
        System.out.println(e);
      }

  }

  private void ReturnConnection( Connection con )
  {
      synchronized( connectionPool )
      {
          connectionPool.addElement( con );
      }
  }
}
