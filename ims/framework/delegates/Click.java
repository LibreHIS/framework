package ims.framework.delegates;

import java.io.Serializable;

/**
 * @author mchashchin
 */
public interface Click extends Serializable
{
	public void handle() throws ims.framework.exceptions.PresentationLogicException;
}
