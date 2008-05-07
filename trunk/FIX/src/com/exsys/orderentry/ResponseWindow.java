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

class ResponseWindow extends BaseApplication implements ITradeResponseHandler
{
	private TradingSessionManager respManager = null;
	private FixMessageAdapter msgAdapter = null;

  Container container = null;
  JList statusList = null;
  JButton btCancel = null;
  Vector status = new Vector();
  JScrollPane scrollPane = null;
  JPanel panel;
  int index;
  int cancel_id = 1;

  String subject;

class CancelListener implements ActionListener
{
	public void actionPerformed( ActionEvent ev )
	{
		
		String cmd = ev.getActionCommand();
		System.out.println("Button is " + cmd );
		if( cmd.equals("CXL") )
		{
			IBusinessObject obj = (IBusinessObject)(statusList.getSelectedValue());
			if( (obj.getType()).equals(IBusinessObject.ACCEPT))
			{
				String user = "RAMS";
				System.out.println("Send Cancel");
				Response resp = (Response)obj;
				Cancel cxl = new Cancel();
				String id = user + String.valueOf(cancel_id); 
				cxl.setCancelId( id );
				cancel_id = cancel_id+1;

				cxl.setTBCOrderId( resp.getRequestId() );
				cxl.setSymbol( resp.getSymbol() );
				cxl.setBuyOrSell( resp.getBuyOrSell() );
				cxl.setUser(user);
				cxl.setSubject("TRADE.REQ.CXL");
				try{
						SendCancel( cxl );
				}catch( Exception exc )
			   	{
			   	}

				
			}
			else
			{
				System.out.println("Nothing to Cancel");
			}
		}

/*
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

		sendOrder( ord );
*/
				
	}

}
  class WindowEventHandler extends WindowAdapter
  {
    public void WindowClosint(WindowEvent we )
    {
      System.exit(0);
    }
  }

  public ResponseWindow( String[] args ) throws ConfigFileNotFound
  {
    super( args );

    // initialize Config Parameters
    String respSubSub=null;

    try
    {
    	respSubSub = ConfigurationService.getValue(OrderEntryConstants.RESPONSEWINDOW_RESP_SUB_SUB);
    	System.out.println("Resp Subscribe Subject is " + respSubSub );
    }
    catch( ConfigAttributeNotFound exc )
    {
	    System.out.println("CONFIG VALUES DOES NOT EXIST");
	    exc.printStackTrace();
	    System.exit(0);	    
    }

    
    this.subject = respSubSub;

    String title = "Response Window";
    JFrame frame = new JFrame(title);
    container = frame.getContentPane();
    container.setBackground( Color.white );
    container.setForeground( Color.blue );



    panel = new JPanel();
    panel.setLayout( new FlowLayout(FlowLayout.CENTER,1,0));

    statusList = new JList();
    statusList.setSelectionBackground(Color.white );
    statusList.setSelectionForeground(Color.blue );
    statusList.setCellRenderer( new MyCellRenderer() );
    scrollPane = new JScrollPane( statusList,
				  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,	
				  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );

   scrollPane.setSize(500,500);
   btCancel = new JButton("CXL");
   btCancel.addActionListener( new CancelListener() );



    panel.add( scrollPane );
    panel.add( btCancel );
    container.add(  panel );
    //container.add( btCancel );

	
		   try
		   {
			   	msgAdapter = new FixMessageAdapter(this);
				respManager = new TradingSessionManager();
				respManager.startTradingSession();		   
				respManager.receiveTradingMessages( msgAdapter,subject );
		   }catch( Exception exc )
		   {
		   }

    
    frame.addWindowListener( new WindowEventHandler() );
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.setSize( 700,400 );
    frame.show();
  }
	public void AddStatus( IBusinessObject obj )
	{
		status.addElement( obj );
		statusList.setListData( status );
		statusList.repaint();
		scrollPane.repaint();
		
	}
	public void AddStatus( String str )
	{
		status.addElement( str );
		statusList.setListData( status );
		statusList.repaint();
		scrollPane.repaint();
		
	}
  public static void main( String[] args )
  {
    
	  try
	  {
   		 ResponseWindow frame = new ResponseWindow(args);
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
		String str = new String("ORD: "+resp.getRequestId()+" "+ "ACCEPTED");
		//AddStatus( str );
		AddStatus( resp );

	}
	public void ReceivedCancel( Cancel cxl )
	{
		System.out.println("In ReceivedCancel:");
		cxl.print();

		String cxlString = new String("CXL: "+cxl.getCancelId() + " CXLing "+cxl.getTBCOrderId()+ "SUBMITTED");
		//AddStatus( cxlString );
		AddStatus( cxl );

		
	}
	public void ReceivedCancelReplace( CancelReplace cxr)
	{

	}
	public void ReceivedConfirm( Confirm conf )
	{
		System.out.println("Received Confirm");
		conf.print();
		String str = new String("CXL: "+conf.getCancelId()+" "+ "CONFIRMED");
		//AddStatus( str );
		AddStatus( conf );

	}
	public void ReceivedFill( Fill fill)
	{
		System.out.println("Received Fill");
		String priceStr = new String((fill.getQuantity()).toString()+"@"+fill.getTradePrice());
		String filString = new String("ORD: "+fill.getRequestId()+" "+priceStr+" "+ "FILLED "+ "OPP: "+fill.getOppositeBroker());
		//AddStatus( filString );
		AddStatus( fill );

		fill.print();

	}
	public void ReceivedOrder( Order ord )
	{
		System.out.println("In ReceivedOrder:");
		ord.print();
		String priceStr = new String(ord.getBuyOrSell()+" "+(ord.getQuantity()).toString()+"@"+ord.getPrice());
		String ordString = new String("ORD: "+ord.getOrderId()+" "+priceStr+" "+ "SUBMITTED");
		//AddStatus( ordString );
		AddStatus( ord );

		
	}
	public void ReceivedPartialFill( Fill fill )
	{
		System.out.println("Received Fill");
		String priceStr = new String((fill.getQuantity()).toString()+"@"+fill.getTradePrice());
		String filString = new String("ORD: "+fill.getRequestId()+" "+priceStr+" LQ: "+(fill.getLeavesQuantity()).toString()+ " PARTIAL " + "OPP: "+fill.getOppositeBroker());
		//AddStatus( filString );
		AddStatus( fill );
		fill.print();

	}
	public void ReceivedQuote( RealTimeQuote quote )
	{
		System.out.println("Received Quote");
		quote.print();
	}
	public RealTimeQuote ReceivedQuoteRequest()
	{
		RealTimeQuote quote = new RealTimeQuote(IBusinessObject.QUOTE);
		System.out.println("Received Quote Request");
		return quote;
	}
	public void ReceivedReject( Response resp )
	{
		System.out.println("Received Reject");
		resp.print();
		String str = new String("ORD: "+resp.getRequestId()+" - "+ resp.getReason()+ " REJECTED");
		//AddStatus( str );
		AddStatus( resp );

	}
	public void ReceivedStatusRequest()
	{
		System.out.println("Received StatusRequest");

	}
/**
 * Insert the method's description here.
 * Creation date: (2/5/2002 5:45:10 AM)
 * @param cxl com.exsys.common.business.Cancel
 */
public void SendCancel(Cancel cxl) 
{
	respManager.sendCancel(cxl, cxl.getSubject());
}
}
