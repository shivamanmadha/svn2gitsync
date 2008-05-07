package com.exsys.mktdata.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created on Feb 10, 2005
 */
public abstract class ConfigurationBuilder {
    /**
     * @see com.exsys.mktdata.cfg.ConfigurationBuilder#build(java.io.File)
     */
    public Configuration build(File f) throws ConfigurationException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
        return build(fis);
    }

    public abstract Configuration build(InputStream ios) throws ConfigurationException;
}