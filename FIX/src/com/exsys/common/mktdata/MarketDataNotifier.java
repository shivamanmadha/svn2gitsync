package com.exsys.common.mktdata;


/**
 * @author kreddy
 *
 * This interface handles market data message notification
 */
public interface MarketDataNotifier {

  public void bestBid(String symbol,BookRow bidRow, boolean isFullRefresh);
  public void bestOffer(String symbol,BookRow offerRow, boolean isFullRefresh);
  public void lastTrade(String symbol,MarketDataTrade last, boolean isFullRefresh);    
  public void volume(String symbol,double vol,boolean isFullRefresh);
  public void openInterest(String symbol,double oi,boolean isFullRefresh);
  public void highPrice(String symbol,String high,boolean isFullRefresh);
  public void lowPrice(String symbol,String low,boolean isFullRefresh);  
  public void openPrice(String symbol,String price,boolean isFullRefresh);  
  public void closePrice(String symbol,String price,boolean isFullRefresh);  
  public void vwapPrice(String symbol,String price,boolean isFullRefresh);  
  public void settlementPrice(String symbol,String price,boolean isFullRefresh);          

}
