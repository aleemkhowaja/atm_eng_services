package com.path.atm.vo.engine;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.path.lib.vo.BaseVO;

/**
 * Represent Atm interface.
 * <p>it will contain the main atm interface info plus a list of available AtmIsoMsgDefCO
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmIsoFieldCO extends BaseVO{
	
	/**
	 * Hold atm Iso field interfaceCode
	 */
	private BigDecimal interfaceCode;
	
	/**
	 * Hold atm Iso field code
	 */
	private BigDecimal code;
	
	/**
	 * Hold atm Iso field fieldCode
	 */
	private BigDecimal fieldCode;
	
	/**
	 * Hold atm Iso field index
	 */
	private BigDecimal index;
	
	/**
	 * Hold atm Iso field name
	 */
	private String name;
	
	/**
	 * Hold atm Iso field type
	 */
	private String type;
	
	/**
	 * Hold atm Iso field fixedLength
	 */
	private BigDecimal fixedLength;
	
	/**
	 * Hold atm Iso field dynamicLength
	 */
	private BigDecimal dynamicLength;
	
	/**
	 * Hold atm Iso field totalMask
	 */
	private String totalMask;
	
	/**
	 * Hold atm Iso field partialMask
	 */
	private String partialMask;

	/**
	 * Hold atm Iso field isDecimal
	 */
	private String isDecimal;

	/**
	 * Hold atm Iso field expression
	 */
	private String expression;
	
	/**
	 *  Hold atm Iso field iso Field SubList
	 */
	private ArrayList<AtmIsoFieldCO> isoFieldSubList;
	

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
	 * @return the fieldCode
	 */
	public BigDecimal getFieldCode() {
		return fieldCode;
	}

	/**
	 * @param fieldCode the fieldCode to set
	 */
	public void setFieldCode(BigDecimal fieldCode) {
		this.fieldCode = fieldCode;
	}

	/**
	 * @return the index
	 */
	public BigDecimal getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(BigDecimal index) {
		this.index = index;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the fixedLength
	 */
	public BigDecimal getFixedLength() {
		return fixedLength;
	}

	/**
	 * @param fixedLength the fixedLength to set
	 */
	public void setFixedLength(BigDecimal fixedLength) {
		this.fixedLength = fixedLength;
	}

	/**
	 * @return the dynamicLength
	 */
	public BigDecimal getDynamicLength() {
		return dynamicLength;
	}

	/**
	 * @param dynamicLength the dynamicLength to set
	 */
	public void setDynamicLength(BigDecimal dynamicLength) {
		this.dynamicLength = dynamicLength;
	}

	/**
	 * @return the totalMask
	 */
	public String getTotalMask() {
		return totalMask;
	}

	/**
	 * @param totalMask the totalMask to set
	 */
	public void setTotalMask(String totalMask) {
		this.totalMask = totalMask;
	}

	/**
	 * @return the partialMask
	 */
	public String getPartialMask() {
		return partialMask;
	}

	/**
	 * @param partialMask the partialMask to set
	 */
	public void setPartialMask(String partialMask) {
		this.partialMask = partialMask;
	}

	/**
	 * @return the isDecimal
	 */
	public String getIsDecimal() {
		return isDecimal;
	}

	/**
	 * @param isDecimal the isDecimal to set
	 */
	public void setIsDecimal(String isDecimal) {
		this.isDecimal = isDecimal;
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
	 * @return the isoFieldSubList
	 */
	public ArrayList<AtmIsoFieldCO> getIsoFieldSubList() {
		return isoFieldSubList;
	}

	/**
	 * @param isoFieldSubList the isoFieldSubList to set
	 */
	public void setIsoFieldSubList(ArrayList<AtmIsoFieldCO> isoFieldSubList) {
		this.isoFieldSubList = isoFieldSubList;
	}
	
}
