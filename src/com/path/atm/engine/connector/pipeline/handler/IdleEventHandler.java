package com.path.atm.engine.connector.pipeline.handler;

import com.path.atm.engine.connector.util.AtmConnectorConstants;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.MessageFactory;
import com.path.lib.log.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;

/**
 * This class house all the behavior and actions related to the idle event
 * 
 * <p>
 * Handling idle event is based on the reactor type ( server / client ). Server
 * : when acting as sever the reactor will send an echo message over the channel
 * and in case the response wasn't delivered, the server will close the channel
 * and assumed that the switch is down.
 * 
 * Client : .....
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IdleEventHandler extends ChannelInboundHandlerAdapter
{

    /**
     * Logger
     */
    private static final Log logger = Log.getInstance();

    /**
     * Message Factory
     */
    private final MessageFactory messageFactory;

    /**
     * @param messageFactory
     */
    public IdleEventHandler(MessageFactory messageFactory)
    {
	this.messageFactory = messageFactory;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {

	logger.debug("[IdleEventHandler] channelRead");
	super.channelRead(ctx, msg);
    }

    @Override
    /**
     * @todo exception thrown at this level should be logged
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws IsoMessageException
    {
	try
	{
	    if(!(evt instanceof IdleStateEvent))
		return;

	    IdleStateEvent e = (IdleStateEvent) evt;
	    if(e.state() == IdleState.READER_IDLE || e.state() == IdleState.ALL_IDLE)
	    {
		// send a log echo message
		final AtmIsoMessage echoMessage = messageFactory.createReqDraftMessage().asEcho();

		Attribute<AtmEngineReactor> attr = ctx.channel().attr(AtmConnectorConstants.REACTOR_KEY);
		AtmEngineReactor reactor = attr.get();

		// this part should be wrapped by try and catch and throw an
		// exception to close the connection
		reactor.getConnector().sendAsync(echoMessage);
	    }
	}
	catch(Exception exp)
	{
	    // @todo log this in the action
	    // This handling should be part of the pipeline exception
	    logger.error(exp, "[IdleEventHandler] Failed to send echo message");
	}

    }

    /**
     * Handle Parse exception thrown during handler invocation
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
	logger.debug("[IdleEventHandler] : exceptionCaught");
	super.exceptionCaught(ctx, cause);
    }
}
