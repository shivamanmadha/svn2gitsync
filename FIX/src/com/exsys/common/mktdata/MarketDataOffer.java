package com.exsys.common.mktdata;
import java.util.*;

/**
 * @author kreddy
 *
 * This class is used to represent a market data offer
 */

public class MarketDataOffer implements Comparable{
	public String displayPrice = "0";
	public double price = 0;
	public double size = 0;
	public String orderID = null;
	public int sequenceID = 0;


	/**
	 * MarketDataOffer constructor
	 */
	public MarketDataOffer()
	{
	}
	
	/**
	 * MarketDataOffer constructor
	 * @param oid - order id
	 * @param sid - sequence id
	 * @param bs - bid size
	 * @param dp - display price
	 * @param p - bid price
	 */
	public MarketDataOffer(String oid,int sid,double bs,String dp, double p)
	{
		displayPrice = dp;
		price = p;
		size = bs;
		orderID = oid;
		sequenceID = sid;

	}
	/**
	 * method to set different attributes
	 * @param oid - order id
	 * @param sid - sequence id
	 * @param bs - bid size
	 * @param dp - display price
	 * @param p - bid price
	 */
	public void setData(String oid,int sid,double bs,String dp, double p)
	{
		displayPrice = dp;
		price = p;
		size = bs;
		orderID = oid;
		sequenceID = sid;

	}	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o)
	{
		return ((((MarketDataOffer)o).price > price)?1:-1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "MarketDataOffer - ";
		str += " OFFER - " +  size ;
		str += " PRICE - "+displayPrice;
		return str;
	}
	/**
	 * method to return string representation of offer
	 * @return
	 */
	public String toBookString()
	{
		String str = "Offer - OrderID = ";
		str += orderID;
		str += " ";
		str += size ;
		str += " @ "+displayPrice;
		return str;
	}	
}
