package com.path.atm.engine.connector.pipeline.codec;

import java.util.List;

import com.path.atm.engine.connector.pipeline.exception.IsoDecoderException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.MessageFactory;
import com.path.atm.engine.util.EngineError;
import com.path.lib.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Iso 8583 Decoder handler
 * 
 * @author MohammadAliMezzawi
 *
 */
public class Iso8583Decoder extends ByteToMessageDecoder
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
     * Number of byte to strip from the message
     */
    private int initialBytesToStrip;

    /**
     * Construct Iso8583 Decoder
     * 
     * @param initialBytesToStrip
     */
    public Iso8583Decoder(MessageFactory messageFactory, int initialBytesToStrip)
    {
	this.messageFactory = messageFactory;
	this.initialBytesToStrip = initialBytesToStrip;
    }

    /**
     * @implNote Message body starts immediately, no length header, see
     *           <code>"lengthFieldFameDecoder"</code> in
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List out) throws Exception
    {

	byte[] messageBytes = null;

	try
	{
	    logger.debug("[Iso8583Decoder] Start decoding");

	    if(!byteBuf.isReadable())
		return;

	    /**
	     * To log the protocol identification and the header we should have
	     * all the message bytes not only the payload. Since the frame
	     * decoder was skipping those data and offer only the payload we had
	     * to move the logic of extracting payload frame to this level (
	     * handler). First we are getting all the data and store it inside
	     * the iso message for logging purpose, then we are extracting the
	     * payload.
	     * 
	     * @todo memory copy test
	     */
	    messageBytes = new byte[byteBuf.readableBytes()];
	    int readerIndex = byteBuf.readerIndex();
	    byteBuf.getBytes(readerIndex, messageBytes);

	    // strip the length and the protocol identification
	    byteBuf.skipBytes(initialBytesToStrip);

	    //@todo get the protocol identification in the parsing
	    
	    // payload bytes
	    byte[] payloadBytes = new byte[byteBuf.readableBytes()];
	    byteBuf.readBytes(payloadBytes);

	    logger.debug("[Iso8583Decoder] decoding".concat(new String(payloadBytes, "Cp1252")));

	    /**
	     * @todo keep in mind large frame size
	     *       https://www.javatips.net/api/netty-in-action-master/src/main/java/com/manning/
	     *       nettyinaction/chapter7/SafeByteToMessageDecoder.java
	     */

	    /**
	     * At this level we will convert the byte buffers to an ISO message
	     * to be handled by the next handler
	     * 
	     * @note : never parse the additional info at this level : 
	     * 1- it's Costly 
	     * 2- ISO message isn't serializable which mean we will
	     * not be able to push it to the task container rather we will
	     * push it as ISO task with Bytes
	     */
	    AtmIsoMessage isoMessage = messageFactory.parseMessage(payloadBytes, false);
	    isoMessage.setMessageBytes(messageBytes);
	    out.add(isoMessage);

	}
	catch(Exception exception)
	{
	    /**
	     * Wrap all exceptions at this level with IsoDecoderException so we
	     * can differentiate it while handling it at the
	     * {@link PipelineExceptionHandler}
	     */
	    throw new IsoDecoderException(EngineError.ISOMSG_PARSE_FAILED, messageBytes, exception);
	}

    }

    /**
     * Handle exception thrown during handler invocation
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {

	logger.debug("[Iso8583Decoder] : exceptionCaught");
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
