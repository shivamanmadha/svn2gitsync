package com.exsys.fix.tools;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.*;

/**
 * Insert the type's description here.
 * Creation date: (8/8/2002 4:14:54 PM)
 * @author: Administrator
 */
public class StatusDisplayPanel extends javax.swing.JPanel 
{
	java.text.SimpleDateFormat sd = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
	private JTextArea textArea = new JTextArea();
/**
 * StatusDisplayPanel constructor comment.
 */
public StatusDisplayPanel() {
	super();
	initializeComponents();
}
/**
 * StatusDisplayPanel constructor comment.
 * @param layout java.awt.LayoutManager
 */
public StatusDisplayPanel(java.awt.LayoutManager layout) {
	super(layout);
	initializeComponents();	
}
/**
 * StatusDisplayPanel constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public StatusDisplayPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
	initializeComponents();	
}
/**
 * StatusDisplayPanel constructor comment.
 * @param isDoubleBuffered boolean
 */
public StatusDisplayPanel(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
	initializeComponents();	
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 4:20:32 PM)
 * @param msg java.lang.String
 */
public void addStatusMessage(String msg) 
{
	textArea.append(sd.format(new java.util.Date()) +" : " + msg + "\n");
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 4:17:24 PM)
 */
private void initializeComponents() 
{
    textArea.setLineWrap(false);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);

    final JScrollPane areaScrollPane = new JScrollPane(textArea);

    //areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setPreferredSize(new Dimension(700,500));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Fix Tools Application Log"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );


    JPanel btnPanel = new JPanel();
    JButton btnClear = new JButton("Clear");
    btnPanel.setLayout( new FlowLayout());
    btnPanel.add(btnClear);
    
    BoxLayout bl = new BoxLayout(this,BoxLayout.X_AXIS);
    
    this.add(btnPanel);
    this.add(areaScrollPane);
    
    btnClear.addActionListener( new ActionListener(){
	    public void actionPerformed( ActionEvent event )
	    {
		 	textArea.setText("");
	    }
    });	
	
}
}
