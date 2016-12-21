package ims.framework.cn.events;

import ims.framework.cn.controls.DiaryButtonCommand;

import java.io.Serializable;

public class DiaryButtonPressed implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public DiaryButtonPressed(int controlID, DiaryButtonCommand command)
	{
		this.controlID = controlID;
		this.command = command;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public DiaryButtonCommand getCommand()
	{
		return this.command;
	}
	private int controlID;
	private DiaryButtonCommand command;
}
