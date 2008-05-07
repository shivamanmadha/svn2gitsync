package com.exsys.orderentry;

import com.exsys.common.business.*;
import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.common.extrading.*;
import com.exsys.application.*;
import com.exsys.service.*;
import com.exsys.common.exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class QuoteWindow extends BaseApplication implements ITradeResponseHandler
{
	private TradingSessionManager respManager = null;
	private FixMessageAdapter msgAdapter = null;
	private RealTimeQuote lastQuote = new RealTimeQuote(IBusinessObject.QUOTE);

	Hashtable lastQuotes = new Hashtable();

  	Container container = null;
  	JLabel lbBid;
  	JLabel lbOffer;
  	JLabel lbLast;
  	JPanel panel;

	JTextField tfBid;
  	JTextField tfOffer;
  	JTextField tfLast;

	JComboBox cbSymbols;  



	String subject;
	
 	class WindowEventHandler extends WindowAdapter
  	{
   	 	public void WindowClosint(WindowEvent we )
    	{
      		System.exit(0);
    	}
  	}

  public QuoteWindow( String[] args )throws ConfigFileNotFound
  {
    super( args );
    
    // initialize Config Parameters
    String quoteSubSub=null;

    try
    {
    	quoteSubSub = ConfigurationService.getValue(OrderEntryConstants.QUOTEWINDOW_QUOTE_SUB_SUB);
    	System.out.println("Quote Subscribe Subject is " + quoteSubSub );
    }
    catch( ConfigAttributeNotFound exc )
    {
	    System.out.println("CONFIG VALUES DOES NOT EXIST");
	    exc.printStackTrace();
	    System.exit(0);	    
    }
    
    
    this.subject = quoteSubSub;
    

    String title = "QuoteWindow";
    JFrame frame = new JFrame(title);
    container = frame.getContentPane();
    container.setBackground( Color.white );
    container.setForeground( Color.blue );
    panel = new JPanel();
    panel.setLayout( new FlowLayout(FlowLayout.CENTER,1,0));

    cbSymbols = new JComboBox();
    cbSymbols.addItemListener( new ItemListener()
    {
	public void itemStateChanged( ItemEvent ev )
	{
		if( ev.getStateChange() == ItemEvent.SELECTED )
		{
		String symbol = (String)ev.getItem();
		System.out.println("Selected :"+symbol);
		SetSymbol(symbol);

		}
	}
    });


    lbBid = new JLabel( "BID" );
    lbBid.setForeground( Color.blue );

    lbOffer = new JLabel( "OFFER" );
    lbOffer.setForeground( Color.red );

    lbLast = new JLabel( "LAST" );
    lbLast.setForeground( Color.gray );

    tfBid = new JTextField(10);
    tfOffer = new JTextField(10);
    tfLast = new JTextField(10);

    tfBid.setForeground( Color.blue );
    tfOffer.setForeground( Color.red );
    tfLast.setForeground( Color.gray );

    tfBid.setText("0@0");
    tfOffer.setText("0@0");
    tfLast.setText("0@0");

    panel.add( cbSymbols );
    panel.add( lbBid );
    panel.add( tfBid );
    panel.add( lbLast );
    panel.add( tfLast );
    panel.add( lbOffer );   
    panel.add( tfOffer );



    container.add(  panel );
    msgAdapter = new FixMessageAdapter(this);

    try
    {
    	respManager = new TradingSessionManager();
    	respManager.startTradingSession();
  
		if( subject != null )
		{
			respManager.receiveTradingMessages(msgAdapter, subject );
			//if( query != null )
			//	respManager.receiveTradingMessages(msgAdapter, query );
		}
 	}
    catch( Exception exc )
    {
	       exc.printStackTrace();
    }

    
    frame.addWindowListener( new WindowEventHandler() );
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setSize( 700,100 );
    frame.show();
  }
  public static void main( String[] args )
  {
	   
	  try
	  {
   		 QuoteWindow frame = new QuoteWindow(args);
	  }
	  catch(Exception e )
	  {
		  e.printStackTrace();
	  }    
  }
	public void ReceivedAccept( Response resp )
	{
		System.out.println("Received Accept");
		resp.print();

	}
	public void ReceivedCancel( Cancel cxl )
	{
		System.out.println("In ReceivedCancel:");
		cxl.print();
	}
	public void ReceivedCancelReplace( CancelReplace cxr)
	{

	}
	public void ReceivedConfirm( Confirm conf )
	{
		System.out.println("In ReceivedConfirm:");
		conf.print();
	}
	public void ReceivedFill( Fill fill)
	{
		System.out.println("Received Fill");
		fill.print();

	}
	public void ReceivedOrder( Order ord )
	{
		System.out.println("In ReceivedOrder:");
		ord.print();
	}
	public void ReceivedPartialFill( Fill fill )
	{
		System.out.println("Received Fill");
		fill.print();

	}
	public void ReceivedQuote( RealTimeQuote quote )
	{
		System.out.println("Received Quote");
		
		String symbol = quote.getSymbol();
		System.out.println( symbol );
		boolean hasSymbol = lastQuotes.containsKey( symbol );
		if( hasSymbol )
		{
			System.out.println("SYMBOL FOUND");
			lastQuotes.remove( symbol );
			lastQuotes.put( symbol,	quote );
		}
		else
		{
		       System.out.println("SYMBOL NOT FOUND");
		       lastQuotes.put( symbol, quote );
		       cbSymbols.addItem( symbol );
		       //cbSymbols.repaint();
		}
		
		SetQuote( quote );

	}
	public RealTimeQuote ReceivedQuoteRequest()
	{

		System.out.println("Received Quote Request");
		return lastQuote;
	}
	public void ReceivedReject( Response resp )
	{
		System.out.println("Received Reject");
		resp.print();

	}
	public void ReceivedStatusRequest()
	{
		System.out.println("Received StatusRequest");

	}
	public void SetQuote( RealTimeQuote quote)
	{
		if( (quote.getSymbol()).equals((String)cbSymbols.getSelectedItem()) ) 
		{
			//System.out.println("In SetQuote");
			//quote.print();
		String bidString = (quote.getBidQuantity()).toString()+new String(" @ ")+
				   quote.getBidPrice();
		String offerString = (quote.getOfferQuantity()).toString()+new String(" @ ")+
				   quote.getOfferPrice();
		String lastString = (quote.getLastQuantity()).toString()+new String(" @ ")+
				   quote.getLastPrice();
		tfBid.setText( bidString );
		tfOffer.setText( offerString );
		tfLast.setText( lastString );
		}

	}
	public void SetSymbol( String symbol ) 
	{
		RealTimeQuote lastQuote = (RealTimeQuote)lastQuotes.get(symbol);
		//System.out.println("InSetSymbol");
		//lastQuote.print();
		SetQuote( lastQuote );
		
	}
}
