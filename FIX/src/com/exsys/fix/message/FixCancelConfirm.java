package com.exsys.fix.message;

/**
 * This sub class of FixExecutionReport can be used to represent 
 * execution reports of type Cancel Confirm.
 */
public class FixCancelConfirm extends FixExecutionReport {

	/**
	 * Constructor to construct FixCancelConfirm object
	 *
	 */
	public FixCancelConfirm() {
		setExecType(FixConstants.FIX_EXECUTIONREPORT_CANCELED);
	}
}