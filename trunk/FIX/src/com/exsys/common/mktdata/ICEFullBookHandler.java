
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
public class ICEFullBookHandler {

	private HashMap bidSymbols = new HashMap();
	private HashMap offerSymbols = new HashMap();
	private MarketDataNotifier mdNotifier = null;	

	/**
	 * ICEBookHandler constructor
	 */
	public ICEFullBookHandler(MarketDataNotifier mdn)
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
			//BookRow bidRows[] = new BookRow[bookSize];
			ArrayList bidRows = new ArrayList();
			BookRow row = new BookRow(size,price,dispPrice,0); 
			bidRows.add(0,row);
			//bidRows[0] = new BookRow(size,price,dispPrice,0);
			bidSymbols.put(symbol,bidRows);
			// THIS MEANS BEST BID GOT MODIFIED
			System.out.println("BEST BID = " + size +"@"+dispPrice);
			mdNotifier.bestBid(symbol,row,false);			

		}
		else
		{
			//BookRow bidRows[] = (BookRow[])bidSymbols.get(symbol);
			ArrayList bidRows = (ArrayList)bidSymbols.get(symbol);
			int bsize = bidRows.size();
			int index = -1;
			for (int i=0; i<bsize; i++)
			{
				BookRow currentBid = (BookRow)bidRows.get(i);
				if(currentBid.bidSize == 0)
				{
					currentBid.setData(size,price,dispPrice,0);
					index = i;
					break;
				}

				if(currentBid.price == price)
				{
					currentBid.bidSize += size;
					index = i;
					break;
				}
				else if(currentBid.price < price)
				{
					// new bid is better bid
					// need to move current bids and insert this one
					boolean anyUpdate = false;
					if(currentBid.price != 0)
					{
						for (int j=bsize-1;j>i;j--)
						{
							anyUpdate = true;
							if(j==bsize-1)
							{
								bidRows.add(new BookRow((BookRow)bidRows.get(j)));
							}
							BookRow obj = (BookRow)bidRows.get(j-1);
							((BookRow)bidRows.get(j)).setData(obj.bidSize,
									       obj.price,
									       obj.displayPrice,
									       0);							
						}
					}
					if(anyUpdate)
						currentBid.setData(size,price,dispPrice,0);
					else
						bidRows.add(i,new BookRow(size,price,dispPrice,0));
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
				BookRow obj = (BookRow)bidRows.get(index);
				System.out.println("BEST BID = " + obj.bidSize +"@"+obj.displayPrice);
				mdNotifier.bestBid(symbol,obj,false);
			}
			// if we did not find a place in the current bids, add it to the 
			// end
			if(index == -1)
			{
					bidRows.add(new BookRow(size,price,dispPrice,0));					
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

			ArrayList bidRows = (ArrayList)bidSymbols.get(symbol);
			int index = -1;
			for (int i=0; i<bidRows.size(); i++)
			{
				if(((BookRow)bidRows.get(i)).bidSize == 0)
				{
					break;
				}
				if(((BookRow)bidRows.get(i)).price == price)
				{
					double diff = size - oldSize;
					((BookRow)bidRows.get(i)).bidSize += (diff);
					index = i;
					break;
				}

			}

			if(index==0)
			{
				// THIS MEANS BEST BID GOT MODIFIED
				BookRow obj = (BookRow)bidRows.get(index);
				System.out.println("BEST BID = " + obj.bidSize +"@"+obj.displayPrice);
				mdNotifier.bestBid(symbol,obj,false);

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

			ArrayList bidRows = (ArrayList)bidSymbols.get(symbol);
			int index = -1;
			int bsize = bidRows.size();
			for (int i=0; i<bsize; i++)
			{
				BookRow currentBid = ((BookRow)bidRows.get(i));
				if( currentBid.bidSize == 0)
				{
					break;
				}
				if(currentBid.price == price)
				{
					currentBid.bidSize -= size;
					// if the size gets to zero, then we need to adjust bids
					if(currentBid.bidSize <= 0)
					{
						for(int j=i;j<bsize-1;j++)
						{
							BookRow obj = (BookRow)bidRows.get(j+1);
							
							((BookRow)bidRows.get(j)).setData(obj.bidSize,
									       obj.price,
									       obj.displayPrice,
									       0);
							// if we see rows with zero price,
							// there is no point in going ahead
							if(obj.price == 0)
							break;
							
						}
						
						((BookRow)bidRows.get(bsize-1)).setData(0,0,"0",0);
					}
					index = i;
					break;
				}

			}

			if(index==0 && !isPartial)
			{
				// THIS MEANS BEST BID GOT MODIFIED
				BookRow obj = (BookRow)bidRows.get(index);
				System.out.println("BEST BID = " + obj.bidSize +"@"+obj.displayPrice);
				mdNotifier.bestBid(symbol,obj,false);
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
			ArrayList offerRows = new ArrayList();
			BookRow row = new BookRow(0,price,dispPrice,size);
			offerRows.add(0, row);
			offerSymbols.put(symbol,offerRows);
			// THIS MEANS BEST OFFER GOT MODIFIED
			System.out.println("BEST OFFER = " + size +"@"+dispPrice);	
			mdNotifier.bestOffer(symbol,row,false);		
			
		}
		else
		{
			ArrayList offerRows = (ArrayList)offerSymbols.get(symbol);
			int index = -1;
			int bsize = offerRows.size();
			
			for (int i=0; i<bsize; i++)
			{
				BookRow currentOffer = (BookRow)offerRows.get(i);
				if( currentOffer.offerSize == 0 )
				{
					currentOffer.setData(0,price,dispPrice,size);
					index = i;
					break;
				}

				if(currentOffer.price == price)
				{
					currentOffer.offerSize += size;
					index = i;
					break;
				}
				else if(currentOffer.price > price)
				{
					boolean anyUpdate = false;
					if(currentOffer.price != 0)
					{
					// new offer is better offer
					// need to move current offers and insert this one
					for (int j=bsize-1;j>i;j--)
					{
						anyUpdate = true;
							if(j==bsize-1)
							{
								offerRows.add(new BookRow((BookRow)offerRows.get(j)));
							}

							BookRow obj = (BookRow)offerRows.get(j-1);

							((BookRow)offerRows.get(j)).setData(0,
									       obj.price,
									       obj.displayPrice,
									       obj.offerSize);
							
						
					}
					}
					if(anyUpdate)
						((BookRow)offerRows.get(i)).setData(0,price,dispPrice,size);
					else
						offerRows.add(i,new	BookRow(0,price,dispPrice,size));					
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
				BookRow obj = (BookRow)offerRows.get(index);
				System.out.println("BEST OFFER = " + obj.offerSize +"@"+obj.displayPrice);
				mdNotifier.bestOffer(symbol,obj,false);
			}
			
			// this means we did not add this offer. so add it
			if(index == -1)				
			{
				offerRows.add(new BookRow(0,price,dispPrice,size));
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
			ArrayList offerRows = (ArrayList)offerSymbols.get(symbol);
			int index = -1;
			for (int i=0; i<offerRows.size(); i++)
			{
				if(((BookRow)offerRows.get(i)).offerSize == 0)
				{
					break;
				}
				if(((BookRow)offerRows.get(i)).price == price)
				{
					double diff = size - oldSize;
					((BookRow)offerRows.get(i)).offerSize += (diff);
					index = i;
					break;
				}

			}

			if(index==0)
			{
				// THIS MEANS BEST OFFER GOT MODIFIED
				BookRow obj = (BookRow)offerRows.get(index);
				System.out.println("BEST OFFER = " + obj.offerSize +"@"+obj.displayPrice);
				mdNotifier.bestOffer(symbol,obj,false);				
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
			ArrayList offerRows = (ArrayList)offerSymbols.get(symbol);
			int index = -1;
			int bsize = offerRows.size();
			for (int i=0; i<bsize; i++)
			{
				BookRow currentOffer = (BookRow)offerRows.get(i);
				if(currentOffer.offerSize == 0)
				{
					break;
				}
				if(currentOffer.price == price)
				{
					currentOffer.offerSize -= size;

					// if the size gets to zero, then we need to adjust offers
					if(currentOffer.offerSize <= 0)
					{

						for(int j=i;j<bsize-1;j++)
						{
							
								BookRow obj = (BookRow)offerRows.get(j+1);
								((BookRow)offerRows.get(j)).setData(0,
									       obj.price,
									       obj.displayPrice,
									       obj.offerSize);
									       
							// if we see rows with zero price,
							// there is no point in going ahead
							if(obj.price == 0)
							break;									       
							
						}
						
						((BookRow)offerRows.get(bsize-1)).setData(0,0,"0",0);
					}


					index = i;
					break;
				}

			}

			if(index==0 && !isPartial)
			{
				// THIS MEANS BEST OFFER GOT MODIFIED
				BookRow obj = (BookRow)offerRows.get(index);
				System.out.println("BEST OFFER = " + obj.offerSize +"@"+obj.displayPrice);
				mdNotifier.bestOffer(symbol,obj,false);				

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
	public ArrayList getBidBook(String symbol)
	{
		if( bidSymbols != null && bidSymbols.containsKey(symbol))
		{			
			return (ArrayList)bidSymbols.get(symbol);
		}

		return null;
	}
	/**
	 * method to get book infor for a given symbol
	 * @param symbol
	 * @return array list of book rows
	 */
	public ArrayList getOfferBook(String symbol)
	{
		if( offerSymbols != null && offerSymbols.containsKey(symbol))
		{
			return (ArrayList)offerSymbols.get(symbol);
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
			ArrayList book = (ArrayList)bidSymbols.get(symbol);
			if(book.size() > 0)
			{
				for(int i=0;i<book.size();i++)
				{
					Logger.debug("Symbol = "+symbol+" " + ((BookRow)book.get(i)).toString());
				}
			}
			else
			{
				Logger.debug("BOOK has no rows for symbol - " + symbol);
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
			ArrayList book = (ArrayList)offerSymbols.get(symbol);
			if(book.size() > 0)
			{
				for(int i=0;i<book.size();i++)
				{
					Logger.debug("Symbol = "+symbol+" " + ((BookRow)book.get(i)).toString());
				}
			}
			else
			{
				Logger.debug("BOOK has no rows for symbol - " + symbol);
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
			ArrayList book = (ArrayList)bidSymbols.get(symbol);
			book.clear();
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
			ArrayList book = (ArrayList)offerSymbols.get(symbol);
			book.clear();
		}
	}


}
