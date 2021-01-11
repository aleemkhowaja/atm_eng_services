package com.path.atm.engine.connector.pipeline.exception;

import com.path.atm.engine.exception.IsoRuntimeException;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.EngineError;

import io.netty.buffer.ByteBuf;

/**
 * This type of exception is thrown whenever the decoder handler failed to
 * decode the message.
 * 
 * <p>
 * Only thrown inside the decoder handler.
 *
 * @author MohammadAliMezzawi
 *
 */
public class IsoDecoderException extends IsoRuntimeException
{
    /**
     * Hold the bytes where the exception has occurred while decoding it
     */
    private byte[] decodeBytes;

    /**
     * Constructs a new IsoFrameDecoderException exception with the specified
     * detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public IsoDecoderException(EngineError error, byte[] bytes, Throwable cause)
    {
	super(error, cause);
	setDecodeBytes(bytes);
    }

    /**
     * Constructs a new IsoFrameDecoderException exception with the specified
     * detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public IsoDecoderException(EngineError error, String errorMessage, byte[] bytes, Throwable cause)
    {
	super(error, errorMessage, cause);
	setDecodeBytes(bytes);
    }

    /**
     * @return the decodeBytes
     */
    public byte[] getDecodeBytes()
    {
	return decodeBytes;
    }

    /**
     * @param decodeBytes the decodeBytes to set
     */
    public void setDecodeBytes(byte[] decodeBytes)
    {
	this.decodeBytes = decodeBytes;
    }

    /**
     * @todo we may move this to a util class Return the buffer info as string
     * @param in
     * @return
     * @deprecated 16/06/2020 but keeping it for a while until the full logging
     *             implementation is done.
     */
    private static String returnBufInfo(ByteBuf in)
    {
	ByteBuf clone = in.copy();
	byte[] bytes = new byte[in.readableBytes()];
	clone.readBytes(bytes);

	StringBuilder sb = new StringBuilder(" Byte Buffer Info : {").append(" readableBytes : ")
		.append(in.readableBytes()).append(" readerIdx : ").append(in.readerIndex()).append(" Writter idx : ")
		.append(in.writerIndex()).append(" Bytes : ")
		.append(new String(bytes, AtmEngineConstants.CHAR_ENCODING));

	return sb.toString();
    }
}
