package com.path.atm.engine.pool.tasks;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Iso task is the main wrapper of the Atm iso message.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IsoTask implements Task
{

    /**
     * Task should be serializable
     */
    private static final long serialVersionUID = 1L;

    /**
     * A Unique id that identify the task
     */
    private long id;

    /**
     * A Unique id that identify the task
     * 
     * @note not in use
     */
    private String uuid;

    /**
     * Iso Interface code
     * 
     * @todo : mainly used in logging
     */
    private BigDecimal interfaceCode;

    /**
     * Hold iso message mti
     */
    private int messageType;
  
    /**
     * Full Message Bytes
     * Used mainly for logging
     */
    private transient byte[] messageBytes;
    
    /**
     * Payload Bytes [header][body]
     */
    private byte[] payloadBytes;
    
    /**
     * Full Message String
     * Used mainly for logging
     */
    private transient String messageIsoString;

    /**
     * Received time
     * 
     * @todo : mainly used in logging
     */
    private Date receivedTime;

    /**
     * Starting time
     * 
     * @todo : mainly used in logging
     */
    private Date startingTime;

    /**
     * End time
     * 
     * @todo : mainly used in logging
     */
    private Date endTime;

    /**
     * Request ID
     * 
     * @note: mainly used to log the iso task Common Pws request id
     */
    private String pwsReqId;
    
    /**
     * Status
     * 
     * @note: mainly used to log the iso task status
     */
    private String status;

    /**
     * Status
     * 
     * @note: mainly used to log the iso task error message
     */
    private String errorMsg;

    /**
     * @return the messageType
     */
    public int getMessageType()
    {
	return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(int messageType)
    {
	this.messageType = messageType;
    }

    /**
     * @return the messageBytes
     */
    public byte[] getMessageBytes()
    {
        return messageBytes;
    }

    /**
     * @param messageBytes the messageBytes to set
     */
    public void setMessageBytes(byte[] messageBytes)
    {
        this.messageBytes = messageBytes;
    }

    /**
     * @return the payloadBytes
     */
    public byte[] getPayloadBytes()
    {
        return payloadBytes;
    }

    /**
     * @param payloadBytes the payloadBytes to set
     */
    public void setPayloadBytes(byte[] payloadBytes)
    {
        this.payloadBytes = payloadBytes;
    }

    /**
     * @return the messageIsoString
     */
    public String getMessageIsoString()
    {
	return messageIsoString;
    }

    /**
     * @param messageIsoString the messageIsoString to set
     */
    public void setMessageIsoString(String messageIsoString)
    {
	this.messageIsoString = messageIsoString;
    }

    /**
     * @return the receivedTime
     */
    public Date getReceivedTime()
    {
	return receivedTime;
    }

    /**
     * @param receivedTime the receivedTime to set
     */
    public void setReceivedTime(Date receivedTime)
    {
	this.receivedTime = receivedTime;
    }

    /**
     * @return the startingTime
     */
    public Date getStartingTime()
    {
	return startingTime;
    }

    /**
     * @param startingTime the startingTime to set
     */
    public void setStartingTime(Date startingTime)
    {
	this.startingTime = startingTime;
    }

    /**
     * @return the endTime
     */
    public Date getEndTime()
    {
	return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime)
    {
	this.endTime = endTime;
    }

    @Override
    public String getGroupKey()
    {
	return null;
    }

    /**
     * @return the id
     */
    public long getId()
    {
	return id;
    }

    /**
     * @param integer the id to set
     */
    public void setId(long id)
    {
	this.id = id;
    }

    /**
     * @return the interfaceCode
     */
    public BigDecimal getInterfaceCode()
    {
	return interfaceCode;
    }

    /**
     * @param interfaceCode the interfaceCode to set
     */
    public void setInterfaceCode(BigDecimal interfaceCode)
    {
	this.interfaceCode = interfaceCode;
    }

    /**
     * @return the uuid
     */
    public String getUuid()
    {
	return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid)
    {
	this.uuid = uuid;
    }

    /**
     * @return the pwsReqId
     */
    public String getPwsReqId()
    {
	return pwsReqId;
    }

    /**
     * @param pwsReqId the pwsReqId to set
     */
    public void setPwsReqId(String pwsReqId)
    {
	this.pwsReqId = pwsReqId;
    }

    /**
     * @return the status
     */
    public String getStatus()
    {
	return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status)
    {
	this.status = status;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg()
    {
	return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg)
    {
	this.errorMsg = errorMsg;
    }

    @Override
    public void run()
    {
	// TODO Auto-generated method stub
	
    }
}
