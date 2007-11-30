package com.exsys.orderentry;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

import com.exsys.application.*;
import com.exsys.service.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.fix.message.*;
import com.exsys.fix.session.*;
import com.exsys.common.business.*;
import com.exsys.orderentry.ui.*;

public class PerfTraderWindow
  extends BaseGUIApplication
  implements TradeMessageProcessor,
  			 ListSelectionListener,
  			 ActionListener,
  			 SecuritiesMessageProcessor
{
  // ========================================================================
  // ========================================================================
  public PerfTraderWindow(String args[]) throws ConfigFileNotFound, Exception
  {
    super(args);
    processConfig();
    initializeSession(); // initialize jms session -- do this first...
    createWindows();
    restoreFromFix();

    mTheFrame = new JFrame("Trading Window");
    mTheFrame.addWindowListener(new WindowAdapter() {
     public void windowClosing(WindowEvent e) {
       System.exit(0);
     }
    });
    mTheFrame.setContentPane(this);
    mTheFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    mTheFrame.pack();
    mTheFrame.setVisible(true);
  }
  public static String translateFromFixOrderStatus(String fixStatus)
  {
	String status = null;
	if(fixStatus.equals("0"))
	{
	  status = FixConstants.FIX_ORD_STATUS_NEW;
	}
	else if(fixStatus.equals("1"))
	{
	  status = FixConstants.FIX_ORD_STATUS_PARTIAL;
	}
	else if(fixStatus.equals("2"))
	{
	  status = FixConstants.FIX_ORD_STATUS_FILLED;
	}
	else if(fixStatus.equals("3"))
	{
	  status = FixConstants.FIX_ORD_STATUS_DONE;
	}
	else if(fixStatus.equals("4"))
	{
	  status = FixConstants.FIX_ORD_STATUS_CANCEL;
	}
	else if(fixStatus.equals("5"))
	{
	  status = FixConstants.FIX_ORD_STATUS_REPLACE;
	}
	else if(fixStatus.equals("6"))
	{
	  status = FixConstants.FIX_ORD_STATUS_PENDCXL;
	}
	else if(fixStatus.equals("7"))
	{
	  status = FixConstants.FIX_ORD_STATUS_STOPPED;
	}
	else if(fixStatus.equals("8"))
	{
	  status = FixConstants.FIX_ORD_STATUS_REJECT;
	}
	else if(fixStatus.equals("9"))
	{
	  status = FixConstants.FIX_ORD_STATUS_SUSPEND;
	}
	else if(fixStatus.equals("A"))
	{
	  status = FixConstants.FIX_ORD_STATUS_PENDNEW;
	}
	else if(fixStatus.equals("B"))
	{
	  status = FixConstants.FIX_ORD_STATUS_CAL;
	}
	else if(fixStatus.equals("C"))
	{
	  status = FixConstants.FIX_ORD_STATUS_EXPIRED;
	}
	else if(fixStatus.equals("D"))
	{
	  status = FixConstants.FIX_ORD_STATUS_ACCEPT;
	}
	else if(fixStatus.equals("E"))
	{
	  status = FixConstants.FIX_ORD_STATUS_PENDREP;
	}
	return status;
  }
  // ========================================================================
  // ========================================================================
  public void processExecutionReport(FixExecutionReport response)
  {
    Logger.debug("processExecutionReport");
    boolean validRespType = true;
    boolean targetFound = false;

    if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_FILL))//2
    {
      Logger.debug("FILL--");
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        Logger.debug("Order Status - "+targetOrder);
        String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
        			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
        			    +"< LeavesQty = >"+response.getLeavesQtyAsString()
        			    +"< LastMkt = >"+response.getLastMkt()+ "<";
        //Order table
        ordData = new Vector();
        ordData.add(response.getSecurityType()+"-ORD");
        ordData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()
        ordData.add(mExchange+"-EXCH"+response.getClOrderID());
        ordData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        ordData.add(ib.translateFromFixOrderType(response.getOrdType()));
        ordData.add(response.getOrderQtyAsString());
        ordData.add(response.getSecurityDesc());
        ordData.add(response.getPriceAsString());
        ordData.add(response.getStopPxAsString());
        ordData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        ordData.add(response.getLastSharesAsString());
        ordData.add(response.getLastPxAsString());
        ordData.add(response.getCumQtyAsString());
        ordData.add(response.getLeavesQtyAsString());
        ordData.add(response.getLastMkt());
        ordData.add("SUBMITTED FILL");
        if(mExchange.equals("ICE"))
        {
      	  strAdd += " DealID = >"+ response.getExecID() + "< ";
      	  strAdd += sdRespHandler.getDescription(response.getSymbol());
      	  ordData.add(response.getExecID());
      	  ordData.add(sdRespHandler.getDescription(response.getSymbol()));
      	  if(response.getOrderState() != null)
      	  {
      		if(response.getOrderState().equals("1"))
      		{
	      		strAdd += " OrderState is INACTIVE ";
      			ordData.add("INACTIVE");
      		}
	      	else
	      	{
		      	strAdd += " OrderState = >"+ response.getOrderState()+ "< ";
		      	ordData.add(response.getOrderState());
	      	}
      	  }
        }
        if(response.getMultiLegReportingType() != null)
        {
      	  strAdd += " MultiLeg ";
      	  ordData.add("Multileg");
        }
        if(mFXFlag)
        {
      	  strAdd += "Base Currency = "+ response.getCurrency();
      	  strAdd += "Value Date = "+ response.getFutSettDateAsString();
      	  strAdd += "Trade Date = "+ response.getTradeDateAsString();
      	  strAdd += "Settl Currency = "+ response.getSettlCurrency();
      	  strAdd += "Gross Trade Amt = "+ response.getGrossTradeAmtAsString();
      	  strAdd += "CalcCcyLastQty = "+ response.getCalcCcyLastQtyAsString();
      	  strAdd += "Aggressor Ind = "+ response.getAggressorIndicator();

      	  strAdd += "Spot Rate = "+ response.getPriceAsString();
      	  strAdd += "Expiration Date = "+ response.getExpireDateAsString();
      	  strAdd += "Security Desc = "+ response.getSecurityDesc();

       	  ordData.add(response.getCurrency());
       	  ordData.add(response.getFutSettDateAsString());
       	  ordData.add(response.getTradeDateAsString());
       	  ordData.add(response.getSettlCurrency());
       	  ordData.add(response.getGrossTradeAmtAsString());
       	  ordData.add(response.getCalcCcyLastQtyAsString());
       	  ordData.add(response.getAggressorIndicator());

       	  ordData.add(response.getPriceAsString());
       	  ordData.add(response.getExpireDateAsString());
       	  ordData.add(response.getSecurityDesc());
        }
        changeRow(ordData, response);
        targetOrder.setOpenQuantity(new Integer(0));
        targetOrder.OrderStatus(IBusinessObject.FILLED);
        mListModel.addElement(targetOrder.toString() + strAdd);
      //Execution table
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED FILL");
        execData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        execModel.addRow(execData);
      }
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PARTIAL))//2
    {
      Logger.debug("PARTIAL"+response.getLeavesQtyAsString());
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        Logger.debug("Order ["+targetOrder+"]");
        // ICE is sending quantity as double like 2.0
        //05212007
        if(mExchange.equals("ICE"))
        {
        	targetOrder.setOpenQuantity(new Integer((Double.valueOf(response.getLeavesQtyAsString())).intValue()));
        }
        else
        {
        	targetOrder.setOpenQuantity(Integer.valueOf(response.getLeavesQtyAsString()));
        }
        if(response.getLeavesQty() > 0)
          targetOrder.OrderStatus(IBusinessObject.PARTIALFILLED);
        else
          targetOrder.OrderStatus(IBusinessObject.FILLED);
        String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
        			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
        			    +"< LeavesQty = >"+response.getLeavesQtyAsString()
        			    +"< LastMkt = >"+response.getLastMkt()+ "<";
        //Order table
        ordData = new Vector();
        ordData.add(response.getSecurityType()+"-ORD");
        ordData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()
        ordData.add(mExchange+"-EXCH"+response.getClOrderID());
        ordData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        ordData.add(ib.translateFromFixOrderType(response.getOrdType()));
        ordData.add(response.getOrderQtyAsString());
        ordData.add(response.getSecurityDesc());
        ordData.add(response.getPriceAsString());
        ordData.add(response.getStopPxAsString());
        ordData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        ordData.add(response.getLastSharesAsString());
        ordData.add(response.getLastPxAsString());
        ordData.add(response.getCumQtyAsString());
        ordData.add(response.getLeavesQtyAsString());
        ordData.add(response.getLastMkt());
        ordData.add("SUBMITTED PARTIAL");
        if(mExchange.equals("ICE"))
        {
      	  strAdd += " DealID = >"+ response.getExecID() + "< ";
      	  strAdd += sdRespHandler.getDescription(response.getSymbol());
      	  ordData.add(response.getExecID());
      	  ordData.add(sdRespHandler.getDescription(response.getSymbol()));
      	  if(response.getOrderState() != null)
      	  {
      		if(response.getOrderState().equals("1"))
      		{
	      		strAdd += " OrderState is INACTIVE ";
	      		ordData.add("INACTIVE");
      		}
	      	else
	      	{
		      	strAdd += " OrderState = >"+ response.getOrderState()+ "< ";
		      	ordData.add(response.getOrderState());
	      	}
      	  }
        }
        if(response.getMultiLegReportingType() != null)
      	{
      	  strAdd += " MultiLeg ";
      	  ordData.add("MultiLeg");
      	}
        if(mFXFlag)
        {
      	  strAdd += "Base Currency = "+ response.getCurrency();
      	  strAdd += "Value Date = "+ response.getFutSettDateAsString();
      	  strAdd += "Trade Date = "+ response.getTradeDateAsString();
      	  strAdd += "Settl Currency = "+ response.getSettlCurrency();
      	  strAdd += "Gross Trade Amt = "+ response.getGrossTradeAmtAsString();
      	  strAdd += "CalcCcyLastQty = "+ response.getCalcCcyLastQtyAsString();
      	  strAdd += "Aggressor Ind = "+ response.getAggressorIndicator();
      	  strAdd += "Spot Rate = "+ response.getPriceAsString();
      	  strAdd += "Expiration Date = "+ response.getExpireDateAsString();
      	  strAdd += "Security Desc = "+ response.getSecurityDesc();
      	  ordData.add(response.getCurrency());
      	  ordData.add(response.getFutSettDateAsString());
      	  ordData.add(response.getTradeDateAsString());
      	  ordData.add(response.getSettlCurrency());
      	  ordData.add(response.getGrossTradeAmtAsString());
      	  ordData.add(response.getCalcCcyLastQtyAsString());
      	  ordData.add(response.getAggressorIndicator());
      	  ordData.add(response.getPriceAsString());
      	  ordData.add(response.getExpireDateAsString());
      	  ordData.add(response.getSecurityDesc());
        }
        changeRow(ordData, response);

        mListModel.addElement(targetOrder.toString() + strAdd);
        //Execution table
      	execData = new Vector();
      	execData.add(response.getSecurityType()+"-ORD");
      	execData.add(response.getCorrelationClOrdID());
      	execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
      	execData.add(response.getSecurityDesc());
      	execData.add(response.getOrderQtyAsString());
      	execData.add(response.getPriceAsString());
      	execData.add("SUBMITTED PARTIAL");
      	execData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        execModel.addRow(execData);
      }
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_CANCELED))//4
    {
      Logger.debug("CANCELED");

      // ICE is sending cancelled message for the cancel, and hence clorderid
      // refers to the cancel's clorderid, so we need to get origclorderid for ICE
      //05212007
      // ICE - in case of FOK - we need to use getClOrderID
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        targetOrder.OrderStatus(IBusinessObject.CLOSED);
        mListModel.addElement(targetOrder.toString() + "- CANCELLED");
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
      	execData.add(response.getClOrderID());
      	execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
      	execData.add(response.getSecurityDesc());
      	execData.add(response.getOrderQtyAsString());
      	execData.add(response.getPriceAsString());
      	execData.add("SUBMITTED CANCEL1");
      	execData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        execModel.addRow(execData);

      }
      else
      {
      	if(mExchange.equals("ICE"))
      	{
      	  Order targetOrder1 = findOrder(response.getOrigClOrderID());
    	  if(targetOrder1 != null)
   		  {
      		targetFound = true;
        	targetOrder1.OrderStatus(IBusinessObject.CLOSED);
        	mListModel.addElement(targetOrder1.toString() + "- CANCELLED");
        	execData = new Vector();
        	execData.add(response.getSecurityType()+"-ORD");
          	execData.add(response.getClOrderID());
          	execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
          	execData.add(response.getSecurityDesc());
          	execData.add(response.getOrderQtyAsString());
          	execData.add(response.getPriceAsString());
          	execData.add("SUBMITTED CANCEL2");
          	execData.add(translateFromFixOrderStatus(response.getOrdStatus()));
            execModel.addRow(execData);
          }
      	}
      	else
      	{
	   	  Cancel targetCancel = findCancel(response.getClOrderID());
 	      if(targetCancel != null)
  	   	  {
  	    	targetFound = true;
   	  		targetCancel.OrderStatus(IBusinessObject.CLOSED);
    		mListModel.addElement(targetCancel.toString()+ "- CANCELLED");
    		execData = new Vector();
   	  		execData.add("CXL: "+targetCancel.getCancelId());
   	  		execData.add("CXLing	:"+targetCancel.getTBCOrderId());
		   	execData.add("Status	:"+targetCancel.OrderStatus());
		   	execData.add("- CANCELLED");
		   	execModel.addRow(execData);
		  }
     	}
      }
      changeStatus(response);
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_TRADE_CANCELED ))
    {
      //!!! 04292007 - CME ONLY SENT last 5 digits of the clorderid NOT last 8
      Logger.debug("TRADE CANCELED");
      Order targetOrder = null;
      if(mExchange.equals("ICE"))
      {
     	targetOrder = findOrder(response.getClOrderID());
      }
      else
      {
      	targetOrder = findOrder(response.getClOrderID(),5);
      }
      if(targetOrder != null)
      {
      	targetFound = true;
      	targetOrder.OrderStatus(IBusinessObject.CLOSED);
      	mListModel.addElement(targetOrder.toString() + "- TRADE CANCELLED");
      	execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED TRADE CANCEL");
        execData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        execModel.addRow(execData);
        mListModel.addElement(targetOrder.toString() + "- TRADE CANCELLED");
      }
      changeStatus(response);
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_EXPIRED))
    {
      Logger.debug("EXPIRED");
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        targetOrder.OrderStatus(IBusinessObject.CLOSED);
        mListModel.addElement(targetOrder.toString() + " - EXPIRED");
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
      	execData.add(response.getCorrelationClOrdID());
      	execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
      	execData.add(response.getSecurityDesc());
      	execData.add(response.getOrderQtyAsString());
      	execData.add(response.getPriceAsString());
      	execData.add("SUBMITTED EXEC EXP");
      	execData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        execModel.addRow(execData);
      }
      changeStatus(response);
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_DONE_FOR_DAY))
    {
      Logger.debug("DONE FOR DAY");
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        targetOrder.OrderStatus(IBusinessObject.CLOSED);
        String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
        			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
        			    +"< LeavesQty = >"+response.getLeavesQtyAsString()
        			    +"< LastMkt = >"+response.getLastMkt()+ "<";
        //Order table
        ordData = new Vector();
        ordData.add(response.getSecurityType()+"-ORD");
        ordData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()
        ordData.add(mExchange+"-EXCH"+response.getClOrderID());
        ordData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        ordData.add(ib.translateFromFixOrderType(response.getOrdType()));
        ordData.add(response.getOrderQtyAsString());
        ordData.add(response.getSecurityDesc());
        ordData.add(response.getPriceAsString());
        ordData.add(response.getStopPxAsString());
        ordData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        ordData.add(response.getLastSharesAsString());
        ordData.add(response.getLastPxAsString());
        ordData.add(response.getCumQtyAsString());
        ordData.add(response.getLeavesQtyAsString());
        ordData.add(response.getLastMkt());
        ordData.add("SUBMITTED DONE FOR DAY");
        if(mExchange.equals("ICE"))
        {
      	  strAdd += " DealID = >"+ response.getExecID() + "< ";
      	  strAdd += sdRespHandler.getDescription(response.getSymbol());
      	  ordData.add(response.getExecID());
      	  ordData.add(sdRespHandler.getDescription(response.getSymbol()));
      	  if(response.getOrderState() != null)
      	  {
      		if(response.getOrderState().equals("1"))
	      		strAdd += " OrderState is INACTIVE ";
	      	else
		      	strAdd += " OrderState = >"+ response.getOrderState()+ "< ";
      	  }
      	  if(response.getMultiLegReportingType() != null)
      	  {
      		strAdd += " MultiLeg ";
      		ordData.add("MultiLeg");
      	  }
        }
        changeRow(ordData, response);
        mListModel.addElement(targetOrder.toString() + strAdd + " - DONE_FOR_DAY");
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED DONE FOR DAY");
        execData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        execModel.addRow(execData);
      }
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_NEW))//0
    {
      Logger.debug("NEW");
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
        			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
        			    +"< LeavesQty = >"+response.getLeavesQtyAsString()+ "<"
        			    +"< OrderType = >"+response.getOrdType()+ "<"
        			    +"< StopPrice = >"+response.getStopPxAsString()+ "<"
        			    +"< LimitPrice = >"+response.getPriceAsString()+ "<"
        			    +"< OrderID = >"+response.getOrderID()
        			    +"< LastMkt = >"+response.getLastMkt()+ "<";
        //Order table
        ordData = new Vector();
        ordData.add(response.getSecurityType()+"-ORD");
        ordData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()
        ordData.add(response.getOrderID());
        ordData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        ordData.add(ib.translateFromFixOrderType(response.getOrdType()));
        ordData.add(response.getOrderQtyAsString());
        ordData.add(response.getSecurityDesc());
        ordData.add(response.getPriceAsString());
        ordData.add(response.getStopPxAsString());
        ordData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        ordData.add(String.valueOf(response.getLastShares()));
        ordData.add(response.getLastPxAsString());
        ordData.add(response.getCumQtyAsString());
        ordData.add(response.getLeavesQtyAsString());
        ordData.add(response.getLastMkt());
        ordData.add("SUBMITTED NEW");
        if(mExchange.equals("ICE"))
        {
      	  strAdd += " DealID = >"+ response.getExecID() + "< ";
      	  strAdd += sdRespHandler.getDescription(response.getSymbol());
      	  ordData.add(response.getExecID());
      	  ordData.add(sdRespHandler.getDescription(response.getSymbol()));
      	  ordData.add(null);
        }
        if(response.getMultiLegReportingType() != null)
      	{
      	  strAdd += " MultiLeg ";
      	  ordData.add("MultiLeg");
      	}
        if(mFXFlag)
        {
      	  strAdd += "Spot Rate = "+ response.getPriceAsString();
      	  strAdd += "Expiration Date = "+ response.getExpireDateAsString();
      	  strAdd += "Security Desc = "+ response.getSecurityDesc();
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(response.getPriceAsString());
      	  ordData.add(response.getExpireDateAsString());
      	  ordData.add(response.getSecurityDesc());
        }
      	orderModel.addRow(ordData);
      	targetOrder.OrderStatus(IBusinessObject.ACCECPTED);
        targetOrder.setOrderId(response.getOrderID());
        mListModel.addElement(targetOrder.toString() + strAdd);
        changeStatus(response);
        //Execution table
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());//getOrderID():CMEEXCH000301		getClOrderID():0000301
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED NEW");
        execData.add(response.getText());
        execModel.addRow(execData);
      }
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PEND_REPLACE))
    {
      Logger.debug("PEND REPLACE");
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
        			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
        			    +"< LeavesQty = >"+response.getLeavesQtyAsString()+ "<"
        			    +"< OrderType = >"+response.getOrdType()+ "<"
        			    +"< StopPrice = >"+response.getStopPxAsString()+ "<"
        			    +"< LimitPrice = >"+response.getPriceAsString()+ "<"
        			    +"< OrderID = >"+response.getOrderID()
        			    +"< LastMkt = >"+response.getLastMkt()+ "<";
        //Order table
        ordData = new Vector();
        ordData.add(response.getSecurityType()+"-ORD");
        ordData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()
        ordData.add(mExchange+"-EXCH"+response.getClOrderID());
        ordData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        ordData.add(ib.translateFromFixOrderType(response.getOrdType()));
        ordData.add(response.getOrderQtyAsString());
        ordData.add(response.getSecurityDesc());
        ordData.add(response.getPriceAsString());
        ordData.add(response.getStopPxAsString());
        ordData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        ordData.add(response.getLastSharesAsString());
        ordData.add(response.getLastPxAsString());
        ordData.add(response.getCumQtyAsString());
        ordData.add(response.getLeavesQtyAsString());
        ordData.add(response.getLastMkt());
        ordData.add("SUBMITTED PEND REPLACE");
        if(mExchange.equals("ICE"))
        {
      	  strAdd += " DealID = >"+ response.getExecID() + "< ";
      	  strAdd += sdRespHandler.getDescription(response.getSymbol());
      	  ordData.add(response.getExecID());
      	  ordData.add(sdRespHandler.getDescription(response.getSymbol()));
      	  if(response.getOrderState() != null)
      	  {
      		if(response.getOrderState().equals("1"))
      		{
	      		strAdd += " OrderState is INACTIVE ";
	      		ordData.add("INACTIVE");
      		}
	      	else
	      	{
		      	strAdd += " OrderState = >"+ response.getOrderState()+ "< ";
		      	ordData.add(response.getOrderState());
	      	}
      	  }
        }
      	if(response.getMultiLegReportingType() != null)
      	{
      		strAdd += " MultiLeg ";
      		ordData.add("MultiLeg");
      	}
      	changeRow(ordData, response);
      	targetOrder.OrderStatus(IBusinessObject.PENDRPL);
        targetOrder.setOrderId(response.getOrderID());
        mListModel.addElement(targetOrder.toString() + strAdd);
        //Execution table
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED PEND REPL");
        execData.add(response.getText());
        execModel.addRow(execData);
      }
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REPLACED))//5
    {
      Logger.debug("REPLACED");
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
        			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
        			    +"< LeavesQty = >"+response.getLeavesQtyAsString()+ "<"
        			    +"< OrderType = >"+response.getOrdType()+ "<"
        			    +"< StopPrice = >"+response.getStopPxAsString()+ "<"
        			    +"< LimitPrice = >"+response.getPriceAsString()+ "<"
        			    +"< OrderID = >"+response.getOrderID()
        			    +"< LastMkt = >"+response.getLastMkt()+ "<";
        //Order table
        ordData = new Vector();
        ordData.add(response.getSecurityType()+"-ORD");
        ordData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()
        ordData.add(mExchange+"-EXCH"+response.getClOrderID());
        ordData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        ordData.add(ib.translateFromFixOrderType(response.getOrdType()));
        ordData.add(response.getOrderQtyAsString());
        ordData.add(response.getSecurityDesc());
        ordData.add(response.getPriceAsString());
        ordData.add(response.getStopPxAsString());
        ordData.add(translateFromFixOrderStatus(response.getOrdStatus()));
        ordData.add(response.getLastSharesAsString());
        ordData.add(response.getLastPxAsString());
        ordData.add(response.getCumQtyAsString());
        ordData.add(response.getLeavesQtyAsString());
        ordData.add(response.getLastMkt());
        ordData.add("SUBMITTED REPLACED");
        if(mExchange.equals("ICE"))
        {
      	  strAdd += " DealID = >"+ response.getExecID() + "< ";
      	  strAdd += sdRespHandler.getDescription(response.getSymbol());
    	  ordData.add(response.getExecID());
    	  ordData.add(sdRespHandler.getDescription(response.getSymbol()));
      	  if(response.getOrderState() != null)
      	  {
      		if(response.getOrderState().equals("1"))
      		{
	      		strAdd += " OrderState is INACTIVE ";
	      		ordData.add("INACTIVE");
      		}
	      	else
	      	{
		      	strAdd += " OrderState = >"+ response.getOrderState()+ "< ";
		      	ordData.add(response.getOrderState());
	      	}
      	  }
      	}
        if(response.getMultiLegReportingType() != null)
      	{
      		strAdd += " MultiLeg ";
      		ordData.add("MultiLeg");
      	}
        if(mFXFlag)
        {
      	  strAdd += "Spot Rate = "+ response.getPriceAsString();
      	  strAdd += "Expiration Date = "+ response.getExpireDateAsString();
      	  strAdd += "Security Desc = "+ response.getSecurityDesc();

      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(null);
      	  ordData.add(response.getPriceAsString());
      	  ordData.add(response.getExpireDateAsString());
      	  ordData.add(response.getSecurityDesc());
        }
        changeRow(ordData, response);
        targetOrder.OrderStatus(IBusinessObject.REPLACED);
        targetOrder.setOrderId(response.getOrderID());
        mListModel.addElement(targetOrder.toString() + strAdd);
//      Execution table
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED REPLACED");
        execData.add(response.getText());
        execModel.addRow(execData);
      }
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REJECTED))//8
    {
      Logger.debug("REJECTED");
      String strAdd = " - Reason = >"+response.getText()+"<";
      Order targetOrder = findOrder(response.getClOrderID());
      if(targetOrder != null)
      {
      	targetFound = true;
        targetOrder.OrderStatus(IBusinessObject.REJECTED);
        mListModel.addElement(targetOrder.toString()+strAdd);
//      Execution table
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED REJECTED");
        execData.add(" - Reason = >"+response.getText()+"<");
        execModel.addRow(execData);
      }
      else
      {
	    Cancel targetCancel = findCancel(response.getClOrderID());
 	    if(targetCancel != null)
  	    {
  	      targetFound = true;
   	  	  targetCancel.OrderStatus(IBusinessObject.REJECTED);
    	  mListModel.addElement(targetCancel.toString()+strAdd);
//        Execution table
   	  	  execData = new Vector();
   	  	  execData.add("CXL: "+targetCancel.getCancelId());
		  execData.add("CXLing	:"+targetCancel.getTBCOrderId());
	      execData.add("Status	:"+targetCancel.OrderStatus());
	      execData.add(" - Reason = >"+response.getText()+"<");
	      execModel.addRow(execData);
     	}
      }
      changeStatus(response);
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PEND_CANCEL))
    {
      Logger.debug("PENDCANCEL");
      String strAdd = " - PENDCANCEL ";
	  Cancel targetCancel = findCancel(response.getClOrderID());
 	  if(targetCancel != null)
  	  {
  	    targetFound = true;
   	  	targetCancel.OrderStatus(IBusinessObject.PENDCXL);
    	mListModel.addElement(targetCancel.toString()+strAdd);
// 	    Execution table
   	  	execData = new Vector();
	   	execData.add("CXL: "+targetCancel.getCancelId());
		execData.add("CXLing	:"+targetCancel.getTBCOrderId());
	    execData.add("Status	:"+targetCancel.OrderStatus());
	    execData.add(" - PENDCANCEL ");
	    execModel.addRow(execData);
      }
 	 changeStatus(response);
    }
    else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_DUG))
    {
     //!!! 04292007 - CME ONLY SENT last 5 digits of the clorderid NOT last 8
     Logger.debug ("DEAL UNDER INVESTIGATION");
     Order targetOrder = null;
     if(mExchange.equals("ICE"))
     {
       targetOrder = findOrder(response.getClOrderID());
     }
     else
     {
      targetOrder = findOrder(response.getClOrderID(),5);
     }
     if(targetOrder != null)
     {
       targetFound = true;
        targetOrder.OrderStatus(IBusinessObject.CLOSED );
        mListModel.addElement(targetOrder.toString() + "- DEAL UNDER INVESTIGATION");
//      Execution table
        execData = new Vector();
        execData.add(response.getSecurityType()+"-ORD");
        execData.add(response.getCorrelationClOrdID());
        execData.add(ib.translateFromFixBuyOrSell(response.getSide()));
        execData.add(response.getSecurityDesc());
        execData.add(response.getOrderQtyAsString());
        execData.add(response.getPriceAsString());
        execData.add("SUBMITTED DUG");
        execData.add(response.getText() + "- DEAL UNDER INVESTIGATION");
        execModel.addRow(execData);
      }
     changeStatus(response);
    }
    else
    {
      validRespType = false;
      System.err.println("Unknown Response Type");
      mListModel.addElement("Unknown Response Type - " + response.getExecType());

      execData = new Vector();
      execData.add("Unknown Response Type - " + response.getExecType());
	  execModel.addRow(execData);
      return;
    }
    if( !targetFound )
    {
      String strAdd = "ORD NOT FOUND  -"+response.getClOrderID()+" - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
        			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
        			    +"< LeavesQty = >"+response.getLeavesQtyAsString()+ "<"
        			    +"< OrderType = >"+response.getOrdType()+ "<"
        			    +"< StopPrice = >"+response.getStopPxAsString()+ "<"
        			    +"< LimitPrice = >"+response.getPriceAsString()+ "<"
        			    +"< OrderID = >"+response.getOrderID()
        			    +"< LastMkt = >"+response.getLastMkt()+ "<";
        //Order table
      ordData = new Vector();
      ordData.add("ORD NOT FOUND ->"+mExchange+"-EXCH"+response.getClOrderID()+"<-"+response.getSecurityType()+"-ORD");
      ordData.add(response.getCorrelationClOrdID());//getCorrelationClOrdID()	mSenderCompID+"ORD"+response.getClOrderID()
      ordData.add(mExchange+"-EXCH"+response.getClOrderID());
      ordData.add(ib.translateFromFixBuyOrSell(response.getSide()));
      ordData.add(ib.translateFromFixOrderType(response.getOrdType()));
      ordData.add(response.getOrderQtyAsString());
      ordData.add(response.getSecurityDesc());
      ordData.add(response.getPriceAsString());
      ordData.add(response.getStopPxAsString());
      ordData.add(translateFromFixOrderStatus(response.getOrdStatus()));
      ordData.add(response.getLastSharesAsString());
      ordData.add(response.getLastPxAsString());
      ordData.add(response.getCumQtyAsString());
      ordData.add(response.getLeavesQtyAsString());
      ordData.add(response.getLastMkt());
      ordData.add("SUBMITTED");
      if(mExchange.equals("ICE"))
      {
      	strAdd += " DealID = >"+ response.getExecID() + "< ";
		strAdd += "Text = >"+response.getText()+"< ";
		strAdd += sdRespHandler.getDescription(response.getSymbol());
		ordData.add(response.getExecID());
      	ordData.add(sdRespHandler.getDescription(response.getSymbol()));
      	if(response.getOrderState() != null)
      	{
      	  if(response.getOrderState().equals("1"))
      	  {
	      		strAdd += " OrderState is INACTIVE ";
	      		ordData.add("INACTIVE ");
      	  }
	      else
	      {
		      strAdd += " OrderState = >"+ response.getOrderState()+ "< ";
		      ordData.add(response.getOrderState());
	      }
      	}
	  }
      if(response.getMultiLegReportingType() != null)
      {
      	  strAdd += " MultiLeg ";
      	  ordData.add("MultiLeg");
      }
      mListModel.addElement(strAdd);
      orderModel.addRow(ordData);
    }
  }
  // ========================================================================
  // ========================================================================
  //to change the atatus in the Order messages tab
  private void changeRow(Vector data,FixExecutionReport response)
  {
	Vector ordTableData = orderModel.getDataVector();
    try
    {
	  for (int i=0; i<orderTable.getRowCount(); i++)
	  {
	    if (orderTable.getValueAt(i, 1).equals(response.getCorrelationClOrdID()))
	    {
	      Vector row = (Vector)ordTableData.elementAt(i);
	      for (int k=0; k<row.size(); k++) {
	        row.set(k, data.get(k));
	        orderModel.fireTableDataChanged();
	      }
	      orderModel.fireTableRowsUpdated(i,i);
	      orderModel.fireTableDataChanged();
	      break;
	    }
	  }
    }
    catch(ArrayIndexOutOfBoundsException e){}
  }
  private void changeStatus(FixExecutionReport response)
  {
	//  Logger.debug("in changestatus");
	Vector ordTableData = orderModel.getDataVector();
    try
    {
	  for (int i=0; i<orderTable.getRowCount(); i++)
	  {
		if(translateFromFixOrderStatus(response.getOrdStatus()) == "NEW")
		{
			//Logger.debug("in new");
			if (orderTable.getValueAt(i, 1).equals(response.getCorrelationClOrdID()))
		    {
		      Vector row = (Vector)ordTableData.elementAt(i);
		      row.set(9, response.getText());
		      orderModel.fireTableDataChanged();
		    }
		}
		//Logger.debug("getCorrelationClOrdID()	:"+response.getCorrelationClOrdID());
		//Logger.debug("getClOrderID()	:"+response.getClOrderID());
		if (orderTable.getValueAt(i, 1).equals(response.getClOrderID()))
	    {
	      Vector row = (Vector)ordTableData.elementAt(i);
	      row.set(9, translateFromFixOrderStatus(response.getOrdStatus()));
	      Logger.debug("status	:"+translateFromFixOrderStatus(response.getOrdStatus()));
	      orderModel.fireTableDataChanged();
	    }
	  }
    }
    catch(ArrayIndexOutOfBoundsException e){}
  }
  // ========================================================================
  // ========================================================================
  public void processCancelReject(FixCancelReject cxlRej)
  {
    Logger.debug("processCancelReject");
    boolean validRespType = true;
    boolean targetFound = false;
    String strAdd = " - Reason = >"+cxlRej.getText()+"<";

    Order targetOrder = findOrder(cxlRej.getClOrderID());
    if(targetOrder != null)
    {
      targetFound = true;
      targetOrder.OrderStatus(IBusinessObject.REJECTED);
      mListModel.addElement(targetOrder.toString()+strAdd);

      execData = new Vector();
      /*
      execData.add(cxlRej.getSecurityType()+"-ORD");
      execData.add(cxlRej.getClOrderID());
      execData.add(ib.translateFromFixBuyOrSell(cxlRej.getSide()));
      execData.add(cxlRej.getSecurityDesc());
	  execData.add(cxlRej.getOrderQty());
	  execData.add(cxlRej.getPrice());
	  */
	  execData.add(" - Reason = >"+cxlRej.getText()+"<");
	  execData.add(cxlRej.getOrdStatus());
	  execModel.addRow(execData);
    }
    else
    {
	  Cancel targetCancel = findCancel(cxlRej.getClOrderID());
 	  if(targetCancel != null)
  	  {
  	    targetFound = true;
   	  	targetCancel.OrderStatus(IBusinessObject.REJECTED);
    	mListModel.addElement(targetCancel.toString()+strAdd);

    	execData = new Vector();
    	execData.add("CXL: "+targetCancel.getCancelId());
		execData.add("CXLing	:"+targetCancel.getTBCOrderId());
	    execData.add("Status	:"+targetCancel.OrderStatus());
	    execData.add(" - Reason = >"+cxlRej.getText()+"<");
	    execModel.addRow(execData);
      }
    }
    if( !targetFound )
    {
      String strAdd1 = "CXL NOT FOUND  -"+cxlRej.getClOrderID()
        			    +" - Reason = >"+cxlRej.getText()+"<";
      mListModel.addElement(strAdd1);

      execData = new Vector();
      execData.add("CXL NOT FOUND	-"+cxlRej.getClOrderID()+" - Reason = >"+cxlRej.getText()+"<");
 	  execModel.addRow(execData);
    }
  }

  // ========================================================================
  // ========================================================================
  private Order findOrder(String targetOrderId)
  {
  	return findOrder(targetOrderId,8);
  }
  private Order findOrder(String targetOrderId, int numChars)
  {
    for(int x = 0; x < mOrders.size(); x++)
    {
      Order daOrder = (Order)mOrders.elementAt(x);

      String clOrderId= daOrder.getClientOrderId();
      String last8 = clOrderId.substring(clOrderId.length()-numChars);
      Logger.debug("ClOrderID="+clOrderId+" targetOrderID="+targetOrderId);
      if(clOrderId.equals(targetOrderId) || last8.equals(targetOrderId))
      {
        Logger.debug("FOUND!!");
        return daOrder;
      }
    }
    return null;
  }

  // ========================================================================
  // ========================================================================
  private Cancel findCancel(String targetCancelId)
  {
    for(int x = 0; x < mCancels.size(); x++)
    {
      Cancel daCancel = (Cancel)mCancels.elementAt(x);
      String clOrderId= daCancel.getCancelId();
      String last5 = clOrderId.substring(clOrderId.length()-5);

      Logger.debug("CancelID="+clOrderId+" targetCancelId="+targetCancelId);
      if(clOrderId.equals(targetCancelId) || last5.equals(targetCancelId))
      {
        Logger.debug("FOUND!!");
        return daCancel;
      }
    }
    return null;
  }
  // ========================================================================
  // ========================================================================
  public void processOrder(FixOrder fixorder)
  {
    Logger.debug("processOrder");
    try {
      mSessionMgr.sendOrder(fixorder, mRequestSubject+"ORD");
    } catch(Exception exp){ exp.printStackTrace(); }
    
    Order anOrder = new Order(fixorder, mRequestSubject+"ORD");
    //mListModel.addElement(anOrder.toString());
    mOrders.addElement(anOrder);

    execData = new Vector();
    execData.add(fixorder.getSecurityType()+"-ORD");
    execData.add(fixorder.getClOrderID());
    execData.add(ib.translateFromFixBuyOrSell(fixorder.getSide()));
    //Logger.debug("side process"+ib.translateFromFixBuyOrSell(fixorder.getSide()));
    execData.add(fixorder.getSecurityDesc());
    execData.add(fixorder.getOrderQtyAsString());
    execData.add(fixorder.getPriceAsString());
    execData.add("SUBMITTED ORDER");
    execData.add("PENDACCEPT");
    execModel.addRow(execData);
  }
  public void processRestoredOrder(FixOrder fixorder)
  {
    Order anOrder = new Order(fixorder, mRequestSubject+"ORD");
    mListModel.addElement(anOrder.toString());
    mOrders.addElement(anOrder);

    execData = new Vector();
    execData.add(fixorder.getSecurityType()+"-ORD");
    execData.add(fixorder.getClOrderID());
    execData.add(ib.translateFromFixBuyOrSell(fixorder.getSide()));
    execData.add(fixorder.getSecurityDesc());
    execData.add(fixorder.getOrderQtyAsString());
    execData.add(fixorder.getPriceAsString());
    execData.add("SUBMITTED ORDER");
    execData.add("PENDACCEPT");
    execModel.addRow(execData);
  }
  // ========================================================================
  // ========================================================================
  public void processCancel(Cancel cancel)
  {
    Logger.debug("processCancel");
    /*
    try {
      mSessionMgr.sendCancel(cancel, mRequestSubject);
    } catch(Exception exp){ exp.printStackTrace(); }
    mListModel.addElement(cancel.toString());
    mCancels.addElement(cancel);
    */
  }
  // ========================================================================
  // ========================================================================
  //public void processCancelReplace(CancelReplace cancelreplace)
  //{
  //  Logger.debug("processCancelReplace");
  //}
  // ========================================================================
  // ========================================================================
  public void processQuote(RealTimeQuote quote)
  {
    Logger.debug("processQuote");
  }
  // ========================================================================
  // ========================================================================
  public void valueChanged(ListSelectionEvent e)
  {
    int selected = mList.getSelectedIndex();
    if(selected >= 0)
    {
      String item = (String)mList.getModel().getElementAt(selected);
      StringTokenizer st = new StringTokenizer(item);
      String targetOrderId = "";
      for(int x = 0; st.hasMoreTokens() && x < 2; x++)
        targetOrderId = st.nextToken(); // order id is at position #2
      Order targetOrder = findOrder(targetOrderId);
      if(targetOrder != null &&
         /*targetOrder.OrderStatus() != IBusinessObject.FILLED &&*/
         targetOrder.OrderStatus() != IBusinessObject.REJECTED)
      {
        //mCXRWindow.displayOrder(targetOrder, mTheFrame);
        mTabbedPane.setSelectedIndex(1); // switch to cxl tab
      }
    }
    Logger.debug("valueChanged:: "+e.toString());
  }
  // ========================================================================
  // ========================================================================
  public void actionPerformed(ActionEvent e)
  {
    Logger.debug("actionPerformed:: "+e.toString());
  }
  // ========================================================================
  // ========================================================================
  private void createWindows() throws Exception
  {
    setLayout(new GridLayout(0,1));
    setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("< Trader Window - Trader:" +mSenderCompID+" >"),
        BorderFactory.createEmptyBorder(0,0,0,0)
      )
    );
    add(createTabbedPanes());
    add(createResponseLog());
  }
  // ========================================================================
  // ========================================================================
  private Component createTabbedPanes() throws Exception
  {
    mOrderWindow = new PerfOrderWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,this,mUniqueIDFile,mExchange,mFXFlag);
    mCXRWindow = new CxlCRWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,this, mUniqueIDFile,mExchange,mFXFlag);
    mTabbedPane = new JTabbedPane();
    mTabbedPane.addTab(tagORDER, mOrderWindow.createComponent());
    mTabbedPane.addTab(tagCXLCR, mCXRWindow.createComponent());
    if(mEnableNews)
    {
	    mNewsWindow = new NewsWindow(mTargetCompID, mSenderCompID, mSenderSubID, mTraderName,mNewsSubject,mUniqueIDFile,mExchange);
	    mTabbedPane.addTab(tagNEWS, mNewsWindow.createResponseLog());
    }
    //mTabbedPane.addTab(tagDETAIL, getContent(tagDETAIL));
    return mTabbedPane;
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
  private Component createResponseLog() throws Exception
  {
	respTabbedPane = new JTabbedPane();
    respTabbedPane.setPreferredSize(new Dimension(600,250));
    respTabbedPane.addTab(orderReport,OrderComponent());
    respTabbedPane.addTab(cxlCRReport,ExecComponent());
    respTabbedPane.addTab(execReport,ExecutionLog());

    return respTabbedPane;
  }

  private Component ExecComponent()
  {
	columnExecNames = new Vector();
	columnExecNames.addElement("Subject");
	columnExecNames.addElement("ClOrderID");
	columnExecNames.addElement("Side");
	columnExecNames.addElement("SecurityDesc");
	columnExecNames.addElement("OrderQty");
	columnExecNames.addElement("Price");
	columnExecNames.addElement("Text");
	columnExecNames.addElement("Status");

	execModel = new DefaultTableModel(){
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	execModel.setColumnIdentifiers(columnExecNames);
	execTable = new JTable(execModel);
	//execTable.setRowSelectionAllowed(true);
	execTable.setDefaultRenderer(Object.class,new DefaultTableCellRenderer() {
		/* (non-Javadoc)
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
		//@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			try
 			{
			if(isSelected){
 				cell.setBackground(Color.white);
 			}
 			else if (table.getValueAt(row, 7).equals("NEW")) {
		        cell.setBackground(Color.cyan);
		    }
		    else if (table.getValueAt(row, 7).equals("PARTIAL FILLED")) {
		        cell.setBackground(Color.orange);
		    }
		    else if (table.getValueAt(row, 7).equals("FILLED")) {
		        cell.setBackground(Color.pink);
		    }
		    else if (table.getValueAt(row, 7).equals("DONE FOR DAY")) {
		        cell.setBackground(Color.blue);
		    }
		    else if (table.getValueAt(row, 7).equals("CANCELLED")) {
		        cell.setBackground(Color.red);
		    }
		    else if (table.getValueAt(row, 7).equals("REPLACED")) {
		        cell.setBackground(Color.getHSBColor(181, 230, 170));
		    }
		    else if (table.getValueAt(row, 7).equals("PENDING CANCEL")) {
		        cell.setBackground(Color.magenta);
		    }
		    else if (table.getValueAt(row, 7).equals("STOPPED")) {
		        cell.setBackground(Color.black);
		    }
		    else if (table.getValueAt(row, 7).equals("REJECTED")) {
		        cell.setBackground(Color.getHSBColor(359, 70, 70));
		    }
		    else if (table.getValueAt(row, 7).equals("SUSPENDED")) {
		        cell.setBackground(Color.gray);
		    }
		    else if (table.getValueAt(row, 7).equals("PENDING NEW")) {
		        cell.setBackground(Color.green);
		    }
		    else if (table.getValueAt(row, 7).equals("CALCULATED")) {
		        cell.setBackground(Color.yellow);
		    }
		    else if (table.getValueAt(row, 7).equals("EXPIRED")) {
		        cell.setBackground(Color.darkGray);
		    }
		    else if (table.getValueAt(row, 7).equals("ACCEPTED")) {
		        cell.setBackground(Color.getHSBColor(0.866F, 0.5305F, 0.8305F));
		    }
		    else if (table.getValueAt(row, 7).equals("PENDING REPLACE")) {
		        cell.setBackground(Color.getHSBColor(255, 190, 170));
		    }
		    else if (table.getValueAt(row, 7).equals("PENDACCEPT")) {
		        cell.setBackground(Color.green);
		    }

		    else if (table.getValueAt(row, 7).equals(null)) {
		    	cell.setBackground(Color.getHSBColor(0.7167F, 0.0F, 0.0F));
		      }
 			}
		    catch(NullPointerException e)
		    {
		    	cell.setBackground(Color.getHSBColor(0.9694F, 0.716F, 0.753F));
		    }

		return cell;
		}
	});
	TableColumn column1 = execTable.getColumnModel().getColumn(0);
    column1.setPreferredWidth(130);
    TableColumn column2 = execTable.getColumnModel().getColumn(1);
    column2.setPreferredWidth(130);
    TableColumn column3 = execTable.getColumnModel().getColumn(2);
    column3.setPreferredWidth(100);
    TableColumn column4 = execTable.getColumnModel().getColumn(3);
    column4.setPreferredWidth(130);
    TableColumn column5 = execTable.getColumnModel().getColumn(4);
    column5.setPreferredWidth(55);
    TableColumn column6 = execTable.getColumnModel().getColumn(5);
    column6.setPreferredWidth(55);
    TableColumn column7 = execTable.getColumnModel().getColumn(6);
    column7.setPreferredWidth(100);

    execTable.setPreferredScrollableViewportSize(new Dimension(380, 200));
    execTable.getTableHeader().setReorderingAllowed(false);
    execTable.getTableHeader().setBackground(Color.yellow);

	JScrollPane areaScrollPane = new JScrollPane(execTable);
	areaScrollPane.setPreferredSize(new Dimension(380,200));
	areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

	return areaScrollPane;
  }

  public Component OrderComponent()
  {
	columnOrderNames = new Vector();
	columnOrderNames.addElement("Subject");
	columnOrderNames.addElement("ClOrderID");
	columnOrderNames.addElement("OrderID");
	columnOrderNames.addElement("Side");
	columnOrderNames.addElement("OrderType");
	columnOrderNames.addElement("OrderQty");
	columnOrderNames.addElement("SecurityDesc");
	columnOrderNames.addElement("Limitprice");
	columnOrderNames.addElement("StopPrice");
	columnOrderNames.addElement("Status");
    columnOrderNames.addElement("LastShares");
    columnOrderNames.addElement("Lastprice");
    columnOrderNames.addElement("CumQty");
    columnOrderNames.addElement("LeavesQty");
    columnOrderNames.addElement("LastMkt");
    columnOrderNames.addElement("Text");
	columnOrderNames.addElement("DealID");
    columnOrderNames.addElement("sdRespHandler");
    columnOrderNames.addElement("Order State");
    columnOrderNames.addElement("MultiLeg");
    columnOrderNames.addElement("BaseCurr");
    columnOrderNames.addElement("ValueDate");
    columnOrderNames.addElement("TradeDate");
    columnOrderNames.addElement("SettlCurr");
    columnOrderNames.addElement("GrossTradeAmt");
    columnOrderNames.addElement("CalcCcyLstQty");
    columnOrderNames.addElement("AggreInd");
    columnOrderNames.addElement("SpotRate");
    columnOrderNames.addElement("ExpDate");
    columnOrderNames.addElement("SecDesc");

    orderModel = new DefaultTableModel(){
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
  	orderModel.setColumnIdentifiers(columnOrderNames);
    orderTable = new JTable(orderModel);

    orderTable.getTableHeader().setReorderingAllowed(false);
    orderTable.getTableHeader().setBackground(Color.yellow);

    orderTable.setDefaultRenderer(Object.class,new DefaultTableCellRenderer() {
    	//@Override
    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
    						boolean hasFocus, int row, int column) {

    		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			try
			{
	    		if (table.getValueAt(row, 3).equals("B")) {
			        cell.setForeground(Color.blue);
			    }
	    		else if (table.getValueAt(row, 3).equals("S")) {
			        cell.setForeground(Color.red);
			    }
			}
			catch (Exception e) {}

    		return cell;
    	}
    });
    //working on mouse double click event
    orderTable.addMouseListener(new MouseAdapter() {
    	public void mouseClicked(MouseEvent e) {
    		if (e.getClickCount() == 2) {
    			JTable target = (JTable)e.getSource();
    			int row = target.getSelectedRow();
    			int column = target.getSelectedColumn();         // do some action
    			Logger.debug("in mouse double click event");
    		}}});

    JScrollPane areaScrollPane = new JScrollPane(orderTable);
	orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    return areaScrollPane;
  }

  public Component ExecutionLog()
  {
	mListModel = new DefaultListModel();
	mList = new JList(mListModel);
	mList.addListSelectionListener(this);

	JScrollPane areaScrollPane = new JScrollPane(mList);
	areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	return areaScrollPane;
  }

  // ========================================================================
  // ========================================================================
  private void initializeSession()
  {
    try
    {
      mSessionMgr = new TradingSessionManager();
      mSessionMgr.startTradingSession();
      //mSessionMgr.receiveTradingMessages(this,mResponseSubject);

      if(mSecResponseSubject != null)
      {
      	sdRespHandler = new SDResponseHandler();
      	mSecSessionMgr = new SecuritiesSessionManager();
      	mSecSessionMgr.startSecuritiesSession();
      	mSecSessionMgr.receiveSecuritiesMessages(this,mSecResponseSubject);
      }
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }
  // ========================================================================
  // ========================================================================
  private void processConfig()
  {
    try
    {
      mResponseSubject = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_RESP_SUB_SUB);
      Logger.debug("Resp Publish Subject is " + mResponseSubject );
      mRequestSubject = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_REQ_PUB_SUB);
      Logger.debug("Req Publish Subject is " + mRequestSubject );
      mSenderCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_SENDER_COMPANY_ID);
      Logger.debug("Sender Comp ID is " + mSenderCompID );
      mTargetCompID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_TARGET_COMPANY_ID);
      Logger.debug("Target Comp ID is " + mTargetCompID );
      mUniqueIDFile = ConfigurationService.getValue("UniqueIDFile");
      Logger.debug("UniqueIDFile is " + mUniqueIDFile );
      mOFMOverrideFlag = ConfigurationService.getValue(OrderEntryConstants.TRADEWINDOW_OFM_OVERRIDE_FLAG);
      Logger.debug("OFM Override Flag is " + mOFMOverrideFlag );
      mExchange = ConfigurationService.getValue("Exchange");
      Logger.debug("Exchange is " + mExchange );
      if(mExchange.equals("ICE"))
      {
   		mSenderSubID = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_SENDER_SUB_ID);
   		Logger.debug("Sender Sub ID is " + mSenderSubID );
   		mUserName = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_USER_NAME);
   		Logger.debug("UserName is " + mUserName );
   		mTraderName = ConfigurationService.getValue(OrderEntryConstants.JMSORDERENTRY_TRADER_NAME);
   		Logger.debug("TraderName is " + mTraderName );
   		mSecResponseSubject = ConfigurationService.getValue("SDResponseSubject");
   		Logger.debug("Securities Resp Publish Subject is " + mSecResponseSubject );
   	  }
	  String fxFlag = ConfigurationService.getValue("FX","N");
	  if(fxFlag.equals("Y"))
	  {
	  	mFXFlag = true;
    	Logger.debug("FXFlag is " + fxFlag );
	  }
 	// news related
	  String newsFlag = ConfigurationService.getValue("EnableNews","N");
	  if(newsFlag.equals("Y"))
	  {
	  	mEnableNews = true;
    	Logger.debug("EnableNews is " + newsFlag );

	    mNewsSubject = ConfigurationService.getValue("NewsSubject");
        Logger.debug("News Publish Subject is " + mNewsSubject );
	  }
    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

  private void restoreFromFix()
  {
    try
    {
      String restore = ConfigurationService.getValue("restore");
      if(restore.equals("Y"))
      {
 	     String outFile = ConfigurationService.getValue("FixOutLogFile");
 	     ArrayList outList = FixSessionManager.getApplicationMessagesList(outFile);
 	     String inFile = ConfigurationService.getValue("FixInLogFile");
 	     ArrayList inList = FixSessionManager.getApplicationMessagesList(inFile);
 	     // first process out list
 	     for(int i=0; i<outList.size();i++)
	     {
    		FixMessage msg = (FixMessage)outList.get(i);
	    	String msgType = msg.getMessageType();
	    	handleRestoredMessage(msgType,msg);
	     }
 	     // then process in list
 	     for(int i=0; i<inList.size();i++)
	     {
    		FixMessage msg = (FixMessage)inList.get(i);
	    	String msgType = msg.getMessageType();
	    	handleRestoredMessage(msgType,msg);
	    }
	  }
    }
    catch( ConfigAttributeNotFound exc )
    {
      Logger.debug("CONFIG VALUES DOES NOT EXIST");
      exc.printStackTrace();
      System.exit(-1);
    }
  }

  private void handleRestoredMessage(String msgType,FixMessage msg)
  {
	if( msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
	{
		processRestoredOrder( (FixOrder)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL) )
	{
		processRestoredCancel( (FixCancel)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE) )
	{
		processRestoredCancelReplace( (FixCancelReplace)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT) )
	{
		processExecutionReport( (FixExecutionReport)msg );
	}
	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT) )
	{
		processCancelReject( (FixCancelReject)msg );
	}
  }

  // ========================================================================
  // ========================================================================
  public static void main(String args[])
  {
    try
    {
      /*
      JFrame frame = new JFrame("Exchange API Trading Suite");
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
      frame.setContentPane(new PerfTraderWindow(args));
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
      */
      new PerfTraderWindow(args);
    }
    catch(Exception exc)
    {
      exc.printStackTrace();
      System.exit(-1);
    }
  }
  // ========================================================================
  // ========================================================================
  private Vector columnOrderNames;
  private Vector columnExecNames;
  private Vector ordData;
  private Vector execData;
  private DefaultTableModel orderModel;
  private DefaultTableModel execModel;
  private JTable orderTable;
  private JTable execTable;

  private String mResponseSubject = null;
  private String mSecResponseSubject = null;
  private String mRequestSubject = null;
  private String mSenderCompID = null;
  private String mSenderSubID = null;
  private String mUserName = null;
  private String mTraderName = null;
  private String mTargetCompID = null;
  private String mUniqueIDFile = null;
  private String mOFMOverrideFlag = null;
  private String mExchange = null;
  private TradingSessionManager mSessionMgr = null;
  private SecuritiesSessionManager mSecSessionMgr = null;
  private SDResponseHandler sdRespHandler = null;
  private IBusinessObject ib = null;
  private JList mList = null;
  private DefaultListModel mListModel = null;
  private PerfOrderWindow mOrderWindow = null;
  private CxlCRWindow mCXRWindow = null;
  private Vector mOrders = new Vector();
  private Vector mCancels = new Vector();
  private JTabbedPane mTabbedPane = null;
  private JTabbedPane respTabbedPane = null;
  private JFrame mTheFrame = null;
  private boolean mFXFlag = false;
    //news
  private boolean mEnableNews = false;
  private String mNewsSubject = null;
  private NewsWindow mNewsWindow = null;

  private final static String tagORDER = "Order";
  private final static String tagCXLCR = "Cancel/Replace";
  private final static String tagDETAIL = "Detail";
  private final static String tagNEWS = "News";
  private final static String execReport = "Execution Responses Log";
  private final static String orderReport = "Orders/Cancel/CxlCR";
  private final static String cxlCRReport = "Execution Messages";


  // ========================================================================
  // ========================================================================
  public void processCancel(FixCancel fixcxl)
  {
    Logger.debug("processCancel fix");
    try {
      mSessionMgr.sendCancel(fixcxl, mRequestSubject+"CXL");
    } catch(Exception exp){ exp.printStackTrace(); }
    Cancel aCancel = new Cancel( fixcxl, mRequestSubject+"CXL");
    mListModel.addElement(aCancel.toString());
    mCancels.addElement(aCancel);

    execData = new Vector();
    execData.add("CXL: " + aCancel.getCancelId());
    execData.add(" CXLing "+ aCancel.getTBCOrderId());
    execData.add("status" + aCancel.OrderStatus());
	execModel.addRow(execData);
  }

  public void processStatusRequest(FixOrderStatusRequest stat)
  {
    Logger.debug("processStatusRequest");
    try {
      mSessionMgr.sendStatusRequest(stat, mRequestSubject+"STAT");
    } catch(Exception exp){ exp.printStackTrace(); }
    //Cancel aCancel = new Cancel( fixcxl, mRequestSubject+"CXL");
    mListModel.addElement("STATUS - " + stat.getClOrderID());
    //mCancels.addElement(aCancel);

    execData = new Vector();
    execData.add("STATUS - " + stat.getClOrderID());
	execModel.addRow(execData);
  }

  // ========================================================================
  // ========================================================================
  public void processCancelReplace(FixCancelReplace fixcxr)
  {
    Logger.debug("processCancelReplace");
    try {
    	//fixcxr.setIFMOverride(mOFMOverrideFlag);
      mSessionMgr.sendCancelReplace(fixcxr, mRequestSubject+"CXR");
    } catch(Exception exp){ exp.printStackTrace(); }
    // do we need to add both order and cancel ???

    CancelReplace cxr = new CancelReplace(fixcxr,mRequestSubject+"CXR");
    Order anOrder = cxr.getNewOrder();
    mListModel.addElement(anOrder.toString());
    Logger.debug("fix cancel replace" + anOrder.toString());
    mOrders.addElement(anOrder);

    //Execution table
    execData = new Vector();
    execData.add(fixcxr.getSecurityType()+"-ORD");
    execData.add(fixcxr.getClOrderID());
    execData.add(ib.translateFromFixBuyOrSell(fixcxr.getSide()));
    execData.add(fixcxr.getSecurityDesc());
    execData.add(fixcxr.getOrderQtyAsString());
    execData.add(fixcxr.getPriceAsString());
    execData.add("SUBMITTED ORDER");
    execData.add("PENDACCEPT");
    execModel.addRow(execData);

  }
  public void processRestoredCancel(FixCancel fixcxl)
  {
    Cancel aCancel = new Cancel( fixcxl, mRequestSubject+"CXL");
    mListModel.addElement(aCancel.toString());
    mCancels.addElement(aCancel);

    execData = new Vector();
    execData.add("CXL: " + aCancel.getCancelId());
    execData.add(" CXLing "+ aCancel.getTBCOrderId());
    execData.add("status" + aCancel.OrderStatus());
	execModel.addRow(execData);
  }

  // ========================================================================
  // ========================================================================
  public void processRestoredCancelReplace(FixCancelReplace fixcxr)
  {
    CancelReplace cxr = new CancelReplace(fixcxr,mRequestSubject+"CXR");
    Order anOrder = cxr.getNewOrder();
    mListModel.addElement(anOrder.toString());
    mOrders.addElement(anOrder);

    //Execution table
    execData = new Vector();
    execData.add(fixcxr.getSecurityType()+"-ORD");
    execData.add(fixcxr.getClOrderID());
    execData.add(ib.translateFromFixBuyOrSell(fixcxr.getSide()));
    execData.add(fixcxr.getSecurityDesc());
    execData.add(fixcxr.getOrderQtyAsString());
    execData.add(fixcxr.getPriceAsString());
    execData.add("SUBMITTED ORDER");
    execData.add("PENDACCEPT");
    execModel.addRow(execData);
  }

  public void processSecurityDefinitionRequest(FixSecurityDefinitionRequest msg)
  {
  }
  public void processSecurityDefinitionResponse(FixSecurityDefinitionResponse response)
  {
    Logger.debug("processSecurityDefinitionResponse");
    if(response.getNoRelatedSym() > 0 )
    {
   	  // add details
   	  String key = "146";
  	  ArrayList rgArray = response.getRepeatingGroups(key);
  	  if( rgArray != null )
  	  {
  		int size = rgArray.size();
  		//mListModel.addElement("Size is " + size );
  		for (int i = 0; i < size; i++) {

  		  FixRGNoRelatedSym rg = (FixRGNoRelatedSym)rgArray.get(i);
  		  sdRespHandler.processSDResponse(rg.getUnderlyingSymbol(),rg);
   		}
  	  }
    }
  }
}

