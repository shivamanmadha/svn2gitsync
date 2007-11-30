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
public class ICEFixControlPanel extends FixControlPanel {
	private ICEFixSessionControlHandler sessionHandler = null;
	private JTextField tfSeqNum = null;
/**
 * ICEFixControlPanel constructor comment.
 */
public ICEFixControlPanel(FixSessionControlHandler fsHandler) {
	super(fsHandler);
	sessionHandler = (ICEFixSessionControlHandler)fsHandler;
	//initializeComponents();
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 12:55:01 PM)
 */
protected void initializeComponents()
{
	super.initializeComponents();

	JButton btnTraderLogin = new JButton("Trader Login");
	JButton btnTraderLogout = new JButton("Trader Logout");

	this.add(btnTraderLogin);
	this.add(btnTraderLogout);


	btnTraderLogin.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.traderLogin();
		};

	});

	btnTraderLogout.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.traderLogout();

		};

	});


}
}
