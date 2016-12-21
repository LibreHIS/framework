package ims.framework.cn.events;

import java.io.Serializable;

public class AvailabilityControlClicked implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public AvailabilityControlClicked(int controlID, int value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getValue()
	{
		return this.value;
	}
	private int controlID;
	private int value;
}
