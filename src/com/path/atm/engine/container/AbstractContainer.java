package com.path.atm.engine.container;

public abstract class AbstractContainer<Config> implements TaskContainer{
	
	/**
	 * Hold reference to the container name
	 */
	private final String name;
	
	/**
	 * Status of the Container
	 */
	private volatile boolean running;
	
	/**
	 * Monitor object
	 */
	private final Object producersMonitor = new Object();
	
	/**
	 * Hold Container configuration
	 */
	protected final Config configuration;
	
	/**
	 * @param name
	 */
	public AbstractContainer(String name,Config configuration) {
		this.name = name;
		this.configuration = configuration;
	}


	/**
	 * @return the Container name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}


	/**
	 * @return the producersMonitor
	 */
	public Object getProducersMonitor() {
		return producersMonitor;
	}


	/**
	 * @return the configuration
	 */
	public Config getConfiguration() {
		return configuration;
	}
}
