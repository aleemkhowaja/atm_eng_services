package com.path.atm.engine.connector.util;

import java.math.BigDecimal;

import com.path.atm.engine.connector.Connector;
import com.path.atm.engine.core.AtmEngineReactor;
import com.path.atm.engine.iso8583.MessageFactory;

import io.netty.util.AttributeKey;

/**
 * Hold all constants related to Atm Server/Client Connector
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmConnectorConstants
{
    /**
     * Connector Reference Key
     */
    public static final AttributeKey<Connector> CONNECTOR_KEY = AttributeKey.newInstance("connectorRef");

    /**
     * Reactor Reference Key
     */
    public static final AttributeKey<AtmEngineReactor> REACTOR_KEY = AttributeKey.newInstance("reactorRef");

    /**
     * Reactor Reference Key
     */
    public static final AttributeKey<MessageFactory> MSGFACTORY_KEY = AttributeKey.newInstance("msgFactoryRef");

    /**
     * Default number of worker
     */
    public static final BigDecimal DEFAULT_NB_OF_WRK = new BigDecimal(2);
    /**
     * Default read/write idle timeout in seconds (ping interval) = 30 sec.
     */
    public static final BigDecimal DEFAULT_IDLE_TIMEOUT_SECONDS = new BigDecimal(30);

    /**
     * Default (max message length) = 8192
     */
    public static final int DEFAULT_MAX_FRAME_LENGTH = 12348;

    /**
     * Default (length of TCP Frame length) = 2
     */
    public static final BigDecimal DEFAULT_FRAME_LENGTH_FIELD_LENGTH = new BigDecimal(2);

    /**
     * Default (compensation value to add to the value of the length field) = 0
     */
    public static final BigDecimal DEFAULT_FRAME_LENGTH_FIELD_ADJUST = BigDecimal.ZERO;

    /**
     * Default (the offset of the length field) = 0
     */
    public static final int DEFAULT_FRAME_LENGTH_FIELD_OFFSET = 0;

    /**
     * Default header length
     */
    public static final BigDecimal DEFAULT_HEADER_LENGTH = BigDecimal.ZERO;

    /**
     * Default client reconnect interval in milliseconds.
     */
    public static final int DEFAULT_RECONNECT_INTERVAL = 100;

}
