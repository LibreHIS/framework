package ims.framework.delegates;

import ims.framework.utils.Date;

public interface BookingCalendarDateChecked
{
	public void handle(Date date, boolean value) throws ims.framework.exceptions.PresentationLogicException;
}
