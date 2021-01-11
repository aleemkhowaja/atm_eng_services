package com.path.atm.engine.iso8583;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

import com.path.atm.bo.engine.AtmEngineConfigBO;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.exception.IsoParseException;
import com.path.atm.engine.generator.BoundedSeqGenerator;
import com.path.atm.engine.iso8583.messages.AtmIsoReqMsgDraft;
import com.path.atm.engine.iso8583.messages.AtmIsoRespMsgDraft;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.config.AtmInterfaceConfigSC;
import com.path.atm.vo.engine.AcquirerCO;
import com.path.atm.vo.engine.AtmInterfaceCO;
import com.path.atm.vo.engine.AtmIsoFieldCO;
import com.path.atm.vo.engine.AtmIsoMessageCO;
import com.path.atm.vo.engine.AtmIsoMessageDefCO;
import com.path.atm.vo.engine.AtmIsoNetMsgFldsCO;
import com.path.atm.vo.engine.AtmIsoResponseMapCO;
import com.path.atm.vo.engine.TerminalCO;
import com.path.atm.vo.engine.TransactionTypeCO;
import com.path.bo.common.ConstantsCommon;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;
import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.codecs.CompositeField;
import com.solab.iso8583.parse.FieldParseInfo;

/**
 * This class will serve as a wrapper for the 3d party Message factory.
 * 
 * @author MohammadAliMezzawi
 * http://j8583.sourceforge.net/encoders.html
 * https://libraries.io/github/Tochemey/Iso85834Net
 */
public class MessageFactory extends com.solab.iso8583.MessageFactory<AtmIsoMessage>
{
    
    /**
     * Hold reference to the interface CO
     * <p>This property will never be exposed to public and any property needed from
     * it should be retrieved indirectly using a method in the factory.
     * This approach will allow us later on to make the message factory self contained.
     * Below is the list of property retrieved indirectly from this object
     * 1- Interface code : the interface code to which belong this factory
     * 2- Length type : Base 256/ Base 10
     * 3- includeHeader: include the header length in the length field ( always true ).
     * 4- includeLength: include length field length in the length value.
     * 5- ProtocolIdentification : the protocol identification value.
     * 6- Length of "FieldLength" : the length of the field that contain the length value
     * 7- Header length : the length of the header
     * 8- List of Iso fields definition 
     */
    private final AtmInterfaceCO interfaceCO;

    /**
     * Hold Auth code generator
     */
    private BoundedSeqGenerator authCodeGenerator;

    /**
     * Hold atm interface response code map
     */
    private HashMap<String, HashMap<String, String>> responseCodeMap;

    /**
     * Flag to specify if we should use base 256 to encode length
     */
    private boolean useAsciiEncoding;

    /**
     * Create a message factory
     */
    public MessageFactory(AtmInterfaceCO interfaceCO)
    {
	this.interfaceCO = interfaceCO;
    }

    /**
     * @todo fix the global variables
     * 
     *       Convert HashMap to ISO message
     * @param isoMsgHm
     * @return
     */
    public AtmIsoMessage convertToIsoMsg(HashMap<String, Object> isoMsgHm)
    {

	if(null == isoMsgHm || isoMsgHm.get("type") == null)
	    throw new IllegalArgumentException("Type is mandatory");

	AtmIsoMessage isoMessage = new AtmIsoMessage();
	isoMessage.setType((int) isoMsgHm.get("type"));
	isoMessage.setCharacterEncoding(getCharacterEncoding());

	try
	{
	    PathPropertyUtil.copyProperties(isoMsgHm, isoMessage);
	}
	catch(Exception e)
	{
	    // @todo implement exception handling
	    e.printStackTrace();
	}

	// get the template of this type
	IsoMessage isoMsgTemplate = getMessageTemplate(isoMessage.getType());

	if(null == isoMsgTemplate)
	    throw new IllegalConfigurationException(EngineError.EX_CONF_WRG_PRIORITY);

	for(int i = 2; i <= 128; i++)
	{

	    // skip non existence values
	    if(!isoMsgTemplate.hasField(i))
		continue;

	    IsoValue<?> isoTemplField = isoMsgTemplate.getAt(i);
	    IsoValue<?> isoValue = createIsoFieldByTemplate(isoTemplField, isoMsgHm, String.valueOf(i), true);

	    if(isoValue != null)
		isoValue.setCharacterEncoding(getCharacterEncoding());

	    isoMessage.setField(i, isoValue);

	}

	return isoMessage;
    }

    /**
     * Create iso field
     * 
     * @param isoTemplField
     * @param index
     * @param topLevel
     * @return
     */
    private IsoValue<?> createIsoFieldByTemplate(IsoValue<?> isoTemplField, HashMap<String, Object> values,
	    String index, boolean topLevel)
    {

	// check if composite field
	if(isoTemplField.getValue() instanceof CompositeField)
	{

	    CompositeField isoTemplCompField = (CompositeField) isoTemplField.getValue();
	    CompositeField newCompfield = new CompositeField();

	    int subIndexInc = 1;

	    for(IsoValue<?> subTemplField : isoTemplCompField.getValues())
	    {

		// build the sub field key
		String subIndex = index.concat(IsoDefinitionConstants.ISO_HM_SUBFIELD_SEPARATOR)
			.concat(String.valueOf(subIndexInc));

		// create iso field
		IsoValue<?> sv = createIsoFieldByTemplate(subTemplField, values, subIndex, false);

		// set sub field encoding
		if(sv != null)
		{
		    sv.setCharacterEncoding(getCharacterEncoding());
		    newCompfield.addValue(sv);
		}

		subIndexInc++;
	    }

	    /**
	     * If Composite field doesn't have any subfields we will consider it
	     * as null
	     */
	    if(null == newCompfield.getValues())
		return null;

	    // composite field
	    return isoTemplField.getType().needsLength()
		    ? new IsoValue<>(isoTemplField.getType(), newCompfield, isoTemplField.getLength(), newCompfield)
		    : new IsoValue<>(isoTemplField.getType(), newCompfield, newCompfield);
	}

	// has encoder, index is the field number
	final CustomField<Object> cf = topLevel ? getCustomField(Integer.valueOf(index)) : null;

	/**
	 * @note @todo : we should check about the value type should we convert
	 *       it to string , do we need to use the parser?? does the parse do
	 *       so ?? or it's the encoder this is a very important point to
	 *       check
	 */
	Object value = values.get(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(index));

	/**
	 * We may reuse parser if needed but the issue for now is that Amount is
	 * a BigDecimal and if we send an empty string we will get a number
	 * format exception Binary will have the same issue also.
	 * 
	 * Seems that create field from template using new contain an error
	 */
	// set default value empty
	if(value == null)
	    return null;

	// subfield or Simple field without encoder
	if(cf == null)
	    return isoTemplField.getType().needsLength()
		    ? new IsoValue<>(isoTemplField.getType(), value, isoTemplField.getLength())
		    : new IsoValue<>(isoTemplField.getType(), value);

	// subfield or Simple field with encoder
	return isoTemplField.getType().needsLength()
		? new IsoValue<>(isoTemplField.getType(), value, isoTemplField.getLength(), cf)
		: new IsoValue<>(isoTemplField.getType(), value, cf);

    }

    /**
     * Convert AtmIsoMessage to hashMap
     * @todo we may move this later on to the Atm Iso message 
     * but the concern was that we want the iso message to be light as possible
     * but later on we have added several methods so we need to revisit this method also.
     * @param isoMessage
     * @return
     */
    public HashMap<String, Object> convertToHashMap(AtmIsoMessage isoMessage)
    {
	HashMap<String, Object> isoMsgHm = new HashMap<>();

	// @todo @note : if we promote fields to protected or public
	// we should make sure to skip it from convertToMap
	isoMsgHm = extractIsoMsgObjFields(isoMessage);

	// set the iso MTI code
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_MTICODE, IsoUtil.convertMtiToHex((int) isoMsgHm.get("type")));

	// populate the basic fields
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_COMPCODE, isoMessage.getCompCode());
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_COMPNAME, isoMessage.getCompBriefName());

	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_BRCODE, isoMessage.getTrxBranchCode());
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_TELLCODE, isoMessage.getTellerId());
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_TELLERUSER, isoMessage.getTellerUserId());

	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_TRXCODE, isoMessage.getTrxType());
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_CHARGETRXCODE, isoMessage.getChargeTrxType());

	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_MERACC, isoMessage.getMerchAdditionalRef());
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_TERMINAL, isoMessage.getTerminalCode());
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_TERMDESC, isoMessage.getTerminalDesc());

	// @todo fix this
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_RESP_RESULT, isoMessage.getTerminalCode());

	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_INTERFACE_CODE, isoMessage.getInterfaceCode());
	isoMsgHm.put(IsoDefinitionConstants.ISO_HEADER, isoMessage.getIsoHeader());

	// get iso Fields
	HashMap<String, Object> isoFieldsHm = extractIsoFieldsHm(isoMessage);

	// set auth code
	String authCodeKey = IsoDefinitionConstants.ISO_HM_FIELD_PREFIX
		.concat(String.valueOf(IsoDefinitionConstants.BIT_APPROVAL_CODE));
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_AUTHCODE, isoFieldsHm.get(authCodeKey));
	isoMsgHm.putAll(isoFieldsHm);
	
	return isoMsgHm;
    }

    
    /**
     * Evaluate the expression set at the field level in the interface
     * configuration.
     * 
     * @todo later on we should refactor the structure of the atm message co
     *       that are stored under the interface co to be something like the
     *       below : 1- AtmInterfaceCO: A - IsoMessages [ 200 => [ 'responseMTI'
     *       => 210, 'fields' => [ 32 => AtmIsoFields , 34 => AtmIsoFields] ] ]
     * @param requestMessage
     * @return
     * @throws BaseException
     */
    public HashMap<String, Object> evaluateFieldExpression(HashMap<String, Object> requestMessage) throws BaseException
    {

	// return ISO fields
	HashMap<BigDecimal, AtmIsoFieldCO> isofieldsList = interfaceCO.getIsoFieldList();
	Iterator<Entry<BigDecimal, AtmIsoFieldCO>> isoFieldIt = isofieldsList.entrySet().iterator();

	HashMap<String, Object> newEvaluatedMessage = new HashMap();
	// @note : we may use for example List<Integer> index =
	// parseOrder.get(isoMessage.getType());

	while(isoFieldIt.hasNext())
	{
	    Entry<BigDecimal, AtmIsoFieldCO> atmIsoFieldEntry = isoFieldIt.next();
	    AtmIsoFieldCO fieldCO = atmIsoFieldEntry.getValue();

	    String parentkey = IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(fieldCO.getCode()));

	    // Evaluate ISO Fields
	    if(StringUtil.isNotEmpty(StringUtil.nullToEmpty(fieldCO.getExpression()).trim())
		    && requestMessage.containsKey(parentkey))
		newEvaluatedMessage.put(parentkey, IsoUtil.evaluateExpression(fieldCO.getExpression(), requestMessage));

	    // no sub fields => skip to the next field
	    if(null == fieldCO.getIsoFieldSubList() || fieldCO.getIsoFieldSubList().isEmpty())
		continue;

	    // evaluate the sub fields expressions
	    for(AtmIsoFieldCO atmSubIsoFieldCO : fieldCO.getIsoFieldSubList())
	    {

		String subFieldKey = parentkey.concat(IsoDefinitionConstants.ISO_HM_SUBFIELD_SEPARATOR)
			.concat(String.valueOf(atmSubIsoFieldCO.getIndex()));

		if(StringUtil.isNotEmpty(StringUtil.nullToEmpty(atmSubIsoFieldCO.getExpression()).trim())
			&& requestMessage.containsKey(subFieldKey))
		    newEvaluatedMessage.put(subFieldKey,
			    IsoUtil.evaluateExpression(atmSubIsoFieldCO.getExpression(), requestMessage));
	    }
	}

	// merge the two hashMap and override the old value with the new
	// evaluated one
	requestMessage.putAll(newEvaluatedMessage);

	return requestMessage;

    }

    /**
     * This method will evaluate the expression related to a network field using
     * the provided data.
     * 
     * @param messageData
     * @param netFields
     * @return
     * @throws BaseException
     */
    public HashMap<String, Object> evaluateNetWorkExpression(HashMap<String, Object> messageData,
	    List<AtmIsoNetMsgFldsCO> netFields) throws BaseException
    {
	HashMap<String, Object> results = new HashMap<String, Object>();

	for(AtmIsoNetMsgFldsCO field : netFields)
	{
	    if(StringUtil.isEmptyString(field.getExpression()))
	    {
		results.put(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(field.getFieldCode())),
			messageData.get(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX
				.concat(String.valueOf(field.getFieldCode()))));
	    }
	    else
	    {
		results.put(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(field.getFieldCode())),
			IsoUtil.evaluateExpression(field.getExpression(), messageData));
	    }
	}

	return results;
    }

    
    
    /**
     * This method will extract the Atm iso message instance fields.
     * Direct access to the properties of an object is much more faster than
     * using reflection and checking properties accessibility.
     * 
     * It's a replacement of PathPropertyUtil.convertToMap since it's costly.
     * @param isoMessage
     * @return
     */
    public HashMap<String, Object> extractIsoMsgObjFields(AtmIsoMessage isoMessage)
    {
	HashMap<String, Object> isoObjFieldsHm = new HashMap<>();
	isoObjFieldsHm.put("id", isoMessage.getId());
	isoObjFieldsHm.put("type", isoMessage.getType());
	isoObjFieldsHm.put("interfaceCode", isoMessage.getInterfaceCode());
	isoObjFieldsHm.put("acquirerId", isoMessage.getAcquirerId());
	isoObjFieldsHm.put("compCode", isoMessage.getCompCode());
	isoObjFieldsHm.put("compBriefName", isoMessage.getCompBriefName());
	isoObjFieldsHm.put("trxBranchCode", isoMessage.getTrxBranchCode());
	isoObjFieldsHm.put("tellerId", isoMessage.getTellerId());
	isoObjFieldsHm.put("tellerUserId", isoMessage.getTellerUserId());
	isoObjFieldsHm.put("trxType", isoMessage.getTrxType());
	isoObjFieldsHm.put("trxNb", isoMessage.getTrxNb());
	isoObjFieldsHm.put("chargeTrxType", isoMessage.getChargeTrxType());
	isoObjFieldsHm.put("terminalCode", isoMessage.getTerminalCode());
	isoObjFieldsHm.put("terminalDesc", isoMessage.getTerminalDesc());
	isoObjFieldsHm.put("merchantCode", isoMessage.getMerchantCode());
	isoObjFieldsHm.put("merchAdditionalRef", isoMessage.getMerchAdditionalRef());
	isoObjFieldsHm.put("merchIbanAccNo", isoMessage.getMerchIbanAccNo());
	isoObjFieldsHm.put("coreErrorDesc", isoMessage.getCoreErrorDesc());
	
	isoObjFieldsHm.put("binary", isoMessage.isBinary());
	isoObjFieldsHm.put("binaryBitmap", isoMessage.isBinaryBitmap());
	isoObjFieldsHm.put("forceSecondaryBitmap", isoMessage.getForceSecondaryBitmap());
	isoObjFieldsHm.put("characterEncoding", isoMessage.getCharacterEncoding());
	isoObjFieldsHm.put("binaryHeader", isoMessage.isBinaryHeader());
	isoObjFieldsHm.put("binaryFields", isoMessage.isBinaryFields());
	isoObjFieldsHm.put("etx", isoMessage.getEtx());
	isoObjFieldsHm.put("encodeVariableLengthFieldsInHex", isoMessage.isEncodeVariableLengthFieldsInHex());
	
	return isoObjFieldsHm;
    }
    
    /**
     * This method extract IsoFields from Atm Iso Message as HashMap
     * 
     * @todo include parameters to control the iso null include
     * @return
     */
    public HashMap<String, Object> extractIsoFieldsHm(AtmIsoMessage isoMessage)
    {

	HashMap<String, Object> isoFieldsHm = new HashMap<>();
	// get the parse template for this mti
	Map<Integer, FieldParseInfo> parseGuide = parseMap.get(isoMessage.getType());
	List<Integer> index = parseOrder.get(isoMessage.getType());

	// @todo handle case where type doesn't exist
	for(Integer i : index)
	{
	    FieldParseInfo fpi = parseGuide.get(i);
	    IsoValue<?> isoField = isoMessage.getField(i);
	    
	    
	    // we should log a warning here
	    if(!isoMessage.hasField(i))
	    {
		/**
		 * extract composite fields
		 */
		if(fpi.getDecoder() instanceof CompositeField)
		{
		    CompositeField decoder = (CompositeField) fpi.getDecoder();
		    
		    int subFieldsCount = decoder.getParsers().size();
		    for(int subId = 1;subId <= subFieldsCount; subId ++)
		    {
			String key = IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(i))
				.concat(IsoDefinitionConstants.ISO_HM_SUBFIELD_SEPARATOR)
				.concat(String.valueOf(subId));
			isoFieldsHm.put(key, null);
		    }
		}

		//@todo replace with IsoDefinitionConstants.getFields().get(i);
		isoFieldsHm.put(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(i)), null);
		continue;
	    }

	    /**
	     * extract composite fields
	     */
	    if(isoField.getValue() instanceof CompositeField)
	    {
		CompositeField field = (CompositeField) isoField.getValue();
		
		int subFieldId = 1;
		for(IsoValue<?> v : field.getValues())
		{
		    String key = IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(i))
			    .concat(IsoDefinitionConstants.ISO_HM_SUBFIELD_SEPARATOR)
			    .concat(String.valueOf(subFieldId));
		    isoFieldsHm.put(key, v.getValue());
		    subFieldId++;
		}

		// @todo check for the date,since we are handling it as string
		String parentkey = IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(i));
		isoFieldsHm.put(parentkey, field.encodeField(field));

		continue;
	    }

	    isoFieldsHm.put(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(i)), isoField.getValue());
	}

	return isoFieldsHm;
    }

    /**
     * Return the iso msg definition specific to the given Atm iso message
     * 
     * @param isoMessage
     * @return
     * @throws BaseException
     */
    public AtmIsoMessageDefCO returnIsoMsgDef(AtmIsoMessage isoMessage) throws BaseException
    {

	HashMap<String, Object> isoMsgHm = convertToHashMap(isoMessage);
	IsoValue<?> isoProcessCodeField = isoMessage.getField(IsoDefinitionConstants.BIT_PROCESSING_CODE);
	/**
	 * CompositeField processCode = (CompositeField) isoMessage
	 * .getField(IsoDefinitionConstants.BIT_PROCESSING_CODE).getValue();
	 */

	// process code
	String processCodeVal = null;

	// @todo what if the process code isn't a composite field ???
	if(null != isoProcessCodeField && isoProcessCodeField.getValue() instanceof CompositeField)
	{
	    CompositeField field = (CompositeField) isoProcessCodeField.getValue();
	    // @todo add this as property
	    processCodeVal = (String) field.getValues().get(0).getValue();
	}

	int level = null != processCodeVal ? 1 : 2;
	return matchIsoMsgDef(isoMsgHm, processCodeVal, level);
    }

    /**
     * Return the message type of the given array of bytes
     * 
     * @param messagBytes
     * @return
     * @throws UnsupportedEncodingException
     * @throws NumberFormatException
     */
    public int returnMessageType(byte[] messagBytes, int mtiOffset)
	    throws NumberFormatException, UnsupportedEncodingException
    {
	int type = 0x0;
	if(isBinaryHeader())
	{
	    type = ((messagBytes[mtiOffset] & 0xff) << 8) | (messagBytes[mtiOffset + 1] & 0xff);
	}
	else if(isForceStringEncoding())
	{
	    type = Integer.parseInt(new String(messagBytes, mtiOffset, 4, getCharacterEncoding()), 16);
	}
	else
	{
	    type = ((messagBytes[mtiOffset] - 48) << 12) | ((messagBytes[mtiOffset + 1] - 48) << 8)
		    | ((messagBytes[mtiOffset + 2] - 48) << 4) | (messagBytes[mtiOffset + 3] - 48);
	}

	return type;
    }

    /**
     * Apply expression to the iso message definition
     * 
     * @note : this method will be merged with the above when the iso message
     *       convert to hashmap is set internally
     * @param isoMsgHm
     * @param processCodeVal
     * @param level
     * @return
     * @throws BaseException
     */
    private AtmIsoMessageDefCO matchIsoMsgDef(HashMap<String, Object> isoMsgHm, String processCodeVal, int level)
	    throws BaseException
    {

	HashMap<String, List<AtmIsoMessageDefCO>> defsList = interfaceCO.getIsoMessageDefMap();
	// 0200
	String mtiCodeStr = (String) isoMsgHm.get("MTICODE");
	String defkey = null;
	AtmIsoMessageDefCO definitionCO = null;

	// set the matching
	switch (level)
	{
	    case 1:
		defkey = IsoUtil.returnIsoMsgDefKey(mtiCodeStr, processCodeVal);
		break;
	    case 2:
		defkey = IsoUtil.returnIsoMsgDefKey(mtiCodeStr, null);
		break;
	    case 3:
		defkey = IsoUtil.returnIsoMsgDefKey(null, processCodeVal);
		break;
	}

	// check if definition is defined
	if(null != defsList.get(defkey) && !defsList.get(defkey).isEmpty())
	{
	    for(AtmIsoMessageDefCO defCo : defsList.get(defkey))
	    {

		if(StringUtil.isEmptyString(defCo.getCriteriaExpression())
			|| (Boolean) IsoUtil.evaluateExpression(defCo.getCriteriaExpression(), isoMsgHm))
		{
		    definitionCO = defCo;
		    break;
		}
	    }
	}

	// if a match found return it
	if(null != definitionCO)
	    return definitionCO;

	// check next level
	level++;

	return level > 3 ? null : matchIsoMsgDef(isoMsgHm, processCodeVal, level);
    }

    /**
     * Creates a message to respond to a request. Increments the message type by
     * 16, sets all fields from the template if there is one, and copies all
     * values from the request, overwriting fields from the template if they
     * overlap.
     * 
     * @param request An ISO8583 message with a request type (ending in 00).
     */

    /**
     * Create an Atm iso response message
     * 
     * <p>
     * while creating and populating the fields the response value is
     * prioritized over the request value which will be overwitten.
     * 
     * @param request
     * @param responseData
     * @return
     * @throws IsoMessageException
     */
    public AtmIsoMessage createResponse(int requestType, HashMap<String, Object> messageData) throws IsoMessageException
    {
	return createResponse(requestType, messageData, null);
    }

    /**
     * Create an Atm iso response message
     * 
     * <p>
     * while creating and populating the fields the response value is
     * prioritized over the request value which will be overwritten.
     * 
     * @param request
     * @param responseData
     * @param includedFields
     * @approved
     * @return
     */
    public AtmIsoMessage createResponse(int requestType, HashMap<String, Object> messageData,
	    ArrayList<Integer> includedFields) throws IsoMessageException
    {

	try
	{
	    AtmIsoMessageCO msgCO = interfaceCO.getIsoMsgList().get(IsoUtil.convertMtiToHex(requestType));

	    // @todo fix this
	    if(null == msgCO)
		throw new RuntimeException("msg not defined for " + requestType);

	    // get the response mti
	    if(StringUtil.nullToEmpty(msgCO.getResponseMti()).isEmpty())
		throw new RuntimeException("msg response not defined for " + requestType);

	    // 0210
	    int responseType = IsoUtil.convertMtiToInt(returnResponseMti(msgCO, messageData));

	    // Create iso message
	    AtmIsoMessage atmIsoMsg = createMessage(responseType, messageData, includedFields);

	    // separate this function from createMessage to keep the create
	    // simple and iso related
	    // populateAdditionanlInfo(atmIsoMsg,request);

	    return atmIsoMsg;

	}
	catch(Exception e)
	{
	    throw new IsoMessageException(EngineError.ISOMSG_RESPONSE_CREATION_FAILED, e);
	}
    }

    /**
     * Create Iso Message based on the given type and populate it using the the
     * supplied message data.
     * 
     * <p>
     * For network messages the fields existence can be overridden in the
     * message definitions which make the ISO message template defined in the
     * configuration invalid for that we are including the new parameter
     * includedFields
     * 
     * @param type
     * @param messageData
     * @param includedFields
     * @approved
     * @return
     */
    public AtmIsoMessage createMessage(int type, HashMap<String, Object> messageData, ArrayList<Integer> includedFields)
    {

	/**
	 * Return the same header unless we have an ISO Header for this MTI
	 */
	String isoHeader = null != getIsoHeader(type) ? getIsoHeader(type)
		: (String) messageData.get(IsoDefinitionConstants.ISO_HEADER);

	// create an empty response
	AtmIsoMessage isoMsg = createIsoMessage(isoHeader);

	isoMsg.setFactory(this);
	isoMsg.setType(type);
	isoMsg.setCharacterEncoding(getCharacterEncoding());
	isoMsg.setBinary(getUseBinaryMessages());
	isoMsg.setForceSecondaryBitmap(isForceSecondaryBitmap());
	// @todo check if it's needed
	// m.setForceStringEncoding(forceStringEncoding);
	isoMsg.setBinaryBitmap(isUseBinaryBitmap());
	isoMsg.setEtx(getEtx());

	// Copy the values from the template or the request (request has
	// preference)
	IsoMessage isoMsgTemplate = getMessageTemplate(type);

	// build message
	for(int i = 2; i < 128; i++)
	{

	    if(!isoMsgTemplate.hasField(i) || (null != includedFields && !includedFields.contains(i)))
		continue;

	    IsoValue<?> isoTemplField = isoMsgTemplate.getAt(i);
	    IsoValue<?> isoValue = createIsoFieldByTemplate(isoTemplField, messageData, String.valueOf(i), true);

	    if(isoValue != null)
		isoValue.setCharacterEncoding(getCharacterEncoding());

	    isoMsg.setField(i, isoValue);
	}

	return isoMsg;
    }

    /**
     * Creates a message to respond to a request. Increments the message type by
     * 16, sets all fields from the template if there is one, and copies all
     * values from the request, overwriting fields from the template if they
     * overlap.
     * 
     * @param request An ISO8583 message with a request type (ending in 00).
     */

    /**
     * Create an Atm ISO Network message REQUEST
     * 
     * @param request
     * @param responseData
     * @approved
     * @return
     */
    public AtmIsoMessage createNetWorkMsg(String netType, HashMap<String, Object> messageData)
	    throws IsoMessageException
    {

	try
	{

	    // get message
	    AtmIsoMessageDefCO msgDefCO = interfaceCO.getIsoNetMessageDefMap().get(netType);

	    if(null == msgDefCO || null == msgDefCO.getNetReqFields())
		throw new RuntimeException("Msg Definition/Fields not defined for network msg: " + netType);

	    // get the mti
	    int netMti = IsoUtil.convertMtiToInt(msgDefCO.getReqMTI());

	    // now evaluate the network expression
	    // @todo we may need to send all fields as empty ( merge with the
	    // empty hashMap )
	    messageData = evaluateNetWorkExpression(messageData, msgDefCO.getNetReqFields());

	    // get list of available fields
	    ArrayList<Integer> fieldsList = new ArrayList<>();

	    // @todo we may cache this also
	    for(AtmIsoNetMsgFldsCO netFieldCO : msgDefCO.getNetReqFields())
	    {
		fieldsList.add(netFieldCO.getFieldCode().intValue());
	    }

	    return createMessage(netMti, messageData, fieldsList);

	}
	catch(Exception e)
	{
	    throw new IsoMessageException(EngineError.ISOMSG_REQUEST_CREATION_FAILED, e);
	}
    }

    /**
     * Create an Atm ISO Network message REQUEST
     * 
     * @param request
     * @param responseData
     * @return
     */
    public AtmIsoMessage createNetWorkRespMsg(String netType, HashMap<String, Object> messageData)
	    throws IsoMessageException
    {

	try
	{

	    // get message
	    AtmIsoMessageDefCO msgDefCO = interfaceCO.getIsoNetMessageDefMap().get(netType);

	    if(null == msgDefCO)
		throw new RuntimeException("msg Definition not defined for network msg: " + netType);

	    // get the mti
	    int netMti = IsoUtil.convertMtiToInt(msgDefCO.getReqMTI());

	    // now evaluate the network expression
	    // @todo we may need to send all fields as empty ( merge with the
	    // empty hashMap )
	    messageData = evaluateNetWorkExpression(messageData, msgDefCO.getNetRespFields());

	    // get list of available fields
	    ArrayList<Integer> fieldsList = new ArrayList<>();

	    // @todo we may cache this also
	    for(AtmIsoNetMsgFldsCO netFieldCO : msgDefCO.getNetRespFields())
	    {
		fieldsList.add(netFieldCO.getFieldCode().intValue());
	    }

	    return createResponse(netMti, messageData, fieldsList);

	}
	catch(Exception e)
	{
	    throw new IsoMessageException(EngineError.ISOMSG_RESPONSE_CREATION_FAILED, e);
	}
    }

    /**
     * Create An ISO draft message
     * 
     * @param mtiType
     * @return
     */
    public AtmIsoRespMsgDraft createRespDraftMessage(int mtiType)
    {
	return new AtmIsoRespMsgDraft(mtiType, this);
    }

    /**
     * Create An ISO draft message
     * 
     * @param mtiType
     * @return
     */
    public AtmIsoReqMsgDraft createReqDraftMessage()
    {
	return new AtmIsoReqMsgDraft(this);
    }

    /**
     * Create An ISO draft message
     * 
     * @param mtiType
     * @return
     */
    public AtmIsoReqMsgDraft createReqDraftMessage(int mtiType)
    {
	return new AtmIsoReqMsgDraft(mtiType, this);
    }

    /**
     * Initialize the message factory components
     */
    public void init()
    {

	try
	{

	    AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		    .getBean("atmEngConfigBO");

	    // @todo field auth 38 size
	    // create the generator
	    AtmInterfaceConfigSC configSC = new AtmInterfaceConfigSC();
	    configSC.setCode(getInterfaceCO().getCode());

	    // initiate auth code -- in case we faced any issue we should
	    // replace with SimpleTraceGenerator
	    // @todo create constant 999999
	    authCodeGenerator = new BoundedSeqGenerator(configBO.returnLastAuthenticationId(configSC), 999999);

	    // populate response code mapping
	    prepareResponseCodeMapping(configSC);

	}
	catch(Exception e)
	{
	    throw new IllegalConfigurationException(EngineError.MSG_FACT_FAILED_TO_CONF, e);
	}

    }

    /**
     * Check if the given mti is related to a response message
     * 
     * @param mti
     * @return
     */
    public boolean isResponse(int mti)
    {
	return null != interfaceCO.getResponseReqMap()
		&& interfaceCO.getResponseReqMap().containsKey(IsoUtil.convertMtiToHex(mti));
    }

    /**
     * Parse a message
     * 
     * @throws ParseException
     * @throws BaseException
     * @not completed @todo
     */
    public AtmIsoMessage parseMessage(byte[] buf, boolean parseAdditionalData) throws IsoParseException
    {

	try
	{
	    AtmIsoMessage isoMessage = super.parseMessage(buf,
		    NumberUtil.nullToZero(getInterfaceCO().getHeaderLength()).intValue());

	    // if failed to parse the message
	    if(null == isoMessage)
		throw new IsoParseException(EngineError.ISOMSG_PARSE_FAILED);

	    // set reference to the factory
	    isoMessage.setFactory(this);

	    // now parse all needed data
	    if(parseAdditionalData)
		parseMsgAdditionalInfo(isoMessage);

	    return isoMessage;

	}
	catch(Exception exception)
	{
	    /**
	     * Usually exception could be one of the following 1 -
	     * UnsupportedEncodingException 2 - ParseException 3 - BaseException
	     * 
	     * For logging purpose we handling the parse exception differently.
	     * in previous approach we have only handled the ParseException, but
	     * since the library j8583 is throwing IllegalArgumentException when
	     * parsing the field also, we have remove the exception type
	     * checking at open it to all type of exceptions
	     */
	    String parseExcpetion = exception.getMessage() != null ? String.format(" (%s)", exception.getMessage())
		    : StringUtils.EMPTY;

	    throw new IsoParseException(EngineError.ISOMSG_PARSE_FAILED, parseExcpetion, exception);
	}
    }

    /**
     * Populate Auth code. Authentication code will be auto generated in case
     * it's null
     * 
     * @param isoMessage
     */
    public void populateAuthCode(AtmIsoMessage isoMessage)
    {

	if(null != isoMessage.getField(IsoDefinitionConstants.BIT_APPROVAL_CODE))
	    return;

	// check if this MTI has that field
	IsoMessage isoMsgTemplate = getMessageTemplate(isoMessage.getType());

	if(!isoMsgTemplate.hasField(IsoDefinitionConstants.BIT_APPROVAL_CODE))
	    return;

	// Simpler Approach but we should check if it will take sub field into
	// consideration
	// m.setValue(11, traceGen.nextTrace(), IsoType.NUMERIC, 6)
	HashMap<String, Object> valueHm = new HashMap<>();
	valueHm.put(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX
		.concat(String.valueOf(IsoDefinitionConstants.BIT_APPROVAL_CODE)), authCodeGenerator.nextId());

	Log.getInstance().debug("Auth Code " + authCodeGenerator.getLastId());
	IsoValue<?> isoTemplField = isoMsgTemplate.getAt(IsoDefinitionConstants.BIT_APPROVAL_CODE);
	IsoValue<?> isoValue = createIsoFieldByTemplate(isoTemplField, valueHm,
		String.valueOf(IsoDefinitionConstants.BIT_APPROVAL_CODE), true);

	isoMessage.setField(IsoDefinitionConstants.BIT_APPROVAL_CODE, isoValue);
    }

    /**
     * This method will parse Iso Message Additional Info such as 1- Acquirer 2-
     * Trx type 3- Terminal
     * 
     * @param isoMessage
     * @return
     * @throws BaseException
     * @throws Exception
     */
    public AtmIsoMessage parseMsgAdditionalInfo(AtmIsoMessage isoMessage) throws IsoParseException
    {

	// convert to hashMap
	HashMap<String, Object> isoMsgHm = convertToHashMap(isoMessage);

	// set comp code
	isoMessage.setCompCode(interfaceCO.getCompCode());
	isoMessage.setCompBriefName(interfaceCO.getCompBriefName());

	// parse acquirer
	AcquirerCO acquirer = parseAcquirer(isoMsgHm);
	isoMessage.setAcquirerId(acquirer.getId());

	// check which trxtype we should use
	TransactionTypeCO trxType = parseTrxType(isoMsgHm, acquirer);

	if(null != trxType)
	{
	    isoMessage.setTrxType(trxType.getTrxType());

	    // parse charge
	    TransactionTypeCO feesChargeTrx = parseChargeTrxType(isoMsgHm, trxType);

	    if(null != feesChargeTrx)
		isoMessage.setChargeTrxType(feesChargeTrx.getTrxType());
	}

	//
	if(StringUtil.nullEmptyToValue(acquirer.getBankAtmYN(), ConstantsCommon.NO)
		.equalsIgnoreCase(ConstantsCommon.NO))
	{
	    isoMessage.setTrxBranchCode(acquirer.getTrxBranchCode());
	    isoMessage.setTellerId(acquirer.getTellerCode());
	    isoMessage.setTellerUserId(acquirer.getTellerUserId());
	    return isoMessage;
	}

	// get terminal info
	TerminalCO terminal = parseTerminal(acquirer, isoMsgHm);
	isoMessage.setTellerId(terminal.getTellerCode());
	isoMessage.setTrxBranchCode(terminal.getTrxBranchCode());
	isoMessage.setTerminalCode(terminal.getCode());
	isoMessage.setTerminalDesc(terminal.getDescription());
	isoMessage.setTellerUserId(terminal.getTellerUserId());

	// set Merchant code
	if(null != terminal.getMerchantCO())
	{
	    isoMessage.setMerchantCode(terminal.getMerchantCO().getCode());
	    isoMessage.setMerchAdditionalRef(terminal.getMerchantCO().getAdditionalRef());
	    isoMessage.setMerchIbanAccNo(terminal.getMerchantCO().getIbanAccNo());
	}

	return isoMessage;
    }


    /**
     * @param isoMsgHm
     * @param acquirer
     * @return
     * @throws IsoParseException
     */
    private TransactionTypeCO parseTrxType(HashMap<String, Object> isoMsgHm, AcquirerCO acquirer)
	    throws IsoParseException
    {
	try
	{
	    TransactionTypeCO trx = null;

	    BigDecimal trxTypeId = (BigDecimal) IsoUtil.evaluateExpression(acquirer.getTrxTypesJoinedExp(), isoMsgHm);

	    if(null != trxTypeId && acquirer.getTransactionTypes().containsKey(trxTypeId))
		trx = acquirer.getTransactionTypes().get(trxTypeId);

	    return trx;
	   
	} catch(BaseException exp){
	    throw new IsoParseException(EngineError.ISOMSG_PARSE_TRX_FAILED, exp);
	}
    }
    

    /**
     * Parse Charge trx type
     * 
     * @param list
     * @param isoMsgHm
     * @return
     * @throws IsoParseException
     * @throws BaseException
     */
    private TransactionTypeCO parseChargeTrxType(HashMap<String, Object> isoMsgHm, TransactionTypeCO trxCO)
	    throws IsoParseException
    {

	try
	{
	    TransactionTypeCO fees = null;
	    List<TransactionTypeCO> listOfFees = trxCO.getListOfFees();

	    /**
	     * @note: currently the expression is used to check if this charge
	     *        is applicable or not.
	     */
	    for(TransactionTypeCO trxType : listOfFees)
	    {

		// if expression is empty that mean this charge will be always
		// applicable
		if(StringUtil.nullToEmpty(trxType.getExpression()).isEmpty())
		{
		    fees = trxType;
		    break;
		}

		Boolean result = (Boolean) IsoUtil.evaluateExpression(trxType.getExpression(), isoMsgHm);

		if(!result)
		    continue;

		fees = trxType;
		break;
	    }

	    return fees;

	}
	catch(BaseException exp)
	{
	    throw new IsoParseException(EngineError.ISOMSG_PARSE_CHARGE_TRX_FAILED, exp);
	}
    }

    /**
     * Parse terminal info
     * 
     * @param acquirer
     * @param isoMessage
     * @return
     * @throws Exception
     */
    public TerminalCO parseTerminal(AcquirerCO acquirer, HashMap<String, Object> isoMessage) throws IsoParseException
    {
	try
	{
	    // check if terminal id exists
	    String terminalCode = (String) isoMessage.get(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX
		    .concat(String.valueOf(IsoDefinitionConstants.BIT_TERMINAL_ID)));

	    if(StringUtil.nullToEmpty(terminalCode).isEmpty())
		throw new IsoParseException(EngineError.ISOMSG_PARSE_TERM_FAILED, "Terminal code not defined");
	    
	    TerminalCO terminal = acquirer.getTerminals().get(terminalCode.trim());

	    // @todo we may include the terminal code later
	    // @todo convert to IllegalConfigurationException
	    if(null == terminal)
		throw new IsoParseException(EngineError.ISOMSG_PARSE_TERM_FAILED);

	    return terminal;

	}catch(BaseException exp){
	    throw new IsoParseException(EngineError.ISOMSG_PARSE_TERM_FAILED, exp);
	}
    }

    /**
     * This method will try to find the acquirer based on the parsed message.
     * 
     * @param isoMessage
     * @return
     * @throws IsoParseException
     */
    public AcquirerCO parseAcquirer(HashMap<String, Object> isoMessage) throws IsoParseException
    {
	// @todo output the expression while throwing exception
	try
	{
	    AcquirerCO acquirer = null;
	    String acquirersJoinedExp = getInterfaceCO().getAcquirersJoinedExp();
	    
	    BigDecimal acquirerId = (BigDecimal) IsoUtil.evaluateExpression(acquirersJoinedExp, isoMessage);
	    
	    if(null != acquirerId && getInterfaceCO().getAcquirers().containsKey(acquirerId))
		acquirer = getInterfaceCO().getAcquirers().get(acquirerId);
	    
	    // if no acquirer is found we should throw an exception
	    // @todo convert to IllegalConfigurationException
	    if(null == acquirer)
		throw new IsoParseException(EngineError.ISOMSG_PARSE_ACQ_FAILED);
	    
	    return acquirer;

	}
	catch(BaseException e)
	{
	    throw new IsoParseException(EngineError.ISOMSG_PARSE_ACQ_FAILED, e);
	}
    }

    
    /**
     * Return iso response code mapping
     * 
     * @param reponseCode
     * @param reponseCodeType
     * @return
     */
    public String returnIsoRespCode(String reponseCode, String reponseCodeType)
    {

	String isoRespCode = null;

	if(null != responseCodeMap.get(reponseCodeType) && !responseCodeMap.get(reponseCodeType).isEmpty())
	    isoRespCode = responseCodeMap.get(reponseCodeType).get(reponseCode);

	if(null != isoRespCode)
	    return isoRespCode;

	if(null == responseCodeMap.get("OTHER") || null == responseCodeMap.get("OTHER").keySet())
	    throw new IllegalConfigurationException(EngineError.ISOMSG_RESPCODE_NO_MATCH_FOUND);

	String key = responseCodeMap.get("OTHER").keySet().iterator().next();
	return responseCodeMap.get("OTHER").get(key);
    }

    /**
     * Creates a Iso message, override this method in the subclass to provide
     * your own implementations of IsoMessage.
     * 
     * @param header The optional ISO header that goes before the message type
     * @return IsoMessage
     */
    protected AtmIsoMessage createIsoMessage(String header)
    {
	return new AtmIsoMessage(header);
    }

    /**
     * Creates a Iso message with the specified binary ISO header. Override this
     * method in the subclass to provide your own implementations of IsoMessage.
     * 
     * @param binHeader The optional ISO header that goes before the message
     *            type
     * @return IsoMessage
     */
    protected AtmIsoMessage createIsoMessageWithBinaryHeader(byte[] binHeader)
    {
	return new AtmIsoMessage(binHeader);
    }

    /**
     * This method will populate the additional value sent in the data hashMap
     * into the AtmIsoMessage.
     * 
     * @note : this should be implemented later on fter making sure that the
     *       request mapping the is getting the right data.
     * @param message
     * @param data
     */
    private void populateAdditionanlInfo(AtmIsoMessage isoMessage, HashMap<String, Object> isoMsgDataHm)
    {

	// Company/branch info
	isoMessage.setCompCode((BigDecimal) isoMsgDataHm.get(IsoDefinitionConstants.ISO_FIELD_COMPCODE.concat("-")));
	isoMessage.setCompBriefName((String) isoMsgDataHm.get(IsoDefinitionConstants.ISO_FIELD_COMPNAME));
	isoMessage.setTrxBranchCode((BigDecimal) isoMsgDataHm.get(IsoDefinitionConstants.ISO_FIELD_BRCODE));

	// set teller info
	isoMessage.setTellerId((BigDecimal) isoMsgDataHm.get(IsoDefinitionConstants.ISO_FIELD_TELLCODE));
	isoMessage.setTellerUserId((String) isoMsgDataHm.get(IsoDefinitionConstants.ISO_FIELD_TELLERUSER));

	// transaction info
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_FIELD_TRXCODE, isoMessage.getTrxType());
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_FIELD_CHARGETRXCODE, isoMessage.getChargeTrxType());
//		
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_FIELD_MERACC, isoMessage.getMerchAdditionalRef());
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_FIELD_TERMINAL, isoMessage.getTerminalCode());
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_FIELD_TERMDESC, isoMessage.getTerminalDesc());
//		
//		//@todo fix this 
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_FIELD_RESP_RESULT, isoMessage.getTerminalCode());
//		
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_FIELD_INTERFACE_CODE, isoMessage.getInterfaceCode());
//    	isoMsgDataHm.put(IsoDefinitionConstants.ISO_HEADER, isoMessage.getIsoHeader());
	isoMessage.setCoreErrorDesc((String) isoMsgDataHm.get(IsoDefinitionConstants.ISO_FIELD_CORE_ERROR_DESC));
    }

    /**
     * Prepare response code mapping
     * 
     * @param configSC
     * @throws BaseException
     */
    private void prepareResponseCodeMapping(AtmInterfaceConfigSC configSC) throws BaseException
    {

	HashMap<String, HashMap<String, String>> responseCodeMapping = new HashMap<>();
	// get the bean
	AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngConfigBO");

	// load all response code
	List<AtmIsoResponseMapCO> listOfResponseCodes = configBO.returnIsoResponseMappingList(configSC);

	// group the codes by categories
	for(AtmIsoResponseMapCO respMap : listOfResponseCodes)
	{

	    responseCodeMapping.putIfAbsent(respMap.getResponseType(), new HashMap<>());
	    responseCodeMapping.get(respMap.getResponseType()).put(respMap.getCoreStatusCode(),
		    respMap.getIsoStatusCode());
	}

	this.responseCodeMap = responseCodeMapping;
    }

    /**
     * return Response MTI/Result of Expression
     * 
     * @param atmIsoMessageCO
     * @param isoMsgHm
     * @return
     * @throws BaseException
     */
    private String returnResponseMti(AtmIsoMessageCO atmIsoMessageCO, HashMap<String, Object> isoMsgHm)
	    throws BaseException
    {
	return StringUtil.isEmptyString(atmIsoMessageCO.getRespExpression()) ? atmIsoMessageCO.getResponseMti()
		: (String) IsoUtil.evaluateExpression(atmIsoMessageCO.getRespExpression(), isoMsgHm);
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString()
    {
	return "MessageFactory";
    }

    /**
     * @return the interfaceCO
     */
    public AtmInterfaceCO getInterfaceCO()
    {
	return interfaceCO;
    }

    /**
     * @return the authCodeGenerator
     */
    public BoundedSeqGenerator getAuthCodeGenerator()
    {
	return authCodeGenerator;
    }

    /**
     * @param authCodeGenerator the authCodeGenerator to set
     */
    public void setAuthCodeGenerator(BoundedSeqGenerator authCodeGenerator)
    {
	this.authCodeGenerator = authCodeGenerator;
    }

    /**
     * @return the responseCodeMap
     */
    public HashMap<String, HashMap<String, String>> getResponseCodeMap()
    {
	return responseCodeMap;
    }

    /**
     * @param responseCodeMap the responseCodeMap to set
     */
    public void setResponseCodeMap(HashMap<String, HashMap<String, String>> responseCodeMap)
    {
	this.responseCodeMap = responseCodeMap;
    }

    /**
     * @return the useAsciiEncoding
     */
    public boolean isUseAsciiEncoding()
    {
	return useAsciiEncoding;
    }

    /**
     * @param useAsciiEncoding the useAsciiEncoding to set
     */
    public void setUseAsciiEncoding(boolean useAsciiEncoding)
    {
	this.useAsciiEncoding = useAsciiEncoding;
    }
    
    /**
     * Return length type ( base 256/10)
     * This property is retrieved from the interfaceCO
     * @return
     */
    public String getLengthType()
    {
	return interfaceCO.getLengthType();
    }
    
    /**
     * Return true if the header length should be included
     * in the length value
     * This property is retrieved from the interfaceCO
     * Note: this value is always true.
     * 
     * @return
     */
    public boolean isIncludeHeader()
    {
	return StringUtil.nullEmptyToValue(getInterfaceCO().getIncludeHeader()
		, ConstantsCommon.NO).equalsIgnoreCase(ConstantsCommon.NO);
    }

    /**
     * Return true if the length of the field length should be included
     * in the length value
     * @return
     */
    public boolean isIncludeLenFieldLength()
    {
	return NumberUtil.nullToZero(getInterfaceCO().getIncludeLength())
		.equals(BigDecimal.ZERO);
    }

    /**
     * Return protocol identification
     * @return
     */
    public String getProtocolIdentification()
    {
	return StringUtil.nullToEmpty(getInterfaceCO().getProtocolIdentification());
    }

    /**
     * Return the length of "Field length"
     * @return
     */
    public int getLenFieldLength()
    {
	return NumberUtil.nullToZero(getInterfaceCO().getMsgLength()).intValue();
    }

    /**
     * Length of the header
     * @return
     */
    public int getHeaderLength()
    {
	return NumberUtil.nullToZero(getInterfaceCO().getHeaderLength()).intValue();
    }

    /**
     * Return the interface code to which belong this message factory
     * @return
     */
    public BigDecimal getInterfaceCode()
    {
	return getInterfaceCO().getCode();
    }

    /**
     * Return the list of iso fields to which belong this message factory
     * @return
     */
    public HashMap<BigDecimal, AtmIsoFieldCO> getIsoFieldList()
    {
	return getInterfaceCO().getIsoFieldList();
    }
}
