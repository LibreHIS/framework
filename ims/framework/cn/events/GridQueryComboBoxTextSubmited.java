package ims.framework.cn.events;

import java.io.Serializable;

public class GridQueryComboBoxTextSubmited implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridQueryComboBoxTextSubmited(int controlID, int row, int column, String text)
	{
		this.controlID = controlID;
		this.row = row;
		this.column = column;
		this.text = text;
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
	public String getText()
	{
		return this.text;
	}
	private int controlID;
	private int row;
	private int column;
	private String text;	
}
