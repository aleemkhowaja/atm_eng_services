package com.path.atm.engine.iso8583.messages;

import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.IsoDefinitionConstants;
import com.path.atm.engine.iso8583.MessageFactory;

/**
 * This class will house all behaviors related Building response message
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmIsoReqMsgDraft extends AtmIsoMessageDraft<AtmIsoReqMsgDraft>
{

    /**
     * @param mtiType
     * @param msgFactory
     */
    public AtmIsoReqMsgDraft(MessageFactory msgFactory)
    {
	super(msgFactory);
    }

    /**
     * @param mtiType
     * @param msgFactory
     */
    public AtmIsoReqMsgDraft(int mtiType, MessageFactory msgFactory)
    {
	super(mtiType, msgFactory);
    }

    /**
     * Build Atm iso Message as echo
     */
    public AtmIsoMessage asEcho() throws IsoMessageException
    {
	// set type as echo
	return getMsgFactory().createNetWorkMsg(IsoDefinitionConstants.NET_MSG_ECHO, fields);
    }

    /**
     * Build Atm iso Message as Log off message
     */
    public AtmIsoMessage asLogOff() throws IsoMessageException
    {

	// set type as log off
	return getMsgFactory().createNetWorkMsg(IsoDefinitionConstants.NET_MSG_LOGOFF, fields);
    }

    /**
     * Build Atm iso Message as Log on message
     */
    public AtmIsoMessage asLogOn() throws IsoMessageException
    {

	// set type as Log on
	return getMsgFactory().createNetWorkMsg(IsoDefinitionConstants.NET_MSG_LOGON, fields);
    }
}
