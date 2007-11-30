package com.exsys.impact.mdf.message;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;

/**
 * Message factory class that would create message objects from inputstream
 *
 * @author David Chen
 * @version 1.0
 * @created 05-Dec-2006 15:01:54
 */

public class RawMessageFactory
{
	public final static char DebugRequestType                = '5';
	public final static char DebugResponseType               = 'P';
	public final static char ErrorResponseType               = 'S';
	public final static char LoginRequestType                = '1';
	public final static char LoginResponseType               = 'A';
	public final static char LogoutRequestType               = '6';
	public final static char LogoutResponseType              = 'R';
	public final static char MarketSnapshotMessageType       = 'C';
	public final static char MarketSnapshotOrderMessageType  = 'D';
	public final static char ProductDefinitionRequestType    = '2';
	public final static char ProductDefinitionResponseType   = 'B';
	public final static char RequestFeedByMarketIDType       = '4';
	public final static char RequestFeedByMarketTypeType     = '3';
	public final static char AddModifyOrderMessageType       = 'E';
	public final static char CancelledTradeMessageType       = 'I';
	public final static char DeleteOrderMessageType          = 'F';
	public final static char HeartBeatMessageType            = 'Q';
	public final static char InvestigatedTradeMessageType    = 'H';
	public final static char MarketStateChangeMessageType    = 'K';
	public final static char MarketStatisticsMessageType     = 'J';
	public final static char OpenInterestMessageType         = 'M';
	public final static char OpenPriceMessageType            = 'N';
	public final static char SettlementPriceMessageType      = 'O';
	public final static char SystemTextMessageType           = 'L';
	public final static char TradeMessageType                = 'G';
	public final static char BundleMarkerMessageType         = 'T';

	public final static char LongProductDefinitionRequestType     = 'a';
	public final static char ExtraProductDefinitionResponseType   = 'x';

   public final static short MAX_MESSAGE_BODY_LENGTH = 1024;

   private RawMessageFactory()
   {
      // Singleton factory method - no need to instantiate the class
   }

	public static MDMessage getObject( DataInputStream inputStream )
      throws IOException, UnknownMessageException, InvalidRequestException
	{
		MDMessage theBase = null;

		// read the message type and body length
      byte[] bytes = new byte[3];
      inputStream.readFully(bytes);

      // instantiate an empty object
      byte messageType = bytes[0];
		theBase = getObject( messageType );
      short bodyLength;
      if ((theBase!=null) && (theBase.getMessageType()==DebugRequestType))
      {
         // treat debug request differently so that it works when it is sent through
         // telnet or F5 because it is hard to manipulate the ASCII string to come up
         // with a binary short value of 4, just use the hardcoded FIXED LENGTH
         bodyLength = 4;
      }
      else
      {
         // 2nd and 3rd byte are used for body length
         bodyLength = ByteBuffer.wrap(bytes, 1, 2).getShort();
      }

      // make sure that length value is reasonable, most likely it could
      // be programmatic error or potential attack from client in requests
      if (bodyLength > MAX_MESSAGE_BODY_LENGTH)
      {
         throw new InvalidRequestException("Invalid request, message body length: " + bodyLength + ", over the limit.");
      }

		// read the body with the length received
		byte messageBodyBytes[] = new byte[bodyLength];
		inputStream.readFully(messageBodyBytes, 0, bodyLength);

      if (theBase!=null)
      {
         theBase.MessageBodyLength = bodyLength;

         // deserialize the body
         theBase.deserialize(ByteBuffer.wrap(messageBodyBytes));
      }
      else
      {
         throw new UnknownMessageException("Unknown message type: " + (char) messageType);
      }

		return theBase;
	}

	public static MDMessage getObject( byte[] message )
      throws IOException, UnknownMessageException, InvalidRequestException
	{
		MDMessage theBase = null;

      // instantiate an  object
      byte messageType = message[0];
		theBase = getObject( messageType );
      short bodyLength = (short)(message.length - 3);


      if (theBase!=null)
      {
         theBase.MessageBodyLength = bodyLength;

         // deserialize the body
         byte messageBodyBytes[] = new byte[bodyLength];
         System.arraycopy(message,3,messageBodyBytes,0,bodyLength);
         theBase.deserialize(ByteBuffer.wrap(messageBodyBytes));
      }
      else
      {
         throw new UnknownMessageException("Unknown message type: " + (char) messageType);
      }

		return theBase;
	}


	public static byte[] getMessageBytes( DataInputStream inputStream )
      throws IOException
	{
		// read the message type and body length
      byte[] bytes = new byte[3];
      inputStream.readFully(bytes);

      // instantiate an empty object
      byte messageType = bytes[0];
      short bodyLength;
      if ((char)messageType == DebugRequestType)
      {
         // treat debug request differently so that it works when it is sent through
         // telnet or F5 because it is hard to manipulate the ASCII string to come up
         // with a binary short value of 4, just use the hardcoded FIXED LENGTH
         bodyLength = 4;
      }
      else
      {
         // 2nd and 3rd byte are used for body length
         bodyLength = ByteBuffer.wrap(bytes, 1, 2).getShort();
      }

		// read the body with the length received
		byte[] messageBodyBytes = new byte[bodyLength];
		inputStream.readFully(messageBodyBytes, 0, bodyLength);

		byte[] combined = new byte[3+bodyLength];
		System.arraycopy(bytes,0,combined,0,3);
		System.arraycopy(messageBodyBytes,0,combined,3,bodyLength);
		return combined;
	}


	public static MDMessage getObject( byte theMessageType ) throws UnknownMessageException
   {
		MDMessage theBase = null;

		switch( (char) theMessageType )
		{

			case DebugRequestType:
				theBase = new DebugRequest();
			   break;

			case DebugResponseType:
				theBase = new DebugResponse();
            break;

			case ErrorResponseType:
				theBase = new ErrorResponse();
			   break;

			case LoginRequestType:
				theBase = new LoginRequest();
			   break;

			case LoginResponseType:
				theBase = new LoginResponse();
			   break;

			case LogoutRequestType:
				theBase = new LogoutRequest();
			   break;

			case MarketSnapshotMessageType:
				theBase = new MarketSnapshotMessage();
			   break;

			case MarketSnapshotOrderMessageType:
				theBase = new MarketSnapshotOrderMessage();
			   break;

			case ProductDefinitionRequestType:
				theBase = new ProductDefinitionRequest();
			   break;

			case ProductDefinitionResponseType:
				theBase = new ProductDefinitionResponse();
			   break;

			case RequestFeedByMarketIDType:
				theBase = new RequestFeedByMarketID();
			   break;

			case RequestFeedByMarketTypeType:
				theBase = new RequestFeedByMarketType();
			   break;

			case AddModifyOrderMessageType:
				theBase = new AddModifyOrderMessage();
			   break;

			case CancelledTradeMessageType:
				theBase = new CancelledTradeMessage();
			   break;

			case DeleteOrderMessageType:
				theBase = new DeleteOrderMessage();
			   break;

			case HeartBeatMessageType:
				theBase = new HeartBeatMessage();
			   break;

			case InvestigatedTradeMessageType:
				theBase = new InvestigatedTradeMessage();
			   break;

			case MarketStateChangeMessageType:
				theBase = new MarketStateChangeMessage();
			   break;

			case MarketStatisticsMessageType:
				theBase = new MarketStatisticsMessage();
			   break;

			case OpenInterestMessageType:
				theBase = new OpenInterestMessage();
			   break;

			case OpenPriceMessageType:
				theBase = new OpenPriceMessage();
			   break;

			case SettlementPriceMessageType:
				theBase = new SettlementPriceMessage();
			   break;

			case SystemTextMessageType:
				theBase = new SystemTextMessage();
			   break;

			case TradeMessageType:
				theBase = new TradeMessage();
			   break;

			case BundleMarkerMessageType:
				theBase = new BundleMarkerMessage();
			   break;

			case LongProductDefinitionRequestType:
				theBase = new LongProductDefinitionRequest();
			   break;

			case ExtraProductDefinitionResponseType:
				theBase = new ExtraProductDefinitionResponse();
			   break;

         default:
            // unknow message, don't know how to instantiate, set it to null
            theBase = null;

		}
		return theBase;
	}
	
	public static boolean isValid( byte theMessageType )
   {
		boolean valid = true;

		switch( (char) theMessageType )
		{

			case DebugRequestType:
				//theBase = new DebugRequest();
			   break;

			case DebugResponseType:
				//theBase = new DebugResponse();
            break;

			case ErrorResponseType:
				//theBase = new ErrorResponse();
			   break;

			case LoginRequestType:
				//theBase = new LoginRequest();
			   break;

			case LoginResponseType:
				//theBase = new LoginResponse();
			   break;

			case LogoutRequestType:
				//theBase = new LogoutRequest();
			   break;

			case MarketSnapshotMessageType:
				//theBase = new MarketSnapshotMessage();
			   break;

			case MarketSnapshotOrderMessageType:
				//theBase = new MarketSnapshotOrderMessage();
			   break;

			case ProductDefinitionRequestType:
				//theBase = new ProductDefinitionRequest();
			   break;

			case ProductDefinitionResponseType:
				//theBase = new ProductDefinitionResponse();
			   break;

			case RequestFeedByMarketIDType:
				//theBase = new RequestFeedByMarketID();
			   break;

			case RequestFeedByMarketTypeType:
				//theBase = new RequestFeedByMarketType();
			   break;

			case AddModifyOrderMessageType:
				//theBase = new AddModifyOrderMessage();
			   break;

			case CancelledTradeMessageType:
				//theBase = new CancelledTradeMessage();
			   break;

			case DeleteOrderMessageType:
				//theBase = new DeleteOrderMessage();
			   break;

			case HeartBeatMessageType:
				//theBase = new HeartBeatMessage();
			   break;

			case InvestigatedTradeMessageType:
				//theBase = new InvestigatedTradeMessage();
			   break;

			case MarketStateChangeMessageType:
				//theBase = new MarketStateChangeMessage();
			   break;

			case MarketStatisticsMessageType:
				//theBase = new MarketStatisticsMessage();
			   break;

			case OpenInterestMessageType:
				//theBase = new OpenInterestMessage();
			   break;

			case OpenPriceMessageType:
				//theBase = new OpenPriceMessage();
			   break;

			case SettlementPriceMessageType:
				//theBase = new SettlementPriceMessage();
			   break;

			case SystemTextMessageType:
				//theBase = new SystemTextMessage();
			   break;

			case TradeMessageType:
				//theBase = new TradeMessage();
			   break;

			case BundleMarkerMessageType:
				//theBase = new BundleMarkerMessage();
			   break;

			case LongProductDefinitionRequestType:
				//theBase = new LongProductDefinitionRequest();
			   break;

			case ExtraProductDefinitionResponseType:
				//theBase = new ExtraProductDefinitionResponse();
			   break;

         default:
            // unknow message, don't know how to instantiate, set it to null
            valid = false;

		}
		return valid;
	}
	
}
