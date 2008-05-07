package com.exsys.orderentry;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

import com.exsys.application.*;
import com.exsys.service.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.common.business.*;
import com.exsys.impact.message.*;
import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;
import com.exsys.orderentry.ui.*;
import com.exsys.common.mktdata.*;


public class ICEImpactFullMktDataWindow
  extends BaseGUIApplication
  implements ImpactMessageProcessor,  ActionListener, MarketDataNotifier
{
	private final static String UPDATE = "update";

	private class SymbolInfo
	{
		public String symbol = null;
		public String description = null;
		public SymbolInfo( String s, String d)
		{
			symbol = s;
			description = d;
		}
		public String toString()
		{
			return (symbol+"("+description+")");
		}
	}
  // ========================================================================
  // ========================================================================
  public ICEImpactFullMktDataWindow(String args[]) throws ConfigFileNotFound, Exception
  {
    super(args);
      processConfig();
      initializeSession(); // initialize jms session -- do this first...
      createWindows();
      //restoreFromFix();

      mTheFrame = new JFrame("ICE Full MktData Window");
        mTheFrame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
      mTheFrame.setContentPane(this);
      mTheFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      mTheFrame.pack();
      mTheFrame.setVisible(true);
		//restoreFromFix();
	 //test();
  }
  /*
  // ========================================================================
  // ========================================================================
  public boolean processMarketDataIncrementalRefresh(FixMarketDataIncrementalRefresh msg)
  {
  	return processMarketDataIncrementalRefresh( msg, null);
  }
  public boolean processMarketDataIncrementalRefresh(FixMarketDataIncrementalRefresh msg, String restoreSymbol)
  {
	//Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());
	System.out.println("FIX SEQ # :" + msg.getMsgSeqNumAsString());

	if(perfMode)
	{
		mdHandler.processIncrementalRefresh(msg,restoreSymbol);
    	return true;
	}

    Logger.debug("processMarketDataIncrementalRefresh");

    String str = " MDReq ID " +  msg.getMDReqID() ;
    str += " Number of MDEntries = " + msg.getNoMDEntries();
    mListModel.addElement("<BEGIN>" + str);
    if(msg.getNoMDEntries() > 0 )
    {
    	mdHandler.processIncrementalRefresh(msg,restoreSymbol);


   	 	// add details
   	 	String key = "268";
  		ArrayList rgArray = msg.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {

  				FixRGNoMDEntries_X rg = (FixRGNoMDEntries_X)rgArray.get(i);
  				String info = "       Symbol =" + rg.getSymbol();
  				    if(restoreSymbol != null && !rg.getSymbol().equals(restoreSymbol)) continue;
  				info += " MDEntryType = "+rg.getMDEntryType();
				info += "(";
  				info += FixLookup.lookupTagValue("269",rg.getMDEntryType());
		  		info += ")";
  				info += " MDEntryPx ="+rg.getMDEntryPxAsString();
  				info += " MDEntrySize ="+rg.getMDEntrySizeAsString();
  				info += " TotalVolume ="+rg.getTotalVolumeTradedAsString();
  				info += " MDUpdateAction ="+rg.getMDUpdateAction();
				info += "(";
  				info += FixLookup.lookupTagValue("279",rg.getMDUpdateAction());
		  		info += ")";

  				mListModel.addElement(info);

  				String entryType = rg.getMDEntryType();
  				if(entryType.equals("0") ||
	  				entryType.equals("1") ||
	  				entryType.equals("2") ||
	  				entryType.equals("U") )
	  			{
  					// update book tab
		    		updateBookTab(rg.getSymbol());

		    		if(entryType.equals("0"))
		    		{
		    			updateBidsTab(rg.getSymbol());
		    		}
		    		else if(entryType.equals("1"))
		    		{
		    			updateOffersTab(rg.getSymbol());
		    		}
		    		else if(entryType.equals("U"))
		    		{
		    			updateBidsTab(rg.getSymbol());
		    			updateOffersTab(rg.getSymbol());

		    		}
		    		else if(entryType.equals("2"))
		    		{
		    			updateTradesTab(rg.getSymbol());
		    			updateBidsTab(rg.getSymbol());
		    			updateOffersTab(rg.getSymbol());
		    		}

	  			}
   			}
   			mListModel.addElement("<END>" + str);


  		}

    }

	return true;
   }
   */
public boolean processMarketSnapshot(MarketSnapshotMessage msg)
{
	return processMarketSnapshot(msg,  null);
}
public boolean processMarketSnapshot(MarketSnapshotMessage msg, String restoreSymbol)
{

	mListModel.addElement("MARKET SNAPSHOT");
    Logger.debug("processMarketSnapshot");

    String str = " MDReq ID " +  msg.RequestSeqID ;
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    mListModel.addElement("<BEGIN>" + str);


    	if(!mdHandler.containsSymbol(symbol))
    	{
    		cbSymbols.addItem(new SymbolInfo(symbol,sdRespHandler.getDescription(symbol)));
    		cbqSymbols.addItem(symbol);
    		if(currentSymbol == null)
    		{
    			currentSymbol = symbol;
    		}
    	}

    	mdHandler.processMarketSnapshot(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}
public boolean processMarketSnapshotOrder(MarketSnapshotOrderMessage msg)
{
	return processMarketSnapshotOrder(msg,null);
}
public boolean processMarketSnapshotOrder(MarketSnapshotOrderMessage msg, String restoreSymbol)
{

	//mListModel.addElement("MARKET SNAPSHOT ORDER");
    Logger.debug("processMarketSnapshotOrder");

    String str = " MDReq ID " +  msg.RequestSeqID ;
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    //mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processMarketSnapshotOrder(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}

public boolean processAddModifyOrder(AddModifyOrderMessage msg)
{
	return processAddModifyOrder(msg,null);
}
public boolean processAddModifyOrder(AddModifyOrderMessage msg, String restoreSymbol)
{

	//mListModel.addElement("ADD MODIFY ORDER");
    Logger.debug("processAddModifyOrder");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    //mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processAddModifyOrder(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}

public boolean processDeleteOrder(DeleteOrderMessage msg)
{
	return processDeleteOrder(msg,null);
}
public boolean processDeleteOrder(DeleteOrderMessage msg, String restoreSymbol)
{

	//mListModel.addElement("DELETE ORDER");
    Logger.debug("processDeleteOrder");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
   // mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processDeleteOrder(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}

public boolean processTrade(TradeMessage msg)
{
	return processTrade(msg,null);
}
public boolean processTrade(TradeMessage msg, String restoreSymbol)
{

	mListModel.addElement("TRADE MESSAGE");
    Logger.debug("processTrade");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processTrade(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}
public boolean processCancelledTrade(CancelledTradeMessage msg)
{
	return processCancelledTrade(msg,null);
}
public boolean processCancelledTrade(CancelledTradeMessage msg, String restoreSymbol)
{

	mListModel.addElement("CANCELLED TRADE MESSAGE");
    Logger.debug("processCancelledTrade");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processCancelledTrade(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}
public boolean processMarketStatistics(MarketStatisticsMessage msg)
{
	return processMarketStatistics(msg,null);
}
public boolean processMarketStatistics(MarketStatisticsMessage msg, String restoreSymbol)
{

	//mListModel.addElement("STATS MESSAGE");
    Logger.debug("processMarketStatistics");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    //mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processMarketStatistics(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}

public boolean processOpenPrice(OpenPriceMessage msg)
{
	return processOpenPrice(msg,null);
}
public boolean processOpenPrice(OpenPriceMessage msg, String restoreSymbol)
{

	mListModel.addElement("OPEN PRICE MESSAGE");
    Logger.debug("processOpenPrice");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processOpenPrice(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}

public boolean processOpenInterest(OpenInterestMessage msg)
{
	return processOpenInterest(msg,null);
}
public boolean processOpenInterest(OpenInterestMessage msg, String restoreSymbol)
{

	mListModel.addElement("OPEN INTEREST MESSAGE");
    Logger.debug("processOpenInterest");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processOpenInterest(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}

public boolean processSettlementPrice(SettlementPriceMessage msg)
{
	return processSettlementPrice(msg,null);
}
public boolean processSettlementPrice(SettlementPriceMessage msg, String restoreSymbol)
{

	mListModel.addElement("SETTLEMENT PRICE MESSAGE");
    Logger.debug("processSettlementPrice");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    //str += " Number of MDEntries = " + msg.getNoMDEntries();
    mListModel.addElement("<BEGIN>" + str);

    	mdHandler.processSettlementPrice(symbol,msg);

    	if(perfMode) return true;

    	// update book tab
    	updateBookTab(symbol);
		updateTradesTab(symbol);
		updateBidsTab(symbol);
		updateOffersTab(symbol);
    

	return true;
}
public boolean processMarketStateChange(MarketStateChangeMessage msg)
{
	return processMarketStateChange(msg,null);
}
public boolean processMarketStateChange(MarketStateChangeMessage msg, String restoreSymbol)
{

	mListModel.addElement("MARKET STATE CHANGE MESSAGE");
    Logger.debug("processMarketStateChange");

    String str = " ";
    String symbol = String.valueOf(msg.MarketID);
    if(restoreSymbol != null && !symbol.equals(restoreSymbol)) return false;
    str +=  "       Symbol =" + symbol;
    str += " TradeStatus = " + String.valueOf(msg.TradingStatus);
    mListModel.addElement("<BEGIN>" + str);
    
	return true;
}

private synchronized void updateBookTab(String symbol)
{
	/*
	if(!symbol.equals(currentSymbol))
		return;


	BookRow[] bidRows = mdHandler.getBidBook(symbol);
	BookRow[] offerRows = mdHandler.getOfferBook(symbol);

	mBookListModel.clear();
	// add summary and last trade

	mBookListModel.addElement("Symbol = " + symbol+"("+sdRespHandler.getDescription(symbol)+")");

	MarketDataTrade lastTrade = mdHandler.getLastTrade(symbol);
	if(lastTrade != null)
	{
		mBookListModel.addElement(lastTrade.toBookString());
	}
	else
	{
		mBookListModel.addElement("No LastTrade for symbol = "+ symbol);
	}


	QuoteData summary = mdHandler.getSummary(symbol);
	if(summary != null)
	{

		mBookListModel.addElement("Trading Session Summary - ");
		mBookListModel.addElement("HIGH - " +   summary.highPrice);
		mBookListModel.addElement("LOW - " + summary.lowPrice);
		mBookListModel.addElement("OPEN - " + summary.openPrice);
		mBookListModel.addElement("CLOSE - " + summary.closePrice);
		mBookListModel.addElement("Volume - "+ summary.volumeSize);
		mBookListModel.addElement("Open Interest - "+ summary.openInterest);
		mBookListModel.addElement("VWAP - " + summary.vwapPrice);
		mBookListModel.addElement("Settlement Price - " + summary.settlementPrice);
	}
	else
	{
		mBookListModel.addElement("No details for symbol = "+ symbol);
	}


	if(bidRows != null)
	{
		mBookListModel.addElement("bids for symbol = "+ symbol);
		for(int i=0;i<bidRows.length;i++)
		{
			if(bidRows[i] != null)
			mBookListModel.addElement(bidRows[i].toBookString());
		}
	}
	else
	{
		mBookListModel.addElement("No active bids for symbol = "+ symbol);
	}
	if(offerRows != null)
	{
		mBookListModel.addElement("offers for symbol = "+ symbol);
		for(int i=0;i<offerRows.length;i++)
		{
			if(offerRows[i] != null)
			mBookListModel.addElement(offerRows[i].toBookString());
		}
	}
	else
	{
		mBookListModel.addElement("No active offers for symbol = "+ symbol);
	}
*/
}

private synchronized void updateFullBookTab(String symbol)
{

	if(!symbol.equals(currentSymbol))
		return;

	ArrayList bidRows = mdHandler.getBidBook(symbol);
	ArrayList offerRows = mdHandler.getOfferBook(symbol);


	mBookListModel.clear();
	// add summary and last trade

	mBookListModel.addElement("Symbol = " + symbol+"("+sdRespHandler.getDescription(symbol)+")");

	MarketDataTrade lastTrade = mdHandler.getLastTrade(symbol);
	if(lastTrade != null)
	{
		mBookListModel.addElement(lastTrade.toBookString());
		lastTrade(symbol,lastTrade,false);
	}
	else
	{
		mBookListModel.addElement("No LastTrade for symbol = "+ symbol);
	}


	QuoteData summary = mdHandler.getSummary(symbol);
	if(summary != null)
	{

		mBookListModel.addElement("Trading Session Summary - ");
		mBookListModel.addElement("HIGH - " +   summary.highPrice);
		highPrice(symbol,summary.highPrice,false);
		mBookListModel.addElement("LOW - " + summary.lowPrice);
		lowPrice(symbol,summary.lowPrice,false);
		mBookListModel.addElement("OPEN - " + summary.openPrice);
		openPrice(symbol,summary.openPrice,false);
		mBookListModel.addElement("CLOSE - " + summary.closePrice);
		closePrice(symbol,summary.closePrice,false);
		mBookListModel.addElement("Volume - "+ summary.volumeSize);
		volume(symbol,summary.volumeSize,false);
		mBookListModel.addElement("Open Interest - "+ summary.openInterest);
		openInterest(symbol,summary.openInterest,false);
		mBookListModel.addElement("VWAP - " + summary.vwapPrice);
		settlementPrice(symbol,summary.vwapPrice,false);
		mBookListModel.addElement("Settlement Price - " + summary.settlementPrice);
		settlementPrice(symbol,summary.settlementPrice,false);
	}
	else
	{
		mBookListModel.addElement("No details for symbol = "+ symbol);
	}


	if(bidRows != null)
	{
		mBookListModel.addElement("BidBook for Symbol = " + symbol);
		//mBookListModel.addElement(BookRow.getHeader());
		for(int i=0;i<bidRows.size();i++)
		{
			if(i==0)
			{
				bestBid(symbol,(BookRow)bidRows.get(i),false);
			}
			mBookListModel.addElement(((BookRow)bidRows.get(i)).toBookString());
		}
	}
	else
	{
		mBookListModel.addElement("No active bid book for symbol = "+ symbol);
	}

	if(offerRows != null)
	{
		mBookListModel.addElement("OfferBook for Symbol = " + symbol);
		//mBookListModel.addElement(BookRow.getHeader());
		for(int i=0;i<offerRows.size();i++)
		{
			if(i==0)
			{
				bestOffer(symbol,(BookRow)offerRows.get(i),false);
			}
			mBookListModel.addElement(((BookRow)offerRows.get(i)).toBookString());
		}
	}
	else
	{
		mBookListModel.addElement("No active offer book for symbol = "+ symbol);
	}


}

private synchronized void updateBidsTab(String symbol)
{
	if(!symbol.equals(currentSymbol))
		return;

	HashMap bids = mdHandler.getBids(symbol);
	mBidsListModel.clear();
	mBidsListModel.addElement("Symbol = " + symbol+"("+sdRespHandler.getDescription(symbol)+")");
	if(bids != null)
	{
		Iterator keys = bids.keySet().iterator();
		while( keys.hasNext() )
		{
			String id = (String) keys.next();
			mBidsListModel.addElement(((MarketDataBid)bids.get(id)).toBookString());
		}

	}
	else
	{
		mBidsListModel.addElement("No active bids for symbol = "+ symbol);
	}
}

private synchronized void updateOffersTab(String symbol)
{
	if(!symbol.equals(currentSymbol))
		return;

	HashMap offers = mdHandler.getOffers(symbol);
	mOffersListModel.clear();

	mOffersListModel.addElement("Symbol = " + symbol+"("+sdRespHandler.getDescription(symbol)+")");
	if(offers != null)
	{
		Iterator keys = offers.keySet().iterator();
		while( keys.hasNext() )
		{
			String id = (String) keys.next();
			mOffersListModel.addElement(((MarketDataOffer)offers.get(id)).toBookString());
		}

	}
	else
	{
		mOffersListModel.addElement("No active offers for symbol = "+ symbol);
	}
}

private synchronized void updateTradesTab(String symbol)
{
	if(!symbol.equals(currentSymbol))
		return;

	Logger.debug("UpdateTradesTab");
	HashMap trades = mdHandler.getTrades(symbol);
	mTradesListModel.clear();

	mTradesListModel.addElement("Symbol = " + symbol+"("+sdRespHandler.getDescription(symbol)+")");
	if(trades != null)
	{
		Iterator keys = trades.keySet().iterator();
		while( keys.hasNext() )
		{
			String id = (String) keys.next();
			mTradesListModel.addElement(((MarketDataTrade)trades.get(id)).toBookString());
		}

	}
	else
	{
		mTradesListModel.addElement("No active trades for symbol = "+ symbol);
	}
}

/*
public void processMarketDataReject(FixMarketDataReject msg)
{
	mListModel.addElement("MARKET DATA REJECT");
}
public void processMarketDataSecurityStatus(FixSecurityStatus msg)
{
	  	String info = "   SECURITY STATUS    Symbol =" + msg.getSymbol();
  		info += " Security Trading Status = "+msg.getSecurityTradingStatus();
  		info += "(";
  		info += FixLookup.lookupTagValue("326",msg.getSecurityTradingStatusAsString());
  		info += ")";
  		info += "Time = ";
  		info += msg.getSendingTimeAsString();

  		mListModel.addElement(info);

}
*/

  public boolean processMarketDataRequest(RequestFeedByMarketID mdReq)
  {
    Logger.debug("processMarketDataRequest");

    try {
      mSessionMgr.sendImpactMessage(mdReq, mRequestSubject+"MKTREQ");
    } catch(Exception exp){ exp.printStackTrace(); }
    String str = "MarketData Request - "+ new String(mdReq.MarketIDs);
    mListModel.addElement(str);
    mListModel.addElement(mdReq.toString());
    return true;
    //requestedSymbols.add(msg.getSecurityType());
  }


/*
   // ========================================================================
  // ========================================================================
  public void processMarketDataRequest(FixMarketDataRequest msg)
  {
    Logger.debug("processMarketDataRequest");

    try {
      mSessionMgr.sendMarketDataRequest(msg, mRequestSubject+"MKTREQ");
    } catch(Exception exp){ exp.printStackTrace(); }
    String str = "MarketData Request - "+msg.getMDReqID();
    str += " Subscription RequestType = "+ msg.getSubscriptionRequestType();

    if(msg.getNoRelatedSym() > 0 )
    {
   	 	// add details
   	 	String key = "146";
  		ArrayList rgArray = msg.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {

  				FixRGNoRelatedSym_V rg = (FixRGNoRelatedSym_V)rgArray.get(i);
  				String info = "       Symbol =" + rg.getSymbol();
  				info += sdRespHandler.getDescription(rg.getSymbol());
  				mListModel.addElement(info);
   			}
   			mListModel.addElement("<END>" + str);
  		}


    }


    mListModel.addElement(str);
    //requestedSymbols.add(msg.getSecurityType());
  }
  public void processRestoredMarketDataRequest(FixMarketDataRequest msg)
  {

    String str = "MarketData Request - "+msg.getMDReqID();
    str += " Subscription RequestType = "+ msg.getSubscriptionRequestType();

    if(msg.getNoRelatedSym() > 0 )
    {
   	 	// add details
   	 	String key = "146";
  		ArrayList rgArray = msg.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {

  				FixRGNoRelatedSym_V rg = (FixRGNoRelatedSym_V)rgArray.get(i);
  				String info = "       Symbol =" + rg.getSymbol();
  				mListModel.addElement(info);
   			}
   			mListModel.addElement("<END>" + str);
  		}


    }
  }
  */
    // ========================================================================
  // ========================================================================
  public void actionPerformed(ActionEvent e)
  {
    Logger.debug("actionPerformed:: "+e.toString());
    String cmd = e.getActionCommand();
    if(cmd == UPDATE)
    {
    	if(currentSymbol == null)
    	{
    		System.out.println("NULL");
    		//test();
    		return;
    	}
		updateFullBookTab(currentSymbol);
		//updateBookTab(currentSymbol);
		updateBidsTab(currentSymbol);
		updateOffersTab(currentSymbol);
		updateTradesTab(currentSymbol);
    }
  }
  // ========================================================================
  // ========================================================================
  private void createWindows() throws Exception
  {
    setLayout(new GridLayout(0,1));
    setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("< Market Data Window - Trader:" +mSenderCompID+" >"),
        BorderFactory.createEmptyBorder(0,0,0,0)
      )
    );
    if(perfMode)
    {
    JButton btnUPDATE = new JButton(UPDATE);
    //c.gridx = 5; c.gridy = y+5;
    btnUPDATE.setActionCommand(UPDATE);
    btnUPDATE.addActionListener(this);
    //add(btnUPDATE, c);
    add(btnUPDATE);
    }
    add(createSymbolCombo());
    add(createTabbedPanes());
    add(createResponseLog());
  }

  private Component createSymbolCombo() throws Exception
  {

    //symbolPanel = new JPanel();
    //symbolPanel.setLayout( new FlowLayout(FlowLayout.CENTER,1,0));

    cbSymbols = new JComboBox();
    cbSymbols.addItemListener( new ItemListener()
    {
	public void itemStateChanged( ItemEvent ev )
	{
		if( ev.getStateChange() == ItemEvent.SELECTED )
		{
		String symbol = ((SymbolInfo)ev.getItem()).symbol;
		Logger.debug("Selected :"+symbol);
		SetSymbol(symbol);
		cbqSymbols.addItem(symbol);

		}
	}
    });

    cbSymbols.setPreferredSize(new Dimension(600,100));
    //symbolPanel.add( cbSymbols );
    //return symbolPanel;
    return cbSymbols;
  }

	private synchronized void SetSymbol( String symbol )
	{
		if(!symbol.equals(currentSymbol))
		{
			currentSymbol = symbol;
			// update tabs now
	    	updateFullBookTab(symbol);
			updateTradesTab(symbol);
			updateBidsTab(symbol);
			updateOffersTab(symbol);


		}
	}

  // ========================================================================
  // ========================================================================
  private Component createTabbedPanes() throws Exception
  {
    mMarketDataWindow = new ImpactMktDataWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,this,mUniqueIDFile,mExchange);

    //mCXRWindow = new CxlCRWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,this, mUniqueIDFile,mExchange);
    mTabbedPane = new JTabbedPane();
    mTabbedPane.addTab(tagMKTDATA, mMarketDataWindow.createComponent());
    mTabbedPane.addTab(tagBOOK,createBookComponent());
    mTabbedPane.addTab(tagBIDS,createBidsComponent());
    mTabbedPane.addTab(tagOFFERS,createOffersComponent());
    mTabbedPane.addTab(tagTRADES,createTradesComponent());
    mTabbedPane.addTab(tagQUOTES,createQuotesComponent());
    if(mEnableNews)
    {
	    mNewsWindow = new NewsWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,mNewsSubject,mUniqueIDFile,mExchange);
	    mTabbedPane.addTab(tagNEWS, mNewsWindow.createResponseLog());
    }

    //mTabbedPane.addTab(tagCXLCR, mCXRWindow.createComponent());
    //mTabbedPane.addTab(tagDETAIL, getContent(tagDETAIL));
    return mTabbedPane;
  }
  // ========================================================================
  // ========================================================================
  private Component getContent(String title)
  {
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    return contentPane;
  }

  public Component createBookComponent()
  {
    mBookListModel = new DefaultListModel();
    mBookList = new JList(mBookListModel);
    //mList.addListSelectionListener(this);


    JScrollPane areaScrollPane = new JScrollPane(mBookList);
    //areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(600,200));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Book"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );
    JPanel respPane = new JPanel();
    respPane.add(areaScrollPane);

   return respPane;
  }

  public Component createBidsComponent()
  {
    mBidsListModel = new DefaultListModel();
    mBidsList = new JList(mBidsListModel);
    //mList.addListSelectionListener(this);


    JScrollPane areaScrollPane = new JScrollPane(mBidsList);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(600,300));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Bids"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );
    JPanel respPane = new JPanel();
    respPane.add(areaScrollPane);

   return respPane;
  }

  public Component createQuotesComponent()
  {

    JPanel panel = new JPanel();
    panel.setLayout( new FlowLayout(FlowLayout.CENTER,1,0));

    cbqSymbols = new JComboBox();
    cbqSymbols.addItemListener( new ItemListener()
    {
	public void itemStateChanged( ItemEvent ev )
	{
		if( ev.getStateChange() == ItemEvent.SELECTED )
		{
		String symbol = (String)ev.getItem();
		System.out.println("Selected :"+symbol);
		//setQuoteSymbol(symbol);
		quoteSymbol = symbol;

		}
	}
    });


    lbBid = new JLabel( "BID" );
    lbBid.setForeground( Color.blue );

    lbOffer = new JLabel( "OFFER" );
    lbOffer.setForeground( Color.red );

    lbLast = new JLabel( "LAST" );
    lbLast.setForeground( Color.gray );

  	lbHigh = new JLabel("HIGH");
  	lbLow= new JLabel("LOW");
  	lbOpen= new JLabel("OPEN");
  	lbClose= new JLabel("CLOSE");
  	lbVolume= new JLabel("VOL");
  	lbOpenInterest= new JLabel("OI");
  	lbSettlementPrice= new JLabel("SP");
  	lbVWAP= new JLabel("VWAP");

    tfBid = new JTextField(10);
    tfOffer = new JTextField(10);
    tfLast = new JTextField(10);

    tfBid.setForeground( Color.blue );
    tfOffer.setForeground( Color.red );
    tfLast.setForeground( Color.gray );


  	 tfHigh = new JTextField(10);
  	 tfLow= new JTextField(10);
  	 tfOpen= new JTextField(10);
  	 tfClose= new JTextField(10);
  	 tfVolume= new JTextField(10);
  	 tfOpenInterest= new JTextField(10);
  	 tfSettlementPrice= new JTextField(10);
  	 tfVWAP= new JTextField(10);








    tfBid.setText("0@0");
    tfOffer.setText("0@0");
    tfLast.setText("0@0");

    panel.add( cbqSymbols );
    panel.add( lbBid );
    panel.add( tfBid );
    panel.add( lbLast );
    panel.add( tfLast );
    panel.add( lbOffer );
    panel.add( tfOffer );

    panel.add( lbHigh );
    panel.add( tfHigh );


    panel.add( lbLow );
    panel.add( tfLow );


    panel.add( lbOpen );
    panel.add( tfOpen );


    panel.add( lbClose );
    panel.add( tfClose );


    panel.add( lbVolume );
    panel.add( tfVolume );


    panel.add( lbOpenInterest );
    panel.add( tfOpenInterest );


    panel.add( lbSettlementPrice );
    panel.add( tfSettlementPrice );


    panel.add( lbVWAP );
    panel.add( tfVWAP );


   return panel;

  }

  public Component createOffersComponent()
  {
    mOffersListModel = new DefaultListModel();
    mOffersList = new JList(mOffersListModel);
    //mList.addListSelectionListener(this);


    JScrollPane areaScrollPane = new JScrollPane(mOffersList);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(600,200));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Offers"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );
    JPanel respPane = new JPanel();
    respPane.add(areaScrollPane);

   return respPane;
  }

  public Component createTradesComponent()
  {
    mTradesListModel = new DefaultListModel();
    mTradesList = new JList(mTradesListModel);
    //mList.addListSelectionListener(this);


    JScrollPane areaScrollPane = new JScrollPane(mTradesList);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(600,200));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Trades"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );
    JPanel respPane = new JPanel();
    respPane.add(areaScrollPane);

   return respPane;
  }

  // ========================================================================
  // ========================================================================
  private Component createResponseLog()
  {
    mListModel = new DefaultListModel();
    mList = new JList(mListModel);
    //mList.addListSelectionListener(this);


    JScrollPane areaScrollPane = new JScrollPane(mList);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(600,300));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Market Data Responses Log"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );
    JPanel respPane = new JPanel();
    respPane.add(areaScrollPane);

   return respPane;
  }
  // ========================================================================
  // ========================================================================
  private void initializeSession()
  {
    try
    {

      mdHandler = new ICEImpactFullMarketDataHandler(this);
      mSessionMgr = new ImpactDataSessionManager();
      mSessionMgr.startImpactSession();
      mSessionMgr.receiveImpactMessages(this,mResponseSubject);
      if(mSecResponseSubject != null)
      {
      	sdRespHandler = new ImpactSDResponseHandler();
      	mSessionMgr.receiveImpactMessages(this,mSecResponseSubject);

      }


    }
    catch( Exception exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }
  // ========================================================================
  // ========================================================================
  private void processConfig()
  {
    try
    {
      mResponseSubject = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_RESP_SUB_SUB);
      Logger.debug("Resp Publish Subject is " + mResponseSubject );
      mRequestSubject = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_REQ_PUB_SUB);
      Logger.debug("Req Publish Subject is " + mRequestSubject );
      mSenderCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_SENDER_COMPANY_ID);
      Logger.debug("Sender Comp ID is " + mSenderCompID );
      mTargetCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_TARGET_COMPANY_ID);
      Logger.debug("Target Comp ID is " + mTargetCompID );
      mUniqueIDFile = ConfigurationService.getValue("UniqueIDFile");
      Logger.debug("UniqueIDFile is " + mUniqueIDFile );
      mExchange = ConfigurationService.getValue("Exchange");
      Logger.debug("Exchange is " + mExchange );
      if(mExchange.equals("ICE"))
      {
   		   mSenderSubID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_SENDER_SUB_ID);
   		   Logger.debug("Sender Sub ID is " + mSenderSubID );
   		   mUserName = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_USER_NAME);
   		   Logger.debug("UserName is " + mUserName );

   		   mTraderName = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_TRADER_NAME);
   		   Logger.debug("TraderName is " + mTraderName );
   		   mSecResponseSubject = ConfigurationService.getValue("SDResponseSubject");
	      Logger.debug("Securities Resp Publish Subject is " + mSecResponseSubject );


      }

	// news related
	  String newsFlag = ConfigurationService.getValue("EnableNews","N");
	  if(newsFlag.equals("Y"))
	  {
	  	mEnableNews = true;
    	Logger.debug("EnableNews is " + newsFlag );

	    mNewsSubject = ConfigurationService.getValue("NewsSubject");
        Logger.debug("News Publish Subject is " + mNewsSubject );
	  }

	  String perfFlag = ConfigurationService.getValue("PerformanceMode","N");
	  if(perfFlag.equals("Y"))
	  {
	  	perfMode = true;
	  }



    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }
/*
  private void restoreFromFix()
  {
    try
    {

      String restore = ConfigurationService.getValue("restore");
      if(restore.equals("Y"))
      {
 	     //String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     //ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("FixInLogFile");




		FileInputStream stream= new FileInputStream(inFile);

 	    FixMessage msg = FixSessionManager.getApplicationMessages(stream);
		Date prevSendingTime = null;
		Date currSendingTime = null;
		long delay = 0;

 	     // then process in list
 	    while(msg != null)
	    {
	    	String msgType = msg.getMessageType();
	    	handleRestoredMessage(msgType,msg);
	    	if(prevSendingTime == null)
	    	{
	    		prevSendingTime = msg.getSendingTime();
	    		currSendingTime = msg.getSendingTime();
	    		//System.out.println("delay is zero");
	    	}
	    	else
	    	{
	    		prevSendingTime = currSendingTime;
	    		currSendingTime = msg.getSendingTime();
	    		delay = (currSendingTime.getTime() - prevSendingTime.getTime());
	    		//System.out.println("delay is " + delay);
	    	}


			try
			{
				if(delay != 0)
				{
					//Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());
					System.out.println("delaying for " + delay + " ms");

					Thread.sleep(delay);
				}
			}
			catch(Throwable e)
			{
			}


	   		msg = FixSessionManager.getApplicationMessages(stream);

	    }
	    stream.close();















      }


    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
    catch( FileNotFoundException exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }
    catch( IOException exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }

  private void restoreFromFixAllAtOnce()
  {
    try
    {

      String restore = ConfigurationService.getValue("restore");
      if(restore.equals("Y"))
      {
 	     //String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     //ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("FixInLogFile");
 	     ArrayList inList = FixSessionManager.getApplicationMessagesList(inFile);




 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	Logger.debug("Restoring");
    		FixMessage msg = (FixMessage)inList.get(i);
	    	String msgType = msg.getMessageType();
	    	Logger.debug("Restoring - "+msgType);
	    	boolean isProcessed = handleRestoredMessage(msgType,msg);
			try
			{
				if(isProcessed)
				{
					Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());

					Thread.sleep(3000);
				}
			}
			catch(Throwable e)
			{
			}


	    }

      }


    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }


  private boolean handleRestoredMessage(String msgType,FixMessage fixMsg)
  {



		String restoreSymbol = "217428";


		if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH))
		{
			return processMarketDataFullRefresh((FixMarketDataFullRefresh)fixMsg,restoreSymbol);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH))
		{
			return processMarketDataIncrementalRefresh((FixMarketDataIncrementalRefresh)fixMsg,restoreSymbol);
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS))
		{
			processMarketDataSecurityStatus((FixSecurityStatus)fixMsg);
			return false;
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REJECT))
		{
			processMarketDataReject((FixMarketDataReject)fixMsg);
			return false;
		}
		else if( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_REQUEST))
		{
			processMarketDataRequest((FixMarketDataRequest)fixMsg);
			return false;
		}

		return false;

  }
  */

  // ========================================================================
  // ========================================================================
  public static void main(String args[])
  {
    try
    {
      /*
      JFrame frame = new JFrame("Exchange API Trading Suite");
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      frame.setContentPane(new ICEMktDataWindow(args));
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
      */
      new ICEImpactFullMktDataWindow(args);

    }
    catch(Exception exc)
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }
  // ========================================================================
  // ========================================================================
  private String mResponseSubject = null;
  private String mSecResponseSubject = null;
  private String mRequestSubject = null;
  private String mSenderCompID = null;
  private String mSenderSubID = null;
  private String mUserName = null;
  private String mTraderName = null;
  private String mTargetCompID = null;
  private String mUniqueIDFile = null;

  //news
  private boolean mEnableNews = false;
  private String mNewsSubject = null;

  private String mExchange = null;
  private ImpactDataSessionManager mSessionMgr = null;
  private ImpactSDResponseHandler sdRespHandler = null;
  //private ICEMarketDataHandler mdHandler = null;
  private ICEImpactFullMarketDataHandler mdHandler = null;

  private JList mList = null;
  private String currentSymbol = null;
  private JComboBox cbSymbols = null;
  private JPanel symbolPanel = null;
  private JList mBookList = null;
  private JList mBidsList = null;
  private JList mOffersList = null;
  private JList mTradesList = null;
  private DefaultListModel mListModel = null;
  private DefaultListModel mBookListModel = null;
  private DefaultListModel mBidsListModel = null;
  private DefaultListModel mOffersListModel = null;
  private DefaultListModel mTradesListModel = null;
  private ImpactMktDataWindow mMarketDataWindow = null;
  private NewsWindow mNewsWindow = null;

  // for quotes component
  	JLabel lbBid;
  	JLabel lbOffer;
  	JLabel lbLast;
  	JLabel lbHigh;
  	JLabel lbLow;
  	JLabel lbOpen;
  	JLabel lbClose;
  	JLabel lbVolume;
  	JLabel lbOpenInterest;
  	JLabel lbSettlementPrice;
  	JLabel lbVWAP;


	JTextField tfBid;
  	JTextField tfOffer;
  	JTextField tfLast;
  	JTextField tfHigh;
  	JTextField tfLow;
  	JTextField tfOpen;
  	JTextField tfClose;
  	JTextField tfVolume;
  	JTextField tfOpenInterest;
  	JTextField tfSettlementPrice;
  	JTextField tfVWAP;


	JComboBox cbqSymbols;
	String quoteSymbol;



  private ArrayList requestedSymbols = new ArrayList();

  private JTabbedPane mTabbedPane = null;
  private JFrame mTheFrame = null;

  private final static String tagMKTDATA = "MarketData";
  private final static String tagNEWS = "News";
  private final static String tagBOOK = "Book";
  private final static String tagBIDS = "Bids";
  private final static String tagOFFERS = "Offers";
  private final static String tagTRADES = "Trades";
private final static String tagQUOTES = "Quotes";

  //private final static String tagCXLCR = "Cancel/Replace";
  //private final static String tagDETAIL = "Detail";

  private boolean perfMode = false;





  public void bestBid(String symbol,BookRow bidRow, boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
		//String bidString = (String.valueOf(bidRow.bidSize)+" @ "+bidRow.displayPrice;
  		tfBid.setText(String.valueOf(bidRow.bidSize)+" @ "+bidRow.displayPrice );
  	}

  }
  public void bestOffer(String symbol,BookRow offerRow, boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
		//String offerString = (String.valueOf(bidRow.bidSize)+" @ "+bidRow.displayPrice;
  		tfOffer.setText(String.valueOf(offerRow.offerSize)+" @ "+offerRow.displayPrice );
  	}
  }
  public void lastTrade(String symbol,MarketDataTrade last, boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
		//String offerString = (String.valueOf(bidRow.bidSize)+" @ "+bidRow.displayPrice;
  		tfLast.setText(String.valueOf(last.size)+" @ "+last.displayPrice );
  	}
  }
  public void volume(String symbol,double vol,boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
  		tfVolume.setText(String.valueOf(vol));
  	}
  }
  public void openInterest(String symbol,double oi,boolean isFullRefresh)
  {

  	if(symbol.equals(quoteSymbol))
  	{
  		tfOpenInterest.setText(String.valueOf(oi));
  	}
  }
  public void highPrice(String symbol,String high,boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
  		tfHigh.setText(high);
  	}
  }
  public void lowPrice(String symbol,String low,boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
  		tfLow.setText(low);
  	}
  }
  public void openPrice(String symbol,String price,boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
  		tfOpen.setText(price);
  	}
  }
  public void closePrice(String symbol,String price,boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
  		tfClose.setText(price);
  	}
  }
  public void vwapPrice(String symbol,String price,boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
  		tfVWAP.setText(price);
  	}
  }
  public void settlementPrice(String symbol,String price,boolean isFullRefresh)
  {
  	if(symbol.equals(quoteSymbol))
  	{
  		tfSettlementPrice.setText(price);
  	}
  }

public boolean processProductDefinitionResponse(ProductDefinitionResponse msg)
{
		Logger.debug("processSecurityDefinitionResponse");
	    sdRespHandler.processSDResponse(String.valueOf(msg.MarketID),msg);

	return true;
}
public boolean processProductDefinitionResponse(byte[] msg)
{
	
	    /*

    if(response.getNoRelatedSym() > 0 )
    {
   	 	// add details
   	 	String key = "146";
  		ArrayList rgArray = response.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {

  				FixRGNoRelatedSym rg = (FixRGNoRelatedSym)rgArray.get(i);
  				sdRespHandler.processSDResponse(rg.getUnderlyingSymbol(),rg);
   			}

  		}


    }
    */
	return true;
	
}
public boolean processProductDefinitionRequest(ProductDefinitionRequest msg)
{
	
	return true;
}

public boolean processMarketSnapshot(byte[] msg)
{
	return true;
}
public boolean processMarketSnapshotOrder(byte[] msg)
{
	return true;
}
public boolean processAddModifyOrder(byte[] msg)
{
	return true;
}
public boolean processDeleteOrder(byte[] msg)
{
	return true;
}
public boolean processTrade(byte[] msg)
{
	return true;
}
public boolean processCancelledTrade(byte[] msg)
{
	return true;
}
public boolean processMarketStatistics(byte[] msg)
{
	return true;
}
public boolean processOpenPrice(byte[] msg)
{
	return true;
}
public boolean processOpenInterest(byte[] msg)
{
	return true;
}
public boolean processSettlementPrice(byte[] msg)
{
	return true;
}
public boolean processMarketStateChange(byte[] msg)
{
	return true;
}



  public void test()
  {
  	int bidSize = 10;
  	int offerSize = 20;
  	double bidPrice = 30;
  	double offerPrice = 40;
  	String bidText,offerText;
  	for (int i=0;i<100000;i++)
  	{
  		bidPrice += 0.01;
  		offerPrice += 0.02;
		bidText = String.valueOf(++bidSize)+" @ "+ String.valueOf(bidPrice);
	    offerText = String.valueOf(++offerSize)+" @ "+ String.valueOf(offerPrice);
		if( i%2 == 0)
		{

			tfBid.setText(bidText);
			tfLast.setText(bidText);
		}
		else
		{

			tfOffer.setText(offerText);
			tfLast.setText(offerText);
		}
		if(i%25 == 0)
		{
			bidSize = 10;
			offerSize = 20;
			bidPrice = 30;
			offerPrice = 40;
		}
  	}
  }

}

