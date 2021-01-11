package com.path.atm.engine.iso8583.messages;

import java.util.HashMap;

import com.path.atm.engine.iso8583.MessageFactory;

/**
 * An ISO message can be created using this draft message class
 * 
 * @author MohammadAliMezzawi
 *
 */
@SuppressWarnings("rawtypes")
abstract public class AtmIsoMessageDraft<DraftMessage extends AtmIsoMessageDraft>
{

    /**
     * MTI type
     */
    protected int type;

    /**
     * Message network type ( ECHO, LOGOFF, LOGON )
     */
    protected String netType;

    /**
     * Hold reference to the message factory
     */
    protected MessageFactory msgFactory;

    /**
     * Hold reference to the response code
     */
    protected HashMap<String, Object> fields = new HashMap<>();

    /**
     * @param msgFactory
     */
    public AtmIsoMessageDraft(MessageFactory msgFactory)
    {
	this.msgFactory = msgFactory;
    }

    /**
     * 
     * @param mtiType
     * @param msgFactory
     */
    public AtmIsoMessageDraft(int mtiType, MessageFactory msgFactory)
    {
	this.type = mtiType;
	this.msgFactory = msgFactory;
    }

    /**
     * @return the type
     */
    public int getType()
    {
	return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type)
    {
	this.type = type;
    }

    /**
     * @return the netType
     */
    public String getNetType()
    {
	return netType;
    }

    /**
     * @param netType the netType to set
     * @return
     */
    @SuppressWarnings("unchecked")
    public DraftMessage setNetType(String netType)
    {
	this.netType = netType;
	return (DraftMessage) this;
    }

    /**
     * @return the fields
     */
    public HashMap<String, Object> getFields()
    {
	return fields;
    }

    /**
     * @param fields the fields to set
     */
    @SuppressWarnings("unchecked")
    public DraftMessage setFields(HashMap<String, Object> fields)
    {
	this.fields = fields;
	return (DraftMessage) this;
    }

    /**
     * @return the msgFactory
     */
    public MessageFactory getMsgFactory()
    {
	return msgFactory;
    }

    /**
     * @param msgFactory the msgFactory to set
     */
    public void setMsgFactory(MessageFactory msgFactory)
    {
	this.msgFactory = msgFactory;
    }
}
