package com.exsys.common.exceptions;

/**
 * This exception is used to represent fix protocol error
 * Creation date: (6/2/01 9:51:43 AM)
 * @author: Administrator
 */
public class FixProtocolError extends BusinessException {
	private String errorCode = null;
/**
 * FixProtocolError constructor.
 */
public FixProtocolError() {
	super();
}
/**
 * FixProtocolError constructor.
 * @param s java.lang.String
 */
public FixProtocolError(String s) {
	super(s);
}


/**
 * FixProtocolError constructor.
 * @param s java.lang.String
 */
public FixProtocolError(String s, String code) {
	super(s);
	errorCode = code;
}
/**
 * FixProtocolError constructor.
 * Creation date: (6/2/01 10:17:51 AM)
 * @param intMsg java.lang.String
 * @param extMsg java.lang.String
 * @param code String
 */
public FixProtocolError(String intMsg, String extMsg, String code)
{
	super( intMsg, extMsg );
	errorCode = code;
}

/**
 * returns errorCode
 * @return errorCode java.lang.String
 */
public String getErrorCode()
{
	return errorCode;
}

/**
 * method to set error code
 * @param code java.lang.String
 */
public void setErrorCode(String code)
{
	errorCode = code;
}

}
