package com.exsys.common.trading;

import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
/**
 * Insert the type's description here.
 * Creation date: (1/31/2002 5:15:27 AM)
 * @author: Administrator
 */
public interface TradeMessageSender {
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:19:08 AM)
 * @param ord com.exsys.fix.message.FixOrder
 * @exception com.exsys.common.exceptions.SenderException The exception description.
 */
void sendOrder(FixOrder ord) throws com.exsys.common.exceptions.SenderException;
}
