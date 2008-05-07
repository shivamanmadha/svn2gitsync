package com.exsys.fix.session;


import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.service.*;


/**
 * @author kreddy
 *
 * This class implements a JMS version of
 * FixClientSession for ICE market data
 */
public class ICEJmsMDFixClientSession extends JmsFixClientSession implements MarketDataMessageProcessor
{
	private MarketDataSessionManager tsManager = null;
	private String publishSubjectPrefix;
	private String reqSubscribeSubject;


/**
 * ICEJmsMDFixClientSession constructor
 * @param playback 
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ICEJmsMDFixClientSession(boolean playback)
		throws ConfigAttributeNotFound, 
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	//initializeConfig();
}
/**
 * ICEJmsMDFixClientSession constructor
 * @param newHost
 * @param newPort
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ICEJmsMDFixClientSession(String newHost, int newPort)
		throws ConfigAttributeNotFound,
			   ConfigAttributeInvalid,
			   SystemException
{
	super(newHost, newPort);
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.JmsFixClientSession#initializeConfig()
 */
protected void initializeConfig()
		throws ConfigAttributeNotFound,
			   SystemException
{
	super.initializeConfig();

	// initialize config parameters
    try
    {
    	publishSubjectPrefix = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_MD_PUBLISH_PREFIX);
	   	Logger.debug("MD Client Publish Subject is " + publishSubjectPrefix );
    	reqSubscribeSubject = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_MD_SUBSCRIBE_SUBJECT);
    	Logger.debug("MD Client Subscribe Subject is " + reqSubscribeSubject );

    }
    catch( ConfigAttributeNotFound exc )
    {
	    Logger.fatal("JmsFixClientSession: REQUIRED CONFIG VALUES DO NOT EXIST");
	    throw exc;
    }

}

/* (non-Javadoc)
 * @see com.exsys.fix.session.JmsFixClientSession#initializeApplicationMessageLayer()
 */
protected void initializeApplicationMessageLayer()
{
	//super.initializeApplicationMessageLayer();

	if( tsManager == null )
	{
		tsManager = new MarketDataSessionManager();
		tsManager.startMarketDataSession();
		tsManager.receiveMarketDataMessages(this, reqSubscribeSubject);
		Logger.debug("Waiting For Market Data Requests");
	}
	registerMarketDataMessageProcessor(this);

}
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#shutdownClientSession()
 */
protected void shutdownClientSession()
{
	super.shutdownClientSession();
	if(tsManager != null)
	{
		tsManager.stopMarketDataSession();
		tsManager = null;
	}
}




/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processTraderLogonResponse(com.exsys.fix.message.FixTraderLogonResponse)
 */
public boolean processTraderLogonResponse(FixTraderLogonResponse msg)
{
	String str = "Trader Logon Response ";
	str += "ClientId = " + msg.getClientId();
	str += " UserName = " + msg.getUserName();
	str += " Text = " + msg.getText();

	//Logger.info(str);
	info(str);
	return true;
}


/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processTraderLogoutResponse(com.exsys.fix.message.FixTraderLogoutResponse)
 */
public boolean processTraderLogoutResponse(FixTraderLogoutResponse msg)
{

	String str = "Trader Logout Response ";
	str += "ClientId = " + msg.getClientId();
	str += " UserName = " + msg.getUserName();
	str += " Text = " + msg.getText();

	//Logger.info(str);
	info(str);

	return true;
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.MarketDataMessageProcessor#processMarketDataFullRefresh(com.exsys.fix.message.FixMarketDataFullRefresh)
 */
public boolean processMarketDataFullRefresh(FixMarketDataFullRefresh msg)
{
	String respSub = publishSubjectPrefix;
	//String scID = getSenderCompID();
	//tsManager.sendMarketDataFullRefresh(msg, respSub);
	tsManager.sendMarketDataResponse(msg,respSub);
	return true;

}
/* (non-Javadoc)
 * @see com.exsys.common.trading.MarketDataMessageProcessor#processMarketDataReject(com.exsys.fix.message.FixMarketDataReject)
 */
public void processMarketDataReject(FixMarketDataReject msg)
{
	String respSub = publishSubjectPrefix;
	//String scID = getSenderCompID();
	//tsManager.sendMarketDataReject(msg, respSub);
	tsManager.sendMarketDataResponse(msg,respSub);


}
/* (non-Javadoc)
 * @see com.exsys.common.trading.MarketDataMessageProcessor#processMarketDataIncrementalRefresh(com.exsys.fix.message.FixMarketDataIncrementalRefresh)
 */
public boolean processMarketDataIncrementalRefresh(FixMarketDataIncrementalRefresh msg)
{
	String respSub = publishSubjectPrefix;
	//String scID = getSenderCompID();
	//tsManager.sendMarketDataIncrementalRefresh(msg, respSub);
	//System.out.println("FIX SEQ # :" + msg.getMsgSeqNumAsString());
	tsManager.sendMarketDataResponse(msg,respSub);
	return true;

}
/* (non-Javadoc)
 * @see com.exsys.common.trading.MarketDataMessageProcessor#processMarketDataSecurityStatus(com.exsys.fix.message.FixSecurityStatus)
 */
public void processMarketDataSecurityStatus(FixSecurityStatus msg)
{
	String respSub = publishSubjectPrefix;
	//String scID = getSenderCompID();
	//tsManager.sendMarketDataSecurityStatus(msg, respSub);
	tsManager.sendMarketDataResponse(msg,respSub);


}
/* (non-Javadoc)
 * @see com.exsys.common.trading.MarketDataMessageProcessor#processMarketDataRequest(com.exsys.fix.message.FixMarketDataRequest)
 */
public void processMarketDataRequest(FixMarketDataRequest msg)
{
	sendMarketDataRequest(msg);
}


}
