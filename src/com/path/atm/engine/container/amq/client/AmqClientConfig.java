package com.path.atm.engine.container.amq.client;

import com.path.atm.engine.config.AbstractConfiguration;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.config.AtmChannelConfigCO;
import com.path.lib.common.util.StringUtil;

/**
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AmqClientConfig extends AbstractConfiguration {

	/**
	 * Task Container name
	 */
	private String name;
	
	/**
	 * Number of Concurrent consumer
	 */
	private int concurrentConsumer;

	/**
	 * Max Producer Concurrent Session
	 */
	private int maxConcurrentSession;

	/**
	 * Number of Concurrent consumer
	 */
	private String destination;

	/**
	 * Constructor
	 */
	public AmqClientConfig(String propertyFile) {
		super(propertyFile);
	}

	/**
	 * Load Properties
	 * @throws Exception
	 * @deprecated to this type
	 */
	public AmqClientConfig loadFromProperties(){

		if(StringUtil.nullToEmpty(propertiesFile).isEmpty())
			throw new IllegalConfigurationException(EngineError.TCLIENT_CONF_NOTSET);
			
		try {
			concurrentConsumer = getPropertyIntValue("atm.tskContainer.concurrentConsumer", 2);
			maxConcurrentSession = getPropertyIntValue("atm.tskContainer.maxConcurrentSession", 5);
			destination = getPropertyValue("atm.tskContainer.destination", "AMQTSKCNT");
		
		}catch (Exception exception) {
			/**
			 * NumberFormatException is the most occurring exception at this level
			 * in case the dev set a string value for an integer property
			 */
			throw new IllegalConfigurationException(EngineError.TCLIENT_CONF_FAIL_TO_LOAD,exception);
		}
		
		return this;
	}
	
	
	/**
	 * Load Properties from CO file
	 * @throws Exception 
	 */
	public AmqClientConfig loadFromCO(AtmChannelConfigCO atmChannelConfigCO){
		
		try {
			concurrentConsumer = atmChannelConfigCO.getTaskContainerConsumerNo().intValue();
			maxConcurrentSession = atmChannelConfigCO.getTaskContainerSessionNo().intValue();
			destination = atmChannelConfigCO.getTaskContainerDestination();
		
		}catch (Exception exception) {
			/**
			 * NumberFormatException is the most occurring exception at this level
			 * in case the dev set a string value for an integer property
			 */
			throw new IllegalConfigurationException(EngineError.TCLIENT_CONF_FAIL_TO_LOAD,exception);
		}
		
		return this;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the concurrentConsumer
	 */
	public int getConcurrentConsumer() {
		return concurrentConsumer;
	}

	/**
	 * @param concurrentConsumer the concurrentConsumer to set
	 */
	public AmqClientConfig setConcurrentConsumer(int concurrentConsumer) {
		this.concurrentConsumer = concurrentConsumer;
		return this;

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
	public AmqClientConfig setMaxConcurrentSession(int maxConcurrentSession) {
		this.maxConcurrentSession = maxConcurrentSession;
		return this;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public AmqClientConfig setDestination(String destination) {
		this.destination = destination;
		return this;
	}

}
