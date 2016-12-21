package ims.framework.cn.events;

import java.io.Serializable;

public class GridIntBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridIntBoxChanged(int controlID, int row, int column, Integer value)
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
	public Integer getValue()
	{
		return this.value;
	}
	private int controlID;
	private int row;
	private int column;
	private Integer value;	
}