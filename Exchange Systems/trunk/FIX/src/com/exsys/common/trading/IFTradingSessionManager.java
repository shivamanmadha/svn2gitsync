package com.exsys.common.trading;
import javax.jms.*;
import com.exsys.common.config.*;
import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
/**
 * @author kreddy
 *
 * This interface handles trade message processing
 */
public interface IFTradingSessionManager {

  public void startTradingSession();
  public void stopTradingSession();

  // for both Orders and Execution Reports
  public void sendFixMessage(FixMessage msg, String topic);
  
  public void receiveTradingMessages(TradeMessageProcessor processor, String topic);
}
