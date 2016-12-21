package ims.framework.controls;

import ims.framework.Control;
import ims.framework.delegates.AvailabilityControlClick;
import ims.framework.exceptions.FrameworkInternalException;
import ims.framework.utils.Date;
import ims.framework.utils.Time;

abstract public class AvailabilityControl extends Control
{
	private static final long serialVersionUID = 1L;
	
	abstract public void setStartDate(Date date);
	abstract public void setEndDate(Date date);

	abstract public void addExcludedDate(Date date) throws FrameworkInternalException;
	abstract public void addBookedTime(Date date, Time startTime, Time endTime)  throws FrameworkInternalException;
	abstract public void addUnavailableTime(Date date, Time startTime, Time endTime)  throws FrameworkInternalException;
	abstract public void addTooltip(Date date, String tooltip);
	abstract public void clear();
	
	public void setAvailabilityControlClickEvent(AvailabilityControlClick delegate)
	{
		this.delegate = delegate;        
	}		
	protected void free()
	{
		super.free();
		
		this.delegate = null;
	}
	protected AvailabilityControlClick delegate = null;
}
