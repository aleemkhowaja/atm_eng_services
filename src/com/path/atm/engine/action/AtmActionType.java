package com.path.atm.engine.action;

/**
 * Atm Action type Enumeration.
 * 
 * @author MohammadAliMezzawi
 *
 */
public enum AtmActionType {
	
	// Engine Action
	ENG_STARTING("ENG_STARTING","Starting engine"),
	ENG_SHUTINGDOWN("ENG_SHUTINGDOWN","Shutdown the engine"),
	ENG_START_TSK_CONTAINER("ENG_START_TSK_CONTAINER","Start Engine task container"),
	ENG_MRK_INT_DOWN("ENG_MRK_INT_DOWN","Mark Interfaces as down"),
	ENG_SHUTDOWN_RECS("ENG_SHUTDOWN_RECS","Shutdown all reactors"),
	
	// Engine Reactor starting Actions
	REC_STARTING("REC_STARTING"," Starting Reactor"),
	REC_LOAD_CONFIG("REC_LOAD_CONFIG", "Reactor load configuration"),
	REC_INIT_TSK_SEQ_GEN("REC_INIT_TSK_SEQ_GEN","Reactor init task id generator"),
	REC_LOAD_INT_DATA("REC_LOAD_INT_DATA","Reactor load interface data"),
	REC_LOAD_INT_ACQ("REC_LOAD_INT_ACQ","load interface Acquirer"),
	REC_LOAD_INT_MSG("REC_LOAD_INT_MSG","load interface messages"),
	REC_LOAD_INT_MSG_DEF("REC_LOAD_INT_MSG_DEF","load interface message definition"),
	REC_LOAD_INT_FIELDS("REC_LOAD_INT_FIELDS","load interface fields"),
	REC_PREPARE_EXPRESSIONS("REC_PREPARE_EXPRESSIONS","Prepare interface expressions"),
	REC_CREATE_MSG_FACTORY("REC_CREATE_MSG_FACTORY","Create Message factory"),
	REC_START_CONNECTOR("REC_START_CONNECTOR","Start connector"),
	REC_START_TSK_CNT_CLIENT("REC_START_TSK_CNT_CLIENT","Start Task container client"),
	
	// Engine Reactor shutting Actions
	REC_SHUTDOWN("REC_SHUTDOWN","Reactor shutdown"),
	REC_SHUTDOWN_CONNECTOR("REC_SHUTDOWN_CONNECTOR","Reactor connector shutdown"),
	REC_SHUTDOWN_TSK_CNT("REC_SHUTDOWN_TSK_CNT", "Reactor task container shutdown"),
	
	
	// Server/client channel Action
	CH_ADD_FRAME_DECODER("CH_ADD_FRAME_DECODER"," Add Frame Decoder Handler"),
	CH_ADD_TASK_WRAPPER("CH_ADD_TASK_WRAPPER"," Add Task Wrapper Handler"),
	CH_ADD_ISO_ENCODER("CH_ADD_ISO_ENCODER"," Add Encoder Handler"),
	CH_ADD_ISO_DECODER("CH_ADD_ISO_DECODER"," Add Decoder Handler"),
	CH_CLIENT_CONNECTED("CH_CLIENT_CONNECTED","Client is connected"),
	CH_CLIENT_DISCONNECTED("CH_CLIENT_DISCONNECTED","Client is disconnected");
	
	
	private String code;
	private String msg;
	
	/**
	 * Enum Constructor
	 */
	AtmActionType(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
