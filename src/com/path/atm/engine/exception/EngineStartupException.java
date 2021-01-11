package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * Implemented
 * @author MohammadAliMezzawi
 *
 */
public class EngineStartupException extends BaseEngineException {
    
    /**
     * Constructs an EngineReactorStartupException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public EngineStartupException(EngineError error) {
    	super(error);
    }
    
    /**
     * Constructs a new EngineReactorStartupException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public EngineStartupException(EngineError error, Throwable cause) {
        super(error,cause);
    }
    
	/**
	 * @param error
	 * @param errorMsg
	 */
	public EngineStartupException(EngineError error, String errorMsg) {
		super(error, errorMsg);
	}
}
