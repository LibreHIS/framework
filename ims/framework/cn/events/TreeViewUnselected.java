package ims.framework.cn.events;

import java.io.Serializable;

public class TreeViewUnselected implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TreeViewUnselected(int controlID)
	{
		this.controlID = controlID;		
	}
	public int getControlID()
	{
		return this.controlID;
	}
	private int controlID;	
}
