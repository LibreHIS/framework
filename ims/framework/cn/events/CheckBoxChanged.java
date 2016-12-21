package ims.framework.cn.events;

import java.io.Serializable;

public class CheckBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public CheckBoxChanged(int controlID, boolean value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public boolean getValue()
	{
		return this.value;
	}
	private int controlID;
	private boolean value;
}
