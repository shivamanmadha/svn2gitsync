package com.exsys.common.trading;

import com.exsys.fix.message.*;
import com.exsys.common.business.*;

public interface TradeRawMessageProcessor extends TradeMessageProcessor
{
  public void processCancel(Cancel cancel, FixMessage fixMsg);
  public void processOrder(FixOrder ord, FixMessage fixMsg);
}
