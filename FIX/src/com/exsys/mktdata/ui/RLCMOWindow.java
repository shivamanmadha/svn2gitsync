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



public class RLCMOWindow
  	extends RLCMarketDataBaseHandler
  	implements ListSelectionListener
{
  // ========================================================================
  // ========================================================================
  public RLCMOWindow() throws ConfigFileNotFound, Exception
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

      mResponseSubject = ConfigurationService.getValue("MOResponseSubject");

    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

public void processMOMessage(RLCMOMessage rlcMsg)
{
	//System.out.println("processMOMessage");
	//System.out.println(rlcMsg.toString());
	mListModel.addElement(rlcMsg);
	try
	{
		Thread.sleep(200);
	}
	catch(Throwable e)
	{
	}

}

public void processRestoredMOMessage(RLCMOMessage rlcMsg)
{

	processMOMessage(rlcMsg);
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
        RLCMOMessage item = (RLCMOMessage)mList.getModel().getElementAt(selected);
        displayMessage(item);
      }

      //System.out.println("valueChanged:: "+e.toString());
      System.out.println("valueChanged:: ");
  }
  public void displayMessage(RLCMOMessage msg)
  {
	  tfCompleteInstrument.setText(msg.getCompleteInstrumentCode());
  	  tfActivation.setText(msg.getInstrumentActivationDateTime());
	  tfExpiration.setText(msg.getInstrumentExpirationDateTime());
	  String strIT = msg.getInstrumentType();
 	  tfInstrumentType.setText(strIT+" ("+
 	  			RLCLookup.lookupTagValue("InstrumentType",strIT)+")");
 	  // temporary setup to hold current price 	  
	  double price = (Double.valueOf(tfNominalValuePrice.getText())).doubleValue();
	  double tickSize = RLCMessage.calculateTickSize(
	  		msg.getNumberOfMinimumTickIncrement(),
	  		msg.getTickDisplayFormatType(),
	  		msg.getNumberOfDecimalsInDisplayedPrice(),
	  		msg.getVariableTickTableIndexCode(),
	  		price
	  );
	  
 	  tfTickSize.setText(String.valueOf(tickSize));
 	  
	  String strSI = msg.getImpliedSpreadIndicator();
 	  tfImpliedSpreadIndicator.setText(strSI+" ("+
 	  			RLCLookup.lookupTagValue("ImpliedSpreadIndicator",strSI)+")"); 	  
	  
	  tfProductGroup.setText(msg.getProductCode());
	  
	  
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

      String restore = ConfigurationService.getValue("restoreMO");
      if(restore.equals("Y"))
      {
 	     String inFile = ConfigurationService.getValue("MOInLogFile");
 	     ArrayList inList = RLCFileManager.getRLCMessagesList(inFile,"MO");

 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		RLCMOMessage msg = (RLCMOMessage)inList.get(i);
	    	processRestoredMOMessage(msg);
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
        BorderFactory.createTitledBorder("< MOMessage Display:>"),
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
   
    JLabel lblAct = new JLabel("Activation");
    lblAct.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblAct,c);
    tfActivation = new JTextField("");
    tfActivation.setEditable(false);
    //tfActivation.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfActivation,c); 
   
   
    JLabel lblExp = new JLabel("Expiration");
    lblExp.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblExp,c);
    tfExpiration = new JTextField("");
    tfExpiration.setEditable(false);
    //tfExpiration.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfExpiration,c);     
    
    JLabel lblIT = new JLabel("InstrumentType");
    lblIT.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblIT,c);
    tfInstrumentType = new JTextField("");
    tfInstrumentType.setEditable(false);
    //tfInstrumentType.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfInstrumentType,c); 


    JLabel lblNVP = new JLabel("NominalValuePrice");
    lblNVP.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblNVP,c);
    tfNominalValuePrice = new JTextField("1.0");
    //tfNominalValuePrice.setEditable(false);
    //tfNominalValuePrice.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfNominalValuePrice,c);

    JLabel lblTS = new JLabel("TickSize");
    lblTS.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblTS,c);
    tfTickSize = new JTextField("");
    tfTickSize.setEditable(false);
    //tfTickSize.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfTickSize,c);

    JLabel lblIS = new JLabel("ImpliedSpreadIndicator");
    lblIS.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblIS,c);
    tfImpliedSpreadIndicator = new JTextField("");
    tfImpliedSpreadIndicator.setEditable(false);
    //tfImpliedSpreadIndicator.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfImpliedSpreadIndicator,c);

    JLabel lblPG = new JLabel("ProductGroup");
    lblPG.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblPG,c);
    tfProductGroup = new JTextField("");
    tfProductGroup.setEditable(false);
    //tfProductGroup.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfProductGroup,c);


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
  private JTextField tfActivation = null;
  private JTextField tfExpiration = null;
  private JTextField tfInstrumentType = null;
  private JTextField tfNominalValuePrice = null;
  private JTextField tfTickSize = null; 
  private JTextField tfImpliedSpreadIndicator = null;
  private JTextField tfProductGroup = null;
    

}

