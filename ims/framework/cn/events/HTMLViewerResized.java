package ims.framework.cn.events;

import java.io.Serializable;

public class HTMLViewerResized implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public HTMLViewerResized(int controlID, int width, int height)
	{
		this.controlID = controlID;
		this.width = width;
		this.height = height;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	private int controlID;
	private int width;
	private int height;
}
