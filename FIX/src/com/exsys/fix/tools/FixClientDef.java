package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (10/8/2002 11:19:45 PM)
 * @author: Administrator
 */
public class FixClientDef {
	private java.lang.String senderCompID;
	private java.lang.String senderSubID;
	private java.lang.String userID;
	private java.lang.String password;
	private java.lang.String targetCompID;
	private java.lang.String targetSubID;
/**
 * FixClientDef constructor comment.
 */
public FixClientDef() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:36:14 PM)
 * @return java.lang.String
 */
public String debugString() {
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("SenderCompID: " + senderCompID );
	returnString.append("\n");

	returnString.append("SenderSubID: " + senderSubID );
	returnString.append("\n");

	returnString.append("TargetCompID: " + targetCompID );
	returnString.append("\n");

	returnString.append("TargetSubID: " + targetSubID );
	returnString.append("\n");
	
	returnString.append("UserID: " + userID );
	returnString.append("\n");

	returnString.append("Password: " + password );
	returnString.append("\n");

	
	
	return returnString.toString();
	
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:41:52 PM)
 * @return java.lang.String
 */
public String getKey() {
	
	String key = StringUtil.checkForNullsAndTrim(senderCompID) +
				 "-"+
				 StringUtil.checkForNullsAndTrim(senderSubID) +
				 "-"+
				 StringUtil.checkForNullsAndTrim(targetCompID) +
				 "-"+
				 StringUtil.checkForNullsAndTrim(targetSubID);
	return key; 
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:59 PM)
 * @return java.lang.String
 */
public java.lang.String getPassword() {
	return password;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:10 PM)
 * @return java.lang.String
 */
public java.lang.String getSenderCompID() {
	return senderCompID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:22 PM)
 * @return java.lang.String
 */
public java.lang.String getSenderSubID() {
	return senderSubID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:21:20 PM)
 * @return java.lang.String
 */
public java.lang.String getTargetCompID() {
	return targetCompID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:40:51 PM)
 * @return java.lang.String
 */
public java.lang.String getTargetSubID() {
	return targetSubID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:47 PM)
 * @return java.lang.String
 */
public java.lang.String getUserID() {
	return userID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:59 PM)
 * @param newPassword java.lang.String
 */
public void setPassword(java.lang.String newPassword) {
	password = newPassword;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:10 PM)
 * @param newSenderCompID java.lang.String
 */
public void setSenderCompID(java.lang.String newSenderCompID) {
	senderCompID = newSenderCompID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:22 PM)
 * @param newSenderSubID java.lang.String
 */
public void setSenderSubID(java.lang.String newSenderSubID) {
	senderSubID = newSenderSubID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:21:20 PM)
 * @param newTargetCompID java.lang.String
 */
public void setTargetCompID(java.lang.String newTargetCompID) {
	targetCompID = newTargetCompID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:40:51 PM)
 * @param newTargetSubID java.lang.String
 */
public void setTargetSubID(java.lang.String newTargetSubID) {
	targetSubID = newTargetSubID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:20:47 PM)
 * @param newUserID java.lang.String
 */
public void setUserID(java.lang.String newUserID) {
	userID = newUserID;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:50:08 PM)
 * @return java.lang.String
 */
public String toString() {
	return getKey();
}
}
