package com.exsys.impact.mdf.message.notification;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * CancelledTradeMessage.java
 * @author David Chen
 */

public class CancelledTradeMessage extends MDMessage
{
   private static final short MESSAGE_LENGTH = 36;

	public int MarketID;
	public long OrderID;
	public long Price;
	public int Quantity;
	public char BlockTradeType;
	public long DateTime;

   public CancelledTradeMessage()
   {
      MessageType = 'I';
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
			SerializedContent.putLong( OrderID );
			SerializedContent.putLong( Price );
			SerializedContent.putInt( Quantity );
			SerializedContent.put( (byte)BlockTradeType );
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
			strBuf.append( OrderID );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( Price );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( Quantity );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( BlockTradeType );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( DateTime );
         ShortLogStr = strBuf.toString();
      }

      return ShortLogStr;
   }

	public void deserialize( ByteBuffer inboundcontent )
	{
		MarketID = inboundcontent.getInt();
		OrderID = inboundcontent.getLong();
		Price = inboundcontent.getLong();
		Quantity = inboundcontent.getInt();
		BlockTradeType = (char)inboundcontent.get();
		DateTime = inboundcontent.getLong();

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("OrderID=");
		str.append(OrderID);
		str.append( "|");
		str.append("Price=");
		str.append(Price);
		str.append( "|");
		str.append("Quantity=");
		str.append(Quantity);
		str.append( "|");
		str.append("BlockTradeType=");
		str.append(BlockTradeType);
		str.append( "|");
		str.append("DateTime=");
		str.append(DateTime);
		str.append( "|");
		return str.toString();
	}

}

