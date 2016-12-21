package ims.framework.delegates;

import ims.framework.controls.DynamicGridCell;

public interface DynamicGridCellValueChanged
{
	public void handle(DynamicGridCell cell) throws ims.framework.exceptions.PresentationLogicException;
}
