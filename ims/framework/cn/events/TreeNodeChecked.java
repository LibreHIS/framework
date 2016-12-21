package ims.framework.cn.events;

import java.io.Serializable;

public class TreeNodeChecked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TreeNodeChecked(int controlID, String selection, boolean checked)
	{
		this.controlID = controlID;
		this.selection = selection;
		this.checked = checked;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getSelection()
	{
		return this.selection;
	}
	public boolean isChecked()
	{
		return this.checked;
	}
	private int controlID;
	private String selection;
	private boolean checked;
}
