package com.exsys.common.trading;

import java.util.*;
import javax.jms.*;
import javax.naming.*;
import java.applet.Applet;
import com.exsys.common.business.*;


/**
 * Common class that provides basic jms methods to start a jms session,
 * to create subscribers for a particular topic, and
 *	to create publishers,
 *	and to publish a message.
 * Creation date: (2/2/2002 10:17:26 AM)
 * @author: Administrator
 */
public class JmsTradingSessionManager {
	private HashMap topicPublishers = null;
	private HashMap topicSubscribers = null;
	private TopicConnectionFactory tcf = null;
	private TopicConnection tc = null;
	private TopicSession ts = null;
	
/**
 * JmsTradingSessionManager constructor .
 */
public JmsTradingSessionManager() {
	super();
	initialize(null);
}
/**
 * JmsTradingSessionManager constructor .
 */
public JmsTradingSessionManager(Applet applet) {
	super();
	initialize(applet);
}
/**
 * Method to close jms session.
 * Creation date: (2/2/2002 10:33:14 AM)
 */
public void close() throws JMSException
{
	if( tc != null )
	{
		tc.close();
		tc = null;
	}
}
/**
 * method to create a jms subscriber.
 * Creation date: (2/2/2002 11:07:07 AM)
 * @param topic java.lang.String
 * @param listener javax.jms.MessageListener
 */
public void createSubscriber(String topic, MessageListener listener) throws JMSException
{
	Topic jmsTopic = ts.createTopic( topic );
	TopicSubscriber sub = ts.createSubscriber(jmsTopic);
	sub.setMessageListener( listener );
}
/**
 * method to get topic publisher.
 * This method returns an existing publisher if one
 * exists. if not, it will create a new publisher
 * for the topic
 * Creation date: (2/2/2002 11:14:19 AM)
 * @return javax.jms.TopicPublisher
 * @param topic java.lang.String
 */
private TopicPublisher getPublisher(String topic) throws JMSException
{
	if( topicPublishers == null )
	{
		topicPublishers = new HashMap();
		// create a new publisher
		Topic jmsTopic = ts.createTopic( topic );
		TopicPublisher tp  = ts.createPublisher(jmsTopic);
		topicPublishers.put( topic, tp );
		return tp;
	}
	else
	{
		// check if publisher exists
		TopicPublisher tp = (TopicPublisher)topicPublishers.get(topic);
		if( tp == null )
		{
			Topic jmsTopic = ts.createTopic( topic );
			tp  = ts.createPublisher(jmsTopic);
			topicPublishers.put( topic, tp );			
		}
		return tp;
	}
	
}
/**
 * initialization method 
 * Creation date: (2/2/2002 10:27:22 AM)
 */
private void initialize(Applet applet) 
{
	String serverUrl = null;
	String userName = null;
	String password = null;
        try 
        {
	        if( applet == null )
	        {
            /* Init JNDI context */
         	   JMSUtilities.initJNDI(serverUrl,userName,password);
               tcf = (TopicConnectionFactory)JMSUtilities.lookup("TopicConnectionFactory",true);         	   
	        }
	        else
	        {
		        // for applet, initialization is different
		        System.out.println("Before Applet JNDI init");
		        JMSUtilities.initAppletJNDI(serverUrl,userName,password,applet);
		        System.out.println("After Applet JNDI init");		        
		        tcf = (TopicConnectionFactory)JMSUtilities.lookup("TopicConnectionFactory",false);
		        System.out.println("After lookup - TCF is "+ tcf != null);		        
	        }


            
            tc = tcf.createTopicConnection(userName,password);
            
            ts = tc.createTopicSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
            
        }
        catch(JMSException e) 
        {
            e.printStackTrace();
        }
        catch(NamingException e) 
        {
            e.printStackTrace();
        }
	
}
/**
 * Method to publish an object of type derived from IBusinessObject.
 * Creation date: (2/2/2002 11:05:32 AM)
 * @param obj IBusinessObject
 * @param topic java.lang.String
 */
public void publishBusinessMessage(IBusinessObject obj, String topic) 
{
	// first check if a publisher is already created
	// for this topic
	try
	{
		TopicPublisher tp = getPublisher( topic );
		StreamMessage sMsg = ts.createStreamMessage();
		if( obj.type.equals(IBusinessObject.QUOTE))
		{			
			JmsMessageTranslator.translateRealTimeQuote((RealTimeQuote)obj,sMsg);
			tp.publish(sMsg);
		}
		else if( obj.type.equals(IBusinessObject.CANCEL))
		{			
			JmsMessageTranslator.translateCancel((Cancel)obj,sMsg);
			tp.publish(sMsg);
		}		
		else
		{

			System.out.println("Unknown Msg type to publish "+topic);
			obj.toString();
		}
		
	}
	catch( JMSException e )
	{
		e.printStackTrace();
	}
}
/**
 * Generic method to publish any byte array message.
 * Creation date: (2/2/2002 11:05:32 AM)
 * @param msg byte[]
 * @param topic java.lang.String
 */
public void publishMessage(byte[] msg, String topic) 
{
	// first check if a publisher is already created
	// for this topic
	try
	{
		TopicPublisher tp = getPublisher( topic );
		//BytesMessage bMsg = ts.createBytesMessage();
		//bMsg.writeBytes(msg);
		//tp.publish(bMsg);
		TextMessage tMsg = ts.createTextMessage();
		tMsg.setText(new String(msg));
		tp.publish(tMsg);
		
	}
	catch( JMSException e )
	{
		e.printStackTrace();
	}
}
/**
 * Generic method to publish any byte array message.
 * Creation date: (2/2/2002 11:05:32 AM)
 * @param msg byte[]
 * @param topic java.lang.String
 */
public void publishBytesMessage(byte[] msg,int length, String topic) 
{
	// first check if a publisher is already created
	// for this topic
	try
	{
		TopicPublisher tp = getPublisher( topic );
		BytesMessage bMsg = ts.createBytesMessage();
		bMsg.writeBytes(msg,0,length);
		tp.publish(bMsg);
		//TextMessage tMsg = ts.createTextMessage();
		//tMsg.setText(new String(msg));
		//tp.publish(tMsg);
		
	}
	catch( JMSException e )
	{
		e.printStackTrace();
	}
}
/**
 * Method to start jms session.
 * Creation date: (2/2/2002 7:53:04 PM)
 */
public void startSession() throws JMSException
{
	tc.start();
}
}
