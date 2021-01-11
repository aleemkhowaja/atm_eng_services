package com.path.atm.engine.container.amq.client;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Use Message Adapter This class hold the Message Async consumer behavior
 * 
 * @author Mohammad Ali Mezzawi
 */
abstract public class TaskMessageListener implements MessageListener {
	
	/**
	 * @note @todo : Should we send MapMessage or ObjectMessage
	 * Performance and Serialization should be kept in mind
	 */
	public void onMessage(Message message) {
		
//		try {
//			T task = (T) ((ObjectMessage)message).getObject();
//			
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		System.out.println("I received the messagex");
	}
}