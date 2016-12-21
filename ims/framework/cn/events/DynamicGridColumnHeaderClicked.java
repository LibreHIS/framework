package ims.framework.cn.events;

import java.io.Serializable;

public class DynamicGridColumnHeaderClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DynamicGridColumnHeaderClicked(int controlID, int column)
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
