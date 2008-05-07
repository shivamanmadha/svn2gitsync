package com.exsys.impact.mdf.message.response;

import com.exsys.impact.mdf.message.MessageUtil;

import java.nio.ByteBuffer;

/**
 * ExtraProductDefinitionResponse.java
 * @author David Chen
 */

public class ExtraProductDefinitionResponse  extends Response
{
	private static final short MESSAGE_LENGTH = 239;

	public int MarketID;
	public char ClearedAlias[] = new char[15];
	public char ProductDesc[] = new char[120];
	public char StripName[] = new char[60];
   public char ProductType[] = new char[10];
   public short LotMultiplier;
   public char LotMultiplierDenominator;
   public int NumBlocks;
   public int BaseNumLots;
   public long TTMarketTickValue;
   public int InitialMargin;

   public ExtraProductDefinitionResponse()
   {
      MessageType = 'x';
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
			SerializedContent.putInt( MarketID );

			for( int i=0; i<ClearedAlias.length  ; i++ )
			{
				SerializedContent.put( (byte)ClearedAlias[i] );
			}

			for( int i=0; i<ProductDesc.length  ; i++ )
			{
				SerializedContent.put( (byte)ProductDesc[i] );
			}

			for( int i=0; i<StripName.length  ; i++ )
			{
				SerializedContent.put( (byte)StripName[i] );
			}

			for( int i=0; i<ProductType.length  ; i++ )
			{
				SerializedContent.put( (byte)ProductType[i] );
			}

         SerializedContent.putShort( LotMultiplier );
         SerializedContent.put( (byte)LotMultiplierDenominator );
         SerializedContent.putInt( NumBlocks );
         SerializedContent.putInt( BaseNumLots );
         SerializedContent.putLong( TTMarketTickValue );
         SerializedContent.putInt( InitialMargin );

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
		MarketID = inboundcontent.getInt();

		for( int i=0; i<ClearedAlias.length  ; i++ )
		{
			ClearedAlias[i] = (char)inboundcontent.get();
		}

		for( int i=0; i<ProductDesc.length  ; i++ )
		{
			ProductDesc[i] = (char)inboundcontent.get();
		}

		for( int i=0; i<StripName.length  ; i++ )
		{
			StripName[i] = (char)inboundcontent.get();
		}

      for( int i=0; i<ProductType.length  ; i++ )
      {
         ProductType[i] = (char)inboundcontent.get();
      }

		LotMultiplier = inboundcontent.getShort();
		LotMultiplierDenominator = (char)inboundcontent.get();


		NumBlocks = inboundcontent.getInt();
		BaseNumLots = inboundcontent.getInt();

		TTMarketTickValue = inboundcontent.getLong();
		InitialMargin = inboundcontent.getInt();

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("ClearedAlias=");
		str.append(MessageUtil.toString(ClearedAlias));
		str.append( "|");
		str.append("ProductDesc=");
		str.append(MessageUtil.toString(ProductDesc));
		str.append( "|");
		str.append("StripName=");
		str.append(MessageUtil.toString(StripName));
		str.append( "|");
		str.append("ProductType=");
		str.append(MessageUtil.toString(ProductType));
		str.append( "|");
		str.append("LotMultiplier=");
		str.append(LotMultiplier);
		str.append( "|");
		str.append("LotMultiplierDenominator=");
		str.append(LotMultiplierDenominator);
		str.append( "|");
		str.append("NumBlocks=");
		str.append(NumBlocks);
		str.append( "|");
		str.append("BaseNumLots=");
		str.append(BaseNumLots);
		str.append( "|");
		str.append("TTMarketTickValue=");
		str.append(TTMarketTickValue);
		str.append( "|");
		str.append("InitialMargin=");
		str.append(InitialMargin);
		str.append( "|");

		return str.toString();
	}

}
