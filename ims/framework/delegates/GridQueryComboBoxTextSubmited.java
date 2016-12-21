package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.controls.GridRow;

public interface GridQueryComboBoxTextSubmited extends Serializable
{
	public void handle(int column, GridRow row, String text) throws ims.framework.exceptions.PresentationLogicException;
}
