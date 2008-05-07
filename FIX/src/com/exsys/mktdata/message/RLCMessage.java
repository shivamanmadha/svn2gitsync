package com.exsys.mktdata.message;

import com.exsys.common.exceptions.RLCProtocolError;
import com.exsys.common.util.*;

import java.util.*;
import java.text.*;
import java.io.*;
import java.math.*;

/**
* This class is used to represent the RLC message RLCMessage
*
*/
public class RLCMessage extends PositionalStream {
	private static SimpleDateFormat cmeDateTimeFormatter =
		new java.text.SimpleDateFormat("yyyymmddhhmmss");

	private static SimpleDateFormat cmeDateFormatter =
		new java.text.SimpleDateFormat("yyyymmdd");

	/**
	* Constructor to construct RLCMessage object 
	*
	*/
	public RLCMessage() {
	}
	/**
	* Constructor to construct RLCMessage object 
	* 
	* @param newBytes
	* @throws RLCProtocolError
	*/
	public RLCMessage(byte[] newBytes) {
		super(newBytes);
	}
	/**
	* Constructor to construct RLCMessage object 
	*  
	* @param size
	* @param fillWithSpaces
	*/
	public RLCMessage(int size, boolean fillWithSpaces) {
		super(size, fillWithSpaces);
	}

	/**
	* setter method to set CMEGlobexInternalID
	*
	* @param String - CMEGlobexInternalID
	*
	*/
	public void setCMEGlobexInternalID(String _CMEGlobexInternalID) {
		write(1, _CMEGlobexInternalID);
	}
	/**
	* getter method to get CMEGlobexInternalID
	*
	* @return String - CMEGlobexInternalID
	*/
	public String getCMEGlobexInternalID() {
		return (readString(1, 12));
	}
	/**
	* setter method to set HostTimestamp
	*
	* @param String - HostTimestamp
	*
	*/
	public void setHostTimestamp(String _HostTimestamp) {
		write(13, _HostTimestamp);
	}
	/**
	* getter method to get HostTimestamp
	*
	* @return String - HostTimestamp
	*/
	public String getHostTimestamp() {
		return (readString(13, 5));
	}
	/**
	* setter method to set OriginTime
	*
	* @param Date - OriginTime
	*
	*/
	public void setOriginTime(Date _OriginTime) {
		write(18, getyyyymmddhhmmssString(_OriginTime));
	}
	/**
	* setter method to set OriginTime
	*
	* @param String - OriginTime
	*
	*/
	public void setOriginTime(String _OriginTime) {
		write(18, _OriginTime);
	}
	/**
	* getter method to get OriginTime
	*
	* @return Date - OriginTime
	*/
	public Date getOriginTime() {
		return (yyyymmddhhmmssStringToDate(readString(18, 14)));
	}
	/**
	* getter method to get OriginTimeAsString
	*
	* @return String - OriginTimeAsString
	*/
	public String getOriginTimeAsString() {
		return (readString(18, 14));
	}
	/**
	* setter method to set Filler_1
	*
	* @param String - Filler_1
	*
	*/
	public void setFiller_1(String _Filler_1) {
		write(32, _Filler_1);
	}
	/**
	* getter method to get Filler_1
	*
	* @return String - Filler_1
	*/
	public String getFiller_1() {
		return (readString(32, 2));
	}
	/**
	* setter method to set MessageType
	*
	* @param String - MessageType
	*
	*/
	public void setMessageType(String _MessageType) {
		write(34, _MessageType);
	}
	/**
	* getter method to get MessageType
	*
	* @return String - MessageType
	*/
	public String getMessageType() {
		return (readString(34, 2));
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (11/12/01 10:44:22 PM)
	 * @return boolean
	 * @param value java.lang.String
	 */
	/**
	* method  stringToboolean
	*
	* @param String - value
	*
	* @return boolean - stringToboolean
	*/
	public static boolean stringToboolean(String value) {

		return (value != null && value.equals("Y")) ? true : false;
	}
	/**
	* method  stringTodouble
	*
	* @param String - value
	*
	* @return double - stringTodouble
	*/
	public static double stringTodouble(String value) {
		return Double.parseDouble(value.trim());
	}
	/**
	* method  stringTofloat
	*
	* @param String - value
	*
	* @return float - stringTofloat
	*/
	public static float stringTofloat(String value) {
		return Float.parseFloat(value.trim());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/12/01 10:41:22 PM)
	 * @return int
	 * @param value java.lang.String
	 */
	/**
	* method  stringToint
	*
	* @param String - value
	*
	* @return int - stringToint
	*/
	public static int stringToint(String value) {
		return Integer.parseInt(value.trim());
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/12/01 10:46:24 PM)
	 * @return java.util.Date
	 * @param value java.lang.String
	 */
	/**
	* method  yyyymmddhhmmssStringToDate
	*
	* @param String - value
	*
	* @return Date - yyyymmddhhmmssStringToDate
	*/
	public static Date yyyymmddhhmmssStringToDate(String value) {

		Date cmeDateTime = null;
		try {
			cmeDateTime = cmeDateTimeFormatter.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cmeDateTime;
	}

	/**
	* getter method to get yyyymmddhhmmssString
	*
	* @param Date - dateTime
	*
	* @return String - yyyymmddhhmmssString
	*/
	public static String getyyyymmddhhmmssString(Date dateTime) {
		return (cmeDateTimeFormatter.format(dateTime));

	}

	/**
	* getter method to get YYYYMMDDString
	*
	* @param Date - date
	*
	* @return String - YYYYMMDDString
	*/
	public static String getYYYYMMDDString(Date date) {
		return (cmeDateFormatter.format(date));

	}
	/**
	* method  YYYYMMDDStringToDate
	*
	* @param String - value
	*
	* @return Date - YYYYMMDDStringToDate
	*/
	public static Date YYYYMMDDStringToDate(String value) {

		Date cmeDate = null;
		try {
			cmeDate = cmeDateFormatter.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cmeDate;
	}
	/**
	* method  stringToPrice
	*
	* @param String - value
	*
	* @return BigDecimal - stringToPrice
	*/
	public static BigDecimal stringToPrice(String value) {
		byte[] myBytes = value.getBytes();
		char firstChar = (char) myBytes[0];
		//System.out.println(">"+(char)myBytes[0]+ "<");
		boolean isNegative = false;
		int numDecimals = 0;
		switch (firstChar) {

			case ' ' :
			case '0' :
				numDecimals = 0;
				break;

			case '1' :
				numDecimals = 1;
				break;

			case '2' :
				numDecimals = 2;
				break;

			case '3' :
				numDecimals = 3;
				break;

			case '4' :
				numDecimals = 4;
				break;

			case '5' :
				numDecimals = 5;
				break;

			case '6' :
				numDecimals = 6;
				break;

			case '7' :
				numDecimals = 7;
				break;

			case 'A' :
				numDecimals = 0;
				isNegative = true;

				break;

			case 'B' :
				numDecimals = 1;
				isNegative = true;
				break;

			case 'C' :
				numDecimals = 2;
				isNegative = true;
				break;

			case 'D' :
				numDecimals = 3;
				isNegative = true;

				break;

			case 'E' :
				numDecimals = 4;
				isNegative = true;
				break;

			case 'F' :
				numDecimals = 5;
				isNegative = true;
				break;

			case 'G' :
				numDecimals = 6;
				isNegative = true;
				break;

			case 'H' :
				numDecimals = 7;
				isNegative = true;
				break;

			default :
				throw new RuntimeException("Value passed is not valid");
		}

		String priceValue = new String(myBytes, 1, myBytes.length - 1);
		//System.out.println(priceValue);

		java.math.BigDecimal dec = new java.math.BigDecimal(priceValue);
		dec = dec.movePointLeft(numDecimals);

		if (isNegative)
			dec = dec.negate();

		return dec;
	}

	/**
	* method  priceToString
	*
	* @param BigDecimal - price
	*
	* @return String - priceToString
	*/
	public static String priceToString(BigDecimal price) {
		int scale = price.scale();
		boolean newNegativeFlag = false;
		if (price.doubleValue() < 0) {

			//System.out.println("Number is negative");
			newNegativeFlag = true;
			price = price.negate();
		} else {

			//System.out.println("Number is Positive");
		}

		java.math.BigInteger intValue = price.unscaledValue();

		String strPrice = null;
		switch (scale) {
			case 0 :
				strPrice = (newNegativeFlag) ? "A" : "0";
				break;

			case 1 :
				strPrice = (newNegativeFlag) ? "B" : "1";
				break;

			case 2 :
				strPrice = (newNegativeFlag) ? "C" : "2";
				break;

			case 3 :
				strPrice = (newNegativeFlag) ? "D" : "3";
				break;

			case 4 :
				strPrice = (newNegativeFlag) ? "E" : "4";
				break;

			case 5 :
				strPrice = (newNegativeFlag) ? "F" : "5";
				break;

			case 6 :
				strPrice = (newNegativeFlag) ? "G" : "6";
				break;

			case 7 :
				strPrice = (newNegativeFlag) ? "H" : "7";
				break;
		}

		String strValue = intValue.toString();

		// prepad with 0s, length = 18
		strValue = pad(strValue, '0', 18);

		strPrice += strValue;

		//System.out.println(strPrice);
		return strPrice;
	}

	/**
	* method  calculateTickSize
	*
	* @return double - calculateTickSize
	*/
	public static double calculateTickSize(
		String f1_NumMinElemTickIncr,
		String f2_TickDisplayFormatType,
		String f3_NumDecimals,
		String f4_VTTIndexCode,
		double currentPrice) {
		double tickSize = 0;
		int f5_numDecInFracPrice = 0;
		if (f2_TickDisplayFormatType.equals("01")) {
			f5_numDecInFracPrice = 0;
		} else
			if (f2_TickDisplayFormatType.equals("02")
				|| f2_TickDisplayFormatType.equals("04")
				|| f2_TickDisplayFormatType.equals("08")) {
				f5_numDecInFracPrice = 1;
			} else
				if (f2_TickDisplayFormatType.equals("16")
					|| f2_TickDisplayFormatType.equals("32")
					|| f2_TickDisplayFormatType.equals("64")) {
					f5_numDecInFracPrice = 2;
				}
		//System.out.println("f5_numDecInFracPrice = "  + f5_numDecInFracPrice);

		if (!f1_NumMinElemTickIncr.equals("0000")) {
			if (StringUtilities.isNumeric(f2_TickDisplayFormatType)) {
				int f2 = Integer.valueOf(f2_TickDisplayFormatType).intValue();
				int f3 = Integer.valueOf(f3_NumDecimals).intValue();
				double oneOverF2 = 1 / f2;
				double tenOver = java.lang.Math.pow(10, f3 - f5_numDecInFracPrice);
				double f6 = oneOverF2 / tenOver;
				int f1 = Integer.valueOf(f1_NumMinElemTickIncr).intValue();
				tickSize = f1 * f6;
			} else {
				if (f2_TickDisplayFormatType.trim().equals("EH")) {
					tickSize = 0.015625;
				} else
					if (f2_TickDisplayFormatType.trim().equals("EQ")) {
						tickSize = 0.0078125;
					} else
						if (f2_TickDisplayFormatType.trim().equals("FH")) {
							tickSize = 0.0078125;
						}
			}
		} else {
			if (f4_VTTIndexCode.equals("01") && currentPrice < -500) {
				tickSize = 10;
			} else
				if (f4_VTTIndexCode.equals("01")
					&& currentPrice >= -500
					&& currentPrice <= 500) {
					tickSize = 5;
				} else
					if (f4_VTTIndexCode.equals("01") && currentPrice > 500) {
						tickSize = 10;
					} else
						if (f4_VTTIndexCode.equals("02") && currentPrice >= 0 && currentPrice <= 5) {
							tickSize = 0.5;
						} else
							if (f4_VTTIndexCode.equals("02") && currentPrice > 5) {
								tickSize = 1;
							} else
								if (f4_VTTIndexCode.equals("03") && currentPrice >= 0 && currentPrice <= 10) {
									tickSize = 1;
								} else
									if (f4_VTTIndexCode.equals("03") && currentPrice > 10) {
										tickSize = 2;
									} else
										if (f4_VTTIndexCode.equals("04") && currentPrice < -500) {
											tickSize = 25;
										} else
											if (f4_VTTIndexCode.equals("04")
												&& currentPrice >= -500
												&& currentPrice <= 500) {
												tickSize = 5;
											} else
												if (f4_VTTIndexCode.equals("04") && currentPrice > 500) {
													tickSize = 25;
												} else
													if (f4_VTTIndexCode.equals("05")) {
														tickSize = 1;
													} else
														if (f4_VTTIndexCode.equals("06")) {
															tickSize = 2;
														} else
															if (f4_VTTIndexCode.equals("07")) {
																tickSize = 1;
															} else
																if (f4_VTTIndexCode.equals("09")) {
																	tickSize = 0.5;
																} else
																	if (f4_VTTIndexCode.equals("10") && currentPrice < -300) {
																		tickSize = 25;
																	} else
																		if (f4_VTTIndexCode.equals("10")
																			&& currentPrice >= -300
																			&& currentPrice <= 300) {
																			tickSize = 5;
																		} else
																			if (f4_VTTIndexCode.equals("10") && currentPrice > 300) {
																				tickSize = 25;
																			} else
																				if (f4_VTTIndexCode.equals("11") && currentPrice < -300) {
																					tickSize = 10;
																				} else
																					if (f4_VTTIndexCode.equals("11")
																						&& currentPrice >= -300
																						&& currentPrice <= 300) {
																						tickSize = 5;
																					} else
																						if (f4_VTTIndexCode.equals("11") && currentPrice > 300) {
																							tickSize = 10;
																						}

		}

		return tickSize;
	}

	/**
	* method  toString
	*
	* @return String - toString
	*/
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.write("CompleteRawMessage=>" + getBytes() + "<\n");
		sw.write("CMEGlobexInternalID=>" + getCMEGlobexInternalID() + "<\n");
		sw.write("HostTimestamp=>" + getHostTimestamp() + "<\n");
		sw.write("OriginTime=>" + getOriginTimeAsString() + "<\n");
		sw.write("Filler_1=>" + getFiller_1() + "<\n");
		sw.write("MessageType=>" + getMessageType() + "<\n");
		return sw.toString();
	}

}
