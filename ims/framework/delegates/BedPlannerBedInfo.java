package ims.framework.delegates;

import ims.framework.controls.Bed;

public interface BedPlannerBedInfo
{
	public void handle(Bed bed, boolean readOnly) throws ims.framework.exceptions.PresentationLogicException;
}
