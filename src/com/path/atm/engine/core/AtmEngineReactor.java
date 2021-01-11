package com.path.atm.engine.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.BindException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.path.atm.bo.engine.AtmEngineConfigBO;
import com.path.atm.engine.action.AtmActionType;
import com.path.atm.engine.action.AtmBaseAction;
import com.path.atm.engine.config.AtmEngineReactorConfig;
import com.path.atm.engine.connector.Connector;
import com.path.atm.engine.connector.client.AtmClientConnector;
import com.path.atm.engine.connector.client.AtmClientConnectorConfig;
import com.path.atm.engine.connector.configuration.AtmConnectorConfiguration;
import com.path.atm.engine.connector.server.AtmServerConnector;
import com.path.atm.engine.connector.server.AtmServerConnectorConfig;
import com.path.atm.engine.connector.util.AtmConnectorConstants;
import com.path.atm.engine.container.amq.client.AmqClientConfig;
import com.path.atm.engine.container.amq.client.AmqIsoClient;
import com.path.atm.engine.exception.EngineReactorShutdownException;
import com.path.atm.engine.exception.EngineReactorStartupException;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.exception.IsoMessageException;
import com.path.atm.engine.generator.SimpleSeqGenerator;
import com.path.atm.engine.handler.IsoMessageHandler;
import com.path.atm.engine.iso8583.AtmIsoMessage;
import com.path.atm.engine.iso8583.IsoConfigFactory;
import com.path.atm.engine.iso8583.IsoConfigParser;
import com.path.atm.engine.iso8583.IsoUtil;
import com.path.atm.engine.iso8583.MessageFactory;
import com.path.atm.engine.pool.tasks.IsoTask;
import com.path.atm.engine.util.AtmCommonUtil;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.AtmLogger;
import com.path.atm.engine.util.EngineError;
import com.path.atm.engine.util.IfExpressionJoiner;
import com.path.atm.vo.config.AtmInterfaceConfigSC;
import com.path.atm.vo.engine.AcquirerCO;
import com.path.atm.vo.engine.AcquirerSC;
import com.path.atm.vo.engine.AtmInterfaceCO;
import com.path.atm.vo.engine.AtmIsoFieldCO;
import com.path.atm.vo.engine.AtmIsoMessageCO;
import com.path.atm.vo.engine.AtmIsoMessageDefCO;
import com.path.atm.vo.engine.AtmIsoNetMsgFldsCO;
import com.path.atm.vo.engine.TerminalCO;
import com.path.atm.vo.engine.TerminalSC;
import com.path.atm.vo.engine.TransactionTypeCO;
import com.path.bo.common.CommonLibBO;
import com.path.bo.common.ConstantsCommon;
import com.path.dbmaps.vo.ATM_ENG_INTERFACEVO;
import com.path.dbmaps.vo.COMPANIESVO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

/**
 * A Reactor will handle all requests made by a switch.
 * 
 * Whenever a request arrived the reactor will submit it to its executor which
 * will handle it
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmEngineReactor
{

    /**
     * Hold reference to the log
     */
    private final static Log log = Log.getInstance();

    /**
     * Hold reference to the log
     */
    private final static AtmLogger atmLog = AtmLogger.getInstance();

    /**
     * Reactor Name
     */
    private String name;

    /**
     * Hold reference to the interface CO
     */
    private AtmInterfaceCO interfaceCO;

    /**
     * Hold reference to the eng interface VO
     */
    private ATM_ENG_INTERFACEVO engInterfaceVO;

    /**
     * Reactor Name
     */
    private volatile AtmEngReactorStatus status;

    /**
     * Reactor Configurations holder
     */
    private AtmEngineReactorConfig configuration;

    /**
     * Reactor Connector
     */
    private Connector connector;

    /**
     * Reactor Message Factory
     */
    private MessageFactory messageFactory;

    /**
     * Hold reference to the engine
     */
    private final AtmEngine engine;

    /**
     * Hold reference to the task container client
     */
    private AmqIsoClient taskContClient;

    /**
     * Hold reference to the task container client
     */
    private SimpleSeqGenerator taskSeqGenerator;

    /**
     * Construct atm engine reactor
     * 
     * @param interfaceCO
     */
    public AtmEngineReactor(AtmInterfaceCO interfaceCO, AtmEngine engine)
    {

	// set the interface definition CO
	this.interfaceCO = interfaceCO;

	// set the eng interface VO
	engInterfaceVO = new ATM_ENG_INTERFACEVO();
	engInterfaceVO.setINTERFACE_ID(interfaceCO.getCode());
	engInterfaceVO.setMACHINE_NAME_IP(AtmCommonUtil.returnLocalServiceUrl());
	engInterfaceVO.setISO_FIELDS_CONFIG("");

	// hold reference to the engine
	this.engine = engine;
    }

    /**
     * Start Up the Atm engine reactor
     * 
     * @throws EngineReactorStartupException
     */
    public void startUp() throws EngineReactorStartupException
    {

	try
	{

	    log.debug("start Reactor");

	    // log action
	    AtmBaseAction.getInstance(AtmActionType.REC_STARTING, this).log();

	    // insert interface instance ( status Down )
	    atmLog.insertReactorInstance(engInterfaceVO);

	    // atmLog.updateReactorStatus(interfaceCO,AtmEngReactorStatus.STARTING);
	    // initialize the main components
	    init();

	    // try to start the engine
	    startConnector();

	    // start the TaskPoolManager
	    startTaskContainerClient();

	    // Set Atm Reactor status to Running
	    setStatus(AtmEngReactorStatus.RUNNING);
	    engInterfaceVO.setSTATUS(status.getCode());
	    engInterfaceVO.setSTART_TIME(new Date());
	    engInterfaceVO.setMESSAGE("");

	    // log the status in the DB
	    atmLog.updateReactorInfo(engInterfaceVO);
	    log.debug("Reactor is started");

	}
	catch(Exception exception)
	{

	    EngineReactorStartupException exp = new EngineReactorStartupException(EngineError.REACTOR_STARTUP_FAILED,
		    exception);

	    // update Engine interface error message
	    engInterfaceVO.setMESSAGE(AtmCommonUtil.getEngExpCauseMsg(exception));

	    // log the status in the DB
	    atmLog.updateReactorInfo(engInterfaceVO);

	    // gracefully shutdown
	    shutdownGracefully();

	    throw exp;
	}
    }

    /**
     * This method will shutdown the reactor
     * 
     * @throws EngineReactorShutdownException * [[SCRAM]] move it to the
     *             shutdown Shutdown the Atm engine reactor
     */
    public void shutdown() throws EngineReactorShutdownException
    {

	try
	{
	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_SHUTDOWN);

	    // @todo enable this after updating the grid
	    // atmLog.updateReactorStatus(interfaceCO,AtmEngReactorStatus.SHUTDOWN);

	    EngineError error = null;

	    // @todo add more logging info to the error
	    if(!shutdownConnector())
		error = EngineError.REACTOR_SD_FAILED_CONNECTOR;

	    if(!shutdownTaskContClient())
		error = EngineError.REACTOR_SD_FAILED_TSK_CONT;

	    if(null != error)
		throw new EngineReactorShutdownException(error);

	    // Set Atm Reactor status to Running
	    setStatus(AtmEngReactorStatus.DOWN);
	    engInterfaceVO.setSTATUS(status.getCode());

	    atmLog.updateReactorInfo(engInterfaceVO);
	    action.log();

	}
	catch(Exception exp)
	{

	    EngineReactorShutdownException exception = new EngineReactorShutdownException(
		    EngineError.REACTOR_SHUTDOWN_FAILED, exp);

	    // update Engine interface error message
	    engInterfaceVO.setMESSAGE(AtmCommonUtil.getEngExpCauseMsg(exception));

	    // log the status in the DB
	    atmLog.updateReactorInfo(engInterfaceVO);

	    // Wrap any exception with EngineReactorShutdownException
	    throw exception;
	}
    }

    
    /**
     * Halt the reactor from the engine.
     * Note : only the engine has a permission to shutdown a reactor.
     */
    public void halt()
    {
	try{
	    getEngine().shutdownReactor(interfaceCO);
	}
	catch(BaseException exp){
	    log.error(exp, "Failed to halt the reactor");
	}
    }
    
    
    /**
     * Executor iso message
     * 
     * @param string
     * @throws IsoMessageException
     */
    public HashMap<String, Object> executeIsoMsg(String string) throws IsoMessageException
    {

	/**
	 * Later on, we should reconsider the logging approach and move it to
	 * the isoMessageHandler
	 */
	// prepare iso task
	IsoTask task = prepareStandaloneMsg(string);

	// log request @todo fix this
	// atmLog.logRequest(task, null);

	IsoMessageHandler messageHandler = new IsoMessageHandler(task, this);
	AtmIsoMessage response = messageHandler.handleMessage(false);
	String responseStr = response.returnIsoString();

	HashMap<String, Object> respHm = messageFactory.convertToHashMap(response);
	respHm.put("ISOMSG", responseStr);

	return respHm;
    }

    /**
     * Parse Standalone iso Msg
     * <p>
     * Mainly this method is used by the simulator
     * 
     * @param string
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> parseIsoMsg(String string) throws Exception
    {
	IsoTask task = prepareStandaloneMsg(string);
	AtmIsoMessage isoReqMessage = messageFactory.parseMessage(task.getPayloadBytes(), true);
	return messageFactory.convertToHashMap(isoReqMessage);
    }

    /**
     * Prepare standaloneMsg
     * <p>
     * messsage sent by the simulator to the engine
     * 
     * @param string
     * @return
     */
    private IsoTask prepareStandaloneMsg(String string)
    {

	IsoTask task = new IsoTask();
	// generate message id
	task.setId(getTaskSeqGenerator().nextId());
	task.setReceivedTime(Calendar.getInstance().getTime());
	task.setInterfaceCode(getInterfaceCO().getCode());
	task.setPayloadBytes(string.getBytes());

	// @todo handle exception for failure reason
	task.setStatus("S");

	return task;
    }

    /**
     * Initialize the core components
     * 
     * @note : no need to wrap the exception at this level since we assume that
     *       it's already wrapped by the internal call method Just pass it to
     *       the Startup method.
     * 
     * @throws Exception
     */
    private void init() throws EngineReactorStartupException
    {

	log.debug("start init");

	// load configuration
	loadConfiguration();

	/**
	 * Initialize sequence generator
	 */
	createTaskIdSequenceGenerator();

	/**
	 * Populate interface data
	 */
	loadInterfaceData();

	/**
	 * Precompile all expressions
	 */
	prepareExpressions();

	/**
	 * Create iso message factory
	 */
	createIsoFactory();

	log.debug("End init");
    }

    /**
     * Load reactor configuration
     */
    private void loadConfiguration() throws EngineReactorStartupException
    {

	try
	{
	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_LOAD_CONFIG, configuration);

	    configuration = new AtmEngineReactorConfig(interfaceCO.getCode());
	    configuration.loadFromProperties();

	    // log action
	    action.log();

	}
	catch(Exception exp)
	{

	    // Wrap any exception with EngineReactorStartupException
	    throw new EngineReactorStartupException(EngineError.REACTOR_ST_FAILED_LD_CONFIG, exp);
	}
    }

    /**
     * Create task Id sequence generator
     * 
     * @throws Exception
     * @todo fix this one and move it to the engine
     */
    private void createTaskIdSequenceGenerator() throws EngineReactorStartupException
    {

	try
	{
	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_INIT_TSK_SEQ_GEN);

	    AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		    .getBean("atmEngConfigBO");

	    AtmInterfaceConfigSC configSc = new AtmInterfaceConfigSC();
	    configSc.setCode(interfaceCO.getCode());

	    /**
	     * task id generator. Currently we will use the same value for the
	     * task id
	     */
	    taskSeqGenerator = new SimpleSeqGenerator(configBO.returnNextMessageId(configSc));

	    // log action
	    action.setReference(taskSeqGenerator).log();

	}
	catch(Exception exp)
	{
	    // Wrap any exception with EngineReactorStartupException
	    throw new EngineReactorStartupException(EngineError.REACTOR_ST_FAILED_INIT_TSK_SEQ, exp);
	}
    }

    /**
     * Create IsoFactory
     * 
     * @throws Exception
     */
    private void createIsoFactory() throws EngineReactorStartupException
    {

	try
	{

	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_CREATE_MSG_FACTORY);

	    AtmInterfaceConfigSC configSc = new AtmInterfaceConfigSC();
	    configSc.setCode(interfaceCO.getCode());

	    // generate the configuration file
	    IsoConfigFactory configFactory = new IsoConfigFactory();
	    configFactory.generateConfig(new ArrayList<AtmIsoMessageCO>(interfaceCO.getIsoMsgList().values()),
		    interfaceCO.getIsoFieldList());

	    // get the configuration and update iso fields config for logging
	    // purpose
	    String configuration = configFactory.convertToStr();
	    engInterfaceVO.setISO_FIELDS_CONFIG(configuration);

	    // create message factory
	    messageFactory = IsoConfigParser.createIsoFactory(interfaceCO, configFactory.convertToStr());

	    // log action
	    action.setReference(messageFactory).log();

	}
	catch(Exception exp)
	{
	    // Wrap any exception with EngineReactorStartupException
	    throw new EngineReactorStartupException(EngineError.REACTOR_ST_FAILED_INIT_ISO_FACT, exp);
	}
    }

    /**
     * Load interface data
     * 
     * <p>
     * This method will load the interface configurations inside the interfaceCO
     * 1- Basic interface info ( Interface type , bitmap length...) 2- List of
     * all MTIs 3- List of Acquirer attached to the interface
     * 
     * @throws Exception
     * 
     */
    private void loadInterfaceData() throws EngineReactorStartupException
    {

	try
	{
	    log.debug("start loadInterfaceData");

	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_LOAD_INT_DATA);

	    // check if code is positive @todo change exception
	    if(NumberUtil.isEmptyDecimal(interfaceCO.getCode()))
		throw new IllegalArgumentException("loadInterfaceData : invalid code");

	    // get the bean
	    AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		    .getBean("atmEngConfigBO");

	    // load interface basic info
	    AtmInterfaceConfigSC configSC = new AtmInterfaceConfigSC();
	    configSC.setCode(interfaceCO.getCode());
	    interfaceCO = configBO.returnInterfaceInfo(configSC);

	    if(null == interfaceCO)
		throw new IllegalConfigurationException(EngineError.REACTOR_DEF_NO_INT);

	    /**
	     * This is a specific case where we need to load the company info
	     * from the core DB
	     * 
	     * @send connection
	     */
	    CommonLibBO commonLibBO = (CommonLibBO) ApplicationContextProvider.getApplicationContext()
		    .getBean("commonLibBO");

	    COMPANIESVO companyVO = new COMPANIESVO();
	    companyVO.setCOMP_CODE(interfaceCO.getCompCode());
	    companyVO = commonLibBO.returnCompany(companyVO);

	    // load Company name
	    interfaceCO.setCompBriefName(companyVO.getBRIEF_DESC_ENG());

	    // load interface fields
	    loadInterfaceFields(configSC);

	    // load Messages
	    loadInterfaceMsgs(configSC);

	    // load Message definitions
	    loadIsoMessageDefintion(configSC);

	    // load Acquirers and all related info
	    loadInterfaceAcq(configSC);

	}
	catch(Exception exp)
	{
	    // Wrap any exception with EngineReactorStartupException
	    throw new EngineReactorStartupException(EngineError.REACTOR_ST_FAILED_LD_INTERFACE, exp);
	}
    }

    /**
     * Load interface Acquirer
     * 
     * @param configSC
     * @throws BaseException
     */
    private void loadInterfaceAcq(AtmInterfaceConfigSC configSC) throws BaseException
    {

	AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_LOAD_INT_ACQ);

	// get the bean
	AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngConfigBO");

	// get all Acquirer
	AcquirerSC acquirerSC = new AcquirerSC();
	acquirerSC.setInterfaceCode(interfaceCO.getCode());

	List<AcquirerCO> acquirerList = configBO.returnAcquirerList(acquirerSC);

	if(null == acquirerList || 0 == acquirerList.size())
	    throw new IllegalConfigurationException(EngineError.REACTOR_DEF_NO_ACQ);

	HashMap<BigDecimal,AcquirerCO> acquirers = new HashMap<>();
	IfExpressionJoiner expression = new IfExpressionJoiner("0");
	
	// populate acquirer
	// @note: usually we don't use such approach but since it's on startup
	// it's ok
	for(AcquirerCO acquirer : acquirerList)
	{
	    // push the expression to the Joiner
	    expression.append(acquirer.getMsgSource(), acquirer.getId().toString());
	    
	    //Load Acquirer trx type
	    loadAcqTrxType(acquirer);
	    
	    // load Acquirer terminal
	    loadAcqTerminals(acquirer);
	    
	    acquirers.put(acquirer.getId(), acquirer);
	}

	interfaceCO.setAcquirers(acquirers);
	interfaceCO.setAcquirersJoinedExp(expression.toString());
	action.log();
    }
    
    
    /**
     * Currently we are loading all the terminals since based on the
     * benchmark 100k Terminal is taking 16MB.
     * 
     * @param acquirer
     * @throws BaseException
     */
    private void loadAcqTerminals(AcquirerCO acquirer) throws BaseException
    {
	// get the bean
	AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngConfigBO");

	TerminalSC terminalSC = new TerminalSC();
	terminalSC.setInterfaceCode(interfaceCO.getCode());
	terminalSC.setAcquirerId(acquirer.getId());
	
	List<TerminalCO> terminals = configBO.returnTerminals(terminalSC);
	HashMap<String,TerminalCO> acquirerTerminals = new HashMap<>(); 
	    
	for(TerminalCO terminalCO : terminals) {
	    acquirerTerminals.put(terminalCO.getCode(), terminalCO);
	}

	acquirer.setTerminals(acquirerTerminals);
    }
    

    /**
     * Load Acquirer trx type
     * @param acquirer
     * @throws BaseException
     */
    private void loadAcqTrxType(AcquirerCO acquirer) throws BaseException
    {

	// get all Acquirer
	AcquirerSC acquirerSC = new AcquirerSC();
	acquirerSC.setInterfaceCode(interfaceCO.getCode());

	// get the bean
	AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngConfigBO");

	acquirerSC.setId(acquirer.getId());
	
	/**
	 * We are grouping the fees that belong to the same trx type/expression
	 * while querying the DB. The row id can't be retrieved in such case for
	 * that we will use a virtual id which will help us to retrieve the trx
	 * type from the acquirer
	 */
	List<TransactionTypeCO> listOfTrx = configBO.returnTrxTypeList(acquirerSC);

	if(null == listOfTrx || 0 == listOfTrx.size())
	    throw new IllegalConfigurationException(EngineError.REACTOR_DEF_NO_TRXTYPE);

	/**
	 * Start preparing the trx related to this acquirer
	 */
	HashMap<BigDecimal, TransactionTypeCO> trxTypes = new HashMap<>();
	IfExpressionJoiner trxExpression = new IfExpressionJoiner("0");

	int trxRowId = 1;
	for(TransactionTypeCO trxType : listOfTrx)
	{
	    /**
	     * In the message factory we were skipping the trx that doesn't have
	     * any expression but after the merge expression the trx expression
	     * is mandatory else we will throw an exception.
	     */
	    if(StringUtil.nullToEmpty(trxType.getExpression()).isEmpty())
		throw new IllegalConfigurationException(EngineError.REACTOR_DEF_NO_TRXTYPE_EXP);

	    BigDecimal trxTypeId = new BigDecimal(trxRowId);
	    trxExpression.append(trxType.getExpression(), trxTypeId.toString());
	    trxTypes.put(trxTypeId, trxType);
	    trxRowId++;
	}

	acquirer.setTrxTypesJoinedExp(trxExpression.toString());
	acquirer.setTransactionTypes(trxTypes);
    }

    
    /**
     * This method will load the interface Messages
     * 
     * @param configSC
     * @throws BaseException
     */
    private void loadInterfaceMsgs(AtmInterfaceConfigSC configSC) throws Exception
    {

	AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_LOAD_INT_MSG);

	// get the bean
	AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngConfigBO");

	// get list of message definition
	List<AtmIsoMessageCO> msgsList = configBO.returnIsoMessages(configSC);

	if(null == msgsList || msgsList.size() < 1)
	    throw new IllegalConfigurationException(EngineError.REACTOR_DEF_NO_MSG);

	// convert to hashMap to allow key access
	HashMap<String, AtmIsoMessageCO> mapOfAtmIsoMsg = new HashMap<String, AtmIsoMessageCO>();
	HashMap<String, String> responseReqMap = new HashMap<String, String>();
	for(AtmIsoMessageCO msgCO : msgsList)
	{
	    mapOfAtmIsoMsg.put(msgCO.getRequestMti(), msgCO);
	    responseReqMap.put(msgCO.getResponseMti(), msgCO.getRequestMti());
	}
	// populate interface co with iso messages
	interfaceCO.setIsoMsgList(mapOfAtmIsoMsg);
	interfaceCO.setResponseReqMap(responseReqMap);
	action.log();
    }

    /**
     * Load ISO Message Definition
     * 
     * @param configSC
     * @throws BaseException
     */
    private void loadIsoMessageDefintion(AtmInterfaceConfigSC configSC) throws BaseException
    {

	AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_LOAD_INT_MSG_DEF);

	// get the bean
	AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngConfigBO");

	List<AtmIsoMessageDefCO> atmIsoMessageDefCOs = configBO.returnIsoMsgDefList(configSC);
	HashMap<String, List<AtmIsoMessageDefCO>> isoMessageDefMap = new HashMap<String, List<AtmIsoMessageDefCO>>();
	HashMap<String, AtmIsoMessageDefCO> isoMessageNetDefMap = new HashMap<String, AtmIsoMessageDefCO>();

	List<AtmIsoNetMsgFldsCO> isoNetMsgFldsCOs = configBO.returnAtmNetMsgFldsList(configSC);
	HashMap<BigDecimal, List<AtmIsoNetMsgFldsCO>> reqNetFieldsMap = new HashMap<BigDecimal, List<AtmIsoNetMsgFldsCO>>();
	HashMap<BigDecimal, List<AtmIsoNetMsgFldsCO>> resNetFieldsMap = new HashMap<BigDecimal, List<AtmIsoNetMsgFldsCO>>();

	/**
	 * Prepare Network Message fields
	 */
	if(null != isoNetMsgFldsCOs && isoNetMsgFldsCOs.size() > 0)
	{
	    for(AtmIsoNetMsgFldsCO isoNetMsgFldsCO : isoNetMsgFldsCOs)
	    {
		/**
		 * Prepare Request Fields
		 */
		if(StringUtil.nullToEmpty(isoNetMsgFldsCO.getRequestFieldYN()).equalsIgnoreCase(ConstantsCommon.YES))
		{
		    if(reqNetFieldsMap.isEmpty() || !reqNetFieldsMap.containsKey(isoNetMsgFldsCO.getIsoMsgDefId()))
		    {
			List<AtmIsoNetMsgFldsCO> fldsCOs = new ArrayList<AtmIsoNetMsgFldsCO>();
			fldsCOs.add(isoNetMsgFldsCO);
			reqNetFieldsMap.put(isoNetMsgFldsCO.getIsoMsgDefId(), fldsCOs);
		    }
		    else if(reqNetFieldsMap.containsKey(isoNetMsgFldsCO.getIsoMsgDefId()))
		    {
			reqNetFieldsMap.get(isoNetMsgFldsCO.getIsoMsgDefId()).add(isoNetMsgFldsCO);
		    }
		}
		else
		{
		    /**
		     * Prepare Response Fields
		     */
		    if(resNetFieldsMap.isEmpty() || !resNetFieldsMap.containsKey(isoNetMsgFldsCO.getIsoMsgDefId()))
		    {
			List<AtmIsoNetMsgFldsCO> fldsCOs = new ArrayList<AtmIsoNetMsgFldsCO>();
			fldsCOs.add(isoNetMsgFldsCO);
			resNetFieldsMap.put(isoNetMsgFldsCO.getIsoMsgDefId(), fldsCOs);
		    }
		    else if(resNetFieldsMap.containsKey(isoNetMsgFldsCO.getIsoMsgDefId()))
		    {
			resNetFieldsMap.get(isoNetMsgFldsCO.getIsoMsgDefId()).add(isoNetMsgFldsCO);
		    }
		}
	    }
	}

	// build a hashMap grouped by mticode_processcode key pattern
	for(AtmIsoMessageDefCO defCO : atmIsoMessageDefCOs)
	{
	    String isoDefKey = IsoUtil.returnIsoMsgDefKey(defCO.getReqMTI(), defCO.getProcessCode());
	    isoMessageDefMap.putIfAbsent(isoDefKey, new ArrayList<AtmIsoMessageDefCO>());
	    isoMessageDefMap.get(isoDefKey).add(defCO);

	    /**
	     * Iso Network messages are specific case and may be needed in
	     * several case due to that and for performance reason we are
	     * duplicating it and push it to a specific map
	     */
	    if(defCO.getNetworkMessageCodeYN().equalsIgnoreCase(ConstantsCommon.YES))
	    {
		if(null != isoMessageNetDefMap.get(defCO.getNetworkMessageType()))
		    throw new IllegalConfigurationException(EngineError.REACTOR_DEF_DUPLICATE_NET);

		/**
		 * added Request and Response Fields in ISOMsgDefinition CO
		 */
		defCO.setNetReqFields(reqNetFieldsMap.get(defCO.getId()));
		defCO.setNetRespFields(resNetFieldsMap.get(defCO.getId()));

		isoMessageNetDefMap.put(defCO.getNetworkMessageType(), defCO);
	    }
	}

	interfaceCO.setIsoNetMessageDefMap(isoMessageNetDefMap);
	interfaceCO.setIsoMessageDefMap(isoMessageDefMap);

	// log action
	action.log();
    }

    /**
     * This method will load the interface Fields
     * 
     * @param configSC
     * @throws BaseException
     */
    private void loadInterfaceFields(AtmInterfaceConfigSC configSC) throws Exception
    {

	AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_LOAD_INT_FIELDS);

	// get the bean
	AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		.getBean("atmEngConfigBO");

	// get all fields for this interface
	List<AtmIsoFieldCO> listOfFieldsDef = configBO.returnIsoFieldsDefByInt(configSC);

	if(null == listOfFieldsDef || 0 == listOfFieldsDef.size())
	    throw new IllegalConfigurationException(EngineError.REACTOR_DEF_NO_FIELDS);

	// index HashMap
	LinkedHashMap<BigDecimal, AtmIsoFieldCO> isoFieldDefs = new LinkedHashMap<BigDecimal, AtmIsoFieldCO>();

	for(AtmIsoFieldCO isoField : listOfFieldsDef)
	{
	    isoFieldDefs.put(isoField.getCode(), isoField);
	}

	interfaceCO.setIsoFieldList(isoFieldDefs);
	action.log();
    }

    /**
     * Prepare the Expression ( Precompile )
     * 
     * @throws BaseException
     */
    private void prepareExpressions() throws EngineReactorStartupException
    {

	try
	{

	    /**
	     * Currently we are logging expression preparation as one action if
	     * needed we may split it to several sub actions.
	     */
	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_PREPARE_EXPRESSIONS);

	    // check if code is positive @todo change exception
	    if(NumberUtil.isEmptyDecimal(interfaceCO.getCode()))
		throw new IllegalArgumentException("loadInterfaceData : invalid code");

	    AtmEngineConfigBO configBO = (AtmEngineConfigBO) ApplicationContextProvider.getApplicationContext()
		    .getBean("atmEngConfigBO");

	    // return formatted Hashmap of expression
	    HashMap<String, Object> isoMsgHm = IsoUtil.returnEmptyIsoMsgHm(interfaceCO.getIsoFieldList());

	    // clear the cached data
	    configBO.clearMappingCache();

	    // Prepare and Execute the ISO field expression
	    prepareISOFieldExpression(isoMsgHm);

	    // Prepare and execute the Acquirer Expression
	    prepareAcquirerExpression(isoMsgHm);

	    // Prepare and execute the ISO Message and ISO Message Definition
	    // Expression
	    prepareISOMessageExpression(isoMsgHm);

	    // prepare and execute the Iso Message definition expression
	    prepareIsoMsgDefExpression(isoMsgHm);

	    // log action
	    action.log();

	}
	catch(Exception exp)
	{
	    // Wrap any exception with EngineReactorStartupException
	    throw new EngineReactorStartupException(EngineError.REACTOR_ST_FAILED_PREPARE_EXP, exp);
	}
    }

    /**
     * Prepare the ISO field and ISO sub fields expression
     * 
     * @param exprDataMap
     * @throws Exception
     */
    private void prepareISOFieldExpression(HashMap<String, Object> exprDataMap) throws Exception
    {
	// return ISO fields
	HashMap<BigDecimal, AtmIsoFieldCO> isofieldsList = interfaceCO.getIsoFieldList();
	Iterator<Entry<BigDecimal, AtmIsoFieldCO>> isoFieldIt = isofieldsList.entrySet().iterator();

	while(isoFieldIt.hasNext())
	{
	    Entry<BigDecimal, AtmIsoFieldCO> atmIsoFieldEntry = isoFieldIt.next();
	    AtmIsoFieldCO fieldCO = atmIsoFieldEntry.getValue();

	    // Evaluate ISO Fields
	    if(StringUtil.isNotEmpty(fieldCO.getExpression()))
		IsoUtil.evaluateExpression(fieldCO.getExpression(), exprDataMap);

	    // no sub fields => skip to the next field
	    if(null == fieldCO.getIsoFieldSubList() || fieldCO.getIsoFieldSubList().isEmpty())
		continue;

	    // evaluate the sub fields expressions
	    for(AtmIsoFieldCO atmSubIsoFieldCO : fieldCO.getIsoFieldSubList())
	    {
		if(StringUtil.isNotEmpty(atmSubIsoFieldCO.getExpression()))
		    IsoUtil.evaluateExpression(atmSubIsoFieldCO.getExpression(), exprDataMap);
	    }
	}
    }

    /**
     * Prepare and execute the Acquirer/Trx type Expressions
     * 
     * @param exprDataMap
     * @throws Exception
     */
    private void prepareAcquirerExpression(Map<String, Object> exprDataMap) throws Exception
    {
	// retrieve acquirer List
	HashMap<BigDecimal, AcquirerCO> acquirerCOs = interfaceCO.getAcquirers();
	Iterator<Entry<BigDecimal, AcquirerCO>> it = acquirerCOs.entrySet().iterator();
	
	// prepare the main acquirers joined expressions
	if(StringUtil.isNotEmpty(interfaceCO.getAcquirersJoinedExp()))
	    IsoUtil.evaluateExpression(interfaceCO.getAcquirersJoinedExp(), exprDataMap);
	
	// prepare trx type joined expressiones
	while(it.hasNext()){
	    Entry<BigDecimal, AcquirerCO> entry = it.next();
	    AcquirerCO acquirerCo = entry.getValue();

	    if(StringUtil.isNotEmpty(acquirerCo.getTrxTypesJoinedExp()))
		IsoUtil.evaluateExpression(acquirerCo.getTrxTypesJoinedExp(), exprDataMap);
	}
    }

    /**
     * Prepare and execute the ISO Message and ISO Message Definition Expression
     * 
     * @param exprDataMap
     * @throws Exception
     */
    private void prepareISOMessageExpression(Map<String, Object> exprDataMap) throws Exception
    {

	HashMap<String, AtmIsoMessageCO> isoMessageList = interfaceCO.getIsoMsgList();
	Iterator<Entry<String, AtmIsoMessageCO>> isoMsgIt = isoMessageList.entrySet().iterator();

	while(isoMsgIt.hasNext())
	{

	    Entry<String, AtmIsoMessageCO> isoMsgEntry = isoMsgIt.next();
	    AtmIsoMessageCO msgCO = isoMsgEntry.getValue();

	    // Evaluate the Definition expressions
	    if(StringUtil.isNotEmpty(msgCO.getRespExpression()))
		IsoUtil.evaluateExpression(msgCO.getRespExpression(), exprDataMap);
	}
    }

    /**
     * Prepare and precompile iso message deinitions
     * 
     * @param exprDataMap
     * @throws Exception
     */
    private void prepareIsoMsgDefExpression(HashMap<String, Object> exprDataMap) throws Exception
    {

	if(null == interfaceCO.getIsoMessageDefMap() || interfaceCO.getIsoMessageDefMap().isEmpty())
	    return;

	Iterator<Entry<String, List<AtmIsoMessageDefCO>>> isoMsgDefIt = interfaceCO.getIsoMessageDefMap().entrySet()
		.iterator();

	while(isoMsgDefIt.hasNext())
	{
	    List<AtmIsoMessageDefCO> listOfDef = isoMsgDefIt.next().getValue();

	    // Evaluate the Definition expressions
	    for(AtmIsoMessageDefCO atmIsoMessageDefCO : listOfDef)
	    {
		if(StringUtil.isNotEmpty(atmIsoMessageDefCO.getCriteriaExpression()))
		    IsoUtil.evaluateExpression(atmIsoMessageDefCO.getCriteriaExpression(), exprDataMap);
	    }
	}
    }

    /**
     * Shutdown the connector. It should always be safe process to allow the
     * system to safely proceed with the shutdown process.
     * 
     * @return
     */
    private boolean shutdownConnector()
    {

	log.debug("shutdownConnector");

	boolean shutdownSuccess = true;

	try
	{
	    // shutdown the connector
	    if(null != connector)
	    {

		// start logging action
		AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_SHUTDOWN_CONNECTOR);

		// shutdown connector
		connector.shutdown();

		// log action
		action.log();
	    }

	}
	catch(Exception exp)
	{
	    log.error(exp, "Failed to shutdown the connector");
	    shutdownSuccess = false;
	}

	return shutdownSuccess;
    }

    /**
     * Shutdown the client task container client
     * 
     * @return
     */
    private boolean shutdownTaskContClient()
    {

	log.debug("shutdownTaskContClient");
	boolean shutdownSuccess = true;

	try
	{
	    // shutdown the client task container client
	    if(null != taskContClient)
	    {

		// start logging action
		AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_SHUTDOWN_TSK_CNT);

		taskContClient.shutDownNow();

		action.log();

	    }

	}
	catch(Exception exp)
	{
	    log.error(exp, "Failed to shutdown the connector");
	    shutdownSuccess = false;
	}

	return shutdownSuccess;
    }

    /**
     * Graceful shutdown the reactor Mainly used on failed start up.
     */
    private void shutdownGracefully()
    {
	try
	{

	    log.debug("shutdownGracefully");

	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_SHUTDOWN);

	    shutdown();

	    action.log();
	}
	catch(Exception exp)
	{
	    log.error(exp, "Failed to shutdown the Reactor gracefully");
	}
    }

    /**
     * Start task container
     * 
     * @throws Exception
     */
    private void startTaskContainerClient() throws EngineReactorStartupException
    {

	try
	{
	    // debug info
	    log.debug("startTaskContainerClient");

	    // log action
	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_START_TSK_CNT_CLIENT);

	    String poolMode = getEngine().getConfiguration().getPoolMode();

	    /**
	     * if pool mode is memory, create then you own container Else use
	     * the engine container.
	     * 
	     * @todo implement this approach later on
	     */
	    if(AtmEngineConstants.TASK_POOL_MODE_MEMORY.equalsIgnoreCase(poolMode))
		return;

	    // load task container client configuration
	    AmqClientConfig config = new AmqClientConfig(AtmEngineConstants.PROP_FILE_NAME);
	    config.loadFromCO(configuration.getChannelConfiguration());
	    // @deprecated config.loadFromProperties();

	    taskContClient = new AmqIsoClient(config, this);
	    taskContClient.start();
	    action.setReference(taskContClient).log();

	}
	catch(Exception exp)
	{
	    // Wrap any exception with EngineReactorStartupException
	    throw new EngineReactorStartupException(EngineError.REACTOR_ST_FAILED_INIT_TSK_CNT, exp);
	}

    }

    /**
     * Start engine connector
     * 
     * @throws Exception
     */
    private void startConnector() throws EngineReactorStartupException
    {

	try
	{
	    AtmBaseAction action = AtmBaseAction.getInstance(AtmActionType.REC_START_CONNECTOR);

	    String actAs = StringUtil.nullToEmpty(configuration.getChannelConfiguration().getServerType());

	    if(StringUtil.isEmptyString(actAs))
		throw new IllegalConfigurationException(EngineError.CON_CONF_MISSING_SERVER_TYPE);

	    connector = actAs.equals(AtmEngineConstants.TCP_SERVER_CONNECTOR) ? createServerConnector()
		    : createClientConnector();

	    connector.start();

	    action.setReference(connector).log();

	}
	catch(Exception exp)
	{
	    EngineError errorCode = EngineError.REACTOR_ST_FAILED_INIT_CON;
	    String errorMessage = null;
	    
	    // In case of Bind exception we are handling the exception message differently
	    if(exp instanceof BindException) {
		errorCode = EngineError.REACTOR_ST_CON_FAILED_BIND;
		errorMessage = exp.getMessage();
	    }

	    // Wrap any exception with EngineReactorStartupException
	    throw new EngineReactorStartupException(errorCode, errorMessage, exp);
	}
    }

    /**
     * Create Atm Server connector using the loaded properties
     * 
     * <p>
     * Connector configurations are stored into 2 tables ( interface , channel )
     * for that we load the channel configuration and we populate the other from
     * the interface
     * 
     * @return
     * @throws IOException
     */
    private Connector createServerConnector() throws IOException
    {

	AtmServerConnectorConfig serverConfig = new AtmServerConnectorConfig(AtmEngineConstants.PROP_FILE_NAME);

	// load common Connector Properties
	loadConnectorProperties(serverConfig);

	// create Server connector
	return new AtmServerConnector(this, serverConfig);
    }

    /**
     * Load Connector Configuration
     * 
     * @param connectorConfig
     */
    private void loadConnectorProperties(AtmConnectorConfiguration connectorConfig)
    {
	/**
	 * Load Configuration from Channel CO and Interface CO
	 */
	connectorConfig.loadFromCO(configuration.getChannelConfiguration())
		// Header configuration ( length/ is included in length )
		.setIncludeHeaderLength(interfaceCO.getIncludeHeader().equalsIgnoreCase(ConstantsCommon.YES))
		.setHeaderLength(interfaceCO.getHeaderLength().intValue())

		// include length field length in length calculation
		.setIncludeLength(NumberUtil.nullToZero(interfaceCO.getIncludeLength()).equals(BigDecimal.ONE))

		// length field Configuration ( offset ,length)
		.setFrameLengthFieldOffset(null != interfaceCO.getProtocolIdentification()
			? interfaceCO.getProtocolIdentification().length()
			: AtmConnectorConstants.DEFAULT_FRAME_LENGTH_FIELD_OFFSET)

		.setFrameLengthFieldLength(interfaceCO.getMsgLength().intValue())
		// Frame length/ and length type
		.setLengthType(interfaceCO.getLengthType())
		.setMaxFrameLength(AtmConnectorConstants.DEFAULT_MAX_FRAME_LENGTH);
    }

    /**
     * Create Client connector
     * 
     * @return
     * @throws IOException
     */
    private Connector createClientConnector() throws IOException
    {

	AtmClientConnectorConfig serverConfig = new AtmClientConnectorConfig(AtmEngineConstants.PROP_FILE_NAME);

	// load common Connector Properties
	loadConnectorProperties(serverConfig);

	// create Server connector
	return new AtmClientConnector(this, serverConfig);
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString()
    {
	return "AtmEngineReactor";
    }

    /**
     * @return the name
     */
    public String getName()
    {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
	this.name = name;
    }

    /**
     * Return atm interfaceCO
     * 
     * @return
     */
    public AtmInterfaceCO getInterfaceCO()
    {
	return interfaceCO;
    }

    /**
     * @return the engInterfaceVO
     */
    public ATM_ENG_INTERFACEVO getEngInterfaceVO()
    {
	return engInterfaceVO;
    }

    /**
     * @return the status
     */
    public AtmEngReactorStatus getStatus()
    {
	return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(AtmEngReactorStatus status)
    {
	this.status = status;
    }

    /**
     * @return the engine
     */
    public AtmEngine getEngine()
    {
	return engine;
    }

    /**
     * @param connector the connector to set
     */
    public void setConnector(Connector connector)
    {
	this.connector = connector;
    }

    /**
     * @return connector
     */
    public Connector getConnector()
    {
	return this.connector;
    }

    /**
     * @return the configuration
     */
    public AtmEngineReactorConfig getConfiguration()
    {
	return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(AtmEngineReactorConfig configuration)
    {
	this.configuration = configuration;
    }

    /**
     * @return the taskContClient
     */
    public AmqIsoClient getTaskContClient()
    {
	return taskContClient;
    }

    /**
     * @param taskContClient the taskContClient to set
     */
    public void setTaskContClient(AmqIsoClient taskContClient)
    {
	this.taskContClient = taskContClient;
    }

    /**
     * @return the messageFacotry
     */
    public MessageFactory getMessageFactory()
    {
	return messageFactory;
    }

    /**
     * @param messageFacotry the messageFacotry to set
     */
    public void setMessageFacotry(MessageFactory messageFactory)
    {
	this.messageFactory = messageFactory;
    }

    /**
     * @return the taskSeqGenerator
     */
    public SimpleSeqGenerator getTaskSeqGenerator()
    {
	return taskSeqGenerator;
    }

    /**
     * @param taskSeqGenerator the taskSeqGenerator to set
     */
    public void setTaskSeqGenerator(SimpleSeqGenerator taskSeqGenerator)
    {
	this.taskSeqGenerator = taskSeqGenerator;
    }
}
