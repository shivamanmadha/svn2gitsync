package com.exsys.common.db;

import java.lang.*;
import java.sql.*;

public abstract class DBSelectQuery extends DBQuery
{
   private static final String SOURCE_FILE = "CmeSelectQuery";
   private static final String EXEC_ERR_MSG = "Database read failed";

   /**
    * The constructor allows the user to pass in the SQL and the query name.
    *
    * @param queryString  the SQL SELECT statement
    * @param queryName    the name of the query.  This could be used for logging
    *                     purpose inside the ICmeDbService
    */
   public DBSelectQuery( String queryString, String queryName )
   {
      super( queryString, queryName );
   }
   /**
    * This call back function implements the embedded SQL SELECT  procedure calling process.
    * ICmeDbService will call this function, pass in the JDBC connection and
    * transaction Id (not used yet).  This function then uses the SQL query string
    * to construct a statement and execute it, then pass the ResultSet to the
    * <code>processResult</code> function to fetch the results.
    *
    * @param conn JDBC database connection
    * @param transactionId transaction id for automatic fail over pos. dup.
    *                      processing. (not implemented yet)
    * @return    <code>QUERY_SUCCESS</code> if query success
    *            <code>QUERY_SQL_ERROR</code> if query encounter SQL exception
    *            <code>QUERY_OTHER_ERROR</code> if query encounter non-SQL exception
    */
   public final void executeQuery( Connection conn ) throws
                     Exception
   {

      System.err.println("DBSelectQuery::executeQuery");

      Statement stmt = null;
      int result = QUERY_SUCCESS;
      try
      {
         stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery( m_QueryString );
         processResult( rs );
         stmt.close();
      }
      catch ( SQLException e )
      {
         result = QUERY_SQL_ERROR;
         if ( stmt!=null )
         {
            try {
               stmt.close();
            } catch ( Exception ex ) {
            ex.printStackTrace();
            };
         }
         throw e;
      }
      catch ( Exception e )
      {
         result = QUERY_OTHER_ERROR;

         if ( stmt!=null )
         {
            try {
               stmt.close();
            } catch ( Exception ex ) {
              ex.printStackTrace();
            };
         }
         throw e;
      }
   }
   /**
    * The <i>callback</i> function for the query object to gather query results from
    * the JDBC ResultSet
    * The implementation of this method usually looping through the ResultSet
    * records, get the necessary fields and store or process the data.
    *
    * @param     rs JDBC ResultSet object passed back from the ICmeDbService
    * @return    true if successful.
    * @exception SQLException
    *             if a database-access error occurs.
    */
   protected abstract boolean processResult( ResultSet rs ) throws SQLException;
}
