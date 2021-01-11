package com.path.atm.vo.engine;

import java.math.BigDecimal;
import com.path.lib.vo.BaseVO;

/**
 * Represent Atm Message.
 * <p>it will contain the main atm interface info plus a list of available AtmIsoMsgDefCO
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmIsoMessageCO extends BaseVO{
	
	/**
	 * Hold atm Msg field code
	 */
	private BigDecimal code;
	
	/**
	 * Hold atm Msg field requestMti
	 */
	private String requestMti;
	
	/**
	 * Hold atm Msg field reqPrimaryBitMap
	 */
	private String reqPrimaryBitMap;
	
	/**
	 * Hold atm Msg field reqSecondaryBitMap
	 */
	private String reqSecondaryBitMap;
	
	/**
	 * Hold atm Msg field responseMti
	 */
	private String responseMti;
	
	/**
	 * Hold atm Msg field respPrimaryBitMap
	 */
	private String respPrimaryBitMap;
	
	/**
	 * Hold atm Msg field respSecondaryBitMap
	 */
	private String respSecondaryBitMap;
	
	/**
	 * Hold atm Msg field respExpression
	 */
	private String respExpression;
	
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
	 * @return the requestMti
	 */
	public String getRequestMti() {
		return requestMti;
	}

	/**
	 * @param requestMti the requestMti to set
	 */
	public void setRequestMti(String requestMti) {
		this.requestMti = requestMti;
	}

	/**
	 * @return the reqPrimaryBitMap
	 */
	public String getReqPrimaryBitMap() {
		return reqPrimaryBitMap;
	}

	/**
	 * @param reqPrimaryBitMap the reqPrimaryBitMap to set
	 */
	public void setReqPrimaryBitMap(String reqPrimaryBitMap) {
		this.reqPrimaryBitMap = reqPrimaryBitMap;
	}

	/**
	 * @return the reqSecondaryBitMap
	 */
	public String getReqSecondaryBitMap() {
		return reqSecondaryBitMap;
	}

	/**
	 * @param reqSecondaryBitMap the reqSecondaryBitMap to set
	 */
	public void setReqSecondaryBitMap(String reqSecondaryBitMap) {
		this.reqSecondaryBitMap = reqSecondaryBitMap;
	}

	/**
	 * @return the responseMti
	 */
	public String getResponseMti() {
		return responseMti;
	}

	/**
	 * @param responseMti the responseMti to set
	 */
	public void setResponseMti(String responseMti) {
		this.responseMti = responseMti;
	}

	/**
	 * @return the respPrimaryBitMap
	 */
	public String getRespPrimaryBitMap() {
		return respPrimaryBitMap;
	}

	/**
	 * @param respPrimaryBitMap the respPrimaryBitMap to set
	 */
	public void setRespPrimaryBitMap(String respPrimaryBitMap) {
		this.respPrimaryBitMap = respPrimaryBitMap;
	}

	/**
	 * @return the respSecondaryBitMap
	 */
	public String getRespSecondaryBitMap() {
		return respSecondaryBitMap;
	}

	/**
	 * @param respSecondaryBitMap the respSecondaryBitMap to set
	 */
	public void setRespSecondaryBitMap(String respSecondaryBitMap) {
		this.respSecondaryBitMap = respSecondaryBitMap;
	}

	/**
	 * @return the respExpression
	 */
	public String getRespExpression() {
		return respExpression;
	}

	/**
	 * @param respExpression the respExpression to set
	 */
	public void setRespExpression(String respExpression) {
		this.respExpression = respExpression;
	}
}
