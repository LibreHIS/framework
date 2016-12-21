package ims.framework.cn.events;

import java.io.Serializable;

public class RangeBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public RangeBoxChanged(int controlID, String minValue, String maxValue)
	{
		this.controlID = controlID;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getMin()
	{
		return this.minValue;
	}
	public String getMax()
	{
		return this.maxValue;
	}
	private int controlID;
	private String minValue;
	private String maxValue;
}
