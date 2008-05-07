package com.exsys.fix.message;

import java.io.*;
import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (11/18/01 7:00:13 PM)
 * @author: Administrator
 */
public class NativeFixTranslator implements FixTranslator 
{
	protected Writer writer;
/**
 * Insert the method's description here.
 * Creation date: (11/18/01 7:02:00 PM)
 * @param pw java.io.PrintWriter
 */
public NativeFixTranslator(Writer pw) 
{
	writer = pw;
}
/**
 * translate method comment.
 */
public void translate(FixMessage msg) throws IOException 
{
	translateHeader(msg, false);
	translateBody(msg);	
}
/**
 * translate method comment.
 */
public void translateBody(FixMessage msg,boolean ignoreCheckSum) throws IOException
{
	boolean hasRG = msg.hasRepeatingGroupFields();
	
	// first get body field map
	TreeMap bodyMap = msg.getBodyFieldMap();
	for( Iterator e=bodyMap.values().iterator(); e.hasNext();)
	{
		FixField field = (FixField)e.next();
		if(!field.getTagNumberAsString().equals(FixConstants.FIX_TRAILER_CHECKSUM_TAG))
		{		
			if(hasRG 
			    && msg.isFieldRepeatingGroup(field.getTagNumberAsString())
			    && !field.getTagValue().equals("0"))
			{
				write(field);
				writer.write(FixConstants.FIX_FIELD_DELIMITER);
				// need to handle repeating groups
				addRepeatingGroups(field.getTagNumberAsString(),msg);
			}
			else
			{
				write(field);
				writer.write(FixConstants.FIX_FIELD_DELIMITER);				
			}
		}
	}

}

private void addRepeatingGroups(String key, FixMessage msg) throws IOException
{	
	ArrayList rgArray = msg.getRepeatingGroups(key);
	//System.out.println("# of groups for key - " + key + " is " + rgArray.size());
	String firstField = FixMessageFactory.getRGFirstField(key+"_"+msg.getMsgType());
	//System.out.println("First field is " + firstField);
	Integer firstFieldKey = Integer.valueOf(firstField);
	if( rgArray != null )
	{
			int size = rgArray.size();
			for (int i = 0; i < size; i++) {
				FixRepeatedGroup rg = (FixRepeatedGroup)rgArray.get(i);
				// first get body field map
				TreeMap bodyMap = rg.getBodyFieldMap();
				// For repeating groups, first tag must be written first
				
				FixField field = (FixField)bodyMap.get(firstFieldKey);
				//System.out.println("Writing - "+field.getTagNumber()+"="+field.getTagValue());
				
				write(field);
				writer.write(FixConstants.FIX_FIELD_DELIMITER);
				
				for( Iterator e=bodyMap.values().iterator(); e.hasNext();)
				{
					// need to skip the first tag
					FixField field1 = (FixField)e.next();
					if(!field1.getTagNumberAsString().equals(firstField))
					{
						//System.out.println("Writing - "+field1.getTagNumber()+"="+field1.getTagValue());
						write(field1);
						writer.write(FixConstants.FIX_FIELD_DELIMITER);
					}
				}				
				
			}		
	}	

}




/**
 * translate method comment.
 */
public void translateBody(FixMessage msg) throws IOException
{
	boolean hasRG = msg.hasRepeatingGroupFields();
	
	// first get body field map
	TreeMap bodyMap = msg.getBodyFieldMap();
	for( Iterator e=bodyMap.values().iterator(); e.hasNext();)
	{
		FixField field = (FixField)e.next();
			if(hasRG 
			    && msg.isFieldRepeatingGroup(field.getTagNumberAsString())
			    && !field.getTagValue().equals("0"))
			{
				write(field);
				writer.write(FixConstants.FIX_FIELD_DELIMITER);
				// need to handle repeating groups
				addRepeatingGroups(field.getTagNumberAsString(),msg);
			}
			else
			{
				write(field);
				writer.write(FixConstants.FIX_FIELD_DELIMITER);				
			}
	}

}
/**
 * Insert the method's description here.
 * Creation date: (11/18/01 7:28:10 PM)
 * @param field com.exsys.fix.message.FixField
 */
public void write(FixField field) throws IOException 
{
	writer.write(field.getTagNumberAsString() +
			 FixConstants.FIX_TAG_VALUE_SEPARATOR +
			 field.getTagValue() );
}

/**
 * translate method comment.
 */
public void translateHeader(FixMessage msg, boolean includeMsgType) throws IOException 
{
	// first get body field map
	TreeMap headerMap = msg.getHeaderMap();
	// This should ignore MSGTYPE field, since it is already
	// taken care of in prepareFixBuffer method
	for( Iterator e=headerMap.values().iterator(); e.hasNext();)
	{
		FixField field = (FixField)e.next();
		if(!field.getTagNumberAsString().equals(FixConstants.FIX_HEADER_MSGTYPE_TAG) || includeMsgType )
		{
			write(field);
			writer.write(FixConstants.FIX_FIELD_DELIMITER);
		}
	}

}

public void translateResendHeader(FixMessage msg) throws IOException 
{
	// first get body field map
	TreeMap headerMap = msg.getHeaderMap();
	// This should ignore MSGTYPE field, since it is already
	// taken care of in prepareFixBuffer method
	for( Iterator e=headerMap.values().iterator(); e.hasNext();)
	{
		FixField field = (FixField)e.next();
		if(!field.getTagNumberAsString().equals(FixConstants.FIX_HEADER_MSGTYPE_TAG) 
		&& !field.getTagNumberAsString().equals(FixConstants.FIX_HEADER_BEGINSTRING_TAG) 
		&& !field.getTagNumberAsString().equals(FixConstants.FIX_HEADER_BODYLENGTH_TAG) )
		{
			write(field);
			writer.write(FixConstants.FIX_FIELD_DELIMITER);
		}
	}

}
/**
 * translate method comment.
 */
public void translateIncludingMsgType(FixMessage msg) throws IOException 
{
	//translateHeader(msg, true);
	//translateBody(msg);	
	translateIncludingMsgType(msg,false);
}
public void translateIncludingMsgType(FixMessage msg,boolean ignoreChecksum) throws IOException 
{
	translateHeader(msg, true);
	if(ignoreChecksum)
	{
		translateBody(msg,true);
	}
	else
	{
		translateBody(msg);
	}	
}

}
