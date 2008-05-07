package com.exsys.common.db;

import java.io.*;
import java.util.*;
import java.text.*;
import java.sql.*;

public class DBFormat
{
public static final char LTSYMBOL='<';
public static final char GTSYMBOL='>';

public static final String LTSTR = "&lt;";
public static final String GTSTR = "&gt;";

public DBFormat()
{
}

  public String formatTable( ResultSet rs )
    throws Exception
  {
    StringWriter writer = new StringWriter();
    formatTable( rs, new PrintWriter( writer ));
    return (writer.toString());
  }
  public String formatMessageLogTable( ResultSet rs )
    throws Exception
  {
    StringWriter writer = new StringWriter();
    formatMessageLogTable( rs, new PrintWriter( writer ));
    return (writer.toString());
  }


  public String formatOrganizations( ResultSet rs )
    throws Exception
  {
    StringWriter writer = new StringWriter();
    formatOrganizations( rs, new PrintWriter( writer ));
    return (writer.toString());
  }

  public String formatOrganizations( ResultSet rs, String select )
    throws Exception
  {
    StringWriter writer = new StringWriter();
    formatOrganizations( rs, select, new PrintWriter( writer ));
    return (writer.toString());
  }


  public void formatTable( ResultSet rs, PrintWriter out)
    throws Exception
  {
    int rowCount = 0;
    // create the table
    //out.println("<center><table celspacing=0 cellpadding=2 border=0>");

    // Process the results. First dump out the column
    // headers from the meta data
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    out.println("<TR>");
    System.out.println("Printing meta data");
    for( int i=0; i<columnCount; i++ )
    {
        out.println("<TH align=\"right\">"+rsmd.getColumnName(i+1) + "</TH>");
        //out.println("<TH width=5>&nbsp;</TH>");
    }
    out.println("</TR>");

    while( rs.next() )
    {
        rowCount++;
        //out.println("<tr bgclor=#F0F0FF>");
	  if( rowCount%2 != 0)
	     out.println("<TR class=TableRow1>");
	  else
		out.println("<TR class=TableRow2>");
        for( int i=0; i<columnCount; i++)
        {
          String val = rs.getString(i+1);
           out.println("<TD>"+ val +"</TD>");
          //out.println("<TD width=5>&nbsp;</TD>");
        }
        out.println("</TR>");
    }

    //out.println("</table></center>");

  }
  public void formatMessageLogTable( ResultSet rs, PrintWriter out)
    throws Exception
  {
    int rowCount = 0;
    // create the table
    //out.println("<center><table celspacing=0 cellpadding=2 border=0>");

    // Process the results. First dump out the column
    // headers from the meta data
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    //out.println("<tr>");
    //for( int i=0; i<columnCount; i++ )
    //{
    //    out.println("<th bgcolor=CECEFF>"+rsmd.getColumnName(i+1) + "</th>");
    //    out.println("<th width=5>&nbsp;</th>");
    //}
    //out.println("</tr>");

    while( rs.next() )
    {
        rowCount++;
        out.println("<tr bgclor=#F0F0FF>");
        for( int i=0; i<columnCount; i++)
        {
          String val = rs.getString(i+1);
          if( i==2 )
          {
           out.println("<td BGCOLOR=#FF9966>"+ formatMessage(val) +"</td>");
          }
          else
          {
            out.println("<td>"+ val +"</td>");
          }
          //out.println("<td width=5>&nbsp;</td>");
        }
        out.println("</tr>");
    }

    //out.println("</table></center>");

  }

  private String formatMessage(String msg )
  {
    StringBuffer outString = new StringBuffer();
    StringCharacterIterator iter = new StringCharacterIterator( msg );
    System.out.println("Message before formatting - " + msg );
    for(char c = iter.first(); c != CharacterIterator.DONE; c = iter.next())
    {
      	if( c == LTSYMBOL )
    	  {
      	   outString.append(LTSTR);
      	}
      	else if( c == GTSYMBOL )
      	{
      	   outString.append(GTSTR);
      	}
      	else
        {
      	   outString.append( c );
        }
    }

        System.out.println("Message after formatting - " + outString );
        return outString.toString();
  }

  public void formatOrganizations( ResultSet rs, String select, PrintWriter out)
    throws Exception
  {
    int rowCount = 0;


    // Process the results. First dump out the column
    // headers from the meta data
    ResultSetMetaData rsmd = rs.getMetaData();
    while( rs.next() )
    {
        rowCount++;
          String val = rs.getString(1);
          if( val.equals(select) )
          {
            out.println("<option selected>"+ val);
          }
          else
          {
            out.println("<option>"+ val);
          }
    }

  }
  public void formatOrganizations( ResultSet rs, PrintWriter out)
    throws Exception
  {
    int rowCount = 0;


    // Process the results. First dump out the column
    // headers from the meta data
    ResultSetMetaData rsmd = rs.getMetaData();
    while( rs.next() )
    {
        rowCount++;
          out.println("<option>"+ rs.getString(1));
    }

  }


}
