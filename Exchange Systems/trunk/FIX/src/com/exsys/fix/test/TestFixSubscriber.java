package com.exsys.fix.test;

import java.io.*;

import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.common.business.*;

public class TestFixSubscriber implements TradeMessageProcessor
{
	public TestFixSubscriber() throws IOException
	{
		String topic = "TRAD.ORD";
		TradingSessionManager sm = new TradingSessionManager();
		sm.startTradingSession();
		sm.receiveTradingMessages(this, topic);
		System.out.println("Waiting for messages on topic - "+topic);
		//sm.stopTradingSession();
		try
		{
		// wait for messages
			Thread.currentThread().join();
		
			//System.out.println("About To Sleep");
			//Thread.sleep(240000);
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		//System.out.println("Sleep Completed");
		
		
	
	}
	public static void main( String[] args )
	{
		try
		{
			new TestFixSubscriber();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:55:06 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processCancel(Cancel cancel) {}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancel(FixCancel cxl)
{
	System.out.println("Received cancel. Need to Send to Fix Server");

      //ord.setSenderCompID(getSenderCompID());
      //ord.setTargetCompID(getTargetCompID());
      //ord.setSendingTime(new Date());
      //ord.setHandlInst("1");
      //ord.setTransactTime(new Date());
	
	  //sendOrder(ord);
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancelReplace(FixCancelReplace cxr)
{
	System.out.println("Received cancel replace. Need to Send to Fix Server");

      //ord.setSenderCompID(getSenderCompID());
      //ord.setTargetCompID(getTargetCompID());
      //ord.setSendingTime(new Date());
      //ord.setHandlInst("1");
      //ord.setTransactTime(new Date());
	
	  //sendOrder(ord);
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 8:03:24 PM)
 * @param response com.exsys.fix.message.FixExecutionReport
 */
public void processExecutionReport(FixExecutionReport response) 
{
	System.out.println("Received Message");
	response.debugPrint();
}
/**
 * Insert the method's description here.
 * Creation date: (2/2/2002 8:03:04 PM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processOrder(FixOrder ord) 
{

	System.out.println("Received Message");
	ord.debugPrint();

}
public void processCancelReject(FixCancelReject rej) 
{

	System.out.println("Received Message");
	rej.debugPrint();

}
public void processStatusRequest(FixOrderStatusRequest stat) 
{

	System.out.println("Received Message");

}

/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 11:26:25 PM)
 * @param quote com.exsys.common.business.RealTimeQuote
 */
public void processQuote(RealTimeQuote quote) 
{
	System.out.println("Received Quote Message");
	quote.toString();

}
}
