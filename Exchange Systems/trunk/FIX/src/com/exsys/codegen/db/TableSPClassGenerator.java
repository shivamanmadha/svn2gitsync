package com.exsys.codegen.db;

import java.io.*;
import java.util.*;

public class TableSPClassGenerator
{
    private int length = 0;

    public static String NON_NULL_IND = "NN";
    public static int PAD_LENGTH = 5;
    public static int DECIMAL_PLACES = 5;
    public static Hashtable sqlTypesMap = new Hashtable();
   

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
    public TableSPClassGenerator(String file, 
		       String name, 
		       String packageName,
		       String procedureName )
    {
     	sqlTypesMap.put("String","Types.VARCHAR");
     	sqlTypesMap.put("Double","Types.DOUBLE");
     	sqlTypesMap.put("Integer","Types.INTEGER");
     	sqlTypesMap.put("Character","Types.CHAR");
     	sqlTypesMap.put("Date","Types.DATE");


      try
      {
        FileInputStream is = new FileInputStream( file );
        FileOutputStream os = new FileOutputStream( "Add"+name+"SP.java");
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        PrintWriter writer = new PrintWriter( new OutputStreamWriter( os ) );

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

        // Write imports
        writer.println("package com.exapi.common.db.dbsp;\n");
        writer.println( "import com.exapi.common.db.*;");
        writer.println( "import com.exapi.common.beans.*;\n");
        writer.println( "import java.sql.*;");
        writer.println("import java.text.SimpleDateFormat;");

        // Class name
        writer.println(" public class Add"+name+"SP extends DBActionQuery");
        writer.println("{");

        // Constructor
        writer.println("\tpublic static SimpleDateFormat YYYYMMDDformatter = ");
        writer.println("\t\t new SimpleDateFormat(\"yyyy-MM-dd\");");
        writer.println("\tpublic static SimpleDateFormat MMYYYYformatter = ");
        writer.println("\t\t new SimpleDateFormat(\"MM-yyyy\");");
        writer.println("\tpublic static SimpleDateFormat SpecialDateformatter = ");
        writer.println("\t\t new SimpleDateFormat(\"yyyy-MM-DDThh:mm:ss-hh:mm\");");

        writer.println("\tprivate "+name+" m_"+name+";");
        writer.println("\tprivate String m_Status;");
        writer.println("\tprivate String m_FailReason;");

        writer.println("public Add"+name+"SP("+name+" m_"+name+")");
        writer.println("{");

        writer.print("\tsuper(\"{call "+packageName+"."+procedureName+"(?,?,");
           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               writer.print("?");
               if( e.hasMoreElements())
                writer.print(",");
               else
                writer.print(")}\",\""+procedureName+"\");");
           }
        writer.println();
        writer.println("\tthis.m_"+name+" = m_"+name+";");

        writer.println("}");


      writer.println("protected void buildStatement( CallableStatement stmt )");
      writer.println("\t\tthrows SQLException");
      writer.println("{");

      int index = 1;
      writer.println("\tstmt.registerOutParameter("+ index++ +",Types.VARCHAR);");
      writer.println("\tstmt.registerOutParameter("+ index++ +",Types.VARCHAR);");
      //writer.println("\tstmt.registerOutParameter("+ index++ +",Types.INTEGER);");
      writer.println("\ttry");
      writer.println("\t{");

           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               FieldInfo fI = (FieldInfo)e.nextElement();
               String field = fI.name;
               String type = fI.type;
               int length = fI.length;
               int decimalPlaces = fI.decimalPlaces;
               String dateFormat = null;
               if( type.equals("Date") )
                 dateFormat = fI.dateFormat;
               boolean isNull = fI.isNull;


               if( type.equals("String") )
               {
            		  if( isNull )
            		  {
                    index++;
                    writer.println("\t\tif( m_"+name+".get"+field+"()==null)");
                    writer.println("\t\t{");
                  	//writer.println("\tstmt.setNull("+ index +",("+type+")sqlTypes.get("+type+"));");
                  	writer.println("\tstmt.setNull("+ index +","+(String)sqlTypesMap.get("String")+");");
                    writer.println("\t\t}");
                    writer.println("\t\telse");
                    writer.println("\t\t{");
                  	writer.println("\tstmt.setString("+ index +",m_"+name+".get"+field+"());");
                    writer.println("\t\t}");

            		  }
          	   	  else
          	  	  {
                     writer.println("\tstmt.setString("+ ++index +",m_"+name+".get"+field+"());");
            		  }
               }
               else if( type.equals("Integer") )
               {
            		  if( isNull )
            		  {
                    index++;
                    writer.println("\t\tif( m_"+name+".get"+field+"()==null)");
                    writer.println("\t\t{");
                  	writer.println("\tstmt.setNull("+ index +","+(String)sqlTypesMap.get("Integer")+");");
                    writer.println("\t\t}");
                    writer.println("\t\telse");
                    writer.println("\t\t{");
                  writer.println("\tstmt.setInt("+ index +",(m_"+name+".get"+field+"()).intValue());");
                    writer.println("\t\t}");

            		  }
          	   	  else
          	  	  {
                  writer.println("\tstmt.setInt("+ ++index +",(m_"+name+".get"+field+"()).intValue());");
            		  }


               }
               else if( type.equals("Character"))
               {
            		  if( isNull )
            		  {
                    index++;
                    writer.println("\t\tif( m_"+name+".get"+field+"()==null)");
                    writer.println("\t\t{");
                  	writer.println("\tstmt.setNull("+ index +","+(String)sqlTypesMap.get("Character")+");");
                    writer.println("\t\t}");
                    writer.println("\t\telse");
                    writer.println("\t\t{");
                  writer.println("\tstmt.setString("+ index +",(m_"+name+".get"+field+"()).toString());");
                    writer.println("\t\t}");

            		  }
          	   	  else
          	  	  {
                  writer.println("\tstmt.setString("+ ++index +",(m_"+name+".get"+field+"()).toString());");
            		  }

               }
               else if( type.equals("Double"))
               {
            		  if( isNull )
            		  {
                   index++;
                    writer.println("\t\tif( m_"+name+".get"+field+"()==null)");
                    writer.println("\t\t{");
                  	writer.println("\tstmt.setNull("+ index +","+(String)sqlTypesMap.get("Double")+");");
                    writer.println("\t\t}");
                    writer.println("\t\telse");
                    writer.println("\t\t{");
                  writer.println("\tstmt.setDouble("+ index +",(m_"+name+".get"+field+"()).doubleValue());");
                    writer.println("\t\t}");

            		  }
          	   	  else
          	  	  {
                  writer.println("\tstmt.setDouble("+ ++index +",(m_"+name+".get"+field+"()).doubleValue());");
            		  }


               }
               else if( type.equals("Date"))
               {
            		  if( isNull )
            		  {
                  index++;
                    writer.println("\t\tif( m_"+name+".get"+field+"()==null)");
                    writer.println("\t\t{");
                  	writer.println("\tstmt.setNull("+ index +","+(String)sqlTypesMap.get("Date")+");");
                    writer.println("\t\t}");
                    writer.println("\t\telse");
                    writer.println("\t\t{");
                  writer.println("\tstmt.setString("+ index +","+
                        dateFormat+"formatter.format( m_"+name+".get"+field+"()));");

                    writer.println("\t\t}");

            		  }
          	   	  else
          	  	  {
                  writer.println("\tstmt.setString("+ ++index +","+
                        dateFormat+"formatter.format( m_"+name+".get"+field+"()));");

            		  }

               }
               else
                System.out.println("UNKNOW DATA TYPE");

           }
           writer.println();
           writer.println("\t}");
           writer.println("\tcatch(SQLException e)");
           writer.println("\t{");
           writer.println("\t\tSystem.out.println(e);");
           writer.println("\t}");
           writer.println("\tcatch(Exception e)");
           writer.println("\t{");
           writer.println("\t\tSystem.out.println(e);");
           writer.println("\t}");

           writer.println("}");


      // process Result
       writer.println("protected boolean processResult( CallableStatement stmt ) throws SQLException");
       writer.println("{");
       writer.println("\tm_Status = stmt.getString(1);");
       writer.println("\tm_FailReason = stmt.getString(2);");
       writer.println("\treturn true;");
       writer.println("}");

       // getStatus
       writer.println("public String GetStatus() ");
       writer.println("{");
       writer.println("\treturn m_Status ;");
       writer.println("}");

      // getFailReason
       writer.println("public String GetFailReason() ");
       writer.println("{");
       writer.println("\treturn m_FailReason ;");
       writer.println("}");


       writer.println("}");

           writer.close();
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
        TableSPClassGenerator c = new TableSPClassGenerator( args[0], args[1], args[2], args[3] );
    }
}
