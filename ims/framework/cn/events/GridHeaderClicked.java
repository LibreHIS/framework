package ims.framework.cn.events;

import java.io.Serializable;

public class GridHeaderClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridHeaderClicked(int controlID, int column)
	{
		this.controlID = controlID;
		this.column = column;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getColumn()
	{
		return this.column;
	}
	private int controlID;
	private int column;
}
