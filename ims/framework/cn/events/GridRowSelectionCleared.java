package ims.framework.cn.events;

import java.io.Serializable;

public class GridRowSelectionCleared implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public GridRowSelectionCleared(int controlID)
	{
		this.controlID = controlID;		
	}
	public int getControlID()
	{
		return this.controlID;
	}
	private int controlID;	
}
