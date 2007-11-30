package com.exsys.common.trading;

import java.io.*;

import com.exsys.fix.message.*;

/**
 * Insert the type's description here.
 * Creation date: (2/2/2002 10:37:54 AM)
 * @author: Administrator
 */
public class SecuritiesSessionManager implements IFSecuritiesSessionManager{
	private JmsTradingSessionManager sessionManager = null;
	private StringWriter sw = null;
	private NativeFixTranslator nft = null;
/**
 * TradingSessionManager constructor comment.
 */
public SecuritiesSessionManager() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 11:10:56 AM)
 * @param topic java.lang.String
 */
public void receiveSecuritiesMessages(SecuritiesMessageProcessor processor, String topic)
{
	try
	{
		JmsSecuritiesMessageReceiver msgReceiver = new JmsSecuritiesMessageReceiver(processor);
		sessionManager.createSubscriber( topic, msgReceiver);
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
public void sendSecurityDefinitionResponse(FixSecurityDefinitionResponse resp, String topic)
{
	// we need to get byte[] from FixOrder
	sendFixMessage( resp, topic );
}
public void sendSecurityDefinitionRequest(FixSecurityDefinitionRequest req, String topic)
{
	// we need to get byte[] from FixOrder
	sendFixMessage( req, topic );
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
 * Insert the method's description here.
 * Creation date: (2/2/2002 10:45:37 AM)
 */
public void startSecuritiesSession()
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
 * Insert the method's description here.
 * Creation date: (2/2/2002 10:45:49 AM)
 */
public void stopSecuritiesSession()
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
