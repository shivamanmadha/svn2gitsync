package com.exsys.mktdata.message;

import java.util.*;
import com.exsys.common.exceptions.*;
/**
 * This class is used to hold lookup hash maps
 * for different RLC messages
 * Creation date: (11/18/01 10:13:09 PM)
 * @author: Administrator
 */
public class RLCLookup
{
	public static Hashtable rlcTagMap = new Hashtable();
	public static Hashtable valueMap_TrendFlag = new Hashtable();
	public static Hashtable valueMap_TradeTrendFlag = new Hashtable();
	public static Hashtable valueMap_Variation = new Hashtable();
	public static Hashtable valueMap_TradingStatusFlag = new Hashtable();
	public static Hashtable valueMap_InstrumentReservationOrderFlag = new Hashtable();
	public static Hashtable valueMap_InstrumentState = new Hashtable();
	public static Hashtable valueMap_InstrumentState_ED = new Hashtable();
	public static Hashtable valueMap_InstrumentGroupStatus = new Hashtable();
	public static Hashtable valueMap_InstrumentGroupStatus_ED = new Hashtable();
	public static Hashtable valueMap_InstrumentType = new Hashtable();
	public static Hashtable valueMap_DefaultDateValidity = new Hashtable();
	public static Hashtable valueMap_PriceCalculationIndicator = new Hashtable();
	public static Hashtable valueMap_TickDisplayFormatType = new Hashtable();

	public static Hashtable valueMap_ReferenceSettlementPriceIndicator = new Hashtable();
	public static Hashtable valueMap_MatchingAlgorithmIndicator = new Hashtable();
	public static Hashtable valueMap_ImpliedSpreadIndicator = new Hashtable();
	public static Hashtable valueMap_Side = new Hashtable();
	public static Hashtable valueMap_LastPriceType = new Hashtable();
	public static Hashtable valueMap_QuoteType = new Hashtable();
	public static Hashtable valueMap_TradeTypeIndicator = new Hashtable();
	
	public static Hashtable valueMap_PriceTypeFlag = new Hashtable();
	public static Hashtable valueMap_LastPriceType_M7 = new Hashtable();	
	public static Hashtable valueMap_ExtendedInstrumentType = new Hashtable();
	
	public static Hashtable valueMap_TradingMode = new Hashtable();
	
	
	public static Hashtable valueMap_MessageType = new Hashtable();
	

	static
	{
		
		//MessageType
		valueMap_MessageType.put("M9","Opening Summary");
		valueMap_MessageType.put("MG","Instrument Status");
		valueMap_MessageType.put("MH","Trading Day Timetables");
		valueMap_MessageType.put("MJ","Group State Change Marker");
		valueMap_MessageType.put("MI","Immediate Threshold");
		valueMap_MessageType.put("MK","Start of CME Globex Platform");
		valueMap_MessageType.put("ML","Start of Instrument Referential Broadcasing");
		valueMap_MessageType.put("MM","End of Instrument Referential Broadcasing");
		valueMap_MessageType.put("MO","Creation of Instrument Characteristics");
		valueMap_MessageType.put("MU","Security Definition");
		valueMap_MessageType.put("09","Heartbeat Message");
		valueMap_MessageType.put("23","SPI Message");
		valueMap_MessageType.put("M4","Request For Quote");
		valueMap_MessageType.put("MA","5 Best Limits");
		valueMap_MessageType.put("MX","Best Indicative Price");
		valueMap_MessageType.put("MY","Modification to the First 5 Implied Orders");
		valueMap_MessageType.put("M0","Last Best Price");
		valueMap_MessageType.put("M5","Opening Trade");
		valueMap_MessageType.put("M6","Trade");
		valueMap_MessageType.put("M7","Price");
		valueMap_MessageType.put("M8","Theoretical Opening Price");
						
				

		rlcTagMap.put("MessageType",valueMap_MessageType);
		
		
		//TrendFlag
		valueMap_TrendFlag.put("01","1st Opening");
		valueMap_TrendFlag.put("07","Other");

		rlcTagMap.put("TrendFlag",valueMap_TrendFlag);

		//TradeTrendFlag
		valueMap_TradeTrendFlag.put("00","Cancelling");
		valueMap_TradeTrendFlag.put("07","Trading");

		rlcTagMap.put("TradeTrendFlag",valueMap_TradeTrendFlag);



		//Variation
		valueMap_Variation.put("+","greater");
		valueMap_Variation.put("-","less than");
		valueMap_Variation.put("0","no variation");

	    rlcTagMap.put("Variation",valueMap_Variation);

		//TradingStatusFlag
		valueMap_TradingStatusFlag.put("P","Simple Reserved");
		valueMap_TradingStatusFlag.put("H","Reserved Upwards (Stop Spike)");
		valueMap_TradingStatusFlag.put("B","Reserved Downward");
		valueMap_TradingStatusFlag.put("S","Suspended (Stop Spike)");
		valueMap_TradingStatusFlag.put("R","Resume (Stop Spike)");
		valueMap_TradingStatusFlag.put(" ","Unchanged or Open (Stop Spike)");

		rlcTagMap.put("TradingStatusFlag",valueMap_TradingStatusFlag);


		//InstrumentReservationOrderFlag
		valueMap_InstrumentReservationOrderFlag.put("A","Automatic");
		valueMap_InstrumentReservationOrderFlag.put("M","Manual");
		valueMap_InstrumentReservationOrderFlag.put(" ","Unchanged");

	    rlcTagMap.put("InstrumentReservationOrderFlag",valueMap_InstrumentReservationOrderFlag);

		//InstrumentState
		valueMap_InstrumentState.put("AR","Instrument reserverd, similar to preopen");
		valueMap_InstrumentState.put("AS","Instrument suspended, may only go from resered to open");
		valueMap_InstrumentState.put("AG","Instrument does not accept orders or cancels-Frozen");
		valueMap_InstrumentState.put("A ","Open");
		valueMap_InstrumentState.put("IR","No order entry or cancels allowed");
		valueMap_InstrumentState.put("IS","No order entry or cancels allowed");
		valueMap_InstrumentState.put("IG","No order entry or cancels allowed");
		valueMap_InstrumentState.put("I ","Forbidden open. No order entry or cancels allowed");

	    rlcTagMap.put("InstrumentState",valueMap_InstrumentState);


		// specific to Euro Dollar Option
		valueMap_InstrumentState_ED.put("IG","Pause/Close.");
		valueMap_InstrumentState.put("AP","PreCross for RFC orders");
		valueMap_InstrumentState.put("AX","Cross for RFC orders");
		rlcTagMap.put("InstrumentState_ED",valueMap_InstrumentState_ED);


		//InstrumentGroupStatus
		valueMap_InstrumentGroupStatus.put("C","Start of Consultation");
		valueMap_InstrumentGroupStatus.put("P","Pre-Opening");
		valueMap_InstrumentGroupStatus.put("O","Opening");
		valueMap_InstrumentGroupStatus.put("E","Intervention before Opening");
		valueMap_InstrumentGroupStatus.put("S","Continuous Trading");
		valueMap_InstrumentGroupStatus.put("M","Reset of statistical data for trading session");
		valueMap_InstrumentGroupStatus.put("N","Surveillance intervention");
		valueMap_InstrumentGroupStatus.put("F","End of Session");
		valueMap_InstrumentGroupStatus.put("I","Forbidden");
		valueMap_InstrumentGroupStatus.put("Z","Interrupted");
		valueMap_InstrumentGroupStatus.put("T","Transient");		

  	    rlcTagMap.put("InstrumentGroupStatus",valueMap_InstrumentGroupStatus);

		//InstrumentGroupStatus_ED
		valueMap_InstrumentGroupStatus_ED.put("S","Open");
		valueMap_InstrumentGroupStatus_ED.put("M","Close (stats reset)");
		valueMap_InstrumentGroupStatus_ED.put("I","Close");
		valueMap_InstrumentGroupStatus_ED.put("N","Pause");

  	    rlcTagMap.put("InstrumentGroupStatus_ED",valueMap_InstrumentGroupStatus_ED);


		//InstrumentType
		valueMap_InstrumentType.put("F","Future");
		valueMap_InstrumentType.put("C","American Call");
		valueMap_InstrumentType.put("P","American Put");
		valueMap_InstrumentType.put("D","CME Spread");
		valueMap_InstrumentType.put("X","European Call");
		valueMap_InstrumentType.put("Y","European Put");
		valueMap_InstrumentType.put("!","not defined correctly");


  	    rlcTagMap.put("InstrumentType",valueMap_InstrumentType);

		//DefaultDateValidity
		valueMap_DefaultDateValidity.put("J","Day");
		valueMap_DefaultDateValidity.put("E","Expiration");

  	    rlcTagMap.put("DefaultDateValidity",valueMap_DefaultDateValidity);

		//PriceCalculationIndicator
		valueMap_PriceCalculationIndicator.put(" ","Normal Settlement");
		valueMap_PriceCalculationIndicator.put("N","No Settle (all spreads)");
		valueMap_PriceCalculationIndicator.put("C","Cabinet");
		valueMap_PriceCalculationIndicator.put("!","not defined correctly");

  	    rlcTagMap.put("PriceCalculationIndicator",valueMap_PriceCalculationIndicator);

		//TickDisplayFormatType
		valueMap_TickDisplayFormatType.put("01","Full tick ");
		valueMap_TickDisplayFormatType.put("02","1/2 tick displayed in 1 decimal(0,5)");
		valueMap_TickDisplayFormatType.put("04","1/4 tick displayed in 1 decimal(0,2,5,7)");
		valueMap_TickDisplayFormatType.put("08","1/8 tick displayed in decimal(0-7)");
		valueMap_TickDisplayFormatType.put("16","1/16 tick displayed in 2 decimals(00-15)");
		valueMap_TickDisplayFormatType.put("32","1/32 tick displayed in 2 decimals(00-31)");
		valueMap_TickDisplayFormatType.put("64","1/64 tick displayed in 2 decimals(00-63)");
		valueMap_TickDisplayFormatType.put("EH","1/32 half tick displayed in 3 decimals");
		valueMap_TickDisplayFormatType.put("EQ","m1/32 quater tick");
		valueMap_TickDisplayFormatType.put("FH","1/64 half tick");

  	    rlcTagMap.put("TickDisplayFormatType",valueMap_TickDisplayFormatType);

  	    //ReferenceSettlementPriceIndicator
		valueMap_ReferenceSettlementPriceIndicator.put("0","ReferencePrice");
		valueMap_ReferenceSettlementPriceIndicator.put("1","SettlementPrice");

  	    rlcTagMap.put("ReferenceSettlementPriceIndicator",valueMap_ReferenceSettlementPriceIndicator);

		//MatchingAlgorithmIndicator
		valueMap_MatchingAlgorithmIndicator.put(" ","FIFO");
		valueMap_MatchingAlgorithmIndicator.put("F","FIFO");
		valueMap_MatchingAlgorithmIndicator.put("A","Allocation");
		valueMap_MatchingAlgorithmIndicator.put("C","Currency Calendar");
		valueMap_MatchingAlgorithmIndicator.put("T","Lead Market Maker W/TOP");
		valueMap_MatchingAlgorithmIndicator.put("S","Lead Market Maker WO/TOP");
		valueMap_MatchingAlgorithmIndicator.put("X","LMM Pro Rata");
		valueMap_MatchingAlgorithmIndicator.put("Y","LMM Pro Rata W/TOP Percentage");
		valueMap_MatchingAlgorithmIndicator.put("N","Lead Market Maker W/TOP for NYMEX");

  	    rlcTagMap.put("MatchingAlgorithmIndicator",valueMap_MatchingAlgorithmIndicator);

		//ImpliedSpreadIndicator
		valueMap_ImpliedSpreadIndicator.put("1","Implied Instrument that provides leg messages");
		valueMap_ImpliedSpreadIndicator.put("2","Non-implied instrument but leg messages are sent");
		valueMap_ImpliedSpreadIndicator.put(" ","Non-implied instrument (no leg messages are sent)");

  	    rlcTagMap.put("ImpliedSpreadIndicator",valueMap_ImpliedSpreadIndicator);

		//ExtendedInstrumentType
		valueMap_ExtendedInstrumentType.put("E","Equity");
		valueMap_ExtendedInstrumentType.put("C","Currency");
		valueMap_ExtendedInstrumentType.put("I","Interest Rate");
		valueMap_ExtendedInstrumentType.put("A","Agriculture");
		valueMap_ExtendedInstrumentType.put("M","Alternative Markets");
		valueMap_ExtendedInstrumentType.put("G","NYMEX");

  	    rlcTagMap.put("ExtendedInstrumentType",valueMap_ExtendedInstrumentType);

  	    //Side
		valueMap_Side.put("A","Buy");
		valueMap_Side.put("V","Sell");
		valueMap_Side.put("X","Cross Request");
		valueMap_Side.put(" ","Two sided");

  	    rlcTagMap.put("Side",valueMap_Side);
  	    
  	    //LastPriceType
		valueMap_LastPriceType.put("A","Best Buy Limit");
		valueMap_LastPriceType.put("V","Best Sell Limit");
		valueMap_LastPriceType.put("I","Theoretical Opening Price");
		valueMap_LastPriceType.put("S","Reference Price");

  	    rlcTagMap.put("LastPriceType",valueMap_LastPriceType);  	    

		//LastPriceType_M7
		valueMap_LastPriceType_M7.put("30","First Price Traded");
		valueMap_LastPriceType_M7.put("31","Highest Price Traded");
		valueMap_LastPriceType_M7.put("32","Lowest Price Traded");
		valueMap_LastPriceType_M7.put("33","Last Price Traded");
		valueMap_LastPriceType_M7.put("34","Prev. day's adjusted closing price");
		valueMap_LastPriceType_M7.put("35","Settlement price");
		
		rlcTagMap.put("LastPriceType_M7",valueMap_LastPriceType_M7);  
		
  	    //QuoteType
		valueMap_QuoteType.put("T","Tradable Quote or Cross Trade Request");
		valueMap_QuoteType.put("I","Indicative Quote");

  	    rlcTagMap.put("QuoteType",valueMap_QuoteType);

		//TradeTypeIndicator
		valueMap_TradeTypeIndicator.put("0","Normal Trade");
		valueMap_TradeTypeIndicator.put("2","Normal Trade for spread leg");
		valueMap_TradeTypeIndicator.put("3","Ex-Pit");
		valueMap_TradeTypeIndicator.put("4","Ex-Pit for spread leg");

  	    rlcTagMap.put("TradeTypeIndicator",valueMap_TradeTypeIndicator);

		//PriceTypeFlag
		valueMap_PriceTypeFlag.put("30","first price traded");
		valueMap_PriceTypeFlag.put("31","highest price traded");
		valueMap_PriceTypeFlag.put("32","lowest price traded");
		valueMap_PriceTypeFlag.put("33","last price traded");
		valueMap_PriceTypeFlag.put("34","prev day's adjusted closing price");
		valueMap_PriceTypeFlag.put("35","Settlement Price");

  	    rlcTagMap.put("PriceTypeFlag",valueMap_PriceTypeFlag);

		//TradingMode
		valueMap_TradingMode.put("0","Pre-opening Mode");
		valueMap_TradingMode.put("1","Opening Mode");
		valueMap_TradingMode.put("2","Continuous Trading Mode");

  	    rlcTagMap.put("TradingMode",valueMap_TradingMode);

	}



/**
 * RLCLookup constructor
 */
public RLCLookup() {
	super();

}

/**
 * lookupTagValue is used to lookup value of a key
 * for a particular tag
 * @param tag
 * @param key
 * @return
 */
public static String lookupTagValue(String tag, String key)
{
	String value = null;
	if(rlcTagMap.containsKey(tag))
	{
		return (String)(((Hashtable)rlcTagMap.get(tag)).get(key));
	}

	return null;
}

}
