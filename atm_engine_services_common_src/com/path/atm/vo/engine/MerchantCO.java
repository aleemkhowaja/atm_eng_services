package com.path.atm.vo.engine;

import java.math.BigDecimal;

/**
 * This Class house the merchant behavior
 * @author MohammadAliMezzawi
 *
 */
public class MerchantCO {
	
	/**
	 * Hold Merchant code
	 */
	private BigDecimal code;
	
	/**
	 * Hold additional Reference
	 */
	private String additionalRef;
	
	/**
	 * Hold iban Acc No
	 */
	private String ibanAccNo;

	/**
	 * @return the code
	 */
	public BigDecimal getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(BigDecimal code) {
		this.code = code;
	}

	/**
	 * @return the additionalRef
	 */
	public String getAdditionalRef() {
		return additionalRef;
	}

	/**
	 * @param additionalRef the additionalRef to set
	 */
	public void setAdditionalRef(String additionalRef) {
		this.additionalRef = additionalRef;
	}

	/**
	 * @return the ibanAccNo
	 */
	public String getIbanAccNo() {
		return ibanAccNo;
	}

	/**
	 * @param ibanAccNo the ibanAccNo to set
	 */
	public void setIbanAccNo(String ibanAccNo) {
		this.ibanAccNo = ibanAccNo;
	}
}
