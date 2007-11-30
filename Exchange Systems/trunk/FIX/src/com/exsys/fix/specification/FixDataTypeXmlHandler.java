package com.exsys.fix.specification;

import org.xml.sax.*;

import java.util.Hashtable;
import java.util.ArrayList;
/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 11:10:53 AM)
 * @author: Administrator
 */
public class FixDataTypeXmlHandler implements DocumentHandler {

	private Hashtable fixDataTypeMap = null;
	//private ArrayList fixFieldList = null;

	private StringBuffer buffer;
	private int state;
	private FixDataTypeDef fixDataType = null;

	private static int COMMENT = 1;
	private static int CUSTOMCOMMENT = 2;
/**
 * FixFieldXmlHandler constructor comment.
 */
public FixDataTypeXmlHandler(Hashtable newFixDataTypeMap) {
	super();
	fixDataTypeMap = newFixDataTypeMap;
}
/**
 * characters method comment.
 */
public void characters(char[] ch, int start, int len) throws SAXException 
{

	if( state == COMMENT ||
		state == CUSTOMCOMMENT )
	{	
         buffer.append(ch,start,len);
	}
}
/**
 * endDocument method comment.
 */
public void endDocument() throws SAXException 
{
	//System.out.println("Inside End Document");
}
/**
 * endElement method comment.
 */
public void endElement(String name) throws SAXException 
{
	//System.out.println("End Element -- " + name );	

	if( name.equals("FixDataType"))
	{
		fixDataTypeMap.put(fixDataType.getDataType(), fixDataType );
	}
	else if( name.equals("Comment"))
	{
		fixDataType.setDescription(buffer.toString());
	}
	else if( name.equals("CustomComment"))
	{
		fixDataType.setCustomDescription(buffer.toString());
	}
}
/**
 * ignorableWhitespace method comment.
 */
public void ignorableWhitespace(char[] arg1, int arg2, int arg3) throws SAXException {}
/**
 * processingInstruction method comment.
 */
public void processingInstruction(String arg1, String arg2) throws SAXException {}
/**
 * setDocumentLocator method comment.
 */
public void setDocumentLocator(Locator arg1) {}
/**
 * startDocument method comment.
 */
public void startDocument() throws SAXException 
{
	//System.out.println("Inside Start Document");
}
/**
 * startElement method comment.
 */
public void startElement(String name, AttributeList attrs) throws SAXException
{
	//System.out.println("Start Element -- " + name );
	if( name.equals("FixDataType"))
	{
		fixDataType = new FixDataTypeDef();

        String dataType = attrs.getValue("dataType");
        fixDataType.setDataType(dataType);        		
		
	}
	else if( name.equals("Comment"))
	{
		state = COMMENT;
		buffer = new StringBuffer();
	}
	else if( name.equals("CustomComment"))
	{
		state = CUSTOMCOMMENT;
		buffer = new StringBuffer();
	}
	
	
		
}
}
