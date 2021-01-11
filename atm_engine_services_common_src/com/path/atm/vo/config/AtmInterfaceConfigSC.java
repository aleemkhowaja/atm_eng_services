
package com.path.atm.vo.config;

import java.math.BigDecimal;
import com.path.struts2.lib.common.BaseSC;

public class AtmInterfaceConfigSC extends BaseSC {

	/**
	 * Interface id
	 */
	private BigDecimal code;

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
}
