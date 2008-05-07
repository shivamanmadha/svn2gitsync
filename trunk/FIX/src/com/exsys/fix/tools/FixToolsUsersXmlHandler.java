package com.exsys.fix.tools;

import org.xml.sax.*;

import java.util.Hashtable;
import java.util.ArrayList;
/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 11:10:53 AM)
 * @author: Administrator
 */
public class FixToolsUsersXmlHandler implements DocumentHandler {

	private UserManager userManager = null;
	

	//private StringBuffer buffer;
	//private int state;
	private UserDetails user = null;

/**
 * FixFieldXmlHandler constructor comment.
 */
public FixToolsUsersXmlHandler(UserManager newManager)
{
	super();
	userManager = newManager;

}
/**
 * characters method comment.
 */
public void characters(char[] ch, int start, int len) throws SAXException 
{
	/*

	if( state == COMMENT ||
		state == CUSTOMCOMMENT )
	{	
         buffer.append(ch,start,len);
	}
	*/
}
/**
 * endDocument method comment.
 */
public void endDocument() throws SAXException 
{
	System.out.println("Inside End Document");
}
/**
 * endElement method comment.
 */
public void endElement(String name) throws SAXException 
{
	System.out.println("End Element -- " + name );	

	if( name.equals("User"))
	{
		userManager.addUser(user);
	}
	/*
	else if( name.equals("ordertype"))
	{
		otList.add(otDef);       		
	}
	else if( name.equals("tif"))
	{
		tifList.add(tifDef);       		
	}	
	
	else if( name.equals("Comment"))
	{
		fixField.setDescription(buffer.toString());
	}
	else if( name.equals("CustomComment"))
	{
		fixField.setCustomDescription(buffer.toString());
	}
	*/
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
	System.out.println("Inside Start Document");
}
/**
 * startElement method comment.
 */
public void startElement(String name, AttributeList attrs) throws SAXException
{
	System.out.println("Start Element -- " + name );
	if( name.equals("User"))
	{
		user = new UserDetails();

        String id = attrs.getValue("id");
        user.setUserID(id);

        String uname = attrs.getValue("name");
        user.setUserName(uname);
        
        String password = attrs.getValue("password");
		user.setPassword(password);        		
		
	}
	/*
	else if( name.equals("ordertype"))
	{
		otDef = new OrderTypeDef();

        String value = attrs.getValue("value");
        otDef.setValue(value);

        String desc = attrs.getValue("description");
        otDef.setDescription(desc);
        
        String fv = attrs.getValue("fixvalue");
        otDef.setFixValue(fv);         
		
	}
	else if( name.equals("tif"))
	{
		tifDef = new TimeInForceDef();

        String value = attrs.getValue("value");
        tifDef.setValue(value);

        String desc = attrs.getValue("description");
        tifDef.setDescription(desc);
        
        String fv = attrs.getValue("fixvalue");
        tifDef.setFixValue(fv);         
		
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
	else if( name.equals("ValidValue"))
	{
        String value = attrs.getValue("value");
        String description = attrs.getValue("description");
        String supported = attrs.getValue("supported");
        boolean isSupported = (supported == null || !supported.equals("No"))?true:false;

        fixField.addAllowedValue(value,description,isSupported);		
		
	}
	*/
	
	
		
}
}
