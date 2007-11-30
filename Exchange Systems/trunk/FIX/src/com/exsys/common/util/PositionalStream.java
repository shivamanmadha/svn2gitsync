package com.exsys.common.util;

import java.text.ParseException;
import java.math.*;
import java.io.*;
import java.util.*;

/**
 * This class supports reading info from different offsets of
 * a string buffer.
 * This is used in processing RLC messages
 * Creation date: (8/9/2002 6:32:40 PM)
 * @author: Administrator
 */

public class PositionalStream
{
	private byte[] myBytes = null;
	private int YYYY_MM_FORMAT_LENGTH = 6;
	private int YYYY_MM_DD_FORMAT_LENGTH = 8;
	private int YY_MM_DD_FORMAT_LENGTH = 6;

	// NOTE: Because of ebcidic conversion constraints, we can't
	// use a primitive char to byte conversion
	private static final byte SPACE = new String(" ").getBytes()[0];

	private boolean isNegitive = false;

	public PositionalStream() {
	}
/**
 * Constructor
 * Creation date: (4/8/02 10:16:20 AM)
 * @param newBytes byte[]
 */
public PositionalStream(byte[] newBytes)
{
	myBytes = newBytes;
}

/**
 * Constructor
 * Creation date: (4/8/02 10:16:20 AM)
 * @param size int
 */
	public PositionalStream(int size) {
		myBytes = new byte[size];
	}
/**
 * Constructor
 * Creation date: (4/8/02 10:16:20 AM)
 * @param size int
 * @param fillWithSpaces boolean
 */
	
	public PositionalStream(int size, boolean fillWithSpaces) {
		myBytes = new byte[size];

		if (fillWithSpaces) {
			for (int i = 0; i < size; i++) {
				myBytes[i] = SPACE;
			}
		}
	}
/**
 * method to get the internal byte array
 * Creation date: (4/8/02 10:16:20 AM)
 * 
 */
	
public byte[] getBytes() {
	return myBytes;
}
/**
 * @deprecated Use readDateFromYYYYmmDD
 */
protected java.sql.Date getDateFromYYYYmmDD(int offset) throws ParseException
{
	return readDateFromYYYYmmDD(offset);
}
/**
 * @deprecated Use readDecimal
 */
protected BigDecimal getDecimal(int offset, int length, int decimalCount, boolean signed)
{
	return readDecimal(offset, length, decimalCount, signed);
}
/**
 * @deprecated Use readInt
 */
protected int getInt(int offset, int length)
{
	return readInt(offset, length);
}
/**
 * @deprecated Use readShort
 */
protected short getShort(int offset, int length)
{
	return readShort(offset, length);
}
/**
 * @deprecated Use readString
 */
protected String getString(int offset, int length)
{
	return readString(offset, length);
}
public static String pad(String value, char pad, int width) {
	int length = value.length();
	if (length < width) {
		StringBuffer buffer = new StringBuffer(value);
		while (length < width) {
			buffer.insert(0, pad);
			length++;
		}
		return buffer.toString();
	} else {
		return value;
	}
}
public final BigDecimal readCOMP3(int position, int size) {
	return readCOMP3(position, size, 0);
}
public final BigDecimal readCOMP3(int position, int wholeSize, int partSize) {
	byte mask = 0xf;

	// Create an array to write to
	int size = wholeSize + partSize;
	char[] value = new char[size + 1];

	// Get the bytes into a local array
	int byteCount = (size / 2) + 1;
	byte[] bytes = new byte[byteCount];
	System.arraycopy(myBytes, position, bytes, 0, byteCount);

	int writeIndex = value.length - 1;
	int readIndex = bytes.length - 1;

	// Read the first byte
	value[writeIndex--] = Character.forDigit(bytes[readIndex] >> 4 & mask, 16);

	// Read the remaining bytes
	for(readIndex--; readIndex >= 0; readIndex--) {
		value[writeIndex--] = Character.forDigit(bytes[readIndex] & mask, 16);
		value[writeIndex--] = Character.forDigit(bytes[readIndex] >> 4 & mask, 16);
	}

	// Pad any remaining values with zero
	while (writeIndex >= 0) {
		value[writeIndex--] = Character.forDigit(0, 16);
	}

	BigDecimal largeValue = new BigDecimal(new String(value));
	return largeValue.movePointLeft(partSize);
}
public final java.sql.Date readDateFromDB2Format(String dateStr) throws ParseException
{
	return new java.sql.Date(readLongFromDB2Format(dateStr));
}
public final java.sql.Date readDateFromYYYYmm(int offset) throws ParseException
{
	return new java.sql.Date(readLongFromYYYYmm(offset));
}
public final java.sql.Date readDateFromYYYYmmDD(int offset) throws ParseException
{
	return new java.sql.Date(readLongFromYYYYmmDD(offset));
}
public final java.sql.Date readDateFromYYMMDD(int offset) throws ParseException
{
	return new java.sql.Date(readLongFromYYMMDD(offset));
}
public final BigDecimal readDecimal(int offset, int length, int decimalCount, boolean signed)
{
	if (signed) {
		throw new RuntimeException("Signed values are not supported");
	}
	//FIX
	String value = new String(myBytes, offset-1, length);
	BigDecimal dec = new BigDecimal(value);
	return dec.movePointLeft(decimalCount);
}
/*
 * This method will only read COBOL equalent of signed decimal variable ex. PIC S99V99.
 * If the value read is not signed decimal variable it will thorw RuntimeException.
 */
public final BigDecimal readDecimalSigned(int offset, int length, int decimalCount)
{
	int lastNumber = 0;

	//FIX
	StringBuffer sb = new StringBuffer(new String(myBytes, offset-1, length));
	lastNumber = validateCharacter(sb.charAt(sb.length() - 1), lastNumber);

	sb.replace(sb.length() - 1, sb.length(), Integer.toString(lastNumber));

	BigDecimal dec = new BigDecimal(sb.toString());
	if(isNegitive){
		return dec.negate().movePointLeft(decimalCount);
	}

	return dec.movePointLeft(decimalCount);
}
public final int readInt(int offset, int length)
{
	//FIX
	String value = new String(myBytes, offset-1, length);
	return Integer.parseInt(value);
}
public final BigInteger readIntSigned(int offset, int length){
	int lastNumber = 0;
	//FIX
	StringBuffer sb = new StringBuffer(new String(myBytes, offset-1, length));
	lastNumber = validateCharacter(sb.charAt(sb.length() - 1), lastNumber);

	sb.replace(sb.length() - 1, sb.length(), Integer.toString(lastNumber));

	BigInteger bInt = new BigInteger(sb.toString());

	if(isNegitive){
		return bInt.negate();
	}

	return bInt;
}
private long readLongFromDB2Format(String dateStr) throws ParseException
{
	java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
	java.util.Date utilDate = format.parse(dateStr);

	return utilDate.getTime();
}
private long readLongFromYYYYmm(int offset) throws ParseException
{
	//FIX
	String value = new String(myBytes, offset-1, YYYY_MM_FORMAT_LENGTH);
	String dateStr = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + "01";
	return readLongFromDB2Format(dateStr);
}
private long readLongFromYYYYmmDD(int offset) throws ParseException
{
	//FIX
	String value = new String(myBytes, offset-1, YYYY_MM_DD_FORMAT_LENGTH);
	String dateStr = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8);
	return readLongFromDB2Format(dateStr);
}
private long readLongFromYYMMDD(int offset) throws ParseException
{
	//FIX
	String value = new String(myBytes, offset-1, YY_MM_DD_FORMAT_LENGTH);
	String dateStr = "20" + value.substring(0,2) + "-" + value.substring(2,4) + "-" + value.substring(4,6);
	return readLongFromDB2Format(dateStr);
}
public final short readShort(int offset, int length)
{
	//FIX
	String value = new String(myBytes, offset-1, length);
	return Short.parseShort(value);
}
public final String readString(int offset, int length)
{
	//FIX
	return new String(myBytes, offset-1, length);
}
public void setBytes(byte[] newRecord)
{
	myBytes = newRecord;
}
/**
 * @deprecated Use setBytes
 */
public final void setRecord(byte[] newRecord)
{
	myBytes = newRecord;
}
public final void write(int position, byte[] bytes) {
	System.arraycopy(bytes, 0, myBytes, position, bytes.length);
}
public final void write(int position, int value, int width) {
	String strValue = pad(String.valueOf(value), '0', width);
	write(position, strValue.getBytes());
}
public final void write(int position, Integer value, int width) {
	if (value != null) {
		String strValue = pad(value.toString(), '0', width);
		write(position, strValue.getBytes());
	}
}
public final void write(int position, String data) {
	if (data != null && data.trim().length() > 0) {
		write(position, data.trim().getBytes());
	}
}
public void write(int position, String value, int width) {
	if (value != null) {
		String strValue = pad(value.trim(), '0', width);
		write(position, strValue.getBytes());
	}
}
private void writeCOMP3(int position, char[] number, int size) {
	if (number.length > size) {
		throw new RuntimeException("The number exceeds the size constraints of the field");
	}
	int byteCount = (size / 2) + 1;
	byte[] buffer = new byte[byteCount];

	int numberIndex = number.length - 1;

	byte mask = 15;
	buffer[buffer.length - 1] = (byte)((Character.digit(number[numberIndex], 16) << 4) | mask);

	numberIndex--;
	for(int i = buffer.length - 2; i >= 0; i--) {
		if (numberIndex >= 0) {
			buffer[i] = (byte)(Character.digit(number[numberIndex--], 16));
		} else {
			buffer[i] = (byte)(Character.digit('0', 16));
		}

		if (numberIndex >= 0) {
			buffer[i] = (byte)(buffer[i] | Character.digit(number[numberIndex--], 16) << 4);
		} else {
			buffer[i] = (byte)(buffer[i] | Character.digit('0', 16) << 4);
		}
	}
	write(position, buffer);
}
public final void writeCOMP3(int position, double data, int wholeSize, int partSize) {
	writeCOMP3(position, new BigDecimal(data), wholeSize, partSize);
}
public final void writeCOMP3(int position, int data, int size) {
	if (data < 0) {
		throw new RuntimeException("Negative values are not supported");
	}

	char[] number = String.valueOf(data).toCharArray();
	writeCOMP3(position, number, size);
}
public final void writeCOMP3(int position, BigDecimal data, int size) {
	writeCOMP3(position, data, size, 0);
}
public final void writeCOMP3(int position, BigDecimal data, int wholeSize, int partSize) {
	if (data.compareTo(new BigDecimal(0)) < 0) {
		throw new RuntimeException("Negative values are not supported");
	}

	BigDecimal adjustedValue = data.setScale(partSize, java.math.BigDecimal.ROUND_HALF_UP).movePointRight(partSize);

	char[] number = adjustedValue.toString().toCharArray();

	int size = wholeSize + partSize;
	writeCOMP3(position, number, size);
}
public final void writeWithoutTrim(int position, String data) {
	if (data != null && data.length() > 0) {
		write(position, data.getBytes());
	}
}
private int validateCharacter(char c, int num){

	switch (c) {
		case '{' :
					num = 0;
					isNegitive = false;
					break;
		case 'A' :
					num = 1;
					isNegitive = false;
					break;
		case 'B' :
					num = 2;
					isNegitive = false;
					break;
		case 'C' :
					num = 3;
					isNegitive = false;
					break;
		case 'D' :
					num = 4;
					isNegitive = false;
					break;
		case 'E' :
					num = 5;
					isNegitive = false;
					break;
		case 'F' :
					num = 6;
					isNegitive = false;
					break;
		case 'G' :
					num = 7;
					isNegitive = false;
					break;
		case 'H' :
					num = 8;
					isNegitive = false;
					break;
		case 'I' :
					num = 9;
					isNegitive = false;
					break;
		case '}' :
					num = 0;
					isNegitive = true;
					break;
		case 'J' :
					num = 1;
					isNegitive = true;
					break;
		case 'K' :
					num = 2;
					isNegitive = true;
					break;
		case 'L' :
					num = 3;
					isNegitive = true;
					break;
		case 'M' :
					num = 4;
					isNegitive = true;
					break;
		case 'N' :
					num = 5;
					isNegitive = true;
					break;
		case 'O' :
					num = 6;
					isNegitive = true;
					break;
		case 'P' :
					num = 7;
					isNegitive = true;
					break;
		case 'Q' :
					num = 8;
					isNegitive = true;
					break;
		case 'R' :
					num = 9;
					isNegitive = true;
					break;

		default :throw new RuntimeException("Value passed/read is not signed variable");

	}
	return num;
}

}
