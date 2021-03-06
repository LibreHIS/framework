package ims.framework.cn.events;

import java.io.Serializable;

public class FloorPlannerNote implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public FloorPlannerNote(int controlID, int area)
	{
		this.controlID = controlID;
		this.area = area;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getAreaID()
	{
		return this.area;
	}
	private int controlID;
	private int area;
}
