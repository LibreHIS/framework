package ims.framework.delegates;

import java.io.Serializable;

public interface ValueChanged extends Serializable
{
	public void handle() throws ims.framework.exceptions.PresentationLogicException;
}
