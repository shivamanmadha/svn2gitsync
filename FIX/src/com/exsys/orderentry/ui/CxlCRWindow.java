package com.exsys.orderentry.ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.fix.tools.*;
import com.exsys.orderentry.TraderWindow;
import com.exsys.common.business.*;
import com.exsys.common.util.*;

public class CxlCRWindow implements ActionListener
{
  private TradeMessageProcessor mTrdWin = null;
  private Component mDialogFrame = null;
  private Order mWorkingOrder = null;
  private String mTargetCompID;
  private String mSenderCompID;
  private String mSenderSubID;
  private String mUserName;
  private String mExchange;
  private boolean isFX = false;
  
  private JTextField tfQTY = null;
  private JTextField tfCONTRACT = null;
  private JTextField tfPRICE = null;
  private JTextField tfSTOPPRICE = null;  
  private JPanel cxrPane = null;

  private JTextField tfOrigORDERID = null;
  private JTextField tfOrigSIDE = null;
  private JTextField tfOrigQTY = null;
  private JTextField tfOrigCONTRACT = null;
  private JTextField tfOrigPRICE = null;
  private JTextField tfOrigSTOPPRICE = null;  
  private JTextField tfOrigORDTYPE = null;
  private JTextField tfOrigPUTCALL = null;

  //private int mAutoGenOrderID = 0;
  private FixUniqueIdGen idGen = null;

  private final static String eBUY = "1";
  private final static String eSELL = "2";
  private static String mSide;

  private static String mCallPut;
  private final static String eCALL = "1";
  private final static String ePUT = "0";

  private static String mOrdType;
  private static String mIFMFlag;  
  private final static String eMARKET = "1";
  private final static String eLIMIT = "2";

  private final static String CANCEL = "Cancel";
  private final static String CANCELREPLACE = "CXR";
  private final static String ORDER_STATUS = "Status";
  private final static String RESET = "Reset";
  private final static String SELL = "Sell";
  private final static String BUY = "Buy";
  private final static String MARKET = "Market";
  private final static String LIMIT = "Limit";
  private final static String STOP = "Stop";
  private final static String MARKET_LIMIT = "MarketLimit";
  private final static String STOP_LIMIT = "StopLimit";

  
  private final static String PUT = "Put";
  private final static String CALL = "Call";

  private final static String IFMY = "Y";
  private final static String IFMN = "N";


  public CxlCRWindow(String targetCompID,
                     String SenderCompID,String SenderSubID,String UserName,
                     TradeMessageProcessor trdwin,
                     String idFile,
                     String exchange,
                     boolean fx) throws Exception
  {
    super();
    mTargetCompID = targetCompID;
    mSenderCompID = SenderCompID;
    mUserName = UserName;
    mSenderSubID = SenderSubID;
    mTrdWin = trdwin;
    idGen = FixUniqueIdGen.getInstance(idFile);
    mExchange = exchange;
    isFX = fx;
  }
  // ========================================================================
  // ========================================================================
  public void actionPerformed(ActionEvent e)
  {
    System.out.println("CxlCRWindow::actionPerformed:: "+e.toString());
    System.out.println("ActionCommand["+e.getActionCommand()+"]");
    String cmd = e.getActionCommand();
    try
    {
    if(cmd == SELL)     mSide = eSELL;
    else if(cmd == BUY) mSide = eBUY;
    else if(cmd == PUT)  mCallPut = ePUT;
    else if(cmd == CALL) mCallPut = eCALL;
    else if(cmd == MARKET) mOrdType = FixFieldConstants.ORDER_TYPE_MARKET;
    else if(cmd == LIMIT)  mOrdType = FixFieldConstants.ORDER_TYPE_LIMIT;
    else if(cmd == STOP) mOrdType = FixFieldConstants.ORDER_TYPE_STOP;
    else if(cmd == MARKET_LIMIT)  mOrdType = FixFieldConstants.ORDER_TYPE_MARKETLIMIT;
    else if(cmd == STOP_LIMIT)  mOrdType = FixFieldConstants.ORDER_TYPE_STOPLIMIT;    
	else if(cmd == IFMY)  mIFMFlag = "Y";        
	else if(cmd == IFMN)  mIFMFlag = "N";
    else if(cmd == CANCEL) submitCancel();
    else if(cmd == CANCELREPLACE) 
    {
    
        if(validateCancelReplace())
    	{
    		try
    		{
	    		submitCancelReplace();
    		}
    		catch(Exception ex )
    		{
    			ex.printStackTrace();
    		}
    	}
    }
    else if(cmd == RESET) resetFields();
	else if(cmd == ORDER_STATUS)
	{
    		try
    		{
	    		submitStatusRequest();
    		}
    		catch(Exception ex )
    		{
    			ex.printStackTrace();
    		}
	  
	}
    }
    catch(Exception ex)
    {
    	ex.printStackTrace();
    }
  }
  // ========================================================================
  // ========================================================================
  public void displayOrder(Order ord, Component dialogFrame)
  {
    mWorkingOrder = ord;
    mDialogFrame = dialogFrame;
    tfOrigORDERID.setText(ord.getOrderId());
    tfOrigSIDE.setText(ord.getBuyOrSell());
    tfOrigQTY.setText(ord.getQuantity().toString());
    tfOrigCONTRACT.setText(ord.getSymbol());
    tfOrigPRICE.setText(ord.getPrice());
    tfOrigORDTYPE.setText(ord.getOrderType());
    tfOrigSTOPPRICE.setText(ord.getStopPrice());
    //tfOrigPUTCALL.setText(ord.getPutCall());
  }
  private boolean validateCancelReplace()
  {
  	String error = null;
  	
    System.out.println("SEND CANCEL REPLACE...");
    if(mWorkingOrder == null)
    {
  		GuiUtil.displayError("You must select an order");
  		return false;    	
    }
     
    String price = tfPRICE.getText();
    String qty = tfQTY.getText();
    String stopPrice = tfSTOPPRICE.getText();
	String orderType = IBusinessObject.translateToFixOrderType(mWorkingOrder.getOrderType());
  	
  	System.out.println("OrdType = " + mOrdType);
  	System.out.println("price = " + price);  	
    System.out.println("Qty="+qty);
  	
  	System.out.println("price is blank = " + StringUtilities.isBlank(price)  ); 
  	
  	// make sure that if order type is stop limit, then stoplimit price is entered
  	if( StringUtilities.isBlank(price) || StringUtilities.isBlank(qty))
  	{
  		error = "Price or Qty is not filled";
  	}
  	else if( (mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOP)||
			  	mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOPLIMIT))
  	   && StringUtilities.isBlank(stopPrice) )
  	{
  		error = "Stop Price must be entered when order type is stop or stop limit";
  	}
  		
	else if(price.equals(tfOrigPRICE.getText().trim())
	   && qty.equals(tfOrigQTY.getText().trim())
	   && orderType.equals(mOrdType))
	{
		error = "At least one of Price, Qty, OrderType must change ";
	}
  	
  	if( error != null )
  	{
  		GuiUtil.displayError(error);
  		return false;
  	}
  	
  	return true;

  	
  }

  // ========================================================================
  // ========================================================================
  private void submitCancelReplace() throws Exception
  {
    System.out.println("SEND CANCEL REPLACE...");
     
    String price = tfPRICE.getText();
    String stopPrice = tfSTOPPRICE.getText();
    String qty = tfQTY.getText();

    FixCancelReplace cxr = new FixCancelReplace();
    
    // ??? ACCOUNT is a modifiable field - for now use the one from order
    // ice does not have account tag
    if(!mExchange.equals("ICE"))
    	cxr.setAccount(mWorkingOrder.getAccount());
    
    String cxrId = null;
        
    if(mExchange.equals("ICE"))
    {
    	cxrId = mSenderCompID + mSenderSubID + idGen.getNextOrderId(7);
    	cxr.setOriginatorUserID(mUserName);
    }
    else
    {
    	cxrId = mSenderCompID + "CXR" + idGen.getNextOrderId();
    }
    
   
   	cxr.setClOrderID(cxrId);
   	 
	cxr.setHandlInst(FixFieldConstants.HANDL_INST_AUTOMATED); // "1"
	
	if(!mExchange.equals("ICE"))
		cxr.setOrderID(mWorkingOrder.getOrderId());       
		
	cxr.setOrderQty(qty);
	// ??? order type is a modifiable field -- for now use it from order
	//String orderType = IBusinessObject.translateToFixOrderType(mWorkingOrder.getOrderType());
	cxr.setOrdType(mOrdType);
	//??? verify orig cl orderid
	cxr.setOrigClOrderID(mWorkingOrder.getClientOrderId());
	
	if(!(mOrdType.equals(FixFieldConstants.ORDER_TYPE_MARKET) ||
		mOrdType.equals(FixFieldConstants.ORDER_TYPE_MARKETLIMIT) ||
		mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOP)))
		cxr.setPrice( price );
		
	cxr.setSide(IBusinessObject.translateToFixBuyOrSell(mWorkingOrder.getBuyOrSell()));	
	cxr.setSymbol(mWorkingOrder.getProductGroup());
	//cxr.setText() -- NR
	// ??? TIF is a modifiable field -- for now use the one from order
	String tif = IBusinessObject.translateToFixTimeInForce(mWorkingOrder.getTimeInForce());
	cxr.setTimeInForce(tif);
	cxr.setTransactTime(FixMessage.getUTCCurrentTime());	
	// cxr.setOpenClose() - NR	
	// stop price is a modifiable field - for now use from order
	if (mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOP)
         || mOrdType.equals(FixFieldConstants.ORDER_TYPE_STOPLIMIT))
     cxr.setStopPx(stopPrice);	
     
     if(mExchange.equals("CME"))
     {
     	// security desc refers to the actual contract symbol
     	cxr.setSecurityDesc(mWorkingOrder.getSymbol());
     }
	if(mExchange.equals("ICE"))
	{
		cxr.setClientID(mSenderCompID);
	}     
	//cxr.setClientID();  -- used in third party transactions
	//cxr.setMinQty() -- NR
    if( !StringUtilities.isBlank(mWorkingOrder.getMinQty()))   
    {
    	cxr.setMinQty(mWorkingOrder.getMinQty());
    }
	
	if(mExchange.equals("CME") || mExchange.equals("ICE"))
    {
		cxr.setSecurityType(mWorkingOrder.getSecurityType());
    }
    
    //?? hard coded to customer
    //cxr.setCustomerOrFirm(FixFieldConstants.CUSTOMER_OR_FIRM_CUSTOMER);    
    if(mExchange.equals("CME"))
    {
    	cxr.setCustomerOrFirm(mWorkingOrder.getCustomerOrFirm());
    }
    
    if( !StringUtilities.isBlank(mWorkingOrder.getMaxShow()))   
    {
    	cxr.setMaxShow(mWorkingOrder.getMaxShow());
    }
    //cxr.setMaxShow() -- NR
    if( !StringUtilities.isBlank(mWorkingOrder.getCtiCode()))   
    {
    	cxr.setCtiCode(mWorkingOrder.getCtiCode());
    }
    
    //cxr.setCtiCode(FixFieldConstants.CTI_CODE_BROKER_CUSTOMER_ACCOUNT); 
    
	if(tif.equals(FixFieldConstants.TIME_IN_FORCE_GTD))
	{
		// assuming that gtDate is in the right format yyyyMMdd
		// we may have to validate the format
		cxr.setExpireDate(mWorkingOrder.getExpireDate());
	}
    if(!StringUtilities.isBlank(mWorkingOrder.getSubAccount()))
    {
    	cxr.setOmnibusAccount(mWorkingOrder.getSubAccount());
    }	
    
    //cxr.setFeeBilling() -- NR

	    if( !StringUtilities.isBlank(mWorkingOrder.getGiveupFirm()))   
 	   {
  	  		cxr.setGiveupFirm(mWorkingOrder.getGiveupFirm());
    		cxr.setCmtaGiveupCD(mWorkingOrder.getCmtaGiveupCode());
    	}
   

    //cxr.setGiveupFirm()  -- NR
    //cxr.setCmtaGiveupCD()  -- NR	
	if(mExchange.equals("CME"))
    {    
		cxr.setCorrelationClOrdID(mWorkingOrder.getCorrelationClientOrderId());     
    
	
    	cxr.setIFMOverride(mIFMFlag);
    }
    
    if(FixFieldConstants.SECURITY_TYPE_OPTION.equals(mWorkingOrder.getSecurityType()))
	{	
			cxr.setPutOrCall(mWorkingOrder.getPutOrCall());
			cxr.setStrikePrice(mWorkingOrder.getStrike());
			cxr.setMaturityYear(mWorkingOrder.getExpYYYYMM());
			
			if( !StringUtilities.isBlank(mWorkingOrder.getExpDay())) 
				cxr.setMaturityDay(mWorkingOrder.getExpDay());
	}
        
    if(mTrdWin != null) {
    	mTrdWin.processCancelReplace(cxr);
    	TraderWindow.logWriter.logInFixMessage(cxr);
    }
    mWorkingOrder = null;
    resetOrigFields();
    resetFields();    

  }
  // ========================================================================
  // ========================================================================
  private void submitCancel() throws Exception
  {
    System.out.println("SEND CANCEL...");
    if(mWorkingOrder == null)return;

    /*
    Cancel cxl = new Cancel();
    String cxlId = mSenderCompID + "-" + String.valueOf(++mAutoGenOrderID);
    cxl.setCancelId(cxlId);
    cxl.setTBCOrderId( mWorkingOrder.getOrderId());
    cxl.setSymbol( mWorkingOrder.getSymbol());
    cxl.setBuyOrSell(mSide);
    cxl.setUser(mSenderCompID);
    cxl.setSubject("TRADE.REQ.CXL");
    */
    
    FixCancel cxl = new FixCancel();
    // ice does not have account tag
    if(!mExchange.equals("ICE"))    
    cxl.setAccount(mWorkingOrder.getAccount());

    
    //cxl.setTargetCompID(mTargetCompID);
    //cxl.setSendingTime(new Date(0));
    //cxl.setTransactTime(new Date(0));
    
    
    String cxlId = null;
        
    if(mExchange.equals("ICE"))
    {
    	cxlId = mSenderCompID + mSenderSubID + idGen.getNextOrderId(7);
    	cxl.setOriginatorUserID(mUserName);
    	
    }
    else
    {
    	cxlId = mSenderCompID + "CXL" + idGen.getNextOrderId();
    }
    
    cxl.setClOrderID(cxlId);
    
    // ice does not have orderid tag
    if(!mExchange.equals("ICE"))        
    	cxl.setOrderID(mWorkingOrder.getOrderId());
    
    cxl.setOrigClOrderID( mWorkingOrder.getClientOrderId());
    
	if(mExchange.equals("ICE"))
	{
		cxl.setClientID(mSenderCompID);
	}
    cxl.setSide(IBusinessObject.translateToFixBuyOrSell(mWorkingOrder.getBuyOrSell()));
    cxl.setSymbol( mWorkingOrder.getProductGroup());

	//cxl.setText() -- NR
	cxl.setTransactTime(FixMessage.getUTCCurrentTime());
	
	if(mExchange.equals("CME") || mExchange.equals("ICE"))
    cxl.setSecurityDesc( mWorkingOrder.getSymbol());

	if(mWorkingOrder.getSecurityType() != null && mExchange.equals("CME"))
		cxl.setSecurityType(mWorkingOrder.getSecurityType());
	
	if(mExchange.equals("CME"))
	cxl.setCorrelationClOrdID(mWorkingOrder.getCorrelationClientOrderId());

        
    //cxl.setSenderCompID(mSenderCompID);
    //if(!mExchange.equals("ICE"))
    cxl.setOrderQty(String.valueOf(mWorkingOrder.getQuantity()));
    //cxl.setSubject("TRADE.REQ.CXL");
   
    
    if(mTrdWin != null) { 
    	mTrdWin.processCancel(cxl);
    	TraderWindow.logWriter.logInFixMessage(cxl);
    }
    mWorkingOrder = null;
    resetOrigFields();
    resetFields();
  }
    // ========================================================================
  // ========================================================================
  private void submitStatusRequest() throws Exception
  {
    System.out.println("SEND STATUS...");
    if(mWorkingOrder == null)return;

    /*
    Cancel cxl = new Cancel();
    String cxlId = mSenderCompID + "-" + String.valueOf(++mAutoGenOrderID);
    cxl.setCancelId(cxlId);
    cxl.setTBCOrderId( mWorkingOrder.getOrderId());
    cxl.setSymbol( mWorkingOrder.getSymbol());
    cxl.setBuyOrSell(mSide);
    cxl.setUser(mSenderCompID);
    cxl.setSubject("TRADE.REQ.CXL");
    */
    
    FixOrderStatusRequest stat = new FixOrderStatusRequest();
    
    stat.setOrderID(mWorkingOrder.getOrderId());
    stat.setClOrderID( mWorkingOrder.getClientOrderId());
    

    stat.setSide(IBusinessObject.translateToFixBuyOrSell(mWorkingOrder.getBuyOrSell()));
    stat.setSymbol( mWorkingOrder.getProductGroup());

	stat.setTransactTime(FixMessage.getUTCCurrentTime());
	
	if(!mExchange.equals("FIX"))
    stat.setSecurityDesc( mWorkingOrder.getSymbol());

	if(mWorkingOrder.getSecurityType() != null && !mExchange.equals("FIX"))
		stat.setSecurityType(mWorkingOrder.getSecurityType());
	
	if(!mExchange.equals("FIX"))
	stat.setCorrelationClOrdID(mWorkingOrder.getCorrelationClientOrderId());
            
    if(mTrdWin != null) { 
    	mTrdWin.processStatusRequest(stat);
 //   	TraderWindow.logWriter.logInFixMessage(stat);
    }

    mWorkingOrder = null;
    resetOrigFields();
    resetFields();
    
  }

  // ========================================================================
  private void resetFields()
  {
    tfQTY.setText("");
    tfCONTRACT.setText("");
    tfPRICE.setText("");
  }
  private void resetOrigFields()
  {
    tfOrigORDERID.setText("");
    tfOrigSIDE.setText("");
    tfOrigQTY.setText("");
    tfOrigCONTRACT.setText("");
    tfOrigPRICE.setText("");
    tfOrigORDTYPE.setText("");
    //tfOrigPUTCALL.setText("");

  }
  // ========================================================================
  public Component createComponent()
  {
    System.out.println("OrderWindow::createComponent");

    Dimension dimField = new Dimension( 120, 20);
    EmptyBorder border = new EmptyBorder(new Insets(0,0,0,10));
    cxrPane = new JPanel(new BorderLayout());
    cxrPane.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    // add space around all component to avoid clutter
    c.insets = new Insets(2,2,2,2);

    int gridY = 0;

    JLabel lbla = new JLabel("Original Order");
    JLabel lblb = new JLabel("Modified Order");
    c.gridx = 1; c.gridy = gridY++;
    cxrPane.add(lbla, c);
    c.gridx = 2;
    cxrPane.add(lblb, c);

    JLabel lbl0 = new JLabel("Original OrderID");
    lbl0.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl0, c);
    tfOrigORDERID = new JTextField();
    tfOrigORDERID.setEditable(false);
    tfOrigORDERID.setPreferredSize(dimField);
    c.gridx = 1;
    cxrPane.add(tfOrigORDERID,c);


    JLabel lbl1 = new JLabel("Side");
    lbl1.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl1, c);
    tfOrigSIDE = new JTextField();
    tfOrigSIDE.setEditable(false);
    tfOrigSIDE.setPreferredSize(dimField);
    c.gridx = 1;
    cxrPane.add(tfOrigSIDE,c);
    /*
    JRadioButton radioBUY = new JRadioButton(BUY);
    radioBUY.setSelected(true);
    radioBUY.addActionListener(this);
    radioBUY.setActionCommand(BUY);
    mSide = eBUY;
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
    c.gridx = 2;
    cxrPane.add(radioPaneSIDE,c);
    */
 

    JLabel lbl2 = new JLabel("Quantity");
    lbl2.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl2,c);
    tfOrigQTY = new JTextField();
    tfOrigQTY.setEditable(false);
    tfOrigQTY.setPreferredSize(dimField);
    c.gridx = 1;
    cxrPane.add(tfOrigQTY, c);
    tfQTY = new JTextField();
    tfQTY.setPreferredSize(dimField);
    tfQTY.addActionListener(this);
    c.gridx = 2;
    cxrPane.add(tfQTY,c);

    JLabel lbl3 = new JLabel("Contract");
    lbl3.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl3,c);
    tfOrigCONTRACT = new JTextField();
    tfOrigCONTRACT.setEditable(false);
    tfOrigCONTRACT.setPreferredSize(dimField);
    c.gridx = 1;
    cxrPane.add(tfOrigCONTRACT,c);
    tfCONTRACT = new JTextField();
    tfCONTRACT.setPreferredSize(dimField);
    tfCONTRACT.setEditable(false);
    tfCONTRACT.addActionListener(this);
    c.gridx = 2;
    cxrPane.add(tfCONTRACT,c);

    JLabel lbl4 = new JLabel("Order Type");
    lbl4.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl4, c);
    tfOrigORDTYPE = new JTextField();
    tfOrigORDTYPE.setEditable(false);
    tfOrigORDTYPE.setPreferredSize(dimField);
    c.gridx = 1;
    cxrPane.add(tfOrigORDTYPE,c);
    
    /*
    JRadioButton radioLMT = new JRadioButton(LIMIT);
    radioLMT.setSelected(true);
    radioLMT.setActionCommand(LIMIT);
    radioLMT.addActionListener(this);
    mOrdType = eLIMIT;
    JRadioButton radioMKT = new JRadioButton(MARKET);
    radioMKT.setActionCommand(MARKET);
    radioMKT.addActionListener(this);
    ButtonGroup btngroupORDTYPE = new ButtonGroup();
    btngroupORDTYPE.add(radioLMT);
    btngroupORDTYPE.add(radioMKT);
    JPanel radioPaneORDTYPE = new JPanel();
    radioPaneORDTYPE.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
    radioPaneORDTYPE.add(radioLMT);
    radioPaneORDTYPE.add(radioMKT);
    c.gridx = 2;
    cxrPane.add(radioPaneORDTYPE,c);
	*/
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
    

    c.gridx = 2;
    cxrPane.add(radioPaneORDTYPE,c);
	

    JLabel lbl6 = new JLabel("LimitPrice");
    lbl6.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl6,c);
    tfOrigPRICE = new JTextField();
    tfOrigPRICE.setEditable(false);
    tfOrigPRICE.setPreferredSize(dimField);
    c.gridx = 1;
    cxrPane.add(tfOrigPRICE,c);
    tfPRICE = new JTextField();
    tfPRICE.setPreferredSize(dimField);
    tfPRICE.addActionListener(this);
    c.gridx = 2;
    cxrPane.add(tfPRICE,c);

    JLabel lbl7 = new JLabel("StopPrice");
    lbl7.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl7,c);
    tfOrigSTOPPRICE = new JTextField();
    tfOrigSTOPPRICE.setEditable(false);
    tfOrigSTOPPRICE.setPreferredSize(dimField);
    c.gridx = 1;
    cxrPane.add(tfOrigSTOPPRICE,c);
    tfSTOPPRICE = new JTextField();
    tfSTOPPRICE.setPreferredSize(dimField);
    tfSTOPPRICE.addActionListener(this);
    c.gridx = 2;
    cxrPane.add(tfSTOPPRICE,c);


    JLabel lbl8 = new JLabel("CXR IFM Flag");
    lbl8.setBorder(border);
    c.gridx = 0; c.gridy = gridY++;
    cxrPane.add(lbl8, c);

    JRadioButton radioIFMY = new JRadioButton("Yes");
    radioIFMY.setSelected(true);
    radioIFMY.setActionCommand(IFMY);
    radioIFMY.addActionListener(this);
    mIFMFlag = "Y";
    
    JRadioButton radioIFMN = new JRadioButton("No");
    radioIFMN.setActionCommand(IFMN);
    radioIFMN.addActionListener(this);

 
    
    ButtonGroup btngroupIFM = new ButtonGroup();
    btngroupIFM.add(radioIFMY);
    btngroupIFM.add(radioIFMN);
    
    JPanel radioPaneIFM = new JPanel();
    radioPaneIFM.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
    
    radioPaneIFM.add(radioIFMY);
    radioPaneIFM.add(radioIFMN);
    

    c.gridx = 2;
    cxrPane.add(radioPaneIFM,c);
    

    JButton btnCANCEL = new JButton(CANCEL);
    c.gridx = 4; c.gridy = gridY++;
    btnCANCEL.setActionCommand(CANCEL);
    btnCANCEL.addActionListener(this);
    cxrPane.add(btnCANCEL, c);
    JButton btnCANCELREPLACE = new JButton(CANCELREPLACE);
    c.gridx = 5;
    btnCANCELREPLACE.addActionListener(this);
    cxrPane.add(btnCANCELREPLACE, c);

    JButton btnRESET = new JButton(RESET);
    btnRESET.setActionCommand(RESET);
    btnRESET.addActionListener(this);
    c.gridx = 5; c.gridy = gridY++;
    cxrPane.add(btnRESET, c);

    JButton btnSTATUS = new JButton(ORDER_STATUS);
    btnSTATUS.setActionCommand(ORDER_STATUS);
    btnSTATUS.addActionListener(this);
    c.gridx = 5; c.gridy = gridY++;
    cxrPane.add(btnSTATUS, c);

    return cxrPane;
  }
}
