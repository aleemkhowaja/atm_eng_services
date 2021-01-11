package com.path.atm.engine.util;

import java.math.BigDecimal;

/**
 * Hold all behaviors related to the engine utilities
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngUtil {
	
	/**
	 * Return the reactor Key which will be used to identify
	 * a specific reactor
	 * @param interfaceId
	 */
	public static String returnReactorKey(BigDecimal interfaceId) {
		return AtmEngineConstants.REACTOR_NAME_PREFIX
				.concat(interfaceId.toPlainString());
	}
}
