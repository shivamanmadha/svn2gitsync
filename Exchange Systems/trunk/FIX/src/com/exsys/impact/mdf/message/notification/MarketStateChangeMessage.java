package com.exsys.impact.mdf.message.notification;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * MarketStateChangeMessage.java
 * @author David Chen
 */

public class MarketStateChangeMessage extends MDMessage
{
	private static final short MESSAGE_LENGTH = 16;

	public int MarketID;
	public char TradingStatus;
	public long DateTime;

   public MarketStateChangeMessage()
   {
      MessageType = 'K';
   }

   public int getMarketID()
   {
      return MarketID;
   }

	public ByteBuffer serialize()
	{
		// Buffer is pre-serialized, so that serialization occurs only once.
		if( SerializedContent == null )
		{
			SerializedContent = ByteBuffer.allocate( MESSAGE_LENGTH );
         MessageBodyLength = MESSAGE_LENGTH - HEADER_LENGTH;

         serializeHeader();
			SerializedContent.putInt( MarketID );
			SerializedContent.put( (byte)TradingStatus );
			SerializedContent.putLong( DateTime );

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

         strBuf.append( MarketID );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( TradingStatus );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( DateTime );
         strBuf.append( LOG_FLD_DELIMITER );

         ShortLogStr = strBuf.toString();
      }

      return ShortLogStr;
   }


	public void deserialize( ByteBuffer inboundcontent )
	{
		MarketID = inboundcontent.getInt();
		TradingStatus = (char)inboundcontent.get();
		DateTime = inboundcontent.getLong();

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("TradingStatus=");
		str.append(TradingStatus);
		str.append( "|");
		str.append("DateTime=");
		str.append(DateTime);
		str.append( "|");
		return str.toString();
	}

}

