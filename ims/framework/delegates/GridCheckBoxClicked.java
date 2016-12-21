package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.controls.GridRow;

public interface GridCheckBoxClicked extends Serializable
{
	public void handle(int column, GridRow row, boolean isChecked) throws ims.framework.exceptions.PresentationLogicException;
}
