package ims.framework.delegates;

import ims.framework.utils.Date;

public interface BookingCalendarMonthSelected
{
	public void handle(Date value) throws ims.framework.exceptions.PresentationLogicException;
}
