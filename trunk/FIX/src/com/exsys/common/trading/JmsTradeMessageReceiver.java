package com.exsys.common.trading;

import javax.jms.*;

import com.exsys.fix.message.*;
import com.exsys.common.business.*;
/**
 * This class implements the jms methods
 * to handle trade messages recieved
 * Creation date: (1/31/2002 5:23:59 AM)
 * @author: Administrator
 */
public class JmsTradeMessageReceiver implements MessageListener{
	private TradeMessageProcessor mProcessor = null;
/**
 * JmsTradeMessageReceiver constructor.
 */
public JmsTradeMessageReceiver(TradeMessageProcessor processor) {
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
		/*
		if( message instanceof BytesMessage )
		{
			System.out.println("Received message");
			BytesMessage bMsg = (BytesMessage)message;
			byte[] bytes = new byte[1000];
			int length = bMsg.readBytes(bytes);
			System.out.println("Msg is " + new String(bytes));
		}
		*/
		
		if( message instanceof TextMessage )
		{
			//System.out.println("Received Text message");
			TextMessage tMsg = (TextMessage)message;
			String msg = tMsg.getText();
			//System.out.println("Msg is " + msg);
			processMessage(msg);
		}
		else if( message instanceof StreamMessage )
		{
			//System.out.println("Received Stream Message");
			StreamMessage sMsg = (StreamMessage)message;
			IBusinessObject obj = JmsMessageFactory.createJmsMessage(sMsg);
			processBusinessMessage(obj);
			
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
 * This calls the processor by handing it the  trade object
 * Creation date: (2/3/2002 6:28:28 AM)
 * @param msg java.lang.String
 */
private void processBusinessMessage(IBusinessObject obj) 
{
	// we need to construct a FixMessage
	try
	{
		if( obj.type.equals(IBusinessObject.QUOTE))
		{
			mProcessor.processQuote((RealTimeQuote)obj);
		}
		else if( obj.type.equals(IBusinessObject.CANCEL))
		{
			mProcessor.processCancel((Cancel)obj);
		}		
		else
		{
			System.out.println("INVALID MESSAGE RECEIVED");
		}
	}
	catch(Exception e )
	{
		e.printStackTrace();
	}
}
/**
 * Method to process the news message received
 * This calls the processor by handing it the Fix trade object.
 * Creation date: (2/3/2002 6:28:28 AM)
 * @param msg java.lang.String
 */
private void processMessage(String msg) 
{
	// we need to construct a FixMessage
	try
	{
		//System.out.println("Inside ProcessMessage");
		FixMessage fixMsg = FixMessageFactory.createFixMessageWithoutValidation(msg.getBytes());
		//fixMsg.debugPrint();
		String msgType = fixMsg.getMessageType();
		if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
		{
			mProcessor.processOrder((FixOrder)fixMsg);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL))
		{
			mProcessor.processCancel((FixCancel)fixMsg);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER_STATUS_REQUEST))
		{
			mProcessor.processStatusRequest((FixOrderStatusRequest)fixMsg);
		}		
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
		{
			mProcessor.processCancelReplace((FixCancelReplace)fixMsg);
		}					
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT))
		{
			mProcessor.processExecutionReport((FixExecutionReport)fixMsg);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT))
		{
			mProcessor.processCancelReject((FixCancelReject)fixMsg);
		}		
		else
		{
			System.out.println("INVALID MESSAGE RECEIVED");
		}
	}
	catch(Exception e )
	{
		e.printStackTrace();
	}
}
}
