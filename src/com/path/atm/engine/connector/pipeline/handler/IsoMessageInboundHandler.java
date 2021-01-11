package com.path.atm.engine.connector.pipeline.handler;

import com.path.atm.engine.connector.pipeline.listener.IsoMessageListener;
import com.path.lib.log.Log;
import com.solab.iso8583.IsoMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handles {@link IsoMessage} s with chain of {@link IsoMessageListener}s.
 * 
 * @author MohammadAliMezzawi
 *
 * @param <T>
 */
public class IsoMessageInboundHandler<T extends IsoMessage> extends ChannelInboundHandlerAdapter
{

    /**
     * Logger
     */
    private Log logger = Log.getInstance();

    /**
     * List of message listeners
     */
    private final List<IsoMessageListener<T>> messageListeners = new CopyOnWriteArrayList<>();

    /**
     * Falg to specify if an exception will be thrown in case it occur while
     * invoking the listeners
     */
    private final boolean failOnError;

    /**
     * @param failOnError
     */
    public IsoMessageInboundHandler(boolean failOnError)
    {
	this.failOnError = failOnError;
    }

    /**
     * Default constructor Always fail on error
     */
    public IsoMessageInboundHandler()
    {
	this(true);
    }

    /**
     * Invoke the attached listener in case the message is an AtmIsoMessage and
     * then forward it to the next {@link ChannelInboundHandler} in the
     * {@link ChannelPipeline}.
     *
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {

	logger.debug("[IsoMessageInboundHandler] start reading");

	// handle IsoMessage only
	if(msg instanceof IsoMessage)
	    doHandleMessage(ctx, msg);

	super.channelRead(ctx, msg);
    }

    /**
     * Invoke attached listeners
     * 
     * @param ctx
     * @param msg
     */
    protected void doHandleMessage(ChannelHandlerContext ctx, Object msg)
    {

	logger.debug("[IsoMessageInboundHandler] start handling message");

	/**
	 * In case casting failed we will not catch and continue, rather we will
	 * handle the exception at the {@link PipelineException}
	 */
	T isoMessage = (T) msg;

	boolean applyNextListener = true;
	final int size = messageListeners.size();

	// start invoking all attached listener
	for(int i = 0; applyNextListener && i < size; i++)
	{

	    IsoMessageListener<T> messageListener = messageListeners.get(i);
	    try
	    {

		// check if we should invoke this listener
		if(!messageListener.applies(isoMessage))
		    continue;

		logger.debug(String.format("Handling IsoMessage[@type=0x%s] with %s",
			String.format("%04X", isoMessage.getType()), messageListener));

		// invoke listener and check if we should fire the next listener
		applyNextListener = messageListener.onMessage(ctx, isoMessage);

		if(!applyNextListener)
		    logger.debug(String.format("Stopping further procession of message %s after handler %s", isoMessage,
			    messageListener));

	    }
	    catch(Exception exception)
	    {
		// logger.debug("Can't evaluate {}.apply({})", messageListener,
		// isoMessage.getClass(), e);
		logger.debug(String.format("Can't evaluate %s", messageListener));

		if(failOnError)
		    throw exception;
	    }
	}
    }

    /**
     * Handle exception thrown during handler invocation
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
	Log.getInstance().error("[IsoMessageInboundHandler] : exceptionCaught");
	super.exceptionCaught(ctx, cause);
    }

    /**
     * Add a new listener to the list
     * 
     * @param listener
     */
    public void addListener(IsoMessageListener<T> listener)
    {
	Objects.requireNonNull(listener, "IsoMessageListener is required");
	messageListeners.add(listener);
    }

    /**
     * Add list of listeners to the handler
     * 
     * @param listeners
     */
    public final void addListeners(IsoMessageListener<T>... listeners)
    {
	Objects.requireNonNull(listeners, "IsoMessageListeners must not be null");
	for(IsoMessageListener<T> listener : listeners)
	{
	    addListener(listener);
	}
    }

    /**
     * Remove listener from the list
     * 
     * @param listener
     */
    public void removeListener(IsoMessageListener<T> listener)
    {
	messageListeners.remove(listener);
    }
}
