package com.exsys.fix.message;

/**
 * This class defines static string constants related to FIX protocol
 */
public class FixConstants {

	public final static byte FIX_FIELD_DELIMITER = 001;
	public final static String FIX_TAG_VALUE_SEPARATOR = "=";
	public final static char CHAR_FIX_TAG_VALUE_SEPERATOR = '=';
	
	public final static String FIX_MSGTYPE_LOGON = "A";
	public final static String FIX_MSGTYPE_TRADER_LOGON = "CG";	//ICE ONLY
	public final static String FIX_MSGTYPE_TRADER_LOGON_RESPONSE = "CI";	//ICE ONLY	
	public final static String FIX_MSGTYPE_HEARTBEAT = "0";
	public final static String FIX_MSGTYPE_TESTREQUEST = "1";
	public final static String FIX_MSGTYPE_RESENDREQUEST = "2";
	public final static String FIX_MSGTYPE_REJECT = "3";
	public final static String FIX_MSGTYPE_BUSINESS_REJECT = "j";
	public final static String FIX_MSGTYPE_SEQUENCERESET = "4";
	public final static String FIX_MSGTYPE_LOGOUT = "5";
	public final static String FIX_MSGTYPE_TRADER_LOGOUT = "CH";//ICE ONLY
	public final static String FIX_MSGTYPE_TRADER_LOGOUT_RESPONSE = "CJ";	//ICE ONLY		
	public final static String FIX_MSGTYPE_ORDER = "D";
	public final static String FIX_MSGTYPE_CANCEL = "F";
	public final static String FIX_MSGTYPE_CANCELREPLACE = "G";	
	public final static String FIX_MSGTYPE_EXECUTIONREPORT = "8";
	public final static String FIX_MSGTYPE_CANCEL_REJECT = "9";
	public final static String FIX_MSGTYPE_ORDER_STATUS_REQUEST = "H";
	public final static String FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST = "c";
	public final static String FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE = "d";
	public final static String FIX_MSGTYPE_MARKETDATA_REQUEST = "V";	
	public final static String FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS = "f";	
	public final static String FIX_MSGTYPE_MARKETDATA_FULL_REFRESH = "W";	
	public final static String FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH = "X";	
	public final static String FIX_MSGTYPE_MARKETDATA_REJECT = "Y";					
	public final static String FIX_MSGTYPE_NEWS = "B";



	public final static String FIX_EXECUTIONREPORT_NEW = "0";
	public final static String FIX_EXECUTIONREPORT_PARTIAL = "1";
	public final static String FIX_EXECUTIONREPORT_FILL = "2";		
	public final static String FIX_EXECUTIONREPORT_DONE_FOR_DAY = "3";	
	public final static String FIX_EXECUTIONREPORT_CANCELED = "4";
	public final static String FIX_EXECUTIONREPORT_REPLACED = "5";	
	public final static String FIX_EXECUTIONREPORT_PEND_CANCEL = "6";
	public final static String FIX_EXECUTIONREPORT_REJECTED = "8";		
	public final static String FIX_EXECUTIONREPORT_PEND_NEW = "A";	
	public final static String FIX_EXECUTIONREPORT_EXPIRED = "C";	
	public final static String FIX_EXECUTIONREPORT_TRADE_CANCELED = "H";	
	public final static String FIX_EXECUTIONREPORT_UNKNOWN_ORDER = "U";	
	public final static String FIX_EXECUTIONREPORT_PEND_REPLACE = "E";
	public final static String FIX_EXECUTIONREPORT_DUG = "G";
	

	public final static String FIX_EXEC_TRANSTYPE_NEW = "0";
	public final static String FIX_EXEC_TRANSTYPE_DELETE = "1";
	public final static String FIX_EXEC_TRANSTYPE_STATUS = "3";		

	public final static String FIX_CANCEL_REJ_RESPONSETO_CANCEL = "1";
	public final static String FIX_CANCEL_REJ_RESPONSETO_REPLACE = "2";
	public final static String FIX_CANCEL_REJ_ORDER_STATUS_UNDEF = "U";
	public final static String FIX_CANCEL_REJ_ORDER_STATUS_NOTFOUND = "Y";	


	public final static String FIX_HEADER_BEGINSTRING_TAG = "8";
	public final static String FIX_HEADER_BODYLENGTH_TAG = "9";
	public final static String FIX_HEADER_MSGTYPE_TAG = "35";

	public final static String FIX_TRAILER_CHECKSUM_TAG = "10";	
	
	// repeating group fields
	public static String FIX_REPEATEDGROUP_NORELATEDSYM = "146";	
	public static String FIX_REPEATEDGROUP_NOMDENTRYTYPTES = "267";
	public static String FIX_REPEATEDGROUP_NOMDENTRIES = "268";
	public static String FIX_REPEATEDGROUP_LINESOFTEXT = "33";
	
	//order status
	public static String FIX_ORD_STATUS_NEW 	= "NEW";
	public static String FIX_ORD_STATUS_PARTIAL = "PARTIAL FILLED";
	public static String FIX_ORD_STATUS_FILLED 	= "FILLED";
	public static String FIX_ORD_STATUS_DONE 	= "DONE FOR DAY";
	public static String FIX_ORD_STATUS_CANCEL	= "CANCELLED";
	public static String FIX_ORD_STATUS_REPLACE = "REPLACED";
	public static String FIX_ORD_STATUS_PENDCXL	= "PENDING CANCEL";
	public static String FIX_ORD_STATUS_STOPPED = "STOPPED";
	public static String FIX_ORD_STATUS_REJECT 	= "REJECTED";
	public static String FIX_ORD_STATUS_SUSPEND = "SUSPENDED";
	public static String FIX_ORD_STATUS_PENDNEW = "PENDING NEW";
	public static String FIX_ORD_STATUS_CAL 	= "CALCULATED";
	public static String FIX_ORD_STATUS_EXPIRED = "EXPIRED";
	public static String FIX_ORD_STATUS_ACCEPT 	= "ACCEPTED";
	public static String FIX_ORD_STATUS_PENDREP = "PENDING REPLACE";

}
