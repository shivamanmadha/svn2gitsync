package com.exsys.fix.tools;

import java.io.IOException;
import java.util.*;

import com.exsys.fix.message.*;
import com.exsys.fix.specification.*;

public class LogMessageHelper  {
    

    public  LogMessageHelper(String specsDir,String str)
    {
        	if ( str != null && str.length() >0)
        	{

             	if(isValidFixLine(str))
             	{
                	constructTable(specsDir,str);
           		}
           		else
           		{
                	System.out.println("Not Valid Fix String");
           		}
			}
			else
			{
				System.out.println("Not Valid Fix String");
			}

	}



 public static void main(String []args)
 {

		java.awt.datatransfer.Transferable t = java.awt.Toolkit.getDefaultToolkit()
							.getSystemClipboard()
							.getContents(new Object());
		if(t != null)
		{
			try
			{
				String str = (String)t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
				System.out.println("Input FIX String is - "+ str);
				
				LogMessageHelper lmv = null;
				if(args.length != 0) 
					lmv = new LogMessageHelper(args[0],str);
				else
					lmv = new LogMessageHelper(null,str);
				System.exit(0);
		    }
		    catch(Exception e)
		    {
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("No data selected");
		}

  }
  private boolean isValidFixLine(String line)
  {

  	int index = line.indexOf("8=FIX");
  	if(index != -1)
  	return true;
  	else
  	return false;

  }
  private void constructTable(String specsDir,String msg){

  try{
        FixSpecification spec = null;
        if(specsDir == null)
        	spec = FixSpecification.getSpecification();
        else
	        spec = FixSpecification.getSpecification(specsDir);
	        
		FixMessage fm = FixMessageFactory.createFixMessageWithoutValidation(msg.getBytes());
		//FixMessage fm = FixMessageFactory.createFixMessage(msg.getBytes(),"ICE",false);
		String fixMessageType = fm.getMessageType();
		FixMessageDef messagedef = spec.getFixMessageInfo(fixMessageType);
		String fixMessageName = messagedef.getMessageName();
		System.out.println("Fix Message Type = " + fixMessageType);
		System.out.println("Fix Message Name = " + fixMessageName);
		// first get header field map
		TreeMap headerMap = fm.getHeaderMap();
		System.out.println("\n--------------");
		System.out.println("Header Fields:");
		System.out.println("--------------");

		System.out.println( append("Tag Name",' ',30)
			+append("Tag Number",' ',15)
			+"Tag Value");
		System.out.println( append("--",'-',30)
				+append("--",'-',15)
				+append("--",'-',20));	
		for( Iterator enum1=headerMap.values().iterator(); enum1.hasNext();)
		{
			FixField field = (FixField)enum1.next();
			FixFieldDef fielddef =spec.getFixFieldInfo(Integer.parseInt(field.getTagNumberAsString()));
			
			System.out.println((fielddef == null? append("unknown",' ',50): 
							append(fielddef.getTagName(),' ',30))
							+append(field.getTagNumberAsString(),' ',15)
							+field.getTagValue());
		}
		System.out.println( append("--",'-',30)
				+append("--",'-',15)
				+append("--",'-',20));				
		

		// printing body fields
		TreeMap bodyMap = fm.getBodyFieldMap();
		System.out.println("\n-----------");
		System.out.println("Body Fields:");
		System.out.println("-----------");
		boolean hasRG = fm.hasRepeatingGroupFields();
		if(hasRG)
		{
			System.out.println("Message has repeating groups");
		}
		else
		{
			System.out.println("Message has NO repeating groups");
		}
		System.out.println( append("Tag Name",' ',30)
				+append("Tag Number",' ',15)
				+"Tag Value");
		System.out.println( append("--",'-',30)
				+append("--",'-',15)
				+append("--",'-',20));		
		for( Iterator enum1=bodyMap.values().iterator(); enum1.hasNext();)
		{
			Vector rowData = new Vector();
			FixField field = (FixField)enum1.next();
			FixFieldDef fielddef =spec.getFixFieldInfo(Integer.parseInt(field.getTagNumberAsString()));

			System.out.println((fielddef == null? append("unknown",' ',50): 
				append(fielddef.getTagName(),' ',30))
				+append(field.getTagNumberAsString(),' ',15)
				+field.getTagValue());
			
				if(hasRG 
				    && fm.isFieldRepeatingGroup(field.getTagNumberAsString())
				    && !field.getTagValue().equals("0"))
				{
					displayRepeatingGroups(spec,field.getTagNumberAsString(),fm);
				}
			
		}
		System.out.println( append("--",'-',30)
				+append("--",'-',15)
				+append("--",'-',20));	

	}
	catch( Exception ex )
    {
		ex.printStackTrace();
    }
  }
  private void displayRepeatingGroups(FixSpecification spec,String key, FixMessage msg) throws IOException
  {	
  	ArrayList rgArray = msg.getRepeatingGroups(key);
  	if( rgArray != null )
  	{
  			int size = rgArray.size();
  			for (int i = 0; i < size; i++) {
  				System.out.println("\n-----------");
  				System.out.println("Repeating Group Fields:");
  				System.out.println("-----------");  			
  				
  				FixRepeatedGroup rg = (FixRepeatedGroup)rgArray.get(i);
  				// first get body field map
  				TreeMap bodyMap = rg.getBodyFieldMap();
  				for( Iterator e=bodyMap.values().iterator(); e.hasNext();)
  				{
  					FixField field = (FixField)e.next();
  					FixFieldDef fielddef =spec.getFixFieldInfo(Integer.parseInt(field.getTagNumberAsString()));

  					System.out.println((fielddef == null? append("unknown",' ',50): 
  						append(fielddef.getTagName(),' ',30))
  						+append(field.getTagNumberAsString(),' ',15)
  						+field.getTagValue());
  				}				
  				
  				System.out.println( append("--",'-',30)
  						+append("--",'-',15)
  						+append("--",'-',20));	  				
  			}		
  	}	

  }
  private static String append(String value, char pad, int width) {
	int length = value.length();
	if (length < width) {
		StringBuffer buffer = new StringBuffer(value);
		while (length < width) {
			buffer.append(pad);
			length++;
		}
		return buffer.toString();
	} else {
		return value;
	}
}
}
