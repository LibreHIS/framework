package ims.framework.cn.events;

import java.io.Serializable;

public class BedPlannerRemoveBed implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public BedPlannerRemoveBed(int controlID, int bedID)
	{
		this.controlID = controlID;
		this.bedID = bedID;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getBedID()
	{
		return this.bedID;
	}
	private int controlID;
	private int bedID;
}