package com.path.atm.engine.connector.pipeline.handler;

import com.path.atm.engine.connector.server.AtmServerConnector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;

/**
 * House Iso 8583 server channel initializer
 * 
 * @author MohammadAliMezzawi
 *
 */
public class Iso8583ServerChannelInitializer extends Iso8583ChannelInitializer<Channel, AtmServerConnector>
{
    /**
     * Iso 8583 server channel initializer
     * 
     * @param configuration
     * @param messageFactory
     */
    public Iso8583ServerChannelInitializer(AtmServerConnector serverConnector)
    {
	super(serverConnector);
    }

    /**
     * Initialize the Channel
     * <p>
     * This method will be invoked when creating a new channel and will add the
     * given handlers to the channel pipeline
     */
    protected void initChannel(Channel ch) throws Exception
    {

	// init and add the common handlers
	super.initChannel(ch);

	// get the pipeline
	final ChannelPipeline pipeline = ch.pipeline();

	// ServerClient handler
	pipeline.addLast("isoServerClientHandler", createServerClientHandler((AtmServerConnector) getConnector()));

	// @todo add log here
    }

    /**
     * Create server client handler
     * 
     * @param serverConnector
     * @return
     */
    private ChannelHandler createServerClientHandler(AtmServerConnector serverConnector)
    {
	return new IsoServerClientHandler(serverConnector);
    }

}
