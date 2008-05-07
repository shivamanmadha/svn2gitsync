package com.exsys.fix.specification;

import java.util.Hashtable;
import java.util.ArrayList;
/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 10:01:27 AM)
 * @author: Administrator
 */ 
public class FixMessageDef {
	Hashtable messageLinesMap = new Hashtable();
	ArrayList messageLinesList = new ArrayList();
	
	private java.lang.String messageType;
	private java.lang.String messageName;
/**
 * FixMessageDef constructor comment.
 */
public FixMessageDef() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 10:04:35 AM)
 * @param line com.exsys.fix.specification.FixMessageLineDef
 */
public void addMessageLine(FixMessageLineDef line) 
{
	messageLinesMap.put( new Integer(line.getTagNumber()), line );
	messageLinesList.add( line );
}
/**
 * Insert the method's description here.
 * Creation date: (9/16/2002 7:38:47 AM)
 * @return com.exsys.fix.specification.FixMessageLineDef
 * @param tagNumber int
 */
public FixMessageLineDef getFixMessageLineInfo(int tagNumber) 
{
	return ( (FixMessageLineDef)messageLinesMap.get(new Integer(tagNumber)));
}
/**
 * Insert the method's description here.
 * Creation date: (8/6/2002 7:59:19 AM)
 * @return java.lang.String
 */
public java.lang.String getMessageName() {
	return messageName;
}
/**
 * Insert the method's description here.
 * Creation date: (8/6/2002 7:17:07 AM)
 * @return java.lang.String
 */
public java.lang.String getMessageType() {
	return messageType;
}
/**
 * Insert the method's description here.
 * Creation date: (8/6/2002 7:59:19 AM)
 * @param newMessageName java.lang.String
 */
public void setMessageName(java.lang.String newMessageName) {
	messageName = newMessageName;
}
/**
 * Insert the method's description here.
 * Creation date: (8/6/2002 7:17:07 AM)
 * @param newMessageType java.lang.String
 */
public void setMessageType(java.lang.String newMessageType) {
	messageType = newMessageType;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/2002 1:49:22 PM)
 * @return java.lang.String
 */
public String toString() {
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("Message Type: " + messageType );
	returnString.append("\n");

	returnString.append("Message Name: " + messageName );
	returnString.append("\n");


    if( !messageLinesMap.isEmpty())
    {
		//returnString.append("Valid Values: " + tagNumber );
		/*
		for( Enumeration e=validValues.elements(); e.hasMoreElements();)
		{
			FixFieldValue fieldValue = (FixFieldValue)e.nextElement();
			returnString.append(fieldValue.toString());
		}
		*/
		for( int i=0; i<messageLinesList.size();i++)
		{
			FixMessageLineDef line = (FixMessageLineDef)messageLinesList.get(i);
			returnString.append(line.toString());
		}		
		
    }
	returnString.append("\n");
	

	
		
	
	return returnString.toString();
}
}
