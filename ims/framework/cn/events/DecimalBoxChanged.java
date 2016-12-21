package ims.framework.cn.events;

import java.io.Serializable;

public class DecimalBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DecimalBoxChanged(int controlID, Float value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public Float getValue()
	{
		return this.value;
	}
	private int controlID;
	private Float value;
}
