package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.PartialDate;

public class GridPartialDateControlChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridPartialDateControlChanged(int controlID, int row, int column, PartialDate date)
	{
		this.controlID = controlID;
		this.row = row;
		this.column = column;
		this.date = date;
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
	public PartialDate getValue()
	{
		return this.date;
	}
	private int controlID;
	private int row;
	private int column;
	private PartialDate date;	
}
