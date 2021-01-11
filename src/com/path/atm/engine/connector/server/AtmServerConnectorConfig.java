package com.path.atm.engine.connector.server;

import com.path.atm.engine.connector.configuration.AtmConnectorConfiguration;
import com.path.atm.engine.connector.util.AtmConnectorConstants;
import com.path.atm.vo.config.AtmChannelConfigCO;
import com.path.lib.common.util.NumberUtil;

/**
 * Hold Configuration related to Atm Connector
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmServerConnectorConfig extends AtmConnectorConfiguration<AtmServerConnectorConfig>
{

    /**
     * @param propertiesFile
     */
    public AtmServerConnectorConfig(String propertiesFile)
    {
	super(propertiesFile);
    }

    /**
     * Load Atm Connector properties from properties file. This one is
     * deprecated
     * 
     * @deprecated for this type
     */
    public AtmServerConnectorConfig loadFromProperties()
    {
	super.loadFromProperties();
	
	// load connector worker properties
	workerThreadsCount = getPropertyIntValue("atm.engine.workerNb");
	
	return this;
    }

    /**
     * This method will load the configuration form configuration CO
     * 
     * @param atmChannelConfigCO
     * @return
     */
    public AtmServerConnectorConfig loadFromCO(AtmChannelConfigCO atmChannelConfigCO)
    {

	super.loadFromCO(atmChannelConfigCO);
	
	// load connector worker properties
	workerThreadsCount = NumberUtil.nullEmptyToValue(atmChannelConfigCO.getConnectorWorkerNo(),
		AtmConnectorConstants.DEFAULT_NB_OF_WRK).intValue();

	return this;
    }

}
