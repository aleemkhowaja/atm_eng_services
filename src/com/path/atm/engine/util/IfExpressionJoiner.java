package com.path.atm.engine.util;

/**
 * This class will join a list of expression(String) based on
 * a specific joiner.
 * 
 * @author MohammadAliMezzawi
 *
 */
public class IfExpressionJoiner
{
    /**
     * 
     */
    private StringBuilder value;
    
    /**
     * 
     */
    private StringBuilder suffix;
    
    /**
     * Default false value
     */
    private String defaultFalseValue;
    
    
    /**
     * @param falseValue
     */
    public IfExpressionJoiner(String falseValue) {
	defaultFalseValue = falseValue;
    }
    
    
    /**
     * @param msgSource
     * @param trueValue
     */
    public void append(String msgSource, String trueValue)
    {
	// TODO Auto-generated method stub
	// IF(expression,valuetrue,IF(expression,valuetrue,valuefalse));
	prepareBuilder().append("IF(").append(msgSource)
		.append(",").append(trueValue).append(",");
	
	prepareSuffixBuilder().append(")");
	
    }
    
    
    /** 
     * Return the full expression
     */
    public String toString() {
	return prepareBuilder().append(defaultFalseValue)
		.append(prepareSuffixBuilder().toString()).toString();
    }
    
    
    /**
     * @return
     */
    private StringBuilder prepareBuilder() {
        if (value == null)
            value = new StringBuilder();
        return value;
    }
    
    
    /**
     * @return
     */
    private StringBuilder prepareSuffixBuilder() {
        if (suffix == null)
            suffix = new StringBuilder();
        return suffix;
    }
}
