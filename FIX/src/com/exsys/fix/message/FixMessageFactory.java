package com.exsys.fix.message;

import java.util.*;
import com.exsys.common.exceptions.*;
/**
 * This class is used to create appropriate fix message
 * This is used by the FixSession to constructe fix messages
 * based on the message recieved over the tcpip connection
 * Creation date: (11/18/01 10:13:09 PM)
 * @author: Administrator
 */
public class FixMessageFactory
{
	public static Hashtable requiredCMESFieldsMap = new Hashtable();
	public static Hashtable requiredCMECFieldsMap = new Hashtable();

	public static Hashtable requiredFIXSFieldsMap = new Hashtable();
	public static Hashtable requiredFIXCFieldsMap = new Hashtable();


	public static Hashtable exchangeServerMap = new Hashtable();
	public static Hashtable exchangeClientMap = new Hashtable();

	public static Hashtable msgTypesMap = new Hashtable();
	public static Hashtable rgFirstFieldsMap = new Hashtable();

	public static final int FIX_FIRST_TAG = 8; //version
	public static final int FIX_SECOND_TAG = 9; // body length
	public static final int FIX_THIRD_TAG = 35; // msg type
	public static final int FIX_SEQNUM_TAG = 34; // seq num

	static
	{
		//rgFirstFieldsMap
	rgFirstFieldsMap.put("146_d","311");
	rgFirstFieldsMap.put("146_V","55");
	rgFirstFieldsMap.put("267_V","269");
	rgFirstFieldsMap.put("268_W","269");
	rgFirstFieldsMap.put("268_X","279");
	rgFirstFieldsMap.put("33_B","58");




		// msgtypes

	msgTypesMap.put("0",		"Heartbeat");
	msgTypesMap.put("1",		"Test Request");
	msgTypesMap.put("2",		"Resend Request");
	msgTypesMap.put("3",		"Reject");
	msgTypesMap.put("4",		"Sequence Reset");
	msgTypesMap.put("5",		"Logout");
	msgTypesMap.put("6",		"Indication of Interest");
	msgTypesMap.put("7",		"Advertisement");
	msgTypesMap.put("8",		"Execution Report");
	msgTypesMap.put("9",		"Order Cancel Reject");
	msgTypesMap.put("A",		"Logon");
	msgTypesMap.put("B",		"News");
	msgTypesMap.put("C",		"Email");
	msgTypesMap.put("D",		"Order  Single");
	msgTypesMap.put("E",		"Order List");
	msgTypesMap.put("F",		"Order Cancel Request");
	msgTypesMap.put("G",	"Order Cancel/Replace Request");
	msgTypesMap.put("H",	"Order Status Request");
	msgTypesMap.put("J",		"Allocation");
	msgTypesMap.put("K",		"List Cancel Request");
	msgTypesMap.put("L",		"List Execute");
	msgTypesMap.put("M",  "List Status Request");
	msgTypesMap.put("N",	"List Status");
	msgTypesMap.put("P",  "Allocation ACK");
	msgTypesMap.put("Q",  "Don’t Know Trade (DK)");
	msgTypesMap.put("R",  "Quote Request");
	msgTypesMap.put("S",  "Quote");
	msgTypesMap.put("T",  "Settlement Instructions");
	msgTypesMap.put("V",  "Market Data Request");
	msgTypesMap.put("W",  "Market Data-Snapshot/Full Refresh");
	msgTypesMap.put("X",  "Market Data-Incremental Refresh");
	msgTypesMap.put("Y",  "Market Data Request Reject");
	msgTypesMap.put("Z",  "Quote Cancel");
	msgTypesMap.put("a",  "Quote Status Request");
	msgTypesMap.put("b",   "Quote Acknowledgement");
	msgTypesMap.put("c",  "Security Definition Request");
	msgTypesMap.put("d", "Security Definition");
	msgTypesMap.put("e",  "Security Status Request");
	msgTypesMap.put("f",  "Security Status");
	msgTypesMap.put("g",  "Trading Session Status Request");
	msgTypesMap.put("h",  "Trading Session Status");
	msgTypesMap.put("i",  "Mass Quote");
	msgTypesMap.put("j",  "Business Message Reject");
	msgTypesMap.put("k",  "Bid Request" );
	msgTypesMap.put("l", "Bid Response (lowercase L)");
	msgTypesMap.put("m", "List Strike Price");


	// custom messages

	msgTypesMap.put("CG", "ICE Trader Logon");
	msgTypesMap.put("CH", "ICE Trader Logout");
	msgTypesMap.put("CI", "ICE Trader Logon Response");
	msgTypesMap.put("CJ", "ICE Trader Logout Response");


		/*****Exchangge = CME ******/
		//header
		// 50,57, 142 , 369 is cme required
		requiredCMESFieldsMap.put("HEADER",new int[]{34,49,50,52,56,57,142});
		// 04272007
		// cme did not send tag 50 on heartbeat and session reject messages

		requiredCMECFieldsMap.put("HEADER",new int[]{34,49,52,56,369});



		// heartbeat
		requiredCMESFieldsMap.put("0",new int[]{});
		requiredCMECFieldsMap.put("0",new int[]{});



		// logon
		//95, 96 are cme required
		requiredCMESFieldsMap.put("A",new int[]{95,96,98,108});
		requiredCMECFieldsMap.put("A",new int[]{98,108});



		// TestRequest
		requiredCMESFieldsMap.put("1",new int[]{112});
		requiredCMECFieldsMap.put("1",new int[]{112});




		// ResendReset
		requiredCMESFieldsMap.put("2",new int[]{7,16});
		requiredCMECFieldsMap.put("2",new int[]{7,16});


		// Session Level Reject
		requiredCMESFieldsMap.put("3",new int[]{45});
		requiredCMECFieldsMap.put("3",new int[]{45});

		// Business Level Reject
		// 58, 380 cme only
		requiredCMESFieldsMap.put("j",new int[]{58, 372,380});
		requiredCMECFieldsMap.put("j",new int[]{58, 372,380});



		// SequenceReset
		requiredCMESFieldsMap.put("4",new int[]{36});
		requiredCMECFieldsMap.put("4",new int[]{36});


		// Logout
		//789 is cme req
		requiredCMESFieldsMap.put("5",new int[]{789});
		// cme did not send tag 789 some times
		requiredCMECFieldsMap.put("5",new int[]{});




		// Order
		// 1,38,107,167, 204, 9702, 9717 are cme req
		requiredCMESFieldsMap.put("D",new int[]{1,11,21,38,40,54,55,60,107,167,204,9702,9717});
		requiredCMECFieldsMap.put("D",new int[]{1,11,21,38,40,54,55,60,107,167,204,9702,9717});




		// Cancel
		// 37, 107, 167,9717 are cme req
		requiredCMESFieldsMap.put("F",new int[]{1,11,37,41,54,55,60,107,167,9717});
		requiredCMECFieldsMap.put("F",new int[]{1,11,37,41,54,55,60,107,167,9717});

		// Cancel Replace
		// 1,38,107,167, 204, 9702, 9717 are cme req

		requiredCMESFieldsMap.put("G",new int[]{1,11,21,38,40,54,55,60,107,167,204,9702,9717});
		requiredCMECFieldsMap.put("G",new int[]{1,11,21,38,40,54,55,60,107,167,204,9702,9717});


		// Order Status
		// 37,107,167, 9717 are cme req
		requiredCMESFieldsMap.put("H",new int[]{11,37,54,55,60,107,167,9717});
		requiredCMECFieldsMap.put("H",new int[]{11,37,54,55,60,107,167,9717});



		// ExecutionReport
		// 1, 11 ,38,40 ,60,107,150,9717 are cme req
		// 04292007 cme did not send tag 38,40,151 when trade cancel is sent (type H)
		// 07082007 cme fx did not send 9717 on an execution report
		requiredCMESFieldsMap.put("8",new int[]{1,6,11,14,17,20,37,38,39,40,54,55,60,107,150,151});
		requiredCMECFieldsMap.put("8",new int[]{1,6,11,14,17,20,37,39,54,55,60,107,150});


		// CancelReject
		// 17,60,9717 are cme req
		requiredCMESFieldsMap.put("9",new int[]{11,17,37,39,41,60,434,9717});
		requiredCMECFieldsMap.put("9",new int[]{11,17,37,39,41,60,434,9717});


		exchangeClientMap.put("CME",requiredCMECFieldsMap);
		exchangeServerMap.put("CME",requiredCMESFieldsMap);


		/*****Exchangge = FIX ******/
		//header
		// 50,57, 142 , 369 is cme required
		requiredFIXSFieldsMap.put("HEADER",new int[]{34,49,52,56,57,142});
		requiredFIXCFieldsMap.put("HEADER",new int[]{34,49,52,56});



		// heartbeat
		requiredFIXSFieldsMap.put("0",new int[]{});
		requiredFIXCFieldsMap.put("0",new int[]{});



		// logon
		//95, 96 are cme required
		requiredFIXSFieldsMap.put("A",new int[]{98,108});
		requiredFIXCFieldsMap.put("A",new int[]{98,108});



		// TestRequest
		requiredFIXSFieldsMap.put("1",new int[]{112});
		requiredFIXCFieldsMap.put("1",new int[]{112});




		// ResendReset
		requiredFIXSFieldsMap.put("2",new int[]{7,16});
		requiredFIXCFieldsMap.put("2",new int[]{7,16});


		// Session Level Reject
		requiredFIXSFieldsMap.put("3",new int[]{45});
		requiredFIXCFieldsMap.put("3",new int[]{45});

		// Business Level Reject
		// 58, 380 cme only
		requiredFIXSFieldsMap.put("j",new int[]{372,380});
		requiredFIXCFieldsMap.put("j",new int[]{372,380});



		// SequenceReset
		requiredFIXSFieldsMap.put("4",new int[]{36});
		requiredFIXCFieldsMap.put("4",new int[]{36});


		// Logout
		//789 is cme req
		requiredFIXSFieldsMap.put("5",new int[]{});
		requiredFIXCFieldsMap.put("5",new int[]{});




		// Order
		// 1,38,107,167, 204, 9702, 9717 are cme req
		requiredFIXSFieldsMap.put("D",new int[]{11,21,40,54,55,60});
		requiredFIXCFieldsMap.put("D",new int[]{11,21,40,54,55,60});




		// Cancel
		// 37, 107, 167,9717 are cme req
		requiredFIXSFieldsMap.put("F",new int[]{11,41,54,55,60});
		requiredFIXCFieldsMap.put("F",new int[]{11,41,54,55,60});

		// Cancel Replace
		// 1,38,107,167, 204, 9702, 9717 are cme req
		requiredFIXSFieldsMap.put("G",new int[]{11,21,38,40,54,55,60});
		requiredFIXCFieldsMap.put("G",new int[]{11,21,38,40,54,55,60});


		// Order Status
		// 37,107,167, 204, 9717 are cme req
		requiredFIXSFieldsMap.put("H",new int[]{11,54,55});
		requiredFIXCFieldsMap.put("H",new int[]{11,54,55});



		// ExecutionReport
		// 1, 11 ,38,40 ,60,107,150,9717 are cme req
		requiredFIXSFieldsMap.put("8",new int[]{6,14,17,20,37,39,54,55,150,151});
		requiredFIXCFieldsMap.put("8",new int[]{6,14,17,20,37,39,54,55,150,151});


		// CancelReject
		// 17,60,9717 are cme req
		requiredFIXSFieldsMap.put("9",new int[]{11,37,39,41,434});
		requiredFIXCFieldsMap.put("9",new int[]{11,37,39,41,434});


		exchangeClientMap.put("FIX",requiredFIXCFieldsMap);
		exchangeServerMap.put("FIX",requiredFIXSFieldsMap);


		/*****Exchangge = ICE ******/
		//header
		// 50,57, 142 , 369 is cme required
		requiredFIXSFieldsMap.put("HEADER",new int[]{34,49,52,56,57,142});
		requiredFIXCFieldsMap.put("HEADER",new int[]{34,49,52,56});



		// heartbeat
		requiredFIXSFieldsMap.put("0",new int[]{});
		requiredFIXCFieldsMap.put("0",new int[]{});



		// logon
		//95, 96 are cme required
		requiredFIXSFieldsMap.put("A",new int[]{98,108,553});
		requiredFIXCFieldsMap.put("A",new int[]{98,108});

		// trader logon
		//95, 96 are cme required
		requiredFIXSFieldsMap.put("CG",new int[]{96,553});
		requiredFIXCFieldsMap.put("CG",new int[]{96,553});

		// trader logon response
		//95, 96 are cme required
		requiredFIXSFieldsMap.put("CI",new int[]{109,553});
		requiredFIXCFieldsMap.put("CI",new int[]{109,553});

		// trader logout
		//95, 96 are cme required
		requiredFIXSFieldsMap.put("CH",new int[]{553});
		requiredFIXCFieldsMap.put("CH",new int[]{553});


		// trader logout response
		//95, 96 are cme required
		requiredFIXSFieldsMap.put("CJ",new int[]{109,553});
		requiredFIXCFieldsMap.put("CJ",new int[]{109,553});



		// TestRequest
		requiredFIXSFieldsMap.put("1",new int[]{112});
		requiredFIXCFieldsMap.put("1",new int[]{112});




		// ResendReset
		requiredFIXSFieldsMap.put("2",new int[]{7,16});
		requiredFIXCFieldsMap.put("2",new int[]{7,16});


		// Session Level Reject
		requiredFIXSFieldsMap.put("3",new int[]{45});
		requiredFIXCFieldsMap.put("3",new int[]{45});

		// Business Level Reject
		// 58, 380 cme only
		requiredFIXSFieldsMap.put("j",new int[]{372,380});
		requiredFIXCFieldsMap.put("j",new int[]{372,380});



		// SequenceReset
		requiredFIXSFieldsMap.put("4",new int[]{36});
		requiredFIXCFieldsMap.put("4",new int[]{36});


		// Logout
		//789 is cme req
		requiredFIXSFieldsMap.put("5",new int[]{});
		requiredFIXCFieldsMap.put("5",new int[]{});



		// Order
		// 1,38,107,167, 204, 9702, 9717 are cme req
		requiredFIXSFieldsMap.put("D",new int[]{11,21,40,54,55,60,9139});
		requiredFIXCFieldsMap.put("D",new int[]{11,21,40,54,55,60});




		// Cancel
		// 37, 107, 167,9717 are cme req
		requiredFIXSFieldsMap.put("F",new int[]{11,41,54,55,60,9139});
		requiredFIXCFieldsMap.put("F",new int[]{11,41,54,55,60});

		// Cancel Replace
		// 1,38,107,167, 204, 9702, 9717 are cme req
		requiredFIXSFieldsMap.put("G",new int[]{11,21,38,40,54,55,60,9139});
		requiredFIXCFieldsMap.put("G",new int[]{11,21,38,40,54,55,60});


		// Order Status
		// 37,107,167, 204, 9717 are cme req
		requiredFIXSFieldsMap.put("H",new int[]{11,54,55});
		requiredFIXCFieldsMap.put("H",new int[]{11,54,55});



		// ExecutionReport
		// 1, 11 ,38,40 ,60,107,150,9717 are cme req
		requiredFIXSFieldsMap.put("8",new int[]{6,14,17,20,37,39,54,55,150,151});
		requiredFIXCFieldsMap.put("8",new int[]{6,14,17,20,37,39,54,55,150,151});


		// CancelReject
		// 17,60,9717 are cme req
		requiredFIXSFieldsMap.put("9",new int[]{11,37,39,41,434});
		requiredFIXCFieldsMap.put("9",new int[]{11,37,39,41,434,9139});

		// security definition request
		requiredFIXSFieldsMap.put("c",new int[]{320,321,167});
		requiredFIXCFieldsMap.put("c",new int[]{320,321,167});

		// security definition response
		requiredFIXSFieldsMap.put("d",new int[]{322,323,320});
		requiredFIXCFieldsMap.put("d",new int[]{322,323,320});

		// market data request
		requiredFIXSFieldsMap.put("V",new int[]{262,263,264,267,269,146});
		requiredFIXCFieldsMap.put("V",new int[]{262,263,264,267,269,146});


		// security status
		requiredFIXSFieldsMap.put("f",new int[]{55,326});
		requiredFIXCFieldsMap.put("f",new int[]{55,326});


		// market data full refresh
		requiredFIXSFieldsMap.put("W",new int[]{262,55,387,268});
		requiredFIXCFieldsMap.put("W",new int[]{262,55,387,268});

		// market data incremental refresh
		requiredFIXSFieldsMap.put("X",new int[]{268});
		requiredFIXCFieldsMap.put("X",new int[]{268});

		// market data reject
		requiredFIXSFieldsMap.put("Y",new int[]{262});
		requiredFIXCFieldsMap.put("Y",new int[]{262});


		// news
		requiredFIXSFieldsMap.put("B",new int[]{148,61,553,33});
		requiredFIXCFieldsMap.put("B",new int[]{148,61,553,33});


		exchangeClientMap.put("ICE",requiredFIXCFieldsMap);
		exchangeServerMap.put("ICE",requiredFIXSFieldsMap);




	}



/**
 * FixMessageFactory constructor.
 */
public FixMessageFactory() {
	super();

}
/**
 * Method to create FixMessage object based on the byte array 
 * Creation date: (11/18/01 10:13:49 PM)
 * @return com.exsys.fix.message.FixMessage
 * @param msg byte[]
 * @exception FixProtocolError
 */
public static FixMessage createFixMessage(byte[] msg, String exchange, boolean isServer)
		throws FixProtocolError
{


	// we need to construct specific type of message
	FixMessage fixMsg = null;

	boolean firstTagValidated = false;
	boolean secondTagValidated = false;
	boolean thirdTagValidated = false;
	int tagStart=0;
	int valueStart=0;
	int tagEnd=0;
	int valueEnd=0;
	int tagCounter=0;
	String msgType = null;
	String bodyString = null;
	// first find msg type
	while( tagStart < msg.length )
	{
		// first get tag byte

		tagEnd = getNextOffset( msg,tagStart, FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR);
		int tagNumber = Integer.parseInt( new String(msg,tagStart, tagEnd-tagStart));
		valueStart=tagEnd+1;
		valueEnd = getNextOffset(msg,valueStart,(char)FixConstants.FIX_FIELD_DELIMITER);
		String tagValue = new String(msg,valueStart,valueEnd-valueStart);

		if( tagCounter < 3 )
		{



			if( ( ++tagCounter == 1 && tagNumber != FIX_FIRST_TAG ) ||
				( tagCounter == 2 && tagNumber != FIX_SECOND_TAG ) ||
				( tagCounter == 3 && tagNumber != FIX_THIRD_TAG ) )
			{
				throw new FixProtocolError("Invalid First Three Fields","0");
			}
			if(  tagCounter == 1
				&& ( tagNumber != FIX_FIRST_TAG
				|| !tagValue.equals(com.exsys.fix.session.FixSession.getFixVersion())))
			{
				throw new FixProtocolError("Invalid Begin String","5");
			}

			if(tagNumber == FIX_SECOND_TAG)
			{
				//System.out.println(valueEnd);
				//System.out.println(msg.length);
				//System.out.println("offset");
				bodyString = new String(msg,valueEnd+1,msg.length-valueEnd-1);
				//System.out.println("BodyString = "+bodyString);
			}

			if( tagNumber == FIX_THIRD_TAG )
			{
				msgType = tagValue;
				//System.out.println("Msg Type is " + msgType );
				if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
				{
					//System.out.println("Msg is ORDER" );
					fixMsg = new FixOrder();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL))
				{
					//System.out.println("Msg is CANCEL" );
					fixMsg = new FixCancel();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
				{
					fixMsg = new FixCancelReplace();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER_STATUS_REQUEST))
				{
					fixMsg = new FixOrderStatusRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT))
				{
					fixMsg = new FixExecutionReport();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_RESENDREQUEST))
				{
					fixMsg = new FixResendRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_SEQUENCERESET))
				{
					fixMsg = new FixSequenceReset();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_LOGON))
				{
					fixMsg = new FixLogon();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_TESTREQUEST))
				{
					fixMsg = new FixTestRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_REJECT))
				{
					fixMsg = new FixSessionReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT))
				{
					fixMsg = new FixCancelReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_BUSINESS_REJECT))
				{
					fixMsg = new FixBusinessReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE))
				{
					fixMsg = new FixSecurityDefinitionResponse();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST))
				{
					fixMsg = new FixSecurityDefinitionRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_TRADER_LOGON_RESPONSE))
				{
					fixMsg = new FixTraderLogonResponse();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_TRADER_LOGOUT_RESPONSE))
				{
					fixMsg = new FixTraderLogoutResponse();
				}				
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS))
				{
					fixMsg = new FixSecurityStatus();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH))
				{
					fixMsg = new FixMarketDataFullRefresh();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH))
				{
					fixMsg = new FixMarketDataIncrementalRefresh();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST))
				{
					fixMsg = new FixMarketDataRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REJECT))
				{
					fixMsg = new FixMarketDataReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_NEWS))
				{
					fixMsg = new FixNews();
				}
				else
				{
					// construct generic message
					fixMsg = new FixMessage();
					fixMsg.setMessageType( msgType );
				}

			}


		}
		tagStart = valueEnd+1;
	}
	if( msgType == null )
	{
		throw new FixProtocolError("Required Header Field MSGTYPE does not exist","4");
	}

	// repeating group support
	boolean hasRG = fixMsg.hasRepeatingGroupFields();
	boolean foundRG = false;
	FixRepeatedGroup frg = null;
	boolean firstTagAdded = false;
	boolean pendingRG = false;



// reset counters
	tagStart=0;
	valueStart=0;
	tagEnd=0;
	valueEnd=0;
	int lastTagOffset = 0;
	while( tagStart < msg.length )
	{
		// first get tag byte
		lastTagOffset = tagStart;
		tagEnd = getNextOffset( msg,tagStart, FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR);
		String tagNumStr = new String(msg,tagStart, tagEnd-tagStart);
		int tagNumber = Integer.parseInt(tagNumStr);
		valueStart=tagEnd+1;
		valueEnd = getNextOffset(msg,valueStart,(char)FixConstants.FIX_FIELD_DELIMITER);
		String tagValue = new String(msg,valueStart,valueEnd-valueStart);

		if(foundRG == false)
		{
			fixMsg.addField(tagNumber, tagValue);
		}
		else
		{
			if(!firstTagAdded)
			{
				frg.addField(tagNumber, tagValue);
				firstTagAdded = true;
			}
			else
			{
				// check if this need to be added or is RG complete
				if(frg.isFirstField(tagNumStr))
				{
					fixMsg.addRepeatingGroup(frg);
					frg = FixRepeatingGroupFactory.createFixRepeatingGroup(
							frg.getRepeatedGroupTag(),fixMsg.getMsgType());
					frg.addField(tagNumber, tagValue);
					pendingRG = true;
					firstTagAdded = true;
				}
				else if(frg.isMemberField(tagNumStr))
				{
					frg.addField(tagNumber, tagValue);
				}
				else
				{
					fixMsg.addRepeatingGroup(frg);
					fixMsg.addField(tagNumber,tagValue);
					pendingRG = false;
					frg = null;
					foundRG = false;
					firstTagAdded = false;
				}
			}
		}

		if(!foundRG
			&& hasRG
			&& fixMsg.isFieldRepeatingGroup(tagNumStr)
			&& !tagValue.equals("0"))
		{
			foundRG = true;
			frg = FixRepeatingGroupFactory.createFixRepeatingGroup(tagNumStr
										,fixMsg.getMsgType());
										
			pendingRG = true;
		}

		tagStart = valueEnd+1;
	}
	if(pendingRG)
		fixMsg.addRepeatingGroup(frg);

	String msgWithoutChecksum = new String(msg,0,lastTagOffset);
	//System.out.println("Msg without checksum - "+msgWithoutChecksum);

	String calculatedCheckSum = FixMessage.checksum(msgWithoutChecksum);
	//System.out.println("Calculated checksum - "+calculatedCheckSum);
	fixMsg.setCalculatedChecksum(calculatedCheckSum);
	//System.out.println("Checksum - "+fixMsg.getCheckSum());
	// calculate bodylength and set it
	String checksumString = FixConstants.FIX_TRAILER_CHECKSUM_TAG +
							  FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
							  fixMsg.getCheckSum() +
							  (char)FixConstants.FIX_FIELD_DELIMITER ;

	fixMsg.setCalculatedBodyLength(bodyString.length()-checksumString.length());
	//System.out.println("Calculated Length = "+ (bodyString.length()-checksumString.length()));

/*
	// validate all the required fields are present
	int reqBFields[] = isServer?
						(int[])requiredSFieldsMap.get(msgType)
						:(int[])requiredCFieldsMap.get(msgType);

	int reqHFields[] = isServer?
						(int[])requiredSFieldsMap.get("HEADER")
						:(int[])requiredCFieldsMap.get("HEADER");
*/
	int reqBFields[] = isServer?
						(int[])((Hashtable)exchangeServerMap.get(exchange)).get(msgType)
						:(int[])((Hashtable)exchangeClientMap.get(exchange)).get(msgType);
	if(reqBFields == null)
	{
		// check if this is unsupported msg type or invalid one
		if(msgTypesMap.get(msgType) != null)
		{
			throw new FixProtocolError("Unsupported Msg Type","99");
		}
		else
		{
			// must be invalid msg type
			throw new FixProtocolError("Invalid MsgType","11");
		}
	}


	int reqHFields[] = isServer?
						(int[])((Hashtable)exchangeServerMap.get(exchange)).get("HEADER")
						:(int[])((Hashtable)exchangeClientMap.get(exchange)).get("HEADER");

	for( int i=0; i<reqHFields.length;i++)
	{

		if( fixMsg.getHeaderField(reqHFields[i]) == null )
		{
			throw new FixProtocolError("Required Header Fields Missing",
										"Field "+reqHFields[i]+" does not exist","10");
		}
	}


	for( int i=0; i<reqBFields.length;i++)
	{

		if( fixMsg.getBodyField(reqBFields[i]) == null )
		{
			throw new FixProtocolError("Required Body Fields Missing",
										"Field "+reqBFields[i]+" does not exist","10");
		}

	}



	return fixMsg;

}

/**
* helper method to get sequence number of fix message
*
* @return String - sequence number
*/
public static String getSeqNum(byte[] msg)
{

	// we need to construct specific type of message
	String seqNum = "0";

	int tagStart=0;
	int valueStart=0;
	int tagEnd=0;
	int valueEnd=0;
	// first find msg type
	while( tagStart < msg.length )
	{
		// first get tag byte

		tagEnd = getNextOffset( msg,tagStart, FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR);
		int tagNumber = Integer.parseInt( new String(msg,tagStart, tagEnd-tagStart));
		valueStart=tagEnd+1;
		valueEnd = getNextOffset(msg,valueStart,(char)FixConstants.FIX_FIELD_DELIMITER);
		String tagValue = new String(msg,valueStart,valueEnd-valueStart);

		if( tagNumber == FIX_SEQNUM_TAG )
		{
			seqNum = tagValue;
			break;

		}

		tagStart = valueEnd+1;
	}



	return seqNum;

}

/**
 * Method to create FixMessage object based on the byte array, without some validations
 * Creation date: (11/18/01 10:13:49 PM)
 * @return com.exsys.fix.message.FixMessage
 * @param msg byte[]
 * @exception FixProtocolError
 */
public static FixMessage createFixMessageWithoutValidation(byte[] msg) throws FixProtocolError
{


	FixMessage fixMsg = null;

	int tagStart=0;
	int valueStart=0;
	int tagEnd=0;
	int valueEnd=0;

	String msgType = null;
	while( tagStart < msg.length )
	{
		// first get tag byte

		tagEnd = getNextOffset( msg,tagStart, FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR);
		int tagNumber = Integer.parseInt( new String(msg,tagStart, tagEnd-tagStart));
		valueStart=tagEnd+1;
		valueEnd = getNextOffset(msg,valueStart,(char)FixConstants.FIX_FIELD_DELIMITER);
		String tagValue = new String(msg,valueStart,valueEnd-valueStart);

		if( tagNumber == FIX_THIRD_TAG )
		{
				msgType = tagValue;
				if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
				{
					fixMsg = new FixOrder();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL))
				{
					fixMsg = new FixCancel();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
				{
					fixMsg = new FixCancelReplace();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER_STATUS_REQUEST))
				{
					fixMsg = new FixOrderStatusRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT))
				{
					fixMsg = new FixExecutionReport();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_RESENDREQUEST))
				{
					fixMsg = new FixResendRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_SEQUENCERESET))
				{
					fixMsg = new FixSequenceReset();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_LOGON))
				{
					fixMsg = new FixLogon();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_TESTREQUEST))
				{
					fixMsg = new FixTestRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_REJECT))
				{
					fixMsg = new FixSessionReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT))
				{
					fixMsg = new FixCancelReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_BUSINESS_REJECT))
				{
					fixMsg = new FixBusinessReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE))
				{
					fixMsg = new FixSecurityDefinitionResponse();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST))
				{
					fixMsg = new FixSecurityDefinitionRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_TRADER_LOGON_RESPONSE))
				{
					fixMsg = new FixTraderLogonResponse();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_TRADER_LOGOUT_RESPONSE))
				{
					fixMsg = new FixTraderLogoutResponse();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS))
				{
					fixMsg = new FixSecurityStatus();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH))
				{
					fixMsg = new FixMarketDataFullRefresh();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH))
				{
					fixMsg = new FixMarketDataIncrementalRefresh();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST))
				{
					fixMsg = new FixMarketDataRequest();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REJECT))
				{
					fixMsg = new FixMarketDataReject();
				}
				else if( msgType.equals(FixConstants.FIX_MSGTYPE_NEWS))
				{
					fixMsg = new FixNews();
				}
				else
				{
					// construct generic message
					fixMsg = new FixMessage();
					fixMsg.setMessageType( msgType );
				}
		}

		tagStart = valueEnd+1;
	}

	if( msgType == null )
	{
		throw new FixProtocolError("Required Header Field MSGTYPE does not exist");
	}

	// repeating group support
	boolean hasRG = fixMsg.hasRepeatingGroupFields();
	boolean foundRG = false;
	FixRepeatedGroup frg = null;
	boolean firstTagAdded = false;
	boolean pendingRG = false;



// reset counters
	tagStart=0;
	valueStart=0;
	tagEnd=0;
	valueEnd=0;
	int lastTagOffset = 0;
	while( tagStart < msg.length )
	{
		// first get tag byte
		lastTagOffset = tagStart;
		tagEnd = getNextOffset( msg,tagStart, FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR);
		String tagNumStr = new String(msg,tagStart, tagEnd-tagStart);
		int tagNumber = Integer.parseInt(tagNumStr);
		valueStart=tagEnd+1;
		valueEnd = getNextOffset(msg,valueStart,(char)FixConstants.FIX_FIELD_DELIMITER);
		String tagValue = new String(msg,valueStart,valueEnd-valueStart);

		if(foundRG == false)
		{
			fixMsg.addField(tagNumber, tagValue);
		}
		else
		{
			if(!firstTagAdded)
			{
				frg.addField(tagNumber, tagValue);
				firstTagAdded = true;
			}
			else
			{
				// check if this need to be added or is RG complete
				if(frg.isFirstField(tagNumStr))
				{
					fixMsg.addRepeatingGroup(frg);
					frg = FixRepeatingGroupFactory.createFixRepeatingGroup(
							frg.getRepeatedGroupTag(),fixMsg.getMsgType());
					frg.addField(tagNumber, tagValue);
					pendingRG = true;
					firstTagAdded = true;
				}
				else if(frg.isMemberField(tagNumStr))
				{
					frg.addField(tagNumber, tagValue);
				}
				else
				{
					fixMsg.addRepeatingGroup(frg);
					pendingRG = false;
					fixMsg.addField(tagNumber,tagValue);
					frg = null;
					foundRG = false;
					firstTagAdded = false;
				}
			}
		}

		if(!foundRG
			&& hasRG
			&& fixMsg.isFieldRepeatingGroup(tagNumStr)
			&& !tagValue.equals("0"))
		{
			foundRG = true;
			frg = FixRepeatingGroupFactory.createFixRepeatingGroup(tagNumStr
										,fixMsg.getMsgType());
			pendingRG = true;
		}

		tagStart = valueEnd+1;
	}

	if(pendingRG)
		fixMsg.addRepeatingGroup(frg);

	// reset counters
	/*
	tagStart=0;
	valueStart=0;
	tagEnd=0;
	valueEnd=0;

	while( tagStart < msg.length )
	{
		// first get tag byte

		tagEnd = getNextOffset( msg,tagStart, FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR);
		int tagNumber = Integer.parseInt( new String(msg,tagStart, tagEnd-tagStart));
		valueStart=tagEnd+1;
		valueEnd = getNextOffset(msg,valueStart,(char)FixConstants.FIX_FIELD_DELIMITER);
		String tagValue = new String(msg,valueStart,valueEnd-valueStart);


		fixMsg.addField(tagNumber, tagValue);
		tagStart = valueEnd+1;
	}
	*/

	return fixMsg;

}
/**
 * Method to get next offset
 * Creation date: (11/18/01 10:32:25 PM)
 * @return int
 * @param msg byte[]
 * @param startOffset int
 */
private static int getNextOffset(byte[] msg, int startOffset, char charToLook)
{
	int offset = 0;
	for( int i=startOffset; i<msg.length; i++)
	{
		if( (char)msg[i]==charToLook)
		{
			offset = i;
			break;
		}

	}

	return offset;
}
public static String getRGFirstField(String key)
{
	return (String)rgFirstFieldsMap.get(key);
}
}
