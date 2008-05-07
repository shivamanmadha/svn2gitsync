package com.exsys.impact.mdf.message.request;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * ProductDefinitionRequest.java
 * @author David Chen
 */

public class ProductDefinitionRequest extends Request
{
	private static final short MESSAGE_LENGTH = 9;
	public short MarketType;

   public ProductDefinitionRequest()
   {
      MessageType = '2';
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
         SerializedContent.putShort(MarketType );

			SerializedContent.rewind();
		}

		return SerializedContent;
	}

	public void deserialize( ByteBuffer inboundcontent )
	{
		RequestSeqID = inboundcontent.getInt();
      MarketType = inboundcontent.getShort();
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketType=");
		str.append(MarketType);
		str.append( "|");
		return str.toString();
	}

}

