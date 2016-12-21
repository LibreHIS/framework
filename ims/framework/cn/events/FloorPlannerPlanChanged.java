package ims.framework.cn.events;

import java.io.Serializable;

public class FloorPlannerPlanChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public FloorPlannerPlanChanged(int controlID, String shape)
	{
		this.controlID = controlID;
		this.shape = shape;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getShape()
	{
		return this.shape;
	}
	private int controlID;
	private String shape;
}
