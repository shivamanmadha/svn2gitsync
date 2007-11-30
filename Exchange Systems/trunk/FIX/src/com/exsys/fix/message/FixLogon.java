package com.exsys.fix.message;

//05152007 - added tag 553 (username) per ICE

/**
* This class is used to represent the FIX message Logon
*
*/
public class FixLogon extends FixMessage {
	/**
	* Constructor to construct FixLogon object 
	*
	*/
	public FixLogon() {
		setMsgType(FixConstants.FIX_MSGTYPE_LOGON);
		setMessageType(FixConstants.FIX_MSGTYPE_LOGON);
	}
	/**
	* getter method to get EncryptMethod
	*
	* @return int - EncryptMethod
	*/
	public int getEncryptMethod() {
		return (stringToint(getBodyFieldValue(98)));
	}
	/**
	* getter method to get EncryptMethodAsString
	*
	* @return String - EncryptMethodAsString
	*/
	public String getEncryptMethodAsString() {
		return (getBodyFieldValue(98));
	}
	/**
	* getter method to get HeartBtInt
	*
	* @return int - HeartBtInt
	*/
	public int getHeartBtInt() {
		return (stringToint(getBodyFieldValue(108)));
	}
	/**
	* getter method to get HeartBtIntAsString
	*
	* @return String - HeartBtIntAsString
	*/
	public String getHeartBtIntAsString() {
		return (getBodyFieldValue(108));
	}
	/**
	* getter method to get MaxMessageSize
	*
	* @return int - MaxMessageSize
	*/
	public int getMaxMessageSize() {
		return (stringToint(getBodyFieldValue(383)));
	}
	/**
	* getter method to get MaxMessageSizeAsString
	*
	* @return String - MaxMessageSizeAsString
	*/
	public String getMaxMessageSizeAsString() {
		return (getBodyFieldValue(383));
	}
	/**
	* getter method to get RawDataLength
	*
	* @return int - RawDataLength
	*/
	public int getRawDataLength() {
		return (stringToint(getBodyFieldValue(95)));
	}
	/**
	* getter method to get RawData
	*
	* @return String - RawData
	*/
	public String getRawData() {
		return (getBodyFieldValue(96));
	}
	/**
	* getter method to get RawDataLengthAsString
	*
	* @return String - RawDataLengthAsString
	*/
	public String getRawDataLengthAsString() {
		return (getBodyFieldValue(95));
	}
	/**
	* getter method to get ResetSeqNumFlag
	*
	* @return boolean - ResetSeqNumFlag
	*/
	public boolean getResetSeqNumFlag() {
		return (stringToboolean(getBodyFieldValue(141)));
	}
	/**
	* getter method to get ResetSeqNumFlagAsString
	*
	* @return String - ResetSeqNumFlagAsString
	*/
	public String getResetSeqNumFlagAsString() {
		return (getBodyFieldValue(141));
	}
	// ICE specific method
	/**
	* getter method to get UserName
	*
	* @return String - UserName
	*/
	public String getUserName() {
		return (getBodyFieldValue(553));
	}
	/**
	* setter method to set EncryptMethod
	*
	* @param int - EncryptMethod
	*/
	public void setEncryptMethod(int _EncryptMethod) {
		addBodyField(98, getString(_EncryptMethod));
	}
	/**
	* setter method to set EncryptMethod
	*
	* @param String - EncryptMethod
	*/
	public void setEncryptMethod(String _EncryptMethod) {
		addBodyField(98, _EncryptMethod);
	}
	/**
	* setter method to set HeartBtInt
	*
	* @param int - HeartBtInt
	*/
	public void setHeartBtInt(int _HeartBtInt) {
		addBodyField(108, getString(_HeartBtInt));
	}
	/**
	* setter method to set HeartBtInt
	*
	* @param String - HeartBtInt
	*/
	public void setHeartBtInt(String _HeartBtInt) {
		addBodyField(108, _HeartBtInt);
	}
	/**
	* setter method to set MaxMessageSize
	*
	* @param int - MaxMessageSize
	*/
	public void setMaxMessageSize(int _MaxMessageSize) {
		addBodyField(383, getString(_MaxMessageSize));
	}
	/**
	* setter method to set MaxMessageSize
	*
	* @param String - MaxMessageSize
	*/
	public void setMaxMessageSize(String _MaxMessageSize) {
		addBodyField(383, _MaxMessageSize);
	}
	/**
	* setter method to set RawDataLength
	*
	* @param int - RawDataLength
	*/
	public void setRawDataLength(int _RawDataLength) {
		addBodyField(95, getString(_RawDataLength));
	}
	/**
	* setter method to set RawDataLength
	*
	* @param String - RawDataLength
	*/
	public void setRawDataLength(String _RawDataLength) {
		addBodyField(95, _RawDataLength);
	}
	/**
	* setter method to set RawData
	*
	* @param String - RawData
	*/
	public void setRawData(String _RawData) {
		addBodyField(96, _RawData);
	}
	/**
	* setter method to set ResetSeqNumFlag
	*
	* @param String - ResetSeqNumFlag
	*/
	public void setResetSeqNumFlag(String _ResetSeqNumFlag) {
		addBodyField(141, _ResetSeqNumFlag);
	}
	/**
	* setter method to set ResetSeqNumFlag
	*
	* @param boolean - ResetSeqNumFlag
	*/
	public void setResetSeqNumFlag(boolean _ResetSeqNumFlag) {
		addBodyField(141, getString(_ResetSeqNumFlag));
	}
	// ICE specific method
	/**
	* setter method to set UserName
	*
	* @param String - UserName
	*/
	public void setUserName(String _UserName) {
		addBodyField(553, _UserName);
	}
}
