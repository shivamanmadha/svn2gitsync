package com.exsys.common.trading;

import com.exsys.fix.message.*;
/**
 * This interface defines methods needed by a security
 * message processor.
 * Creation date: (1/31/2002 5:25:16 AM)
 * @author: Administrator
 */
public interface SecuritiesMessageProcessor {

public void processSecurityDefinitionResponse(FixSecurityDefinitionResponse msg);
public void processSecurityDefinitionRequest(FixSecurityDefinitionRequest msg);
}
