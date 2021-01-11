package com.path.atm.vo.config;

import java.math.BigDecimal;

import com.path.lib.vo.BaseVO;

/**
 * 
 * Copyright 2019, Path Solutions Path Solutions retains all ownership rights to
 * this source code
 * 
 * AtmChannelConfigCO.java used to hold channel and channel parameters
 */
public class AtmChannelConfigCO extends BaseVO{

	/**
	 * Hold Channel id
	 */
	private BigDecimal channelId;

	/**
	 * Hold channel Description 
	 */
	private String description;

	/**
	 * Hold Communication type ( tcp .. )
	 */
	private String communicationType;

	/**
	 * Hold Server type ( Server/Client )
	 */
	private String serverType;

	/**
	 * Hold connector ip
	 */
	private String ip;

	/**
	 * hold connector port
	 */
	private String port;

	/**
	 * Hold interface code
	 */
	private BigDecimal interfaceCode;

	/**
	 * Hold Socket restart interval
	 */
	private BigDecimal socketRestartInterval;

	/**
	 * Hold channel param id
	 */
	private BigDecimal channelParamId;

	/**
	 * hold nb of acceptor
	 */
	private BigDecimal acceptorWorkerNo;

	/**
	 * hold connector worker number
	 */
	private BigDecimal connectorWorkerNo;

	/**
	 * Hold addLoggingHandlerYN
	 */
	private String addLoggingHandlerYN;

	/**
	 * hold addEchoMessageListenerYN
	 */
	private String addEchoMessageListenerYN;

	/**
	 * hold logFieldDescriptionYN
	 */
	private String logFieldDescriptionYN;

	/**
	 * Hold logSensitiveDataYN
	 */
	private String logSensitiveDataYN;

	/**
	 * hold replyOnErrorYN
	 */
	private String replyOnErrorYN;

	/**
	 * hold idleTimeOut
	 */
	private BigDecimal idleTimeOut;

	/**
	 * hold connector charSet
	 */
	private String connectorCharSet;

	/**
	 * hold taskContainerConsumerNo
	 */
	private BigDecimal taskContainerConsumerNo;

	/**
	 * hold taskContainerSessionNo
	 */
	private BigDecimal taskContainerSessionNo;

	/**
	 * hold taskContainerDestination
	 */
	private String taskContainerDestination;

	/**
	 * @return the channelId
	 */
	public BigDecimal getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(BigDecimal channelId) {
		this.channelId = channelId;
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
	 * @return the communicationType
	 */
	public String getCommunicationType() {
		return communicationType;
	}

	/**
	 * @param communicationType the communicationType to set
	 */
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}

	/**
	 * @return the serverType
	 */
	public String getServerType() {
		return serverType;
	}

	/**
	 * @param serverType the serverType to set
	 */
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
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
	 * @return the socketRestartInterval
	 */
	public BigDecimal getSocketRestartInterval() {
		return socketRestartInterval;
	}

	/**
	 * @param socketRestartInterval the socketRestartInterval to set
	 */
	public void setSocketRestartInterval(BigDecimal socketRestartInterval) {
		this.socketRestartInterval = socketRestartInterval;
	}

	/**
	 * @return the channelParamId
	 */
	public BigDecimal getChannelParamId() {
		return channelParamId;
	}

	/**
	 * @param channelParamId the channelParamId to set
	 */
	public void setChannelParamId(BigDecimal channelParamId) {
		this.channelParamId = channelParamId;
	}

	/**
	 * @return the acceptorWorkerNo
	 */
	public BigDecimal getAcceptorWorkerNo() {
		return acceptorWorkerNo;
	}

	/**
	 * @param acceptorWorkerNo the acceptorWorkerNo to set
	 */
	public void setAcceptorWorkerNo(BigDecimal acceptorWorkerNo) {
		this.acceptorWorkerNo = acceptorWorkerNo;
	}

	/**
	 * @return the connectorWorkerNo
	 */
	public BigDecimal getConnectorWorkerNo() {
		return connectorWorkerNo;
	}

	/**
	 * @param connectorWorkerNo the connectorWorkerNo to set
	 */
	public void setConnectorWorkerNo(BigDecimal connectorWorkerNo) {
		this.connectorWorkerNo = connectorWorkerNo;
	}

	/**
	 * @return the addLoggingHandlerYN
	 */
	public String getAddLoggingHandlerYN() {
		return addLoggingHandlerYN;
	}

	/**
	 * @param addLoggingHandlerYN the addLoggingHandlerYN to set
	 */
	public void setAddLoggingHandlerYN(String addLoggingHandlerYN) {
		this.addLoggingHandlerYN = addLoggingHandlerYN;
	}

	/**
	 * @return the addEchoMessageListenerYN
	 */
	public String getAddEchoMessageListenerYN() {
		return addEchoMessageListenerYN;
	}

	/**
	 * @param addEchoMessageListenerYN the addEchoMessageListenerYN to set
	 */
	public void setAddEchoMessageListenerYN(String addEchoMessageListenerYN) {
		this.addEchoMessageListenerYN = addEchoMessageListenerYN;
	}

	/**
	 * @return the logFieldDescriptionYN
	 */
	public String getLogFieldDescriptionYN() {
		return logFieldDescriptionYN;
	}

	/**
	 * @param logFieldDescriptionYN the logFieldDescriptionYN to set
	 */
	public void setLogFieldDescriptionYN(String logFieldDescriptionYN) {
		this.logFieldDescriptionYN = logFieldDescriptionYN;
	}

	/**
	 * @return the logSensitiveDataYN
	 */
	public String getLogSensitiveDataYN() {
		return logSensitiveDataYN;
	}

	/**
	 * @param logSensitiveDataYN the logSensitiveDataYN to set
	 */
	public void setLogSensitiveDataYN(String logSensitiveDataYN) {
		this.logSensitiveDataYN = logSensitiveDataYN;
	}

	/**
	 * @return the replyOnErrorYN
	 */
	public String getReplyOnErrorYN() {
		return replyOnErrorYN;
	}

	/**
	 * @param replyOnErrorYN the replyOnErrorYN to set
	 */
	public void setReplyOnErrorYN(String replyOnErrorYN) {
		this.replyOnErrorYN = replyOnErrorYN;
	}

	/**
	 * @return the idleTimeOut
	 */
	public BigDecimal getIdleTimeOut() {
		return idleTimeOut;
	}

	/**
	 * @param idleTimeOut the idleTimeOut to set
	 */
	public void setIdleTimeOut(BigDecimal idleTimeOut) {
		this.idleTimeOut = idleTimeOut;
	}

	/**
	 * @return the connectorCharSet
	 */
	public String getConnectorCharSet() {
		return connectorCharSet;
	}

	/**
	 * @param connectorCharSet the connectorCharSet to set
	 */
	public void setConnectorCharSet(String connectorCharSet) {
		this.connectorCharSet = connectorCharSet;
	}

	/**
	 * @return the taskContainerConsumerNo
	 */
	public BigDecimal getTaskContainerConsumerNo() {
		return taskContainerConsumerNo;
	}

	/**
	 * @param taskContainerConsumerNo the taskContainerConsumerNo to set
	 */
	public void setTaskContainerConsumerNo(BigDecimal taskContainerConsumerNo) {
		this.taskContainerConsumerNo = taskContainerConsumerNo;
	}

	/**
	 * @return the taskContainerSessionNo
	 */
	public BigDecimal getTaskContainerSessionNo() {
		return taskContainerSessionNo;
	}

	/**
	 * @param taskContainerSessionNo the taskContainerSessionNo to set
	 */
	public void setTaskContainerSessionNo(BigDecimal taskContainerSessionNo) {
		this.taskContainerSessionNo = taskContainerSessionNo;
	}

	/**
	 * @return the taskContainerDestination
	 */
	public String getTaskContainerDestination() {
		return taskContainerDestination;
	}

	/**
	 * @param taskContainerDestination the taskContainerDestination to set
	 */
	public void setTaskContainerDestination(String taskContainerDestination) {
		this.taskContainerDestination = taskContainerDestination;
	}

	
}