package com.exsys.common.trading;

import com.exsys.fix.message.*;
/**
 * This interface defines methods needed by a news
 * message processor.
 * Creation date: (1/31/2002 5:25:16 AM)
 * @author: Administrator
 */
public interface NewsMessageProcessor {
/**
 * method to process news message received
 * Creation date: (2/8/2002 5:19:45 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processNews(FixNews msg);

}
