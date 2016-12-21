package ims.framework.cn.events;

import java.io.Serializable;

public class CheckedListBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public CheckedListBoxChanged(int controlID, int selectionIndex)
	{
		this(controlID, selectionIndex, null);		
	}
	public CheckedListBoxChanged(int controlID, int selectionIndex, CheckedListBoxItemChanged[] items)
	{
		this.controlID = controlID;
		this.selectionIndex = selectionIndex;
		this.items = items;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getSelectionIndex()
	{
		return this.selectionIndex;
	}
	public CheckedListBoxItemChanged[] getItems()
	{
		return this.items;
	}
	private int controlID;
	private int selectionIndex;
	private CheckedListBoxItemChanged[] items;
}
