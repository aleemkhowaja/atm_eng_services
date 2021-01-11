package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * Implemented
 * @author MohammadAliMezzawi
 *
 */
public class EngineReactorStartupException extends BaseEngineException {
    
    /**
     * Constructs an EngineReactorStartupException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public EngineReactorStartupException(EngineError error) {
    	super(error);
    }
    
    /**
     * Constructs a new EngineReactorStartupException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public EngineReactorStartupException(EngineError error, Throwable cause) {
        super(error,cause);
    }
    
    /**
     * @param error
     * @param errorMsg
     * @param cause
     */
    public EngineReactorStartupException(EngineError error, String errorMsg, Throwable cause)
    {
	super(error, errorMsg, cause);
    }
}
