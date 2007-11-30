package com.exsys.impact.test;

import com.exsys.impact.message.*;
import com.exsys.common.exceptions.*;


import java.io.*;



public class ImpactDataReader
{
   public static void main(String[] args)
   {
   	
   	LoginRequest logon = new LoginRequest();
	logon.setRequestSeqID(1);
	logon.setUserName("es_fix_ps");
	logon.setPassword("Starts123");
	logon.setGetMarketStatsUpdate("Y");
	logon.setMktDataBuffering("Y");
	logon.setGetMessageBundleMarker("N");
	logon.setGetImpliedOrders("Y");
	logon.setVersion("1.01.13");
	System.out.println("Length = " + logon.getBytes().length);
	logon.setMessageBodyLength(logon.getBytes().length-3);

   	System.out.println(new String(logon.getBytes()));
   	System.exit(0);
   	
      DataInputStream inStream = null;
      PrintStream outStream = null;
      try
      {
         // The file with sample data
         inStream = new DataInputStream(new FileInputStream("SampleData.dat"));

         // Text file to print out the message debug info, in addtion to the console
         outStream = new PrintStream(new FileOutputStream("SampleDataDebugOutput.txt"));
         while (true)
         {
            try
            {
               // Message factory read InputStream and do de-serialization
               String impMsg = getMessage(inStream);
               ImpactMessage msg = ImpactMessageFactory.createImpactMessage(impMsg.getBytes());

               // print debug info of the msg to screen
               System.out.println(msg.toString());

               // print debug info of the msg to file
               outStream.println(msg.toString());
            }
            catch (ImpactUnknownMsgError e)
            {
               // unknown message exception caught, the factory
               // handled reading of the message, log it and move on
               System.out.println(e.getMessage());
            }
         }
      }
      catch (FileNotFoundException e)
      {
         System.out.println("Unable to find SampleData.data.");
         e.printStackTrace();
      }
      catch (EOFException e)
      {
         System.out.println("End of sample data.");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      catch (Throwable e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            inStream.close();
            outStream.close();
         } catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }



   	public static String getMessage( DataInputStream inputStream )
         throws IOException
   	{

   		// read the message type and body length
         byte[] bytes = new byte[3];
         inputStream.readFully(bytes);

      short bodyLength;

         // 2nd and 3rd byte are used for body length
      bodyLength = Short.parseShort(new String(bytes,1,2));
     
   		// read the body with the length received
   		byte messageBodyBytes[] = new byte[bodyLength];
   		inputStream.readFully(messageBodyBytes, 0, bodyLength);

   		return new String(bytes) + new String(messageBodyBytes);

   	}

}
