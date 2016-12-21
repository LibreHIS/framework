package ims.framework.cn.events;

import java.io.Serializable;

public class TreeNodeExpanded implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TreeNodeExpanded(int controlID, String selection, boolean expanded)
	{
		this.controlID = controlID;
		this.selection = selection;
		this.expanded = expanded;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getSelection()
	{
		return this.selection;
	}
	public boolean isExpanded()
	{
		return this.expanded;
	}
	private int controlID;
	private String selection;
	private boolean expanded;
}
