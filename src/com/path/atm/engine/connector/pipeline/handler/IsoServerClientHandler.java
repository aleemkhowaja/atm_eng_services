package com.path.atm.engine.connector.pipeline.handler;

import com.path.atm.engine.action.AtmActionType;
import com.path.atm.engine.action.AtmBaseAction;
import com.path.atm.engine.connector.server.AtmServerConnector;
import com.path.lib.log.Log;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author MohammadAliMezzawi
 *
 */
public class IsoServerClientHandler extends ChannelInboundHandlerAdapter
{

    /**
     * Hold Server connector reference
     */
    private final AtmServerConnector serverConnector;

    /**
     * Create Iso Server/client Handler
     * 
     * @param serverConnector
     */
    public IsoServerClientHandler(AtmServerConnector serverConnector)
    {
	this.serverConnector = serverConnector;
    }

    /**
     * Whenever a new client connection is received from the server, the
     * client's Channel is stored in the Channel Group list and notified to the
     * other clients in the list
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
	Channel incoming = ctx.channel();

	// if another switch is connected than we should throw an exception
	if(null != serverConnector.getChannel())
	{
	    // @todo throw an exception
	    incoming.close();
	    System.out.println("Another switch is connected");
	    return;
	}

	Log.getInstance().debug("[SERVER] -" + incoming.remoteAddress() + "Add \n");

	// @todo check later if we should set it at added or active level
	AtmBaseAction.getInstance(AtmActionType.CH_CLIENT_CONNECTED)
		.setAdditionalMsg(incoming.remoteAddress().toString()).log();

	// add to the channels list
	serverConnector.setChannel(ctx.channel());
    }

    /**
     * Whenever the client is disconnected from the server, Channel of the
     * client removes the Channel Group list and notifies other clients in the
     * list of Channels
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
	Channel incoming = ctx.channel();
	Log.getInstance().debug("[SERVER] -" + incoming.remoteAddress() + "Leave \n");

	// log disconnection action
	AtmBaseAction.getInstance(AtmActionType.CH_CLIENT_DISCONNECTED)
		.setAdditionalMsg(incoming.remoteAddress().toString()).log();

	// remove the channel
	serverConnector.setChannel(null);
    }

    /**
     * When a session is established
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
	// (5)
	Channel incoming = ctx.channel();
	Log.getInstance().debug("Client:" + incoming.remoteAddress() + "Online");
    }

    /**
     * At the end of the session
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
	// (6)
	Channel incoming = ctx.channel();
	Log.getInstance().debug("Client:" + incoming.remoteAddress() + "Offline");
    }

    /**
     * Abnormal
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
	// (7)
	Channel incoming = ctx.channel();
	Log.getInstance().debug("Client:" + incoming.remoteAddress() + "Exception");
	// Close the connection when an exception occurs
	cause.printStackTrace();

	// in case another switch is connected we should force close this
	// channel
	// ctx.close();

	/**
	 * @todo we should log this exception in the action table
	 */
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {

	Log.getInstance().debug("[Handler] IsoServerClientHandler");
	super.channelRead(ctx, msg);
    }
}
