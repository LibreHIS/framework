package ims.framework.cn.events;

import java.io.Serializable;

public class LogoutEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	public LogoutEvent(boolean isIE)
	{
		this.isIE = isIE;
	}
	
	public boolean isIE()
	{
		return this.isIE;
	}
	
	private boolean isIE;
}
