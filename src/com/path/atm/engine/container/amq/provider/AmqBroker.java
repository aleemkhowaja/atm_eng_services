package com.path.atm.engine.container.amq.provider;

import java.io.File;

import javax.jms.Connection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;

import com.path.lib.common.util.PathFileSecure;

/**
 * This class is the house of AMQ Broker behavior
 * 
 * @author Mohammad Ali Mezzawi
 *
 */
public class AmqBroker implements JmsBroker {

	/**
	 * @todo keep in mind that this instance maybe will not be populated in cluster
	 *       env
	 */
	private BrokerService broker;

	/**
	 * Hold the current broker url
	 */
	private String connectorUrl;

	/**
	 * failover flag, If enabled the engine will start in Master/slave topology
	 */
	private boolean failover;

	/**
	 * sharedFileSystem hold the path of the shared file system
	 */
	private String sharedFileSystemPath;

	/**
	 * Flag if the message should be persistent
	 */
	private boolean persistent;

	/**
	 * Flag to determine if we should use jmx
	 */
	private boolean useJmx;

	/**
	 * This method will start the amq broker
	 * @throws Exception 
	 */
	public void start() throws Exception{

		/**
		 * @note in case we will start this broker directly we may need to sync start
		 *       and stop ( between nodes, clusters)
		 * @note : above not isn't valid anymore, will be removed on code cleanup
		 */
		if (isBrokerAlive()) {
			return;
		}

		broker = new BrokerService();
		// @todo check if this the best practice
		broker.addConnector(getConnectorUrl());

		// @todo check this
		broker.setAdvisorySupport(false);

		// @todo issue occur when true
		broker.setPersistent(isPersistent());
		broker.setUseJmx(isUseJmx());

		// broker.getPersistenceAdapter();
		// set configuration here

		if (isFailover()) {

			// prepare the KahaDB
			File dataFileDir = new PathFileSecure(sharedFileSystemPath);
			KahaDBPersistenceAdapter kahaStore = new KahaDBPersistenceAdapter();
			kahaStore.setDirectory(dataFileDir);
			broker.setPersistenceAdapter(kahaStore);

			// ????
			broker.setPersistent(false);
			asynchronizeStart(broker);
			return;
		}

		// start the broker
		broker.start();
	}

	/**
	 * This method will shutdown the broker
	 * @todo change the log here
	 */
	public void shutdown() throws Exception {

		try {
			if (broker != null)
				broker.stop();

		} catch (Exception e) {
			// @todo add log here 
			
		}
	}

	/**
	 * Ping the local broker to check if connection still alive
	 * 
	 * @param brokerURL
	 * @return
	 * @throws Exception
	 */
	public boolean isBrokerAlive(){
		return isBrokerAlive(getConnectorUrl());
	}

	/**
	 * Ping broker to check if connection still alive
	 * 
	 * @param brokerURL
	 * @return
	 */
	public boolean isBrokerAlive(String brokerUrl) {

		Connection connection = null;
		ActiveMQConnectionFactory factory = null;
		boolean isAlive = false;

		try {
			factory = new ActiveMQConnectionFactory(brokerUrl);
			connection = factory.createConnection();
			connection.close();
			isAlive = true;
		} catch (/* @todo should we use JMSException ? */ Exception ex) {
			isAlive = false;
		} finally {
			connection = null;
			factory = null;
		}

		return isAlive;
	}

	/**
	 * Start the broker in asynchronize mode
	 * 
	 * @param broker
	 */
	private void asynchronizeStart(BrokerService broker) {

		Thread t = new Thread() {
			public void run() {
				try {
					broker.start();
				}catch (Exception e) {
					// @todo log exception
				}
			}
		};

		t.start();
	}

	/**
	 * @return
	 */
	public String getConnectorUrl() {
		return connectorUrl;
	}

	/**
	 * @param connectorUrl
	 */
	public void setConnectorUrl(String connectorUrl) {
		this.connectorUrl = connectorUrl;
	}

	public boolean isFailover() {
		return failover;
	}

	/**
	 * @param failover
	 */
	public void setFailover(boolean failover) {
		this.failover = failover;
	}

	public String getSharedFileSystemPath() {
		return sharedFileSystemPath;
	}

	public void setSharedFileSystemPath(String sharedFileSystemPath) {
		this.sharedFileSystemPath = sharedFileSystemPath;
	}

	/**
	 * @return the persistent
	 */
	public boolean isPersistent() {
		return persistent;
	}

	/**
	 * @param persistent the persistent to set
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**
	 * @return the useJmx
	 */
	public boolean isUseJmx() {
		return useJmx;
	}

	/**
	 * @param useJmx the useJmx to set
	 */
	public void setUseJmx(boolean useJmx) {
		this.useJmx = useJmx;
	}

	/**
	 * Task container Builder Class.
	 */
	public static class AmqBrokerBuilder {
		/**
		 * Hold the current broker url
		 */
		private String connectorUrl;

		/**
		 * failover flag, If enabled the engine will start in Master/slave topology
		 */
		private boolean failover;

		/**
		 * sharedFileSystem hold the path of the shared file system
		 */
		private String sharedFileSystemPath;

		/**
		 * Flag if the message should be persistent
		 */
		private boolean persistent;

		/**
		 * Flag to determine if we should use jmx
		 */
		private boolean useJmx;

		/**
		 * Build an AmqBroker
		 * 
		 * @return
		 */
		public AmqBroker build() {

			/**
			 * Build the broker
			 */
			AmqBroker amqBroker = new AmqBroker();
			amqBroker.setConnectorUrl(buildBrokerUrl());
			amqBroker.setFailover(isFailover());
			amqBroker.setPersistent(isPersistent());
			amqBroker.setSharedFileSystemPath(getSharedFileSystemPath());
			amqBroker.setUseJmx(isUseJmx());

			return amqBroker;
		}

		/**
		 * Build the broker url using given Broker Urls List and broker transport
		 * protocol
		 * 
		 * @return
		 * @return
		 * @throws Exception
		 * @todo fix this
		 */
		private String buildBrokerUrl() {
			return "tcp://" + getConnectorUrl();
		}

		/**
		 * @return the connectorUrl
		 */
		public String getConnectorUrl() {
			return connectorUrl;
		}

		/**
		 * @param connectorUrl the connectorUrl to set
		 * @return
		 */
		public AmqBrokerBuilder setConnectorUrl(String connectorUrl) {
			this.connectorUrl = connectorUrl;
			return this;
		}

		/**
		 * @return the failover
		 */
		public boolean isFailover() {
			return failover;
		}

		/**
		 * @param failover the failover to set
		 * @return
		 */
		public AmqBrokerBuilder setFailover(boolean failover) {
			this.failover = failover;
			return this;
		}

		/**
		 * @return the sharedFileSystemPath
		 */
		public String getSharedFileSystemPath() {
			return sharedFileSystemPath;
		}

		/**
		 * @param sharedFileSystemPath the sharedFileSystemPath to set
		 * @return
		 */
		public AmqBrokerBuilder setSharedFileSystemPath(String sharedFileSystemPath) {
			this.sharedFileSystemPath = sharedFileSystemPath;
			return this;
		}

		/**
		 * @return the persistent
		 */
		public boolean isPersistent() {
			return persistent;
		}

		/**
		 * @param persistent the persistent to set
		 * @return
		 */
		public AmqBrokerBuilder setPersistent(boolean persistent) {
			this.persistent = persistent;
			return this;
		}

		/**
		 * @return the useJmx
		 */
		public boolean isUseJmx() {
			return useJmx;
		}

		/**
		 * @param useJmx the useJmx to set
		 * @return
		 */
		public AmqBrokerBuilder setUseJmx(boolean useJmx) {
			this.useJmx = useJmx;
			return this;
		}

	}
}
