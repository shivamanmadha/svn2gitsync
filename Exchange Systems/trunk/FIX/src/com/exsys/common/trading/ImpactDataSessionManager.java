package com.exsys.common.trading;

import java.io.*;

import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;


/**
 * This class implements IFMarketDataSessionManager
 * and provides session management functionality
 * Creation date: (2/2/2002 10:37:54 AM)
 * @author: Administrator
 */
public class ImpactDataSessionManager implements IFImpactSessionManager{
	private JmsTradingSessionManager sessionManager = null;
	//private StringWriter sw = null;
	//private NativeFixTranslator nft = null;
/**
 * ImpactSessionManager constructor .
 */
public ImpactDataSessionManager() {
	super();
}
/**
 * This method sets up the subscription for market data messages.
 * Creation date: (2/2/2002 11:10:56 AM)
 * @param processor NewsMessageProcessor
 * @param topic java.lang.String
 */
public void receiveImpactMessages(ImpactMessageProcessor processor, String topic)
{
	try
	{
		JmsImpactMessageReceiver msgReceiver = new JmsImpactMessageReceiver(processor);
		sessionManager.createSubscriber( topic, msgReceiver);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
}

public void sendImpactMessage(MDMessage msg, String topic)
{
	sendImpactMessage(msg.serialize().array(),topic);
}

public void sendImpactMessage(byte[] msg, String topic)
{
	sendImpactMessage(msg,msg.length,topic);
}
/**
 * This method implements publishing any fix message.
 * Creation date: (2/2/2002 11:31:19 AM)
 * @param msg com.exsys.fix.message.FixMessage
 * @param topic java.lang.String
 */
public void sendImpactMessage(byte[] msg,int length, String topic)
{

	try
	{
		//nft.translateIncludingMsgType( msg);
		//System.out.println("Publishing - " + sw.toString());
		sessionManager.publishBytesMessage(msg,length, topic);

		//sw.getBuffer().setLength(0);
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
	finally
	{
		//sw.getBuffer().setLength(0);
	}

}
/**
 * Method to start market data session
 * Creation date: (2/2/2002 10:45:37 AM)
 */
public void startImpactSession()
{

	if( sessionManager == null )
	{
		try
		{
			sessionManager = new JmsTradingSessionManager();
			//sw = new StringWriter();
			//sw.getBuffer().setLength(0);
			//nft = new NativeFixTranslator( sw );
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
public void stopImpactSession()
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
