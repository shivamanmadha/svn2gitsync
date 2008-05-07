package com.exsys.fix.message;
import com.exsys.common.util.*;


import java.text.SimpleDateFormat;
import java.util.*;


import com.exsys.service.Logger;

/**
 * @author kreddy
 *
 * This class is used to represent a row in book window
 * with both bid and offer info for a given price
 */
public class FixRegulatoryMsg implements Comparable{
	private FixMessage fixMessage  = null;
	private boolean toExchange = false;
	private String exchName = null;
	private static SimpleDateFormat regDateFormatter =
		new java.text.SimpleDateFormat("yyyy-MM-dd");


	/**
	 * FixRegulatoryMsg contstructor
	 */
	public FixRegulatoryMsg(FixMessage msg,
			String exch,
			boolean msgToExchange)
	{
		fixMessage = msg;
		toExchange = msgToExchange;
		exchName = exch;
	}
	public Date getSendingTime()
	{
		return fixMessage.getSendingTime();
	}
	public long getSendingTimeInMillis()
	{
		return fixMessage.getSendingTime().getTime();
	}
	public boolean isMsgToExchange()
	{
		return toExchange;
	}
	public static String getRegDateString(Date value) {
		return regDateFormatter.format(value);
	}	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o)
	{
		return ((((FixRegulatoryMsg)o).getSendingTimeInMillis() <= getSendingTimeInMillis())?1:-1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "FixMsg - ";
		str += (String)FixMessageFactory.msgTypesMap.get(fixMessage.getMessageType());
		str += " SendingTime = " + fixMessage.getSendingTimeAsString();
		return str;
	}
	public static String getCSVHeader()
	{
		String header = "";
		header += "SLE ServerID,"; //1
		header += "Server Trans Num,"; //2
		header += "Server Process Date,";//3
		header += "Server Timestamp,";//4
		header += "Exchange,";//5
		header += "MessageDirection,";//6
		header += "Status,";//7
		header += "Order Number,";//8
		header += "Relative Key,";//9
		header += "Action,";//10
		header += "BuySell Ind,";//11
		header += "Quantity,";//12
		header += "Instrument,";//13
		header += "Product,";//14
		header += "Delivery Date,";//15
		header += "Put Call Ind,";//16
		header += "Strike Price,";//17
		header += "Limit Price,";//18
		header += "Stop Price,";//19
		header += "Fill Price,";//20
		header += "Order Type,";//21
		header += "Order Qualifier,";//22
		header += "Globex UserID,";//23
		header += "Firm UserID,";//24
		header += "Customer Type Ind,";//25
		header += "Origin,";//26
		header += "Account Number,";//27
		header += "Giveup Firm,";//28
		header += "Discretionary Ind,";//29
		header += "Discretionary Points,";//30
		header += "Comments,";//31
		header += "Firm";//32

		return header;
	}
	public String toCSV()
	{
		String csv = "";
		csv += getToken1()+",";
		csv += getToken2()+",";
		csv += getToken3()+",";
		csv += getToken4()+",";
		csv += getToken5()+",";
		csv += getToken6()+",";
		csv += getToken7()+",";
		csv += getToken8()+",";
		csv += getToken9()+",";
		csv += getToken10()+",";
		csv += getToken11()+",";
		csv += getToken12()+",";
		csv += getToken13()+",";
		csv += getToken14()+",";
		csv += getToken15()+",";
		csv += getToken16()+",";
		csv += getToken17()+",";
		csv += getToken18()+",";
		csv += getToken19()+",";
		csv += getToken20()+",";
		csv += getToken21()+",";
		csv += getToken22()+",";
		csv += getToken23()+",";
		csv += getToken24()+",";
		csv += getToken25()+",";
		csv += getToken26()+",";
		csv += getToken27()+",";
		csv += getToken28()+",";
		csv += getToken29()+",";
		csv += getToken30()+",";
		csv += getToken31()+",";
		csv += getToken32()+"";

		return csv;
	}
	public String getToken1()
	{
		// SLE ServerID
		return "";
	}
	public String getToken2()
	{
		// Server Trans Num
		return fixMessage.getMsgSeqNumAsString();
	}
	public String getToken3()
	{
		// Server Process Date
		Date sendTime = fixMessage.getSendingTime();
		String processDate = getRegDateString(sendTime);
		return processDate;
	}
	public String getToken4()
	{
		// Server Timestamp

		// Server Process Date
		Date sendTime = fixMessage.getSendingTime();
		String processTime = FixMessage.getUTCTimeString(sendTime);

		return processTime;
	}
	public String getToken5()
	{
		// Exchange
		return (toExchange?fixMessage.getTargetCompID():fixMessage.getSenderCompID());
	}
	public String getToken6()
	{
		// direction
		return (toExchange?"TO "+exchName:"FROM "+exchName);
	}
	public String getToken7()
	{
		// Status
		String status = "";
		String msgType = fixMessage.getMessageType();
		if(msgType.equals(FixConstants.FIX_MSGTYPE_LOGON))
		{
			status = "OK";
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_LOGOUT))
		{
			if(((FixLogout)fixMessage).getText() != null)
			{
				status = "REJECT";
			}
			else
			{
				status = "OK";
			}
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_ORDER)||
				msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL)||
				msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
		{
			status = "OK";
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT))
		{
			if(((FixExecutionReport)fixMessage).getText() != null)
			{
				status = "REJECT";
			}
			else
			{
				status = "OK";
			}
		}
		else
		{
			status = "UNKN-"+msgType;
		}		

		return status;
	}
	public String getToken8()
	{
		// Order Number
		String clOrderID = fixMessage.getBodyFieldValue(11);
		
		return (clOrderID != null?clOrderID:"");
	}
	public String getToken9()
	{
		// Relative Key
		String corrClOrderID = fixMessage.getBodyFieldValue(9717);
		
		if(corrClOrderID == null) return "";
		
		String msgType = fixMessage.getMessageType();		
		
		if(msgType.equals(FixConstants.FIX_MSGTYPE_ORDER)||
				msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL)||
				msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
		{
			corrClOrderID = corrClOrderID.substring(corrClOrderID.length()-5);
		}
		
		
		return (corrClOrderID);
	}
	public String getToken10()
	{
		// Action
		String action = "";
		String msgType = fixMessage.getMessageType();
		if(msgType.equals(FixConstants.FIX_MSGTYPE_LOGON))
		{
			action = "LOGIN";
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_LOGOUT))
		{
			action = "LOGOUT";
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
		{
			action = "ENTER";
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL))
		{
			action = "CANCEL";
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
		{
			action  = "MODIFY";
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT))
		{
			FixExecutionReport rep = (FixExecutionReport)fixMessage;
			
			if(rep.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REJECTED))
			{
				action = "REJECT";
			}
			else if(rep.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_REPLACED))
			{
				action = "REPLACED";
			}
			else if(rep.getExecType().equals(FixConstants.FIX_EXECUTIONREPORT_CANCELED))
			{
				action = "CANCELCONFIRM";
			}			
			else
			{
				action = "EXECUTION";	
			}
			
		}
		else if(msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT))
		{
			action = "REJECT";
		}		
		else
		{
			action = "UNKN-"+msgType;
		}
		
		return action;
	}
	public String getToken11()
	{
		// Buy Sell Ind
		String side = fixMessage.getBodyFieldValue(54);
		String value = FixLookup.lookupTagValue("54",side);
		return (value != null?value:"");
	}
	public String getToken12()
	{
		// Quantity
		String value = fixMessage.getBodyFieldValue(38);
		return (value != null?value:"");			
	}
	public String getToken13()
	{
		// Instrument
		String value = fixMessage.getBodyFieldValue(107);
		return (value != null?value:"");			
	}
	public String getToken14()
	{
		// Product
		String value = fixMessage.getBodyFieldValue(55);
		return (value != null?value:"");			
	}
	public String getToken15()
	{
		// Delivery Date
		String deliveryDate = getDeliveryDate(fixMessage.getBodyFieldValue(107));
		return deliveryDate;
	}
	public String getDeliveryDate(String instrument)
	{
		String date ="";
		if(instrument != null)
		{
			if(exchName.equals("CME"))
			{
				char monthCode = instrument.charAt(instrument.length()-2);
				char yearCode = instrument.charAt(instrument.length()-1);
				char [] year = {'2','0','0',yearCode};
				String yr = new String(year);
				String month = "";
				if(monthCode == 'H')
				{
					month = "03";
				}else if( monthCode == 'M')
				{
					month = "06";
				}
				else if( monthCode == 'U')
				{
					month = "09";
				}
				else if( monthCode == 'Z')
				{
					month = "09";
				}				
				date = yr + "-"+month;
			}
		}
		
		return date;		
	}
	public String getToken16()
	{
		// Put Call Ind
		String pc = fixMessage.getBodyFieldValue(201);
		String value = FixLookup.lookupTagValue("201",pc);
		return (value != null?value:"");
					
		
	}
	public String getToken17()
	{
		// Strike Price
		String value = fixMessage.getBodyFieldValue(202);
		return (value != null?value:"");			
	}
	public String getToken18()
	{
		// Limit Price
		String value = fixMessage.getBodyFieldValue(44);
		return (value != null?value:"");			

	}
	public String getToken19()
	{
		// Stop Price
		String value = fixMessage.getBodyFieldValue(99);
		return (value != null?value:"");			

	}
	public String getToken20()
	{
		// Fill Price
		String value = fixMessage.getBodyFieldValue(31);
		return (value != null?value:"");			

	}
	public String getToken21()
	{
		//Order type
		String ot = fixMessage.getBodyFieldValue(40);
		String value = FixLookup.lookupTagValue("40",ot);
		return (value != null?value:"");

	}
	public String getToken22()
	{
		// Order Qualifier
		String ot = fixMessage.getBodyFieldValue(59);
		String value = FixLookup.lookupTagValue("59",ot);
		return (value != null?value:"");

	}
	public String getToken23()
	{
		// Globex User ID
		//String value = fixMessage.getBodyFieldValue(37);
		//return (value != null?value:"");
		return "";
	}
	public String getToken24()
	{
		// Firm User ID
		String value = null;
		value = (toExchange?fixMessage.getHeaderFieldValue(50):fixMessage.getHeaderFieldValue(57));
		return (value != null?value:"");

	}
	public String getToken25()
	{
		// Customer Type Ind
		String value = fixMessage.getBodyFieldValue(9702);
		return (value != null?value:"");
	}
	public String getToken26()
	{
		// Origin
		String value = fixMessage.getBodyFieldValue(204);
		return (value != null?value:"");
	}
	public String getToken27()
	{
		// Account Number
		String value = fixMessage.getBodyFieldValue(1);
		return (value != null?value:"");
	}
	public String getToken28()
	{
		// Giveup Firm
		String value = fixMessage.getBodyFieldValue(9707);
		return (value != null?value:"");

	}
	public String getToken29()
	{
		// Discretionary Ind
		return "";
	}
	public String getToken30()
	{
		// Discretionary Points
		return "";
	}
	public String getToken31()
	{
		// Comments
		String value = fixMessage.getBodyFieldValue(58);
		return (value != null?value:"");
	}
	public String getToken32()
	{
		// firm
		String firm = "";
		String firmid=toExchange?fixMessage.getSenderCompID():fixMessage.getTargetCompID();
		
		if(firmid != null)
		{
			firm = firmid;
			if(exchName.equals("CME"))
			{
				firm = firmid.substring(2,5);
			}
			
		}
		return firm;
	}

}
