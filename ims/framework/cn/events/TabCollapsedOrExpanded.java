package ims.framework.cn.events;

import java.io.Serializable;

public class TabCollapsedOrExpanded implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TabCollapsedOrExpanded(int controlID, boolean collapsed)
	{
		this.controlID = controlID;
		this.collapsed = collapsed;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public boolean isCollapsed()
	{
		return collapsed;
	}
	private int controlID;	
	private boolean collapsed;
}
