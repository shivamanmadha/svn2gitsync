package com.exsys.common.business;

import com.exsys.fix.message.*;
import java.io.*;

/**
 *
 * <B>Order</B> class defines an order to buy or sell. Currently this class only
 * contains minimum number of attributes. This will be enhanced shortly
 * This class provides an empty constructor, which creates an empty order
 * and use different <B>set</B> methods to populate the object. This constructor
 * is used by an application producing an order message, for example, an order entry.
 * application. Another constructor
 * constructs Order object by taking RvMsg & subject name as parameter. This
 * constructor will be used by applications which recieve an TIBCO message
 * of type <B>ORDER</B> and constructs Order object based on this message
 * and use different <B>get</B> methods to get details about this object.
 * <B>TradeResponseManager</B> class uses this constructor to construct business
 * objects and invokes proper callback method of class <B>ITradeResponseHandler</B>
 * Applications need not have to use this constructor. Instead applications implement
 * <B>ITradeResponseHandler</B> interface and use TradeResponseManager to get application
 * level callbacks.
 */

public class Order extends IBusinessObject
{
	private String order_id;// this should not be used for an order
	// when constructing an order. But when cofirm is received, it should be
	// set with value received from the exchange
	private String cl_order_id;
	private String corr_cl_order_id; // same as cl_order_id for first order
	// in the chain.
	private String user;
	private String order_type;
	private String buy_or_sell;
	private String time_in_force;
	private String symbol;
	private String productGroup;
	private Integer quantity;
	private Integer open_quantity;
	private String price;
	private String stopPrice;
	private String account;
	private String subAccount;
	private String securityType;
	private String expireDate;
	private String minQty;
	private String maxShow;
	private String customerOrFirm;
	private String giveupFirm;
	private String cmtaGiveupCode;					
	private String ctiCode;
	private String putCall;						
	private String strike;	
	private String expYYYYMM;
	private String expDay;
						
	
	public Order()
	{
		type = IBusinessObject.ORDER;
                orderStatus = IBusinessObject.PENDACCEPT;
		order_id = new String();
		cl_order_id = new String();
		corr_cl_order_id = new String();		
		user = new String();
		order_type = new String();
		buy_or_sell = new String();
		time_in_force = new String();
		symbol = new String();
		productGroup = new String();
		quantity = new Integer(0);
		open_quantity = new Integer(0);
		price = new String();
		stopPrice = new String();
		subject = new String();
		account = new String();
		subAccount = new String();
		securityType = new String();
		expireDate = new String();
		minQty = new String();
		maxShow = new String();
		customerOrFirm = new String();
		giveupFirm = new String();
		cmtaGiveupCode = new String();
		ctiCode = new String();		
		putCall = new String();
		strike = new String();
		expYYYYMM = new String();
		expDay = new String();		

	}
	public Order( FixOrder msg, String sub )
	{
		type = IBusinessObject.ORDER;
                orderStatus = IBusinessObject.PENDACCEPT;
		order_id = msg.getClOrderID();
		cl_order_id = msg.getClOrderID();
		corr_cl_order_id = msg.getCorrelationClOrdID();		
		user = msg.getSenderCompID();
		order_type = translateFromFixOrderType(msg.getOrdType());
		buy_or_sell = translateFromFixBuyOrSell(msg.getSide());
		time_in_force = translateFromFixTimeInForce(msg.getTimeInForce());
		productGroup = msg.getSymbol();
		symbol = msg.getSecurityDesc();
		quantity = new Integer(msg.getOrderQtyAsString());
		open_quantity = quantity;
		price = msg.getPriceAsString();
		stopPrice = msg.getStopPxAsString();
		subject = sub;
		account = msg.getAccount();
		subAccount = msg.getOmnibusAccount();
		securityType = msg.getSecurityType();
		expireDate = msg.getExpireDateAsString();
		minQty = msg.getMinQtyAsString();
		maxShow = msg.getMaxShowAsString();
		customerOrFirm = msg.getCustomerOrFirmAsString();
		giveupFirm = msg.getGiveupFirm();
		cmtaGiveupCode = msg.getCmtaGiveupCD();
		ctiCode = msg.getCtiCodeAsString();	
		if(securityType != null && securityType.equals(FixFieldConstants.SECURITY_TYPE_OPTION))
		{	
			putCall = msg.getPutOrCallAsString();
			strike = msg.getStrikePriceAsString();
			expYYYYMM = msg.getMaturityYearAsString();
			expDay = msg.getMaturityDayAsString();		
		}
		

	}
	public boolean equals( Object object )
	{
		//if( object instanceof Order  )
		//{
			System.out.println("In Equals");
			//String clOrderId = this.cl_order_id;
			
			if( (this.cl_order_id).equals(((Order)object).cl_order_id) )
				return true;
			else
				return false;

		//}
		//else
		  //return false;
	}
	public String getBuyOrSell()
	{
		return buy_or_sell;
	}
	public FixMessage getFixMsg()
	{
		FixOrder ord = new FixOrder();
		ord.setClOrderID(cl_order_id);
		ord.setSenderCompID(user);
		ord.setOrdType(translateToFixOrderType(order_type));
		ord.setSide(translateToFixBuyOrSell(buy_or_sell));
		ord.setTimeInForce(translateToFixTimeInForce(time_in_force));
		ord.setSymbol(symbol);
		ord.setOrderQty(quantity.toString());
		ord.setPrice(price);

		return ord;
	}

        public void OrderStatus(String status) { this.orderStatus = status; }
        public String OrderStatus() { return this.orderStatus; }

	public Integer getOpenQuantity()
	{
		return open_quantity;
	}
	public String getAccount()
	{
		return account;
	}
	public String getSubAccount()
	{
		return subAccount;
	}
	public String getSecurityType()
	{
		return securityType;
	}

	public String getOrderId()
	{
		return order_id;
	}	
	public String getClientOrderId()
	{
		return cl_order_id;
	}	
	public String getCorrelationClientOrderId()
	{
		return corr_cl_order_id;
	}	
	
	public String getOrderType()
	{
		return order_type;
	}
	public String getPrice()
	{
		return price;
	}
	public String getStopPrice()
	{
		return stopPrice;
	}	
	public Integer getQuantity()
	{
		return quantity;
	}
	public String getSubject()
	{
		return subject;
	}
	public String getSymbol()
	{
		return symbol;
	}
	public String getProductGroup()
	{
		return productGroup;
	}	
	public String getTimeInForce()
	{
		return time_in_force;
	}
	public String getExpireDate()
	{
		return expireDate;
	}	
	public String getType()
	{
		return type;
	}

	public String getMinQty()
	{
		return minQty;
	}
	public String getMaxShow()
	{
		return maxShow;
	}
	public String getCustomerOrFirm()
	{
		return customerOrFirm;
	}
	public String getCmtaGiveupCode()
	{
		return cmtaGiveupCode;
	}
	public String getCtiCode()
	{
		return ctiCode;
	}
	public String getGiveupFirm()
	{
		return giveupFirm;
	}	

	public String getPutOrCall()
	{
		return putCall;
	}
	public String getStrike()
	{
		return strike;
	}
	public String getExpYYYYMM()
	{
		return expYYYYMM;
	}
	public String getExpDay()
	{
		return expDay;
	}								
		
	
	public String getUser()
	{
		return user;
	}
	public void print()
	{
		System.out.println("Message Type :" + type );
		System.out.println("ClOrder Id :" + cl_order_id );
                System.out.println("Order Status :"+ orderStatus);
		System.out.println("User :" + user );
		System.out.println("Order Type :" + order_type );
		System.out.println("Buy Or Sell :" + buy_or_sell );
		System.out.println("TIF :" + time_in_force );
		System.out.println("Symbol :" + symbol );
		System.out.println("Quantity :" + quantity );
		System.out.println("Open Quantity :" + open_quantity );
		System.out.println("Price :" + price );
		System.out.println("Account :" + account );
		System.out.println("SubAccount :" + subAccount );
	}
	public void setAccount( String val)
	{
		account = val;
	}
	public void setSubAccountl( String val)
	{
		subAccount = val;
	}
	public void setSecurityType( String val)
	{
		securityType = val;
	}			
	public void setBuyOrSell( String val)
	{
		buy_or_sell = val;
	}
	public void setOpenQuantity( Integer val)
	{
		open_quantity = val;
	}
	public void setOrderId( String val)
	{
		order_id = val;
	}
	public void setClientOrderId( String val)
	{
		cl_order_id = val;
	}	
	public void setCorrelationClientOrderId( String val)
	{
		corr_cl_order_id = val;
	}	
	
	public void setOrderType( String val )
	{
		order_type = val;
	}
	public void setPrice( String val)
	{
		 price = val;
	}
	public void setStopPrice( String val)
	{
		 stopPrice = val;
	}	
	public void setQuantity( Integer val)
	{
		quantity = val;
	}
	public void setSubject( String val )
	{
		subject = val;
	}
	public void setSymbol( String val)
	{
		symbol = val;
	}
	public void setProductGroup( String val)
	{
		productGroup = val;
	}	
	public void setTimeInForce( String val)
	{
		time_in_force = val;
	}
	public void setExpireDate( String val)
	{
		expireDate = val;
	}
	
	public void setMinQty( String val)
	{
		minQty = val;
	}
	public void setMaxShow( String val)
	{
		maxShow = val;
	}
	public void setCustomerOrFirm( String val)
	{
		customerOrFirm = val;
	}
	public void setCmtaGiveupCode( String val)
	{
		cmtaGiveupCode = val;
	}
	public void setCtiCode( String val)
	{
		ctiCode = val;
	}
	public void setGiveupFirm( String val)
	{
		giveupFirm = val;
	}
	
	public void setPutOrCall( String val)
	{
		putCall = val;
	}
	public void setStrike( String val)
	{
		strike = val;
	}
	public void setExpYYYYMM( String val)
	{
		expYYYYMM = val;
	}
	public void setExpDay( String val)
	{
		expDay = val;
	}								
	
	public void setUser( String val )
	{
		user = val;
	}
	public String toString()
	{
		String priceStr = new String(buy_or_sell+" "+symbol+" "+quantity.toString()+"@"+price);
		String ordString = new String(securityType+"-ORD: "+cl_order_id+" "+priceStr+" "+ "SUBMITTED - OrdStatus:"+orderStatus);

		return ordString;

	}
}
