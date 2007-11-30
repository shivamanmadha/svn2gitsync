package com.exsys.fix.test;

import java.io.*;
import java.util.*;

import com.exsys.fix.message.*;
import com.exsys.fix.session.*;
import com.exsys.common.exceptions.*;

public class FixTest
{
	private FileOutputStream outWriter = null;
	public FixTest(String[] args) throws IOException
	{


		Properties	prop = new Properties();
		  try
		  {
			 prop.load( new FileInputStream( args[0] ) );
			 outWriter = new FileOutputStream( args[0], true );
	  	  }
	  	  catch( Exception e )
	  	  {
			e.printStackTrace();
		  }
		
		String orderId = prop.getProperty("OrderID");
		System.out.println("OrderID = " + orderId );
		
		String cancelId = prop.getProperty("CancelID");
		System.out.println("CancelID = " + cancelId );

		String execId = prop.getProperty("ExecID");
		System.out.println("ExecID = " + execId );
		
		prop.setProperty("OrderID", "10");
		//prop.setProperty("CancelID","10");
		//prop.setProperty("ExecID","10");
		
		prop.store( outWriter,null);
		
		orderId = prop.getProperty("OrderID");
		System.out.println("OrderID = " + orderId );
		
		cancelId = prop.getProperty("CancelID");
		System.out.println("CancelID = " + cancelId );

		execId = prop.getProperty("ExecID");
		System.out.println("ExecID = " + execId );


/*		
		
		FixLogon logon = new FixLogon();
		logon.setHeartBtInt(100);
		logon.setSenderCompID("00070");

		StringWriter sw = new StringWriter();
		StringWriter sw1 = new StringWriter();		
		NativeFixTranslator ft = new NativeFixTranslator( sw );
		NativeFixTranslator ft1 = new NativeFixTranslator( sw1 );
		ft.translateHeader(logon,true);
		String headerString = sw.toString();
		System.out.println("Header buffer is "+headerString);
		sw.getBuffer().setLength(0);
		System.out.println("Header buffer is "+headerString);		
		ft.translateBody( logon );
		
		System.out.println("Body buffer is "+sw.toString());
		sw.getBuffer().setLength(0);

		ft.translate( logon );
		String fixString = sw.toString();
		System.out.println(" Msg is " + fixString );

			byte delim = 001;
			String str = "8=Fix4.2";
			str += (char)delim;
			str += "9=200";
			str += (char)delim;			
			str += "35=A";
			str += (char)delim;
			str += "10=100";
			str += (char)delim;
			System.out.println( str );
		
*/
		/*

		FixSessionManager mgr = new FixSessionManager("00070");
		mgr.initializeSequenceNumbers();

		int inSeq = mgr.getInSeq();
		int outSeq = mgr.getOutSeq();
		System.out.println("Seq initialized IN - "+ inSeq + " OUT - "+ outSeq);
		for( int i=0; i<10; i++)
		{
			mgr.logInFixMessage(inSeq++,fixString.getBytes());
		}

		for( int i=0; i<5; i++ )
		{
			mgr.logOutFixMessage(outSeq++,fixString.getBytes());			
		}
		
		mgr.initializeSequenceNumbers();

		inSeq = mgr.getInSeq();
		outSeq = mgr.getOutSeq();
		System.out.println("Seq initialized IN - "+ inSeq + " OUT - "+ outSeq);

		mgr.sessionClosed();
		*/
		/*
		try
		{

			System.out.println("Before Validation");
			//FixMessage fm = FixMessageFactory.createFixMessage(fixString.getBytes());
			FixMessage fm = FixMessageFactory.createFixMessage(str.getBytes());			

			System.out.println("Message type is "+ fm.getMsgType());

		
			ft1.translate( fm );
		
			System.out.println(" Msg is "+ sw.toString());
		}
		catch( FixProtocolError e )
		{
			System.out.println(e.getExternalMessage());
			System.out.println(e.getMessage());			
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		*/

	
	}
	public static void main( String[] args )
	{
		try
		{
			new FixTest(args);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
