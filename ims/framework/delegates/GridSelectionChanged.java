package ims.framework.delegates;

import ims.framework.enumerations.MouseButton;

import java.io.Serializable;

public interface GridSelectionChanged extends Serializable
{
	public void handle(MouseButton mouseButton) throws ims.framework.exceptions.PresentationLogicException;
}
