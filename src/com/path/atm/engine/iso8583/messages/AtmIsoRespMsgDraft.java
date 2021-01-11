package com.path.atm.engine.iso8583.messages;

import java.math.BigDecimal;
import java.util.HashMap;

import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.IsoDefinitionConstants;
import com.path.atm.engine.iso8583.MessageFactory;
import com.path.lib.common.util.StringUtil;

/**
 * This class will house all behaviors related Building response message
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmIsoRespMsgDraft extends AtmIsoMessageDraft<AtmIsoRespMsgDraft>
{

    /**
     * Hold reference to the response code
     */
    private String reponseCode;

    /**
     * Hold reference to the response code type
     */
    private String reponseCodeType;

    /**
     * Hold reference to the response code
     */
    private HashMap<String, Object> responseData = new HashMap<>();

    /**
     * @param mtiType
     * @param msgFactory
     */
    public AtmIsoRespMsgDraft(int mtiType, MessageFactory msgFactory)
    {
	super(mtiType, msgFactory);
    }

    /**
     * Build Atm iso Message response
     */
    public AtmIsoMessage build() throws IsoMessageException
    {
	return StringUtil.isEmptyString(getNetType()) ? createResponse() : createNetResponse();
    }

    /**
     * Return Network response message
     * 
     * @param networkMessageType
     * @return
     * @throws IsoMessageException
     */
    private AtmIsoMessage createNetResponse() throws IsoMessageException
    {
	return getMsgFactory().createNetWorkRespMsg(getNetType(), fields);
    }

    /**
     * @throws IsoMessageException
     * 
     */
    private AtmIsoMessage createResponse() throws IsoMessageException
    {

	reponseCodeType = StringUtil.nullEmptyToValue(reponseCodeType, IsoDefinitionConstants.RESP_TYPE_CORE);

	// response field
	String fieldCode = IsoDefinitionConstants.ISO_HM_FIELD_PREFIX
		.concat(String.valueOf(IsoDefinitionConstants.BIT_ACTION_CODE));

	// check if response code is sent in response data
	reponseCode = !StringUtil.nullToEmpty(reponseCode).isEmpty() ? reponseCode
		: (String) (null != responseData ? responseData.get(fieldCode) : "");

	// if not empty
	if(StringUtil.isNotEmpty(reponseCode))
	    reponseCode = String.valueOf((new BigDecimal(reponseCode)).setScale(0).intValue());

	String responseCode = getMsgFactory().returnIsoRespCode(reponseCode, reponseCodeType);
	responseData.put(fieldCode, responseCode);

	if(null != responseData)
	    fields.putAll(responseData);

	// create response containing only the iso fields ??!!
	return getMsgFactory().createResponse(type, fields);
    }

    /**
     * @param reponseCode the reponseCode to set
     */
    public AtmIsoRespMsgDraft setReponseCode(String reponseCode)
    {
	this.reponseCode = reponseCode;
	return this;
    }

    /**
     * @return the reponseCodeType
     */
    public String getReponseCodeType()
    {
	return reponseCodeType;
    }

    /**
     * @param reponseCodeType the reponseCodeType to set
     * @return
     */
    public AtmIsoRespMsgDraft setReponseCodeType(String reponseCodeType)
    {
	this.reponseCodeType = reponseCodeType;
	return this;
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

    /**
     * @return the responseData
     */
    public HashMap<String, Object> getResponseData()
    {
	return responseData;
    }

    /**
     * @param responseData the responseData to set
     */
    public AtmIsoRespMsgDraft setResponseData(HashMap<String, Object> responseData)
    {
	this.responseData = responseData;
	return this;
    }
}
