package com.exsys.common.tcpsession;

import java.io.*;

import java.net.*;
import com.exsys.common.util.*;
import com.exsys.service.*;
import com.exsys.common.exceptions.*;

/**
 * @author kreddy
 *
 * This is the base class that handles tcpip communication
 * that happens in a FixSession.
 */
public class TcpSession extends Thread 
			implements DequeueAdapter,
			IFStatusNotification
{
  private Queue inQueue = null;
  private Queue replyQueue = null;
  private boolean shutdown = false;
  private Socket socket;
  private InputStreamReader reader;
  private OutputStreamWriter writer;
  private DequeueThread inThread;
  private StringBuffer  lastTag = new StringBuffer();
  private boolean tagSeperatorFound = false;
  private boolean fieldSeperatorFound = false;
  private byte delim =  001;
  private boolean waitingForResponse = false;
  private boolean socketClosed = false;
  
  private IFStatusNotification statusNotifier = null;
  
/**
 * TcpSession constructor
 * @param playback 
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public TcpSession(boolean playback)
		throws ConfigAttributeNotFound, 
			   ConfigAttributeInvalid,
			   SystemException
{
}  
/**
 * TcpSession constructor
 * Creation date: (11/22/01 12:24:24 PM)
 * @param host java.lang.String
 * @param port int
 */
public TcpSession(String host, int port) throws SystemException
{

	try
	{
		socket = new Socket( host, port );
	}
	catch( Exception e )
	{
		SystemException se = 
				new SystemException(e.getMessage(),
						"Unable to create socket connection");
	}
	initializeResources();
}
 /**
  * TcpSession constructor
 * @param _socket
 * @throws SystemException
 */
public TcpSession( Socket _socket ) throws SystemException
  {

	socket = _socket;
	initializeResources();

	// wait for the dequeue thread to terminate

  }
  
  /* (non-Javadoc)
 * @see com.exsys.common.util.DequeueAdapter#dequeuedMessage(java.lang.Object)
 */
public void dequeuedMessage( Object msg )
  {
	//Logger.debug("In dequeuedMessage() of TcpSession : "+ (StringBuffer)msg );
  }
     

/**
 * method to initialize socket resources
 * @throws SystemException
 */
private void initializeResources() throws SystemException
{

	inQueue = new Queue();
	replyQueue = new Queue();

	try
	{
	reader = new InputStreamReader( socket.getInputStream());

	writer = new OutputStreamWriter( socket.getOutputStream());
	}
	catch( Exception e )
	{
		SystemException se = 
			new SystemException(e.getMessage(),
					"Unable to create  input output streams");
		throw se;
	}

	inThread = new DequeueThread( inQueue, this );	
	inThread.start();

}
 /**
 *
 * This method checks if the message being read over the socket
 * is complete or not 
 * @param c
 * @return
 */
public boolean isMessageComplete( char c )
  {

	//return true;
	if ( c == (char)delim )
	{
		//System.out.println(" Field Delimiter Received");
		fieldSeperatorFound = true;
//		System.out.println("Last Tag is " + lastTag.toString());
//		System.out.println(lastTag.length());
		if( lastTag.toString().equals("10") )
		{
//		  System.out.println("Message Complete " );
		  fieldSeperatorFound = false;
		  tagSeperatorFound = false;
		  return true;
		}
		else
		{
//			System.out.println("Not the end of message");
			tagSeperatorFound = false;
			//lastTag.setLength(0);
			lastTag = new StringBuffer();
			return false;
		}
	}
	if ( c == '=' )
	{
//		System.out.println(" Tag seperator Received");
		tagSeperatorFound = true;
	}
	//if( fieldSeperatorFound )
	//   lastTag = c;
	if( !tagSeperatorFound )
		lastTag.append( c );
	return false;
  }    
/**
 * method to check if tcp session is waiting for response
 * from the other side
 * Creation date: (11/21/01 8:06:01 PM)
 * @return boolean
 */
public  boolean isWaitingForResponse() 
{
	return (waitingForResponse == true );
}
  /* (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
public void run()
  {

	  int exceptionRetry = 0;
	  StringBuffer readBuffer = new StringBuffer();
	  while( shutdown != true )
	  {
		  // do dequeue

		  try
		  {
	      
	      // read a character
	      //char c = (char)reader.read();
	      int charRead = reader.read();
	      if( charRead == -1 )
	      {
	      	shutdown = true;
	      	Logger.fatal("Socket Closed at the other end");
	      	fatal("Socket Closed at the other end");
	      	socketClosed = true;
	      	shutdownSession();
	      }
	      else
	      {
	      
		  	//System.out.println(" read - " + c);
	    	// append the character
	      	readBuffer.append((char)charRead);
	        //System.out.println("Message read from socket "+ readBuffer  + " Length " + readBuffer.length());
	      	if( isMessageComplete( (char)charRead ) )
	      	{
				if( isWaitingForResponse() )
				{
					//Logger.debug("Sending via reply Queue");
					replyQueue.enqueue( readBuffer );
				}
				else
				{ 
					inQueue.enqueue( readBuffer );
				}
				readBuffer = new StringBuffer();
	  	    }
	      
			  exceptionRetry = 0;
	      }
	      
		  }
		  catch(IOException e )
		  {
			exceptionRetry++;
			String msg = " IO Exception Received -  "+ e.getMessage();
			Logger.fatal(msg);
			fatal(msg);
			shutdown = true;
	  	  }
		  
/*
		  if( exceptionRetry == 10 )
		  {
			  // shut down
			  System.out.println(" MAX RETRY ATTEMPTS exceeded ");
			  shutdown = true;
		  }
*/		  
	  }

	 Logger.fatal( "Socket Thread Shutting - ThreadName - " + getName() );
	 fatal( "Socket Thread Shutting - ThreadName - " + getName() );
	 Logger.debug(" End of  run() in TcpSession ");	
  }    
/**
 * method to shutdown the tcp session - implemented by derived classes
 */
protected void shutdownSession()
{
}          
/**
 * sets waiting flag
 * Creation date: (11/21/01 8:04:54 PM)
 * @param isWaiting boolean
 */
public synchronized void setWaitingFlag(boolean isWaiting) 
{
	waitingForResponse = isWaiting;
}
 /**
 *
 * method to stop the tcp thread and close all resources 
 */
public synchronized void stopThread()
  {
	  shutdown = true;
	  Logger.debug("TCPSession - stopThread called - Stopping the queues");
	  debug("TCPSession - stopThread called - Stopping the queues");
	  inQueue.closeQueue();
	  try
	  {
	  	
	  inThread.stopThread();
	  Logger.debug("Waiting for queue thread to exit");
	  inThread.join();
	  	  	  	  
	  }catch(Exception e ){e.printStackTrace();}
	  Logger.debug("Queue thread exited");
	  
	  try
	  {


	  Logger.debug("About to close writer");	  
	  writer.close();

	  Logger.debug("About to close reader");	  
	  reader.close(); 

	  
	  //Logger.debug("About to close Output Stream");	
	  //socket.getOutputStream().close();
	  
	 // Logger.debug("About to close Input Stream");	
	 // socket.getInputStream().close();
	 
	  //Logger.debug("About to shutdown input");
	  //socket.shutdownInput();	  
	  //Logger.debug("About to shutdown output");
	  //socket.shutdownOutput();	  
	  
	  Logger.debug("About to close socket");	  
	  if(!socketClosed)
		  socket.close();	  
	  }catch(Exception e ){e.printStackTrace();}

	  
	  //interrupt();
  }          
/**
 * method to write and wait for response
 * @param msg
 * @return response message returned
 */
public synchronized StringBuffer writeAndWaitForResponse( String msg )
  {

	StringBuffer response = null;
	try
	{
	//Logger.debug("Write And Wait - About to write "+ msg );
	writer.write( msg );
	writer.flush();

	// now wait for response
	setWaitingFlag(true);
  	  int exceptionRetry=0;
	  while( shutdown != true )
	  {
		  // do dequeue
		  
		  try
		  {

	   		  Object replyMsg = replyQueue.dequeue();
			  //Logger.debug(" WriteAndWait - dequeued from reply queue");	   		  
	   		  setWaitingFlag(false);
			  if( replyMsg != null )
			  {
				//Logger.debug("Response Msg Dequeued " + (StringBuffer)replyMsg );
				response = (StringBuffer)replyMsg;
				break;
			  }
			  else
			  {
				Logger.debug("WriteAndWait  - Invalid Message - msg dequeued is null");
				break;
			  }
			  
		  }
		  catch( Exception e )
		  {
			exceptionRetry++;
	    	e.printStackTrace();
		  }
		  
		  if( exceptionRetry == 10 )
		  {
			  // shut down
			  System.out.println(" MAX RETRY ATTEMPTS exceeded ");
			  shutdown = true;
		  }
	  }

	}
	catch( Exception e )
	{
		e.printStackTrace();
	}

	return response;
	
  }                    
/**
 * method to write a message over the socket
 * @param msg
 */
public synchronized void writeMessage( String msg )
  {
	try
	{

	writer.write( msg );
	writer.flush();
	//System.out.println("Message written");
	}
	catch( Exception e )
	{
		e.printStackTrace();
	}
	
  }    
  
/**
 * status notification handler
 * @param sn
 */
public void registerStatusNotifier(IFStatusNotification sn)
  {
  	statusNotifier = sn;
  }
  /* (non-Javadoc)
 * @see com.exsys.common.util.IFStatusNotification#info(java.lang.String)
 */
public void info(String msg)
  {
  	if(statusNotifier != null)
  	{
  		statusNotifier.info(msg);
  	}
  }
  /* (non-Javadoc)
 * @see com.exsys.common.util.IFStatusNotification#warn(java.lang.String)
 */
public void warn(String msg)
  {
  	if(statusNotifier != null)
  	{
  		statusNotifier.warn(msg);
  	}	
  }
  /* (non-Javadoc)
 * @see com.exsys.common.util.IFStatusNotification#debug(java.lang.String)
 */
public void debug(String msg)
  {
  	if(statusNotifier != null)
  	{
  		statusNotifier.debug(msg);
  	}	
  }
  /* (non-Javadoc)
 * @see com.exsys.common.util.IFStatusNotification#fatal(java.lang.String)
 */
public void fatal(String msg)
  {
  	if(statusNotifier != null)
  	{
  		statusNotifier.fatal(msg);
  	}	
  }
  /* (non-Javadoc)
 * @see com.exsys.common.util.IFStatusNotification#error(java.lang.String)
 */
public void error(String msg)
  {
  	if(statusNotifier != null)
  	{
  		statusNotifier.error(msg);
  	}	
  }  
}
