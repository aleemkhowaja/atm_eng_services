package com.path.atm.engine.locks;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class represent the parents of all Locks in the system
 * 
 * @author MohammadAliMezzawi
 * @param <K>
 */
public abstract class AbstractLock {

	/**
	 * Hold lock id
	 */
	protected String key;
	
	/**
	 * Hold the lock status
	 * 
	 */
	private AtomicReference<LockStatus> status = new AtomicReference<>();
	
	/**
	 * @return
	 * @throws IOException
	 */
	public boolean keepAlive() throws IOException {
		return true;
	}
	
	/**
	 * Get lock Key
	 * @return
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Set lock key
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * @return the status
	 */
	public AtomicReference<LockStatus> getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(AtomicReference<LockStatus> status) {
		this.status = status;
	}
}
