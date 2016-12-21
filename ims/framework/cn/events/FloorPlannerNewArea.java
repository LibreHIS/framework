package ims.framework.cn.events;

import java.io.Serializable;

public class FloorPlannerNewArea implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public FloorPlannerNewArea(int controlID, int area, String path)
	{
		this.controlID = controlID;
		this.area = area;
		this.path = path;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getAreaID()
	{
		return this.area;
	}
	public String getPath()
	{
		return this.path;
	}
	private int controlID;
	private int area;
	private String path;
}
