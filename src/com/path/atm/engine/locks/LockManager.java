package com.path.atm.engine.locks;

import java.util.concurrent.TimeUnit;

/**
 * Represents a concurrency lock manager which will handle lock retrieval, release
 * and statistic about lock acquisition.
 * It uses a custom concurrent hash map to store the pairs of key and locks.
 * There is no limitation to the storage size.
 * 
 * <p> Lock Manager will try to protect the process from the invalid lock
 * by trying to renew them
 * 
 * @author MohammadAliMezzawi
 *
 * @param <K>
 * @param <V>
 */
public class LockManager<K> {
	
	/**
	 * Locks Pool
	 */
	private LocksPool<K> locksPool;
	
	
	/**
	 * Acquire the lock for the specified key.
	 * @param key
	 * @throws InterruptedException 
	 * @throws InvalidLockException 
	 * @throws Exception 
	 */
	public void acquireLock(K key) throws InvalidLockException,
		InterruptedException{
		
		Lock<K> lock = locksPool.createIfAbsent(key);
		lock.lock();
	}
	
	
	/**
	 * Try to Acquire the lock for the specified key.
	 * return true if lock was successfully acquired by the current thread
	 * @param key
	 * @return Boolean
	 * @throws InvalidLockException 
	 * @throws Exception 
	 */
	public boolean tryAcquireLock(K key) throws InvalidLockException {
		
		Lock<K> lock = locksPool.createIfAbsent(key);
		return lock.tryLock();
	}
	
	
    /**
     * Acquires the lock if it is not held by another thread within the given
     * waiting time and the current thread has not been
     * {@linkplain Thread#interrupt interrupted}.
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 * 
     * @throws InterruptedException 
     * @throws InvalidLockException 
	 * @throws Exception
	 */
	public boolean tryAcquireLock(K key, long timeout, TimeUnit unit) 
			throws InvalidLockException, 
			InterruptedException{
		
		Lock<K> lock = locksPool.createIfAbsent(key);
		return lock.tryLock(timeout,unit);
	}
	
	
	/**
	 * Release the lock for the specified key.
	 * @throws InvalidLockException 
	 * @throws Exception 
	 */
	public void releaseLock(K key) throws InvalidLockException{
		
		/**
		 * Try to get the lock if exist
		 * and try to release it.
		 */
		Lock<K> lock = locksPool.getLock(key);
		
		if(lock != null )
			lock.unlock();
	}
	
	
	/**
	 * Release the lock for the specified key.
	 * @throws InvalidLockException 
	 * @throws Exception 
	 */
	public void breakLock(K key) throws InvalidLockException{
		
		/**
		 * Try to get the lock if exist
		 * and try to release it.
		 */
		Lock<K> lock = locksPool.getLock(key);
		
		if(lock != null )
			lock.breakLock();
	}
	
	
	/**
	 * Return locks pool
	 * @return
	 */
	public LocksPool<K> getLocksPool() {
		return locksPool;
	}


	/**
	 * Set locks Pool
	 * @param locksPool
	 */
	public void setLocksPool(LocksPool<K> locksPool) {
		this.locksPool = locksPool;
	}
	
	
	/**
	 * Return a string representation of the lock.
	 */
	public String toString() {
		
		String lockInfo = "Object: %s , "
				+ "Internal Pool : %s";
		
		return String.format(lockInfo,
			super.toString(),
			locksPool.toString());
	}


	/**
	 * Lock Manager Builder Class.
	 * This builder lets us construct complex Locks Manager step by step.
	 * It allows us to produce different types and representations of an LockManager
	 * using the same construction code 
	 */
	public static class LockManagerBuilder<BK>{
		
		/**
		 * Initial pool capacity
		 */
		private int initialCapacity;
		
		/**
		 * Concurrent level
		 */
		private int concurrencyLevel;
		
		/**
		 * Lock time to live
		 */
		private long lockTtl;
		
		
		/**
		 * Build and return Lock Manager
		 * @return
		 */
		public LockManager<BK> build() {
			
			// create lockPool
			LocksPool<BK> pool = new LocksPool<BK>(getInitialCapacity()
					,getConcurrencyLevel(),getLockTtl());
			LockManager<BK> lockManager = new LockManager<BK>();
			lockManager.setLocksPool(pool);
			return lockManager;
		}
		
		
		/**
		 * Return pool initial Capacity
		 * @return
		 */
		public int getInitialCapacity() {
			return initialCapacity;
		}

		
		/**
		 * Set pool initial Capacity
		 * @return 
		 * @return
		 */
		public LockManagerBuilder<BK> setInitialCapacity(int initialCapacity) {
			this.initialCapacity = initialCapacity;
			return this;
		}
		
		
		/**
		 * Return Pool Conncurreny level
		 * @return
		 */
		public int getConcurrencyLevel() {
			return concurrencyLevel;
		}

		
		/**
		 * Set Pool Concurrent level
		 * @param concurrencyLevel
		 */
		public LockManagerBuilder<BK> setConcurrencyLevel(int concurrencyLevel) {
			this.concurrencyLevel = concurrencyLevel;
			return this;
		}

		
		/**
		 * Return lokc time to live
		 * @return
		 */
		public long getLockTtl() {
			return lockTtl;
		}

		
		/**
		 * Set Lock Time to live
		 * @param ttl
		 */
		public LockManagerBuilder<BK> setLockTtl(long ttl) {
			this.lockTtl = ttl;
			return this;
		}
	}
}
