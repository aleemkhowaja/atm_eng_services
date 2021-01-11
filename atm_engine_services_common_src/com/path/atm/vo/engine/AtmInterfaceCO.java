package com.path.atm.vo.engine;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.path.lib.vo.BaseVO;

/**
 * Represent Atm interface.
 * <p>
 * it will contain the main atm interface info plus a list of available
 * AtmIsoMsgDefCO
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmInterfaceCO extends BaseVO
{

    /**
     * Hold atm interface code
     */
    private BigDecimal code;

    /**
     * Hold atm interface compCode
     */
    private BigDecimal compCode;

    /**
     * Hold atm interface compName
     */
    private String compBriefName;

    /**
     * Hold atm interface description
     */
    private String description;

    /**
     * Hold atm interface type
     */
    private String type;

    /**
     * Hold atm interface includeLength
     */
    private BigDecimal includeLength;

    /**
     * Hold atm interface msgLength
     */
    private BigDecimal msgLength;

    /**
     * Hold atm interface protocolIdentification
     */
    private String protocolIdentification;

    /**
     * Hold atm interface headerLength
     */
    private BigDecimal headerLength;

    /**
     * Hold atm interface description
     */
    private String headerData;

    /**
     * Hold atm interface description
     */
    private String bitmapType;

    /**
     * Hold atm interface description
     */
    private String lengthType;

    /**
     * Hold atm interface description
     */
    private Date currentDate;

    /**
     * Hold atm interface description
     */
    private String includeHeader;

    /**
     * Hold atm interface description
     */
    private String posInHeader;

    /**
     * Hold atm interface description
     */
    private String isoPresent;

    /**
     * Hold atm interface description
     */
    private String skipBitmap;

    /**
     * Hold atm interface description
     */
    private String status;

    /**
     * Hold atm interface list of iso fields
     */
    private LinkedHashMap<BigDecimal, AtmIsoFieldCO> isoFieldList;

    /**
     * Hold atm interface list of iso Msg
     */
    private HashMap<String, AtmIsoMessageCO> isoMsgList;

    /**
     * Hold atm interface list of response and request map
     */
    private HashMap<String, String> responseReqMap;

    /**
     * Hold atm interface list of iso Msg definition keyed by MTI_CODE
     * PROCESS_CODE
     */
    private HashMap<String, List<AtmIsoMessageDefCO>> isoMessageDefMap;

    /**
     * Hold list of Network iso Msg definition keyed by Network type
     */
    private HashMap<String, AtmIsoMessageDefCO> isoNetMessageDefMap;

    /**
     * Hold atm interface list of iso Acquirers
     */
    private HashMap<BigDecimal,AcquirerCO> acquirers;
    
    /**
     * Hold the acquirers Joined expressions
     */
    private String acquirersJoinedExp;
    

    /**
     * @return the code
     */
    public BigDecimal getCode()
    {
	return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(BigDecimal code)
    {
	this.code = code;
    }

    /**
     * @return the compCode
     */
    public BigDecimal getCompCode()
    {
	return compCode;
    }

    /**
     * @param compCode the compCode to set
     */
    public void setCompCode(BigDecimal compCode)
    {
	this.compCode = compCode;
    }

    /**
     * @return the compBriefName
     */
    public String getCompBriefName()
    {
	return compBriefName;
    }

    /**
     * @param compBriefName the compBriefName to set
     */
    public void setCompBriefName(String compBriefName)
    {
	this.compBriefName = compBriefName;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
	return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
	this.description = description;
    }

    /**
     * @return the type
     */
    public String getType()
    {
	return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
	this.type = type;
    }

    /**
     * @return the includeLength
     */
    public BigDecimal getIncludeLength()
    {
	return includeLength;
    }

    /**
     * @param includeLength the includeLength to set
     */
    public void setIncludeLength(BigDecimal includeLength)
    {
	this.includeLength = includeLength;
    }

    /**
     * @return the protocolIdentification
     */
    public String getProtocolIdentification()
    {
	return protocolIdentification;
    }

    /**
     * @param includeLength the protocolIdentification to set
     */
    public void setProtocolIdentification(String protocolIdentification)
    {
	this.protocolIdentification = protocolIdentification;
    }

    /**
     * @return the msgLength
     */
    public BigDecimal getMsgLength()
    {
	return msgLength;
    }

    /**
     * @param msgLength the msgLength to set
     */
    public void setMsgLength(BigDecimal msgLength)
    {
	this.msgLength = msgLength;
    }

    /**
     * @return the headerLength
     */
    public BigDecimal getHeaderLength()
    {
	return headerLength;
    }

    /**
     * @param headerLength the headerLength to set
     */
    public void setHeaderLength(BigDecimal headerLength)
    {
	this.headerLength = headerLength;
    }

    /**
     * @return the headerData
     */
    public String getHeaderData()
    {
	return headerData;
    }

    /**
     * @param headerData the headerData to set
     */
    public void setHeaderData(String headerData)
    {
	this.headerData = headerData;
    }

    /**
     * @return the bitmapType
     */
    public String getBitmapType()
    {
	return bitmapType;
    }

    /**
     * @param bitmapType the bitmapType to set
     */
    public void setBitmapType(String bitmapType)
    {
	this.bitmapType = bitmapType;
    }

    /**
     * @return the lengthType
     */
    public String getLengthType()
    {
	return lengthType;
    }

    /**
     * @param lengthType the lengthType to set
     */
    public void setLengthType(String lengthType)
    {
	this.lengthType = lengthType;
    }

    /**
     * @return the currentDate
     */
    public Date getCurrentDate()
    {
	return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate)
    {
	this.currentDate = currentDate;
    }

    /**
     * @return the includeHeader
     */
    public String getIncludeHeader()
    {
	return includeHeader;
    }

    /**
     * @param includeHeader the includeHeader to set
     */
    public void setIncludeHeader(String includeHeader)
    {
	this.includeHeader = includeHeader;
    }

    /**
     * @return the posInHeader
     */
    public String getPosInHeader()
    {
	return posInHeader;
    }

    /**
     * @param posInHeader the posInHeader to set
     */
    public void setPosInHeader(String posInHeader)
    {
	this.posInHeader = posInHeader;
    }

    /**
     * @return the isoPresent
     */
    public String getIsoPresent()
    {
	return isoPresent;
    }

    /**
     * @param isoPresent the isoPresent to set
     */
    public void setIsoPresent(String isoPresent)
    {
	this.isoPresent = isoPresent;
    }

    /**
     * @return the skipBitmap
     */
    public String getSkipBitmap()
    {
	return skipBitmap;
    }

    /**
     * @param skipBitmap the skipBitmap to set
     */
    public void setSkipBitmap(String skipBitmap)
    {
	this.skipBitmap = skipBitmap;
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
     * @return
     */
    public LinkedHashMap<BigDecimal, AtmIsoFieldCO> getIsoFieldList()
    {
	return isoFieldList;
    }

    /**
     * @param isoFieldList
     */
    public void setIsoFieldList(LinkedHashMap<BigDecimal, AtmIsoFieldCO> isoFieldList)
    {
	this.isoFieldList = isoFieldList;
    }

    /**
     * @return the isoMsgList
     */
    public HashMap<String, AtmIsoMessageCO> getIsoMsgList()
    {
	return isoMsgList;
    }

    /**
     * @param isoMsgList the isoMsgList to set
     */
    public void setIsoMsgList(HashMap<String, AtmIsoMessageCO> isoMsgList)
    {
	this.isoMsgList = isoMsgList;
    }

    /**
     * @return the isoMessageDefMap
     */
    public HashMap<String, List<AtmIsoMessageDefCO>> getIsoMessageDefMap()
    {
	return isoMessageDefMap;
    }

    /**
     * @param isoMessageDefMap the isoMessageDefMap to set
     */
    public void setIsoMessageDefMap(HashMap<String, List<AtmIsoMessageDefCO>> isoMessageDefMap)
    {
	this.isoMessageDefMap = isoMessageDefMap;
    }

    /**
     * @return the isoNetMessageDefMap
     */
    public HashMap<String, AtmIsoMessageDefCO> getIsoNetMessageDefMap()
    {
	return isoNetMessageDefMap;
    }

    /**
     * @param isoNetMessageDefMap the isoNetMessageDefMap to set
     */
    public void setIsoNetMessageDefMap(HashMap<String, AtmIsoMessageDefCO> isoNetMessageDefMap)
    {
	this.isoNetMessageDefMap = isoNetMessageDefMap;
    }

    /**
     * @return the acquirerList
     */
    public HashMap<BigDecimal,AcquirerCO> getAcquirers()
    {
	return acquirers;
    }
    
    /**
     * @param acquirersHm
     */
    public void setAcquirers(HashMap<BigDecimal,AcquirerCO> acquirersHm)
    {
	acquirers = acquirersHm;
    }

    /**
     * @return the acquirersJoinedExp
     */
    public String getAcquirersJoinedExp()
    {
	return acquirersJoinedExp;
    }

    /**
     * @param acquirersJoinedExp the acquirersJoinedExp to set
     */
    public void setAcquirersJoinedExp(String acquirersJoinedExp)
    {
	this.acquirersJoinedExp = acquirersJoinedExp;
    }

    /**
     * @return the responseReqMap
     */
    public HashMap<String, String> getResponseReqMap()
    {
	return responseReqMap;
    }

    /**
     * @param responseReqMap the responseReqMap to set
     */
    public void setResponseReqMap(HashMap<String, String> responseReqMap)
    {
	this.responseReqMap = responseReqMap;
    }

}
