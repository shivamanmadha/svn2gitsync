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



public class RLCM8Window
  	extends RLCMarketDataBaseHandler
  	implements ListSelectionListener
{
  // ========================================================================
  // ========================================================================
  public RLCM8Window() throws ConfigFileNotFound, Exception
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

      mResponseSubject = ConfigurationService.getValue("M8ResponseSubject");

    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

public void processM8Message(RLCM8Message rlcMsg)
{
	System.out.println("processM8Message");
	//System.out.println(rlcMsg.toString());
	mListModel.addElement(rlcMsg);

}

public void processRestoredM8Message(RLCM8Message rlcMsg)
{

	processM8Message(rlcMsg);
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
          BorderFactory.createTitledBorder("M8 Messages Log"),
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
        RLCM8Message item = (RLCM8Message)mList.getModel().getElementAt(selected);
        displayMessage(item);
      }

      //System.out.println("valueChanged:: "+e.toString());
      System.out.println("valueChanged:: ");
  }
  public void displayMessage(RLCM8Message msg)
  {
	  tfCompleteInstrument.setText(msg.getCompleteInstrumentCode());
  	  tfUnfilledQty.setText(msg.getUnfilledQuantitiesAtOpening());
	  tfUnfilledQtySide.setText(msg.getUnfilledQuantitySideAtOpening());


 	  tfTheoPrice.setText(String.valueOf(msg.getTheoreticalPriceAsPrice()));


 	  tfSimBuyPrice.setText(String.valueOf(msg.getSimulatedBuyLimitPriceAsPrice()));

	  tfSimSellPrice.setText(String.valueOf(msg.getSimulatedSellLimitPriceAsPrice()));


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

      String restore = ConfigurationService.getValue("restoreM8");
      if(restore.equals("Y"))
      {
 	     String inFile = ConfigurationService.getValue("M8InLogFile");
 	     ArrayList inList = RLCFileManager.getRLCMessagesList(inFile,"M8");

 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		RLCM8Message msg = (RLCM8Message)inList.get(i);
	    	processRestoredM8Message(msg);
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
        BorderFactory.createTitledBorder("< M8Message Display:>"),
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

    JLabel lblAct = new JLabel("UnfilledQty");
    lblAct.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblAct,c);
    tfUnfilledQty = new JTextField("");
    tfUnfilledQty.setEditable(false);
    //tfUnfilledQty.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfUnfilledQty,c);


    JLabel lblExp = new JLabel("UnfilledQtySide");
    lblExp.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblExp,c);
    tfUnfilledQtySide = new JTextField("");
    tfUnfilledQtySide.setEditable(false);
    //tfUnfilledQtySide.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfUnfilledQtySide,c);

    JLabel lblIT = new JLabel("Theo Open Price");
    lblIT.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblIT,c);
    tfTheoPrice = new JTextField("");
    tfTheoPrice.setEditable(false);
    //tfTheoPrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfTheoPrice,c);


    JLabel lblNVP = new JLabel("Sim Buy Price");
    lblNVP.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblNVP,c);
    tfSimBuyPrice = new JTextField("");
    tfSimBuyPrice.setEditable(false);
    //tfSimBuyPrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSimBuyPrice,c);

    JLabel lblTS = new JLabel("Sim Sell Price");
    lblTS.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblTS,c);
    tfSimSellPrice = new JTextField("");
    tfSimSellPrice.setEditable(false);
    //tfSimSellPrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSimSellPrice,c);




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
  private JTextField tfUnfilledQty = null;
  private JTextField tfUnfilledQtySide = null;
  private JTextField tfTheoPrice = null;
  private JTextField tfSimBuyPrice = null;
  private JTextField tfSimSellPrice = null;


}

