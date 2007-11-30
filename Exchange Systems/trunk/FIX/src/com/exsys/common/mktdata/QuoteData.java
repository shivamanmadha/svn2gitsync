package com.exsys.common.mktdata;

/**
 * @author kreddy
 *
 * This class is used to hold a summary view of the market
 * for a symbol
 */
public class QuoteData{
	public String highPrice = "0";
	public String closePrice = "0";
	public String lowPrice = "0";
	public String openPrice = "0";
	public double volumeSize = 0;
	public double openInterest = 0;
	public String vwapPrice = "0";
	public String settlementPrice = "0";


	/**
	 * QuoteData constructor
	 */
	public QuoteData()
	{		
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "QuoteData - ";
		str += " HIGH - " +   highPrice;
		str += " LOW - " + lowPrice;
		str += " OPEN - " + openPrice;
		str += " CLOSE - " + closePrice;
		str += " Volume - "+ volumeSize;
		str += " OpenInterest - "+ openInterest;
		str += " VWAP - " + vwapPrice;	
		str += " Settlement Price - " + settlementPrice;	
		
		return str;
	}
	/**
	 * method to return info as a string
	 * @return
	 */
	public String toBookString()
	{
		String str = "Trading Session Summary - ";
		str += "\n HIGH - " +   highPrice;
		str += "\n LOW - " + lowPrice;
		str += "\n OPEN - " + openPrice;
		str += "\n CLOSE - " + closePrice;
		str += "\n Volume - "+ volumeSize;
		str += "\n OpenInterest - "+ openInterest;
		str += "\n VWAP - " + vwapPrice;	
		str += "\n Settlement Price - " + settlementPrice;	
		
		return str;
	}	
}
