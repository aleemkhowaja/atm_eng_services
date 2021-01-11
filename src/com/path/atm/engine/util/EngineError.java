package com.path.atm.engine.util;

public enum EngineError {
	
	/**
	 * General Error
	 * 0 -> 99
	 */
	CONF_NOTSET(1,"Configuration file not set"),
	CONF_INVALID_PROP_VALUE(2,"Property value is invalid"),
	
	/**
	 * Engine exception range
	 * 100 -> 199 
	 */
	ENG_CONF_FAIL_TO_LOAD(100, "Engine Configuration failed to load."),
	ENG_CONF_FILE_NOTSET(101, "Engine Configuration file isn't set."),
	ENG_CONF_FNAME_NOTSET(102, "Engine Configuration file name isn't set."),
	ENG_STARTUP_FAILED(103, "Engine failed to start"),
	ENG_SHUTDOWN_FAILED(104, "Engine failed to shutdown"),
	
	/**
	 * Engine Task Container exception range
	 * 200 -> 299 
	 */
	TC_CONF_NOTSET(200, "Task container Configuration isn't set."),
	TC_CONF_FAIL_TO_LOAD(201, "Failed to load Task container Configuration which could be due to invalid value."),
	TC_CONF_HOST_NOTSET(202, "Task container connector isn't set."),
	TC_CONF_WRONG_BROKER_URL(203,"Broker url is missing or null "),

	
	/**
	 * Reactor Action Range
	 * 300 -> 399
	 */
	REACTOR_CONF_FILE_NOTSET(300, "Reactor Configuration file isn't set."),
	REACTOR_CONF_FNAME_NOTSET(301, "Reactor Configuration file name isn't set."),
	REACTOR_CONF_FAIL_TO_LOAD(302, "Failed to load Reactor Configuration which could be due to invalid value."),
	REACTOR_DEF_NO_INT(303, "No interface defined for this interface code"),
	REACTOR_DEF_NO_FIELDS(304, "No Fields defined for this interface"),
	REACTOR_DEF_NO_MSG(305, "No Message defined for this interface"),
	REACTOR_DEF_NO_ACQ(306,"No Acquirer defined for this interface"),
	REACTOR_DEF_NO_TRXTYPE(307,"No transaction type defined for this interface"),
	REACTOR_DEF_NO_TRXTYPE_EXP(308,"Transaction type without expression"),
	REACTOR_DEF_DUPLICATE_NET(309, "Duplicate Network Message"),
	
	// those are general errors codes to handle reactor start and shutdown
	REACTOR_STARTUP_FAILED(310, "Failed to start the reactor"),
	REACTOR_ST_FAILED_LD_CONFIG(311,"Failed to load Configuration on startup"),
	REACTOR_ST_FAILED_INIT_TSK_SEQ(312,"Failed Create task Seq generator on startup"),
	REACTOR_ST_FAILED_LD_INTERFACE(313,"Failed to load interface data"),
	REACTOR_ST_FAILED_PREPARE_EXP(314,"Failed to prepare Expression"),
	REACTOR_ST_FAILED_INIT_ISO_FACT(315,"Failed to initiate Iso Factory"),
	REACTOR_ST_FAILED_INIT_CON(316,"Failed to initiate the Netty Connector"),
	REACTOR_ST_CON_FAILED_BIND(317,"Failed to Bind to Ip/Port"),
	REACTOR_ST_FAILED_INIT_TSK_CNT(318,"Failed to initiate client task container"),
	REACTOR_SHUTDOWN_FAILED(319, "Failed to shutdown the reactor"),
	REACTOR_SD_FAILED_CONNECTOR(320,"Failed to shutdown the connector"),
	REACTOR_SD_FAILED_TSK_CONT(321,"Failed to shutdown the task container"),
	
	
	
	
	/**
	 * Task Client container
	 * 400 -> 499
	 */
	TCLIENT_CONF_NOTSET(400, "Task Client container Configuration file isn't set."),
	TCLIENT_CONF_FAIL_TO_LOAD(401, "Task Client container Configuration file name isn't set."),
	
	/**
	 * Connector exception range 
	 * 500 -> 599
	 */
	CON_CONF_NOTSET(500, "Connector Configuration file isn't set."),
	CON_CONF_HOST_NOTSET(501, "Connector Host/port isn't set."),
	CON_CONF_PORT_OUTRANGE(502, "Port out of range"),
	CON_CONF_FAIL_TO_LOAD(503, "Failed to load Connector Configuration which could be due to invalid value"),
	CON_CONF_MISSING_SERVER_TYPE(504, "Missing Server type (TS/TC)"),
	
	/**
	 * Message Factory Range
	 * 600 -> 699
	 */
	MSG_FACT_FAILED_TO_INIT(600,"Failed to create message facotry"), // not used recheck it
	MSG_FACT_FAILED_TO_CONF(601,"Failed to create message facotry"),
	MSG_FACT_WRONG_FIELD_CONF(602,"Wrong field Configuration"),
	
	/**
	 * Iso Message manipulation ( Creation, Parsing, updating ) error range
	 * 700 -> 799
	 */
	ISOMSG_FRAMEDECODE_FAILED(700,"Failed to decode message frame"),
	ISOMSG_FRAMEDECODE_INVALID_LEN(701,"Invalid frame length"),
	ISOMSG_TSK_WRAP_FAILED(702,"Failed to wrap Iso message with Iso task"),
	ISOMSG_PARSE_FAILED(703,"Failed to parse message"),
	ISOMSG_PARSE_ACQ_FAILED(704,"Failed to parse acquirer message"),
	ISOMSG_PARSE_TRX_FAILED(705,"Failed to parse tranx type message"),
	ISOMSG_PARSE_CHARGE_TRX_FAILED(706,"Failed to parse charge tranx type message"),
	ISOMSG_PARSE_TERM_FAILED(707,"Failed to parse terminal type message"),
	ISOMSG_REQUEST_CREATION_FAILED(708,"Failed to create iso msg request"),
	ISOMSG_RESPONSE_CREATION_FAILED(709,"Failed to create iso msg response"),
	ISOMSG_RESPCODE_NO_MATCH_FOUND(710,"Failed to find Iso message response code mapping"),
	ISOMSG_FORMAT_FAILED(711,"Failed to format message"),
	ISOMSG_NO_MSG_DEF_FOUND(712,"no message definition found"),
	ISOMSG_ENCODING_FAILED(713,"Failed to encode message"),
	ISOMSG_PROCESSING_FAILED_BF_DEF(714,"Processing failed before Executing Definition"),
	ISOMSG_PROCESSING_FAILED_AF_DEF(715,"Processing failed After Executing Definition"),
	/**
	 * Pool Executor errors range
	 * @deprecated
	 */
	EX_CONF_WRG_PRIORITY(1000, "Task Executor Wrong priority");
	
	
	/**
	 * Error Code
	 */
	private final int code;
	
	/**
	 * Error description
	 */
	private final String description;

	/**
	 * Create engine error
	 * @param code
	 * @param description
	 */
	private EngineError(int code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * Return error description
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Return error code
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Return string representation of the error enum
	 */
	public String toString() {
		return "["+code+ "] " + description;
	}
}
