package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.Time;

public class TimeControlChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TimeControlChanged(int controlID, Time value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public Time getValue()
	{
		return this.value;
	}
	private int controlID;
	private Time value;
}
