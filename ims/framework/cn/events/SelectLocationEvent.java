package ims.framework.cn.events;

import java.io.Serializable;

public class SelectLocationEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	public SelectLocationEvent(int location)
	{
		this.location = location;
	}
	public int getLocation()
	{
		return this.location;
	}
	private int location;
}
