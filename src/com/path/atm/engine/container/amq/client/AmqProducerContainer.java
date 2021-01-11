package com.path.atm.engine.container.amq.client;

import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.broker.region.policy.RedeliveryPolicyMap;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import com.path.atm.engine.pool.tasks.Task;


/**
 * @author MohammadAliMezzawi
 *
 */
public class AmqProducerContainer {

	/**
	 * Container name
	 */
	protected String name;

	/**
	 * 
	 */
	protected final Object producersMonitor = new Object();

	/**
	 * Status of the Container
	 */
	protected volatile boolean running;

	/**
	 * 
	 */
	protected String brokerUrl;

	/**
	 * Destination
	 */
	protected String destination;

	/**
	 * 
	 */
	protected Connection sharedConnection;

	/**
	 * 
	 */
	private CachingConnectionFactory cachedconnectionFactory;

	/**
	 * 
	 */
	private int maxConcurrentSession;

	/**
	 * 
	 */
	protected JmsTemplate jmsTemplate;

	
	/**
	 * @param containerName
	 */
	public AmqProducerContainer(String containerName) {
		this.name = containerName;
	}

	
	
	public void start(){

		// initialize the connection factory
		initConnectionFactory();

		// set Jms template
		jmsTemplate = new JmsTemplate(cachedconnectionFactory);
		jmsTemplate.setDefaultDestinationName(destination);
		// mark as running
		running = true;

	}

	/**
	 * Submit task
	 */
	public boolean submitTask(Task message) {

		synchronized (producersMonitor) {
			if (!running)
				return false;

			try {
				jmsTemplate.convertAndSend(message);
			} catch (Exception e) {
				// @todo failed to submit task to the jms
			}
		}

		return true;

	}


	public void shutDownNow() {
		// drain the pool
		synchronized (producersMonitor) { 
			
			if(!running)
				return;
			
			// @todo later test if this close the producer
			// and the sessins bla bla ..
			cachedconnectionFactory.destroy();
			
			// mark as stopped
			running = false;
			
		}
	}

	/**
	 * Initialize the connection factory
	 */
	protected void initConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		
		RedeliveryPolicy queuePolicy = new RedeliveryPolicy();
		queuePolicy.setInitialRedeliveryDelay(0);
		queuePolicy.setRedeliveryDelay(1000);
		queuePolicy.setUseExponentialBackOff(false);
		queuePolicy.setMaximumRedeliveries(2);

		RedeliveryPolicy topicPolicy = new RedeliveryPolicy();
		topicPolicy.setInitialRedeliveryDelay(0);
		topicPolicy.setRedeliveryDelay(1000);
		topicPolicy.setUseExponentialBackOff(false);
		topicPolicy.setMaximumRedeliveries(3);

		// Receive a message with the JMS API
		RedeliveryPolicyMap map = connectionFactory.getRedeliveryPolicyMap();
		map.put(new ActiveMQTopic(">"), topicPolicy);
		map.put(new ActiveMQQueue(">"), queuePolicy);
		
		connectionFactory.setRedeliveryPolicyMap(map);
		
		cachedconnectionFactory = new CachingConnectionFactory(connectionFactory);
		cachedconnectionFactory.setCacheConsumers(false);
		cachedconnectionFactory.setSessionCacheSize(this.getMaxConcurrentSession());
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return
	 */
	public String getBrokerUrl() {
		return brokerUrl;
	}

	/**
	 * @param localBorkerUrl
	 */
	public void setBrokerUrl(String localBorkerUrl) {
		brokerUrl = localBorkerUrl;
	}

	/**
	 * @return the maxConcurrentSession
	 */
	public int getMaxConcurrentSession() {
		return maxConcurrentSession;
	}

	/**
	 * @param maxConcurrentSession the maxConcurrentSession to set
	 */
	public void setMaxConcurrentSession(int maxConcurrentSession) {
		this.maxConcurrentSession = maxConcurrentSession;
	}
}
