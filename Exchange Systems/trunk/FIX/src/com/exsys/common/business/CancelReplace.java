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


public class CancelReplace extends IBusinessObject
{
	private Order newOrder;
	private Cancel cancel;
	public CancelReplace(Order newOrd, Cancel cxl)
	{
		this.type = IBusinessObject.CANCEL_REPLACE;
		this.newOrder = newOrd;
		this.cancel = cxl;

	}
	public CancelReplace( FixCancelReplace msg, String sub )
	{

		// initialize cancel and new order attributes
		cancel = new Cancel();
		newOrder = new Order();

		type = IBusinessObject.CANCEL_REPLACE;		


		cancel.setCancelId( msg.getClOrderID());
		cancel.setTBCOrderId( msg.getOrigClOrderID());
		//cancel.setSymbol(msg.getSymbol());
		cancel.setSymbol(msg.getSecurityDesc());
		cancel.setBuyOrSell( translateFromFixBuyOrSell(msg.getSide()) );
		cancel.setUser(msg.getSenderCompID());
		cancel.setCancelQuantity(new Integer(msg.getOrderQtyAsString()) );
		subject = sub;
        orderStatus = IBusinessObject.PENDACCEPT;
		newOrder.setOrderId(msg.getClOrderID());
		newOrder.setClientOrderId(msg.getClOrderID());
		newOrder.setCorrelationClientOrderId(msg.getCorrelationClOrdID());
		newOrder.setUser(msg.getSenderCompID());
		newOrder.setOrderType(translateFromFixOrderType(msg.getOrdType()));
		newOrder.setBuyOrSell(translateFromFixBuyOrSell(msg.getSide()));
		newOrder.setTimeInForce(translateFromFixTimeInForce(msg.getTimeInForce()));
		newOrder.setSymbol(msg.getSecurityDesc());
		newOrder.setProductGroup(msg.getSymbol());
		newOrder.setAccount(msg.getAccount());
		newOrder.setSubAccountl(msg.getOmnibusAccount());
		newOrder.setQuantity(new Integer(msg.getOrderQtyAsString()));
		newOrder.setOpenQuantity(newOrder.getQuantity());
		newOrder.setPrice(msg.getPriceAsString());
		newOrder.setMinQty(msg.getMinQtyAsString());
		newOrder.setMaxShow(msg.getMaxShowAsString());
		newOrder.setCtiCode(msg.getCtiCodeAsString());
		newOrder.setCustomerOrFirm(msg.getCustomerOrFirmAsString());
		newOrder.setGiveupFirm(msg.getGiveupFirm());
		newOrder.setCmtaGiveupCode(msg.getCmtaGiveupCD());
		newOrder.setSecurityType(msg.getSecurityType());
			
	}		
	public String getBuyOrSell()
	{
		return cancel.getBuyOrSell();
	}
/**
 * Insert the method's description here.
 * Creation date: (4/2/2002 5:40:28 PM)
 * @return com.exsys.common.business.Cancel
 */
public Cancel getCancel() {
	return cancel;
}
	public String getCancelId()
	{
		return cancel.getCancelId();
	}
	public Integer getCancelQuantity()
	{
		return cancel.getCancelQuantity();
	}
	public FixMessage getFixMsg()
	{

		FixCancelReplace cxr = new FixCancelReplace();
		cxr.setClOrderID(newOrder.getOrderId());
		cxr.setSenderCompID(newOrder.getUser());
		cxr.setOrdType(translateToFixOrderType(newOrder.getOrderType()));
		cxr.setSide(translateToFixBuyOrSell(newOrder.getBuyOrSell()));
		cxr.setTimeInForce(translateToFixTimeInForce(newOrder.getTimeInForce()));
		cxr.setSymbol(newOrder.getSymbol());
		cxr.setOrderQty(String.valueOf(newOrder.getQuantity()));
		cxr.setPrice(newOrder.getPrice());

		cxr.setOrigClOrderID(cancel.getTBCOrderId());
		//cxl.setOrderQty(cancel_quantity.toString());
		
		return cxr;
		
	}
/**
 * Insert the method's description here.
 * Creation date: (4/2/2002 5:39:57 PM)
 * @return com.exsys.common.business.Order
 */
public Order getNewOrder() {
	return newOrder;
}
	public String getSubject()
	{
		return subject;
	}
	public String getSymbol()
	{
		return cancel.getSymbol();
	}
	public String getTBCOrderId()
	{
		return cancel.getTBCOrderId();
	}
	public String getType()
	{
		return type;
	}
	public String getUser()
	{
		return cancel.getUser();
	}
	public void print()
	{
		System.out.println("Message Type :" + type );
		System.out.println("Cancel Id :" + cancel.getCancelId() );
		System.out.println("TBC Order Id :" + cancel.getTBCOrderId() );
		System.out.println("Symbol :" + cancel.getSymbol() );
		System.out.println("User :" + cancel.getUser() );
		System.out.println("Buy Or Sell :" + cancel.getBuyOrSell() );
		System.out.println("Cancel Quantity :" + cancel.getCancelQuantity() );
	}
	public void setSubject( String val )
	{
		subject = val;
	}
	public void setType( String val )
	{
		type = val;
	}
	public String toString()
	{
		String cxrString = new String("CXR: "+getCancelId() + " CXRing "+getTBCOrderId()+ "SUBMITTED");
		return cxrString;

	}
}
