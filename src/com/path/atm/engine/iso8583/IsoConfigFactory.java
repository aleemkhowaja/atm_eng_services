package com.path.atm.engine.iso8583;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import com.path.atm.engine.exception.IllegalConfigurationException;
import com.path.atm.engine.util.AtmEngineConstants;
import com.path.atm.engine.util.EngineError;
import com.path.atm.vo.engine.AtmIsoFieldCO;
import com.path.atm.vo.engine.AtmIsoMessageCO;
import com.path.lib.log.Log;

/**
 * This class hold the iso configuration file generation behavior
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IsoConfigFactory
{

    /**
     * Hold Document reference
     */
    private Document document;

    /**
     * This method generate the configuration as xml document object
     * 
     * @param isoMsgDefList
     * @param isoFieldDefs
     * @return
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * 
     * @todo throw configuratio generation exception that hold reference to the
     *       cause
     */
    public void generateConfig(ArrayList<AtmIsoMessageCO> isoMsgDefList,
	    HashMap<BigDecimal, AtmIsoFieldCO> isoFieldDefs) throws ParserConfigurationException
    {
	/**
	 * Xml Section will be moved to another method
	 */
	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
	document = documentBuilder.newDocument();

	// root element
	Element root = document.createElement("j8583-config");
	document.appendChild(root);

	// build messages configuration
	for(AtmIsoMessageCO msg : isoMsgDefList)
	{

	    // get list of Request tag field
	    String reqBitmap = msg.getReqPrimaryBitMap().concat(msg.getReqSecondaryBitMap());
	    List<AtmIsoFieldCO> availableFiels = IsoUtil.returnAvailableFields(reqBitmap, isoFieldDefs);
	    createMessageXml(root, msg.getRequestMti(), availableFiels);

	    // get list of Response
	    String respBitmap = msg.getRespPrimaryBitMap().concat(msg.getRespSecondaryBitMap());
	    List<AtmIsoFieldCO> availableRespFiels = IsoUtil.returnAvailableFields(respBitmap, isoFieldDefs);

	    createMessageXml(root, msg.getResponseMti(), availableRespFiels);
	}
    }

    /**
     * 
     * @param document
     * @return
     * @throws TransformerException
     */
    public String convertToStr() throws TransformerException
    {

	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	DOMImplementation domImpl = document.getImplementation();

	// @todo change this
	DocumentType doctype = domImpl.createDocumentType("doctype", "-//J8583//DTD CONFIG 1.0//EN",
		"http://j8583.sourceforge.net/j8583.dtd");

	transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
	transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

	StringWriter writer = new StringWriter();
	StreamResult result = new StreamResult(writer);
	DOMSource domSource = new DOMSource(document);
	transformer.transform(domSource, result);

	// print out the configuration to the file
	Log.getInstance().debug(writer.toString());

	return writer.toString();

    }

    /**
     * This methos will create the iso message xm configuration
     * 
     * @param document
     * @param mti
     * @param isoFieldDefs
     * 
     * @todo add the xnl structure here
     * 
     */
    private void createMessageXml(Element root, String mti, List<AtmIsoFieldCO> isoFieldDefs)
    {

	Document document = root.getOwnerDocument();

	// prepare parse element
	Element parseElement = document.createElement("parse");
	Attr attr = document.createAttribute("type");
	attr.setValue(mti);
	parseElement.setAttributeNode(attr);

	// prepare template element
	Element templateElement = document.createElement("template");
	Attr templateAttr = document.createAttribute("type");
	templateAttr.setValue(mti);
	templateElement.setAttributeNode(templateAttr);

	for(AtmIsoFieldCO fieldDef : isoFieldDefs)
	{

	    // skip the first field ( Bitmpa field )
	    if(fieldDef.getCode().equals(BigDecimal.ONE))
		continue;

	    Element field = createXmlMessageField(document, fieldDef);
	    Element fieldTemplate = (Element) field.cloneNode(true);

	    templateElement.appendChild(fieldTemplate);
	    parseElement.appendChild(field);

	}

	// append the parse and template element of this message to the root
	root.appendChild(parseElement);
	root.appendChild(templateElement);
    }

    /**
     * @param document
     * @param isoFieldDef
     * @return
     */
    private Element createXmlMessageField(Document document, AtmIsoFieldCO isoFieldDef)
    {
	// validate the field configuration
	validateField(isoFieldDef);

	// create field
	Element field = document.createElement("field");

	// check if composite field or not
	Attr attr = document.createAttribute("num");
	BigDecimal numVal = null != isoFieldDef.getIndex() ? isoFieldDef.getIndex() : isoFieldDef.getCode();

	attr.setValue(numVal.toString());
	field.setAttributeNode(attr);

	String typeVal = IsoUtil.returnISOTypes(isoFieldDef.getType(), isoFieldDef.getDynamicLength()).toString();

	attr = document.createAttribute("type");
	attr.setValue(typeVal);
	field.setAttributeNode(attr);

	/**
	 * Skip length Attribute for dynamic length
	 */
	if(null == isoFieldDef.getDynamicLength())
	{
	    attr = document.createAttribute("length");
	    attr.setValue(isoFieldDef.getFixedLength().toString());
	    field.setAttributeNode(attr);
	}

	// check if composite field
	List<AtmIsoFieldCO> subFields = isoFieldDef.getIsoFieldSubList();

	if(null != subFields && subFields.size() > 0)
	{
	    int subFieldIndex = 1;
	    for(AtmIsoFieldCO subField : subFields)
	    {
		// fix index since it's an incremental key , we should use
		// 1,2,3,4
		subField.setIndex(new BigDecimal(subFieldIndex));
		field.appendChild(createXmlMessageField(document, subField));
		subFieldIndex++;
	    }
	}

	return field;
    }

    /**
     * Validate the field configuration
     * 
     * @param isoFieldDef
     */
    private void validateField(AtmIsoFieldCO isoFieldDef)
    {
	/**
	 * Check if length or dynamic length is provided and it's positive
	 * 
	 * @todo check if we can use the number util is positive
	 */
	if(((null == isoFieldDef.getDynamicLength() || isoFieldDef.getDynamicLength().intValue() <= 0)
		&& (null == isoFieldDef.getFixedLength() || isoFieldDef.getFixedLength().intValue() <= 0))
		|| (null != isoFieldDef.getDynamicLength() && null != isoFieldDef.getFixedLength()))
	    throw new IllegalConfigurationException(EngineError.MSG_FACT_WRONG_FIELD_CONF,
		    String.format("Invalid length field %d", isoFieldDef.getCode().intValue()));

	/**
	 * Numeric field can only have fixed length
	 */
	if(isoFieldDef.getType().equalsIgnoreCase(AtmEngineConstants.ISO_FIELD_TYPE_NUMERIC)
		&& (null == isoFieldDef.getFixedLength() || isoFieldDef.getFixedLength().intValue() <= 0))
	    throw new IllegalConfigurationException(EngineError.MSG_FACT_WRONG_FIELD_CONF,
		    String.format("Numeric field ( %d ) require fixed length", isoFieldDef.getCode().intValue()));
    }

}
