package com.exsys.mktdata.session;

import javax.jms.*;

import com.exsys.mktdata.message.*;

/**
 * Insert the type's description here.
 * Creation date: (1/31/2002 5:23:59 AM)
 * @author: Administrator
 */
public class JmsRLCMarketDataMessageReceiver implements MessageListener{
	private RLCMarketDataMessageProcessor mProcessor = null;
/**
 * JmsMarketDataMessageReceiver constructor comment.
 */
public JmsRLCMarketDataMessageReceiver(RLCMarketDataMessageProcessor processor) {
	super();
	mProcessor = processor;
}
/**
 * Insert the method's description here.
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
			System.out.println("Received Text message");
			TextMessage tMsg = (TextMessage)message;
			String msg = tMsg.getText();
			System.out.println("Msg is " + msg);
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
 * Insert the method's description here.
 * Creation date: (2/3/2002 6:28:28 AM)
 * @param msg java.lang.String
 */
private void processMessage(String msg)
{
	// we need to construct a FixMessage
	try
	{
		System.out.println("Inside ProcessMessage");
		RLCMessage rlcMsg = RLCMessageFactory.createRLCMessage(msg.getBytes());
		//System.out.println(rlcMsg.toString());
		String msgType = rlcMsg.getMessageType();
	if( msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M0))
	{
		mProcessor.processMZeroMessage((RLCMZeroMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M9))
	{
		mProcessor.processM9Message((RLCM9Message)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MG))
	{
		mProcessor.processMGMessage((RLCMGMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MH))
	{
		mProcessor.processMHMessage((RLCMHMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MJ))
	{
		mProcessor.processMJMessage((RLCMJMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MI))
	{
		mProcessor.processMIMessage((RLCMIMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MK))
	{
		mProcessor.processMKMessage((RLCMKMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_ML))
	{
		mProcessor.processMLMessage((RLCMLMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MM))
	{
		mProcessor.processMMMessage((RLCMMMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MO))
	{
		mProcessor.processMOMessage((RLCMOMessage)rlcMsg);
	}
	/*
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MU))
	{
		//rlcMsg = new RLCMUMessage(msg);
		throw new RLCProtocolError("Not Implemented Yet");
	}
	*/
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M4))
	{
		mProcessor.processM4Message((RLCM4Message)rlcMsg);
	}	
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MA))
	{
		//rlcMsg = new RLCMAMessage(msg);
		//throw new RLCProtocolError("Not Implemented Yet");
		mProcessor.processMAMessage((RLCMAMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MX))
	{
		//rlcMsg = new RLCMXMessage(msg);
		//throw new RLCProtocolError("Not Implemented Yet");
		mProcessor.processMXMessage((RLCMXMessage)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_MY))
	{
		//rlcMsg = new RLCMYMessage(msg);
		//throw new RLCProtocolError("Not Implemented Yet");
		mProcessor.processMYMessage((RLCMYMessage)rlcMsg);
	}	
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M5))
	{
		mProcessor.processM5Message((RLCM5Message)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M6))
	{
		mProcessor.processM6Message((RLCM6Message)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M7))
	{
		mProcessor.processM7Message((RLCM7Message)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_M8))
	{
		mProcessor.processM8Message((RLCM8Message)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_09))
	{
		mProcessor.process09Message((RLC09Message)rlcMsg);
	}
	else if(msgType.equals(RLCMessageConstants.RLC_MESSAGE_TYPE_23))
	{
		mProcessor.process23Message((RLC23Message)rlcMsg);
	}
	else
	{
			mProcessor.processMessage(rlcMsg);
	}
	}
	catch(Exception e )
	{
		e.printStackTrace();
	}
}
}
