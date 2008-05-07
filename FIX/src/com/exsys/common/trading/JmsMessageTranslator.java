package com.exsys.common.trading;

import javax.jms.*;
import com.exsys.common.business.*;
/**
 * Insert the type's description here.
 * Creation date: (2/7/2002 6:49:45 AM)
 * @author: Administrator
 */
public class JmsMessageTranslator {
/**
 * JmsMessageTranslator constructor comment.
 */
public JmsMessageTranslator() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 6:51:12 AM)
 * @return javax.jms.StreamMessage
 * @param quote com.exsys.common.business.RealTimeQuote
 */
public static void translateCancel(Cancel cancel, StreamMessage msg) throws JMSException
{

	
	msg.writeString(IBusinessObject.CANCEL);
	msg.writeString(cancel.getCancelId());
	msg.writeString(cancel.getTBCOrderId());
	msg.writeString(cancel.getSymbol());
	msg.writeString(cancel.getUser());
	msg.writeInt(cancel.getCancelQuantity().intValue());	
	msg.writeString(cancel.getBuyOrSell());		

	
}	
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 6:51:12 AM)
 * @return javax.jms.StreamMessage
 * @param quote com.exsys.common.business.RealTimeQuote
 */
public static void translateRealTimeQuote(RealTimeQuote quote, StreamMessage msg) throws JMSException
{
	msg.writeString(IBusinessObject.QUOTE);
	msg.writeInt(quote.getBidQuantity().intValue());
	msg.writeString(quote.getBidPrice());
	msg.writeInt(quote.getOfferQuantity().intValue());
	msg.writeString(quote.getOfferPrice());
	msg.writeInt(quote.getLastQuantity().intValue());
	msg.writeString(quote.getLastPrice());
	msg.writeString(quote.getSymbol());

	
}
}
