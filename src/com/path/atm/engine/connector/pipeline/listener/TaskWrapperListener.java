package com.path.atm.engine.connector.pipeline.listener;

import java.util.Calendar;

import com.path.atm.engine.connector.pipeline.handler.PipelineExceptionHandler;
import com.path.atm.engine.connector.util.AtmConnectorConstants;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.pool.tasks.IsoTask;
import com.path.atm.engine.util.AtmCommonUtil;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.AtmLogger;
import com.path.lib.log.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;

/**
 * This class will wrap a IsoMessage with a task wrapper and push it to the task
 * container
 * 
 * @author MohammadAliMezzawi
 *
 * @param <T>
 */
public class TaskWrapperListener implements IsoMessageListener<AtmIsoMessage>
{

    /**
     * Hold reference to the logger
     */
    private static AtmLogger atmlogger = AtmLogger.getInstance();

    /**
     * Error Logger
     */
    private Log logger = Log.getInstance();

    /**
     * In case of Iso Message we should always apply this wrapper
     */
    public boolean applies(AtmIsoMessage isoMessage)
    {
	return true;
    }

    /**
     * Always returns <code>false</code>.
     * 
     * <p>
     * Never ever throw an exception outside the taskWrapper listener since the
     * {@link PipelineExceptionHandler} will catch it and log an empty request
     * in the request table.
     * 
     * <p>
     * Taskwrapper is the last 'Real' inbound handler for that we are logging
     * the request at this level to make sure that any error that could occur in
     * the inbound channel is caught and stored in the DB.
     * 
     * @param isoMessage a message to handle
     * @return <code>false</code> - message should not be handled by any other
     *         handler.
     */
    public boolean onMessage(ChannelHandlerContext ctx, AtmIsoMessage isoMessage)
    {

	try
	{
	    Attribute<AtmEngineReactor> attr = ctx.channel().attr(AtmConnectorConstants.REACTOR_KEY);
	    AtmEngineReactor reactor = attr.get();

	    return reactor.getMessageFactory().isResponse(isoMessage.getType()) ? handleResponse(ctx, isoMessage)
		    : handleRequest(ctx, isoMessage);

	}
	catch(Exception e)
	{
	    logger.error(e, "Error while creating task message");
	}

	return false;
    }

    /**
     * Push a new task to the task container. Always returns <code>false</code>.
     * 
     * @param ctx
     * @param isoMessage
     * @return
     */
    public boolean handleRequest(ChannelHandlerContext ctx, AtmIsoMessage isoMessage)
    {
	IsoTask tsk = prepareTask(ctx, isoMessage);

	try
	{
	    // submit the task if it was successfully prepared
	    if(AtmEngineConstants.MSG_STATUS_FAILED.equalsIgnoreCase(tsk.getStatus()))
		return false;

	    // get reactor
	    Attribute<AtmEngineReactor> attr = ctx.channel().attr(AtmConnectorConstants.REACTOR_KEY);
	    AtmEngineReactor reactor = attr.get();

	    reactor.getTaskContClient().submit(tsk);
	    tsk.setStatus(AtmEngineConstants.MSG_STATUS_RECEIVED);

	}
	catch(Exception e)
	{

	    /**
	     * Since none of the above action throw an Iso exception the error
	     * message will be reflecting the real technical message. In another
	     * word no Engine Error message will be generated at this level.
	     */
	    tsk.setErrorMsg(AtmCommonUtil.getEngExpCauseMsg(e));
	    tsk.setStatus(AtmEngineConstants.MSG_STATUS_FAILED);
	    logger.error(e, "Error while creating task message");

	}
	finally
	{
	    atmlogger.logIncomingRequest(tsk);
	}

	return true;
    }

    /**
     * Log the response
     * 
     * @param ctx
     * @param isoMessage
     * @return
     */
    public boolean handleResponse(ChannelHandlerContext ctx, AtmIsoMessage isoMessage)
    {
	IsoTask tsk = prepareTask(ctx, isoMessage);
	tsk.setStatus(AtmEngineConstants.MSG_STATUS_RECEIVED);
	atmlogger.logIncomingResponse(tsk);
	return true;
    }

    /**
     * Prepare the ISO Task to be submitted or logged
     * 
     * @param ctx
     * @param isoMessage
     * @return
     */
    private IsoTask prepareTask(ChannelHandlerContext ctx, AtmIsoMessage isoMessage)
    {

	// get the task container and push the task to it
	IsoTask tsk = new IsoTask();
	try
	{
	    // @todo fix this , calendar so costy on performance
	    tsk.setReceivedTime(Calendar.getInstance().getTime());

	    // get reactor
	    Attribute<AtmEngineReactor> attr = ctx.channel().attr(AtmConnectorConstants.REACTOR_KEY);
	    AtmEngineReactor reactor = attr.get();

	    /**
	     * Always generate the message id before any action to identify the
	     * message in the db log.
	     */
	    tsk.setId(reactor.getTaskSeqGenerator().nextId());
	    tsk.setInterfaceCode(reactor.getInterfaceCO().getCode());

	    /**
	     * All the below infos are needed to logging except the payloadbytes
	     * which will be submitted to the processing queue.
	     */
	    tsk.setMessageType(isoMessage.getType());
	    tsk.setMessageBytes(isoMessage.getMessageBytes());
	    tsk.setMessageIsoString(isoMessage.returnIsoString());
	    tsk.setPayloadBytes(isoMessage.writeData());

	}
	catch(Exception e)
	{
	    tsk.setErrorMsg(AtmCommonUtil.getEngExpCauseMsg(e));
	    tsk.setStatus(AtmEngineConstants.MSG_STATUS_FAILED);
	    logger.error(e, "Error while creating task message");
	}

	return tsk;
    }
}
