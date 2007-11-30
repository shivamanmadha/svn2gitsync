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
public class FixServerControlPanel extends javax.swing.JPanel {
	private FixServerSessionControlHandler sessionHandler = null;
/**
 * FixControlPanel constructor comment.
 */
public FixServerControlPanel(FixServerSessionControlHandler fsHandler) {
	super();
	sessionHandler = fsHandler;
	initializeComponents();
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 12:55:01 PM)
 */
private void initializeComponents()
{
	JButton btnPrimary = new JButton("Switch To Primary");
	JButton btnSecondary = new JButton("Switch To Secondary");
	//JButton btnTestOrder = new JButton("TestOrder...");

	//JPanel btnPanel = new JPanel();
	this.setLayout(new FlowLayout());
	this.add(btnPrimary);
	this.add(btnSecondary);
	//this.add(btnTestOrder);
	//btnTestOrder.setVisible(false);

    this.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Fix Server Session Control"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        this.getBorder()
      )
    );



	btnPrimary.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.switchToPrimary();
		};

	});

	btnSecondary.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			sessionHandler.switchToSecondary();

		};

	});
/*
	btnTestOrder.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			OrderEntryDialog oeDialog = new OrderEntryDialog(null,
							"Order Entry",
							sessionHandler );
		};

	});
*/



}
}
