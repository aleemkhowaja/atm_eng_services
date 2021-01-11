package com.path.atm.engine.container.amq.client;

import com.path.atm.engine.container.amq.provider.AmqIsoTaskContainer;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.pool.tasks.IsoTask;

/**
 * Iso Task Container
 * @author MohammadAliMezzawi
 */
public class AmqIsoClient extends AmqClientContainer<AmqIsoTaskContainer>{
	
	/**
	 * Hold the Atm engine
	 */
	private final AtmEngineReactor reactor;
	
	
	/**
	 * injection of Reactor and Message factory
	 * @param configuration
	 */
	public AmqIsoClient(AmqClientConfig configuration,AtmEngineReactor reactor) {
		super(configuration,reactor.getEngine().getTaskContainer());
		this.reactor = reactor;
	}
	
	/**
	 * Submit a task to the container
	 */
	public boolean submit(IsoTask tsk) throws Exception{
		return amqProducerContainer.submitTask(tsk);
	}

	/**
	 * Create task message listener
	 */
	protected TaskMessageListener createTaskMessageListener() {
		IsoTaskMessageListener listener = new IsoTaskMessageListener(reactor);
		return (TaskMessageListener)listener;
	}

	/**
	 * @return the reactor
	 */
	public AtmEngineReactor getReactor() {
		return reactor;
	}
	
	/**
	 * Returns a string representation of the object.
	 */
	public String toString() {
		return "AmqIsoClient";
	}
	
}
