package com.path.atm.engine.container.amq.client;

import com.path.atm.engine.container.AbstractContainer;
import com.path.atm.engine.container.amq.provider.AmqTaskContainer;
import com.path.atm.engine.container.amq.provider.AmqTaskContainerConfig;
import com.path.atm.engine.container.exception.InvalidContainerStateException;

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
 * @todo refactor the shutdown process make it safe as the engine
 */
abstract public class AmqClientContainer<Container extends AmqTaskContainer> extends AbstractContainer<AmqClientConfig>  {
	
	/**
	 * Amq Producer container
	 */
	protected AmqProducerContainer amqProducerContainer;
	
	/**
	 * Amq Consumer container
	 */
	protected AmqConsumerContainer consumerContainer;
	
	/**
	 * hold reference to the container
	 */
	protected Container taskContainer;
	
	/**
	 * Create Amq Task Container client.
	 * @param configuration
	 * @param container 
	 */
	public AmqClientContainer(AmqClientConfig configuration, Container container) {
		super(configuration.getName(),configuration);
		this.taskContainer = container;
	}
	
	
	/**
	 * Start the container
	 * @todo it should be safe start 
	 * 1- all components should be shutdown on exception
	 */
	public void start() {
		//@todo change the exception handling
		synchronized (getProducersMonitor()) {

			if (isRunning())
				throw new InvalidContainerStateException(this, "Already started");

			// start Consumer contanier
			startConsumerContainer();

			// start Producer container
			startProducerContainer();
			setRunning(true);
		}
	}
	

	@Override
	public void shutDownNow() throws Exception {
		
		synchronized (getProducersMonitor()) {

			if(!isRunning())
				throw new InvalidContainerStateException(this,"Not started");
			
			// turn off producer
			getProducerContainer()
				.shutDownNow();
			
			// turn off Consumer
			getConsumerContainer()
				.shutDownNow();
		}
	}
	
	
	/**
	 * @todo change comment
	 * This method will kickoff the instantly msg request container
	 * which will handle the translation of the request to JMS Message
	 * @throws Exception 
	 * 
	 */
	private void startProducerContainer() {
		
		AmqTaskContainerConfig containerConfig = taskContainer.getConfiguration();
		
		amqProducerContainer = new AmqProducerContainer("InstantMsgHandler");
		amqProducerContainer.setBrokerUrl(containerConfig.returnBrokerUrl());
		amqProducerContainer.setDestination(configuration.getDestination());
		amqProducerContainer.setMaxConcurrentSession(configuration.getMaxConcurrentSession());
		
		// ----------- @todo configure number of cached session 
		// ----------- @todo check producer id --------
		amqProducerContainer.start();
		
	}

	/**
	 * Start the consumer container
	 * @throws Exception 
	 */
	private void startConsumerContainer() {
		
		AmqTaskContainerConfig containerConfig = taskContainer.getConfiguration();
		
		consumerContainer = new AmqConsumerContainer("InstantConsumerContainer");
		consumerContainer.setBrokerUrl(containerConfig.returnBrokerUrl());
		consumerContainer.setPrefetchLimit(containerConfig.getPrefetchLimit());
		consumerContainer.setConcurrentConsumers(configuration.getConcurrentConsumer());
		consumerContainer.setDestinationName(configuration.getDestination());
		consumerContainer.setMessageListener(createTaskMessageListener());
		consumerContainer.start();
	}

	/**
	 * Create task message listener
	 * @return
	 */
	abstract protected TaskMessageListener createTaskMessageListener();
	
	/**
	 * @return the consumerContainer
	 */
	public AmqConsumerContainer getConsumerContainer() {
		return consumerContainer;
	}


	/**
	 * @param consumerContainer the consumerContainer to set
	 */
	public void setConsumerContainer(AmqConsumerContainer consumerContainer) {
		this.consumerContainer = consumerContainer;
	}


	/**
	 * @return the amqProducerContainer
	 */
	public AmqProducerContainer getProducerContainer() {
		return amqProducerContainer;
	}


	/**
	 * @param amqProducerContainer the amqProducerContainer to set
	 */
	public void setProducerContainer(AmqProducerContainer amqProducerContainer) {
		this.amqProducerContainer = amqProducerContainer;
	}


	/**
	 * @return the taskContainer
	 */
	public Container getTaskContainer() {
		return taskContainer;
	}


	/**
	 * @param taskContainer the taskContainer to set
	 */
	public void setTaskContainer(Container taskContainer) {
		this.taskContainer = taskContainer;
	}
}
