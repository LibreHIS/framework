package ims.framework.cn.events;

import java.io.Serializable;

public class ComboBoxSearch implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public ComboBoxSearch(int controlID, String text, Integer selection)
	{
		this.controlID = controlID;
		this.text = text;
		this.selection = selection;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public String getText()
	{
		return this.text;
	}
	public Integer getSelection()
	{
		return this.selection;		
	}
	
	private int controlID;
	private String text;
	private Integer selection;
}
