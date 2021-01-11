package com.path.atm.engine.connector.pipeline.handler;

import com.path.atm.engine.connector.client.AtmClientConnector;
import io.netty.channel.Channel;

/**
 * 
 * @author MohammadAliMezzawi
 *
 */
public class Iso8583ClientChannelInitializer extends Iso8583ChannelInitializer<Channel, AtmClientConnector>
{

    /**
     * Iso 8583 server channel initializer
     * 
     * @param configuration
     * @param messageFactory
     */
    public Iso8583ClientChannelInitializer(AtmClientConnector clientConnector)
    {
	super(clientConnector);
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
	//final ChannelPipeline pipeline = ch.pipeline();

	// Client handler
	// pipeline.addLast("anynewHandler",createAnyNewHandler());

	// @todo add log here
    }
}
