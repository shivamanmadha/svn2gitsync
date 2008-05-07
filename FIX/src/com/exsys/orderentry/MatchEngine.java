package com.exsys.orderentry;

import com.exsys.common.business.*;
import com.exsys.common.extrading.*;
import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.application.*;
import com.exsys.service.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.util.*;


import java.io.*;
import java.util.*;

public class MatchEngine extends BaseApplication implements ITradeResponseHandler
{
	private TradingSessionManager tsManager;

	private OrderManager ordManager;

	private String ACC_SUB;
	private String REJ_SUB;
	private String FILL_SUB;
	private String CONFIRM_SUB;
	private String QUOTE_SUB;
	private String mUniqueIDFile = null;
	private FixUniqueIdGen idGen = null;
	private String exchange = null;


	private Hashtable bestQuotes = new Hashtable();
	private FixMessageAdapter msgAdapter = null;


	int acc_response_id = 0;
	int conf_response_id = 0;

	int fill_id = 0;
	
	String quoteSub;
	
	public MatchEngine( String[] args ) throws ConfigFileNotFound
	{
		super(args);
		String querySub = null;
	    String reqSubSub=null;
 
  	  	try
   		{
    		reqSubSub = ConfigurationService.getValue(OrderEntryConstants.MATCHENGINE_REQ_SUB_SUB);
	    	Logger.debug("Req Receive Subject is " + reqSubSub );
    		ACC_SUB = ConfigurationService.getValue(OrderEntryConstants.MATCHENGINE_ACC_PUBLISH_PREFIX);
	    	Logger.debug("ACC Publish Prefix" + ACC_SUB );
    		REJ_SUB = ConfigurationService.getValue(OrderEntryConstants.MATCHENGINE_REJ_PUBLISH_PREFIX);
	    	Logger.debug("REJ Publish Prefix" + REJ_SUB );	    	
    		FILL_SUB = ConfigurationService.getValue(OrderEntryConstants.MATCHENGINE_FILL_PUBLISH_PREFIX);
	    	Logger.debug("FILL Publish Prefix" + FILL_SUB );	    	
    		CONFIRM_SUB = ConfigurationService.getValue(OrderEntryConstants.MATCHENGINE_CONF_PUBLISH_PREFIX);
	    	Logger.debug("CONFIRM Publish Prefix" + CONFIRM_SUB );	    	
    		QUOTE_SUB = ConfigurationService.getValue(OrderEntryConstants.MATCHENGINE_QUOTE_PUBLISH_PREFIX);
	    	Logger.debug("QUOTE Publish Prefix" + QUOTE_SUB );	    	
      		mUniqueIDFile = ConfigurationService.getValue("UniqueIDFile");
     		Logger.debug("UniqueIDFile is " + mUniqueIDFile );
     		idGen = FixUniqueIdGen.getInstance(mUniqueIDFile);
      		exchange = ConfigurationService.getValue("Exchange");
     		Logger.debug("Exchange is " + exchange );
     		
 
	    		
    	}
	    catch( ConfigAttributeNotFound exc )
 		{
	 	    Logger.debug("CONFIG VALUES DOES NOT EXIST");
	  	  	exc.printStackTrace();
	  	 	System.exit(0);	    
	     }
	    catch( Exception exc )
 		{
	 	    Logger.debug("CONFIG VALUES DOES NOT EXIST");
	  	  	exc.printStackTrace();
	  	 	System.exit(0);	    
	     }	     


		
		ordManager = new OrderManager();
		msgAdapter = new FixMessageAdapter(this);
		//String hostname = getCodeBase().getHost();
		try
		{
						
			tsManager = new TradingSessionManager();
			tsManager.startTradingSession();
			tsManager.receiveTradingMessages(msgAdapter,reqSubSub);

			while(true)
			{
				Logger.debug("Waiting for Orders");
				Thread.sleep(100000);
				
			}
			
			
		}
		catch( Exception exc )
		{
			exc.printStackTrace();
		}
		

	}
	public void AcceptOrder( Order ord, boolean isCXR )
	{
		try
		{
		Response resp = new Response( IBusinessObject.ACCEPT,ord );
		resp.setSubject(ACC_SUB+ord.getUser());
		resp.setResponseId(exchange+ "-ACC" + idGen.getNextExecId());
		resp.setOrderId(exchange+"-EXCH"+idGen.getNextOrderId());
		resp.setReason( "ACCEPTED" );
		resp.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		if(isCXR)
		{
			resp.setExecType(FixConstants.FIX_EXECUTIONREPORT_NEW);
			resp.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_REPLACED);			
		}
		else
		{
			resp.setExecType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
			resp.setOrderStatus(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		}
		
		//SendObject( resp );
		Logger.debug("Sending Accept - sub="+resp.getSubject());
		tsManager.sendAccept(resp, resp.getSubject());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		
	}
	public void AdjustQuote( String symbol, Double lastPrice, Integer lastQty,boolean publish )
	{
	   Double bestBidPrice = new Double(0);
	   Integer bestBidQty = new Integer(0);
	   Double bestOfferPrice = new Double(0);
	   Integer bestOfferQty = new Integer(0);

	   RealTimeQuote quote = new RealTimeQuote( IBusinessObject.QUOTE );
	   ordManager.GetBestBidOffer( symbol, quote );

	   bestBidQty = quote.getBidQuantity();
	   bestBidPrice = new Double(quote.getBidPrice());
	   bestOfferQty = quote.getOfferQuantity();
	   bestOfferPrice = new Double(quote.getOfferPrice());
	   

	   SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,publish);
	}
	public void ConfirmCancel( Cancel cxl )
	{
		/*
		Confirm conf = new Confirm();
		conf.setSubject(CONFIRM_SUB + cxl.getUser());
		conf.setCancelId( cxl.getCancelId() );
		conf.setOrderId( cxl.getTBCOrderId() );
		conf.setConfirmId( new Integer(++conf_response_id));
		//SendObject( conf );
		tsManager.sendCancelConfirm(conf, conf.getSubject());
		*/
		
		try
		{
		Response resp = new Response( IBusinessObject.CONFIRM, cxl );
		resp.setSubject(CONFIRM_SUB + cxl.getUser());
		//resp.setRequestId( cxl.getCancelId() );
		resp.setRequestId( cxl.getTBCOrderId() );
		resp.setResponseId( exchange+"-CXLCONF" + idGen.getNextExecId());
		//resp.setReason( reason );
		resp.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		resp.setExecType(FixConstants.FIX_EXECUTIONREPORT_CANCELED);
		resp.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_CANCELED);
		
		
		//SendObject( resp );
		tsManager.sendAccept(resp, resp.getSubject());		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		
		
	}
	//private String
	public static void main( String[] args )
	{
	  try
	  {
   		 MatchEngine me = new MatchEngine(args);
	  }
	  catch(Exception e )
	  {
		  e.printStackTrace();
	  }
		
	}
	public void MatchAndAdjustQuote( Order ord,Double bestBidPrice, Integer bestBidQty,
				  Double bestOfferPrice, Integer bestOfferQty,
				  Double lastPrice, Integer lastQty, String symbol,boolean publish )
	{
	  try
	  {
	   Fill fill = new Fill(IBusinessObject.FILL);
	   fill.setSubject( FILL_SUB );
	   //matchOrders.removeAll();
	   Double matchPrice = new Double( ord.getPrice() );
	   Integer qty = ord.getQuantity();
	   int matchQty = qty.intValue();
	   int remQty = matchQty;
	   int filledQty = 0;

	   Vector matchOrders = ordManager.GetMatchingOrders( ord );

	   if( matchOrders.size() == 0 )
	   {
		// nothing to match
		Logger.debug("Nothing to match");
		AdjustQuote( symbol,lastPrice,lastQty,publish );
	   }
	   else
	   {
		 Logger.debug("Orders From Matching List");

	      for( Enumeration e = matchOrders.elements(); e.hasMoreElements();)
	      {
		Order oppOrd = (Order) e.nextElement();
		 Logger.debug(oppOrd.toString());
		Integer oqty = oppOrd.getQuantity();
		int oppQty = oqty.intValue();
		Double oppPrice = new Double( oppOrd.getPrice() );
		if( remQty > oppQty )
		{
		   // Generate Partial Fill for currentORD, FILL for oppORD
		   //Fill for oppORD
		   fill.setRequestId( oppOrd.getOrderId() );
		   fill.setResponseId(exchange+"-FILL" + idGen.getNextExecId());
		   fill.setQuantity( oqty );
		   fill.setLeavesQuantity( new Integer(0) );
		   fill.setTradePrice( oppOrd.getPrice() );
		   fill.setSubject( FILL_SUB + oppOrd.getUser() );
		   fill.setOppositeBroker( ord.getUser() );
		   
		   fill.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		   fill.setExecType(FixConstants.FIX_EXECUTIONREPORT_FILL);
		   fill.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_FILL);		   
		   
		   fill.setOrder(oppOrd);
		   //SendObject( fill );
		   tsManager.sendFill(fill, fill.getSubject());
		   ordManager.RemoveOrder( oppOrd );
		   

		   //PARTIAL Fill for currentORD
		   fill.setType(IBusinessObject.PARTIAL);
		   fill.setRequestId( ord.getOrderId() );
		   fill.setResponseId(exchange+"-PARTIAL" + idGen.getNextExecId());
		   fill.setQuantity( oqty );
		   fill.setLeavesQuantity( new Integer(matchQty-oppQty) );
		   fill.setTradePrice( oppOrd.getPrice() );
		   fill.setSubject( FILL_SUB + ord.getUser() );
		   fill.setOppositeBroker( oppOrd.getUser() );
		   fill.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		   fill.setExecType(FixConstants.FIX_EXECUTIONREPORT_PARTIAL);
		   fill.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_PARTIAL);		   
		   fill.setOrder(ord);
		   //SendObject( fill );
		   tsManager.sendFill( fill, fill.getSubject());

		  filledQty += oppQty;
		  remQty -= oppQty;

		   
		   lastPrice = oppPrice;
		   lastQty = oqty;
		   ord.setQuantity(new Integer(remQty - oppQty));
		   
		   ord.setOpenQuantity(new Integer(remQty - oppQty));
		   SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,publish);
		   
			
		}
		else if( remQty == oppQty )
		{
		   // Generate FILLs for both bid offer
		   //Fill for offer
		   fill.setRequestId( oppOrd.getOrderId() );
		   fill.setResponseId(exchange+"-FILL" + idGen.getNextExecId());
		   fill.setQuantity( oqty );
		   fill.setLeavesQuantity( new Integer(0) );
		   fill.setTradePrice( oppOrd.getPrice() );
		   fill.setSubject( FILL_SUB + oppOrd.getUser() );
		   fill.setOppositeBroker( ord.getUser() );
		   fill.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		   fill.setExecType(FixConstants.FIX_EXECUTIONREPORT_FILL);
		   fill.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_FILL);		   
		   
		   fill.setOrder(oppOrd);
		   //SendObject( fill );
		   tsManager.sendFill( fill, fill.getSubject());		   
		   ordManager.RemoveOrder( oppOrd );

		   // Fill for bid
		   fill.setRequestId( ord.getOrderId() );
		   fill.setResponseId(exchange+"-FILL" + idGen.getNextExecId());
		   fill.setQuantity( oqty );
		   fill.setLeavesQuantity( new Integer(0) );
		   fill.setTradePrice( oppOrd.getPrice() );
		   fill.setSubject( FILL_SUB + ord.getUser() );
		   fill.setOppositeBroker( oppOrd.getUser() );
		   fill.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		   fill.setExecType(FixConstants.FIX_EXECUTIONREPORT_FILL);
		   fill.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_FILL);		   
		   
		   fill.setOrder(ord);
		   tsManager.sendFill( fill, fill.getSubject());		   
		   //SendObject( fill );
		   lastPrice = oppPrice;
		   lastQty = oqty;
		   //ordManager.RemoveOrder( ord );
		   SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,publish);
		   filledQty += oppQty;
		   remQty -= oppQty;



		   break;

		}
		else
		{
		   // Generate FILL for curr ord, partial for opp ord
		   // Fill for bid
		   fill.setRequestId( ord.getOrderId() );
		   fill.setResponseId(exchange+"-FILL" + idGen.getNextExecId());
		   //fill.setQuantity( new Integer(remQty) );
		   fill.setQuantity( oqty );
		   fill.setLeavesQuantity( new Integer(0) );
		   fill.setTradePrice( oppOrd.getPrice() );
		   fill.setSubject( FILL_SUB + ord.getUser() );
		   fill.setOppositeBroker( oppOrd.getUser() );
		   fill.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		   fill.setExecType(FixConstants.FIX_EXECUTIONREPORT_FILL);
		   fill.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_FILL);		   
		   fill.setOrder(ord);
		   //buyOrders.remove( ord );
		   //SendObject( fill );
		   tsManager.sendFill( fill, fill.getSubject());		   

			// PARTIAL for sell
		   fill.setType( IBusinessObject.PARTIAL);
		   fill.setRequestId( oppOrd.getOrderId() );
		   fill.setResponseId(exchange+"-PARTIAL" + idGen.getNextExecId());
		   //fill.setQuantity( new Integer( remQty ) );
		   fill.setQuantity( oqty );
		   fill.setLeavesQuantity( new Integer(oppQty-remQty) );
		   fill.setTradePrice( oppOrd.getPrice() );
		   fill.setSubject( FILL_SUB + oppOrd.getUser() );
		   fill.setOppositeBroker( ord.getUser() );
		   fill.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		   fill.setExecType(FixConstants.FIX_EXECUTIONREPORT_PARTIAL);
		   fill.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_PARTIAL);		   
		   fill.setOrder(oppOrd);
		   //SendObject( fill );
		   tsManager.sendFill( fill, fill.getSubject());		   
		   oppOrd.setQuantity(new Integer(oppQty-remQty));
		   oppOrd.setOpenQuantity(new Integer(oppQty-remQty));
		   lastPrice = oppPrice;
		   lastQty = new Integer(remQty);
		   SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,true);
		   filledQty += remQty;
		   remQty = 0;


		   break;

		}
		
	      }
		if( remQty > 0 )
		{
			ord.setQuantity( new Integer(remQty) );
			ordManager.AddOrder( ord );
		}

		//matchOrders.removeAllElements();
		AdjustQuote( symbol,lastPrice,lastQty,publish );
			
	   }
	
	  }
	  catch (Exception e )
	  {
	  	e.printStackTrace();
	  }
	  
	}
	public boolean processCancel( Cancel cxl, boolean publish )
	{
		//Construct order object to be cancelled
		Order ord = new Order();
		//ord.setOrderId( cxl.getTBCOrderId() );
		ord.setClientOrderId( cxl.getTBCOrderId() );
		ord.setSymbol( cxl.getSymbol() );		
		ord.setBuyOrSell( cxl.getBuyOrSell() );

		boolean cancelled = ordManager.CancelOrder( ord,cxl );
		if( cancelled )
		{
			// Adjust quote
			 Double lastPrice;
			 Integer lastQty;
			 RealTimeQuote bestQuote;
			 String symbol = ord.getSymbol();
			 boolean hasQuote = bestQuotes.containsKey( cxl.getSymbol() );
			 if( hasQuote )
			 {
			   bestQuote = (RealTimeQuote)bestQuotes.get( symbol );
			   lastPrice = new Double( bestQuote.getLastPrice());
			   lastQty = bestQuote.getLastQuantity();
			   AdjustQuote( cxl.getSymbol(), lastPrice, lastQty, publish );

			 }
		}
		return cancelled;
	}
	public void ReceivedAccept( Response resp )
	{
		Logger.debug("Received Accept");
		resp.print();

	}
	public void ReceivedCancel( Cancel cxl )
	{
		//Construct order object to be cancelled
		Order ord = new Order();
		ord.setOrderId( cxl.getTBCOrderId() );
		ord.setSymbol( cxl.getSymbol() );
		ord.setBuyOrSell( cxl.getBuyOrSell() );

		boolean cancelled = processCancel(cxl, true);
		if( cancelled )
		{
			ConfirmCancel( cxl );

			
		}
		else
		{
			RejectCancel( cxl, "TOO LATE TO CANCEL", true);

		}
	}
	public void ReceivedCancelReplace( CancelReplace cxr)
	{
		// This will just simulate CXR as CXL and ADD
		Cancel cxl = cxr.getCancel();
		Order ord = cxr.getNewOrder();		
		
		boolean cancelled = processCancel( cxl, false );
		if( cancelled )
		{
			// now add new order
			// if this fails, do we need to do different
			// than ReceovedOrder does ???
			ReceivedOrder( ord,true );
		}
		else
		{
			// reject cancel replace
			RejectCancel( cxl, "TOO LATE TO CANCEL", false);
		}
		

	}
	public void ReceivedConfirm( Confirm conf )
	{
		Logger.debug("Received Confirm");
		conf.print();

	}
	public void ReceivedFill( Fill fill)
	{
		Logger.debug("Received Fill");
		fill.print();

	}
	public void ReceivedOrder( Order ord )
	{
		ReceivedOrder(ord,false);
	}
	public void ReceivedOrder( Order ord, boolean isCXR )
	{

	
	 Double bestBidPrice;
	 Double bestOfferPrice;
	 Double lastPrice;
	 Integer bestBidQty;
	 Integer bestOfferQty;
	 Integer lastQty;
	 RealTimeQuote bestQuote;
	 String symbol = ord.getSymbol();
	 boolean hasQuote = bestQuotes.containsKey( symbol );
	 if( hasQuote )
	 {
	   bestQuote = (RealTimeQuote)bestQuotes.get( symbol );
	   bestBidPrice = new Double( bestQuote.getBidPrice());
	   bestOfferPrice = new Double(bestQuote.getOfferPrice());
	   lastPrice = new Double( bestQuote.getLastPrice());
	   bestBidQty = bestQuote.getBidQuantity();
	   bestOfferQty = bestQuote.getOfferQuantity();
	   lastQty = bestQuote.getLastQuantity();
	   Logger.debug("Best Quote is " + bestQuote.toString());

	 }
	 else
	 {
	   bestBidPrice = new Double(0);
	   bestOfferPrice = new Double(0);
	   lastPrice = new Double(0);
	   bestBidQty = new Integer(0);
	   bestOfferQty = new Integer(0);
	   lastQty = new Integer(0);

	 }
	
	
		Logger.debug("In ReceivedOrder:");
		ord.print();
		String orderType = ord.getOrderType();
		if( !orderType.equals(IBusinessObject.LMT) )
		{
			// Reject the order
			String reason = "INVALID ORDER TYPE";
			RejectOrder( ord, reason );
		}
		else
		{
			// Accept Order
			AcceptOrder( ord, isCXR );
			// Check if this is the best bid/offer
			boolean isBuy = (ord.getBuyOrSell()).equals(IBusinessObject.BUY);
			Double price = new Double( ord.getPrice() );
			Integer qty = ord.getQuantity();
			if( isBuy )
			{
			  if( ( bestBidPrice.compareTo(new Double(0)) != 0 ) &&
			      ( bestBidPrice.compareTo( price ) > 0 ) ) 
			  {
				  Logger.debug("Not better ");
				// This is not the best bid, and no chance of getting filled
				// Just add into the vector
				ordManager.AddOrder( ord );
			  }
			  else
			  {
			    if( ( bestBidPrice.compareTo(new Double(0)) == 0 ) ||
				( bestBidPrice.compareTo( price ) < 0 ) )
			    {
				// This is the best bid
				Logger.debug("This is a better bid");
				if( ( bestOfferPrice.compareTo( new Double(0) ) != 0 ) &&
				    ( bestOfferPrice.compareTo( price ) <= 0 ) )
				{
				  // Match Orders and generate new bids offers
				  MatchAndAdjustQuote( ord,bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,true );
				}
				else
				{
				  bestBidPrice = price;
				  bestBidQty = qty;
				  ordManager.AddOrder( ord );
				  SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,true);
				   	
				}	
			    }
			    else
			    {
				if( bestBidPrice.compareTo( price ) == 0 )
				{
				  bestBidQty = new Integer(bestBidQty.intValue() + qty.intValue() );
				  ordManager.AddOrder( ord );
				  SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,true);
			        }
			    }
			  }
			}
			else
			{
			  if( ( bestOfferPrice.compareTo(new Double(0)) != 0 ) &&
			      ( bestOfferPrice.compareTo( price ) < 0 ) )
			  {
				// This is not the best offer, and no chance of getting filled
				// Just add into the vector
				ordManager.AddOrder( ord );
			  }
			  else
			  {
			    if( ( bestOfferPrice.compareTo(new Double(0)) == 0 ) ||
				( bestOfferPrice.compareTo( price ) > 0 ) )
			    {
				// This is the best offer
				if( ( bestBidPrice.compareTo( new Double(0) ) != 0 ) &&
				    ( bestBidPrice.compareTo( price ) >= 0 ) )
				{
				  // Match Orders and generate new bids offers
				  MatchAndAdjustQuote( ord,bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,true );
				}
				else
				{
				  bestOfferPrice = price;
				  bestOfferQty = qty;
				  ordManager.AddOrder( ord );
				  SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,true);
				   	
				}	
			    }
			    else
			    {
				if( bestOfferPrice.compareTo( price ) == 0 )
				{
				  bestOfferQty = new Integer( bestOfferQty.intValue() + qty.intValue() );
				  ordManager.AddOrder( ord );
				  SendNewQuote(bestBidPrice,bestBidQty,bestOfferPrice,bestOfferQty,lastPrice,lastQty,symbol,true);
			        }
			    }
			  }

			}
			
		}
	}
	public void ReceivedPartialFill( Fill fill )
	{
		Logger.debug("Received Fill");
		fill.print();

	}
	public void ReceivedQuote( RealTimeQuote quote )
	{
		Logger.debug("Received Quote");
		quote.print();
	}
	public RealTimeQuote ReceivedQuoteRequest()
	{
		RealTimeQuote quote = new RealTimeQuote(IBusinessObject.QUOTE);
		Logger.debug("Received Quote Request");
		return quote;
	}
	public void ReceivedReject( Response resp )
	{
		Logger.debug("Received Reject");
		resp.print();

	}
	public void ReceivedStatusRequest()
	{
		Logger.debug("Received StatusRequest");

	}
	public void RejectCancel( Cancel cxl, String reason, boolean isCxl )
	{
		try
		{
		FixCancelReject rej = new FixCancelReject();
		rej.setAccount(cxl.getAccount());
		rej.setClOrderID(cxl.getCancelId());
		rej.setExecID(exchange+(isCxl?"-REJCXL":"-REJCXR") + idGen.getNextExecId());	
		rej.setOrderID(cxl.getExchangeOrderId());
		rej.setOrdStatus(FixConstants.FIX_CANCEL_REJ_ORDER_STATUS_NOTFOUND);
		rej.setOrigClOrderID(cxl.getTBCOrderId());
		rej.setText(reason);
		rej.setTransactTime(FixMessage.getUTCCurrentTime());
		//rej.setCxlRejReason(); not required
		rej.setCxlRejResponseTo(isCxl?
							FixConstants.FIX_CANCEL_REJ_RESPONSETO_CANCEL
							:FixConstants.FIX_CANCEL_REJ_RESPONSETO_REPLACE);
		rej.setCorrelationClOrdID(cxl.getCorrClOrderId());							
			
		tsManager.sendFixMessage(rej, REJ_SUB + cxl.getUser());		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		
	}
	
	/* NOT NEEDED - use RejectCancel
	public void RejectCancelReplace( CancelReplace cxr, String reason )
	{
		try
		{
		Response resp = new Response( IBusinessObject.REJECT,cxr.getCancel() );
		resp.setSubject(REJ_SUB + cxr.getUser());
		resp.setRequestId( cxr.getCancelId() );
		resp.setResponseId("REJCXR" + idGen.getNextExecId());
		resp.setReason( reason );
		resp.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		resp.setExecType(FixConstants.FIX_EXECUTIONREPORT_REJECTED);
		resp.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_REJECTED);
		//SendObject( resp );
		tsManager.sendReject(resp, resp.getSubject());	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
			
		
	}
	*/
	public void RejectOrder( Order ord, String reason )
	{
		
		try
		{
		Response resp = new Response( IBusinessObject.REJECT, ord );
		resp.setSubject(REJ_SUB+ord.getUser());
		resp.setResponseId(exchange+"-REJORD" + idGen.getNextExecId());
		resp.setOrderId(exchange+"-EXCH"+idGen.getNextOrderId());
		resp.setReason( reason );
		resp.setExecTransType(FixConstants.FIX_EXEC_TRANSTYPE_NEW);
		
		resp.setExecType(FixConstants.FIX_EXECUTIONREPORT_REJECTED);
		resp.setOrderStatus(FixConstants.FIX_EXECUTIONREPORT_REJECTED);
		
		
		//SendObject( resp );
		tsManager.sendReject(resp, resp.getSubject());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		
		
		
	}
	public void SendNewQuote( Double bestBidPrice, Integer bestBidQty,
				  Double bestOfferPrice, Integer bestOfferQty,
				  Double lastPrice, Integer lastQty, String symbol, boolean publish )
	{
		RealTimeQuote quote = new RealTimeQuote( IBusinessObject.QUOTE );
		quote.setBidPrice( bestBidPrice.toString() );
		quote.setBidQuantity( bestBidQty );
		quote.setOfferPrice( bestOfferPrice.toString() );
		quote.setOfferQuantity( bestOfferQty );
		quote.setLastPrice( lastPrice.toString() );
		quote.setLastQuantity( lastQty );
		quote.setSymbol( symbol );
		
		quote.setSubject( QUOTE_SUB + symbol );

		 boolean hasQuote = bestQuotes.containsKey( symbol );
		 if( hasQuote )
	 	 {
			RealTimeQuote bestQuote = (RealTimeQuote)bestQuotes.get(symbol);
			//bestQuote = quote;
			bestQuotes.remove(symbol);
			bestQuotes.put(symbol,quote);
		 }
		 else
		 {
			bestQuotes.put(symbol,quote);
		 }
		
		
		quote.print();
		
		if( publish )
		{
			Logger.debug("Sending Quote: Subject: " + QUOTE_SUB+symbol );
			//SendObject( quote );
			tsManager.sendQuote(quote, quote.getSubject());
		}
		


	}
	public void SendObject( IBusinessObject obj )
	{
		// Implement sending through trading session manager
		/*
			if( reqManager != null )
				reqManager.SendObject( obj );
		*/
	
	}
}
