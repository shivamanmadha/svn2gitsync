package com.exsys.orderentry;

/**
 * Insert the type's description here.
 * Creation date: (2/10/2002 7:04:55 AM)
 * @author: Administrator
 */
public interface OrderEntryConstants 
{
	// constants for TRADE WINDOW
	public static String TRADEWINDOW_REQ_PUB_SUB = "RequestPublishSubject";
	public static String TRADEWINDOW_USER_ID = "UserID";
	public static String TRADEWINDOW_OFM_OVERRIDE_FLAG = "OFMOverrideFlag";
	


	// constants for MATCH ENGINE
	public static String MATCHENGINE_REQ_SUB_SUB = "RequestSubscribeSubject";
	public static String MATCHENGINE_ACC_PUBLISH_PREFIX = "AcceptPublishPrefix";
	public static String MATCHENGINE_REJ_PUBLISH_PREFIX = "RejectPublishPrefix";
	public static String MATCHENGINE_FILL_PUBLISH_PREFIX = "FillPublishPrefix";
	public static String MATCHENGINE_CONF_PUBLISH_PREFIX = "ConfirmPublishPrefix";
	public static String MATCHENGINE_QUOTE_PUBLISH_PREFIX = "QuotePublishPrefix";	

	// constants for QUOTE WINDOW
	public static String QUOTEWINDOW_QUOTE_SUB_SUB = "QuoteSubscribeSubject";

	// constants for RESPONSE WINDOW
	public static String RESPONSEWINDOW_RESP_SUB_SUB = "ResponseSubscribeSubject";

	// constants for JMS ORDERENTRY
	public static String JMSORDERENTRY_REQ_PUB_SUB = "RequestPublishSubject";
	public static String JMSORDERENTRY_RESP_SUB_SUB = "ResponseSubscribeSubject";	
	public static String JMSORDERENTRY_SENDER_COMPANY_ID = "SenderCompanyID";
	public static String JMSORDERENTRY_SENDER_SUB_ID = "SenderSubID";
	public static String JMSORDERENTRY_USER_NAME = "UserName";
	public static String JMSORDERENTRY_TRADER_NAME = "TraderName";
	public static String JMSORDERENTRY_TARGET_COMPANY_ID = "TargetCompanyID";	
	
	


}
