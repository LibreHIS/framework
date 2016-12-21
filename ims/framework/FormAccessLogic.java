package ims.framework;

import ims.framework.interfaces.IFormAccessValidator;

public abstract class FormAccessLogic implements IFormAccessValidator
{
	public abstract void setContext(Context context, FormName formName);
}
