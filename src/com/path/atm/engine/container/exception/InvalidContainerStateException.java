package com.path.atm.engine.container.exception;

import com.path.atm.engine.container.AbstractContainer;

public class InvalidContainerStateException extends RuntimeException {
    /**
     * Constructs an {@code InvalidContainerStateException} with no detail message.
     * The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     */
    protected InvalidContainerStateException() { }

    /**
     * Constructs an {@code InvalidContainerStateException} with the specified detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     *
     * @param message the detail message
     */
    public InvalidContainerStateException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code InvalidContainerStateException} with the specified detail
     * message and cause.
     *
     * @param  message the detail message
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public InvalidContainerStateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code InvalidContainerStateException} with the specified cause.
     * The detail message is set to {@code (cause == null ? null :
     * cause.toString())} (which typically contains the class and
     * detail message of {@code cause}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method)
     */
    public InvalidContainerStateException(Throwable cause) {
        super(cause);
    }

    
    /**
     * Constructs an {@code InvalidContainerStateException} with the specified Task container.
     * The detail message.
	 * @param simpleTaskContainer
	 * @param message
	 */
	public InvalidContainerStateException(AbstractContainer taskContainer, String message) {
		super(new StringBuilder(taskContainer.getName())
				.append(message).toString());
		
	}
}
