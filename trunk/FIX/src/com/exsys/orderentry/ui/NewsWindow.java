package com.exsys.orderentry.ui;

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


public class NewsWindow
  implements NewsMessageProcessor
{
  // ========================================================================
  // ========================================================================
  public NewsWindow(String targetCompID,
                     String SenderCompID,String SenderSubID,String UserName,
                     String respSub,
                     String idFile,
                     String exchange) throws ConfigFileNotFound, Exception
  {
      //processConfig();
    super();
    mTargetCompID = targetCompID;
    mSenderCompID = SenderCompID;
    mSenderSubID = SenderSubID;
    mUserName = UserName;
    mResponseSubject = respSub;
    mExchange = exchange;

      initializeSession(); // initialize jms session -- do this first...
      createWindows();
      restoreFromFix();
/*
      mTheFrame = new JFrame("ICE News Window");
        mTheFrame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        });
      //mTheFrame.setContentPane(this);
      mTheFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      mTheFrame.pack();
      mTheFrame.setVisible(true);
*/     

  }
  // ========================================================================
  // ========================================================================
  public void processNews(FixNews news)
  {

    System.out.println("processNews");

    String str = " HeadLine " +  news.getHeadLine();
    str += " Urgency = " + news.getUrgency();
    str += " UserName = " + news.getUserName();
    mListModel.addElement("<BEGIN>" + str);
    if(news.getLinesOfText() > 0 )
    {
   	 	// add details
   	 	String key = "33";
  		ArrayList rgArray = news.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {

  				FixRGLinesOfText_B rg = (FixRGLinesOfText_B)rgArray.get(i);
  				String info = "       Text =" + rg.getText();
  				mListModel.addElement(info);
   			}
   			mListModel.addElement("<END>" + str);
  		}


    }
    else
    {
    	mListModel.addElement("NO NEWS ");
    }


   }

  public void processRestoredNews(FixNews news)
  {

	processNews(news);
  }
  // ========================================================================
  // ========================================================================
  private void createWindows() throws Exception
  {
  	/*
    setLayout(new GridLayout(0,1));
    setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("< Securities Window - Trader:" +mSenderCompID+" >"),
        BorderFactory.createEmptyBorder(0,0,0,0)
      )
    );
    add(createResponseLog());
    */
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
  public Component createResponseLog()
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
          BorderFactory.createTitledBorder("News Messages Log"),
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
      mSessionMgr = new NewsSessionManager();
      mSessionMgr.startNewsSession();
      mSessionMgr.receiveNewsMessages(this,mResponseSubject);
    }
    catch( Exception exc )
    {
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
	if( msgType.equals(FixConstants.FIX_MSGTYPE_NEWS))
	{
		processRestoredNews((FixNews)msg );
	}
  }

  // ========================================================================
  // ========================================================================
  private String mTargetCompID;
  private String mSenderCompID;
  private String mSenderSubID;
  private String mUserName;
  private String mExchange;
  private String mResponseSubject = null;
  private NewsSessionManager mSessionMgr = null;
  private JList mList = null;
  private DefaultListModel mListModel = null;
  private JFrame mTheFrame = null;


}

