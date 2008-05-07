package com.exsys.fix.message;
/**
* This class is used to represent the FIX message SequenceReset
*
*/
public class FixSequenceReset extends FixMessage {

	/**
	* setter method to set GapFillFalg
	*
	* @param boolean - GapFillFalg
	*/
	public void setGapFillFalg(boolean _GapFillFalg) {
		addBodyField(123, getString(_GapFillFalg));
	}
	/**
	* setter method to set GapFillFalg
	*
	* @param String - GapFillFalg
	*/
	public void setGapFillFalg(String _GapFillFalg) {
		addBodyField(123, _GapFillFalg);
	}
	/**
	* getter method to get GapFillFalg
	*
	* @return boolean - GapFillFalg
	*/
	public boolean getGapFillFalg() {
		return (stringToboolean(getBodyFieldValue(123)));
	}
	/**
	* getter method to get GapFillFalgAsString
	*
	* @return String - GapFillFalgAsString
	*/
	public String getGapFillFalgAsString() {
		return (getBodyFieldValue(123));
	}
	/**
	* setter method to set NewSeqNo
	*
	* @param int - NewSeqNo
	*/
	public void setNewSeqNo(int _NewSeqNo) {
		addBodyField(36, getString(_NewSeqNo));
	}
	/**
	* setter method to set NewSeqNo
	*
	* @param String - NewSeqNo
	*/
	public void setNewSeqNo(String _NewSeqNo) {
		addBodyField(36, _NewSeqNo);
	}
	/**
	* getter method to get NewSeqNo
	*
	* @return int - NewSeqNo
	*/
	public int getNewSeqNo() {
		return (stringToint(getBodyFieldValue(36)));
	}
	/**
	* getter method to get NewSeqNoAsString
	*
	* @return String - NewSeqNoAsString
	*/
	public String getNewSeqNoAsString() {
		return (getBodyFieldValue(36));
	}

	/**
	* Constructor to construct FixSequenceReset object 
	*
	*/
	public FixSequenceReset() {
		setMessageType(FixConstants.FIX_MSGTYPE_SEQUENCERESET);
		setMsgType(FixConstants.FIX_MSGTYPE_SEQUENCERESET);
	}
}
