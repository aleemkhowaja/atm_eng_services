package com.path.atm.engine.locks;

import com.path.atm.engine.util.concurrent.ConcurrentHashMapEx;

/**
 * A Lock pool maintains a pool of lock instances which will
 * be shared between different threads
 * 
 * @author MohammadAliMezzawi
 *
 */
public class LocksPool<K> {
	
	/**
	 * Hold reference to the pool
	 */
	private ConcurrentHashMapEx<K,Lock> pool;
	
	/**
	 * Key Time to live
	 */
	private long lockTtl;
	
	/**
	 * Hold the maximum renew time per request
	 */
	private final long maxRenewTime = 2;
	
	/**
	 * Create an empty lock pool with default 
	 * initial capacity 16
	 */
	public LocksPool(){
		pool = new ConcurrentHashMapEx<>();
		lockTtl = Long.MAX_VALUE;
	}
	
	/**
	 * Create an empty lock pool with 
	 * initial capacity equal to initialCapacity and concurrencyLevel
	 * equal to concurrencyLevel
	 */
	public LocksPool(int initialCapacity, int concurrencyLevel, long ttl){
		pool = new ConcurrentHashMapEx<>(initialCapacity,concurrencyLevel);
		lockTtl = ttl;
	}
	
	/**
	 * Create an empty lock pool with default 
	 * initial capacity 16
	 * 
	 */
	public LocksPool(int initialCapacity, long ttl){
		pool = new ConcurrentHashMapEx<>(initialCapacity,16);
		lockTtl = ttl;
	}
	
	/**
	 * Create an empty lock pool with default 
	 * initial capacity 16
	 * 
	 */
	public Lock<K> getLock(K lockKey) {
		return retrieveLock(lockKey);
	}
	
	
	/**
	 * <p> Retrieve a lock based on a given key.
	 * 1- if the lock isn't found or isn't valid then it will be recreated.
	 * 2- it should never return an invalid lock or a lock that could be suspect of an eviction in the near time.
	 * 
	 * @return
	 */
	public Lock<K> createIfAbsent(K lockKey ) {
		
		// create tmp lock
		Lock<K> lock = new Lock<K>(lockKey, lockTtl );
		
		// get the segment lock
		Object segLock = pool.getSegLockByKey(lockKey);
		
		// Govern the segment 
		synchronized(segLock) {
			
			// put if absent
			pool.putIfAbsent(lockKey, lock);
			
			// get the lock
			lock = pool.getEx(lockKey);
			
			// force renew the key
			lock.extendLife();
		}
		
		return lock;
		
	}
	
	
	/**
	 * <p> Retrieve a lock based on a given key.
	 * It will try to extend it is life if possible
	 * 
	 * @return Lock/Null
	 */
	private Lock<K> retrieveLock(K lockKey )  {
		
		// create tmp lock
		Lock<K> lock = null;
		
		Object segLock = pool.getSegLockByKey(lockKey);
		
		// Govern the segment 
		synchronized(segLock) {
			
			// get the lock
			lock = pool.getEx(lockKey);
			
			// renew the key
			if( null != lock )
				 lock.extendLife();
		}
		
		return lock;
	}
	
	
	/**
	 * Return a string representation of the Pool lock.
	 */
	public String toString() {
		
		String lockInfo = "Object: %s , \n"
				+ "Pool Info : { basic info : %s }"
				+ "TTl : %s";
		
		return String.format(lockInfo,
			super.toString(),
			pool.toString(),
			lockTtl);
	}
}
