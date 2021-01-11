package com.path.atm.dao.engine.impl;

import java.util.List;

import org.apache.ibatis.cache.Cache;

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
import com.path.lib.common.base.BaseDAO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.exception.DAOException;
import com.path.lib.log.Log;

/**
 * This class house all behaviors and functionality related to atm engine
 * configuration.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngineConfigDAOImpl extends BaseDAO implements AtmEngineConfigDAO {

	/**
	 * Clear Mapping cache
	 * @throws DAOException
	 */
	public void clearMappingCache() throws DAOException {
		try {
			Cache cache = getSqlMap().getSqlSession().getConfiguration().getCache("webServiceMapper");
			if (cache != null) {
				cache.clear();
			}
		} catch (Exception exp) {
			// we should add a try{}catch because the getCache("companiesMapper") will throw
			// IllegalStateException if "companiesMapper" is not yet in cache or if the
			// cache is still empty
			Log.getInstance().error(exp,"Clear Mapping cache failed due :" + exp.getMessage());
		}
	}
	
	
	/**
	 * Return Channel configuration related to a specific interface
	 * @param configSC
	 * @return
	 * @throws DAOException
	 */
	public AtmChannelConfigCO returnChannelConfig(AtmInterfaceConfigSC configSC) throws DAOException {
		return  (AtmChannelConfigCO)getSqlMap().queryForObject("atmEngineConfigMapper.returnChannelConfig", configSC);
	}
	
	/**
	 * Return the last used authentication code by interface
	 * @param atmInterfaceConfigSC
	 * @return
	 * @throws DAOException
	 */
	public long returnLastAuthenticationId(AtmInterfaceConfigSC configSC) throws DAOException
	{
		return (long) getSqlMap().queryForObject(
				"atmEngineConfigMapper.returnLastAuthenticationId", configSC);
	}
	
	
	/**
	 * Return Next Message id
	 * @param atmInterfaceConfigSC
	 * @return
	 * @throws DAOException
	 */
	public long returnNextMessageId(AtmInterfaceConfigSC atmInterfaceConfigSC) throws DAOException{
		return (long) getSqlMap().queryForObject(
				"atmEngineConfigMapper.returnNextMessageId", atmInterfaceConfigSC);
	}
	
	
	/**
	 * @param atmInterfaceConfigSC
	 * @return
	 * @throws DAOException
	 */
	public List<AtmIsoResponseMapCO> returnIsoResponseMappingList(AtmInterfaceConfigSC configSC) 
			throws DAOException
	{
	    return  (List<AtmIsoResponseMapCO>)getSqlMap().
			queryForList("atmEngineConfigMapper.returnIsoResponseMappingList", configSC);
	}
	
	
	/**
	 * return ISO Message Definition
	 * 
	 * @param configSc
	 * @return
	 * @throws DAOException
	 */
	public List<AtmIsoMessageDefCO> returnIsoMsgDefList(AtmInterfaceConfigSC configSc) throws DAOException{
	    return  (List<AtmIsoMessageDefCO>)getSqlMap().
			queryForList("atmEngineConfigMapper.returnIsoMsgDefList", configSc);
	}
	
	
	/**
	 * Return list of ISO fields definition by interface id
	 * @param configSc 
	 * @return 
	 * @throws BaseException
	 */
	@SuppressWarnings("unchecked")
	public List<AtmIsoFieldCO> returnIsoFieldsDefByInt(AtmInterfaceConfigSC configSc) throws DAOException {
		return  (List<AtmIsoFieldCO>)getSqlMap().
				queryForList("atmEngineConfigMapper.returnIsoFieldsDefByInt", configSc);
	}

	/**
	 * Return Atm interface co with list of attached iso messages
	 * @param configSc
	 * @return
	 * @throws DAOException
	 */
	public AtmInterfaceCO returnInterfaceInfo(AtmInterfaceConfigSC configSc) throws DAOException {
		return (AtmInterfaceCO) getSqlMap().queryForObject("atmEngineConfigMapper.returnInterfaceInfo", configSc);
	}
	
	
	/**
	 * Return List of Iso messages based on the given criteria
	 * @param configSC
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<AtmIsoMessageCO> returnIsoMessages(AtmInterfaceConfigSC configSC) throws DAOException{
		return  (List<AtmIsoMessageCO>)getSqlMap().
				queryForList("atmEngineConfigMapper.returnIsoMessages", configSC);
	}
	
	/**
	 * Return ATM Network Msg Request and Response Fields
	 * @param configSC
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<AtmIsoNetMsgFldsCO> returnAtmNetMsgFldsList(AtmInterfaceConfigSC configSC) throws DAOException
	{
	    return  (List<AtmIsoNetMsgFldsCO>)getSqlMap().
			queryForList("atmEngineConfigMapper.returnAtmNetMsgFldsList", configSC);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AcquirerCO> returnAcquirerList(AcquirerSC acquirer) throws DAOException {
		return  (List<AcquirerCO>)getSqlMap().
				queryForList("atmEngineConfigMapper.returnAcquirerList", acquirer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionTypeCO> returnTrxTypeList(AcquirerSC acquirer) throws DAOException {
		return  (List<TransactionTypeCO>)getSqlMap().
				queryForList("atmEngineConfigMapper.returnTrxTypeList", acquirer);
	}

	

	@Override
	public List<TerminalCO> returnTerminals(TerminalSC termnialSC) throws DAOException {
		return (List<TerminalCO>) getSqlMap().
			queryForList("atmEngineConfigMapper.returnTerminals", termnialSC);
	}
}
