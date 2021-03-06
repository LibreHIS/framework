package ims.framework.cn.events;

import java.io.Serializable;

public class AnswerBoxChanged implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public AnswerBoxChanged(int controlID, int selection)
	{
		this.controlID = controlID;
		this.selection = selection;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getSelection()
	{
		return this.selection;
	}
	private int controlID;
	private int selection;
}
