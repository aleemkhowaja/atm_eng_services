package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * {@code IsoRuntimeException} is the superclass of those
 * exceptions that can be thrown during the manipulation of ISO Message
 * or task.
 * @author MohammadAliMezzawi
 *
 */
public class IsoRuntimeException extends BaseEngineRuntimeException{
    
    /**
     * Constructs an IsoRuntimeException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public IsoRuntimeException(EngineError error) {
    	super(error);
    }
    
    
    /**
     * Constructs a new IsoRuntimeException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public IsoRuntimeException(EngineError error, Throwable cause) {
    	super(error,cause);
    }
    
    /**
     * Constructs a new IsoRuntimeException exception with 
     * the specified detail Error and cause.
     *
     * @param error
     * @param errorMsg
     */
    public IsoRuntimeException(EngineError error, String errorMsg) {
    	super(error, errorMsg);
    }
    
    /**
     * Constructs a new IsoRuntimeException exception with 
     * the specified detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public IsoRuntimeException(EngineError error, String errorMsg, Throwable cause) {
    	super(error,errorMsg,cause);
    }
}
