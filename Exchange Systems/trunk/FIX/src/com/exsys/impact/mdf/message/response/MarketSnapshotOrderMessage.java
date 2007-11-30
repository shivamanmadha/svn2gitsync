package com.exsys.impact.mdf.message.response;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * MarketSnapshotOrderMessage.java
 * @author David Chen
 */

public class MarketSnapshotOrderMessage  extends Response
{
   private static final short MESSAGE_LENGTH = 46;

	public short MarketType;
	public int MarketID;
	public long OrderID;
	public short OrderSeqID;
	public char Side;
	public long Price;
	public int Quantity;
	public char Implied;
	public char IsRFQ;
	public long DateTime;

   public MarketSnapshotOrderMessage()
   {
      MessageType = 'D';
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
         SerializedContent.putInt( RequestSeqID );
         SerializedContent.putShort( MarketType );
         SerializedContent.putInt( MarketID );
         SerializedContent.putLong( OrderID );
         SerializedContent.putShort( OrderSeqID );
         SerializedContent.put( (byte)Side );
         SerializedContent.putLong( Price );
         SerializedContent.putInt( Quantity );
         SerializedContent.put( (byte)Implied );
         SerializedContent.put( (byte)IsRFQ );
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

			strBuf.append( RequestSeqID );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( MarketType );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( MarketID );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( OrderID );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( OrderSeqID );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( Side );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( Price );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( Quantity );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( Implied );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( IsRFQ );
         strBuf.append( LOG_FLD_DELIMITER );
         strBuf.append( DateTime );
         strBuf.append( LOG_FLD_DELIMITER );

         ShortLogStr = strBuf.toString();
      }

      return ShortLogStr;
   }

   public void deserialize( ByteBuffer inboundcontent )
   {
      RequestSeqID = inboundcontent.getInt();
      MarketType = inboundcontent.getShort();
      MarketID = inboundcontent.getInt();
      OrderID = inboundcontent.getLong();
      OrderSeqID = inboundcontent.getShort();
      Side = (char)inboundcontent.get();
      Price = inboundcontent.getLong();
      Quantity = inboundcontent.getInt();
      Implied = (char)inboundcontent.get();
      IsRFQ = (char)inboundcontent.get();
      DateTime = inboundcontent.getLong();

   }


	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketType=");
		str.append(MarketType);
		str.append( "|");
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("OrderID=");
		str.append(OrderID);
		str.append( "|");
		str.append("OrderSeqID=");
		str.append(OrderSeqID);
		str.append( "|");
		str.append("Side=");
		str.append(Side);
		str.append( "|");
		str.append("Price=");
		str.append(Price);
		str.append( "|");
		str.append("Quantity=");
		str.append(Quantity);
		str.append( "|");
		str.append("Implied=");
		str.append(Implied);
		str.append( "|");
		str.append("IsRFQ=");
		str.append(IsRFQ);
		str.append( "|");
		str.append("DateTime=");
		str.append(DateTime);
		str.append( "|");
		return str.toString();
	}

}

