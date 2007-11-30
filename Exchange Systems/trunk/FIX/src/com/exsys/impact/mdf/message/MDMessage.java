package com.exsys.impact.mdf.message;

import java.nio.ByteBuffer;

/**
 * <code>MDMessage</code> is the abstract class that should be inherited by all
 * message class. It contains the message type and message body length field which
 * are the first 2 fields in every message..
 *
 * @author David Chen
 * @version %I%, %G%
 * @since 12/12/2006
 */

public abstract class MDMessage
{
   public static final short HEADER_LENGTH = 3;
   protected static final String LOG_FLD_DELIMITER = "|";

	protected ByteBuffer SerializedContent;
	protected char MessageType = ' ';
   protected short MessageBodyLength;
   public static boolean SHORT_LOG_STR_PRE_ALLOCATED = false;
   protected String ShortLogStr = null;

	public abstract ByteBuffer serialize();

	public abstract void deserialize(ByteBuffer content);

   public abstract int getMarketID();

   public abstract String getShortLogStr();

   public short getMessageBodyLength()
   {
      return MessageBodyLength;
   }
   
   public void setMessageBodyLength(short len)
   {
      MessageBodyLength = len;
   }   

   public short getMessageLength()
   {
      return (short)(MessageBodyLength + HEADER_LENGTH);
   }

   public char getMessageType()
   {
      return MessageType;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer();
		str.append("MessageType=");
		str.append(MessageType);
		str.append( LOG_FLD_DELIMITER);
		str.append("MessageBodyLength=");
		str.append(MessageBodyLength);
		str.append(LOG_FLD_DELIMITER);

      return str.toString();
   }

   protected void serializeHeader()
   {
      SerializedContent.put( (byte)MessageType );
      SerializedContent.putShort(MessageBodyLength );
   }

   public String getLogHeaderShortStr()
   {
      StringBuffer str = new StringBuffer();
      str.append(MessageType);
      str.append(LOG_FLD_DELIMITER);
      str.append(MessageBodyLength);
      str.append(LOG_FLD_DELIMITER);
      return str.toString();
   }

   public boolean isImpliedOrderMsg()
   {
      return false;
   }

}
