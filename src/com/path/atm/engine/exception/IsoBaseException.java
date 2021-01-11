package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * Represent the main parent of all iso exception which could be thrown while
 *  creating, parsing and formatting an ISO message.
 *  
 * @author MohammadAliMezzawi
 *
 */
public class IsoBaseException extends BaseEngineException{

    /**
     * Constructs an IsoBaseException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public IsoBaseException(EngineError error) {
        super(error);
    }
    
    /**
     * Constructs a new IsoBaseException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public IsoBaseException(EngineError error, Throwable cause) {
    	super(error,cause);
    }


	/**
	 * Constructs a new IsoBaseException exception with 
     * the specified detail Error and an error message
     * 
	 * @param error
	 * @param errorMsg
	 */
	public IsoBaseException(EngineError error, String errorMsg) {
		super(error,errorMsg);
	}


	/**
	 * Constructs a new IsoBaseException exception with 
     * the specified detail Error,cause and error message.
     * 
	 * @param error
	 * @param errorMsg
	 * @param cause
	 */
	public IsoBaseException(EngineError error, String errorMsg, Throwable cause) {
		super(error,errorMsg,cause);
	}

}
