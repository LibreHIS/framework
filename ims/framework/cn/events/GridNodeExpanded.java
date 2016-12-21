package ims.framework.cn.events;

import java.io.Serializable;

public class GridNodeExpanded implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridNodeExpanded(int controlID, int row, boolean expanded)
	{
		this.controlID = controlID;
		this.row = row;
		this.expanded = expanded;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getRow()
	{
		return this.row;
	}
	public boolean isExpanded()
	{
		return this.expanded;
	}
	private int controlID;
	private int row;
	private boolean expanded;	
}