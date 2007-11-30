package com.exsys.application;

import com.exsys.common.exceptions.*;
import com.exsys.service.*;

/**
 * @author kreddy
 *
 * Base class for any application that is not a GUI application
 */
public class BaseApplication {
	String cfgFileName = null;
	String logonType = null;

/**
 * BaseApplication constructor
 */
public BaseApplication() {
	super();
}

/**
 * BaseApplication constructor
 * @param args
 * @throws ConfigFileNotFound
 */
public BaseApplication(String[] args) throws ConfigFileNotFound
{
	// parse arguments
	parseArgs(args);

	// initialize Configuration Service
	ConfigurationService.initialize( cfgFileName );

	// initialize Logger
	Logger.initialize( cfgFileName );
}
/**
 * This method helps parsing arguments
 * @param args
 */
public  void parseArgs(String[] args)
    {
        int i=0;
        System.out.println("In base pa");

        while(i < args.length)
        {
            if (args[i].compareTo("-config")==0)
            {
                if ((i+1) >= args.length) usage();
                cfgFileName = args[i+1];

            }
            else if (args[i].compareTo("-logon")==0)
            {
                if ((i+1) >= args.length) usage();
                logonType = args[i+1];

            }
            i += 2;
            /*
            else
            if (args[i].compareTo("-topic")==0)
            {
                if ((i+1) >= args.length) usage();
                topicName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-user")==0)
            {
                if ((i+1) >= args.length) usage();
                userName = args[i+1];
                i += 2;
            }
            else
            if (args[i].compareTo("-password")==0)
            {
                if ((i+1) >= args.length) usage();
                password = args[i+1];
                i += 2;
            }

            else
            {
                System.err.println("Unrecognized parameter: "+args[i]);
                usage();
            }*/
        }
    }
    /**
     * method to displau usage info
     */
    void usage()
    {
        System.err.println("\nUsage: java TestApplication [options]");
        System.err.println("");
        System.err.println("   where options are:");
        System.err.println("");
        System.err.println(" -config   <config file> - Config File ");
        System.exit(0);
    }

	/**
	 * method to get logontype
	 * @return String logonType
	 */
	public String getLogonType() {
		return logonType;
	}
	
	/**
	 * method to set logonType
	 * @param String logonType
	 */
	public void setLogonType(String logonType) {
		this.logonType = logonType;
	}
}
