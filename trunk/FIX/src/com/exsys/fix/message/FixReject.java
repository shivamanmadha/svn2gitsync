package com.exsys.fix.message;

/**
/**
 * This sub class of FixExecutionReport can be used to represent 
 * execution reports of type REJECT.
 */
public class FixReject extends FixExecutionReport {

   /**
    * Constructor to construct FixReject object
    *
    */
public FixReject() 
{
	setExecType(FixConstants.FIX_EXECUTIONREPORT_REJECTED);
}
}
