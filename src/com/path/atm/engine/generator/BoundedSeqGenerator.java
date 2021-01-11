package com.path.atm.engine.generator;

/**
 * This class house the sequence generator behaviors
 * 
 * @author MohammadAliMezzawi
 *
 */
public class BoundedSeqGenerator extends SimpleSeqGenerator
{

    /**
     * Hold the maximum value
     */
    private long maxValue;

    /**
     * Constructor
     * 
     * @param maxValue
     */
    public BoundedSeqGenerator(long sequence, long maxValue)
    {
	super(sequence);
	this.maxValue = maxValue;
    }

    @Override
    public long nextId()
    {
	return sequence.updateAndGet(value -> value >= maxValue ? 1 : value + 1);
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString()
    {
	return "BoundedSeqGenerator";
    }

    /**
     * @return the maxValue
     */
    public long getMaxValue()
    {
	return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(long maxValue)
    {
	this.maxValue = maxValue;
    }

}
