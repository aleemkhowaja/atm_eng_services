package com.path.atm.engine.locks;

/**
 * This class represent the parents of all Locks in the system
 * 
 * @author MohammadAliMezzawi
 * @param <K>
 */
public class LockResource<K> {
	
	/**
	 * Hold lock id
	 */
	private K key;
	
	
	/**
	 * Create lock resource instance
	 * @param key
	 */
	public LockResource(K key){
		setKey(key);
	}
	
	
	/**
	 * Get lock Key
	 * @return
	 */
	public K getKey() {
		return key;
	}
	
	
	/**
	 * Set lock key
	 * @param key
	 */
	public void setKey(K key) {
		this.key = key;
	}

}
