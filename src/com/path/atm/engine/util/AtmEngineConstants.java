package com.path.atm.engine.util;

import java.nio.charset.Charset;

import io.netty.util.CharsetUtil;

public class AtmEngineConstants {

	// property file name
	public static final String PROP_FILE_NAME = "PathAtmEngineRemoting";
	
	// cluster lock name
	public static final String CLUSTER_LCK = "AtmClusterLck";
	
	// Task Pooling Mode
	public static final String TASK_POOL_MODE_MEMORY = "mem";
	public static final String TASK_POOL_MODE_MQ = "mq";

	// list of type
	public static final String ISO_FIELD_TYPE_NUMERIC = "N";
	public static final String ISO_FIELD_TYPE_BINARY = "B";
	public static final String ISO_FIELD_TYPE_ALPHA = "V";
	public static final String ISO_FIELD_TYPE_BITMAP = "T";
	public static final String ISO_FIELD_TYPE_AMOUNT = "A";
	public static final String ISO_FIELD_TYPE_DATE10 = "D";
	public static final String ISO_FIELD_TYPE_DATE4 = "M";
	public static final String ISO_FIELD_TYPE_TIME = "H";
	public static final String ISO_FIELD_TYPE_EXP_DATE = "E";
	public static final String ISO_FIELD_TYPE_DATE6 = "D6";
	public static final String ISO_FIELD_TYPE_DATE12 = "D12";
	public static final String ISO_FIELD_TYPE_DATE14 = "D14";
	
	// Message Status
	public static final String MSG_STATUS_RECEIVED = "R";
	public static final String MSG_STATUS_PROCESSED = "P";
	public static final String MSG_STATUS_FAILED = "F";
	
	// Internal Task Status
	public static final String TSK_STATUS_NEW = "N";
	public static final String TSK_STATUS_MAP_EX = "E";
	public static final String TSK_STATUS_DONE = "D";
	
	//
	public static final String REACTOR_NAME_PREFIX = "Reactor_";

	/**
	 * Type of Bitmap One : Hexadecimal Two : Binary
	 */
	public static final String BITMAP_16 = "1";
	public static final String BITMAP_8 = "2";
	
	/**
	 * Length Base type 
	 * Base 256 : Ascii code
	 * Base 10 : Decimal system
	 */
	public static final String LENGTH_BASE256 = "1";
	public static final String LENGTH_BASE10 = "2";
	
	/**
	 * Used Encoding
	 */
	public static final Charset CHAR_ENCODING = CharsetUtil.ISO_8859_1;
	//public static final Charset CHAR_ENCODING = Charset.forName("Cp1252");

	/**
	 * Connector Server and Client
	 */
	public static final String TCP_SERVER_CONNECTOR = "TS";
	public static final String TCP_CLIENT_CONNECTOR = "TC";
    
}
