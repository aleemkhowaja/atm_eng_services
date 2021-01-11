package com.path.atm.engine.util;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Simple stop watch, allowing for timing of a number of tasks, exposing total
 * running time and running time for each named task.
 *
 */
public class LightSimpleChronometer {

	/**
	 * SimpleChronometer Singleton instance
	 */
	private volatile static ConcurrentHashMap
		<String,LightSimpleChronometer> _instances = new ConcurrentHashMap<>();
	
	/**
	 * Identifier
	 * This will be used later on when multiple chronometers are created
	 */
	private final String id;

	/**
	 * Hold the started tasks
	 */
	private final HashMap<String,Long> startingTask = new HashMap<>();
	
	/**
	 * Hold the started tasks
	 */
	private final HashMap<String,Long> endingTask = new HashMap<>();


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
	public static LightSimpleChronometer getInstance(String instanceKey)
	{
		// workarround to avoid extra object creation
		if(_instances.get(instanceKey) == null){
			_instances.putIfAbsent(instanceKey, new LightSimpleChronometer(instanceKey));
		}
		
		return _instances.get(instanceKey);
	}
	
	/**
	 * Construct a new {@code StopWatch} with the given ID.
	 * <p>The ID is handy when we have output from multiple stop watches and need
	 * to distinguish between them.
	 * <p>Does not start any task.
	 * @param id identifier for this stop watch
	 */
	private LightSimpleChronometer(String id) {
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
		startingTask.put(taskName,System.nanoTime());
	}

	/**
	 * Stop the current task.
	 * <p>The results are undefined if timing methods are called without invoking
	 * at least one pair of {@code start()} / {@code stop()} methods.
	 * @see #start()
	 * @see #start(String)
	 */
	public void stop(String taskName) throws IllegalStateException {
		endingTask.put(taskName,System.nanoTime());
		++this.taskCount;
	}
	
	
	/**
	 * Get the total time in nanoseconds for all tasks.
	 * @since 5.2
	 * @see #getTotalTimeMillis()
	 * @see #getTotalTimeSeconds()
	 */
	public long getTotalTimeNanos() {
		
		if(totalTimeNanos <= 0 )
			totalTimeNanos = calculateTotalElapsed();
		
		return this.totalTimeNanos;
	}

	/**
	 * Get the total time in milliseconds for all tasks.
	 * @see #getTotalTimeNanos()
	 * @see #getTotalTimeSeconds()
	 */
	public long getTotalTimeMillis() {
		return nanosToMillis(getTotalTimeNanos());
	}

	/**
	 * Get the total time in seconds for all tasks.
	 * @see #getTotalTimeNanos()
	 * @see #getTotalTimeMillis()
	 */
	public double getTotalTimeSeconds() {
		return nanosToSeconds(getTotalTimeNanos());
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
		return "SimpleChronometer '" + getId() 
		+ "': running time = " + getTotalTimeMillis() + " ms";
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
        Iterator<Entry<String, Long>> hmIterator = startingTask.entrySet().iterator(); 
  
        while (hmIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            String taskName = (String) mapElement.getKey();
            Long startingTime = (Long) mapElement.getValue(); 
            Long endingTime = endingTask.get(taskName);
            
            sb.append(nf.format(nanosToMillis(endingTime - startingTime))).append("  ");
            sb.append(pf.format((double) (endingTime - startingTime) / getTotalTimeNanos())).append("  ");
			sb.append(taskName).append("\n");
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
		Iterator<Entry<String, Long>> hmIterator = startingTask.entrySet().iterator(); 
		   
  
        while (hmIterator.hasNext()) { 
        	 Map.Entry mapElement = (Map.Entry)hmIterator.next();
             String taskName = (String) mapElement.getKey();
             Long startingTime = (Long) mapElement.getValue(); 
             Long endingTime = endingTask.get(taskName);
             
            sb.append("; [").append(taskName).append("] took ").append((endingTime - startingTime)).append(" ns");
			long percent = Math.round(100.0 * (endingTime - startingTime) / getTotalTimeNanos());
			sb.append(" = ").append(percent).append("%");
        } 
        
		return sb.toString();
	}


	/**
	 * Calculate total elapsed time
	 */
	private long calculateTotalElapsed() {
		// Getting an iterator 
        Iterator<Entry<String, Long>> hmIterator = startingTask.entrySet().iterator(); 
        long totalElapsed = 0; 
        while (hmIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            String taskName = (String) mapElement.getKey();
            Long startingTime = (Long) mapElement.getValue(); 
            Long endingTime = endingTask.get(taskName);
            totalElapsed += endingTime - startingTime;
        }
 
		return totalElapsed; 	
	}
	
	private static long nanosToMillis(long duration) {
		return TimeUnit.NANOSECONDS.toMillis(duration);
	}

	private static double nanosToSeconds(long duration) {
		return duration / 1_000_000_000.0;
	}


	public static void destoryInstance() {
		_instances = null;
	}
	
	public void clear() {
		startingTask.clear();
		endingTask.clear();
		taskCount = 0;
		totalTimeNanos = 0;
	}

}
