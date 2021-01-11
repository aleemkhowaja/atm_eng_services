package com.path.atm.engine.container.amq.provider;

/**
 * @author Mohammad Ali Mezzawi
 *
 */
public interface JmsBroker {

	/**
	 * This method will start the Broker
	 * @throws Exception
	 */
	public void start() throws Exception;

	/**
	 * This method will Stop the Broker
	 * @throws Exception
	 */
	public void shutdown() throws Exception;

	/**
	 * This method will check if the broker is alive
	 * @return
	 * @throws Exception
	 */
	boolean isBrokerAlive() throws Exception;
}
