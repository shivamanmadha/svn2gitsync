package com.exsys.common.business;

import java.io.*;

import com.exsys.fix.message.*;

/**
 *
 * IBusinessObject abstract class is the base class for all the business objects
 * like Order, cancel etc. This defines basic methods to be overriden by
 * derived classes
 */
public abstract class IBusinessObject extends Object
{
	/**
 	 * Constants to define Buy or sell
	 */
	public static final String BUY = "B";
	public static final String SELL = "S";

	/**
 	 * Constants to define Order Type
	 */
	public static final String LMT = "LMT";
	public static final String STP = "STP";
	public static final String STL = "STL";
	public static final String MKT = "MKT";
	public static final String MIT = "MIT";
	public static final String MKTL = "MKTL";	

	/**
 	 * Constants to define Time In Force or Validity of order
	 */
	public static final String TIF_DAY = "DAY";
	public static final String TIF_GTD = "GTD";
	public static final String TIF_FAK = "FAK";
	public static final String TIF_GTC = "GTC";

	/**
 	 * Constants to define Types of TIB Messages
	 */
	public static final String ORDER = "ORD";
	public static final String CANCEL = "CXL";
	public static final String CANCEL_REPLACE = "CXR";
	public static final String QUOTE = "QOT";
	public static final String ACCEPT = "ACC";
	public static final String REJECT = "REJ";
	public static final String CONFIRM   = "CONFIRM";
	public static final String FILL   = "FILL";
	public static final String PARTIAL = "PARTIAL";
	public static final String QUOTE_QUERY = "QUOTE_QRY";
	public static final String STATUS_QUERY = "STAT_QRY";
	public static final String CHAT = "CHAT";
	public static final String CREQ = "CHAT_REQ";
	public static final String CRESP = "CHAT_RESP";
        public static final String STROKE = "STROKE";

	public String type;
	public String subject;
        public String orderStatus = this.NEW; // default value

        /**
         * Order Status
         */
        public static final String NEW = "NEW";
        public static final String PENDACCEPT = "PENDACCEPT";
        public static final String ACCECPTED = "ACCEPTED";
        public static final String PARTIALFILLED = "PARTIALFILLED";
        public static final String FILLED = "FILLED";
        public static final String REJECTED = "REJECTED";
        public static final String CLOSED = "CLOSED";
        public static final String PENDCXL = "PENDCXL";        
        public static final String PENDRPL = "PENDRPL";        
        public static final String REPLACED = "REPLACED";        

	/**
	 * Derived class must override this and return TIBCO message
	 * corresponding to this object.
	 * @return TibrvMsg TIBRvMessage
	 */
	abstract public FixMessage getFixMsg();
	/**
	 * Derived class must override this and return TIBCO message
	 * subject name
	 * @return String subject name of this message
	 */

	abstract public String getSubject();
	/**
	 * Derived class must override this and return TIBCO message
	 * type for this object.
	 * @return String message type
	 */

	abstract public String getType();
	/**
	 * Derived class must override this and provide print
	 * details of the object.
	 *
	 */

	abstract public void print();
	/**
	 * Derived class must override this and return String
	 * representation corresponding to this object.
	 * @return String Object details
	 */

	abstract public String toString();
/**
 * Insert the method's description here.
 * Creation date: (2/3/2002 2:28:38 PM)
 * @return java.lang.String
 * @param fixSide java.lang.String
 */
public static String translateFromFixBuyOrSell(String fixSide)
{
	String buyOrSell = null;
	if(fixSide.equals("1"))
	{
		buyOrSell = IBusinessObject.BUY;
	}
	else if(fixSide.equals("2"))
	{
		buyOrSell = IBusinessObject.SELL;
	}

	return buyOrSell;
}
/**
 * Insert the method's description here.
 * Creation date: (2/3/2002 2:20:06 PM)
 * @return java.lang.String
 * @param fixOrdType java.lang.String
 */
public static String translateFromFixOrderType(String fixOrdType)
{
	String ordType = null;
	if( fixOrdType.equals("1") )
	{
		ordType = IBusinessObject.MKT;
	}
	else if( fixOrdType.equals("2") )
	{
		ordType = IBusinessObject.LMT;
	}
	else if( fixOrdType.equals("3") )
	{
		ordType = IBusinessObject.STP;
	}
	else if( fixOrdType.equals("4") )
	{
		ordType = IBusinessObject.STL;
	}
	else if( fixOrdType.equals("5") )
	{
		ordType = IBusinessObject.MIT;
	}
	else if( fixOrdType.equals("K") )
	{
		ordType = IBusinessObject.MKTL;
	}	


	return ordType;
}
/**
 * Insert the method's description here.
 * Creation date: (2/3/2002 2:32:03 PM)
 * @return java.lang.String
 * @param fixTIF java.lang.String
 */
public static String translateFromFixTimeInForce(String fixTIF)
{
	String tif = null;
	if(fixTIF.equals("0"))
	{
		tif = IBusinessObject.TIF_DAY;
	}
	else if(fixTIF.equals("1"))
	{
		tif = IBusinessObject.TIF_GTC;
	}
	else if(fixTIF.equals("3"))
	{
		tif = IBusinessObject.TIF_FAK;
	}
	else if(fixTIF.equals("6"))
	{
		tif = IBusinessObject.TIF_GTD;
	}	


	return tif;
}
/**
 * Insert the method's description here.
 * Creation date: (2/3/2002 2:28:38 PM)
 * @return java.lang.String
 * @param fixSide java.lang.String
 */
public static String translateToFixBuyOrSell(String buyOrSell)
{
	String fixSide = null;
	if(buyOrSell.equals(IBusinessObject.BUY))
	{
		fixSide = "1";
	}
	else if(buyOrSell.equals(IBusinessObject.SELL))
	{
		fixSide = "2";
	}

	return fixSide;
}
/**
 * Insert the method's description here.
 * Creation date: (2/3/2002 2:20:06 PM)
 * @return java.lang.String
 * @param fixOrdType java.lang.String
 */
public static String translateToFixOrderType(String ordType)
{
	String fixOrdType = null;
	if( ordType.equals(IBusinessObject.MKT) )
	{
		fixOrdType = "1";
	}
	else if( ordType.equals(IBusinessObject.LMT) )
	{
		fixOrdType = "2";
	}
	else if( ordType.equals(IBusinessObject.STP) )
	{
		fixOrdType = "3";
	}
	else if( ordType.equals(IBusinessObject.STL) )
	{
		fixOrdType = "4";
	}
	else if( ordType.equals(IBusinessObject.MIT) )
	{
		fixOrdType = "5";
	}
	else if( ordType.equals(IBusinessObject.MKTL) )
	{
		fixOrdType = "K";
	}
	


	return fixOrdType;
}
/**
 * Insert the method's description here.
 * Creation date: (2/3/2002 2:32:03 PM)
 * @return java.lang.String
 * @param fixTIF java.lang.String
 */
public static String translateToFixTimeInForce(String tif)
{
	String fixTIF = null;
	if(tif.equals(IBusinessObject.TIF_DAY))
	{
		fixTIF = "0";;
	}
	else if(tif.equals(IBusinessObject.TIF_GTC))
	{
		fixTIF = "1";
	}
	else if(tif.equals(IBusinessObject.TIF_FAK))
	{
		fixTIF = "3";
	}
	else if(tif.equals(IBusinessObject.TIF_GTD))
	{
		fixTIF = "6";
	}
	


	return fixTIF;
}
}
