package ims.framework.delegates;

import ims.framework.controls.DynamicGridCell;

public interface DynamicGridCellTextSubmited
{
	public void handle(DynamicGridCell cell) throws ims.framework.exceptions.PresentationLogicException;
}
