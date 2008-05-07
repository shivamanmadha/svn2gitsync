package com.exsys.impact.mdf.client.examples;

import com.exsys.impact.mdf.message.RawMessageFactory;
import com.exsys.impact.mdf.message.MDMessage;
import com.exsys.impact.mdf.message.UnknownMessageException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * <code>SimpleClientSocketReader</code> processes the inbound messages from the
 * price feed server and print the debug info out to the console.
 *
 * THE CLASSES USED HERE, INCLUDING THE MESSSAGE CLASSES ARE EXAMPLE CODES ONLY.
 * THEY WON'T BE SUPPORTED AS LIBRARY.
 *
 * @author David Chen
 * @since 12/28/2006
 */

public class SimpleClientSocketReader implements Runnable
{
   private DataInputStream _inputStream = null;
   private LinkedList _simpleMsgQueue = null;

   public SimpleClientSocketReader(DataInputStream inStream)
   {
      _inputStream = inStream;
      _simpleMsgQueue = new LinkedList();
   }

   public void run()
   {
      try
      {
         while (true)
         {
            try
            {
               // Wait for message from the feed server
               MDMessage msg = RawMessageFactory.getObject(_inputStream);

               // make sure the queue is locked before we access it
               synchronized (_simpleMsgQueue)
               {
                  // add message to the queue
                  _simpleMsgQueue.addLast(msg);

                  // notify the consumer that is waiting
                  _simpleMsgQueue.notify();
               }

            }
            catch (UnknownMessageException e)
            {
               // unknown message exception caught, the factory
               // handled reading of the message, log it and move on
               System.out.println(e.getMessage());
            }
         }

      }
      catch (IOException e)
      {
         System.out.println("SimpleClientSocketReader.run: IOException caught");
         e.printStackTrace();
      }
      catch (Throwable e)
      {
         System.out.println("SimpleClientSocketReader.run: Throwable caught");
         e.printStackTrace();
      }

      System.out.println("SimpleClientSocketReader.run: Exit socket reader!!");
   }


   public MDMessage getNextMessage()
   {
      MDMessage mdMsg;

      synchronized (_simpleMsgQueue)
      {
         while (_simpleMsgQueue.size()==0)
         {
            try
            {
              _simpleMsgQueue.wait();
            }
            catch (InterruptedException e)
            {
               e.printStackTrace();
            }
         }
         mdMsg = (MDMessage)_simpleMsgQueue.removeFirst();
      }

      return mdMsg;
   }
}
