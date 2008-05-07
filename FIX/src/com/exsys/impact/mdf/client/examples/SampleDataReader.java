package com.exsys.impact.mdf.client.examples;

import com.theice.mdf.message.RawMessageFactory;
import com.theice.mdf.message.MDMessage;
import com.theice.mdf.message.UnknownMessageException;

import java.io.*;

/**
 * <code>SampleDataReader</code> class reads the sample messages that have been serialized
 * into a binary data file (SampleData.data). It shows them in readable debug format and
 * also saves them into a text file (SampleDataDebugOutput.txt).
 *
 * The purpose of the program is to demonstrate how messages could be de-serialized
 * through RawMessageFactory, which behind the scene calls the de-serialization
 * routine of each message class.
 *
 * THE CLASSES USED HERE, INCLUDING THE MESSSAGE CLASSES ARE EXAMPLE CODES ONLY.
 * THEY WON'T BE SUPPORTED AS LIBRARY.
 *
 * @author David Chen
 * @since 12/28/2006
 */

public class SampleDataReader
{
   public static void main(String[] args)
   {
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
               MDMessage msg = RawMessageFactory.getObject(inStream);

               // print debug info of the msg to screen
               System.out.println(msg.toString());

               // print debug info of the msg to file
               outStream.println(msg.toString());
            }
            catch (UnknownMessageException e)
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
}
