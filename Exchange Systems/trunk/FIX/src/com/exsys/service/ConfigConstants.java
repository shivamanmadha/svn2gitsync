package com.exsys.service;

/**
 * Insert the type's description here.
 * Creation date: (2/10/2002 7:04:55 AM)
 * @author: Administrator
 */
public interface ConfigConstants 
{
	// constants for FIX SERVER

	public static String FIXSERVER_LISTEN_PORT = "ListenPort";
	public static String FIXSERVER_PUBLISH_PREFIX = "PublishSubjectPrefix";
	public static String FIXSERVER_RESPONSE_SUBSCRIBE_PREFIX = "ResponseSubscribePrefix";		

	

	// constants for FIX CLIENT

	public static String FIXCLIENT_CONNECT_PORT = "ConnectionPort";
	public static String FIXCLIENT_SERVER_HOST = "ServerHost";
	public static String FIXCLIENT_SENDER_COMP_ID = "SenderCompID";
	public static String FIXCLIENT_TARGET_COMP_ID = "TargetCompID";
	public static String FIXCLIENT_PUBLISH_PREFIX = "PublishSubjectPrefix";
	public static String FIXCLIENT_SUBSCRIBE_SUBJECT = "RequestSubscribeSubject";
	
	public static String FIXCLIENT_ENABLE_SECURITIES = "EnableSecuritiesSession";
	public static String FIXCLIENT_ENABLE_MKTDATA = "EnableMarketDataSession";

	
	// added for ICE
	public static String FIXCLIENT_USERNAME = "UserName";
	public static String FIXCLIENT_TRADERNAME = "TraderName";
	public static String FIXCLIENT_TRADER_PASSWORD = "TraderPassword";
	public static String FIXCLIENT_SEC_PUBLISH_PREFIX = "SecPublishSubjectPrefix";
	public static String FIXCLIENT_SEC_SUBSCRIBE_SUBJECT = "SecRequestSubscribeSubject";
	public static String FIXCLIENT_MD_PUBLISH_PREFIX = "MDPublishSubjectPrefix";
	public static String FIXCLIENT_MD_SUBSCRIBE_SUBJECT = "MDRequestSubscribeSubject";	
	


	public static String FIXCLIENT_SESSION_ID  = "SessionID";
	public static String FIXCLIENT_FIRM_ID =  "FirmID";
	public static String FIXCLIENT_SENDER_SUB_ID =  "SenderSubID";
	public static String FIXCLIENT_SENDER_LOCATION_ID =  "SenderLocationID";
	public static String FIXCLIENT_TARGET_SUB_ID =  "TargetSubID";
	public static String FIXCLIENT_PASSWORD =   "Password";
	public static String FIXCLIENT_AGEDORDER_LIMIT_IN_SECONDS = "AgedOrderLimitInSeconds";	
	public static String FIXCLIENT_SERVER_PRIMARY_FLAG  = "ServerPrimary";	

	
	// common
	public static String FIXCOMMON_LOGON_TYPE_BOW =   "BOW";
	public static String FIXCOMMON_FIX_VERSION =   "FixVersion";
	public static String FIXCOMMON_FIX_EXCHANGE =   "Exchange";
	public static String FIXCOMMON_HEARTBEAT_IN_SECONDS =   "HeartBeatInSeconds";
	public static String FIXCOMMON_FTI =   "FTI";
	public static String FIXCOMMON_LOG_FILE_DIRECTORY =   "LogFileDirectory";	
}
