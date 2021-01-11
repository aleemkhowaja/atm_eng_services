package com.path.atm.engine.connector.pipeline.codec;

import com.path.atm.engine.connector.pipeline.exception.IsoEncoderException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.MessageFactory;
import com.path.atm.engine.util.AtmCommonUtil;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.AtmLogger;
import com.path.atm.engine.util.EngineError;
import com.path.lib.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * Encodes message in a stream-like fashion from one message to an
 * {@link ByteBuf}.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class Iso8583Encoder extends MessageToByteEncoder<AtmIsoMessage>
{

    /**
     * Logger
     */
    private Log logger = Log.getInstance();

    /**
     * Hold reference to the logger
     */
    private static AtmLogger atmlogger = AtmLogger.getInstance();

    /**
     * Length of the Header
     */
    private final int lengthHeaderLength;

    /**
     * Message factory
     */
    private final MessageFactory messageFactory;

    /**
     * Constructor
     * 
     * @param messageFactory
     * 
     * @param lengthHeaderLength
     */
    public Iso8583Encoder(MessageFactory messageFactory, int lengthHeaderLength)
    {
	this.lengthHeaderLength = lengthHeaderLength;
	this.messageFactory = messageFactory;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AtmIsoMessage isoMessage, ByteBuf out)
    {
	try
	{
	    /**
	     * @todo fix this , we should use maybe the new AtmIsoMessage
	     *       writeToBuffer instead of using the writeData/writeToBuffer
	     */
	    logger.debug("[Iso8583Encoder] Start encoding");

	    // @todo we may need to remove this part
	    // in case message doesn't have a header
	    if(lengthHeaderLength == 0)
	    {
		byte[] bytes = isoMessage.writeData();
		out.writeBytes(bytes);
		return;
	    }

	    // write the data to the buffer with a length header
	    ByteBuffer byteBuffer = isoMessage.writeToBuffer(lengthHeaderLength);
	    out.writeBytes(byteBuffer);
	    isoMessage.setStatus(AtmEngineConstants.MSG_STATUS_PROCESSED);

	}
	catch(Exception exception)
	{

	    isoMessage.setStatus(AtmEngineConstants.MSG_STATUS_FAILED);
	    isoMessage.setErrorMessage(AtmCommonUtil.getEngExpCauseMsg(exception));
	    /**
	     * Wrap all exceptions at this level with IsoEncoderException so we
	     * can differentiate it while handling it at the
	     * {@link PipelineExceptionHandler}
	     */
	    throw new IsoEncoderException(EngineError.ISOMSG_ENCODING_FAILED, isoMessage, exception);
	}
	finally
	{
	    logIsoMessage(isoMessage);
	}

    }

    /**
     * @param isoMessage
     */
    private void logIsoMessage(AtmIsoMessage isoMessage)
    {
	// @todo we will update the status in case of failed

	if(getMessageFactory().isResponse(isoMessage.getType()))
	    // we just need to check if we have an exception to update the
	    // status
	    return;

	isoMessage.setInterfaceCode(getMessageFactory().getInterfaceCode());
	isoMessage.setStartingTime(Calendar.getInstance().getTime());
	atmlogger.logOutgoingRequest(isoMessage);
    }

    /**
     * Handle exception thrown during encoding
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {

	logger.debug("[Iso8583Encoder] : exceptionCaught");
	super.exceptionCaught(ctx, cause);
    }

    /**
     * @return the messageFactory
     */
    public MessageFactory getMessageFactory()
    {
	return messageFactory;
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString()
    {
	return "Iso8583Decoder";
    }
}
