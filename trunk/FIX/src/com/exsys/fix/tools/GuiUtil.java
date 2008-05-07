package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (10/9/2002 9:22:13 PM)
 * @author: Administrator
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*; 
public class GuiUtil {
/**
 * GuiUtil constructor comment.
 * @param title java.lang.String
 */
public GuiUtil(String title) {
	
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:29:54 AM)
 * @param msg java.lang.String
 */
public static void displayError(String msg) 
{
	displayError( msg, "Error Message");
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:29:54 AM)
 * @param msg java.lang.String
 */
public static void displayError(String msg, String title) 
{
	JOptionPane.showMessageDialog( null, 
									msg, 
									title,
									JOptionPane.ERROR_MESSAGE );
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:29:54 AM)
 * @param msg java.lang.String
 */
public static void displayInformation(String msg) 
{
	displayError( msg, "Information Message");
}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:29:54 AM)
 * @param msg java.lang.String
 */
public static void displayInformation(String msg, String title) 
{
	JOptionPane.showMessageDialog( null, 
									msg, 
									title,
									JOptionPane.INFORMATION_MESSAGE );
}
}
