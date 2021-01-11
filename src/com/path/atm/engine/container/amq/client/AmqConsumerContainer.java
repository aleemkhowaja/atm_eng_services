package com.path.atm.engine.container.amq.client;

import java.util.StringJoiner;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import com.path.atm.engine.container.exception.InvalidContainerStateException;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

/**
 * This class represent the Consumer container.
 * 
 * @author Mohammad Ali Mezzawi
 *
 */
public class AmqConsumerContainer<Listener extends TaskMessageListener> 
	extends SimpleMessageListenerContainer {

	/**
	 * Number of prefetch message per consumer
	 */
	private int prefetchLimit;

	/**
	 * 
	 */
	private String brokerUrl;

	/**
	 * Consumer container name
	 */
	private String name;
	
	/**
	 * @param containerName
	 */
	public AmqConsumerContainer(String containerName ) {
		super();
		setName(containerName);
	}
	
	/**
	 * @param containerName
	 */
	public AmqConsumerContainer(String containerName , String brokerUrl,
			int prefetchLimit) {
		super();
		setName(containerName);
		setBrokerUrl(brokerUrl);
		setPrefetchLimit(prefetchLimit);
	}
	
	
	/**
	 * @param containerName
	 */
	public AmqConsumerContainer(String containerName , String brokerUrl,
			int prefetchLimit, Listener taskListener) {
		super();
		setName(containerName);
		setBrokerUrl(brokerUrl);
		setPrefetchLimit(prefetchLimit);
		this.setMessageListener(taskListener);
	}
	

	/**
	 * Start the message consumer Container
	 * 1- establish a connection with the broker
	 * 2- Set the needed options as prefetch limit
	 * 3- Set the Message listener
	 * 4- start the main message listener
	 */
	public void start() {
		
		// validate Consumer internal state before starting it
		validateInternalState();
		
		// connection factory
		ActiveMQConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactory(getBrokerUrl());
		
		// apply prefetch policy
		ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
		prefetchPolicy.setQueuePrefetch(prefetchLimit);
		connectionFactory.setPrefetchPolicy(prefetchPolicy);
		
		this.setConnectionFactory(connectionFactory);
		
		// Spring AbstractMessage container will handle the acknowledgement
		this.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		super.start();
	}


	/**
	 * Validate the internal state of the container before launching it
	 * 
	 * @throws InvalidContainerStateException
	 */
	private void validateInternalState(){
		
		StringJoiner strJoiner = new StringJoiner(" | ");
		
		
		if(StringUtil.nullToEmpty(getBrokerUrl()).trim().isEmpty())
			strJoiner.add("Broker url can't be empty");
		
		if(null == getMessageListener())
			strJoiner.add("Message listener isn't defined");
		
		if(strJoiner.length() > 0 )
			throw new InvalidContainerStateException(strJoiner.toString());
	}
	
	
	/**
	 * @note : to check if we need to shutdown then stop
	 * @note: In case we need to apply the Pooled conx factory we should
	 * check the POC
	 */
	public void shutDownNow()
	{
		/**
		 * In case we will implement the sender pool later
		 * check the Engine Phase 1
		 * We should stop the execution of the pool first
		 * to release the consumer listener which is waiting the tasks
		 * if we don't do that the shutdown of the consumer listener container
		 * will be blocked until all the Message consumer tasks are done.
		 */
				
		/**
		 * kill all consumers.
		 * Blocking until all Listener are released
		 */
		try{
			
			Log.getInstance().debug(formatLog("Shutting down is ongoing"));
			
			super.shutdown();
			super.stop();
			
			Log.getInstance().debug(formatLog("Shutdown Consumer container"));
			
		}catch (Exception e) {
			// @todo handle this exception
			// Usually it will be a runtime exception "JmsException"
			Log.getInstance().debug(formatLog("Failed to shutdown the broker "));
		}
	}
	
	
	/**
	 * This method return all information about A broker as string
	 * @todo try to add Concurrent Consumer [%d]
	 * @return
	 */
	public String toString() {
		return String.format("[name] [%s] , Broker Url: [%s], prefetchLimit: [%s] ", 
				getName(), getBrokerUrl(), getPrefetchLimit());
	}
	
	public String getBrokerUrl() {
		return brokerUrl;
	}


	/**
	 * @param localBorkerUrl
	 */
	public void setBrokerUrl(String borkerUrl) {
		this.brokerUrl = borkerUrl;
		
	}
	

	/**
	 * @return
	 */
	public int getPrefetchLimit() {
		return prefetchLimit;
	}

	
	/**
	 * @param prefetchLimit
	 */
	public void setPrefetchLimit(int prefetchLimit) {
		this.prefetchLimit = prefetchLimit;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * Custom logging function
	 * @param string
	 */
	private String formatLog(String message) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("[AtmEngine][AtmConsumerContainer] ")
			.append(this)
			.append(message);
		
		return sb.toString();
	}
	

}
