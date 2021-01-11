package com.path.atm.engine.pool.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * This Timing task pool should be used only in debug mode else it would create
 * more overload and slow IO will occur due to nanoTime implementation.
 * 
 * @author MohammadAliMezzawi
 * @deprecated But don't delete it. rather we will use the before and after execution handler
 */
public class TimingTaskPoolExecutor extends TaskPoolExecutor {

	/**
	 * Hold the start time of execution
	 */
	private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	
	/**
	 * Hold reference to the logger.
	 */
	private final Logger log = Logger.getLogger("TimingThreadPool");
	
	/**
	 * Hold number of task executed
	 */
	private final AtomicLong nbOfTsk = new AtomicLong();
	
	/**
	 * Total time, hold the total execution time amount
	 */
	private final AtomicLong totalTime = new AtomicLong();

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 * @param handler
	 */
	public TimingTaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
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
	public TimingTaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
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
	public TimingTaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
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
	public TimingTaskPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Method invoked prior to executing the given Runnable in the given thread.
	 *
	 * @param t the thread that will run task {@code r}
	 * @param r the task that will be executed
	 */
	protected void beforeExecute(Thread t, Runnable r) {

		// invoke the normal process
		super.beforeExecute(t, r);

		// @todo we should log this part
		log.fine(String.format("Thread %s: start %s", t, r));

		// set start time
		startTime.set(System.nanoTime());
	}

	
	/**
	 * @param r the runnable that has completed
	 * @param t the exception that caused termination, or null if execution
	 *          completed normally
	 */
	protected void afterExecute(Runnable r, Throwable t) {
		try {
			// costly due to it's implementation
			long endTime = System.nanoTime();
			long taskTime = endTime - startTime.get();

			nbOfTsk.incrementAndGet();
			totalTime.addAndGet(taskTime);

			log.fine(String.format("Thread %s: end %s, time=%dns", t, r, taskTime));
		} finally {
			// always call the parent
			super.afterExecute(r, t);
		}
	}

	
    /**
     * Method invoked when the Executor has terminated.
     */
	protected void terminated() {
		try {
			log.info(String.format("Terminated: avg time=%dns", totalTime.get() / nbOfTsk.get()));
		} finally {
			super.terminated();
		}
	}
}