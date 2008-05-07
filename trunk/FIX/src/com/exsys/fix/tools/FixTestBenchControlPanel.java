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
public class FixTestBenchControlPanel extends javax.swing.JPanel {
	private FixTestBenchSessionControlHandler sessionHandler = null;
/**
 * FixControlPanel constructor comment.
 */
public FixTestBenchControlPanel(FixTestBenchSessionControlHandler fsHandler) {
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
	//JButton btnConnect = new JButton("Connect To Fix Server");
	//JButton btnDisconnect = new JButton("Disconnect");
	JButton btnOpenClose = new JButton("FixSession...");

	//JPanel btnPanel = new JPanel();
	this.setLayout(new FlowLayout());
	//this.add(btnConnect);
	//this.add(btnDisconnect);
	this.add(btnOpenClose);

    this.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Fix Test Bench Session Control"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        this.getBorder()
      )
    );
	


	btnOpenClose.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			OpenCloseFixSessionDialog oeDialog = new OpenCloseFixSessionDialog(null,
							"Fix Session Open Close",
							sessionHandler );
		};
		
	});
	

	
}
}
