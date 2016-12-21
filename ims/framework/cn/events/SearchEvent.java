package ims.framework.cn.events;

import java.io.Serializable;

public class SearchEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public SearchEvent(boolean lightweight, String text)
	{
		this.lightweight = lightweight;
		this.text = text;
	}
	public boolean isLightWeight()
	{
		return this.lightweight;
	}
	public String getText()
	{
		return this.text;
	}
	private boolean lightweight;
	private String text;	
}
