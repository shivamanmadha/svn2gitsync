package com.exsys.impact.tools;

import com.exsys.common.exceptions.*;
import com.exsys.service.*;
import com.exsys.fix.tools.*;
import com.exsys.impact.session.*;

// Fix Session related imports
import com.exsys.common.trading.*;
import com.exsys.common.util.*;
import com.exsys.common.business.*;
import com.exsys.fix.message.*;
import com.exsys.fix.session.*;


import org.xml.sax.*;

import org.apache.xerces.parsers.SAXParser;

import java.io.*;
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
public class ImpactToolsWindow extends JFrame implements LogonHandler,
					ICEFixSessionControlHandler,
					IFStatusNotification

{
	private static StatusDisplayPanel statusPanel = new StatusDisplayPanel();

	private static UserManager userManager = UserManager.getInstance();
	private LoginDialog loginDialog = null;
	private FixControlPanel fixPanel = null;
	private boolean loginSuccessful = false;
	private FixOrderIdGen fixOrderIdGen = FixOrderIdGen.getInstance();


	// GUI panel attributes
	static public ArrayList orderTypesList = new ArrayList();
	static public  ArrayList tifList = new ArrayList();

	// Fix Session attributes
	private boolean connected = false;
	private boolean loggedon = false;
	private   String connectionPort=null;
	private   String serverHost=null;
	private	  String senderCompID = null;
	private   String mExchange = null;
	private   String targetCompID = null;
	//private FixClientSession fcSession = null;
	private ICEJmsMDImpactClientSession fcSession = null;
	private boolean enableSecuritiesSession = false;
	private boolean enableMarketDataSession = false;



/**
 * ImpactToolsWindow constructor comment.
 * @param title java.lang.String
 */
public ImpactToolsWindow(String title) {
	super(title);

	initializeConfig();
	initializeXMLSetup();

	String strTitle = title;
	// setup main panel
	if(mExchange.equals("ICE"))
	{
		fixPanel = new ICEFixControlPanel(this);

	}
	else
	{
		fixPanel = new FixControlPanel(this);
	}
	strTitle += " - Exchange = "+ mExchange ;
	strTitle += " ConnectionPort = "+ connectionPort;
	strTitle += " ServerHost = "+ serverHost;

	setTitle(strTitle);


	JSplitPane	mainPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
										fixPanel,statusPanel );
	mainPane.setOneTouchExpandable(true);

	getContentPane().add( mainPane);

	statusPanel.addStatusMessage("Initialization Complete");

	// We need to display login dialog

	/*
	loginDialog = new LoginDialog(null, this);


	if(! loginSuccessful )
	{
		exitApplication(false,0);
	}
	*/



	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	addWindowListener( new WindowAdapter(){
		public void windowClosing(WindowEvent e )
		{
			exitApplication(true,0);
		}
	});

}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:10:38 PM)
 */
public void connectToFixServer()
{

	boolean pb = false;
	if( !connected )
	{
		int port = Integer.parseInt( connectionPort );
		statusPanel.addStatusMessage("About to Connect to Impact Server");

		try
		{

      		String playback = ConfigurationService.getValue("playback");
      		if(playback.equals("Y"))
      		{
      			pb = true;
      		}
			
			if(pb)
			{
				fcSession = new ICEJmsMDImpactClientSession(pb);
			}
			else
			{
				fcSession = new ICEJmsMDImpactClientSession(serverHost,port);
			}
			fcSession.registerStatusNotifier(this);
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

		if(pb)
		{
			fcSession.playbackMessages();
			connected = true;
		}
		else
		{
			statusPanel.addStatusMessage("Sending Logon Message");
			fcSession.start();
			fcSession.sendResetLogonMessage();
			connected = true;
		}
	}


}
public void resetSequenceNum(String newSeq)
{
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:10:54 PM)
 */
public void disconnectFromFixServer()
{
	if( connected )
	{
		statusPanel.addStatusMessage("Sending Logout Message");
		fcSession.sendLogoutMessage(0);
		connected = false;
		loggedon = false;
		// stop trading session

	}
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:10:54 PM)
 */
public void traderLogout()
{
	if( connected && loggedon )
	{
		statusPanel.addStatusMessage("Sending Trader Logout Message");
		//fcSession.sendTraderLogoutMessage();
		loggedon = false;
		// stop trading session

	}
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 1:10:54 PM)
 */
public void traderLogin()
{
	if( connected && !loggedon )
	{
		statusPanel.addStatusMessage("Sending Trader Logon Message");
		//fcSession.sendTraderLogonMessage();
		loggedon = true;
		// stop trading session

	}
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
                	"ImpactToolsWindow", JOptionPane.YES_NO_OPTION,
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
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 7:57:08 PM)
 * @param userId java.lang.String
 * @param password java.lang.String
 */
public boolean handleLogon(String userId, String password)
{

	boolean success = false;
	if( userManager.isUserValid(userId) )
	{
		if( userManager.isUserValid(userId,password))
		{
			loginSuccessful = true;
			success = true;

			//displayInformation("Login Successful","Login");
		}
		else
		{
			displayError("Invalid Password","LoginError");
		}

	}
	else
	{
		displayError("Invalid UserID","LoginError");
	}

	return success;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 2:14:48 PM)
 */
private void initializeConfig()
{
	    try
	    {
	    	connectionPort = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_CONNECT_PORT);
		   	System.out.println("Connection Port is " + connectionPort );
	    	serverHost = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SERVER_HOST);
		   	System.out.println("Server Host is " + serverHost );
	    	//senderCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SENDER_COMP_ID);
		   	//System.out.println("Sender Comp ID is " + senderCompID );
	    	//targetCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_TARGET_COMP_ID);
		   	//System.out.println("Target Comp ID is " + targetCompID );
 		     mExchange = ConfigurationService.getValue("Exchange");
 		     if(mExchange.equals("ICE"))
 		     {
 		     	/*
 		     	String strSec = ConfigurationService.getValue(ImpactMessageConstants.IMPACTCLIENT_ENABLE_SECURITIES);
 		     	if(strSec.equals("Y")) enableSecuritiesSession = true;
 		     	String strMkt = ConfigurationService.getValue(ImpactMessageConstants.IMPACTCLIENT_ENABLE_MARKETDATA);
 		     	if(strMkt.equals("Y")) enableMarketDataSession = true;
 		     	System.out.println("Securities - " + enableSecuritiesSession);
 		     	System.out.println("MarketData - " + enableMarketDataSession);
 		     	*/
 		     }
  		    System.out.println("Exchange is " + mExchange );


	    }
	    catch( ConfigAttributeNotFound exc )
	    {
		    System.out.println("CONFIG VALUES DOES NOT EXIST");
		    exc.printStackTrace();
		    System.exit(0);
	    }

}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:50:48 PM)
 */
private void initializeXMLSetup()
{
	String xmlFile = null;

	try
	{
		// get fixtools.xml file location
		xmlFile = ConfigurationService.getValue(FixToolsConstants.FIXTOOLS_SETUP_FILE);
	}
	catch( Exception e )
	{
		displayError(e.getMessage(),"ConfigurationError");
		System.exit(0);
	}



	UserManager userManager = UserManager.getInstance();
	// instantiate FixFieldXmlHandler
	SAXParser parser = new SAXParser();
	FixToolsXmlHandler fixToolsXmlHandler = new FixToolsXmlHandler(userManager,
							orderTypesList, tifList);
	parser.setDocumentHandler(fixToolsXmlHandler);
	//parser.setErrorHandler( new FixSpecification.MyErrorHanler());


	try
	{
		//System.out.println("Here");
		//InputStream is = new FileInputStream("c:\\fixtest\\config\\fixfields.xml");
		//System.out.println("Here");
		FileReader fr = new FileReader(xmlFile);
		if( fr == null ) System.out.println("NULL");
		System.out.println("Here");
		parser.parse( new InputSource(fr));
		System.out.println("Here");
	}
	catch(SAXException e )
	{
		System.out.println("Sax Exception");
		System.out.println(e.getMessage());
	}
	catch( Exception e )
	{
		System.out.println("Exception");
		e.printStackTrace();
	}

}
public void processOrder(String orderType, 
					     String tif, 
					     String symbol, 
					     String qty, 
					     String price,
					     boolean isBuy)
{
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 10:08:12 AM)
 */
public void logonCancelled()
{
	loginSuccessful = false;

}
/**
 * Insert the method's description here.
 * Creation date: (8/8/2002 11:22:16 AM)
 * @param args java.lang.String[]
 */
public static void main(String[] args)
{
	// initialize configuration service
	String cfgFile = parseArgs( args );
	if( cfgFile == null )
	{
		displayError("ConfigFile parameter not provided",
						"ConfigurationError");
		System.exit(0);
	}
	//displayInformation(cfgFile, "ConfigurationSuccessful");

	try
	{
		// initialize Configuration Service
		ConfigurationService.initialize( cfgFile );

		// initialize Logger
		Logger.initialize( cfgFile );

	}
	catch( Exception e )
	{
		displayError(e.getMessage(),"ConfigurationError");
		System.exit(0);
	}


	ImpactToolsWindow toolsWindow = new ImpactToolsWindow("ImpactToolsWindow");
	toolsWindow.pack();
	toolsWindow.setVisible(true);
	//userManager.debugPrint();



	// initialize user login details




	// close application
	//System.exit(0);

}
    static public String parseArgs(String[] args)
    {
	    String fileName = null;
        int i=0;

        while(i < args.length)
        {
            if (args[i].compareTo("-config")==0)
            {
                //if ((i+1) >= args.length) usage();
                fileName = args[i+1];
                i += 2;
            }
            /*
            else
            if (args[i].compareTo("-topic")==0)
            {
                if ((i+1) >= args.length) usage();
                topicName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-user")==0)
            {
                if ((i+1) >= args.length) usage();
                userName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-password")==0)
            {
                if ((i+1) >= args.length) usage();
                password = args[i+1];
                i += 2;
            }
            */
        }
        return fileName;
    }
/**
 * Insert the method's description here.
 * Creation date: (2/8/2002 5:19:45 AM)
 * @param cancel com.exsys.common.business.Cancel
 */
public void processCancel(Cancel cancel)
{
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancel(FixCancel cxl)
{
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancelReplace(FixCancelReplace cxr)
{
}
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:49 AM)
 * @param response com.exsys.fix.message.FixExecutionReport
 */
public void processExecutionReport(FixExecutionReport response)
{
	statusPanel.addStatusMessage(response.toString());
}
public void processCancelReject(FixCancelReject cxlRej)
{
	statusPanel.addStatusMessage(cxlRej.toString());
}

public void processStatusRequest(FixOrderStatusRequest stat)
{
	statusPanel.addStatusMessage(stat.toString());
}

/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processOrder(FixOrder ord)
{
}
public void printStatistics()
{
	MetricsCollector.printStats(System.out);
}


//public void processOrder(FixOrder ord, String rawMsg );
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processQuote(RealTimeQuote quote)
{
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
