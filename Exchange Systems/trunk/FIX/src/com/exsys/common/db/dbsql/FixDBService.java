package com.exsys.common.db.dbsql;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.math.*;

import com.exsys.fix.message.*;
import com.exsys.common.db.*;

public class FixDBService
{
  private Connection dbConnection = null;
  private DatabaseManager dbManager = null;
  private boolean isSQLServer = false;

  public void setSQLServer(boolean sqlServerFlag)
  {
  	isSQLServer = sqlServerFlag;
  }
  public void setDatabaseManager( DatabaseManager dbMgr, boolean usePW )
  {
    dbManager = dbMgr;
    try
    {
      dbConnection = dbManager.GetConnection(usePW);
      //dbConnection.setAutoCommit( false );
    }
    catch( Exception e )
    {
      e.printStackTrace();

    }
}

public int addFixMessage(FixMessage fixMsg, String userId, String typeCode)
    throws Exception
{
    System.out.println("addFixMessage");
    java.math.BigDecimal idColVal = new BigDecimal("0");
    ResultSet rs;
    
    if( dbConnection == null )
      throw new Exception("No Connection To Database");

    synchronized ( dbConnection )
    {
      String query = constructMessageDetailsQuery(fixMsg,userId, typeCode); 
      System.out.println("HDR Query = ");
      System.out.println(query);   	

      try
      {
        Statement stmt = dbConnection.createStatement();
        stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
        rs = stmt.getGeneratedKeys();
        if(rs.next())
        {
        	idColVal = rs.getBigDecimal(1);
        }
        else
        {
        	throw  new Exception("IDENTITY KEY NOT RETURNED");
        }
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    return idColVal.intValue();
}

private String updateOrderCancelQuery(String id, 
						String orderId,
						String status, 
						String userId,
						int numChars,
						boolean isOrder)
{						


      String query = "UPDATE ";
      if(isOrder)
	      query += "FIXORDER SET ";
	  else    
	      query += "FIXCANCEL SET ";	      
	      
      if(status != null )
      {
      	if(isOrder)
      	{
      		query += " ORDERSTATUS1 = ";
      		query += "'"+status+"' ,";
      	}
      	else
      	{
      		query += " CANCELSTATUS = ";
      		query += "'"+status+"' ,";
      		
      	}
      }
      if(orderId != null)
      {
      	query += " ORDERID = ";
      	query += "'"+orderId+"' ,";
      }
      query += " LASTMODIFIEDBY = ";
      if(!isSQLServer)
      {
      	query += "'"+userId+"' ,";
      	query += " LASTMODIFIEDDATE = CURRENT TIMESTAMP ";      
      }
      else
      {
      	query += "'"+userId+"'";
      }      	
      
      query += " WHERE ";
      if(!isSQLServer)
      	query += " (SUBSTR(CLORDERID,LENGTH(CLORDERID)-"+numChars+") = ";
      else
        query += " (SUBSTRING(CLORDERID,LEN(CLORDERID)-"+numChars+",LEN(CLORDERID)) = ";
      query +=    "'"+id+"'";
      query +=    "OR CLORDERID = ";
      query +=    "'"+id+"')";
      
      return query;

}

private int updateOrder(Connection con, String id, 
						String orderId,
						String status, 
						String userId,
						int numChars,
						boolean checkCancel)
    throws Exception
{
    System.out.println("updateOrder - "+ id);
    int numUpdate = 0;
    
	  String query = updateOrderCancelQuery(id,orderId,status,userId,numChars,true);      
      System.out.println("UPDATE Query = ");
      System.out.println(query);   	

      try
      {
        Statement stmt = con.createStatement();
        numUpdate = stmt.executeUpdate(query);
        if(numUpdate == 0 && checkCancel)
        {
        	query = updateOrderCancelQuery(id,orderId,status,userId,5,false);
        	stmt = con.createStatement();
	        numUpdate = stmt.executeUpdate(query);
        }
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    
    
    return numUpdate;
}

private int updateCancel(Connection con, String id, 
						String orderId,
						String status, 
						String userId,
						int numChars)
    throws Exception
{
    System.out.println("updateCancel - "+ id);
    int numUpdate = 0;
    
	  String query = updateOrderCancelQuery(id,orderId,status,userId,numChars,false);      
      System.out.println("UPDATE Query = ");
      System.out.println(query);   	

      try
      {
        Statement stmt = con.createStatement();
        numUpdate = stmt.executeUpdate(query);
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    
    
    return numUpdate;
}


public int addFixExecutionReport(FixExecutionReport response, 
  						String userId,
  						String status)
    throws Exception
{
    System.out.println("addFixExecutionReport");
    //ResultSet rs;
    int messageId = 0;
    if( dbConnection == null )
      throw new Exception("No Connection To Database");


    synchronized ( dbConnection )
    {
      // first add into MESSAGEDETAILS table and get messageid
      messageId = addFixMessage(response,userId,"EXR");	
      System.out.println("Message ID = " + messageId);
      
      // update the order with orderid
      
      boolean validRespType = true;
      boolean targetFound = false;
      if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_FILL))
      {
        System.out.println("FILL--");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						null,
        						"FILL",
        						userId,8,false);
        
        if(num != 0)
        {
        	targetFound = true;
          //System.out.println("Order Status - "+targetOrder);
          String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
          			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
          			    +"< LeavesQty = >"+response.getLeavesQtyAsString()
          			    +"< LastMkt = >"+response.getLastMkt()+ "<";
          //targetOrder.setOpenQuantity(new Integer(0));
          //targetOrder.OrderStatus(IBusinessObject.FILLED);
          //mListModel.addElement(targetOrder.toString() + strAdd);
        }
        
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PARTIAL))
      {
        System.out.println("PARTIAL"+response.getLeavesQtyAsString());
        
         int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						null,
        						response.getLeavesQty() > 0 ?"PFIL":"FILL",
        						userId,8,false);
        
        if(num != 0)
        {
        	targetFound = true;
          //System.out.println("Order ["+targetOrder+"]");
          //targetOrder.setOpenQuantity(Integer.valueOf(response.getLeavesQtyAsString()));
          //if(response.getLeavesQty() > 0)
          //  targetOrder.OrderStatus(IBusinessObject.PARTIALFILLED);
         // else
          //  targetOrder.OrderStatus(IBusinessObject.FILLED);
          String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
          			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
          			    +"< LeavesQty = >"+response.getLeavesQtyAsString()
          			    +"< LastMkt = >"+response.getLastMkt()+ "<";

          //mListModel.addElement(targetOrder.toString() + strAdd);
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_CANCELED))
      {
        System.out.println("CANCELED");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						null,
        						"CLOS",
        						userId,8,true);
        
        if(num != 0)
        {
        	targetFound = true;
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_TRADE_CANCELED))
      {
      	//!!! 04292007 - CME ONLY SENT last 5 digits of the clorderid NOT last 8
        System.out.println("TRADE CANCELED");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						null,
        						"CLOS",
        						userId,5,false);
        
        if(num != 0)
        {
        	targetFound = true;
        }
       }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_EXPIRED))
      {
        System.out.println("EXPIRED");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						null,
        						"EXPR",
        						userId,8,false);
        
        if(num != 0)
        {
        	targetFound = true;
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_DONE_FOR_DAY))
      {
        System.out.println("DONE FOR DAY");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						null,
        						"DNFD",
        						userId,8,false);
        
        if(num != 0)
        {
        	targetFound = true;
          //targetOrder.OrderStatus(IBusinessObject.CLOSED);
          String strAdd = " - LastShares = >"+response.getLastSharesAsString()+"< LastPrice = >"+response.getLastPx()
          			    +"< CumQty = >"+response.getCumQtyAsString()+"< OrderQty = >"+response.getOrderQtyAsString()
          			    +"< LeavesQty = >"+response.getLeavesQtyAsString()
          			    +"< LastMkt = >"+response.getLastMkt()+ "<";

          //mListModel.addElement(targetOrder.toString() + strAdd + " - DONE_FOR_DAY");
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_NEW))
      {
        System.out.println("NEW");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						response.getOrderID(),
        						"ACPT",
        						userId,8,false);
        
        if(num != 0)
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


          //targetOrder.OrderStatus(IBusinessObject.ACCECPTED);
          //targetOrder.setOrderId(response.getOrderID());
          //mListModel.addElement(targetOrder.toString() + strAdd);
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PEND_REPLACE))
      {
        System.out.println("PEND REPLACE");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						response.getOrderID(),
        						"PRPL",
        						userId,8,false);
        
        if(num != 0)
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


          //targetOrder.OrderStatus(IBusinessObject.PENDRPL);
          //targetOrder.setOrderId(response.getOrderID());
          //mListModel.addElement(targetOrder.toString() + strAdd);
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REPLACED))
      {
        System.out.println("REPLACED");
        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						response.getOrderID(),
        						"RPLD",
        						userId,8,false);
        
        if(num != 0)
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


          //targetOrder.OrderStatus(IBusinessObject.REPLACED);
          //targetOrder.setOrderId(response.getOrderID());
          //mListModel.addElement(targetOrder.toString() + strAdd);
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REJECTED))
      {
        System.out.println("REJECTED");
        String strAdd = " - Reason = >"+response.getText()+"<";

        int num = updateOrder(dbConnection,
        						response.getClOrderID(),
        						null,
        						"REJD",
        						userId,8,true);
        
        if(num != 0)
        {        	targetFound = true;
          //targetOrder.OrderStatus(IBusinessObject.REJECTED);
          //mListModel.addElement(targetOrder.toString()+strAdd);
        }
      }
      else if(response.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_PEND_CANCEL))
      {
        System.out.println("PENDCANCEL");
        String strAdd = " - PENDCANCEL ";

        int num = updateCancel(dbConnection,
        						response.getClOrderID(),
        						null,
        						"PCXL",
        						userId,5);
        
        if(num != 0)
        {        	
        	targetFound = true;
    	   
     	  	//targetCancel.OrderStatus(IBusinessObject.PENDCXL);
      	    //mListModel.addElement(targetCancel.toString()+strAdd);
      	}

      }
      else
      {
        validRespType = false;
        System.err.println("Unknown Response Type");
        //mListModel.addElement("Unknown Response Type - " + response.getExecType());
        return 0;
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

      	//mListModel.addElement(strAdd);
      	// status remains PEND
      }
      else
      {
      	status = "COMP";
      }
      
      
      
      
      String query = constructShortExecutionReportQuery(response,messageId, 
      				userId,status);    	
      System.out.println("ExecutionReport Query = ");
      System.out.println(query);   	


      try
      {
        Statement stmt = dbConnection.createStatement();
        stmt.executeUpdate(query);
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    return messageId;
}

private String constructShortExecutionReportQuery(FixExecutionReport fixMsg, int messageId, 
  						String userId,String status)
{
    String query = null;
    
 query = "INSERT INTO FIXEXECUTIONREPORT(";
	query += "DBEXECUTIONREPORTID,";
	query += "ACCOUNT,";
	query += "AVGPX,";
	query += "CLORDERID,";
	query += "CUMQTY,";
	query += "CURRENCY,";
	query += "EXECID,";
	query += "EXECTRANSTYPE,";
	query += "LASTMKT,";
	query += "LASTPX,";
	query += "LASTSHARES,";
	query += "ORDERID,";
	query += "ORDERQTY,";
	query += "ORDSTATUS,";
	query += "ORDTYPE,";
	query += "ORIGCLORDERID,";
	query += "PRICE,";
	query += "SIDE,";
	query += "SYMBOL,";
	query += "TIMEINFORCE,";
	query += "TRANSACTTIME,";
	query += "STOPPX,";
	query += "ORDREJREASON,";
	query += "SECURITYDESC,";
	query += "EXPIRETIME,";
	query += "EXECTYPE,";
	query += "LEAVESQTY,";
	query += "SECURITYTYPE,";
	query += "MATURITYMONTHYEAR,";
	query += "PUTORCALL,";
	query += "STRIKEPRICE,";
	query += "MATURIYDAY,";
	query += "EXPIREDATE,";
	query += "CLEARINGFIRM,";
	query += "CLEARINGACCOUNT,";
	query += "CORRELATIONCLORDID,";
	query += "REPORTSTATUS,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getAccount() == null?"NULL":"'"+fixMsg.getAccount()+"'")+",";
	query += (fixMsg.getAvgPxAsString() == null?"NULL":fixMsg.getAvgPxAsString())+",";
	query += (fixMsg.getClOrderID() == null?"NULL":"'"+fixMsg.getClOrderID()+"'")+",";
	query += (fixMsg.getCumQtyAsString() == null?"NULL":fixMsg.getCumQtyAsString())+",";
	query += (fixMsg.getCurrency() == null?"NULL":"'"+fixMsg.getCurrency()+"'")+",";
	query += (fixMsg.getExecID() == null?"NULL":"'"+fixMsg.getExecID()+"'")+",";
	query += (fixMsg.getExecTransType() == null?"NULL":"'"+fixMsg.getExecTransType()+"'")+",";
	query += (fixMsg.getLastMkt() == null?"NULL":"'"+fixMsg.getLastMkt()+"'")+",";
	query += (fixMsg.getLastPxAsString() == null?"NULL":fixMsg.getLastPxAsString())+",";
	query += (fixMsg.getLastSharesAsString() == null?"NULL":fixMsg.getLastSharesAsString())+",";
	query += (fixMsg.getOrderID() == null?"NULL":"'"+fixMsg.getOrderID()+"'")+",";
	query += (fixMsg.getOrderQtyAsString() == null?"NULL":fixMsg.getOrderQtyAsString())+",";
	query += (fixMsg.getOrdStatus() == null?"NULL":"'"+fixMsg.getOrdStatus()+"'")+",";
	query += (fixMsg.getOrdType() == null?"NULL":"'"+fixMsg.getOrdType()+"'")+",";
	query += (fixMsg.getOrigClOrderID() == null?"NULL":"'"+fixMsg.getOrigClOrderID()+"'")+",";
	query += (fixMsg.getPriceAsString() == null?"NULL":fixMsg.getPriceAsString())+",";
	query += (fixMsg.getSide() == null?"NULL":"'"+fixMsg.getSide()+"'")+",";
	query += (fixMsg.getSymbol() == null?"NULL":"'"+fixMsg.getSymbol()+"'")+",";
	query += (fixMsg.getTimeInForce() == null?"NULL":"'"+fixMsg.getTimeInForce()+"'")+",";
	query += (fixMsg.getTransactTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getTransactTime())+"'")+",";
	query += (fixMsg.getStopPxAsString() == null?"NULL":fixMsg.getStopPxAsString())+",";
	query += (fixMsg.getOrdRejReasonAsString() == null?"NULL":fixMsg.getOrdRejReasonAsString())+",";
	query += (fixMsg.getSecurityDesc() == null?"NULL":"'"+fixMsg.getSecurityDesc()+"'")+",";
	query += (fixMsg.getExpireTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getExpireTime())+"'")+",";
	query += (fixMsg.getExecType() == null?"NULL":"'"+fixMsg.getExecType()+"'")+",";
	query += (fixMsg.getLeavesQtyAsString() == null?"NULL":fixMsg.getLeavesQtyAsString())+",";
	query += (fixMsg.getSecurityType() == null?"NULL":"'"+fixMsg.getSecurityType()+"'")+",";
	query += (fixMsg.getMaturityMonthYearAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getMaturityMonthYear())+"'")+",";
	query += (fixMsg.getPutOrCallAsString() == null?"NULL":fixMsg.getPutOrCallAsString())+",";
	query += (fixMsg.getStrikePriceAsString() == null?"NULL":fixMsg.getStrikePriceAsString())+",";
	query += (fixMsg.getMaturiyDayAsString() == null?"NULL":fixMsg.getMaturiyDayAsString())+",";
	query += (fixMsg.getExpireDateAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getExpireDate())+"'")+",";
	query += (fixMsg.getClearingFirm() == null?"NULL":"'"+fixMsg.getClearingFirm()+"'")+",";
	query += (fixMsg.getClearingAccount() == null?"NULL":"'"+fixMsg.getClearingAccount()+"'")+",";
	query += (fixMsg.getCorrelationClOrdID() == null?"NULL":"'"+fixMsg.getCorrelationClOrdID()+"'")+",";
	query += "'"+status+"'"+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}

    return query;
  }


public int addFixCancel(FixCancel fixMsg, 
  						String userId,
  						String status,
  						boolean isCXR)
    throws Exception
{
    System.out.println("addFixCancel");
    //ResultSet rs;
    int messageId = 0;
    if( dbConnection == null )
      throw new Exception("No Connection To Database");


    synchronized ( dbConnection )
    {
      // first add into MESSAGEDETAILS table and get messageid
      messageId = addFixMessage(fixMsg,userId,"CXL");	
      System.out.println("Message ID = " + messageId);
      
      String query = constructShortCancelQuery(fixMsg,messageId, userId,status, isCXR);    	
      System.out.println("ORDER Query = ");
      System.out.println(query);   	


      try
      {
        Statement stmt = dbConnection.createStatement();
        stmt.executeUpdate(query);
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    return messageId;
}

private String constructShortCancelQuery(FixCancel fixMsg, int messageId, 
  						String userId,String status,
  						boolean isCXR)
{
    String query = null;
  query = "INSERT INTO FIXCANCEL(";
	query += "DBCANCELID,";
	query += "ACCOUNT,";
	query += "CLORDERID,";
	query += "ORDERID,";
	query += "ORDERQTY,";
	query += "ORIGCLORDERID,";
	query += "SIDE,";
	query += "SYMBOL,";
	query += "TRANSACTTIME,";
	query += "SECURITYDESC,";
	query += "SECURITYTYPE,";
	query += "MATURITYYEAR,";
	query += "PUTORCALL,";
	query += "STRIKEPRICE,";
	query += "MATURITYDAY,";
	query += "CORRELATIONCLORDID,";
	query += "CANCELSTATUS,";
	query += "ISCXR,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getAccount() == null?"NULL":"'"+fixMsg.getAccount()+"'")+",";
	query += (fixMsg.getClOrderID() == null?"NULL":"'"+fixMsg.getClOrderID()+"'")+",";
	query += (fixMsg.getOrderID() == null?"NULL":"'"+fixMsg.getOrderID()+"'")+",";
	query += (fixMsg.getOrderQtyAsString() == null?"NULL":fixMsg.getOrderQtyAsString())+",";
	query += (fixMsg.getOrigClOrderID() == null?"NULL":"'"+fixMsg.getOrigClOrderID()+"'")+",";
	query += (fixMsg.getSide() == null?"NULL":"'"+fixMsg.getSide()+"'")+",";
	query += (fixMsg.getSymbol() == null?"NULL":"'"+fixMsg.getSymbol()+"'")+",";
	query += (fixMsg.getTransactTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getTransactTime())+"'")+",";
	query += (fixMsg.getSecurityDesc() == null?"NULL":"'"+fixMsg.getSecurityDesc()+"'")+",";
	query += (fixMsg.getSecurityType() == null?"NULL":"'"+fixMsg.getSecurityType()+"'")+",";
	query += (fixMsg.getMaturityYearAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getMaturityYear())+"'")+",";
	query += (fixMsg.getPutOrCallAsString() == null?"NULL":fixMsg.getPutOrCallAsString())+",";
	query += (fixMsg.getStrikePriceAsString() == null?"NULL":fixMsg.getStrikePriceAsString())+",";
	query += (fixMsg.getMaturityDayAsString() == null?"NULL":fixMsg.getMaturityDayAsString())+",";
	query += (fixMsg.getCorrelationClOrdID() == null?"NULL":"'"+fixMsg.getCorrelationClOrdID()+"'")+",";
	query += "'"+status+"'"+",";
	query += "'"+(isCXR?"Y":"N")+"'"+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}
			 	

    return query;
  }

private String constructShortCancelQuery(FixCancelReplace fixMsg, int messageId, 
  						String userId,String status,
  						boolean isCXR)
{
    String query = null;
  query = "INSERT INTO FIXCANCEL(";
	query += "DBCANCELID,";
	query += "ACCOUNT,";
	query += "CLORDERID,";
	query += "ORDERID,";
	query += "ORDERQTY,";
	query += "ORIGCLORDERID,";
	query += "SIDE,";
	query += "SYMBOL,";
	query += "TRANSACTTIME,";
	query += "SECURITYDESC,";
	query += "SECURITYTYPE,";
	query += "MATURITYYEAR,";
	query += "PUTORCALL,";
	query += "STRIKEPRICE,";
	query += "MATURITYDAY,";
	query += "CORRELATIONCLORDID,";
	query += "CANCELSTATUS,";
	query += "ISCXR,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getAccount() == null?"NULL":"'"+fixMsg.getAccount()+"'")+",";
	query += (fixMsg.getClOrderID() == null?"NULL":"'"+fixMsg.getClOrderID()+"'")+",";
	query += (fixMsg.getOrderID() == null?"NULL":"'"+fixMsg.getOrderID()+"'")+",";
	query += (fixMsg.getOrderQtyAsString() == null?"NULL":fixMsg.getOrderQtyAsString())+",";
	query += (fixMsg.getOrigClOrderID() == null?"NULL":"'"+fixMsg.getOrigClOrderID()+"'")+",";
	query += (fixMsg.getSide() == null?"NULL":"'"+fixMsg.getSide()+"'")+",";
	query += (fixMsg.getSymbol() == null?"NULL":"'"+fixMsg.getSymbol()+"'")+",";
	query += (fixMsg.getTransactTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getTransactTime())+"'")+",";
	query += (fixMsg.getSecurityDesc() == null?"NULL":"'"+fixMsg.getSecurityDesc()+"'")+",";
	query += (fixMsg.getSecurityType() == null?"NULL":"'"+fixMsg.getSecurityType()+"'")+",";
	query += (fixMsg.getMaturityYearAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getMaturityYear())+"'")+",";
	query += (fixMsg.getPutOrCallAsString() == null?"NULL":fixMsg.getPutOrCallAsString())+",";
	query += (fixMsg.getStrikePriceAsString() == null?"NULL":fixMsg.getStrikePriceAsString())+",";
	query += (fixMsg.getMaturityDayAsString() == null?"NULL":fixMsg.getMaturityDayAsString())+",";
	query += (fixMsg.getCorrelationClOrdID() == null?"NULL":"'"+fixMsg.getCorrelationClOrdID()+"'")+",";
	query += "'"+status+"'"+",";
	query += "'"+(isCXR?"Y":"N")+"'"+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}
			 	

    return query;
  }


public int addFixCancelReplace(FixCancelReplace fixMsg, 
  						String userId,
  						String status1,
  						String status2,
  						boolean isCXR)
    throws Exception
{
    System.out.println("addFixCancelReplace");
    //ResultSet rs;
    int messageId = 0;
    if( dbConnection == null )
      throw new Exception("No Connection To Database");


    synchronized ( dbConnection )
    {
      // first add into MESSAGEDETAILS table and get messageid
      messageId = addFixMessage(fixMsg,userId,"ORD");	
      System.out.println("Message ID = " + messageId);
      
      String query = constructShortOrderQuery(fixMsg,messageId, userId,status1, status2, isCXR);    	
      System.out.println("CXR-ORDER Query = ");
      System.out.println(query);   	

      String query1 = constructShortCancelQuery(fixMsg,messageId, userId,status1,isCXR);    	
      System.out.println("CXR-CANCEL Query = ");
      System.out.println(query1);   	


      try
      {
        Statement stmt = dbConnection.createStatement();
        stmt.executeUpdate(query);
        stmt.executeUpdate(query1);        
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    return messageId;
}


public int addFixOrder(FixOrder fixMsg, 
  						String userId,
  						String status1,
  						String status2,
  						boolean isCXR)
    throws Exception
{
    System.out.println("addFixOrder");
    //ResultSet rs;
    int messageId = 0;
    if( dbConnection == null )
      throw new Exception("No Connection To Database");


    synchronized ( dbConnection )
    {
      // first add into MESSAGEDETAILS table and get messageid
      messageId = addFixMessage(fixMsg,userId,"ORD");	
      System.out.println("Message ID = " + messageId);
      
      String query = constructShortOrderQuery(fixMsg,messageId, userId,status1, status2, isCXR);    	
      System.out.println("ORDER Query = ");
      System.out.println(query);   	


      try
      {
        Statement stmt = dbConnection.createStatement();
        stmt.executeUpdate(query);
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    return messageId;
}

private String constructShortOrderQuery(FixOrder fixMsg, int messageId, 
  						String userId,String status1, String status2,
  						boolean isCXR)
{
    String query = null;
    
 query = "INSERT INTO FIXORDER(";
	query += "DBORDERID,";
	query += "ACCOUNT,";
	query += "CLORDERID,";
	query += "ORDERQTY,";
	query += "ORDTYPE,";
	query += "PRICE,";
	query += "SIDE,";
	query += "SYMBOL,";
	query += "TIMEINFORCE,";
	query += "TRANSACTTIME,";
	query += "STOPPX,";
	query += "SECURITYDESC,";
	query += "SECURITYTYPE,";
	query += "MATURITYYEAR,";
	query += "PUTORCALL,";
	query += "STRIKEPRICE,";
	query += "MATURITYDAY,";
	query += "EXPIREDATE,";
	query += "CLEARINGFIRM,";
	query += "CLEARINGACCOUNT,";
	query += "OMNIBUSACCOUNT,";
	query += "CTICODE,";
	query += "CORRELATIONCLORDID,";
	query += "ORDERSTATUS1,";
	query += "ORDERSTATUS2,";
	query += "ISCXR,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getAccount() == null?"NULL":"'"+fixMsg.getAccount()+"'")+",";
	query += (fixMsg.getClOrderID() == null?"NULL":"'"+fixMsg.getClOrderID()+"'")+",";
	query += (fixMsg.getOrderQtyAsString() == null?"NULL":fixMsg.getOrderQtyAsString())+",";
	query += (fixMsg.getOrdType() == null?"NULL":"'"+fixMsg.getOrdType()+"'")+",";
	query += (fixMsg.getPriceAsString() == null?"NULL":fixMsg.getPriceAsString())+",";
	query += (fixMsg.getSide() == null?"NULL":"'"+fixMsg.getSide()+"'")+",";
	query += (fixMsg.getSymbol() == null?"NULL":"'"+fixMsg.getSymbol()+"'")+",";
	query += (fixMsg.getTimeInForce() == null?"NULL":"'"+fixMsg.getTimeInForce()+"'")+",";
	query += (fixMsg.getTransactTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getTransactTime())+"'")+",";
	query += (fixMsg.getStopPxAsString() == null?"NULL":fixMsg.getStopPxAsString())+",";
	query += (fixMsg.getSecurityDesc() == null?"NULL":"'"+fixMsg.getSecurityDesc()+"'")+",";
	query += (fixMsg.getSecurityType() == null?"NULL":"'"+fixMsg.getSecurityType()+"'")+",";
	query += (fixMsg.getMaturityYearAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getMaturityYear())+"'")+",";
	query += (fixMsg.getPutOrCallAsString() == null?"NULL":fixMsg.getPutOrCallAsString())+",";
	query += (fixMsg.getStrikePriceAsString() == null?"NULL":fixMsg.getStrikePriceAsString())+",";
	query += (fixMsg.getMaturityDayAsString() == null?"NULL":fixMsg.getMaturityDayAsString())+",";
	query += (fixMsg.getExpireDateAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getExpireDate())+"'")+",";
	query += (fixMsg.getClearingFirm() == null?"NULL":"'"+fixMsg.getClearingFirm()+"'")+",";
	query += (fixMsg.getClearingAccount() == null?"NULL":"'"+fixMsg.getClearingAccount()+"'")+",";
	query += (fixMsg.getOmnibusAccount() == null?"NULL":"'"+fixMsg.getOmnibusAccount()+"'")+",";
	query += (fixMsg.getCtiCodeAsString() == null?"NULL":fixMsg.getCtiCodeAsString())+",";
	query += (fixMsg.getCorrelationClOrdID() == null?"NULL":"'"+fixMsg.getCorrelationClOrdID()+"'")+",";
	query += "'"+status1+"'"+",";
	query += "'"+status2+"'"+",";
	query += "'"+(isCXR?"Y":"N")+"'"+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}
	

    return query;
  }

private String constructShortOrderQuery(FixCancelReplace fixMsg, int messageId, 
  						String userId,String status1, String status2,
  						boolean isCXR)
{
    String query = null;
    
 query = "INSERT INTO FIXORDER(";
	query += "DBORDERID,";
	query += "ACCOUNT,";
	query += "CLORDERID,";
	query += "ORDERQTY,";
	query += "ORDTYPE,";
	query += "PRICE,";
	query += "SIDE,";
	query += "SYMBOL,";
	query += "TIMEINFORCE,";
	query += "TRANSACTTIME,";
	query += "STOPPX,";
	query += "SECURITYDESC,";
	query += "SECURITYTYPE,";
	query += "MATURITYYEAR,";
	query += "PUTORCALL,";
	query += "STRIKEPRICE,";
	query += "MATURITYDAY,";
	query += "EXPIREDATE,";
	query += "CLEARINGFIRM,";
	query += "CLEARINGACCOUNT,";
	query += "OMNIBUSACCOUNT,";
	query += "CTICODE,";
	query += "CORRELATIONCLORDID,";
	query += "ORDERSTATUS1,";
	query += "ORDERSTATUS2,";
	query += "ISCXR,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getAccount() == null?"NULL":"'"+fixMsg.getAccount()+"'")+",";
	query += (fixMsg.getClOrderID() == null?"NULL":"'"+fixMsg.getClOrderID()+"'")+",";
	query += (fixMsg.getOrderQtyAsString() == null?"NULL":fixMsg.getOrderQtyAsString())+",";
	query += (fixMsg.getOrdType() == null?"NULL":"'"+fixMsg.getOrdType()+"'")+",";
	query += (fixMsg.getPriceAsString() == null?"NULL":fixMsg.getPriceAsString())+",";
	query += (fixMsg.getSide() == null?"NULL":"'"+fixMsg.getSide()+"'")+",";
	query += (fixMsg.getSymbol() == null?"NULL":"'"+fixMsg.getSymbol()+"'")+",";
	query += (fixMsg.getTimeInForce() == null?"NULL":"'"+fixMsg.getTimeInForce()+"'")+",";
	query += (fixMsg.getTransactTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getTransactTime())+"'")+",";
	query += (fixMsg.getStopPxAsString() == null?"NULL":fixMsg.getStopPxAsString())+",";
	query += (fixMsg.getSecurityDesc() == null?"NULL":"'"+fixMsg.getSecurityDesc()+"'")+",";
	query += (fixMsg.getSecurityType() == null?"NULL":"'"+fixMsg.getSecurityType()+"'")+",";
	query += (fixMsg.getMaturityYearAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getMaturityYear())+"'")+",";
	query += (fixMsg.getPutOrCallAsString() == null?"NULL":fixMsg.getPutOrCallAsString())+",";
	query += (fixMsg.getStrikePriceAsString() == null?"NULL":fixMsg.getStrikePriceAsString())+",";
	query += (fixMsg.getMaturityDayAsString() == null?"NULL":fixMsg.getMaturityDayAsString())+",";
	query += (fixMsg.getExpireDateAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getExpireDate())+"'")+",";
	query += (fixMsg.getClearingFirm() == null?"NULL":"'"+fixMsg.getClearingFirm()+"'")+",";
	query += (fixMsg.getClearingAccount() == null?"NULL":"'"+fixMsg.getClearingAccount()+"'")+",";
	query += (fixMsg.getOmnibusAccount() == null?"NULL":"'"+fixMsg.getOmnibusAccount()+"'")+",";
	query += (fixMsg.getCtiCodeAsString() == null?"NULL":fixMsg.getCtiCodeAsString())+",";
	query += (fixMsg.getCorrelationClOrdID() == null?"NULL":"'"+fixMsg.getCorrelationClOrdID()+"'")+",";
	query += "'"+status1+"'"+",";
	query += "'"+status2+"'"+",";
	query += "'"+(isCXR?"Y":"N")+"'"+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}
	

    return query;
  }

private String constructOrderQuery(FixOrder fixMsg, int messageId, 
  						String userId,String status1, String status2,
  						boolean isCXR)
{
    String query = null;
    
     query = "INSERT INTO FIXORDER(";
	query += "DBORDERID,";
	query += "ACCOUNT,";
	query += "CLORDERID,";
	query += "COMMISSION,";
	query += "COMMTYPE,";
	query += "CURRENCY,";
	query += "EXECINST,";
	query += "HANDLINST,";
	query += "IDSOURCE,";
	query += "IOIID,";
	query += "ORDERQTY,";
	query += "ORDTYPE,";
	query += "PRICE,";
	query += "ORDERCAPACITY,";
	query += "SECURITYID,";
	query += "SIDE,";
	query += "SYMBOL,";
	query += "TEXT,";
	query += "TIMEINFORCE,";
	query += "TRANSACTTIME,";
	query += "SETTLEMENTTYP,";
	query += "FUTSETTDATE,";
	query += "SYMBOLSFX,";
	query += "EXECBROKER,";
	query += "OPENCLOSE,";
	query += "PROCESSCODE,";
	query += "STOPPX,";
	query += "EXDESTINATION,";
	query += "ISSUER,";
	query += "SECURITYDESC,";
	query += "CLIENTID,";
	query += "MINQTY,";
	query += "MAXFLOOR,";
	query += "LOCATEREQD,";
	query += "QUOTEID,";
	query += "SETTLCURRENCY,";
	query += "FOREXREQ,";
	query += "EXPIRETIME,";
	query += "PREVCLOSEPX,";
	query += "CASHORDERQTY,";
	query += "SECURITYTYPE,";
	query += "EFFECTIVETIME,";
	query += "ORDERQTY2,";
	query += "FUTSETTDATE2,";
	query += "MATURITYYEAR,";
	query += "PUTORCALL,";
	query += "STRIKEPRICE,";
	query += "COVEREDORUNCOVERED,";
	query += "CUSTOMERORFIRM,";
	query += "MATURITYDAY,";
	query += "OPTATTRIBUTE,";
	query += "SECURITYEXCHANGE,";
	query += "MAXSHOW,";
	query += "PEGDIFFERENCE,";
	query += "COUPONRATE,";
	query += "CONTRACTMULTIPLIER,";
	query += "COMPLIANCEID,";
	query += "SOLICITEDFLAG,";
	query += "DISCRETIONINST,";
	query += "DISCRETIONOFFSET,";
	query += "EXPIREDATE,";
	query += "CLEARINGFIRM,";
	query += "CLEARINGACCOUNT,";
	query += "OMNIBUSACCOUNT,";
	query += "CTICODE,";
	query += "FEEBILLING,";
	query += "GIVEUPFIRM,";
	query += "CMTAGIVEUPCD,";
	query += "CORRELATIONCLORDID,";
	query += "ORDERSTATUS1,";
	query += "ORDERSTATUS2,";
	query += "ISCXR,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getAccount() == null?"NULL":"'"+fixMsg.getAccount()+"'")+",";
	query += (fixMsg.getClOrderID() == null?"NULL":"'"+fixMsg.getClOrderID()+"'")+",";
	query += (fixMsg.getCommissionAsString() == null?"NULL":fixMsg.getCommissionAsString())+",";
	query += (fixMsg.getCommType() == null?"NULL":"'"+fixMsg.getCommType()+"'")+",";
	query += (fixMsg.getCurrency() == null?"NULL":"'"+fixMsg.getCurrency()+"'")+",";
	query += (fixMsg.getExecInst() == null?"NULL":"'"+fixMsg.getExecInst()+"'")+",";
	query += (fixMsg.getHandlInst() == null?"NULL":"'"+fixMsg.getHandlInst()+"'")+",";
	query += (fixMsg.getIDSource() == null?"NULL":"'"+fixMsg.getIDSource()+"'")+",";
	query += (fixMsg.getIOIid() == null?"NULL":"'"+fixMsg.getIOIid()+"'")+",";
	query += (fixMsg.getOrderQtyAsString() == null?"NULL":fixMsg.getOrderQtyAsString())+",";
	query += (fixMsg.getOrdType() == null?"NULL":"'"+fixMsg.getOrdType()+"'")+",";
	query += (fixMsg.getPriceAsString() == null?"NULL":fixMsg.getPriceAsString())+",";
	query += (fixMsg.getOrderCapacity() == null?"NULL":"'"+fixMsg.getOrderCapacity()+"'")+",";
	query += (fixMsg.getSecurityID() == null?"NULL":"'"+fixMsg.getSecurityID()+"'")+",";
	query += (fixMsg.getSide() == null?"NULL":"'"+fixMsg.getSide()+"'")+",";
	query += (fixMsg.getSymbol() == null?"NULL":"'"+fixMsg.getSymbol()+"'")+",";
	query += (fixMsg.getText() == null?"NULL":"'"+fixMsg.getText()+"'")+",";
	query += (fixMsg.getTimeInForce() == null?"NULL":"'"+fixMsg.getTimeInForce()+"'")+",";
	query += (fixMsg.getTransactTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getTransactTime())+"'")+",";
	query += (fixMsg.getSettlementTyp() == null?"NULL":"'"+fixMsg.getSettlementTyp()+"'")+",";
	query += (fixMsg.getFutSettDateAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getFutSettDate())+"'")+",";
	query += (fixMsg.getSymbolSfx() == null?"NULL":"'"+fixMsg.getSymbolSfx()+"'")+",";
	query += (fixMsg.getExecBroker() == null?"NULL":"'"+fixMsg.getExecBroker()+"'")+",";
	query += (fixMsg.getOpenClose() == null?"NULL":"'"+fixMsg.getOpenClose()+"'")+",";
	query += (fixMsg.getProcessCode() == null?"NULL":"'"+fixMsg.getProcessCode()+"'")+",";
	query += (fixMsg.getStopPxAsString() == null?"NULL":fixMsg.getStopPxAsString())+",";
	query += (fixMsg.getExDestination() == null?"NULL":"'"+fixMsg.getExDestination()+"'")+",";
	query += (fixMsg.getIssuer() == null?"NULL":"'"+fixMsg.getIssuer()+"'")+",";
	query += (fixMsg.getSecurityDesc() == null?"NULL":"'"+fixMsg.getSecurityDesc()+"'")+",";
	query += (fixMsg.getClientID() == null?"NULL":"'"+fixMsg.getClientID()+"'")+",";
	query += (fixMsg.getMinQtyAsString() == null?"NULL":fixMsg.getMinQtyAsString())+",";
	query += (fixMsg.getMaxFloorAsString() == null?"NULL":fixMsg.getMaxFloorAsString())+",";
	query += (fixMsg.getLocateReqdAsString() == null?"NULL":"'"+fixMsg.getLocateReqdAsString()+"'")+",";
	query += (fixMsg.getQuoteID() == null?"NULL":"'"+fixMsg.getQuoteID()+"'")+",";
	query += (fixMsg.getSettlCurrency() == null?"NULL":"'"+fixMsg.getSettlCurrency()+"'")+",";
	query += (fixMsg.getForexReqAsString() == null?"NULL":"'"+fixMsg.getForexReqAsString()+"'")+",";
	query += (fixMsg.getExpireTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getExpireTime())+"'")+",";
	query += (fixMsg.getPrevClosePxAsString() == null?"NULL":fixMsg.getPrevClosePxAsString())+",";
	query += (fixMsg.getCashOrderQtyAsString() == null?"NULL":fixMsg.getCashOrderQtyAsString())+",";
	query += (fixMsg.getSecurityType() == null?"NULL":"'"+fixMsg.getSecurityType()+"'")+",";
	query += (fixMsg.getEffectiveTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getEffectiveTime())+"'")+",";
	query += (fixMsg.getOrderQty2AsString() == null?"NULL":fixMsg.getOrderQty2AsString())+",";
	query += (fixMsg.getFutSettDate2AsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getFutSettDate2())+"'")+",";
	query += (fixMsg.getMaturityYearAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getMaturityYear())+"'")+",";
	query += (fixMsg.getPutOrCallAsString() == null?"NULL":fixMsg.getPutOrCallAsString())+",";
	query += (fixMsg.getStrikePriceAsString() == null?"NULL":fixMsg.getStrikePriceAsString())+",";
	query += (fixMsg.getCoveredOrUncoveredAsString() == null?"NULL":fixMsg.getCoveredOrUncoveredAsString())+",";
	query += (fixMsg.getCustomerOrFirmAsString() == null?"NULL":fixMsg.getCustomerOrFirmAsString())+",";
	query += (fixMsg.getMaturityDayAsString() == null?"NULL":fixMsg.getMaturityDayAsString())+",";
	query += (fixMsg.getOptAttribute() == null?"NULL":"'"+fixMsg.getOptAttribute()+"'")+",";
	query += (fixMsg.getSecurityExchange() == null?"NULL":"'"+fixMsg.getSecurityExchange()+"'")+",";
	query += (fixMsg.getMaxShowAsString() == null?"NULL":fixMsg.getMaxShowAsString())+",";
	query += (fixMsg.getPegDifferenceAsString() == null?"NULL":fixMsg.getPegDifferenceAsString())+",";
	query += (fixMsg.getCouponRateAsString() == null?"NULL":fixMsg.getCouponRateAsString())+",";
	query += (fixMsg.getContractMultiplierAsString() == null?"NULL":fixMsg.getContractMultiplierAsString())+",";
	query += (fixMsg.getComplianceID() == null?"NULL":"'"+fixMsg.getComplianceID()+"'")+",";
	query += (fixMsg.getSolicitedFlagAsString() == null?"NULL":"'"+fixMsg.getSolicitedFlagAsString()+"'")+",";
	query += (fixMsg.getDiscretionInst() == null?"NULL":"'"+fixMsg.getDiscretionInst()+"'")+",";
	query += (fixMsg.getDiscretionOffsetAsString() == null?"NULL":fixMsg.getDiscretionOffsetAsString())+",";
	query += (fixMsg.getExpireDateAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getExpireDate())+"'")+",";
	query += (fixMsg.getClearingFirm() == null?"NULL":"'"+fixMsg.getClearingFirm()+"'")+",";
	query += (fixMsg.getClearingAccount() == null?"NULL":"'"+fixMsg.getClearingAccount()+"'")+",";
	query += (fixMsg.getOmnibusAccount() == null?"NULL":"'"+fixMsg.getOmnibusAccount()+"'")+",";
	query += (fixMsg.getCtiCodeAsString() == null?"NULL":fixMsg.getCtiCodeAsString())+",";
	query += (fixMsg.getFeeBilling() == null?"NULL":"'"+fixMsg.getFeeBilling()+"'")+",";
	query += (fixMsg.getGiveupFirm() == null?"NULL":"'"+fixMsg.getGiveupFirm()+"'")+",";
	query += (fixMsg.getCmtaGiveupCD() == null?"NULL":"'"+fixMsg.getCmtaGiveupCD()+"'")+",";
	query += (fixMsg.getCorrelationClOrdID() == null?"NULL":"'"+fixMsg.getCorrelationClOrdID()+"'")+",";
	query += "'"+status1+"'"+",";
	query += "'"+status2+"'"+",";
	query += "'"+(isCXR?"Y":"N")+"'"+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}
	
	

    return query;
  }
  


  public boolean processResult( ResultSet rs )
    throws SQLException
  {
    return true;
  }


  private String constructMessageDetailsQuery(FixMessage fixMsg, String userId, String typeCode)
  {
    String query = null;
  query = "INSERT INTO MESSAGEDETAILS(";
	query += "BEGINSTRING,";
	query += "BODYLENGTH,";
	query += "MSGSEQNUM,";
	query += "MSGTYPE,";
	query += "POSSDUPFLAG,";
	query += "SENDERCOMPID,";
	query += "SENDERSUBID,";
	query += "SENDINGTIME,";
	query += "TARGETCOMPID,";
	query += "TARGETSUBID,";
	query += "POSSRESEND,";
	query += "ONBEHALFOFCOMPID,";
	query += "ONBEHALFOFSUBID,";
	query += "ORIGSENDINGTIME,";
	query += "DELIVERTOCOMPID,";
	query += "DELIVERTOSUBID,";
	query += "SENDERLOCATIONID,";
	query += "TARGETLOCATIONID,";
	query += "ONBEHALFOFLOCATIONID,";
	query += "DELIVERTOLOCATIONID,";
	query += "MESSAGEENCODING,";
	query += "LASTMSGSEQNUMPROCESSED,";
	query += "ONBEHALFOFSENDINGTIME,";
	query += "TYPECODE,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += (fixMsg.getBeginString() == null?"NULL":"'"+fixMsg.getBeginString()+"'")+",";
	query += (fixMsg.getBodyLengthAsString() == null?"NULL":fixMsg.getBodyLengthAsString())+",";
	query += (fixMsg.getMsgSeqNumAsString() == null?"NULL":fixMsg.getMsgSeqNumAsString())+",";
	query += (fixMsg.getMsgType() == null?"NULL":"'"+fixMsg.getMsgType()+"'")+",";
	query += (fixMsg.getPossDupFlagAsString() == null?"NULL":"'"+fixMsg.getPossDupFlagAsString()+"'")+",";
	query += (fixMsg.getSenderCompID() == null?"NULL":"'"+fixMsg.getSenderCompID()+"'")+",";
	query += (fixMsg.getSenderSubID() == null?"NULL":"'"+fixMsg.getSenderSubID()+"'")+",";
	query += (fixMsg.getSendingTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getSendingTime())+"'")+",";
	query += (fixMsg.getTargetCompID() == null?"NULL":"'"+fixMsg.getTargetCompID()+"'")+",";
	query += (fixMsg.getTargetSubID() == null?"NULL":"'"+fixMsg.getTargetSubID()+"'")+",";
	query += (fixMsg.getPossResendAsString() == null?"NULL":"'"+fixMsg.getPossResendAsString()+"'")+",";
	query += (fixMsg.getOnBehalfOfCompID() == null?"NULL":"'"+fixMsg.getOnBehalfOfCompID()+"'")+",";
	query += (fixMsg.getOnBehalfOfSubID() == null?"NULL":"'"+fixMsg.getOnBehalfOfSubID()+"'")+",";
	query += (fixMsg.getOrigSendingTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getOrigSendingTime())+"'")+",";
	query += (fixMsg.getDeliverToCompID() == null?"NULL":"'"+fixMsg.getDeliverToCompID()+"'")+",";
	query += (fixMsg.getDeliverToSubID() == null?"NULL":"'"+fixMsg.getDeliverToSubID()+"'")+",";
	query += (fixMsg.getSenderLocationID() == null?"NULL":"'"+fixMsg.getSenderLocationID()+"'")+",";
	query += (fixMsg.getTargetLocationID() == null?"NULL":"'"+fixMsg.getTargetLocationID()+"'")+",";
	query += (fixMsg.getOnBehalfOfLocationID() == null?"NULL":"'"+fixMsg.getOnBehalfOfLocationID()+"'")+",";
	query += (fixMsg.getDeliverToLocationID() == null?"NULL":"'"+fixMsg.getDeliverToLocationID()+"'")+",";
	query += (fixMsg.getMessageEncoding() == null?"NULL":"'"+fixMsg.getMessageEncoding()+"'")+",";
	query += (fixMsg.getLastMsgSeqNumProcessedAsString() == null?"NULL":fixMsg.getLastMsgSeqNumProcessedAsString())+",";
	query += (fixMsg.getOnBehalfOfSendingTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getOnBehalfOfSendingTime())+"'")+",";
	//query += (fixMsg.getTypeCode() == null?"NULL":"'"+fixMsg.getTypeCode()+"'")+",";
	query += "'"+typeCode+"'"+",";
	query += "'"+userId+"'"+",";
	
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}

	return query;
  }  
  
  
public int addFixCancelReject(FixCancelReject fixMsg, 
  						String userId)
    throws Exception
{
    System.out.println("addFixCancelReject");
    //ResultSet rs;
    int messageId = 0;
    if( dbConnection == null )
      throw new Exception("No Connection To Database");


    synchronized ( dbConnection )
    {
      // first add into MESSAGEDETAILS table and get messageid
      messageId = addFixMessage(fixMsg,userId,"CRJ");	
      System.out.println("Message ID = " + messageId);
      
      String query = constructShortCancelRejectQuery(fixMsg,messageId, userId);

      System.out.println("Cancel Reject Query = ");
      System.out.println(query);   	


      try
      {
        Statement stmt = dbConnection.createStatement();
        stmt.executeUpdate(query);
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    return messageId;
}
private String constructShortCancelRejectQuery(FixCancelReject fixMsg, int messageId, 
  						String userId)
{
    String query = null;
    
  query = "INSERT INTO FIXCANCELREJECT(";
	query += "DBCANCELREJECTID,";
	query += "ACCOUNT,";
	query += "CLORDERID,";
	query += "EXECID,";
	query += "ORDERID,";
	query += "ORIGCLORDERID,";
	query += "TEXT,";
	query += "TRANSACTTIME,";
	query += "CXLREJREASON,";
	query += "CXLREJRESPONSETO,";
	query += "CORRELATIONCLORDID,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getAccount() == null?"NULL":"'"+fixMsg.getAccount()+"'")+",";
	query += (fixMsg.getClOrderID() == null?"NULL":"'"+fixMsg.getClOrderID()+"'")+",";
	query += (fixMsg.getExecID() == null?"NULL":"'"+fixMsg.getExecID()+"'")+",";
	query += (fixMsg.getOrderID() == null?"NULL":"'"+fixMsg.getOrderID()+"'")+",";
	query += (fixMsg.getOrigClOrderID() == null?"NULL":"'"+fixMsg.getOrigClOrderID()+"'")+",";
	query += (fixMsg.getText() == null?"NULL":"'"+fixMsg.getText()+"'")+",";
	query += (fixMsg.getTransactTimeAsString() == null?"NULL":"'"+FixMessage.getDBTimestamp(fixMsg.getTransactTime())+"'")+",";
	query += (fixMsg.getCxlRejReason() == null?"NULL":fixMsg.getCxlRejReason())+",";
	query += (fixMsg.getCxlRejResponseTo() == null?"NULL":"'"+fixMsg.getCxlRejResponseTo()+"'")+",";
	query += (fixMsg.getCorrelationClOrdID() == null?"NULL":"'"+fixMsg.getCorrelationClOrdID()+"'")+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}
	
	

    return query;
  }
  
public int addFixBusinessReject(FixBusinessReject fixMsg, 
  						String userId)
    throws Exception
{
    System.out.println("addFixBusinessReject");
    //ResultSet rs;
    int messageId = 0;
    if( dbConnection == null )
      throw new Exception("No Connection To Database");


    synchronized ( dbConnection )
    {
      // first add into MESSAGEDETAILS table and get messageid
      messageId = addFixMessage(fixMsg,userId,"BRJ");	
      System.out.println("Message ID = " + messageId);
      
      String query = constructShortBusinessRejectQuery(fixMsg,messageId, userId);

      System.out.println("Business Reject Query = ");
      System.out.println(query);   	


      try
      {
        Statement stmt = dbConnection.createStatement();
        stmt.executeUpdate(query);
      }
      catch( Exception e )
      {
        e.printStackTrace();
        throw e;
      }
    }
    
    return messageId;
}
private String constructShortBusinessRejectQuery(FixBusinessReject fixMsg, int messageId, 
  						String userId)
{
    String query = null;
    
    
 query = "INSERT INTO FIXBUSINESSREJECT(";
	query += "DBBUSINESSREJECTID,";
	query += "REFSEQNUM,";
	query += "TEXT,";
	query += "REFMSGTYPE,";
	query += "BUSINESSREJECTREASON,";
	query += "CREATEDBY,";
	if(!isSQLServer)
	{
		query += "CREATIONDATE,";
		query += "LASTMODIFIEDBY,";
	}
	else
		query += "LASTMODIFIEDBY";
	if(!isSQLServer)
		query += "LASTMODIFIEDDATE";
	query += ") VALUES (";
	query += messageId+",";
	query += (fixMsg.getRefSeqNumAsString() == null?"NULL":fixMsg.getRefSeqNumAsString())+",";
	query += (fixMsg.getText() == null?"NULL":"'"+fixMsg.getText()+"'")+",";
	query += (fixMsg.getRefMsgType() == null?"NULL":"'"+fixMsg.getRefMsgType()+"'")+",";
	query += (fixMsg.getBusinessRejectReasonAsString() == null?"NULL":fixMsg.getBusinessRejectReasonAsString())+",";
	query += "'"+userId+"'"+",";
	if(!isSQLServer)
	{
		query += "CURRENT_TIMESTAMP"+",";
		query += "'"+userId+"'"+",";
	    query += "CURRENT_TIMESTAMP)";
	}
	else
	{
		query += "'"+userId+"'"+")";
	}	
	

    return query;
  }
  
}
