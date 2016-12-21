package ims.framework.cn.events;

import java.io.Serializable;

public class TreeViewSelected implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TreeViewSelected(int controlID, String selection)
	{
		this.controlID = controlID;
		this.selection = selection;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getSelection()
	{
		return this.selection;
	}
	private int controlID;
	private String selection;
}
