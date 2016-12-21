package ims.framework.delegates;

import java.io.Serializable;

import ims.framework.controls.AnswerBoxOption;
import ims.framework.controls.GridRow;

public interface GridMutableAnswerBoxSelected extends Serializable
{
	public void handle(int column, GridRow row, AnswerBoxOption option) throws ims.framework.exceptions.PresentationLogicException;
}
