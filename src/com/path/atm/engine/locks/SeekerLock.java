package com.path.atm.engine.locks;

public abstract class SeekerLock extends AbstractLock {
	/**
	 * 
	 */
	public static final long DEFAULT_LOCK_ACQUIRE_SLEEP_INTERVAL = 10000L;
	
	/**
	 * 
	 */
	protected boolean failIfLocked = false;
	
	/**
	 * 
	 */
	protected long lockAcquireSleepInterval = 10000L;
	
	/**
	 * @return the failIfLocked
	 */
	public boolean isFailIfLocked() {
		return failIfLocked;
	}
	
	/**
	 * @param failIfLocked the failIfLocked to set
	 */
	public void setFailIfLocked(boolean failIfLocked) {
		this.failIfLocked = failIfLocked;
	}
	
	/**
	 * @return the lockAcquireSleepInterval
	 */
	public long getLockAcquireSleepInterval() {
		return lockAcquireSleepInterval;
	}
	
	/**
	 * @param lockAcquireSleepInterval the lockAcquireSleepInterval to set
	 */
	public void setLockAcquireSleepInterval(long lockAcquireSleepInterval) {
		this.lockAcquireSleepInterval = lockAcquireSleepInterval;
	}
	
}
