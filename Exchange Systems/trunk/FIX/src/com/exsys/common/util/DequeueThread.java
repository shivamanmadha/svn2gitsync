package com.exsys.common.util;
import com.exsys.service.*;

/**
 * This clas is used to dequeue messages from a queue
 * Creation date: (6/5/01 3:09:13 AM)
 * @author: Administrator
 */
public class DequeueThread extends Thread {
	private Queue queue;
	private boolean shutdown = false;
	private DequeueAdapter dqClient;
/**
 * DequeueThread constructor.
 */
public DequeueThread( Queue newQueue,
					  DequeueAdapter newClient)
{
	super();
	queue = newQueue;
	dqClient = newClient;
	
}
/**
 * DequeueThread constructor
 * Creation date: (6/5/01 3:16:03 AM)
 * @param newQueue com.exsys.common.util.Queue
 * @param newClient com.exsys.common.util.DequeueAdapter
 * @param name java.lang.String
 */
public DequeueThread(Queue newQueue, DequeueAdapter newClient, String name) 
{
	super(name );
	queue = newQueue;
	dqClient = newClient;
	
}
/**
 * main method logic
 * Creation date: (6/5/01 3:20:20 AM)
 */
public void run() 
{

	  int exceptionRetry = 0;
	  while( shutdown != true )
	  {
		  // do dequeue

		  try
		  {
	      	  Object msg = queue.dequeue();
			  if( msg != null )
			  {
				//Logger.debug("Msg Dequeued " + msg );
				
				dqClient.dequeuedMessage( msg );
			  }
			  else
			  {
			  	if(!shutdown)
					Logger.debug("Invalid Message ");
			  }
			  
			  if(!shutdown)
   		 	  	Logger.debug( "Thread Active - ThreadName - " + getName() );
			  exceptionRetry = 0;
		  }
		  catch( Exception e )
		  {
			exceptionRetry++;
	    	e.printStackTrace();
		  }
		  if(!shutdown)
		  {
		  	if( exceptionRetry == 10 )
		  	{
			 	 // shut down
			 	 Logger.debug(" MAX RETRY ATTEMPTS exceeded ");
			 	 shutdown = true;
		  	}
		  }
	  }

	  Logger.debug( "Thread Shutting - ThreadName - " + getName() );

}
	
/**
 * method to handle stopping of dequeue thread
 * Creation date: (6/5/01 3:18:49 AM)
 */
public synchronized void stopThread() 
{
	shutdown = true;
	interrupt();
}
}
