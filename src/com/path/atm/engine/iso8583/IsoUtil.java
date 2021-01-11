package com.path.atm.engine.iso8583;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.vo.engine.AtmIsoFieldCO;
import com.path.bo.common.CommonLibBO;
import com.path.lib.common.exception.BaseException;
import com.path.lib.common.util.ApplicationContextProvider;
import com.path.lib.common.util.StringUtil;
import com.path.lib.log.Log;
import com.solab.iso8583.IsoType;

/**
 * This class serve as iso util
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IsoUtil
{

    /**
     * Error Logger
     */
    private static Log logger = Log.getInstance();

    /**
     * Array of hexadecimal characters
     */
    static final byte[] HEX = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
	    'F' };

    public static final String[] hexStrings;

    static
    {
	hexStrings = new String[256];
	for(int i = 0; i < 256; i++)
	{
	    StringBuilder d = new StringBuilder(2);
	    char ch = Character.forDigit((byte) i >> 4 & 0x0F, 16);
	    d.append(Character.toUpperCase(ch));
	    ch = Character.forDigit((byte) i & 0x0F, 16);
	    d.append(Character.toUpperCase(ch));
	    hexStrings[i] = d.toString();
	}

    }

    /**
     * Return available fields
     * 
     * @todo move to util
     * @param bitMap
     * @param isoFieldDefs
     * @return
     */
    public static List<AtmIsoFieldCO> returnAvailableFields(String bitMap,
	    HashMap<BigDecimal, AtmIsoFieldCO> isoFieldDefs)
    {

	// @todo we may use java 8 filter here
	ArrayList<AtmIsoFieldCO> fields = new ArrayList<>();
	List<BigDecimal> msgReqFields = IsoUtil.getAvailableFields(bitMap);

	// filter the needed fields
	for(BigDecimal fieldIdx : msgReqFields)
	    fields.add(isoFieldDefs.get(fieldIdx));

	return fields;
    }

    /**
     * Return list of available fields in an iso message based on the given
     * bitmap
     * 
     * @param bitmap
     * @return
     */
    public static List<BigDecimal> getAvailableFields(String bitmapStr)
    {

	if(null == bitmapStr || bitmapStr.length() == 0)
	    throw new IllegalArgumentException("Bitmap string can't be null or empty");

	String bitmapBin = hexaToBinary(bitmapStr);

	// if secondary bitmap not set
	if(bitmapBin.startsWith("0"))
	    bitmapBin = bitmapBin.substring(0, 64);

	List<BigDecimal> bitMapFields = new ArrayList<BigDecimal>();

	char[] bitmapBits = bitmapBin.toCharArray();

	for(int i = 0; i < bitmapBits.length; i++)
	{

	    if('1' == bitmapBits[i])
		bitMapFields.add(new BigDecimal(i + 1));
	}

	return bitMapFields;

    }

    /**
     * Convert hexa decimal number to binary
     * 
     * @note : to refactor
     * @param hexaDecimal
     * @return
     */
    public static String hexaToBinary(String hexaDecimal)
    {
	StringBuilder binary = new StringBuilder();
	if(!StringUtil.isEmptyString(hexaDecimal))
	{
	    String bin = "";
	    char hexa[] = hexaDecimal.toCharArray();
	    for(int i = 0; i < hexa.length; i++)
	    {
		int j = Integer.parseInt(hexa[i] + "", 16);
		bin = Integer.toBinaryString(j);
		while(bin.length() != 4)
		{
		    bin = "0" + bin;
		}
		binary.append(bin);
	    }
	}
	return binary.toString();
    }

    /**
     * This method will convert a bit set ( bitmap ) to hex string
     * 
     * @param bs
     * @return
     */
    public static String convertBitmapToHex(BitSet bs)
    {

	int pos = 0;
	int lim = bs.size() / 4;

	StringBuilder sb = new StringBuilder();

	for(int i = 0; i < lim; i++)
	{
	    int nibble = 0;
	    if(bs.get(pos++))
		nibble |= 8;
	    if(bs.get(pos++))
		nibble |= 4;
	    if(bs.get(pos++))
		nibble |= 2;
	    if(bs.get(pos++))
		nibble |= 1;
	    sb.append(new String(HEX, nibble, 1));
	}

	return sb.toString();
    }

    /**
     * Convert mti integer to it's hex representation in the DB which is hex
     * without x ex: 512 int will be converted to 0200 not 0x200
     * 
     * @return
     */
    public static String convertMtiToHex(int mtiCode)
    {
	return StringUtils.leftPad(Integer.toHexString(mtiCode), 4, "0");
    }

    /**
     * Convert mti hex to int Ex : 0200 will be converted to 528 after adding 0x
     * 
     * @return
     */
    public static int convertMtiToInt(String mtiCode)
    {
	mtiCode = mtiCode.startsWith("0") ? mtiCode.replaceFirst("^0+(?!$)", "0x") : "0x".concat(mtiCode);
	return Integer.decode(mtiCode);
    }

    /**
     * Creates a BitSet for the bitmap of a given message.
     **/
    public static BitSet createBitmapBitSet(AtmIsoMessage isoMsg)
    {
	BitSet bs = new BitSet(isoMsg.getForceSecondaryBitmap() ? 128 : 64);
	for(int i = 2; i < 129; i++)
	{
	    if(isoMsg.hasField(i))
	    {
		bs.set(i - 1);
	    }
	}
	if(isoMsg.getForceSecondaryBitmap())
	{
	    bs.set(0);
	}
	else if(bs.length() > 64)
	{
	    // Extend to 128 if needed
	    BitSet b2 = new BitSet(128);
	    b2.or(bs);
	    bs = b2;
	    bs.set(0);
	}
	return bs;
    }

    /**
     * Note : If input is greater than length range we are returning it
     * 
     * @param inputNum
     * @param length
     * @return
     */
    public static String convertLenToAscii(int inputNum, int length)
    {
	String asciiBase256 = fromDeci(256, inputNum);
	return StringUtils.leftPad(asciiBase256, length, (char) 0);
    }

    /**
     * 
     * @param inputNum
     * @param length
     * @return
     */
    public static int convertLenFromAscii(String asciiNb)
    {
	return toDeci(asciiNb, 256);
    }

    /**
     * converts a byte array to printable characters
     * 
     * @param b - byte array
     * @return String representation
     */
    public static String dumpString(byte[] b)
    {
	StringBuilder d = new StringBuilder(b.length * 2);
	for(byte aB : b)
	{
	    char c = (char) aB;
	    if(Character.isISOControl(c))
	    {
		// TODO: complete list of control characters,
		// use a String[] instead of this weird switch
		switch (c)
		{
		    case '\r':
			d.append("{CR}");
			break;
		    case '\n':
			d.append("{LF}");
			break;
		    case '\000':
			d.append("{NULL}");
			break;
		    case '\001':
			d.append("{SOH}");
			break;
		    case '\002':
			d.append("{STX}");
			break;
		    case '\003':
			d.append("{ETX}");
			break;
		    case '\004':
			d.append("{EOT}");
			break;
		    case '\005':
			d.append("{ENQ}");
			break;
		    case '\006':
			d.append("{ACK}");
			break;
		    case '\007':
			d.append("{BEL}");
			break;
		    case '\020':
			d.append("{DLE}");
			break;
		    case '\025':
			d.append("{NAK}");
			break;
		    case '\026':
			d.append("{SYN}");
			break;
		    case '\034':
			d.append("{FS}");
			break;
		    case '\036':
			d.append("{RS}");
			break;
		    default:
			d.append('[');
			d.append(hexStrings[(int) aB & 0xFF]);
			d.append(']');
			break;
		}
	    }
	    else
		d.append(c);

	}
	return d.toString();
    }

    /**
     * 
     * evaluate the expression
     * 
     * @throws BaseException
     * 
     */
    public static Object evaluateExpression(String exprSyntax, Map<String, Object> exprDataMap) throws BaseException
    {
	if(StringUtil.isEmptyString(exprSyntax))
	    throw new IllegalArgumentException("evaluateExpression: exprSyntax is required ");

	// get the bean
	CommonLibBO commonLibBO = (CommonLibBO) ApplicationContextProvider.getApplicationContext()
		.getBean("commonLibBO");

	List<Map<String, Object>> exprDataList = new ArrayList<Map<String, Object>>();
	exprDataList.add(exprDataMap);

	Object result = commonLibBO.executeExpression(exprSyntax, 0, exprDataList);

	Log.getInstance().debug("Expression".concat(exprSyntax).concat(" Params").concat(exprDataMap.toString())
		.concat("Result").concat(String.valueOf(result)));

	return result;
    }

    /**
     * Function to convert a given decimal number to a base 'base'
     * 
     * @param base
     * @param inputNum
     * @return
     */
    public static String fromDeci(int base, int inputNum)
    {

	String s = "";

	/**
	 * Convert input number is given base by repeatedly dividing it by base
	 * and taking remainder
	 */
	while(inputNum > 0)
	{
	    s += (char) (inputNum % base);
	    inputNum /= base;
	}

	StringBuilder ix = new StringBuilder();

	// append a string into StringBuilder input1
	ix.append(s);

	// Reverse the result
	return new String(ix.reverse());
    }

    /**
     * Mask Field based on the given mask
     * 
     * @param mask
     * @param field
     * @return
     */
    public static String maskField(String field, String mask)
    {

	// build the mask and complete the missing bits by applying right
	// padding
	mask = StringUtils.rightPad(StringUtil.nullEmptyToValue(mask, ""), field.length(),
		IsoDefinitionConstants.MASK_SHOW_BIT);

	// if mask is full 0
	if(mask.indexOf(IsoDefinitionConstants.MASK_SHOW_BIT) < 0)
	    return field.replaceAll("(?s).", String.valueOf(IsoDefinitionConstants.MASK_CHAR));

	// if mask is full 1
	if(mask.indexOf(IsoDefinitionConstants.MASK_HIDE_BIT) < 0)
	    return field;

	StringBuilder maskedField = new StringBuilder();
	char[] fieldChars = field.toCharArray();

	for(int i = 0; i < fieldChars.length; i++)
	{
	    maskedField.append(mask.charAt(i) == IsoDefinitionConstants.MASK_SHOW_BIT ? fieldChars[i]
		    : IsoDefinitionConstants.MASK_CHAR);
	}

	return maskedField.toString();
    }

    /**
     * Return iso type based on the configuration type
     * 
     * <p>
     * List of configuration type N Numeric V Alpha Numeric T Bitmap D
     * Date(mmddHHMMSS) B Binary M Date(mmdd) H Time(HHMMSS) E Date(yymm) A
     * Amount
     * 
     * @param dataType
     * @param length
     * @return
     */
    public static IsoType returnISOTypes(String dataType, BigDecimal dynamicLength)
    {
	IsoType isoType = null;

	switch (dataType)
	{
	    case AtmEngineConstants.ISO_FIELD_TYPE_NUMERIC:
		isoType = IsoType.NUMERIC;
		break;

	    case AtmEngineConstants.ISO_FIELD_TYPE_BINARY:

		dynamicLength = null == dynamicLength ? BigDecimal.ZERO : dynamicLength;

		if(dynamicLength.intValue() > 4 || dynamicLength.intValue() < 0)
		    throw new IllegalArgumentException("Invalid Binary Length");

		// @todo change the approach
		int indexBin = dynamicLength.intValue() > 0 ? dynamicLength.intValue() - 1 : dynamicLength.intValue();
		IsoType[] isoBinaryType = { IsoType.BINARY, IsoType.LLBIN, IsoType.LLLBIN, IsoType.LLLLBIN };
		isoType = isoBinaryType[indexBin];

		break;

	    case AtmEngineConstants.ISO_FIELD_TYPE_ALPHA:

		dynamicLength = null == dynamicLength ? BigDecimal.ZERO : dynamicLength;

		if(dynamicLength.intValue() > 5 || dynamicLength.intValue() < 0)
		    throw new IllegalArgumentException("Invalid Alpha Length");

		// @todo change the approach
		int index = dynamicLength.intValue();
		IsoType[] isoAlphaType = { IsoType.ALPHA, IsoType.LVAR, IsoType.LLVAR, IsoType.LLLVAR, IsoType.LLLLVAR,
			IsoType.LLLLLVAR };

		isoType = isoAlphaType[index];

		break;

	    // fixed width of 12. For example one dollar is encoded as
	    // 000000000100
	    case AtmEngineConstants.ISO_FIELD_TYPE_AMOUNT:
		isoType = IsoType.AMOUNT;
		break;

	    // Date in format mmdd, fixed width of 4
	    case AtmEngineConstants.ISO_FIELD_TYPE_DATE4:
		isoType = IsoType.DATE4;
		break;

	    // Date in format mmddHHMMSS, fixed width of 10
	    case AtmEngineConstants.ISO_FIELD_TYPE_DATE10:
		isoType = IsoType.DATE10;
		break;

	    // Time of day in format HHMMSS, fixed width of 6
	    case AtmEngineConstants.ISO_FIELD_TYPE_TIME:
		isoType = IsoType.TIME;
		break;

	    // Date in format yymm, fixed width of 4.
	    // Used for credit card expiration dates
	    case AtmEngineConstants.ISO_FIELD_TYPE_EXP_DATE:
		isoType = IsoType.DATE_EXP;
		break;

	    // Date in format yyMMdd, fixed width of 6
	    case AtmEngineConstants.ISO_FIELD_TYPE_DATE6:
		isoType = IsoType.DATE6;
		break;

	    // Date in format yyMMddHHmmss, fixed width of 12
	    case AtmEngineConstants.ISO_FIELD_TYPE_DATE12:
		isoType = IsoType.DATE12;
		break;

	    // Date in format YYYYMMddHHmmss, fixed width of 14
	    case AtmEngineConstants.ISO_FIELD_TYPE_DATE14:
		isoType = IsoType.DATE14;
		break;
	}

	return isoType;
    }

    /**
     * This method will return an empty iso Message hashMap
     * 
     * @param isofieldsList
     * @return
     */
    public static HashMap<String, Object> returnEmptyIsoMsgHm(HashMap<BigDecimal, AtmIsoFieldCO> isofieldsList)
    {

	if(null == isofieldsList || isofieldsList.isEmpty())
	    throw new IllegalArgumentException("isofieldsList is mandatory");

	HashMap<String, Object> isoMsgHm = new HashMap<String, Object>();
	Iterator<Entry<BigDecimal, AtmIsoFieldCO>> it = isofieldsList.entrySet().iterator();

	while(it.hasNext())
	{

	    Entry<BigDecimal, AtmIsoFieldCO> fieldEntry = it.next();
	    AtmIsoFieldCO isoField = fieldEntry.getValue();

	    isoMsgHm.put(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(isoField.getCode())), null);

	    if(null == isoField.getIsoFieldSubList() || isoField.getIsoFieldSubList().isEmpty())
		continue;

	    int subFieldIndex = 1;
	    for(AtmIsoFieldCO subField : isoField.getIsoFieldSubList())
	    {
		isoMsgHm.put(IsoDefinitionConstants.ISO_HM_FIELD_PREFIX.concat(String.valueOf(isoField.getCode()))
			.concat(IsoDefinitionConstants.ISO_HM_SUBFIELD_SEPARATOR).concat(String.valueOf(subFieldIndex)),
			null);
		subFieldIndex++;
	    }
	}

	// Set the main custom fields list
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_MTICODE, null);
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_COMPCODE, null);
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_BRCODE, null);
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_TELLCODE, null);
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_TRXCODE, null);
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_AUTHCODE, null);
	isoMsgHm.put(IsoDefinitionConstants.ISO_FIELD_MERACC, null);

	return isoMsgHm;
    }

    /**
     * Return the iso message definition key based on mti code and process code
     * MTICODE_PROCESSCODE
     * 
     * @param mtiCode
     * @param processCode
     * @return
     */
    public static String returnIsoMsgDefKey(String mtiCode, String processCode)
    {

	StringBuilder key = new StringBuilder();
	key.append(StringUtil.nullEmptyToValue(mtiCode, IsoDefinitionConstants.ISO_MSG_DEF_KEY_MASK))
		.append(IsoDefinitionConstants.ISO_MSG_DEF_KEY_JOINER)
		.append(StringUtil.nullEmptyToValue(processCode, IsoDefinitionConstants.ISO_MSG_DEF_KEY_MASK));

	return key.toString();

    }

    /**
     * This is a help method to retrieve the trx number sent in the response
     * it's full safe method since some teams are returning the trx nb as string
     * number with a long decimal part while others return it as an string
     * integer.
     * 
     * @todo revisit
     */
    public static BigDecimal returnTrxNb(HashMap<String, Object> data)
    {

	if(!data.containsKey(IsoDefinitionConstants.ISO_FIELD_CORE_TRX_NB)
		|| null == data.get(IsoDefinitionConstants.ISO_FIELD_CORE_TRX_NB))
	    return null;

	try
	{

	    if(data.get(IsoDefinitionConstants.ISO_FIELD_CORE_TRX_NB) instanceof String)
	    {

		String trxNbStr = (String) data.get(IsoDefinitionConstants.ISO_FIELD_CORE_TRX_NB);
		return new BigDecimal(trxNbStr);
	    }

	    // @note : till now we didn't face such scenario
	    if(data.get(IsoDefinitionConstants.ISO_FIELD_CORE_TRX_NB) instanceof BigDecimal)
		return (BigDecimal) data.get(IsoDefinitionConstants.ISO_FIELD_CORE_TRX_NB);

	}
	catch(Exception exp)
	{
	    logger.error(exp, "Error while extracting the trx number");
	}

	return null;
    }

    /**
     * Convert A string from given base to decimal
     * 
     * @param str
     * @param base
     * @return
     */
    public static int toDeci(String nbAsStr, int base)
    {

	int len = nbAsStr.length();
	int power = 1;
	int num = 0;

	/**
	 * Decimal equivalent is str[len-1]*1 + str[len-1] * base +
	 * str[len-1]*(base^2) + ...
	 */
	for(int i = len - 1; i >= 0; i--)
	{
	    /**
	     * A digit in input number must be less than number's base
	     */
	    if((int) (nbAsStr.charAt(i)) >= base)
		// Invalid Number
		return -1;

	    num += (int) (nbAsStr.charAt(i)) * power;
	    power = power * base;
	}

	return num;
    }
}
