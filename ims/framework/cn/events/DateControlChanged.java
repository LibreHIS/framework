package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.Date;

public class DateControlChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DateControlChanged(int controlID, Date value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public Date getValue()
	{
		return this.value;
	}
	private int controlID;
	private Date value;
}
