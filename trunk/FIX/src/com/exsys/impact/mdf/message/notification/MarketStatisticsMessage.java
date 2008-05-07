package com.exsys.impact.mdf.message.notification;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * MarketStatisticsMessage.java
 * @author David Chen
 */

public class MarketStatisticsMessage extends MDMessage
{
	private static final short MESSAGE_LENGTH = 55;

	public int MarketID;
	public int TotalVolume;
	public int BlockVolume;
	public int EFSVolume;
	public int EFPVolume;
	public long High;
	public long Low;
	public long VWAP;
	public long DateTime;

   public MarketStatisticsMessage()
   {
      MessageType = 'J';
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
			SerializedContent.putInt( TotalVolume );
			SerializedContent.putInt( BlockVolume );
			SerializedContent.putInt( EFSVolume );
			SerializedContent.putInt( EFPVolume );
			SerializedContent.putLong( High );
			SerializedContent.putLong( Low );
			SerializedContent.putLong( VWAP );
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
			strBuf.append( TotalVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( BlockVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( EFSVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( EFPVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( High );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( Low );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( VWAP );
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
		TotalVolume = inboundcontent.getInt();
		BlockVolume = inboundcontent.getInt();
		EFSVolume = inboundcontent.getInt();
		EFPVolume = inboundcontent.getInt();
		High = inboundcontent.getLong();
		Low = inboundcontent.getLong();
		VWAP = inboundcontent.getLong();
		DateTime = inboundcontent.getLong();
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("TotalVolume=");
		str.append(TotalVolume);
		str.append( "|");
		str.append("BlockVolume=");
		str.append(BlockVolume);
		str.append( "|");
		str.append("EFSVolume=");
		str.append(EFSVolume);
		str.append( "|");
		str.append("EFPVolume=");
		str.append(EFPVolume);
		str.append( "|");
		str.append("High=");
		str.append(High);
		str.append( "|");
		str.append("Low=");
		str.append(Low);
		str.append( "|");
		str.append("VWAP=");
		str.append(VWAP);
		str.append( "|");
		str.append("DateTime=");
		str.append(DateTime);
		str.append( "|");
		return str.toString();
	}

}

