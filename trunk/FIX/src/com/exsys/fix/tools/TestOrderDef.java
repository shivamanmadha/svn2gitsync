package com.exsys.fix.tools;

/**
 * Insert the type's description here.
 * Creation date: (10/8/2002 11:24:46 PM)
 * @author: Administrator
 */
public class TestOrderDef {
	private java.lang.String symbol;
	private java.lang.String orderType;
	private java.lang.String timeInForce;
	private java.lang.String price;
	private java.lang.String buyOrSell;
/**
 * TestOrderDef constructor comment.
 */
public TestOrderDef() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 7:16:55 AM)
 * @return java.lang.String
 */
public String debugString() {
	StringBuffer returnString = new StringBuffer();
	
	returnString.append("Symbol: " + symbol );
	returnString.append("\n");

	returnString.append("Order Type: " + orderType );
	returnString.append("\n");

	returnString.append("Time In Force: " + timeInForce );
	returnString.append("\n");

	returnString.append("BuyOrSell: " + buyOrSell );
	returnString.append("\n");
	
	return returnString.toString();
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 7:18:52 AM)
 * @return java.lang.String
 */
public java.lang.String getBuyOrSell() {
	return buyOrSell;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:25:19 PM)
 * @return java.lang.String
 */
public java.lang.String getOrderType() {
	return orderType;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:26:01 PM)
 * @return java.lang.String
 */
public java.lang.String getPrice() {
	return price;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:25:06 PM)
 * @return java.lang.String
 */
public java.lang.String getSymbol() {
	return symbol;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:25:35 PM)
 * @return java.lang.String
 */
public java.lang.String getTimeInForce() {
	return timeInForce;
}
/**
 * Insert the method's description here.
 * Creation date: (10/9/2002 7:18:52 AM)
 * @param newBuyOrSell java.lang.String
 */
public void setBuyOrSell(java.lang.String newBuyOrSell) {
	buyOrSell = newBuyOrSell;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:25:19 PM)
 * @param newOrderType java.lang.String
 */
public void setOrderType(java.lang.String newOrderType) {
	orderType = newOrderType;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:26:01 PM)
 * @param newPrice java.lang.String
 */
public void setPrice(java.lang.String newPrice) {
	price = newPrice;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:25:06 PM)
 * @param newSymbol java.lang.String
 */
public void setSymbol(java.lang.String newSymbol) {
	symbol = newSymbol;
}
/**
 * Insert the method's description here.
 * Creation date: (10/8/2002 11:25:35 PM)
 * @param newTimeInForce java.lang.String
 */
public void setTimeInForce(java.lang.String newTimeInForce) {
	timeInForce = newTimeInForce;
}
}
