package com.path.atm.vo.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a transaction type.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class TransactionTypeCO implements Cloneable
{

    /**
     * Hold acquirer id
     */
    private BigDecimal acquirerId;

    /**
     * Hold Transaction type
     */
    private BigDecimal trxType;

    /**
     * Hold Expression
     */
    private String expression;

    /**
     * Hold list of fees
     */
    private List<TransactionTypeCO> listOfFees = new ArrayList<TransactionTypeCO>();

    /**
     * @return the acquirerId
     */
    public BigDecimal getAcquirerId()
    {
	return acquirerId;
    }

    /**
     * @param acquirerId the acquirerId to set
     */
    public void setAcquirerId(BigDecimal acquirerId)
    {
	this.acquirerId = acquirerId;
    }

    /**
     * @return the trxType
     */
    public BigDecimal getTrxType()
    {
	return trxType;
    }

    /**
     * @param trxType the trxType to set
     */
    public void setTrxType(BigDecimal trxType)
    {
	this.trxType = trxType;
    }

    /**
     * @return the expression
     */
    public String getExpression()
    {
	return expression;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(String expression)
    {
	this.expression = expression;
    }

    /**
     * @return the listOfFees
     */
    public List<TransactionTypeCO> getListOfFees()
    {
	return listOfFees;
    }

    /**
     * @param listOfFees the listOfFees to set
     */
    public void setListOfFees(List<TransactionTypeCO> listOfFees)
    {
	this.listOfFees = listOfFees;
    }
}
