package com.exsys.impact.mdf.message.request;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * RequestFeedByMarketType.java
 * @author David Chen
 */

public class RequestFeedByMarketType extends Request
{
	private static final short MESSAGE_LENGTH = 11;

	public short MarketType;
	public short MarketDepth;

   public RequestFeedByMarketType()
   {
      MessageType = '3';
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
			SerializedContent.putShort( MarketDepth );

			SerializedContent.rewind();
		}

		return SerializedContent;
	}

	public void deserialize( ByteBuffer inboundcontent )
	{
		RequestSeqID = inboundcontent.getInt();
		MarketType = inboundcontent.getShort();
		MarketDepth = inboundcontent.getShort();

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketType=");
		str.append(MarketType);
		str.append( "|");
		str.append("MarketDepth=");
		str.append(MarketDepth);
		str.append( "|");
		return str.toString();
	}


}

