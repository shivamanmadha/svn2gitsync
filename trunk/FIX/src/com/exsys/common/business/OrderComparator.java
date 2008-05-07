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
class OrderComparator implements Comparator
{

	public int compare( Object o1, Object o2 )
	{

		if( (((Order)o1).getOrderId()).equals(((Order)o1).getOrderId()))
		{
			return 0;
		}
		else
		   return 1;
		

		
		
	}
	public boolean equals( Object o1 )
	{
		return true;
	}
	
}
