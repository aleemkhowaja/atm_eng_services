package com.path.atm.bo.engine.impl;

import com.path.atm.bo.engine.AtmEngineLogsBO;
import com.path.atm.dao.engine.AtmEngineLogsDAO;
import com.path.dbmaps.vo.ATM_ENG_ACTION_LOGVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_REQUESTVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_REQ_DETAILVO;
import com.path.dbmaps.vo.ATM_ENG_INCOMING_RESPONSEVO;
import com.path.dbmaps.vo.ATM_ENG_INTERFACEVO;
import com.path.dbmaps.vo.ATM_ENG_OUTGOING_REQUESTVO;
import com.path.dbmaps.vo.ATM_ENG_OUTGOING_RESPONSEVO;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BaseException;

/**
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngineLogsBOImpl extends BaseBO implements AtmEngineLogsBO
{

    private AtmEngineLogsDAO atmEngineLogsDAO;

    @Override
    public void logEngAction(ATM_ENG_ACTION_LOGVO action_LOGVO) throws BaseException
    {
	atmEngineLogsDAO.logEngAction(action_LOGVO);
    }

    /**
     * Log Iso Message request in atm eng request table
     * 
     * @param requestVO
     * @throws BaseException
     */
    public void logIncomingRequest(ATM_ENG_INCOMING_REQUESTVO requestVO) throws BaseException
    {
	genericDAO.insert(requestVO);
    }

    /**
     * Log Iso Message request details in atm eng request table
     * 
     * @param reqDetails
     * @throws BaseException
     */
    public void logIncomingReqDetails(ATM_ENG_INCOMING_REQ_DETAILVO reqDetails) throws BaseException
    {
	genericDAO.insert(reqDetails);
    }

    /**
     * Log Out outgoing Iso Message response
     * 
     * @param responseVO
     * @throws BaseException
     */
    public void logOutgoingResponse(ATM_ENG_OUTGOING_RESPONSEVO responseVO) throws BaseException
    {
	genericDAO.insert(responseVO);
    }

    /**
     * @param outgoingReq
     * @throws BaseException
     */
    public void logOutgoingRequest(ATM_ENG_OUTGOING_REQUESTVO outgoingReq) throws BaseException
    {
	genericDAO.insert(outgoingReq);
    }

    /**
     * Log Incoming response
     * 
     * @param incomingRespVO
     * @throws BaseException
     */
    public void logIncomingResponse(ATM_ENG_INCOMING_RESPONSEVO incomingRespVO) throws BaseException
    {
	genericDAO.insert(incomingRespVO);
    }

    /**
     * Insert a row in atm eng interface
     * 
     * @param interfaceCO
     * @throws BaseException
     */
    public Integer insertReactorInstance(ATM_ENG_INTERFACEVO interfaceCO) throws BaseException
    {
	return atmEngineLogsDAO.insertReactorInstance(interfaceCO);
    }

    /**
     * Insert/Update Interface log
     * 
     * @param interfaceCO
     * @return
     * @throws BaseException
     */
    public Integer updateReactorStatus(ATM_ENG_INTERFACEVO engInterfaceVO) throws BaseException
    {
	return atmEngineLogsDAO.updateEngInterface(engInterfaceVO);
    }

    /**
     * @param atmEngineLogsDAO
     */
    public void setAtmEngineLogsDAO(AtmEngineLogsDAO atmEngineLogsDAO)
    {
	this.atmEngineLogsDAO = atmEngineLogsDAO;
    }
}