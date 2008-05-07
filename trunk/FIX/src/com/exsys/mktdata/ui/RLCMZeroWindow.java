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



public class RLCMZeroWindow
  	extends RLCMarketDataBaseHandler
  	implements ListSelectionListener
{
  // ========================================================================
  // ========================================================================
  public RLCMZeroWindow() throws ConfigFileNotFound, Exception
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

      mResponseSubject = ConfigurationService.getValue("MZeroResponseSubject");

    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

public void processMZeroMessage(RLCMZeroMessage rlcMsg)
{
	System.out.println("processMZeroMessage");
	//System.out.println(rlcMsg.toString());
	mListModel.addElement(rlcMsg);

}

public void processRestoredMZeroMessage(RLCMZeroMessage rlcMsg)
{

	processMZeroMessage(rlcMsg);
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
          BorderFactory.createTitledBorder("MO Messages Log"),
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
        RLCMZeroMessage item = (RLCMZeroMessage)mList.getModel().getElementAt(selected);
        displayMessage(item);
      }

      //System.out.println("valueChanged:: "+e.toString());
      System.out.println("valueChanged:: ");
  }
  public void displayMessage(RLCMZeroMessage msg)
  {
	  tfCompleteInstrument.setText(msg.getCompleteInstrumentCode());
  	  tfLastTradeQty.setText(msg.getLastTradeQuantity());
  	  tfLastTradePrice.setText(String.valueOf(msg.getLastPriceAsPrice()));


	  String strLPT = msg.getLastPriceType();
	   	  tfLastPriceType.setText(strLPT+" ("+
 	  			RLCLookup.lookupTagValue("LastPriceType",strLPT)+")");

	  tfHighPrice.setText(String.valueOf(msg.getHighestPriceAsPrice()));
	  tfLowPrice.setText(String.valueOf(msg.getLowestPriceAsPrice()));


 	  tfNetChange.setText(String.valueOf(msg.getActualNetChangeAsPrice()));




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

      String restore = ConfigurationService.getValue("restoreMZero");
      if(restore.equals("Y"))
      {
 	     String inFile = ConfigurationService.getValue("MZeroInLogFile");
 	     ArrayList inList = RLCFileManager.getRLCMessagesList(inFile,"M0");

 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		RLCMZeroMessage msg = (RLCMZeroMessage)inList.get(i);
	    	processRestoredMZeroMessage(msg);
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
        BorderFactory.createTitledBorder("< MZeroMessage Display:>"),
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

    JLabel lblAct = new JLabel("LastTradeQty");
    lblAct.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblAct,c);
    tfLastTradeQty = new JTextField("");
    tfLastTradeQty.setEditable(false);
    //tfLastTradeQty.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfLastTradeQty,c);


    JLabel lblExp = new JLabel("LastTradePrice");
    lblExp.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblExp,c);
    tfLastTradePrice = new JTextField("");
    tfLastTradePrice.setEditable(false);
    //tfLastTradePrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfLastTradePrice,c);

    JLabel lblIT = new JLabel("High Price");
    lblIT.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblIT,c);
    tfHighPrice = new JTextField("");
    tfHighPrice.setEditable(false);
    //tfHighPrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfHighPrice,c);

    JLabel lblLP = new JLabel("Low Price");
    lblLP.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblLP,c);
    tfLowPrice = new JTextField("");
    tfLowPrice.setEditable(false);
    //tfLowPrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfLowPrice,c);

    JLabel lblNVP = new JLabel("Net Change");
    lblNVP.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblNVP,c);
    tfNetChange = new JTextField("");
    tfNetChange.setEditable(false);
    //tfNetChange.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfNetChange,c);

    JLabel lblTS = new JLabel("Last Price Type");
    lblTS.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblTS,c);
    tfLastPriceType = new JTextField("");
    tfLastPriceType.setEditable(false);
    //tfLastPriceType.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfLastPriceType,c);




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
  private JTextField tfHighPrice = null;
  private JTextField tfLowPrice = null;
  private JTextField tfLastTradeQty = null;
  private JTextField tfLastTradePrice = null;
  private JTextField tfNetChange = null;
  private JTextField tfLastPriceType = null;


}

