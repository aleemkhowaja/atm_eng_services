package com.path.atm.engine.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Simple stop watch, allowing for timing of a number of tasks, exposing total
 * running time and running time for each named task.
 *
 */
public class SimpleChronometer {

	/**
	 * SimpleChronometer Singleton instance
	 */
	private volatile static SimpleChronometer instance;
	
	/**
	 * Identifier
	 * This will be used later on when multiple chronometers are created
	 */
	private final String id;

	/**
	 * Hold the started tasks
	 */
	private final HashMap<String,TaskInfo> tasks = new HashMap<>();

	/**
	 * Number of tasks started
	 */
	private int taskCount;
	
	/**
	 * Total running time.
	 */
	private long totalTimeNanos;


	/**
	 * Monitor object
	 */
	private static Object monitor = new Object();
	
	
	/**
	 * Return SimpleChronometer instance
	 */
	public static SimpleChronometer getInstance()
	{
		if(instance == null){
			synchronized(monitor){
				if(instance == null){
					instance = new SimpleChronometer();
				}
			}
		}
		
		return instance;
	}
	
	
	/**
	 * Construct a new {@code StopWatch}.
	 * <p>Does not start any task.
	 */
	private SimpleChronometer() {
		this("");
	}

	/**
	 * Construct a new {@code StopWatch} with the given ID.
	 * <p>The ID is handy when we have output from multiple stop watches and need
	 * to distinguish between them.
	 * <p>Does not start any task.
	 * @param id identifier for this stop watch
	 */
	private SimpleChronometer(String id) {
		this.id = id;
	}


	/**
	 * Get the ID of this {@code StopWatch}, as specified on construction.
	 * @return the ID (empty String by default)
	 * @since 4.2.2
	 * @see #SimpleChronometer(String)
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Start a named task.
	 * <p>The results are undefined if {@link #stop()} or timing methods are
	 * called without invoking this method first.
	 * @param taskName the name of the task to start
	 * @see #start()
	 * @see #stop()
	 */
	public void start(String taskName) throws IllegalStateException {
		// get task from the list
		if( null != getTask(taskName) )
			throw new IllegalStateException("task already started");
		
		TaskInfo taskInfo = new TaskInfo(taskName);
		taskInfo.setStartTimeNanos(System.nanoTime());
		setTask(taskName,taskInfo);
	}

	/**
	 * Stop the current task.
	 * <p>The results are undefined if timing methods are called without invoking
	 * at least one pair of {@code start()} / {@code stop()} methods.
	 * @see #start()
	 * @see #start(String)
	 */
	public void stop(String taskName) throws IllegalStateException {
		
		TaskInfo task = getTask(taskName);
		if (null == task) {
			throw new IllegalStateException("Can't stop task: it's not running");
		}
		
		long lastTime = System.nanoTime() - task.getStartTimeNanos();
		task.setTimeNanos(lastTime);
		this.totalTimeNanos += lastTime;
		++this.taskCount;
	}


	/**
	 * @return the tasks
	 */
	public HashMap<String, TaskInfo> getTasks() {
		return tasks;
	}

	/**
	 * @param taskName
	 * @return
	 */
	private TaskInfo getTask(String taskName) {
		return tasks.get(taskName);
	}
	
	
	/**
	 * Set a task to the list
	 * @param taskName
	 * @param taskInfo
	 */
	private void setTask(String taskName, TaskInfo taskInfo) {
		tasks.put(taskName, taskInfo);
	}
	
	/**
	 * Get the total time in nanoseconds for all tasks.
	 * @since 5.2
	 * @see #getTotalTimeMillis()
	 * @see #getTotalTimeSeconds()
	 */
	public long getTotalTimeNanos() {
		return this.totalTimeNanos;
	}

	/**
	 * Get the total time in milliseconds for all tasks.
	 * @see #getTotalTimeNanos()
	 * @see #getTotalTimeSeconds()
	 */
	public long getTotalTimeMillis() {
		return nanosToMillis(this.totalTimeNanos);
	}

	/**
	 * Get the total time in seconds for all tasks.
	 * @see #getTotalTimeNanos()
	 * @see #getTotalTimeMillis()
	 */
	public double getTotalTimeSeconds() {
		return nanosToSeconds(this.totalTimeNanos);
	}

	/**
	 * Get the number of tasks timed.
	 */
	public int getTaskCount() {
		return this.taskCount;
	}

	/**
	 * Get a short description of the total running time.
	 */
	public String shortSummary() {
		return "SimpleChronometer '" + getId() + "': running time = " + getTotalTimeMillis() + " ms";
	}

	/**
	 * Generate a string with a table describing all tasks performed.
	 * <p>For custom reporting, call {@link #getTaskInfo()} and use the task info
	 * directly.
	 */
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder(shortSummary());
		sb.append('\n');
		sb.append("---------------------------------------------\n");
		sb.append("ms         %     Task name\n");
		sb.append("---------------------------------------------\n");
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(9);
		nf.setGroupingUsed(false);
		NumberFormat pf = NumberFormat.getPercentInstance();
		pf.setMinimumIntegerDigits(3);
		pf.setGroupingUsed(false);
		
		// Getting an iterator 
        Iterator<Entry<String, TaskInfo>> hmIterator = getTasks().entrySet().iterator(); 
  
        while (hmIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
            TaskInfo taskInfo = (TaskInfo) mapElement.getValue(); 
            sb.append(nf.format(taskInfo.getTimeMillis())).append("  ");
            sb.append(pf.format((double) taskInfo.getTimeNanos() / getTotalTimeNanos())).append("  ");
			sb.append(taskInfo.getTaskName()).append("\n");
        } 
        
		return sb.toString();
	}

	/**
	 * Generate an informative string describing all tasks performed
	 * <p>For custom reporting, call {@link #getTaskInfo()} and use the task info
	 * directly.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(shortSummary());
		
		// Getting an iterator 
        Iterator<Entry<String, TaskInfo>> hmIterator = getTasks().entrySet().iterator(); 
  
        while (hmIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
            TaskInfo taskInfo = (TaskInfo) mapElement.getValue(); 
            sb.append("; [").append(taskInfo.getTaskName()).append("] took ").append(taskInfo.getTimeMillis()).append(" ns");
			long percent = Math.round(100.0 * taskInfo.getTimeNanos() / getTotalTimeNanos());
			sb.append(" = ").append(percent).append("%");
        } 
        
		return sb.toString();
	}


	private static long nanosToMillis(long duration) {
		return TimeUnit.NANOSECONDS.toMillis(duration);
	}

	private static double nanosToSeconds(long duration) {
		return duration / 1_000_000_000.0;
	}


	/**
	 * Nested class to hold data about one task executed within the {@code StopWatch}.
	 */
	public static final class TaskInfo {

		private final String taskName;

		/**
		 * Start time of the current task
		 */
		private long startTimeNanos;
		
		/**
		 * Elapsed time on the current task
		 */
		private long timeNanos;

		TaskInfo(String taskName) {
			this.taskName = taskName;
		}

		/**
		 * @return the startTimeNanos
		 */
		public long getStartTimeNanos() {
			return startTimeNanos;
		}

		/**
		 * @param startTimeNanos the startTimeNanos to set
		 */
		public void setStartTimeNanos(long startTimeNanos) {
			this.startTimeNanos = startTimeNanos;
		}

		/**
		 * Get the name of this task.
		 */
		public String getTaskName() {
			return this.taskName;
		}

		/**
		 * Get the time in nanoseconds this task took.
		 * @since 5.2
		 * @see #getTimeMillis()
		 * @see #getTimeSeconds()
		 */
		public long getTimeNanos() {
			return this.timeNanos;
		}

		/**
		 * @param timeNanos the timeNanos to set
		 */
		public void setTimeNanos(long timeNanos) {
			this.timeNanos = timeNanos;
		}

		/**
		 * Get the time in milliseconds this task took.
		 * @see #getTimeNanos()
		 * @see #getTimeSeconds()
		 */
		public long getTimeMillis() {
			return nanosToMillis(this.timeNanos);
		}

		/**
		 * Get the time in seconds this task took.
		 * @see #getTimeMillis()
		 * @see #getTimeNanos()
		 */
		public double getTimeSeconds() {
			return nanosToSeconds(this.timeNanos);
		}

	}


	public static void destoryInstance() {
		instance = null;
	}
	
	public void clear() {
		tasks.clear();
		taskCount = 0;
		totalTimeNanos = 0;
	}

}
