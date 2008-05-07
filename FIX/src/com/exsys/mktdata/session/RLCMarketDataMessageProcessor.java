package com.exsys.mktdata.session;

import com.exsys.mktdata.message.*;

/**
 * Insert the type's description here.
 * Creation date: (1/31/2002 5:25:16 AM)
 * @author: Administrator
 */
public interface RLCMarketDataMessageProcessor {
/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:19:45 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processMZeroMessage(RLCMZeroMessage msg);
public void processMessage(RLCMessage msg);
public void processM9Message(RLCM9Message rlcMsg);
public void processMGMessage(RLCMGMessage rlcMsg);
public void processMHMessage(RLCMHMessage rlcMsg);
public void processMJMessage(RLCMJMessage rlcMsg);
public void processMIMessage(RLCMIMessage rlcMsg);
public void processMKMessage(RLCMKMessage rlcMsg);
public void processMLMessage(RLCMLMessage rlcMsg);
public void processMMMessage(RLCMMMessage rlcMsg);
public void processMOMessage(RLCMOMessage rlcMsg);
//public void processMUMessage(RLCMUMessage rlcMsg);
public void processM4Message(RLCM4Message rlcMsg);
public void processMAMessage(RLCMAMessage rlcMsg);
public void processMXMessage(RLCMXMessage rlcMsg);
public void processMYMessage(RLCMYMessage rlcMsg);
public void processM5Message(RLCM5Message rlcMsg);
public void processM6Message(RLCM6Message rlcMsg);
public void processM7Message(RLCM7Message rlcMsg);
public void processM8Message(RLCM8Message rlcMsg);
public void process09Message(RLC09Message rlcMsg);
public void process23Message(RLC23Message rlcMsg);
}
