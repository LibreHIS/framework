package ims.framework;

public abstract class FormContext
{
	public FormContext(Context context)
	{
		this.context = context;
	}
	protected Context context = null;
}
