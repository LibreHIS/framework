package ims.framework.cn.events;

import ims.framework.enumerations.MouseButton;

import java.io.Serializable;

public class GridSelected implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int controlID;
	private int row;
	private MouseButton mouseButton;
	
	public GridSelected(int controlID, int row, MouseButton mouseButton)
	{
		this.controlID = controlID;
		this.row = row;
		this.mouseButton = mouseButton;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getRow()
	{
		return this.row;
	}
	public MouseButton getMouseButton()
	{
		return mouseButton;
	}
}
