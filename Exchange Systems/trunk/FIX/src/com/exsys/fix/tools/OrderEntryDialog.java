package com.exsys.fix.tools;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Insert the type's description here.
 * Creation date: (8/9/2002 4:07:38 PM)
 * @author: Administrator
 */
public class OrderEntryDialog extends javax.swing.JDialog 
{
	private FixSessionControlHandler fsHandler = null;

  	private JComboBox cbOrderType;
    private JComboBox cbTimeInForce;
    //private JComboBox cbSymbol;
    private JTextField tfSymbol;
  	private JTextField tfQuantity;
  	private JTextField tfPrice;
  	private JButton   btBuy;
  	private JButton   btSell;
  	private JButton   btClose;
    private JPanel    panel;
    

  class BuySellActionListener implements ActionListener
  {
	public void actionPerformed( ActionEvent ev )
	{

		boolean isBuy = false;
		String fixOrderType;
		String fixTimeInForce;
		String qty;
		String price;
		String symbol;
			
		String buy_sell = ev.getActionCommand();
		System.out.println("Buy Sell is " + buy_sell );

		if( buy_sell.equals("BUY") )
		{
			isBuy = true;
		}

		fixOrderType = 	((OrderTypeDef)cbOrderType.getSelectedItem()).getFixValue();
		fixTimeInForce = ((TimeInForceDef)cbTimeInForce.getSelectedItem()).getFixValue();
		qty = tfQuantity.getText().trim();
		price = tfPrice.getText().trim();
		symbol = tfSymbol.getText().trim();
		if( qty.length() == 0 ||
			price.length() == 0 ||
			symbol.length() == 0 )
		{
			FixToolsWindow.displayError("Quantity, Price and Symbol must be filled","Order Entry");
		}
		else
		{
			fsHandler.processOrder(fixOrderType,
								fixTimeInForce,
								symbol,
								qty,
								price,
								isBuy);
		}
			
				
	}
  }    

	
/**
 * OrderEntryDialog constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public OrderEntryDialog(java.awt.Frame owner, String title,
						FixSessionControlHandler handler) {
	super(owner, title);
	fsHandler = handler;
	initializeComponents();
	this.pack();
	this.setVisible(true);		
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 4:09:31 PM)
 */
public void initializeComponents() 
{


	JLabel otLabel = new JLabel("Order Type");
	JLabel tifLabel = new JLabel("Time In Force");
	JLabel qtyLabel = new JLabel("Quantity");
	JLabel symbolLabel = new JLabel("Symbol");
	JLabel priceLabel = new JLabel("Limit Price");

		
	panel = new JPanel();
	
		
    cbOrderType = new JComboBox( FixToolsWindow.orderTypesList.toArray());
    cbTimeInForce = new JComboBox( FixToolsWindow.tifList.toArray() );
    //cbSymbol = new JComboBox( new cbModel( symbols ) );
    cbOrderType.setSelectedIndex(0);
    cbTimeInForce.setSelectedIndex(0);
    //cbSymbol.setSelectedIndex(0);

    tfSymbol = new JTextField(5);
    tfQuantity = new JTextField(5);
    tfPrice = new JTextField(10);
    btBuy = new JButton("BUY");
    btBuy.setForeground(Color.blue );
    btSell = new JButton("SELL");
    btSell.setForeground(Color.red);

	btClose = new JButton("Close");
	
     

    panel.add( cbOrderType );
    panel.add( cbTimeInForce );
    //panel.add( cbSymbol );
    panel.add( tfSymbol );    
    panel.add( tfQuantity );
    panel.add( tfPrice );

	JPanel labelPanel = new JPanel();
	labelPanel.setLayout(new GridLayout(0,1));
	labelPanel.add(otLabel);
	labelPanel.add(tifLabel);
	labelPanel.add(symbolLabel);
	labelPanel.add(qtyLabel);
	labelPanel.add(priceLabel);

	JPanel tfPanel = new JPanel();
	tfPanel.setLayout(new GridLayout(0,1));	
    tfPanel.add( cbOrderType );
    tfPanel.add( cbTimeInForce );
    //panel.add( cbSymbol );
    tfPanel.add( tfSymbol );    
    tfPanel.add( tfQuantity );
    tfPanel.add( tfPrice );

	
    
    JPanel btnPanel = new JPanel();
	btnPanel.setLayout(new FlowLayout());
    btnPanel.add( btBuy );
    btnPanel.add( btSell );
    btnPanel.add( btClose );

    
	panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	panel.setLayout(new BorderLayout());
	panel.add(labelPanel,BorderLayout.CENTER);
	panel.add(tfPanel,BorderLayout.EAST);
	panel.add(btnPanel,BorderLayout.SOUTH);
    
     
    this.getContentPane().add(panel);

    btBuy.addActionListener( new BuySellActionListener() );
    btSell.addActionListener( new BuySellActionListener() );

	btClose.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			OrderEntryDialog.this.hide();
			OrderEntryDialog.this.dispose();				
				
		};
		
	});    
	
}
}
