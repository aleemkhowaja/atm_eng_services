package com.path.atm.bo.engine;

import java.util.HashMap;

import com.path.lib.common.exception.BaseException;

/**
 * This BO will control the atm engine actions
 * @author Mohammad Ali Mezzawi
 */
public interface AtmEngineBO {
	
	/**
	 * Start the engine
	 * @throws BaseException
	 */
	public void start() throws BaseException;
	
	/**
	 * Shutdown the engine
	 * @throws BaseException 
	 */
	public void shutdown() throws BaseException;
	
	/**
	 * 
	 * @return Hashmap of status
	 * @throws BaseException
	 */
	public HashMap<String,Object> returnEngineStatus(HashMap<String,Object> inputMap) throws BaseException;
	
	/**
	 * Start a specific reactor without impacting other components
	 * @param interfaceCO
	 * @throws BaseException
	 */
	public void startReactor(HashMap<String,Object> interfaceCO) throws BaseException;
	
	/**
	 * Shutdown a specific reactor without impacting other reactors
	 * @param interfaceCO
	 * @throws BaseException
	 */
	public void shutdownReactor(HashMap<String,Object> interfaceCO) throws BaseException;
	
	/**
	 * Execute Iso Message
	 * @param isoMsgDetails
	 * @return
	 * @throws BaseException
	 */
	public HashMap<String,Object> executeIsoMsg(HashMap<String,Object> isoMsgDetails) throws BaseException;
	
	/**
	 * Parse Iso Message
	 * @param isoMsgDetails
	 * @return
	 * @throws BaseException
	 */
	public HashMap<String,Object> parseIsoMsg(HashMap<String,Object> isoMsgDetails) throws BaseException;
}
