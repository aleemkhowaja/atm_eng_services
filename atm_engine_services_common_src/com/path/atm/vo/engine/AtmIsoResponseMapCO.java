package com.path.atm.vo.engine;

import java.math.BigDecimal;
import com.path.lib.vo.BaseVO;

/**
 * Represent Atm interface.
 * <p>
 * it will contain the main atm interface info plus a list of available
 * AtmIsoMsgDefCO
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmIsoResponseMapCO extends BaseVO {

	/**
	 * Hold Engine iso response map id
	 */
	private BigDecimal atmIsoResponseMapId;
	
	/**
	 * Hold interface Code
	 */
	private BigDecimal interfaceCode;

	/**
	 * Hold response type
	 */
	private String responseType;

	/**
	 * Hold Core Status Code
	 */
	private String coreStatusCode;

	/**
	 * Hold ISO Status Code
	 */
	private String isoStatusCode;

	/**
	 * @return
	 */
	public BigDecimal getAtmIsoResponseMapId() {
		return atmIsoResponseMapId;
	}

	/**
	 * @param atmIsoResponseMapId
	 */
	public void setAtmIsoResponseMapId(BigDecimal atmIsoResponseMapId) {
		this.atmIsoResponseMapId = atmIsoResponseMapId;
	}

	/**
	 * @return
	 */
	public String getResponseType() {
		return responseType;
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
	 * @param responseType
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * @return
	 */
	public String getCoreStatusCode() {
		return coreStatusCode;
	}

	/**
	 * @param coreStatusCode
	 */
	public void setCoreStatusCode(String coreStatusCode) {
		this.coreStatusCode = coreStatusCode;
	}

	/**
	 * @return
	 */
	public String getIsoStatusCode() {
		return isoStatusCode;
	}

	/**
	 * @param isoStatusCode
	 */
	public void setIsoStatusCode(String isoStatusCode) {
		this.isoStatusCode = isoStatusCode;
	}
}