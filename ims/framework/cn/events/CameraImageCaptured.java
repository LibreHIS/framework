package ims.framework.cn.events;

import java.io.Serializable;

public class CameraImageCaptured implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public CameraImageCaptured(int controlID, String imageString)
	{
		this.controlID = controlID;
		this.imageString = imageString;			
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getImageString()
	{
		return this.imageString;
	}
	
	private int controlID;
	private String imageString;		
}