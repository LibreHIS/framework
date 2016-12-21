package ims.framework;

import ims.framework.interfaces.IAppForm;
import ims.framework.interfaces.IFormAccessValidator;

public abstract class FormAccessLoader
{
	public abstract IFormAccessValidator getFormAccessValidator(int value) throws Exception;
	public abstract IFormAccessValidator getFormAccessValidator(IAppForm form) throws Exception;

}