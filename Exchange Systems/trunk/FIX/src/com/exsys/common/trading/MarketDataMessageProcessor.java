package com.exsys.common.trading;

import com.exsys.fix.message.*;
/**
 * This interface defines methods needed by a market data
 * message processor.
 * Creation date: (1/31/2002 5:25:16 AM)
 * @author: Administrator
 */
public interface MarketDataMessageProcessor {

public boolean processMarketDataFullRefresh(FixMarketDataFullRefresh msg);
public void processMarketDataReject(FixMarketDataReject msg);
public boolean processMarketDataIncrementalRefresh(FixMarketDataIncrementalRefresh msg);
public void processMarketDataSecurityStatus(FixSecurityStatus msg);
public void processMarketDataRequest(FixMarketDataRequest msg);
}
