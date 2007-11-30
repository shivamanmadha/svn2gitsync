
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
public class BookHandler {
	private HashMap symbols = new HashMap();

	/**
	 * BookHandler constructor
	 */
	public BookHandler()
	{
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

		if( !symbols.containsKey(symbol))
		{
			ArrayList book = new ArrayList();
			book.add(new BookRow(size,price,dispPrice,0));
			symbols.put(symbol,book);

		}
		else
		{
			// this means we need to add this bid
			ArrayList book = (ArrayList)symbols.get(symbol);
			int s = book.size();
			if(s > 0)
			{
				// since this is a bid, first check if the price is lower
				// than the last row???
				ArrayList b = (ArrayList)symbols.get(symbol);
				boolean pendAdd = false;
				//int index = 0;
				for(int i=0;i<s;i++)
				{
					BookRow row = (BookRow)b.get(i);
					if( row.price == price )
					{
						row.bidSize += size;
						row.accumBidSize += size;
						// once you find the exact price
						// increment accumbidsize on remaining
						for(int j=i+1;j<s;j++)
						{
							((BookRow)b.get(j)).accumBidSize += size;
						}
						pendAdd = false;
						break;
						// increment accumBidSize
					}
					else if( row.price < price )
					{
						// we will need to add this one
						b.add(i,new BookRow(size,price,dispPrice,0));
						if(i>0)
							((BookRow)b.get(i)).accumBidSize += ((BookRow)b.get(i-1)).accumBidSize;
						for(int j=i+1;j<s+1;j++)
						{
							((BookRow)b.get(j)).accumBidSize += size;
						}
						pendAdd = false;
						break;


					}
					else // row.price > price
					{
						//if(((BookRow)b.get(i)).bidSize > 0)
						//{
						//	((BookRow)b.get(i)).accumBidSize += size;
						//}
						pendAdd = true;
						//index = i;
						// we will need to add this one later
					}

				}
				if(pendAdd == true)
				{
					//this must be the lowest bid
					// we will need to add this one
					b.add(s,new BookRow(size,price,dispPrice,0));
					((BookRow)b.get(s)).accumBidSize += ((BookRow)b.get(s-1)).accumBidSize;
				}
			}
			else
			{
				book.add(new BookRow(size,price,dispPrice,0));
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
	public void changeBid(String symbol,double price, String dp, double size)
	{


		if( symbols.containsKey(symbol))
		{
			// this means we need to add this bid
			ArrayList book = (ArrayList)symbols.get(symbol);
			int s = book.size();
			if(s > 0)
			{

				ArrayList b = (ArrayList)symbols.get(symbol);
				
				for(int i=0;i<s;i++)
				{
					BookRow row = (BookRow)b.get(i);
					double diff = size - row.bidSize;
					if( row.price == price )
					{
						
						row.accumBidSize += (diff);
						row.bidSize = size;
						
						// once you find the exact price
						// decrement accumbidsize on remaining
						for(int j=i+1;j<s;j++)
						{
							((BookRow)b.get(j)).accumBidSize += diff;
						}

						// if bid size is zero, then remove the row
						if(row.bidSize <= 0 && row.offerSize <= 0)
						b.remove(i);
						
						
						break;
						// increment accumBidSize
					}
					else if( row.price > price )
					{
						if(((BookRow)b.get(i)).bidSize > 0)
						{
							((BookRow)b.get(i)).accumBidSize += diff;
						}						
						
					}

				}//for

			}//s>0

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


		if( symbols.containsKey(symbol))
		{
			// this means we need to add this bid
			ArrayList book = (ArrayList)symbols.get(symbol);
			int s = book.size();
			if(s > 0)
			{

				ArrayList b = (ArrayList)symbols.get(symbol);
				
				for(int i=0;i<s;i++)
				{
					BookRow row = (BookRow)b.get(i);
					if( row.price == price )
					{
						row.bidSize -= size;
						row.accumBidSize -= size;
						// once you find the exact price
						// decrement accumbidsize on remaining
						for(int j=i+1;j<s;j++)
						{
							((BookRow)b.get(j)).accumBidSize -= size;
						}
						
						// if bid size is zero, then remove the row
						if(row.bidSize <= 0 && row.offerSize <= 0)
						b.remove(i);
						
						
						break;
						// increment accumBidSize
					}
					else if( row.price > price )
					{
						if(((BookRow)b.get(i)).bidSize > 0)
						{
							((BookRow)b.get(i)).accumBidSize -= size;
						}						
						
					}

				}//for

			}//s>0

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

		if( !symbols.containsKey(symbol))
		{
			ArrayList book = new ArrayList();
			book.add(new BookRow(0,price,dispPrice,size));
			symbols.put(symbol,book);

		}
		else
		{
			// this means we need to add this offer
			ArrayList book = (ArrayList)symbols.get(symbol);
			int s = book.size();
			if(s > 0)
			{
				boolean pendAdd = false;

				ArrayList b = (ArrayList)symbols.get(symbol);

				for(int i=0;i<s;i++)
				{
					BookRow row = (BookRow)b.get(i);
					if( row.price == price )
					{
						row.offerSize += size;
						row.accumOfferSize += size;
						Logger.debug("ADD OFFER");
						Logger.debug(row.toString());
						pendAdd = false;
						break;

					}
					else if( row.price < price )
					{
						// new offer is higher than current offer

						b.add(i,new BookRow(0,price,dispPrice,size));
						((BookRow)b.get(i)).accumOfferSize += ((BookRow)b.get(i+1)).accumOfferSize;
						
						pendAdd = false;
						break;
					}
					else // row.price > price
					{
						// new offer is lower than current offer
						// we will need to add this one
						
						((BookRow)b.get(i)).accumOfferSize += size;
						
						pendAdd = true;
						
					}

				}
				if(pendAdd == true)
				{
					//this must be the highest offer
					// we will need to add this one
					b.add(new BookRow(0,price,dispPrice,size));
				}
			}
			else
			{
				book.add(new BookRow(0,price,dispPrice,size));
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
	public void changeOffer(String symbol,double price, String dp,double size)
	{
		if( symbols.containsKey(symbol))
		{
			// this means we need to add this offer
			ArrayList book = (ArrayList)symbols.get(symbol);
			int s = book.size();
			if(s > 0)
			{				

				ArrayList b = (ArrayList)symbols.get(symbol);

				boolean pendRemove = false;
				int removeIndex = 0;
				for(int i=0;i<s;i++)
				{
					BookRow row = (BookRow)b.get(i);
					double diff = size - row.offerSize;
					if( row.price == price )
					{
						
						row.accumOfferSize += (diff);
						row.offerSize = size;
						
						
						// if offer size is zero, then remove the row
						if(row.bidSize <= 0 && row.offerSize <= 0)
						{
							//b.remove(i);
							//20070921 - dont remove and break
							// instead remove later, and update remaining offers also
							pendRemove = true;
							removeIndex = i;
						}
						
						
						
						//break;

					}
					else if( row.price > price )
					{
						// new offer is lower than current offer
						// we will need to add this one
						if(((BookRow)b.get(i)).offerSize > 0 )
						{
							((BookRow)b.get(i)).accumOfferSize += diff;
						}

					}


				}
				if(pendRemove)
				{
					b.remove(removeIndex);
				}

			}//s>0


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
		
		if( symbols.containsKey(symbol))
		{
			// this means we need to add this offer
			ArrayList book = (ArrayList)symbols.get(symbol);
			int s = book.size();
			if(s > 0)
			{				

				ArrayList b = (ArrayList)symbols.get(symbol);
				
				boolean pendRemove = false;
				int removeIndex = 0;

				for(int i=0;i<s;i++)
				{
					BookRow row = (BookRow)b.get(i);
					if( row.price == price )
					{
						row.offerSize -= size;
						row.accumOfferSize -= size;
						
						Logger.debug("BOOK ROW = ");
						Logger.debug(row.toString());
						// if offer size is zero, then remove the row
						if(row.bidSize <= 0 && row.offerSize <= 0)
						{
							//b.remove(i);
							//20070921 - dont remove and break
							// instead remove later, and update remaining offers also
							pendRemove = true;
							removeIndex = i;
						}
						
						
						
						//break;						

					}
					else if( row.price > price )
					{
						// new offer is lower than current offer
						// we will need to add this one
						if(((BookRow)b.get(i)).offerSize > 0 )
						{
							((BookRow)b.get(i)).accumOfferSize -= size;
						}

					}


				}
				if(pendRemove)
				{
					b.remove(removeIndex);
				}
				

			}//s>0


		}//symbols
	}



	/**
	 * method to display all books
	 */
	public void displayAllBooks()
	{
		if(symbols == null) return;
		Iterator keys = symbols.keySet().iterator();
		Logger.debug("Book Symbols :");
		while( keys.hasNext() )
		{
			String symbol = (String) keys.next();
			displayBook(symbol);
		}
	}
	
	/**
	 * method to get book infor for a given symbol
	 * @param symbol
	 * @return array list of book rows
	 */
	public ArrayList getBook(String symbol)
	{
		if( symbols != null && symbols.containsKey(symbol))
		{
			ArrayList book = (ArrayList)symbols.get(symbol);
			if(book.size() > 0)
			{
				return book;
			}
			else
			{
				return null;
			}
		}
		
		return null;
	}	
	
	/**
	 * method to display book for a given symbol
	 * @param symbol
	 */
	public void displayBook(String symbol)
	{
		if( symbols != null && symbols.containsKey(symbol))
		{
			ArrayList book = (ArrayList)symbols.get(symbol);
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
		if(symbols == null) return;
		Iterator keys = symbols.keySet().iterator();
		Logger.debug("Book Symbols :");
		while( keys.hasNext() )
		{
			String symbol = (String) keys.next();
			clearBook(symbol);
		}

	}	
	
	/**
	 * method to clear book rows for a given symbol
	 * @param symbol
	 */
	public void clearBook(String symbol)
	{
		if( symbols != null && symbols.containsKey(symbol))
		{
			ArrayList book = (ArrayList)symbols.get(symbol);
			book.clear();
		}
	}	

}
