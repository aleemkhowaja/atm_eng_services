package com.path.atm.engine.pool.tasks;

import java.io.Serializable;

/**
 * This Class represent a wrapper for an atomic task which will be executed by
 * the Atm engine.
 * 
 * <p>
 * whenever an operation is submitted to the engine, it will be wrapped with
 * this class.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class SimpleTask implements Task, Serializable
{

    /**
     * A Unique id that identify the task
     */
    private String uuid;

    @Override
    public String getGroupKey()
    {
	return null;
    }

    /**
     * @return the uuid
     */
    public String getUuid()
    {
	return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid)
    {
	this.uuid = uuid;
    }

    @Override
    public void run()
    {
	// TODO Auto-generated method stub
	
    }

}
