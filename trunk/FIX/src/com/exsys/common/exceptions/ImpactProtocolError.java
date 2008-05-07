package com.exsys.common.exceptions;

/**
 * This exception is used to represent impact protocol error
 * Creation date: (6/2/01 9:51:43 AM)
 * @author: Administrator
 */
public class ImpactProtocolError extends BusinessException {
/**
 * ImpactProtocolError constructor.
 */
public ImpactProtocolError() {
	super();
}
/**
 * ImpactProtocolError constructor.
 * @param s java.lang.String
 */
public ImpactProtocolError(String s) {
	super(s);
}
/**
 * ImpactProtocolError constructor.
 * Creation date: (6/2/01 10:17:51 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public ImpactProtocolError(String intMsg, String extMsg)
{
	super( intMsg, extMsg );
}
}
