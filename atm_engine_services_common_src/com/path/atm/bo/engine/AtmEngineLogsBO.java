package com.path.atm.bo.engine;

import com.path.dbmaps.vo.ATM_ENG_ACTION_LOGVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_REQUESTVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_REQ_DETAILVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_RESPONSEVO;
import com.path.dbmaps.vo.ATM_ENG_INTERFACEVO;
import com.path.dbmaps.vo.ATM_ENG_OUTGOING_REQUESTVO;
import com.path.dbmaps.vo.ATM_ENG_OUTGOING_RESPONSEVO;
import com.path.lib.common.exception.BaseException;

/**
 * @author MohammadAliMezzawi
 *
 */
public interface AtmEngineLogsBO
{
    /**
     * Log Engine Action
     * 
     * @param actionName
     * @throws BaseException
     */
    public void logEngAction(ATM_ENG_ACTION_LOGVO action_LOGVO) throws BaseException;

    /**
     * Log Iso Message request in atm eng request table
     * 
     * @param requestVO
     * @throws BaseException
     */
    public void logIncomingRequest(ATM_ENG_INCOMING_REQUESTVO requestVO) throws BaseException;
    

    /**
     * Log Iso Message request details in atm eng request table
     * @param reqDetails
     * @throws BaseException
     */
    public void logIncomingReqDetails(ATM_ENG_INCOMING_REQ_DETAILVO reqDetails) throws BaseException;
    
    /**
     * Log Out outgoing Iso Message response
     * @param responseVO
     * @throws BaseException
     */
    public void logOutgoingResponse(ATM_ENG_OUTGOING_RESPONSEVO responseVO) throws BaseException;

    /**
     * @param outgoingReq
     * @throws BaseException
     */
    public void logOutgoingRequest(ATM_ENG_OUTGOING_REQUESTVO outgoingReq) throws BaseException;
    
    /**
     * Log Incoming response
     * 
     * @param incomingRespVO
     * @throws BaseException
     */
    public void logIncomingResponse(ATM_ENG_INCOMING_RESPONSEVO incomingRespVO) throws BaseException;

    /**
     * Insert a row in atm eng interface
     * 
     * @param engInterfaceVO
     * @return
     * @throws BaseException
     */
    public Integer insertReactorInstance(ATM_ENG_INTERFACEVO engInterfaceVO) throws BaseException;

    /**
     * Log Iso Message response in atm eng response table
     * 
     * @param interfaceCO
     * @throws BaseException
     */
    public Integer updateReactorStatus(ATM_ENG_INTERFACEVO engInterfaceVO) throws BaseException;
}