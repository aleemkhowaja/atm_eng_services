package com.path.atm.engine.iso8583;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.exception.IsoRuntimeException;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.engine.AtmIsoFieldCO;
import com.path.bo.common.ConstantsCommon;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.codecs.CompositeField;
import com.solab.iso8583.util.HexCodec;

import net.sf.json.JSONObject;

/**
 * This class house all the ATM ISO message behaviors
 * 
 * @todo we may remove the reference to the objects
 * @author MohammadAliMezzawi
 *
 */
public class AtmIsoMessage extends IsoMessage implements Serializable
{

    /**
     * Iso Message id inherited from task
     */
    private BigDecimal id;

    /**
     * Interface code inherited from task
     */
    private BigDecimal interfaceCode;

    @JsonIgnore
    private byte[] messageBytes;

    /**
     * Message processing status mainly used for outgoing request logging
     */
    @JsonIgnore
    private String status;

    /**
     * Message processing error
     */
    @JsonIgnore
    private String errorMessage;

    /**
     * Processing start time
     * 
     * @note: related to the request Iso Message
     */
    @JsonIgnore
    private Date startingTime;

    /**
     * Processing end time
     * 
     * @note: related to the request Iso Message
     */
    @JsonIgnore
    private Date endingTime;

    /**
     * Acquirer
     */
    private BigDecimal acquirerId;

    /**
     * Hold reference to comp code
     */
    private BigDecimal compCode;

    /**
     * Hold reference to comp brief Name
     */
    private String compBriefName;

    /**
     * Hold reference to Branch code
     */
    private BigDecimal trxBranchCode;

    /**
     * Teller
     */
    private BigDecimal tellerId;

    /**
     * Hold reference to teller user Id
     */
    private String tellerUserId;

    /**
     * transaction type id
     */
    private BigDecimal trxType;

    /**
     * Charge transaction type id
     */
    private BigDecimal chargeTrxType;

    /**
     * Merchant
     */
    private BigDecimal merchantCode;

    /**
     * Merchant Additional Reference
     */
    private String merchAdditionalRef;

    /**
     * Merchant Iban Acc no
     */
    private String merchIbanAccNo;

    /**
     * Terminal
     */
    private String terminalCode;

    /**
     * Terminal
     */
    private String terminalDesc;

    /**
     * Hold error desc Mainly used to hold response error Desc
     */
    private String coreErrorDesc;

    /**
     * Hold core trxNb
     */
    private BigDecimal trxNb;

    /**
     * Reference to the creator
     */
    @JsonIgnore
    private MessageFactory factory;

    /**
     * Default constructor
     */
    public AtmIsoMessage()
    {
	super();
    }

    /**
     * @param header
     */
    public AtmIsoMessage(String header)
    {
	super(header);
    }

    /**
     * @param binHeader
     */
    public AtmIsoMessage(byte[] binHeader)
    {
	super(binHeader);
    }

    /**
     * Format message as Associative Array
     * 
     * @param includeSensitiveData
     * @param includeDesc
     * 
     * @return
     */
    public String formatMessage(boolean includeSensitiveData, boolean includeDesc) throws IsoMessageException
    {

	if(null == getFactory())
	    throw new IsoMessageException(EngineError.ISOMSG_FORMAT_FAILED);

	StringBuilder sb = new StringBuilder();

	// include sensitive data
	if(includeSensitiveData)
	    sb.append("Message: ").append(debugString()).append("\n");

	// Set Basic fields
	sb.append("MTI: 0x").append(String.format("%04x", getType())).append("\n")
		.append(IsoDefinitionConstants.ISO_FIELD_COMPCODE).append(": ").append(getCompCode()).append("\n")
		.append(IsoDefinitionConstants.ISO_FIELD_COMPNAME).append(": ").append(getCompBriefName()).append("\n")
		.append(IsoDefinitionConstants.ISO_FIELD_BRCODE).append(": ").append(getTrxBranchCode()).append("\n")
		.append(IsoDefinitionConstants.ISO_FIELD_TELLCODE).append(": ").append(getTellerId()).append("\n")
		.append(IsoDefinitionConstants.ISO_FIELD_TELLERUSER).append(": ").append(getTellerUserId()).append("\n")
		.append(IsoDefinitionConstants.ISO_FIELD_TRXCODE).append(": ").append(getTrxType()).append("\n")
		// @todo add charges
		.append(IsoDefinitionConstants.ISO_FIELD_MERACC).append(": ").append(getMerchAdditionalRef())
		.append("\n")
		// @todo add terminal description
		.append(IsoDefinitionConstants.ISO_FIELD_TERMINAL).append(": ").append(getTerminalCode());

	HashMap<BigDecimal, AtmIsoFieldCO> isofieldsList = getFactory().getIsoFieldList();
	Iterator<Entry<BigDecimal, AtmIsoFieldCO>> isoFieldIt = isofieldsList.entrySet().iterator();

	while(isoFieldIt.hasNext())
	{

	    Entry<BigDecimal, AtmIsoFieldCO> atmIsoFieldEntry = isoFieldIt.next();
	    AtmIsoFieldCO fieldCO = atmIsoFieldEntry.getValue();
	    int fieldCode = fieldCO.getCode().intValue();

	    if(!hasField(fieldCode))
		continue;

	    final IsoValue<Object> field = getField(fieldCode);
	    sb.append("\n  ").append(fieldCode).append(": [");

	    // add field description
	    if(includeDesc)
		sb.append(fieldCO.getName()).append(':');

	    // always include field value
	    String formattedValue = field.toString();

	    String mask = StringUtil.nullToEmpty(fieldCO.getPartialMask());

	    // if total mask
	    if(StringUtil.nullEmptyToValue(fieldCO.getTotalMask(), ConstantsCommon.NO)
		    .equalsIgnoreCase(ConstantsCommon.YES))
	    {
		mask = StringUtils.rightPad("", formattedValue.length(), IsoDefinitionConstants.MASK_HIDE_BIT);
	    }

	    // if no mask applied
	    if(!mask.isEmpty())
		formattedValue = IsoUtil.maskField(formattedValue, mask);

	    // append field info
	    sb.append(field.getType()).append('(').append(field.getLength()).append(")] = '").append(formattedValue)
		    .append('\'');
	}

	return sb.toString();
    }

    /**
     * Return a String representation of the Authorization code.
     * 
     * Since authorization code isn't an imal calculated field but is an alias
     * to an ISO field we didn't create an new property in AtmIsoMessage rather
     * we created a function.
     * 
     * @return
     */
    public String returnAuthCodeStr()
    {
	return returnFieldValueStr(IsoDefinitionConstants.BIT_APPROVAL_CODE);
    }

    /**
     * Return Process code.
     * 
     * Since Process code isn't an imal calculated field but is an alias to an
     * ISO field we didn't create an new property in AtmIsoMessage rather we
     * created a function.
     * 
     * @return
     */
    public String returnProcessCode()
    {
	return returnFieldValueStr(IsoDefinitionConstants.BIT_PROCESSING_CODE);
    }

    /**
     * Return Response code.
     * 
     * Since Process code isn't an imal calculated field but is an alias to an
     * ISO field we didn't create an new property in AtmIsoMessage rather we
     * created a function.
     * 
     * @return
     */
    public String returnResponseCode()
    {
	return returnFieldValueStr(IsoDefinitionConstants.BIT_ACTION_CODE);
    }

    /**
     * Return a String representation of the Retrieval Code.
     * 
     * Since Retrieval Code isn't an imal calculated field but is an alias to an
     * ISO field we didn't create an new property in AtmIsoMessage rather we
     * created a function.
     * 
     * @return
     */
    public String returnRetrievalRefNbrStr()
    {
	return returnFieldValueStr(IsoDefinitionConstants.BIT_RETRIEVAL_NUMBER);
    }
    
    /**
     * Return a String representation of the Terminal Code.
     * 
     * Since Terminal Code isn't an imal calculated field but is an alias to an
     * ISO field we didn't create an new property in AtmIsoMessage rather we
     * created a function.
     * 
     * @return
     */
    public String returnTerminalCode()
    {
	return StringUtil.nullToEmpty(returnFieldValueStr(IsoDefinitionConstants.BIT_TERMINAL_ID));
    }
    

    /**
     * Return The full message String
     * 
     * @return
     * @throws IsoMessageException
     */
    public String returnIsoString() throws IsoMessageException
    {
	return returnIsoString(false);
    }

    /**
     * Returns a string representation of the message, as if it were encoded in
     * ASCII with no binary bitmap taking into consideration the masked fields
     * 
     * @return A string representation of the message
     */
    public String returnIsoString(boolean onlyPayload) throws IsoMessageException
    {

	if(null == getFactory())
	    throw new IsoMessageException(EngineError.ISOMSG_FORMAT_FAILED);

	StringBuilder sb = new StringBuilder();

	// Append the header
	if(getIsoHeader() != null)
	{
	    sb.append(getIsoHeader());
	}
	else if(getBinaryIsoHeader() != null)
	    sb.append("[0x").append(HexCodec.hexEncode(getBinaryIsoHeader(), 0, getBinaryIsoHeader().length))
		    .append("]");

	// append the type
	sb.append(String.format("%04x", getType()));

	// append bitmap
	sb.append(IsoUtil.convertBitmapToHex(IsoUtil.createBitmapBitSet(this)));

	// start formatting the
	HashMap<BigDecimal, AtmIsoFieldCO> isofieldsList = getFactory().getIsoFieldList();
	Iterator<Entry<BigDecimal, AtmIsoFieldCO>> isoFieldIt = isofieldsList.entrySet().iterator();

	while(isoFieldIt.hasNext())
	{

	    Entry<BigDecimal, AtmIsoFieldCO> atmIsoFieldEntry = isoFieldIt.next();
	    AtmIsoFieldCO fieldCO = atmIsoFieldEntry.getValue();
	    int fieldCode = fieldCO.getCode().intValue();

	    if(!hasField(fieldCode))
		continue;

	    final IsoValue<Object> field = getField(fieldCode);

	    // always include field value
	    String formattedValue = field.toString();

	    // check mask
	    String mask = StringUtil.nullToEmpty(fieldCO.getPartialMask());

	    // if total mask
	    if(StringUtil.nullEmptyToValue(fieldCO.getTotalMask(), ConstantsCommon.NO)
		    .equalsIgnoreCase(ConstantsCommon.YES))
	    {
		mask = StringUtils.rightPad("", formattedValue.length(), IsoDefinitionConstants.MASK_HIDE_BIT);
	    }

	    // if no mask applied
	    if(!mask.isEmpty())
		formattedValue = IsoUtil.maskField(formattedValue, mask);

	    // build fields
	    if(field.getType() == IsoType.LVAR)
	    {
		sb.append(String.format("%01d", formattedValue.length()));
	    }
	    else if(field.getType() == IsoType.LLBIN || field.getType() == IsoType.LLVAR)
	    {
		sb.append(String.format("%02d", formattedValue.length()));
	    }
	    else if(field.getType() == IsoType.LLLBIN || field.getType() == IsoType.LLLVAR)
	    {
		sb.append(String.format("%03d", formattedValue.length()));
	    }
	    else if(field.getType() == IsoType.LLLLBIN || field.getType() == IsoType.LLLLVAR)
	    {
		sb.append(String.format("%04d", formattedValue.length()));
	    }
	    else if(field.getType() == IsoType.LLLLLVAR)
	    {
		sb.append(String.format("%05d", formattedValue.length()));
	    }

	    // append field info
	    sb.append(formattedValue);
	}

	if(!onlyPayload)
	{
	    // Add protocol identification and message size
	    String protocolIdentification = getFactory().getProtocolIdentification();

	    String format = "%s%0".concat(String.valueOf(getFactory().getLenFieldLength()))
		    .concat("d");
	    sb.insert(0, String.format(format, protocolIdentification, sb.length()));
	}

	return sb.toString();
    }

    /**
     * Returns a Associative array representation of the message
     * 
     * @return string
     * @throws IsoMessageException
     */
    public String toString()
    {

	// @todo fix this
	if(null == getFactory())
	    throw new IsoRuntimeException(EngineError.ISOMSG_FORMAT_FAILED);

	JSONObject jsonObject = new JSONObject();
	jsonObject.accumulateAll(getFactory().convertToHashMap(this));

	return jsonObject.toString();
    }

    /**
     * Creates and returns a ByteBuffer with the data of the message, including
     * the length header. The returned buffer is already flipped, so it is ready
     * to be written to a Channel.
     *
     */
    public ByteBuffer writeToBuffer(int lengthBytes)
    {

	// validate length bytes
	if(lengthBytes > 4)
	    throw new IllegalArgumentException("The length header can have at most 4 bytes");

	byte[] data = writeData();

	String protocolIdentification = getFactory().getProtocolIdentification();

	ByteBuffer buf = ByteBuffer
		.allocate(protocolIdentification.length() + lengthBytes + data.length + (getEtx() > -1 ? 1 : 0));

	// Add protocol identification
	if(!protocolIdentification.isEmpty())
	{
	    buf.put(protocolIdentification.getBytes(AtmEngineConstants.CHAR_ENCODING));
	}

	// calculate length
	byte[] lengthValue = calculateLength(data, lengthBytes);

	if(null != lengthValue)
	    buf.put(lengthValue);

	// push data to the byte buffer
	buf.put(data);

	// ETX
	if(getEtx() > -1)
	{
	    buf.put((byte) getEtx());
	}
	buf.flip();
	return buf;
    }

    /**
     * Return header length
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public int returnHeaderLength()
    {

	if(getIsoHeader() != null)
	    try
	    {
		return getIsoHeader().getBytes(getCharacterEncoding()).length;
	    }
	    catch(UnsupportedEncodingException e)
	    {

		// @todo Throw runtime exception
	    }

	if(getBinaryIsoHeader() != null)
	    return getBinaryIsoHeader().length;

	return 0;
    }

    /**
     * Calculate Message length based on the 
     * 1- Length type ( Base 10 / 256 ) 
     * 2- Include length flag 
     * 3- Include header flag ( always yes )
     * 
     * @param data
     * @param lengthBytes
     * @return
     */
    private byte[] calculateLength(byte[] data, int lengthBytes)
    {
	if(lengthBytes <= 0)
	    return null;

	byte[] lengthByteArray = null;
	// include header length
	int l = getFactory().isIncludeHeader()? data.length - returnHeaderLength() : data.length;

	// include Length field length to length
	l = getFactory().isIncludeLenFieldLength() ? l : l + lengthBytes;

	if(getEtx() > -1)
	    l++;

	if(getFactory().isUseAsciiEncoding())
	{
	    String asciiLength = IsoUtil.convertLenToAscii(l, lengthBytes);
	    lengthByteArray = asciiLength.getBytes(AtmEngineConstants.CHAR_ENCODING);

	}
	else if(getFactory().getLengthType().equals(AtmEngineConstants.LENGTH_BASE10))
	{

	    String decimalLength = StringUtils.leftPad(String.valueOf(l), lengthBytes, ConstantsCommon.ZERO);
	    lengthByteArray = decimalLength.getBytes(AtmEngineConstants.CHAR_ENCODING);

	}
	else
	{

	    ArrayList<Byte> lengthBytesList = new ArrayList<Byte>();
	    lengthByteArray = new byte[lengthBytes];

	    // @todo add the prepender check at this level
	    if(lengthBytes == 4)
		lengthBytesList.add((byte) ((l & 0xff000000) >> 24));

	    if(lengthBytes > 2)
		lengthBytesList.add((byte) ((l & 0xff0000) >> 16));

	    if(lengthBytes > 1)
		lengthBytesList.add((byte) ((l & 0xff00) >> 8));

	    lengthBytesList.add((byte) (l & 0xff));

	    lengthByteArray = (byte[]) ArrayUtils.toPrimitive(lengthBytesList.toArray());
	}

	return lengthByteArray;
    }

    /**
     * @param fieldCode
     * @return
     */
    private String returnFieldValueStr(short fieldCode)
    {
	// if ISO Message doesn't have an authorization code field
	if(!hasField(fieldCode))
	    return null;

	IsoValue<?> isoField = getField(fieldCode);

	// if it's a composite field we will use another toString
	if(null == isoField || null == isoField.getValue())
	{
	    return null;
	}

	if(!(isoField.getValue() instanceof CompositeField))
	{
	    return isoField.getValue().toString();
	}

	/**
	 * we assume that we have only one nested composite field per iso field
	 * else it's should be recursive.
	 */
	CompositeField isoComposite = (CompositeField) isoField.getValue();
	StringBuilder sb = new StringBuilder();
	for(IsoValue<?> value : isoComposite.getValues())
	{
	    sb.append(value.toString());
	}

	return sb.toString();

    }

    /**
     * @return the id
     */
    public BigDecimal getId()
    {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(BigDecimal id)
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
     * @return the messageBytes
     */
    public byte[] getMessageBytes()
    {
	return messageBytes;
    }

    public void setMessageBytes(byte[] messageBytes)
    {
	this.messageBytes = messageBytes;
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
     * @return the errorMessage
     */
    public String getErrorMessage()
    {
	return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage)
    {
	this.errorMessage = errorMessage;
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
     * @return the endingTime
     */
    public Date getEndingTime()
    {
	return endingTime;
    }

    /**
     * @param endingTime the endingTime to set
     */
    public void setEndingTime(Date endingTime)
    {
	this.endingTime = endingTime;
    }

    /**
     * @return the acquirerId
     */
    public BigDecimal getAcquirerId()
    {
	return acquirerId;
    }

    /**
     * @param acquirerId the acquirerId to set
     */
    public void setAcquirerId(BigDecimal acquirerId)
    {
	this.acquirerId = acquirerId;
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
     * @return the trxBranchCode
     */
    public BigDecimal getTrxBranchCode()
    {
	return trxBranchCode;
    }

    /**
     * @param trxBranchCode the trxBranchCode to set
     */
    public void setTrxBranchCode(BigDecimal trxBranchCode)
    {
	this.trxBranchCode = trxBranchCode;
    }

    /**
     * @return the tellerUserId
     */
    public String getTellerUserId()
    {
	return tellerUserId;
    }

    /**
     * @param tellerUserId the tellerUserId to set
     */
    public void setTellerUserId(String tellerUserId)
    {
	this.tellerUserId = tellerUserId;
    }

    /**
     * @return the tellerId
     */
    public BigDecimal getTellerId()
    {
	return tellerId;
    }

    /**
     * @param tellerId the tellerId to set
     */
    public void setTellerId(BigDecimal tellerId)
    {
	this.tellerId = tellerId;
    }

    /**
     * @return the trxType
     */
    public BigDecimal getTrxType()
    {
	return trxType;
    }

    /**
     * @param trxType the trxType to set
     */
    public void setTrxType(BigDecimal trxType)
    {
	this.trxType = trxType;
    }

    /**
     * @return the chargeTrxType
     */
    public BigDecimal getChargeTrxType()
    {
	return chargeTrxType;
    }

    /**
     * @param chargeTrxType the chargeTrxType to set
     */
    public void setChargeTrxType(BigDecimal chargeTrxType)
    {
	this.chargeTrxType = chargeTrxType;
    }

    /**
     * @return the merchantCode
     */
    public BigDecimal getMerchantCode()
    {
	return merchantCode;
    }

    /**
     * @param merchantCode the merchantCode to set
     */
    public void setMerchantCode(BigDecimal merchantCode)
    {
	this.merchantCode = merchantCode;
    }

    /**
     * @return the merchAdditionalRef
     */
    public String getMerchAdditionalRef()
    {
	return merchAdditionalRef;
    }

    /**
     * @param merchAdditionalRef the merchAdditionalRef to set
     */
    public void setMerchAdditionalRef(String merchAdditionalRef)
    {
	this.merchAdditionalRef = merchAdditionalRef;
    }

    /**
     * @return the merchIbanAccNo
     */
    public String getMerchIbanAccNo()
    {
	return merchIbanAccNo;
    }

    /**
     * @param merchIbanAccNo the merchIbanAccNo to set
     */
    public void setMerchIbanAccNo(String merchIbanAccNo)
    {
	this.merchIbanAccNo = merchIbanAccNo;
    }

    /**
     * @return the terminalCode
     */
    public String getTerminalCode()
    {
	return terminalCode;
    }

    /**
     * @param terminalCode the terminalCode to set
     */
    public void setTerminalCode(String terminalCode)
    {
	this.terminalCode = terminalCode;
    }

    /**
     * @return the terminalDesc
     */
    public String getTerminalDesc()
    {
	return terminalDesc;
    }

    /**
     * @param terminalDesc the terminalDesc to set
     */
    public void setTerminalDesc(String terminalDesc)
    {
	this.terminalDesc = terminalDesc;
    }

    /**
     * @return the coreErrorDesc
     */
    public String getCoreErrorDesc()
    {
	return coreErrorDesc;
    }

    /**
     * @param coreErrorDesc the coreErrorDesc to set
     */
    public void setCoreErrorDesc(String coreErrorDesc)
    {
	this.coreErrorDesc = coreErrorDesc;
    }

    /**
     * @return the trxNb
     */
    public BigDecimal getTrxNb()
    {
	return trxNb;
    }

    /**
     * @param trxNb the trxNb to set
     */
    public void setTrxNb(BigDecimal trxNb)
    {
	this.trxNb = trxNb;
    }

    /**
     * @return the factory
     */
    public MessageFactory getFactory()
    {
	return factory;
    }

    /**
     * @param factory the factory to set
     */
    public void setFactory(MessageFactory factory)
    {
	this.factory = factory;
    }

    /**
     * @todo temporary until we patch the iso8583 jar
     */
    public int getEtx()
    {
	return -1;
    }
}
