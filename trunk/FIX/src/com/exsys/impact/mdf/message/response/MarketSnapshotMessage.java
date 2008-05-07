package com.exsys.impact.mdf.message.response;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * MarketSnapshotMessage.java
 * @author David Chen
 */

public class MarketSnapshotMessage  extends Response
{
	private static final short MESSAGE_LENGTH = 78;

	public short MarketType;
	public int MarketID;
	public char TradingStatus;
	public int TotalVolume;
	public int BlockVolume;
	public int EFSVolume;
	public int EFPVolume;
	public int OpenInterest;
	public long OpeningPrice;
	public long SettlementPrice;
	public long High;
	public long Low;
	public long VWAP;
	public int NumOfBookEntries;

   public MarketSnapshotMessage()
   {
      MessageType = 'C';
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
			SerializedContent.put( (byte)TradingStatus );
			SerializedContent.putInt( TotalVolume );
			SerializedContent.putInt( BlockVolume );
			SerializedContent.putInt( EFSVolume );
			SerializedContent.putInt( EFPVolume );
			SerializedContent.putInt( OpenInterest );
			SerializedContent.putLong( OpeningPrice );
			SerializedContent.putLong( SettlementPrice );
			SerializedContent.putLong( High );
			SerializedContent.putLong( Low );
			SerializedContent.putLong( VWAP );
			SerializedContent.putInt( NumOfBookEntries );

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
			strBuf.append(  MarketID );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( (byte)TradingStatus );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append(  TotalVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append(  BlockVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append(  EFSVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append(  EFPVolume );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append(  OpenInterest );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append(  OpeningPrice );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( SettlementPrice );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( High );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( Low );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( VWAP );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append(  NumOfBookEntries );
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
		TradingStatus = (char)inboundcontent.get();
		TotalVolume = inboundcontent.getInt();
		BlockVolume = inboundcontent.getInt();
		EFSVolume = inboundcontent.getInt();
		EFPVolume = inboundcontent.getInt();
		OpenInterest = inboundcontent.getInt();
		OpeningPrice = inboundcontent.getLong();
		SettlementPrice = inboundcontent.getLong();
		High = inboundcontent.getLong();
		Low = inboundcontent.getLong();
		VWAP = inboundcontent.getLong();
		NumOfBookEntries = inboundcontent.getInt();

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
		str.append("TradingStatus=");
		str.append(TradingStatus);
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
		str.append("OpenInterest=");
		str.append(OpenInterest);
		str.append( "|");
		str.append("OpeningPrice=");
		str.append(OpeningPrice);
		str.append( "|");
		str.append("SettlementPrice=");
		str.append(SettlementPrice);
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
		str.append("NumOfBookEntries=");
		str.append(NumOfBookEntries);
		str.append( "|");
		return str.toString();
	}

}

