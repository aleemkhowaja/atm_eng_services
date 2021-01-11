package com.path.atm.engine.connector.configuration;

import java.net.InetAddress;

import com.path.atm.engine.config.AbstractConfiguration;
import com.path.atm.engine.connector.util.AtmConnectorConstants;
import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.config.AtmChannelConfigCO;
import com.path.bo.common.ConstantsCommon;
import com.path.lib.common.util.NumberUtil;
import com.path.lib.common.util.StringUtil;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;

/**
 * Hold Configuration related to Atm Connector
 * 
 * @author MohammadAliMezzawi
 *
 */
public abstract class AtmConnectorConfiguration<Config extends AtmConnectorConfiguration>
	extends AbstractConfiguration<Config>
{

    /**
     * Connector port number
     */
    protected Integer port;

    /**
     * Connector Host name
     */
    protected String host;

    /**
     * Returns number of threads in worker {@link EventLoopGroup}.
     */
    protected int workerThreadsCount;

    /**
     * Add default echo message listener {@link EchoMessageListener} Connector
     * handler
     */
    protected boolean addEchoMessageListener;

    /**
     * Frame length field Offset
     */
    protected int frameLengthFieldOffset;

    /**
     * Frame length field length
     */
    protected int frameLengthFieldLength;

    /**
     * Frame length field type Base256/ Base10
     */
    protected String lengthType;

    /**
     * Include length field length in length
     */
    protected boolean includeLength;

    /**
     * Header length
     */
    protected int headerLength;

    /**
     * Header length
     */
    protected boolean includeHeaderLength;

    /**
     * Frame length
     */
    protected int maxFrameLength;

    /**
     * Channel read/write idle timeout in seconds.
     * <p>
     * If no message was received/sent during specified time interval then
     * `Echo` message will be sent.
     * </p>
     */
    protected int idleTimeout;

    /**
     * Whether to reply with administrative message in case of message syntax
     * errors. Default value is <code>false.</code>
     */
    protected boolean replyOnError;

    /**
     * Define if logging handler should be added to pipeline
     */
    protected boolean addLoggingHandler;

    /**
     * Define if sensitive information like PAN, CVV/CVV2, and Track2 should be
     * printed to log.
     */
    protected boolean logSensitiveData;

    /**
     * Set of field numbers to be treated as sensitive data.
     */
    protected int[] sensitiveDataFields;

    /**
     * flag to determine if field descriptions should be printed in log.
     */
    protected boolean logFieldDescription;

    /**
     * @param propertiesFile
     */
    public AtmConnectorConfiguration(String propertiesFile)
    {
	super(propertiesFile);
    }

    /**
     * Load Properties from file
     */
    public Config loadFromProperties()
    {

	try
	{

	    if(StringUtil.nullToEmpty(propertiesFile).isEmpty())
		throw new IllegalConfigurationException(EngineError.CON_CONF_NOTSET);

	    // load all attributes from properties
	    port = getPropertyIntValue("atm.engine.port");
	    host = getPropertyValue("atm.engine.host", null);
	    addLoggingHandler = getPropertyBoolValue("atm.engine.addLoggingHandler");
	    addEchoMessageListener = getPropertyBoolValue("atm.engine.addEchoMessageListener");
	    logFieldDescription = getPropertyBoolValue("atm.engine.logFieldDescription");
	    logSensitiveData = getPropertyBoolValue("atm.engine.logSensitiveData");
	    replyOnError = getPropertyBoolValue("atm.engine.replyOnError");
	    idleTimeout = getPropertyIntValue("atm.engine.idleTimeout");

	    // this should be loaded from db probably
	    String sensitiveFields = getPropertyValue("atm.engine.sensitiveFields", null);
	    if(StringUtil.isNotEmpty(sensitiveFields))
	    {
		String[] strArray = sensitiveFields.split(",");
		sensitiveDataFields = new int[strArray.length];
		for(int i = 0; i < strArray.length; i++)
		{
		    sensitiveDataFields[i] = Integer.parseInt(strArray[i]);
		}
	    }

	}
	catch(Exception exception)
	{
	    /**
	     * NumberFormatException is the most occurring exception at this
	     * level in case the dev set a string value for an integer property
	     */
	    throw new IllegalConfigurationException(EngineError.CON_CONF_FAIL_TO_LOAD, exception);
	}
	return (Config) this;
    }

    /**
     * Load configuration from channel co.
     * 
     * @param atmChannelConfigCO
     * @return
     */
    public Config loadFromCO(AtmChannelConfigCO atmChannelConfigCO)
    {
	try
	{

	    String currentHostIp = InetAddress.getLocalHost().getHostAddress();

	    // load connector host info
	    port = Integer.parseInt(atmChannelConfigCO.getPort());
	    host = StringUtil.nullEmptyToValue(atmChannelConfigCO.getIp(), currentHostIp);

	    host = "192.168.50.14";
	    port = 6508;
	    
	    // load logging properties
	    addLoggingHandler = StringUtil.nullToEmpty(atmChannelConfigCO.getAddLoggingHandlerYN())
		    .equals(ConstantsCommon.YES);
	    logFieldDescription = StringUtil.nullToEmpty(atmChannelConfigCO.getLogFieldDescriptionYN())
		    .equals(ConstantsCommon.YES);
	    logSensitiveData = StringUtil.nullToEmpty(atmChannelConfigCO.getLogSensitiveDataYN())
		    .equals(ConstantsCommon.YES);

	    // load listener info
	    addEchoMessageListener = StringUtil.nullToEmpty(atmChannelConfigCO.getAddEchoMessageListenerYN())
		    .equals(ConstantsCommon.YES);
	    replyOnError = StringUtil.nullToEmpty(atmChannelConfigCO.getReplyOnErrorYN()).equals(ConstantsCommon.YES);
	    idleTimeout = NumberUtil.nullEmptyToValue(atmChannelConfigCO.getIdleTimeOut(),
		    AtmConnectorConstants.DEFAULT_IDLE_TIMEOUT_SECONDS).intValue();

	}
	catch(Exception exception)
	{
	    throw new IllegalConfigurationException(EngineError.CON_CONF_FAIL_TO_LOAD, exception);
	}

	return (Config) this;
    }

    /**
     * Allows to add default echo message listener to the pipeline
     *
     * @return true if {@link EchoMessageListener} should be added to the
     *         pipeline
     */

    public boolean shouldAddEchoMessageListener()
    {
	return addEchoMessageListener;
    }

    /**
     * @param addEchoMessageListener the addEchoMessageListener to set
     */
    public Config setAddEchoMessageListener(boolean addEchoMessageListener)
    {
	this.addEchoMessageListener = addEchoMessageListener;
	return (Config) this;
    }

    /**
     * @return the port
     */
    public Integer getPort()
    {
	return port;
    }

    /**
     * @param port the port to set
     * @return
     */
    public Config setPort(Integer port)
    {
	this.port = port;
	return (Config) this;
    }

    /**
     * @return the host
     */
    public String getHost()
    {
	return host;
    }

    /**
     * @param host the host to set
     * @return
     */
    public Config setHost(String host)
    {
	this.host = host;
	return (Config) this;
    }

    /**
     * @return the workerThreadsCount
     */
    public int getWorkerThreadsCount()
    {
	return workerThreadsCount;
    }

    /**
     * @param workerThreadsCount the workerThreadsCount to set
     */
    public Config setWorkerThreadsCount(int workerThreadsCount)
    {
	this.workerThreadsCount = workerThreadsCount;
	return (Config) this;

    }

    /**
     * @return the frameLengthFieldLength
     */
    public int getFrameLengthFieldLength()
    {
	return frameLengthFieldLength;
    }

    /**
     * @param frameLengthFieldLength the frameLengthFieldLength to set
     */
    public Config setFrameLengthFieldLength(int frameLengthFieldLength)
    {
	this.frameLengthFieldLength = frameLengthFieldLength;
	return (Config) this;
    }

    /**
     * @return the lengthType
     */
    public String getLengthType()
    {
	return lengthType;
    }

    /**
     * @param lengthType the lengthType to set
     */
    public Config setLengthType(String lengthType)
    {
	this.lengthType = lengthType;
	return (Config) this;
    }

    /**
     * @return the includeLength
     */
    public boolean isIncludeLength()
    {
	return includeLength;
    }

    /**
     * @param includeLength the includeLength to set
     */
    public Config setIncludeLength(boolean includeLength)
    {
	this.includeLength = includeLength;
	return (Config) this;
    }

    /**
     * @return the frameLengthFieldOffset
     */
    public int getFrameLengthFieldOffset()
    {
	return frameLengthFieldOffset;
    }

    /**
     * @param frameLengthFieldOffset the frameLengthFieldOffset to set
     */
    public Config setFrameLengthFieldOffset(int frameLengthFieldOffset)
    {
	this.frameLengthFieldOffset = frameLengthFieldOffset;
	return (Config) this;
    }

    /**
     * @return the headerLength
     */
    public int getHeaderLength()
    {
	return headerLength;
    }

    /**
     * @param headerLength the headerLength to set
     */
    public Config setHeaderLength(int headerLength)
    {
	this.headerLength = headerLength;
	return (Config) this;
    }

    /**
     * @return the includeHeaderLength
     */
    public boolean getIncludeHeaderLength()
    {
	return includeHeaderLength;
    }

    /**
     * @param includeHeaderLength the includeHeaderLength to set
     */
    public Config setIncludeHeaderLength(boolean includeHeaderLength)
    {
	this.includeHeaderLength = includeHeaderLength;
	return (Config) this;
    }

    /**
     * @return the maxFrameLength
     */
    public int getMaxFrameLength()
    {
	return maxFrameLength;
    }

    /**
     * @param maxFrameLength the maxFrameLength to set
     */
    public Config setMaxFrameLength(int maxFrameLength)
    {
	this.maxFrameLength = maxFrameLength;
	return (Config) this;
    }

    /**
     * Channel read/write idle timeout in seconds.
     * <p>
     * If no message was received/sent during specified time interval then
     * `Echo` message will be sent.
     * </p>
     *
     * @return timeout in seconds
     */
    public int getIdleTimeout()
    {
	return idleTimeout;
    }

    /**
     * @param idleTimeout the idleTimeout to set
     */
    public Config setIdleTimeout(int idleTimeout)
    {
	this.idleTimeout = idleTimeout;
	return (Config) this;
    }

    /**
     * Returns true is {@link IsoMessageLoggingHandler}
     * <p>
     * Allows to disable adding default logging handler to
     * {@link ChannelPipeline}.
     * </p>
     *
     * @return true if {@link IsoMessageLoggingHandler} should be added.
     */
    public boolean addLoggingHandler()
    {
	return addLoggingHandler;
    }

    /**
     * @param addLoggingHandler the addLoggingHandler to set
     */
    public Config setAddLoggingHandler(boolean addLoggingHandler)
    {
	this.addLoggingHandler = addLoggingHandler;
	return (Config) this;
    }

    /**
     * Whether to reply with administrative message in case of message syntax
     * errors. Default value is <code>false.</code>
     *
     * @return true if reply message should be sent in case of error parsing the
     *         message.
     */
    public boolean replyOnError()
    {
	return replyOnError;
    }

    /**
     * @param replyOnError the replyOnError to set
     */
    public Config setReplyOnError(boolean replyOnError)
    {
	this.replyOnError = replyOnError;
	return (Config) this;
    }

    /**
     * Returns <code>true</code> if sensitive information like PAN, CVV/CVV2,
     * and Track2 should be printed to log.
     * <p>
     * Default value is <code>true</code> (sensitive data is printed).
     * </p>
     *
     * @return <code>true</code> if sensitive data should be printed to log
     */
    public boolean logSensitiveData()
    {
	return logSensitiveData;
    }

    /**
     * @param logSensitiveData the logSensitiveData to set
     */
    public Config setLogSensitiveData(boolean logSensitiveData)
    {
	this.logSensitiveData = logSensitiveData;
	return (Config) this;
    }

    /**
     * Return true if field descriptions should be printed in log.
     * 
     * @return
     */
    public boolean logFieldDescription()
    {
	return logFieldDescription;
    }

    /**
     * @param logFieldDescription the logFieldDescription to set
     */
    public Config setLogFieldDescription(boolean logFieldDescription)
    {
	this.logFieldDescription = logFieldDescription;
	return (Config) this;
    }

    /**
     * Returns field numbers to be treated as sensitive data. Use
     * <code>null</code> to use default ones
     *
     * @return array of ISO8583 sensitive field numbers to be masked, or
     *         <code>null</code> to use default fields.
     * @see IsoMessageLoggingHandler
     * @see IsoMessageLoggingHandler#DEFAULT_MASKED_FIELDS
     */
    public int[] getSensitiveDataFields()
    {
	return sensitiveDataFields;
    }

    /**
     * @param sensitiveDataFields the sensitiveDataFields to set
     */
    public Config setSensitiveDataFields(int[] sensitiveDataFields)
    {
	this.sensitiveDataFields = sensitiveDataFields;
	return (Config) this;
    }
}
