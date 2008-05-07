package com.exsys.fix.tools;

import com.exsys.common.exceptions.*;
import com.exsys.service.*;

// Fix Session related imports
import com.exsys.common.trading.*;
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
public class FixTestBench extends JFrame implements LogonHandler,
				FixTestBenchSessionControlHandler
					
					
{
	private static StatusDisplayPanel statusPanel = new StatusDisplayPanel();
	
	private static UserManager userManager = UserManager.getInstance();
	private static FixConnectionsManager fcManager = new FixConnectionsManager();
	
	private static TestOrderDef testOrder = new TestOrderDef();
	// GUI panel attributes
	static public ArrayList orderTypesList = new ArrayList();
	static public  ArrayList tifList = new ArrayList();

	
	private LoginDialog loginDialog = null;
	private FixTestBenchControlPanel fixPanel = null;
	private boolean loginSuccessful = false;	

	
/**
 * FixToolsWindow constructor comment.
 * @param title java.lang.String
 */
public FixTestBench(String title) {
	super(title);

	initializeConfig();
	initializeXMLSetup();
	

	// setup main panel
	/*
	JPanel topPanel = new JPanel();
	JLabel testLabel = new JLabel(title);
	topPanel.setLayout(new BorderLayout());
	topPanel.add(testLabel, BorderLayout.CENTER );
	topPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	*/

	fixPanel = new FixTestBenchControlPanel(this);
	
	JSplitPane	mainPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT,
										fixPanel,statusPanel );
	mainPane.setOneTouchExpandable(true);

	getContentPane().add( mainPane);

	statusPanel.addStatusMessage("Initialization Complete");

	// We need to display login dialog

	
	loginDialog = new LoginDialog(null, this);

	
	if(! loginSuccessful )
	{
		exitApplication(false,0);
	}
	
	
	

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
 * Creation date: (10/9/2002 5:50:54 PM)
 * @param server com.exsys.fix.tools.FixServerDef
 * @param client com.exsys.fix.tools.FixClientDef
 */
public void closeFixSession(FixServerDef server, FixClientDef client) {}
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
 * Creation date: (10/9/2002 5:46:45 PM)
 * @return java.util.ArrayList
 */
public static ArrayList getFixClientList() {
	return fcManager.getFixClientList();
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 5:46:45 PM)
 * @return java.util.ArrayList
 */
public static ArrayList getFixServerList() {
	return fcManager.getFixServerList();
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 9:46:28 PM)
 * @return com.exsys.fix.tools.TestOrderDef
 */
public static TestOrderDef getFixTestOrder() {
	return testOrder;
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
			GuiUtil.displayError("Invalid Password","LoginError");	
		}
		
	}
	else
	{
		GuiUtil.displayError("Invalid UserID","LoginError");
	}
	
	return success;
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 2:14:48 PM)
 */
private void initializeConfig() 
{
	/*
	    try
	    {
	    	connectionPort = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_CONNECT_PORT);
		   	System.out.println("Connection Port is " + connectionPort );
	    	serverHost = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SERVER_HOST);
		   	System.out.println("Server Host is " + serverHost );
	    	senderCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SENDER_COMP_ID);
		   	System.out.println("Sender Comp ID is " + senderCompID );
	    	targetCompID = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_TARGET_COMP_ID);
		   	System.out.println("Target Comp ID is " + targetCompID );
		   	
		   	
	    }
	    catch( ConfigAttributeNotFound exc )
	    {
		    System.out.println("CONFIG VALUES DOES NOT EXIST");
		    exc.printStackTrace();
		    System.exit(0);	    
	    }
	*/	
	
}
/**
 * Insert the method's description here.
 * Creation date: (8/9/2002 3:50:48 PM)
 */
private void initializeXMLSetup() 
{
	String xmlSetupFile = null;
	String xmlUsersFile = null;
	String xmlSessionsFile = null;
	
	try
	{
		// get fixtools.xml file location
		xmlSetupFile = ConfigurationService.getValue(FixToolsConstants.FIXTOOLS_SETUP_FILE);
		xmlUsersFile = ConfigurationService.getValue(FixToolsConstants.FIXTOOLS_USERS_FILE);
		xmlSessionsFile = ConfigurationService.getValue(FixToolsConstants.FIXTOOLS_SESSIONS_FILE);			
	}
	catch( Exception e )
	{
		GuiUtil.displayError(e.getMessage(),"ConfigurationError");
		System.exit(0);		
	}
	

	
	UserManager userManager = UserManager.getInstance();
	// instantiate FixFieldXmlHandler
	SAXParser parser = new SAXParser();
	FixToolsUsersXmlHandler fixToolsUsersXmlHandler = new FixToolsUsersXmlHandler(userManager);
	FixToolsXmlHandler fixToolsXmlHandler = new FixToolsXmlHandler(userManager,orderTypesList, tifList);
	FixTestBenchXmlHandler fixTestBenchXmlHandler = new FixTestBenchXmlHandler(
														fcManager, testOrder);	
	
	parser.setDocumentHandler(fixToolsUsersXmlHandler);
	//parser.setErrorHandler( new FixSpecification.MyErrorHanler());

	try
	{
		//System.out.println("Here");
		//InputStream is = new FileInputStream("c:\\fixtest\\config\\fixfields.xml");
		//System.out.println("Here");		
		FileReader fr = new FileReader(xmlUsersFile);
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

		
	parser.setDocumentHandler(fixToolsXmlHandler);

	try
	{
		//System.out.println("Here");
		//InputStream is = new FileInputStream("c:\\fixtest\\config\\fixfields.xml");
		//System.out.println("Here");		
		FileReader fr = new FileReader(xmlSetupFile);
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

	parser.setDocumentHandler(fixTestBenchXmlHandler);

	try
	{
		//System.out.println("Here");
		//InputStream is = new FileInputStream("c:\\fixtest\\config\\fixfields.xml");
		//System.out.println("Here");		
		FileReader fr = new FileReader(xmlSessionsFile);
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
		GuiUtil.displayError("ConfigFile parameter not provided",
						"ConfigurationError");
		System.exit(0);
	}
	GuiUtil.displayInformation(cfgFile, "ConfigurationSuccessful");

	try
	{
		// initialize Configuration Service
		ConfigurationService.initialize( cfgFile );
	}
	catch( Exception e )
	{
		GuiUtil.displayError(e.getMessage(),"ConfigurationError");
		System.exit(0);
	}


	FixTestBench testBench = new FixTestBench("FixTestBench");
	testBench.pack();
	testBench.setVisible(true);
	//userManager.debugPrint();
	
	
	
	// initialize user login details




	// close application
	//System.exit(0);
	
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 5:50:54 PM)
 * @param server com.exsys.fix.tools.FixServerDef
 * @param client com.exsys.fix.tools.FixClientDef
 */
public void openFixSession(FixServerDef server, FixClientDef client) 
{


	String sessionKey = server.getKey() + ";" + client.getKey();
	statusPanel.addStatusMessage("Request to open Fix Session for "+ sessionKey);
	
	//FixToolsWindow ftw = new FixToolsWindow("FixToolsWindow for "+ sessionKey, server, client );
	FixToolsWindow ftw = new FixToolsWindow("FixToolsWindow for "+ sessionKey );
	ftw.pack();
	ftw.setVisible(true);
	
	
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
}
