package com.exsys.codegen.fix;

import java.io.*;
import java.util.*;

public class FixHeaderGenerator
{
    private int length = 0;
    public static String NON_NULL_IND = "NN";
    class FieldInfo
    {
      public String name;
      public int num;
      public String type;

      public FieldInfo( String name,
			int num,
                        String type )
      {
          this.name = name;
          this.type = type;
	  this.num = num;
      }

    }
    public FixHeaderGenerator(String file, String name )
    {

      try
      {
        FileInputStream is = new FileInputStream( file );
        FileOutputStream os = new FileOutputStream( name+".java");
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        PrintWriter writer = new PrintWriter( new OutputStreamWriter( os ) );
        //writer.println( "package com.exapi.common.beans;\n");        
        //writer.println("import java.io.*;");
        //writer.println("import java.util.*;");
        //writer.println("import com.exsys.common.fixmessage.*;");
        writer.println();
        writer.println("public class "+name+" extends FixMessage");
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
            int num = (Integer.valueOf( st.nextToken())).intValue();
            String type = st.nextToken();
	    String format = null;
	    if( type.equals("Date")) format = st.nextToken();

	 		
            fieldsInfo.add( new FieldInfo( field,
					   num,
                                           type ));

	     if( type.equals("data") )
		continue;

            //fields.add("  private "+ type +" m_"+field+";");
            writer.print("\t");
	    if( !type.equals("String") )
	    {
            writer.println("public void set"+field+"( "+type+" "+"_"+field+" )");
            writer.println("\t{");
	    if( type.equals("Date"))
	            writer.println("\t\taddHeaderField( "+num+", "+" get"+format+"String( _"+field+"));");
	    else
		    writer.println("\t\taddHeaderField( "+num+", "+" getString( _"+field+"));");	
            writer.println("\t}");
	    writer.print("\t");
	    }
	    
	    {
	    //writer.print("\t");
            writer.println("public void set"+field+"( String "+"_"+field+" )");
            writer.println("\t{");
            writer.println("\t\taddHeaderField( "+num+", "+" _"+field+");");
            writer.println("\t}");

	    }

            writer.print("\t");
	    if( !type.equals("String") )
	    {

            writer.println("public "+type+" get"+field+"()");
            writer.println("\t{");
	    if( type.equals("Date"))
	            writer.println("\t\t return ("+format+"StringTo"+type+"(getHeaderFieldValue( "+num+" ))) ;");
	    else
	            writer.println("\t\t return (stringTo"+type+"(getHeaderFieldValue( "+num+" ))) ;");
            writer.println("\t}");

	    writer.print("\t");
            writer.println("public String  get"+field+"AsString()");
            writer.println("\t{");
            writer.println("\t\t return (getHeaderFieldValue( "+num+" )) ;");
            writer.println("\t}");

	    }
	    else	
	    {
	    //writer.print("\t");
            writer.println("public String  get"+field+"()");
            writer.println("\t{");
            writer.println("\t\t return (getHeaderFieldValue( "+num+" )) ;");
            writer.println("\t}");
	    }


          }
        }

        writer.println();
        writer.println();


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
        FixHeaderGenerator c = new FixHeaderGenerator( args[0], args[1] );
    }
}
