package ims.framework.cn.events;

import java.io.Serializable;

public class AlertEvent implements Serializable
{
	private static final long serialVersionUID = 1L;

	public AlertEvent(int id, boolean fromDialog)
	{
		this.id = id;
		this.fromDialog = fromDialog;
	}
	public int getID()
	{
		return this.id;
	}
	public boolean isFromDialog()
	{
		return fromDialog;
	}
	
	private int id;
	private boolean fromDialog = false;
}
