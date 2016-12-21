package ims.framework.cn.events;

import java.io.Serializable;

public class IntBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public IntBoxChanged(int controlID, Integer value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public Integer getValue()
	{
		return this.value;
	}
	private int controlID;
	private Integer value;
}
