package com.exsys.orderentry;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import com.exsys.application.*;
import com.exsys.service.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.common.business.*;
import com.exsys.fix.message.*;
import com.exsys.fix.session.*;
import com.exsys.orderentry.ui.*;


public class ICESecuritiesWindow
  extends BaseGUIApplication
  implements SecuritiesMessageProcessor,  ActionListener
{
  // ========================================================================
  // ========================================================================
  public ICESecuritiesWindow(String args[]) throws ConfigFileNotFound, Exception
  {
    super(args);
      processConfig();
      initializeSession(); // initialize jms session -- do this first...
      createWindows();
      restoreFromFix();

      mTheFrame = new JFrame("ICE Securities Window");
        mTheFrame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
      mTheFrame.setContentPane(this);
      mTheFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      mTheFrame.pack();
      mTheFrame.setVisible(true);

  }
  // ========================================================================
  // ========================================================================
  public void processSecurityDefinitionResponse(FixSecurityDefinitionResponse response)
  {
  	
    System.out.println("processSecurityDefinitionResponse");
    
    String str = " Response ID " +  response.getSecurityResponseID() +" For " + response.getSecurityReqID();
    str += " Number of Symbols = " + response.getNoRelatedSym();
    str += " Total # of Securities = " + response.getTotalNumSecurities();
    str += " List Seq No = " + response.getListSeqNoAsString();
    str += " Number of Reports = " + response.getNoRpts();
    mListModel.addElement("<BEGIN>" + str);
    if(response.getNoRelatedSym() > 0 )
    {
   	 	// add details
   	 	String key = "146";
  		ArrayList rgArray = response.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {
   				
  				FixRGNoRelatedSym rg = (FixRGNoRelatedSym)rgArray.get(i);
  				String info = "       Symbol =" + rg.getUnderlyingSymbol();
  				info += " "+rg.getUnderlyingSecurityDes();
  				info += " "+rg.getUnderlyingMaturityMonthYearAsString();
  				info += " "+rg.getUnderlyingMaturityDayAsString();
  				
  				info += " Status = "+rg.getSecurityTradingStatus();
  				info += "(";
  				info += FixLookup.lookupTagValue("326",rg.getSecurityTradingStatusAsString());
  				info += ")";
  				  				
  				info += " Incr Price = "+rg.getIncrementPriceAsString();
  				info += " Incr Qty = "+rg.getIncrementQtyAsString();
  				info += " Product Type = "+rg.getProductType();
  				info += " Prim Leg Sym = "+rg.getPrimaryLegSymbol();
  				info += " Sec Leg Sym = "+rg.getSecondaryLegSymbol();  				
  				mListModel.addElement(info);
   			}	
   			mListModel.addElement("<END>" + str);	
  		}	
    	
    	
    }
    else
    {
    	mListModel.addElement("REJECTED - "+response.getText());
    }
    
    
   }

   // ========================================================================
  // ========================================================================
  public void processSecurityDefinitionRequest(FixSecurityDefinitionRequest msg)
  {
    System.out.println("processSecurityDefinitionRequest");
    if(requestedSymbols.contains(msg.getSecurityType()))
    {
    	mListModel.addElement("ALREADY REQUESTED");
    	return;
    }
    try {
      mSessionMgr.sendSecurityDefinitionRequest(msg, mRequestSubject+"SECREQ");
    } catch(Exception exp){ exp.printStackTrace(); }
    String str = "Security Request - "+msg.getSecurityReqID();
    str += " RequestType = "+ msg.getSecurityRequestTypeAsString();
    str += " Security Type = "+ msg.getSecurityType();
    
    mListModel.addElement(str);
    requestedSymbols.add(msg.getSecurityType());
  }
  public void processRestoredSecurityDefinitionRequest(FixSecurityDefinitionRequest msg)
  {

    String str = "Security Request - "+msg.getSecurityReqID();
    str += " RequestType = "+ msg.getSecurityRequestTypeAsString();
    str += " Security Type = "+ msg.getSecurityType();
    
    mListModel.addElement(str);
    requestedSymbols.add(msg.getSecurityType());
  } 
    // ========================================================================
  // ========================================================================
  public void actionPerformed(ActionEvent e)
  {
    System.out.println("actionPerformed:: "+e.toString());
  }
  // ========================================================================
  // ========================================================================
  private void createWindows() throws Exception
  {
    setLayout(new GridLayout(0,1));
    setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("< Securities Window - Trader:" +mSenderCompID+" >"),
        BorderFactory.createEmptyBorder(0,0,0,0)
      )
    );
    add(createTabbedPanes());
    add(createResponseLog());
  }
  // ========================================================================
  // ========================================================================
  private Component createTabbedPanes() throws Exception
  {
    mSecuritiesWindow = new SecuritiesWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,this,mUniqueIDFile,mExchange);
    //mCXRWindow = new CxlCRWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,this, mUniqueIDFile,mExchange);
    mTabbedPane = new JTabbedPane();
    mTabbedPane.addTab(tagSECURITIES, mSecuritiesWindow.createComponent());
    //mTabbedPane.addTab(tagCXLCR, mCXRWindow.createComponent());
    //mTabbedPane.addTab(tagDETAIL, getContent(tagDETAIL));
    return mTabbedPane;
  }
  // ========================================================================
  // ========================================================================
  private Component getContent(String title)
  {
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    return contentPane;
  }
  // ========================================================================
  // ========================================================================
  private Component createResponseLog()
  {
    mListModel = new DefaultListModel();
    mList = new JList(mListModel);
    //mList.addListSelectionListener(this);


    JScrollPane areaScrollPane = new JScrollPane(mList);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(600,400));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Security Definition Responses Log"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );
    JPanel respPane = new JPanel();
    respPane.add(areaScrollPane);

   return respPane;
  }
  // ========================================================================
  // ========================================================================
  private void initializeSession()
  {
    try
    {
      mSessionMgr = new SecuritiesSessionManager();
      mSessionMgr.startSecuritiesSession();
      mSessionMgr.receiveSecuritiesMessages(this,mResponseSubject);
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }
  // ========================================================================
  // ========================================================================
  private void processConfig()
  {
    try
    {
      mResponseSubject = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_RESP_SUB_SUB);
      System.out.println("Resp Publish Subject is " + mResponseSubject );
      mRequestSubject = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_REQ_PUB_SUB);
      System.out.println("Req Publish Subject is " + mRequestSubject );
      mSenderCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_SENDER_COMPANY_ID);
      System.out.println("Sender Comp ID is " + mSenderCompID );
      mTargetCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_TARGET_COMPANY_ID);
      System.out.println("Target Comp ID is " + mTargetCompID );
      mUniqueIDFile = ConfigurationService.getValue("UniqueIDFile");
      System.out.println("UniqueIDFile is " + mUniqueIDFile );
      mExchange = ConfigurationService.getValue("Exchange");
      Logger.debug("Exchange is " + mExchange );
      if(mExchange.equals("ICE"))
      {
   		   mSenderSubID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_SENDER_SUB_ID);
   		   System.out.println("Sender Sub ID is " + mSenderSubID );
   		   mUserName = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_USER_NAME);
   		   System.out.println("UserName is " + mUserName );

   		   mTraderName = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_TRADER_NAME);
   		   System.out.println("TraderName is " + mTraderName );

      }


    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

  private void restoreFromFix()
  {
    try
    {

      String restore = ConfigurationService.getValue("restore");
      if(restore.equals("Y"))
      {
 	     String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("FixInLogFile");
 	     ArrayList inList = FixSessionManager.getApplicationMessagesList(inFile);
 	         
 	     // first process out list
 	    for(int i=0; i<outList.size();i++)
	    {
    		FixMessage msg = (FixMessage)outList.get(i);
	    	String msgType = msg.getMessageType();
	    		System.out.println("Restoring - "+msgType);
	    	handleRestoredMessage(msgType,msg);
	    }
	    


 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		FixMessage msg = (FixMessage)inList.get(i);
	    	String msgType = msg.getMessageType();
	    	System.out.println("Restoring - "+msgType);
	    	handleRestoredMessage(msgType,msg);
	    }

      }


    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }


  private void handleRestoredMessage(String msgType,FixMessage msg)
  {
	if( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST))
	{
		processRestoredSecurityDefinitionRequest( (FixSecurityDefinitionRequest)msg );
	}
	else if( msgType.equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE))
	{
		processSecurityDefinitionResponse( (FixSecurityDefinitionResponse)msg );
	}
  }

  // ========================================================================
  // ========================================================================
  public static void main(String args[])
  {
    try
    {
      /*
      JFrame frame = new JFrame("Exchange API Trading Suite");
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      frame.setContentPane(new ICESecuritiesWindow(args));
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
      */
      new ICESecuritiesWindow(args);
    }
    catch(Exception exc)
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }
  // ========================================================================
  // ========================================================================
  private String mResponseSubject = null;
  private String mRequestSubject = null;
  private String mSenderCompID = null;
  private String mSenderSubID = null;
  private String mUserName = null;
  private String mTraderName = null;
  private String mTargetCompID = null;
  private String mUniqueIDFile = null;
  
  private String mExchange = null;
  private SecuritiesSessionManager mSessionMgr = null;
  private JList mList = null;
  private DefaultListModel mListModel = null;
  private SecuritiesWindow mSecuritiesWindow = null;
  
  private ArrayList requestedSymbols = new ArrayList();
  
  private JTabbedPane mTabbedPane = null;
  private JFrame mTheFrame = null;

  private final static String tagSECURITIES = "Securities";
  //private final static String tagCXLCR = "Cancel/Replace";
  //private final static String tagDETAIL = "Detail";

public void processCancel(Cancel cancel){};
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:49 AM)
 * @param response com.exsys.fix.message.FixExecutionReport
 */
public void processExecutionReport(FixExecutionReport response){};
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processOrder(FixOrder ord){};
//public void processOrder(FixOrder ord, String rawMsg ){};
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processQuote(RealTimeQuote quote){};

/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancel(FixCancel cxl){};

/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processStatusRequest(FixOrderStatusRequest stat){};
/**
 * Insert the method's description here.
 * Creation date: (1/31/2002 5:28:12 AM)
 * @param ord com.exsys.fix.message.FixOrder
 */
public void processCancelReplace(FixCancelReplace cxr){};

public void processCancelReject(FixCancelReject cxlRej){};



}

