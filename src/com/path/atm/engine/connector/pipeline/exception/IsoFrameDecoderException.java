package com.path.atm.engine.connector.pipeline.exception;

import com.path.atm.engine.exception.IsoRuntimeException;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.EngineError;

import io.netty.buffer.ByteBuf;

/**
 * This exception is thrown whenever an operation related to ISO frame decoder
 * manipulation failed
 * 
 * <p>
 * Only thrown inside the IsoLengthFieldBasedFrameDecoder handler.
 *
 * @author MohammadAliMezzawi
 *
 */
public class IsoFrameDecoderException extends IsoRuntimeException
{

    /**
     * Buffer reference
     */
    private ByteBuf byteBuf;

    /**
     * Constructs an IsoFrameDecoderException with the specified detail Error.
     * 
     * @param error the EngineError thrown
     */
    public IsoFrameDecoderException(EngineError error)
    {
	super(error);
    }

    /**
     * Constructs a new IsoFrameDecoderException exception with the specified
     * detail Error and cause.
     * 
     * @param error
     * @param cause
     */
    public IsoFrameDecoderException(EngineError error, Throwable cause)
    {
	super(error, cause);
    }

    /**
     * Constructs a new IsoFrameDecoderException exception with the specified
     * detail Error and cause.
     *
     * @param error
     * @param errorMsg
     */
    public IsoFrameDecoderException(EngineError error, String errorMsg)
    {
	super(error, errorMsg);
    }

    /**
     * Constructs a new IsoFrameDecoderException exception with the specified
     * detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public IsoFrameDecoderException(EngineError error, String errorMsg, Throwable cause)
    {
	super(error, errorMsg, cause);
    }

    /**
     * Constructs a new IsoFrameDecoderException exception with the specified
     * detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public IsoFrameDecoderException(EngineError error, ByteBuf byteBuf, Throwable cause)
    {
	super(error, returnBufInfo(byteBuf), cause);
	this.byteBuf = byteBuf;
    }

    /**
     * @return the byteBuf
     */
    public ByteBuf getByteBuf()
    {
	return byteBuf;
    }

    /**
     * @param byteBuf the byteBuf to set
     */
    public void setByteBuf(ByteBuf byteBuf)
    {
	this.byteBuf = byteBuf;
    }

    /**
     * @todo we may move this to a util class Return the buffer info as string
     * @param in
     * @return
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
