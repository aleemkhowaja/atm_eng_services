package com.path.atm.engine.connector.pipeline.listener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import com.path.atm.engine.connector.client.AtmClientConnector;
import com.path.lib.log.Log;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReconnectOnCloseListener implements ChannelFutureListener
{
    /**
     * Hold reference to the log
     */
    private final static Log logger = Log.getInstance();

    private final AtmClientConnector connector;
    private int nbOfEssay = 0;
    private final int reconnectInterval;
    private final AtomicBoolean disconnectRequested = new AtomicBoolean(false);
    private final ScheduledExecutorService executorService;

    public ReconnectOnCloseListener(AtmClientConnector connector, int reconnectInterval,
	    ScheduledExecutorService executorService)
    {
	this.connector = connector;
	this.reconnectInterval = reconnectInterval;
	this.executorService = executorService;
    }

    @Override
    public void operationComplete(ChannelFuture future)
    {
	// @todo add log here
	final Channel channel = future.channel();
	logger.debug(String.format("Client connection was closed to %s", channel.remoteAddress()));
	channel.disconnect();
	scheduleReconnect();
    }

    /**
     * scheduleReconnect
     */
    public void scheduleReconnect()
    {
	if(disconnectRequested.get())
	{
	    return;
	}

	// we should halt the engine at this level
	if(nbOfEssay > 3)
	{
	    // we should stop the engine at this level
	   connector.getReactor().halt();
	    return;
	}

	logger.debug(String.format("Failed to connect. Will try again in %s millis", reconnectInterval));
	executorService.schedule(() -> connector.connectAsync(), reconnectInterval, TimeUnit.MILLISECONDS);
    }

    public void resetCounter()
    {
	nbOfEssay = 0;
    }

    /**
     * Update disconnectRequested flag, set it to false
     */
    public void requestReconnect()
    {
	nbOfEssay++;
	disconnectRequested.set(false);
    }

    /**
     * Update disconnectRequested flag, set it to true
     */
    public void requestDisconnect()
    {
	disconnectRequested.set(true);
    }
}