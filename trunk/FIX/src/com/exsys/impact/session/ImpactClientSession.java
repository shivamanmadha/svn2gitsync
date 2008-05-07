package com.exsys.impact.session;



import com.exsys.common.exceptions.*;

import com.exsys.service.Logger;
import com.exsys.impact.message.*;
import com.exsys.service.ConfigConstants;
import com.exsys.service.ConfigurationService;
import com.exsys.impact.mdf.message.*;
import com.exsys.impact.mdf.message.request.*;
import com.exsys.impact.mdf.message.notification.*;
import com.exsys.impact.mdf.message.response.*;

/**
 * @author kreddy
 *
 * This class provides functionality necessary
 * for the fix client
 */
public class ImpactClientSession extends ImpactSession
{
	private int port=0;
	private String host;
	private java.lang.String senderCompID;
	
	private java.lang.String traderName;// specific to ICE
	private java.lang.String traderpw;//specific to ICE
	private int reqSeqID = 1;
	private String mktStatsFlag = null;
	private String dataBufferingFlag = null;
	private String bundleMarkerFlag = null;
	private String impliedOrdersFlag = null;			


/**
 * ImpactClientSession constructor
 * @param playback
 * @throws ConfigAttributeNotFound
 * @throws ConfigAttributeInvalid
 * @throws SystemException
 */
public ImpactClientSession(boolean playback)
		throws ConfigAttributeNotFound,
			   ConfigAttributeInvalid,
			   SystemException
{
	super(playback);
	//initializeConfig();
}

	/**
	 * ImpactClientSession constructor
	 * @param newHost
	 * @param newPort
	 * @throws ConfigAttributeNotFound
	 * @throws ConfigAttributeInvalid
	 * @throws SystemException
	 */
	public ImpactClientSession( String newHost, int newPort )
						throws ConfigAttributeNotFound,
								ConfigAttributeInvalid,
								SystemException
	{
		super( newHost, newPort );

		
		traderName = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_TRADERNAME);
		senderCompID = traderName;
		traderpw = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_TRADER_PASSWORD);
		mktStatsFlag = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_MKTSTATSFLAG);
		dataBufferingFlag = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_DATABUFFERINGFLAG);
		bundleMarkerFlag = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_BUNDLEMARKERFLAG);		
		impliedOrdersFlag = ConfigurationService.getValue(ImpactMessageConstants.IMPACT_IMPLIEDORDERSFLAG);		

		host = newHost;
		port = newPort;
		initializeImpactSession();

	}

public java.lang.String getSenderCompID() {
	return senderCompID;
}
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#initializeFixSession()
 */
public boolean initializeImpactSession()
{

	// we need to send Logon Message
	// construct FixSessionContext
	super.initializeImpactSession();

	initializeSessionManager();
	return true;
}

// MAY BE THIS METHOD SHOULD THROW EXCEPTION
/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processLogonMessage(com.exsys.fix.message.FixMessage, boolean)
 */
public boolean processLogonMessage(LoginResponse logon)
{
	Logger.debug(logon.toString());
	if(logon.Code == LoginResponse.CODE_LOGIN_SUCCESS)
		logonSuccessful();
	else
	{
		Logger.debug("Logon Failed with reason - " + new String(logon.Text) );
	}
	return true;
}


/**
 * method to send a logon message
 */
public void sendResetLogonMessage()
{


	LoginRequest logon = new LoginRequest();
	setHeaderFields(logon);
	logon.UserName = MessageUtil.toRawChars(traderName,logon.UserName.length);
	logon.Password = MessageUtil.toRawChars(traderpw,logon.Password.length);
	logon.GetMarketStats = mktStatsFlag.toCharArray()[0];
	logon.MktDataBuffering =dataBufferingFlag.toCharArray()[0];
	logon.GetMessageBundleMarker = bundleMarkerFlag.toCharArray()[0];
	logon.GetImpliedOrders = impliedOrdersFlag.toCharArray()[0];
	
	sendResetLogonMessage(logon);



}






/**
 * method to send fix logout message
 * @param seq
 */
public void sendLogoutMessage(int seq)
{

	//logoutSent = true;
	
	LogoutRequest logout = new LogoutRequest();
	//setHeaderFields(logout);
	sendMessage(logout);


}

/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#processLogoutMessage(java.lang.String, int)
 */
public void processLogoutMessage(String text, int nextExpSeq)
{

/*

	if( logoutSent )
	{
		logoutSent = false;
		// this must be logout confirmation
		String msg = "Logout Confirm Received from Server - " + text;
		Logger.debug(msg);
		debug(msg);
		shutdownSession();
		// after shutting down, stop listening for application messages
	}
	else
	{
		// this must be an error
  	    // NOT SURE IF CLIENT NEED TO SEND LOGOUT CONFIRM
		// we can just shut down session
		Logger.error(" Unsolicited Logout Received from Server - " + text);
		//send logout
		sendLogoutMessage(0);
		shutdownSession();
	}
*/

}


/* (non-Javadoc)
 * @see com.exsys.fix.session.FixSession#setHeaderFields(com.exsys.fix.message.FixMessage)
 */
public void setHeaderFields(Request impMsg)
{
	impMsg.RequestSeqID = reqSeqID++;

}



}
