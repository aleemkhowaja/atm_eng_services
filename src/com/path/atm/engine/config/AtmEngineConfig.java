package com.path.atm.engine.config;

import org.apache.struts2.json.JSONException;

import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.EngineError;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;
import com.path.struts2.json.PathJSONUtil;

/**
 * Hold All attributes and behaviors related to the AtmEngine Reactor
 * configuration
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngineConfig extends AbstractConfiguration
{

    /**
     * Reactor name
     */
    private String name;

    /**
     * Hold the pooling mode ( in memory/mq )
     */
    private String poolMode;

    /**
     * Constructor
     */
    public AtmEngineConfig()
    {
	super(AtmEngineConstants.PROP_FILE_NAME);
    }

    /**
     * Constructor
     * 
     * @param propFileName
     */
    public AtmEngineConfig(String propFileName)
    {
	super(propFileName);
    }

    /**
     * Load Atm reactor from properties file
     */
    public AtmEngineConfig loadFromProperties()
    {

	try
	{
	    // check if configuration file is set
	    checkConfigurationFile();

	    poolMode = getPropertyValue("atm.engine.poolMode", AtmEngineConstants.TASK_POOL_MODE_MQ);

	}
	catch(Exception exception)
	{

	    /**
	     * NumberFormatException is the most occurring exception at this
	     * level in case the dev set a string value for an integer property
	     */
	    throw new IllegalConfigurationException(EngineError.ENG_CONF_FAIL_TO_LOAD, exception);
	}

	return this;
    }

    /**
     * Return a String representation of this Object
     */
    public String toString()
    {
	try
	{
	    return PathJSONUtil.strutsJsonSerialize(this, null, null, false, true);
	}
	catch(JSONException e)
	{
	    e.printStackTrace();
	}

	return "Failed";
    }

    /**
     * Check if Configuration file is set
     * 
     * @throws Exception The method checkPropertyFileExistance signature state
     *             that it throws an exception while the body can't throw other
     *             than unchecked exception
     */
    private void checkConfigurationFile() throws Exception
    {

	// properties file name not set
	if(StringUtil.nullToEmpty(propertiesFile).isEmpty())
	    throw new IllegalConfigurationException(EngineError.ENG_CONF_FNAME_NOTSET);

	// file not created
	if(!PathPropertyUtil.checkPropertyFileExistance(propertiesFile))
	    throw new IllegalConfigurationException(EngineError.ENG_CONF_FILE_NOTSET);
    }

    /**
     * @return the name
     */
    public String getName()
    {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
	this.name = name;
    }

    /**
     * @return the poolMode
     */
    public String getPoolMode()
    {
	return poolMode;
    }

    /**
     * @param poolMode the poolMode to set
     */
    public void setPoolMode(String poolMode)
    {
	this.poolMode = poolMode;
    }

}
