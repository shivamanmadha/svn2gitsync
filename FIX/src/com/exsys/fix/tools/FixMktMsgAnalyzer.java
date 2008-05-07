package com.exsys.fix.tools;


import java.io.*;
import java.util.*;

import com.exsys.fix.message.*;
import com.exsys.fix.specification.*;
import com.exsys.fix.session.*;

public class FixMktMsgAnalyzer  {

	private HashMap symbols = new HashMap();
    public  FixMktMsgAnalyzer(String filename,String symbol)
    {
        	if ( filename != null && filename.length() >0)
        	{


    try
    {
    	
 	     
		FileInputStream stream= new FileInputStream(filename); 	     
 	     
 	    FixMessage msg = FixSessionManager.getApplicationMessages(stream);
		Date prevSendingTime = null;
		Date currSendingTime = null;

 	     // then process in list
 	    while(msg != null)
	    {
	    	String msgType = msg.getMessageType();
	    	if(msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH))
	    	{
	    		if(symbol != null && symbol.equals("stats"))
	    		{
	    			collectStatsMarketDataFullRefresh((FixMarketDataFullRefresh)msg);
	    		}
	    		else
	    		{
	    			processMarketDataFullRefresh((FixMarketDataFullRefresh)msg,symbol);
	    		}
	    		
	    	}
	    	else if ( msgType.equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH) )
	    	{
	    		if(prevSendingTime == null)
	    		{
	    			prevSendingTime = msg.getSendingTime();
	    			currSendingTime = msg.getSendingTime();
	    			System.out.println("delay is zero");
	    		}
	    		else
	    		{
	    			prevSendingTime = currSendingTime;
	    			currSendingTime = msg.getSendingTime();
	    			System.out.println("delay is " + (currSendingTime.getTime() - prevSendingTime.getTime()));
	    		}
	    		
	    		if(symbol != null && symbol.equals("stats"))
	    		{
	    			collectStatsMarketDataIncrementalRefresh((FixMarketDataIncrementalRefresh)msg);
	    		}
	    		else
	    		{
	    			processMarketDataIncrementalRefresh((FixMarketDataIncrementalRefresh)msg,symbol);
	    		}	    		
	    	}
	    	
	    	/*
			try
			{
				if(isProcessed)
				{
					Logger.debug("FIX SEQ # :" + msg.getMsgSeqNumAsString());
					
					Thread.sleep(3000);
				}
			}
			catch(Throwable e)
			{
			}	    	
	   		*/ 	
	   		
	   		msg = FixSessionManager.getApplicationMessages(stream);
	    	
	    }
	    stream.close();
	    
		if(symbol != null && symbol.equals("stats"))
		{
			displayStats();
		}
      


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
			else
			{
				System.out.println("No Fix file name");
			}

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
				String fileName = strTok.nextToken();
				String symbol = strTok.nextToken();
				if(symbol.trim().equals("all"))
					symbol = null;
				FixMktMsgAnalyzer lmv = null;
				if(str != null)
					lmv = new FixMktMsgAnalyzer(fileName, symbol);
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
  
  
  public boolean processMarketDataFullRefresh(FixMarketDataFullRefresh msg, String traceSymbol)
{
	
    String symbol = msg.getSymbol();
    if(traceSymbol != null && !symbol.equals(traceSymbol)) return false;
    String str = "FR: ";
    str += "FIX SEQ # :" + msg.getMsgSeqNumAsString();
    str += " Symbol =" + symbol;
    str += " NumEntries = " + msg.getNoMDEntries();
    System.out.println(str);
    if(msg.getNoMDEntries() > 0 )
    {
    	
   	 	// add details
   	 	String key = "268";
  		ArrayList rgArray = msg.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			
  			for (int i = 0; i < size; i++) {

  				FixRGNoMDEntries_W rg = (FixRGNoMDEntries_W)rgArray.get(i);
  				String entryType = rg.getMDEntryType();
  				if(entryType.equals("0")||
  				entryType.equals("1")||
  				entryType.equals("2"))
  				{
  				String info = " ";
  				info += " OID="+rg.getOrderID();
  				info += " SID="+rg.getSequenceIdAsString();
  				info += " OrdSt = "+rg.getOrderState();
				info += "(";
  				info += FixLookup.lookupTagValue("9175",rg.getOrderState());
		  		info += ")";  				  				
  				info += " MDEType = "+rg.getMDEntryType();
				info += "(";
  				info += FixLookup.lookupTagValue("269",rg.getMDEntryType());
		  		info += ")";  				
  				info += " MDEntry ="+rg.getMDEntrySizeAsString();
  				info += " @ "+rg.getMDEntryPxAsString();
  				System.out.println("\t"+info);
  				
  				}
   			}
   			
  		}

    }
	
	return true;
}

  public boolean processMarketDataIncrementalRefresh(FixMarketDataIncrementalRefresh msg, String traceSymbol)
  {
  	String str = "IR: ";
    str += "FIX SEQ # :" + msg.getMsgSeqNumAsString() + " Time = " + msg.getSendingTimeAsString();
    str += " NumEntries = " + msg.getNoMDEntries();
    System.out.println(str);
    
    if(msg.getNoMDEntries() > 0 )    
    {
    	
   	 	// add details
   	 	String key = "268";
  		ArrayList rgArray = msg.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {

  				FixRGNoMDEntries_X rg = (FixRGNoMDEntries_X)rgArray.get(i);
  				
  				
  				    if(traceSymbol != null && !rg.getSymbol().equals(traceSymbol)) continue;
  				String entryType = rg.getMDEntryType();
  				if(entryType.equals("0")||
  				entryType.equals("1")||
  				entryType.equals("2")||
  				entryType.equals("U"))
  				{  				    
  				    
  				String info = "Sym =" + rg.getSymbol();
  				
  				info += " OID="+rg.getOrderID();
  				info += " SID="+rg.getSequenceIdAsString();
  				info += " OrdSt = "+rg.getOrderState();  				
				info += "(";
  				info += FixLookup.lookupTagValue("9175",rg.getOrderState());
		  		info += ")";  				  				
  				    
  				info += " MDEType = "+entryType;
				info += "(";
  				info += FixLookup.lookupTagValue("269",entryType);
		  		info += ")";  				  				
  				info += " TotVol ="+rg.getTotalVolumeTradedAsString();
  				info += " UpdAct ="+rg.getMDUpdateAction();
				info += "(";
  				info += FixLookup.lookupTagValue("279",rg.getMDUpdateAction());
		  		info += ")"; 		  		 			
  				info += " MDEntry ="+rg.getMDEntrySizeAsString();
  				info += " @ "+rg.getMDEntryPxAsString();
  				System.out.println("\t"+info);
  				}
		  			
  			}	  			
  		}

    }

	return true;
   }

public boolean collectStatsMarketDataFullRefresh(FixMarketDataFullRefresh msg)
{
	
	
	return true;
}

  public boolean collectStatsMarketDataIncrementalRefresh(FixMarketDataIncrementalRefresh msg)
  {
  	/*
  	String str = "IR: ";
    str += "FIX SEQ # :" + msg.getMsgSeqNumAsString();
    str += " NumEntries = " + msg.getNoMDEntries();
    System.out.println(str);
    */
    
    
    
    
    if(msg.getNoMDEntries() > 0 )    
    {
    	
   	 	// add details
   	 	String key = "268";
  		ArrayList rgArray = msg.getRepeatingGroups(key);
  		if( rgArray != null )
  		{
  			int size = rgArray.size();
  			//mListModel.addElement("Size is " + size );
  			for (int i = 0; i < size; i++) {

  				FixRGNoMDEntries_X rg = (FixRGNoMDEntries_X)rgArray.get(i);
  				  				  				
  				String symbol = rg.getSymbol();
  				String updateAction = rg.getMDUpdateAction();
  				String entryType = rg.getMDEntryType();
  				String orderState = rg.getOrderState();
  				
  				if(symbols.containsKey(symbol))
  				{
  					HashMap symbolMap = (HashMap)symbols.get(symbol);
  					if(symbolMap.containsKey(updateAction))
  					{
  						HashMap updMap = (HashMap)symbolMap.get(updateAction);
  						if(updMap.containsKey(entryType))
  						{
  							HashMap entryMap = (HashMap)updMap.get(entryType);
  							if(entryMap.containsKey(orderState))
  							{
  								Integer counter = (Integer)entryMap.get(orderState);
  								entryMap.put(orderState,new Integer(counter.intValue()+1));  								  								
  							}
  							else
  							{
  								entryMap.put(orderState,new Integer(1));
  							}
  						}
  						else
  						{
  							HashMap entryMap = new HashMap();
  							entryMap.put(orderState,new Integer(1));
  							updMap.put(entryType,entryMap);
  						}
  					}
  					else
  					{
  						HashMap updateMap = new HashMap();
  						HashMap entryMap = new HashMap();
  						entryMap.put(orderState,new Integer(1));
  						updateMap.put(entryType,entryMap);
  						symbolMap.put(updateAction,updateMap);
  					}
  				}
  				else
  				{
  						HashMap symbolMap = new HashMap();  						
  						HashMap updateMap = new HashMap();
  						HashMap entryMap = new HashMap();
  						entryMap.put(orderState,new Integer(1));
  						updateMap.put(entryType,entryMap);
  						symbolMap.put(updateAction,updateMap);  					
  						symbols.put(symbol,symbolMap);
  				}
  				
  				

		  			
  			}	  			
  		}

    }

	return true;
   }
  
  private void displayStats()
  {
  	
			Iterator keys = symbols.keySet().iterator();
			
			while( keys.hasNext() )
			{
				String symbol = (String) keys.next();
				System.out.println("Symbol = " + symbol);
				HashMap updateMap = (HashMap)symbols.get(symbol);
				Iterator upds = updateMap.keySet().iterator();
				while(upds.hasNext())
				{
					String updateAction = (String)upds.next();
					String out = "\tUpdateAction = "+ updateAction;
					out += "(";
  					out += FixLookup.lookupTagValue("279",updateAction);
		  			out += ")"; 					
					
					System.out.println(out);
					HashMap entryMap = (HashMap)updateMap.get(updateAction);
					Iterator entries = entryMap.keySet().iterator();
					while(entries.hasNext())
					{
						String entryType = (String)entries.next();
						
						String out1 = "\t\tEntryType = "+ entryType;
						out1 += "(";
  						out1 += FixLookup.lookupTagValue("269",entryType);
		  				out1 += ")";						
						
						System.out.println(out1);
						HashMap osMap = (HashMap)entryMap.get(entryType);
						Iterator osts = osMap.keySet().iterator();
						while(osts.hasNext())
						{
							String ost = (String)osts.next();
							String out2 = "\t\t\tOrderState = " +ost;
							out2 += "(";
  							out2 += (ost != null)?FixLookup.lookupTagValue("9175",ost):ost;
		  					out2 += ")";							
							System.out.println(out2 + " count = " +(Integer)osMap.get(ost));
						}
						
					}
					
					
					
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
