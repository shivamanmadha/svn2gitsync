package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (8/8/2002 11:50:15 AM)
 * @author: Administrator
 */
public class UserDetails {
	private java.lang.String userID;
	private java.lang.String userName;
	private java.lang.String password;
/**
 * UserDetails constructor comment.
 */
public UserDetails() {
	super();
}
/**
 * UserDetails constructor comment.
 */
public UserDetails(String newUserID,
				   String newUserName,
				   String newPassword) 
{
	super();
	userID = newUserID;
	userName = newUserName;
	password = newPassword;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:51:08 AM)
 * @return java.lang.String
 */
public java.lang.String getPassword() {
	return password;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:50:41 AM)
 * @return java.lang.String
 */
public java.lang.String getUserID() {
	return userID;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:50:53 AM)
 * @return java.lang.String
 */
public java.lang.String getUserName() {
	return userName;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:51:08 AM)
 * @param newPassword java.lang.String
 */
public void setPassword(java.lang.String newPassword) {
	password = newPassword;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:50:41 AM)
 * @param newUserID java.lang.String
 */
public void setUserID(java.lang.String newUserID) {
	userID = newUserID;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:50:53 AM)
 * @param newUserName java.lang.String
 */
public void setUserName(java.lang.String newUserName) {
	userName = newUserName;
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 12:38:05 PM)
 * @return java.lang.String
 */
public String toString() 
{
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("UserID: " + userID );
	returnString.append("\n");

	returnString.append("User Name: " + userName );
	returnString.append("\n");

	returnString.append("Password: " + password );
	returnString.append("\n");
			
	return returnString.toString();
}
}
