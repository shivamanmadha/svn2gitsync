package com.exsys.common.db;

import java.lang.*;
import java.sql.*;
import java.io.*;

public abstract class DBQuery
{
   /**
    * Indicate  if the query is being retried.
    */
   protected boolean m_IsRetry = false;
   /**
    * Either stored procedure call or embedded SQL string.
    */
   protected String m_QueryString;
   /**
    * Name of the query.
    */
   protected String m_QueryName;
   /**
    * Error Message String.
    */
   protected String m_QueryErrorMessage;
   /**
    * Indicate  if the query processing success.
    */
   public static final int QUERY_SUCCESS = 0;
   /**
    * Indicate  if the query processing with SQL error.
    */
   public static final int QUERY_SQL_ERROR = 1;
   /**
    * Indicate  if the query processing with non-SQL error.
    */
   public static final int QUERY_OTHER_ERROR = 2;

   public DBQuery( String queryString, String queryName )
   {
      m_QueryString = queryString;
      m_QueryName = queryName;
      m_QueryErrorMessage = null;
   }
   /**
    * This abstract function tells the ICmeDbService how to process the query.
    *
    * @param     conn  java.sql.Connection object
    * @param     transactionId transaction Id, not used yet.
    * @return    <code>QUERY_SUCCESS</code> if query success
    *            <code>QUERY_SQL_ERROR</code> if query encounter SQL exception
    *            <code>QUERY_OTHER_ERROR</code> if query encounter non-SQL exception
    */
   public abstract void executeQuery( Connection conn )
                       throws Exception;
   /**
    * This function returns the name of the query
    *
    *@return Query error message.
    */
    public String getQueryErrorMessage()
    {
       return m_QueryErrorMessage;
    }
   /**
    * This function returns the name of the query
    *
    *@return name of the query.
    */
   public String getQueryName()
   {
      return m_QueryName;
   }
   /**
    * This function sets the error message
    *
    *@param   Query error message string
    */
    public void setQueryErrorMessage( String errorMessage )
    {
       m_QueryErrorMessage = errorMessage;
    }
   public void setRetry()
   {
      m_IsRetry = true;
   }
}
