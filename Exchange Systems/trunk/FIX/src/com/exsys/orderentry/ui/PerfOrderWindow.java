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
import com.exsys.orderentry.PerfTraderWindow;

public class PerfOrderWindow implements ActionListener
{
  private TradeMessageProcessor mTrdWin = null;
  private String mTargetCompID;
  private String mSenderCompID;
  private String mSenderSubID;
  private String mUserName;
  private String mExchange;
  private boolean isFX = false;
  private JTextField tfQTY = null;
  private JTextField tfCONTRACT = null;
  private JTextField tfPRODGROUP = null;
  private JTextField tfPRICE = null;
  private JTextField tfSLPRICE = null;
  private JTextField tfGTDate = null;
  private JTextField tfAccount = null;
  private JTextField tfSubAccount = null;
  private JTextField tfGiveupFirm = null;
  private JTextField tfStrikePrice = null;
  private JTextField tfExpYYYYMM = null;
  private JTextField tfExpDay = null;
  private JTextField tfMaxShow = null;
  private JTextField tfMinQty = null;
  private JComboBox cbPC;


  //private int mAutoGenOrderID = 0;
  private FixUniqueIdGen idGen = null;
  private static String mSide;
  private static String mCallPut;
  private static String mCustomerFirm;
  private static String mOrdType;
  private static String mTimeInForce;
  private boolean isFuture = true;

  private final static String SUBMIT = "Submit";
  private final static String SELL = "Sell";
  private final static String BUY = "Buy";
  private final static String MARKET = "Market";
  private final static String LIMIT = "Limit";
  private final static String STOP = "Stop";
  private final static String MARKET_LIMIT = "MarketLimit";
  private final static String STOP_LIMIT = "StopLimit";

  private final static String DAY = "Day";
  private final static String GTC = "GTC";
  private final static String GTD = "GTD";
  private final static String FAK = "FAK";
  private final static String FOK = "FOK";


  private final static String PUT = "Put";
  private final static String CALL = "Call";
  private final static String CUSTOMER = "Customer";
  private final static String FIRM = "Firm";

  public PerfOrderWindow(String targetCompID,
                     String SenderCompID,String SenderSubID,String UserName,
                     TradeMessageProcessor trdwin,
                     String idFile,
                     String exchange,
                     boolean fx) throws Exception
  {
    super();
    mTargetCompID = targetCompID;
    mSenderCompID = SenderCompID;
    mSenderSubID = SenderSubID;
    mUserName = UserName;
    mTrdWin = trdwin;
    mExchange = exchange;
    idGen = FixUniqueIdGen.getInstance(idFile);
    isFX = fx;
  }
  // ========================================================================
  // ========================================================================
  public void actionPerformed(ActionEvent e)
  {
    //System.out.println("PerfOrderWindow::actionPerformed:: "+e.toString());
    //System.out.println("ActionCommand["+e.getActionCommand()+"]");
    String cmd = e.getActionCommand();
    if(cmd == SELL)     mSide = FixFieldConstants.SIDE_SELL;
    else if(cmd == BUY) mSide = FixFieldConstants.SIDE_BUY;
    //else if(cmd == PUT)  mCallPut = FixFieldConstants.PUT_CALL_PUT;
    //else if(cmd == CALL) mCallPut = FixFieldConstants.PUT_CALL_CALL;
    else if(cmd == MARKET) mOrdType = FixFieldConstants.ORDER_TYPE_MARKET;
    else if(cmd == LIMIT)  mOrdType = FixFieldConstants.ORDER_TYPE_LIMIT;
    else if(cmd == STOP) mOrdType = FixFieldConstants.ORDER_TYPE_STOP;
    else if(cmd == MARKET_LIMIT)  mOrdType = FixFieldConstants.ORDER_TYPE_MARKETLIMIT;
    else if(cmd == STOP_LIMIT)  mOrdType = FixFieldConstants.ORDER_TYPE_STOPLIMIT;
    else if(cmd == DAY ) mTimeInForce = FixFieldConstants.TIME_IN_FORCE_DAY;
    else if(cmd == GTC ) mTimeInForce = FixFieldConstants.TIME_IN_FORCE_GTC;
    else if(cmd == FAK ) mTimeInForce = FixFieldConstants.TIME_IN_FORCE_FAK;
    else if(cmd == FOK ) mTimeInForce = FixFieldConstants.TIME_IN_FORCE_FOK;
    else if(cmd == GTD ) mTimeInForce = FixFieldConstants.TIME_IN_FORCE_GTD;
    else if(cmd == CUSTOMER)  mCustomerFirm = FixFieldConstants.CUSTOMER_OR_FIRM_CUSTOMER;
    else if(cmd == FIRM) mCustomerFirm = FixFieldConstants.CUSTOMER_OR_FIRM_FIRM;
    else
    if(cmd == SUBMIT)
    {
    	if(validateOrder())
    	{
    		try
    		{
	    		submitOrder();
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
  private boolean validateOrder()
  {
  	String error = null;

  	String slPrice = tfSLPRICE.getText();
  	String contract = tfCONTRACT.getText();
  	String pg = tfPRODGROUP.getText();

  	String account = tfAccount.getText();
  	String price = tfPRICE.getText();
  	String gtDate = tfGTDate.getText();
  	String qty = tfQTY.getText();
  	String strike = tfStrikePrice.getText();
  	String expYYYYMM = tfExpYYYYMM.getText();
  	String minQty = tfMinQty.getText();

  	//System.out.println("OrdType = " + mOrdType);
  	//System.out.println("price = " + price);
  	//System.out.println("price is blank = " + StringUtilities.isBlank(price)  );

  	// make sure that if order type is stop limit, then stoplimit price is entered
  	if( StringUtilities.isBlank(contract) || StringUtilities.isBlank(pg))
  	{
  		error = "Symbol and Product Group must be entered";
  	}
  	else if( StringUtilities.isBlank(qty) )
  	{
  		error = "Quantity must be entered";
  	}
  	else if( (mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOP)||
			  	mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOPLIMIT))
  	   && StringUtilities.isBlank(slPrice) )
  	{
  		error = "Stop Price must be entered when order type is stop or stop limit";
  	}
  	else if(StringUtilities.isBlank(account))
  	{
  		error = "Account must be entered";
  	}
  	else if( !((mOrdType.equals(FixFieldConstants.ORDER_TYPE_MARKET) ||
  			(mOrdType.equals(FixFieldConstants.ORDER_TYPE_MARKETLIMIT))))
  	   && StringUtilities.isBlank(price) )
  	{
  		error = "Price must be entered when order type is neither MKT nor MKTLIMIT";
  	}
  	else if( mTimeInForce.equals(FixFieldConstants.TIME_IN_FORCE_GTD)
  	   && StringUtilities.isBlank(gtDate) )
  	{
  		error = "GTD date must be entered when time in force is GTD";
  	}
  	else if(mCallPut != null && (StringUtilities.isBlank(strike) || StringUtilities.isBlank(expYYYYMM)))
  	{
  		error = "Strike and Expiration must be specified for options";
  	}
  	/*
  	else if(mTimeInForce.equals(FixFieldConstants.TIME_IN_FORCE_FAK)
		  	&& StringUtilities.isBlank(minQty))
	{
		error = "MinQty must be specified for FAK orders";
	}*/

  	if( error != null )
  	{
  		GuiUtil.displayError(error);
  		return false;
  	}

  	return true;


  }
  private void submitOrder() throws Exception
  {
    //System.out.println("SEND ORDER...");

    String contract = tfCONTRACT.getText();
    String pg = tfPRODGROUP.getText();
    String price = tfPRICE.getText();
    String qty = tfQTY.getText();
  	String account = tfAccount.getText();
  	String subAccount = tfSubAccount.getText();
  	String gtDate = tfGTDate.getText();
  	String slPrice = tfSLPRICE.getText();
  	String expYYYYMM = tfExpYYYYMM.getText();
  	String expDay = tfExpDay.getText();
  	String strike = tfStrikePrice.getText();
  	String giveup = tfGiveupFirm.getText();
  	String minQty = tfMinQty.getText();
  	String maxShow = tfMaxShow.getText();


    FixOrder order = new FixOrder();
    //order.setSenderCompID(mSenderCompID);
    //order.setTargetCompID(mTargetCompID);
    // ice does not have account tag
    if(!mExchange.equals("ICE"))
    	order.setAccount(account.trim());

    String clOrderID = null;
    if(mExchange.equals("ICE"))
    {
    	clOrderID = mSenderCompID +mSenderSubID+ idGen.getNextOrderId(7);
    	order.setOriginatorUserID(mUserName);
    }
    else
    {
    	clOrderID = mSenderCompID + "ORD" + idGen.getNextOrderId();
    }

    order.setClOrderID(clOrderID);

    order.setHandlInst(FixFieldConstants.HANDL_INST_AUTOMATED); // "1"
    order.setOrderQty(qty.trim());
    order.setOrdType(mOrdType);

    //??? verify if price can be 0.0

    if(mOrdType.equals(FixFieldConstants.ORDER_TYPE_MARKET)
    	|| mOrdType.equals(FixFieldConstants.ORDER_TYPE_MARKETLIMIT))
    {
    	// even cme does not want price for MKT and MTL - 04292007
	    //if(!mExchange.equals("FIX"))
 		    // order.setPrice("0.0");
    }
    else if (mOrdType.equals(FixFieldConstants.ORDER_TYPE_LIMIT)
         || mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOPLIMIT))
      order.setPrice(price);


    order.setSide(mSide);
    // symbol refers to product group
	order.setSymbol(pg.trim());
	// order.setText() - NR
	order.setTimeInForce(mTimeInForce);
	if(mTimeInForce.equals(FixFieldConstants.TIME_IN_FORCE_GTD))
	{
		// assuming that gtDate is in the right format yyyyMMdd
		// we may have to validate the format
		order.setExpireDate(gtDate.trim());
	}

	order.setTransactTime(FixMessage.getUTCCurrentTime());
	// order.setOpenClose() - NR
	if (mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOP)
         || mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOPLIMIT))
     order.setStopPx(slPrice);

     if(mExchange.equals("CME") || mExchange.equals("ICE"))
     {
     // security desc refers to the actual contract symbol
     order.setSecurityDesc(contract.trim());
     }

	if(mExchange.equals("ICE"))
	{
		order.setClientID(mSenderCompID);
	}
	//order.setClientID();  -- used in third party transactions
	//order.setMinQty() -- NR
	if(mTimeInForce.equals(FixFieldConstants.TIME_IN_FORCE_FAK))
	{
		if(!StringUtilities.isBlank(minQty))
			order.setMinQty(minQty.trim());
	}

	// ??? currently isFuture is hard coded to be future
	if(mExchange.equals("CME") || mExchange.equals("ICE"))
	{

		if(mCallPut != null)
			isFuture = false;
		else
			isFuture = true;

		if(isFX)
		{
			order.setSecurityType(FixFieldConstants.SECURITY_TYPE_FX);
		}
		else
		{
			order.setSecurityType(isFuture? FixFieldConstants.SECURITY_TYPE_FUTURE
								: FixFieldConstants.SECURITY_TYPE_OPTION);
		}



	  if(mExchange.equals("CME"))
	  {
	  	//?? hard coded to customer
   		 order.setCustomerOrFirm(mCustomerFirm);
   		 // cti code is hard coded
  		 order.setCtiCode(FixFieldConstants.CTI_CODE_BROKER_CUSTOMER_ACCOUNT);
	  }


	}

    //order.setMaxShow() -- NR
	if(!StringUtilities.isBlank(maxShow))
	{
		order.setMaxShow(maxShow.trim());
	}

    if(!StringUtilities.isBlank(subAccount))
    {
		if(mExchange.equals("CME"))
  		  	order.setOmnibusAccount(subAccount);
    }
    //order.setFeeBilling() -- NR
    if(!StringUtilities.isBlank(giveup))
    {
    	order.setGiveupFirm(giveup.trim());
    	order.setCmtaGiveupCD(FixFieldConstants.CMTA_GIVEUP_CODE_GIVEUP);
    }
    //order.setGiveupFirm()  -- NR
    //order.setCmtaGiveupCD()  -- NR
    // set CorrelationClOrderId as ClOrderId
    if(mExchange.equals("CME"))
    order.setCorrelationClOrdID(order.getClOrderID());

    // ??? is put call needed
    if(mCallPut != null)
    {
    	order.setPutOrCall(mCallPut);
    	order.setMaturityYear(expYYYYMM.trim());
    	order.setStrikePrice(strike.trim());
    	if(!StringUtilities.isBlank(expDay))
    	{
    		order.setMaturityDay(expDay.trim());
    	}
    }

    // ICE has some account fields, that may have to be taken care of???

    if(mTrdWin != null)
    {
		// get the quantity
		// send order for that quantity
		// send one lot orders of opposite side
    	mTrdWin.processOrder(order);
    	if(mSide.equals(FixFieldConstants.SIDE_SELL))
    	{
			mSide = FixFieldConstants.SIDE_BUY;
		}
		else
		{
			mSide = FixFieldConstants.SIDE_SELL;
		}
    	String clOrderID1 = null;

    	int quantity = Integer.parseInt(qty.trim());
    	for( int i=0; i<quantity; i++)
    	{
			if(mExchange.equals("ICE"))
			{
			    clOrderID1 = mSenderCompID +mSenderSubID+ idGen.getNextOrderId(7);
			}
			else
			{
			  	clOrderID1 = mSenderCompID + "ORD" + idGen.getNextOrderId();
			}

        	order.setClOrderID(clOrderID1);
        	order.setSide(mSide);
        	order.setOrderQty("1");
        	mTrdWin.processOrder(order);
		}






	}
  }
  // ========================================================================
  // ========================================================================
  public Component createComponent()
  {
   // System.out.println("PerfOrderWindow::createComponent");

    Dimension dimField = new Dimension( 220, 20);
    Dimension dimField1 = new Dimension( 20, 20);
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

    JLabel lbl1 = new JLabel("Side");
    lbl1.setBorder(border);
    c.gridx = 0;
    c.gridy = y++;
    ordpane.add(lbl1, c);
    JRadioButton radioBUY = new JRadioButton(BUY);
    radioBUY.setSelected(true);
    radioBUY.addActionListener(this);
    radioBUY.setActionCommand(BUY);
    mSide = FixFieldConstants.SIDE_BUY;
    JRadioButton radioSELL = new JRadioButton(SELL);
    radioSELL.addActionListener(this);
    radioSELL.setActionCommand(SELL);
    ButtonGroup btngroupSIDE = new ButtonGroup();
    btngroupSIDE.add(radioBUY);
    btngroupSIDE.add(radioSELL);
    JPanel radioPaneSIDE = new JPanel();
    radioPaneSIDE.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    radioPaneSIDE.add(radioSELL);
    radioPaneSIDE.add(radioBUY);
    c.gridx = 1;
    ordpane.add(radioPaneSIDE,c);

    JLabel lbl2 = new JLabel("Quantity");
    lbl2.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lbl2,c);
    tfQTY = new JTextField();
    tfQTY.setPreferredSize(dimField);
    tfQTY.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfQTY,c);

    JLabel lbl3 = new JLabel("Contract");
    lbl3.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lbl3,c);
    tfCONTRACT = new JTextField("ESZ2");
    tfCONTRACT.setPreferredSize(dimField);
    tfCONTRACT.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfCONTRACT,c);

    JLabel lblPG = new JLabel("ProdGroup");
    lblPG.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblPG,c);
    tfPRODGROUP = new JTextField("ES");
    tfPRODGROUP.setPreferredSize(dimField);
    tfPRODGROUP.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfPRODGROUP,c);




	// add order type
    JLabel lbl4 = new JLabel("Order Type");
    lbl4.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lbl4, c);
    JRadioButton radioLMT = new JRadioButton(LIMIT);
    radioLMT.setSelected(true);
    radioLMT.setActionCommand(LIMIT);
    radioLMT.addActionListener(this);
    mOrdType = FixFieldConstants.ORDER_TYPE_LIMIT;

    JRadioButton radioMKT = new JRadioButton(MARKET);
    radioMKT.setActionCommand(MARKET);
    radioMKT.addActionListener(this);

    JRadioButton radioSTOP = new JRadioButton(STOP);
    radioSTOP.setActionCommand(STOP);
    radioSTOP.addActionListener(this);

    JRadioButton radioSTOPLIMIT = new JRadioButton(STOP_LIMIT);
    radioSTOPLIMIT.setActionCommand(STOP_LIMIT);
    radioSTOPLIMIT.addActionListener(this);

    JRadioButton radioMARKETLIMIT = new JRadioButton(MARKET_LIMIT);
    radioMARKETLIMIT.setActionCommand(MARKET_LIMIT);
    radioMARKETLIMIT.addActionListener(this);


    ButtonGroup btngroupORDTYPE = new ButtonGroup();
    btngroupORDTYPE.add(radioLMT);
    btngroupORDTYPE.add(radioMKT);
    btngroupORDTYPE.add(radioSTOP);
    btngroupORDTYPE.add(radioSTOPLIMIT);
    btngroupORDTYPE.add(radioMARKETLIMIT);

    JPanel radioPaneORDTYPE = new JPanel();
    radioPaneORDTYPE.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

    radioPaneORDTYPE.add(radioLMT);
    radioPaneORDTYPE.add(radioMKT);
    radioPaneORDTYPE.add(radioSTOP);
    radioPaneORDTYPE.add(radioSTOPLIMIT);
    radioPaneORDTYPE.add(radioMARKETLIMIT);


    c.gridx = 1;
    ordpane.add(radioPaneORDTYPE,c);


	// add time in force
    JLabel lblTIF = new JLabel("Time In Force");
    lblTIF.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblTIF, c);
    JRadioButton radioDAY = new JRadioButton(DAY);
    radioDAY.setSelected(true);
    radioDAY.setActionCommand(DAY);
    radioDAY.addActionListener(this);
    mTimeInForce = FixFieldConstants.TIME_IN_FORCE_DAY;

    JRadioButton radioGTC = new JRadioButton(GTC);
    radioGTC.setActionCommand(GTC);
    radioGTC.addActionListener(this);

    JRadioButton radioFAK = new JRadioButton(FAK);
    radioFAK.setActionCommand(FAK);
    radioFAK.addActionListener(this);

	JRadioButton radioFOK = new JRadioButton(FOK);
    radioFOK.setActionCommand(FOK);
    radioFOK.addActionListener(this);

    JRadioButton radioGTD = new JRadioButton(GTD);
    radioGTD.setActionCommand(GTD);
    radioGTD.addActionListener(this);

    ButtonGroup btngroupTIF = new ButtonGroup();
    btngroupTIF.add(radioDAY);
    btngroupTIF.add(radioGTC);
    btngroupTIF.add(radioFAK);
    btngroupTIF.add(radioFOK);
    btngroupTIF.add(radioGTD);


    JPanel radioPaneTIF = new JPanel();
    radioPaneTIF.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

    radioPaneTIF.add(radioDAY);
    radioPaneTIF.add(radioGTC);
    radioPaneTIF.add(radioFAK);
    radioPaneTIF.add(radioFOK);
    radioPaneTIF.add(radioGTD);

    c.gridx = 1;
    ordpane.add(radioPaneTIF,c);

    tfGTDate = new JTextField();
    tfGTDate.setPreferredSize(dimField);
    tfGTDate.addActionListener(this);
    c.gridx = 2;
    ordpane.add(tfGTDate,c);





/* Implement Options Later */



    JLabel lbl5 = new JLabel("Put/Call");
    lbl5.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lbl5, c);

    cbPC = new JComboBox();
    cbPC.addItemListener( new ItemListener()
    {
	public void itemStateChanged( ItemEvent ev )
	{
		if( ev.getStateChange() == ItemEvent.SELECTED )
		{
		String pc = (String)ev.getItem();
		//System.out.println("Selected :"+pc);
		if(pc.equals("Put"))
		{
			mCallPut = FixFieldConstants.PUT_CALL_PUT;
		}
		else if(pc.equals("Call"))
		{
			mCallPut = FixFieldConstants.PUT_CALL_CALL;
		}
		else
		{
			mCallPut = null;
		}

		}
	}
    });
    cbPC.addItem("   ");
    cbPC.addItem("Put");
    cbPC.addItem("Call");
    c.gridx = 1;
    ordpane.add(cbPC,c);


/*

    JRadioButton radioPUT = new JRadioButton(PUT);
    radioPUT.setSelected(true);
    radioPUT.setActionCommand(PUT);
    mCallPut = FixFieldConstants.PUT_CALL_PUT;
    radioPUT.addActionListener(this);
    JRadioButton radioCALL = new JRadioButton(CALL);
    radioCALL.setActionCommand(CALL);
    radioCALL.addActionListener(this);
    ButtonGroup btngroupPUTCALL = new ButtonGroup();
    btngroupPUTCALL.add(radioPUT);
    btngroupPUTCALL.add(radioCALL);
    JPanel radioPanePUTCALL = new JPanel();
    radioPanePUTCALL.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    radioPanePUTCALL.add(radioPUT);
    radioPanePUTCALL.add(radioCALL);
    c.gridx = 1;
    ordpane.add(radioPanePUTCALL,c);
 */

    int yOld = y++;

    JLabel lblStrikePrice = new JLabel("Strike");
    lblStrikePrice.setBorder(border);
    c.gridx = 0; c.gridy = yOld;
    ordpane.add(lblStrikePrice,c);
    tfStrikePrice = new JTextField();
    tfStrikePrice.setPreferredSize(dimField);
    tfStrikePrice.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfStrikePrice,c);
    JLabel lblExpYYYYMM = new JLabel("Expiration(YYYYMM)");
    lblExpYYYYMM.setBorder(border);
    c.gridx = 2; c.gridy = yOld;
    ordpane.add(lblExpYYYYMM,c);
    tfExpYYYYMM = new JTextField();
    tfExpYYYYMM.setPreferredSize(dimField);
    tfExpYYYYMM.addActionListener(this);
    c.gridx = 3;
    ordpane.add(tfExpYYYYMM,c);
    JLabel lblExpDay = new JLabel("Day(0-31)");
    lblExpDay.setBorder(border);
    c.gridx = 4; c.gridy = yOld;
    ordpane.add(tfExpYYYYMM,c);
    tfExpDay = new JTextField();
    tfExpDay.setPreferredSize(dimField);
    tfExpDay.addActionListener(this);
    c.gridx = 5;
    ordpane.add(tfExpDay,c);




    JLabel lblAccount = new JLabel("Account");
    lblAccount.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblAccount,c);
    tfAccount = new JTextField();
    tfAccount.setPreferredSize(dimField);
    tfAccount.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfAccount,c);

    JLabel lblSubAccount = new JLabel("SubAccount");
    lblSubAccount.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblSubAccount,c);
    tfSubAccount = new JTextField();
    tfSubAccount.setPreferredSize(dimField);
    tfSubAccount.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfSubAccount,c);

    JLabel lblGiveup = new JLabel("GiveupFirm");
    lblGiveup.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblGiveup,c);
    tfGiveupFirm = new JTextField();
    tfGiveupFirm.setPreferredSize(dimField);
    tfGiveupFirm.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfGiveupFirm,c);


    JLabel lblCF = new JLabel("Customer/Firm");
    lblCF.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblCF, c);
    JRadioButton radioCUST = new JRadioButton(CUSTOMER);
    radioCUST.setSelected(true);
    radioCUST.setActionCommand(CUSTOMER);
    mCustomerFirm = FixFieldConstants.CUSTOMER_OR_FIRM_FIRM;
    radioCUST.addActionListener(this);
    JRadioButton radioFIRM = new JRadioButton(FIRM);
    radioFIRM.setActionCommand(FIRM);
    radioFIRM.addActionListener(this);
    ButtonGroup btngroupCUSTOMERFIRM = new ButtonGroup();
    btngroupCUSTOMERFIRM.add(radioCUST);
    btngroupCUSTOMERFIRM.add(radioFIRM);
    JPanel radioPaneCUSTOMERFIRM = new JPanel();
    radioPaneCUSTOMERFIRM.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    radioPaneCUSTOMERFIRM.add(radioCUST);
    radioPaneCUSTOMERFIRM.add(radioFIRM);
    c.gridx = 1;
    ordpane.add(radioPaneCUSTOMERFIRM,c);
    radioFIRM.setSelected(true);



    JLabel lbl6 = new JLabel("LimitPrice");
    lbl6.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lbl6,c);
    tfPRICE = new JTextField("1200");
    tfPRICE.setPreferredSize(dimField);
    tfPRICE.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfPRICE,c);

    JLabel lblSLPrice = new JLabel("StopPrice");
    lblSLPrice.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblSLPrice,c);
    tfSLPRICE = new JTextField();
    tfSLPRICE.setPreferredSize(dimField);
    tfSLPRICE.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfSLPRICE,c);

    JLabel lblMaxShow = new JLabel("MaxShow");
    lblMaxShow.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblMaxShow,c);
    tfMaxShow = new JTextField();
    tfMaxShow.setPreferredSize(dimField);
    tfMaxShow.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfMaxShow,c);

    JLabel lblMinQty = new JLabel("MinQty(Reqd for FAK)");
    lblMinQty.setBorder(border);
    c.gridx = 0; c.gridy = y++;
    ordpane.add(lblMinQty,c);
    tfMinQty = new JTextField();
    tfMinQty.setPreferredSize(dimField);
    tfMinQty.addActionListener(this);
    c.gridx = 1;
    ordpane.add(tfMinQty,c);


    JButton btnSUMMIT = new JButton(SUBMIT);
    c.gridx = 2; c.gridy = y;
    btnSUMMIT.setActionCommand(SUBMIT);
    btnSUMMIT.addActionListener(this);
    ordpane.add(btnSUMMIT, c);

    JButton btnPREVIEW = new JButton("Preview");
    c.gridx = 3; c.gridy = y;
    ordpane.add(btnPREVIEW, c);

    JButton btnRESET = new JButton("Reset");
    c.gridx = 4; c.gridy = y;
    ordpane.add(btnRESET, c);

    return ordpane;
  }
}
