package com.exsys.impact.mdf.message.notification;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * SettlementPriceMessage.java
 * @author David Chen
 */

public class SettlementPriceMessage extends MDMessage
{
	private static final short MESSAGE_LENGTH = 23;

	public int MarketID;
	public long SettlementPrice;
	public long DateTime;

   public SettlementPriceMessage()
   {
      MessageType = 'O';
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
			SerializedContent.putLong( SettlementPrice );
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
			strBuf.append( SettlementPrice );
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
		SettlementPrice = inboundcontent.getLong();
		DateTime = inboundcontent.getLong();

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("SettlementPrice=");
		str.append(SettlementPrice);
		str.append( "|");
		str.append("DateTime=");
		str.append(DateTime);
		str.append( "|");
		return str.toString();
	}

}

