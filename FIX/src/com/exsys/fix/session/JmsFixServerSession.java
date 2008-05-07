package com.exsys.fix.session;

import java.net.*;
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
 * FixServerSession 
 */
public class JmsFixServerSession extends FixServerSession implements TradeMessageProcessor
{
	private TradingSessionManager tsManager = null;
	private String publishSubjectPrefix;
	private String responseSubscribeSubjectPrefix;

/**
 * JmsFixServerSession constructor
 * @param clientSocket
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public JmsFixServerSession(Socket clientSocket) throws ConfigAttributeNotFound, 
								ConfigAttributeInvalid,
								SystemException
{
	super(clientSocket);

	// initialize config parameters
		publishSubjectPrefix = ConfigurationService.getValue(ConfigConstants.FIXSERVER_PUBLISH_PREFIX);
	   	System.out.println("Server Publish Subject is " + publishSubjectPrefix );
    	responseSubscribeSubjectPrefix = ConfigurationService.getValue(ConfigConstants.FIXSERVER_RESPONSE_SUBSCRIBE_PREFIX);
	   	System.out.println("Response Subscribe Subject is " + responseSubscribeSubjectPrefix );

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
		Logger.debug("PRIMARY SERVER SESSION");
		initializeApplicationMessageLayer();
	}
	else
	{
		Logger.debug("BACKUP SERVER SESSION");
	}

	super.logonSuccessful();
}

/**
 * 
 */
private void initializeApplicationMessageLayer()
{

	String respSubSub = responseSubscribeSubjectPrefix;
	if( tsManager == null )
	{
		tsManager = new TradingSessionManager();
		tsManager.startTradingSession();
		//String senderCompID = getSessionContext().getSenderCompID();
		//respSubSub += getSenderCompID();
		respSubSub += getTargetCompID();
		tsManager.receiveTradingMessages(this, respSubSub);

		Logger.debug("Waiting For Execution Reports for " + getSenderCompID());
	}
	registerApplicationMessageProcessor(this);
	
}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#switchToPrimary()
 */
public void switchToPrimary()
{
	
	if(!isPrimary())
	{
		super.switchToPrimary();
		Logger.debug("SWITCHING TO PRIMARY SERVER SESSION");
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
	System.out.println("Received Cancel. Need to publish");

	tsManager.sendCancel(cxl, publishSubjectPrefix + getSenderCompID());
}
/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processStatusRequest(com.exsys.fix.message.FixOrderStatusRequest)
 */
public void processStatusRequest(FixOrderStatusRequest stat)
{
	Logger.debug("Received status. Need to publish");
	tsManager.sendStatusRequest(stat,publishSubjectPrefix + getSenderCompID());
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processCancelReplace(com.exsys.fix.message.FixCancelReplace)
 */
public void processCancelReplace(FixCancelReplace cxr)
{
	System.out.println("Received cancel replace. Need to publish");

	tsManager.sendCancelReplace(cxr, publishSubjectPrefix + getSenderCompID());
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processExecutionReport(com.exsys.fix.message.FixExecutionReport)
 */
public void processExecutionReport(FixExecutionReport response)
{
	System.out.println("Recieved Execution Report. Need to Send to Fix Client");

	// we need to add some of the required fields
	//response.setSenderCompID(getSenderCompID());
	// need to fix
	// why does not, session context contain target
	//response.setTargetCompID(getSenderCompID());
	//response.setSendingTime(FixMessage.getUTCCurrentTime());
	// need to fix
	//response.setExecTransType("1");
	// we do not have buy or sell on the Fill object
	// need to fix
	//response.setSide("1");
	//response.setAvgPx("0.0");
	//response.setCumQty("0");
	// we currently are not setting symbol on FILLs, as it is not
	// available on the FILL.
	//if( response.getSymbol() == null )
	//	response.setSymbol("FIXIT");

	//response.setOrdStatus(response.getExecType());

	sendExecutionReport( response );

}
/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processCancelReject(com.exsys.fix.message.FixCancelReject)
 */
public void processCancelReject(FixCancelReject cxlRej)
{
	System.out.println("Recieved Cancel Reject. Need to Send to Fix Client");


	sendCancelReject( cxlRej );

}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processOrder(com.exsys.fix.message.FixOrder)
 */
public void processOrder(FixOrder ord)
{
	System.out.println("Received Order. Need to publish");

	tsManager.sendOrder(ord, publishSubjectPrefix + getSenderCompID());

}

/* (non-Javadoc)
 * @see com.exsys.common.trading.TradeMessageProcessor#processQuote(com.exsys.common.business.RealTimeQuote)
 */
public void processQuote(RealTimeQuote quote)
{
}
}
