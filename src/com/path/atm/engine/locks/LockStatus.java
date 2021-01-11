package com.path.atm.engine.locks;

/**
 * Enumeration of lock status.
 * <p> Whenever a lock is created it will be marked as New.
 * Whenever it's lock it's status will be set as Locked until it get unlocked
 * 
 * 
 * @author MohammadAliMezzawi
 */
public enum LockStatus {
	
	NEW("N"),
	LOCKED("L"),
	UNLOCKED("U"),
	
	/**
	 * After the implementation of Abstract lock we have reconsider the usage of this
	 * status and will be deprecated in future release
	 * @deprecated
	 */
	INVALID("INV");
	
	/**
	 * Lock status
	 */
	private String status;
	
	
	/**
	 * @param status
	 */
	private LockStatus(String status ){
		this.status = status;
	}
}
