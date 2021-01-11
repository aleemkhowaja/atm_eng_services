package com.path.atm.engine.pool.executor;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.pool.handler.AfterExecutionHandler;
import com.path.atm.engine.pool.handler.BeforeExecutionHandler;
import com.path.atm.engine.util.EngineError;

/**
 * This class build a reactor executor
 * 
 * @author MohammadAliMezzawi
 *
 */
public class TaskExecutorBuilder {

	/**
	 * Hold Worker Name prefix
	 */
	private String workerNamePrefix;

	/**
	 * Factory : Worker Priority
	 * 
	 * @return
	 */
	public int workerPriority;

	/**
	 * Core pool size
	 */
	private int corePoolSize;

	/**
	 * Maximum pool size
	 */
	private int maxPoolSize;

	/**
	 * Queue Capacity
	 */
	private int queueCapacity;

	/**
	 * Handler called before executing the runnable
	 */
	private List<BeforeExecutionHandler> beforeHandlers = new ArrayList<>();

	/**
	 * Handler called before executing the runnable
	 */
	private List<AfterExecutionHandler> afterHandlers = new ArrayList<>();

	/**
	 * Hold the UncaughtExceptionHandler Handler
	 */
	private UncaughtExceptionHandler uncaughtExceptionHandler;

	/**
	 * Hold the UncaughtExceptionHandler Handler
	 */
	private RejectedExecutionHandler rejectedExecutionHandler;

	/**
	 * Build the reactor
	 * 
	 * @return
	 */
	public TaskExecutor build() {

		/**
		 * Validate the given arguments before building the reactor
		 */
		validateArguments();

		// build the factory
		TaskWorkerFactory thFactory = new TaskWorkerFactory(workerNamePrefix, workerPriority);

		/**
		 * Build the queue We may add before queue and before dequeue handler to set the
		 * time in and time out.
		 */
		queueCapacity = queueCapacity > 0 ? queueCapacity : Integer.MAX_VALUE;
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(queueCapacity);

		
		maxPoolSize = maxPoolSize > 0 ? maxPoolSize : corePoolSize;
		
		/**
		 * Build the thread executor
		 */
		TaskPoolExecutor poolExecutor = new TaskPoolExecutor(corePoolSize, maxPoolSize, 0L,
				TimeUnit.MILLISECONDS, queue, thFactory);

		if (null != rejectedExecutionHandler)
			poolExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);

		if (afterHandlers != null) {

			for (AfterExecutionHandler handler : afterHandlers)
				poolExecutor.addAfterExecHandler(handler);
		}

		if (beforeHandlers != null) {

			for (BeforeExecutionHandler handler : beforeHandlers)
				poolExecutor.addBeforeExecHandler(handler);
		}

		TaskExecutor reactor = new TaskExecutor();
		reactor.setPoolExecutor(poolExecutor);

		return reactor;
	}

	
	/**
	 * Validate the given arguments
	 */
	private void validateArguments() {
		
		if(workerPriority > Thread.MAX_PRIORITY 
				|| workerPriority < Thread.MIN_PRIORITY)
			throw new IllegalConfigurationException(EngineError.EX_CONF_WRG_PRIORITY);
		
	}


	/**
	 * Add After execution handler
	 * 
	 * @param handler
	 * @return 
	 */
	public TaskExecutorBuilder addAfterExecHandler(AfterExecutionHandler... handlers) {

		if (handlers == null)
			throw new NullPointerException("AfterExecutionHandler handlers can't be null");

		for (AfterExecutionHandler handler : handlers) {

			if (handler == null)
				throw new NullPointerException("AfterExecutionHandler handler can't be null");

			afterHandlers.add(handler);
		}
		
		return this;
	}

	
	/**
	 * Add before execution handler
	 * 
	 * @param handler
	 * @return 
	 */
	public TaskExecutorBuilder addBeforeExecHandler(BeforeExecutionHandler... handlers) {

		if (handlers == null)
			throw new NullPointerException("BeforeExecutionHandler handlers can't be null");

		for (BeforeExecutionHandler handler : handlers) {

			if (handler == null)
				throw new NullPointerException("BeforeExecutionHandler handler can't be null");

			beforeHandlers.add(handler);
		}
		
		return this;
	}

	/**
	 * @return the priority
	 */
	public int getWorkerPriority() {
		return workerPriority;
	}

	/**
	 * @param priority the priority to set
	 * @return 
	 */
	public TaskExecutorBuilder setWorkerPriority(int priority) {
		this.workerPriority = priority;
		return this;
	}

	/**
	 * @return the workerNamePrefix
	 */
	public String getWorkerNamePrefix() {
		return workerNamePrefix;
	}

	/**
	 * @param workerNamePrefix the workerNamePrefix to set
	 * @return 
	 */
	public TaskExecutorBuilder setWorkerNamePrefix(String workerNamePrefix) {
		this.workerNamePrefix = workerNamePrefix;
		return this;
	}

	/**
	 * @return the uncaughtExceptionHandler
	 */
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}

	/**
	 * @param uncaughtExceptionHandler the uncaughtExceptionHandler to set
	 * @return 
	 */
	public TaskExecutorBuilder setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
		return this;
	}

	/**
	 * @return the corePoolSize
	 */
	public int getCorePoolSize() {
		return corePoolSize;
	}

	/**
	 * @param corePoolSize the corePoolSize to set
	 * @return 
	 */
	public TaskExecutorBuilder setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
		return this;
	}

	/**
	 * @return the maxPoolSize
	 */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	/**
	 * @param maxPoolSize the maxPoolSize to set
	 * @return 
	 */
	public TaskExecutorBuilder setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}

	/**
	 * @return the queueCapacity
	 */
	public int getQueueCapacity() {
		return queueCapacity;
	}

	/**
	 * @param queueCapacity the queueCapacity to set
	 * @return 
	 */
	public TaskExecutorBuilder setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
		return this;
	}

	/**
	 * @return the rejectedExecutionHandler
	 */
	public RejectedExecutionHandler getRejectedExecutionHandler() {
		return rejectedExecutionHandler;
	}

	/**
	 * @param rejectedExecutionHandler the rejectedExecutionHandler to set
	 * @return 
	 */
	public TaskExecutorBuilder setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = rejectedExecutionHandler;
		return this;
	}
	

	/**
	 * @return the beforeHandlers
	 */
	public List<BeforeExecutionHandler> getBeforeHandlers() {
		return beforeHandlers;
	}


	/**
	 * @param beforeHandlers the beforeHandlers to set
	 */
	public void setBeforeHandlers(List<BeforeExecutionHandler> beforeHandlers) {
		this.beforeHandlers = beforeHandlers;
	}


	/**
	 * @return the afterHandlers
	 */
	public List<AfterExecutionHandler> getAfterHandlers() {
		return afterHandlers;
	}


	/**
	 * @param afterHandlers the afterHandlers to set
	 */
	public void setAfterHandlers(List<AfterExecutionHandler> afterHandlers) {
		this.afterHandlers = afterHandlers;
	}



}