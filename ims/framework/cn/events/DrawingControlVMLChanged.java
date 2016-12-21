package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.controls.DrawingControlShape;

public class DrawingControlVMLChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DrawingControlVMLChanged(int controlID, DrawingControlShape shape)
	{
		this.controlID = controlID;
		this.shape = shape;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public DrawingControlShape getShape()
	{
		return this.shape;
	}
	private int controlID;
	private DrawingControlShape shape;
}
