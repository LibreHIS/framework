package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.Date;

public class GridDateControlChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridDateControlChanged(int controlID, int row, int column, Date date)
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
	public Date getValue()
	{
		return this.date;
	}
	private int controlID;
	private int row;
	private int column;
	private Date date;	
}
