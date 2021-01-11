package com.path.atm.bo.engine.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import com.path.atm.bo.engine.AtmEngineBO;
import com.path.atm.dao.engine.AtmEngineDAO;
import com.path.atm.engine.core.AtmEngine;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.core.AtmEngineStarter;
import com.path.atm.engine.core.AtmEngineStatus;
import com.path.atm.engine.util.AtmCommonUtil;
import com.path.atm.vo.engine.AtmInterfaceCO;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.log.Log;

public class AtmEngineBOImpl extends BaseBO implements AtmEngineBO
{

    /**
     * Hold reference to the log
     */
    protected final static Log log = Log.getInstance();

    /**
     * Hold reference to the engine
     */
    private volatile AtmEngine engine = new AtmEngine();

    /**
     * hold reference to the Engine DAO
     */
    private AtmEngineDAO atmEngineDAO;

    /**
     * This method will execute a given iso msg on specific reactor
     * 
     * @param isoMsgDetails
     * @return
     * @throws BaseException
     */
    public HashMap<String, Object> executeIsoMsg(HashMap<String, Object> isoMsgDetails) throws BaseException
    {

	HashMap<String, Object> responseData = new HashMap<String, Object>();
	try
	{

	    if(!AtmEngineStatus.RUNNING.equals(this.engine.getStatus()))
		throw new IllegalStateException("Atm engine not started");

	    if(null == isoMsgDetails.get("interfaceCode"))
		throw new IllegalArgumentException("interfaceCode can't be null");

	    AtmEngineReactor reactor = this.engine.returnReactor((BigDecimal) isoMsgDetails.get("interfaceCode"));

	    // @todo check if reactor is started and get it's status

	    responseData = reactor.executeIsoMsg((String) isoMsgDetails.get("isoMsg"));

	}
	catch(Exception exp)
	{
	    log.error(exp, "Faild to execute Iso message");
	    throw new BaseException(AtmCommonUtil.getEngExpCauseMsg(exp));

	}

	return responseData;
    }

    /**
     * Parse Iso Message
     * 
     * @param isoMsgDetails
     * @return
     * @throws BaseException
     */
    public HashMap<String, Object> parseIsoMsg(HashMap<String, Object> isoMsgDetails) throws BaseException
    {

	HashMap<String, Object> isoMsg = new HashMap<>();

	try
	{

	    if(!AtmEngineStatus.RUNNING.equals(this.engine.getStatus()))
		throw new IllegalStateException("Atm engine not started");

	    if(null == isoMsgDetails.get("interfaceCode"))
		throw new IllegalArgumentException("interfaceCode can't be null");

	    AtmEngineReactor reactor = this.engine.returnReactor((BigDecimal) isoMsgDetails.get("interfaceCode"));

	    // @todo check if reactor is started and get it's status

	    isoMsg = reactor.parseIsoMsg((String) isoMsgDetails.get("isoMsg"));

	}
	catch(Exception exp)
	{
	    log.error(exp, "Faild to start the reactor");
	    throw new BaseException(exp.getMessage());

	}

	return isoMsg;
    }

    /**
     * This method will start a specific reactor ( interface ) of the atm engine
     * without impacting the other reactors
     */
    public void startReactor(HashMap<String, Object> interfaceCOHm) throws BaseException
    {

	try
	{

	    // prevent multiple action on the same instance
	    synchronized(engine.getMonitor())
	    {

		if(!AtmEngineStatus.RUNNING.equals(this.engine.getStatus()))
		    throw new IllegalStateException("Atm engine is not started");

		if(null == interfaceCOHm.get("interfaceCode"))
		    throw new IllegalArgumentException("interfaceCode can't be null");

		AtmInterfaceCO interfaceCO = new AtmInterfaceCO();
		interfaceCO.setCode((BigDecimal) interfaceCOHm.get("interfaceCode"));

		this.engine.startReactor(interfaceCO);
	    }

	}
	catch(Exception exp)
	{
	    log.error(exp, "Faild to start the reactor");
	    throw new BaseException(exp);

	}
    }

    /**
     * This method will shutdown a specific reactor ( interface ) of the atm
     * engine without impacting the other reactors
     */
    public void shutdownReactor(HashMap<String, Object> interfaceCOHm) throws BaseException
    {

	try
	{
	    synchronized(engine.getMonitor())
	    {

		if(!AtmEngineStatus.RUNNING.equals(this.engine.getStatus()))
		    throw new IllegalStateException("Atm engine is not started");

		if(null == interfaceCOHm.get("interfaceCode"))
		    throw new IllegalArgumentException("interfaceCode can't be null");

		AtmInterfaceCO interfaceCO = new AtmInterfaceCO();
		interfaceCO.setCode((BigDecimal) interfaceCOHm.get("interfaceCode"));
		this.engine.shutdownReactor(interfaceCO);

	    }

	}
	catch(Exception exp)
	{
	    log.error(exp, "Faild to shutdown the reactor");
	    throw new BaseException(exp);
	}
    }

    /**
     * Start Atm engine on start up
     * 
     * @note : this approach doesn't cover all racing condition scenario. but
     *       for now we assume that a start and shutdown method are done once by
     *       deploying un-deploying.
     */
    public void start() throws BaseException
    {
	try
	{

	    log.info("Starting engine");

	    // prevent multiple action on the same instance
	    synchronized(engine.getMonitor())
	    {

		log.info("Starting engine at " + Calendar.getInstance().getTime());

		/**
		 * A starter is running but didn't finalize due to lock a
		 * shutdown command occur and status is changed to D -> Starter
		 * is waiting for a lock but can't be notified. A new start
		 * command is executed. Behavior : We assume that we have one
		 * started and we shouldn't start another one unless the first
		 * is done
		 */
		if(AtmEngineStarter.getInstance().isRunning())
		{
		    throw new IllegalStateException("Another Starter thread is running " + this.engine.getStatus());
		}

		/**
		 * An engine can be either 1- Starting 2- Shutting down
		 */
		if(!AtmEngineStatus.DOWN.equals(this.engine.getStatus()))
		    throw new IllegalStateException(
			    "Failed to start engine since its status is : " + this.engine.getStatus());

		// kickoff the engine asynchronously
		log.info("kickoff the engine asynchronously at " + Calendar.getInstance().getTime());

		// change the status to Starting
		engine.setStatus(AtmEngineStatus.STARTING);

		// now start Asynchronously the engine
		AtmEngineStarter.getInstance().setAtmEngine(engine).kickOff();
	    }

	}
	catch(Exception exp)
	{

	    /**
	     * @note : never change status at this level to ( SHUTDOWN/DOWN )
	     *       This could impact the Starter thread and push it to halt
	     *       the starting of the engine
	     */
	    log.error(exp, "Failed to start engine");
	    throw new BaseException(exp);
	}

    }

    /**
     * This method will shutdown the engine
     * 
     * @throws BaseException
     */
    public void shutdown() throws BaseException
    {
	try
	{

	    log.info("Shutdown the engine ");

	    // prevent multiple action on the same instance
	    synchronized(engine.getMonitor())
	    {
		log.info("Shutdown the engine at " + Calendar.getInstance().getTime());

		if(!AtmEngineStatus.STARTING.equals(this.engine.getStatus())
			&& !AtmEngineStatus.RUNNING.equals(this.engine.getStatus()))
		    log.info("Failed to Shutdown engine since its status is : " + this.engine.getStatus());

		// shutdown the engine
		this.engine.shutdownNow();

	    }

	}
	catch(Exception exp)
	{
	    log.error(exp, "Failed to Shutdown engine");
	    throw new BaseException(exp);
	}
    }

    /**
     * Return Engine status
     */
    public HashMap<String, Object> returnEngineStatus(HashMap<String, Object> inputMap) throws BaseException
    {
	try
	{
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    synchronized(engine.getMonitor())
	    {
		map.put("status", engine.getStatus().getCode());
		return map;
	    }

	}
	catch(Exception exp)
	{
	    log.error(exp, "Failed to Return Engine status");
	    throw new BaseException(exp);
	}
    }

    /**
     * Get Atm Engine DAO
     * 
     * @return
     */
    public AtmEngineDAO getAtmEngineDAO()
    {
	return atmEngineDAO;
    }

    /**
     * Set Atm engine DAO
     * 
     * @param atmEngineDAO
     */
    public void setAtmEngineDAO(AtmEngineDAO atmEngineDAO)
    {
	this.atmEngineDAO = atmEngineDAO;
    }

}
