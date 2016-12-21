package ims.framework.utils;

import ims.framework.interfaces.IAppParam;

public class AppParam implements IAppParam
{
	public AppParam(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	public String getName()
	{
		return name;
	}

	public String getValue()
	{
		return value;
	}
	
	private String name;
	private String value;		
}