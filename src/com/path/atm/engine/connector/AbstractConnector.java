package com.path.atm.engine.connector;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.path.atm.engine.connector.configuration.AtmConnectorConfiguration;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.EngineError;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;
import com.solab.iso8583.IsoMessage;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public abstract class AbstractConnector<BootStrap extends AbstractBootstrap, Config extends AtmConnectorConfiguration>
	implements Connector
{
    /**
     * Hold reference to the log
     */
    protected final static Log log = Log.getInstance();

    /**
     * Hold reference to the reactor
     */
    protected final AtmEngineReactor reactor;

    /**
     * <p>
     * A Bootstrap is a helper class that sets up a server or client We have two
     * types of bootstrap : 1- Bootstrap for client 2- ServerBootstrap for
     * server
     * 
     * A Bootstrap is similar to ServerBootstrap except that it's for non-server
     * channels such as a client-side or connectionless channel.
     */
    private BootStrap bootstrap;

    /**
     * Connector Socket address For Server it will be the port that it listen to
     * For Client it will be the port that connect to
     */
    private SocketAddress socketAddress;

    /**
     * A multithreaded event loop that handles I/O operation. This event loop
     * will accepts an incoming connection.
     */
    private EventLoopGroup bossEventLoopGroup;

    /**
     * Reference to the created channel
     */
    private final AtomicReference<Channel> channelRef = new AtomicReference<>();

    /**
     * hold configurations
     */
    private final Config configuration;

    /**
     * Hold Channel Handler
     */
    private ArrayList<ChannelHandler> channelHandlers;

    /**
     * Construct
     * @param messageFactory
     * @param reactor
     * 
     * @param messageFactory
     */
    protected AbstractConnector(AtmEngineReactor reactor, Config configuration)
    {
	this.configuration = configuration;
	this.reactor = reactor;
    }

    /**
     * Making connector ready to create a connection / bind to port. Creates a
     * Bootstrap
     */
    protected void init()
    {

	validateConfiguration();

	// set socket address
	setSocketAddress(new InetSocketAddress(configuration.getHost(), configuration.getPort()));

	bossEventLoopGroup = createBossEventLoopGroup();
	setBootstrap(createBootstrap());
    }

    /**
     * Validate connector configuration before starting it
     */
    protected void validateConfiguration()
    {

	// validate configuration
	if(null == configuration)
	    throw new IllegalConfigurationException(EngineError.CON_CONF_NOTSET);

	// validate socket Address
	if(StringUtil.nullToEmpty(configuration.getHost()).trim().isEmpty()
		|| StringUtil.nullToEmpty(configuration.getPort()).trim().isEmpty())
	    throw new IllegalConfigurationException(EngineError.CON_CONF_HOST_NOTSET);

	// validate the port value
	if(configuration.getPort() < 0 || configuration.getPort() > 0xFFFF)
	    throw new IllegalConfigurationException(EngineError.CON_CONF_PORT_OUTRANGE);
    }

    /**
     * Sends asynchronously and returns a {@link ChannelFuture}
     *
     * @param isoMessage A message to send
     * @return ChannelFuture which will be notified when message is sent
     */
    public ChannelFuture sendAsync(IsoMessage isoMessage)
    {
	return sendAsync(getChannel(), isoMessage);
    }

    /**
     * Sends asynchronously and returns a {@link ChannelFuture}
     *
     * @param channel
     * @param isoMessage A message to send
     * @return ChannelFuture which will be notified when message is sent
     */
    public ChannelFuture sendAsync(Channel channel, IsoMessage isoMessage)
    {
	if(channel == null)
	    throw new IllegalStateException("Channel is not opened");
	
	if(!channel.isWritable()) {
	   
	    //Check if channel isn't writable due to full memory
	    NioSocketChannel nioChannel = (NioSocketChannel)channel;
	    
	    StringBuilder sb = new StringBuilder();
	    sb.append("Channel is not writable")
	    	.append("\n Send buffer size : ").append(nioChannel.config().getSendBufferSize())
	    	.append("\n Receive buffer size : ").append(nioChannel.config().getReceiveBufferSize())
	    	.append("\n Bytes before unwritable : ").append(nioChannel.bytesBeforeUnwritable())
	    	.append("\n Bytes before writable : ").append(nioChannel.bytesBeforeWritable())
	    	.append("\n LowWaterMark : ").append(nioChannel.config().getWriteBufferLowWaterMark())
	    	.append("\n HighWaterMark : ").append(nioChannel.config().getWriteBufferHighWaterMark());
	    
	    log.error(sb.toString());
	    throw new IllegalStateException("Channel is not writable");
	}
	
	return channel.writeAndFlush(isoMessage);
    }

    /**
     * Send Synchronous Iso message
     * 
     * @param isoMessage
     * @throws InterruptedException
     */
    public void send(IsoMessage isoMessage) throws InterruptedException
    {
	sendAsync(getChannel(), isoMessage).sync().await();
    }

    /**
     * Send Synchronous Iso message
     * 
     * @param isoMessage
     * @throws InterruptedException
     */
    public void send(Channel channel, IsoMessage isoMessage) throws InterruptedException
    {
	sendAsync(channel, isoMessage).sync().await();
    }

    /**
     * Send Synchronous Iso message with timeout
     * 
     * @param channel
     * @param isoMessage
     * @param timeout
     * @param timeUnit
     * @throws InterruptedException
     */
    public void send(Channel channel, IsoMessage isoMessage, long timeout, TimeUnit timeUnit)
	    throws InterruptedException
    {

	sendAsync(channel, isoMessage).sync().await(timeout, timeUnit);
    }

    /**
     * Start the connector by turning on 1- workerEventLoopGroup 2-
     * bossEventLoopGroup
     */
    public void start() throws InterruptedException
    {

	/**
	 * initialize the connector Components Initialize the components is done
	 * while starting the Connector for two purpose : 1- Components should
	 * be populate and instantiated if connecter is started ( which is logic
	 * ) 2- Allow Shutdown gracefully to shutdown components even the server
	 * isn't started in case we have any clearance logic to be implemented
	 */
	init();
    }

    /**
     * Shutdown the connector by turning off 1 - workerEventLoopGroup 2 -
     * bossEventLoopGroup
     */
    public void shutdown()
    {

	/**
	 * @todo @note : should we wait the future to close ?? Future<?> future
	 *       = group.shutdownGracefully(); // block until the group has
	 *       shutdown future.syncUninterruptibly(); or
	 *       group.shutdownGracefully().sync();???
	 */
	if(bossEventLoopGroup != null)
	{
	    bossEventLoopGroup.shutdownGracefully();
	    bossEventLoopGroup = null;
	}
    }

    /**
     * Create bootstrap which will help us to create a connector
     * 
     * @return
     */
    protected abstract BootStrap createBootstrap();

    /**
     * Initialize the channel initialized
     * 
     * @return {@link ChannelInitializer}
     */
    abstract protected ChannelInitializer<Channel> initChannelInitialize();

    /**
     * This method should be overridden in child
     * 
     * @param bootstrap
     */
    protected void configureBootstrap(BootStrap bootstrap)
    {
	bootstrap.option(ChannelOption.AUTO_READ, true);
    }

    /**
     * Create Boss event loop group
     * 
     * @return
     */
    private EventLoopGroup createBossEventLoopGroup()
    {
	return new NioEventLoopGroup();
    }

    /**
     * @return the reactor
     */
    public AtmEngineReactor getReactor()
    {
	return reactor;
    }

    /**
     * @return the bootstrap
     */
    public BootStrap getBootstrap()
    {
	return bootstrap;
    }

    /**
     * @param bootstrap the bootstrap to set
     */
    public void setBootstrap(BootStrap bootstrap)
    {
	this.bootstrap = bootstrap;
    }

    /**
     * @return the socketAddress
     */
    public SocketAddress getSocketAddress()
    {
	return socketAddress;
    }

    /**
     * @param socketAddress the socketAddress to set
     */
    public void setSocketAddress(SocketAddress socketAddress)
    {
	this.socketAddress = socketAddress;
    }

    /**
     * @return the bossEventLoopGroup
     */
    public EventLoopGroup getBossEventLoopGroup()
    {
	return bossEventLoopGroup;
    }

    /**
     * @param bossEventLoopGroup the bossEventLoopGroup to set
     */
    public void setBossEventLoopGroup(EventLoopGroup bossEventLoopGroup)
    {
	this.bossEventLoopGroup = bossEventLoopGroup;
    }

    /**
     * Get the channel created by the connector either to receive message or
     * send message via it
     * 
     * @return
     */
    public Channel getChannel()
    {
	return channelRef.get();
    }

    /**
     * set the channel created by the connector either to receive message or
     * send message via it
     * 
     * @return
     */
    public void setChannel(Channel channel)
    {
	this.channelRef.set(channel);
    }

    /**
     * @return the configuration
     */
    public Config getConfiguration()
    {
	return configuration;
    }

    /**
     * @return the channelHandlers
     */
    public ArrayList<ChannelHandler> getChannelHandlers()
    {
	return channelHandlers;
    }

    /**
     * @param channelHandlers the channelHandlers to set
     */
    public void setChannelHandlers(ArrayList<ChannelHandler> channelHandlers)
    {
	this.channelHandlers = channelHandlers;
    }

    /**
     * This class hold the Server connector builder behavior
     */
    public static class ConnectorBuilder<ConnectorConfigBuilder>
    {

	/**
	 * Hold reference of connector configuration builder
	 */
	private ConnectorConfigBuilder config;

	/**
	 * @return the config
	 */
	public ConnectorConfigBuilder getConfig()
	{
	    return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(ConnectorConfigBuilder config)
	{
	    this.config = config;
	}
    }
}
