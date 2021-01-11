package com.path.atm.engine.pool.executor;

import java.util.concurrent.Future;

import com.path.atm.engine.pool.tasks.Task;

/**
 * We may change this to reactor core
 * @author MohammadAliMezzawi
 *
 */
public class TaskExecutor extends AbstractExecutor {

	/**
	 * Hold reactor executor
	 */
	private String name;
	
	/**
	 * Hold pool executor
	 */
	private TaskPoolExecutor poolExecutor;

	/**
	 * Create new Task Executor builder
	 * @return
	 */
	public static TaskExecutorBuilder newBuilder() {
		return new TaskExecutorBuilder();
	}
	
	/**
	 * Shutdown the task executor.
	 */
	public void shutdownNow() {
		poolExecutor.shutdownNow();
	}

	/**
	 * Submit a task to the task executor
	 * @param tsk
	 * @return
	 */
	public Future<?> submit(Task tsk) {
		return poolExecutor.submit(tsk);
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
	 * @return the poolExecutor
	 */
	public TaskPoolExecutor getPoolExecutor() {
		return poolExecutor;
	}

	/**
	 * @param poolExecutor the poolExecutor to set
	 */
	public void setPoolExecutor(TaskPoolExecutor poolExecutor) {
		this.poolExecutor = poolExecutor;
	}
}
