package com.exsys.fix.test;

import java.io.*;

import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;

public class TestFixPublisher
{
	public TestFixPublisher() throws IOException
	{
		String topic = "TRAD.ORD";
		TradingSessionManager sm = new TradingSessionManager();
		sm.startTradingSession();
		FixOrder ord = new FixOrder();
		ord.setSenderCompID("00700");
		ord.setOrderQty("10");
		ord.setSymbol("IBM");
		ord.setSide("1");
		ord.setPrice(100);
		ord.debugPrint();
		
		for( int i=0; i<10; i++ )
		{
			System.out.println("About to publish Message");
			sm.sendOrder(ord, topic);
			System.out.println("Published Message");
			try
			{
				Thread.sleep(30000);
			}
			catch(Exception e )
			{
				e.printStackTrace();
			}
			
		}
		sm.stopTradingSession();
		

	
	}
	public static void main( String[] args )
	{
		try
		{
			new TestFixPublisher();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
