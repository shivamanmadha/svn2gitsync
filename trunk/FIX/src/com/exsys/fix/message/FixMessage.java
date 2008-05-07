package com.exsys.fix.message;

import java.util.*;
import java.text.*;
import java.io.*;

import com.exsys.common.util.StringUtilities;

/**
 * Base class for all types of fix messages.
 * This class holds the storage for all the fix fields in a fix message
 * Creation date: (11/11/01 6:18:27 PM)
 * @author: Administrator
 */
public class FixMessage {
	private java.lang.String messageType;
	private TreeMap headerFieldMap = new TreeMap();
	private TreeMap bodyFieldMap = new TreeMap();
	private static SimpleDateFormat utcTimeStampFormatter =
		new java.text.SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");
	private static SimpleDateFormat utcTimeStampFormatterAlt =
		new java.text.SimpleDateFormat("yyyyMMdd-HH:mm:ss");

	private static SimpleDateFormat utcTimeOnlyFormatter =
		new java.text.SimpleDateFormat("HH:mm:ss.SSS");
	private static SimpleDateFormat utcTimeOnlyFormatterAlt =
		new java.text.SimpleDateFormat("HH:mm:ss");

	private static SimpleDateFormat localMktDateFormatter =
		new java.text.SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat utcDateFormatter =
		new java.text.SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat monthYearFormatter =
		new java.text.SimpleDateFormat("yyyyMM");
	//private static java.util.Calendar cal = java.util.Calendar.getInstance();

	private static SimpleDateFormat dbFormatTimestampFormatter =
		new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String calculatedChecksum = null;
	private int calculatedBodyLength = 0;

	private Hashtable repeatingGroupMap = null;

	/**
	* method to add a FixRepeatedGroup, if one exists
	*
	* @param msg - FixRepeatedGroup
	*/
	public void addRepeatingGroup(FixRepeatedGroup msg) {
		if (repeatingGroupMap == null) {
			repeatingGroupMap = new Hashtable();
		}
		String key = msg.getRepeatedGroupTag();
		if (!repeatingGroupMap.containsKey(key)) {
			ArrayList groupArray = new ArrayList();
			groupArray.add(msg);
			repeatingGroupMap.put(key, groupArray);
		} else {
			((ArrayList) repeatingGroupMap.get(key)).add(msg);

		}

	}
	/**
	* method to get  FixRepeatedGroup array, if one exists
	*
	* @return ArrayList - repeatingGroup
	*/
	public ArrayList getRepeatingGroups(String tagNum) {
		if (repeatingGroupMap == null) {
			return null;
		}

		return (ArrayList) repeatingGroupMap.get(tagNum);

	}

	/**
	 * FixMessage constructor.
	 */
	public FixMessage() {
		super();
		utcTimeStampFormatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		utcTimeStampFormatterAlt.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		utcDateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		utcTimeOnlyFormatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		utcTimeOnlyFormatterAlt.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));

	}
	/**
	 * FixMessage constructor.
	 * Creation date: (11/11/01 6:21:54 PM)
	 * @param newMessageType java.lang.String
	 */
	public FixMessage(String newMessageType) {
		messageType = newMessageType;
	}
	/**
	* method to get  message age in milli seconds
	*
	* @return long - diff between sending time and curent time
	*/
	public long getMessageAgeInMillis() {
		return (
			java.util.Calendar.getInstance().getTime().getTime()
				- getSendingTime().getTime());

	}
	/**
	* method to test if the message contains repeating groups
	*
	* @return boolean - base class returns false - derived classes must override
	* and return true if it has repeating group fields
	*/
	public boolean hasRepeatingGroupFields() {
		return false;
	}
	/**
	* method to test if the tag number is a repeating groups
	*
	* @return boolean - base class returns false - derived classes must override
	* and return true if it this tagnum is repeating group field
	*/	
	public boolean isFieldRepeatingGroup(String tagNum) {
		return false;
	}
	/**
	* helper method to return current time as UTC timestamp
	*
	* @return String - current UTC time as string
	*/
	public static String getUTCCurrentTime() {
		return (
			utcTimeStampFormatter.format(java.util.Calendar.getInstance().getTime()));

	}
	/**
	* helper method to return date value in DB timestamp format
	*
	* @param Date - date
	* @return String - current UTC time as string
	*/	
	public static String getDBTimestamp(Date date) {
		return (dbFormatTimestampFormatter.format(date));
	}

	/**
	* helper method to calculate checksum value of message
	*
	* @param String - message
	* @return String - checksum value
	*/
	public static String checksum(String message) {
		char[] cArray = message.toCharArray();
		long cks = 0;

		for (int i = 0; i < cArray.length; cks += (short) cArray[i++]);

		short num = (short) (cks % 256);
		return (StringUtilities.prePad(String.valueOf(num), '0', 3));

	}
	/**
	 * adds the field into body field map.
	 * Creation date: (11/11/01 6:39:35 PM)
	 * @param field com.exsys.fix.message.FixField
	 */
	protected void addBodyField(int tagNum, FixField field) {
		bodyFieldMap.put(new Integer(tagNum), field);
	}
	/**
	 * adds the field into body field map
	 * Creation date: (11/11/01 6:42:19 PM)
	 * @param tagNum int
	 * @param tagValue java.lang.String
	 */
	protected void addBodyField(int tagNum, String tagValue) {
		bodyFieldMap.put(new Integer(tagNum), new FixField(tagNum, tagValue));
	}
	/**
	 * adds the field into body field map or header field map
	 * Creation date: (11/11/01 6:39:35 PM)
	 * @param field com.exsys.fix.message.FixField
	 */
	protected void addField(int tagNum, FixField field) {
		if (isHeaderTag(tagNum)) {
			headerFieldMap.put(new Integer(tagNum), field);
		} else {
			bodyFieldMap.put(new Integer(tagNum), field);
		}
	}
	/**
	 * adds the field into body field map or header field map
	 * Creation date: (11/11/01 6:42:19 PM)
	 * @param tagNum int
	 * @param tagValue java.lang.String
	 */
	protected void addField(int tagNum, String tagValue) {
		if (isHeaderTag(tagNum)) {
			headerFieldMap.put(new Integer(tagNum), new FixField(tagNum, tagValue));
		} else {
			bodyFieldMap.put(new Integer(tagNum), new FixField(tagNum, tagValue));
		}

	}
	/**
	 * adds the field into header field map
	 * Creation date: (11/11/01 6:39:35 PM)
	 * @param field com.exsys.fix.message.FixField
	 */
	protected void addHeaderField(int tagNum, FixField field) {
		headerFieldMap.put(new Integer(tagNum), field);
	}
	/**
	 * adds the field into header field map
	 * Creation date: (11/11/01 6:42:19 PM)
	 * @param tagNum int
	 * @param tagValue java.lang.String
	 */
	protected void addHeaderField(int tagNum, String tagValue) {
		headerFieldMap.put(new Integer(tagNum), new FixField(tagNum, tagValue));
	}
	/**
	 * prints debug output of the message
	 * Creation date: (1/31/2002 8:23:59 AM)
	 */
	public void debugPrint() {
		debugPrint(new PrintWriter(System.out));
	}
	/**
	 * prints debug output of the message
	 * Creation date: (1/31/2002 8:25:36 AM)
	 * @param writer java.io.PrintWriter
	 */
	public void debugPrint(PrintWriter writer) {
		// print header fields
		writer.println("Header Fields");
		for (Iterator e = headerFieldMap.values().iterator(); e.hasNext();) {
			FixField field = (FixField) e.next();
			writer.println(field.getTagNumberAsString() + "--" + field.getTagValue());

		}

		// print body fields
		writer.println("Body Fields");

		for (Iterator e = bodyFieldMap.values().iterator(); e.hasNext();) {
			FixField field = (FixField) e.next();
			writer.println(field.getTagNumberAsString() + "--" + field.getTagValue());

		}
		writer.flush();
	}
	/**
	*  method to get begin string of the fix message
	*
	* @return String - begin string
	*/	
	public String getBeginString() {
		return (getHeaderFieldValue(8));
	}
	/**
	 * method to return body field corresponding to the tag number tagNum
	 * Creation date: (11/11/01 6:52:45 PM)
	 * @return com.exsys.fix.message.FixField
	 * @param tagNum int
	 */
	protected FixField getBodyField(int tagNum) {
		return ((FixField) bodyFieldMap.get(new Integer(tagNum)));
	}
	/**
	 * Method to return the body field map
	 * Creation date: (11/18/01 7:19:57 PM)
	 * @return java.util.Hashtable
	 */
	public TreeMap getBodyFieldMap() {
		return bodyFieldMap;
	}
	/**
	 * Method to return the body field value for the given tag number
	 * Creation date: (11/11/01 6:54:29 PM)
	 * @return java.lang.String
	 * @param tagNum int
	 */
	protected String getBodyFieldValue(int tagNum) {
		FixField tag = (FixField) bodyFieldMap.get(new Integer(tagNum));
		return (tag != null ? tag.getTagValue() : null);
		//return ( ((FixField)bodyFieldMap.get(new Integer(tagNum))).getTagValue());
	}
	public int getBodyLength() {
		return (stringToint(getHeaderFieldValue(9)));
	}
	public String getBodyLengthAsString() {
		return (getHeaderFieldValue(9));
	}
	public String getDeliverToCompID() {
		return (getHeaderFieldValue(128));
	}
	public String getDeliverToLocationID() {
		return (getHeaderFieldValue(145));
	}
	public String getDeliverToSubID() {
		return (getHeaderFieldValue(129));
	}
	/**
	 * Method to return body field or header field for the given tag number
	 * Creation date: (11/11/01 6:52:45 PM)
	 * @return com.exsys.fix.message.FixField
	 * @param tagNum int
	 */
	protected FixField getField(int tagNum) {
		if (isHeaderTag(tagNum)) {
			return ((FixField) headerFieldMap.get(new Integer(tagNum)));
		} else {
			return ((FixField) bodyFieldMap.get(new Integer(tagNum)));
		}
	}
	/**
	 * Method to return field value for the given tag number
	 * Creation date: (11/11/01 6:54:29 PM)
	 * @return java.lang.String
	 * @param tagNum int
	 */
	protected String getFieldValue(int tagNum) {
		if (isHeaderTag(tagNum)) {
			//return ( ((FixField)headerFieldMap.get(new Integer(tagNum))).getTagValue());
			return getHeaderFieldValue(tagNum);
		} else {
			//return ( ((FixField)bodyFieldMap.get(new Integer(tagNum))).getTagValue());
			return getBodyFieldValue(tagNum);
		}
	}
	/**
	 * Method to return header field for the given tag number
	 * Creation date: (11/11/01 6:52:45 PM)
	 * @return com.exsys.fix.message.FixField
	 * @param tagNum int
	 */
	protected FixField getHeaderField(int tagNum) {
		return ((FixField) headerFieldMap.get(new Integer(tagNum)));
	}
	/**
	 * Method to return header field value for the given tag number
	 * Creation date: (11/11/01 6:54:29 PM)
	 * @return java.lang.String
	 * @param tagNum int
	 */
	protected String getHeaderFieldValue(int tagNum) {
		FixField tag = (FixField) headerFieldMap.get(new Integer(tagNum));
		return (tag != null ? tag.getTagValue() : null);
	}
	/**
	 * Method to return header field map
	 * Creation date: (11/18/01 7:19:26 PM)
	 * @return java.util.Hashtable
	 */
	public TreeMap getHeaderMap() {
		return headerFieldMap;
	}
	public int getLastMsgSeqNumProcessed() {
		return (stringToint(getHeaderFieldValue(369)));
	}
	public String getLastMsgSeqNumProcessedAsString() {
		return (getHeaderFieldValue(369));
	}
	public static String getLocalMktDateString(Date value) {
		return localMktDateFormatter.format(value);
	}
	public String getMessageEncoding() {
		return (getHeaderFieldValue(347));
	}
	/**
	 * Method to get messageType of the fix message
	 * Creation date: (11/11/01 6:20:52 PM)
	 * @return java.lang.String
	 */
	public java.lang.String getMessageType() {
		return messageType;
	}
	public static String getMonthYearString(Date value) {
		return monthYearFormatter.format(value);
	}
	public int getMsgSeqNum() {
		return (stringToint(getHeaderFieldValue(34)));
	}
	public String getMsgSeqNumAsString() {
		return (getHeaderFieldValue(34));
	}
	public String getMsgType() {
		return (getHeaderFieldValue(35));
	}
	public String getOnBehalfOfCompID() {
		return (getHeaderFieldValue(115));
	}
	public String getOnBehalfOfLocationID() {
		return (getHeaderFieldValue(144));
	}
	public Date getOnBehalfOfSendingTime() {
		return (UTCTimeStampStringToDate(getHeaderFieldValue(370)));
	}
	public String getOnBehalfOfSendingTimeAsString() {
		return (getHeaderFieldValue(370));
	}
	public String getOnBehalfOfSubID() {
		return (getHeaderFieldValue(116));
	}
	public Date getOrigSendingTime() {
		return (UTCTimeStampStringToDate(getHeaderFieldValue(122)));
	}
	public String getOrigSendingTimeAsString() {
		return (getHeaderFieldValue(122));
	}
	public boolean getPossDupFlag() {
		return (stringToboolean(getHeaderFieldValue(43)));
	}
	public String getPossDupFlagAsString() {
		return (getHeaderFieldValue(43));
	}
	public boolean getPossResend() {
		return (stringToboolean(getHeaderFieldValue(97)));
	}
	public String getPossResendAsString() {
		return (getHeaderFieldValue(97));
	}
	public int getSecureDataLen() {
		return (stringToint(getHeaderFieldValue(90)));
	}
	public String getSecureDataLenAsString() {
		return (getHeaderFieldValue(90));
	}
	public String getSenderCompID() {
		return (getHeaderFieldValue(49));
	}
	public String getSenderLocationID() {
		return (getHeaderFieldValue(142));
	}
	public String getSenderSubID() {
		return (getHeaderFieldValue(50));
	}
	public Date getSendingTime() {
		return (UTCTimeStampStringToDate(getHeaderFieldValue(52)));
	}
	public String getSendingTimeAsString() {
		return (getHeaderFieldValue(52));
	}
	public String getCheckSum() {
		return (getBodyFieldValue(10));
	}
	public void setCalculatedChecksum(String val) {
		calculatedChecksum = val;
	}
	public String getCalculatedChecksum() {
		return calculatedChecksum;
	}
	public void setCalculatedBodyLength(int val) {
		calculatedBodyLength = val;
	}
	public int getCalculatedBodyLength() {
		return calculatedBodyLength;
	}
	public static String getString(float value) {
		return String.valueOf(value);
	}
	/**
	 * helper method to get string value of the int value
	 * Creation date: (11/12/01 10:36:43 PM)
	 * @return java.lang.String
	 * @param value int
	 */
	public static String getString(int value) {
		return String.valueOf(value);
	}
	/**
	 * helper method to get boolean value as a string Y or N
	 * Creation date: (11/12/01 10:37:17 PM)
	 * @return java.lang.String
	 * @param value boolean
	 */
	public static String getString(boolean value) {
		return value ? "Y" : "N";
	}
	public String getTargetCompID() {
		return (getHeaderFieldValue(56));
	}
	public String getTargetLocationID() {
		return (getHeaderFieldValue(143));
	}
	public String getTargetSubID() {
		return (getHeaderFieldValue(57));
	}
	/**
	 * helper method to get the date value in UTCTimestamp format
	 * Creation date: (11/12/01 10:37:53 PM)
	 * @return java.lang.String
	 * @param value java.util.Date
	 */
	public static String getUTCTimeStampString(Date value) {
		return utcTimeStampFormatter.format(value);
	}

	public static String getUTCTimeString(Date value) {
		return utcTimeOnlyFormatter.format(value);
	}

	public static String getUTCDateString(Date value) {
		return utcDateFormatter.format(value);
	}

	public int getXmlDataLen() {
		return (stringToint(getHeaderFieldValue(212)));
	}
	public String getXmlDataLenAsString() {
		return (getHeaderFieldValue(212));
	}
	/**
	 * Method to find if the tag is a header tag or not
	 * Creation date: (11/11/01 7:31:30 PM)
	 * @return boolean
	 * @param tag int
	 */
	public static boolean isHeaderTag(int tag) {
		boolean isHeaderTag = false;
		if (tag == 8
			|| tag == 9
			|| tag == 35
			|| tag == 49
			|| tag == 56
			|| tag == 115
			|| tag == 128
			|| tag == 90
			|| tag == 91
			|| tag == 34
			|| tag == 50
			|| tag == 142
			|| tag == 57
			|| tag == 143
			|| tag == 116
			|| tag == 144
			|| tag == 129
			|| tag == 145
			|| tag == 43
			|| tag == 97
			|| tag == 52
			|| tag == 122
			|| tag == 212
			|| tag == 213
			|| tag == 347
			|| tag == 369
			|| tag == 370) {
			isHeaderTag = true;
		}

		return isHeaderTag;
	}
	public static Date LocalMktDateStringToDate(String value) {

		Date localTimeStamp = null;
		try {
			localTimeStamp = localMktDateFormatter.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return localTimeStamp;
	}
	public static Date MonthYearStringToDate(String value) {

		Date monthYearTimeStamp = null;
		try {
			monthYearTimeStamp = monthYearFormatter.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return monthYearTimeStamp;
	}
	public void setBeginString(String _BeginString) {
		addHeaderField(8, _BeginString);
	}
	public void setBodyLength(int _BodyLength) {
		addHeaderField(9, getString(_BodyLength));
	}
	public void setBodyLength(String _BodyLength) {
		addHeaderField(9, _BodyLength);
	}
	public void setCheckSum(int _CheckSum) {
		addBodyField(10, getString(_CheckSum));
	}
	public void setCheckSum(String _CheckSum) {
		addBodyField(10, _CheckSum);
	}

	public void setDeliverToCompID(String _DeliverToCompID) {
		addHeaderField(128, _DeliverToCompID);
	}
	public void setDeliverToLocationID(String _DeliverToLocationID) {
		addHeaderField(145, _DeliverToLocationID);
	}
	public void setDeliverToSubID(String _DeliverToSubID) {
		addHeaderField(129, _DeliverToSubID);
	}
	public void setLastMsgSeqNumProcessed(int _LastMsgSeqNumProcessed) {
		addHeaderField(369, getString(_LastMsgSeqNumProcessed));
	}
	public void setLastMsgSeqNumProcessed(String _LastMsgSeqNumProcessed) {
		addHeaderField(369, _LastMsgSeqNumProcessed);
	}
	public void setMessageEncoding(String _MessageEncoding) {
		addHeaderField(347, _MessageEncoding);
	}
	/**
	 * Method to set message type of the fix message
	 * Creation date: (11/11/01 6:20:52 PM)
	 * @param newMessageType java.lang.String
	 */
	public void setMessageType(java.lang.String newMessageType) {
		messageType = newMessageType;
	}
	public void setMsgSeqNum(int _MsgSeqNum) {
		addHeaderField(34, getString(_MsgSeqNum));
	}
	public void setMsgSeqNum(String _MsgSeqNum) {
		addHeaderField(34, _MsgSeqNum);
	}
	public void setMsgType(String _MsgType) {
		addHeaderField(35, _MsgType);
	}
	public void setOnBehalfOfCompID(String _OnBehalfOfCompID) {
		addHeaderField(115, _OnBehalfOfCompID);
	}
	public void setOnBehalfOfLocationID(String _OnBehalfOfLocationID) {
		addHeaderField(144, _OnBehalfOfLocationID);
	}
	public void setOnBehalfOfSendingTime(String _OnBehalfOfSendingTime) {
		addHeaderField(370, _OnBehalfOfSendingTime);
	}
	public void setOnBehalfOfSendingTime(Date _OnBehalfOfSendingTime) {
		addHeaderField(370, getUTCTimeStampString(_OnBehalfOfSendingTime));
	}
	public void setOnBehalfOfSubID(String _OnBehalfOfSubID) {
		addHeaderField(116, _OnBehalfOfSubID);
	}
	public void setOrigSendingTime(String _OrigSendingTime) {
		addHeaderField(122, _OrigSendingTime);
	}
	public void setOrigSendingTime(Date _OrigSendingTime) {
		addHeaderField(122, getUTCTimeStampString(_OrigSendingTime));
	}
	public void setPossDupFlag(String _PossDupFlag) {
		addHeaderField(43, _PossDupFlag);
	}
	public void setPossDupFlag(boolean _PossDupFlag) {
		addHeaderField(43, getString(_PossDupFlag));
	}
	public void setPossResend(String _PossResend) {
		addHeaderField(97, _PossResend);
	}
	public void setPossResend(boolean _PossResend) {
		addHeaderField(97, getString(_PossResend));
	}
	public void setSecureDataLen(int _SecureDataLen) {
		addHeaderField(90, getString(_SecureDataLen));
	}
	public void setSecureDataLen(String _SecureDataLen) {
		addHeaderField(90, _SecureDataLen);
	}
	public void setSenderCompID(String _SenderCompID) {
		addHeaderField(49, _SenderCompID);
	}
	public void setSenderLocationID(String _SenderLocationID) {
		addHeaderField(142, _SenderLocationID);
	}
	public void setSenderSubID(String _SenderSubID) {
		addHeaderField(50, _SenderSubID);
	}
	public void setSendingTime(String _SendingTime) {
		addHeaderField(52, _SendingTime);
	}
	public void setSendingTime(Date _SendingTime) {
		addHeaderField(52, getUTCTimeStampString(_SendingTime));
	}
	public void setTargetCompID(String _TargetCompID) {
		addHeaderField(56, _TargetCompID);
	}
	public void setTargetLocationID(String _TargetLocationID) {
		addHeaderField(143, _TargetLocationID);
	}
	public void setTargetSubID(String _TargetSubID) {
		addHeaderField(57, _TargetSubID);
	}
	public void setXmlDataLen(int _XmlDataLen) {
		addHeaderField(212, getString(_XmlDataLen));
	}
	public void setXmlDataLen(String _XmlDataLen) {
		addHeaderField(212, _XmlDataLen);
	}
	/**
	 * helper method to convert Y or N string to boolean
	 * Creation date: (11/12/01 10:44:22 PM)
	 * @return boolean
	 * @param value java.lang.String
	 */
	public static boolean stringToboolean(String value) {

		return (value != null && value.equals("Y")) ? true : false;
	}

	public static double stringTodouble(String value) {
		return (value != null ? Double.parseDouble(value) : 0);
	}
	public static float stringTofloat(String value) {
		return (value != null ? Float.parseFloat(value) : 0);
	}
	/**
	 * helper method to convert string to int
	 * Creation date: (11/12/01 10:41:22 PM)
	 * @return int
	 * @param value java.lang.String
	 */
	public static int stringToint(String value) {
		return (value != null ? Integer.parseInt(value) : 0);
	}
	/**
	 * helper method to convert string value into date
	 * Creation date: (11/12/01 10:46:24 PM)
	 * @return java.util.Date
	 * @param value java.lang.String
	 */
	public static Date UTCTimeStampStringToDate(String value) {

		Date utcTimeStamp = null;
		try {
			try {
				utcTimeStamp = utcTimeStampFormatter.parse(value);
			} catch (ParseException e) {
				// try to parse using alternate
				utcTimeStamp = utcTimeStampFormatterAlt.parse(value);
				//e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return utcTimeStamp;
	}
	public static Date UTCTimeStringToDate(String value) {

		Date utcTime = null;
		try {
			try {
				utcTime = utcTimeOnlyFormatter.parse(value);
			} catch (ParseException e) {
				// try to parse using alternate
				utcTime = utcTimeOnlyFormatterAlt.parse(value);
				//e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return utcTime;
	}
	public static Date UTCDateStringToDate(String value) {

		Date utcDate = null;
		try {
			utcDate = utcDateFormatter.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return utcDate;
	}

	public String toString() {
		StringWriter sw = new StringWriter();
		NativeFixTranslator nft = new NativeFixTranslator(sw);
		try {
			nft.translateIncludingMsgType(this);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return sw.toString();
	}

}