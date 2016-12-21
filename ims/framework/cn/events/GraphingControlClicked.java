package ims.framework.cn.events;

import java.io.Serializable;

public class GraphingControlClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GraphingControlClicked(int controlID, int pointID)
	{
		this.controlID = controlID;
		this.pointID = pointID;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getPointID()
	{
		return this.pointID;
	}
	private int controlID;
	private int pointID;
}
