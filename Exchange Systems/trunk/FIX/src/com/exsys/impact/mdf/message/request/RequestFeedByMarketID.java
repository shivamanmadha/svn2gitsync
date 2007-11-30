package com.exsys.impact.mdf.message.request;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * RequestFeedByMarketID.java
 * @author David Chen
 */

public class RequestFeedByMarketID extends Request
{
	private static final short MESSAGE_LENGTH = 709;

	public char MarketIDs[] = new char[700];
	public short MarketDepth;

   public RequestFeedByMarketID()
   {
      MessageType = '4';
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
			for( int i=0; i<MarketIDs.length  ; i++ )
			{
				SerializedContent.put( (byte)MarketIDs[i] );
			}

			SerializedContent.putShort( MarketDepth );

			SerializedContent.rewind();
		}

		return SerializedContent;
	}

	public void deserialize( ByteBuffer inboundcontent )
	{

		RequestSeqID = inboundcontent.getInt();

		for( int i=0; i<MarketIDs.length  ; i++ )
		{
			MarketIDs[i] = (char)inboundcontent.get();
		}
		MarketDepth = inboundcontent.getShort();

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketIDs=");
		str.append(MessageUtil.toString(MarketIDs));
		str.append( "|");
		str.append("MarketDepth=");
		str.append(MarketDepth);
		str.append( "|");
		return str.toString();
	}

}

