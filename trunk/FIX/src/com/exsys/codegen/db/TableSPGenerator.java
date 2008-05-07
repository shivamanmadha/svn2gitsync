package com.exsys.codegen.db;

import java.io.*;
import java.util.*;

public class TableSPGenerator
{
    private int length = 0;

    public static String NON_NULL_IND = "NN";
    public static int PAD_LENGTH = 5;
    public static int DECIMAL_PLACES = 10;

    class FieldInfo
    {
      public String name;
      public String type;
      public String dateFormat;
      public String formElement;
      public String formElementType;
      public int length;
      public int decimalPlaces;
      public boolean isNull;

      public FieldInfo( String name,
                        String type,
                        String dateFormat,                        
                        String formElement,
                        String formElementType,
                        int length,
                        int decimalPlaces,
                        boolean isNull )
      {
          this.name = name;
          this.type = type;
          this.dateFormat = dateFormat;
          this.formElement = formElement;
          this.formElementType = formElementType;
          this.length = length;
          this.decimalPlaces = decimalPlaces;
          this.isNull = isNull;
      }

    }
    public TableSPGenerator(String file, String name )
    {

      try
      {
        FileInputStream is = new FileInputStream( file );
        FileOutputStream tblos = new FileOutputStream( name+"_tbl.sql");
        FileOutputStream specos = new FileOutputStream( name+"_spec_sp.sql");
        FileOutputStream bodyos = new FileOutputStream( name+"_body_sp.sql");
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        PrintWriter tblWriter = new PrintWriter( new OutputStreamWriter( tblos ) );
        PrintWriter specWriter = new PrintWriter( new OutputStreamWriter( specos ) );
        PrintWriter bodyWriter = new PrintWriter( new OutputStreamWriter( bodyos ) );

        Vector fieldsInfo = new Vector();

        String line;
        while ( (line = reader.readLine()) != null)
        {
    			StringTokenizer st = new StringTokenizer(line, ",");
		    	for (int i = 0; st.hasMoreTokens(); )
    			{

            int decimalPlaces = 0;
            String field = st.nextToken();
            System.out.println(field);
            String type = st.nextToken();
            int length = (Integer.valueOf( st.nextToken())).intValue();
            if( type.equals("Double") )
            {
              decimalPlaces = (Integer.valueOf( st.nextToken())).intValue();
            }
            String nullInd = st.nextToken();

            String formElement = st.nextToken();
            String formElementType = st.nextToken();
            String dateFormat = null;
            if( type.equals("Date") )
                dateFormat = st.nextToken();


            fieldsInfo.add( new FieldInfo( field,
                                           type,
                                           dateFormat,
                                           formElement,
                                           formElementType,
                                           length,
                                           decimalPlaces,
                                           (nullInd.equals(NON_NULL_IND))?false:true ));

          }
        }

// First write the table
          // Now write the print method
            // DROP Statement
            tblWriter.println("DROP TABLE "+name.toUpperCase()+" CASCADE CONSTRAINTS;");
            // CREATE TABLE statement
            tblWriter.println("CREATE TABLE "+name.toUpperCase()+" (");


           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name.toUpperCase();
               String type = fI.type;
               int length = fI.length;
               int decimalPlaces = fI.decimalPlaces;
               String formElement = fI.formElement;
               String formElementType = fI.formElementType;
               String dateFormat = null;
               if( type.equals("Date") )
                  dateFormat = fI.dateFormat;
               boolean isNull = fI.isNull;

               tblWriter.print("\t"+field+"\t");

               if( type.equals("String") )
               {
                  tblWriter.print("VARCHAR2("+(length)+")"+"\t");
               }
               else if( type.equals("Integer") )
               {
                  tblWriter.print("NUMBER("+length+")"+"\t");
               }
               else if( type.equals("Character"))
               {
                  tblWriter.print("CHAR(1)\t");
               }
               else if( type.equals("Double"))
               {
                  tblWriter.print("NUMBER("+(length)+","+decimalPlaces+")"+"\t");
               }
               else if( type.equals("Date"))
               {
                  tblWriter.print("DATE\t");
               }
               else
                System.out.println("UNKNOW DATA TYPE");

                if( isNull )
                {
                  tblWriter.print("NULL");
                }
                else
                {
                  tblWriter.print("NOT NULL");
                }
                //if( e.hasMoreElements() )
                  tblWriter.print(",");
                tblWriter.println();
           }
           // write db_last_changed time and user
            //tblWriter.println("\tDB_LAST_CHANGED_TIME  DATE  NULL,");
            //tblWriter.println("\tDB_USER_IDENTIFIER  VARCHAR2(32)  NULL");
            tblWriter.println(")");
// Write trailer stuff
            tblWriter.println("\t PCTFREE 10");
            tblWriter.println("\t PCTUSED 40");
            tblWriter.println("\t INITRANS 1");
            tblWriter.println("\t MAXTRANS 255");
            //tblWriter.println("\t TABLESPACE USERS");
            tblWriter.println("\t STORAGE (");
            tblWriter.println("\t \tMINEXTENTS 1");
            tblWriter.println("\t \tMAXEXTENTS 598");
            tblWriter.println("\t \tPCTINCREASE 40");
            tblWriter.println("\t);");

            tblWriter.flush();

// write the package spec
          // Now write the print method
            // PROCEDURE Statement
            specWriter.println("PROCEDURE "+name.toUpperCase() );
            specWriter.println("\t(");
            specWriter.println("\t\t o_Status \t OUT \t VARCHAR2,");
            specWriter.println("\t\t o_FailReason \t OUT \t VARCHAR2,");
            // CREATE TABLE statement

           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name;
               String type = fI.type;
               int length = fI.length;
               int decimalPlaces = fI.decimalPlaces;
               String formElement = fI.formElement;
               String formElementType = fI.formElementType;
               String dateFormat = null;
               if( type.equals("Date") )
                  dateFormat = fI.dateFormat;
               boolean isNull = fI.isNull;


               specWriter.print("\tp_"+field+"\tIN");
               if( type.equals("Date"))
               {
                    specWriter.print("\tVARCHAR2");
               }
               else
               {
                    specWriter.print("\t"+name.toUpperCase()+"."+field.toUpperCase()+"%TYPE");
               }

                if( e.hasMoreElements() )
                  specWriter.print(",");
                specWriter.println();
           }
            specWriter.println("\t);");
           specWriter.flush();


// write the package body
          // Now write the print method
            // PROCEDURE Statement
            bodyWriter.println("PROCEDURE "+name.toUpperCase() );
            bodyWriter.println("\t(");
            bodyWriter.println("\t\t o_Status \t OUT \t VARCHAR2,");
            bodyWriter.println("\t\t o_Fail_Reason \t OUT \t VARCHAR2,");
            // CREATE TABLE statement

           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name;
               String type = fI.type;
               int length = fI.length;
               int decimalPlaces = fI.decimalPlaces;
               String formElement = fI.formElement;
               String formElementType = fI.formElementType;
               String dateFormat = null;
               if( type.equals("Date") )
                  dateFormat = fI.dateFormat;
               boolean isNull = fI.isNull;

               bodyWriter.print("\tp_"+field+"\tIN");
               if( type.equals("Date"))
               {
                    bodyWriter.print("\tVARCHAR2");
               }
               else
               {
                    bodyWriter.print("\t"+name.toUpperCase()+"."+field.toUpperCase()+"%TYPE");
               }

                if( e.hasMoreElements() )
                  bodyWriter.print(",");
                bodyWriter.println();
           }
            bodyWriter.println("\t) AS");


            bodyWriter.println();
            bodyWriter.println();
            // add application error variables
            bodyWriter.println("\tv_mess_or\tVARCHAR2(300);");
            bodyWriter.println("\tv_mess_ap\tVARCHAR2(300);");

            bodyWriter.println("BEGIN");
            // Add insert code
            bodyWriter.println("\tINSERT INTO "+name.toUpperCase());
            bodyWriter.println("\t\t(");

           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name;
               bodyWriter.println("\t\t"+field.toUpperCase()+",");
           }
               //bodyWriter.println("\t\tDB_LAST_CHANGED_TIME,");
               //bodyWriter.println("\t\tDB_USER_IDENTIFIER");

            bodyWriter.println("\t\t) VALUES");
            bodyWriter.println("\t\t(");
           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name;
               String type = fI.type;
               String dateFormat = null;
               if( type.equals("Date") )
               {
                 dateFormat = fI.dateFormat;
                 bodyWriter.println("\t\tto_date(p_"+field+",'"+dateFormat+"'),");
               }
               else
               {
                 bodyWriter.println("\t\tp_"+field+",");
               }

           }
           bodyWriter.println("\t\tSYSDATE,");
           bodyWriter.println("\t\tUSER");


            bodyWriter.println("\t\t);");

            bodyWriter.println("COMMIT;");
            bodyWriter.println("o_Status := 'T';");

            // add exception handling code
            bodyWriter.println("EXCEPTION");
            bodyWriter.println("\tWHEN OTHERS THEN");
            bodyWriter.println("\t\tv_mess_or := substr(sqlerrm,1,200);");
            bodyWriter.println("\t\to_Status := 'F';");
            bodyWriter.println("\t\to_FailReason := v_mess_or;");
            bodyWriter.println("END "+name.toUpperCase()+";");


           bodyWriter.flush();



           tblWriter.close();
           specWriter.close();
           bodyWriter.close();
           reader.close();
        }
        catch(Exception e )
        {
            System.out.println(e);
            e.printStackTrace();
        }

    }
    public static void main(String[] args)
    {
        TableSPGenerator c = new TableSPGenerator( args[0], args[1] );
    }
}
