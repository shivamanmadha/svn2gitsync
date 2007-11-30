package com.exsys.common.util;

/**
 * interface to support a queue
 * Creation date: (6/5/01 3:04:28 AM)
 * @author: Administrator
 */
public interface DequeueAdapter {
/**
 * method to handle message dequeued from the queue
 * Creation date: (6/5/01 3:06:24 AM)
 * @param msg java.lang.Object
 */
public void dequeuedMessage(Object msg);
}
