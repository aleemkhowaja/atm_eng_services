package com.path.atm.dao.engine.impl;

import com.path.atm.dao.engine.AtmEngineLogsDAO;
import com.path.dbmaps.vo.ATM_ENG_ACTION_LOGVO;
import com.path.dbmaps.vo.ATM_ENG_INTERFACEVO;
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.DAOException;

/**
 * 
 * Copyright 2019, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * 
 * @author: Alim Khowaja
 * AtmEngineLogsDAOImpl.java used to select/insert logging in the engine logging tables
 */
public class AtmEngineLogsDAOImpl extends BaseDAO implements AtmEngineLogsDAO
{

    @Override
    public Integer insertAtmEngineLogs(ATM_ENG_INTERFACEVO eng_INTERFACEVO) throws DAOException
    {
	return (Integer) getSqlMap().insert("atmEngineLogsMapper.insertATM_ENG_INTERFACE", eng_INTERFACEVO);
    }

    /**
     * Return Reactor engine interface row
     * 
     * @param atm_ENG_INTERFACEVO
     * @return ATM_ENG_INTERFACEVO
     * @throws DAOException
     */
    public ATM_ENG_INTERFACEVO returnEngInterface(ATM_ENG_INTERFACEVO atm_ENG_INTERFACEVO) throws DAOException
    {
	return (ATM_ENG_INTERFACEVO) getSqlMap().queryForObject("atmEngineLogsMapper.returnEngInterface",
		atm_ENG_INTERFACEVO);
    }

    /**
     * Insert a reactor instance
     * 
     * @param interfaceCO
     * @return
     * @throws DAOException
     */
    public Integer insertReactorInstance(ATM_ENG_INTERFACEVO interfaceCO) throws DAOException
    {
	return (Integer) getSqlMap().insert("atmEngineLogsMapper.insertReactorInstance", interfaceCO);
    }

    /**
     * @param engInterfaceVO
     * @return
     * @throws DAOException
     */
    public Integer updateEngInterface(ATM_ENG_INTERFACEVO engInterfaceVO) throws DAOException
    {
	return (Integer) getSqlMap().update("atmEngineLogsMapper.updateEngInterface", engInterfaceVO);
    }

    @Override
    public Integer logEngAction(ATM_ENG_ACTION_LOGVO action_LOGVO) throws DAOException
    {
	return (Integer) getSqlMap().insert("atmEngineLogsMapper.logEngAction", action_LOGVO);
    }
}