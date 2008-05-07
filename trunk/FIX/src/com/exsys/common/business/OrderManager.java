/*
 *@(#)OrderManager.java
 *
 * Copyright 2000, by Saven Technologies Inc.
 * Chicago, USA
 * All rights reserved.
 * This software is the confidential and proprietary information
 * of Saven Technologies, Inc. You shall not disclose such 
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * Saven.
 */


package com.exsys.common.business;

import java.io.*;
import java.util.*;
import com.exsys.service.*;
/**
 *
 * <B>OrderManager</B> class is a utility class used by MatchEngine application.
 * This is not needed for Order Entry GUI application
 */

public class OrderManager
{
	private Hashtable buyList = new Hashtable();
	private Hashtable sellList = new Hashtable();
	private OrderComparator comp = new OrderComparator();
	private SortComparator scomp = new SortComparator();

	

	public OrderManager()
	{
	}

	public void AddOrder( Order ord )
	{
		Logger.debug("Adding ORD" + ord.toString() );
		String symbol = ord.getSymbol();
		boolean hasSymbol;
		boolean isBuy = (ord.getBuyOrSell()).equals(IBusinessObject.BUY );
		//TreeSet oldSet;

		Vector oldSet;

		if( isBuy)
		   hasSymbol = buyList.containsKey( symbol );
		else
		   hasSymbol = sellList.containsKey( symbol );

		if( hasSymbol )
		{

			if( isBuy )
			{
				oldSet = (Vector)buyList.get( symbol );
				Logger.debug("Orders are "+oldSet.size());
				try{
					boolean added = oldSet.add( ord );
					//Logger.debug( added );
				}catch( Exception exc )
				{
				   exc.printStackTrace();
				}

			}
			else
			{
				oldSet = (Vector)sellList.get( symbol );
				Logger.debug("Orders are "+oldSet.size());
				try{
					boolean added = oldSet.add( ord );
					//Logger.debug( added );
				}catch( Exception exc )
				{
				   exc.printStackTrace();
				}

			}

		}
		else
		{

			Vector orderSet = new Vector();
			orderSet.add( ord );
			if( isBuy )
			{
				buyList.put( symbol, orderSet );
			}
			else
			{
				sellList.put( symbol, orderSet );
			}

		}
	}

	//public Order GetOrder( String order_id )
	//{
	//}

	public void RemoveOrder(String order_id)
	{
	}

	public boolean CancelOrder( Order ord, Cancel cxl )
	{
		return( RemoveOrder( ord, cxl ) );
	}
	
	public boolean RemoveOrder( Order ord )
	{
		return( RemoveOrder(ord,null));
	}


	public boolean RemoveOrder( Order ord, Cancel cxl )
	{
		Logger.debug("Removing ORD" + ord.toString() );
		boolean removed = false;

		String symbol = ord.getSymbol();
		boolean hasSymbol;
		boolean isBuy = (ord.getBuyOrSell()).equals(IBusinessObject.BUY );

		if( isBuy)
		   hasSymbol = buyList.containsKey( symbol );
		else
		   hasSymbol = sellList.containsKey( symbol );

		if( hasSymbol )
		{

			if( isBuy )
			{

				((Vector)buyList.get( symbol )).toString();
	        try{
			
			//removed = ((Vector)buyList.get( symbol )).removeElement( ord );
			int index = ((Vector)buyList.get( symbol )).indexOf(ord);
			if(index != -1)
			{
				Order remOrd = (Order)((Vector)buyList.get( symbol )).remove(index);
				if(cxl != null)
					cxl.setOrderType(remOrd.getOrderType());
				removed = true;
			}
        		//Logger.debug(removed);
	        }catch(Exception exc)
        	{
	          exc.printStackTrace();
        	}

			}
			else
			{
				((Vector)sellList.get( symbol )).toString();
	        try{
  				//removed = ((Vector)sellList.get( symbol )).removeElement( ord );
			int index = ((Vector)sellList.get( symbol )).indexOf(ord);
			if(index != -1)
			{
				Order remOrd = (Order)((Vector)sellList.get( symbol )).remove(index);
				if(cxl != null)
					cxl.setOrderType(remOrd.getOrderType());
				removed = true;
			}
   				
        	  //Logger.debug(removed);
	        }catch(Exception exc)
        	{
	          exc.printStackTrace();
        	}
			}

		}
		else
		{
			// Later we may throw an exception
		}

		return removed;

	}
	public boolean ContainsMatchingOrders( Order ord )
	{
		boolean isBuy = (ord.getBuyOrSell()).equals(IBusinessObject.BUY);
		if( isBuy )
		{
			return( sellList.containsKey(ord.getSymbol()) );
		}
		else
		{
			return( buyList.containsKey(ord.getSymbol()) );
		}


	}

	public Vector GetMatchingOrders( Order ord )
	{
		Vector matchingOrders = new Vector();
		Iterator ordIterator;
		Vector orderSet;
		boolean isBuy = (ord.getBuyOrSell()).equals(IBusinessObject.BUY);
		if( isBuy )
		{
		   orderSet =  (Vector)(sellList.get(ord.getSymbol()));
		   try{

		   Collections.sort( (List)orderSet, scomp );

		   }catch(Exception exc)
		   {
			    //exc.printStackTrace();
          exc.printStackTrace();

	   	   }
		   ordIterator = orderSet.iterator();
		}
		else
		{
		   orderSet =  (Vector)(buyList.get(ord.getSymbol()));
		   try{

		   Collections.sort( (List)orderSet, scomp );

		   }catch(Exception exc)
		   {
			    //exc.printStackTrace();
          exc.printStackTrace();

       }

		   ordIterator = orderSet.iterator();

		}

		int qty = (ord.getQuantity()).intValue();
		int foundQty = 0;
		Double price = new Double( ord.getPrice() );

		for( ;ordIterator.hasNext() && foundQty < qty; )
		{
			Logger.debug("Found QTY is "+ foundQty);
			Order oppOrder = (Order)ordIterator.next();
			if( ( isBuy && price.compareTo( new Double(oppOrder.getPrice()))<= 0 )
			   || (!isBuy && price.compareTo( new Double(oppOrder.getPrice()))<= 0 ))
			{
				foundQty += (oppOrder.getQuantity()).intValue();
				matchingOrders.add( oppOrder );
				Logger.debug("Adding to MatchOrders List");
				Logger.debug(oppOrder.toString());
			}
			else
			{
				break;
			}

		}

		Logger.debug("Printing Matching Orders");
		matchingOrders.toString();
		return matchingOrders;

	}

	public void GetBestBidOffer( String symbol,RealTimeQuote quote )
	{
    Logger.debug("In GetBestBidOffer");

 	   Double bestBidPrice = new Double(0);
	   Integer bestBidQty = new Integer(0);
	   Double bestOfferPrice = new Double(0);
	   Integer bestOfferQty = new Integer(0);


		if( sellList.containsKey( symbol) )
		{
		   try{
         Vector sellOrders = (Vector)sellList.get(symbol);
	       for( Enumeration e=sellOrders.elements();e.hasMoreElements();)
	       {
        		Order sellOrd = (Order)e.nextElement();
        		Double sellPrice = new Double( sellOrd.getPrice() );
        		Integer sellQuantity = sellOrd.getQuantity();
        		if( bestOfferPrice.compareTo( new Double(0) ) == 0 )
        		{
        		   bestOfferPrice = sellPrice;
        		   bestOfferQty = sellQuantity;
        		}
        		else
        		{
		          if( bestOfferPrice.compareTo(sellPrice) == 0 )
      		   {
			          bestOfferQty = new Integer( bestOfferQty.intValue()+sellQuantity.intValue() );
      		   }
		         else if( bestOfferPrice.compareTo(sellPrice) > 0 )
      		   {
		           	bestOfferPrice = sellPrice;
        		   	bestOfferQty = sellQuantity;
      		   }
        		}
	      }

			  quote.setOfferQuantity( bestOfferQty );
			  quote.setOfferPrice( bestOfferPrice.toString() );

		   }
		   catch( Exception exc )
		   {
			    //Logger.debug( exc );
    			Logger.debug("Nothing in Sell List");
    			quote.setOfferQuantity( new Integer(0) );
		    	quote.setOfferPrice( new String("0") );

		    }
    }
    else
    {
			quote.setOfferQuantity( new Integer(0) );
			quote.setOfferPrice( new String("0") );

    }

		if( buyList.containsKey( symbol) )
		{
      try{
          Vector buyOrders = (Vector)buyList.get(symbol);
	        for( Enumeration e=buyOrders.elements();e.hasMoreElements();)
    	   {
		        Order buyOrd = (Order)e.nextElement();
        		Double buyPrice = new Double( buyOrd.getPrice() );
        		Integer buyQuantity = buyOrd.getQuantity();
        		if( bestBidPrice.compareTo( new Double(0) ) == 0 )
        		{
		            bestBidPrice = buyPrice;
          		   bestBidQty = buyQuantity;
        		}
        		else
        		{
		             if( bestBidPrice.compareTo(buyPrice) == 0 )
          		   {
              			bestBidQty = new Integer( bestBidQty.intValue()+buyQuantity.intValue() );
          		   }
          		   else if( bestBidPrice.compareTo(buyPrice) < 0 )
          		   {
            		   	bestBidPrice = buyPrice;
	            	   	bestBidQty = buyQuantity;
          		   }
          		}
      	   }

			    quote.setBidQuantity( bestBidQty );
    			quote.setBidPrice( bestBidPrice.toString() );

		   }
		   catch( Exception exc )
		   {
		      	//Logger.debug( exc );
      			Logger.debug("Nothing in Buy List");
			      quote.setBidQuantity( new Integer(0) );
      			quote.setBidPrice( new String("0") );

		   }

		}

  else
  {
			quote.setBidQuantity( new Integer(0) );
			quote.setBidPrice( new String("0") );

  }
}
}
