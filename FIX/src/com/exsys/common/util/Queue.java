package com.exsys.common.util;
import com.exsys.service.*;

import java.util.*;

/**
 * @author kreddy
 *
 * Class to implement a queue object
 */
public class Queue
{
	private Vector messageQueue;
	private boolean shutdown = false;
	private boolean onHold = false;
	public static int BEGIN_INDEX = 0;
	/**
	 * Queue constructor
	 */
	public Queue()
	{

		messageQueue = new Vector();
	}
	/**
	 *  method to close the queue
	 */
	public synchronized void closeQueue()
	{
	   shutdown = true;
	}
	/**
	 * method to dequeue message
	 * @return
	 */
	public Object dequeue()
	{

		Object msg = null;
		while( !shutdown && (isEmpty() || isOnHold()) )
		{
		    try
		    {
				synchronized( messageQueue )
				{
					//Logger.debug("Waiting");
					messageQueue.wait();
					
					if( !shutdown && !isEmpty() )
					{
						synchronized( messageQueue )
						{
							//Logger.debug("Removing element");
							int lastIndex = messageQueue.size() - 1;
							msg = messageQueue.elementAt( lastIndex );
							messageQueue.removeElementAt( lastIndex );
							return( msg );
						}
						
					}
				}
		    }
		    catch( InterruptedException e )
		    {
			//Logger.debug("Waiting Interrupted");
			e.printStackTrace();
		    }
		}
		if(!shutdown)
		{
			// queue is not empty remove
			synchronized( messageQueue )
			{
				//Logger.debug("Removing element");
				int lastIndex = messageQueue.size() - 1;
				msg = messageQueue.elementAt( lastIndex );
				messageQueue.removeElementAt( lastIndex );
				return( msg );
			}
		}
		else
		{
			return null;
		}
		
		
	}
	// enqueue at the top
	/**
	 * method to enqueue message
	 * @param msg
	 */
	public  void enqueue( Object msg )
	{

		synchronized( messageQueue )
		{
		  messageQueue.insertElementAt( msg, BEGIN_INDEX );
		  if(!isOnHold())
		  {
		  	//Logger.debug("Notifying All");
		  	messageQueue.notifyAll();
		  }
		  
		}
	}
	/**
	 * method to check if the queue is empty
	 * @return
	 */
	public boolean isEmpty()
	{
		return( messageQueue.isEmpty() );
	}

	/**
	 * method to check if the queue is on hold
	 * @return
	 */
	public boolean isOnHold()
	{
		return( onHold );
	}
	/**
	 *
	 * method to set the queue on hold 
	 */
	synchronized public void setOnHold()
	{
		onHold = true;
	}
	/**
	 * method to remove hold on the queue
	 * 
	 */
	synchronized public void removeHold()
	{
		onHold = false;
		synchronized( messageQueue )
		{		
			messageQueue.notifyAll();
		}
	}	
	
	
}
