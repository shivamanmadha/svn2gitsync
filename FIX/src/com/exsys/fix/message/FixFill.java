package com.exsys.fix.message;

/**
 * This sub class of FixExecutionReport can be used to represent 
 * execution reports of type Fill, either partial fill or complete fill.
 */
public class FixFill extends FixExecutionReport {
	private boolean partial = false;
/**
 * Constructor to create FixFill object.
 * Creation date: (2/6/2002 7:18:32 AM)
 * @param isPartial boolean
 */
public FixFill(boolean isPartial) 
{
	partial = isPartial;
	if( isPartial )
	{
		setExecType(FixConstants.FIX_EXECUTIONREPORT_PARTIAL);
	}
	else
	{
		setExecType(FixConstants.FIX_EXECUTIONREPORT_FILL);
	}
}
/**
 * Method to test if this is partial fill or not
 * Creation date: (2/6/2002 7:18:07 AM)
 * @return boolean
 */
public boolean isPartial() {
	return partial;
}
}
