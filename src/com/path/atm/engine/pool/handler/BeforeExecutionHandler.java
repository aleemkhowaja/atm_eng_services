package com.path.atm.engine.pool.handler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * A handler that will be executed before the task execution.
 * 
 * @author MohammadAliMezzawi
 */
public interface BeforeExecutionHandler{
	
	/**
	 * Method that may be invoked by a {@link ThreadPoolExecutor} before
	 * the execution
	 * @param th 
	 */
	void handleExecution(Runnable r, Thread th, ThreadPoolExecutor executor);
}
