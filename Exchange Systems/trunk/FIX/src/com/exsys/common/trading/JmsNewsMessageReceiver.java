package com.exsys.common.trading;

import javax.jms.*;

import com.exsys.fix.message.*;
/**
 * This class implements the jms methods
 * to handle news messages recieved
 * Creation date: (1/31/2002 5:23:59 AM)
 * @author: Administrator
 */
public class JmsNewsMessageReceiver implements MessageListener{
	private NewsMessageProcessor mProcessor = null;
/**
 * JmsNewsMessageReceiver constructor.
 * @param processor NewsMessageProcessor
 */
public JmsNewsMessageReceiver(NewsMessageProcessor processor) {
	super();
	mProcessor = processor;
}
/**
 * JMS main message where messages are received
 * for topics that are subscribed to.
 * Creation date: (2/2/2002 10:56:02 AM)
 * @param message javax.jms.Message
 */
public void onMessage(Message message)
{

	try
	{
		if( message instanceof TextMessage )
		{
			//System.out.println("Received Text message");
			TextMessage tMsg = (TextMessage)message;
			String msg = tMsg.getText();
			//System.out.println("Msg is " + msg);
			processMessage(msg);
		}
		else
		{
			System.out.println("Unknown Message Received");
		}
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

}
/**
 * Method to process the news message received
 * This calls the processor by handing it the FixNews object
 * Creation date: (2/3/2002 6:28:28 AM)
 * @param msg java.lang.String
 */
private void processMessage(String msg)
{
	// we need to construct a FixMessage
	try
	{
		//System.out.println("Inside ProcessMessage");
		//System.out.println(new String(msg.getBytes()));
		FixMessage fixMsg = FixMessageFactory.createFixMessageWithoutValidation(msg.getBytes());
		//fixMsg.debugPrint();
		String msgType = fixMsg.getMessageType();
		if( msgType.equals(FixConstants.FIX_MSGTYPE_NEWS))
		{
			mProcessor.processNews((FixNews)fixMsg);
		}
		else
		{
			System.out.println("JMS NewsMessageReceiver - INVALID MESSAGE RECEIVED");
		}
	}
	catch(Exception e )
	{
		e.printStackTrace();
	}
}
}
