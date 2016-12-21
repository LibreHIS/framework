package ims.framework.delegates;

import java.io.Serializable;

/**
 * @author mchashchin
 */
public interface FormClosing extends Serializable
{
	public void handle(CancelArgs args) throws ims.framework.exceptions.PresentationLogicException;
}
