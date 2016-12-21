package ims.framework.delegates;

import ims.framework.FormName;

public interface EngineFormOpenFailed
{
	public void handle(FormName form, Exception exception) throws ims.framework.exceptions.PresentationLogicException;
}
