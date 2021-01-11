package com.path.atm.engine.locks;

import org.apache.kahadb.util.LockFile;

import com.path.bo.reporting.ReportingConstantsCommon;
import com.path.lib.common.util.FileUtil;
import com.path.lib.common.util.PathFileSecure;
import com.path.lib.log.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Represents an exclusive lock on a file.
 *
 */
public class SharedFileLock extends SeekerLock {

	/**
	 * Hold reference to the log
	 */
	private final static Log log = Log.getInstance();

	/**
	 * 
	 */
	private final static String DEFAULT_DIR = ReportingConstantsCommon.repositoryFolder;

	/**
	 * 
	 */
	private LockFile lockFile;

	/**
	 * 
	 */
	protected final String directory;

	/**
	 * @param key
	 */
	public SharedFileLock(String key) {
		this(key, DEFAULT_DIR);
	}

	/**
	 * @param key
	 */
	public SharedFileLock(String key, String directory) {

		// set the lock key
		setKey(key);

		// set the directory
		this.directory = directory;

		// initiate the lock status UNLOCKED
		getStatus().set(LockStatus.UNLOCKED);

	}

	/**
	 * Try to lock the File.
	 * <p>
	 * 
	 * @throws Exception
	 */
	public void lock() throws Exception {

		// two lock call, skip the last one
		if (lockFile != null)
			return;

		PathFileSecure lockFileName = new PathFileSecure(FileUtil.getFileURLByName(directory)
			.concat(File.separator),getKey().concat(".alck"));

		// keep using the LockFile defined in the AMQ
		lockFile = new LockFile(lockFileName, false);

		// check if fail if locked is true
		if (failIfLocked) {
			lockFile.lock();
			getStatus().compareAndSet(LockStatus.UNLOCKED, LockStatus.LOCKED);
			return;
		}

		// try to lock as long as we can
		boolean locked = false;
		boolean warned = false;

		// while the status isn't unlock try to lock it
		while (getStatus().get().equals(LockStatus.UNLOCKED)) {
			try {
				lockFile.lock();
				
				/**
				 * @note : keep in mind the issue
				 * https://issues.apache.org/jira/browse/AMQ-4705
				 * Issue on nsfv4 with a master slave configuration, where both the slave and the master
				 * could obtain a lock.
				 */
				
				locked = true;
				warned = true;
				break;
				
			} catch (IOException e) {

				log.debug("Database " + lockFileName + " is locked... waiting "
						+ (lockAcquireSleepInterval / 1000) + " seconds for the database to be unlocked. Reason: " + e);

				if (!warned) {
					log.info("Database " + lockFileName
							+ " is locked by another server. This broker is now in slave mode waiting a lock to be acquired");
					warned = true;
				}

				log.debug("Database " + lockFileName + " is locked... waiting " + this.lockAcquireSleepInterval / 1000L
						+ " seconds for the database to be unlocked. Reason: " + e);
				try {
					TimeUnit.MILLISECONDS.sleep(this.lockAcquireSleepInterval);
				} catch (InterruptedException localInterruptedException1) {
					log.info("SharedFileLock interrupted ");
				}

			}
		}
		
		
		if (!locked)
			throw new IOException("attempt to obtain lock aborted due to shutdown");
	}

	/**
	 * Release the lock
	 */
	public void unlock() {
		
		if (lockFile == null)
			return;
			
		lockFile.unlock();
		lockFile = null;
	}

	/**
	 * @note keep in mind to update this whenever we update the amq lib
	 */
	public boolean keepAlive() {
		
		/**
		 * boolean result = (this.lockFile != null) && (this.lockFile.keepAlive());
		 * log.trace("keepAlive result: " + result + (key != null ? ", name: " + key : ""));
		 * return result;
		 */
		return true;
	}
	
	  
	/**
	 * Get the directory
	 * 
	 * @return
	 */
	public String getDirectory() {
		return directory;
	}
}
