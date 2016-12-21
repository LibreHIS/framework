package ims.framework.delegates;

import java.io.Serializable;

public interface ComboBoxSearch extends Serializable
{
	public void handle(String value) throws ims.framework.exceptions.PresentationLogicException;
}
