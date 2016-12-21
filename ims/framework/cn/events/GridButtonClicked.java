package ims.framework.cn.events;

import java.io.Serializable;

public class GridButtonClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridButtonClicked(int controlID, int row, int column)
	{
		this.controlID = controlID;
		this.row = row;
		this.column = column;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getRow()
	{
		return this.row;
	}
	public int getColumn()
	{
		return this.column;
	}
	private int controlID;
	private int row;
	private int column;
}
