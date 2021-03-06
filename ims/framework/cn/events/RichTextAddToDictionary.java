package ims.framework.cn.events;

import java.io.Serializable;

public class RichTextAddToDictionary implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public RichTextAddToDictionary(int controlID, String value)
	{
		this.controlID = controlID;
		this.value = value;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getValue()
	{
		return this.value;
	}
	private int controlID;
	private String value;
}
