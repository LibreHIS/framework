package ims.framework.cn.events;

import ims.framework.CustomEvent;

public class ExternalCustomEvent extends CustomEvent
{
	private static final long serialVersionUID = 1L;

	public ExternalCustomEvent(String name, String action, String value)
	{
		this.name = name;
		this.action = action;
		this.value = value;
	}
	public String getName()
	{
		return this.name;
	}
	public String getAction()
	{
		return action;
	}
	public String getValue()
	{
		return value;
	}
	
	private String name;
	private String action;
	private String value;
}
