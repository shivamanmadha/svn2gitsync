package com.exsys.fix.session;


/**
 * @author kreddy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FixSessionContext {
	private java.lang.String senderCompID;
	private java.lang.String version;
	private int heartBeatIntSeconds;

/**
 * 
 */
public FixSessionContext() {
	super();
}

/**
 * @param senderID
 * @param version
 * @param heartBeatInterval
 */
public FixSessionContext(String senderID, 
						 String version,
						 int heartBeatInterval)
{
	this.senderCompID = senderID;
	this.version = version;
	heartBeatIntSeconds = heartBeatInterval;
}

/**
 * @return
 */
public int getHeartBeatInterval() {
	return heartBeatIntSeconds;
}

/**
 * @return
 */
public java.lang.String getSenderCompID() {
	return senderCompID;
}

/**
 * @return
 */
public java.lang.String getVersion() {
	return version;
}

/**
 * @param seconds
 */
public void setHeartBeatInteral(int seconds) 
{
	heartBeatIntSeconds = seconds;
}

/**
 * @param newSenderCompID
 */
public void setSenderCompID(java.lang.String newSenderCompID) {
	senderCompID = newSenderCompID;
}

/**
 * @param newVersion
 */
public void setVersion(java.lang.String newVersion) {
	version = newVersion;
}
}
