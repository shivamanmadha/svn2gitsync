package com.exsys.common.trading;

import java.io.*;

import com.exsys.fix.message.*;

/**
 * This class implements IFMarketDataSessionManager
 * and provides session management functionality
 * Creation date: (2/2/2002 10:37:54 AM)
 * @author: Administrator
 */
public class MarketDataSessionManager implements IFMarketDataSessionManager{
	private JmsTradingSessionManager sessionManager = null;
	private StringWriter sw = null;
	private NativeFixTranslator nft = null;
/**
 * MarketDataSessionManager constructor .
 */
public MarketDataSessionManager() {
	super();
}
/**
 * This method sets up the subscription for market data messages.
 * Creation date: (2/2/2002 11:10:56 AM)
 * @param processor NewsMessageProcessor
 * @param topic java.lang.String
 */
public void receiveMarketDataMessages(MarketDataMessageProcessor processor, String topic)
{
	try
	{
		JmsMarketDataMessageReceiver msgReceiver = new JmsMarketDataMessageReceiver(processor);
		sessionManager.createSubscriber( topic, msgReceiver);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
}
/**
 * method to send FixMarketDataResponse message
 * Creation date: (2/2/2002 11:08:40 AM)
 * @param ord com.exsys.fix.message.FixOrder
 * @param topic java.lang.String
 */
public void sendMarketDataResponse(FixMessage resp, String topic)
{
	// we need to get byte[] from FixOrder
	sendFixMessage( resp, topic );
}
/**
 * method to send FixMarketDataRequest message
 * @param req
 * @param topic
 */
public void sendMarketDataRequest(FixMarketDataRequest req, String topic)
{
	// we need to get byte[] from FixOrder
	sendFixMessage( req, topic );
}

/**
 * This method implements publishing any fix message.
 * Creation date: (2/2/2002 11:31:19 AM)
 * @param msg com.exsys.fix.message.FixMessage
 * @param topic java.lang.String
 */
public void sendFixMessage(FixMessage msg, String topic)
{

	try
	{
		nft.translateIncludingMsgType( msg);
		//System.out.println("Publishing - " + sw.toString());
		sessionManager.publishMessage(sw.toString().getBytes(), topic);
		
		sw.getBuffer().setLength(0);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
	finally
	{
		sw.getBuffer().setLength(0);
	}

}
/**
 * Method to start market data session
 * Creation date: (2/2/2002 10:45:37 AM)
 */
public void startMarketDataSession()
{

	if( sessionManager == null )
	{
		try
		{
			sessionManager = new JmsTradingSessionManager();
			sw = new StringWriter();
			//sw.getBuffer().setLength(0);
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
 * Method to stop market data session
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
