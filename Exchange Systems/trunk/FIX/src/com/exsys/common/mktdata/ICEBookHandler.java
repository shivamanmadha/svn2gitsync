
package com.exsys.common.mktdata;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import com.exsys.service.Logger;

/**
 * @author kreddy
 *
 * This class manages the book entries
 * as bids and offers are added for different symbols
 */
public class ICEBookHandler {
	private int bookSize = 100;
	private HashMap bidSymbols = new HashMap();
	private HashMap offerSymbols = new HashMap();
	private MarketDataNotifier mdNotifier = null;	


	/**
	 * ICEBookHandler constructor
	 */
	public ICEBookHandler(MarketDataNotifier mdn)
	{
		mdNotifier = mdn;		
	}

	/**
	 * method to add a bid
	 * @param symbol
	 * @param price
	 * @param dispPrice
	 * @param size
	 */
	public void addBid(String symbol,double price, String dispPrice, double size)
	{
		

		if( !bidSymbols.containsKey(symbol))
		{
			BookRow bidRows[] = new BookRow[bookSize];
			bidRows[0] = new BookRow(size,price,dispPrice,0);
			bidSymbols.put(symbol,bidRows);
			// THIS MEANS BEST BID GOT MODIFIED
			System.out.println("BEST BID = " + bidRows[0].bidSize +"@"+bidRows[0].displayPrice);
			mdNotifier.bestBid(symbol,bidRows[0],false);
			

		}
		else
		{
			BookRow bidRows[] = (BookRow[])bidSymbols.get(symbol);
			int index = -1;			
			for (int i=0; i<bookSize; i++)
			{
				if(bidRows[i] == null)
				{
					bidRows[i] = new BookRow(size,price,dispPrice,0);
					index = i;					
					break;
				}
				else if(bidRows[i].bidSize == 0)
				{
					bidRows[i].setData(size,price,dispPrice,0);
					index = i;
					break;
				}
				
				if(bidRows[i].price == price)
				{
					bidRows[i].bidSize += size;
					index = i;					
					break;
				}
				else if(bidRows[i].price < price)
				{
					// new bid is better bid
					// need to move current bids and insert this one
					if(bidRows[i].price != 0)
					{
						for (int j=bookSize-1;j>i;j--)
						{							

							if(bidRows[j-1] == null)						
								bidRows[j] = bidRows[j-1];
							else
							{
								if(bidRows[j] == null)
								{
									bidRows[j] = new BookRow(bidRows[j-1].bidSize,
									       bidRows[j-1].price,
									       bidRows[j-1].displayPrice,
									       0);
								}
								else
								{
									bidRows[j].setData(bidRows[j-1].bidSize,
									       bidRows[j-1].price,
									       bidRows[j-1].displayPrice,
									       0);
								}
							}							
							
							
							
						}
					}
					bidRows[i].setData(size,price,dispPrice,0);
					index = i;
					
					break;
					
				}
				else
				{
					// new bid is worse bid
					// nothing is needed
					
				}
				
			}
			
			if(index==0)
			{
				// THIS MEANS BEST BID GOT MODIFIED
				System.out.println("BEST BID = " + bidRows[index].bidSize +"@"+bidRows[index].displayPrice);
				mdNotifier.bestBid(symbol,bidRows[index],false);
			}
			
		}
		
	}

	/**
	 * method to change a bid
	 * @param symbol
	 * @param price
	 * @param dp
	 * @param size
	 */
	public void changeBid(String symbol,double price, String dp, double size, double oldSize)
	{

		
		if( bidSymbols.containsKey(symbol))
		{
						
			BookRow bidRows[] = (BookRow[])bidSymbols.get(symbol);
			int index = -1;			
			for (int i=0; i<bookSize; i++)
			{
				if(bidRows[i] == null || bidRows[i].bidSize == 0)
				{
					break;
				}
				if(bidRows[i].price == price)
				{
					double diff = size - oldSize;
					bidRows[i].bidSize += (diff);
					index = i;					
					break;
				}
				
			}
			
			if(index==0)
			{
				// THIS MEANS BEST BID GOT MODIFIED
				System.out.println("BEST BID = " + bidRows[index].bidSize +"@"+bidRows[index].displayPrice);
				mdNotifier.bestBid(symbol,bidRows[index],false);				
			}
						

		}//symbols
		
	}

	/**
	 * method to delete a bid
	 * @param symbol
	 * @param price
	 * @param dp
	 * @param size
	 */
	public void removeBid(String symbol,double price, String dp,double size)
	{
		removeBid(symbol,price,dp,size,false);
	}

	/**
	 * method to delete a bid
	 * @param symbol
	 * @param price
	 * @param dp
	 * @param size
	 * @param isPartial
	 */
	public void removeBid(String symbol,double price, String dp,double size,boolean isPartial)
	{

		
		if( bidSymbols.containsKey(symbol))
		{
						
			BookRow bidRows[] = (BookRow[])bidSymbols.get(symbol);
			int index = -1;			
			for (int i=0; i<bookSize; i++)
			{
				if(bidRows[i] == null || bidRows[i].bidSize == 0)
				{
					break;
				}
				if(bidRows[i].price == price)
				{
					bidRows[i].bidSize -= size;
					// if the size gets to zero, then we need to adjust bids
					if(bidRows[i].bidSize <= 0)
					{
						for(int j=i;j<bookSize-1;j++)
						{
							if(bidRows[j+1] != null)
							{
								bidRows[j].setData(bidRows[j+1].bidSize,
									       bidRows[j+1].price,
									       bidRows[j+1].displayPrice,
									       0);
							}
							else
							{
								if(bidRows[j] != null)
									bidRows[j].setData(0,0,"0",0);
							}
						}
						if(bidRows[bookSize-1] != null)
							bidRows[bookSize-1].setData(0,0,"0",0);
					}
					index = i;					
					break;
				}
				
			}
			
			if(index==0 && !isPartial)
			{
				// THIS MEANS BEST BID GOT MODIFIED
				System.out.println("BEST BID = " + bidRows[index].bidSize +"@"+bidRows[index].displayPrice);
				if(bidRows[index].bidSize != 0)
					mdNotifier.bestBid(symbol,bidRows[index],false);
			}
						

		}//symbols
	}


	/**
	 * method to add offer
	 * @param symbol
	 * @param price
	 * @param dispPrice
	 * @param size
	 */
	public void addOffer(String symbol,double price, String dispPrice, double size)
	{
		

		if( !offerSymbols.containsKey(symbol))
		{
			BookRow offerRows[] = new BookRow[bookSize];
			offerRows[0] = new BookRow(0,price,dispPrice,size);
			offerSymbols.put(symbol,offerRows);
			// THIS MEANS BEST OFFER GOT MODIFIED
			System.out.println("BEST OFFER = " + offerRows[0].offerSize +"@"+offerRows[0].displayPrice);
			mdNotifier.bestOffer(symbol,offerRows[0],false);
			

		}
		else
		{
			BookRow offerRows[] = (BookRow[])offerSymbols.get(symbol);
			int index = -1;			
			for (int i=0; i<bookSize; i++)
			{
				if(offerRows[i] == null)
				{
					offerRows[i] = new BookRow(0,price,dispPrice,size);
					index = i;					
					break;
				}
				else if( offerRows[i].offerSize == 0 )
				{
					offerRows[i].setData(0,price,dispPrice,size);
					index = i;					
					break;
				}
				
				if(offerRows[i].price == price)
				{
					offerRows[i].offerSize += size;
					index = i;					
					break;
				}
				else if(offerRows[i].price > price)
				{
					if(offerRows[i].price != 0)
					{
					// new offer is better offer
					// need to move current offers and insert this one
					for (int j=bookSize-1;j>i;j--)
					{
						if(offerRows[j-1] == null)						
							offerRows[j] = offerRows[j-1];
						else
						{
							if(offerRows[j] == null)
							{
								offerRows[j] = new BookRow(0,
									       offerRows[j-1].price,
									       offerRows[j-1].displayPrice,
									       offerRows[j-1].offerSize);
							}
							else
							{
								offerRows[j].setData(0,
									       offerRows[j-1].price,
									       offerRows[j-1].displayPrice,
									       offerRows[j-1].offerSize);
							}
						}
					}
					}
					offerRows[i].setData(0,price,dispPrice,size);
					index = i;
					
					break;
					
				}
				else
				{
					// new offer is worse offer
					// nothing is needed
					
				}
				
			}
			
			if(index==0)
			{
				// THIS MEANS BEST OFFER GOT MODIFIED
				System.out.println("BEST OFFER = " + offerRows[index].offerSize +"@"+offerRows[index].displayPrice);
				mdNotifier.bestOffer(symbol,offerRows[index],false);
			}
			

		}
	}


	/**
	 * method to change offer
	 * @param symbol
	 * @param price
	 * @param dp
	 * @param size
	 */
	public void changeOffer(String symbol,double price, String dp,double size, double oldSize)
	{
		
		if( offerSymbols.containsKey(symbol))
		{
			BookRow offerRows[] = (BookRow[])offerSymbols.get(symbol);
			int index = -1;			
			for (int i=0; i<bookSize; i++)
			{
				if(offerRows[i] == null || offerRows[i].offerSize == 0)
				{
					break;
				}
				if(offerRows[i].price == price)
				{
					double diff = size - oldSize;
					offerRows[i].offerSize += (diff);
					index = i;					
					break;
				}
				
			}
			
			if(index==0)
			{
				// THIS MEANS BEST OFFER GOT MODIFIED
				System.out.println("BEST OFFER = " + offerRows[index].offerSize +"@"+offerRows[index].displayPrice);
				mdNotifier.bestOffer(symbol,offerRows[index],false);				
			}
		}//symbols

	}

	/**
	 * method to delete offer
	 * @param symbol
	 * @param price
	 * @param dp
	 * @param size
	 */
	public void removeOffer(String symbol,double price, String dp, double size)
	{
		removeOffer(symbol,price,dp,size,false);
	}

	/**
	 * method to delete offer
	 * @param symbol
	 * @param price
	 * @param dp
	 * @param size
	 * @param isPartial
	 */
	public void removeOffer(String symbol,double price, String dp, double size, boolean isPartial)
	{
		

		if( offerSymbols.containsKey(symbol))
		{
			BookRow offerRows[] = (BookRow[])offerSymbols.get(symbol);
			int index = -1;			
			for (int i=0; i<bookSize; i++)
			{
				if(offerRows[i] == null || offerRows[i].offerSize == 0)
				{
					break;
				}
				if(offerRows[i].price == price)
				{
					offerRows[i].offerSize -= size;
					
					// if the size gets to zero, then we need to adjust offers
					if(offerRows[i].offerSize <= 0)
					{
						for(int j=i;j<bookSize-1;j++)
						{
							if(offerRows[j+1] != null)
							{
								offerRows[j].setData(0,
									       offerRows[j+1].price,
									       offerRows[j+1].displayPrice,
									       offerRows[j+1].offerSize);
							}
							else
							{
								if(offerRows[j] != null)
									offerRows[j].setData(0,0,"0",0);
									
							}
						}
						if(offerRows[bookSize-1] != null)
							offerRows[bookSize-1].setData(0,0,"0",0);
					}
					
					
					index = i;					
					break;
				}
				
			}
			
			if(index==0 && !isPartial)
			{
				// THIS MEANS BEST OFFER GOT MODIFIED
				System.out.println("BEST OFFER = " + offerRows[index].offerSize +"@"+offerRows[index].displayPrice);
				if(	offerRows[index].offerSize != 0 )			
				mdNotifier.bestOffer(symbol,offerRows[index],false);				
			}
		}//symbols
	}



	/**
	 * method to display all books
	 */
	public void displayAllBooks()
	{
		if(bidSymbols == null && offerSymbols == null) return;
		if(bidSymbols != null)
		{
			Iterator keys = bidSymbols.keySet().iterator();
			Logger.debug("Book Symbols :");
			while( keys.hasNext() )
			{
				String symbol = (String) keys.next();
				displayBidBook(symbol);
			}
		}
		if(offerSymbols != null)
		{
			Iterator keys = offerSymbols.keySet().iterator();
			Logger.debug("Book Symbols :");
			while( keys.hasNext() )
			{
				String symbol = (String) keys.next();
				displayOfferBook(symbol);
			}
		}		
	}

	/**
	 * method to get book infor for a given symbol
	 * @param symbol
	 * @return array list of book rows
	 */
	public BookRow[] getBidBook(String symbol)
	{
		if( bidSymbols != null && bidSymbols.containsKey(symbol))
		{
			return (BookRow[])bidSymbols.get(symbol);
			
		}

		return null;
	}
	/**
	 * method to get book infor for a given symbol
	 * @param symbol
	 * @return array list of book rows
	 */
	public BookRow[] getOfferBook(String symbol)
	{
		if( offerSymbols != null && offerSymbols.containsKey(symbol))
		{
			return (BookRow[])offerSymbols.get(symbol);
			
		}

		return null;
	}	

	/**
	 * method to display book for a given symbol
	 * @param symbol
	 */
	public void displayBook(String symbol)
	{
		displayBidBook(symbol);
		displayOfferBook(symbol);
	}

	/**
	 * method to display book for a given symbol
	 * @param symbol
	 */
	public void displayBidBook(String symbol)
	{
		if( bidSymbols != null && bidSymbols.containsKey(symbol))
		{
			BookRow book[] = (BookRow[])bidSymbols.get(symbol);
			for (int i=0; i<book.length;i++)
			{
				if(book[i] != null)
				Logger.debug("Symbol = "+symbol+" " + book[i].toString());
			} 
		}
		else
		{
			Logger.debug("BOOK not found for symbol - " + symbol);
		}
	}
	
	/**
	 * method to display book for a given symbol
	 * @param symbol
	 */
	public void displayOfferBook(String symbol)
	{
		if( offerSymbols != null && offerSymbols.containsKey(symbol))
		{
			BookRow book[] = (BookRow[])offerSymbols.get(symbol);
			for (int i=0; i<book.length;i++)
			{
				if(book[i] != null)
				Logger.debug("Symbol = "+symbol+" " + book[i].toString());
			} 
		}
		else
		{
			Logger.debug("BOOK not found for symbol - " + symbol);
		}
	}	

	/**
	 * method to clear book rows for all symbols
	 */
	public void clearBook()
	{
		if(bidSymbols != null)
		{
			Iterator keys = bidSymbols.keySet().iterator();
			Logger.debug("Bid Book Symbols :");
			while( keys.hasNext() )
			{
				String symbol = (String) keys.next();
				clearBidBook(symbol);
			}
		}
		
		if(offerSymbols != null)
		{
			Iterator keys = offerSymbols.keySet().iterator();
			Logger.debug("Offer Book Symbols :");
			while( keys.hasNext() )
			{
				String symbol = (String) keys.next();
				clearOfferBook(symbol);
			}
		}		

	}

	/**
	 * method to clear book rows for a given symbol
	 * @param symbol
	 */
	public void clearBidBook(String symbol)
	{
		if( bidSymbols != null && bidSymbols.containsKey(symbol))
		{
			BookRow book[] = (BookRow[])bidSymbols.get(symbol);
			book = null;
		}
		
	}
	/**
	 * method to clear book rows for a given symbol
	 * @param symbol
	 */
	public void clearOfferBook(String symbol)
	{		
		if( offerSymbols != null && offerSymbols.containsKey(symbol))
		{
			BookRow book[] = (BookRow[])offerSymbols.get(symbol);
			book = null;
		}		
	}
	

}
