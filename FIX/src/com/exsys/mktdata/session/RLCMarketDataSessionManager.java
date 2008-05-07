package com.exsys.mktdata.session;

import java.io.*;
import com.exsys.common.trading.*;
import com.exsys.mktdata.message.*;

/**
 * Insert the type's description here.
 * Creation date: (2/2/2002 10:37:54 AM)
 * @author: Administrator
 */
public class RLCMarketDataSessionManager implements IFRLCMarketDataSessionManager{
	private JmsTradingSessionManager sessionManager = null;
/**
 * MarketDataSessionManager constructor comment.
 */
public RLCMarketDataSessionManager() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 11:10:56 AM)
 * @param topic java.lang.String
 */
public void receiveMarketDataMessages(RLCMarketDataMessageProcessor processor, String topic)
{
	try
	{
		JmsRLCMarketDataMessageReceiver msgReceiver = new JmsRLCMarketDataMessageReceiver(processor);
		sessionManager.createSubscriber( topic, msgReceiver);
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
public void sendMarketDataMessage(RLCMessage msg, String topic)
{

	try
	{
		sessionManager.publishMessage(msg.getBytes(), topic);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 10:45:37 AM)
 */
public void startMarketDataSession()
{

	if( sessionManager == null )
	{
		try
		{
			sessionManager = new JmsTradingSessionManager();
			sessionManager.startSession();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 10:45:49 AM)
 */
public void stopMarketDataSession()
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

}
