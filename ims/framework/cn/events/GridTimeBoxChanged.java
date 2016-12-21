package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.Time;

public class GridTimeBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridTimeBoxChanged(int controlID, int row, int column, Time value)
	{
		this.controlID = controlID;
		this.row = row;
		this.column = column;
		this.value = value;
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
	public Time getValue()
	{
		return this.value;
	}
	private int controlID;
	private int row;
	private int column;
	private Time value;	
}
