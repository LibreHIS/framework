package ims.framework.cn.events;

import java.io.Serializable;

public class TreeNodeDropped implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public TreeNodeDropped(int controlID, String selection, String newIndex)
	{
		this.controlID = controlID;
		this.selection = selection;
		this.newIndex = newIndex;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getSelection()
	{
		return this.selection;
	}
	public String getNewIndex()
	{
		return this.newIndex;
	}
	
	private int controlID;
	private String selection;
	private String newIndex;
}
