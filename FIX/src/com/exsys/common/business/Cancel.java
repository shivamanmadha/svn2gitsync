package com.exsys.common.business;

import com.exsys.fix.message.*;
import java.io.*;

/**
 *
 * <B>Cancel</B> class defines a cancel request to cancel an existing order.
 * Currently this class only
 * contains minimum number of attributes. This will be enhanced shortly
 * This class provides an empty constructor, which creates an empty <B>cancel</B>
 * and use different <B>set</B> methods to populate the object. This constructor
 * is used by an application producing a cancel message, for example, an order entry.
 * application. Another constructor
 * constructs Cancel object by taking RvMsg & subject name as parameter. This
 * constructor will be used by applications which recieve an TIBCO message
 * of type <B>CANCEL</B> and constructs Order object based on this message
 * and use different <B>get</B> methods to get details about this object
 * <B>TradeResponseManager</B> class uses this constructor to construct business
 * objects and invokes proper callback method of class <B>ITradeResponseHandler</B>
 * Applications need not have to use this constructor. Instead applications implement
 * <B>ITradeResponseHandler</B> interface and use TradeResponseManager to get application
 * level callbacks.
 */


public class Cancel extends IBusinessObject
{
	private String cancel_id; // this is similar to ClOrderID
	private String tbc_order_id; // this is similar to OrigClOrderID
	private String exch_order_id; // this is the unique OrderID returned
								  // from the exch when confirming an order or cxr	
	private String corr_cl_order_id; // same as cl_order_id for first order
	// in the chain.
								  
	private String symbol;
	private String productGroup;	
	private String user;
	private Integer cancel_quantity;
	private String buy_or_sell;
	
	private String account;
	private String time_in_force;
	private String stopPrice;
	private String securityType;
	private String order_type;

	public Cancel()
	{
		this.type = IBusinessObject.CANCEL;
		cancel_id = new String();
		tbc_order_id = new String();
		buy_or_sell = new String();
		symbol = new String();
		user = new String();
		cancel_quantity = new Integer(0);
		subject = new String();
		this.orderStatus = "SUBMITTED";

	}
    public void OrderStatus(String status) { this.orderStatus = status; }
    public String OrderStatus() { return this.orderStatus; }	
	public String getBuyOrSell()
	{
		return buy_or_sell;
	}
	public String getCancelId()
	{
		return cancel_id;
	}
	public Integer getCancelQuantity()
	{
		return cancel_quantity;
	}
	public FixMessage getFixMsg()
	{

		FixCancel cxl = new FixCancel();
		cxl.setClOrderID(cancel_id);
		cxl.setOrigClOrderID(tbc_order_id);
		cxl.setSenderCompID(user);
		cxl.setSide(translateToFixBuyOrSell(buy_or_sell));
		cxl.setSymbol(symbol);
		cxl.setOrderQty(cancel_quantity.toString());

		return cxl;
		
	}
	public String getSecurityType()
	{
		return securityType;
	}
	public String getOrderType()
	{
		return order_type;
	}
	public String getStopPrice()
	{
		return stopPrice;
	}
	public String getTimeInForce()
	{
		return time_in_force;
	}			
	public String getSubject()
	{
		return subject;
	}
	public String getSymbol()
	{
		return symbol;
	}
	public String getTBCOrderId()
	{
		return tbc_order_id;
	}
	public String getExchangeOrderId()
	{
		return exch_order_id;
	}
	public String getCorrClOrderId()
	{
		return corr_cl_order_id;
	}
	
	public String getType()
	{
		return type;
	}
	public String getUser()
	{
		return user;
	}
	public void print()
	{
		System.out.println("Message Type :" + type );
		System.out.println("Cancel Id :" + cancel_id );
		System.out.println("TBC Order Id :" + tbc_order_id );
		System.out.println("Symbol :" + symbol );
		System.out.println("User :" + user );
		System.out.println("Buy Or Sell :" + buy_or_sell );
		System.out.println("Cancel Quantity :" + cancel_quantity );
	}
	public void setOrderType( String val )
	{
		order_type = val;
	}
    public void setSecurityType( String val)
	{
		securityType = val;
	}
	public void setStopPrice( String val)
	{
		 stopPrice = val;
	}
	public void setTimeInForce( String val)
	{
		time_in_force = val;
	}				
	public void setBuyOrSell( String val)
	{
		buy_or_sell = val;
	}
	public void setCancelId( String val)
	{
		cancel_id = val;
	}
	public void setCancelQuantity( Integer val)
	{
		cancel_quantity = val;
	}
	public void setSubject( String val )
	{
		subject = val;
	}
	public void setSymbol( String val)
	{
		symbol = val;
	}
	public void setTBCOrderId( String val)
	{
		tbc_order_id = val;
	}
	public void setExchangeOrderId( String val)
	{
		exch_order_id = val;
	}
	public void setCorrClOrderId( String val)
	{
		corr_cl_order_id = val;
	}
	
	public void setType( String val )
	{
		type = val;
	}
	public void setUser( String val)
	{
		user = val;
	}
	public String toString()
	{
		String cxlString = new String("CXL: "+getCancelId() + " CXLing "+getTBCOrderId()+ OrderStatus());
		return cxlString;

	}
	public String getAccount()
	{
		return account;
	}
	public void setAccount( String val)
	{
		account = val;
	}	
	public void setProductGroup( String val)
	{
		productGroup = val;
	}	
	public String getProductGroup()
	{
		return productGroup;
	}	
	public Cancel( FixCancel msg, String sub )
	{

		type = IBusinessObject.CANCEL;
		cancel_id = msg.getClOrderID();
		tbc_order_id = msg.getOrigClOrderID();
		//symbol = msg.getSymbol();
		symbol = msg.getSecurityDesc();
		buy_or_sell = translateFromFixBuyOrSell(msg.getSide());
		user = msg.getSenderCompID();
		cancel_quantity = new Integer(msg.getOrderQtyAsString());
		subject = sub;
		this.orderStatus = "SUBMITTED";		
		
		account = msg.getAccount();
		//stopPrice = msg.getStopPxAsString();
		securityType = msg.getSecurityType();
		//time_in_force = translateFromFixTimeInForce(msg.getTimeInForce());
		//order_type = translateFromFixOrderType(msg.getOrdType());

	}
}
