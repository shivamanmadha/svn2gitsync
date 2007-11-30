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



public class RLCMAWindow
  	extends RLCMarketDataBaseHandler
  	implements ListSelectionListener
{
  // ========================================================================
  // ========================================================================
  public RLCMAWindow() throws ConfigFileNotFound, Exception
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

      mResponseSubject = ConfigurationService.getValue("MAResponseSubject");

    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

public void processMAMessage(RLCMAMessage rlcMsg)
{
	System.out.println("processMAMessage");
	//System.out.println(rlcMsg.toString());
	mListModel.addElement(rlcMsg);

}

public void processRestoredMAMessage(RLCMAMessage rlcMsg)
{

	processMAMessage(rlcMsg);
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
          BorderFactory.createTitledBorder("MAMessages Log"),
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
      	clearWindow();
        RLCMAMessage item = (RLCMAMessage)mList.getModel().getElementAt(selected);
        displayMessage(item);
      }

      //System.out.println("valueChanged:: "+e.toString());
      System.out.println("valueChanged:: ");
  }
  public void clearWindow()
  {
		 tfBuyNum1.setText("");
		 tfBuy1.setText("");
		 tfSellNum1.setText("");
		 tfSell1.setText("");
		 tfBuyNum2.setText("");
		 tfBuy2.setText("");
		 tfSellNum2.setText("");
		 tfSell2.setText("");
		 tfBuyNum3.setText("");
		 tfBuy3.setText("");
		 tfSellNum3.setText("");
		 tfSell3.setText("");
		 tfBuyNum4.setText("");
		 tfBuy4.setText("");
		 tfSellNum4.setText("");
		 tfSell4.setText("");
		 tfBuyNum5.setText("");
		 tfBuy5.setText("");
		 tfSellNum5.setText("");
		 tfSell5.setText("");

  	
  }
  public void displayMessage(RLCMAMessage msg)
  {
	  tfCompleteInstrument.setText(msg.getCompleteInstrumentCode());
  	  tfFlags.setText(msg.getChangeOfLimitFlag());

		 String str = msg.getChangeOfLimitFlag();
		 int index=0;
		 for( int i=0; i<5; i++)
		 {
		 	if(str.charAt(i) == '1')
		 	{
		 		if( (i+1) == 1)
		 		{
		 			tfBuyNum1.setText(msg.getNumberOfBuyOrders(index));
		 			tfBuy1.setText(msg.getBuyLimitQuantityAsint(index)+"@"+msg.getBuyLimitPriceAsPrice(index));
		 			tfSellNum1.setText(msg.getNumberOfSellOrders(index));
		 			tfSell1.setText(msg.getSellLimitQuantityAsint(index)+"@"+msg.getSellLimitPriceAsPrice(index));
		 		}
		 		if( (i+1) == 2)
		 		{
		 			tfBuyNum2.setText(msg.getNumberOfBuyOrders(index));
		 			tfBuy2.setText(msg.getBuyLimitQuantityAsint(index)+"@"+msg.getBuyLimitPriceAsPrice(index));
		 			tfSellNum2.setText(msg.getNumberOfSellOrders(index));
		 			tfSell2.setText(msg.getSellLimitQuantityAsint(index)+"@"+msg.getSellLimitPriceAsPrice(index));
		 		}
		 		if( (i+1) == 3)
		 		{
		 			tfBuyNum3.setText(msg.getNumberOfBuyOrders(index));
		 			tfBuy3.setText(msg.getBuyLimitQuantityAsint(index)+"@"+msg.getBuyLimitPriceAsPrice(index));
		 			tfSellNum3.setText(msg.getNumberOfSellOrders(index));
		 			tfSell3.setText(msg.getSellLimitQuantityAsint(index)+"@"+msg.getSellLimitPriceAsPrice(index));
		 		}
		 		if( (i+1) == 4)
		 		{
		 			tfBuyNum4.setText(msg.getNumberOfBuyOrders(index));
		 			tfBuy4.setText(msg.getBuyLimitQuantityAsint(index)+"@"+msg.getBuyLimitPriceAsPrice(index));
		 			tfSellNum4.setText(msg.getNumberOfSellOrders(index));
		 			tfSell4.setText(msg.getSellLimitQuantityAsint(index)+"@"+msg.getSellLimitPriceAsPrice(index));
		 		}
		 		if( (i+1) == 5)
		 		{
		 			tfBuyNum5.setText(msg.getNumberOfBuyOrders(index));
		 			tfBuy5.setText(msg.getBuyLimitQuantityAsint(index)+"@"+msg.getBuyLimitPriceAsPrice(index));
		 			tfSellNum5.setText(msg.getNumberOfSellOrders(index));
		 			tfSell5.setText(msg.getSellLimitQuantityAsint(index)+"@"+msg.getSellLimitPriceAsPrice(index));
		 		}		 				 				 				 		
		 		
				index++;
		 		
		 	}
		 		
		 }





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

      String restore = ConfigurationService.getValue("restoreMA");
      if(restore.equals("Y"))
      {
 	     String inFile = ConfigurationService.getValue("MAInLogFile");
 	     ArrayList inList = RLCFileManager.getRLCMessagesList(inFile,"MA");

 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		RLCMAMessage msg = (RLCMAMessage)inList.get(i);
	    	processRestoredMAMessage(msg);
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
        BorderFactory.createTitledBorder("< MAMessage Display:>"),
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

    JLabel lblFlags = new JLabel("ChangeFlags");
    lblFlags.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblFlags,c);
    tfFlags = new JTextField("");
    tfFlags.setEditable(false);
    //tfActivation.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfFlags,c);

	//Level1
	/*
	JLabel lblL1 = new JLabel("Level 1");
    lblL1.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL1,c);
   */ 	
    JLabel lblBuyNum1 = new JLabel("NumBuyOrders1");
    lblBuyNum1.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuyNum1,c);
    
    tfBuyNum1 = new JTextField("");
    tfBuyNum1.setEditable(false);
    //tfBuyNum1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuyNum1,c);
    
    JLabel lblBuy1 = new JLabel("Bid1");
    lblBuy1.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuy1,c);
    
    tfBuy1 = new JTextField("");
    tfBuy1.setEditable(false);
    //tfBuy1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuy1,c);

    JLabel lblSellNum1 = new JLabel("NumSellOrders1");
    lblSellNum1.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSellNum1,c);
    
    tfSellNum1 = new JTextField("");
    tfSellNum1.setEditable(false);
    //tfSellNum1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSellNum1,c);
    
    JLabel lblSell1 = new JLabel("Ask1");
    lblSell1.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSell1,c);
    
    tfSell1 = new JTextField("");
    tfSell1.setEditable(false);
    //tfSell1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSell1,c);
    
  
  	//Level2
	/*
	JLabel lblL2 = new JLabel("Level 2");
    lblL2.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL2,c);
    */	
    JLabel lblBuyNum2 = new JLabel("NumBuyOrders2");
    lblBuyNum2.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuyNum2,c);
    
    tfBuyNum2 = new JTextField("");
    tfBuyNum2.setEditable(false);
    //tfBuyNum2.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuyNum2,c);
    
    JLabel lblBuy2 = new JLabel("Bid2");
    lblBuy2.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuy2,c);
    
    tfBuy2 = new JTextField("");
    tfBuy2.setEditable(false);
    //tfBuy2.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuy2,c);

    JLabel lblSellNum2 = new JLabel("NumSellOrders2");
    lblSellNum2.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSellNum2,c);
    
    tfSellNum2 = new JTextField("");
    tfSellNum2.setEditable(false);
    //tfSellNum2.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSellNum2,c);
    
    JLabel lblSell2 = new JLabel("Ask2");
    lblSell2.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSell2,c);
    
    tfSell2 = new JTextField("");
    tfSell2.setEditable(false);
    //tfSell1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSell2,c);
  
  
	//Level3
	/*
	JLabel lblL3 = new JLabel("Level 3");
    lblL3.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL3,c);
    */	
    JLabel lblBuyNum3 = new JLabel("NumBuyOrders3");
    lblBuyNum3.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuyNum3,c);
    
    tfBuyNum3 = new JTextField("");
    tfBuyNum3.setEditable(false);
    //tfBuyNum3.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuyNum3,c);
    
    JLabel lblBuy3 = new JLabel("Bid3");
    lblBuy3.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuy3,c);
    
    tfBuy3 = new JTextField("");
    tfBuy3.setEditable(false);
    //tfBuy3.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuy3,c);

    JLabel lblSellNum3 = new JLabel("NumSellOrders3");
    lblSellNum3.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSellNum3,c);
    
    tfSellNum3 = new JTextField("");
    tfSellNum3.setEditable(false);
    //tfSellNum3.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSellNum3,c);
    
    JLabel lblSell3 = new JLabel("Ask3");
    lblSell3.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSell3,c);
    
    tfSell3 = new JTextField("");
    tfSell3.setEditable(false);
    //tfSell1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSell3,c);  
    
    	//Level4
	/*
	JLabel lblL4 = new JLabel("Level 4");
    lblL4.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL4,c);
    */	
    JLabel lblBuyNum4 = new JLabel("NumBuyOrders4");
    lblBuyNum4.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuyNum4,c);
    
    tfBuyNum4 = new JTextField("");
    tfBuyNum4.setEditable(false);
    //tfBuyNum4.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuyNum4,c);
    
    JLabel lblBuy4 = new JLabel("Bid4");
    lblBuy4.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuy4,c);
    
    tfBuy4 = new JTextField("");
    tfBuy4.setEditable(false);
    //tfBuy4.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuy4,c);

    JLabel lblSellNum4 = new JLabel("NumSellOrders4");
    lblSellNum4.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSellNum4,c);
    
    tfSellNum4 = new JTextField("");
    tfSellNum4.setEditable(false);
    //tfSellNum4.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSellNum4,c);
    
    JLabel lblSell4 = new JLabel("Ask4");
    lblSell4.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSell4,c);
    
    tfSell4 = new JTextField("");
    tfSell4.setEditable(false);
    //tfSell1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSell4,c);
    
    //Level5
	/*
	JLabel lblL5 = new JLabel("Level 5");
    lblL5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL5,c);
    */	
    JLabel lblBuyNum5 = new JLabel("NumBuyOrders5");
    lblBuyNum5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuyNum5,c);
    
    tfBuyNum5 = new JTextField("");
    tfBuyNum5.setEditable(false);
    //tfBuyNum5.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuyNum5,c);
    
    JLabel lblBuy5 = new JLabel("Bid5");
    lblBuy5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblBuy5,c);
    
    tfBuy5 = new JTextField("");
    tfBuy5.setEditable(false);
    //tfBuy5.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfBuy5,c);

    JLabel lblSellNum5 = new JLabel("NumSellOrders5");
    lblSellNum5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSellNum5,c);
    
    tfSellNum5 = new JTextField("");
    tfSellNum5.setEditable(false);
    //tfSellNum5.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSellNum5,c);
    
    JLabel lblSell5 = new JLabel("Ask5");
    lblSell5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblSell5,c);
    
    tfSell5 = new JTextField("");
    tfSell5.setEditable(false);
    //tfSell1.setPreferredSize(dimField);
    c.gridx = 1;
    contentPanel.add(tfSell5,c);    


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
  private JTextField tfFlags = null;
  
  private JTextField tfBuyNum1 = null;
  private JTextField tfBuy1 = null;
  private JTextField tfSellNum1 = null;
  private JTextField tfSell1 = null;


  private JTextField tfBuyNum2 = null;
  private JTextField tfBuy2 = null;
  private JTextField tfSellNum2 = null;
  private JTextField tfSell2 = null;


  private JTextField tfBuyNum3 = null;
  private JTextField tfBuy3 = null;
  private JTextField tfSellNum3 = null;
  private JTextField tfSell3 = null;


  private JTextField tfBuyNum4 = null;
  private JTextField tfBuy4 = null;
  private JTextField tfSellNum4 = null;
  private JTextField tfSell4 = null;


  private JTextField tfBuyNum5 = null;
  private JTextField tfBuy5 = null;
  private JTextField tfSellNum5 = null;
  private JTextField tfSell5 = null;



}

