package com.path.atm.engine.pool.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.path.atm.engine.pool.handler.AfterExecutionHandler;
import com.path.atm.engine.pool.handler.BeforeExecutionHandler;

/**
 * An {@link ReactorthreadPoolExecutor} that executes each submitted task using one of
 * possibly several pooled threads.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class TaskPoolExecutor extends ThreadPoolExecutor {

	/**
	 * Handler called before executing the runnable
	 */
	private volatile List<BeforeExecutionHandler> beforeHandlers = new ArrayList<BeforeExecutionHandler>();

	/**
	 * Handler called before executing the runnable
	 */
	private volatile List<AfterExecutionHandler> afterHandlers = new ArrayList<AfterExecutionHandler>();

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 * @param handler
	 */
	public TaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 */
	public TaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param handler
	 */
	public TaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 */
	public TaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Add After execution handler
	 * 
	 * @param handler
	 */
	public void addAfterExecHandler(AfterExecutionHandler handler) {

		if (handler == null)
			throw new NullPointerException("AfterExecutionHandler handler can't be null");

		afterHandlers.add(handler);
	}

	/**
	 * Add before execution handler
	 * 
	 * @param handler
	 */
	public void addBeforeExecHandler(BeforeExecutionHandler handler) {

		if (handler == null)
			throw new NullPointerException("BeforeExecutionHandler handler can't be null");

		beforeHandlers.add(handler);
	}

	/**
	 * Method invoked prior to executing the given Runnable in the given thread.
	 *
	 * @param t the thread that will run task {@code r}
	 * @param r the task that will be executed
	 */
	protected void beforeExecute(Thread th, Runnable r) {

		if (null != beforeHandlers) {
			for (BeforeExecutionHandler handler : beforeHandlers) {
				
				/**
				 * @todo handle a case where submit our version of future
				 * task which will allow us to get the runnable
				 * we should prevent submitting any task using the parent
				 * submit method which doesn't provide us with the access to
				 * the runnable
				 * 
				 * Fix the test unit when implemented
				 */
				handler.handleExecution(r, th, this);
			}
		}

		super.beforeExecute(th, r);
	}

	
	/**
	 * @param r the runnable that has completed
	 * @param t the exception that caused termination, or null if execution
	 *   completed normally
	 */
	protected void afterExecute(Runnable r, Throwable t) {

		if (null != afterHandlers) {
			for (AfterExecutionHandler handler : afterHandlers) {
				/**
				 * @todo handle a case where submit our version of future
				 * task which will allow us to get the runnable
				 * we should prevent submitting any task using the parent
				 * submit method which doesn't provide us with the access to
				 * the runnable
				 * 
				 * Fix the test unit when implemented
				 */
				handler.handleExecution(r, this);
			}
		}

		super.afterExecute(r, t);
	}
	
	
	/**
     * Method invoked when the Executor has terminated.
     * Current implementation does nothing other than calling the parent.
     * @todo : we should add log here 
     */
	protected void terminated() {
		super.terminated();
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

		if (beforeHandlers == null)
			throw new NullPointerException("beforeHandlers can't be null");

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

		if (afterHandlers == null)
			throw new NullPointerException("afterHandlers can't be null");

		this.afterHandlers = afterHandlers;
	}
}