package ims.framework.cn.events;

import java.io.Serializable;

public class TopButtonClicked implements Serializable
{
	private static final long serialVersionUID = 1L;
	public TopButtonClicked(int id)
	{
		this.id = id;
	}	
	public int getID()
	{
		return this.id;
	}
	private int id;
}
