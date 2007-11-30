package com.exsys.fix.message;

/**
* This class is used to represent the FIX message News
*
*/
public class FixNews extends FixMessage {
	/**
	* method  hasRepeatingGroupFields
	*
	* @return boolean - hasRepeatingGroupFields
	*/
	public boolean hasRepeatingGroupFields() {
		return true;
	}
	/**
	* method  isFieldRepeatingGroup
	*
	* @param String - tagNum
	*
	* @return boolean - isFieldRepeatingGroup
	*/
	public boolean isFieldRepeatingGroup(String tagNum) {
		return (tagNum.equals(FixConstants.FIX_REPEATEDGROUP_LINESOFTEXT));
	}

	/**
	* Constructor to construct FixNews object 
	*
	*/
	public FixNews() {
		setMessageType(FixConstants.FIX_MSGTYPE_NEWS);
		setMsgType(FixConstants.FIX_MSGTYPE_NEWS);
	}

	/**
	* setter method to set HeadLine
	*
	* @param String - Headline
	*/
	public void setHeadLine(String _Headline) {
		addBodyField(148, _Headline);
	}
	/**
	* getter method to get HeadLine
	*
	* @return String - HeadLine
	*/
	public String getHeadLine() {
		return (getBodyFieldValue(148));
	}
	/**
	* setter method to set Urgency
	*
	* @param String - Urgency
	*/
	public void setUrgency(String _Urgency) {
		addBodyField(61, _Urgency);
	}
	/**
	* getter method to get Urgency
	*
	* @return String - Urgency
	*/
	public String getUrgency() {
		return (getBodyFieldValue(61));
	}
	/**
	* setter method to set UserName
	*
	* @param String - UserName
	*/
	public void setUserName(String _UserName) {
		addBodyField(553, _UserName);
	}
	/**
	* getter method to get UserName
	*
	* @return String - UserName
	*/
	public String getUserName() {
		return (getBodyFieldValue(553));
	}
	/**
	* setter method to set LinesOfText
	*
	* @param int - LinesOfText
	*/
	public void setLinesOfText(int _LinesOfText) {
		addBodyField(33, getString(_LinesOfText));
	}
	/**
	* setter method to set LinesOfText
	*
	* @param String - LinesOfText
	*/
	public void setLinesOfText(String _LinesOfText) {
		addBodyField(33, _LinesOfText);
	}
	/**
	* getter method to get LinesOfText
	*
	* @return int - LinesOfText
	*/
	public int getLinesOfText() {
		return (stringToint(getBodyFieldValue(33)));
	}
	/**
	* getter method to get LinesOfTextAsString
	*
	* @return String - LinesOfTextAsString
	*/
	public String getLinesOfTextAsString() {
		return (getBodyFieldValue(33));
	}

}
