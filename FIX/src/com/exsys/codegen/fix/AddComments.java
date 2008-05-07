package com.exsys.codegen.fix;

import java.io.*;
import java.util.*;

public class AddComments
{

    public AddComments(String file, String name, String type )
    {

      try
      {
        FileInputStream is = new FileInputStream( file );
        FileOutputStream os = new FileOutputStream( name+"_1.java");
        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        PrintWriter writer = new PrintWriter( new OutputStreamWriter( os ) );


        //writer.println( "package com.exapi.common.beans;\n");
        //writer.println("import java.io.*;");
        //writer.println("import java.util.*;");
        //writer.println("import com.exsys.common.fixmessage.*;");
        //writer.println();
        //writer.println("public class "+name+" extends FixMessage");
        //writer.println("{");

        //writer.print("\t");
        //writer.println("public "+name+"()");
        //writer.println("\t{");
        //writer.println("\t}");

        //Vector fields = new Vector();
        //Vector fieldsInfo = new Vector();

        String line;
        while ( (line = reader.readLine()) != null)
        {
        	if((line.indexOf("public") != -1
        	    || line.indexOf("private") != -1
        	    || line.indexOf("protected") != -1) && line.indexOf("=") != -1)
        	{
        		writer.println(line);
        	    continue;
        	}
        	
        	if(line.indexOf("public") != -1 && line.indexOf("class") != -1)
        	{
        		System.out.println("Line is "+line);
        		System.out.println("Class description");
				writer.println("/**");
 				writer.println("* This class is used to represent the " + type + " message "+ ((type.equals("FIX")?name.substring(3):name)));
 				writer.println("*"); 
 		 		writer.println("*/");         		
        	}
        	else if(line.indexOf("public") != -1 ||
        			line.indexOf("protected") != -1 ||
        			line.indexOf("private") != -1 )
        	{
        		System.out.println("Line is "+line);
        		int index = 0;
        		index = line.indexOf("public");
        		if(index == -1)
        		{
        			index = line.indexOf("protected");
	        		if(index == -1)
 		       		{
   		     			index = line.indexOf("private");
     		   		}
        			
        		}
        		
        		if(line.indexOf(name) != -1 )
        		{
        			System.out.println("Constructor");
				writer.println("\t/**");
 				writer.println("\t* Constructor to construct " + name + " object ");
 				//String paramString = line.substring
 				writer.println("\t*");
 		 		writer.println("\t*/");         			
        		}
        		else
        		{
        			System.out.println("Line is " + line.substring(index));
        			
        			boolean addIndex = false;
        			boolean isGet = true;
        			boolean nonGetOrSet = false;
        			String methodName = null;
        			String desc = null;
        			String returnType = null;
        			String parmType = null;
        			String parm = null;
        			
        			String line1 = line.substring(index);
        			StringTokenizer st = new StringTokenizer(line1, " ");
		    		for(int i = 0; st.hasMoreTokens();i++ )
    				{

  			          //String type = st.nextToken();
  			          //String returnType = st.nextToken();
  			          String token = st.nextToken();
  			          System.out.println("i="+i+"Token = "+token);
  			          if(i==1 )
  			          {
  			          	returnType = token;
  			            if(token.equals("void"))
  			            {
  			          		isGet = false;
  			            }
  			            if(token.equals("static"))
  			            {
  			            	i=i-1;
  			            	continue;
  			            }
  			          	System.out.println("Getter Method");
  			          }
  			          
  			          
  			          if(i==2)
  			          {
  			          	 if(isGet)
  			          	 {
  			          	 	methodName = token.substring(0,token.indexOf("("));
  			          	 	if(!methodName.substring(0,3).equals("get"))
  			          	 	{
  			          	 		
  			          	 		parmType = token.substring(token.indexOf("(")+1);
  			          	 		nonGetOrSet = true;
  			          	 	}
  			          	 	else
  			          	 	{  		
  			          	 		parmType = token.substring(token.indexOf("(")+1);
  			          	 		if(parmType.equals(")"))
  			          	 		{
  			          	 			parmType = null;
  			          	 		}
  			          	 	}
  			          	 	
  			          	 }
  			          	 else
  			          	 {
  			          	 	methodName = token.substring(0,token.indexOf("("));
  			          	 	parmType = token.substring(token.indexOf("(")+1);
  			          	 }
  			          	 if(nonGetOrSet)
  			          	 {
  			          	 	desc = methodName;
  			          	 }
  			          	 else
  			          	 {
  			          	 	desc = methodName.substring(3);
  			          	 }
  			          	 //methodName = token;
  			          }
  			          
  			          if(i==3 && (isGet || !isGet || ( nonGetOrSet && !parmType.equals(")"))))
  			          {
  			          	if(!token.trim().equals("{"))
  			          	{
  			          	if(token.charAt(0) == '_')
  			          	{
  			          		if(token.indexOf(")") != -1)
  			          		{
  			          			parm = token.substring(1,token.indexOf(")"));
  			          		}
  			          		else
  			          		{
  			          			parm = token.substring(1,token.indexOf(","));
  			          			addIndex = true;
  			          		}
  			          	}
  			          	else
  			          	{
  			          		if(token.indexOf(")") != -1)
  			          		{
  			          			parm = token.substring(0,token.indexOf(")"));
  			          		}
  			          		else
  			          		{
  			          			parm = token.substring(0,token.indexOf(","));
  			          			addIndex = true;
  			          		}
  			          	}
  			          	}
  			          }
  			          
  			          
    				}
    				
    				
        			System.out.println("method");
        			System.out.println("returnType = " + returnType);
        			System.out.println("methodName = " + methodName);
        			System.out.println("desc = " + desc);
        			System.out.println("parmType = " + parmType);
        			System.out.println("parm = " + parm);
        			if(nonGetOrSet)
        			{
				writer.println("\t/**");
 				writer.println("\t* method  " + desc );
 				writer.println("\t*"); 
 				if(parm != null)
 				{
 					writer.println("\t* @param "+ parmType + " - " + parm);
 		 			writer.println("\t*"); 				
 				}
 				writer.println("\t* @return "+ returnType + " - " + desc);
 		 		writer.println("\t*/");         				
        				
        			}        			
        			else if(isGet )
        			{
				writer.println("\t/**");
 				writer.println("\t* getter method to get " + desc );
 				writer.println("\t*"); 
 				if(parm != null)
 				{
 					writer.println("\t* @param "+ parmType + " - " + parm);
 		 			writer.println("\t*"); 				
 				} 				
 				writer.println("\t* @return "+ returnType + " - " + desc);
 		 		writer.println("\t*/");         				
        			}
        			else
        			{
				writer.println("\t/**");
 				writer.println("\t* setter method to set " + desc );
 				writer.println("\t*"); 
 				writer.println("\t* @param "+ parmType + " - " + parm);
 		 		writer.println("\t*");
 		 		if(addIndex)
 		 		{
 				writer.println("\t* @param "+ "int" + " - " + "index");
 		 		writer.println("\t*/");
 		 		}
 		 		else
 		 		{
 		 			writer.println("\t*/");	
 		 		}
 		 		
        			}
        			
        		}
        		
        		
        	}
        	
        	writer.println(line);
        }
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
        AddComments c = new AddComments( args[0], args[1], args[2] );
    }
}
