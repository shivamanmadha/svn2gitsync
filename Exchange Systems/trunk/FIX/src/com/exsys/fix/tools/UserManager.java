package com.exsys.fix.tools;

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;
/**
 * Insert the type's description here.
 * Creation date: (8/8/2002 11:53:20 AM)
 * @author: Administrator
 */
public class UserManager {

	private static UserManager instance = null;
	private Hashtable validUsers = null;
/**
 * UserManager constructor comment.
 */
private UserManager() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:55:55 AM)
 * @param user com.exsys.fix.tools.UserDetails
 */
public void addUser(UserDetails user) 
{
	if( validUsers == null )
	{
		validUsers = new Hashtable();
	}
	validUsers.put( user.getUserID(), user );
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
	writer.println("Users");
	
	for( Enumeration e=validUsers.elements(); e.hasMoreElements();)
	{
		UserDetails user = (UserDetails)e.nextElement();
		writer.println(user.toString());
	}
		
	
	writer.flush();
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 12:13:18 PM)
 * @return com.exsys.fix.tools.UserManager
 */
synchronized static public  UserManager getInstance() {
	if( instance == null )
	{
		instance = new UserManager();
	}
	
	return instance;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:56:59 AM)
 * @return boolean
 * @param userID java.lang.String
 */
public boolean isUserValid(String userID)
{

	return( validUsers.containsKey(userID) );
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:56:59 AM)
 * @return boolean
 * @param userID java.lang.String
 */
public boolean isUserValid(String userID, String password)
{
	boolean valid = false;
	if( validUsers != null )
	{
		UserDetails ud = (UserDetails)validUsers.get(userID);
		if( ud != null && ud.getPassword().equals(password))
		{
			valid = true;
		}
	}
	
	return valid;
}
}
