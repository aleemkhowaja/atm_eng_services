package com.path.atm.engine.connector.pipeline.listener;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 */
public class EchoMessageListener<T extends IsoMessage> implements IsoMessageListener<T>
{

    private final MessageFactory<T> isoMessageFactory;

    public EchoMessageListener(MessageFactory<T> isoMessageFactory)
    {
	this.isoMessageFactory = isoMessageFactory;
    }

    @Override
    public boolean applies(IsoMessage isoMessage)
    {
	return isoMessage != null && isoMessage.getType() == 0x800;
    }

    /**
     * Sends EchoResponse message. Always returns <code>false</code>.
     *
     * @param isoMessage a message to handle
     * @return <code>false</code> - message should not be handled by any other
     *         handler.
     */
    @Override
    public boolean onMessage(ChannelHandlerContext ctx, T isoMessage)
    {

	System.out.println("[EchoMessageListener] Iso8583Decoder");
	final IsoMessage echoResponse = isoMessageFactory.createResponse(isoMessage);
	ctx.writeAndFlush(echoResponse);
	return false;
    }
}
