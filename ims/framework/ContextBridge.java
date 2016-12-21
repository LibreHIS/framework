package ims.framework;

import java.io.Serializable;

public class ContextBridge implements Serializable
{
	private static final long serialVersionUID = 1L;
	protected void setContext(Context context)
	{
		this.context = context;
	}
	void free()
	{
		this.context = null;
	}
	protected Context context = null;
}
