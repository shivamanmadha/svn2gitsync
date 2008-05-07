package com.exsys.common.util;

import java.text.*;
import java.util.*;
import java.io.*;
/**
 * helper class used to handle generation ofunique id numbers
 * for orders, cancels, cancel replaces and other business
 * messages
 * Creation date: (8/9/2002 6:32:40 PM)
 * @author: Administrator
 */
public class FixUniqueIdGen {

	private static FixUniqueIdGen instance = null;	
	private static int orderId = 0;
	private static int cancelId = 0;
	private static int execId = 0;
	private static int secId = 0;
	private static int mktdataId = 0;
	private static Properties prop = new Properties();
	private static  FileOutputStream outWriter = null;
	


/**
 * FixUniqueIdGen constructor.
 */
private FixUniqueIdGen() {
	super();
}
/**
 * Static method to initialize the instance from a file
 * Creation date: (8/9/2002 6:34:21 PM)
 */
synchronized public static FixUniqueIdGen getInstance(String fileName) throws Exception
{
	if( instance == null )
	{
		try
		{
			instance = new FixUniqueIdGen();
			prop.load(new FileInputStream(fileName));
			outWriter = new FileOutputStream(fileName,true);
			orderId = Integer.parseInt(prop.getProperty("OrderID"));
			cancelId = Integer.parseInt(prop.getProperty("CancelID"));
			execId = Integer.parseInt(prop.getProperty("ExecID"));
			secId = Integer.parseInt(prop.getProperty("SecID"));
			mktdataId = Integer.parseInt(prop.getProperty("MktID"));
		}
		catch( Exception e )
		{
			e.printStackTrace();
			throw e;
		}
	}
	return instance;
}
/**
 * method to get next orderid
 * Creation date: (8/9/2002 6:39:35 PM)
 */
synchronized public static String getNextOrderId() throws Exception
{
		
	return(getNextOrderId(8));
}
/**
 * method to get next order id
 * @param length
 * @return
 * @throws Exception
 */
synchronized public static String getNextOrderId(int length) throws Exception
{
	++orderId;
	String id = String.valueOf(orderId);
	
	try
	{
		prop.setProperty("OrderID",id);
		prop.store(outWriter,null);
	}
	catch (Exception e )
	{
		e.printStackTrace();
		throw e;
	}
		
	return( StringUtilities.prePad(id,'0',length));
}
/**
 * method to get next cancel id
 * @return
 * @throws Exception
 */
synchronized public static String getNextCancelId() throws Exception
{
	++cancelId;
	String id = String.valueOf(cancelId);
	
	try
	{
		prop.setProperty("CancelID",id);
		prop.store(outWriter,null);
	}
	catch (Exception e )
	{
		e.printStackTrace();
		throw e;
	}
		
	return( StringUtilities.prePad(id,'0',5));
}
/**
 * method to get next execution id
 * @return
 * @throws Exception
 */
synchronized public static String getNextExecId() throws Exception
{
	++execId;
	String id = String.valueOf(execId);
	
	try
	{
		prop.setProperty("ExecID",id);
		prop.store(outWriter,null);
	}
	catch (Exception e )
	{
		e.printStackTrace();
		throw e;
	}
		
	return( StringUtilities.prePad(id,'0',8));
}
/**
 * method to get next security id
 * @return
 * @throws Exception
 */
synchronized public static String getNextSecId() throws Exception
{
	++secId;
	String id = String.valueOf(secId);
	
	try
	{
		prop.setProperty("SecID",id);
		prop.store(outWriter,null);
	}
	catch (Exception e )
	{
		e.printStackTrace();
		throw e;
	}
		
	return( StringUtilities.prePad(id,'0',4));
}

/**
 * method to get next market data id
 * @return
 * @throws Exception
 */
synchronized public static String getNextMktDataId() throws Exception
{
	++mktdataId;
	String id = String.valueOf(mktdataId);
	
	try
	{
		prop.setProperty("MktID",id);
		prop.store(outWriter,null);
	}
	catch (Exception e )
	{
		e.printStackTrace();
		throw e;
	}
		
	return( StringUtilities.prePad(id,'0',4));
}
}
