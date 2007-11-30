/*
 * Created on Mar 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.exsys.orderentry;
import com.exsys.fix.message.*;

import java.util.HashMap;
//import java.util.Iterator;
/**
 * @author kreddy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SDResponseHandler {

	private HashMap sdResponses = null;

	public SDResponseHandler()
	{
	}

	public void processSDResponse(String symbol, FixRGNoRelatedSym sdResponse)
	{
		if( sdResponses == null)
		{
			sdResponses = new HashMap(3);
		}

		if( !sdResponses.containsKey(symbol))
		{
			sdResponses.put(symbol,sdResponse);
		}
	}



	public FixRGNoRelatedSym getFixRGNoRelatedSym( String symbol )
	{
		 if( sdResponses == null || sdResponses.get(symbol) == null)
		 	return null;
		return ((FixRGNoRelatedSym)sdResponses.get(symbol));

	}

	public String getDescription( String symbol )
	{
		 if( sdResponses == null || sdResponses.get(symbol) == null)
		 	return null;
		FixRGNoRelatedSym rg = (FixRGNoRelatedSym)sdResponses.get(symbol);
  				String info = "       Symbol =" + rg.getUnderlyingSymbol();
  				info += " "+rg.getUnderlyingSecurityDes();
  				info += " "+rg.getUnderlyingMaturityMonthYearAsString();
  				info += " "+rg.getUnderlyingMaturityDayAsString();


		return info;

	}


}
