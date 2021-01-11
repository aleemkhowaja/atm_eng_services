package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;
import com.path.lib.common.exception.BaseException;

/**
 * @author MohammadAliMezzawi
 *
 */
abstract public class BaseEngineException extends BaseException
{

    /**
     * Constructs an IllegalConfigurationException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public BaseEngineException(EngineError error)
    {
	super(error.toString());
	setErrorCode(error.getCode());
    }

    /**
     * Constructs a new IllegalConfigurationException exception with the
     * specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public BaseEngineException(EngineError error, Throwable cause)
    {
	super(error.toString(), cause);
	setErrorCode(error.getCode());
    }

    /**
     * @param error
     * @param errorMsg
     */
    public BaseEngineException(EngineError error, String errorMsg)
    {
	super(error.toString().concat(errorMsg));
	setErrorCode(error.getCode());
    }

    /**
     * @param error
     * @param errorMsg
     * @param cause
     */
    public BaseEngineException(EngineError error, String errorMsg, Throwable cause)
    {
	super(error.toString().concat(" ").concat(errorMsg), cause);
	setErrorCode(error.getCode());
    }
}
