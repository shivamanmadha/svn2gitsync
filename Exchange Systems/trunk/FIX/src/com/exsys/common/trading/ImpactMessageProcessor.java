package com.exsys.common.trading;

import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;

/**
 * This interface defines methods needed by a market data
 * message processor.
 * Creation date: (1/31/2002 5:25:16 AM)
 * @author: Administrator
 */
public interface ImpactMessageProcessor {

public boolean processProductDefinitionResponse(ProductDefinitionResponse msg);
public boolean processProductDefinitionResponse(byte[] msg);
public boolean processProductDefinitionRequest(ProductDefinitionRequest msg);

public boolean processMarketDataRequest(RequestFeedByMarketID mdReq);





public boolean processMarketSnapshot(MarketSnapshotMessage msg);
public boolean processMarketSnapshotOrder(MarketSnapshotOrderMessage msg);
public boolean processAddModifyOrder(AddModifyOrderMessage msg);
public boolean processDeleteOrder(DeleteOrderMessage msg);
public boolean processTrade(TradeMessage msg);
public boolean processCancelledTrade(CancelledTradeMessage msg);
public boolean processMarketStatistics(MarketStatisticsMessage msg);
public boolean processOpenPrice(OpenPriceMessage msg);
public boolean processOpenInterest(OpenInterestMessage msg);
public boolean processSettlementPrice(SettlementPriceMessage msg);
public boolean processMarketStateChange(MarketStateChangeMessage msg);

public boolean processMarketSnapshot(byte[] msg);
public boolean processMarketSnapshotOrder(byte[] msg);
public boolean processAddModifyOrder(byte[] msg);
public boolean processDeleteOrder(byte[] msg);
public boolean processTrade(byte[] msg);
public boolean processCancelledTrade(byte[] msg);
public boolean processMarketStatistics(byte[] msg);
public boolean processOpenPrice(byte[] msg);
public boolean processOpenInterest(byte[] msg);
public boolean processSettlementPrice(byte[] msg);
public boolean processMarketStateChange(byte[] msg);

}
