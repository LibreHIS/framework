package ims.framework.delegates;

import java.io.Serializable;

public interface GridHeaderClicked extends Serializable
{
	public void handle(int column) throws ims.framework.exceptions.PresentationLogicException;
}
