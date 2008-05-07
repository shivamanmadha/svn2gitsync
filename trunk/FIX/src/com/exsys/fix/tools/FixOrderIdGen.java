package com.exsys.fix.tools;

import java.text.*;
import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (8/9/2002 6:32:40 PM)
 * @author: Administrator
 */
public class FixOrderIdGen {
	private static String prefix = "Test-";
	private static FixOrderIdGen instance = null;
	private static java.text.SimpleDateFormat sd = new SimpleDateFormat("MMddyyyyhhmmss");
	private int count=0;

	
/**
 * FixOrderIdGen constructor comment.
 */
private FixOrderIdGen() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 6:34:21 PM)
 */
synchronized public static FixOrderIdGen getInstance() 
{
	if( instance == null )
	{
		prefix += sd.format(new Date());
		prefix += "-";
		instance = new FixOrderIdGen();
	}
	return instance;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 6:39:35 PM)
 */
synchronized public String getNextOrderId() 
{
	return( prefix+String.valueOf(++count));
}
}
