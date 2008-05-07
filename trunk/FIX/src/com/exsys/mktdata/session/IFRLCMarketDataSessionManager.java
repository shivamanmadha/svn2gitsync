package com.exsys.mktdata.session;
import com.exsys.mktdata.message.*;

public interface IFRLCMarketDataSessionManager {

  public void startMarketDataSession();
  public void stopMarketDataSession();

  // for both Orders and Execution Reports
  public void sendMarketDataMessage(RLCMessage msg, String topic);

  public void receiveMarketDataMessages(RLCMarketDataMessageProcessor processor, String topic);
}
