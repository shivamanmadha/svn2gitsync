package com.exsys.common.db;

import java.lang.*;
import java.sql.*;

public abstract class DBActionQuery extends DBQuery
{

   /**
    * The constructor allows the user to pass in the SQL and the query name.
    *
    * @param queryString  the SQL statement that calls the stored procedure with
    *                     parameters.  For example: "{call proc1(?,?,?)}" or
    *                     "{?=call func1(?,?,?)}"
    * @param queryName    the name of the query.  This could be used for logging
    *                     purpose inside the ICmeDbService
    */
   public DBActionQuery( String queryString, String queryName )
   {
      super( queryString, queryName );
   }
   /**
    * The <i>callback</i> function for the query object to bind variables with
    * the JDBC statement.
    * Need to pay attention to the m_IsRetry flag, if the query supports
    * fail over with pos. dup. processing.
    *
    * @param     stmt JDBC statement object passed back from the ICmeDbService
    * @exception SQLException
    *             if a database-access error occurs.
    */
   protected abstract void buildStatement( CallableStatement stmt )
         throws SQLException;
   /**
    * This call back function implements the stored procedure calling process.
    * ICmeDbService will call this function, pass in the JDBC connection and
    * transaction Id (not used yet).  This function then calls the object's
    * <code>buildStatement</code> function to bind the necessary input
    * variables, then it execute the statement, and call the <code>processResult</code>
    * function to gather results back from the statement.
    *
    * @param conn JDBC database connection
    * @param transactionId transaction id for automatic fail over pos. dup.
    *                      processing. (not implemented yet)
    * @return    <code>QUERY_SUCCESS</code> if query success
    *            <code>QUERY_SQL_ERROR</code> if query encounter SQL exception
    *            <code>QUERY_OTHER_ERROR</code> if query encounter non-SQL exception
    */
   public final void executeQuery( Connection conn ) throws Exception
   {
      int result = QUERY_SUCCESS;
      CallableStatement stmt = null;
      try
      {
         stmt = conn.prepareCall( m_QueryString );
         //stmt.setQueryTimeout(10);
         System.out.println("Statement prepared");
         buildStatement( stmt );
         System.out.println("Statement build");
         boolean success  = stmt.execute();
         System.out.println("Statement executed " + success);
         processResult( stmt );
         System.out.println("Result processed");
         stmt.close();
         System.out.println("Statement closed");         
      }
      catch ( SQLException e )
      {
         result = QUERY_SQL_ERROR;

         if ( stmt!=null )
         {
            try
            {
               stmt.close();
            } catch ( Exception ex ) {

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

            };
         }
         throw e;
      }
   }
   /**
    * The <i>callback</i> function for the query object to gather query results from
    * the JDBC statement
    * The implementation of this method usually use the JDBC get??? method
    * to obtain the returning parameters from the statements.
    *
    * @param     stmt JDBC statement object passed back from the ICmeDbService
    * @return    true if successful.
    * @exception SQLException
    *             if a database-access error occurs.
    */
   protected boolean processResult( CallableStatement stmt ) throws SQLException
   {
      return true;
   }
}
