package com.exsys.common.mktdata;
import com.exsys.common.util.*;
import java.util.*;

import com.exsys.service.Logger;

/**
 * @author kreddy
 *
 * This class is used to represent a row in book window
 * with both bid and offer info for a given price
 */
public class BookRow implements Comparable{
	public String displayPrice = "0";
	public double price = 0;
	public double bidSize = 0;
	public double offerSize = 0;
	public double accumBidSize = 0;
	public double accumOfferSize = 0;



	/**
	 * BookRow contstructor
	 */
	public BookRow()
	{
	}
	/**
	 * BookRow contstructor
	 * @param bs - bid size
	 * @param p - price
	 * @param dp - display price
	 * @param as - ask size
	 */
	public BookRow(double bs,double p,String dp, double as)
	{
		price = p;
		displayPrice = dp;
		bidSize = bs;
		offerSize = as;
		//accumBidSize = bs;
		//accumOfferSize = as;

	}
	/**
	 * BookRow contstructor
	 * @param br - new book row
	 */	
	public BookRow(BookRow br)
	{
		price = br.price;
		displayPrice = br.displayPrice;
		bidSize = br.bidSize;
		offerSize = br.offerSize;
		//accumBidSize = bs;
		//accumOfferSize = as;

	}	
	/**
	 * BookRow contstructor
	 * @param bs - bid size
	 * @param p - price
	 * @param dp - display price
	 * @param as - ask size
	 */
	public void setData(double bs,double p,String dp, double as)
	{
		price = p;
		displayPrice = dp;
		bidSize = bs;
		offerSize = as;
		//accumBidSize = bs;
		//accumOfferSize = as;

	}	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o)
	{
		return ((((BookRow)o).price < price)?1:-1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "BookRow - ";
		str += " ACCUM BID - " +  accumBidSize;
		str += " BID - " +  bidSize ;
		str += " PRICE - "+displayPrice;
		str += " OFFER - " + offerSize;
		str += " ACCUM OFFER - " +  accumOfferSize ;
		return str;
	}
	/**
	 * method to return book row info as string 
	 * @return
	 */
	public String toBookString()
	{
		String str = StringUtilities.prePad(String.valueOf(accumBidSize),' ',25);

		str += StringUtilities.prePad(String.valueOf(bidSize),' ',25);
		
		str += StringUtilities.prePad(displayPrice,' ',20);
		
		str += StringUtilities.prePad(String.valueOf(offerSize),' ',25);
		
		str += StringUtilities.prePad(String.valueOf(accumOfferSize),' ',25);
		return str;
	}

	/**
	 * @return
	 */
	public static String getHeader()
	{
		String str = StringUtilities.prePad("ACCUM BID",' ',20);		
		str += StringUtilities.prePad("BID",' ',20);
		str += StringUtilities.prePad("PRICE",' ',20);
		str += StringUtilities.prePad("OFFER",' ',20);
		str += StringUtilities.prePad("ACCUM OFFER",' ',20);
		return str;
	}	
}
