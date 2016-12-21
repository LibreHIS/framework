package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.controls.GridRow;

public interface GridButtonClicked extends Serializable
{
	public void handle(int column, GridRow row) throws ims.framework.exceptions.PresentationLogicException;
}
