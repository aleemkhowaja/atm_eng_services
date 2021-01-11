package com.path.atm.engine.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.StringJoiner;

import com.path.atm.bo.engine.AtmEngineLogsBO;
import com.path.atm.engine.action.AtmBaseAction;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.IsoUtil;
import com.path.atm.engine.pool.tasks.IsoTask;
import com.path.dbmaps.vo.ATM_ENG_ACTION_LOGVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_REQUESTVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_REQ_DETAILVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_RESPONSEVO;
import com.path.dbmaps.vo.ATM_ENG_INTERFACEVO;
import com.path.dbmaps.vo.ATM_ENG_OUTGOING_REQUESTVO;
import com.path.dbmaps.vo.ATM_ENG_OUTGOING_RESPONSEVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

/**
 * @author MohammadAliMezzawi
 *
 */
public class AtmLogger
{

    /**
     * Atm engine logger Singleton instance
     */
    private volatile static AtmLogger instance;

    /**
     * Error Logger
     */
    private Log logger = Log.getInstance();

    /**
     * Reference to Atm Engine logging BO
     */
    private AtmEngineLogsBO atmEngineLogsBO;

    /**
     * Monitor object
     */
    private static Object monitor = new Object();

    /**
     * Return Atm Engine Logger instance
     */
    public static AtmLogger getInstance()
    {
	if(instance == null)
	{
	    synchronized(monitor)
	    {
		if(instance == null)
		{
		    instance = new AtmLogger();
		    instance.setAtmEngineLogsBO((AtmEngineLogsBO) ApplicationContextProvider.getApplicationContext()
			    .getBean("atmEngineLogsBO"));
		}
	    }
	}

	return instance;
    }

    /**
     * ATM Log Actions with Action Message
     * 
     * @param actionName
     * @param additionalData
     * 
     */
    public void logAction(AtmBaseAction action)
    {
	StringJoiner stringJoiner = new StringJoiner(" | ");

	try
	{

	    ATM_ENG_ACTION_LOGVO actionLogVO = new ATM_ENG_ACTION_LOGVO();
	    actionLogVO.setATM_ENG_ACTION(action.getActionType().getCode());
	    actionLogVO.setSTART_TIME(action.getStartingDate());
	    // @temporary
	    actionLogVO.setINTERFACE_CODE(BigDecimal.ZERO);

	    // build action message
	    stringJoiner.add(action.getActionType().getMsg());

	    if(StringUtil.isNotEmpty(action.toString()))
		stringJoiner.add(action.toString());

	    if(StringUtil.isNotEmpty(action.getAdditionalMsg()))
		stringJoiner.add(action.getAdditionalMsg());

	    actionLogVO.setMESSAGE(stringJoiner.toString());

	    // set ending date
	    if(null != action.getEndingDate())
		actionLogVO.setEND_TIME(action.getEndingDate());

	    // log action
	    atmEngineLogsBO.logEngAction(actionLogVO);

	}
	catch(Exception e)
	{
	    logger.error(e, String.format("Failed to log Engine action %s", stringJoiner.toString()));
	}
    }
    
    
    /**
     * Log Atm Engine reactor Instance
     * 
     * @param engInterfaceVO
     * @throws BaseException
     */
    public void insertReactorInstance(ATM_ENG_INTERFACEVO engInterfaceVO) throws BaseException
    {
	atmEngineLogsBO.insertReactorInstance(engInterfaceVO);
    }

    public void updateReactorInfo(ATM_ENG_INTERFACEVO engInterfaceVO)
    {
	try
	{
	    engInterfaceVO.setMESSAGE(StringUtil.substring(engInterfaceVO.getMESSAGE(), 3999));
	    atmEngineLogsBO.updateReactorStatus(engInterfaceVO);
	}
	catch(BaseException e)
	{
	    // @todo change to string format
	    logger.error(e, "Failed to Update reactor status : " + engInterfaceVO.getINTERFACE_ID() + " Status "
		    + engInterfaceVO.getSTATUS());
	}

    }

    /**
     * @return the atmEngLoggingBO
     */
    public AtmEngineLogsBO getAtmEngineLogsBO()
    {
	return atmEngineLogsBO;
    }

    /**
     * @param atmEngLoggingBO the atmEngLoggingBO to set
     */
    public void setAtmEngineLogsBO(AtmEngineLogsBO atmEngineLogsBO)
    {
	this.atmEngineLogsBO = atmEngineLogsBO;
    }

    /**
     * Log incoming request
     * 
     * @param tsk
     */
    public void logIncomingRequest(IsoTask tsk)
    {
	try
	{

	    ATM_ENG_INCOMING_REQUESTVO requestVO = new ATM_ENG_INCOMING_REQUESTVO();
	    requestVO.setMESSAGE_ID(new BigDecimal(tsk.getId()));
	    requestVO.setMTI_CODE_REQUEST(IsoUtil.convertMtiToHex(tsk.getMessageType()));
	    /**
	     * Usually this array will be populated unless we have an exception
	     * at the pipeline level.
	     * @note : should we dump the string
	     * IsoUtil.dumpString(tsk.getMessageIsoString().getBytes(AtmEngineConstants.CHAR_ENCODING)
	     */
	    String requestStr = null != tsk.getMessageIsoString()?
		 tsk.getMessageIsoString(): ( null != tsk.getMessageBytes()?
			 new String(tsk.getMessageBytes(),AtmEngineConstants.CHAR_ENCODING) : null);
			    
	    if(null != requestStr)
		requestVO.setMESSAGE_REQUEST(requestStr);

	    requestVO.setSTATUS(tsk.getStatus());
	    requestVO.setERROR_MESSAGE(StringUtil.substring(tsk.getErrorMsg(), 255));

	    requestVO.setINTERFACE_ID(NumberUtil.nullToZero(tsk.getInterfaceCode()));
	    requestVO.setRECEIVE_TIME(tsk.getReceivedTime());

	    atmEngineLogsBO.logIncomingRequest(requestVO);

	}
	catch(Exception e)
	{
	    // @todo we should notify the monitor
	    if(tsk.getMessageBytes() != null)
		logger.error(e, "Failed to log Iso Request".concat(tsk.getMessageBytes().toString()));
	}

    }

    /**
     * Log incoming details
     * 
     * @param requestMessage
     */
    public void logIncomingReqDetails(AtmIsoMessage requestMessage)
    {
	try
	{ 
	    ATM_ENG_INCOMING_REQ_DETAILVO reqDetails = new ATM_ENG_INCOMING_REQ_DETAILVO();
	    
	    // request key
	    reqDetails.setINTERFACE_ID(requestMessage.getInterfaceCode());
	    reqDetails.setMESSAGE_ID(requestMessage.getId());
	    
	    // request basic info
	    reqDetails.setACQUIRER_CODE(requestMessage.getAcquirerId());
	    reqDetails.setTELLER_CODE(requestMessage.getTellerId());
	    reqDetails.setCOMP_CODE(requestMessage.getCompCode());
	    reqDetails.setBRANCH_CODE(requestMessage.getTrxBranchCode());
	    reqDetails.setTERMINAL_ID(requestMessage.returnTerminalCode());
	    reqDetails.setTRX_TYPE(requestMessage.getTrxType());
	    reqDetails.setCHARGE_TRX_TYPE(requestMessage.getChargeTrxType());
	    
	    reqDetails.setRETRIEVAL_REF_NB(requestMessage.returnRetrievalRefNbrStr());
	    reqDetails.setAUTH_CODE(requestMessage.returnAuthCodeStr());
	    reqDetails.setPROCESS_CODE(requestMessage.returnProcessCode());
	    
	    // request map
	    reqDetails.setREQUEST_MAP(requestMessage.toString());
	    
	    atmEngineLogsBO.logIncomingReqDetails(reqDetails);
	}
	catch(Exception e)
	{
	    logger.error(e, "Failed to log Iso Request");
	}
    }

    /**
     * Log outgoing response
     * 
     * @param task
     * @param isoRespMessage
     */
    public void logOutgoingResponse(IsoTask task, AtmIsoMessage isoRespMessage)
    {
	try
	{
	    ATM_ENG_OUTGOING_RESPONSEVO responseVO = new ATM_ENG_OUTGOING_RESPONSEVO();
	    responseVO.setMESSAGE_ID(new BigDecimal(task.getId()));
	    responseVO.setINTERFACE_ID(task.getInterfaceCode());

	    responseVO.setSTART_TIME(task.getStartingTime());
	    responseVO.setEND_TIME(Calendar.getInstance().getTime());
	    
	    responseVO.setSTATUS(task.getStatus());
	    String errorMessage = task.getErrorMsg();
	    
	    if(null != isoRespMessage){
		responseVO.setMTI_CODE_RESPONSE(IsoUtil.convertMtiToHex(isoRespMessage.getType()));
		responseVO.setRESPONSE_MAP(isoRespMessage.toString());
		responseVO.setISO_RESPONSE(isoRespMessage.returnIsoString());
		responseVO.setRESPONSE_CODE(isoRespMessage.returnResponseCode());
		responseVO.setTRS_NO(isoRespMessage.getTrxNb());

		if(StringUtil.isEmptyString(errorMessage))
		errorMessage = isoRespMessage.getCoreErrorDesc();
	    }
	    
	    responseVO.setCOMMON_PWS_REQUEST_ID(task.getPwsReqId());
	    responseVO.setERROR_MESSAGE(StringUtil.substring(errorMessage, 255));
	    atmEngineLogsBO.logOutgoingResponse(responseVO);

	}catch(Exception exp){
	    logger.error(exp, "Failed to log Iso Response");
	}
    }
    
    
    /**
     * Log outgoing request
     * @param message
     */
    public void logOutgoingRequest(AtmIsoMessage message)
    {
	try
	{
	    ATM_ENG_OUTGOING_REQUESTVO outgoingReq = new ATM_ENG_OUTGOING_REQUESTVO();
	    outgoingReq.setINTERFACE_ID(message.getInterfaceCode());
	    outgoingReq.setMESSAGE_ID(message.getId());
	    outgoingReq.setMTI_CODE_REQUEST(IsoUtil.convertMtiToHex(message.getType()));
	    outgoingReq.setMESSAGE_REQUEST(message.returnIsoString());
	    outgoingReq.setSENT_TIME(message.getStartingTime());
	    outgoingReq.setERROR_MESSAGE(StringUtil.substring(message.getErrorMessage(), 255));
	    outgoingReq.setSTATUS(message.getStatus());
	    atmEngineLogsBO.logOutgoingRequest(outgoingReq);

	}catch(Exception e){
	    logger.error(e, "Failed to log outgoing request");
	}
    }
    

    /**
     * Log incoming response
     * @param tsk
     */
    public void logIncomingResponse(IsoTask tsk)
    {
	try
	{
	    ATM_ENG_INCOMING_RESPONSEVO incomingRespVO = new ATM_ENG_INCOMING_RESPONSEVO();

	    String responseStr = null != tsk.getMessageIsoString()?
		    tsk.getMessageIsoString(): ( null != tsk.getMessageBytes()?
			    new String(tsk.getMessageBytes(),AtmEngineConstants.CHAR_ENCODING) : null);

	    if(null != responseStr)
		incomingRespVO.setISO_RESPONSE(responseStr);

	    incomingRespVO.setMTI_CODE_RESPONSE(IsoUtil.convertMtiToHex(tsk.getMessageType()));
	    incomingRespVO.setSTATUS(tsk.getStatus());
	    incomingRespVO.setERROR_MESSAGE(StringUtil.substring(tsk.getErrorMsg(), 255));

	    incomingRespVO.setINTERFACE_ID(NumberUtil.nullToZero(tsk.getInterfaceCode()));
	    incomingRespVO.setRECEIVED_TIME(tsk.getReceivedTime());

	    atmEngineLogsBO.logIncomingResponse(incomingRespVO);

	}
	catch(Exception e)
	{
	    if(tsk.getMessageBytes() != null)
		logger.error(e, "Failed to log Iso Response".concat(
			new String(tsk.getMessageBytes(),AtmEngineConstants.CHAR_ENCODING)));
	}
	
    }

}