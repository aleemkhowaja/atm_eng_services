package com.path.atm.vo.engine;

import java.math.BigDecimal;
import java.util.List;

import com.path.lib.vo.BaseVO;

/**
 * Represent Atm Message Definition.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmIsoMessageDefCO extends BaseVO{
	
	/**
	 * Hold Iso Message Def id
	 */
	private BigDecimal id;
	
	/**
	 * Hold Iso Message Def description
	 */
	private String description;
	
	/**
	 * Hold Iso Message Def interfaceCode
	 */
	private BigDecimal interfaceCode;
	
	/**
	 * Hold Iso Message Def networkMessageCodeYN
	 */
	private String networkMessageCodeYN;
	
	/**
	 * Hold Iso Message Def networkMessageType
	 */
	private String networkMessageType;
	
	/**
	 * Hold Iso Message Def atmIsoMsgCode
	 */
	private BigDecimal atmIsoMsgCode;
	
	/**
	 * Hold Iso Message Def reqMTI
	 */
	private String reqMTI;
	
	/**
	 * Hold Iso Message Def processCode
	 */
	private String processCode;
	
	/**
	 * Hold Iso Message Def criteriaExpression
	 */
	private String criteriaExpression;
	
	/**
	 * Hold Iso Message Def mappingId
	 */
	private BigDecimal mappingId;
	
	/**
	 * Hold records of Network Request Fields
	 */
	private List<AtmIsoNetMsgFldsCO> netReqFields;

	/**
	 * Hold records of Network Response Fields
	 */
	private List<AtmIsoNetMsgFldsCO> netRespFields;

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
	 * @return the networkMessageCodeYN
	 */
	public String getNetworkMessageCodeYN() {
		return networkMessageCodeYN;
	}

	/**
	 * @param networkMessageCodeYN the networkMessageCodeYN to set
	 */
	public void setNetworkMessageCodeYN(String networkMessageCodeYN) {
		this.networkMessageCodeYN = networkMessageCodeYN;
	}

	/**
	 * @return the networkMessageType
	 */
	public String getNetworkMessageType() {
		return networkMessageType;
	}

	/**
	 * @param networkMessageType the networkMessageType to set
	 */
	public void setNetworkMessageType(String networkMessageType) {
		this.networkMessageType = networkMessageType;
	}

	/**
	 * @return the atmIsoMsgCode
	 */
	public BigDecimal getAtmIsoMsgCode() {
		return atmIsoMsgCode;
	}

	/**
	 * @param atmIsoMsgCode the atmIsoMsgCode to set
	 */
	public void setAtmIsoMsgCode(BigDecimal atmIsoMsgCode) {
		this.atmIsoMsgCode = atmIsoMsgCode;
	}

	/**
	 * @return the reqMTI
	 */
	public String getReqMTI() {
		return reqMTI;
	}

	/**
	 * @param reqMTI the reqMTI to set
	 */
	public void setReqMTI(String reqMTI) {
		this.reqMTI = reqMTI;
	}

	/**
	 * @return the processCode
	 */
	public String getProcessCode() {
		return processCode;
	}

	/**
	 * @param processCode the processCode to set
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	/**
	 * @return the criteriaExpression
	 */
	public String getCriteriaExpression() {
		return criteriaExpression;
	}

	/**
	 * @param criteriaExpression the criteriaExpression to set
	 */
	public void setCriteriaExpression(String criteriaExpression) {
		this.criteriaExpression = criteriaExpression;
	}

	/**
	 * @return the mappingId
	 */
	public BigDecimal getMappingId() {
		return mappingId;
	}

	/**
	 * @param mappingId the mappingId to set
	 */
	public void setMappingId(BigDecimal mappingId) {
		this.mappingId = mappingId;
	}

	/**
	 * @return the netReqFields
	 */
	public List<AtmIsoNetMsgFldsCO> getNetReqFields() {
		return netReqFields;
	}

	/**
	 * @param netReqFields the netReqFields to set
	 */
	public void setNetReqFields(List<AtmIsoNetMsgFldsCO> netReqFields) {
		this.netReqFields = netReqFields;
	}

	/**
	 * @return the netRespFields
	 */
	public List<AtmIsoNetMsgFldsCO> getNetRespFields() {
		return netRespFields;
	}

	/**
	 * @param netRespFields the netRespFields to set
	 */
	public void setNetRespFields(List<AtmIsoNetMsgFldsCO> netRespFields) {
		this.netRespFields = netRespFields;
	}

}
