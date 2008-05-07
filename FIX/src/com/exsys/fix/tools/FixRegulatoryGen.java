package com.exsys.fix.tools;


import java.io.*;
import java.util.*;

import com.exsys.fix.message.*;
import com.exsys.fix.specification.*;
import com.exsys.fix.session.*;

public class FixRegulatoryGen  {

	
	private ArrayList logonMsgList = new ArrayList();
	private HashMap businessMsgs = new HashMap();
	private ArrayList businessKeys = new ArrayList();
	private ArrayList unknownMsgs = new ArrayList();
	
	

	private HashMap symbols = new HashMap();
    public  FixRegulatoryGen(String inFile,String outFile,String exchange)
    {



    try
    {


		FileInputStream inStream= new FileInputStream(inFile);

 	    FixMessage msg = FixSessionManager.getApplicationMessages(inStream,true);

 	     // then process in list
 	    while(msg != null)
	    {

			handleMessage(msg,true,exchange);

	   		msg = FixSessionManager.getApplicationMessages(inStream);

	    }
	    inStream.close();
	    
		FileInputStream outStream= new FileInputStream(outFile);

 	    msg = FixSessionManager.getApplicationMessages(outStream,true);

 	     // then process in list
 	    while(msg != null)
	    {
			
			handleMessage(msg,false,exchange);

	   		msg = FixSessionManager.getApplicationMessages(outStream);

	    }
	    outStream.close();	  
	    
	    // display info
	    displayMessages(false);
	    System.out.println(FixRegulatoryMsg.getCSVHeader());
	    //display csv
	    displayMessages(true);




    }
    catch( FileNotFoundException exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }
    catch( IOException exc )
    {
      exc.printStackTrace();
      System.exit(-1);
    }



	}

private void handleMessage(FixMessage msg,boolean toExchange, String exch)
{
			System.out.println("<BEGIN>");
			System.out.println(msg.toString());
			System.out.println("<END>");
	    	String msgType = msg.getMessageType();
	    	FixRegulatoryMsg rmsg = new FixRegulatoryMsg(msg,exch,toExchange);
	    	if(msgType.equals(FixConstants.FIX_MSGTYPE_LOGON)
	    	|| msgType.equals(FixConstants.FIX_MSGTYPE_LOGOUT))
	    	{
	    		System.out.println("Msg is logon");
	    		logonMsgList.add(rmsg);
	    	}
	    	else
	    	{	    		
	    		
	    		if(msgType.equals(FixConstants.FIX_MSGTYPE_ORDER)
	    		|| msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL)
	    		|| msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
	    		{
	    			
	    			
	    			String clOrderId = null;
	    			String origClOrderId = null;
	    			if(msgType.equals(FixConstants.FIX_MSGTYPE_ORDER))
	    			{
	    				FixOrder ord = (FixOrder)msg;
	    				clOrderId = ord.getClOrderID();
	    				origClOrderId = clOrderId;
	    			}
	    			else if(msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL))
	    			{
	    				FixCancel cxl = (FixCancel)msg;
	    				clOrderId = cxl.getClOrderID();
	    				origClOrderId = cxl.getOrigClOrderID();
	    			}
	    			else if(msgType.equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE))
	    			{
	    				FixCancelReplace cxr = (FixCancelReplace)msg;
	    				clOrderId = cxr.getClOrderID();
	    				origClOrderId = cxr.getOrigClOrderID();
	    				
	    			}
	    			
	    			if(clOrderId != null)
	    			{
	    				if(businessMsgs.containsKey(clOrderId))
	    				{
	    					ArrayList msgList = (ArrayList)businessMsgs.get(clOrderId);
	    					msgList.add(rmsg);
	    				}
	    				else
	    				{
	    					ArrayList msgs = new ArrayList();
	    					msgs.add(rmsg);
	    					businessKeys.add(clOrderId);
	    					businessMsgs.put(clOrderId,msgs);
	    				}
	    				
	    				// in case of cxl and cxr add the same msg in the other
	    				// list also
	    				if(!clOrderId.equals(origClOrderId))
	    				{
	    					ArrayList msgList = (ArrayList)businessMsgs.get(origClOrderId);
	    					if(msgList != null)
	    						msgList.add(rmsg);
	    					else
	    						System.out.println("ORIG ORDER NOT FOUND FOR CXL/CXR");
	    				}
	    			}
	    			else
	    				System.out.println("ClOrderId is NULL");
	    			
	    			
	    		}
	    		else if(msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT)
	    		|| msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT))
	    		{
	    			
	    			// first try to find the list based on the clorderid
	    			String clOrderId = null;
	    			if(msgType.equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT))
	    			{
	    				FixExecutionReport er = (FixExecutionReport)msg;
	    				clOrderId = er.getClOrderID();
	    			}
	    			else if(msgType.equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT))
	    			{
	    				FixCancelReject cr = (FixCancelReject)msg;
	    				clOrderId = cr.getClOrderID();	    				
	    			}
	    			
	    			if(clOrderId != null)
	    			{
	    				String key = findMessageKey(clOrderId);
	    				if(key != null)
	    				{
	    					ArrayList msgList = (ArrayList)businessMsgs.get(key);
	    					if(msgList != null)
	    						msgList.add(rmsg);	    						    					
	    				}
	    				else
	    				{	    					
	    					System.out.println("No Message Key Found. add to other list");
	    					unknownMsgs.add(rmsg);
	    				}
	    			}
	    			else
	    				System.out.println("ORIG CLORDERID from Response is NULL");
	    			
	    		}
	    	}
	
}

private String findMessageKey(String clOrderId)
{
	String key = null;
	
	Iterator keys = businessMsgs.keySet().iterator();
			
	while( keys.hasNext() )
	{
		String origKey = (String) keys.next();
        String last8 = origKey.substring(origKey.length()-8);
        String last5 = origKey.substring(origKey.length()-5);      
        if(origKey.equals(clOrderId) 
        || last8.equals(clOrderId)
        || last5.equals(clOrderId))
		{
			key = origKey;
			break;			
		}	
		
	}	
	
	return key;
}


 public static void main(String []args)
 {

		java.awt.datatransfer.Transferable t = java.awt.Toolkit.getDefaultToolkit()
							.getSystemClipboard()
							.getContents(new Object());
		if(t != null)
		{
			try
			{
				String str = (String)t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
				System.out.println("Input String is - "+ str);

				StringTokenizer strTok = new StringTokenizer(str);
				String inFileName = strTok.nextToken();
				String outFileName = strTok.nextToken();
				String exchange = strTok.nextToken();
				

				FixRegulatoryGen reg = null;
				if(str != null)
					reg = new FixRegulatoryGen(inFileName, outFileName,exchange);
				else
					System.out.println("No input file name");

				System.exit(0);
		    }
		    catch(Exception e)
		    {
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("No data selected");
		}

  }




  private void displayMessages(boolean csv)
  {
  		if(!csv)
  		System.out.println("Logon Messages");
  		for(int i=0; i<logonMsgList.size(); i++)
  		{
  			FixRegulatoryMsg rmsg = (FixRegulatoryMsg)logonMsgList.get(i);
  			if(csv)
  				System.out.println(rmsg.toCSV());
  			else
  				System.out.println(rmsg.toString());
  		}
  		if(!csv)
  			System.out.println("Unknown Messages");
  		for(int i=0; i<unknownMsgs.size(); i++)
  		{
  			FixRegulatoryMsg rmsg = (FixRegulatoryMsg)unknownMsgs.get(i);
  			System.out.println(rmsg.toString());  			
  		}  		

  			if(!csv)
			System.out.println("Business Messages");
			//Iterator keys = businessMsgs.keySet().iterator();
			Iterator keys = businessKeys.iterator();
			while( keys.hasNext() )
			{
				String key = (String) keys.next();
				if(!csv)
				{
					System.out.println("Key is " + key );
					System.out.println("Messages for key - " + key );
				}
				ArrayList msgs = (ArrayList)businessMsgs.get(key);
		  		for(int i=0; i<msgs.size(); i++)
		  		{
		  			FixRegulatoryMsg rmsg = (FixRegulatoryMsg)msgs.get(i);
		  			if(csv)
		  				System.out.println(rmsg.toCSV());
		  			else
		  				System.out.println(rmsg.toString());
		  		}												
			}

  }

  private static String append(String value, char pad, int width) {
	int length = value.length();
	if (length < width) {
		StringBuffer buffer = new StringBuffer(value);
		while (length < width) {
			buffer.append(pad);
			length++;
		}
		return buffer.toString();
	} else {
		return value;
	}
}
}
