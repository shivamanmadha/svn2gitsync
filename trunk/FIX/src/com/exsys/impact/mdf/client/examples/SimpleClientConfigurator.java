package com.exsys.impact.mdf.client.examples;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <code>SimpleClientConfigurator</code> contains the list of properties
 * used in the simple client.
 *
 * @author David Chen
 * @since 12/28/2006
 */

public class SimpleClientConfigurator
{
   private static Properties CLIENT_CONFIG_PROPS = new Properties();
   private static final String PROP_KEY_FEED_SERVER_ADDRESS       = "mdf.server.address";
   private static final String PROP_KEY_FEED_SERVER_PORT          = "mdf.server.port";
   private static final String PROP_KEY_CLIENT_USERANME           = "mdf.client.username";
   private static final String PROP_KEY_CLIENT_PASSWORD           = "mdf.client.password";
   private static final String PROP_KEY_MARKET_TYPES              = "mdf.client.interest.market.types";
   private static final String PROP_KEY_DATA_BUFFERING            = "mdf.data.buffering";
   private static final String PROP_KEY_GET_MESSAGE_BUNDLE_MARKER = "mdf.get_bundle_marker";
   private static final String PROP_KEY_GET_MARKET_STATS          = "mdf.get_market_stats";
   private static final String PROP_KEY_GET_IMPLIED_ORDERS = "mdf.get_implied_orders";
   private static final String PROP_KEY_CLIENT_VERSION            = "mdf.client.version";

   // Load properties from simpleClient.properties
   static
   {
      try
      {
         CLIENT_CONFIG_PROPS.load(new FileInputStream("simpleClient.properties"));
      }
      catch (IOException e)
      {
         System.out.println("Failed to load simpleClient.properties.");
         e.printStackTrace();
      }
   }

   private SimpleClientConfigurator()
   {
   }

   /**
    * Get the feed server IP address
    * @return the server IP address
    */
   public static String getServerAddress()
   {
      return CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_FEED_SERVER_ADDRESS);
   }

   /**
    * Get the feed server port
    * @return the port number
    */
   public static int getServerPort()
   {
      return Integer.valueOf(CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_FEED_SERVER_PORT)).intValue();
   }

   /**
    * Get user name for login
    * @return the user name
    */
   public static String getUserName()
   {
      return CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_CLIENT_USERANME);
   }

   /**
    * Get the password for login
    * @return the password
    */
   public static String getPassword()
   {
      return CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_CLIENT_PASSWORD);
   }

   /**
    * Get the market types that we are interested in
    * @return the array of market types
    */
   public static int[] getMarketTypes()
   {
      String mktTypes = CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_MARKET_TYPES);
      String[] list = mktTypes.split(",");
      int[] IDs = new int[list.length];
      for (int i=0; i<list.length; i++)
      {
         IDs[i] = Integer.valueOf(list[i]).intValue();
      }

      return IDs;
   }

   /**
    * Check if we want the feed server to do data buffering for us
    * to avoid too many small packets and save bandwidth
    *
    * @return the flag
    */
   public static boolean isMktDataBuffering()
   {
      return "true".equals(CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_DATA_BUFFERING));
   }

   /**
    * Check if we want to get market statistics update in the data feed
    *
    * @return the flag
    */
   public static boolean getMarketStats()
   {
      return "true".equals(CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_GET_MARKET_STATS));
   }

   /**
    * Check if we want to get message bundle marker in the data feed or not
    *
    * @return the flag
    */
   public static boolean getMessageBundleMarker()
   {
      return "true".equals(CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_GET_MESSAGE_BUNDLE_MARKER));
   }

   /**
    * Check if we want to get implied orders in the data feed or not
    *
    * @return the flag
    */
   public static boolean getImpliedOrders()
   {
      return !"false".equals(CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_GET_IMPLIED_ORDERS));
   }


   /**
    * Get the client version
    * @return the version
    */
   public static String getClientVersion()
   {
      return CLIENT_CONFIG_PROPS.getProperty(PROP_KEY_CLIENT_VERSION);
   }
}
