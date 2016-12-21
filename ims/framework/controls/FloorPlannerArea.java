package ims.framework.controls;

import java.io.Serializable;

public class FloorPlannerArea implements Serializable
{
	private static final long serialVersionUID = 1L;
	public FloorPlannerArea(int index, String areaName, String path)
	{
		this.index = index;
		this.areaName = areaName;
		this.path = path;
	}
	public int getIndex()
	{
		return this.index;
	}
	public String getAreaName()
	{
		return this.areaName;
	}
	public String getPath()
	{
		return this.path;
	}
	private int index;
	private String areaName;
	private String path;
}
