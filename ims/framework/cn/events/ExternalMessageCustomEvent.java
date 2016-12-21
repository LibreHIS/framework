package ims.framework.cn.events;

import ims.framework.CustomEvent;

public class ExternalMessageCustomEvent extends CustomEvent
{
	private static final long serialVersionUID = 1L;

	public ExternalMessageCustomEvent(String title, String text)
	{
		this.title = title;
		this.text = text;
	}
	public String getTitle()
	{
		return this.title;
	}
	public String getText()
	{
		return text;
	}
	
	private String title;
	private String text;
}
