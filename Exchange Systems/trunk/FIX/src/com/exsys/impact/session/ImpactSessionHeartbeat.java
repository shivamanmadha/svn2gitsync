package com.exsys.impact.session;
import com.exsys.service.*;


/**
 * @author kreddy
 *
 * Implements fix heartbeat thread functionality
 */
public class ImpactSessionHeartbeat extends Thread
{
	ImpactSession impactSession = null;
	private boolean shutdown = false;

/**
 * ImpactSessionHeartbeat constructor
 * @param session
 */
public ImpactSessionHeartbeat(ImpactSession session) {
	super();
	impactSession = session;
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
		currentOutSeq = impactSession.getOutSequenceNumber();
		currentInSeq = impactSession.getInSequenceNumber();
		Logger.debug("Out = "+currentOutSeq + " In = "+ currentInSeq);
		try
		{
			Thread.sleep( impactSession.getHeartBeatInterval() );
			Logger.debug("Wokeup");
		}
		catch( InterruptedException e )
		{
			Logger.debug("Heartbeat Thread Interrupted");
		}
		if(!shutdown)
		{
			Logger.debug("OutS = "+impactSession.getOutSequenceNumber() +
			 " InS = "+ impactSession.getInSequenceNumber());

			if( currentOutSeq == impactSession.getOutSequenceNumber()
				&& currentInSeq == impactSession.getInSequenceNumber())
			{
				// This means the session has not sent
				// out any data or received any data and we need to force
				// heartbeat
				++noHeartbeatCount;
				if(noHeartbeatCount <= 1)
				{
					Logger.debug("About to send heartbeat #"+ noHeartbeatCount);
					//fixSession.sendHeartBeat();
				}
				else
				{
					Logger.debug("Missed 2 test requests. Stale Connection");
					impactSession.staleConnectionDetected();

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
	impactSession.debug("Heartbeat Thread Exited");
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
