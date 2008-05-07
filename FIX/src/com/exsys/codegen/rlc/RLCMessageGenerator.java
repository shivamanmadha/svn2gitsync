package com.exsys.codegen.rlc;

import java.io.*;
import java.util.*;

public class RLCMessageGenerator
{
    private int length = 0;
    public static String NON_NULL_IND = "NN";
    public RLCMessageGenerator(String file, String name, String msgType )
    {

      try
      {
        FileInputStream is = new FileInputStream( file );
        FileOutputStream os = new FileOutputStream( name+".java");
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        PrintWriter writer = new PrintWriter( new OutputStreamWriter( os ) );
        writer.println( "package com.exsys.mktdata.message;\n");
        writer.println("import java.io.*;");
        writer.println("import java.util.*;");
        writer.println("import java.math.*;");                
        //writer.println("import java.util.*;");
        writer.println("import com.exsys.common.util.*;");
        writer.println("import com.exsys.common.exceptions.*;");
        writer.println();
        writer.println("public class "+name+" extends RLCMessage");
        writer.println("{");

        writer.print("\t");
        writer.println("public "+name+"()");
        writer.println("\t{");
        writer.println("\t\t super(RLCMessageConstants.RLC_MESSAGE_LENGTH_"+msgType+",true);");
        writer.println("\t}");

        writer.print("\t");
		writer.println("public "+name+"(byte[] newBytes) throws RLCProtocolError");        
        writer.println("\t{");
        writer.println("\t\t super(newBytes);");
        writer.println("\t\t if(newBytes.length != RLCMessageConstants.RLC_MESSAGE_LENGTH_"+msgType+")");
        writer.println("\t\t {");        
        writer.println("\t\t\t throw new RLCProtocolError(\"Invalid Message Length\",");
        writer.println("\t\t\t \"MessageLength for "+msgType+" Message = \"+");
        writer.println("\t\t\t RLCMessageConstants.RLC_MESSAGE_LENGTH_"+msgType+");");                
        writer.println("\t\t }");
        
        
        writer.println("\t}");

        writer.print("\t");
		writer.println("public "+name+"(int size, boolean fillWithSpaces)");        
        writer.println("\t{");
        writer.println("\t\tsuper(size,fillWithSpaces);");
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
            int num1 = (Integer.valueOf( st.nextToken())).intValue();
            int num2 = ( Integer.valueOf( st.nextToken())).intValue();
            String type = st.nextToken();
	        String format = null;
 	        System.out.println("Type is " + type);

	       if( type.equals("Date")) format = st.nextToken();


            fieldsInfo.add( new FieldInfo( field,
              num1,num2,type, format ));


            //fields.add("  private "+ type +" m_"+field+";");
            writer.print("\t");
     if( !type.equals("String") )
     {
			if(type.equals("Price"))
	            writer.println ("public void set"+field+"( "+"BigDecimal "+"_"+field+" )");
	        else
		        writer.println ("public void set"+field+"( "+type+" "+"_"+field+" )");
		        
            writer.println("\t{");
    		 if( type.equals("Date"))
      		       writer.println ("\t\twrite( "+num1+", "+" get"+format+"String( _"+field+"));");
     		else if(type.equals("Price"))
		     		writer.println("\t\twrite( "+num1+", "+" priceToString( _"+field+"));");
     		else 
     		 		writer.println("\t\twrite( "+num1+", "+" pad(String.valueOf( _"+field+"),'0',"+((num2-num1)==0?1:(num2-num1+1))+"));");
            writer.println("\t}");
		    writer.print("\t");
     }

     {
     //writer.print("\t");
            writer.println("public void set"+field+"( String "+"_"+field+" )");
            writer.println("\t{");
            writer.println("\t\twrite( "+num1+", "+" _"+field+");");
            writer.println("\t}");

     }

            writer.print("\t");
     if( !type.equals("String") )
     {
			if(type.equals("Price"))
				writer.println("public "+"BigDecimal"+" get"+field+"As"+type+"()");
			else			
       		     writer.println("public "+type+" get"+field+"As"+type+"()");
            
            writer.println("\t{");
		     if( type.equals("Date"))
  		           writer.println("\t\t return ("+format+"StringTo"+type+"(readString( "+num1+","+((num2-num1)==0?1:(num2-num1+1))+" ))) ;");
    		 else
      		       writer.println("\t\t return (stringTo"+type+"(readString( "+num1+","+((num2-num1)==0?1:(num2-num1+1))+" ))) ;");
            writer.println("\t}");

		     writer.print("\t");
            writer.println("public String  get"+field+"()");
            writer.println("\t{");
            writer.println("\t\t return (readString( "+num1+","+((num2-num1)==0?1:(num2-num1+1))+" )) ;");
            writer.println("\t}");

     }
     else
     {
     //writer.print("\t");
            writer.println("public String  get"+field+"()");
            writer.println("\t{");
            writer.println("\t\t return (readString( "+num1+","+((num2-num1)==0?1:(num2-num1+1))+" )) ;");
            writer.println("\t}");
     }


          }
        }

        writer.println();
        writer.println();

		// write tostring method
            writer.println("\tpublic String  toString()");
            writer.println("\t{");
            writer.println("\t\tStringWriter sw = new StringWriter();");
           for( Enumeration e=fieldsInfo.elements(); e.hasMoreElements();)
           {
           	   FieldInfo field = (FieldInfo)e.nextElement();
               String _name = field.name;
               int num1 = field.num1;
               int num2 = field.num2;
               String type = field.type;
               writer.print("\t");
               writer.print("\t");
          
            if(!type.equals("String"))
            {
            	writer.println(" sw.write(\""+_name+"=>\"+ get"+_name+"As"+type+"() +\"<\\n\");" );	
            }     
            //writer.println("sw.write(\""+_name+"=>\"+ readString("+num1+","+((num2-num1)==0?1:(num2-num1))+") +\"<\\n\");" );
            writer.println(" sw.write(\""+_name+"=>\"+ get"+_name+"() +\"<\\n\");" );
               
           }                                   
            
            writer.println("\t\treturn (super.toString() + sw.toString());");
            writer.println("\t}");


           writer.println("}");
           writer.flush();
           writer.close();
           reader.close();
        }
        catch(Exception e )
        {
            System.out.println (e);
            e.printStackTrace();
        }

    }

    public static void main(String[] args)
    {
        RLCMessageGenerator c = new RLCMessageGenerator( args[0], args[1], args[2] );
    }

    class FieldInfo
    {
      public String name;
      public int num1;
      public int num2;
      public String type;
      public String format;

      public FieldInfo( String name,
      int num1,int num2,
                        String type, String format )
      {
          this.name = name;
          this.type = type;
      this.num1 = num1;
       this.num2 = num2;
       this.format = format;
      }

    }
}
