/*
 *@(#)ITradeResponseHandler.java
 *
 * Copyright 2000, by Exchange Systems Inc.
 * Chicago, USA
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Saven Technologies, Inc. You shall not disclose such 
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * Saven.
 */

package com.exsys.common.extrading;

import com.exsys.common.business.*;
import java.io.*;

/**
 * This class defines generic interface for trading applications to
 * recieve application level messages. Applications implementing this 
 * interface, would provide different behaviors when these events happen
 */
public interface ITradeResponseHandler
{
	public void ReceivedOrder( Order ord );
	public void ReceivedCancel( Cancel cxl );
	public void ReceivedFill( Fill fill );
	public void ReceivedPartialFill( Fill fill );
	public void ReceivedAccept( Response resp );
	public void ReceivedReject( Response resp );
	public void ReceivedConfirm( Confirm conf );
	public void ReceivedQuote( RealTimeQuote quote );
	public void ReceivedStatusRequest();
	public RealTimeQuote ReceivedQuoteRequest();

	public void ReceivedCancelReplace( CancelReplace cxr );
}