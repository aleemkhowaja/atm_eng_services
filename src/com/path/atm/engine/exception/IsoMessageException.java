package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * This exception is thrown whenever an operation related
 * to iso message manipulation/creation failed
 *
 */
public class IsoMessageException extends IsoBaseException{

    /**
     * Constructs an IsoMessageException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public IsoMessageException(EngineError error) {
        super(error);
    }
    
    /**
     * Constructs a new IsoMessageException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public IsoMessageException(EngineError error, Throwable cause) {
        super(error, cause);
    }
}
