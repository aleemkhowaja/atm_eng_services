package com.path.atm.vo.engine;

import java.math.BigDecimal;

/**
 * This class represent the terminal
 * @author MohammadAliMezzawi
 *
 */
public class TerminalCO {
	
	/**
	 * 
	 */
	private String code;
	
	/**
	 * 
	 */
	private BigDecimal compCode;
	
	/**
	 * 
	 */
	private BigDecimal terminalId;
	
	/**
	 * 
	 */
	private String description;
	
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
	 * 
	 */
	private String address;
	
	/**
	 * 
	 */
	private String telephone;
	
	/**
	 * 
	 */
	private String email;
	
	/**
	 * 
	 */
	private String expression;
	
	/**
	 * 
	 */
	private BigDecimal interfaceCode;
	
	/**
	 * Hold acquirer id
	 */
	private BigDecimal acquirerId;
	
	/**
	 * Merchant CO holder
	 */
	private MerchantCO merchantCO;

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
	 * @return the compCode
	 */
	public BigDecimal getCompCode() {
		return compCode;
	}

	/**
	 * @param compCode the compCode to set
	 */
	public void setCompCode(BigDecimal compCode) {
		this.compCode = compCode;
	}

	/**
	 * @return the terminalId
	 */
	public BigDecimal getTerminalId() {
		return terminalId;
	}

	/**
	 * @param terminalId the terminalId to set
	 */
	public void setTerminalId(BigDecimal terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the trxBranchCode
	 */
	public BigDecimal getTrxBranchCode() {
		return trxBranchCode;
	}

	/**
	 * @param trxBranchCode the trxBranchCode to set
	 */
	public void setTrxBranchCode(BigDecimal trxBranchCode) {
		this.trxBranchCode = trxBranchCode;
	}

	/**
	 * @return the tellerCode
	 */
	public BigDecimal getTellerCode() {
		return tellerCode;
	}

	/**
	 * @param tellerCode the tellerCode to set
	 */
	public void setTellerCode(BigDecimal tellerCode) {
		this.tellerCode = tellerCode;
	}

	/**
	 * @return the tellerUserId
	 */
	public String getTellerUserId() {
		return tellerUserId;
	}

	/**
	 * @param tellerUserId the tellerUserId to set
	 */
	public void setTellerUserId(String tellerUserId) {
		this.tellerUserId = tellerUserId;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
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

	/**
	 * @return the merchantCO
	 */
	public MerchantCO getMerchantCO() {
		return merchantCO;
	}

	/**
	 * @param merchantCO the merchantCO to set
	 */
	public void setMerchantCO(MerchantCO merchantCO) {
		this.merchantCO = merchantCO;
	}
}
