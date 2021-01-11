package com.path.atm.engine.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.path.atm.bo.engine.AtmEngineLogsBO;
import com.path.atm.engine.action.AtmActionType;
import com.path.atm.engine.action.AtmBaseAction;
import com.path.atm.engine.config.AtmEngineConfig;
import com.path.atm.engine.container.amq.provider.AmqIsoTaskContainer;
import com.path.atm.engine.container.amq.provider.AmqTaskContainerConfig;
import com.path.atm.engine.exception.EngineReactorShutdownException;
import com.path.atm.engine.exception.EngineShutdownException;
import com.path.atm.engine.exception.EngineStartupException;
import com.path.atm.engine.locks.SharedFileLock;
import com.path.atm.engine.util.AtmEngUtil;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.AtmLogger;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.engine.AtmInterfaceCO;
import com.path.dbmaps.vo.ATM_ENG_INTERFACEVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.log.Log;

/**
 * This class represent the Atm engine
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngine
{

    /**
     * Hold reference to the log
     */
    private final static Log log = Log.getInstance();

    /**
     * Hold reference to the log
     */
    private final static AtmLogger atmLog = AtmLogger.getInstance();

    /**
     * Hold list of reactors
     */
    private volatile HashMap<String, AtmEngineReactor> reactors = new HashMap<String, AtmEngineReactor>();

    /**
     * Reactor status
     */
    private volatile AtmEngineStatus status = AtmEngineStatus.DOWN;

    /**
     * Monitor object
     */
    private Object monitor = new Object();

    /**
     * Lock used for Master/Slave topology When full fixing the lock manager we
     * should use the manager lock
     */
    private SharedFileLock lock = new SharedFileLock(AtmEngineConstants.CLUSTER_LCK);

    /**
     * Hold atm engine configuration
     */
    private AtmEngineConfig configuration;

    /**
     * Hold reference to the task container
     */
    private AmqIsoTaskContainer taskContainer;

    /**
     * Start the Atm engine
     * 
     * @throws EngineStartupException
     */
    public void startUp() throws EngineStartupException
    {

	// acquire Lock
	acquireLock();

	// synch over monitor object
	synchronized(monitor)
	{

	    /**
	     * If engine isn't started we shouldn't force shutdown it. Scenario
	     * : 1- Engine is starting but didn't acquire the lock 2- A shutdown
	     * (undeploy) action occur, the status is changed to down 3- lock is
	     * acquired.
	     */
	    if(status != AtmEngineStatus.STARTING)
	    {

		// release the lock
		releaseLock();

		throw new EngineStartupException(EngineError.ENG_STARTUP_FAILED, "Engine isn't ready to kickoff ");
	    }

	    try
	    {
		log.info("[ATM Engine] startUp : Starting the Engine");

		// @todo move this up log engine start
		AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.ENG_STARTING);

		// load main engine configuration
		configuration = new AtmEngineConfig();
		configuration.loadFromProperties();

		// log Engine configuration
		log.info("[ATM Engine] startUp configuration :" + configuration.toString());

		// start engine reactor
		startTaskContainer();

		// reset interfaces
		markInterfaceAsDown();

		// set status as running
		status = AtmEngineStatus.RUNNING;

		// log action
		action.setReference(configuration).log();

		log.info("Engine started");
	    }
	    catch(Exception e)
	    {

		shutdownGracefully();
		throw new EngineStartupException(EngineError.ENG_STARTUP_FAILED, e);
	    }
	}

    }

    /**
     * SCRAM, Never check the Engine status. Always Try to shutdown all the
     * engine components.
     * 
     * Shutdown the Atm engine
     * 
     * @throws Exception
     * @throws IllegalStateException
     * @throws EngineReactorShutdownException
     */
    public void shutdownNow() throws Exception
    {

	// release the Sharedlock
	releaseLock();

	// log the action
	AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.ENG_SHUTINGDOWN);

	// synch over monitor object
	synchronized(monitor)
	{

	    // set status as shutting down
	    status = AtmEngineStatus.SHUTDOWN;

	    if(!shutdownTaskContainer() || !shutdownReactors())
		throw new EngineShutdownException(EngineError.ENG_SHUTDOWN_FAILED);

	    // set status as down
	    status = AtmEngineStatus.DOWN;

	    // log the action
	    action.log();
	}
    }

    /**
     * Turn on a specific reactor on
     * 
     * @param interfaceCO
     * @throws BaseException
     * @throws IllegalStateException
     */
    public void startReactor(AtmInterfaceCO interfaceCO) throws BaseException
    {

	log.debug("start reactor");

	if(null == interfaceCO || interfaceCO.getCode() == null)
	    throw new IllegalArgumentException("InterfaceCO can't be null");

	synchronized(monitor)
	{

	    // check if reactor already started.
	    String reactorId = AtmEngUtil.returnReactorKey(interfaceCO.getCode());

	    /**
	     * which mean that the current reactor is working. for
	     * sychronization purpose and due to the issue where two different
	     * node connected to the same db and not sharing the same lock we
	     * will update the db status to make sure it reflect the real case
	     */

	    AtmEngineReactor reactor = new AtmEngineReactor(interfaceCO, this);

	    if(null == reactors.get(reactorId))
	    {
		// start the reactor
		reactor.startUp();
		// set the reactor reference
		reactors.put(reactorId, reactor);
		log.debug("Reactor started");
		return;
	    }

	    ATM_ENG_INTERFACEVO engInterfaceVO = reactor.getEngInterfaceVO();
	    atmLog.insertReactorInstance(engInterfaceVO);
	    engInterfaceVO.setSTATUS(AtmEngReactorStatus.RUNNING.getCode());
	    engInterfaceVO.setSTART_TIME(new Date());
	    engInterfaceVO.setMESSAGE("");
	    // log the status in the DB
	    atmLog.updateReactorInfo(engInterfaceVO);
	    log.debug("Reactor is already started");
	}

    }

    /**
     * Turn off a specific reactor on
     * 
     * @param reactorIntId
     * @throws BaseException
     * @throws IllegalStateException
     */
    public void shutdownReactor(AtmInterfaceCO interfaceCO) throws BaseException
    {

	log.debug("shutdown Reactor");

	if(null == interfaceCO || interfaceCO.getCode() == null)
	    throw new IllegalArgumentException("InterfaceCO can't be null");

	synchronized(monitor)
	{

	    // get the reactor reference
	    String reactorId = AtmEngUtil.returnReactorKey(interfaceCO.getCode());

	    if(null != reactors.get(reactorId))
	    {
		AtmEngineReactor reactor = reactors.get(reactorId);
		reactor.shutdown();
		reactors.remove(reactorId);
		log.debug("Reactor is shutdowned");
		return;
	    }

	    // else we need to synchronize
	    ATM_ENG_INTERFACEVO engInterfaceVO = new ATM_ENG_INTERFACEVO();
	    engInterfaceVO.setINTERFACE_ID(interfaceCO.getCode());

	    // insert the engine interface details if not exist
	    atmLog.insertReactorInstance(engInterfaceVO);

	    engInterfaceVO.setSTATUS(AtmEngReactorStatus.DOWN.getCode());
	    engInterfaceVO.setSTART_TIME(new Date());
	    engInterfaceVO.setMESSAGE("");

	    // log the status in the DB
	    atmLog.updateReactorInfo(engInterfaceVO);
	    log.debug("Reactor is already shutdowned");
	}
    }

    /**
     * Return a specific reactor
     * 
     * @param interfaceCode
     * @return
     */
    public AtmEngineReactor returnReactor(BigDecimal interfaceCode)
    {

	// check if there is any reactor on
	if(null == reactors || reactors.isEmpty() || null == reactors.get(AtmEngUtil.returnReactorKey(interfaceCode)))
	    throw new IllegalStateException("Engine reactor".concat(String.valueOf(interfaceCode)).concat(" is off"));

	return reactors.get(AtmEngUtil.returnReactorKey(interfaceCode));
    }

    /**
     * Acquire Master lock
     * 
     * @throws EngineStartupException
     */
    public void acquireLock() throws EngineStartupException
    {
	try
	{
	    lock.lock();
	}
	catch(Exception e)
	{
	    throw new EngineStartupException(EngineError.ENG_STARTUP_FAILED, e);
	}
    }

    /**
     * Release the lock
     */
    public void releaseLock()
    {

	log.error("Release lock");
	lock.unlock();
    }

    /**
     * This method will mark all interfaces as down
     * 
     * @throws BaseException
     */
    private void markInterfaceAsDown() throws BaseException
    {

	// log the action start the task container
	AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.ENG_MRK_INT_DOWN);

	AtmEngineLogsBO atmEngLogsBO = (AtmEngineLogsBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngineLogsBO");

	ATM_ENG_INTERFACEVO engInterfaceVO = new ATM_ENG_INTERFACEVO();

	// mark interface as down
	engInterfaceVO.setSTATUS(AtmEngReactorStatus.DOWN.getCode());
	engInterfaceVO.setMACHINE_NAME_IP(null);

	// reset the following data
	engInterfaceVO.setMESSAGE("");
	engInterfaceVO.setISO_FIELDS_CONFIG("");

	// @todo fix this
	engInterfaceVO.setSTART_TIME(null);

	atmEngLogsBO.updateReactorStatus(engInterfaceVO);

	// log action
	action.log();
    }

    /**
     * Graceful shutdown the engine Mainly used on failed start up.
     */
    private void shutdownGracefully()
    {
	try
	{

	    // log the action
	    log.debug("shutdownGracefully the engine");

	    // shutdown the engine
	    shutdownNow();

	}
	catch(Exception exp)
	{

	    // set status as corrupted
	    status = AtmEngineStatus.CORRUPTED;
	    log.error(exp, "Failed to shutdown the engine gracefully status : Corrupted");
	}
    }

    /**
     * Start task container
     * 
     * @throws Exception
     */
    private void startTaskContainer() throws Exception
    {

	/**
	 * Task container will be kicked off at the reactor level in memory pool
	 * mode
	 */
	if(!AtmEngineConstants.TASK_POOL_MODE_MQ.equalsIgnoreCase(configuration.getPoolMode()))
	    return;

	// log the action start the task container
	AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.ENG_START_TSK_CONTAINER);

	// create amq task container
	AmqTaskContainerConfig config = new AmqTaskContainerConfig(AtmEngineConstants.PROP_FILE_NAME);
	config.loadFromProperties();
	taskContainer = new AmqIsoTaskContainer(config, this);
	taskContainer.start();

	// log the action
	action.log();
    }

    /**
     * This method will try to shutdown all reactors
     * 
     * @return
     */
    private boolean shutdownReactors()
    {

	boolean shutdownSuccess = true;

	log.info("shutdownReactors");

	try
	{

	    // log action
	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.ENG_SHUTDOWN_RECS);

	    Iterator<Entry<String, AtmEngineReactor>> hmIterator = reactors.entrySet().iterator();

	    while(hmIterator.hasNext())
	    {

		@SuppressWarnings("rawtypes")
		Map.Entry pair = (Map.Entry) hmIterator.next();
		AtmEngineReactor reactor = (AtmEngineReactor) pair.getValue();

		try
		{

		    // shutdown the reactor
		    shutdownReactor(reactor.getInterfaceCO());

		}
		catch(Exception e)
		{

		    // log the error and move on to the next reactor
		    log.error(e,
			    "Failed to stop the reactor".concat(reactor.getInterfaceCO().getCode().toPlainString()));
		    shutdownSuccess = false;
		}
	    }

	    action.log();

	}
	catch(Exception exp)
	{
	    log.error(exp, "Failed to shutdown some reactors");
	    shutdownSuccess = false;
	}

	return shutdownSuccess;
    }

    /**
     * This method will gracefully shutdown the task container
     * 
     * @return
     */
    private boolean shutdownTaskContainer()
    {

	// log engine action
	log.info("shutdownTaskContainer");

	boolean shutdownSuccess = true;

	try
	{
	    if(null != taskContainer)
	    {

		// start logging the action
		AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_SHUTDOWN_TSK_CNT);

		taskContainer.shutDownNow();
		taskContainer = null;

		// log the action
		action.log();
	    }

	}
	catch(Exception exp)
	{
	    log.error(exp, "Failed to shutdown Task container");
	    shutdownSuccess = false;
	}

	return shutdownSuccess;
    }

    /**
     * @return
     */
    public AtmEngineStatus getStatus()
    {
	return status;
    }

    /**
     * @param status
     */
    public void setStatus(AtmEngineStatus status)
    {
	this.status = status;
    }

    /**
     * @return the monitor
     */
    public Object getMonitor()
    {
	return monitor;
    }

    /**
     * @param monitor the monitor to set
     */
    public void setMonitor(Object monitor)
    {
	this.monitor = monitor;
    }

    /**
     * @return
     */
    public HashMap<String, AtmEngineReactor> getReactors()
    {
	return reactors;
    }

    /**
     * @param reactors
     */
    public void setReactors(HashMap<String, AtmEngineReactor> reactors)
    {
	this.reactors = reactors;
    }

    /**
     * @return the configuration
     */
    public AtmEngineConfig getConfiguration()
    {
	return configuration;
    }

    /**
     * @return the taskContainer
     */
    public AmqIsoTaskContainer getTaskContainer()
    {
	return taskContainer;
    }

    /**
     * @param taskContainer the taskContainer to set
     */
    public void setTaskContainer(AmqIsoTaskContainer taskContainer)
    {
	this.taskContainer = taskContainer;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(AtmEngineConfig configuration)
    {
	this.configuration = configuration;
    }
}
