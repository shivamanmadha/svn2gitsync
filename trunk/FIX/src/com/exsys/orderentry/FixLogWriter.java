package com.exsys.orderentry;

import java.io.*;

import com.exsys.fix.message.FixConstants;
import com.exsys.fix.message.FixMessage;
import com.exsys.fix.message.NativeFixTranslator;

/**
 * @author kreddy
 *
 * This class provides fix log file management and
 * sequence number management for both fix client
 * and fix server. Also provides functions to support
 *  resend message processing.
 */
public class FixLogWriter {
	
	private String senderCompID;

	private FileOutputStream inWriter = null;

	private FileOutputStream outWriter = null;

	private boolean addNL = true;

	private String logDirectory = null;

	private Object inFileLock = new Object();

	private Object outFileLock = new Object();
	
	private Object sendMessageLock = new Object();
	
	public final static String FIX_MESSAGE_DELIM = ";";

	public final static char NL = '\n';

	/**
	 * FixSessionManager constructor
	 * @param senderID
	 */
	public FixLogWriter(String senderID) {
		senderCompID = senderID;
	}

	/**
	 * @return
	 */
	public java.lang.String getSenderCompID() {
		return senderCompID;
	}

	/**
	 * method to log an inbound fix message
	 * @param seq
	 * @param msg
	 */
	public void logInFixMessage(FixMessage msg) {
		
		String message = translateMessage(msg);
		synchronized (inFileLock) {
			
			try {				
				if (inWriter == null) {
					
					String inFileName = "FIX-" + senderCompID + "-IN.log";
					
					if (logDirectory != null)
						inFileName = logDirectory + inFileName;

					inWriter = new FileOutputStream(inFileName, true);
				}

				inWriter.write(message.getBytes());
				
				if (addNL) {
					inWriter.write('\r');
					inWriter.write('\n');
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * method to log outbound fix message
	 * @param seq
	 * @param msg
	 */
	public void logOutFixMessage(FixMessage msg) {

		String message = translateMessage(msg);
		synchronized (outFileLock) {
			
			try {
				if (outWriter == null) {
					
					String outFileName = "FIX-" + senderCompID + "-OUT.log";
					
					if (logDirectory != null)
						outFileName = logDirectory + outFileName;

					outWriter = new FileOutputStream(outFileName, true);
				}

				outWriter.write(message.getBytes());

				if (addNL) {
					inWriter.write('\r');
					outWriter.write('\n');
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * method to close all resources
	 */
	public void sessionClosed() {
		
		try {
			inWriter.close();
			outWriter.close();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param newSenderCompID
	 */
	public void setSenderCompID(String newSenderCompID) {
		senderCompID = newSenderCompID;
	}

	/**
	 * @param newLogDirectory
	 */
	public void setLogDirectory(String newLogDirectory) {
		logDirectory = newLogDirectory;
	}

	/**
	 * @return
	 */
	public String getLogDirectory() {
		return logDirectory;
	}
	
	public String translateMessage(FixMessage msg) 
	{
		synchronized(sendMessageLock)
		{
			return prepareFixBuffer( msg, false );
			//Logger.debug(" About to send Msg is " + fixString );
		}
	}
	
	public String prepareFixBuffer(FixMessage msg, boolean resend) 
	{
		// This needs to translate msg to FIX String
		// Prepend required header tags
		// compute checksum
		String fixString = null;

		try
		{
			StringWriter sw = new StringWriter();
			NativeFixTranslator ft = new NativeFixTranslator( sw );
			
			if(!resend)
				ft.translateHeader( msg, false );
			else
				ft.translateResendHeader(msg);
				
			String headerString = sw.toString();
			sw.getBuffer().setLength(0);
			
			
			if(!resend)
				ft.translateBody( msg );
			else
				ft.translateBody(msg,true);
				
			String bodyString = sw.toString();

			String msgTypeString = FixConstants.FIX_HEADER_MSGTYPE_TAG +
								  FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
								  msg.getMessageType() +
								  (char)FixConstants.FIX_FIELD_DELIMITER ;
								  
			int bodyLength = headerString.getBytes().length + 
							 bodyString.getBytes().length +
							 msgTypeString.getBytes().length;
			
			// prefix header fields
			String prefixString = FixConstants.FIX_HEADER_BEGINSTRING_TAG +
								  FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
								  TraderWindow.fixVersion +
								  (char)FixConstants.FIX_FIELD_DELIMITER +
								  FixConstants.FIX_HEADER_BODYLENGTH_TAG +
								  FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
								  String.valueOf( bodyLength ) +
								  (char)FixConstants.FIX_FIELD_DELIMITER +
								  msgTypeString;							  

								  
			fixString = prefixString + headerString + bodyString;					  
			
			String checkSum = FixMessage.checksum(fixString);

			String trailerString = 	FixConstants.FIX_TRAILER_CHECKSUM_TAG +
									FixConstants.CHAR_FIX_TAG_VALUE_SEPERATOR +
									checkSum +
									(char)FixConstants.FIX_FIELD_DELIMITER ;
			
			fixString += trailerString;
			
				

		}
		catch(Exception e )
		{
			e.printStackTrace();
		}

		return fixString;
	}

}
