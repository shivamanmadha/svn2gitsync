package com.exsys.codegen.db;

import java.io.*;
import java.util.*;

public class BeanGenerator
{
    private int length = 0;
    public static String NON_NULL_IND = "NN";
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
    public BeanGenerator(String file, String name )
    {

      try
      {
        FileInputStream is = new FileInputStream( file );
        FileOutputStream os = new FileOutputStream( name+".java");
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        PrintWriter writer = new PrintWriter( new OutputStreamWriter( os ) );
        writer.println( "package com.exapi.common.beans;\n");        
        writer.println("import java.io.*;");
        writer.println("import java.util.*;");
        writer.println();
        writer.println("public class "+name);
        writer.println("{");

        writer.print("\t");
        writer.println("public "+name+"()");
        writer.println("\t{");
        writer.println("\t}");

        Vector fields = new Vector();
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

            fields.add("  private "+ type +" m_"+field+";");
            writer.print("\t");
            writer.println("public void set"+field+"( "+type+" "+"_"+field+" )");
            writer.println("\t{");
            writer.println("\t\tm_"+field+" = _"+field+";");
            writer.println("\t}");
            writer.print("\t");
            writer.println("public "+type+" get"+field+"()");
            writer.println("\t{");
            writer.println("\t\t return m_"+field+" ;");
            writer.println("\t}");

          }
        }

        writer.println();
        writer.println();

          // Now write the print method
            writer.print("\t");
            writer.println("public void print()");
            writer.println("\t{");

           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
               String field = ((FieldInfo)e.nextElement()).name;
               writer.print("\t");
               writer.print("\t");
               writer.println("System.out.println(\"Value Of " + field + " : \"+"+"m_"+ field+");" );
               //writer.println("System.out.println(\"Value Of "+field+ "\"+"": m_"+field +");" );
           }
            writer.println("\t}");


          // Now write the fields
           for( Enumeration e=fields.elements(); e.hasMoreElements();)
           {
                writer.println((String)e.nextElement() );
           }

           writer.println("}");
           writer.flush();
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
        BeanGenerator c = new BeanGenerator( args[0], args[1] );
    }
}
