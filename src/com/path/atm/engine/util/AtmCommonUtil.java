package com.path.atm.engine.util;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.path.atm.engine.exception.BaseEngineException;
import com.path.atm.engine.exception.BaseEngineRuntimeException;
import com.path.atm.engine.exception.IsoRuntimeException;
import com.path.lib.common.util.PathPropertyUtil;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;

/**
 * Hold All utility related to the atm Engine
 * 
 * @author MohammadAliMezzawi
 *
 */
public class AtmCommonUtil
{

    /**
     * Get Engine Base Exception cause Message This method will try it best to
     * always return the related Engine exception Since it will be used for
     * Display not for logging.
     * 
     * @note : Don't ever using this for exception logging.
     * 
     *  <p>
     *  Get the first Engine parent exception if exists to get the error
     *  code
     *  
     * @param cause
     * @return
     */
    public static String getEngExpCauseMsg(Throwable cause)
    {

	Throwable engRootCause = getEngRootCause(cause);

	if(null != engRootCause)
	    return engRootCause.getMessage();

	// now check if the main wrapper is an Engine exception
	if(cause instanceof BaseEngineRuntimeException 
		|| cause instanceof BaseEngineException)
	    return cause.getMessage();

	return getRootCauseMsg(cause);
    }

    /**
     * Return the first Iso parent exception.
     * 
     * @param throwable
     * @return
     */
    public static Throwable getEngRootCause(Throwable throwable)
    {

	// get all list exception
	ArrayList<Throwable> throwableList = (ArrayList) ExceptionUtils
		.getThrowableList(throwable);

	Throwable engineException = null;

	// loop over the exceptions list
	for(int i = throwableList.size() - 1; i > 0; i--)
	{

	    Throwable exp = throwableList.get(i);
	    if(!(exp instanceof BaseEngineRuntimeException 
		    || exp instanceof BaseEngineException))
		continue;

	    engineException = exp;
	    break;
	}

	return engineException;
    }

    /**
     * Return the Root cause Message
     * 
     * @param cause
     * @return
     */
    public static String getRootCauseMsg(Throwable cause)
    {

	Throwable rootCause = ExceptionUtils.getRootCause(cause);

	if(null == rootCause)
	    return cause.getMessage();

	return rootCause.getMessage();
    }
    
    
    /**
     * Returns the first <code>Throwable</code> that matches the specified class 
     * or subclass in the exception chain.
     */
    public static Throwable getExceptionByTtype(Throwable cause, Class clazz) {
	int index = ExceptionUtils.indexOfThrowable(cause, clazz);
	
	if(index < 0)
	    return null;
	
	Throwable[] throwables = ExceptionUtils.getThrowables(cause);
	return throwables[index];
    }
    

    /**
     * @param clazz
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static Field getField(Class<?> clazz, String fieldName) throws Exception
    {
	Class<?> tmpClass = clazz;
	do
	{
	    try
	    {
		return tmpClass.getDeclaredField(fieldName);
	    }
	    catch(NoSuchFieldException e)
	    {
		tmpClass = tmpClass.getSuperclass();
	    }
	}while(tmpClass != null);

	throw new Exception("Field '" + fieldName + 
		"' not found on class " + clazz);
    }
    
    /**
     * Check if this handler should handle the exception The below exception
     * should be handled 1- UnsupportedEncodingException 2- ParseException
     * 
     * @param cause
     * @return
     */
    public static boolean isExceptionInstanceOf(Throwable cause, Class clazz)
    {

	if(clazz.isInstance(cause))
	    return true;

	// get cause
	Throwable exceptionCause = cause.getCause();

	if(null == exceptionCause)
	    return false;

	// check direct cause
	if(clazz.isInstance(exceptionCause))
	    return true;

	// get the root cause
	Throwable rootCause = ExceptionUtils.getRootCause(cause);

	if(null == rootCause)
	    return false;

	if(clazz.isInstance(rootCause))
	    return true;

	return false;
    }

    /**
     * Return Local Service Url
     * 
     * @return
     */
    public static String returnLocalServiceUrl()
    {

	String servicesUrl = null;
	String localServiceURL = null;

	try
	{
	    servicesUrl = PathPropertyUtil.getPathRemotingProp("PathAtmEngineRemoting",
		    "atmEngine.serviceURL");

	    String hostIp = InetAddress.getLocalHost().getHostAddress();
	    String hostName = StringUtil.nullToEmpty(InetAddress.getLocalHost()
		    .getHostName()).toLowerCase();

	    String[] listOfServicesUrl = servicesUrl.split(",");

	    for(String url : listOfServicesUrl)
	    {
		if(url.contains(hostIp) || url.toLowerCase().contains(hostName))
		{
		    localServiceURL = url;
		    break;
		}
	    }

	}
	catch(Exception e)
	{
	    Log.getInstance().error(e, "Error while retrieving local Service Url");
	}

	return localServiceURL;
    }
}
