package com.exsys.fix.session;

import java.io.*;
import java.net.*;
import java.util.*;
import com.exsys.common.tcpsession.*;
import com.exsys.fix.tools.*;

/**
 * @author kreddy
 *
 * This class provides a fix server functionality, that listens
 * for connections from multiple fix clients
 * Establishes a fix session for each client
 */
public class FixServer extends Thread
{
	private boolean shutdown = false;
	private Vector clientSessions = null;
	private int port=0;
	private ServerSocket m_ServerSocket=null;
	private boolean bowLogon = false;
	
	/**
	 * FixServer constructor
	 * @param newPort
	 * @param bow
	 */
	public FixServer( int newPort, boolean bow )
	{
		bowLogon = bow;
		clientSessions = new Vector();
		port = newPort;
		try
		{
			m_ServerSocket = new ServerSocket( port );
			System.out.println("Server initialization successful");
		}
		catch(Exception e )
		{
			e.printStackTrace();
			System.out.println( e );
			System.out.println("Server initialization Failed");
		}
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{

	      int exceptionRetry = 0;
	      while( shutdown != true )
	      {
		  // do dequeue

		  try
		  {
		      System.out.println("Waiting for connections from GUI clients");
		      Socket clientSocket = m_ServerSocket.accept();
		      System.out.println("Connection Received From client");
		      System.out.println("Connection Received From GUI client");

		      //FixServerSession session = new FixServerSession( clientSocket );
		      /* uncomment to create non-gui server session
		      JmsFixServerSession session = new JmsFixServerSession( clientSocket );
		      session.setBOWLogon(bowLogon);
		      session.start();
		      */
		      FixServerWindow fsWindow = new FixServerWindow("Fix Server Session",
		      		clientSocket,bowLogon);
		      
		      fsWindow.pack();
			  fsWindow.setVisible(true);
		
		      exceptionRetry = 0;
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

	      System.out.println( "Thread Shutting - ThreadName - " + getName() );
	}
	/**
	 * method to stop session
	 */
	public void stopSession()
	{
		shutdown = true;
		interrupt();
	}
}
