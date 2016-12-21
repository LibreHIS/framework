package ims.framework.delegates;

import java.io.Serializable;

/**
 * @author mmihalec
 */
public interface RIEDialogOpened extends Serializable
{
	public void handle() throws ims.framework.exceptions.PresentationLogicException;
}
