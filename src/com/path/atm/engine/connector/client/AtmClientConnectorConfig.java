package com.path.atm.engine.connector.client;

import com.path.atm.engine.connector.configuration.AtmConnectorConfiguration;
import com.path.atm.vo.config.AtmChannelConfigCO;

/**
 * Hold Configuration related to Atm Connector
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmClientConnectorConfig extends AtmConnectorConfiguration<AtmClientConnectorConfig>
{

    /**
     * @param propertiesFile
     */
    public AtmClientConnectorConfig(String propertiesFile)
    {
	super(propertiesFile);
    }

    /**
     * Load Atm Connector properties from properties file. This one is
     * deprecated
     * 
     * @deprecated for this type
     */
    public AtmClientConnectorConfig loadFromProperties()
    {
	super.loadFromProperties();
	return this;
    }

    /**
     * This method will load the configuration form configuration CO
     * 
     * @param atmChannelConfigCO
     * @return
     */
    public AtmClientConnectorConfig loadFromCO(AtmChannelConfigCO atmChannelConfigCO)
    {

	super.loadFromCO(atmChannelConfigCO);
	return this;
    }

}
