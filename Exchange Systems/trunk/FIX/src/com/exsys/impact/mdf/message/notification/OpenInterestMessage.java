package com.exsys.impact.mdf.message.notification;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * OpenInterestMessage.java
 * @author David Chen
 */

public class OpenInterestMessage extends MDMessage
{
	private static final short MESSAGE_LENGTH = 23;

	public int MarketID;
	public int OpenInterest;
	public int OpenInterestChange;
	public long DateTime;

   public OpenInterestMessage()
   {
      MessageType = 'M';
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
			SerializedContent.putInt( OpenInterest );
			SerializedContent.putInt( OpenInterestChange );
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
			strBuf.append( OpenInterest );
         strBuf.append( LOG_FLD_DELIMITER );
			strBuf.append( OpenInterestChange );
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
		OpenInterest = inboundcontent.getInt();
		OpenInterestChange = inboundcontent.getInt();
		DateTime = inboundcontent.getLong();

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("MarketID=");
		str.append(MarketID);
		str.append( "|");
		str.append("OpenInterest=");
		str.append(OpenInterest);
		str.append( "|");
		str.append("OpenInterestChange=");
		str.append(OpenInterestChange);
		str.append( "|");
		str.append("DateTime=");
		str.append(DateTime);
		str.append( "|");
		return str.toString();
	}
}

