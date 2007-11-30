/*
 * Created on Mar 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.exsys.orderentry;
import com.exsys.impact.mdf.message.response.*;
import com.exsys.impact.mdf.message.*;

import java.util.HashMap;
//import java.util.Iterator;
/**
 * @author kreddy
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImpactSDResponseHandler {

	private HashMap sdResponses = null;

	public ImpactSDResponseHandler()
	{
	}

	public void processSDResponse(String symbol, ProductDefinitionResponse sdResponse)
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



	public ProductDefinitionResponse getProductDefinitionResponse( String symbol )
	{
		 if( sdResponses == null || sdResponses.get(symbol) == null)
		 	return null;
		return ((ProductDefinitionResponse)sdResponses.get(symbol));

	}

	public String getDescription( String symbol )
	{
		 if( sdResponses == null || sdResponses.get(symbol) == null)
		 	return null;
		ProductDefinitionResponse rg = (ProductDefinitionResponse)sdResponses.get(symbol);
  				String info = "       Symbol =" + String.valueOf(rg.MarketID);
  				info += " "+MessageUtil.toString(rg.MarketDesc);
  				info += " "+String.valueOf(rg.MaturityMonth);
  				info += " "+String.valueOf(rg.MaturityYear);
  				info += " "+String.valueOf(rg.MaturityDay);


		return info;

	}


}
