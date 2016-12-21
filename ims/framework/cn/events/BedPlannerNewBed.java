package ims.framework.cn.events;

import ims.framework.enumerations.Position;

import java.io.Serializable;

public class BedPlannerNewBed implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int controlID;
	private int bedID;
	private int bedType;
	private String path;
	private Position textPosition;
	private Position imagePosition;
	
	public BedPlannerNewBed(int controlID, int bedID, int bedType, String path, Position textPosition, Position imagePosition)
	{
		this.controlID = controlID;
		this.bedID = bedID;
		this.bedType = bedType;
		this.path = path;
		this.textPosition = textPosition;
		this.imagePosition = imagePosition;
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
	public Position getTextPosition()	
	{
		return textPosition;
	}
	public Position getImagePosition()	
	{
		return imagePosition;
	}
}