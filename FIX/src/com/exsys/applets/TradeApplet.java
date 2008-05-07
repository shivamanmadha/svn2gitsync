package com.exsys.applets;

import com.exsys.common.business.*;
import com.exsys.common.trading.*;
import com.exsys.fix.message.*;


import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class TradeApplet extends Applet
{
  //Container container = null;
  Choice cbOrderType;
  Choice cbTimeInForce;
  Choice cbSymbol;
  TextField tfQuantity;
  TextField tfPrice;
  Button   btBuy;
  Button   btSell;
  Panel    panel;
  //SendRequest reqProcessor;
  private TradingSessionManager reqManager;

  String[] symbols = {"IBM","MSFT","CSCO","INTC","ORCL","HWP"};
  String[] tif = {"DAY","GTC"};
  String[] orderTypes = {"LMT","MKT","STP","STL"};

  String subject;
  Order ord = new Order();
  int order_id = 1;
  String user;

  class BuySellActionListener implements ActionListener
  {
	public void actionPerformed( ActionEvent ev )
	{
		String buy_sell = ev.getActionCommand();
		System.out.println("Buy Sell is " + buy_sell );
		if( buy_sell.equals("BUY") )
		{
			ord.setBuyOrSell( IBusinessObject.BUY );
		}
		else
		{
			ord.setBuyOrSell( IBusinessObject.SELL );
		}

		String id = user + String.valueOf(order_id); 
		ord.setOrderId( id );
		order_id = order_id+1;
		ord.setUser( user );
		ord.setOrderType((String)cbOrderType.getSelectedItem() );
		ord.setTimeInForce( (String)cbTimeInForce.getSelectedItem() );
		ord.setSymbol((String)cbSymbol.getSelectedItem() );
		ord.setSubject( subject );
		ord.setPrice( tfPrice.getText() );
		String qty = (tfQuantity.getText()).trim();
		System.out.println("Value of text field:"+qty);
		Integer int_qty = Integer.valueOf(qty);
		System.out.println("Value of int is:"+int_qty);
		ord.setQuantity( int_qty );
		ord.setOpenQuantity( int_qty );

		sendOrder( ord, subject );
				
	}
  }

  class WindowEventHandler extends WindowAdapter
  {
    public void WindowClosint(WindowEvent we )
    {
      System.exit(0);
    }
  }
  public void init()
  {
   
    String title="Trade Applet";
    // initialize Config Parameters
    String reqPubSub= getParameter("ReqPublishSubject");
    String userID=getParameter("User");
    
    this.subject = reqPubSub;
    this.user = userID;
    
    //JFrame frame = new JFrame(title);
    //container = this.getContentPane();
    //container = frame.getContentPane();
    this.setBackground( Color.white );
    this.setForeground( Color.blue );
    panel = new Panel();
    panel.setLayout( new FlowLayout(FlowLayout.CENTER,1,0));

    cbOrderType = new Choice();
    cbTimeInForce = new Choice();
    cbSymbol = new Choice();

    cbOrderType.add("LMT" );
    cbOrderType.add("MKT" );
    cbOrderType.add("STP" );
    cbOrderType.add("STL" );

    cbTimeInForce.add("DAY");
    cbTimeInForce.add("GTC");

    cbSymbol.add("IBM");
    cbSymbol.add("MSFT");
    cbSymbol.add("CSCO");
    cbSymbol.add("INTC");
    cbSymbol.add("ORCL");
    cbSymbol.add("INFY");
    cbSymbol.add("SIFY");
    cbSymbol.add("HWP");
    cbSymbol.add("BIDS");



    cbOrderType.select(0);
    cbTimeInForce.select(0);
    cbSymbol.select(0);


    tfQuantity = new TextField(5);
    tfPrice = new TextField(10);
    btBuy = new Button("BUY");
    btBuy.setForeground(Color.blue );
    btSell = new Button("SELL");
    btSell.setForeground(Color.red);


    panel.add( cbOrderType );
    panel.add( cbTimeInForce );
    panel.add( cbSymbol );   
    panel.add( tfQuantity );
    panel.add( tfPrice );
    panel.add( btBuy );
    panel.add( btSell );

    btBuy.addActionListener( new BuySellActionListener() );
    btSell.addActionListener( new BuySellActionListener() );

    this.add(  panel );


   	try
   	{
		reqManager = new TradingSessionManager();
		reqManager.startTradingSession();

	}
   	catch( Exception exc )
	{
		exc.printStackTrace();
	}

    
    //frame.addWindowListener( new WindowEventHandler() );
    //frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setSize( 500,100 );
    setVisible(true);
  }
  public void sendOrder( Order ord, String sub )
  {	
	  reqManager.sendOrder(ord,sub);	
	
  }
}
