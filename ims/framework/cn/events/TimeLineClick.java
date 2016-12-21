package ims.framework.cn.events;

import java.io.Serializable;

import ims.framework.utils.Date;

public class TimeLineClick implements IControlEvent, Serializable
{
	private static final long serialVersionUID = 1L;
	public TimeLineClick(int controlID, int eventID, Date date)
	{
		this.controlID = controlID;
		this.eventID = eventID;
		this.date = date;
	}
	public int getControlID()
	{
		return this.controlID;
	}
	public int getEventID()
	{
		return this.eventID;
	}
	public Date getDate()
	{
		return this.date;
	}
	private int controlID;
	private int eventID;
	private Date date;
}
