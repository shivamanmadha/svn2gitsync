package com.exsys.mktdata.book;
import com.exsys.common.util.*;
import java.util.*;
import java.math.BigDecimal;

/**
*
* Created on Mar 27, 2006
*
* This class is used to represent a RLC MY book row
*/
public class RLCMYBookRow {

	public int buyLimitQty = 0;
	public BigDecimal buyLimitPrice = new BigDecimal(0.0);
	public int sellLimitQty = 0;
	public BigDecimal sellLimitPrice = new BigDecimal(0.0);



	/**
	 * RLCMYBookRow constructor
	 */
	public RLCMYBookRow()
	{
	}
	/**
	 * RLCMYBookRow constructor
	 * @param _buyLimitQty
	 * @param _buyLimitPrice
	 * @param _sellLimitQty
	 * @param _sellLimitPrice
	 */
	public RLCMYBookRow(int _buyLimitQty,
						BigDecimal _buyLimitPrice,
						int _sellLimitQty,
						BigDecimal _sellLimitPrice)
	{
			setData(_buyLimitQty,
					_buyLimitPrice,
					_sellLimitQty,
					_sellLimitPrice);


	}
	
	/**
	 * RLCMYBookRow constructor
	 * @param maRow
	 */
	public RLCMYBookRow(RLCMYBookRow maRow)
	{
			setData(maRow.buyLimitQty,
					maRow.buyLimitPrice,
					maRow.sellLimitQty,
					maRow.sellLimitPrice);


	}	
	/**
	 * RLCMYBookRow constructor
	 * @param maRow
	 */
	public RLCMYBookRow(RLCMABookRow maRow)
	{
			setData(maRow.buyLimitQty,
					maRow.buyLimitPrice,
					maRow.sellLimitQty,
					maRow.sellLimitPrice);


	}	
	/**
	 * method to set different attributes
	 * @param _buyLimitQty
	 * @param _buyLimitPrice
	 * @param _sellLimitQty
	 * @param _sellLimitPrice
	 */
	public void setData(int _buyLimitQty,
						BigDecimal _buyLimitPrice,
						int _sellLimitQty,
						BigDecimal _sellLimitPrice)
	{
		buyLimitQty = _buyLimitQty;
		buyLimitPrice = _buyLimitPrice;
		sellLimitQty = _sellLimitQty;
		sellLimitPrice = _sellLimitPrice;


	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "RLCMYBookRow - ";
		str += " BID - " +  buyLimitQty ;
		str += " PRICE - "+ buyLimitPrice;
		str += " ASK - " + sellLimitQty;
		str += " PRICe - " +  sellLimitPrice ;
		return str;
	}
	/**
	 * method to return header info
	 * @return
	 */
	public static String toHeaderString()
	{
		String str = StringUtilities.prePad("Bid Qty",' ',15);
		str += StringUtilities.prePad("Bid Price",' ',10);

		str += StringUtilities.prePad("Ask Qty",' ',15);
		str += StringUtilities.prePad("Ask Price",' ',10);

		return str;
	}	
	/**
	 * method to return book row as string
	 * @return
	 */
	public String toBookString()
	{
		String str = "";
		

		str +=  StringUtilities.prePad(String.valueOf(buyLimitQty),' ',15);
		
		if(buyLimitQty != 0)
		{
			str +=  StringUtilities.prePad(String.valueOf(buyLimitPrice),' ',10);
		}
		else
		{
			str +=  StringUtilities.prePad("0.00",' ',10);
		}		

		str += StringUtilities.prePad(String.valueOf(sellLimitQty),' ',15);
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
