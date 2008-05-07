package com.exsys.common.config;

import com.exsys.common.exceptions.*;


/**
 * @author kreddy
 *
 * Interface to help read configuration values
 */
public interface ConfigSource {

/**
 * getValue method returns the value of an attribute
 * Creation date: (6/2/01 9:48:27 AM)
 * @return java.lang.String
 * @param attribute java.lang.String
 * @exception com.exsys.common.exceptions.ConfigAttributeNotFound The exception description.
 */
String getValue(String attribute) throws ConfigAttributeNotFound;
/**
 * This method returns defaultValue if the attribute is not found
 * Creation date: (6/2/01 9:50:36 AM)
 * @return java.lang.String
 * @param attribute java.lang.String
 * @param defaultValue java.lang.String
 */
String getValue(String attribute, String defaultValue);
}
