package com.path.atm.engine.container;

/**
 * This interface represent the behavior of task container
 * 
 * @author MohammadAliMezzawi
 */
public interface TaskContainer{
	/**
	 * Start the container
	 */
	public void start() throws Exception;
	
	/**
	 * shutdown the container
	 */
	public void shutDownNow() throws Exception;

}
