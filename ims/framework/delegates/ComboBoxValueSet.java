package ims.framework.delegates;

import java.io.Serializable;

public interface ComboBoxValueSet extends Serializable
{
	public void handle(Object value); //throws ims.framework.exceptions.PresentationLogicException;
}
