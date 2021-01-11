package com.path.atm.vo.engine;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Represent Acquirer.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AcquirerCO
{
    /**
     * Hold acquirer id
     */
    private BigDecimal id;

    /**
     * Hold company code
     */
    private BigDecimal compCode;

    /**
     * Hold description
     */
    private String description;

    /**
     * 
     */
    private String msgSource;

    /**
     * 
     */
    private String bankAtmYN;

    /**
     * 
     */
    private BigDecimal trxBranchCode;

    /**
     * 
     */
    private BigDecimal tellerCode;

    /**
     * 
     */
    private String tellerUserId;

    /**
     * hold interface code
     */
    private BigDecimal interfaceCode;

    /**
     * Hold the list of trx type related to this acquirer
     */
    private HashMap<BigDecimal, TransactionTypeCO> transactionTypes;
    
    /**
     * Hold the list of trx type related to this acquirer
     */
    private HashMap<String, TerminalCO> terminals;
    
    /**
     * Hold the trx type Joined expressions
     */
    private String trxTypesJoinedExp;

    /**
     * @return the id
     */
    public BigDecimal getId()
    {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(BigDecimal id)
    {
	this.id = id;
    }

    /**
     * @return the compCode
     */
    public BigDecimal getCompCode()
    {
	return compCode;
    }

    /**
     * @param compCode the compCode to set
     */
    public void setCompCode(BigDecimal compCode)
    {
	this.compCode = compCode;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
	return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
	this.description = description;
    }

    /**
     * @return the msgSource
     */
    public String getMsgSource()
    {
	return msgSource;
    }

    /**
     * @param msgSource the msgSource to set
     */
    public void setMsgSource(String msgSource)
    {
	this.msgSource = msgSource;
    }

    /**
     * @return the bankAtmYN
     */
    public String getBankAtmYN()
    {
	return bankAtmYN;
    }

    /**
     * @param bankAtmYN the bankAtmYN to set
     */
    public void setBankAtmYN(String bankAtmYN)
    {
	this.bankAtmYN = bankAtmYN;
    }

    /**
     * @return the trxBranchCode
     */
    public BigDecimal getTrxBranchCode()
    {
	return trxBranchCode;
    }

    /**
     * @param trxBranchCode the trxBranchCode to set
     */
    public void setTrxBranchCode(BigDecimal trxBranchCode)
    {
	this.trxBranchCode = trxBranchCode;
    }

    /**
     * @return the tellerCode
     */
    public BigDecimal getTellerCode()
    {
	return tellerCode;
    }

    /**
     * @param tellerCode the tellerCode to set
     */
    public void setTellerCode(BigDecimal tellerCode)
    {
	this.tellerCode = tellerCode;
    }

    /**
     * @return the tellerUserId
     */
    public String getTellerUserId()
    {
	return tellerUserId;
    }

    /**
     * @param tellerUserId the tellerUserId to set
     */
    public void setTellerUserId(String tellerUserId)
    {
	this.tellerUserId = tellerUserId;
    }

    /**
     * @return the interfaceCode
     */
    public BigDecimal getInterfaceCode()
    {
	return interfaceCode;
    }

    /**
     * @param interfaceCode the interfaceCode to set
     */
    public void setInterfaceCode(BigDecimal interfaceCode)
    {
	this.interfaceCode = interfaceCode;
    }

    /**
     * @return the transactionTypes
     */
    public HashMap<BigDecimal, TransactionTypeCO> getTransactionTypes()
    {
        return transactionTypes;
    }

    /**
     * @param transactionTypes the transactionTypes to set
     */
    public void setTransactionTypes(HashMap<BigDecimal, TransactionTypeCO> transactionTypes)
    {
        this.transactionTypes = transactionTypes;
    }

    /**
     * @return the trxTypesJoinedExp
     */
    public String getTrxTypesJoinedExp()
    {
	return trxTypesJoinedExp;
    }

    /**
     * @param trxTypesJoinedExp the trxTypesJoinedExp to set
     */
    public void setTrxTypesJoinedExp(String trxTypesJoinedExp)
    {
	this.trxTypesJoinedExp = trxTypesJoinedExp;
    }

    /**
     * @return the terminals
     */
    public HashMap<String, TerminalCO> getTerminals()
    {
        return terminals;
    }

    /**
     * @param terminals the terminals to set
     */
    public void setTerminals(HashMap<String, TerminalCO> terminals)
    {
        this.terminals = terminals;
    }
}
