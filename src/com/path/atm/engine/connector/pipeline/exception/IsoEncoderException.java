package com.path.atm.engine.connector.pipeline.exception;

import com.path.atm.engine.exception.IsoRuntimeException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.util.EngineError;

/**
 * This type of exception is thrown whenever the encoder handler failed to
 * encode the message.
 * 
 * <p>
 * Only thrown inside the encoder handler.
 *
 * @author MohammadAliMezzawi
 *
 */
public class IsoEncoderException extends IsoRuntimeException
{

    /**
     * Hold Atm Iso Message
     */
    private AtmIsoMessage isoMessage;

    /**
     * Constructs a new IsoFrameDecoderException exception with the specified
     * detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public IsoEncoderException(EngineError error, AtmIsoMessage isoMessage, Throwable cause)
    {
	super(error, cause);
	setIsoMessage(isoMessage);
    }

    /**
     * @return the isoMessage
     */
    public AtmIsoMessage getIsoMessage()
    {
	return isoMessage;
    }

    /**
     * @param isoMessage the isoMessage to set
     */
    public void setIsoMessage(AtmIsoMessage isoMessage)
    {
	this.isoMessage = isoMessage;
    }
}
