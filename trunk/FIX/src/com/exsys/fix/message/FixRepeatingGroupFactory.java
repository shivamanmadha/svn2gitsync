package com.exsys.fix.message;


import com.exsys.common.exceptions.*;
/**
 * This class is used to create appropriate fix repeating group object
 * Creation date: (11/18/01 10:13:09 PM)
 * @author: Administrator
 */
public class FixRepeatingGroupFactory
{
/**
 * FixRepeatingGroupFactory constructor comment.
 */
public FixRepeatingGroupFactory() {
	super();

}
/**
 * Method to create appropriate repeatin group object based on the tagNum
 * and msgType 
 * Creation date: (11/18/01 10:13:49 PM)
 * @return com.exsys.fix.message.FixMessage
 * @param msg byte[]
 */
public static FixRepeatedGroup createFixRepeatingGroup(String tagNum,String msgType)
		throws FixProtocolError
{


	// we need to construct specific type of message
	FixRepeatedGroup fixRG = null;

	if( tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NORELATEDSYM)
  	    && msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE)	)
	{		
		fixRG = new FixRGNoRelatedSym();
	}
	else if( tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NORELATEDSYM)
  	    && msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST)	)
	{
		fixRG = new FixRGNoRelatedSym_V();
	}
	else if( tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NOMDENTRYTYPTES)
  	    && msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST)	)
	{
		fixRG = new FixRGNoMDEntryTypes_V();
	}	
	else if( tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NOMDENTRIES)
  	    && msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH)	)
	{
		fixRG = new FixRGNoMDEntries_W();
	}	
	else if( tagNum.equals(FixConstants.FIX_REPEATEDGROUP_NOMDENTRIES)
  	    && msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH)	)
	{
		fixRG = new FixRGNoMDEntries_X();
	}		
	else if( tagNum.equals(FixConstants.FIX_REPEATEDGROUP_LINESOFTEXT)
  	    && msgType.equals(FixConstants.FIX_MSGTYPE_NEWS)	)
	{
		fixRG = new FixRGLinesOfText_B();
	}			
	else
	{
		throw new FixProtocolError("Unknown Repeating Group","0");
	}

	return fixRG;

}
}
