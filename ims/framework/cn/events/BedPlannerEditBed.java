package ims.framework.cn.events;

import java.io.Serializable;

public class BedPlannerEditBed implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public BedPlannerEditBed(int controlID, int bedID, int bedType, String path)
	{
		this.controlID = controlID;
		this.bedID = bedID;
		this.bedType = bedType;
		this.path = path;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getBedID()
	{
		return this.bedID;
	}
	public int getBedType()
	{
		return this.bedType;
	}
	public String getPath()
	{
		return this.path;
	}
	private int controlID;
	private int bedID;
	private int bedType;
	private String path;
}