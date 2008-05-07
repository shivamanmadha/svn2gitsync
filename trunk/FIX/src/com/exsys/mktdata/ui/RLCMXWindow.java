package com.exsys.mktdata.ui;

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
import com.exsys.mktdata.session.*;
import com.exsys.mktdata.message.*;



public class RLCMXWindow
  	extends RLCMarketDataBaseHandler
  	implements ListSelectionListener
{
  // ========================================================================
  // ========================================================================
  public RLCMXWindow() throws ConfigFileNotFound, Exception
  {
      //processConfig();
    super();

	  processConfig();
      initializeSession(); // initialize jms session -- do this first...
      createWindows();
      //restoreFromFile();


  }
  private void processConfig()
  {
    try
    {

      mResponseSubject = ConfigurationService.getValue("MXResponseSubject");

    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

public void processMXMessage(RLCMXMessage rlcMsg)
{
	System.out.println("processMXMessage");
	//System.out.println(rlcMsg.toString());
	mListModel.addElement(rlcMsg);

}

public void processRestoredMXMessage(RLCMXMessage rlcMsg)
{

	processMXMessage(rlcMsg);
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
    mList.addListSelectionListener(this);


    JScrollPane areaScrollPane = new JScrollPane(mList);
    areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setPreferredSize(new Dimension(600,400));
    areaScrollPane.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("MX Messages Log"),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ),
        areaScrollPane.getBorder()
      )
    );
    JPanel respPane = new JPanel();
    respPane.add(areaScrollPane);

   return respPane;
  }
  public void valueChanged(ListSelectionEvent e)
  {
      int selected = mList.getSelectedIndex();
      if(selected >= 0)
      {
        RLCMXMessage item = (RLCMXMessage)mList.getModel().getElementAt(selected);
        displayMessage(item);
      }

      //System.out.println("valueChanged:: "+e.toString());
      System.out.println("valueChanged:: ");
  }
  public void displayMessage(RLCMXMessage msg)
  {
	  tfCompleteInstrument.setText(msg.getCompleteInstrumentCode());

  	  tfBidQty.setText(msg.getBidQuantity());
  	  tfBidNum.setText(msg.getNumberOfBidQuotes());
  	  tfBidPrice.setText(String.valueOf(msg.getBidPriceAsPrice()));


  	  tfAskQty.setText(msg.getAskQuantity());
  	  tfAskNum.setText(msg.getNumberOfAskQuotes());
  	  tfAskPrice.setText(String.valueOf(msg.getAskPriceAsPrice()));




  }
  // ========================================================================
  // ========================================================================
  private void initializeSession()
  {
    try
    {
      mSessionMgr = new RLCMarketDataSessionManager();
      mSessionMgr.startMarketDataSession();
      mSessionMgr.receiveMarketDataMessages(this,mResponseSubject);
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }

  private void restoreFromFile()
  {
    try
    {

      String restore = ConfigurationService.getValue("restoreMX");
      if(restore.equals("Y"))
      {
 	     String inFile = ConfigurationService.getValue("MXInLogFile");
 	     ArrayList inList = RLCFileManager.getRLCMessagesList(inFile,"MX");

 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		RLCMXMessage msg = (RLCMXMessage)inList.get(i);
	    	processRestoredMXMessage(msg);
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

  public Component createComponent()
  {
	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new GridLayout(0,1));
	mainPanel.setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("< MXMessage Display:>"),
        BorderFactory.createEmptyBorder(0,0,0,0)
      )
    );




   // System.out.println("OrderWindow::createComponent");

    Dimension dimField = new Dimension( 220, 20);
    Dimension dimField1 = new Dimension( 20, 20);
    EmptyBorder border = new EmptyBorder(new Insets(0,0,0,0));
    JPanel contentPanel = new JPanel(new GridBagLayout());
    //ordpane.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    // add space around all component to avoid clutter
    //c.insets = new Insets(10,10,10,10);
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
	int y = 1;
	int x = 1;


    JLabel lblCI = new JLabel("CompleteInstrument");
    lblCI.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblCI,c);
    tfCompleteInstrument = new JTextField("");
    tfCompleteInstrument.setEditable(false);
    //tfCompleteInstrument.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfCompleteInstrument,c);

    JLabel lblBidNum = new JLabel("Bid Depth");
    lblBidNum.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBidNum,c);
    tfBidNum = new JTextField("");
    tfBidNum.setEditable(false);
    //tfBidNum.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBidNum,c);

    JLabel lblAskNum = new JLabel("Ask Depth");
    lblAskNum.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblAskNum,c);
    tfAskNum = new JTextField("");
    tfAskNum.setEditable(false);
    //tfAskNum.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfAskNum,c);


    JLabel lblBidQty = new JLabel("Bid Qty");
    lblBidQty.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBidQty,c);
    tfBidQty = new JTextField("");
    tfBidQty.setEditable(false);
    //tfBidQty.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBidQty,c);

    JLabel lblAskQty = new JLabel("Ask Qty");
    lblAskQty.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblAskQty,c);
    tfAskQty = new JTextField("");
    tfAskQty.setEditable(false);
    //tfAskQty.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfAskQty,c);


    JLabel lblBidPrice = new JLabel("Bid Price");
    lblBidPrice.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBidPrice,c);
    tfBidPrice = new JTextField("");
    tfBidPrice.setEditable(false);
    //tfBidPrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBidPrice,c);

    JLabel lblAskPrice = new JLabel("Ask Price");
    lblAskPrice.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblAskPrice,c);
    tfAskPrice = new JTextField("");
    tfAskPrice.setEditable(false);
    //tfAskPrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfAskPrice,c);


	mainPanel.add(contentPanel);
	mainPanel.add(createResponseLog());
	restoreFromFile();
    return mainPanel;
  }


  // ========================================================================
  // ========================================================================

  private String mResponseSubject = null;
  private RLCMarketDataSessionManager mSessionMgr = null;
  private JList mList = null;
  private DefaultListModel mListModel = null;

  // display details
  private JTextField tfCompleteInstrument = null;

  private JTextField tfBidNum = null;
  private JTextField tfBidQty = null;
  private JTextField tfBidPrice = null;
  private JTextField tfAskNum = null;
  private JTextField tfAskQty = null;
  private JTextField tfAskPrice = null;



}

