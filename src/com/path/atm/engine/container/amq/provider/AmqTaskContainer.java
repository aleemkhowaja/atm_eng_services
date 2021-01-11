package com.path.atm.engine.container.amq.provider;

import com.path.atm.engine.container.AbstractContainer;
import com.path.atm.engine.container.exception.InvalidContainerStateException;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.EngineError;
import com.path.lib.common.util.StringUtil;

/**
 * This class hold all task container behavior using the AMQ
 * <p> AMQ task container has 3 major components
 * 	1- Broker
 *  2- producer container
 *  3- Consumer container
 *  
 *  If one of the components failed to kickoff the whole process should be terminated
 *  and an {@link ContainerFailedActionException} will be thrown
 *  
 * @author MohammadAliMezzawi
 *
 */
abstract public class AmqTaskContainer extends AbstractContainer<AmqTaskContainerConfig> {
	
	/**
	 * Amq Broker
	 */
	private AmqBroker broker;
	
	/**
	 * @todo , @note 
	 * we may add hashMap of list of attached TaskClient
	 */
	
	/**
	 * Create Amq Task Container.
	 * @param configuration
	 */
	public AmqTaskContainer(AmqTaskContainerConfig configuration) {
		super(configuration.getName(),configuration);
	}
	
	
	/**
	 * Start the container
	 * @throws Exception 
	 * @todo it should be safe start 
	 * 1- all components should be shutdown on exception
	 */
	public void start() throws Exception{
		
		//@todo change the exception handling
		synchronized (getProducersMonitor()) {

			if(isRunning())
				throw new InvalidContainerStateException(this,"Already started");
			
			//start the broker
			startBroker();
			
			setRunning(true);
		}
	}

	@Override
	public void shutDownNow() throws Exception {
		
		synchronized (getProducersMonitor()) {

			if(isRunning()) {
			    broker.shutdown();
			}
		}
		
	}
	
	
	public void shutdownGracefully() {
		
		// shutdown tasks
		try {
			broker.shutdown();
		} catch (InvalidContainerStateException e) {
			
			// skip we are safe 
		} catch (Exception e) {
			//@todo fix this
			// **** it we eat crow
		}
					
	}
	
	/**
	 * Start the AMQ Broker
	 * @throws Exception
	 */
	private void startBroker() throws Exception{
	    
	    String brokerUrl = StringUtil.nullToEmpty(configuration.returnBrokerConnector());
	    
	    if(brokerUrl.isEmpty()) {
		throw new IllegalConfigurationException(EngineError.TC_CONF_WRONG_BROKER_URL);
	    }
		broker = new AmqBroker();
		broker.setConnectorUrl(configuration.returnBrokerConnector());
		broker.setSharedFileSystemPath(configuration.getSharedFileSystemPath());
		broker.setFailover(configuration.isFailover());
		broker.start();
	}
	
	/**
	 * @return
	 */
	public AmqBroker getBroker() {
		return broker;
	}

	/**
	 * @param broker
	 */
	public void setBroker(AmqBroker broker) {
		this.broker = broker;
	}
}
