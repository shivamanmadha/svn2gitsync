package com.exsys.application;

import javax.swing.*;

import com.exsys.common.exceptions.*;
import com.exsys.service.*;

/**
 * @author kreddy
 *
 * Base class for any GUI application 
 */
public class BaseGUIApplication extends JPanel{
	String cfgFileName = null;

/**
 * BaseGUIApplication constructor
 */
public BaseGUIApplication() {
	super();
}

/**
 * BaseGUIApplication constructor
 * @param args
 * @throws ConfigFileNotFound
 */
public BaseGUIApplication(String[] args) throws ConfigFileNotFound
{
	// parse arguments
	parseArgs(args);

	// initialize Configuration Service
	ConfigurationService.initialize( cfgFileName );

	// initialize Logger
	Logger.initialize( cfgFileName );
}
    /**
     * method to help parsing arguments
     * @param args
     */
    void parseArgs(String[] args)
    {
        int i=0;

        while(i < args.length)
        {
            if (args[i].compareTo("-config")==0)
            {
                if ((i+1) >= args.length) usage();
                cfgFileName = args[i+1];
                i += 2;
            }
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
            */
            else
            {
                System.err.println("Unrecognized parameter: "+args[i]);
                usage();
            }
        }
    }
    /**
     * method to display usage info
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
}
