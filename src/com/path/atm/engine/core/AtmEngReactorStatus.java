package com.path.atm.engine.core;

/**
 * This enumeration list all the engine reactor available status
 * 
 * @author MohammadAliMezzawi
 */
public enum AtmEngReactorStatus
{

    STARTING("ST"), RUNNING("R"), SHUTDOWN("SD"), DOWN("D"), CORRUPTED("C");

    /**
     * Status name
     */
    private String code;

    /**
     * Constructor
     * 
     * @param code
     */
    private AtmEngReactorStatus(String code)
    {
	setCode(code);
    }

    /**
     * @return the code
     */
    public String getCode()
    {
	return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code)
    {
	this.code = code;
    }

    /**
     * Return String representation of the Status
     */
    public String toString()
    {
	return this.code;
    }

}
