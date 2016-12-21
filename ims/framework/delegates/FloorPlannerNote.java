package ims.framework.delegates;

import ims.framework.controls.FloorPlannerArea;

public interface FloorPlannerNote
{
	public void handle(FloorPlannerArea area) throws ims.framework.exceptions.PresentationLogicException;
}
