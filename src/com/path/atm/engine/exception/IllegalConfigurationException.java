package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * Signals that the object configuration is not in 
 * an appropriate state for the requested operation.
 *
 */
public class IllegalConfigurationException extends BaseEngineRuntimeException{
    
    /**
     * Constructs an IllegalConfigurationException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public IllegalConfigurationException(EngineError error) {
        super(error);
    }
    
    /**
     * Constructs a new IllegalConfigurationException exception with 
     * the specified detail Error and cause.
     *
     * @param error
     * @param errorMsg
     */
    public IllegalConfigurationException(EngineError error, String errorMsg) {
    	super(error,errorMsg);
    }
    
    /**
     * Constructs a new IllegalConfigurationException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public IllegalConfigurationException(EngineError error, Throwable cause) {
        super(error, cause);
    }
}
