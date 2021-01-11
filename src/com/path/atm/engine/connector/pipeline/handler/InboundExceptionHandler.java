package com.path.atm.engine.connector.pipeline.handler;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import com.path.atm.engine.connector.pipeline.exception.IsoDecoderException;
import com.path.atm.engine.connector.pipeline.exception.IsoFrameDecoderException;
import com.path.atm.engine.connector.pipeline.exception.TaskWrapperInboundException;
import com.path.atm.engine.connector.util.AtmConnectorConstants;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.iso8583.MessageFactory;
import com.path.atm.engine.pool.tasks.IsoTask;
import com.path.atm.engine.util.AtmCommonUtil;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.AtmLogger;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.log.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

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
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter
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
    public InboundExceptionHandler(MessageFactory messageFactory)
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
	 * to the next handler
	 */
	if(shouldHandleException(cause))
	{
	    handleIsoInboundException(ctx, cause);
	    logger.debug("[InboundExceptionHandler] : exceptionCaught");
	    return;
	}

	super.exceptionCaught(ctx, cause);
    }

    /**
     * Handle ISO Inbounds exception
     * 
     * @throws UnsupportedEncodingException
     * @throws NumberFormatException
     */
    private void handleIsoInboundException(ChannelHandlerContext ctx, Throwable cause)
    {
	try
	{

	    // get request data
	    IsoTask task = returnIncomingData(cause);

	    // by default we consider all incoming as Request
	    boolean isRequest = true;

	    // check if it's a request or a response
	    if(task.getMessageType() > 0)
		isRequest = !messageFactory.isResponse(task.getMessageType());

	    Attribute<AtmEngineReactor> attr = ctx.channel().attr(AtmConnectorConstants.REACTOR_KEY);
	    AtmEngineReactor reactor = attr.get();

	    task.setReceivedTime(Calendar.getInstance().getTime());
	    task.setErrorMsg(AtmCommonUtil.getEngExpCauseMsg(cause));
	    task.setStatus(AtmEngineConstants.MSG_STATUS_FAILED);
	    task.setInterfaceCode(reactor.getInterfaceCO().getCode());

	    if(isRequest)
	    {
		// generate message id and populate the interfaceCode
		task.setId(reactor.getTaskSeqGenerator().nextId());
		AtmLogger.getInstance().logIncomingRequest(task);
		return;
	    }

	    /**
	     * @todo we still may need to log the response map in this case
	     */
	    // handle iso response exception
	    AtmLogger.getInstance().logIncomingResponse(task);

	    // @todo later on for POS we should fix the read index
	}
	catch(Exception exp)
	{

	    logger.error(exp, "Failed to handle inbound exception due");

	}
    }

    /**
     * Return the request data
     * 
     * @param cause
     * @return
     * @throws IsoMessageException
     * @throws UnsupportedEncodingException
     * @throws NumberFormatException
     */
    private IsoTask returnIncomingData(Throwable cause)
	    throws IsoMessageException, NumberFormatException, UnsupportedEncodingException
    {
	// prepare the log info
	IsoTask task = new IsoTask();

	int mtiOffset = messageFactory.getHeaderLength();
	byte[] messageBytes = null;

	/**
	 * In case it's a IsoFrameDecoderException. The buffer byte will contain
	 * the full data [protocol][length][header][Payload]
	 */
	if(AtmCommonUtil.isExceptionInstanceOf(cause, IsoFrameDecoderException.class))
	{
	    IsoFrameDecoderException exception = (IsoFrameDecoderException) AtmCommonUtil.getExceptionByTtype(cause,
		    IsoFrameDecoderException.class);

	    ByteBuf data = exception.getByteBuf();

	    if(null != data && data.readableBytes() > 0)
	    {
		// @todo we may remove all none printable character later on
		messageBytes = new byte[data.readableBytes()];
		int readerIndex = data.readerIndex();
		data.getBytes(readerIndex, messageBytes);

		// @todo add protocol identification length here
		mtiOffset += messageFactory.getLenFieldLength();
	    }

	}
	else if(AtmCommonUtil.isExceptionInstanceOf(cause, IsoDecoderException.class))
	{
	    /**
	     * in case it's an Iso decoder exception get the data from the
	     * decode bytes array. [header][Payload]
	     */
	    IsoDecoderException exception = (IsoDecoderException) AtmCommonUtil.getExceptionByTtype(cause,
		    IsoDecoderException.class);

	    // get bytes from the exception
	    messageBytes = exception.getDecodeBytes();
	    mtiOffset += messageFactory.getLenFieldLength();
	}
	else
	{

	    /**
	     * last options TaskWrapperInboundException
	     */
	    TaskWrapperInboundException taskWrapperExp = (TaskWrapperInboundException) AtmCommonUtil
		    .getExceptionByTtype(cause, TaskWrapperInboundException.class);

	    /**
	     * Currently task wrapper is handling the exception internally in
	     * the listener and not throwing anything outside it. but keeping
	     * this check here for safety reason in case the
	     * taskwrapperInboundhanlder itself throw an exception
	     */
	    if(null != taskWrapperExp && null != taskWrapperExp.getIsoMessage())
		messageBytes = taskWrapperExp.getIsoMessage().writeData();
	}

	int mti = 0;
	// get the message mti
	if(null != messageBytes && messageBytes.length > 0)
	    mti = messageFactory.returnMessageType(messageBytes, mtiOffset);

	// set message info
	task.setMessageType(mti);
	task.setMessageBytes(messageBytes);

	return task;
    }

    /**
     * Check if this handler should handle the exception The below exception
     * should be handled 1- IsoFrameDecoderException 2- IsoDecoderException 3-
     * TaskWrapperInboundException
     * 
     * @param cause
     * @return
     */
    private boolean shouldHandleException(Throwable cause)
    {
	return AtmCommonUtil.isExceptionInstanceOf(cause, IsoFrameDecoderException.class)
		|| AtmCommonUtil.isExceptionInstanceOf(cause, IsoDecoderException.class)
		|| AtmCommonUtil.isExceptionInstanceOf(cause, TaskWrapperInboundException.class);
    }

}
