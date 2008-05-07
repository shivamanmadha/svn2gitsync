package com.exsys.fix.message;
/**
 * This class defines static string constants related to FIX protocol,
 * especialy, enumerated values for some of the fix tags
 */
public interface FixFieldConstants {
	
  public final static String SIDE_BUY = "1";
  public final static String SIDE_SELL = "2";



  public final static String PUT_CALL_CALL = "1";
  public final static String PUT_CALL_PUT = "0";


  public final static String ORDER_TYPE_MARKET = "1";
  public final static String ORDER_TYPE_LIMIT = "2";
  public final static String ORDER_TYPE_STOP = "3";
  public final static String ORDER_TYPE_STOPLIMIT = "4";
  public final static String ORDER_TYPE_MARKETLIMIT = "K";
  

  public final static String TIME_IN_FORCE_DAY = "0";  
  public final static String TIME_IN_FORCE_GTC = "1";
  public final static String TIME_IN_FORCE_FAK = "3";
  public final static String TIME_IN_FORCE_FOK = "4";
  public final static String TIME_IN_FORCE_GTD = "6";      
  
  public final static String HANDL_INST_AUTOMATED = "1";
  
  
  public final static String SECURITY_TYPE_FUTURE = "FUT";
  public final static String SECURITY_TYPE_OPTION = "OPT";  
  public final static String SECURITY_TYPE_FX = "FOR";


  public final static String CUSTOMER_OR_FIRM_CUSTOMER = "0";  
  public final static String CUSTOMER_OR_FIRM_FIRM = "1";

  public final static String CTI_CODE_BROKER_OWN_ACCOUNT = "1";
  public final static String CTI_CODE_BROKER_HOUSE_ACCOUNT = "2";
  public final static String CTI_CODE_BROKER_BROKER_ACCOUNT = "3";
  public final static String CTI_CODE_BROKER_CUSTOMER_ACCOUNT = "4";  
  
  public final static String FEE_BILLING_CBOE_MEMBER = "B";      
  public final static String FEE_BILLING_NON_MEMBER = "C";      
  public final static String FEE_BILLING_EQUITY_MEMBER = "E";      
  public final static String FEE_BILLING_106HJ_MEMBER = "H";      
  public final static String FEE_BILLING_LESSEE_MEMBER = "L";              


  public final static String CMTA_GIVEUP_CODE_GIVEUP = "GU";
  public final static String CMTA_GIVEUP_CODE_SGX_OFFSET = "SX";  

}

