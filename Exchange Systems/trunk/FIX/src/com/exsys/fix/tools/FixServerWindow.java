package com.exsys.fix.tools;

import com.exsys.common.exceptions.*;
import com.exsys.service.*;

// Fix Session related imports
import com.exsys.common.trading.*;
import com.exsys.common.util.*;
import com.exsys.common.business.*;
import com.exsys.fix.message.*;
import com.exsys.fix.session.*;


import org.xml.sax.*;

import org.apache.xerces.parsers.SAXParser;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
/**
 * Insert the type's description here.
 * Creation date: (8/8/2002 11:15:14 AM)
 * @author: Administrator
 */
public class FixServerWindow extends JFrame
			implements FixServerSessionControlHandler,
					IFStatusNotification

{
	private static StatusDisplayPanel statusPanel = new StatusDisplayPanel();


	private FixServerControlPanel fixPanel = null;

	// Fix Session attributes
	//private FixClientSession fcSession = null;
	private JmsFixServerSession fsSession = null;



/**
 * FixToolsWindow constructor comment.
 * @param title java.lang.String
 */
public FixServerWindow(String title,Socket clientSocket,boolean bow) {
	super(title);

	// setup main panel

	fixPanel = new FixServerControlPanel(this);

	JSplitPane	mainPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
										fixPanel,statusPanel );
	mainPane.setOneTouchExpandable(true);

	getContentPane().add( mainPane);

	statusPanel.addStatusMessage("Initialization Complete");


	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	addWindowListener( new WindowAdapter(){
		public void windowClosing(WindowEvent e )
		{
			exitApplication(true,0);
		}
	});
	
		try
		{

			fsSession = new JmsFixServerSession( clientSocket );
			fsSession.registerStatusNotifier(this);
			fsSession.setBOWLogon(bow);
			fsSession.start();
		}
		catch(ConfigAttributeNotFound exc)
		{
			exc.printStackTrace();			
			statusPanel.addStatusMessage("Please check the configuration - " + exc.getExternalMessage());
			return;
		}
		catch(ConfigAttributeInvalid exc)
		{
			exc.printStackTrace();			
			statusPanel.addStatusMessage("Please check the configuration - " + exc.getExternalMessage());
			return;
		}	
		catch(SystemException exc)
		{
			exc.printStackTrace();			
			statusPanel.addStatusMessage("System Exception External Message - " + exc.getExternalMessage());
			statusPanel.addStatusMessage("System Exception Internal Message - " + exc.getMessage());
			return;
		}		


}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:10:38 PM)
 */
public void switchToPrimary()
{
	fsSession.switchToPrimary();
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:10:54 PM)
 */
public void switchToSecondary()
{
	fsSession.switchToBackup();
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
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 12:53:46 PM)
 * @param confirm boolean
 * @param exitStatus int
 */
public void exitApplication(boolean confirm, int exitStatus)
{
        boolean exit = true;

        if (confirm) {
            try {

                // Show a confirmation dialog
                int reply = JOptionPane.showConfirmDialog(this,
	                "Do you really want to exit?",
                	"FixToolsWindow", JOptionPane.YES_NO_OPTION,
                	JOptionPane.QUESTION_MESSAGE);

                // If the confirmation was affirmative, handle exiting.
                if (reply == JOptionPane.YES_OPTION) {
                    this.setVisible(false);     // hide the Frame
                    this.dispose();             // free the system resources
                    exitStatus = 0;
                } else {
	                System.out.println("Not Closing");
                    exit = false;
                }
            }
            catch (Exception e)
            {
	            e.printStackTrace();
	        }
        }

        if (exit) {
            System.exit(exitStatus);
        }

	 System.out.println("Not Closing");

}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 4:32:46 PM)
 * @return com.exsys.fix.tools.StatusDisplayPanel
 */
public static StatusDisplayPanel getStatusDisplayPanel()
{
	return statusPanel;
}


public void info(String msg)
{
	if(statusPanel != null)
	{
		statusPanel.addStatusMessage("Info - " + msg);
	}
}
public void warn(String msg)
{
	if(statusPanel != null)
	{
		statusPanel.addStatusMessage("Warn - " + msg);
	}
}
public void debug(String msg)
{
	if(statusPanel != null)
	{
		statusPanel.addStatusMessage("Debug - " + msg);
	}
}
public void fatal(String msg)
{
	if(statusPanel != null)
	{
		statusPanel.addStatusMessage("Fatal - " + msg);
	}
}
public void error(String msg)
{
	if(statusPanel != null)
	{
		statusPanel.addStatusMessage("Error - " + msg);
	}
}
}
