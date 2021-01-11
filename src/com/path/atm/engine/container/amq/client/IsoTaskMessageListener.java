package com.path.atm.engine.container.amq.client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.handler.IsoMessageHandler;
import com.path.atm.engine.pool.tasks.IsoTask;
import com.path.atm.engine.util.EngineError;
import com.path.atm.engine.util.SimpleChronometer;
import com.path.lib.log.Log;

/**
 * This class hold the message listener
 * @author MohammadAliMezzawi
 *
 *@todo : 
 * 1- fix the Message Redelivery and DLQ Handling
 */
public class IsoTaskMessageListener extends TaskMessageListener{
	
	/**
	 * Hold reference to the log
	 */
	private final static Log log = Log.getInstance();
	
	/**
	 * Hold reference to the message factory
	 */
	private final AtmEngineReactor reactor;
	
	
	/**
	 * @param amqIsoTaskContainer
	 * @param simpleMessageFactory 
	 */
	public IsoTaskMessageListener(AtmEngineReactor reactor) {
		this.reactor = reactor;
	}
	
	/**
	 * @note @todo : Should we send MapMessage or ObjectMessage
	 * Performance and Serialization should be kept in mind
	 */
	public void onMessage(Message message) {
		
		log.debug("[IsoTaskMessageListener] onMessage");
		//SimpleChronometer chrono = new SimpleChronometer();
		
		try {
			/**
			 * All logic should be centralize inside the iso Message handler
			 * to assure that message handling will work the same for 
			 * stand alone mode and normal mode
			 */
			IsoTask task = (IsoTask) ((ObjectMessage)message).getObject();
			
			// handle the message
			IsoMessageHandler messageHandler = new IsoMessageHandler(task,reactor);
			messageHandler.handleMessage(true);
			
			//log.debug("[IsoTaskMessageListener] Handle ISO Task took: "+chrono.elapsed());
			
		} catch (JMSException e ) {
			// @todo log a failed response
			e.printStackTrace();
		}
	}
}
