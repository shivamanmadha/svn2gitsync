package com.exsys.impact.mdf.client.examples;

import com.theice.mdf.message.MDMessage;

/**
 * <code>SimpleClientMessageConsumer</code> gets messages from the reader and
 * print them to console.
 *
 * THE CLASSES USED HERE, INCLUDING THE MESSSAGE CLASSES ARE EXAMPLE CODES ONLY.
 * THEY WON'T BE SUPPORTED AS LIBRARY.
 *
 * @author David Chen
 * @since 12/28/2006
 */

public class SimpleClientMessageConsumer implements Runnable
{
   SimpleClientSocketReader _socketReader;

   public SimpleClientMessageConsumer(SimpleClientSocketReader socketReader)
   {
      _socketReader = socketReader;
   }

   public void run()
   {
      while (true)
      {
         MDMessage msg = _socketReader.getNextMessage();

         // Print out every message
         System.out.println("Inbound Msg: " + msg.toString());
      }
   }
}
