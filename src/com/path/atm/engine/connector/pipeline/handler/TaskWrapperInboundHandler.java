package com.path.atm.engine.connector.pipeline.handler;

import com.path.atm.engine.connector.pipeline.exception.TaskWrapperInboundException;
import com.path.atm.engine.connector.pipeline.listener.TaskWrapperListener;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.util.EngineError;
import com.path.lib.log.Log;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author MohammadAliMezzawi
 *
 */
public class TaskWrapperInboundHandler extends IsoMessageInboundHandler<AtmIsoMessage>
{

    /**
     * Logger
     */
    private Log logger = Log.getInstance();

    /**
     * Default constructor
     */
    public TaskWrapperInboundHandler()
    {

	// always fail on error
	super(true);

	// Add task wrapper listener
	addListener(new TaskWrapperListener());

	// @note: more listener can be added at this level
	// iso.addListener(new EchoMessageListener());
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString()
    {
	return "TaskWrapperInboundHandler";
    }

    /**
     * Invoke attached listeners
     * 
     * @param ctx
     * @param msg
     */
    protected void doHandleMessage(ChannelHandlerContext ctx, Object msg)
    {

	try
	{

	    logger.debug("[TaskWrapperInboundHandler] start reading");
	    super.doHandleMessage(ctx, msg);

	}
	catch(Exception exp)
	{
	    /**
	     * Wrap all exceptions at this level with
	     * TaskWrapperInboundException so we can differentiate it while
	     * handling it at the {@link PipelineExceptionHandler}
	     */
	    throw new TaskWrapperInboundException(EngineError.ISOMSG_TSK_WRAP_FAILED, (AtmIsoMessage) msg, exp);
	}
    }

}
