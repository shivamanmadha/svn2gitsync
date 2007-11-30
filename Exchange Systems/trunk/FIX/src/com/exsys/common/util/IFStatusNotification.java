package com.exsys.common.util;

/**
 * helper class interface to handle notifications
 */
public interface IFStatusNotification {

  public void info(String msg);
  public void warn(String msg);
  public void debug(String msg);
  public void fatal(String msg);
  public void error(String msg);

}
