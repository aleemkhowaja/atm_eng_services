package com.path.atm.engine.util.concurrent;

import java.util.Date;

//https://github.com/infinispan/infinispan/blob/796a53067866f0565b6c4dcaa254f88db6585423/commons/src/main/java/org/infinispan/commons/time/DefaultTimeService.java
public class TimerService {

	private Date instance;
	
	
	public static long getTime() {
		return System.currentTimeMillis();
	}
	
}
