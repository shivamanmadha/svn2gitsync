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
public class OpenCloseFixSessionDialog extends javax.swing.JDialog 
{
	private FixTestBenchSessionControlHandler fsHandler = null;

  	private JComboBox cbServers;
    private JComboBox cbClients;
  	private JButton   btOpen;
  	private JButton   btClose;
  	private JButton   btCancel;
    private JPanel    panel;
    

  class OpenCloseActionListener implements ActionListener
  {
	public void actionPerformed( ActionEvent ev )
	{

		boolean isOpen = false;
		String fixOrderType;
		String fixTimeInForce;
		String qty;
		String price;
		String symbol;
			
		String open_close = ev.getActionCommand();
		System.out.println("OpenOrClose is " + open_close );

		if( open_close.equals("Open Fix Session") )
		{
			isOpen = true;
		}

		FixServerDef server = (FixServerDef)cbServers.getSelectedItem();
		FixClientDef client = (FixClientDef)cbClients.getSelectedItem();

		if( server == null || client == null )
		{
			GuiUtil.displayError("Please Select Server and Client");
		}
		else
		{
			if( isOpen )
			{
				fsHandler.openFixSession(server,client);
			}
			else
			{
				fsHandler.closeFixSession(server,client);
			}
		}	
				
	}
  }    

	
/**
 * OrderEntryDialog constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public OpenCloseFixSessionDialog(java.awt.Frame owner, String title,
						FixTestBenchSessionControlHandler handler) {
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


	JLabel svLabel = new JLabel("Fix Servers");
	JLabel clLabel = new JLabel("Fix Clients");
		
	panel = new JPanel();
	
		
    cbServers = new JComboBox( FixTestBench.getFixServerList().toArray());
    cbClients = new JComboBox( FixTestBench.getFixClientList().toArray());
    
    btOpen = new JButton("Open Fix Session");
    //btBuy.setForeground(Color.blue );
    btClose = new JButton("Close Fix Session");
    //btSell.setForeground(Color.red);

	btCancel = new JButton("Cancel");
	
     

    panel.add( cbServers );
    panel.add( cbClients );

	JPanel labelPanel = new JPanel();
	labelPanel.setLayout(new GridLayout(0,1));
	labelPanel.add(svLabel);
	labelPanel.add(clLabel);

	JPanel tfPanel = new JPanel();
	tfPanel.setLayout(new GridLayout(0,1));	
    tfPanel.add( cbServers );
    tfPanel.add( cbClients );

	
    
    JPanel btnPanel = new JPanel();
	btnPanel.setLayout(new FlowLayout());
    btnPanel.add( btOpen );
    btnPanel.add( btClose );
    btnPanel.add( btCancel );

    
	panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	panel.setLayout(new BorderLayout());
	panel.add(labelPanel,BorderLayout.CENTER);
	panel.add(tfPanel,BorderLayout.EAST);
	panel.add(btnPanel,BorderLayout.SOUTH);
    
     
    this.getContentPane().add(panel);

    btOpen.addActionListener( new OpenCloseActionListener() );
    btClose.addActionListener( new OpenCloseActionListener() );

	btCancel.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			OpenCloseFixSessionDialog.this.hide();
			OpenCloseFixSessionDialog.this.dispose();				
				
		};
		
	});    
	
}
}
