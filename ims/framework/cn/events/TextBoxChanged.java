package ims.framework.cn.events;

import java.io.Serializable;

public class TextBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TextBoxChanged(int controlID, String value, String selectedText)
	{
		this.controlID = controlID;
		this.value = value;
		this.selectedText = selectedText;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getValue()
	{
		return this.value;
	}
	public String getSelectedText()
	{
		return this.selectedText;
	}
	private int controlID;
	private String value;
	private String selectedText;
}
