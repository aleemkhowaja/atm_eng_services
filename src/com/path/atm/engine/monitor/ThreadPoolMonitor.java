package com.path.atm.engine.monitor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 
 * @author MohammadAliMezzawi
 *
 */
public class ThreadPoolMonitor implements Runnable {
	
	private ThreadPoolExecutor executor;
	private int seconds;
	private boolean run = true;

	public ThreadPoolMonitor(ThreadPoolExecutor executor, int delay) {
		this.executor = executor;
		this.seconds = delay;
	}

	@Override
	public void run() {
		while (run) {
			StringBuilder sb = new StringBuilder();
			sb.append("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d,");
			sb.append(" isShutdown: %s, isTerminated: %s");
			
			//@todo check if executor is off also
			System.out.println(String.format(
					sb.toString(),
					this.executor.getPoolSize(),
					this.executor.getCorePoolSize(),
					this.executor.getActiveCount(),
					this.executor.getCompletedTaskCount(),
					this.executor.getTaskCount(),
					this.executor.isShutdown(),
					this.executor.isTerminated()));
			try {
				Thread.sleep(seconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Turn off the monitor thread
	 */
	public void shutdown() {
		this.run = false;
	}
}