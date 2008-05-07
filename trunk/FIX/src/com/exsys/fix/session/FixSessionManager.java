package com.exsys.fix.session;

import java.io.*;
import java.util.*;
import com.exsys.fix.message.*;
import com.exsys.service.*;

/**
 * @author kreddy
 *
 * This class provides fix log file management and
 * sequence number management for both fix client
 * and fix server. Also provides functions to support
 *  resend message processing.
 */
public class FixSessionManager {
	private java.lang.String senderCompID;
	private FileOutputStream inWriter = null;
	private FileOutputStream outWriter = null;
	private FileOutputStream ignoreWriter = null;
	private boolean addNL = true;
	
	private String logDirectory = null;
		
	private Object inFileLock = new Object();
	private Object outFileLock = new Object();	
		
	private int inSeq = 0;
	private int outSeq = 0;
	public final static String FIX_MESSAGE_DELIM = ";";	
	public final static char NL = '\n';
	

	

/**
 * FixSessionManager constructor
 * @param senderID
 */
public FixSessionManager(String senderID) 
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

		String strCurrentFile ="FIX-"+senderCompID+"-IN.log";
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
		strCurrentFile ="FIX-"+senderCompID+"-OUT.log";
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
	
	String fileName_prefix = "FIX-"+id+"-IN";
	File lookForFile = getNextAvailableLogFile(fileName_prefix);
	return lookForFile;		
}

/**
 * @param id
 * @return
 */
private File getNextAvailableOutLogFile(String id)
{
	String fileName_prefix = "FIX-"+id+"-OUT";
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
		String fileName = "FIX-"+senderCompID+"-IN.log";

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
		
		String outFileName = "FIX-"+senderCompID+"-OUT.log";
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
public void logInFixMessage(int seq, byte[] msg) 
{
	synchronized(inFileLock)
	{
	try
	{
		if( inWriter == null )
		{
			String inFileName = "FIX-"+senderCompID+"-IN.log";
			if(logDirectory != null)
				inFileName = logDirectory + inFileName;
			
			inWriter = new FileOutputStream( inFileName, true );		
		}


			inWriter.write( (String.valueOf(seq)+
						FIX_MESSAGE_DELIM +
						String.valueOf(msg.length) +
						FIX_MESSAGE_DELIM ).getBytes() );
		
		
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
public void logIgnoreFixMessage(int seq, byte[] msg) 
{

	try
	{
		if( ignoreWriter == null )
		{
			String ignFileName = "FIX-"+senderCompID+"-IGNORE.log";
			if(logDirectory != null)
				ignFileName = logDirectory + ignFileName;
			
			ignoreWriter = new FileOutputStream( ignFileName, true );		
		}

		ignoreWriter.write( (String.valueOf(seq)+
						FIX_MESSAGE_DELIM +
						String.valueOf(msg.length) +
						FIX_MESSAGE_DELIM).getBytes() );


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
public void logOutFixMessage(int seq, byte[] msg) 
{

	synchronized(outFileLock)
	{
	try
	{
		if( outWriter == null )
		{
			String outFileName = "FIX-"+senderCompID+"-OUT.log";
			if(logDirectory != null)
				outFileName = logDirectory + outFileName;
			
			outWriter = new FileOutputStream( outFileName, true );		
		}

		outWriter.write( (String.valueOf(seq)+
						FIX_MESSAGE_DELIM +
						String.valueOf(msg.length) +
						FIX_MESSAGE_DELIM ).getBytes() );

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
 * method to get resend messages
 * @param seqNum
 * @param endSeq
 * @return
 */
public ArrayList getResendMessages(int seqNum, int endSeq) {
	
	int charRead;
	StringBuffer seq = new StringBuffer();
	StringBuffer length = new StringBuffer();
	String returnMessage=null;
	ArrayList resendList=new ArrayList();
	synchronized(outFileLock)
	{
	try
	{
		String outFileName = "FIX-"+senderCompID+"-OUT.log";
		if(logDirectory != null)
			outFileName = logDirectory + outFileName;
			
		FileInputStream stream= new FileInputStream(outFileName);

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
		
			while( (charRead=stream.read()) != -1 )
			{
				if( (char)charRead != ';' )
				{
					length.append( (char)charRead );
				}
				else
				  break;			
			}
			int currSeq;
			currSeq=Integer.parseInt(seq.toString());
			
			if (endSeq == 0)			
 			{
				if ( seqNum <= currSeq)
				{
					int len = Integer.parseInt(length.toString());
					byte[] msgRead = new byte[len];
					stream.read( msgRead );
					String message1 = new String(msgRead);
				//	Logger.debug("Message is : " + message1);
					//FixMessage fixMsg = FixMessageFactory.createFixMessage( message.toString().getBytes());
					FixMessage fixMsg = FixMessageFactory.createFixMessageWithoutValidation(msgRead);
					resendList.add(fixMsg);
				}else
					stream.skip( Integer.parseInt(length.toString()) );

			}else if (endSeq > 0)
			{
				if (( seqNum <= currSeq) && ( (endSeq) >= currSeq))
				{
					int len = Integer.parseInt(length.toString());
					byte[] msgRead = new byte[len];
					stream.read( msgRead );
					String message2 = new String(msgRead);
				//	Logger.debug("Message is : " + message2);
					FixMessage fixMsg = FixMessageFactory.createFixMessageWithoutValidation(msgRead);
					resendList.add(fixMsg);
				}else
					stream.skip( Integer.parseInt(length.toString()) );
			}
			
			
				
		}
		stream.close();
				
		
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
		
	}

	return resendList;
	

}


/**
 * method to get Session Reject message sequence numbers
 * @return
 */
public ArrayList getSessionRejectSeqList() {
	
	int charRead;
	StringBuffer seq = new StringBuffer();
	StringBuffer length = new StringBuffer();
	String returnMessage=null;
	ArrayList list=new ArrayList(10);
	synchronized(outFileLock)
	{
	try
	{
		String inFileName = "FIX-"+senderCompID+"-IN.log";
		if(logDirectory != null)
			inFileName = logDirectory + inFileName;

		FileInputStream stream= new FileInputStream(inFileName);

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
		
			while( (charRead=stream.read()) != -1 )
			{
				if( (char)charRead != ';' )
				{
					length.append( (char)charRead );
				}
				else
				  break;			
			}
			int currSeq;
			currSeq=Integer.parseInt(seq.toString());
			
			int len = Integer.parseInt(length.toString());
			byte[] msgRead = new byte[len];
			stream.read( msgRead );
			String message1 = new String(msgRead);
			//	Logger.debug("Message is : " + message1);
			//FixMessage fixMsg = FixMessageFactory.createFixMessage( message.toString().getBytes());
			FixMessage fixMsg = FixMessageFactory.createFixMessageWithoutValidation(msgRead);
			if(fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_REJECT))
			{
				list.add(((FixSessionReject)fixMsg).getRefSeqNumAsString());
			}
			
				
		}
		stream.close();
				
		
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
		
	}

	return list;
	

}

/**
 * method to get application messages list
 * @param file
 * @return
 */
public static ArrayList getApplicationMessagesList(String file) {
	
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

		while( (charRead=stream.read()) != -1 )
		{
			if(charRead == NL )
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
			Logger.debug("Seq is >" + seq+"<");
		
			while( (charRead=stream.read()) != -1 )
			{
				if( (char)charRead != ';' )
				{
					length.append( (char)charRead );
				}
				else
				  break;			
			}
			Logger.debug("Length is >" + length+"<");
			int currSeq;
			currSeq=Integer.parseInt(seq.toString().trim());
			
			int len = Integer.parseInt(length.toString());
			byte[] msgRead = new byte[len];
			stream.read( msgRead );
			String message1 = new String(msgRead);
			Logger.debug("Message is : " + message1);
			//FixMessage fixMsg = FixMessageFactory.createFixMessage( message.toString().getBytes());
			FixMessage fixMsg = FixMessageFactory.createFixMessageWithoutValidation(msgRead);
			if(fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_ORDER)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_CANCEL)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS)	
			)			
			{
				list.add(fixMsg);
			}
			
				
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
public static FixMessage getApplicationMessages(FileInputStream stream) {
	return getApplicationMessages(stream,false);
}

/**
 * method to get one message at a time
 * @param file
 * @return
 */
public static FixMessage getApplicationMessages(FileInputStream stream, boolean inclLogon) {
	
	int charRead;
	StringBuffer seq = new StringBuffer();
	StringBuffer length = new StringBuffer();
	String returnMessage=null;
	FixMessage returnedMessage = null;
	//synchronized(outFileLock)
	//{
	try
	{
		//String inFileName = file;

		//FileInputStream stream= new FileInputStream(inFileName);
		//Logger.debug("File is " + inFileName);

		while( (charRead=stream.read()) != -1 )
		{
			if(charRead == NL )
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
			Logger.debug("Seq is >" + seq+"<");
		
			while( (charRead=stream.read()) != -1 )
			{
				if( (char)charRead != ';' )
				{
					length.append( (char)charRead );
				}
				else
				  break;			
			}
			Logger.debug("Length is >" + length+"<");
			int currSeq;
			currSeq=Integer.parseInt(seq.toString().trim());
			
			int len = Integer.parseInt(length.toString());
			byte[] msgRead = new byte[len];
			stream.read( msgRead );
			String message1 = new String(msgRead);
			Logger.debug("Message is : " + message1);
			//FixMessage fixMsg = FixMessageFactory.createFixMessage( message.toString().getBytes());
			FixMessage fixMsg = FixMessageFactory.createFixMessageWithoutValidation(msgRead);
			if((fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_ORDER)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_CANCEL)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_CANCELREPLACE)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_EXECUTIONREPORT)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_CANCEL_REJECT)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_REQUEST)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_MARKETDATA_FULL_REFRESH)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_MARKETDATA_INCREMENTAL_REFRESH)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_SECURITY_DEFINITION_RESPONSE)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_MARKETDATA_SECURITY_STATUS)	
			) || inclLogon && (fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_LOGON)
			||fixMsg.getMsgType().equals(FixConstants.FIX_MSGTYPE_LOGOUT)))			
			{
				returnedMessage = fixMsg;
				//System.out.println("Found Msg");
				break;
			}
			
				
		}
		if(returnedMessage == null)
		stream.close();
				
		
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
		
	//}

	return returnedMessage;
	

}



}
