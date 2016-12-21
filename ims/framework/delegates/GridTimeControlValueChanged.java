package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.controls.GridRow;
import ims.framework.utils.Time;

public interface GridTimeControlValueChanged extends Serializable
{
	public void handle(int column, GridRow row, Time value) throws ims.framework.exceptions.PresentationLogicException;
}
