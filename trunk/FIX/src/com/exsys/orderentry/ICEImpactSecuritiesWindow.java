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
import com.exsys.impact.message.*;
import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;
import com.exsys.orderentry.ui.*;


public class ICEImpactSecuritiesWindow
  extends BaseGUIApplication
  implements ImpactMessageProcessor,  ActionListener
{
  // ========================================================================
  // ========================================================================
  public ICEImpactSecuritiesWindow(String args[]) throws ConfigFileNotFound, Exception
  {
    super(args);
      processConfig();
      initializeSession(); // initialize jms session -- do this first...
      createWindows();
      //restoreFromFix();

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
   public boolean processProductDefinitionResponse(byte[] msg)
  {
  	return true;
  }
  // ========================================================================
  // ========================================================================
  public boolean processProductDefinitionResponse(ProductDefinitionResponse response)
  {

    System.out.println("processProductDefinitionResponse");
    String str = "Symbol = " + response.MarketID;
    str += "  ("+ MessageUtil.toString(response.MarketDesc) + ")";
    if(response.IsOptions == 'Y')
    {
    	str += " Maturity " + response.MaturityMonth+"/"+response.MaturityDay+"/"+response.MaturityYear;
    }
  	str += " Status = "+ response.TradingStatus;
  	str += "(";
  	str += ImpactMessageConstants.getTradeStatusDesc(response.TradingStatus);
  	str += ")";    
  	mListModel.addElement(str);
    return true;
/*
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

*/
   }

  public boolean processMarketDataRequest(RequestFeedByMarketID mdReq)
  {
  	return true;
  }
   // ========================================================================
  // ========================================================================
  public boolean processProductDefinitionRequest(ProductDefinitionRequest msg)
  {
    System.out.println("processProductDefinitionRequest");
    String secType = String.valueOf(msg.MarketType);
    if(requestedSymbols.contains(secType))
    {
    	mListModel.addElement("ALREADY REQUESTED");
    	return true;
    }
    try {
      mSessionMgr.sendImpactMessage(msg, mRequestSubject+"SECREQ");
    } catch(Exception exp){ exp.printStackTrace(); }
    

    mListModel.addElement(msg.toString());
    requestedSymbols.add(secType);
    return true;
  }
  public void processRestoredSecurityDefinitionRequest(ProductDefinitionRequest msg)
  {
  	
    String secType = String.valueOf(msg.MarketType);
    mListModel.addElement(msg.toString());
    requestedSymbols.add(secType);
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
    mSecuritiesWindow = new ImpactSecuritiesWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,this,mUniqueIDFile,mExchange);
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
      mSessionMgr = new ImpactDataSessionManager();
      mSessionMgr.startImpactSession();
      mSessionMgr.receiveImpactMessages(this,mResponseSubject);
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
  
public boolean processMarketSnapshot(MarketSnapshotMessage msg)
{
	return true;
}
public boolean processMarketSnapshotOrder(MarketSnapshotOrderMessage msg)
{
	return true;
}
public boolean processAddModifyOrder(AddModifyOrderMessage msg)
{
	return true;
}
public boolean processDeleteOrder(DeleteOrderMessage msg)
{
	return true;
}
public boolean processTrade(TradeMessage msg)
{
	return true;
}
public boolean processCancelledTrade(CancelledTradeMessage msg)
{
	return true;
}
public boolean processMarketStatistics(MarketStatisticsMessage msg)
{
	return true;
}
public boolean processOpenPrice(OpenPriceMessage msg)
{
	return true;
}
public boolean processOpenInterest(OpenInterestMessage msg)
{
	return true;
}
public boolean processSettlementPrice(SettlementPriceMessage msg)
{
	return true;
}
public boolean processMarketStateChange(MarketStateChangeMessage msg)
{
	return true;
}
  
public boolean processMarketSnapshot(byte[] msg)
{
	return true;
}
public boolean processMarketSnapshotOrder(byte[] msg)
{
	return true;
}
public boolean processAddModifyOrder(byte[] msg)
{
	return true;
}
public boolean processDeleteOrder(byte[] msg)
{
	return true;
}
public boolean processTrade(byte[] msg)
{
	return true;
}
public boolean processCancelledTrade(byte[] msg)
{
	return true;
}
public boolean processMarketStatistics(byte[] msg)
{
	return true;
}
public boolean processOpenPrice(byte[] msg)
{
	return true;
}
public boolean processOpenInterest(byte[] msg)
{
	return true;
}
public boolean processSettlementPrice(byte[] msg)
{
	return true;
}
public boolean processMarketStateChange(byte[] msg)
{
	return true;
}  
  
/*
  private void restoreFromFix()
  {
    try
    {

      String restore = ConfigurationService.getValue("restore");
      if(restore.equals("Y"))
      {
 	     String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     ArrayList outList = ImpactSessionManager.getApplicationMessagesList(outFile);
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
*/
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
      frame.setContentPane(new ICEImpactSecuritiesWindow(args));
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
      */
      new ICEImpactSecuritiesWindow(args);
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
  private ImpactDataSessionManager mSessionMgr = null;
  private JList mList = null;
  private DefaultListModel mListModel = null;
  private ImpactSecuritiesWindow mSecuritiesWindow = null;

  private ArrayList requestedSymbols = new ArrayList();

  private JTabbedPane mTabbedPane = null;
  private JFrame mTheFrame = null;

  private final static String tagSECURITIES = "Securities";
  //private final static String tagCXLCR = "Cancel/Replace";
  //private final static String tagDETAIL = "Detail";




}

