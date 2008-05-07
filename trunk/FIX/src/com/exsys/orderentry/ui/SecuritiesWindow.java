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

public class SecuritiesWindow implements ActionListener
{
  private SecuritiesMessageProcessor mSecWin = null;
  private String mTargetCompID;
  private String mSenderCompID;
  private String mSenderSubID;
  private String mUserName;
  private String mExchange;
  
  private JComboBox cbSecurityType;


  //private int mAutoGenOrderID = 0;
  private FixUniqueIdGen idGen = null;
  private static String mSecurityType;
  
  public static Hashtable secTypesMap = new Hashtable();
  
  	static
	{
		
	// security types
	secTypesMap.put("Financial Gas",		"0");
	secTypesMap.put("Financial Power",		"1");
	secTypesMap.put("Oil",		"2");
	secTypesMap.put("IPE Natural Gas Futures",		"3");
	secTypesMap.put("IPE Gas Oil Futures",		"4");
	secTypesMap.put("IPE Brent Futures",		"5");
	secTypesMap.put("IPE Gas Oil Futures Crack",		"6");
	secTypesMap.put("IPE UK Electricity Futures Peak",		"7");
	secTypesMap.put("IPE UK Electricity Futures Base",		"8");
	secTypesMap.put("ICE Heating Oil/WTI Futures Crack",		"14");
	secTypesMap.put("ICE NYH (RBOB) Gasoline Futures",		"15");
	secTypesMap.put("ICE NYH (RBOB) Gasoline/WTI Futures Crack",		"16");
		
	}

  private final static String SUBMIT = "Submit";

  public SecuritiesWindow(String targetCompID,
                     String SenderCompID,String SenderSubID,String UserName,
                     SecuritiesMessageProcessor secwin,
                     String idFile,
                     String exchange) throws Exception
  {
    super();
    mTargetCompID = targetCompID;
    mSenderCompID = SenderCompID;
    mSenderSubID = SenderSubID;
    mUserName = UserName;
    mSecWin = secwin;
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
  	if( mSecurityType == null)
  	{
  		error = "Security Type must be selected";
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

	

   	FixSecurityDefinitionRequest sec = new FixSecurityDefinitionRequest();
	
	sec.setSecurityReqID("SECREQ" + idGen.getNextSecId());
	sec.setSecurityRequestType("3");
	sec.setSecurityType((String)secTypesMap.get(mSecurityType));
	

    if(mSecWin != null) mSecWin.processSecurityDefinitionRequest(sec);
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


    JLabel lbl5 = new JLabel("Security Type");
    lbl5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lbl5, c);

    cbSecurityType = new JComboBox();
    cbSecurityType.addItemListener( new ItemListener()
    {
	public void itemStateChanged( ItemEvent ev )
	{
		if( ev.getStateChange() == ItemEvent.SELECTED )
		{
			mSecurityType = (String)ev.getItem();

		}
	}
    });
    
    //cbSecurityType.addItem("   ");
    
    Iterator keys = secTypesMap.keySet().iterator();
	while( keys.hasNext() )
	{
			String l_key = (String) keys.next();
			cbSecurityType.addItem(l_key);
			
	}
	
    c.gridx = 0; c.gridy = y++;
    ordpane.add(cbSecurityType,c);


    JButton btnSUMMIT = new JButton(SUBMIT);
    c.gridx = 5; c.gridy = y+5;
    btnSUMMIT.setActionCommand(SUBMIT);
    btnSUMMIT.addActionListener(this);
    ordpane.add(btnSUMMIT, c);


    return ordpane;
  }
}
