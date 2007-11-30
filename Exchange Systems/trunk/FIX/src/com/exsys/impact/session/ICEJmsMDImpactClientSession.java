package com.exsys.impact.session;


import com.exsys.impact.message.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.service.*;
import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;

/**
 * @author kreddy
 *
 * This class implements a JMS version of
 * FixClientSession for ICE market data
 */
public class ICEJmsMDImpactClientSession extends ImpactClientSession implements ImpactMessageProcessor
{
	private ImpactDataSessionManager tsManager = null;
	private String publishSubjectPrefixMD;
	private String reqSubscribeSubjectMD;
	private String publishSubjectPrefixSec;
	private String reqSubscribeSubjectSec;
	
	private boolean enableSecuritiesSession = false;
	private boolean enableMarketDataSession = false;


/**
 * ICEJmsMDImpactClientSession constructor
 * @param playback
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ICEJmsMDImpactClientSession(boolean playback)
		throws ConfigAttributeNotFound,
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	initializeConfig();
}
/**
 * ICEJmsMDImpactClientSession constructor
 * @param newHost
 * @param newPort
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ICEJmsMDImpactClientSession(String newHost, int newPort)
		throws ConfigAttributeNotFound,
			   ConfigAttributeInvalid,
			   SystemException
{
	super(newHost, newPort);
	initializeConfig();
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.JmsFixClientSession#initializeConfig()
 */
protected void initializeConfig()
		throws ConfigAttributeNotFound,
			   SystemException
{
	//super.initializeConfig();

	// initialize config parameters
    try
    {
    	publishSubjectPrefixMD = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_MD_PUBLISH_PREFIX);
	   	Logger.debug("MD Client Publish Subject is " + publishSubjectPrefixMD );
    	reqSubscribeSubjectMD = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_MD_SUBSCRIBE_SUBJECT);
    	Logger.debug("MD Client Subscribe Subject is " + reqSubscribeSubjectMD );
 		String strSec = ConfigurationService.getValue(ImpactMessageConstants.IMPACTCLIENT_ENABLE_SECURITIES);
 		if(strSec.equals("Y")) enableSecuritiesSession = true;
 		String strMkt = ConfigurationService.getValue(ImpactMessageConstants.IMPACTCLIENT_ENABLE_MARKETDATA);
 		if(strMkt.equals("Y")) enableMarketDataSession = true;
 		System.out.println("Securities - " + enableSecuritiesSession);
 		System.out.println("MarketData - " + enableMarketDataSession);
    	publishSubjectPrefixSec = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_SEC_PUBLISH_PREFIX);
	   	Logger.debug("SEC Client Publish Subject is " + publishSubjectPrefixSec );
    	reqSubscribeSubjectSec = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_SEC_SUBSCRIBE_SUBJECT);
    	Logger.debug("SEC Client Subscribe Subject is " + reqSubscribeSubjectSec );
 		

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
		tsManager = new ImpactDataSessionManager();
		tsManager.startImpactSession();
		tsManager.receiveImpactMessages(this, reqSubscribeSubjectMD);
		tsManager.receiveImpactMessages(this, reqSubscribeSubjectSec);
		Logger.debug("Waiting For Market Data Requests");
	}
	registerImpactMessageProcessor(this);

}
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#shutdownClientSession()
 */
protected void shutdownClientSession()
{
	super.shutdownClientSession();
	if(tsManager != null)
	{
		tsManager.stopImpactSession();
		tsManager = null;
	}
}

public void logonSuccessful()
{
	// only instantiate application message layer 
	// if primary
		
	initializeApplicationMessageLayer();

	info("LOGON SUCCESSFUL");
	super.logonSuccessful();
}


public boolean processProductDefinitionRequest(ProductDefinitionRequest msg)
{
	sendMessage(msg);
	return true;
}

public boolean processProductDefinitionResponse(ProductDefinitionResponse msg)
{
	return true;
}

public boolean processProductDefinitionResponse(byte[] msg)
{
	String respSub = publishSubjectPrefixSec;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processMarketDataRequest(RequestFeedByMarketID mdReq)
{
	sendMessage(mdReq);
	return true;
}

public boolean processMarketSnapshot(MarketSnapshotMessage msg)
{
	return true;
}
public boolean processMarketSnapshotOrder(MarketSnapshotOrderMessage msg)
{
	return true;
}
public boolean processAddModifyOrder(AddModifyOrderMessage msg)
{
	return true;
}
public boolean processDeleteOrder(DeleteOrderMessage msg)
{
	return true;
}
public boolean processTrade(TradeMessage msg)
{
	return true;
}
public boolean processCancelledTrade(CancelledTradeMessage msg)
{
	return true;
}
public boolean processMarketStatistics(MarketStatisticsMessage msg)
{
	return true;
}
public boolean processOpenPrice(OpenPriceMessage msg)
{
	return true;
}
public boolean processOpenInterest(OpenInterestMessage msg)
{
	return true;
}
public boolean processSettlementPrice(SettlementPriceMessage msg)
{
	return true;
}
public boolean processMarketStateChange(MarketStateChangeMessage msg)
{
	return true;
}
  
public boolean processMarketSnapshot(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processMarketSnapshotOrder(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processAddModifyOrder(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processDeleteOrder(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processTrade(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processCancelledTrade(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processMarketStatistics(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processOpenPrice(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processOpenInterest(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processSettlementPrice(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}
public boolean processMarketStateChange(byte[] msg)
{
	String respSub = publishSubjectPrefixMD;
	tsManager.sendImpactMessage(msg, respSub);
	
	return true;
}  



}
