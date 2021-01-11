package com.path.atm.bo.engine.impl;

import java.util.List;
import com.path.atm.bo.engine.AtmEngineConfigBO;
import com.path.atm.dao.engine.AtmEngineConfigDAO;
import com.path.atm.vo.config.AtmChannelConfigCO;
import com.path.atm.vo.config.AtmInterfaceConfigSC;
import com.path.atm.vo.engine.AcquirerCO;
import com.path.atm.vo.engine.AcquirerSC;
import com.path.atm.vo.engine.AtmInterfaceCO;
import com.path.atm.vo.engine.AtmIsoFieldCO;
import com.path.atm.vo.engine.AtmIsoMessageCO;
import com.path.atm.vo.engine.AtmIsoMessageDefCO;
import com.path.atm.vo.engine.AtmIsoNetMsgFldsCO;
import com.path.atm.vo.engine.AtmIsoResponseMapCO;
import com.path.atm.vo.engine.TerminalCO;
import com.path.atm.vo.engine.TerminalSC;
import com.path.atm.vo.engine.TransactionTypeCO;
import com.path.lib.common.base.BaseBO;
import com.path.lib.common.exception.BaseException;

public class AtmEngineConfigBOImpl extends BaseBO implements AtmEngineConfigBO
{

    /**
     * hold reference to the Engine Config DAO
     */
    private AtmEngineConfigDAO atmEngConfigDAO;

    /**
     * Clear Mapping cache
     */
    public void clearMappingCache() throws BaseException
    {
	atmEngConfigDAO.clearMappingCache();
    }

    /**
     * Return Atm Channel Configuration
     * 
     * @param configSC
     * @return
     * @throws BaseException
     */
    public AtmChannelConfigCO returnChannelConfig(AtmInterfaceConfigSC configSC) throws BaseException
    {
	return atmEngConfigDAO.returnChannelConfig(configSC);
    }

    /**
     * return Last Authentication Code
     * 
     * @param atmInterfaceConfigSC
     * @return
     * @throws BaseException
     */
    public long returnLastAuthenticationId(AtmInterfaceConfigSC atmInterfaceConfigSC) throws BaseException
    {
	return atmEngConfigDAO.returnLastAuthenticationId(atmInterfaceConfigSC);
    }

    /**
     * Return Next Message id
     * 
     * @param atmInterfaceConfigSC
     * @return
     * @throws BaseException
     */
    public long returnNextMessageId(AtmInterfaceConfigSC atmInterfaceConfigSC) throws BaseException
    {
	return atmEngConfigDAO.returnNextMessageId(atmInterfaceConfigSC);
    }

    /**
     * Return list of iso response mapping by interface
     * 
     * @param atmInterfaceConfigSC
     * @return
     * @throws BaseException
     */
    public List<AtmIsoResponseMapCO> returnIsoResponseMappingList(AtmInterfaceConfigSC configSc) throws BaseException
    {
	// retrieve ISO Response Mapping List
	return atmEngConfigDAO.returnIsoResponseMappingList(configSc);
    }

    /**
     * return ISO Message Definition
     * 
     * @param configSc
     * @return
     * @throws BaseException
     */
    public List<AtmIsoMessageDefCO> returnIsoMsgDefList(AtmInterfaceConfigSC configSc) throws BaseException
    {
	return atmEngConfigDAO.returnIsoMsgDefList(configSc);
    }

    /**
     * Return list of ISO field definitions by interface id
     * 
     * @param configSc
     * @return
     * @throws BaseException
     */
    public List<AtmIsoFieldCO> returnIsoFieldsDefByInt(AtmInterfaceConfigSC configSc) throws BaseException
    {
	return atmEngConfigDAO.returnIsoFieldsDefByInt(configSc);
    }

    /**
     * Return Atm interface co with list of attached iso message
     * 
     * @param configSc
     * @return
     * @throws BaseException
     */
    public AtmInterfaceCO returnInterfaceInfo(AtmInterfaceConfigSC configSc) throws BaseException
    {
	return atmEngConfigDAO.returnInterfaceInfo(configSc);
    }

    /**
     * Return List of Iso messages based on the given criteria
     * 
     * @param configSC
     * @return
     * @throws BaseException
     */
    public List<AtmIsoMessageCO> returnIsoMessages(AtmInterfaceConfigSC configSC) throws BaseException
    {
	return atmEngConfigDAO.returnIsoMessages(configSC);
    }

    /**
     * Return ATM Network Msg Request and Response Fields
     * 
     * @param configSC
     * @return
     * @throws BaseException
     */
    public List<AtmIsoNetMsgFldsCO> returnAtmNetMsgFldsList(AtmInterfaceConfigSC configSC) throws BaseException
    {
	return atmEngConfigDAO.returnAtmNetMsgFldsList(configSC);
    }

    /**
     * Return list of acquirer
     * 
     * @param acquirerSC
     * @return
     * @throws BaseException
     */
    public List<AcquirerCO> returnAcquirerList(AcquirerSC acquirer) throws BaseException
    {
	return atmEngConfigDAO.returnAcquirerList(acquirer);
    }

    /**
     * @param acquirer
     * @return
     * @throws BaseException
     */
    public List<TransactionTypeCO> returnTrxTypeList(AcquirerSC acquirer) throws BaseException
    {
	return atmEngConfigDAO.returnTrxTypeList(acquirer);
    }

    /**
     * Return terminal details
     * 
     * @param termnialSC
     * @return
     * @throws BaseException
     */
    public List<TerminalCO> returnTerminals(TerminalSC termnialSC) throws BaseException
    {
	return atmEngConfigDAO.returnTerminals(termnialSC);
    }

    /**
     * Return Atm engine config DAO
     * 
     * @return
     */
    public AtmEngineConfigDAO getAtmEngConfigDAO()
    {
	return atmEngConfigDAO;
    }

    /**
     * Set Atm engine config DAO
     * 
     * @param atmEngineConfigDAO
     */
    public void setAtmEngConfigDAO(AtmEngineConfigDAO atmEngineConfigDAO)
    {
	this.atmEngConfigDAO = atmEngineConfigDAO;
    }
}
