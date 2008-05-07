package com.exsys.common.trading;

/*
 * This sample uses JNDI to retrieve administered objects.
 *
 * Optionally all parameters hardcoded in this sample can be
 * read from the jndi.properties file.
 *
 */

import javax.jms.*;
import javax.naming.*;
import java.util.Hashtable;
import java.applet.Applet;


public class JMSUtilities
{
    static Context jndiContext = null;

    static final String  providerContextFactory =
                            "com.tibco.tibjms.naming.TibjmsInitialContextFactory";

    static final String  defaultProtocol = "tibjmsnaming";

    static final String  defaultProviderURL =
                            defaultProtocol + "://localhost:7222";

    public static void initAppletJNDI(String providerURL, 
	    							  String userName, 
	    							  String password,
	    							  Applet applet) throws NamingException
    {
        if (jndiContext != null)
            return;
            
         System.out.println("JNDI Context is null ");

        if (providerURL == null || (providerURL.length() == 0))
            providerURL = defaultProviderURL;
        
        try
        {
	            System.out.println(" Before New Hashtable");
                Hashtable env = new Hashtable();
                env.put(Context.APPLET,applet );
	            System.out.println("After new Hashtable ");
           		env.put(Context.OBJECT_FACTORIES,"test");
           		env.put(Context.STATE_FACTORIES,"test");
           		env.put(Context.URL_PKG_PREFIXES,"javax");
           		env.put(Context.DNS_URL,"test");
           		env.put("java.naming.factory.control","test");
           		env.put("java.home","test");
	            //env.put 
                //env.put(Context.INITIAL_CONTEXT_FACTORY,providerContextFactory);
	            //System.out.println(" ");                
                //env.put(Context.PROVIDER_URL,providerURL);
	            //System.out.println(" ");                
                /*
                if (userName != null) {

                    env.put(Context.SECURITY_PRINCIPAL,userName);
                    
                    if (password != null)
                        env.put(Context.SECURITY_CREDENTIALS,password);
                }
                */
                
	            System.out.println("Before new Initial Context ");                
                jndiContext = new InitialContext(env);
	            System.out.println(" After new initial context");                
        }
        catch (NamingException e)
        {
                System.out.println("Failed to create JNDI InitialContext with provider URL set to "+
                                providerURL+", error = "+e.toString());
                throw e;
        }
    }
    public static void initJNDI(String providerURL) throws NamingException
    {
        initJNDI(providerURL,null,null);
    }
    public static void initJNDI(String providerURL, String userName, String password) throws NamingException
    {
        if (jndiContext != null)
            return;

        if (providerURL == null || (providerURL.length() == 0))
            providerURL = defaultProviderURL;
        
        try
        {
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY,providerContextFactory);
                env.put(Context.PROVIDER_URL,providerURL);
                
                if (userName != null) {

                    env.put(Context.SECURITY_PRINCIPAL,userName);
                    
                    if (password != null)
                        env.put(Context.SECURITY_CREDENTIALS,password);
                }

                jndiContext = new InitialContext(env);
        }
        catch (NamingException e)
        {
                System.out.println("Failed to create JNDI InitialContext with provider URL set to "+
                                providerURL+", error = "+e.toString());
                throw e;
        }
    }
    public static Object lookup(String objectName, boolean doInitialize) throws NamingException
    {
        if (objectName == null)
            throw new IllegalArgumentException("null object name not legal");

        if (objectName.length() == 0)
            throw new IllegalArgumentException("empty object name not legal");

        /*
         * check if not initialized, then initialize
         * with default parameters
         */
         if(doInitialize )
        	initJNDI(null);

        /*
         * do the lookup
         */
            return jndiContext.lookup(objectName);

    }
}
