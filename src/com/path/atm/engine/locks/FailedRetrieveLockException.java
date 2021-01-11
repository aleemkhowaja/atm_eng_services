package com.path.atm.engine.locks;

/**
 * Exception thrown when attempting to retrieve lock. This exception can be
 * inspected using the {@link #getCause()} method.
 * 
 * @todo : we may remove it.
 * @author MohammadAliMezzawi
 */
public class FailedRetrieveLockException extends Exception {
	
    /**
     * Constructs an {@code FailedRetrieveLockException} with no detail message.
     * The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     */
    protected FailedRetrieveLockException() { }

    /**
     * Constructs an {@code FailedRetrieveLockException} with the specified detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     *
     * @param message the detail message
     */
    protected FailedRetrieveLockException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code FailedRetrieveLockException} with the specified detail
     * message and cause.
     *
     * @param  message the detail message
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public FailedRetrieveLockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code FailedRetrieveLockException} with the specified cause.
     * The detail message is set to {@code (cause == null ? null :
     * cause.toString())} (which typically contains the class and
     * detail message of {@code cause}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public FailedRetrieveLockException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs an {@code FailedRetrieveLockException} with the specified Key 
     * and try index.
     *
	 * @param lockKey
	 * @param tryIndex
	 */
	public FailedRetrieveLockException(Object lockKey, int tryIndex) {
		super(new StringBuilder("Try To Retrieve Key => ")
				.append(lockKey.toString())
				.append(" Try Count => ")
				.append( tryIndex).toString());
	}

}
