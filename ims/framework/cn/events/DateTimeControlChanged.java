package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.DateTime;

public class DateTimeControlChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DateTimeControlChanged(int controlID, DateTime value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public DateTime getValue()
	{
		return this.value;
	}
	private int controlID;
	private DateTime value;
}
