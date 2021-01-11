package com.path.atm.engine.core;

/**
 * This enumeration list all the engine available status
 * 
 * @author MohammadAliMezzawi
 */
public enum AtmEngineStatus
{

    STARTING("ST"),
    RUNNING("R"),
    SHUTDOWN("SD"),
    DOWN("D"),
    CORRUPTED("C");

    /**
     * Status name
     */
    private String code;

    /**
     * Constructor
     * 
     * @param code
     */
    private AtmEngineStatus(String code)
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

}
