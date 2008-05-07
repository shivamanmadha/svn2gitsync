package com.exsys.fix.message;

/**
 * This sub class of FixExecutionReport can be used to represent 
 * execution reports of type ACCEPT.
 */
public class FixAccept extends FixExecutionReport {

   /**
    * Constructor to construct FixAccept object
    *
    */
	public FixAccept() {
		setExecType(FixConstants.FIX_EXECUTIONREPORT_NEW);
	}
}