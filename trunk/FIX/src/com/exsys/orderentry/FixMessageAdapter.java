package com.exsys.orderentry;

import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.common.extrading.*;
import com.exsys.common.business.*;
/**
 * Insert the type's description here.
 * Creation date: (2/5/2002 5:25:31 AM)
 * @author: Administrator
 */
public class FixMessageAdapter implements TradeMessageProcessor {
	private ITradeResponseHandler msgClient = null;
/**
 * Insert the method's description here.
 * Creation date: (2/5/2002 5:26:56 AM)
 * @param client com.exsys.common.extrading.ITradeResponseHandler
 */
public FixMessageAdapter(ITradeResponseHandler client) 
{
	msgClient = client;
}
/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:20:08 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processCancel(Cancel cancel) 
{
	msgClient.ReceivedCancel(cancel);
}
/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:20:08 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processCancel(FixCancel cxl) 
{
	//msgClient.ReceivedCancel(cxl);
	Cancel cancel = new Cancel( cxl, "dummy");
	msgClient.ReceivedCancel( cancel );	
}
public void processCancelReject(FixCancelReject cxlRej) 
{
	//msgClient.ReceivedCancel(cxl);
	//Cancel cancel = new Cancel( cxl, "dummy");
	//msgClient.ReceivedCancel( cancel );	
}
public void processStatusRequest(FixOrderStatusRequest stat) 
{
}

/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:20:08 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processCancelReplace(FixCancelReplace cxr) 
{
	cxr.debugPrint();
	System.out.println("Test --" + cxr.getClOrderID() );
	CancelReplace replace = new CancelReplace( cxr, "dummy");
	msgClient.ReceivedCancelReplace(replace);
}
/**
 * Insert the method's description here.
 * Creation date: (2/5/2002 5:25:31 AM)
 * @param response com.exsys.fix.message.FixExecutionReport
 */
public void processExecutionReport(FixExecutionReport response) 
{
	if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_FILL))
	{
		Fill fill = new Fill( response, false, "dummy" );
		msgClient.ReceivedFill(fill);
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PARTIAL))
	{
		Fill fill = new Fill( response, true, "dummy" );
		msgClient.ReceivedPartialFill(fill);
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_CANCELED))
	{
		Confirm confirm = new Confirm( response,"dummy" );
		msgClient.ReceivedConfirm(confirm);
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_NEW))
	{
		Response accept = new Response( response, IBusinessObject.ACCEPT, "dummy" );
		msgClient.ReceivedReject(accept);
	}
	else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REJECTED))
	{
		Response reject = new Response( response, IBusinessObject.REJECT, "dummy" );
		msgClient.ReceivedReject(reject);
	}
	else
	{
		System.out.println("Unknown Execution Report");
	}

	
}
/**
 * Insert the method's description here.
 * Creation date: (2/5/2002 5:25:31 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processOrder(FixOrder ord) 
{
	// This needs to construct Order object and
	// call ReceivedOrder
	Order order = new Order( ord, "dummy");
	msgClient.ReceivedOrder( order );
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 10:15:38 PM)
 * @param quote com.exsys.common.business.RealTimeQuote
 */
public void processQuote(RealTimeQuote quote) 
{
	msgClient.ReceivedQuote(quote);
}
}
