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
import com.exsys.orderentry.FixLogWriter;
import com.exsys.orderentry.TraderWindow;

public class OrderWindow implements ActionListener
{
  private TradeMessageProcessor mTrdWin = null;
  
  private String mTargetCompID;
  private String mSenderCompID;
  private String mSenderSubID;
  private String mUserName;
  private String mExchange;
  private boolean isFX = false;

  private JPanel ordpane = null;
  private JRadioButton radioBUY = null;
  private JRadioButton radioLMT = null;
  private JRadioButton radioDAY = null;
  private JRadioButton radioFIRM  = null;
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
  private JPanel main = null;
  private JPanel radioPaneSIDE = null;
  private JRadioButton radioSELL = null;
  private JRadioButton radioMKT = null;
  private JRadioButton radioSTOP = null;
  private JRadioButton radioSTOPLIMIT = null;
  private JRadioButton radioMARKETLIMIT = null;
  private JRadioButton radioGTC = null;
  private JRadioButton radioFAK = null;
  private JRadioButton radioFOK = null;
  private JRadioButton radioGTD = null;
  private JFrame preview = null;

  //private int mAutoGenOrderID = 0;
  private FixUniqueIdGen idGen = null;
  private static String mSide;
  private static String mCallPut;
  private static String mCustomerFirm;
  private static String mOrdType;
  private static String mTimeInForce;
  private boolean isFuture = true;

  private final static String SUBMIT = "Submit";
  private final static String RESET = "Reset";
  private final static String PREVIEW = "Preview";
  private final static String SELL = "Sell";
  private final static String BUY = "Buy";
  private final static String MARKET = "Market";
  private final static String LIMIT = "Limit";
  private final static String STOP = "Stop";
  private final static String MARKET_LIMIT = "MarketLimit";
  private final static String STOP_LIMIT = "StopLimit";
  private final static String OK = "ok";

  private final static String DAY = "Day";
  private final static String GTC = "GTC";
  private final static String GTD = "GTD";
  private final static String FAK = "FAK";
  private final static String FOK = "FOK";


  private final static String PUT = "Put";
  private final static String CALL = "Call";
  private final static String CUSTOMER = "Customer";
  private final static String FIRM = "Firm";

  public OrderWindow(String targetCompID, String SenderCompID, String SenderSubID, String UserName, TradeMessageProcessor trdwin,
                     String idFile, String exchange, boolean fx) throws Exception
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
  
  public OrderWindow(String targetCompID, String SenderCompID, String SenderSubID, String UserName, TradeMessageProcessor trdwin,
          String idFile, String exchange, boolean fx, FixLogWriter writer) throws Exception
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
    //System.out.println("OrderWindow::actionPerformed:: "+e.toString());
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
    else if(cmd == PREVIEW)previewOrder();
    else if(cmd == RESET) resetFields();
    else if(cmd == OK) preview.dispose();
  }
  // ========================================================================
  // ========================================================================
  private void previewOrder()
  {
	  preview = new JFrame("Preview Order");

		Dimension dimField = new Dimension( 120, 20);
	    EmptyBorder border = new EmptyBorder(new Insets(0,0,0,0));
	    JPanel panel = new JPanel(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.BOTH;
	    c.weightx = 1.0;
	    int y = 1;

		JLabel side = new JLabel("side");
		side.setBorder(border);
		c.gridx = 0;
		panel.add(side, c);

		JTextField rside = new JTextField();
		rside.setBorder(border);
		rside.setEditable(false);
		if(radioBUY.isSelected())
			rside.setText(radioBUY.getText());
		else if(radioSELL.isSelected())
			rside.setText(radioSELL.getText());
		c.gridx = 2;
		panel.add(rside, c);

		JLabel qty = new JLabel("Quantity");
		qty.setBorder(border);
		c.gridx = 0;c.gridy = y++;
		panel.add(qty, c);

		JTextField tqty = new JTextField();
		tqty.setBorder(border);
		tqty.setEditable(false);
		tqty.setText(tfQTY.getText());
		c.gridx = 2;
		panel.add(tqty, c);

		JLabel prd = new JLabel("Contract");
		prd.setBorder(border);
		c.gridx = 0;c.gridy = y++;
		panel.add(prd, c);

		JTextField tprd = new JTextField();
		tprd.setBorder(border);
		tprd.setEditable(false);
		tprd.setText(tfCONTRACT.getText());
		c.gridx = 2;
		panel.add(tprd, c);

		JLabel price = new JLabel("Price");
		price.setBorder(border);
		c.gridx = 0;c.gridy = y++;
		panel.add(price, c);

		JTextField tprice = new JTextField();
		tprice.setBorder(border);
		tprice.setEditable(false);
		tprice.setText(tfPRICE.getText());
		c.gridx = 2;
		panel.add(tprice, c);

		JLabel stprice = new JLabel("Stop Price");
		stprice.setBorder(border);
		c.gridx = 0;c.gridy = y++;
		panel.add(stprice, c);

		JTextField stopprice = new JTextField();
		stopprice.setBorder(border);
		stopprice.setEditable(false);
		stopprice.setText(tfSLPRICE.getText());
		c.gridx = 2;
		panel.add(stopprice, c);

		JLabel ordType = new JLabel("Order Type");
		ordType.setBorder(border);
		c.gridx = 0;c.gridy = y++;
		panel.add(ordType, c);

		JTextField tordType = new JTextField();
		tordType.setBorder(border);
		tordType.setEditable(false);
		if(radioLMT.isSelected())
			tordType.setText(radioLMT.getText());
		else if(radioMKT.isSelected())
			tordType.setText(radioMKT.getText());
		else if(radioSTOP.isSelected())
			tordType.setText(radioSTOP.getText());
		else if(radioSTOPLIMIT.isSelected())
			tordType.setText(radioSTOPLIMIT.getText());
		else if(radioMARKETLIMIT.isSelected())
			tordType.setText(radioMARKETLIMIT.getText());
		c.gridx = 2;
		panel.add(tordType, c);

		JLabel time = new JLabel("Time In Force");
		time.setBorder(border);
		c.gridx = 0;c.gridy = y++;
		panel.add(time, c);

		JTextField ttime = new JTextField();
		ttime.setBorder(border);
		ttime.setEditable(false);
		if(radioDAY.isSelected())
			ttime.setText(radioDAY.getText());
		else if(radioGTC.isSelected())
			ttime.setText(radioGTC.getText());
		else if(radioFAK.isSelected())
			ttime.setText(radioFAK.getText());
		else if(radioFOK.isSelected())
			ttime.setText(radioFOK.getText());
		else if(radioGTD.isSelected())
			ttime.setText(radioGTD.getText());
		c.gridx = 2;
		panel.add(ttime, c);

		JButton btnSUMMIT = new JButton(OK);
		btnSUMMIT.setPreferredSize(new Dimension(50,20));
		c.gridx = 2;c.gridy = y++;
		btnSUMMIT.setActionCommand(OK);
		btnSUMMIT.addActionListener(this);
		panel.add(btnSUMMIT,c);

		preview.setPreferredSize(new Dimension(300, 200));
		preview.getContentPane().add(panel) ;
		preview.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		preview.setVisible(true);
		preview.pack();
  }
  private void resetFields()
  {
	tfQTY.setText("");
	tfCONTRACT.setText("ESZ2");
	tfPRODGROUP.setText("ES");
	tfAccount.setText("");
	tfSubAccount.setText("");
	tfGiveupFirm.setText("");
	tfPRICE.setText("1200");
	tfSLPRICE.setText("");
	tfMaxShow.setText("");
	tfMinQty.setText("");
	tfStrikePrice.setText("");
	tfExpYYYYMM.setText("");
	tfExpDay.setText("");
	tfGTDate.setText("");
	radioBUY.setSelected(true);
	radioLMT.setSelected(true);
	radioDAY.setSelected(true);
	radioFIRM.setSelected(true);
  }

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
  	System.out.println("quantity"+tfQTY.getText());
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
    System.out.println("quantity"+order.getOrderQtyAsString());
    if(mTrdWin != null) { 
    	mTrdWin.processOrder(order);
    	TraderWindow.logWriter.logInFixMessage(order);
    }
  }
  // ========================================================================
  // ========================================================================
  public Component createComponent()
  {
	Dimension dimField = new Dimension( 120, 20);
	//Dimension dimField1 = new Dimension( 20, 20);
	EmptyBorder border = new EmptyBorder(new Insets(0,0,0,0));

	main = new JPanel(new BorderLayout());
	main.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

	ordpane = new JPanel(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	// add space around all component to avoid clutter
	//c.insets = new Insets(2,2,2,2);
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1.0;
	int y = 1;
	//int x = 1;

	JLabel lbl1 = new JLabel("Side");
	lbl1.setBorder(border);
	c.gridx = 0;
	//c.gridy = y++;
	ordpane.add(lbl1, c);
	radioBUY = new JRadioButton(BUY);
	radioBUY.setSelected(true);
	radioBUY.addActionListener(this);
	radioBUY.setActionCommand(BUY);
	mSide = FixFieldConstants.SIDE_BUY;
	radioSELL = new JRadioButton(SELL);
	radioSELL.addActionListener(this);
	radioSELL.setActionCommand(SELL);
	ButtonGroup btngroupSIDE = new ButtonGroup();
	btngroupSIDE.add(radioBUY);
	btngroupSIDE.add(radioSELL);
	radioPaneSIDE = new JPanel();
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
	radioFIRM = new JRadioButton(FIRM);
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

	main.add(ordpane,BorderLayout.WEST);

	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	panel.setAlignmentY(Component.TOP_ALIGNMENT);

	JPanel radioORDTYPE = new JPanel();
	radioORDTYPE.setAlignmentY(Component.TOP_ALIGNMENT);
	radioORDTYPE.setBorder(new TitledBorder(border,"Order Type"));
	radioORDTYPE.setLayout(new BoxLayout(radioORDTYPE, BoxLayout.Y_AXIS));

	radioLMT = new JRadioButton(LIMIT);
	radioLMT.setSelected(true);
	radioLMT.setActionCommand(LIMIT);
	radioLMT.addActionListener(this);
	mOrdType = FixFieldConstants.ORDER_TYPE_LIMIT;

	radioMKT = new JRadioButton(MARKET);
	radioMKT.setActionCommand(MARKET);
	radioMKT.addActionListener(this);

	radioSTOP = new JRadioButton(STOP);
	radioSTOP.setActionCommand(STOP);
	radioSTOP.addActionListener(this);

	radioSTOPLIMIT = new JRadioButton(STOP_LIMIT);
	radioSTOPLIMIT.setActionCommand(STOP_LIMIT);
	radioSTOPLIMIT.addActionListener(this);

	radioMARKETLIMIT = new JRadioButton(MARKET_LIMIT);
	radioMARKETLIMIT.setActionCommand(MARKET_LIMIT);
	radioMARKETLIMIT.addActionListener(this);

	ButtonGroup btngroupORDTYPE = new ButtonGroup();
	btngroupORDTYPE.add(radioLMT);
	btngroupORDTYPE.add(radioMKT);
	btngroupORDTYPE.add(radioSTOP);
	btngroupORDTYPE.add(radioSTOPLIMIT);
	btngroupORDTYPE.add(radioMARKETLIMIT);

	radioORDTYPE.add(radioLMT);
	radioORDTYPE.add(radioMKT);
	radioORDTYPE.add(radioSTOP);
	radioORDTYPE.add(radioSTOPLIMIT);
	radioORDTYPE.add(radioMARKETLIMIT);

	panel.add(radioORDTYPE);

	JPanel radioTIF = new JPanel();
	radioTIF.setBorder(new TitledBorder(border,"Time In Force"));
	radioTIF.setLayout(new BoxLayout(radioTIF,BoxLayout.Y_AXIS));
	radioTIF.setAlignmentY(Component.TOP_ALIGNMENT);

	radioDAY = new JRadioButton(DAY);
	radioDAY.setSelected(true);
	radioDAY.setActionCommand(DAY);
	radioDAY.addActionListener(this);
	mTimeInForce = FixFieldConstants.TIME_IN_FORCE_DAY;

	radioGTC = new JRadioButton(GTC);
	radioGTC.setActionCommand(GTC);
	radioGTC.addActionListener(this);

	radioFAK = new JRadioButton(FAK);
	radioFAK.setActionCommand(FAK);
	radioFAK.addActionListener(this);

	radioFOK = new JRadioButton(FOK);
	radioFOK.setActionCommand(FOK);
	radioFOK.addActionListener(this);

	radioGTD = new JRadioButton(GTD);
	radioGTD.setActionCommand(GTD);
	radioGTD.addActionListener(this);

	ButtonGroup btngroupTIF = new ButtonGroup();
	btngroupTIF.add(radioDAY);
	btngroupTIF.add(radioGTC);
	btngroupTIF.add(radioFAK);
	btngroupTIF.add(radioFOK);
	btngroupTIF.add(radioGTD);

	radioTIF.setLayout(new BoxLayout(radioTIF, BoxLayout.Y_AXIS));
	radioTIF.add(radioDAY);
	radioTIF.add(radioGTC);
	radioTIF.add(radioFAK);
	radioTIF.add(radioFOK);
	radioTIF.add(radioGTD);

	tfGTDate = new JTextField();
	tfGTDate.setPreferredSize(dimField);
	tfGTDate.addActionListener(this);
	radioTIF.add(tfGTDate);
	panel.add(radioTIF);

	JPanel options = new JPanel();
	options.setLayout(new GridBagLayout());
	//options.setBorder(new TitledBorder(border,"Options"));
	//options.setAlignmentY(TOP_ALIGNMENT);
	GridBagConstraints c1 = new GridBagConstraints();
	c1.fill = GridBagConstraints.BOTH;
	c1.weightx = 1.0;
	int y1 = 1;

	JLabel lblStrikePrice = new JLabel("Strike");
	lblStrikePrice.setBorder(border);
	c1.gridx = 0; //c.gridy = y++;
	options.add(lblStrikePrice,c1);
	tfStrikePrice = new JTextField();
	tfStrikePrice.setPreferredSize(dimField);
	tfStrikePrice.addActionListener(this);
	c1.gridx = 1;
	options.add(tfStrikePrice,c1);

	JLabel lbl5 = new JLabel("Put/Call");
	lbl5.setBorder(border);
	c1.gridx = 0; c1.gridy = y1++;
	options.add(lbl5, c1);

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
	c1.gridx = 1;
	options.add(cbPC,c1);

	JLabel lblExpYYYYMM = new JLabel("Expiration(YYYYMM)");
	lblExpYYYYMM.setBorder(border);
	c1.gridx = 0; c1.gridy = y1++;
	options.add(lblExpYYYYMM,c1);
	tfExpYYYYMM = new JTextField();
	tfExpYYYYMM.setPreferredSize(dimField);
	tfExpYYYYMM.addActionListener(this);
	c1.gridx = 1;
	options.add(tfExpYYYYMM,c1);

	JLabel lblExpDay = new JLabel("Day(0-31)");
	lblExpDay.setBorder(border);
	c1.gridx = 0; c1.gridy = y1++;
	options.add(lblExpDay,c1);
	tfExpDay = new JTextField();
	tfExpDay.setPreferredSize(dimField);
	tfExpDay.addActionListener(this);
	c1.gridx = 1;
	options.add(tfExpDay,c1);

	panel.add(options);
	main.add(panel,BorderLayout.CENTER);

	JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	JButton btnSUMMIT = new JButton(SUBMIT);
	btnSUMMIT.setPreferredSize(dimField);
	btnSUMMIT.setActionCommand(SUBMIT);
	btnSUMMIT.addActionListener(this);
	buttons.add(btnSUMMIT);

	JButton btnPREVIEW = new JButton(PREVIEW);
	btnPREVIEW.setPreferredSize(dimField);
	btnPREVIEW.setActionCommand(PREVIEW);
	btnPREVIEW.addActionListener(this);
	buttons.add(btnPREVIEW);

	JButton btnRESET = new JButton(RESET);
	btnRESET.setPreferredSize(dimField);
	btnRESET.setActionCommand(RESET);
	btnRESET.addActionListener(this);
	buttons.add(btnRESET);
	main.add(buttons,BorderLayout.SOUTH);

	return main;
  }
}
