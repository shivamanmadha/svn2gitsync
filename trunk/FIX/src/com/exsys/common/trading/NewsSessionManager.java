package com.exsys.common.trading;

import java.io.*;

import com.exsys.fix.message.*;

/**
 * This class implements IFNewsSessionManager
 * and provides session management functionality
 * Creation date: (2/2/2002 10:37:54 AM)
 * @author: Administrator
 */
public class NewsSessionManager implements IFNewsSessionManager{
	private JmsTradingSessionManager sessionManager = null;
	private StringWriter sw = null;
	private NativeFixTranslator nft = null;
/**
 * NewsSessionManager constructor.
 */
public NewsSessionManager() {
	super();
}
/**
 * This method sets up the subscription for news messages.
 * Creation date: (2/2/2002 11:10:56 AM)
 * @param processor NewsMessageProcessor
 * @param topic java.lang.String
 */
public void receiveNewsMessages(NewsMessageProcessor processor, String topic)
{
	try
	{
		JmsNewsMessageReceiver msgReceiver = new JmsNewsMessageReceiver(processor);
		sessionManager.createSubscriber( topic, msgReceiver);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
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
		nft.translateIncludingMsgType( msg,true );
		sessionManager.publishMessage(sw.toString().getBytes(), topic);
		sw.getBuffer().setLength(0);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

}
/**
 * Method to start news session.
 * Creation date: (2/2/2002 10:45:37 AM)
 */
public void startNewsSession()
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
 * method to stop news session.
 * Creation date: (2/2/2002 10:45:49 AM)
 */
public void stopNewsSession()
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
