package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (8/9/2002 1:07:05 PM)
 * @author: Administrator
 */
public interface FixTestBenchSessionControlHandler {
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:07:41 PM)
 */
void closeFixSession(FixServerDef server, FixClientDef client);
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:07:26 PM)
 */
void openFixSession(FixServerDef server, FixClientDef client);
}
