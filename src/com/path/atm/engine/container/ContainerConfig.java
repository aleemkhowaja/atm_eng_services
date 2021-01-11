package com.path.atm.engine.container;

import com.path.atm.engine.config.AbstractConfiguration;

/**
 * This class represent is the parent of all containers configuration classes.
 * 
 * @author MohammadAliMezzawi
 *
 * @param <Config>
 */
abstract public class ContainerConfig<Config> extends AbstractConfiguration{

	/**
	 * Create ContainerConfig
	 * @param propertiesFile
	 */
	public ContainerConfig(String propertiesFile) {
		super(propertiesFile);
	}
	
}
