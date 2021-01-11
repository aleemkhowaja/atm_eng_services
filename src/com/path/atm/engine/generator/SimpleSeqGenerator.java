package com.path.atm.engine.generator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This class house the sequence generator behaviors
 * 
 * @author MohammadAliMezzawi
 *
 */
public class SimpleSeqGenerator implements SequenceGenerator
{

    /**
     * Hold reference to the Sequence
     */
    protected AtomicLong sequence;

    /**
     * Constructor
     * 
     * @param maxValue
     */
    public SimpleSeqGenerator(long sequence)
    {
	this.sequence = new AtomicLong(sequence);
    }

    @Override
    public long nextId()
    {
	return sequence.getAndIncrement();
    }

    /**
     * 
     */
    public long getLastId()
    {
	return sequence.get();
    }

    /**
     * Returns a string representation of the object.
     */
    public String toString()
    {
	return "SimpleSeqGenerator";
    }
}
