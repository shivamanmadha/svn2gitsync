package com.exsys.fix.session;


import com.exsys.fix.message.*;
import com.exsys.common.exceptions.*;
import com.exsys.common.trading.*;
import com.exsys.service.*;


/**
 * @author kreddy
 *
 * This class implements a JMS version of
 * FixClientSession for ICE
 */
public class ICEJmsFixClientSession extends JmsFixClientSession implements SecuritiesMessageProcessor
{
	private SecuritiesSessionManager tsManager = null;
	private String publishSubjectPrefix;
	private String reqSubscribeSubject;
	
/**
 * ICEJmsFixClientSession constructor
 * @param playback 
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ICEJmsFixClientSession(boolean playback)
		throws ConfigAttributeNotFound, 
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	//initializeConfig();
}

/**
 * ICEJmsFixClientSession constructor
 * @param newHost
 * @param newPort
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ICEJmsFixClientSession(String newHost, int newPort)
		throws ConfigAttributeNotFound,
			   ConfigAttributeInvalid,
			   SystemException
{
	super(newHost, newPort);
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.JmsFixClientSession#initializeConfig()
 */
protected void initializeConfig() 
		throws ConfigAttributeNotFound,
			   SystemException
{
	super.initializeConfig();
	
	// initialize config parameters
    try
    {
    	publishSubjectPrefix = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SEC_PUBLISH_PREFIX);
	   	Logger.debug("Securities Client Publish Subject is " + publishSubjectPrefix );
    	reqSubscribeSubject = ConfigurationService.getValue(ConfigConstants.FIXCLIENT_SEC_SUBSCRIBE_SUBJECT);
    	Logger.debug("Securities Client Subscribe Subject is " + reqSubscribeSubject );

    }
    catch( ConfigAttributeNotFound exc )
    {
	    Logger.fatal("JmsFixClientSession: REQUIRED CONFIG VALUES DO NOT EXIST");
	    throw exc;	    
    }

}

/* (non-Javadoc)
 * @see com.exsys.fix.session.JmsFixClientSession#initializeApplicationMessageLayer()
 */
protected void initializeApplicationMessageLayer()
{
	super.initializeApplicationMessageLayer();
	
	if( tsManager == null )
	{
		tsManager = new SecuritiesSessionManager();
		tsManager.startSecuritiesSession();
		tsManager.receiveSecuritiesMessages(this, reqSubscribeSubject);				
		Logger.debug("Waiting For Securities Requests");
	}
	registerSecuritiesMessageProcessor(this);
	
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#shutdownClientSession()
 */
protected void shutdownClientSession()
{
	super.shutdownClientSession();
	if(tsManager != null)
	{
		tsManager.stopSecuritiesSession();
		tsManager = null;		
	}
}




/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processTraderLogonResponse(com.exsys.fix.message.FixTraderLogonResponse)
 */
public boolean processTraderLogonResponse(FixTraderLogonResponse msg)
{
	String str = "Trader Logon Response ";
	str += "ClientId = " + msg.getClientId();
	str += " UserName = " + msg.getUserName();
	str += " Text = " + msg.getText();

	//Logger.info(str);
	info(str);
	return true;
}


/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processTraderLogoutResponse(com.exsys.fix.message.FixTraderLogoutResponse)
 */
public boolean processTraderLogoutResponse(FixTraderLogoutResponse msg)
{

	String str = "Trader Logout Response ";
	str += "ClientId = " + msg.getClientId();
	str += " UserName = " + msg.getUserName();
	str += " Text = " + msg.getText();

	//Logger.info(str);
	info(str);

	return true;
}

/* (non-Javadoc)
 * @see com.exsys.common.trading.SecuritiesMessageProcessor#processSecurityDefinitionResponse(com.exsys.fix.message.FixSecurityDefinitionResponse)
 */
public void processSecurityDefinitionResponse(FixSecurityDefinitionResponse msg)
{
	String respSub = publishSubjectPrefix;
	//String scID = getSenderCompID();
	tsManager.sendSecurityDefinitionResponse(msg, respSub);

	
}
/* (non-Javadoc)
 * @see com.exsys.common.trading.SecuritiesMessageProcessor#processSecurityDefinitionRequest(com.exsys.fix.message.FixSecurityDefinitionRequest)
 */
public void processSecurityDefinitionRequest(FixSecurityDefinitionRequest msg)
{
	sendSecurityDefinitionRequest(msg);	
}


}
