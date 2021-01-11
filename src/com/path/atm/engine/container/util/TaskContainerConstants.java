package com.path.atm.engine.container.util;

public class TaskContainerConstants {

    // AMQ Transport protocols
    public static final String DEFAULT_TRANSPORT_PROTOCOL = "tcp";
    public static final String VM_TRANSPORT_PROTOCOL = "vm";
    public static final String NIO_TRANSPORT_PROTOCOL = "nio";
    public static final String TCP_TRANSPORT_PROTOCOL = "tcp";
    
    // failover prefix
	public static final Object FAILOVER_WRAPPER_PREFIX = "failover:(";
	public static final Object FAILOVER_WRAPPER_SUFFIX = ")";
}
