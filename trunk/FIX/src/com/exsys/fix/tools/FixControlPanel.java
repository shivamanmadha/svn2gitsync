package com.exsys.fix.tools;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Insert the type's description here.
 * Creation date: (8/9/2002 12:53:55 PM)
 * @author: Administrator
 */
public class FixControlPanel extends javax.swing.JPanel {
	protected FixSessionControlHandler sessionHandler = null;
	private JTextField tfSeqNum = null;
/**
 * FixControlPanel constructor comment.
 */
public FixControlPanel(FixSessionControlHandler fsHandler) {
	super();
	sessionHandler = fsHandler;
	initializeComponents();
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 12:55:01 PM)
 */
protected void initializeComponents() 
{
	JButton btnConnect = new JButton("Connect To Fix Server");
	JButton btnDisconnect = new JButton("Disconnect");
	JButton btnTestOrder = new JButton("TestOrder...");

	JButton btnStats = new JButton("Statistics");
	JLabel lblSeq = new JLabel("SeqNum");
	tfSeqNum = new JTextField("100");
	JButton btnResetSeq = new JButton("ResetSeqNum");

	//JPanel btnPanel = new JPanel();
	this.setLayout(new FlowLayout());
	this.add(btnConnect);
	this.add(btnDisconnect);
	this.add(btnTestOrder);
	this.add(btnStats);
	this.add(lblSeq);
	this.add(tfSeqNum);
	this.add(btnResetSeq);
	btnTestOrder.setVisible(false);

    this.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Fix Session Control"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        this.getBorder()
      )
    );
	


	btnConnect.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.connectToFixServer();
		};
		
	});
	
	btnDisconnect.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.disconnectFromFixServer();
				
		};
		
	});

	btnStats.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.printStatistics();
				
		};
		
	});
	
	btnResetSeq.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.resetSequenceNum(tfSeqNum.getText());
				
		};
		
	});
		
	btnTestOrder.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			OrderEntryDialog oeDialog = new OrderEntryDialog(null,
							"Order Entry",
							sessionHandler );
		};
		
	});
	

	
}
}
