package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * Implemented
 * @author MohammadAliMezzawi
 *
 */
public class EngineShutdownException extends BaseEngineException {

    
    /**
     * Constructs an EngineReactorStartupException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public EngineShutdownException(EngineError error) {
    	super(error);
    }
    
    /**
     * Constructs a new EngineReactorStartupException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public EngineShutdownException(EngineError error, Throwable cause) {
        super(error,cause);
    }
}
