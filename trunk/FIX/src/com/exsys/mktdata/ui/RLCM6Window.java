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



public class RLCM6Window
  	extends RLCMarketDataBaseHandler
  	implements ListSelectionListener
{
  // ========================================================================
  // ========================================================================
  public RLCM6Window() throws ConfigFileNotFound, Exception
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

      mResponseSubject = ConfigurationService.getValue("M6ResponseSubject");

    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

public void processM6Message(RLCM6Message rlcMsg)
{
	System.out.println("processM6Message");
	//System.out.println(rlcMsg.toString());
	mListModel.addElement(rlcMsg);

}

public void processRestoredM6Message(RLCM6Message rlcMsg)
{

	processM6Message(rlcMsg);
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
        RLCM6Message item = (RLCM6Message)mList.getModel().getElementAt(selected);
        displayMessage(item);
      }

      //System.out.println("valueChanged:: "+e.toString());
      System.out.println("valueChanged:: ");
  }
  public void displayMessage(RLCM6Message msg)
  {
	  tfCompleteInstrument.setText(msg.getCompleteInstrumentCode());
  	  tfTotalQty.setText(msg.getTotalTradedDailyQuantity());
	  tfTradeQty.setText(msg.getTradeQuantity());


 	  tfTradePrice.setText(String.valueOf(msg.getTradePriceAsPrice()));


 	  tfNetChange.setText(String.valueOf(msg.getNetChangeAsPrice()));

	  tfVariationInd.setText(msg.getPriceVariation());


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

      String restore = ConfigurationService.getValue("restoreM6");
      if(restore.equals("Y"))
      {
 	     String inFile = ConfigurationService.getValue("M6InLogFile");
 	     ArrayList inList = RLCFileManager.getRLCMessagesList(inFile,"M6");

 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		RLCM6Message msg = (RLCM6Message)inList.get(i);
	    	processRestoredM6Message(msg);
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
        BorderFactory.createTitledBorder("< M6Message Display:>"),
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

    JLabel lblAct = new JLabel("TotalQty");
    lblAct.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblAct,c);
    tfTotalQty = new JTextField("");
    tfTotalQty.setEditable(false);
    //tfTotalQty.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfTotalQty,c);


    JLabel lblExp = new JLabel("TradeQty");
    lblExp.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblExp,c);
    tfTradeQty = new JTextField("");
    tfTradeQty.setEditable(false);
    //tfTradeQty.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfTradeQty,c);

    JLabel lblIT = new JLabel("TradePrice");
    lblIT.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblIT,c);
    tfTradePrice = new JTextField("");
    tfTradePrice.setEditable(false);
    //tfTradePrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfTradePrice,c);


    JLabel lblNVP = new JLabel("Net Change");
    lblNVP.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblNVP,c);
    tfNetChange = new JTextField("");
    tfNetChange.setEditable(false);
    //tfNetChange.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfNetChange,c);

    JLabel lblTS = new JLabel("Variation Ind");
    lblTS.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblTS,c);
    tfVariationInd = new JTextField("");
    tfVariationInd.setEditable(false);
    //tfVariationInd.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfVariationInd,c);




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
  private JTextField tfTotalQty = null;
  private JTextField tfTradeQty = null;
  private JTextField tfTradePrice = null;
  private JTextField tfNetChange = null;
  private JTextField tfVariationInd = null;


}

