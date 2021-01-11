package com.path.atm.engine.connector.pipeline.exception;

import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.exception.IsoRuntimeException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.util.AtmCommonUtil;
import com.path.atm.engine.util.EngineError;

/**
 * 
 * <p>
 * Only thrown inside the TaskWrapperInboundHandler handler.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class TaskWrapperInboundException extends IsoRuntimeException
{

    /**
     * Hold reference to the AtmIsoMessage
     */
    private AtmIsoMessage isoMessage;

    /**
     * Constructs a new TaskWrapperInboundException exception with the specified
     * detail Error and cause.
     *
     * @param error
     * @param errorMsg
     * @param cause
     */
    public TaskWrapperInboundException(EngineError error, AtmIsoMessage isoMessage, Throwable cause)
    {
	super(error, returnMessageInfo(isoMessage), cause);
	this.isoMessage = isoMessage;
    }

    /**
     * Return iso Message string
     * 
     * @param isoMessage
     * @return
     */
    private static String returnMessageInfo(AtmIsoMessage isoMessage)
    {
	String messageInfo = "";

	try
	{

	    isoMessage.returnIsoString();

	}
	catch(IsoMessageException cause)
	{
	    messageInfo = AtmCommonUtil.getEngExpCauseMsg(cause);
	}

	return messageInfo;
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
