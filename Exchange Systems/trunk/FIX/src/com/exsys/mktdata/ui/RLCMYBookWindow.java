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
import com.exsys.mktdata.book.*;



public class RLCMYBookWindow
  	extends RLCMarketDataBaseHandler
  	implements ListSelectionListener
{
  // ========================================================================
  // ========================================================================
  public RLCMYBookWindow() throws ConfigFileNotFound, Exception
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

      mResponseSubject = ConfigurationService.getValue("MYResponseSubject");

    }
    catch( ConfigAttributeNotFound exc )
    {
      System.out.println("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

public void processMYMessage(RLCMYMessage rlcMsg)
{
	System.out.println("processMYMessage");
	//System.out.println(rlcMsg.toString());
	String instr = rlcMsg.getCompleteInstrumentCode();

	 String str = rlcMsg.getChangeOfLimitFlag();
	 int index=0;
	 for( int i=0; i<5; i++)
	 {
		if(str.charAt(i) == '1')
		{
			System.out.println("index is " + index);
			bh.handleMYBookRow(instr,
					i+1,
					rlcMsg.getBuyLimitQuantityAsint(index),
					rlcMsg.getBuyLimitPriceAsPrice(index),
					rlcMsg.getSellLimitQuantityAsint(index),
					rlcMsg.getSellLimitPriceAsPrice(index)
					);
			index++;

		}

	 }


	if(!symbols.contains(instr))
	{
			mListModel.addElement(instr);
			symbols.add(instr);
	}
	else
	{
		synchronized(m_currentSymbol)
		{
			if(instr.equals(m_currentSymbol))
			{
				clearWindow();
				displayMessage(m_currentSymbol);	
			}
		}
	}
			

}

public void processRestoredMYMessage(RLCMYMessage rlcMsg)
{

	processMYMessage(rlcMsg);
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
          BorderFactory.createTitledBorder("MYMessages Log"),
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
      	synchronized(m_currentSymbol)
		{
  	      m_currentSymbol = (String)mList.getModel().getElementAt(selected);
   	      displayMessage(m_currentSymbol);
		}      	

      }

      //System.out.println("valueChanged:: "+e.toString());
      System.out.println("valueChanged:: ");
  }
  public void clearWindow()
  {
  }
  public void displayMessage(String symbol)
  {

	  lblCI.setText("CompleteInstrument : "+ symbol);
	  RLCMYBookRow[] rows = bh.getMYBook(symbol);
	  if(rows != null)
	  {
		  lblL1.setText("Level 1" + rows[0].toBookString());
		  lblL2.setText("Level 2" + rows[1].toBookString());
		  lblL3.setText("Level 3" + rows[2].toBookString());
		  lblL4.setText("Level 4" + rows[3].toBookString());
		  lblL5.setText("Level 5" + rows[4].toBookString());
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

      String restore = ConfigurationService.getValue("restoreMY");
      if(restore.equals("Y"))
      {
 	     String inFile = ConfigurationService.getValue("MYInLogFile");
 	     ArrayList inList = RLCFileManager.getRLCMessagesList(inFile,"MY");

 	     // then process in list
 	    for(int i=0; i<inList.size();i++)
	    {
	    	System.out.println("Restoring");
    		RLCMYMessage msg = (RLCMYMessage)inList.get(i);
	    	processRestoredMYMessage(msg);
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
        BorderFactory.createTitledBorder("< MYMessage Display:>"),
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



    lblCI = new JLabel("CompleteInstrument");
    lblCI.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblCI,c);
    //tfCompleteInstrument = new JTextField("");
    //tfCompleteInstrument.setEditable(false);
    //tfCompleteInstrument.setPreferredSize(dimField);
    //c.gridx = 1;
    //contentPanel.add(tfCompleteInstrument,c);

	JLabel lblL = new JLabel("Level   " + RLCMYBookRow.toHeaderString());
    lblL.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL,c);



	//Level1

	lblL1 = new JLabel("Level 1     0@0.00        0@0.00");
    lblL1.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL1,c);


  	//Level2

	lblL2 = new JLabel("Level 2     0@0.00        0@0.00");
    lblL2.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL2,c);


	//Level3

	lblL3 = new JLabel("Level 3     0@0.00        0@0.00");
    lblL3.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL3,c);


    //Level4

	lblL4 = new JLabel("Level 4     0@0.00        0@0.00");
    lblL4.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL4,c);


    //Level5

	lblL5 = new JLabel("Level 5     0@0.00        0@0.00");
    lblL5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    contentPanel.add(lblL5,c);



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
  private JLabel lblL1 = null;
  private JLabel lblL2 = null;
  private JLabel lblL3 = null;
  private JLabel lblL4 = null;
  private JLabel lblL5 = null;
  private JLabel lblCI = null;

  private String m_currentSymbol = "";

  private ArrayList symbols = new ArrayList();
  private RLCBookHandler bh = new RLCBookHandler();

}

