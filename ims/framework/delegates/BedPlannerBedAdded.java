package ims.framework.delegates;

import ims.framework.controls.Bed;

public interface BedPlannerBedAdded
{
	public void handle(Bed bed) throws ims.framework.exceptions.PresentationLogicException;
}
