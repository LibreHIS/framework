package ims.framework.cn.events;

import java.io.Serializable;

public class GridAnswerBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridAnswerBoxChanged(int controlID, int row, int column, int index)
	{
		this.controlID = controlID;
		this.row = row;
		this.column = column;
		this.index = index;
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
	public int getIndex()
	{
		return this.index;
	}
	private int controlID;
	private int row;
	private int column;
	private int index;	
}
