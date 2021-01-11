package com.path.atm.engine.config;

import java.math.BigDecimal;

import com.path.atm.bo.engine.AtmEngineConfigBO;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.config.AtmChannelConfigCO;
import com.path.atm.vo.config.AtmInterfaceConfigSC;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;

/**
 * Hold All attributes and behviors related to the AtmEngine Reactor
 * configuration
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngineReactorConfig extends AbstractConfiguration<AtmEngineReactorConfig>
{

    /**
     * Reactor name
     */
    private String name;

    /**
     * Reactor interface code
     */
    private BigDecimal interfaceCode;

    /**
     * Hold reference to the channel configuration
     */
    private AtmChannelConfigCO channelConfiguration;

    /**
     * Constructor
     */
    public AtmEngineReactorConfig(BigDecimal interfaceCode)
    {
	super(AtmEngineConstants.PROP_FILE_NAME);
	this.interfaceCode = interfaceCode;
    }

    /**
     * Load Atm reactor from properties file
     */
    public AtmEngineReactorConfig loadFromProperties()
    {

	try
	{
	    // check if configuration file is set
	    checkConfigurationFile();

	    // laod channel configuration from the DB
	    AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		    .getBean("atmEngConfigBO");

	    AtmInterfaceConfigSC configSc = new AtmInterfaceConfigSC();
	    configSc.setCode(interfaceCode);

	    // load properties related to the channel
	    setChannelConfiguration(configBO.returnChannelConfig(configSc));

	}
	catch(Exception exception)
	{

	    /**
	     * NumberFormatException is the most occurring exception at this
	     * level in case the dev set a string value for an integer property
	     */
	    throw new IllegalConfigurationException(EngineError.REACTOR_CONF_FAIL_TO_LOAD, exception);
	}

	return this;
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
	    throw new IllegalConfigurationException(EngineError.REACTOR_CONF_FNAME_NOTSET);

	// file not created
	if(!PathPropertyUtil.checkPropertyFileExistance(propertiesFile))
	    throw new IllegalConfigurationException(EngineError.REACTOR_CONF_FILE_NOTSET);
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
     * @return the channelConfiguration
     */
    public AtmChannelConfigCO getChannelConfiguration()
    {
	return channelConfiguration;
    }

    /**
     * @param channelConfiguration the channelConfiguration to set
     */
    public void setChannelConfiguration(AtmChannelConfigCO channelConfiguration)
    {
	this.channelConfiguration = channelConfiguration;
    }

}
