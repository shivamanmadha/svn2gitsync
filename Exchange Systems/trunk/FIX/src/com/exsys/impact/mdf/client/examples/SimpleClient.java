package com.exsys.impact.mdf.client.examples;

import com.theice.mdf.message.MessageUtil;
import com.theice.mdf.message.request.*;

import java.net.Socket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataInputStream;

/**
 * <code>SimpleClient</code> is a small program that demonstrates how to connect to the price
 * feed server, sending requests and reading the response/notification messages. This class handles
 * the connection and sending requests, while a separate socket reader thread is started to process
 * the inbound messages.
 *
 * THE CLASSES USED HERE, INCLUDING THE MESSSAGE CLASSES ARE EXAMPLE CODES ONLY.
 * THEY WON'T BE SUPPORTED AS LIBRARY.
 *
 * @author David Chen
 * @since 12/28/2006
 */

public class SimpleClient
{
   private static int RequestSeqID = 0;

   static public void main(String[] args)
   {
      try
      {
         // Connect to the feed server
         System.out.println("SimpleClient.main: Connecting to feed server - " + SimpleClientConfigurator.getServerAddress()
                   + ":" + SimpleClientConfigurator.getServerPort());

         Socket clientSoc = new Socket(SimpleClientConfigurator.getServerAddress(),
                                       SimpleClientConfigurator.getServerPort());
         clientSoc.setTcpNoDelay(true);
         InputStream inStream = clientSoc.getInputStream();
         System.out.println("SimpleClient.main: Connected to server");

         // Start socket reader thread for processing response/streamed data from server
         SimpleClientSocketReader reader = new SimpleClientSocketReader(new DataInputStream(inStream));
         Thread readerThread = new Thread(reader, "ReaderThread");
         readerThread.start();

         // Start message consumer thread for processing messages
         SimpleClientMessageConsumer consumer = new SimpleClientMessageConsumer(reader);
         Thread consumerThread = new Thread(consumer, "ConsumerThread");
         consumerThread.start();

         OutputStream outStream = clientSoc.getOutputStream();

         // send debug request
         System.out.println("SimpleClient.main: send debug request");
         sendDebugRequest(outStream);

         // send login request
         System.out.println("SimpleClient.main: send login request");
         login(outStream);

         // send product definition requests
         System.out.println("SimpleClient.main: send product definition requests");
         requestProductDefintions(outStream);

         // send market data requests
         System.out.println("SimpleClient.main: send market data requests");
         requestMarketDataByMarkeType(outStream);

         //requestMarketDataByID(outStream);

         //requestLogout(outStream);

         readerThread.join();

      }
      catch (IOException e)
      {
         System.out.println("SimpleClient.main: IOException caught");
         e.printStackTrace();
      }
      catch (InterruptedException e)
      {
         System.out.println("SimpleClient.main: InterruptedException caught");
         e.printStackTrace();
      }
   }

   /**
    * Request Logout
    * @param outStream
    * @throws IOException
    */
   private static void requestLogout(OutputStream outStream)
      throws IOException
   {
      LogoutRequest logoutReq = new LogoutRequest();
      logoutReq.RequestSeqID = getRequestSeqID();
      outStream.write(logoutReq.serialize().array());
   }

   /**
    * Request market data by market type
    * @param outStream
    * @throws IOException
    */
   private static void requestMarketDataByMarkeType(OutputStream outStream)
      throws IOException
   {
      int[] marketTypes = SimpleClientConfigurator.getMarketTypes();

      // iterate through the market types that we are interested in
      // and send out the market data request
      for (int i=0; i<marketTypes.length; i++)
      {
         RequestFeedByMarketType mdReq = new RequestFeedByMarketType();
         mdReq.MarketType = (short) marketTypes[i];
         mdReq.RequestSeqID = getRequestSeqID();
         mdReq.MarketDepth = 0;
         outStream.write(mdReq.serialize().array());
      }
   }

   /**
    * Request market data by market ID
    * @param outStream
    * @throws IOException
    */
   private static void requestMarketDataByID(OutputStream outStream)
      throws IOException
   {
      RequestFeedByMarketID mdReq = new RequestFeedByMarketID();
      mdReq.MarketIDs = MessageUtil.toRawChars("500004", mdReq.MarketIDs.length);
      mdReq.RequestSeqID = getRequestSeqID();
      mdReq.MarketDepth = 0;
      outStream.write(mdReq.serialize().array());
   }

   /**
    * Send a debug request
    * @param outStream
    * @throws IOException
    */
   private static void sendDebugRequest(OutputStream outStream)
      throws IOException
   {
      DebugRequest debugRequest = new DebugRequest();
      debugRequest.RequestSeqID = getRequestSeqID();
      outStream.write(debugRequest.serialize().array());
   }

   /**
    * Request for the product defintions of the market types that we are interested in
    * @param outStream
    * @throws IOException
    */
   private static void requestProductDefintions(OutputStream outStream)
      throws IOException
   {
      int[] marketTypes = SimpleClientConfigurator.getMarketTypes();

      // iterate through the market types that we are interested in
      // and send out the market data request
      for (int i=0; i<marketTypes.length; i++)
      {
         ProductDefinitionRequest pdRequest = new ProductDefinitionRequest();
         pdRequest.MarketType = (short) marketTypes[i];
         pdRequest.RequestSeqID = getRequestSeqID();
         outStream.write(pdRequest.serialize().array());
      }
   }

   /**
    * Send login request
    *
    * @param outStream
    * @throws IOException
    */
   private static void login(OutputStream outStream)
      throws IOException
   {
      LoginRequest loginRequest = new LoginRequest();

      loginRequest.UserName
         = MessageUtil.toRawChars(SimpleClientConfigurator.getUserName(), loginRequest.UserName.length);

      loginRequest.Password
         = MessageUtil.toRawChars(SimpleClientConfigurator.getPassword(), loginRequest.Password.length);

      loginRequest.RequestSeqID = getRequestSeqID();

      loginRequest.MktDataBuffering = SimpleClientConfigurator.isMktDataBuffering() ? 'Y' : 'N';

      loginRequest.GetMarketStats = SimpleClientConfigurator.getMarketStats() ? 'Y' : 'N';

      loginRequest.GetMessageBundleMarker = SimpleClientConfigurator.getMessageBundleMarker() ? 'Y' : 'N';

      loginRequest.GetImpliedOrders = SimpleClientConfigurator.getImpliedOrders() ? 'Y' : 'N';

      loginRequest.Version
         = MessageUtil.toRawChars(SimpleClientConfigurator.getClientVersion(), loginRequest.Version.length);

      System.out.println("LOGIN MESSAGE IS  = >"+ loginRequest.serialize().toString()+"<");
      byte[] bytes = loginRequest.serialize().array();
      for(int i=0;i<bytes.length;i++)
      {
		  System.out.print((char)bytes[i]);
	  }
	  System.out.println("");

      outStream.write(loginRequest.serialize().array());
   }

   /**
    * Increment and return the request sequence ID
    * @return the request sequence ID
    */
   private static int getRequestSeqID()
   {
      return RequestSeqID++;
   }



}
