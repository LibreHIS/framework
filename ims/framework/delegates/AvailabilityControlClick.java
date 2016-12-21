package ims.framework.delegates;

import ims.framework.utils.Time;

public interface AvailabilityControlClick
{
	public void handle(Time value) throws ims.framework.exceptions.PresentationLogicException;
}
