package com.path.atm.engine.handler;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;

import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.exception.IsoParseException;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.IsoDefinitionConstants;
import com.path.atm.engine.iso8583.IsoUtil;
import com.path.atm.engine.iso8583.MessageFactory;
import com.path.atm.engine.iso8583.messages.AtmIsoRespMsgDraft;
import com.path.atm.engine.pool.tasks.IsoTask;
import com.path.atm.engine.util.AtmCommonUtil;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.AtmLogger;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.engine.AtmIsoMessageDefCO;
import com.path.bo.common.WebServiceCommonBO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.log.Log;

/**
 * This class will handle the ISO message
 * 
 * <p> This class will process a given task by getting 
 * the appropriate definition and execute it.
 * The task status and error message will be update on each phase accordingly.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IsoMessageHandler
{

    /**
     * Hold reference to the logger
     */
    private static AtmLogger atmlogger = AtmLogger.getInstance();

    /**
     * Error Logger
     */
    private static Log logger = Log.getInstance();

    /**
     * Hold Iso Task
     */
    private final IsoTask task;
    
    /**
     * Hold Internal ( current phase ).
     * Initialize the internal status to New
     */
    private String internalStatus = AtmEngineConstants.TSK_STATUS_NEW; 

    /**
     * Hold Request Message
     */
    private AtmIsoMessage requestMessage;

    /**
     * Hold Response message
     */
    private AtmIsoMessage isoRespMessage;

    /**
     * Hold reference to the message factory
     */
    private final AtmEngineReactor reactor;
    
    /**
     * Hold reference to the message factory
     */
    private final  MessageFactory messageFactory;
    
    /**
     * @param messageFactory
     * @param amqIsoTaskContainer
     */
    public IsoMessageHandler(IsoTask task, AtmEngineReactor reactor)
    {
	this.task = task;
	this.reactor = reactor;
	messageFactory = reactor.getMessageFactory();
    }

    /**
     * @param autoDispatch
     * @return 
     * @return
     */
    public AtmIsoMessage handleMessage(boolean autoDispatch)
    {

	/***
	 * @todo we should handle exception to prevent jms from re-delivering
	 * the message again
	 */
	try
	{
	    // set task starting time
	    task.setStartingTime(Calendar.getInstance().getTime());
		
	    // parse the incoming message
	    parseIncomingMessage();

	    // execute message definition
	    executeMessageDefinition();

	    if(autoDispatch)
		reactor.getConnector().sendAsync(isoRespMessage);

	    // mark the message as processed
	    task.setStatus(AtmEngineConstants.MSG_STATUS_PROCESSED);
	    internalStatus = AtmEngineConstants.TSK_STATUS_DONE;

	}catch(Exception cause){
	    // handleException
	    handleException(cause, autoDispatch);
	    logger.error(cause, "Error while handling message");

	}finally{
	    // log request details
	    atmlogger.logIncomingReqDetails(requestMessage);
	    
	    // log response
	    atmlogger.logOutgoingResponse(task,isoRespMessage);
	}
	
	return isoRespMessage;
    }
    
    
    /**
     * Parse incoming message and populate the needed variables
     * @throws IsoParseException
     */
    private void parseIncomingMessage() throws IsoParseException
    {
	/**
	 * At this level the parsing is safe since we have already passed the
	 * decoder handler. But we should never parse additional info at this
	 * level to make sure that any exception that occur during that parsing
	 * doesn't make the iso fields vanish.
	 */
	requestMessage = messageFactory.parseMessage(task.getPayloadBytes(), false);

	// populate basic task info needed for logging
	requestMessage.setId(new BigDecimal(task.getId()));
	requestMessage.setInterfaceCode(task.getInterfaceCode());

	// populate the authorization code and parse additional info
	messageFactory.populateAuthCode(requestMessage);
	messageFactory.parseMsgAdditionalInfo(requestMessage);
    }
    

    /**
     * Execute the message definition
     * @throws BaseException
     */
    private void executeMessageDefinition() throws BaseException
    {
	// get message definition
	AtmIsoMessageDefCO msgDef = messageFactory.returnIsoMsgDef(requestMessage);

	if(null == msgDef)
	    throw new IsoParseException(EngineError.ISOMSG_NO_MSG_DEF_FOUND);
	
	// execute mapping
	if(!NumberUtil.isEmptyDecimal(msgDef.getMappingId())){
	    executeMsgMapping(msgDef);
	    return;
	}
	
	// execute network message
	executeNetWorkMsg(msgDef);
    }
    

    /**
     * Handle Mapped Process
     * 
     * @param isoReqMessage
     * @param msgDef
     * @throws BaseException
     * 
     */
    private void executeMsgMapping(AtmIsoMessageDefCO msgDef) throws BaseException
    {

	// get Web Service Common BO
	WebServiceCommonBO webservice = (WebServiceCommonBO) ApplicationContextProvider.getApplicationContext()
		.getBean("webServiceCommonBO");

	MessageFactory messageFactory = reactor.getMessageFactory();
	// convert iso message to hashMap
	HashMap<String, Object> requestData = messageFactory.convertToHashMap(requestMessage);

	// @note : no need for the assign since we aren't creating a deep copy
	requestData = messageFactory.evaluateFieldExpression(requestData);
	
	// call the mapped service
	HashMap<String, Object> responseHm = webservice.callPWS(msgDef.getMappingId(), requestData);
	internalStatus = AtmEngineConstants.TSK_STATUS_MAP_EX;
	
	// IsoResp Builder
	AtmIsoRespMsgDraft messageDraft = messageFactory.createRespDraftMessage(requestMessage.getType());
	messageDraft.setFields(requestData).setResponseData(responseHm)
		.setReponseCodeType(IsoDefinitionConstants.RESP_TYPE_CORE);

	isoRespMessage = messageDraft.build();

	// populate basic field needed for logging, this should be replaced by
	// populateAdditionalInfo
	isoRespMessage.setCoreErrorDesc((String) responseHm.get(IsoDefinitionConstants.ISO_FIELD_CORE_ERROR_DESC));

	// set common pws request id
	task.setPwsReqId((String) responseHm.get(IsoDefinitionConstants.ISO_FIELD_PWS_REQ_ID));
	
	// set trx number
	isoRespMessage.setTrxNb(IsoUtil.returnTrxNb(responseHm));
    }

    /**
     * Handle Network message
     * 
     * <p>
     * Current implementation a response message is sent without any
     * manipulation
     * 
     * @param isoReqMessage
     * @param msgDef
     * @return
     * @throws IsoMessageException
     */
    private void executeNetWorkMsg(AtmIsoMessageDefCO msgDef) throws BaseException
    {

	MessageFactory messageFactory = reactor.getMessageFactory();
	HashMap<String, Object> requestData = messageFactory.convertToHashMap(requestMessage);

	AtmIsoRespMsgDraft messageDraft = messageFactory.createRespDraftMessage(requestMessage.getType());
	messageDraft.setFields(requestData).setNetType(msgDef.getNetworkMessageType())
		.setReponseCodeType(IsoDefinitionConstants.RESP_TYPE_CORE);

	isoRespMessage = messageDraft.build();
    }

    /**
     * Build Error Response
     * <p>
     * This response usually is sent when an exception occur before executing
     * the core mapping
     * 
     * @return
     * @throws IsoMessageException
     */
    private AtmIsoMessage buildErrorResponse() throws IsoMessageException
    {
	MessageFactory messageFactory = reactor.getMessageFactory();
	HashMap<String, Object> requestData = messageFactory.convertToHashMap(requestMessage);
	AtmIsoRespMsgDraft messageDraft = messageFactory.createRespDraftMessage(requestMessage.getType());

	// @note Response code will be defined at the Response code Mapping tbl
	messageDraft.setFields(requestData).setReponseCode("-1")
		.setReponseCodeType(IsoDefinitionConstants.RESP_TYPE_TECHNICAL);

	return messageDraft.build();
    }

    /**
     * This will handle caught exception
     * 
     * @param autoDispatch
     * @param e
     * @return
     */
    private void handleException(Exception cause, boolean autoDispatch)
    {
	try
	{
	    // before parsing is handled inside the pipeline
	    // we will include the additional parsing with the before core
	    task.setStatus(AtmEngineConstants.MSG_STATUS_FAILED);

	    // error message string builder
	    StringBuilder sb = new StringBuilder();
	    
	    // @todo what is the exception occur in the callPWS just after the
	    // call of rmi
	    // in current implementation it will be handled as it wasn't done
	    // Behavior to implement : we need the call pws to throw a specific
	    // exception
	    // or exception code to let us determine if the rmi call was
	    // executed yes or no.

	    // after core => timeout
	    if(AtmEngineConstants.TSK_STATUS_MAP_EX.equalsIgnoreCase(internalStatus)) {
		sb.append(EngineError.ISOMSG_PROCESSING_FAILED_AF_DEF)
		  .append(" ")
		  .append(AtmCommonUtil.getEngExpCauseMsg(cause));
		
		task.setErrorMsg(sb.toString());
		return;
	    }
	    
	    sb.append(EngineError.ISOMSG_PROCESSING_FAILED_BF_DEF)
		.append(" ").append(AtmCommonUtil.getEngExpCauseMsg(cause));
	    task.setErrorMsg(sb.toString());
		
	    // build error response message
	    isoRespMessage = buildErrorResponse();

	    // Before core => reply with error response
	    if(autoDispatch && null != isoRespMessage)
		reactor.getConnector().sendAsync(isoRespMessage);

	}catch(Exception exp){
	    // @todo should we wrap it inside ISO exception ??
	    task.setErrorMsg(AtmCommonUtil.getEngExpCauseMsg(exp));
	    logger.error(exp, "Error while handleException");
	}
    }
    
    /**
     * @return the requestMessage
     */
    public AtmIsoMessage getRequestMessage()
    {
	return requestMessage;
    }

    /**
     * @param requestMessage the requestMessage to set
     */
    public void setRequestMessage(AtmIsoMessage requestMessage)
    {
	this.requestMessage = requestMessage;
    }
}
