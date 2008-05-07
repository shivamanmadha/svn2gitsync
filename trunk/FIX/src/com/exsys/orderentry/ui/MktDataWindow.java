package com.exsys.orderentry.ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import com.exsys.common.trading.*;
import com.exsys.fix.tools.*;
import com.exsys.fix.message.*;
import com.exsys.common.util.*;

public class MktDataWindow implements ActionListener
{
  private MarketDataMessageProcessor mMDWin = null;
  private String mTargetCompID;
  private String mSenderCompID;
  private String mSenderSubID;
  private String mUserName;
  private String mExchange;

  private JTextField tfSymbol;


  //private int mAutoGenOrderID = 0;
  private FixUniqueIdGen idGen = null;
  
  private static String mSymbol;

  private final static String SUBMIT = "Submit";

  public MktDataWindow(String targetCompID,
                     String SenderCompID,String SenderSubID,String UserName,
                     MarketDataMessageProcessor mdwin,
                     String idFile,
                     String exchange) throws Exception
  {
    super();
    mTargetCompID = targetCompID;
    mSenderCompID = SenderCompID;
    mSenderSubID = SenderSubID;
    mUserName = UserName;
    mMDWin = mdwin;
    mExchange = exchange;
    idGen = FixUniqueIdGen.getInstance(idFile);
  }
  // ========================================================================
  // ========================================================================
  public void actionPerformed(ActionEvent e)
  {
    //System.out.println("OrderWindow::actionPerformed:: "+e.toString());
    //System.out.println("ActionCommand["+e.getActionCommand()+"]");
    String cmd = e.getActionCommand();
    if(cmd == SUBMIT)
    {
    	if(validateRequest())
    	{
    		try
    		{
	    		submitRequest();
    		}
    		catch(Exception ex )
    		{
    			ex.printStackTrace();
    		}
    	}
    }
  }
  // ========================================================================
  // ========================================================================
  private boolean validateRequest()
  {
  	String error = null;
  	mSymbol = tfSymbol.getText();
  	if( StringUtilities.isBlank(mSymbol) )
  	{
  		error = "Symbol  must be entered";
  	}

  	if( error != null )
  	{
  		GuiUtil.displayError(error);
  		return false;
  	}

  	return true;


  }
  private void submitRequest() throws Exception
  {



   	FixMarketDataRequest mr = new FixMarketDataRequest();

	mr.setMDReqID("MKTREQ" + idGen.getNextMktDataId());
	mr.setSubscriptionRequestType("1");	
	mr.setMarketDepth("0");
	mr.setMDUpdateType("1");
	
	mr.setNoMDEntryTypes(4);
	// add MDEntryType repeating group
	FixRGNoMDEntryTypes_V rg1 = new FixRGNoMDEntryTypes_V();
	rg1.setMDEntryType("0");
	mr.addRepeatingGroup(rg1);
	
	rg1 = new FixRGNoMDEntryTypes_V();
	rg1.setMDEntryType("1");
	mr.addRepeatingGroup(rg1);
	
	rg1 = new FixRGNoMDEntryTypes_V();
	rg1.setMDEntryType("2");
	mr.addRepeatingGroup(rg1);

	rg1 = new FixRGNoMDEntryTypes_V();
	rg1.setMDEntryType("4");
	mr.addRepeatingGroup(rg1);
	
	
	
	mr.setNoRelatedSym(1);
	// add symbol repeating group
	FixRGNoRelatedSym_V rg2 = new FixRGNoRelatedSym_V();
	rg2.setSymbol(mSymbol);
	mr.addRepeatingGroup(rg2);
	
	System.out.println("DEBUG");
	
	
	
	String key = "267";
	ArrayList rgArray = mr.getRepeatingGroups(key);
	System.out.println("# of groups for key - " + key + " is " + rgArray.size());
	String firstField = FixMessageFactory.getRGFirstField(key+"_"+mr.getMsgType());
	System.out.println("First field is " + firstField);
	Integer firstFieldKey = Integer.valueOf(firstField);
	if( rgArray != null )
	{
			int size = rgArray.size();
			for (int i = 0; i < size; i++) {
				FixRepeatedGroup rg = (FixRepeatedGroup)rgArray.get(i);
				// first get body field map
				TreeMap bodyMap = rg.getBodyFieldMap();
				// For repeating groups, first tag must be written first
				
				FixField field = (FixField)bodyMap.get(firstFieldKey);
				System.out.println("Writing - "+field.getTagNumber()+"="+field.getTagValue());
								
			}		
	}
	
	key = "146";
	rgArray = mr.getRepeatingGroups(key);
	System.out.println("# of groups for key - " + key + " is " + rgArray.size());
	 firstField = FixMessageFactory.getRGFirstField(key+"_"+mr.getMsgType());
	System.out.println("First field is " + firstField);
	firstFieldKey = Integer.valueOf(firstField);
	if( rgArray != null )
	{
			int size = rgArray.size();
			for (int i = 0; i < size; i++) {
				FixRepeatedGroup rg = (FixRepeatedGroup)rgArray.get(i);
				// first get body field map
				TreeMap bodyMap = rg.getBodyFieldMap();
				// For repeating groups, first tag must be written first
				
				FixField field = (FixField)bodyMap.get(firstFieldKey);
				System.out.println("Writing - "+field.getTagNumber()+"="+field.getTagValue());
								
			}		
	}	
	
	
	
	
	

   if(mMDWin != null) mMDWin.processMarketDataRequest(mr);
  }
  // ========================================================================
  // ========================================================================
  public Component createComponent()
  {
   // System.out.println("OrderWindow::createComponent");

    //Dimension dimField = new Dimension( 220, 20);
    //Dimension dimField1 = new Dimension( 20, 20);
    EmptyBorder border = new EmptyBorder(new Insets(0,0,0,0));
    JPanel ordpane = new JPanel(new GridBagLayout());
    //ordpane.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    // add space around all component to avoid clutter
    //c.insets = new Insets(10,10,10,10);
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
	int y = 1;
	int x = 1;


    JLabel lbl5 = new JLabel("Symbol");
    lbl5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lbl5, c);

    tfSymbol = new JTextField();


    c.gridx = 0; c.gridy = y++;
    ordpane.add(tfSymbol,c);
    tfSymbol.addActionListener(this);


    JButton btnSUMMIT = new JButton(SUBMIT);
    c.gridx = 5; c.gridy = y+5;
    btnSUMMIT.setActionCommand(SUBMIT);
    btnSUMMIT.addActionListener(this);
    ordpane.add(btnSUMMIT, c);


    return ordpane;
  }
}
