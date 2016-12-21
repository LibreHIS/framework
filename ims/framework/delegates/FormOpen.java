package ims.framework.delegates;

import java.io.Serializable;

/**
 * @author mchashchin
 */
public interface FormOpen extends Serializable
{
	public void handle(Object[] args) throws ims.framework.exceptions.PresentationLogicException;
}
