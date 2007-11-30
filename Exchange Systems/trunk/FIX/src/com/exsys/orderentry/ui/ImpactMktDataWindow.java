package com.exsys.orderentry.ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import com.exsys.common.trading.*;
import com.exsys.impact.message.*;
import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;
import com.exsys.common.util.*;
import com.exsys.fix.tools.*;

public class ImpactMktDataWindow implements ActionListener
{
  private ImpactMessageProcessor mMDWin = null;
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

  public ImpactMktDataWindow(String targetCompID,
                     String SenderCompID,String SenderSubID,String UserName,
                     ImpactMessageProcessor mdwin,
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

      RequestFeedByMarketID mdReq = new RequestFeedByMarketID();
      mdReq.MarketIDs = MessageUtil.toRawChars(mSymbol, mdReq.MarketIDs.length);
      mdReq.RequestSeqID = Integer.parseInt(idGen.getNextMktDataId());
      mdReq.MarketDepth = 0;

   if(mMDWin != null) mMDWin.processMarketDataRequest(mdReq);
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
