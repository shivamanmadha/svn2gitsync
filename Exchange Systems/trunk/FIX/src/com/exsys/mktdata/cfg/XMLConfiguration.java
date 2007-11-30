package com.exsys.mktdata.cfg;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created on Feb 10, 2005
 */
public class XMLConfiguration implements Configuration {
	private Element rootElement = null;

	protected XMLConfiguration(Element e) {
		this.rootElement = e;
	}

	/**
	 * @see com.cme.retransmission.cfg.Configuration#getAttribute(java.lang.String)
	 */
	public String getAttribute(String name) throws ConfigurationException {
		if (rootElement.hasAttribute(name))
			return rootElement.getAttribute(name);
		else
			throw new ConfigurationException("Attribute does not exist for " + getName() + " with name " + name);
	}

	/**
	 * @see com.cme.retransmission.cfg.Configuration#getAttribute(java.lang.String,
	 *      java.lang.String)
	 */
	public String getAttribute(String name, String defaultValue) {
		if (rootElement.hasAttribute(name))
			return rootElement.getAttribute(name);
		else
			return defaultValue;
	}

	public boolean getAttributeAsBoolean(String name) throws ConfigurationException {
		return Boolean.parseBoolean(getAttribute(name));
	}

	public boolean getAttributeAsBoolean(String name, boolean defaultValue) {
		if (rootElement.hasAttribute(name))
			return Boolean.parseBoolean(rootElement.getAttribute(name));
		else
			return defaultValue;
	}

	public int getAttributeAsInteger(String name) throws ConfigurationException {
		try {
			return Integer.parseInt(getAttribute(name));
		} catch (NumberFormatException nfe) {
			throw new ConfigurationException("attribute: " + name
					+ " cannot convert attribute value to integer datatype");
		}
	}

	public int getAttributeAsInteger(String name, int defaultValue) throws ConfigurationException {
		if (rootElement.hasAttribute(name)) {
			try {
				return Integer.parseInt(rootElement.getAttribute(name));
			} catch (NumberFormatException nfe) {
				throw new ConfigurationException("attribute: " + name
						+ " cannot convert attribute value to integer datatype");
			}
		} else
			return defaultValue;

	}

	/**
	 * Get the attribute names for the rootElement of this configuration.
	 *
	 * @see com.cme.retransmission.cfg.Configuration#getAttributeNames()
	 */
	public String[] getAttributeNames() {
		NamedNodeMap map = rootElement.getAttributes();
		if (map == null)
			return null;
		int len = map.getLength();
		String[] names = new String[len];
		for (int i = 0; i < len; i++) {
			names[i] = map.item(i).getNodeName();
		}
		return names;
	}

	/**
	 * @see com.cme.retransmission.cfg.Configuration#getChild(java.lang.String)
	 */
	public Configuration getChild(String name) throws ConfigurationException {
		Configuration[] c = getChildren(name);
		if (c != null && c.length > 0) {
			return c[0];
		}
		throw new ConfigurationException("No child exists for " + getName() + " with name " + name);
	}

	/**
	 * @see com.cme.retransmission.cfg.Configuration#getChildren(java.lang.String)
	 */
	public Configuration[] getChildren(String name) throws ConfigurationException {
		NodeList nl = rootElement.getElementsByTagName(name);
		int len = nl.getLength();
		if (len < 1)
			throw new ConfigurationException("No children exist for " + getName() + " with name " + name);
		Configuration[] cfg = new Configuration[len];
		for (int i = 0; i < len; i++) {
			cfg[i] = new XMLConfiguration((Element) nl.item(i));
		}
		return cfg;
	}

	/**
	 * @see com.cme.retransmission.cfg.Configuration#getName()
	 */
	public String getName() {
		return rootElement.getTagName();
	}

	/**
	 * @see com.cme.retransmission.cfg.Configuration#getValue()
	 */
	public String getValue() throws ConfigurationException {
		Node n = rootElement.getFirstChild();
		if (n == null)
			throw new ConfigurationException("Element " + getName() + " does not have a value");
		return n.getNodeValue();
	}

	public long getValueAsLong() throws ConfigurationException {
		try {
			return Long.parseLong(getValue());
		} catch (NumberFormatException nfe) {
			throw new ConfigurationException(getName() + " value cannot be converted to a long datatype.");
		}
	}

	public long getValueAsLong(long defaultValue) throws ConfigurationException {
		try {
			Node n = rootElement.getFirstChild();
			if (n == null)
				return defaultValue;
			return Long.parseLong(n.getNodeValue());
		} catch (NumberFormatException nfe) {
			throw new ConfigurationException(getName() + " value cannot be converted to a long datatype");
		}
	}

	public int getValueAsInteger() throws ConfigurationException {
		try {
			return Integer.parseInt(getValue());
		} catch (NumberFormatException nfe) {
			throw new ConfigurationException(getName() + " value cannot be converted to an integer datatype");
		}
	}

	public int getValueAsInteger(int defaultValue) throws ConfigurationException {
		try {
			Node n = rootElement.getFirstChild();
			if (n == null)
				return defaultValue;
			return Integer.parseInt(n.getNodeValue());
		} catch (NumberFormatException nfe) {
			throw new ConfigurationException(getName() + " value cannot be converted to an integer datatype");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cme.configuration.Configuration#hasChild()
	 */
	public boolean hasChild(String name) {
		try {
			Configuration[] c = getChildren(name);
			return c != null && c.length > 0;
		} catch (ConfigurationException ce) {
			return false;
		}
	}
}
