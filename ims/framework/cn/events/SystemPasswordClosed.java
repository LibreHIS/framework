package ims.framework.cn.events;

import java.io.Serializable;

public class SystemPasswordClosed implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private boolean canceled;

	public SystemPasswordClosed(boolean canceled)
	{
		this.canceled = canceled;		
	}
	public boolean wasCanceled()
	{
		return canceled;
	}	
}
