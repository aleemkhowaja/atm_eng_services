package com.path.atm.engine.connector.pipeline.handler;

import java.text.ParseException;
import com.path.atm.engine.iso8583.IsoDefinitionConstants;
import com.path.atm.engine.iso8583.MessageFactory;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles {@link ParseException}s and responds with administrative message
 *
 * @see <a href=
 *      "http://stackoverflow.com/questions/28275677/how-to-answer-an-invalid-iso8583-message">StackOverflow:
 *      How to answer an invalid ISO8583 message</a>
 */
public class ParseExceptionHandler extends ChannelInboundHandlerAdapter
{

    private final boolean includeErrorDetails;

    private final MessageFactory messageFactory;

    public ParseExceptionHandler(MessageFactory messageFactory, boolean includeErrorDetails)
    {
	this.includeErrorDetails = includeErrorDetails;
	this.messageFactory = messageFactory;
    }

    /**
     * Handle any exception thrown during the pipeline's handler invocation
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {

	/**
	 * Whenever a parseException is caught, the connector should respond
	 * with "unable to parse message" Message
	 */
	if(cause instanceof ParseException)
	{
	    final IsoMessage message = createErrorResponseMessage((ParseException) cause);
	    ctx.writeAndFlush(message);
	}

	super.exceptionCaught(ctx, cause);
    }

    /**
     * Create Error response message.
     * 
     * @param cause
     * @return
     */
    protected IsoMessage createErrorResponseMessage(ParseException cause)
    {

	final IsoMessage message = messageFactory.newMessage(0x1644);

	// 650 (Unable to parse message)
	message.setValue(24, IsoDefinitionConstants.FUNC_ADMIN_NOT_PARSE, IsoType.NUMERIC, 3);

	if(includeErrorDetails)
	{
	    // @todo handle null to empty
	    String details = cause.getMessage();
	    if(details.length() > 25)
	    {
		details = details.substring(0, 22) + "...";
	    }

	    // Add additional response data
	    message.setValue(IsoDefinitionConstants.BIT_ADDITIONAL_RESPONSE_DATA, details, IsoType.LLVAR, 25);
	}

	return message;
    }
}
