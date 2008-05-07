package com.exsys.impact.mdf.message.response;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * ProductDefinitionResponse.java
 * @author David Chen
 */

public class ProductDefinitionResponse  extends Response
{
	private static final short MESSAGE_LENGTH = 247;

	public short RequestMarketType;
	public short NumOfMarkets;
	public int MarketID;
	public char ContractSymbol[] = new char[35];
	public char TradingStatus;
	public char OrderPriceDenominator;
	public int IncrementPrice;
	public int IncrementQty;
	public int LotSize;
	public char MarketDesc[] = new char[120];
	public short MaturityYear;
	public short MaturityMonth;
	public short MaturityDay;
	public char IsSpread;
	public char IsCrackSpread;
	public int PrimaryMarketID;
	public int SecondaryMarketID;
	public char IsOptions;
	public char OptionType;
	public long StrikePrice;
	public long SecondStrike;
   public char DealPriceDenominator;
   public int MinQty;
   public int UnitQuantity;
   public char Currency[] = new char[20];

   public ProductDefinitionResponse()
   {
      MessageType = 'B';
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
			SerializedContent.putShort( RequestMarketType );
			SerializedContent.putShort( NumOfMarkets );
			SerializedContent.putInt( MarketID );
			for( int i=0; i<ContractSymbol.length  ; i++ )
			{
				SerializedContent.put( (byte)ContractSymbol[i] );
			}

			SerializedContent.put( (byte)TradingStatus );
			SerializedContent.put( (byte)OrderPriceDenominator );
			SerializedContent.putInt( IncrementPrice );
			SerializedContent.putInt( IncrementQty );
			SerializedContent.putInt( LotSize );
			for( int i=0; i<MarketDesc.length  ; i++ )
			{
				SerializedContent.put( (byte)MarketDesc[i] );
			}

			SerializedContent.putShort( MaturityYear );
			SerializedContent.putShort( MaturityMonth );
			SerializedContent.putShort( MaturityDay );
			SerializedContent.put( (byte)IsSpread );
			SerializedContent.put( (byte)IsCrackSpread );
			SerializedContent.putInt( PrimaryMarketID );
			SerializedContent.putInt( SecondaryMarketID );
			SerializedContent.put( (byte)IsOptions );
			SerializedContent.put( (byte)OptionType );
			SerializedContent.putLong( StrikePrice );
			SerializedContent.putLong( SecondStrike );

         SerializedContent.put( (byte)DealPriceDenominator );
         SerializedContent.putInt( MinQty );
         SerializedContent.putInt ( UnitQuantity );

			for( int i=0; i<Currency.length  ; i++ )
			{
				SerializedContent.put( (byte)Currency[i] );
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
      // too much for logging, and it is pretty static, just
      // return null
      return null;
   }

	public void deserialize( ByteBuffer inboundcontent )
	{
		RequestSeqID = inboundcontent.getInt();
		RequestMarketType = inboundcontent.getShort();
		NumOfMarkets = inboundcontent.getShort();
		MarketID = inboundcontent.getInt();

		for( int i=0; i<ContractSymbol.length  ; i++ )
		{
			ContractSymbol[i] = (char)inboundcontent.get();
		}
		TradingStatus = (char)inboundcontent.get();
		OrderPriceDenominator = (char)inboundcontent.get();
		IncrementPrice = inboundcontent.getInt();
		IncrementQty = inboundcontent.getInt();
		LotSize = inboundcontent.getInt();

		for( int i=0; i<MarketDesc.length  ; i++ )
		{
			MarketDesc[i] = (char)inboundcontent.get();
		}
		MaturityYear = inboundcontent.getShort();
		MaturityMonth = inboundcontent.getShort();
		MaturityDay = inboundcontent.getShort();
		IsSpread = (char)inboundcontent.get();
		IsCrackSpread = (char)inboundcontent.get();
		PrimaryMarketID = inboundcontent.getInt();
		SecondaryMarketID = inboundcontent.getInt();
		IsOptions = (char)inboundcontent.get();
		OptionType = (char)inboundcontent.get();
		StrikePrice = inboundcontent.getLong();
		SecondStrike = inboundcontent.getLong();

      DealPriceDenominator = (char)inboundcontent.get();
      MinQty = inboundcontent.getInt();
      UnitQuantity = inboundcontent.getInt();
		for( int i=0; i<Currency.length  ; i++ )
		{
			Currency[i] = (char)inboundcontent.get();
		}
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("RequestMarketType=");
		str.append(RequestMarketType);
		str.append( "|");
		str.append("NumOfMarkets=");
		str.append(NumOfMarkets);
		str.append( "|");
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("ContractSymbol=");
		str.append(MessageUtil.toString(ContractSymbol));
		str.append( "|");
		str.append("TradingStatus=");
		str.append(TradingStatus);
		str.append( "|");
		str.append("PriceDenominator=");
		str.append(OrderPriceDenominator);
		str.append( "|");
		str.append("IncrementPrice=");
		str.append(IncrementPrice);
		str.append( "|");
		str.append("IncrementQty=");
		str.append(IncrementQty);
		str.append( "|");
		str.append("LotSize=");
		str.append(LotSize);
		str.append( "|");
		str.append("MarketDesc=");
		str.append(MessageUtil.toString(MarketDesc));
		str.append( "|");
		str.append("MaturityYear=");
		str.append(MaturityYear);
		str.append( "|");
		str.append("MaturityMonth=");
		str.append(MaturityMonth);
		str.append( "|");
		str.append("MaturityDay=");
		str.append(MaturityDay);
		str.append( "|");
		str.append("IsSpread=");
		str.append(IsSpread);
		str.append( "|");
		str.append("IsCrackSpread=");
		str.append(IsCrackSpread);
		str.append( "|");
		str.append("PrimaryMarketID=");
		str.append(PrimaryMarketID);
		str.append( "|");
		str.append("SecondaryMarketID=");
		str.append(SecondaryMarketID);
		str.append( "|");
		str.append("IsOptions=");
		str.append(IsOptions);
		str.append( "|");
		str.append("OptionType=");
		str.append(OptionType);
		str.append( "|");
		str.append("StrikePrice=");
		str.append(StrikePrice);
		str.append( "|");
		str.append("DealPriceDenominator=");
		str.append(DealPriceDenominator);
		str.append( "|");
		str.append("MinQty=");
		str.append(MinQty);
		str.append( "|");
		str.append("UnitQuantity=");
		str.append(UnitQuantity);
		str.append( "|");
		str.append("Currency=");
		str.append(MessageUtil.toString(Currency));
		str.append( "|");

		return str.toString();
	}

}

