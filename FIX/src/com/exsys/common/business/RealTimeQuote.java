package com.exsys.common.business;

import com.exsys.fix.message.*;
import java.io.*;
/**
 *
 * <B>RealTimeQuote</B> class defines a quote produced by the exchange 
 * This contains best bid, best offer, and last sale. Currently this class only
 * contains minimum number of attributes. This will be enhanced shortly
 * This class provides a constructor, which creates an empty <B>RealTimeQuote</B>
 * based on the parameter, and use different <B>set</B> methods to populate the object. This constructor
 * is used by an application producing a real time quote message, for example, order matching exchange 
 * application. Another constructor
 * constructs RealTimeQuote object by taking RvMsg & subject name as parameter. This
 * constructor will be used by applications which recieve an TIBCO message
 * of type <B>QUOTE</B> and constructs RealTimeQuote object based on this message
 * and use different <B>get</B> methods to get details about this object
 * <B>TradeResponseManager</B> class uses this constructor to construct business 
 * objects and invokes proper callback method of class <B>ITradeResponseHandler</B>
 * Applications need not have to use this constructor. Instead applications implement
 * <B>ITradeResponseHandler</B> interface and use TradeResponseManager to get application
 * level callbacks.
 */

public class RealTimeQuote extends IBusinessObject
{
	private Integer bid_quantity;
	private String  bid_price;
	private Integer offer_quantity;
	private String  offer_price;
	private Integer last_quantity;
	private String  last_price;
	private String  symbol;

	public RealTimeQuote( String type )
	{
		this.type = new String(type);
		bid_quantity = new Integer(0);
		bid_price = new String();
		offer_quantity = new Integer(0);
		offer_price = new String();
		last_quantity = new Integer(0);
		last_price = new String();
		symbol = new String();
	
		subject = new String();
		
	}
	public String getBidPrice()
	{
		return bid_price;
	}
	public Integer getBidQuantity()
	{
		return bid_quantity;
	}
	public FixMessage getFixMsg()
	{

		return null;		
	}
	public String getLastPrice()
	{
		return last_price;
	}
	public Integer getLastQuantity()
	{
		return last_quantity;
	}
	public String getOfferPrice()
	{
		return offer_price;
	}
	public Integer getOfferQuantity()
	{
		return offer_quantity;
	}
	public String getSubject()
	{
		return subject;
	}
	public String getSymbol()
	{
		return symbol;
	}
	public String getType()
	{
		return type;
	}
	public void print()
	{
		System.out.println("Message Type :" + type + " for " + symbol);
		System.out.println("Bid :" + bid_quantity + "@" + bid_price );
		System.out.println("Offer :" + offer_quantity + "@" + offer_price );
		System.out.println("Last :" + last_quantity + "@" + last_price );
	}
	public void setBidPrice( String val)
	{
		bid_price = val;
	}
	public void setBidQuantity( Integer val)
	{
		bid_quantity = val;
	}
	public void setLastPrice( String val)
	{
		last_price = val;
	}
	public void setLastQuantity( Integer val)
	{
		last_quantity = val;
	}
	public void setOfferPrice( String val)
	{
		offer_price = val;
	}
	public void setOfferQuantity( Integer val)
	{
		offer_quantity = val;
	}
	public void setSubject( String val )
	{
		subject = val;
	}
	public void setSymbol( String val )
	{
		symbol = val;
	}
	public String toString()
	{
		String quote = "QUOTE";
		return( quote );
	}
}
