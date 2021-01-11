package com.path.atm.engine.connector.pipeline.handler;

import com.path.atm.engine.iso8583.MessageFactory;
import com.path.lib.log.Log;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * This class will handle any exception that occur in pipeline
 * 
 * <p>
 * When an exception occur pipeline exception handler will check the type of the
 * exception and act accordingly. Several type of exception can occur during
 * request handling and while moving from one handler to another such as
 * CorruptedFrameException, IsoParseException ... Each one of those exception
 * will be handled differently, some will be ignored and no technical response
 * will be sent.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class PipelineExceptionHandler extends ChannelDuplexHandler
{

    /**
     * Logger
     */
    private Log logger = Log.getInstance();

    /**
     * Message Factory
     */
    private final MessageFactory messageFactory;

    /**
     * Construct InboundExceptionHandler
     */
    public PipelineExceptionHandler(MessageFactory messageFactory)
    {
	this.messageFactory = messageFactory;
    }

    /**
     * Handle Frame decoding exception.
     * <p>
     * Exception thrown at this level will be logged in file since the engine
     * couldn't assign a request id to the message.
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
	/**
	 * Check if this exception can be handled by this handler else throw it
	 * to the next handler We are only handle the encoding exception that
	 * could occur while sending a request Since response exception are
	 * handled inside the iso message Handler
	 */
//	if(shouldHandleException(cause))
//	{
//	    handleIsoOutboundException(ctx, cause);
//	    logger.debug("[PipelineExceptionHandler] : exceptionCaught");
//
//	    return;
//	}

	// we should log the exception at this level and not propagate it to the
	// next handler since netty will log it as unhandled exception
	// In case we needed to handle any exception at server handler we can
	// filter
	// the exception here
	// super.exceptionCaught(ctx, cause);

	logger.error(cause, "[PipelineExceptionHandler] : exceptionCaught while sending data");
	super.exceptionCaught(ctx, cause);
    }
}
