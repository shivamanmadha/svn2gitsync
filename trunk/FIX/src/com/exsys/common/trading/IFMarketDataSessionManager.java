package com.exsys.common.trading;

import com.exsys.fix.message.*;
/**
 * @author kreddy
 *
 * This interface handles market data message processing
 */
public interface IFMarketDataSessionManager {

  public void startMarketDataSession();
  public void stopMarketDataSession();

  public void sendFixMessage(FixMessage msg, String topic);

  public void receiveMarketDataMessages(MarketDataMessageProcessor processor, String topic);
}
