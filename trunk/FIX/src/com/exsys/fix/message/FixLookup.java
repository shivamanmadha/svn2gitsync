package com.exsys.fix.message;

import java.util.*;
import com.exsys.common.exceptions.*;
/**
 * This class is a helper class to define look up tables for some of the
 * tags defined in fix protocol.
 * Creation date: (11/18/01 10:13:09 PM)
 * @author: Administrator
 */
public class FixLookup {
	public static Hashtable fixTagMap = new Hashtable();
	public static Hashtable valueMap_269 = new Hashtable();
	public static Hashtable valueMap_9175 = new Hashtable();
	public static Hashtable valueMap_326 = new Hashtable();
	public static Hashtable valueMap_279 = new Hashtable();
	public static Hashtable valueMap_54 = new Hashtable();
	public static Hashtable valueMap_201 = new Hashtable();
	public static Hashtable valueMap_40 = new Hashtable();
	public static Hashtable valueMap_59 = new Hashtable();

	static {
		//269 = MDEntryType
		valueMap_269.put("0", "Bid");
		valueMap_269.put("1", "Offer");
		valueMap_269.put("2", "Trade");
		valueMap_269.put("4", "Opening Price");
		valueMap_269.put("6", "Settlement Price");
		valueMap_269.put("7", "Trading Session High Price");
		valueMap_269.put("8", "Trading Session Low Price");
		valueMap_269.put("9", "Trading Session VWAP Price");
		valueMap_269.put("C", "Open Interest");

		fixTagMap.put("269", valueMap_269);

		//9175 = OrderState
		valueMap_9175.put("0", "Active");
		valueMap_9175.put("1", "Inactive");
		valueMap_9175.put("2", "Withdrawn");
		valueMap_9175.put("3", "Pending");
		valueMap_9175.put("4", "Consummated");
		valueMap_9175.put("5", "PreOpen");

		fixTagMap.put("9175", valueMap_9175);

		//326 = SecurityTradingStatus
		valueMap_326.put("2", "Trading Halt(Suspended)");
		valueMap_326.put("4", "No Open/No Resume(Pre-Close)");
		valueMap_326.put("17", "Ready to Trade (Open)");
		valueMap_326.put("18", "Not Available for Trading (Close)");
		valueMap_326.put("20", "Unknown or Invalid (Expired)");
		valueMap_326.put("21", "Pre-Open");

		fixTagMap.put("326", valueMap_326);

		//279 = MDUpdateAction
		valueMap_279.put("0", "New");
		valueMap_279.put("1", "Change");
		valueMap_279.put("2", "Delete");

		fixTagMap.put("279", valueMap_279);
		
		//54 = Side
		valueMap_54.put("1", "B");
		valueMap_54.put("2", "S");

		fixTagMap.put("54", valueMap_54);	
		
		//201 = putcall
		valueMap_201.put("0", "P");
		valueMap_201.put("1", "C");

		fixTagMap.put("201", valueMap_201);		

		//40 = order type
		valueMap_40.put("1", "MKT");
		valueMap_40.put("2", "LMT");
		valueMap_40.put("3", "STP");
		valueMap_40.put("4", "STL");
		valueMap_40.put("5", "MOC");		

		fixTagMap.put("40", valueMap_40);	

		//50 = order qualifier
		valueMap_59.put("0", "DAY");
		valueMap_59.put("1", "GTC");
		valueMap_59.put("3", "FAK");
		valueMap_59.put("4", "FOK");
		valueMap_59.put("6", "GTD");
				

		fixTagMap.put("59", valueMap_59);		
		
	}

	/**
	 * FixLookup constructor.
	 */
	public FixLookup() {
		super();

	}

	/**
	* method to get descriptive name for the given tag and key
	*
	* @return String - tag value description
	*/
	public static String lookupTagValue(String tag, String key) {
		String value = null;
		if(key != null)
		{
			if (fixTagMap.containsKey(tag)) {
				return (String) (((Hashtable) fixTagMap.get(tag)).get(key));
			}
		}

		return null;
	}

}