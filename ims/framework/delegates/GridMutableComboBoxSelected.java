package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.controls.GridRow;

public interface GridMutableComboBoxSelected extends Serializable
{
	public void handle(int column, GridRow row, Object value) throws ims.framework.exceptions.PresentationLogicException;
}
