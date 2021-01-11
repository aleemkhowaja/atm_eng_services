package com.path.atm.vo.engine;

import java.math.BigDecimal;
import com.path.lib.vo.BaseVO;

/**
 * 
 * Copyright 2020, Path Solutions
 * Path Solutions retains all ownership rights to this source code 
 * 
 * @author: Alim Khowaja
 *
 * Represent Atm ISO Network Message Fields
 */
public class AtmIsoNetMsgFldsCO extends BaseVO {
    
    /**
     * Hold Network Message Field Id
     */
    private BigDecimal netMsgFldId;

    /**
     * Hold ISO Message Definition Id
     */
    private BigDecimal isoMsgDefId;

    /**
     * Hold Field Code
     */
    private BigDecimal fieldCode;

    /**
     * Hold Request Field Y/N
     */
    private String requestFieldYN;

    /**
     * Hold Expression of every field
     */
    private String expression;

    public BigDecimal getNetMsgFldId()
    {
        return netMsgFldId;
    }

    public void setNetMsgFldId(BigDecimal netMsgFldId)
    {
        this.netMsgFldId = netMsgFldId;
    }

    public BigDecimal getIsoMsgDefId()
    {
        return isoMsgDefId;
    }

    public void setIsoMsgDefId(BigDecimal isoMsgDefId)
    {
        this.isoMsgDefId = isoMsgDefId;
    }

    public BigDecimal getFieldCode()
    {
        return fieldCode;
    }

    public void setFieldCode(BigDecimal fieldCode)
    {
        this.fieldCode = fieldCode;
    }

    public String getRequestFieldYN()
    {
        return requestFieldYN;
    }

    public void setRequestFieldYN(String requestFieldYN)
    {
        this.requestFieldYN = requestFieldYN;
    }

    public String getExpression()
    {
        return expression;
    }

    public void setExpression(String expression)
    {
        this.expression = expression;
    }
}