package com.exsys.common.business;

import com.exsys.fix.message.*;
import java.io.*;

/**
 *
 * <B>Confirm</B> class defines a cancel confirmation produced by an exchange,
 *in response to a successful cancel request. Currently this class only
 * contains minimum number of attributes. This will be enhanced shortly
 * This class provides an empty constructor, which creates an empty <B>Confirm</B>
 * and use different <B>set</B> methods to populate the object. This constructor
 * is used by an application producing cancel confirm like Matching Engine
 * application. Another constructor
 * constructs Confirm object by taking RvMsg & subject name as parameter. This
 * constructor will be used by applications which recieve an TIBCO message
 * of type <B>CONFIRM</B> and constructs Confirm object based on this message
 * and use different <B>get</B> methods to get details about this object
 * <B>TradeResponseManager</B> class uses this constructor to construct business
 * objects and invokes proper callback method of class <B>ITradeResponseHandler</B>
 * Applications need not have to use this constructor. Instead applications implement
 * <B>ITradeResponseHandler</B> interface and use TradeResponseManager to get application
 * level callbacks.
 */


public class Confirm extends IBusinessObject
{
	private Integer confirm_id;
	private String cancel_id;
	private String order_id;

	public Confirm()
	{
		this.type = IBusinessObject.CONFIRM;
		confirm_id = new Integer(0);
		cancel_id = new String();
		order_id = new String();
		subject = new String();

	}
	public Confirm( FixExecutionReport msg, String sub )
	{
		type = IBusinessObject.CONFIRM;
		cancel_id = msg.getExecRefID();
		order_id = msg.getClOrderID();
		confirm_id = new Integer(msg.getExecID());
		subject = sub;

	}
	public String getCancelId()
	{
		return cancel_id;
	}
	public Integer getConfirmId()
	{
		return confirm_id;
	}
	public FixMessage getFixMsg()
	{
		FixCancelConfirm confirm = new FixCancelConfirm();
		confirm.setExecRefID(cancel_id);
		confirm.setClOrderID(order_id);
		confirm.setOrderID(order_id);		
		confirm.setExecID(confirm_id.toString());

		return confirm;		
				
	}
	public String getOrderId()
	{
		return order_id;
	}
	public String getSubject()
	{
		return subject;
	}
	public String getType()
	{
		return type;
	}
	public void print()
	{
		System.out.println("Message Type :" + type );
		System.out.println("Confirm Id :" + confirm_id );
		System.out.println("Cancel Id :" + cancel_id );
		System.out.println("Order Id :" + order_id );
	}
	public void setCancelId( String val)
	{
		cancel_id = val;
	}
	public void setConfirmId( Integer val)
	{
		confirm_id = val;
	}
	public void setOrderId( String val )
	{
		order_id = val;
	}
	public void setSubject( String val )
	{
		subject = val;
	}
	public String toString()
	{
		String str = new String("CXL: "+getCancelId()+" "+ "CONFIRMED");
		return str;
	}
}
