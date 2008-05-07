package com.exsys.impact.mdf.message.response;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * DebugResponse.java
 * @author David Chen
 */

public class DebugResponse extends Response
{
   private static final short MESSAGE_LENGTH = 67;
	public char Text[] = new char[60];

   public DebugResponse()
   {
      MessageType = 'P';
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
         SerializedContent.putInt( RequestSeqID );
         for( int i=0; i<Text.length  ; i++ )
         {
            SerializedContent.put( (byte)Text[i] );
         }

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

			strBuf.append( RequestSeqID );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( MessageUtil.toString(Text) );
         strBuf.append( LOG_FLD_DELIMITER );

         ShortLogStr = strBuf.toString();
      }

      return ShortLogStr;
   }

   public void deserialize( ByteBuffer inboundcontent )
   {

      RequestSeqID = inboundcontent.getInt();

      for( int i=0; i<Text.length  ; i++ )
      {
         Text[i] = (char)inboundcontent.get();
      }

   }

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("Text=");
		str.append(MessageUtil.toString(Text));
		str.append( "|");
		return str.toString();
	}

}

