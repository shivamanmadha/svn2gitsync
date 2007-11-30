package com.exsys.common.trading;

import com.exsys.fix.message.*;
/**
 * @author kreddy
 *
 * This interface handles securities message processing
 */
public interface IFSecuritiesSessionManager {

  public void startSecuritiesSession();
  public void stopSecuritiesSession();

  public void sendFixMessage(FixMessage msg, String topic);

  public void receiveSecuritiesMessages(SecuritiesMessageProcessor processor, String topic);
}
