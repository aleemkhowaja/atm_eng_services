package com.path.atm.engine.iso8583;

import java.io.IOException;
import java.io.StringReader;
import org.xml.sax.InputSource;

import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.engine.AtmInterfaceCO;
import com.solab.iso8583.parse.ConfigParser;

/**
 * This class house all the behaviors related to the configuration parsing
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IsoConfigParser extends ConfigParser
{

    /**
     * Creates a message factory from the specified path inside the classpath,
     * using MessageFactory's ClassLoader.
     * 
     * @param interfaceCO
     * 
     * @todo Split exception to two io and bo exception and handle each one and
     *       re-throw a runtime exception
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static MessageFactory createIsoFactory(AtmInterfaceCO interfaceCO, String configuration)
    {

	MessageFactory mfact = new MessageFactory(interfaceCO);

	/**
	 * One : Hexadecimal Two : Binary
	 */
	mfact.setUseBinaryBitmap(interfaceCO.getBitmapType().equals(AtmEngineConstants.BITMAP_8));
	mfact.setUseAsciiEncoding(interfaceCO.getLengthType().equals(AtmEngineConstants.LENGTH_BASE256));
	mfact.setCharacterEncoding(AtmEngineConstants.CHAR_ENCODING.toString());
	// @note : enable this line when implemented on the interface
	mfact.setUseBinaryMessages(false);

	configureFromStr(mfact, configuration);
	mfact.init();
	return mfact;
    }

    /**
     * Configure the message factory from a string
     * 
     * @throws IOException
     */
    private static void configureFromStr(MessageFactory mfact, String configStr)
    {

	try
	{
	    InputSource inputSource = new InputSource(new StringReader(configStr));
	    parse(mfact, inputSource);
	}
	catch(IOException exp)
	{
	    // @note: not sure if this is the best exception
	    throw new IllegalConfigurationException(EngineError.MSG_FACT_FAILED_TO_CONF, exp);
	}

    }

}
