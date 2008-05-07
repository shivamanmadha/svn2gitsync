package com.exsys.codegen.db;

import java.io.*;
import java.util.*;

public class DerbyTableGenerator
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
      public int length;
      public int decimalPlaces;
      public boolean isNull;

      public FieldInfo( String name,
                        String type,
                        String dateFormat,
                        int length,
                        int decimalPlaces,
                        boolean isNull )
      {
          this.name = name;
          this.type = type;
          this.dateFormat = dateFormat;
          this.length = length;
          this.decimalPlaces = decimalPlaces;
          this.isNull = isNull;
      }

    }
    public DerbyTableGenerator(String file, String name )
    {

      try
      {
        FileInputStream is = new FileInputStream( file );
        FileOutputStream tblos = new FileOutputStream( name+"_tbl.sql");
        FileOutputStream insos = new FileOutputStream( name+"_ins.java");        
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        PrintWriter tblWriter = new PrintWriter( new OutputStreamWriter( tblos ) );
        PrintWriter insWriter = new PrintWriter( new OutputStreamWriter( insos ) );        

        Vector fieldsInfo = new Vector();

        String line;
        while ( (line = reader.readLine()) != null)
        {
    		StringTokenizer st = new StringTokenizer(line, ",");
		    for (int i = 0; st.hasMoreTokens();)
    		{
            String nullInd = "Y";
            String dateFormat = null;
            int length = 0;

            int decimalPlaces = 0;
            String field = st.nextToken();
            System.out.println(field);
            // ignore the tag field
            String ingnore1 = st.nextToken();

            String type = st.nextToken();
            System.out.println(type);
            if(!type.equals("IDENTITYKEY"))
            {
            
            if(!type.equals("Date"))
            {
            	length = (Integer.valueOf( st.nextToken())).intValue();
            	System.out.println(length);
           	 	if( type.equals("float") )
            	{
            	  decimalPlaces = (Integer.valueOf( st.nextToken())).intValue();
            	  System.out.println(decimalPlaces);
            	}
			}

            //String nullInd = st.nextToken();

            
            if( type.equals("Date") )
                dateFormat = st.nextToken();

            }
            
            fieldsInfo.add( new FieldInfo( field,
                                           type,
                                           dateFormat,
                                           length,
                                           decimalPlaces,
                                           (nullInd.equals(NON_NULL_IND))?false:true ));
			break;
          }
        }

// First write the table
          // Now write the print method
            // DROP Statement
            //tblWriter.println("DROP TABLE "+name.toUpperCase()+" CASCADE CONSTRAINTS;");
            tblWriter.println("DROP TABLE "+name.toUpperCase()+";");
            // CREATE TABLE statement
            tblWriter.println("CREATE TABLE "+name.toUpperCase()+" (");


           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name.toUpperCase();
               String type = fI.type;
               int length = fI.length;
               int decimalPlaces = fI.decimalPlaces;
               String dateFormat = null;
               if( type.equals("Date") )
                  dateFormat = fI.dateFormat;
               boolean isNull = fI.isNull;

               tblWriter.print("\t"+field+"\t");

			   if(type.equals("IDENTITYKEY"))
			   {
			   	tblWriter.print("INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)");
			   }
               else if( type.equals("String") )
               {
				   if(length > 4 )
                  		tblWriter.print("VARCHAR("+(length)+")"+"\t");
                   else
                   		tblWriter.print("CHAR("+(length)+")"+"\t");
               }
               else if( type.equals("Integer") || type.equals("int") )
               {
				   if(length < 4)
                  		tblWriter.print("SMALLINT"+"\t");
                   else
                   		tblWriter.print("INTEGER"+"\t");

               }
               else if( type.equals("Character") || type.equals("boolean"))
               {
                  tblWriter.print("CHAR(1)\t");
               }
               else if( type.equals("Double") || type.equals("float"))
               {
                  tblWriter.print("DECIMAL("+(length)+","+decimalPlaces+")"+"\t");
               }
               else if( type.equals("Date"))
               {
				   if(dateFormat.equals("LocalMktDate"))
                  		tblWriter.print("DATE\t");
                   else if(dateFormat.equals("UTCTimestamp"))
                   		tblWriter.print("TIMESTAMP\t");
                   else if(dateFormat.equals("MonthYear\t"))
                   		tblWriter.print("CHAR(6)\t");
                   else
                   	    tblWriter.print("DATE\t");


               }
               else
                System.out.println("UNKNOW DATA TYPE");

				if(!type.equals("IDENTITYKEY") )
				{
     	           if( !type.equals("Integer") && isNull )
      	           {
       	        	   //tblWriter.print("NULL");
        	       }
         	       else
      	    	   {
     	      	       tblWriter.print("NOT NULL");
            	   }
				}
                //if( e.hasMoreElements() )
                  tblWriter.print(",");
                tblWriter.println();
           }
           tblWriter.println("\tCREATEDBY VARCHAR(32),");
           tblWriter.println("\tCREATIONDATE TIMESTAMP,");
           tblWriter.println("\tLASTMODIFIEDBY VARCHAR(32),");
           tblWriter.println("\tLASTMODIFIEDDATE TIMESTAMP");

           // write db_last_changed time and user
            //tblWriter.println("\tDB_LAST_CHANGED_TIME  DATE  NULL,");
            //tblWriter.println("\tDB_USER_IDENTIFIER  VARCHAR2(32)  NULL");
            tblWriter.println(")");
            /*
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
			*/
           tblWriter.flush();
           tblWriter.close();
           
           
           // write the insert file
		   insWriter.println(" query = \"INSERT INTO "+name.toUpperCase()+"(\";");
		   
           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name.toUpperCase();
               String type = fI.type;
               if(!type.equals("IDENTITYKEY"))
	               insWriter.println("\tquery += \""+field+",\";");
           }
           insWriter.println("\tquery += \"CREATEDBY,\";");
           insWriter.println("\tquery += \"CREATIONDATE,\";");
           insWriter.println("\tquery += \"LASTMODIFIEDBY,\";");
           insWriter.println("\tquery += \"LASTMODIFIEDDATE\";");
           
           insWriter.println("\tquery += \") VALUES (\";");
           
           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               //String field = fI.name.toUpperCase();
               String field = fI.name;
               String type = fI.type;
               String dateFormat = null;
               if( type.equals("Date") )
                  dateFormat = fI.dateFormat;
               //insWriter.print("\t"+field+"\t");

			   if(type.equals("IDENTITYKEY"))
			   {
			   	 continue;
			   }
			   insWriter.print("\tquery += ");
			   
               if( type.equals("String") )
               {               		
               		
               	   insWriter.print("(fixMsg.get"+field+"() == null?\"NULL\":\"'\"+fixMsg.get"+field+"()+\"'\")");               	    
               }
               else if( type.equals("Integer") )
               {
				   insWriter.print("messageId");
				   
					
               }
               else if ( type.equals("int")|| type.equals("float"))
               {
               	   insWriter.print("(fixMsg.get"+field+"AsString() == null?\"NULL\":fixMsg.get"+field+"AsString())"); 
               }
               else if( type.equals("boolean"))
               {
                  insWriter.print("(fixMsg.get"+field+"AsString() == null?\"NULL\":\"'\"+fixMsg.get"+field+"AsString()+\"'\")"); 
               }
               else if( type.equals("Date"))
               {
				  insWriter.print("(fixMsg.get"+field+"AsString() == null?\"NULL\":\"'\"+FixMessage.getDBTimestamp(fixMsg.get"+field+"())+\"'\")"); 
               }
               else
                System.out.println("UNKNOW DATA TYPE");


                //if( e.hasMoreElements() )
                 insWriter.print("+\",\";");
                insWriter.println();
           }
           insWriter.print("\tquery += \"'\"+userId+\"'\"");
           insWriter.println("+\",\";");
           insWriter.print("\tquery += \"CURRENT_TIMESTAMP\"");
           insWriter.println("+\",\";");
           insWriter.print("\tquery += \"'\"+userId+\"'\"");
           insWriter.println("+\",\";");
           insWriter.print("\tquery += \"CURRENT_TIMESTAMP )\";");
           //insWriter.print("+\",\";");


            //insWriter.println(")");

           insWriter.flush();
           insWriter.close();           
           
           
           

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
        DerbyTableGenerator c = new DerbyTableGenerator( args[0], args[1] );
    }
}
