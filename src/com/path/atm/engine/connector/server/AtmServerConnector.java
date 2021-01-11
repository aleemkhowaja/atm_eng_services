package com.path.atm.engine.connector.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import com.path.atm.engine.connector.AbstractConnector;
import com.path.atm.engine.connector.pipeline.handler.Iso8583ChannelInitializer;
import com.path.atm.engine.connector.pipeline.handler.Iso8583ServerChannelInitializer;
import com.path.atm.engine.connector.util.AtmConnectorConstants;
import com.path.atm.engine.core.AtmEngineReactor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Represent a netty server connector with all ATM needed configurations.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmServerConnector extends AbstractConnector<ServerBootstrap, AtmServerConnectorConfig>
{

    /**
     * handles the traffic of the accepted connection once the boss accepts the
     * connection and registers the accepted connection to the worker
     */
    private EventLoopGroup workerEventLoopGroup;

    /**
     * Reference to the created channel
     */
    private final AtomicReference<Channel> connectionChannel = new AtomicReference<>();

    /**
     * @param serverConfig
     * @param reactor
     */
    public AtmServerConnector(AtmEngineReactor reactor, AtmServerConnectorConfig serverConfig)
    {
	super(reactor, serverConfig);
    }

    /**
     * Create a server bootstrap 1- Set Boss and worker group event loop 2- Set
     * needed channel pipeline
     */
    protected ServerBootstrap createBootstrap()
    {

	final ServerBootstrap bootstrap = new ServerBootstrap();

	/**
	 * - Set boss and worker group - Class to instantiate Channel - set
	 * ChildHandler
	 */
	bootstrap.group(getBossEventLoopGroup(), getWorkerEventLoopGroup()).channel(NioServerSocketChannel.class)
		.localAddress(getSocketAddress()).childHandler(initChannelInitialize());

	/**
	 * @todo 1- do we need to hook configuration 2- set more network layer
	 *       options
	 */
	configureBootstrap(bootstrap);
	bootstrap.validate();

	return bootstrap;
    }

    /**
     * Initialize the main Connected components
     */
    protected void init()
    {
	// @todo fix the number of thread inside the worker event loop group
	workerEventLoopGroup = createWorkerEventLoopGroup();
	super.init();
    }

    /**
     * Initialize the channel initialized
     * 
     * 1- create Iso8583ChannelInitialize 2- Set configuration/worker group 3-
     * Add custom channel handlers
     * 
     * @return {@link Iso8583ChannelInitializer}
     */
    protected Iso8583ChannelInitializer<Channel, AtmServerConnector> initChannelInitialize()
    {

	Iso8583ServerChannelInitializer channelInitializer = new Iso8583ServerChannelInitializer(this);

	channelInitializer.setWorkerGroup(getWorkerEventLoopGroup());

	if(null != getChannelHandlers())
	{
	    ChannelHandler[] handlers = (ChannelHandler[]) getChannelHandlers().toArray();
	    channelInitializer.setCustomChannelHandlers(handlers);
	}

	return channelInitializer;

    }

    /**
     * This method should be overridden in child
     * 
     * @param bootstrap
     */
    protected void configureBootstrap(ServerBootstrap bootstrap)
    {
	super.configureBootstrap(bootstrap);

	// @todo add this as property
	final boolean tcpNoDelay = Boolean.parseBoolean(System.getProperty("nfs.rpc.tcp.nodelay", "true"));

	bootstrap.childOption(ChannelOption.TCP_NODELAY, tcpNoDelay);
	bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

	// override whatever we want
	bootstrap.childAttr(AtmConnectorConstants.CONNECTOR_KEY, this);
	bootstrap.childAttr(AtmConnectorConstants.REACTOR_KEY, this.reactor);
    }

    /**
     * Check if the server connector is started
     * 
     * @return
     */
    public boolean isStarted()
    {
	final Channel channel = getConnectionChannel();
	return channel != null && channel.isOpen();
    }

    /**
     * Start the server
     * 
     * @throws InterruptedException
     */
    public void start() throws InterruptedException
    {

	// init the connector
	super.start();
	// @todo to check it later

	// Bind and start to accept incoming connections.
	ChannelFuture channelFuture = getBootstrap().bind().sync();

	// keep reference of the channel created
	setConnectionChannel(channelFuture.channel());

	// logger.info("Server is started and listening at {}",
	// channel.localAddress());
	channelFuture.await();
    }

    /**
     * Shutdown the server SCRAM/Safe operation
     * 
     * @throws Exception
     */
    public void shutdown()
    {

	// close the channe
	closeChannel();

	/**
	 * @todo @note : should we wait the future to close ?? Future<?> future
	 *       = group.shutdownGracefully(); // block until the group has
	 *       shutdown future.syncUninterruptibly(); or
	 *       group.shutdownGracefully().sync();???
	 */
	if(workerEventLoopGroup != null)
	{
	    workerEventLoopGroup.shutdownGracefully();
	    workerEventLoopGroup = null;
	}

	super.shutdown();
    }

    /**
     * Create Worker event loop group
     * 
     * @return
     */
    protected EventLoopGroup createWorkerEventLoopGroup()
    {
	return new NioEventLoopGroup(getConfiguration().getWorkerThreadsCount());
    }

    /**
     * Close the channel created by the server SCRAM/Safe operation
     */
    protected void closeChannel()
    {

	final Channel channel = getConnectionChannel();

	if(!isStarted())
	    // throw new IllegalStateException("Server is not started.");
	    return;

	// logger.info("Stopping the Server...");
	try
	{

	    // deregister the channel from the executor
	    channel.deregister();

	    // @todo revisit
	    channel.close().sync().await(10, TimeUnit.SECONDS);
	    // logger.info("Server was Stopped.");
	}
	catch(InterruptedException e)
	{
	    // logger.error("Error while stopping the server", e);
	}

    }

    /**
     * Returns a string representation of the object.
     */
    public String toString()
    {
	return "AtmServerConnector";
    }

    /**
     * @return the connectionChannel
     */
    public Channel getConnectionChannel()
    {
	return connectionChannel.get();
    }

    /**
     * set the connectionChannel created by the Server connector
     * 
     * @return
     */
    public void setConnectionChannel(Channel channel)
    {
	this.connectionChannel.set(channel);
    }

    /**
     * @return the reactor
     */
    public AtmEngineReactor getReactor()
    {
	return reactor;
    }

    /**
     * @return the workerEventLoopGroup
     */
    public EventLoopGroup getWorkerEventLoopGroup()
    {
	return workerEventLoopGroup;
    }

    /**
     * @param workerEventLoopGroup the workerEventLoopGroup to set
     */
    public void setWorkerEventLoopGroup(EventLoopGroup workerEventLoopGroup)
    {
	this.workerEventLoopGroup = workerEventLoopGroup;
    }
}
