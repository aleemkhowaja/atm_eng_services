package com.path.atm.bo.engine;

import java.math.BigDecimal;
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

/**
 * This BO will control the atm engine actions
 * @author Mohammad Ali Mezzawi
 * 
 * @Note
 * This interface should only contain
 * 1 - Start
 * 2 - Stop
 * Any object used here should be in Alert common area
 * since this bo is linked to alert services
 */
public interface AtmEngineConfigBO {
	
	/**
	 * Clear the Mapping cache
	 * @throws BaseException
	 */
	public void clearMappingCache() throws BaseException;
	
	/**
	 * Return Atm Channel Configuration
	 * @param configSC
	 * @return
	 * @throws BaseException
	 */
	public AtmChannelConfigCO returnChannelConfig(AtmInterfaceConfigSC configSC) throws BaseException;

	/**
	 * return Last Authentication Code
	 * @param atmInterfaceConfigSC
	 * @return
	 * @throws BaseException
	 */
	public long returnLastAuthenticationId(AtmInterfaceConfigSC configSc) throws BaseException;
	
	/**
	 * Return Next Message id
	 * @param configSc
	 * @return
	 * @throws BaseException
	 */
	public long returnNextMessageId(AtmInterfaceConfigSC configSc) throws BaseException;
	
	
	/**
	 * Return list of iso response mapping by interface
	 * @param atmInterfaceConfigSC
	 * @return
	 * @throws BaseException
	 */
	public List<AtmIsoResponseMapCO> returnIsoResponseMappingList(AtmInterfaceConfigSC configSc) throws BaseException;

	/**
	 * return ISO Message Definition
	 * 
	 * @param configSc
	 * @return
	 * @throws BaseException
	 */
	public List<AtmIsoMessageDefCO> returnIsoMsgDefList(AtmInterfaceConfigSC configSc) throws BaseException;

	/**
	 * @param configSc
	 * @return
	 * @throws BaseException
	 */
	public List<AtmIsoFieldCO> returnIsoFieldsDefByInt(AtmInterfaceConfigSC configSc) throws BaseException;
	
	/**
	 * Return Atm interface co with list of attached iso messages
	 * @param configSc
	 * @return
	 * @throws BaseException
	 */
	public AtmInterfaceCO returnInterfaceInfo(AtmInterfaceConfigSC configSc) throws BaseException;
	
	/**
	 * Return List of Iso messages based on the given criteria
	 * @param configSC
	 * @return
	 * @throws BaseException
	 */
	public List<AtmIsoMessageCO> returnIsoMessages(AtmInterfaceConfigSC configSC)throws BaseException;
	
	/**
	 * Return ATM Network Msg Request and Response Fields
	 * @param configSC
	 * @return
	 * @throws BaseException
	 */
	public List<AtmIsoNetMsgFldsCO> returnAtmNetMsgFldsList(AtmInterfaceConfigSC configSC) throws BaseException;
	
	/**
	 * Return list of acquirer
	 * @param acquirerSC
	 * @return
	 * @throws BaseException
	 */
	public List<AcquirerCO> returnAcquirerList(AcquirerSC acquirerSC) throws BaseException;
	
	/**
	 * @param acquirer
	 * @return
	 * @throws BaseException
	 */
	public List<TransactionTypeCO> returnTrxTypeList(AcquirerSC acquirer) throws BaseException;
	
	/**
	 * Return terminal details
	 * @param termnialSC
	 * @return
	 * @throws BaseException
	 */
	public List<TerminalCO> returnTerminals(TerminalSC termnialSC) throws BaseException;

}