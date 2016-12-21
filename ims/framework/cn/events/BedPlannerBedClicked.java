package ims.framework.cn.events;

import java.io.Serializable;

public class BedPlannerBedClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public BedPlannerBedClicked(int controlID, int bedID, boolean readOnly)
	{
		this.controlID = controlID;
		this.bedID = bedID;	
		this.readOnly = readOnly;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getBedID()
	{
		return this.bedID;
	}
	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	private int controlID;
	private int bedID;	
	private boolean readOnly = true;
}