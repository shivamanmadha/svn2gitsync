package com.exsys.mktdata.cfg;

/**
 * Created on Feb 10, 2005
 */
public class ConfigurationBuilderFactory {
    public static ConfigurationBuilder createConfigurationBuilder() throws ConfigurationException {
        return new XMLConfigurationBuilder();
    }
}