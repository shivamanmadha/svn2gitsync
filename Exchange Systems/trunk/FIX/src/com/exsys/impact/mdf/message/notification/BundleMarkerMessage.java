package com.exsys.impact.mdf.message.notification;

import com.exsys.impact.mdf.message.MDMessage;

import java.nio.ByteBuffer;

/**
 * BundleMarkerMessage.java
 * @author David Chen
 */

public class BundleMarkerMessage  extends MDMessage
{
   private static final short MESSAGE_LENGTH = 4;
   public char MarkerType;

   public BundleMarkerMessage()
   {
      MessageType = 'T';
   }

   public int getMarketID()
   {
      return -1;
   }

   public ByteBuffer serialize()
   {
      // Buffer is pre-serialized, so that serialization occurs only once.
      if( SerializedContent == null )
      {
         SerializedContent = ByteBuffer.allocate( MESSAGE_LENGTH );
         MessageBodyLength = MESSAGE_LENGTH - HEADER_LENGTH;

         serializeHeader();
         SerializedContent.put( (byte) MarkerType );

         SerializedContent.rewind();

         if (SHORT_LOG_STR_PRE_ALLOCATED)
         {
            getShortLogStr();
         }
      }

      return SerializedContent;
   }

   public String getShortLogStr()
   {
      if (ShortLogStr==null)
      {
         StringBuffer strBuf = new StringBuffer();
         strBuf.append( getLogHeaderShortStr());
         strBuf.append( MarkerType );
         strBuf.append( LOG_FLD_DELIMITER );
         ShortLogStr = strBuf.toString();
      }

      return ShortLogStr;
   }

   public void deserialize( ByteBuffer inboundcontent )
   {
      MarkerType = (char) inboundcontent.get();
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer();

      str.append(super.toString());
      str.append("MarkerType=");
      str.append(MarkerType);
      str.append( "|");
      return str.toString();
   }

}
