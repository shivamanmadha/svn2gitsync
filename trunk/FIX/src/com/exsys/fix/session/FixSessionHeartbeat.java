package com.exsys.fix.session;
import com.exsys.service.*;


/**
 * @author kreddy
 *
 * Implements fix heartbeat thread functionality
 */
public class FixSessionHeartbeat extends Thread 
{
	FixSession fixSession = null;
	private boolean shutdown = false;

/**
 * FixSessionHeartbeat constructor
 * @param session
 */
public FixSessionHeartbeat(FixSession session) {
	super();
	fixSession = session;
}

/* (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
public void run() 
{
	int currentOutSeq = 0;
	int currentInSeq = 0;
	int noHeartbeatCount = 0;
	while(shutdown != true )
	{
		currentOutSeq = fixSession.getOutSequenceNumber();
		currentInSeq = fixSession.getInSequenceNumber();
		Logger.debug("Out = "+currentOutSeq + " In = "+ currentInSeq);
		try
		{
			Thread.sleep( fixSession.getHeartBeatInterval() );
			Logger.debug("Wokeup");
		}
		catch( InterruptedException e )
		{
			Logger.debug("Heartbeat Thread Interrupted");
		}
		if(!shutdown)
		{
			Logger.debug("OutS = "+fixSession.getOutSequenceNumber() +
			 " InS = "+ fixSession.getInSequenceNumber());
			 
			if( currentOutSeq == fixSession.getOutSequenceNumber() 
				&& currentInSeq == fixSession.getInSequenceNumber())
			{
				// This means the session has not sent
				// out any data or received any data and we need to force
				// heartbeat
				++noHeartbeatCount;
				if(noHeartbeatCount <= 1)
				{
					Logger.debug("About to send heartbeat #"+ noHeartbeatCount);
					fixSession.sendHeartBeat();
				}
				else if(noHeartbeatCount <= 2)
				{
					if( fixSession.isPrimary() )
					{
						Logger.debug("Missed 2 heartbeats. About to send test request");
						fixSession.sendTestRequest("TESTRQ");					
					}
					else
					{
						fixSession.sendHeartBeat();
						noHeartbeatCount = 0;
					}
				}
				else if(noHeartbeatCount <= 3)
				{
					if( fixSession.isPrimary() )
					{
						Logger.debug("Missed 2 heartbeats. About to send test request");
						fixSession.sendTestRequest("TESTRQ");					
					}
					else
					{
						fixSession.sendHeartBeat();
						noHeartbeatCount = 0;
					}					
				}				
				else // must be more than 3
				{
					if( fixSession.isPrimary() )
					{
						Logger.debug("Missed 2 test requests. Stale Connection");
						fixSession.staleConnectionDetected();					
					}
					else
					{
						noHeartbeatCount = 0;
					}
				}
				
			}
			else
			{
				// reset counter
				noHeartbeatCount = 0;
			}
		}
		
	}
	Logger.debug("Heartbeat Thread Exited");
	fixSession.debug("Heartbeat Thread Exited");
}

/**
 * method to stop heartbeat thread
 */
public synchronized void stopThread() 
{
	shutdown = true;
	interrupt();
	
}
}
