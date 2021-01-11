package com.path.atm.engine.pool.executor;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Encapsulates the logic of creation and configuration of threads. It creates
 * and returns thread instances on demand. This class will allow us to : 1-
 * Names the threads 2- Create daemon threads 3- Assigns thread priority 4-
 * Assigns the thread to a thread group 5- Sets handler for uncaught exceptions
 * 
 * @author MohammadAliMezzawi
 *
 */
public class TaskWorkerFactory implements ThreadFactory {

	/**
	 * Hold number of Thread factory created
	 */
	static final AtomicInteger facotryId = new AtomicInteger(1);

	/**
	 * Hold thread number ( index )
	 */
	final AtomicInteger threadNumber = new AtomicInteger(1);
	
	/**
	 * Hold the thread group
	 */
	final ThreadGroup group;
	
	/**
	 * Hold Name prefix
	 */
	private String namePrefix;

	/**
	 * Hold flag is thread is daemon
	 */
	private boolean daemon = false;

	/**
	 * Default thread priority
	 */
	private int priority = Thread.NORM_PRIORITY;

	/**
	 * Hold the UncaughtExceptionHandler Handler
	 */
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	
	
	/**
	 * @constructor
	 */
	public TaskWorkerFactory() {
		
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() :
			Thread.currentThread().getThreadGroup();

		namePrefix = null == namePrefix || namePrefix.isEmpty() ?
			"pool[" + facotryId.getAndIncrement() + "]thread": namePrefix;
	}

	
	/**
	 * @param namePrefix
	 */
	public TaskWorkerFactory( String namePrefix ) {
		this();
		setNamePrefix(namePrefix);
	}
	
	
	/**
	 * @param namePrefix
	 * @param priority
	 */
	public TaskWorkerFactory( String namePrefix, int priority ) {
		this();
		setNamePrefix(namePrefix);
		setPriority(priority);
	}
	
	
	/**
	 * @param namePrefix
	 * @param priority
	 * @param daemon
	 */
	public TaskWorkerFactory( String namePrefix, int priority, boolean daemon ) {
		this();
		setNamePrefix(namePrefix);
		setPriority(priority);
		setDaemon(daemon);
	}

	
	@Override
	public Thread newThread(Runnable r) {
		Thread newThread = new Thread(group, r, 
			namePrefix + "[" + threadNumber.getAndIncrement() + "]", 0);
		
		// set basic flag
		newThread.setDaemon(daemon);
		newThread.setPriority(priority);

		if (uncaughtExceptionHandler != null)
			newThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);

		return newThread;
	}


	/**
	 * @return the namePrefix
	 */
	public String getNamePrefix() {
		return namePrefix;
	}


	/**
	 * @param namePrefix the namePrefix to set
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}


	/**
	 * @return the daemon
	 */
	public boolean isDaemon() {
		return daemon;
	}


	/**
	 * @param daemon the daemon to set
	 */
	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}


	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}


	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}


	/**
	 * @return the uncaughtExceptionHandler
	 */
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}


	/**
	 * @param uncaughtExceptionHandler the uncaughtExceptionHandler to set
	 */
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}
}
