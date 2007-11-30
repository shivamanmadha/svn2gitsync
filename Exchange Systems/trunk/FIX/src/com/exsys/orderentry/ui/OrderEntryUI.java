package com.exsys.orderentry.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
import com.exsys.service.*;
import com.exsys.orderentry.*;

public class OrderEntryUI {

    private static IFTradingSessionManager mTradingSession;
    private static String mContractName;
    private static String mContractPrice;
    private static String mQty;

    private static String mOrderPubSub;
    private static String mSenderCompID;
    private static String mTargetCompID;
    private static int orderid = 0;

    private final static String eSideBUY = "1";
    private final static String eSideSELL = "2";
    private static String mSide;

    private static String mCallPut;
    private final static String eCALL = "1";
    private final static String ePUT = "0";

    private static String mOrdType;
    private final static String eMARKET = "1";
    private final static String eLIMIT = "2";

    private JTextField tfContract;
    private JTextField tfPrice;
    private JTextField tfQty;

    public OrderEntryUI(IFTradingSessionManager inSession)
    {
      mTradingSession = inSession;

      //KRR --- added
      String reqPubSub=null;

      try
      {
        reqPubSub = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_REQ_PUB_SUB);
        System.out.println("Req Publish Subject is " + reqPubSub );
        mSenderCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_SENDER_COMPANY_ID);
        System.out.println("Sender Comp ID is " + mSenderCompID );
        mTargetCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_TARGET_COMPANY_ID);
        System.out.println("Target Comp ID is " + mTargetCompID );
      }
      catch( ConfigAttributeNotFound exc )
      {
        System.out.println("CONFIG VALUES DOES NOT EXIST");
        exc.printStackTrace();
        System.exit(0);
      }

      mOrderPubSub = reqPubSub;
    }

    public Component createOrderScreen()
    {
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints  c = new GridBagConstraints();
      JPanel pane = new JPanel();
      pane.setBorder(
        BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder("Order Entry"),
          BorderFactory.createEmptyBorder(5,5,5,5)));
      pane.setLayout(gridbag);

      // ********** CONTRACT ***********
      final JLabel labelContract = new JLabel("Contract Name");
      pane.add(labelContract);
      c.gridx = 0; c.gridy = 0;
      gridbag.setConstraints(labelContract, c);
      pane.add(labelContract);

      tfContract = new JTextField("ESZ2");
      tfContract.setColumns(10);
      c.gridx = 0; c.gridy = 1;
      gridbag.setConstraints(tfContract, c);
      pane.add(tfContract);
      tfContract.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
          System.out.println("Inside CONTRACT tF");
        }
      });

      // ********** PRICE ***********
      final JLabel labelPrice = new JLabel("Contract Price");
      c.gridx = 1; c.gridy = 0;
      gridbag.setConstraints(labelPrice, c);
      pane.add(labelPrice);

      tfPrice = new JTextField("1200");
      tfPrice.setColumns(10);
      c.gridx = 1; c.gridy = 1;
      gridbag.setConstraints(tfPrice, c);
      pane.add(tfPrice);
      tfPrice.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
          System.out.println("Inside PRICE tF");
        }
      });

      // ********** Quantity ***********
      final JLabel labelQty = new JLabel("Quantity");
      c.gridx = 2; c.gridy = 0;
      gridbag.setConstraints(labelQty, c);
      pane.add(labelQty);

      tfQty = new JTextField("1");
      tfQty.setColumns(10);
      c.gridx = 2; c.gridy = 1;
      gridbag.setConstraints(tfQty, c);
      pane.add(tfQty);
      tfQty.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
          System.out.println("Inside PRICE tF");
        }
      });

      // *********** PUT CALL ***********
      final JLabel labelPutCall = new JLabel("Put/Call");
      c.gridx = 0; c.gridy = 2;
      gridbag.setConstraints(labelPutCall, c);
      pane.add(labelPutCall);

      String[] strCallPut = { "Call", "Put" };
      JComboBox listCallPut = new JComboBox(strCallPut);
      c.gridx = 0; c.gridy = 3;
      gridbag.setConstraints(listCallPut, c);
      pane.add(listCallPut);
      listCallPut.setSelectedIndex(0);
      mCallPut = ePUT;
      listCallPut.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JComboBox cb = (JComboBox)e.getSource();
          String pcName = (String)cb.getSelectedItem();
          if(pcName.compareTo("Call") == 0)
            mCallPut = ePUT;
          else
            mCallPut = eCALL;
        }
      });

      // *********** Order Type ***********
      final JLabel labelOrdType = new JLabel("Order Type");
      c.gridx = 1; c.gridy = 2;
      gridbag.setConstraints(labelOrdType, c);
      pane.add(labelOrdType);

      String[] strOrdType = { "Market", "Limit" };
      JComboBox listOrdType = new JComboBox(strOrdType);
      listOrdType.setSize(10,2);
      c.gridx = 1; c.gridy = 3;
      gridbag.setConstraints(listOrdType, c);
      pane.add(listOrdType);
      listOrdType.setSelectedIndex(1);
      mOrdType = eMARKET;
      listOrdType.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JComboBox cb = (JComboBox)e.getSource();
          String pcName = (String)cb.getSelectedItem();
          if(pcName.compareTo("Market") == 0)
          {
            mOrdType = eMARKET;
            mContractPrice = "0.0";
          }
          else
            mOrdType = eLIMIT;
        }
      });

      // *********** SIDE ***********
      final JLabel labelSide = new JLabel("Side");
      c.gridx = 2; c.gridy = 2;
      gridbag.setConstraints(labelSide, c);
      pane.add(labelSide);

      String[] strSide = { "Buy", "Sell" };
      JComboBox listSide = new JComboBox(strSide);
      c.gridx = 2; c.gridy = 3;
      gridbag.setConstraints(listSide, c);
      pane.add(listSide);
      listSide.setSelectedIndex(0);
      mSide = eSideBUY;
      listSide.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JComboBox cb = (JComboBox)e.getSource();
          String pcName = (String)cb.getSelectedItem();
          if(pcName.compareTo("Buy") == 0)
            mSide = eSideBUY;
          else
            mSide = eSideSELL;
        }
      });

      // **** EXECUTION ****
      final JButton buttonSendOrder = new JButton("SendOrder");
      c.gridx = 3; c.gridy = 4;
      gridbag.setConstraints(buttonSendOrder, c);
      pane.add(buttonSendOrder);
      buttonSendOrder.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          sendOrder();
        }
      });

      return pane;
    }

    private void sendOrder() {

      System.out.println("SEND ORDER...");

      mContractName = tfContract.getText();
      mContractPrice = tfPrice.getText();
      mQty = tfQty.getText();

      FixOrder order = new FixOrder();
      order.setSenderCompID(mSenderCompID);
      order.setTargetCompID(mTargetCompID);
      order.setSendingTime(new Date(0));
      order.setSide(mSide);
      //KRR -- added
      order.setTimeInForce("0");
      String clOrderID = mSenderCompID + "-" + String.valueOf(orderid);
      order.setOrderQty(mQty);
      order.setClOrderID(clOrderID);
      order.setHandlInst("1");
      order.setSymbol(mContractName);
      order.setPutOrCall(mCallPut);

      order.setOrdType(mOrdType);

      order.setTransactTime(new Date(0));
      if(mOrdType == eMARKET)
        order.setPrice("0.0");
      else
        order.setPrice(mContractPrice);

      try {
        mTradingSession.sendFixMessage(order, mOrderPubSub);
        ++orderid;
      }
      catch(Exception exp) {
        System.out.println("sendOrder exception: " + exp.getMessage());
      }
    }
}
