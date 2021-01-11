package com.path.atm.engine.core;

import java.util.concurrent.atomic.AtomicBoolean;

import com.path.atm.engine.util.AtmCommonUtil;
import com.path.lib.log.Log;

/**
 * This Class will start the engine asynchronously.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngineStarter
{

    /**
     * Atm engine logger Singleton instance
     */
    private volatile static AtmEngineStarter _instance;

    /**
     * Monitor object
     */
    private static Object monitor = new Object();

    /**
     * flag that determine if the engine is running.
     */
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * Hold reference to the log
     */
    protected final static Log log = Log.getInstance();

    /**
     * AtmEngine Reference
     */
    private volatile AtmEngine atmEngine;

    /**
     * Return Atm Engine Logger instance
     */
    public static AtmEngineStarter getInstance()
    {
	if(_instance == null)
	{
	    synchronized(monitor)
	    {
		if(_instance == null)
		    _instance = new AtmEngineStarter();
	    }
	}

	return _instance;
    }

    /**
     * Kickoff the Atm Engine Starter
     */
    public void kickOff()
    {

	log.info("[AtmEngineStarter] Starting Thread");

	if(isRunning.get())
	{
	    log.error("Thread already running");
	    throw new IllegalStateException("Thread already running");
	}

	// kick off thread
	getNewWorkerThread().start();
    }

    /**
     * Create a new thread
     * 
     * @return
     */
    private Thread getNewWorkerThread()
    {
	Thread kickoffThread = new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {

		log.info("[AtmEngineStarter] Starting Thread");

		try
		{

		    isRunning.set(true);

		    // start the engine
		    atmEngine.startUp();

		}
		catch(Throwable e)
		{

		    atmEngine.releaseLock();

		    // we should show the first engine issue
		    log.error(e, "Faild to start the Engine due :".concat(AtmCommonUtil.getEngExpCauseMsg(e)));

		}
		finally
		{
		    isRunning.set(false);
		}
	    }
	});

	return kickoffThread;
    }

    /**
     * @return the atmEngine
     */
    public AtmEngine getAtmEngine()
    {
	return atmEngine;
    }

    /**
     * @param atmEngine the atmEngine to set
     */
    public AtmEngineStarter setAtmEngine(AtmEngine atmEngine)
    {
	this.atmEngine = atmEngine;
	return this;
    }

    /**
     * @return
     */
    public boolean isRunning()
    {
	return isRunning.get();
    }
}
