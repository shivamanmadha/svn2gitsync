package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (8/9/2002 1:07:05 PM)
 * @author: Administrator
 */
public interface FixSessionControlHandler {
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:07:26 PM)
 */
void connectToFixServer();
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:07:41 PM)
 */
void disconnectFromFixServer();
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 4:11:53 PM)
 * @param orderType java.lang.String
 * @param tif java.lang.String
 * @param symbol java.lang.String
 * @param qty java.lang.String
 * @param price java.lang.String
 */
void printStatistics();
void resetSequenceNum(String newSeq);
void processOrder(String orderType, String tif, String symbol, String qty, String price, boolean isBuy);
}
