package com.path.atm.engine.action;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.path.atm.engine.util.AtmLogger;

/**
 * @author MohammadAliMezzawi
 *
 * @param <T>
 */
public class AtmBaseAction<T>
{

    /**
     * Hold Action additional message
     */
    private String additionalMsg;

    /**
     * Hold Action starting date
     */
    private Date startingDate;

    /**
     * Hold Action end date
     */
    private Date endingDate;

    /**
     * Hold Action key
     */
    private AtmActionType actionType;

    /**
     * Hold the object reference
     */
    private T reference;

    /**
     * @param action
     * @param reference
     * @return
     */
    public static <S> AtmBaseAction<S> getInstance(AtmActionType action)
    {
	return getInstance(action, null);
    }

    /**
     * @param action
     * @param reference
     * @return
     */
    public static <S> AtmBaseAction<S> getInstance(AtmActionType action, S reference)
    {

	// create atm Action instance and set Starting date
	AtmBaseAction<S> atmAction = new AtmBaseAction<S>(action, reference);
	atmAction.setStartingDate(new Date());

	return atmAction;
    }

    /**
     * @param action
     * @param reference
     */
    public AtmBaseAction(AtmActionType action, T reference)
    {
	this.reference = reference;
	this.actionType = action;
    }

    /**
     * Log this action
     */
    public void log()
    {

	this.setEndingDate(new Date());
	AtmLogger.getInstance().logAction(this);
    }

    /**
     * Returns a string representation of the object.
     * 
     * <p>
     * As default behavior the to String will return the string presentation of
     * the reference object
     */
    public String toString()
    {

	if(null == reference)
	    return StringUtils.EMPTY;

	return reference.toString();
    }

    /**
     * @return the startingDate
     */
    public Date getStartingDate()
    {
	return startingDate;
    }

    /**
     * @param startingDate the startingDate to set
     */
    public AtmBaseAction<T> setStartingDate(Date startingDate)
    {
	this.startingDate = startingDate;
	return this;
    }

    /**
     * @return the endingDate
     */
    public Date getEndingDate()
    {
	return endingDate;
    }

    /**
     * @param endingDate the endingDate to set
     */
    public AtmBaseAction<T> setEndingDate(Date endingDate)
    {
	this.endingDate = endingDate;
	return this;
    }

    /**
     * @return the reference
     */
    public T getReference()
    {
	return reference;
    }

    /**
     * @param reference the reference to set
     * @return
     */
    public AtmBaseAction<T> setReference(T reference)
    {
	this.reference = reference;
	return this;
    }

    /**
     * @return the actionType
     */
    public AtmActionType getActionType()
    {
	return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public AtmBaseAction<T> setActionType(AtmActionType actionType)
    {
	this.actionType = actionType;
	return this;
    }

    /**
     * @return the additionalMsg
     */
    public String getAdditionalMsg()
    {
	return additionalMsg;
    }

    /**
     * @param additionalMsg the additionalMsg to set
     */
    public AtmBaseAction<T> setAdditionalMsg(String additionalMsg)
    {
	this.additionalMsg = additionalMsg;
	return this;
    }

}
