package com.path.atm.engine.exception;

import com.path.atm.engine.util.EngineError;

public class IsoParseException  extends IsoBaseException  {

    /**
     * The zero-based character offset into the string being parsed at which
     * the error was found during parsing.
     * @serial
     */
    private int errorOffset;
    
    /**
     * Constructs an EngineReactorStartupException with the specified detail
     * Error.
     * 
     * @param error the EngineError thrown
     */
    public IsoParseException(EngineError error) {
    	super(error);
    }
    
    
    /**
     * Constructs a new EngineReactorStartupException exception with 
     * the specified detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public IsoParseException(EngineError error, Throwable cause) {
        super(error,cause);
    }
    
    /**
     * Constructs a new EngineReactorStartupException exception with 
     * the specified detail Error and cause.
     *
     * @param error
     * @param errorMsg
     */
    public IsoParseException(EngineError error, String errorMsg) {
        super(error,errorMsg);
    }
    
    /**
     * Constructs a new EngineReactorStartupException exception with 
     * the specified detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public IsoParseException(EngineError error, String errorMsg, Throwable cause) {
        super(error,errorMsg,cause);
    }
    
    
    /**
     * Returns the position where the error was found.
     *
     * @return the position where the error was found
     */
    public int getErrorOffset () {
        return errorOffset;
    }

}
