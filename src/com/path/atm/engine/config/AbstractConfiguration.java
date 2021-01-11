package com.path.atm.engine.config;

import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.EngineError;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;

/**
 * This class house the basic behaviors related to configuration file
 * 
 * @author MohammadAliMezzawi
 *
 */
abstract public class AbstractConfiguration<Config>
{

    /**
     * Hold Main property file
     */
    protected String propertiesFile;

    /**
     * Create AbstractConfiguration
     * 
     * @param propertiesFile
     */
    public AbstractConfiguration(String propertiesFile)
    {
	this.propertiesFile = propertiesFile;
    }

    /**
     * Load Properties from file
     */
    abstract public Config loadFromProperties();

    /**
     * Return the property value
     * 
     * @param propName
     * @param defaultValue
     * @return
     */
    public String getPropertyValue(String propName, String defaultValue)
    {
	return getPropertyValue(propertiesFile, propName, defaultValue);
    }

    /**
     * Return the property value
     * 
     * @param propFile
     * @param propName
     * @param defaultValue
     * @return
     */
    public static String getPropertyValue(String propFile, String propName, String defaultValue)
    {
	try
	{
	    String propertyValue = PathPropertyUtil.returnPathPropertyFromFile(propFile, propName);

	    return (null == defaultValue) ? propertyValue
		    : StringUtil.nullEmptyToValue(propertyValue, defaultValue).trim();

	}
	catch(Exception e)
	{

	    throw new IllegalConfigurationException(EngineError.CONF_NOTSET);
	}
    }

    /**
     * Return the integer value of a property
     * 
     * @param propName
     * @return
     */
    public int getPropertyIntValue(String propName)
    {
	return getPropertyIntValue(propName, 0);
    }

    /**
     * Return the boolean value of a property
     * 
     * @param propName
     * @return
     */
    public boolean getPropertyBoolValue(String propName)
    {
	return getPropertyBoolValue(propName, null);
    }

    /**
     * Return the integer value of a property
     * 
     * @param propName
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public Integer getPropertyIntValue(String propName, Integer defaultValue)
    {

	// never use String.valueOf(defaultValue); due to "null" string
	String defaultVal = (null == defaultValue) ? null : defaultValue.toString();
	String propVal = getPropertyValue(propName, defaultVal);

	return null == propVal ? null : Integer.valueOf(propVal);
    }

    /**
     * Return the boolean value of a property
     * 
     * @param propName
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public Boolean getPropertyBoolValue(String propName, Boolean defaultValue)
    {

	// never use String.valueOf(defaultValue); due to "null" string
	String defaultVal = (null == defaultValue) ? null : defaultValue.toString();
	String propVal = getPropertyValue(propName, defaultVal);

	// check if propVal isn't a boolean value ( true/false )
	if(null != propVal && !(propVal.equalsIgnoreCase(Boolean.TRUE.toString())
		|| propVal.equalsIgnoreCase(Boolean.FALSE.toString())))
	    throw new IllegalConfigurationException(EngineError.CONF_INVALID_PROP_VALUE);

	// @todo @note we may use BooleanUtils.toBooleanObject(null) from common
	// lang3
	return null == propVal ? null : Boolean.valueOf(propVal);
    }

    /**
     * @return the propertiesFile
     */
    public String getPropertiesFile()
    {
	return propertiesFile;
    }
}
