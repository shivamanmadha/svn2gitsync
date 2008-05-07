package com.exsys.fix.specification;

import org.xml.sax.*;

import java.util.Hashtable;
import java.util.ArrayList;
/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 11:10:53 AM)
 * @author: Administrator
 */
public class FixMessageXmlHandler implements DocumentHandler {

	private Hashtable fixMessageMap = null;

	private StringBuffer buffer;
	private int state;
	private FixMessageDef fixMessage = null;
	private FixMessageLineDef fixMessageLine = null;

	private static int COMMENT = 1;
	private static int CUSTOMCOMMENT = 2;
/**
 * FixFieldXmlHandler constructor comment.
 */
public FixMessageXmlHandler(Hashtable newFixMessageMap) {
	super();
	fixMessageMap = newFixMessageMap;

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

	if( name.equals("FixMessageLine"))
	{
		fixMessage.addMessageLine(fixMessageLine);
	}
	else if( name.equals("Comment"))
	{
		fixMessageLine.setDescription(buffer.toString());
	}
	else if( name.equals("CustomComment"))
	{
		fixMessageLine.setCustomDescription(buffer.toString());
	}
	else if( name.equals("FixMessage"))
	{
		fixMessageMap.put( fixMessage.getMessageType(),
						   fixMessage );
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
	if( name.equals("FixMessage"))
	{
		fixMessage = new FixMessageDef();

        String type = attrs.getValue("type");
        fixMessage.setMessageType(type);

        String msgName = attrs.getValue("name");
        fixMessage.setMessageName(msgName);
                        				
	}		
	else if( name.equals("FixMessageLine"))
	{
		fixMessageLine = new FixMessageLineDef();

        String tagNumber = attrs.getValue("tagNumber");
        fixMessageLine.setTagNumber(Integer.parseInt(tagNumber));

        String tagName = attrs.getValue("tagName");
        fixMessageLine.setTagName(tagName);
        
        String required = attrs.getValue("required");
        boolean isRequired = (required == null || !required.equals("No"))?true:false;
        fixMessageLine.setRequired(true);        
        				
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
