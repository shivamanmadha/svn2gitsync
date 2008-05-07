package com.exsys.fix.session;


import java.util.*;
import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.common.business.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;

/**
 * @author kreddy
 *
 * This class implements a JMS version of
 * FixClientSession ( currently used for CME  and
 * can be used for any exchange )
 */
public class JmsFixClientSession extends FixClientSession 
			implements TradeMessageProcessor,NewsMessageProcessor
{
	private TradingSessionManager tsManager = null;
	private String publishSubjectPrefix;
	private String reqSubscribeSubject;

  //news
  private boolean mEnableNews = false;
  private String mNewsSubject = null;
	
	

/**
 * JmsFixClientSession constructor
 * @param newHost
 * @param newPort
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public JmsFixClientSession(String newHost, int newPort)
		throws ConfigAttributeNotFound, 
			   ConfigAttributeInvalid,
			   SystemException
{
	super(newHost, newPort);
	initializeConfig();
}
/**
 * JmsFixClientSession constructor
 * @param playback 
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public JmsFixClientSession(boolean playback)
		throws ConfigAttributeNotFound, 
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	initializeConfig();
}

/**
 * @throws ConfigAttributeNotFound
 * @throws SystemException
 */
protected void initializeConfig() 
		throws ConfigAttributeNotFound,
			   SystemException
{
	// initialize config parameters
    try
    {
    	publishSubjectPrefix = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_PUBLISH_PREFIX);
	   	Logger.debug("Client Publish Subject is " + publishSubjectPrefix );
    	reqSubscribeSubject = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SUBSCRIBE_SUBJECT);
    	Logger.debug("Client Subscribe Subject is " + reqSubscribeSubject );
		// news related
		String newsFlag = ConfigurationService.getValue("EnableNews","N");
	    if(newsFlag.equals("Y"))
	    {
	  		mEnableNews = true;
    		System.out.println("EnableNews is " + newsFlag );
	  
	    	mNewsSubject = ConfigurationService.getValue("NewsSubject");
        	System.out.println("News Publish Subject is " + mNewsSubject );
	    }
    	

    }
    catch( ConfigAttributeNotFound exc )
    {
	    Logger.fatal("JmsFixClientSession: REQUIRED CONFIG VALUES DO NOT EXIST");
	    throw exc;	    
    }

}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#logonSuccessful()
 */
public void logonSuccessful()
{
	// only instantiate application message layer 
	// if primary

	if(isPrimary())
	{		
		Logger.debug("PRIMARY CLIENT CHANNEL");
		if(mEnableNews)
		{
			initializeNewsLayer();
		}
		
		initializeApplicationMessageLayer();
	}
	else
	{
		Logger.debug("BACKUP CLIENT CHANNEL");
	}

	info("LOGON SUCCESSFUL");
	super.logonSuccessful();
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#switchToPrimary()
 */
public void switchToPrimary()
{
	if(!isPrimary())
	{
		super.switchToPrimary();
		Logger.debug("SWITCHING TO PRIMARY CLIENT CHANNEL");
		if(mEnableNews)
		{
			initializeNewsLayer();
		}		
		initializeApplicationMessageLayer();		
	}	
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#switchToBackup()
 */
public void switchToBackup()
{
	super.switchToBackup();
}

/**
 * method to initialize application layer
 */
protected void initializeApplicationMessageLayer()
{
	if( tsManager == null )
	{
		tsManager = new TradingSessionManager();
		tsManager.startTradingSession();
		tsManager.receiveTradingMessages(this, reqSubscribeSubject);				
		Logger.debug("Waiting For Orders");
	}
	registerApplicationMessageProcessor(this);
	
}
/**
 * method to initialize News Layer
 */
protected void initializeNewsLayer()
{
	registerNewsMessageProcessor(this);
	
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#shutdownClientSession()
 */
protected void shutdownClientSession()
{
	super.shutdownClientSession();
	if(tsManager != null)
	{
		tsManager.stopTradingSession();
		tsManager = null;		
	}
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processCancel(com.exsys.common.business.Cancel)
 */
public void processCancel(Cancel cancel)
{
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processCancel(com.exsys.fix.message.FixCancel)
 */
public void processCancel(FixCancel cxl)
{
	Logger.debug("Received cancel. Need to Send to Fix Server");
	sendCancel(cxl);
}
/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processStatusRequest(com.exsys.fix.message.FixOrderStatusRequest)
 */
public void processStatusRequest(FixOrderStatusRequest stat)
{
	Logger.debug("Received status. Need to Send to Fix Server");
	sendStatusRequest(stat);
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processCancelReplace(com.exsys.fix.message.FixCancelReplace)
 */
public void processCancelReplace(FixCancelReplace cxr)
{
	  Logger.debug("Received cancel replace. Need to Send to Fix Server");	  
	  sendCancelReplace(cxr);
}
/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processCancelReject(com.exsys.fix.message.FixCancelReject)
 */
public void processCancelReject(FixCancelReject cxlRej)
{
	Logger.debug("Received cancel reject. Need to publish");	  
	String respSub = publishSubjectPrefix;
	String scID = getSenderCompID();
	String scNoFTI = scID.substring(0,scID.length()-1);
	
	if(!getExchange().equals("CME"))
		scNoFTI = scID;
		
	respSub += ("REJ."+ scNoFTI);
	tsManager.sendFixMessage(cxlRej, respSub);	

}
/* (non-Javadoc)
 * @see com.exsys.common.trading.NewsMessageProcessor#processNews(com.exsys.fix.message.FixNews)
 */
public void processNews(FixNews  news)
{
	Logger.debug("Recieved News. Need to Publish");
	tsManager.sendFixMessage(news, mNewsSubject);
	
}	

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processExecutionReport(com.exsys.fix.message.FixExecutionReport)
 */
public void processExecutionReport(FixExecutionReport response)
{
	Logger.debug("Recieved Execution Report. Need to Publish");

	String respSub = publishSubjectPrefix;
	String scID = getSenderCompID();
	
	String scNoFTI = scID.substring(0,scID.length()-1);
	if(!getExchange().equals("CME"))
		scNoFTI = scID;
		
	if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_FILL))
	{
		respSub += ("FILL."+ scNoFTI);
		//MetricsCollector.endTimer("sendOrder");
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PARTIAL))
	{
		respSub += ("FILL."+ scNoFTI);
		//MetricsCollector.endTimer("sendOrder","fill");
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_CANCELED))
	{
		respSub += ("CONF."+ scNoFTI);
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_TRADE_CANCELED))
	{
		respSub += ("TCXL."+ scNoFTI);
	}	
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_EXPIRED))
	{
		respSub += ("EXPR."+ scNoFTI);
	}		
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_NEW))
	{
		respSub += ("ACC."+ scNoFTI);
		//MetricsCollector.endTimer("sendOrder");
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REPLACED))
	{
		respSub += ("RPL."+ scNoFTI);
		//MetricsCollector.endTimer("sendOrder");
	}	
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REJECTED))
	{
		respSub += ("REJ."+ scNoFTI);
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_DONE_FOR_DAY))
	{
		respSub += ("DONE."+ scNoFTI);
	}	
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PEND_CANCEL))
	{
		respSub += ("PNDCXL."+ scNoFTI);
	}		
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PEND_REPLACE))
	{
		respSub += ("PNDRPL."+ scNoFTI);
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_DUG))
 	{
  		respSub += ("DUG."+ scNoFTI);
 	} 			
	else
	{
		String msg = "Received Unknown Execution Report - " + response.getExecType();
		Logger.error("Received Unknown Execution Report - " + response.getExecType());
		error(msg);
		return;

	}
	tsManager.sendExecutionReport(response, respSub);

}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processOrder(com.exsys.fix.message.FixOrder)
 */
public void processOrder(FixOrder ord)
{
	Logger.debug("Received Order. Need to Send to Fix Server");
	  MetricsCollector.beginTimer("sendOrder");
	  sendOrder(ord);
	  MetricsCollector.endTimer("sendOrder");
	  	  
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processQuote(com.exsys.common.business.RealTimeQuote)
 */
public void processQuote(RealTimeQuote quote)
{
}
}
