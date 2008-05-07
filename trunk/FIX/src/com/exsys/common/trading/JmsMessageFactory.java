package com.exsys.common.trading;

import javax.jms.*;
import com.exsys.common.business.*;
/**
 * Insert the type's description here.
 * Creation date: (2/7/2002 6:43:46 AM)
 * @author: Administrator
 */
public class JmsMessageFactory {
/**
 * JmsMessageFactory constructor comment.
 */
public JmsMessageFactory() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 6:48:39 AM)
 * @return com.exsys.common.business.RealTimeQuote
 * @param msg javax.jms.StreamMessage
 */
private static Cancel createCancel(StreamMessage msg, String type) throws JMSException
{
	Cancel cancel = new Cancel();
	cancel.setType(type);
    cancel.setCancelId(msg.readString());
	cancel.setTBCOrderId(msg.readString());
	cancel.setSymbol(msg.readString());
	cancel.setUser(msg.readString());
	cancel.setCancelQuantity(new Integer(msg.readInt()));
	cancel.setBuyOrSell(msg.readString());

	return cancel;
	
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 6:45:42 AM)
 * @return com.exsys.common.business.IBusinessObject
 * @param msg javax.jms.StreamMessage
 */
public static IBusinessObject createJmsMessage(StreamMessage msg) throws JMSException
{
	String type = msg.readString();
	if( type.equals(IBusinessObject.QUOTE ))
	{
		return createRealTimeQuote( msg,IBusinessObject.QUOTE );
	}
	else if( type.equals(IBusinessObject.CANCEL) )
	{
		return createCancel(msg, IBusinessObject.CANCEL );
		
	}
	

	return null;
	 
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 6:48:39 AM)
 * @return com.exsys.common.business.RealTimeQuote
 * @param msg javax.jms.StreamMessage
 */
private static RealTimeQuote createRealTimeQuote(StreamMessage msg, String type) throws JMSException
{
	RealTimeQuote quote = new RealTimeQuote(type);
	quote.setBidQuantity(new Integer(msg.readInt()));
	quote.setBidPrice( msg.readString());
	quote.setOfferQuantity(new Integer(msg.readInt()));
	quote.setOfferPrice(msg.readString());
	quote.setLastQuantity(new Integer(msg.readInt()));
	quote.setLastPrice(msg.readString());
	quote.setSymbol(msg.readString());
	return quote;
	
}
}
