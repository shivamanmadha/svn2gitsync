package com.exsys.common.trading;

import com.exsys.impact.message.*;
import com.exsys.impact.mdf.message.*;
/**
 * @author kreddy
 *
 * This interface handles market data message processing
 */
public interface IFImpactSessionManager {

  public void startImpactSession();
  public void stopImpactSession();

  public void sendImpactMessage(byte[] msg, String topic);
  public void sendImpactMessage(byte[] msg,int length, String topic);
  public void sendImpactMessage(MDMessage msg, String topic);
  

  public void receiveImpactMessages(ImpactMessageProcessor processor, String topic);
}
