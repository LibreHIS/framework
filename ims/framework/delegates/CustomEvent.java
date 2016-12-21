package ims.framework.delegates;

import java.io.Serializable;

/**
 * @author mchashchin
 */
public interface CustomEvent extends Serializable
{
	public void handle(ims.framework.CustomEvent customEvent) throws ims.framework.exceptions.PresentationLogicException;
}
