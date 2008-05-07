package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (8/8/2002 7:55:15 PM)
 * @author: Administrator
 */
public interface LogonHandler {
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 7:55:57 PM)
 * @param userId java.lang.String
 * @param password java.lang.String
 */
boolean handleLogon(String userId, String password);
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 10:07:54 AM)
 */
void logonCancelled();
}
