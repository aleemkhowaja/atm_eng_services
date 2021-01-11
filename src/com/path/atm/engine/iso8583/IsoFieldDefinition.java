package com.path.atm.engine.iso8583;

import com.path.lib.vo.BaseVO;
import com.solab.iso8583.IsoType;

/**
 * This class represent an ISO field definition
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IsoFieldDefinition extends BaseVO
{

    /**
     * Field index
     */
    private int index;

    /**
     * Field name
     */
    private String name;

    /**
     * Field Description
     */
    private String description;

    /**
     * Field type
     */
    private IsoType type;

    /**
     * @return the index
     */
    public int getIndex()
    {
	return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index)
    {
	this.index = index;
    }

    /**
     * @return the name
     */
    public String getName()
    {
	return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
	this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
	return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
	this.description = description;
    }

    /**
     * @return the type
     */
    public IsoType getType()
    {
	return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(IsoType type)
    {
	this.type = type;
    }
}
