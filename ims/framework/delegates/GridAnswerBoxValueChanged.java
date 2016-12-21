package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.controls.GridRow;

public interface GridAnswerBoxValueChanged extends Serializable
{
	public void handle(int column, GridRow row, int index) throws ims.framework.exceptions.PresentationLogicException;
}
