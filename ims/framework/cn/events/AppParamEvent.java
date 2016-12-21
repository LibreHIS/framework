package ims.framework.cn.events;

import ims.framework.interfaces.IAppParam;

import java.io.Serializable;

public class AppParamEvent implements Serializable
{
	private static final long serialVersionUID = 1L;

	public AppParamEvent(IAppParam[] params)
	{
		this.params = params;
	}
	public IAppParam[] getAppParams()
	{
		return this.params;
	}	
	
	private IAppParam[] params;
}
