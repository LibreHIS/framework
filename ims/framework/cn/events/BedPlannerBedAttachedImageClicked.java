package ims.framework.cn.events;

import java.io.Serializable;

public class BedPlannerBedAttachedImageClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public BedPlannerBedAttachedImageClicked(int controlID, int bedID, int imageID)
	{
		this.controlID = controlID;
		this.bedID = bedID;	
		this.imageID = imageID;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getBedID()
	{
		return this.bedID;
	}
	public int getImageID()
	{
		return imageID;
	}
	
	private int controlID;
	private int bedID;	
	private int imageID;
}