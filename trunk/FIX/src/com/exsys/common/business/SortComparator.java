/*
 *@(#)OrderManager.java
 *
 * Copyright 2000, by Saven Technologies Inc.
 * Chicago, USA
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Saven Technologies, Inc. You shall not disclose such 
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * Saven.
 */


package com.exsys.common.business;

import java.io.*;
import java.util.*;
class SortComparator implements Comparator
{

	public int compare( Object o1, Object o2 )
	{

		Double price1 = new Double( ((Order)o1).getPrice() );
		Double price2 = new Double( ((Order)o2).getPrice() );

		if( (((Order)o1).getBuyOrSell()).equals(IBusinessObject.BUY))
		{
			return( price2.compareTo(price1) );
		}
		else
		{
			return( price2.compareTo(price1) );
		}
		
	}
	public boolean equals( Object o1 )
	{
		return true;
	}
	
}
