package com.path.atm.dao.engine;

import com.path.dbmaps.vo.ATM_ENG_ACTION_LOGVO;
import com.path.dbmaps.vo.ATM_ENG_INTERFACEVO;
import com.path.lib.common.exception.DAOException;

/**
 * 
 * Copyright 2019, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * 
 * @author: Alim Khowajaa
 *
 *          AtmEngineLogsDAO.java used to
 */
public interface AtmEngineLogsDAO
{

    /**
     * 
     * @param eng_INTERFACEVO
     * @return
     * @throws DAOException
     */
    public Integer insertAtmEngineLogs(ATM_ENG_INTERFACEVO eng_INTERFACEVO) throws DAOException;

    /**
     * Return Reactor engine interface row
     * 
     * @param atm_ENG_INTERFACEVO
     * @return ATM_ENG_INTERFACEVO
     * @throws DAOException
     */
    public ATM_ENG_INTERFACEVO returnEngInterface(ATM_ENG_INTERFACEVO atm_ENG_INTERFACEVO) throws DAOException;

    /**
     * Insert a reactor instance
     * 
     * @param interfaceCO
     * @return
     * @throws DAOException
     */
    public Integer insertReactorInstance(ATM_ENG_INTERFACEVO interfaceCO) throws DAOException;

    /**
     * @param engInterfaceVO
     * @return
     * @throws DAOException
     */
    public Integer updateEngInterface(ATM_ENG_INTERFACEVO engInterfaceVO) throws DAOException;

    /**
     * insert logs of Engine Action
     * 
     * @param action_LOGVO
     * @throws DAOException
     */
    public Integer logEngAction(ATM_ENG_ACTION_LOGVO action_LOGVO) throws DAOException;

}