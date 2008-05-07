package com.exsys.fix.specification;

import org.xml.sax.*;

import org.apache.xerces.parsers.SAXParser;


import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 7:18:45 AM)
 * @author: Administrator
 */
public class FixSpecification {
	
	private Hashtable fieldDefMap = null;
	private ArrayList fieldDefList = null;
		
	private Hashtable dataTypesDefMap = null;
	
	private Hashtable fixMessageDefMap = null;
	
	private static FixSpecification spec = null;


    	
/**
 * FixSpecification constructor comment.
 */
private FixSpecification() 
{
	super();
	initialize();
}
/**
 * FixSpecification constructor comment.
 */
private FixSpecification(String specsDir) 
{
	super();
	initialize(specsDir);
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 8:23:59 AM)
 */
public void debugPrint()
{
	debugPrint(new PrintWriter(System.out));
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 8:25:36 AM)
 * @param writer java.io.PrintWriter
 */
public void debugPrint(PrintWriter writer)
{
	// print header fields
	writer.println("FixFields");
	/*
	for( Enumeration e=fieldDefMap.elements(); e.hasMoreElements();)
	{
		FixFieldDef field = (FixFieldDef)e.nextElement();
		writer.println(field.toString());
	}
	*/
	for( int i=0; i<fieldDefList.size(); i++)
	{
		FixFieldDef field = (FixFieldDef)fieldDefList.get(i);
		writer.println(field.toString());
	}
	
	writer.println("FixDataTypes");
	
	for( Enumeration e=dataTypesDefMap.elements(); e.hasMoreElements();)
	{
		FixDataTypeDef fd = (FixDataTypeDef)e.nextElement();
		writer.println(fd.toString());
	}

	writer.println("FixMessages");
	for( Enumeration e=fixMessageDefMap.elements(); e.hasMoreElements();)
	{
		FixMessageDef message = (FixMessageDef)e.nextElement();
		writer.println(message.toString());
	}
	

	
	writer.flush();
}
/**
 * Insert the method's description here.
 * Creation date: (9/16/2002 7:29:04 AM)
 * @return com.exsys.fix.specification.FixDataTypeDef
 * @param dataType java.lang.String
 */
public FixDataTypeDef getFixDataTypeInfo(String dataType) 
{
	return ( (FixDataTypeDef)dataTypesDefMap.get(dataType));
}
/**
 * Insert the method's description here.
 * Creation date: (9/16/2002 7:22:36 AM)
 * @return com.exsys.fix.specification.FixFieldDef
 * @param tagNumber int
 */
public FixFieldDef getFixFieldInfo(int tagNumber)
{
	return ( (FixFieldDef)fieldDefMap.get(new Integer(tagNumber)) );
}
/**
 * Insert the method's description here.
 * Creation date: (9/16/2002 7:32:43 AM)
 * @return com.exsys.fix.specification.FixMessageDef
 * @param messageType java.lang.String
 */
public FixMessageDef getFixMessageInfo(String messageType) 
{
	return ( (FixMessageDef)fixMessageDefMap.get(messageType));
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 10:50:07 AM)
 * @return com.exsys.fix.specification.FixSpecification
 */
synchronized static public  FixSpecification getSpecification() 
{
	if( spec == null )
	{
		// initialize the specification
		spec = new FixSpecification();
	}
	
	return spec;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 10:50:07 AM)
 * @return com.exsys.fix.specification.FixSpecification
 */
synchronized static public  FixSpecification getSpecification(String specsDir) 
{
	if( spec == null )
	{
		// initialize the specification
		spec = new FixSpecification(specsDir);
	}
	
	return spec;
}
private void initialize() 
{
	 initialize("c:\\fixtest\\config\\");
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 10:57:22 AM)
 */
private void initialize(String specsDir) 
{
	fieldDefMap = new Hashtable();
	fieldDefList = new ArrayList();
	
	
	dataTypesDefMap = new Hashtable();
	
	fixMessageDefMap = new Hashtable();
	
	// instantiate FixFieldXmlHandler
	SAXParser parser = new SAXParser();
	FixFieldXmlHandler fixFieldXmlHandler = new FixFieldXmlHandler(fieldDefMap, fieldDefList);
	parser.setDocumentHandler(fixFieldXmlHandler);
	//parser.setErrorHandler( new FixSpecification.MyErrorHanler());


	try
	{
		//System.out.println("Here");
		//InputStream is = new FileInputStream("c:\\fixtest\\config\\fixfields.xml");
		//System.out.println("Here");		
		FileReader fr = new FileReader(specsDir+"fixfields.xml");
		if( fr == null ) System.out.println("NULL");
		//System.out.println("Here");		
		parser.parse( new InputSource(fr));
		//System.out.println("Here");		
	}
	catch(SAXException e )
	{
		System.out.println("Sax Exception");
		System.out.println(e.getMessage());
	}
	catch( Exception e )
	{
		System.out.println("Exception");		
		e.printStackTrace();
	}
	

	FixDataTypeXmlHandler fixDataTypeXmlHandler = new FixDataTypeXmlHandler(dataTypesDefMap);
	parser.setDocumentHandler(fixDataTypeXmlHandler);
	//parser.setErrorHandler( new FixSpecification.MyErrorHanler());


	try
	{
		//System.out.println("Here");
		//InputStream is = new FileInputStream("c:\\fixtest\\config\\fixfields.xml");
		//System.out.println("Here");		
		FileReader fr = new FileReader(specsDir+"fixdatatypes.xml");
		if( fr == null ) System.out.println("NULL");
		//System.out.println("Here");		
		parser.parse( new InputSource(fr));
		//System.out.println("Here");		
	}
	catch(SAXException e )
	{
		System.out.println("Sax Exception");
		System.out.println(e.getMessage());
	}
	catch( Exception e )
	{
		System.out.println("Exception");		
		e.printStackTrace();
	}


	FixMessageXmlHandler fixMessageXmlHandler = new FixMessageXmlHandler(fixMessageDefMap);
	parser.setDocumentHandler(fixMessageXmlHandler);
	//parser.setErrorHandler( new FixSpecification.MyErrorHanler());


	try
	{
		//System.out.println("Here");
		//InputStream is = new FileInputStream("c:\\fixtest\\config\\fixfields.xml");
		//System.out.println("Here");		
		FileReader fr = new FileReader(specsDir+"fixmessages.xml");
		if( fr == null ) System.out.println("NULL");
		//System.out.println("Here");		
		parser.parse( new InputSource(fr));
		//System.out.println("Here");		
	}
	catch(SAXException e )
	{
		System.out.println("Sax Exception");
		System.out.println(e.getMessage());
	}
	catch( Exception e )
	{
		System.out.println("Exception");		
		e.printStackTrace();
	}	
		
	
}
}
