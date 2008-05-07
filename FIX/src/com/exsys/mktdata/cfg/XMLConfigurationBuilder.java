package com.exsys.mktdata.cfg;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

/**
 * Created on Feb 10, 2005
 *
 */
public class XMLConfigurationBuilder extends ConfigurationBuilder {
    private DocumentBuilder builder = null;

    public XMLConfigurationBuilder() throws ConfigurationException {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new ConfigurationException(pce.getMessage(), pce);
        }
    }

    /**
     * @see com.exsys.mktdata.cfg.ConfigurationBuilder#build(java.io.InputStream)
     */
    public Configuration build(InputStream ios) throws ConfigurationException {
        if (ios == null)
            throw new ConfigurationException("XMLConfigurationBuilder received a null input stream to build from");
        try {
            Document d = builder.parse(ios);
            return new XMLConfiguration(d.getDocumentElement());
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), e);
        } finally {
            try {
                ios.close();
            } catch (IOException ioe) {
            }
        }
    }
}