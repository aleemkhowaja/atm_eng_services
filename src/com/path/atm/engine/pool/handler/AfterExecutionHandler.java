package com.path.atm.engine.pool.handler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * A handler that will be executed after the task execution.
 * 
 * @author MohammadAliMezzawi
 */
public interface AfterExecutionHandler{

	/**
	 * Method that may be invoked by a {@link ThreadPoolExecutor} After
	 * the execution
	 * 
	 * @param r task that is executed
	 * @param executor
	 */
	void handleExecution(Runnable r, ThreadPoolExecutor executor);
	
}