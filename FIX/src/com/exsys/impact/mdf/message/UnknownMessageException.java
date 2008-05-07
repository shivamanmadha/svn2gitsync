package com.exsys.impact.mdf.message;

/**
 * <code>UnknownMessageException</code> is used when message is inavlid.
 *
 * @author David Chen
 * @version %I%, %G%
 * @since 12/12/2006
 */

public class UnknownMessageException extends Exception
{
   public UnknownMessageException(String errMsg)
   {
      super(errMsg);
   }
}
