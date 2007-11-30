package com.exsys.fix.tools;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.*;
/**
 * Insert the type's description here.
 * Creation date: (8/8/2002 11:53:20 AM)
 * @author: Administrator
 */
public class FixConnectionsManager {
	
	private Hashtable fixServers = null;
	private ArrayList fixServerList = null;
	private ArrayList fixClientList = null;
	private Hashtable fixClients = null;
/**
 * UserManager constructor comment.
 */
public FixConnectionsManager() {
	super();

	
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:55:55 AM)
 * @param user com.exsys.fix.tools.UserDetails
 */
public void addFixClient(FixClientDef client) 
{
	if( fixClients == null )
	{
		fixClients = new Hashtable();

		fixClientList = new ArrayList();		
	}
	
	fixClients.put( client.getKey(), client );
	fixClientList.add( client );
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:55:55 AM)
 * @param user com.exsys.fix.tools.UserDetails
 */
public void addFixServer(FixServerDef server) 
{
	if( fixServers == null )
	{
		fixServers = new Hashtable();
		fixServerList = new ArrayList();
	}
	fixServers.put( server.getKey(), server );
	fixServerList.add( server );
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
	writer.println("Fix Servers");
	
	for( Enumeration e=fixServers.elements(); e.hasMoreElements();)
	{
		FixServerDef server = (FixServerDef)e.nextElement();
		writer.println(server.debugString());
	}

	writer.println("Fix Clients");
	
	for( Enumeration e=fixClients.elements(); e.hasMoreElements();)
	{
		FixClientDef client = (FixClientDef)e.nextElement();
		writer.println(client.debugString());
	}	
		
	
	writer.flush();
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 5:43:17 PM)
 * @return java.util.ArrayList
 */
public ArrayList getFixClientList() {
	return fixClientList;
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 5:42:51 PM)
 * @return java.util.ArrayList
 */
public ArrayList getFixServerList() {
	return fixServerList;
}
}
