package com.path.atm.vo.engine;

import java.math.BigDecimal;

import com.path.struts2.lib.common.BaseSC;

/**
 * This class hold the Acquirer search criteria behavior 
 * @author MohammadAliMezzawi
 *
 */
public class AcquirerSC extends BaseSC{

	/**
	 * Hold Acquirer
	 */
	private BigDecimal id;
	
	/**
	 * interface code
	 */
	private BigDecimal interfaceCode;
	
	/**
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

	/**
	 * @return the interfaceCode
	 */
	public BigDecimal getInterfaceCode() {
		return interfaceCode;
	}

	/**
	 * @param interfaceCode the interfaceCode to set
	 */
	public void setInterfaceCode(BigDecimal interfaceCode) {
		this.interfaceCode = interfaceCode;
	}
}
