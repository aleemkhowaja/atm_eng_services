package com.path.atm.engine.connector.pipeline.handler;

import java.util.concurrent.TimeUnit;

import com.path.lib.log.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;

public class IsoIdleStateHandler extends IdleStateHandler
{

    /**
     * @param readerIdleTimeSeconds
     * @param writerIdleTimeSeconds
     * @param allIdleTimeSeconds
     */
    public IsoIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds)
    {

	super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds, TimeUnit.SECONDS);
    }

    /**
     * @param observeOutput
     * @param readerIdleTime
     * @param writerIdleTime
     * @param allIdleTime
     * @param unit
     */
    public IsoIdleStateHandler(boolean observeOutput, long readerIdleTime, long writerIdleTime, long allIdleTime,
	    TimeUnit unit)
    {
	super(observeOutput, readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    /**
     * Handle Parse exception thrown during handler invocation
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
	Log.getInstance().error("[IsoIdleStateHandler] : exceptionCaught");
	super.exceptionCaught(ctx, cause);
    }

}
