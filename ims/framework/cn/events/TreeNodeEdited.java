package ims.framework.cn.events;

import java.io.Serializable;

public class TreeNodeEdited implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TreeNodeEdited(int controlID, String selection, String text)
	{
		this.controlID = controlID;
		this.selection = selection;
		this.text = text;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getSelection()
	{
		return this.selection;
	}
	public String getText()
	{
		return this.text;
	}
	private int controlID;
	private String selection;
	private String text;
}
