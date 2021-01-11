package com.path.atm.engine.pool.tasks;

import java.io.Serializable;

/**
 * This interface represent the atomic task functionality 
 * of the ATM engine
 * 
 * @author MohammadAliMezzawi
 */
public interface Task extends Runnable, Serializable {

	/**
	 * Return the group key of this task
	 * @return
	 */
	public String getGroupKey();
	
	/**
	 * Return the task identifier
	 * @return
	 */
	public String getUuid();
	
	/**
	 * Set the task identifier
	 * @return
	 */
	public void setUuid(String uuid);
}
