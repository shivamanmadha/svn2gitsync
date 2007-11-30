package com.exsys.fix.tools;

import org.xml.sax.*;

import java.util.Hashtable;
import java.util.ArrayList;
/**
 * Insert the type's description here.
 * Creation date: (8/4/2002 11:10:53 AM)
 * @author: Administrator
 */
public class FixTestBenchXmlHandler implements DocumentHandler {

	private FixConnectionsManager fcManager = null;
	private TestOrderDef testOrder = null;
	

	//private StringBuffer buffer;
	//private int state;
	private FixServerDef server = null;
	private FixClientDef client = null;


/**
 * FixFieldXmlHandler constructor comment.
 */
public FixTestBenchXmlHandler(FixConnectionsManager newManager,
						  TestOrderDef newOrderDef )
{
	super();
	fcManager = newManager;
	testOrder = newOrderDef;

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

	if( name.equals("FixServer"))
	{
		fcManager.addFixServer(server);
	}
	else if( name.equals("FixClient"))
	{
		fcManager.addFixClient(client);       		
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
	System.out.println("Inside Start Document");
}
/**
 * startElement method comment.
 */
public void startElement(String name, AttributeList attrs) throws SAXException
{
	System.out.println("Start Element -- " + name );
	if( name.equals("FixServer"))
	{
		server = new FixServerDef();

        String primaryHost = attrs.getValue("primaryHost");
        server.setPrimaryHost(primaryHost);
        String primaryPort = attrs.getValue("primaryPort");
        server.setPrimaryPort(primaryPort);
        
        String secondaryHost = attrs.getValue("secondaryHost");
        server.setSecondaryHost(secondaryHost);
        String secondaryPort = attrs.getValue("secondaryPort");
        server.setSecondaryPort(secondaryPort);
		
	}
	else if( name.equals("FixClient"))
	{
		client = new FixClientDef();

        String senderCompID = attrs.getValue("senderCompID");
        client.setSenderCompID(senderCompID);
        String senderSubID = attrs.getValue("senderSubID");
        client.setSenderSubID(senderSubID);


        String targetCompID = attrs.getValue("targetCompID");
        client.setTargetCompID(targetCompID);
        String targetSubID = attrs.getValue("targetSubID");
        client.setTargetSubID(targetSubID);

        String userid = attrs.getValue("userid");
        client.setUserID(userid);
        String password = attrs.getValue("password");
        client.setPassword(password);          
        
		
	}
	else if( name.equals("TestOrder"))
	{
		//testOrder = new TestOrderDef();

        String buyOrSell = attrs.getValue("buyOrSell");
        testOrder.setBuyOrSell(buyOrSell);

        String orderType = attrs.getValue("orderType");
        testOrder.setOrderType(orderType);

        String timeInForce = attrs.getValue("timeInForce");
        testOrder.setTimeInForce(timeInForce);

        String symbol = attrs.getValue("symbol");
        testOrder.setSymbol(symbol);

        String price = attrs.getValue("price");
        testOrder.setPrice(price);
        
         
		
	}
	
	
	
	
		
}
}
