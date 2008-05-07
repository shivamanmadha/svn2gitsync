package com.exsys.mktdata.book;
import com.exsys.common.util.*;
import java.util.*;
import java.math.BigDecimal;

/**
 *
 * Created on Mar 27, 2006
 *
 * This class is used to represent a RLC MA book row
 */
public class RLCMABookRow {

	public int numBuys = 0;
	public int buyLimitQty = 0;
	public BigDecimal buyLimitPrice = new BigDecimal(0.0);
	public int numSells = 0;
	public int sellLimitQty = 0;
	public BigDecimal sellLimitPrice = new BigDecimal(0.0);



	/**
	 * RLCMABookRow constructor
	 */
	public RLCMABookRow()
	{
	}
	/**
	 * RLCMABookRow constructor
	 * @param _numBuys
	 * @param _buyLimitQty
	 * @param _buyLimitPrice
	 * @param _numSells
	 * @param _sellLimitQty
	 * @param _sellLimitPrice
	 */
	public RLCMABookRow(int _numBuys,
						int _buyLimitQty,
						BigDecimal _buyLimitPrice,
						int _numSells,
						int _sellLimitQty,
						BigDecimal _sellLimitPrice)
	{
		setData(_numBuys,
				_buyLimitQty,
				_buyLimitPrice,
				_numSells,
				_sellLimitQty,
				_sellLimitPrice);


	}
	/**
	 * method to set different attributes
	 * @param _numBuys
	 * @param _buyLimitQty
	 * @param _buyLimitPrice
	 * @param _numSells
	 * @param _sellLimitQty
	 * @param _sellLimitPrice
	 */
	public void setData(int _numBuys,
						int _buyLimitQty,
						BigDecimal _buyLimitPrice,
						int _numSells,
						int _sellLimitQty,
						BigDecimal _sellLimitPrice)
	{
		numBuys = _numBuys;
		buyLimitQty = _buyLimitQty;
		buyLimitPrice = _buyLimitPrice;
		numSells = _numSells;
		sellLimitQty = _sellLimitQty;
		sellLimitPrice = _sellLimitPrice;


	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "RLCMABookRow - ";
		str += " NUM BUYS - " +  numBuys ;
		str += " BID - " +  buyLimitQty ;
		str += " PRICE - "+buyLimitPrice;
		str += " NUM SELLS - " +  numSells ;
		str += " ASK - " + sellLimitQty;
		str += " PRICe - " +  sellLimitPrice ;
		return str;
	}

	/**
	 * method that prints header info
	 * @return
	 */
	public static String toHeaderString()
	{
		String str = StringUtilities.prePad("NumBuys",' ',15);

		str += StringUtilities.prePad("Bid Qty",' ',15);
		str += "   ";
		str += StringUtilities.prePad("Bid Price",' ',10);
		
		str += StringUtilities.prePad("NumSells",' ',15);

		str += StringUtilities.prePad("Ask Qty",' ',15);
		str += "   ";		
		str += StringUtilities.prePad("Ask Price",' ',10);

		return str;
	}
	/**
	 * method to return book row info as string
	 * @return
	 */
	public String toBookString()
	{
		String str = StringUtilities.prePad(String.valueOf(numBuys),' ',15);

		str += "   ";
		str += StringUtilities.prePad(String.valueOf(buyLimitQty),' ',15);
		
		if(buyLimitQty != 0)
		{
			str +=  StringUtilities.prePad(String.valueOf(buyLimitPrice),' ',10);
		}
		else
		{
			str +=  StringUtilities.prePad("0.00",' ',10);
		}
		str += "   ";
		str += StringUtilities.prePad(String.valueOf(numSells),' ',15);
		str += "   ";
		str += StringUtilities.prePad(String.valueOf(sellLimitQty),' ',15);
		str += "   ";
		if(sellLimitQty != 0)
		{
			str += StringUtilities.prePad(String.valueOf(sellLimitPrice),' ',10);		
		}
		else
		{
			str += StringUtilities.prePad("0.00",' ',10);
		}		

		return str;
	}
		
}
