package com.path.atm.dao.engine;

import java.util.List;

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
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.exception.DAOException;

/**
 * @author MohammadAliMezzawi
 *
 */
public interface AtmEngineConfigDAO
{

    /**
     * Clear Mapping cache
     * 
     * @throws DAOException
     */
    public void clearMappingCache() throws DAOException;

    /**
     * Return Channel configuration related to a specific interface
     * 
     * @param configSC
     * @return
     * @throws DAOException
     */
    public AtmChannelConfigCO returnChannelConfig(AtmInterfaceConfigSC configSC) throws DAOException;

    /**
     * Return the last authentication code by interface
     * 
     * @param atmInterfaceConfigSC
     * @return
     * @throws DAOException
     */
    public long returnLastAuthenticationId(AtmInterfaceConfigSC configSC) throws DAOException;

    /**
     * Return Next Message id
     * 
     * @param atmInterfaceConfigSC
     * @return
     * @throws DAOException
     */
    public long returnNextMessageId(AtmInterfaceConfigSC atmInterfaceConfigSC) throws DAOException;

    /**
     * @param configSC
     * @return
     * @throws DAOException
     */
    public List<AtmIsoResponseMapCO> returnIsoResponseMappingList(AtmInterfaceConfigSC configSC) throws DAOException;

    /**
     * return ISO Message Definition
     * 
     * @param configSc
     * @return
     * @throws DAOException
     */
    public List<AtmIsoMessageDefCO> returnIsoMsgDefList(AtmInterfaceConfigSC configSc) throws DAOException;

    /**
     * Return list of ISO fields definition by interface id
     * 
     * @param configSc
     * @return
     * @throws BaseException
     */
    public List<AtmIsoFieldCO> returnIsoFieldsDefByInt(AtmInterfaceConfigSC configSc) throws DAOException;

    /**
     * Return Atm interface co with list of attached iso messages
     * 
     * @param configSc
     * @return
     * @throws DAOException
     */
    public AtmInterfaceCO returnInterfaceInfo(AtmInterfaceConfigSC configSc) throws DAOException;

    /**
     * Return List of Iso messages based on the given criteria
     * 
     * @param configSC
     * @return
     * @throws DAOException
     */
    public List<AtmIsoMessageCO> returnIsoMessages(AtmInterfaceConfigSC configSC) throws DAOException;

    /**
     * Return ATM Network Msg Request and Response Fields
     * 
     * @param configSC
     * @return
     * @throws DAOException
     */
    public List<AtmIsoNetMsgFldsCO> returnAtmNetMsgFldsList(AtmInterfaceConfigSC configSC) throws DAOException;

    /**
     * Return list of Acquirer based on sc criteria
     * 
     * @param acquirer
     * @return
     * @throws DAOException
     */
    public List<AcquirerCO> returnAcquirerList(AcquirerSC acquirer) throws DAOException;

    /**
     * @param termnialSC
     * @return
     * @throws DAOException
     */
    public List<TerminalCO> returnTerminals(TerminalSC termnialSC) throws DAOException;

    /**
     * @param acquirer
     * @return
     * @throws DAOException
     */
    public List<TransactionTypeCO> returnTrxTypeList(AcquirerSC acquirer) throws DAOException;

}
