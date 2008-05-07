package com.exsys.impact.mdf.message;

/**
 * <code>InvalidRequestException</code> is used when request is inavlid.
 *
 * @author David Chen
 * @version %I%, %G%
 * @since 12/12/2006
 */
public class InvalidRequestException extends Exception
{
   public InvalidRequestException(String errMsg)
   {
      super(errMsg);
   }
}
