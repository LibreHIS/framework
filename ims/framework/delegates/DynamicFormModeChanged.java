package ims.framework.delegates;

import ims.framework.enumerations.FormMode;

import java.io.Serializable;

public interface DynamicFormModeChanged extends Serializable
{
	public void handle(FormMode formMode);
}
