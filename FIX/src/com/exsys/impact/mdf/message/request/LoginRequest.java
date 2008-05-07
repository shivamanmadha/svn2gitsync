package com.exsys.impact.mdf.message.request;

import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.*;

/**
 * LoginRequest.java
 * @author David Chen
 */

public class LoginRequest extends Request
{
	private static final short MESSAGE_LENGTH = 83;

	public char UserName[] = new char[30];
	public char Password[] = new char[30];
	public char GetMarketStats;
	public char MktDataBuffering;
	public char Version[] = new char[12];
    public char GetMessageBundleMarker='N';
    public char GetImpliedOrders = 'Y';

   public LoginRequest()
   {
      MessageType = '1';
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
			for( int i=0; i<UserName.length  ; i++ )
			{
				SerializedContent.put( (byte)UserName[i] );
			}

			for( int i=0; i<Password.length  ; i++ )
			{
				SerializedContent.put( (byte)Password[i] );
			}

			SerializedContent.put( (byte)GetMarketStats );
			SerializedContent.put( (byte)MktDataBuffering );
			for( int i=0; i<Version.length  ; i++ )
			{
				SerializedContent.put( (byte)Version[i] );
			}

			SerializedContent.put( (byte)GetMessageBundleMarker );

         SerializedContent.put( (byte)GetImpliedOrders );

			SerializedContent.rewind();
		}

		return SerializedContent;
	}

	public void deserialize( ByteBuffer inboundcontent )
	{
		RequestSeqID = inboundcontent.getInt();

		for( int i=0; i<UserName.length  ; i++ )
		{
			UserName[i] = (char)inboundcontent.get();
		}

		for( int i=0; i<Password.length  ; i++ )
		{
			Password[i] = (char)inboundcontent.get();
		}
		GetMarketStats = (char)inboundcontent.get();
		MktDataBuffering = (char)inboundcontent.get();

		for( int i=0; i<Version.length  ; i++ )
		{
			Version[i] = (char)inboundcontent.get();
		}

      if (inboundcontent.remaining()>0)
      {
         GetMessageBundleMarker = (char)inboundcontent.get();
      }

      if (inboundcontent.remaining()>0)
      {
         GetImpliedOrders = (char)inboundcontent.get();
      }

	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();

      str.append(super.toString());
		str.append("UserName=");
		str.append(MessageUtil.toString(UserName));
		str.append( "|");
		str.append("Password=");
		str.append("*****");
		str.append( "|");
		str.append("GetMarketStats=");
		str.append(GetMarketStats);
		str.append( "|");
		str.append("MktDataBuffering=");
		str.append(MktDataBuffering);
		str.append( "|");
		str.append("Version=");
		str.append(MessageUtil.toString(Version));
		str.append( "|");
		str.append("GetMessageBundleMarker=");
		str.append(GetMessageBundleMarker);
		str.append( "|");
		str.append("GetImpliedOrders=");
		str.append(GetImpliedOrders);
		str.append( "|");
		return str.toString();
	}

}

