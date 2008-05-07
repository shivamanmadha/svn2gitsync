package com.exsys.common.mktdata;
import java.util.StringTokenizer;
/**
 * @author kreddy
 *
 * This class is used to hold info for tick size
 * for a symbol
 */
public class TickSize   {
	int m_tickType;
	int m_tickSize;

	/**
	 * Constructor for TickSize
	 */
	public TickSize()
	{
	}
	/**
	 * Constructor for TickSize
	 * @param tickType
	 * @param tickSize
	 */
	public TickSize(int tickType, int tickSize) {
		super();
		m_tickType = tickType;
		m_tickSize = tickSize;
	}


}

