package com.exsys.common.business;

import com.exsys.fix.message.*;
import java.io.*;

/**
 *
 * <B>Response</B> class defines an accept or reject of an order  by the exchange 
 * Currently this class only
 * contains minimum number of attributes. This will be enhanced shortly
 * This class provides a constructor, which creates an empty <B>Response, Accept or Reject</B>
 * based on the parameter, and use different <B>set</B> methods to populate the object. This constructor
 * is used by an application producing a Response message, for example, order matching exchange 
 * application. Another constructor
 * constructs Response object by taking RvMsg & subject name as parameter. This
 * constructor will be used by applications which recieve an TIBCO message
 * of type <B>ACCEPT or REJECT</B> and constructs Response object based on this message
 * and use different <B>get</B> methods to get details about this object
 * <B>TradeResponseManager</B> class uses this constructor to construct business 
 * objects and invokes proper callback method of class <B>ITradeResponseHandler</B>
 * Applications need not have to use this constructor. Instead applications implement
 * <B>ITradeResponseHandler</B> interface and use TradeResponseManager to get application
 * level callbacks.
 */

public class Response extends IBusinessObject
{
	private String request_id; // this is same as cl_order_id
	private String response_id; // this is same as execid
	private String reason;
	private String buy_or_sell;
	private String symbol;
	private Integer request_open_quantity;
	private String account;
	private String execTransType;
	private String execType;
	private String orderStatus;
	private String order_id;
	private Integer orderQty;
	private String ordType;
	private String securityDesc;
	private String time_in_force;
	private String stop_price;
	private String security_type;
	private String corr_cl_order_id;
	
	
private void init(FixExecutionReport msg, String msgType, String sub) 
{
		type = msgType;
		request_id = msg.getClOrderID();
		account = msg.getAccount();
		//reason = msg.getOrdRejReasonAsString();
		reason = msg.getText();
		symbol = msg.getSymbol();
		buy_or_sell = translateFromFixBuyOrSell(msg.getSide());
		response_id = msg.getExecID();
		request_open_quantity = new Integer(msg.getLeavesQtyAsString());
		subject = new String( sub );
}

	

public Response( FixAccept msg, String sub )
{
		type = IBusinessObject.ACCEPT;
		init(msg,type,sub);
}
/**
 * Insert the method's description here.
 * Creation date: (2/6/2002 11:05:21 PM)
 * @param msg com.exsys.fix.message.FixReject
 * @param sub java.lang.String
 */
public Response(FixExecutionReport msg, String msgType, String sub) 
{
	init(msg,msgType,sub);
}
/**
 * Insert the method's description here.
 * Creation date: (2/6/2002 11:05:21 PM)
 * @param msg com.exsys.fix.message.FixReject
 * @param sub java.lang.String
 */
public Response(FixReject msg, String sub) 
{
		type = IBusinessObject.REJECT;
		init(msg,type,sub);
}

public Response( String type, Order ord )
{
		this.type = type;
				
		// set last 8 chars as request id
		String clOrderId = ord.getClientOrderId();
		request_id = clOrderId.substring(clOrderId.length()-8);		
		account = ord.getAccount();
		request_open_quantity = ord.getOpenQuantity();
		symbol =  ord.getProductGroup();
		buy_or_sell =  ord.getBuyOrSell();
		orderQty = ord.getQuantity();
		ordType = ord.getOrderType();
		securityDesc = ord.getSymbol();
		time_in_force = ord.getTimeInForce();
		stop_price = ord.getStopPrice();
		security_type = ord.getSecurityType();
		corr_cl_order_id = ord.getCorrelationClientOrderId();
		
}
public Response( String type, Cancel cancel )
{
		this.type = type;
				
		// set last 5 chars as request id
		String clOrderId = cancel.getCancelId();
		request_id = clOrderId.substring(clOrderId.length()-5);		
		account = cancel.getAccount();
		request_open_quantity = cancel.getCancelQuantity();
		symbol =  cancel.getProductGroup();
		buy_or_sell =  cancel.getBuyOrSell();
		orderQty = cancel.getCancelQuantity();
		ordType = cancel.getOrderType();
		securityDesc = cancel.getSymbol();
		time_in_force = cancel.getTimeInForce();
		stop_price = cancel.getStopPrice();
		security_type = cancel.getSecurityType();
		corr_cl_order_id = cancel.getCorrClOrderId();
		
}
	
	public String getBuyOrSell()
	{
		return buy_or_sell;
	}
	public FixMessage getFixMsg()
	{

		FixMessage msg = null;
			
		if( type.equals(IBusinessObject.ACCEPT) || type.equals(IBusinessObject.CONFIRM))
		{
			msg = new FixAccept();
			
			((FixAccept)msg).setSymbol(symbol);
			((FixAccept)msg).setSide(translateToFixBuyOrSell(buy_or_sell));
			((FixAccept)msg).setText(reason);
			
			((FixAccept)msg).setLeavesQty(request_open_quantity.toString());
			((FixAccept)msg).setAccount(account);			
			((FixAccept)msg).setAvgPx("0.0");
			((FixAccept)msg).setClOrderID(request_id);
			((FixAccept)msg).setCumQty("0");
			((FixAccept)msg).setExecID(response_id);
			((FixAccept)msg).setExecType(execType);
			((FixAccept)msg).setExecTransType(execTransType);
			((FixAccept)msg).setOrdStatus(orderStatus);
			((FixAccept)msg).setLastPx("0");
			((FixAccept)msg).setLastShares("0");
			((FixAccept)msg).setOrderID(order_id);
			((FixAccept)msg).setOrderQty(String.valueOf(orderQty));
			if(ordType != null )
				((FixAccept)msg).setOrdType(translateToFixOrderType(ordType));
			((FixAccept)msg).setOrigClOrderID("0");
			//((FixAccept)msg).setPrice(); ???
			((FixAccept)msg).setSecurityDesc(securityDesc);
			((FixAccept)msg).setTransactTime(FixMessage.getUTCCurrentTime());
			if(time_in_force != null)			
				((FixAccept)msg).setTimeInForce(translateToFixTimeInForce(time_in_force));
			((FixAccept)msg).setStopPx(stop_price);
			((FixAccept)msg).setSecurityType(security_type);
			((FixAccept)msg).setCorrelationClOrdID(corr_cl_order_id);
			
			
						
			
		}
		else
		{
			msg = new FixReject();
		
			//((FixReject)msg).setOrdRejReason(reason);
			((FixReject)msg).setText(reason);
			((FixReject)msg).setSymbol(symbol);
			((FixReject)msg).setSide(translateToFixBuyOrSell(buy_or_sell));
		
			((FixReject)msg).setLeavesQty(request_open_quantity.toString());
			((FixReject)msg).setAccount(account);
			((FixReject)msg).setAvgPx("0.0");			
			((FixReject)msg).setClOrderID(request_id);
			((FixReject)msg).setCumQty("0");
			((FixReject)msg).setExecID(response_id);
			((FixReject)msg).setExecType(execType);
			((FixReject)msg).setExecTransType(execTransType);
			((FixReject)msg).setOrdStatus(orderStatus);
			((FixReject)msg).setLastPx("0");
			((FixReject)msg).setLastShares("0");
			((FixReject)msg).setOrderID(order_id);
			((FixReject)msg).setOrderQty(String.valueOf(orderQty));
			if(ordType != null )
				((FixReject)msg).setOrdType(translateToFixOrderType(ordType));
			((FixReject)msg).setOrigClOrderID("0");
			//((FixAccept)msg).setPrice(); ???			
			((FixReject)msg).setSecurityDesc(securityDesc);
			((FixReject)msg).setTransactTime(FixMessage.getUTCCurrentTime());
			if(time_in_force != null)							
				((FixReject)msg).setTimeInForce(translateToFixTimeInForce(time_in_force));
			((FixReject)msg).setStopPx(stop_price);
			((FixReject)msg).setSecurityType(security_type);
			((FixReject)msg).setCorrelationClOrdID(corr_cl_order_id);
			
			
			
		}

		
		return msg;
	}
	public String getReason()
	{
		return reason;
	}
	public String getRequestId()
	{
		return request_id;
	}
	public Integer getRequestOpenQuantity()
	{
		return request_open_quantity;
	}
	public String getResponseId()
	{
		return response_id;
	}
	public String getSubject()
	{
		return subject;
	}
	public String getSymbol()
	{
		return symbol;
	}
	public String getAccount()
	{
		return account;
	}
	public String getExecTransType()
	{
		return execTransType;
	}
	public String getExecType()
	{
		return execType;
	}
	public String getOrderStatus()
	{
		return orderStatus;
	}
	public String getOrderId()
	{
		return order_id;
	}	
	
	public String getType()
	{
		return type;
	}
	public void print()
	{
		System.out.println("Message Type :" + type );
		System.out.println("Response Id :" + response_id );
		System.out.println("Request Id :" + request_id );
		System.out.println("Request Open Quantity :" + request_open_quantity );
		System.out.println("Reason :" + reason );
		System.out.println("Buy Or Sell :" + buy_or_sell );
		System.out.println("Symbol :" + symbol );

	}
	public void setBuyOrSell( String val)
	{
		buy_or_sell = val;
	}
	public void setReason( String val )
	{
		reason = val;
	}
	public void setRequestId( String val)
	{
		request_id = val;
	}
	public void setRequestOpenQuantity( Integer val)
	{
		request_open_quantity = val;
	}
	public void setResponseId( String val)
	{
		response_id = val;
	}
	public void setSubject( String val )
	{
		subject = val;
	}
	public void setSymbol( String val )
	{
		symbol = val;
	}
	public void setAccount( String val )
	{
		account = val;
	}
	public void setExecTransType( String val )
	{
		execTransType = val;
	}
	public void setExecType( String val )
	{
		execType = val;
	}
	public void setOrderStatus( String val )
	{
		orderStatus = val;
	}			
	public void setOrderId( String val )
	{
		order_id = val;
	}	
	public String toString()
	{
		String str = new String("ORD: "+getRequestId()+" "+ type);
		return str;
	}
}
