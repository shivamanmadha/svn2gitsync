package com.exsys.impact.mdf.message.request;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * DebugRequest.java
 * @author David Chen
 */

public class DebugRequest extends Request
{
	private static final short MESSAGE_LENGTH = 7;

   public DebugRequest()
   {
      MessageType = '5';
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

			SerializedContent.rewind();
		}

		return SerializedContent;
	}

	public void deserialize( ByteBuffer inboundcontent )
	{
		RequestSeqID = inboundcontent.getInt();
	}

}

