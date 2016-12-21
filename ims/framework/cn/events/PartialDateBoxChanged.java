package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.PartialDate;

public class PartialDateBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public PartialDateBoxChanged(int controlID, PartialDate value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public PartialDate getValue()
	{
		return this.value;
	}
	private int controlID;
	private PartialDate value;
}
