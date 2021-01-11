package com.path.atm.engine.locks;

/**
 * Exception thrown when attempting to update the internal state of
 * the lock marked as Invalid or ttl is exceeded. This exception can be
 * inspected using the {@link #getCause()} method.
 * 
 * @author MohammadAliMezzawi
 */
public class InvalidLockException extends Exception {
    /**
     * Constructs an {@code InvalidLockException} with no detail message.
     * The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     */
    protected InvalidLockException() { }

    /**
     * Constructs an {@code InvalidLockException} with the specified detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     *
     * @param message the detail message
     */
    protected InvalidLockException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code InvalidLockException} with the specified detail
     * message and cause.
     *
     * @param  message the detail message
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public InvalidLockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code InvalidLockException} with the specified cause.
     * The detail message is set to {@code (cause == null ? null :
     * cause.toString())} (which typically contains the class and
     * detail message of {@code cause}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public InvalidLockException(Throwable cause) {
        super(cause);
    }
}
