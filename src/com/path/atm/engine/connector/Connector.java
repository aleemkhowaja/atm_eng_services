package com.path.atm.engine.connector;

import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelFuture;

/**
 * @author MohammadAliMezzawi
 *
 */
public interface Connector
{

    /**
     * Start connector
     */
    public void start() throws Exception;

    /**
     * Shutdown the connector
     */
    public void shutdown() throws Exception;

    /**
     * @param isoMessage
     * @return
     */
    public ChannelFuture sendAsync(IsoMessage isoMessage);
}
