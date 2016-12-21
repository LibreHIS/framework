package ims.framework.delegates;

import ims.framework.controls.DynamicGridColumn;

public interface DynamicGridColumnHeaderClicked
{
	public void handle(DynamicGridColumn column) throws ims.framework.exceptions.PresentationLogicException;
}
