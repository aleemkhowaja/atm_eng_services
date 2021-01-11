package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

/**
 * @author MohammadAliMezzawi
 *
 */
abstract public class BaseEngineRuntimeException extends RuntimeException {

	/**
	 * variable that will hold the errorCode of the message
	 */ 
	private Integer errorCode;
	
    /**
     * Constructs an BaseEngineRuntimeException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public BaseEngineRuntimeException(EngineError error) {
    	super(error.toString());
        setErrorCode(error.getCode());
    }
    
    
    /**
     * Constructs a new BaseEngineRuntimeException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public BaseEngineRuntimeException(EngineError error, Throwable cause) {
    	super(error.toString(),cause);
    	setErrorCode(error.getCode());
    }
    
    /**
     * Constructs a new IsoRuntimeException exception with 
     * the specified detail Error and cause.
     *
     * @param error
     * @param errorMsg
     */
    public BaseEngineRuntimeException(EngineError error, String errorMsg) {
    	super(error.toString().concat(" ").concat(errorMsg));
    	setErrorCode(error.getCode());
    }
    
    /**
     * Constructs a new IsoRuntimeException exception with 
     * the specified detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public BaseEngineRuntimeException(EngineError error, String errorMsg, Throwable cause) {
    	super(error.toString().concat(errorMsg),cause);
    	setErrorCode(error.getCode());
    }
    
    
	/**
	 * @return the errorCode
	 */
	public Integer getErrorCode() {
		return errorCode;
	}


	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
}
