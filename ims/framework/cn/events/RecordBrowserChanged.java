package ims.framework.cn.events;

import java.io.Serializable;

public class RecordBrowserChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public RecordBrowserChanged(int controlID, int selectedID)
	{
		this.controlID = controlID;
		this.selectedID = selectedID;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getSelectedID()
	{
		return this.selectedID;
	}
	private int controlID;
	private int selectedID;
}
