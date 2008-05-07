package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (10/8/2002 11:17:55 PM)
 * @author: Administrator
 */
public class FixServerDef {
	private java.lang.String primaryHost;
	private java.lang.String secondaryHost;
	private java.lang.String primaryPort;
	private java.lang.String secondaryPort;
/**
 * FixServerDef constructor comment.
 */
public FixServerDef() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:33:46 PM)
 * @return java.lang.String
 */
public String debugString() {
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("PrimaryHost: " + primaryHost );
	returnString.append("\n");

	returnString.append("Primary Port: " + primaryPort );
	returnString.append("\n");

	returnString.append("SecondaryHost: " + secondaryHost );
	returnString.append("\n");

	returnString.append("Secondary Port: " + secondaryPort );
	returnString.append("\n");			
	return returnString.toString();
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:50:48 PM)
 * @return java.lang.String
 */
public String getKey() {
	String key = StringUtil.checkForNullsAndTrim(primaryHost) +
				 "-"+
				 StringUtil.checkForNullsAndTrim(primaryPort);

	return key;

}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:18:39 PM)
 * @return java.lang.String
 */
public java.lang.String getPrimaryHost() {
	return primaryHost;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:19:15 PM)
 * @return java.lang.String
 */
public java.lang.String getPrimaryPort() {
	return primaryPort;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:18:56 PM)
 * @return java.lang.String
 */
public java.lang.String getSecondaryHost() {
	return secondaryHost;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:19:29 PM)
 * @return java.lang.String
 */
public java.lang.String getSecondaryPort() {
	return secondaryPort;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:18:39 PM)
 * @param newPrimaryHost java.lang.String
 */
public void setPrimaryHost(java.lang.String newPrimaryHost) {
	primaryHost = newPrimaryHost;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:19:15 PM)
 * @param newPrimaryPort java.lang.String
 */
public void setPrimaryPort(java.lang.String newPrimaryPort) {
	primaryPort = newPrimaryPort;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:18:56 PM)
 * @param newSecondaryHost java.lang.String
 */
public void setSecondaryHost(java.lang.String newSecondaryHost) {
	secondaryHost = newSecondaryHost;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:19:29 PM)
 * @param newSecondaryPort java.lang.String
 */
public void setSecondaryPort(java.lang.String newSecondaryPort) {
	secondaryPort = newSecondaryPort;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:52:28 PM)
 * @return java.lang.String
 */
public String toString() {
	return getKey();
}
}
