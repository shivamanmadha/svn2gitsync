package com.exsys.common.trading;

import java.io.*;
import java.applet.Applet;

import com.exsys.common.business.*;
import com.exsys.fix.message.*;

/**
 * This class implements IFTradingSessionManager
 * and provides session management functionality
 * Creation date: (2/2/2002 10:37:54 AM)
 * @author: Administrator
 */
public class TradingSessionManager implements IFTradingSessionManager{
	private JmsTradingSessionManager sessionManager = null;
	private StringWriter sw = null;
	private NativeFixTranslator nft = null;
/**
 * TradingSessionManager constructor .
 */
public TradingSessionManager() {
	super();
}
/**
 * This method sets up the subscription for trade messages.
 * Creation date: (2/2/2002 11:10:56 AM)
 * @param processor TradeMessageProcessor
 * @param topic java.lang.String
 */
public void receiveTradingMessages(TradeMessageProcessor processor, String topic) 
{
	try
	{
		JmsTradeMessageReceiver msgReceiver = new JmsTradeMessageReceiver(processor);
		sessionManager.createSubscriber( topic, msgReceiver);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:24:23 PM)
 */
public void sendAccept(Response accept, String sub) 
{
	  // implement send order using trading sesion manager
	  FixAccept fixAccept = (FixAccept)accept.getFixMsg();
	  try
	  {
		  sendExecutionReport(fixAccept, sub);
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:24:23 PM)
 */
public void sendCancel(Cancel cancel, String sub) 
{
	  try
	  {
		  sessionManager.publishBusinessMessage(cancel, sub);
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:24:23 PM)
 */
public void sendCancelConfirm(Confirm confirm, String sub) 
{
	  // implement send order using trading sesion manager
	  FixCancelConfirm fixConfirm = (FixCancelConfirm)confirm.getFixMsg();
	  try
	  {
		  sendExecutionReport(fixConfirm, sub);
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 11:08:40 AM)
 * @param ord com.exsys.fix.message.FixOrder
 * @param topic java.lang.String
 */
public void sendExecutionReport(FixExecutionReport resp, String topic) 
{
	// we need to get byte[] from FixOrder
	sendFixMessage( resp, topic );
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:24:23 PM)
 */
public void sendFill(Fill fill, String sub) 
{
	  // implement send fill using trading sesion manager
	  FixExecutionReport fixER = (FixExecutionReport)fill.getFixMsg();
	  try
	  {
		  sendExecutionReport(fixER, sub);
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 11:31:19 AM)
 * @param msg com.exsys.fix.message.FixMessage
 * @param topic java.lang.String
 */
public void sendFixMessage(FixMessage msg, String topic) 
{

	try
	{
		nft.translateIncludingMsgType( msg );
		sessionManager.publishMessage(sw.toString().getBytes(), topic);
		sw.getBuffer().setLength(0);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:24:23 PM)
 */
public void sendOrder(Order ord, String sub) 
{
	  // implement send order using trading sesion manager
	  FixOrder fixOrder = (FixOrder)ord.getFixMsg();
	  try
	  {
		  sendOrder(fixOrder, sub);
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 11:08:40 AM)
 * @param ord com.exsys.fix.message.FixOrder
 * @param topic java.lang.String
 */
public void sendOrder(FixOrder ord, String topic) 
{
	// we need to get byte[] from FixOrder
	sendFixMessage( ord, topic );
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:24:23 PM)
 */
public void sendQuote(RealTimeQuote quote, String sub) 
{
	  try
	  {
		  sessionManager.publishBusinessMessage(quote, sub);
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:24:23 PM)
 */
public void sendReject(Response reject, String sub) 
{
	  // implement send order using trading sesion manager
	  FixReject fixReject = (FixReject)reject.getFixMsg();
	  try
	  {
		  sendExecutionReport(fixReject, sub);
	  }
	  catch( Exception e )
	  {
		  e.printStackTrace();
	  }
	
}
/**
 * method to start trading session.
 * Creation date: (2/2/2002 10:45:37 AM)
 */
public void startTradingSession() 
{
	
	if( sessionManager == null )
	{
		try
		{
			sessionManager = new JmsTradingSessionManager();
			sw = new StringWriter();
			nft = new NativeFixTranslator( sw );
			sessionManager.startSession();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
/**
 * method to start trading session.
 * Creation date: (2/2/2002 10:45:37 AM)
 */
public void startTradingSession(Applet applet) 
{
	
	if( sessionManager == null )
	{
		try
		{
			sessionManager = new JmsTradingSessionManager(applet);
			sw = new StringWriter();
			nft = new NativeFixTranslator( sw );
			sessionManager.startSession();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
/**
 * method to stop trading session
 * Creation date: (2/2/2002 10:45:49 AM)
 */
public void stopTradingSession() 
{	
	if( sessionManager != null)
	{
		try
		{
			sessionManager.close();
		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
	}
	
}

/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 11:08:40 AM)
 * @param ord com.exsys.fix.message.FixOrder
 * @param topic java.lang.String
 */
public void sendCancel(FixCancel cxl, String topic) 
{
	// we need to get byte[] from FixOrder
	sendFixMessage( cxl, topic );
}

public void sendStatusRequest(FixOrderStatusRequest stat, String topic) 
{
	// we need to get byte[] from FixOrder
	sendFixMessage( stat, topic );
}

/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 11:08:40 AM)
 * @param ord com.exsys.fix.message.FixOrder
 * @param topic java.lang.String
 */
public void sendCancelReplace(FixCancelReplace cxr, String topic) 
{
	// we need to get byte[] from FixOrder
	sendFixMessage( cxr, topic );
}
}
