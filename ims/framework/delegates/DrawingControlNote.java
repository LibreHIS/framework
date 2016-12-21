package ims.framework.delegates;

import ims.framework.controls.DrawingControlShape;

public interface DrawingControlNote
{
	public void handle(DrawingControlShape shape) throws ims.framework.exceptions.PresentationLogicException;
}
