package com.exsys.common.exceptions;

/**
 * This exception is used to represent impact protocol error
 * Creation date: (6/2/01 9:51:43 AM)
 * @author: Administrator
 */
public class ImpactUnknownMsgError extends BusinessException {
/**
 * ImpactUnknownMsgError constructor.
 */
public ImpactUnknownMsgError() {
	super();
}
/**
 * ImpactUnknownMsgError constructor.
 * @param s java.lang.String
 */
public ImpactUnknownMsgError(String s) {
	super(s);
}
/**
 * ImpactUnknownMsgError constructor.
 * Creation date: (6/2/01 10:17:51 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public ImpactUnknownMsgError(String intMsg, String extMsg)
{
	super( intMsg, extMsg );
}
}
