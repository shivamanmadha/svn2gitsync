package com.exsys.common.exceptions;

/**
 * This exception is used to represent rlc protocol error
 * Creation date: (6/2/01 9:51:43 AM)
 * @author: Administrator
 */
public class RLCProtocolError extends BusinessException {
/**
 * RLCProtocolError constructor.
 */
public RLCProtocolError() {
	super();
}
/**
 * RLCProtocolError constructor.
 * @param s java.lang.String
 */
public RLCProtocolError(String s) {
	super(s);
}
/**
 * RLCProtocolError constructor.
 * Creation date: (6/2/01 10:17:51 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 */
public RLCProtocolError(String intMsg, String extMsg)
{
	super( intMsg, extMsg );
}
}
