package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * Implemented
 * @author MohammadAliMezzawi
 *
 */
public class EngineReactorShutdownException extends BaseEngineException {
    
    /**
     * Constructs an EngineReactorShutdownException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public EngineReactorShutdownException(EngineError error) {
    	super(error);
    }
    
    /**
     * Constructs a new EngineReactorShutdownException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public EngineReactorShutdownException(EngineError error, Throwable cause) {
        super(error,cause);
    }

}
