package com.path.atm.engine.container.amq.provider;

import com.path.atm.engine.util.EngineError;

import java.net.InetAddress;

import com.path.atm.engine.container.ContainerConfig;
import com.path.atm.engine.container.util.TaskContainerConstants;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.lib.common.util.StringUtil;

/**
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AmqTaskContainerConfig extends ContainerConfig<AmqTaskContainerConfig> {

	/**
	 * Task Container name
	 */
	private String name;

	/**
	 * Hold the current broker url
	 */
	private String connectorUrl;

	/**
	 * Hold list of connector url
	 */
	private String connectorUrlList;

	/**
	 * Holder Broker connector protocol
	 */
	private String connectorProtocol;

	/**
	 * failover flag, If enabled the engine will start in Master/slave topology
	 */
	private boolean failover;

	/**
	 * sharedFileSystem hold the path of the shared file system
	 */
	private String sharedFileSystemPath;

	/**
	 * Number of prefetch message per consumer
	 */
	private int prefetchLimit;

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
	public AmqTaskContainerConfig(String propertyFile) {
		super(propertyFile);
	}

	/**
	 * Load Properties
	 * @throws Exception 
	 */
	public AmqTaskContainerConfig loadFromProperties(){

		if(StringUtil.nullToEmpty(propertiesFile).isEmpty())
			throw new IllegalConfigurationException(EngineError.TC_CONF_NOTSET);
			
		try {
			
			name = getPropertyValue("atm.tskContainer.name", "AmqTaskContainer");
			connectorUrlList = getPropertyValue("atm.tskContainer.connectorUrl", null);
			connectorProtocol = getPropertyValue("atm.tskContainer.connectorProtocol",
					TaskContainerConstants.DEFAULT_TRANSPORT_PROTOCOL);
			failover = getPropertyBoolValue("atm.tskContainer.failover", false);
			sharedFileSystemPath = getPropertyValue("atm.tskContainer.sharedFileSystemPath", null);
			prefetchLimit = getPropertyIntValue("atm.tskContainer.prefetchLimit", 1);
	
			buildBrokerUrl();
		
		}catch (Exception exception) {
			/**
			 * NumberFormatException is the most occurring exception at this level
			 * in case the dev set a string value for an integer property
			 */
			throw new IllegalConfigurationException(EngineError.TC_CONF_FAIL_TO_LOAD,exception);
		}
		
		return this;
	}
	

	/**
	 * Return Broker Url
	 * @return
	 */
	public String returnBrokerUrl(){
		
		if(!StringUtil.nullToEmpty(connectorUrl)
				.trim().equalsIgnoreCase(""))
			return connectorUrl;
		
		// build broker url
		buildBrokerUrl();
		
		return connectorUrl;
	}
	
	
	/**
	 * Loop through the given list of the brokers url
	 * and return the local url based on IP or hostname
	 * @return
	 * @throws Exception 
	 */
	public String returnBrokerConnector() throws Exception {
		
		String hostIp = InetAddress.getLocalHost().getHostAddress();
		String hostName = StringUtil.nullToEmpty(
			InetAddress.getLocalHost().getHostName()).toLowerCase();
		String localBrokerURL = null;
		String[] listOfBrokersUrl = connectorUrlList.split(",");
		
		for(String url : listOfBrokersUrl){
			if(url.contains(hostIp) 
				|| url.toLowerCase().contains(hostName)){
				localBrokerURL = url;
				break;
			}
		}
		
		
		// build the connector
		if(localBrokerURL != null ){
			
			StringBuilder sb = new StringBuilder()
				.append(returnTransportProtocol()).append("://")
				.append(localBrokerURL);
			
			localBrokerURL = sb.toString();
			
		}
		
		return localBrokerURL;	
	}
	
	
	/**
	 * Build the broker url using given Broker Urls List and broker transport
	 * protocol
	 * 
	 * @return
	 * @throws Exception
	 */
	private void buildBrokerUrl(){

		if (StringUtil.nullToEmpty(getConnectorUrlList()).trim().equalsIgnoreCase(""))
			throw new IllegalConfigurationException(EngineError.TC_CONF_HOST_NOTSET);

		// Get the list of broker
		String[] urls = getConnectorUrlList().split(",");
		StringBuilder sb = new StringBuilder();

		// if we have multiple urls this mean we should apply the master/slave topology
		failover = urls.length > 1;

		for (String url : urls) {
			sb.append(returnTransportProtocol()).append("://").append(url)
				.append("?jms.useAsyncSend=true&jms.dispatchAsync=true").append(",");
		}
		
		// remove last comma
		sb.deleteCharAt(sb.length() - 1);
		
		// failover:(tcp://broker1:61616,tcp://broker2:61616,tcp://broker3:61616)
		if (failover)
			sb.insert(0, TaskContainerConstants.FAILOVER_WRAPPER_PREFIX)
					.append(TaskContainerConstants.FAILOVER_WRAPPER_SUFFIX);

		connectorUrl = sb.toString();
	}

	/**
	 * Return the transport protocol. If null the default transport protocol will be
	 * returned
	 * 
	 * @return
	 */
	private String returnTransportProtocol() {

		if (StringUtil.nullToEmpty(getConnectorProtocol()).length() > 0)
			return getConnectorProtocol();

		setConnectorProtocol(StringUtil.nullEmptyToValue(connectorProtocol, 
			TaskContainerConstants.DEFAULT_TRANSPORT_PROTOCOL));

		return getConnectorProtocol();
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
	public AmqTaskContainerConfig setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return the connectorUrl
	 */
	public String getConnectorUrl() {
		return connectorUrl;
	}

	/**
	 * @param connectorUrl the connectorUrl to set
	 */
	public AmqTaskContainerConfig setConnectorUrl(String connectorUrl) {
		this.connectorUrl = connectorUrl;
		return this;
	}

	/**
	 * @return the connectorUrlList
	 */
	public String getConnectorUrlList() {
		return connectorUrlList;
	}

	/**
	 * @param connectorUrlList the connectorUrlList to set
	 */
	public void setConnectorUrlList(String connectorUrlList) {
		this.connectorUrlList = connectorUrlList;
	}

	/**
	 * @return the connectorProtocol
	 */
	public String getConnectorProtocol() {
		return connectorProtocol;
	}

	/**
	 * @param connectorProtocol the connectorProtocol to set
	 */
	public void setConnectorProtocol(String connectorProtocol) {
		this.connectorProtocol = connectorProtocol;
	}

	/**
	 * @return the failover
	 */
	public boolean isFailover() {
		return failover;
	}

	/**
	 * @param failover the failover to set
	 */
	public AmqTaskContainerConfig setFailover(boolean failover) {
		this.failover = failover;
		return this;
	}

	/**
	 * @return the sharedFileSystemPath
	 */
	public String getSharedFileSystemPath() {
		return sharedFileSystemPath;
	}

	/**
	 * @param sharedFileSystemPath the sharedFileSystemPath to set
	 */
	public AmqTaskContainerConfig setSharedFileSystemPath(String sharedFileSystemPath) {
		this.sharedFileSystemPath = sharedFileSystemPath;
		return this;
	}

	/**
	 * @return the prefetchLimit
	 */
	public int getPrefetchLimit() {
		return prefetchLimit;
	}

	/**
	 * @param prefetchLimit the prefetchLimit to set
	 */
	public AmqTaskContainerConfig setPrefetchLimit(int prefetchLimit) {
		this.prefetchLimit = prefetchLimit;
		return this;
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
	public AmqTaskContainerConfig setConcurrentConsumer(int concurrentConsumer) {
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
	public AmqTaskContainerConfig setMaxConcurrentSession(int maxConcurrentSession) {
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
	public AmqTaskContainerConfig setDestination(String destination) {
		this.destination = destination;
		return this;
	}

}
