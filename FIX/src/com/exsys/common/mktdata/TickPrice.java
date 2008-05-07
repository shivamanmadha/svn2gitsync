package com.exsys.common.mktdata;
import java.util.StringTokenizer;
/**
 * @author kreddy
 *
 * This class is used to hold info for tick price
 * for a symbol
 */
public class TickPrice   {

	int m_tickType;
	double m_tickPrice;

	/**
	 * Constructor for TickPrice
	 */
	public TickPrice()
	{
	}
	/**
	 * Constructor for TickPrice
	 * @param tickType
	 * @param tickPrice
	 */
	public TickPrice(int tickType, double tickPrice) {
		super();
		m_tickType = tickType;
		m_tickPrice = tickPrice;
	}



}

