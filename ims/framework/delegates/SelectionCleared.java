package ims.framework.delegates;

import java.io.Serializable;

public interface SelectionCleared extends Serializable
{
	public void handle() throws ims.framework.exceptions.PresentationLogicException;
}
