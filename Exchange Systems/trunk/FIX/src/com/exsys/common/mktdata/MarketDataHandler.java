package com.exsys.common.mktdata;
import com.exsys.fix.message.*;

import java.util.*;

import com.exsys.service.Logger;

//import java.util.Iterator;
/**
 * @author kreddy
 *
 * This is the main class that handles market data bids and offers
 * This class contains a book handler, that maintains a book
 * for each symbol, as the market data bids and offers are processed
 */
public class MarketDataHandler {

	private HashMap mdBids = null;
	private HashMap mdOffers = null;
	private HashMap mdTrades = null;
	private HashMap mdLastTrade = null;
	private HashMap mdPrevTrade = null;
	private HashMap mdSummary = null;	
	private BookHandler bookHandler = null;		
	

	/**
	 * MarketDataHandler constructor
	 */
	public MarketDataHandler()
	{
	}
	
	/**
	 * method returns the book for the given symbol
	 * @param symbol
	 * @return - array list of book entries
	 */
	public ArrayList getBook(String symbol)
	{
		if(bookHandler != null)
		{
			return bookHandler.getBook(symbol);
		}
		
		return null;
	}
	/**
	 * method returns bids for the given symbol
	 * @param symbol
	 * @return hash map of bids
	 */
	public HashMap getBids(String symbol)
	{
		if(mdBids != null && mdBids.containsKey(symbol))
		{
			return (HashMap)mdBids.get(symbol);
		}
		
		return null;
	}	

	/**
	 * method returns offers for the given symbol
	 * @param symbol
	 * @return hash map of offers
	 */
	public HashMap getOffers(String symbol)
	{
		if(mdOffers != null && mdOffers.containsKey(symbol))
		{
			return (HashMap)mdOffers.get(symbol);
		}
		
		return null;
	}	
	/**
	 * method returns trades for the given symbol
	 * @param symbol
	 * @return hash map of trades
	 */
	public HashMap getTrades(String symbol)
	{
		if(mdTrades != null && mdTrades.containsKey(symbol))
		{
			return (HashMap)mdTrades.get(symbol);
		}
		
		return null;
	}	
	
	/**
	 * method returns summary info for the given symbol
	 * @param symbol
	 * @return summary in QuoteData structure 
	 */
	public QuoteData getSummary(String symbol)
	{
		if(mdSummary != null && mdSummary.containsKey(symbol))
		{
			return (QuoteData)mdSummary.get(symbol);
		}
		
		return null;
	}
		
	/**
	 * method returns last trade info for the given symbol
	 * @param symbol
	 * @return last trade info
	 */
	public MarketDataTrade getLastTrade(String symbol)
	{
		if(mdLastTrade != null && mdLastTrade.containsKey(symbol))
		{
			return (MarketDataTrade)mdLastTrade.get(symbol);
		}
		
		return null;
	}	
	
    /**
     * method to initialize market data data structures
     * @param symbol
     */
    private void initializeData( String symbol )
    {
		if(mdBids == null)
		{
			mdBids = new HashMap();
			
		}
		else
		{
			if(mdBids.containsKey(symbol))
			{
				((HashMap)mdBids.get(symbol)).clear();
			}
		}
		
		if(mdOffers == null)
		{
			mdOffers = new HashMap();
			
		}
		else
		{
			if(mdOffers.containsKey(symbol))
			{
				((HashMap)mdOffers.get(symbol)).clear();
			}
		}		
		
		if(mdTrades == null)
		{
			mdTrades = new HashMap();
			
		}
		else
		{
			if(mdTrades.containsKey(symbol))
			{
				((HashMap)mdTrades.get(symbol)).clear();
			}
		}	

		if(mdLastTrade == null)
		{
			mdLastTrade = new HashMap();
			mdPrevTrade = new HashMap();
			
		}
		else
		{
			if(mdLastTrade.containsKey(symbol))
			{
				mdLastTrade.remove(symbol);
			}
			if(mdPrevTrade.containsKey(symbol))
			{
				mdPrevTrade.remove(symbol);
			}			
		}   
		if(mdSummary == null)
		{
			mdSummary = new HashMap();
			
		}
		else
		{
			if(mdSummary.containsKey(symbol))
			{
				mdSummary.remove(symbol);
			}
		}
		
		if(bookHandler == null)
		{
			bookHandler = new BookHandler();
		}
		else
		{
			bookHandler.clearBook(symbol);
		}
		
				 	
    }
    /**
     * method to check if there is market data for the given symbol
     * @param symbol
     * @return true or false
     */
    public boolean containsSymbol(String symbol)
    {
      return (mdSummary != null && mdSummary.containsKey(symbol));
    }
    
    /**
     * method to handle market data full refresh bid
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dispPrice
     * @param price
     */
    private void handleFullRefreshBid(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dispPrice,
    				double price)
    {
    	if(mdBids.containsKey(symbol))
    	{
    		HashMap bl = (HashMap)mdBids.get(symbol);
    		bl.put(orderID,new MarketDataBid(orderID,sequenceID,size,dispPrice,price));
    	}
    	else
    	{
    		HashMap bidList = new HashMap();
    		bidList.put(orderID,new MarketDataBid(orderID,sequenceID,size,dispPrice,price));
    		mdBids.put(symbol,bidList);
    	}
    	
    	bookHandler.addBid(symbol,price,dispPrice,size);	
    }
    /**
     * method to handle market data full refresh offer
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dispPrice
     * @param price
     */
    private void handleFullRefreshOffer(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dispPrice,
    				double price)
    {
    	if(mdOffers.containsKey(symbol))
    	{
    		HashMap ol = (HashMap)mdOffers.get(symbol);
    		ol.put(orderID,new MarketDataOffer(orderID,sequenceID,size,dispPrice,price));
    	}
    	else
    	{
    		HashMap offerList = new HashMap();
    		offerList.put(orderID,new MarketDataOffer(orderID,sequenceID,size,dispPrice,price));
    		mdOffers.put(symbol,offerList);
    	}
    	
    	bookHandler.addOffer(symbol,price,dispPrice,size);	
    }
    
    /**
     * method to handle full refresh trade
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dispPrice
     * @param price
     */
    private void handleFullRefreshTrade(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dispPrice,
    				double price)
    {
    	if(mdTrades.containsKey(symbol))
    	{
    		HashMap tl = (HashMap)mdTrades.get(symbol);
    		tl.put(orderID,new MarketDataTrade(orderID,sequenceID,size,dispPrice,price));
    	}
    	else
    	{
    		HashMap tradeList = new HashMap();
    		tradeList.put(orderID,new MarketDataTrade(orderID,sequenceID,size,dispPrice,price));
    		mdTrades.put(symbol,tradeList);
    	}
    	// check last trade
    	if(mdLastTrade.containsKey(symbol))
    	{
    		((MarketDataTrade)mdLastTrade.get(symbol)).setData(orderID,sequenceID,size,dispPrice,price);
    	}
    	else
    	{
    		mdLastTrade.put(symbol,new MarketDataTrade(orderID,sequenceID,size,dispPrice,price));
    	}    	
    	
    	    	
    }   
    /**
     * method to handle full refresh opening price
     * @param symbol
     * @param price
     */
    private void handleFullRefreshOpeningPrice(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).openPrice = price;
    	}
    	else
    	{
    		QuoteData qd = new QuoteData();
    		qd.openPrice = price;
    		mdSummary.put(symbol,qd);
    	}    	    	    	    	
    }     
    /**
     * method to handle full refresh settlement price
     * @param symbol
     * @param price
     */
    private void handleFullRefreshSettlementPrice(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).settlementPrice = price;
    	}
    	else
    	{
    		QuoteData qd = new QuoteData();
    		qd.settlementPrice = price;
    		mdSummary.put(symbol,qd);
    	}    	    	    	    	
    } 
    /**
     * method to handle full refresh high price
     * @param symbol
     * @param price
     */
    private void handleFullRefreshHighPrice(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).highPrice = price;
    	}
    	else
    	{
    		QuoteData qd = new QuoteData();
    		qd.highPrice = price;
    		mdSummary.put(symbol,qd);
    	}    	    	    	    	
    }    
    /**
     * method to handle full refresh low price
     * @param symbol
     * @param price
     */
    private void handleFullRefreshLowPrice(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).lowPrice = price;
    	}
    	else
    	{
    		QuoteData qd = new QuoteData();
    		qd.lowPrice = price;
    		mdSummary.put(symbol,qd);
    	}    	    	    	    	
    }     
    /**
     * method to handle full refresh VWAP price
     * @param symbol
     * @param price
     */
    private void handleFullRefreshVWAPPrice(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).vwapPrice = price;
    	}
    	else
    	{
    		QuoteData qd = new QuoteData();
    		qd.vwapPrice = price;
    		mdSummary.put(symbol,qd);
    	}    	    	    	    	
    }    
    /**
     * method to handle full refresh open interest
     * @param symbol
     * @param size
     */
    private void handleFullRefreshOpenInterest(String symbol,	    				
		    				double size)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).openInterest = size;
    	}
    	else
    	{
    		QuoteData qd = new QuoteData();
    		qd.openInterest = size;
    		mdSummary.put(symbol,qd);
    	}    	    	    	    	
    }      
    
    
	/**
	 * main method to process a market data full refresh message
	 * @param symbol
	 * @param fr
	 */
	public void processFullRefresh(String symbol, FixMarketDataFullRefresh fr)
	{	
		// need to clear existing quotes
		initializeData(symbol);
		
   	 	// add details
   	 	String key = "268";
  		ArrayList rgArray = fr.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int asize = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < asize; i++) {

  				FixRGNoMDEntries_W rg = (FixRGNoMDEntries_W)rgArray.get(i);
  				String mdEType = rg.getMDEntryType();
  				String ordState = rg.getOrderState();
  				if((mdEType.equals("0")||mdEType.equals("1")) && 
  					!"0".equals(ordState))
  				{
  					// ignore
  					continue;
  				}
  				
  				String orderID = rg.getOrderID();
  				int sequenceID = rg.getSequenceId();
  				double price = rg.getMDEntryPx();
  				String dp = rg.getMDEntryPxAsString();
  				double size = rg.getMDEntrySize();
  				
  				if(mdEType.equals("0"))
  				{
  					handleFullRefreshBid(symbol,orderID,sequenceID,size,dp,price);
  				}
  				else if(mdEType.equals("1"))
  				{
  					handleFullRefreshOffer(symbol,orderID,sequenceID,size,dp,price);
  				}
  				else if(mdEType.equals("2"))
  				{
  					handleFullRefreshTrade(symbol,orderID,sequenceID,size,dp,price);
  				}
  				else if(mdEType.equals("4"))
  				{  					
  					handleFullRefreshOpeningPrice(symbol,dp);
  				}
  				else if(mdEType.equals("6"))
  				{
  					handleFullRefreshSettlementPrice(symbol,dp);
  				}
  				else if(mdEType.equals("7"))
  				{
  					handleFullRefreshHighPrice(symbol,dp);
  				}
  				else if(mdEType.equals("8"))
  				{
  					handleFullRefreshLowPrice(symbol,dp);
  				}
  				else if(mdEType.equals("9"))
  				{
  					handleFullRefreshVWAPPrice(symbol,dp);
  				}
  				else if(mdEType.equals("C"))
  				{
  					handleFullRefreshOpenInterest(symbol,size);
  				}    				  				  				  				  				  				  				
  				else
  				{
  					Logger.debug("UNKNOWN MDETYPE = " + mdEType);
  				}
  				
  				  			  				  				
   			}
   			
  		}//rgArray != null	
  		//bookHandler.displayBook(symbol);  			
		
	}
	/**
	 * main method to process market data incremental refresh message
	 * @param ir
	 * @param restoreSymbol
	 */
	public void processIncrementalRefresh(FixMarketDataIncrementalRefresh ir, String restoreSymbol)
	{	
		// need to clear existing quotes
		//initializeData(symbol);
		if(mdBids == null || mdOffers == null || mdTrades == null)
		{
			Logger.debug("Full refresh was not received");
			return;
		}
		
   	 	// add details
   	 	String key = "268";
  		ArrayList rgArray = ir.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int asize = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < asize; i++) {

  				FixRGNoMDEntries_X rg = (FixRGNoMDEntries_X)rgArray.get(i);
  				String symbol = rg.getSymbol();
  				if(restoreSymbol != null && !rg.getSymbol().equals(restoreSymbol)) continue;
  				String mdEType = rg.getMDEntryType();
  				String ordState = rg.getOrderState();
  				String mdUpdateAction = rg.getMDUpdateAction();
  				
  				 String orderID = rg.getOrderID();
  				int sequenceID = rg.getSequenceId();
  				double price = rg.getMDEntryPx();
  				String dp = rg.getMDEntryPxAsString();
  				double size = rg.getMDEntrySize();
  				Logger.debug("i = " + i);
  				Logger.debug("MDUpdateAction = " + mdUpdateAction);
				Logger.debug("ordState = " + ordState);
				Logger.debug("mdEType = " + mdEType);
				Logger.debug("Symbol = " + symbol + " orderid = " + orderID + " " + size + "@"+dp);
				// ignore block trades
				if(rg.getTradeType() != null)
				{
					Logger.debug("Block Trade - " + rg.getTradeType());
					continue;
				}
  				
  				/*
  				if(mdUpdateAction.equals("0") && !"0".equals(ordState))
  				{
  					// remove this entry from the book
			    	// we should also remove the appropriate order - either bid or offer
   				 	if(!handleIncrRefreshBidDelete(symbol,orderID,sequenceID,size,price))
			    		handleIncrRefreshOfferDelete(symbol,orderID,sequenceID,size,price);
  					continue;			
  				}
  				*/
  				
  				
  				if(mdUpdateAction.equals("0"))//new
  				{
  					if(mdEType.equals("0"))
  					{
  						handleIncrRefreshBidNew(symbol,orderID,sequenceID,size,dp,price);
  					}
  					else if(mdEType.equals("1"))
  					{
  						handleIncrRefreshOfferNew(symbol,orderID,sequenceID,size,dp,price);
  					}
  					else if(mdEType.equals("2"))
  					{
  						handleIncrRefreshTradeNew(symbol,orderID,sequenceID,size,dp,price);
  						double totalVolumeTraded = rg.getTotalVolumeTraded();
  						handleIncrRefreshVolumeNew(symbol,totalVolumeTraded);
  					}
  					else if(mdEType.equals("4"))
  					{  					
  						handleIncrRefreshOpeningPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("6"))
  					{
  						handleIncrRefreshSettlementPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("7"))
  					{
  						handleIncrRefreshHighPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("8"))
  					{
  						handleIncrRefreshLowPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("9"))
  					{
  						handleIncrRefreshVWAPPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("C"))
  					{
  						handleIncrRefreshOpenInterestNew(symbol,size);
  					} 
  					else if(mdEType.equals("U"))
  					{
  						//handleIncrRefreshOpenInterestNew(symbol,size);
  						Logger.debug("UNKNOWN MDETYPE = " + mdEType);
  					}   				  				  				  				  				  				  				
  					else
  					{	
  						Logger.debug("UNKNOWN MDETYPE = " + mdEType);
  					}
  					
  				}
  				else if(mdUpdateAction.equals("1"))//change
  				{
  					if(mdEType.equals("0"))
  					{
  						handleIncrRefreshBidChange(symbol,orderID,sequenceID,size,dp,price);
  					}
  					else if(mdEType.equals("1"))
  					{
  						handleIncrRefreshOfferChange(symbol,orderID,sequenceID,size,dp,price);
  					}
  					else if(mdEType.equals("2"))
  					{
  						handleIncrRefreshTradeChange(symbol,orderID,sequenceID,size,dp,price);
  						double totalVolumeTraded = rg.getTotalVolumeTraded();
  						handleIncrRefreshVolumeNew(symbol,totalVolumeTraded);
  						
  					}
  					else if(mdEType.equals("4"))
  					{  					
  						handleIncrRefreshOpeningPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("6"))
  					{
  						handleIncrRefreshSettlementPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("7"))
  					{
  						handleIncrRefreshHighPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("8"))
  					{
  						handleIncrRefreshLowPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("9"))
  					{
  						handleIncrRefreshVWAPPriceNew(symbol,dp);
  					}
  					else if(mdEType.equals("C"))
  					{
  						handleIncrRefreshOpenInterestNew(symbol,size);
  					} 
  					else if(mdEType.equals("U"))
  					{
  						//handleIncrRefreshOpenInterestNew(symbol,size);
  						Logger.debug("UNKNOWN MDETYPE = " + mdEType);
  					}   				  				  				  				  				  				  				
  					else
  					{	
  						Logger.debug("UNKNOWN MDETYPE = " + mdEType);
  					}  					
  				}
  				else if(mdUpdateAction.equals("2"))//delete
  				{
  					if(mdEType.equals("0"))
  					{
  						handleIncrRefreshBidDelete(symbol,orderID,sequenceID,size,dp,price);
  					}
  					else if(mdEType.equals("1"))
  					{
  						handleIncrRefreshOfferDelete(symbol,orderID,sequenceID,size,dp,price);
  					}
  					else if(mdEType.equals("2"))
  					{
  						handleIncrRefreshTradeDelete(symbol,orderID,sequenceID,size,dp,price,rg.getDeleteReason());
  						double totalVolumeTraded = rg.getTotalVolumeTraded();
  						handleIncrRefreshVolumeNew(symbol,totalVolumeTraded);

  					}
  					else if(mdEType.equals("4"))
  					{  					
  						handleIncrRefreshOpeningPriceDelete(symbol,dp);
  					}
  					else if(mdEType.equals("6"))
  					{
  						handleIncrRefreshSettlementPriceDelete(symbol,dp);
  					}
  					else if(mdEType.equals("7"))
  					{
  						handleIncrRefreshHighPriceDelete(symbol,dp);
  					}
  					else if(mdEType.equals("8"))
  					{
  						handleIncrRefreshLowPriceDelete(symbol,dp);
  					}
  					else if(mdEType.equals("9"))
  					{
  						handleIncrRefreshVWAPPriceDelete(symbol,dp);
  					}
  					else if(mdEType.equals("C"))
  					{
  						handleIncrRefreshOpenInterestDelete(symbol,size);
  					} 
  					else if(mdEType.equals("U"))
  					{
  						//handleIncrRefreshOpenInterestNew(symbol,size);
  						Logger.debug("UNKNOWN MDETYPE = " + mdEType);
				    	// we should also remove the appropriate order - either bid or offer
    					if(!handleIncrRefreshBidDelete(symbol,orderID,sequenceID,size,dp,price))
				    		handleIncrRefreshOfferDelete(symbol,orderID,sequenceID,size,dp,price);  						
  					}   				  				  				  				  				  				  				
  					else
  					{	
  						Logger.debug("UNKNOWN MDETYPE = " + mdEType);
  					}   					
  				}
  				else
  				{
  					Logger.debug("UNKNOWN MDUPDATE ACTION= " + mdUpdateAction);
  				}
  				
  				
				//bookHandler.displayAllBooks();  			
  				
  				  			  				  				
   			}
   			
  		}//rgArray != null	
  		//bookHandler.displayAllBooks();  			
		
	}
	
	
    /**
     * method to handle incr refresh bid new
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     */
    private void handleIncrRefreshBidNew(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	boolean added = true;
    	if(mdBids.containsKey(symbol))
    	{
    		HashMap bl = (HashMap)mdBids.get(symbol);
    		// add only if sequence id is greater
    		if(bl.containsKey(orderID))
    		{ 
	    		if(((MarketDataBid)bl.get(orderID)).sequenceID < sequenceID)
 		   		{
   		 			bl.put(orderID,new MarketDataBid(orderID,sequenceID,size,dp,price));
    			}
    			else
    			{
    				added = false;
    			}
    		}
    		else
    		{
    			bl.put(orderID,new MarketDataBid(orderID,sequenceID,size,dp,price));
    		}
    		
    		
    	}
    	else
    	{
    		HashMap bidList = new HashMap();
    		bidList.put(orderID,new MarketDataBid(orderID,sequenceID,size,dp,price));
    		mdBids.put(symbol,bidList);
    		//Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		//return;
    	}
    	if(added)
    	{
    		Logger.debug("BID Order Added - "+orderID + " >"+size+"@"+dp+"<");
    		bookHandler.addBid(symbol,price,dp,size);	
    	}
    }
    
    /**
     * method to handle incr refresh offer new
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     */
    private void handleIncrRefreshOfferNew(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	boolean added = true;
    	if(mdOffers.containsKey(symbol))
    	{
    		HashMap ol = (HashMap)mdOffers.get(symbol);
    		// add only if sequence id is greater
    		if(ol.containsKey(orderID))
    		{
    			if(((MarketDataOffer)ol.get(orderID)).sequenceID < sequenceID)
    			{
    				ol.put(orderID,new MarketDataOffer(orderID,sequenceID,size,dp,price));	
    			}
    			else
    			{
    				added = false;
    			}
    		}
    		else
    		{
    			ol.put(orderID,new MarketDataOffer(orderID,sequenceID,size,dp,price));	
    		}
    		
    	}
    	else
    	{
   			HashMap offerList = new HashMap();
    		offerList.put(orderID,new MarketDataOffer(orderID,sequenceID,size,dp,price));
    		mdOffers.put(symbol,offerList);
    		
    		//Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		//return;
    	}
    	
    	if(added)
    	bookHandler.addOffer(symbol,price,dp,size);	
    }
    
    /**
     * method to handle incr refresh trade new
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     */
    private void handleIncrRefreshTradeNew(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	Logger.debug("New Trade Received - orderID ="+orderID + " sequenceID = " + sequenceID);
    	Logger.debug(size+"@"+price);
    	
    	if(mdTrades.containsKey(symbol))
    	{
    		Logger.debug("symbol not in mdTrades");
    		HashMap tl = (HashMap)mdTrades.get(symbol);
    		if(tl.containsKey(orderID))
    		{ 
    			if(((MarketDataTrade)tl.get(orderID)).sequenceID < sequenceID)
    			{
    				tl.put(orderID,new MarketDataTrade(orderID,sequenceID,size,dp,price));
    			}
    		}
    		else
    		{
    			tl.put(orderID,new MarketDataTrade(orderID,sequenceID,size,dp,price));
    		}    		
    	}
    	else
    	{
    		Logger.debug("Adding symbol in mdTrades");
    		
			HashMap tradeList = new HashMap();
    		tradeList.put(orderID,new MarketDataTrade(orderID,sequenceID,size,dp,price));
    		mdTrades.put(symbol,tradeList);    		
    		//Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		//return;
    	}
    	// check last trade
    	if(mdLastTrade.containsKey(symbol))
    	{
    		// save current values in prev trade
    		MarketDataTrade lTrade = (MarketDataTrade)mdLastTrade.get(symbol);
    		if(mdPrevTrade.containsKey(symbol))
    		{
    			
    			((MarketDataTrade)mdPrevTrade.get(symbol)).setData(lTrade.orderID,lTrade.sequenceID,lTrade.size,lTrade.displayPrice,lTrade.price);
    		}
    		else
    		{
    			mdPrevTrade.put(symbol,new MarketDataTrade(lTrade.orderID,lTrade.sequenceID,lTrade.size,lTrade.displayPrice,lTrade.price));
    		}
    		((MarketDataTrade)mdLastTrade.get(symbol)).setData(orderID,sequenceID,size,dp,price);
    	}
    	else
    	{
    		mdLastTrade.put(symbol,new MarketDataTrade(orderID,sequenceID,size,dp,price));
    	}
    	
    	// we should also remove the appropriate order - either bid or offer
    	if(!handleIncrRefreshBidDelete(symbol,orderID,sequenceID,size,dp,price))
    		handleIncrRefreshOfferDelete(symbol,orderID,sequenceID,size,dp,price);
    	
    	    	
    }   
    /**
     * method to handle incre refresh opening price new
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshOpeningPriceNew(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).openPrice = price;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }     
    /**
     * method to handle incr refresh settlement price new
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshSettlementPriceNew(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).settlementPrice = price;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    } 
    /**
     * method to handle incr refresh high price new
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshHighPriceNew(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).highPrice = price;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }  
    /**
     * method to handle incr refresh volume new
     * @param symbol
     * @param size
     */
    private void handleIncrRefreshVolumeNew(String symbol,	    				
		    				double size)
	{
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).volumeSize = size;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
		
	}		    				  
    /**
     * method to handle incr refresh low price new
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshLowPriceNew(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).lowPrice = price;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }     
    /**
     * method to handle incr refresh vwap price new
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshVWAPPriceNew(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).vwapPrice = price;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }    
    /**
     * method to handle incr refresh open interest new
     * @param symbol
     * @param size
     */
    private void handleIncrRefreshOpenInterestNew(String symbol,	    				
		    				double size)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).openInterest = size;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    } 	
	

    /**
     * method to handle incr refresh bid change
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     */
    private void handleIncrRefreshBidChange(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	if(mdBids.containsKey(symbol))
    	{
    		HashMap bl = (HashMap)mdBids.get(symbol);
    		if(bl.containsKey(orderID))
    		{
    			
    			MarketDataBid oldBid = (MarketDataBid)bl.get(orderID);
    			if(oldBid.price != price)
    			{
    				bookHandler.removeBid(symbol,oldBid.price, oldBid.displayPrice, oldBid.size);
    				bookHandler.addBid(symbol,price, dp, size);
    			}
    			else
    			{
    				bookHandler.changeBid(symbol,price,dp,size);	
    			}
    			((MarketDataBid)bl.get(orderID)).setData(orderID,sequenceID,size,dp,price);    		
    		}
    		else
    		{
    			bl.put(orderID,new MarketDataBid(orderID,sequenceID,size,dp,price));
    			bookHandler.addBid(symbol,price, dp, size);
    		}
    		
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}
    	
    	//bookHandler.changeBid(symbol,price,dp,size);	
    }
    /**
     * method to handle incr refresh offer change
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     */
    private void handleIncrRefreshOfferChange(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	if(mdOffers.containsKey(symbol))
    	{
    		HashMap ol = (HashMap)mdOffers.get(symbol);
    		if(ol.containsKey(orderID))
    		{
    			MarketDataOffer oldOffer = (MarketDataOffer)ol.get(orderID);
    			if(oldOffer.price != price)
    			{
    				bookHandler.removeOffer(symbol,oldOffer.price, oldOffer.displayPrice, oldOffer.size);
    				bookHandler.addOffer(symbol,price, dp, size);
    			}
    			else
    			{
    				bookHandler.changeOffer(symbol,price,dp,size);	
    			}    			
    			((MarketDataOffer)ol.get(orderID)).setData(orderID,sequenceID,size,dp,price);    		
    		}
    		else
    		{
    			ol.put(orderID,new MarketDataOffer(orderID,sequenceID,size,dp,price));
    			bookHandler.addOffer(symbol,price,dp,size);	
    		}    		
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}
    	
    	//bookHandler.changeOffer(symbol,price,dp,size);	
    }
    
    /**
     * method to handle incr refresh trade change
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     */
    private void handleIncrRefreshTradeChange(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	if(mdTrades.containsKey(symbol))
    	{
    		HashMap tl = (HashMap)mdTrades.get(symbol);
    		if(tl.containsKey(orderID))
    		{
    			((MarketDataTrade)tl.get(orderID)).setData(orderID,sequenceID,size,dp,price);    		
    		}
    		else
    		{
    			tl.put(orderID,new MarketDataOffer(orderID,sequenceID,size,dp,price));
    		}    		
    		
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}
    	// check last trade
    	if(mdLastTrade.containsKey(symbol))
    	{
    		// save current values in prev trade
    		MarketDataTrade lTrade = (MarketDataTrade)mdLastTrade.get(symbol);
    		if(mdPrevTrade.containsKey(symbol))
    		{
    			
    			((MarketDataTrade)mdPrevTrade.get(symbol)).setData(lTrade.orderID,lTrade.sequenceID,lTrade.size,lTrade.displayPrice,lTrade.price);
    		}
    		else
    		{
    			mdPrevTrade.put(symbol,new MarketDataTrade(lTrade.orderID,lTrade.sequenceID,lTrade.size,lTrade.displayPrice,lTrade.price));
    		}
    		((MarketDataTrade)mdLastTrade.get(symbol)).setData(orderID,sequenceID,size,dp,price);

    	}
    	
    	
    	    	
    }   

    /**
     * method to handle incr refresh bid delete
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     * @return
     */
    private boolean handleIncrRefreshBidDelete(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	boolean removed = false;
    	double sizeRemoved = 0;
    	if(mdBids.containsKey(symbol))
    	{
    		HashMap bl = (HashMap)mdBids.get(symbol);
    		if(bl.containsKey(orderID))
    		{
    			MarketDataBid bid = (MarketDataBid)bl.remove(orderID);
    			sizeRemoved = bid.size;
    			Logger.debug("BID Order Removed - "+orderID + " >"+size+"@"+dp+"<");
    			removed = true;
    		}
    		else
    		{
    			Logger.debug("BID Order NOT Removed - "+orderID+ " >"+size+"@"+dp+"<");
    			//sizeRemoved = size;
    		}
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol+" - "+ orderID+ " >"+size+"@"+dp+"<");
    		return false;
    	}
    	
    	if(removed)
    		bookHandler.removeBid(symbol,price,dp,sizeRemoved);
    			
    	return removed;
    }
    /**
     * method to handle incr refresh offer delete
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     * @return
     */
    private boolean handleIncrRefreshOfferDelete(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price)
    {
    	boolean removed = false;
    	double sizeRemoved = 0;
    	if(mdOffers.containsKey(symbol))
    	{
    		HashMap ol = (HashMap)mdOffers.get(symbol);
    		if(ol.containsKey(orderID))
    		{
    			
    			MarketDataOffer offer = (MarketDataOffer)ol.remove(orderID);
    			sizeRemoved = offer.size;
    			Logger.debug("OFFER Order Removed - "+orderID +" >"+size+"@"+dp+"<");
    			removed = true;
    		}
    		else
    		{
    			  Logger.debug("OFFER Order NOT Removed - "+orderID+ " >"+size+"@"+dp+"<");
    		}
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol+" - " + orderID+ " >"+size+"@"+dp+"<");
    		return false;
    	}
    	if(removed)
    		bookHandler.removeOffer(symbol,price,dp,sizeRemoved);	
    	return removed;
    }
    
    /**
     * method to handle incr refresh trade delete
     * @param symbol
     * @param orderID
     * @param sequenceID
     * @param size
     * @param dp
     * @param price
     */
    private void handleIncrRefreshTradeDelete(String symbol,
    				String orderID,
    				int sequenceID,
    				double size,
    				String dp,
    				double price,
    				String deleteReason)
    {
    	if(mdTrades.containsKey(symbol))
    	{
    		HashMap tl = (HashMap)mdTrades.get(symbol);
    		if(tl.containsKey(orderID))
    		{
    			tl.remove(orderID);
    		}
    		// check last trade
    		if(mdLastTrade.containsKey(symbol))    	
    		{
    			// if the trade that is busted is not current last trade - no need to update last price
    			MarketDataTrade lTrade = (MarketDataTrade)mdLastTrade.get(symbol);
    			if( orderID.equals(lTrade.orderID))
    			{
    				// then we may have to get earlier last trade
    				// see if there are other trades
    				if(!tl.isEmpty())
    				{
    					MarketDataTrade prevTrade = (MarketDataTrade)mdPrevTrade.get(symbol);
    					((MarketDataTrade)mdLastTrade.get(symbol)).setData(prevTrade.orderID,
    															prevTrade.sequenceID,
    															prevTrade.size,
    															prevTrade.displayPrice,
    															prevTrade.price);
    				}
    				else
    				{
    					//((MarketDataTrade)mdLastTrade.get(symbol)).setData(orderID,sequenceID,0,"0",0);
    					// may be we should remove last trade completely
    					mdLastTrade.remove(symbol);
    				}
    			}
    			//if(deleteReason != null)
    			//{
    			//	((MarketDataTrade)mdLastTrade.get(symbol)).setData(orderID,sequenceID,0,"0",0);
    			//}
    			//((MarketDataTrade)mdLastTrade.get(symbol)).setData(orderID,sequenceID,0,"0",0);
    		}
    		
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}
    	// check last trade
    	/*
    	if(mdLastTrade.containsKey(symbol))    	
    	{
    		
    		((MarketDataTrade)mdLastTrade.get(symbol)).setData(orderID,sequenceID,0,"0",0);
    	}
    	*/
    	
    	
    	    	
    }   
    /**
     * method to handle incr refresh opening price delete
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshOpeningPriceDelete(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).openPrice = "0";
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }     
    /**
     * method to handle incr refresh settlement price delete
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshSettlementPriceDelete(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).settlementPrice = "0";
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    } 
    /**
     * method to handle incr refresh high price delete
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshHighPriceDelete(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).highPrice = "0";
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }    
    /**
     * method to handle incr refresh low price delete
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshLowPriceDelete(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).lowPrice = "0";
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }     
    /**
     * method to handle incr refresh vwap price delete
     * @param symbol
     * @param price
     */
    private void handleIncrRefreshVWAPPriceDelete(String symbol,	    				
		    				String price)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).vwapPrice = "0";
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }    
    /**
     * method to handle incr refresh open interest delete
     * @param symbol
     * @param size
     */
    private void handleIncrRefreshOpenInterestDelete(String symbol,	    				
		    				double size)
    {
    	if(mdSummary.containsKey(symbol))
    	{
    		((QuoteData)mdSummary.get(symbol)).openInterest = 0;
    	}
    	else
    	{
    		Logger.debug("ERROR - NO BOOK ENTRY FOR SYMBOL = " + symbol);
    		return;
    	}    	    	    	    	
    }    


}
