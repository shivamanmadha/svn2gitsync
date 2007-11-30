package com.exsys.mktdata.book;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.math.BigDecimal;

/**
 *
 * This class handles book for RLC market data messages
 * recieved from CME
 */
public class RLCBookHandler {
	private HashMap maSymbols = new HashMap();
	private HashMap mySymbols = new HashMap();

	/**
	 * RLCBookHandler constructor
	 */
	public RLCBookHandler()
	{
	}

	/**
	 * method to handle MYBookRow
	 * @param symbol
	 * @param level
	 * @param buyLimitQty
	 * @param buyLimitPrice
	 * @param sellLimitQty
	 * @param sellLimitPrice
	 */
	public void handleMYBookRow(String symbol,
								int level,
								int buyLimitQty,
								BigDecimal buyLimitPrice,
								int sellLimitQty,
								BigDecimal sellLimitPrice)


	{
		synchronized(mySymbols)
		{

		if( !mySymbols.containsKey(symbol))
		{
			RLCMYBookRow[] bookRows = new RLCMYBookRow[5];
			//initialize
			bookRows[level-1] = new RLCMYBookRow(buyLimitQty,
									buyLimitPrice,
									sellLimitQty,
									sellLimitPrice);
			for(int i=0;i<5;i++)
			{
				if(i != (level-1))
				{
					bookRows[i] = new RLCMYBookRow();
				}
			}

			mySymbols.put(symbol,bookRows);

		}
		else
		{
			// this means we need to update existing one
			RLCMYBookRow[] bookRows = (RLCMYBookRow[])mySymbols.get(symbol);
			bookRows[level-1].setData(buyLimitQty,
									buyLimitPrice,
									sellLimitQty,
									sellLimitPrice);

		}
		
		}
	}

	/**
	 * method to handle MA Book Row
	 * @param symbol
	 * @param level
	 * @param numBuys
	 * @param buyLimitQty
	 * @param buyLimitPrice
	 * @param numSells
	 * @param sellLimitQty
	 * @param sellLimitPrice
	 */
	public void handleMABookRow(String symbol,
								int level,
								int numBuys,
								int buyLimitQty,
								BigDecimal buyLimitPrice,
								int numSells,
								int sellLimitQty,
								BigDecimal sellLimitPrice)


	{
		synchronized(maSymbols)
		{


		if( !maSymbols.containsKey(symbol))
		{
			RLCMABookRow[] bookRows = new RLCMABookRow[5];
			//initialize
			bookRows[level-1] = new RLCMABookRow(numBuys,
									buyLimitQty,
									buyLimitPrice,
									numSells,
									sellLimitQty,
									sellLimitPrice);
			for(int i=0;i<5;i++)
			{
				if(i != (level-1))
				{
					bookRows[i] = new RLCMABookRow();
				}
			}

			maSymbols.put(symbol,bookRows);

		}
		else
		{
			// this means we need to update existing one
			RLCMABookRow[] bookRows = (RLCMABookRow[])maSymbols.get(symbol);
			bookRows[level-1].setData(numBuys,
									buyLimitQty,
									buyLimitPrice,
									numSells,
									sellLimitQty,
									sellLimitPrice);

		}
		
		}
	}


	/**
	 * method to display MY Books for all symbols
	 * 
	 */
	public void displayAllMYBooks()
	{
		if(mySymbols == null) return;
		Iterator keys = mySymbols.keySet().iterator();
		System.out.println("MY Book Symbols :");
		while( keys.hasNext() )
		{
			String symbol = (String) keys.next();
			displayMYBook(symbol);
		}
	}
	/**
	 * method to display MA Books for all symbols
	 */
	public void displayAllMABooks()
	{
		if(maSymbols == null) return;
		Iterator keys = maSymbols.keySet().iterator();
		System.out.println("MA Book Symbols :");
		while( keys.hasNext() )
		{
			String symbol = (String) keys.next();
			displayMABook(symbol);
		}
	}

	/**
	 * method to get MY book for a given symbol
	 * @param symbol
	 * @return
	 */
	public RLCMYBookRow[] getMYBook(String symbol)
	{
		synchronized(mySymbols)
		{

		if( mySymbols != null && mySymbols.containsKey(symbol))
		{
			return (RLCMYBookRow[])mySymbols.get(symbol);
		}
		
		}

		return null;
	}
	
	/**
	 * method to get consolidated book for a given symbol
	 * @param symbol
	 * @return
	 */
	public RLCMYBookRow[] getBook(String symbol)
	{
		// first get MY book
		RLCMYBookRow[] myRows = getMYBook(symbol);
		
		// then get MA book		
		RLCMABookRow[] maRows = getMABook(symbol);
		
		if(maRows == null)
		{
			return myRows;
		}
		
		if(myRows == null)
		{
			if(maRows == null)
			{
				return null;
			}
			else
			{
				// fill ma rows as my rows and return
				RLCMYBookRow[] myRows_1 = new RLCMYBookRow[5];
				for(int i=0;i<5;i++)
				{
					myRows_1[i] = new RLCMYBookRow(maRows[i]);
				}
				
				return myRows_1;
			}
		}
		
		// this means neither of them is null
		// need to merge the rows
		RLCMYBookRow[] mergedRows = new RLCMYBookRow[5];
		// initialize mergedRows with MA Book		
		for(int i=0; i<5; i++)
		{
				mergedRows[i] = new RLCMYBookRow(maRows[i]);
		}
		
		// now go thru my rows, and update merged rows appropriately for bids
		for(int i=0; i<5; i++)
		{
			if(myRows[i].buyLimitQty != 0)
			{
				for(int j=0; j<5; j++)
				{
					int compare = (myRows[i].buyLimitPrice).compareTo(mergedRows[j].buyLimitPrice);
					if(compare == 0)
					{
						mergedRows[j].buyLimitQty += myRows[i].buyLimitQty;
						break;
					}
					else if(compare < 0)
					{
						// ignore
					}
					else // >0
					{
						// need to shift, max j-1 shifts
						//RLCMYBookRow currentRow = mergedRows[j];
						for(int k=4; k>j; k--)
						{
							mergedRows[k].buyLimitPrice = mergedRows[k-1].buyLimitPrice;
							mergedRows[k].buyLimitQty = mergedRows[k-1].buyLimitQty;
						}
						mergedRows[j].buyLimitQty = myRows[i].buyLimitQty;
						mergedRows[j].buyLimitPrice = myRows[i].buyLimitPrice;
						
						//if(j != 4)
						//{
							//mergedRows[j+1].buyLimitPrice = currentRow.buyLimitPrice;
							//mergedRows[j+1].buyLimitQty = currentRow.buyLimitQty;
						//}
						break;
					}
				}				
				
			}
		}
		
		
		// now go thru my rows, and update merged rows appropriately for offers
		for(int i=0; i<5; i++)
		{
			if(myRows[i].sellLimitQty != 0)
			{
				for(int j=0; j<5; j++)
				{
					int compare = (myRows[i].sellLimitPrice).compareTo(mergedRows[j].sellLimitPrice);
					if(compare == 0)
					{
						mergedRows[j].sellLimitQty += myRows[i].sellLimitQty;
						break;
					}
					else if(compare > 0)
					{
						// ignore						
					}
					else // <0
					{
						// need to shift, max j-1 shifts
						//RLCMYBookRow currentRow = mergedRows[j];
						for(int k=4; k>j; k--)
						{
							mergedRows[k].sellLimitPrice = mergedRows[k-1].sellLimitPrice;
							mergedRows[k].sellLimitQty = mergedRows[k-1].sellLimitQty;
						}
						mergedRows[j].sellLimitQty = myRows[i].sellLimitQty;
						mergedRows[j].sellLimitPrice = myRows[i].sellLimitPrice;
						
						//if(j!=4)
						//{
							//mergedRows[j+1].sellLimitPrice = currentRow.sellLimitPrice;
							//mergedRows[j+1].sellLimitQty = currentRow.sellLimitQty;
						//}
						break;
					}
				}				
				
			}
		}		


		return mergedRows;
	}
	
	/**
	 * method to get MA book for a given symbol
	 * @param symbol
	 * @return
	 */
	public RLCMABookRow[] getMABook(String symbol)
	{
		synchronized(maSymbols)
		{
		
		if( maSymbols != null && maSymbols.containsKey(symbol))
		{
			return (RLCMABookRow[])maSymbols.get(symbol);
		}
		
		}

		return null;
	}


	/**
	 * method to display MY book for a given symbol
	 * @param symbol
	 */
	public void displayMYBook(String symbol)
	{
		if( mySymbols != null && mySymbols.containsKey(symbol))
		{
			RLCMYBookRow[] book = (RLCMYBookRow[])mySymbols.get(symbol);
			for(int i=0;i<5;i++)
			{
				System.out.println("Level "+(i+1) + " " + book[i].toString());
			}
		}
		else
		{
			System.out.println("BOOK not found for symbol - " + symbol);
		}
	}

	/**
	 * method to display MA book for a given symbol
	 * @param symbol
	 */
	public void displayMABook(String symbol)
	{
		if( maSymbols != null && maSymbols.containsKey(symbol))
		{
			RLCMABookRow[] book = (RLCMABookRow[])maSymbols.get(symbol);
			for(int i=0;i<5;i++)
			{
				System.out.println("Level "+(i+1) + " " + book[i].toString());
			}
		}
		else
		{
			System.out.println("BOOK not found for symbol - " + symbol);
		}
	}

	/**
	 * method to clear MY book for all symbols
	 * 
	 */
	public void clearMYBook()
	{
		if(mySymbols == null) return;
		Iterator keys = mySymbols.keySet().iterator();
		System.out.println("MY Book Symbols :");
		while( keys.hasNext() )
		{
			String symbol = (String) keys.next();
			clearMYBook(symbol);
		}

	}

	/**
	 * method to clear MY book for a given symbol
	 * @param symbol
	 */
	public void clearMYBook(String symbol)
	{
		if( mySymbols != null && mySymbols.containsKey(symbol))
		{
			mySymbols.remove(symbol);
		}
	}

	/**
	 * method to clear MA book for all symbols
	 */
	public void clearMABook()
	{
		if(maSymbols == null) return;
		Iterator keys = maSymbols.keySet().iterator();
		System.out.println("MA Book Symbols :");
		while( keys.hasNext() )
		{
			String symbol = (String) keys.next();
			clearMABook(symbol);
		}

	}

	/**
	 * method to clear MA book for a given symbol
	 * @param symbol
	 */
	public void clearMABook(String symbol)
	{
		if( maSymbols != null && maSymbols.containsKey(symbol))
		{
			maSymbols.remove(symbol);
		}
	}

}
