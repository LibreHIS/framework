package ims.framework.delegates;

import ims.framework.controls.DynamicGridRow;
import ims.framework.enumerations.MouseButton;

public interface DynamicGridRowSelectionChanged
{
	public void handle(DynamicGridRow row, MouseButton mouseButton) throws ims.framework.exceptions.PresentationLogicException;
}
