package ims.framework.delegates;

import ims.framework.controls.DynamicGridRow;

public interface DynamicGridRowExpandedCollapsed
{
	public void handle(DynamicGridRow row) throws ims.framework.exceptions.PresentationLogicException;
}
