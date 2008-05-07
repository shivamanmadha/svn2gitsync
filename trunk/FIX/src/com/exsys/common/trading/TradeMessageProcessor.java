package com.exsys.common.trading;

import com.exsys.fix.message.*;
import com.exsys.common.business.*;
/**
 * This interface defines methods needed by a trade
 * message processor.
 * Creation date: (1/31/2002 5:25:16 AM)
 * @author: Administrator
 */
public interface TradeMessageProcessor {
/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:19:45 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processCancel(Cancel cancel);
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:49 AM)
 * @param response com.exsys.fix.message.FixExecutionReport
 */
public void processExecutionReport(FixExecutionReport response);
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processOrder(FixOrder ord);
//public void processOrder(FixOrder ord, String rawMsg );
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processQuote(RealTimeQuote quote);

/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancel(FixCancel cxl);

/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processStatusRequest(FixOrderStatusRequest stat);
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancelReplace(FixCancelReplace cxr);

public void processCancelReject(FixCancelReject cxlRej);
}
