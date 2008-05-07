package com.exsys.impact.message;
public class ImpactMessageConstants
{
	
	public static String IMPACT_VERSION = "ImpactVersion";
	public static String IMPACT_HEARTBEAT_IN_SECONDS =   "HeartBeatInSeconds";
	public static String IMPACT_LOG_FILE_DIRECTORY =   "LogFileDirectory";	
	public static String IMPACT_TRADERNAME = "TraderName";
	public static String IMPACT_TRADER_PASSWORD = "TraderPassword";
	public static String IMPACT_MKTSTATSFLAG = "MarketStatsFlag";
	public static String IMPACT_DATABUFFERINGFLAG = "DataBufferingFlag";
	public static String IMPACT_BUNDLEMARKERFLAG = "BundleMarkerFlag";
	public static String IMPACT_IMPLIEDORDERSFLAG = "ImpliedOrdersFlag";
	
	public static String IMPACT_MD_PUBLISH_PREFIX = "MDPublishSubjectPrefix";
	public static String IMPACT_MD_SUBSCRIBE_SUBJECT = "MDRequestSubscribeSubject";	
		
	public static String IMPACT_SEC_PUBLISH_PREFIX = "SecPublishSubjectPrefix";
	public static String IMPACT_SEC_SUBSCRIBE_SUBJECT = "SecRequestSubscribeSubject";	

	public static String IMPACTCLIENT_ENABLE_SECURITIES = "EnableSecuritiesSession";
	public static String IMPACTCLIENT_ENABLE_MARKETDATA = "EnableMarketDataSession";	
		


	public static String IMPACT_MESSAGE_TYPE_1 = "1";
	public static int IMPACT_MESSAGE_LENGTH_1 = 83;
	
	public static String IMPACT_MESSAGE_TYPE_A = "A";
	public static int IMPACT_MESSAGE_LENGTH_A = 128;	

	public static String IMPACT_MESSAGE_TYPE_B = "B";
	public static int IMPACT_MESSAGE_LENGTH_B = 247;	
	

	public static String getTradeStatusDesc(char status)
	{
		String desc;
		switch(status)
		{
			case 'O':
				desc = "Open";
			   break;
			case 'C':
				desc = "Close";
			   break;
			case 'E':
				desc = "Expired";
			   break;
			case '1':
				desc = "Pre-Open";
			   break;
			case '2':
				desc = "Pre-Close";
			   break;
			case 'S':
				desc = "Suspended";
			   break;
			default:
				desc = "UnKnown";
			   			   			   			   			   			   			
		}
		return desc;
	}	


}

