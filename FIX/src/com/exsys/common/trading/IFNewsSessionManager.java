package com.exsys.common.trading;

import com.exsys.fix.message.*;
/**
 * @author kreddy
 *
 * This interface handles news message processing
 */
public interface IFNewsSessionManager {

  public void startNewsSession();
  public void stopNewsSession();
  
  public void sendFixMessage(FixMessage msg, String topic);

  public void receiveNewsMessages(NewsMessageProcessor processor, String topic);
}
