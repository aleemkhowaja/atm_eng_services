/**
 * 
 */
package com.path.atm.engine.generator;

/**
 * @author MohammadAliMezzawi
 *
 */
public interface SequenceGenerator
{

    /**
     * Returns the next trace number.
     */
    public long nextId();

    /**
     * Returns the last number that was generated.
     */
    public long getLastId();
}
