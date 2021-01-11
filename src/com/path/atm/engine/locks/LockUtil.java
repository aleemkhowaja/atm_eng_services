package com.path.atm.engine.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MohammadAliMezzawi
 *
 */
public class LockUtil {

	/**
	 * Fully release the lock by decrement the hold count
	 * @param lock
	 */
	public static void releaseLock(ReentrantLock lock) {
		
		while(lock.getHoldCount() > 0)
			lock.unlock();
		
	}

}
