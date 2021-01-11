package com.path.atm.engine.container.amq.provider;

import com.path.atm.engine.core.AtmEngine;

/**
 * Iso Task Container
 * @author MohammadAliMezzawi
 */
public class AmqIsoTaskContainer extends AmqTaskContainer{
	
	/**
	 * Hold the Atm engine
	 */
	private final AtmEngine engine;
	
	/**
	 * injection of Reactor and Message factory
	 * @param configuration
	 */
	public AmqIsoTaskContainer(AmqTaskContainerConfig configuration,AtmEngine engine) {
		super(configuration);
		this.engine = engine;
	}
}
