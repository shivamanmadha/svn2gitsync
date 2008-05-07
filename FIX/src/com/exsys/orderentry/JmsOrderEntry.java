package com.exsys.orderentry;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.exsys.fix.message.*;
import com.exsys.fix.session.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.orderentry.ui.*;
import com.exsys.application.*;
import com.exsys.common.business.*;
import com.exsys.service.*;

class JmsOrderEntry extends BaseApplication implements TradeMessageProcessor{

	private OrderResponseLog traderRespLog;
  public JmsOrderEntry(String[] args) throws ConfigFileNotFound {
	  super(args);
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) { }

     
    JPanel bigPane = new JPanel();
    bigPane.setLayout(new GridLayout(0,1));
    bigPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("<Project Etna> Trader Application"),
        BorderFactory.createEmptyBorder(10,10,10,10)
      )
    );

    //KRR --- added
    String respSubSub=null;

    try
    {
  		  	respSubSub = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_RESP_SUB_SUB);
    		System.out.println("Resp Publish Subject is " + respSubSub );    		
    		
    }
    catch( ConfigAttributeNotFound exc )
    {
	   		 System.out.println("CONFIG VALUES DOES NOT EXIST");
	   		 exc.printStackTrace();
	    	System.exit(0);	    
    }


    
    // create a JMS session
    //KRR -- Commented
    //JmsTradingSessionManager jmsTSM = new JmsTradingSessionManager();
	TradingSessionManager jmsTSM = new TradingSessionManager();
    // Order Entry
    OrderEntryUI traderOEApp = new OrderEntryUI(jmsTSM);
    Component contentsOE = traderOEApp.createOrderScreen();
    bigPane.add(contentsOE);


    // construct msg processor & it's subscriber/msg-listener
    //JmsTradeMessageProcessor trdMsgProcessor = new JmsTradeMessageProcessor();
    //JmsTradeMessageReceiver subscriber = new JmsTradeMessageReceiver(trdMsgProcessor);
    //jmsTSM.regSubscriber(subscriber, "TRAD.ORD");


    traderRespLog = new OrderResponseLog();
    Component contentsOrdRespLog = traderRespLog.createLogArea();
    bigPane.add(contentsOrdRespLog);

    //Create the top-level container and add contents to it.
    JFrame frame = new JFrame("Trader Application");
    frame.getContentPane().add(bigPane, BorderLayout.CENTER);


    //Finish setting up the frame, and show it.
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.pack();
    frame.setVisible(true);

    jmsTSM.startTradingSession();
    //KRR -- added
    // call to receive response and other messaegs
    jmsTSM.receiveTradingMessages(this,respSubSub);
    
    
    // connect to fix server
    //FixClientSession fixSession = new FixClientSession("localhost", 3000, "kdr", "ekc");
    //fixSession.registerResponseDispatcher(jmsMsgProcessor);
    //reqMgr.registerFixSession(fixSession);
		//fixSession.start();
		//fixSession.sendLogonMessage();


  }
  public static void main(String[] args)
  {
	  try
	  {
    		new JmsOrderEntry(args);
	  }
	  catch(Exception e )
	  {
		  e.printStackTrace();
	  }
  }
/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:19:45 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processCancel(Cancel cancel)
{
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancel(FixCancel cxl)
{
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancelReplace(FixCancelReplace cxr)
{
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:49 AM)
 * @param response com.exsys.fix.message.FixExecutionReport
 */
public void processExecutionReport(FixExecutionReport response)
{
	String displayText = "Response: ";
	
	if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_FILL))
	{
		displayText += "FILL - ";
		displayText += response.getClOrderID();
		displayText += (" - "+ response.getLastSharesAsString()+"@"+
						response.getLastPxAsString() );
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PARTIAL))
	{
		displayText += " PARTIAL FILL";
		displayText += response.getClOrderID();
		displayText += (" - "+ response.getLastSharesAsString()+"@"+
						response.getLastPxAsString() );
		
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_CANCELED))
	{
		displayText += "CANCEL CONFIRM";
		displayText += response.getOrderID();		
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_NEW))
	{
		displayText += "ACCEPTED";
		displayText += response.getOrderID();		
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REJECTED))
	{
		displayText += "REJECTED";
		displayText += response.getOrderID();		
	}
	else
	{
		displayText += "UNKNOWN";
	}

	traderRespLog.displayText(displayText);
	
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processOrder(FixOrder ord)
{
}
public void processCancelReject(FixCancelReject cxlRej)
{
}
public void processStatusRequest(FixOrderStatusRequest stat) 
{
}

/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processQuote(RealTimeQuote quote)
{
}
}
