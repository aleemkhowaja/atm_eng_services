package com.path.atm.vo.engine;

import java.math.BigDecimal;

/**
 * This class hold the Acquirer search criteria behavior 
 * @author MohammadAliMezzawi
 *
 */
public class TerminalSC {

	/**
	 * Terminal code
	 */
	private String code;
	
	/**
	 * interface code
	 */
	private BigDecimal interfaceCode;
	
	/**
	 * acquirer Id
	 */
	private BigDecimal acquirerId;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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

	/**
	 * @return the acquirerId
	 */
	public BigDecimal getAcquirerId() {
		return acquirerId;
	}

	/**
	 * @param acquirerId the acquirerId to set
	 */
	public void setAcquirerId(BigDecimal acquirerId) {
		this.acquirerId = acquirerId;
	}
}
