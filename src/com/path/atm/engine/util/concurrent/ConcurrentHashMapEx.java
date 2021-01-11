package com.path.atm.engine.util.concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapEx<K, V> extends ConcurrentHashMap<K, V>{
	
	/**
	 * usable bits of normal node hash
	 */
	static final int HASH_BITS = 0x7fffffff;
	
	/**
	 * Hold Segment locks
	 */
	private Object[] locks = null;
	
	
	/**
	 * Creates a new, empty map with a default initial capacity (16),
	 * load factor (0.75) and concurrencyLevel (16).
	 */
	public ConcurrentHashMapEx() {
		
		super();
		initSegmentLocks(16);
	}
	
	
	/**
	 * Creates a new, empty map with a default initial capacity (16),
	 * load factor (0.75) and concurrencyLevel.
	 */
	public ConcurrentHashMapEx(int concurrencyLevel) {
		super(16,(float) 0.75, concurrencyLevel);
		initSegmentLocks(concurrencyLevel);
		
	}


	/**
	 * Creates a new, empty map with a initial capacity,
	 * load factor (0.75) and concurrencyLevel.
	 */
	public ConcurrentHashMapEx(int initialCapacity, int concurrencyLevel) {
		
		super(initialCapacity,(float) 0.75, concurrencyLevel);
		initSegmentLocks(concurrencyLevel);
	}
	
	
	/**
	 * Maps the specified key to the specified value in this table.
	 * This method use the segment locking
	 * @param key
	 * @param value
	 * @return
	 */
	public V putEx(K key, V value) {
		int hash = key.hashCode() & HASH_BITS;
		synchronized (locks[hash % locks.length]) {
			return this.put(key, value);
		}
	}
	
	/**
	 * Returns the value to which the specified key is mapped,
	 * or null if this map contains no mapping for the key.
	 * @param key
	 * @return
	 */
	public V getEx(K key) {
		int hash = key.hashCode() & HASH_BITS;
		synchronized (locks[hash % locks.length]) {
			return this.get(key);
		}
	}
	
	
	/**
     * Removes the key (and its corresponding value) from this map.
     * This method does nothing if the key is not in the map.
     *
     * @param  key the key that needs to be removed
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}
     * @throws NullPointerException if the specified key is null
	 */
	public void removeEx(K key) {
		int hash = key.hashCode() & HASH_BITS;
		synchronized (locks[hash % locks.length]) {
			this.remove(key);
		}
	}
	
	
	/**
	 * Return segment lock of the given key
	 * @param key
	 * @return
	 */
	public Object getSegLockByKey(K key ){
		int hash = key.hashCode() & HASH_BITS;
		return locks[hash % locks.length];
	}
	
	
	/**
	 * Initialize and populate the locks array
	 * @param concurrencyLevel
	 */
	private void initSegmentLocks(int concurrencyLevel) {
		locks = new Object[concurrencyLevel];
		for(int i = 0;i<locks.length;i++)
			locks[i] = new Object();
	}


    /**
     * Returns a string representation of this map.
     * @return a string representation of this map
     */
	public String toString() {
		/**
		 * @todo We should add more details abotu the locks
		 * and the segments
		 */
		String concurrentHmExInfo = "Object: %s , "
				+ "locks : { count => %s }";
		
		return String.format(concurrentHmExInfo,
			super.toString(),
			locks.length
		);
	}
	
}
