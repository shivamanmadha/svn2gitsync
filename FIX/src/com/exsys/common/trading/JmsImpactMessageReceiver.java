package com.exsys.common.trading;

import javax.jms.*;

import java.nio.ByteBuffer;
import com.exsys.impact.message.*;
import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;
/**
 * This class implements the jms methods
 * to handle market data messages recieved
 * Creation date: (1/31/2002 5:23:59 AM)
 * @author: Administrator
 */
public class JmsImpactMessageReceiver implements MessageListener{
	private ImpactMessageProcessor mProcessor = null;
/**
 * JmsMarketDataMessageReceiver constructor.
 */
public JmsImpactMessageReceiver(ImpactMessageProcessor processor) {
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
		if( message instanceof BytesMessage )
		{
			//System.out.println("Received Text message");
			BytesMessage bMsg = (BytesMessage)message;
			processMessage(bMsg);
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
 * This calls the processor by handing it the Fix Marketdata objects
 * Creation date: (2/3/2002 6:28:28 AM)
 * @param msg java.lang.String
 */
private void processMessage(BytesMessage msg)
{
	// we need to construct a FixMessage
	try
	{
		//System.out.println("Inside ProcessMessage");
		//System.out.println(new String(msg.getBytes()));
		MDMessage impMsg = getObject(msg);
		System.out.println("Msg Received - " + impMsg.toString());
		
		char msgType = impMsg.getMessageType();
		if(msgType == RawMessageFactory.ProductDefinitionRequestType)
		{
			mProcessor.processProductDefinitionRequest((ProductDefinitionRequest)impMsg);
		}
		else if(msgType == RawMessageFactory.ProductDefinitionResponseType)
		{
			mProcessor.processProductDefinitionResponse((ProductDefinitionResponse)impMsg);
		}
		else if(msgType == RawMessageFactory.RequestFeedByMarketIDType)
		{
			mProcessor.processMarketDataRequest((RequestFeedByMarketID)impMsg);
		}
		else if(msgType==RawMessageFactory.MarketSnapshotMessageType)
		{
			mProcessor.processMarketSnapshot((MarketSnapshotMessage)impMsg);
		}
		else if(msgType==RawMessageFactory.MarketSnapshotOrderMessageType)
		{
			mProcessor.processMarketSnapshotOrder((MarketSnapshotOrderMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.AddModifyOrderMessageType)
		{
			mProcessor.processAddModifyOrder((AddModifyOrderMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.DeleteOrderMessageType)
		{
			mProcessor.processDeleteOrder((DeleteOrderMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.TradeMessageType)
		{
			mProcessor.processTrade((TradeMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.CancelledTradeMessageType)
		{
			mProcessor.processCancelledTrade((CancelledTradeMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.MarketStatisticsMessageType)
		{
			mProcessor.processMarketStatistics((MarketStatisticsMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.OpenPriceMessageType)
		{
			mProcessor.processOpenPrice((OpenPriceMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.OpenInterestMessageType)
		{
			mProcessor.processOpenInterest((OpenInterestMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.SettlementPriceMessageType)
		{
			mProcessor.processSettlementPrice((SettlementPriceMessage)impMsg);
		}		
		else if(msgType==RawMessageFactory.MarketStateChangeMessageType)
		{
			mProcessor.processMarketStateChange((MarketStateChangeMessage)impMsg);
		}						
		else
		{
			System.out.println("Unhandled message");
		}

/*
		//fixMsg.debugPrint();
		String msgType = impMsg.getMessageType();
		if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH))
		{
			mProcessor.processMarketDataFullRefresh((FixMarketDataFullRefresh)fixMsg);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH))
		{
			mProcessor.processMarketDataIncrementalRefresh((FixMarketDataIncrementalRefresh)fixMsg);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS))
		{
			mProcessor.processMarketDataSecurityStatus((FixSecurityStatus)fixMsg);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REJECT))
		{
			mProcessor.processMarketDataReject((FixMarketDataReject)fixMsg);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST))
		{
			mProcessor.processMarketDataRequest((FixMarketDataRequest)fixMsg);
		}
		else
		{
			System.out.println("JMS SecuritiesMessageReceiver - INVALID MESSAGE RECEIVED");
		}
		*/
	}
	catch(Exception e )
	{
		e.printStackTrace();
	}
}

	public static MDMessage getObject( BytesMessage inputStream )
      throws Exception
	{
		MDMessage theBase = null;

		// read the message type and body length
      byte[] bytes = new byte[3];
      inputStream.readBytes(bytes);

      // instantiate an empty object
      byte messageType = bytes[0];
		theBase = RawMessageFactory.getObject( messageType );
      short bodyLength;
      if ((theBase!=null) && (theBase.getMessageType()==RawMessageFactory.DebugRequestType))
      {
         // treat debug request differently so that it works when it is sent through
         // telnet or F5 because it is hard to manipulate the ASCII string to come up
         // with a binary short value of 4, just use the hardcoded FIXED LENGTH
         bodyLength = 4;
      }
      else
      {
         // 2nd and 3rd byte are used for body length
         bodyLength = ByteBuffer.wrap(bytes, 1, 2).getShort();
      }


		// read the body with the length received
		byte messageBodyBytes[] = new byte[bodyLength];
		inputStream.readBytes(messageBodyBytes);

      if (theBase!=null)
      {
         theBase.setMessageBodyLength(bodyLength);

         // deserialize the body
         theBase.deserialize(ByteBuffer.wrap(messageBodyBytes));
      }


		return theBase;
	}

}
