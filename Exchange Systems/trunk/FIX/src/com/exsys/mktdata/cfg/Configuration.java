package com.exsys.mktdata.cfg;

/**
 * Created on Feb 10, 2005
 */
public interface Configuration {
    /**
     *
     * @param the attribute name
     * @return the attribute value
     * @throws ConfigurationException if name attribute does not exist
     */
    public String getAttribute(String name) throws ConfigurationException;
    public String getAttribute(String name, String defalutValue);
    public boolean getAttributeAsBoolean(String name) throws ConfigurationException;
    public boolean getAttributeAsBoolean(String name, boolean defaultValue);
    public int getAttributeAsInteger(String name) throws ConfigurationException;
    public int getAttributeAsInteger(String name, int defaultValue) throws ConfigurationException;
//    public float getAttributeAsFloat(String name);
//    public float getAttributeAsFloat(String name, String defalutValue);
// 	  ...
    public String[] getAttributeNames();
    /**
     *
     * @param name the name of the child configuration element
     * @return the child configuration
     * @throws ConfigurationException if the child with name does not exist.
     */
    public Configuration getChild(String name) throws ConfigurationException;
    /**
     *
     * @param name the name of the child configuration elements
     * @return an array of configuration objects for all children of name
     * @throws ConfigurationException if no child exists with name
     */
    public Configuration[] getChildren(String name) throws ConfigurationException;
    /**
     *
     * @param name
     * @return true if this configuration has a child element with name "name"
     */
    public boolean hasChild(String name);
    public String getName();
    public String getValue() throws ConfigurationException;
//    public boolean getValueAsBoolean();
//    public boolean getValueAsBoolean(boolean);
//    	...
    public long getValueAsLong() throws ConfigurationException;
    public long getValueAsLong(long defaultValue) throws ConfigurationException;

    public int getValueAsInteger() throws ConfigurationException;
    public int getValueAsInteger(int defaultValue) throws ConfigurationException;



}
