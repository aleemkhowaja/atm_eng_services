package com.path.atm.engine.pool.tasks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.path.atm.engine.locks.LockManager;

/**
 * This class house the ordered execution of thread
 * that belong to the same group.
 * 
 * Kindly don't update the code before contacting the author
 * @author MohammadAliMezzawi
 *
 */
public class OrderedRunnableTask implements Runnable{

	/**
	 * Task to execute
	 */
	private Task task;
	
	/**
	 * @param taskExecute
	 */
	public OrderedRunnableTask( Task taskExecute ) {
		this.task = taskExecute;
	}
	
	@Override
	public void run() {
		
//		try {
//			
//			// get the lock based on the group key
//			String groupKey = task.getGroupKey();
//			ReentrantLock lock = LockManager.getLock(groupKey);
//			
//			if()
//				tryLock( lock );
//			
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
