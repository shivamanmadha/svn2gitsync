package com.exsys.impact.session;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
//import com.exsys.impact.message.*;
import com.exsys.impact.mdf.message.*;
import com.exsys.service.*;

/**
 * @author kreddy
 *
 * This class provides fix log file management and
 * sequence number management for both fix client
 * and fix server. Also provides functions to support
 *  resend message processing.
 */
public class ImpactSessionManager {
	private java.lang.String senderCompID;
	private FileOutputStream inWriter = null;
	private FileOutputStream outWriter = null;
	private FileOutputStream ignoreWriter = null;
	private boolean addNL = false;

	private String logDirectory = null;

	private Object inFileLock = new Object();
	private Object outFileLock = new Object();

	private int inSeq = 0;
	private int outSeq = 0;
	public final static String FIX_MESSAGE_DELIM = ";";
	public final static char NL = '\n';




/**
 * ImpactSessionManager constructor
 * @param senderID
 */
public ImpactSessionManager(String senderID)
{
	senderCompID = senderID;
}

/**
 * @return
 */
public int getInSeq()
{
	return inSeq;
}

/**
 * @param stream
 * @return
 */
public int getLastSequenceNumber(FileInputStream stream)
{
	return 0;
	/*
	int charRead;
	StringBuffer seq = new StringBuffer();
	StringBuffer length = new StringBuffer();

	try
	{
		while( (charRead=stream.read()) != -1 )
		{
			if(addNL && charRead == NL )
				continue;

			seq.setLength(0);
			length.setLength(0);
			seq.append((char)charRead);
			while( (charRead=stream.read()) != -1 )
			{

				if( (char)charRead != ';' )
				{
					seq.append( (char)charRead );
				}
				else
				  break;
			}
			//Logger.debug("Seq is "+ seq.toString() );

			while( (charRead=stream.read()) != -1 )
			{
				if( (char)charRead != ';' )
				{
					length.append( (char)charRead );
				}
				else
				  break;
			}
			//Logger.debug("Length is "+length );

			stream.skip( Integer.parseInt(length.toString()) );
		}


	}
	catch( Exception e )
	{
		e.printStackTrace();
	}


	return Integer.parseInt((seq.toString()).trim());
	*/
}

/**
 * @return
 */
public int getOutSeq()
{
	return outSeq;
}

/**
 * @return
 */
public java.lang.String getSenderCompID() {
	return senderCompID;
}
/**
 * method to reset sequence numbers
 */
public void resetSequenceNumbers()
{
	try
	{
	// close the current in file
	if( inWriter != null )
	{
		inWriter.close();
	}

		String strCurrentFile ="IMPACT-"+senderCompID+"-IN.log";
		if(logDirectory != null)
				strCurrentFile = logDirectory + strCurrentFile;

		File currentFile = new File(strCurrentFile);


		File newFile = getNextAvailableInLogFile(senderCompID);
		currentFile.renameTo(newFile);
		inSeq = 0;



	// close the current out file
	if( outWriter != null )
	{
		outWriter.close();
	}
		strCurrentFile ="IMPACT-"+senderCompID+"-OUT.log";
		if(logDirectory != null)
				strCurrentFile = logDirectory + strCurrentFile;

		currentFile = new File(strCurrentFile);
		newFile = getNextAvailableOutLogFile(senderCompID);
		currentFile.renameTo(newFile);
		outSeq = 0;

	}
	catch (Exception e )
	{
		e.printStackTrace();
	}

}

/**
 * @param id
 * @return
 */
private File getNextAvailableInLogFile(String id)
{

	String fileName_prefix = "IMPACT-"+id+"-IN";
	File lookForFile = getNextAvailableLogFile(fileName_prefix);
	return lookForFile;
}

/**
 * @param id
 * @return
 */
private File getNextAvailableOutLogFile(String id)
{
	String fileName_prefix = "IMPACT-"+id+"-OUT";
	File lookForFile = getNextAvailableLogFile(fileName_prefix);
	return lookForFile;
}

/**
 * @param prefix
 * @return
 */
private File getNextAvailableLogFile(String prefix)
{

	boolean found = false;
	int suffix = 1;
	File lookForFile = null;
	String lookFor = null;
	try
	{
	while(!found)
	{
		lookFor = prefix+"_"+suffix+".log";
		if(logDirectory != null)
				lookFor = logDirectory + lookFor;
	    lookForFile = new File(lookFor);
		Logger.debug("Looking for for " + lookFor);
		if( !lookForFile.exists() )
		{
			found = true;
			break;
		}
		else
		{
			suffix++;
		}
	}
	}
	catch (Exception e )
	{
		e.printStackTrace();
	}

	return lookForFile;

}



/**
 * method to initialize sequence numbers
 */
public void initializeSequenceNumbers()
{
	// open the in message file

	try
	{
		String fileName = "IMPACT-"+senderCompID+"-IN.log";

		if(logDirectory != null)
				fileName = logDirectory + fileName;

		File file = new File(fileName);
		if( file.exists() )
		{
			//Logger.debug("IN File exists");
			// file exists, open the stream
			FileInputStream inStream = new FileInputStream( file );
			inSeq = getLastSequenceNumber( inStream );
			Logger.debug("Returned InSeq is :" + inSeq);
			inStream.close();

		}
		else
		{
			//Logger.debug("IN File does not exist");
			inSeq = 0;
		}

		String outFileName = "IMPACT-"+senderCompID+"-OUT.log";
		if(logDirectory != null)
				outFileName = logDirectory + outFileName;
		File outFile = new File(outFileName);
		if( outFile.exists() )
		{
			Logger.debug("Out File exists");
			// file exists, open the stream
			FileInputStream outStream = new FileInputStream( outFile );
			outSeq = getLastSequenceNumber( outStream );
			Logger.debug("Returned OutSeq is :" + outSeq);
			outStream.close();

		}
		else
		{
			//Logger.debug("OUT File does not exist");
			outSeq = 0;
		}

	}
	catch( Exception e )
	{
		e.printStackTrace();
	}


}

/**
 * method to log an inbound fix message
 * @param seq
 * @param msg
 */
public void logInImpactMessage(int seq, byte[] msg)
{
	synchronized(inFileLock)
	{
	try
	{
		if( inWriter == null )
		{
			String inFileName = "IMPACT-"+senderCompID+"-IN.log";
			if(logDirectory != null)
				inFileName = logDirectory + inFileName;

			inWriter = new FileOutputStream( inFileName, true );
		}


			/*
			inWriter.write( (String.valueOf(seq)+
						FIX_MESSAGE_DELIM +
						String.valueOf(msg.length) +
						FIX_MESSAGE_DELIM ).getBytes() );
			*/

			inWriter.write( msg );
			if(addNL)
			{
				inWriter.write('\n');
			}


	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

	}

}

/**
 * method to log ignored fix messages
 * @param seq
 * @param msg
 */
public void logIgnoreImpactMessage(int seq, byte[] msg)
{

	try
	{
		if( ignoreWriter == null )
		{
			String ignFileName = "IMPACT-"+senderCompID+"-IGNORE.log";
			if(logDirectory != null)
				ignFileName = logDirectory + ignFileName;

			ignoreWriter = new FileOutputStream( ignFileName, true );
		}

/*
		ignoreWriter.write( (String.valueOf(seq)+
						FIX_MESSAGE_DELIM +
						String.valueOf(msg.length) +
						FIX_MESSAGE_DELIM).getBytes() );
*/

		ignoreWriter.write( msg );

		if(addNL)
		{
			ignoreWriter.write('\n');
		}

	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
}

/**
 * method to log outbound fix message
 * @param seq
 * @param msg
 */
public void logOutImpactMessage(int seq, byte[] msg)
{

	synchronized(outFileLock)
	{
	try
	{
		if( outWriter == null )
		{
			String outFileName = "IMPACT-"+senderCompID+"-OUT.log";
			if(logDirectory != null)
				outFileName = logDirectory + outFileName;

			outWriter = new FileOutputStream( outFileName, true );
		}
/*
		outWriter.write( (String.valueOf(seq)+
						FIX_MESSAGE_DELIM +
						String.valueOf(msg.length) +
						FIX_MESSAGE_DELIM ).getBytes() );
*/
		outWriter.write( msg );

		if(addNL)
		{
			outWriter.write('\n');
		}


	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

	}

}

/**
 * method to close all resources
 */
public void sessionClosed()
{
	try
	{
		inWriter.close();
		outWriter.close();
		if(ignoreWriter != null )
		{
			ignoreWriter.close();
		}
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
}

/**
 * @param newSenderCompID
 */
public void setSenderCompID(java.lang.String newSenderCompID) {
	senderCompID = newSenderCompID;
}

/**
 * @param newLogDirectory
 */
public void setLogDirectory(java.lang.String newLogDirectory) {
	logDirectory = newLogDirectory;
}

/**
 * @return
 */
public String getLogDirectory() {
	return logDirectory;
}






/**
 * method to get application messages list
 * @param file
 * @return
 */
public static ArrayList getApplicationMessagesList(String file) throws IOException {

	int charRead;
	StringBuffer seq = new StringBuffer();
	StringBuffer length = new StringBuffer();
	String returnMessage=null;
	ArrayList list=new ArrayList(10);
	//synchronized(outFileLock)
	//{
	try
	{
		String inFileName = file;

		FileInputStream stream= new FileInputStream(inFileName);
		Logger.debug("File is " + inFileName);
		byte[] msg = getMessageBytes(stream);
		while(msg != null)
		{
			list.add(msg);

		}
		stream.close();


	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

	//}

	return list;


}
public static byte[] getApplicationMessages(FileInputStream stream) throws IOException {
	return getApplicationMessages(stream,false);
}

	public static byte[] getMessageBytes( FileInputStream inputStream )
      throws IOException
	{
		byte[] combined = null;
		// read the message type and body length
      byte[] bytes = new byte[3];
     
     while( inputStream.read(bytes) != -1 )
     {

      // instantiate an empty object
      byte messageType = bytes[0];
      short bodyLength;
      if ((char)messageType == RawMessageFactory.DebugRequestType)
      {
         // treat debug request differently so that it works when it is sent through
         // telnet or F5 because it is hard to manipulate the ASCII string to come up
         // with a binary short value of 4, just use the hardcoded FIXED LENGTH
         bodyLength = 4;
      }
      else
      {
         // 2nd and 3rd byte are used for body length
         bodyLength = ByteBuffer.wrap(bytes, 1, 2).getShort();
      }

		// read the body with the length received
		byte[] messageBodyBytes = new byte[bodyLength];
		inputStream.read(messageBodyBytes);
		
	if( messageType == RawMessageFactory.LoginResponseType
		|| messageType == RawMessageFactory.LogoutRequestType
		|| messageType == RawMessageFactory.LoginResponseType
		|| messageType == RawMessageFactory.LogoutResponseType
		|| messageType == RawMessageFactory.DebugResponseType
		|| messageType == RawMessageFactory.DebugResponseType
		)
	{
		continue;
	}

		combined = new byte[3+bodyLength];
		System.arraycopy(bytes,0,combined,0,3);
		System.arraycopy(messageBodyBytes,0,combined,3,bodyLength);
		break;
		
     }
		return combined;
	}

/**
 * method to get one message at a time
 * @param file
 * @return
 */
public static byte[] getApplicationMessages(FileInputStream stream, boolean inclLogon)  throws IOException
{
	return getMessageBytes(stream);
}



}
