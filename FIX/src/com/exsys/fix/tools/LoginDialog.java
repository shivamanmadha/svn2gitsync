package com.exsys.fix.tools;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Insert the type's description here.
 * Creation date: (8/8/2002 8:01:03 PM)
 * @author: Administrator
 */
public class LoginDialog extends javax.swing.JDialog 
{
	LogonHandler logonHandler = null;
	JTextField tfUserId = new JTextField(10);
	JTextField tfPassword = new JPasswordField(10);
	
/**
 * LoginDialog constructor comment.
 * @param owner java.awt.Frame
 */
public LoginDialog(java.awt.Frame owner, LogonHandler newLogonHandler) {
	super(owner, "Please Login", true);
	logonHandler = newLogonHandler;	
	initializeComponents();
	this.pack();
	this.setVisible(true);	
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 8:04:17 PM)
 */
public void initializeComponents() 
{
	JLabel userIdLabel = new JLabel("UserId");
	JLabel pwLabel = new JLabel("Password");
	//JTextField tfUserId = new JTextField(10);
	//JTextField tfPassword = new JPasswordField(10);
	JButton btnLogon = new JButton("Logon");
	JButton btnCancel = new JButton("Cancel");

	JPanel btnPanel = new JPanel();
	btnPanel.setLayout(new FlowLayout());
	btnPanel.add(btnLogon);
	btnPanel.add(btnCancel);

	
		
	JPanel labelPanel = new JPanel();
	labelPanel.setLayout(new GridLayout(0,1));
	labelPanel.add(userIdLabel);
	labelPanel.add(pwLabel);


	JPanel tfPanel = new JPanel();
	tfPanel.setLayout(new GridLayout(0,1));
	tfPanel.add(tfUserId);
	tfPanel.add(tfPassword);

	JPanel contentPane = new JPanel();
	contentPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	contentPane.setLayout(new BorderLayout());
	contentPane.add(labelPanel,BorderLayout.CENTER);
	contentPane.add(tfPanel,BorderLayout.EAST);
	contentPane.add(btnPanel,BorderLayout.SOUTH);

	this.getContentPane().add( contentPane );


	btnLogon.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			// check if text fields are blank or null
			String userId = tfUserId.getText();
			String password = tfPassword.getText();
			if( ( userId != null && userId.trim().length() == 0 )
				|| ( password != null && password.trim().length() == 0 ) )
			{
				FixToolsWindow.displayError("Please Enter Login Information",
								"Login Error");
			}
			else
			{

				if( logonHandler.handleLogon(userId, password) )
				{
					System.out.println("Login Successful");
						LoginDialog.this.hide();
						LoginDialog.this.dispose();														
				}
			}

			
		};
		
	});
	
	btnCancel.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent event)
		{
			LoginDialog.this.hide();
			LoginDialog.this.dispose();				
				
		};
		
	});
		
	
}
}
