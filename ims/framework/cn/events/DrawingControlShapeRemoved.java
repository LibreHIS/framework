package ims.framework.cn.events;

import java.io.Serializable;

public class DrawingControlShapeRemoved implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DrawingControlShapeRemoved(int controlID, int index)
	{
		this.controlID = controlID;
		this.index = index;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getIndex()
	{
		return this.index;
	}
	private int controlID;
	private int index;
}
